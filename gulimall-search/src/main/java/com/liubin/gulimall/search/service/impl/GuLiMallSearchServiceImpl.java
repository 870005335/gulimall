package com.liubin.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liubin.common.dto.es.SkuEsModel;
import com.liubin.common.utils.R;
import com.liubin.gulimall.search.config.ElasticSearchConfig;
import com.liubin.gulimall.search.constant.EsConstant;
import com.liubin.gulimall.search.feign.ProductFeignService;
import com.liubin.gulimall.search.service.GuLiMallSearchService;
import com.liubin.gulimall.search.vo.AttrRespVo;
import com.liubin.gulimall.search.vo.SearchParam;
import com.liubin.gulimall.search.vo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author liubin
 * @Date 2021/4/21 22:50
 * @Version 1.0
 */
@Service
public class GuLiMallSearchServiceImpl implements GuLiMallSearchService {

    @Autowired
    private RestHighLevelClient searchClient;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        // ??????????????????
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // ??????????????????
            SearchResponse searchResponse = searchClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // ?????????????????????????????????????????????
            result = buildSearchResult(searchResponse, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @Author liubin
     * @Description ????????????????????????
     * @Date 14:52 2021/5/8
     * @param param
     * @return org.elasticsearch.action.search.SearchRequest
     **/
    private SearchRequest buildSearchRequest(SearchParam param) {
        // ??????DSL????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         *  ??????????????????????????????????????????????????????????????????????????????????????????
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            // ????????????
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // bool - filter?????? - ????????????Id
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // bool - filter?????? - ??????Id
        if (!CollectionUtils.isEmpty(param.getBrandId())) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // bool - filter?????? - ?????????????????????????????????
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            for (String attrStr : param.getAttrs()) {
                BoolQueryBuilder attrBoolQuery = QueryBuilders.boolQuery();
                // attrs=1_5???:8???&attrs=2_16G:8G
                String[] attrArr = attrStr.split("_");
                // ?????????attrId
                String attrId = attrArr[0];
                // ?????????attrValue
                String[] attrValues = attrArr[1].split(":");
                attrBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                attrBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", attrBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        // bool - filter?????? - ????????????????????????
        if (param.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        // bool - filter?????? - ????????????????????????
        if (StringUtils.isNotBlank(param.getSkuPrice())) {
            String[] skuPriceArr = param.getSkuPrice().split("_");
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            if (skuPriceArr.length == 2) {
                rangeQuery.gte(skuPriceArr[0]).lte(skuPriceArr[1]);
            } else {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(skuPriceArr[0]);
                } else {
                    rangeQuery.gte(skuPriceArr[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }
        sourceBuilder.query(boolQuery);
        // ??????
        if (StringUtils.isNotBlank(param.getSort())) {
            // sort=hotScore_asc
            String[] sortArr = param.getSort().split("_");
            sourceBuilder.sort(sortArr[0], SortOrder.fromString(sortArr[1]));
        }
        // ??????
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE.intValue());
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE.intValue());
        // ????????????
        if (StringUtils.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style=color:red>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        //*  ????????????
        // ????????????
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        // ????????????????????????
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName"));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg"));
        sourceBuilder.aggregation(brandAgg);
        // ????????????
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg");
        catalogAgg.field("catalogId").size(20);
        // ????????????????????????
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName"));
        sourceBuilder.aggregation(catalogAgg);
        // ????????????
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // ????????????attrId?????????attrName
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName"));
        // ????????????attrId?????????attrValue
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(attrAgg);
        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
    }


    private SearchResult buildSearchResult(SearchResponse response, SearchParam param) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
        // 1????????????????????????????????????
        List<SkuEsModel> esModelList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(Arrays.asList(hits.getHits()))) {
            for (SearchHit hit : hits.getHits()) {
                SkuEsModel esModel = JSON.parseObject(hit.getSourceAsString(), SkuEsModel.class);
                if (StringUtils.isNotBlank(param.getKeyword())) {
                    String skuTitle = hit.getHighlightFields().get("skuTitle").getFragments()[0].string();
                    esModel.setSkuTitle(skuTitle);
                }
                esModelList.add(esModel);
            }
        }
        result.setProducts(esModelList);
        // 2????????????????????????????????????????????????
        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        List<SearchResult.Attr> attrs = new ArrayList<>();
        for (Terms.Bucket attrIdBucket : attrIdAgg.getBuckets()) {
            SearchResult.Attr attr = new SearchResult.Attr();
            // ??????Id
            Number attrId = attrIdBucket.getKeyAsNumber();
            attr.setAttrId(attrId.longValue());
            // ????????????
            ParsedStringTerms attrNameAgg = attrIdBucket.getAggregations().get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attr.setAttrName(attrName);
            // ???????????????
            List<String> attrValues = new ArrayList<>();
            ParsedStringTerms attrValueAgg = attrIdBucket.getAggregations().get("attr_value_agg");
            for (Terms.Bucket attrValueBucket : attrValueAgg.getBuckets()) {
                String attrValue = attrValueBucket.getKeyAsString();
                attrValues.add(attrValue);
            }
            attr.setAttrValue(attrValues);
            attrs.add(attr);
        }
        result.setAttrs(attrs);
        // 3????????????????????????????????????????????????
        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        List<SearchResult.Brand> brands = new ArrayList<>();
        for (Terms.Bucket brandBucket : brandAgg.getBuckets()) {
            SearchResult.Brand brand = new SearchResult.Brand();
            Number brandId = brandBucket.getKeyAsNumber();
            // ??????Id
            brand.setBrandId(brandId.longValue());
            // ????????????
            ParsedStringTerms brandNameAgg = brandBucket.getAggregations().get("brand_name_agg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brand.setBrandName(brandName);
            // ????????????
            ParsedStringTerms brandImgAgg = brandBucket.getAggregations().get("brand_img_agg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brand.setBrandImg(brandImg);
            brands.add(brand);
        }
        result.setBrands(brands);
        // 4????????????????????????????????????????????????
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        List<SearchResult.category> categories = new ArrayList<>();
        for (Terms.Bucket catalogBucket : catalogAgg.getBuckets()) {
            SearchResult.category category = new SearchResult.category();
            // ??????Id
            Number catalogId = catalogBucket.getKeyAsNumber();
            category.setCatalogId(catalogId.longValue());
            // ????????????
            ParsedStringTerms catalogNameAgg = catalogBucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            category.setCatalogName(catalogName);
            categories.add(category);
        }
        result.setCatalogs(categories);
        // 5???????????????-??????
        result.setPageNum(param.getPageNum());
        // 6???????????????-????????????
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        // 7???????????????-?????????
        long page = total / EsConstant.PRODUCT_PAGE_SIZE;
        int totalPages = (int) (total % EsConstant.PRODUCT_PAGE_SIZE == 0 ? page : page + 1);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages ; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
        result.setTotalPages(totalPages);
        result.setNavs(buildBreadNavs(param));
        return result;
    }

    private List<SearchResult.NavVo> buildBreadNavs(SearchParam param) {
        List<SearchResult.NavVo> navs = new ArrayList<>();
        // TODO ?????? ??????
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            // ???????????????????????????
            navs.addAll(param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] attrArr = attr.split("_");
                navVo.setNavValue(attrArr[1]);
                R info = productFeignService.info(Long.parseLong(attrArr[0]));
                if (info.getCode() == 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        AttrRespVo attrResp = objectMapper.readValue(JSON.toJSONString(info.get("attr")), new TypeReference<AttrRespVo>() {
                        });
                        navVo.setNavName(attrResp.getAttrName());
                    } catch (JsonProcessingException e) {
                        navVo.setNavName("");
                        e.printStackTrace();
                    }
                } else {
                    navVo.setNavName(attrArr[0]);
                }
                // ??????
                String encode = null;
                try {
                    encode = URLEncoder.encode(attr, "UTF-8");
                    // ??????????????????
                    encode = encode.replace("+", "%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String replace = param.getQueryString().replace("&attrs=" + encode, "");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList()));
        }

        return navs;
    }
}
