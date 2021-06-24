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
        // 构建检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // 执行检索请求
            SearchResponse searchResponse = searchClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // 分析响应结果并封装成需要的格式
            result = buildSearchResult(searchResponse, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @Author liubin
     * @Description 构建检索查询请求
     * @Date 14:52 2021/5/8
     * @param param
     * @return org.elasticsearch.action.search.SearchRequest
     **/
    private SearchRequest buildSearchRequest(SearchParam param) {
        // 构建DSL检索语句
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         *  查询，模糊匹配，过滤（按照属性，分类，品牌，价格区间，库存）
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            // 模糊匹配
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // bool - filter过滤 - 三级分类Id
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // bool - filter过滤 - 品牌Id
        if (!CollectionUtils.isEmpty(param.getBrandId())) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // bool - filter过滤 - 按照所有指定的属性查询
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            for (String attrStr : param.getAttrs()) {
                BoolQueryBuilder attrBoolQuery = QueryBuilders.boolQuery();
                // attrs=1_5寸:8存&attrs=2_16G:8G
                String[] attrArr = attrStr.split("_");
                // 检索的attrId
                String attrId = attrArr[0];
                // 检索的attrValue
                String[] attrValues = attrArr[1].split(":");
                attrBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                attrBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", attrBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        // bool - filter过滤 - 按照库存所有查询
        if (param.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        // bool - filter过滤 - 按照价格区间查询
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
        // 排序
        if (StringUtils.isNotBlank(param.getSort())) {
            // sort=hotScore_asc
            String[] sortArr = param.getSort().split("_");
            sourceBuilder.sort(sortArr[0], SortOrder.fromString(sortArr[1]));
        }
        // 分页
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE.intValue());
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE.intValue());
        // 高亮显示
        if (StringUtils.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style=color:red>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        //*  聚合分析
        // 品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        // 品牌聚合的子聚合
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName"));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg"));
        sourceBuilder.aggregation(brandAgg);
        // 分类聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg");
        catalogAgg.field("catalogId").size(20);
        // 分类聚合的子聚合
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName"));
        sourceBuilder.aggregation(catalogAgg);
        // 属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // 聚合分析attrId对应的attrName
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName"));
        // 聚合分析attrId对应的attrValue
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(attrAgg);
        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
    }


    private SearchResult buildSearchResult(SearchResponse response, SearchParam param) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
        // 1、返回所查询到的所有商品
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
        // 2、当前商品所涉及到的所有属性信息
        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        List<SearchResult.Attr> attrs = new ArrayList<>();
        for (Terms.Bucket attrIdBucket : attrIdAgg.getBuckets()) {
            SearchResult.Attr attr = new SearchResult.Attr();
            // 属性Id
            Number attrId = attrIdBucket.getKeyAsNumber();
            attr.setAttrId(attrId.longValue());
            // 属性名称
            ParsedStringTerms attrNameAgg = attrIdBucket.getAggregations().get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attr.setAttrName(attrName);
            // 属性值列表
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
        // 3、当前商品所涉及到的所有品牌信息
        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        List<SearchResult.Brand> brands = new ArrayList<>();
        for (Terms.Bucket brandBucket : brandAgg.getBuckets()) {
            SearchResult.Brand brand = new SearchResult.Brand();
            Number brandId = brandBucket.getKeyAsNumber();
            // 品牌Id
            brand.setBrandId(brandId.longValue());
            // 品牌名称
            ParsedStringTerms brandNameAgg = brandBucket.getAggregations().get("brand_name_agg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brand.setBrandName(brandName);
            // 品牌图片
            ParsedStringTerms brandImgAgg = brandBucket.getAggregations().get("brand_img_agg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brand.setBrandImg(brandImg);
            brands.add(brand);
        }
        result.setBrands(brands);
        // 4、当前商品所涉及到的所有分类信息
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        List<SearchResult.category> categories = new ArrayList<>();
        for (Terms.Bucket catalogBucket : catalogAgg.getBuckets()) {
            SearchResult.category category = new SearchResult.category();
            // 分类Id
            Number catalogId = catalogBucket.getKeyAsNumber();
            category.setCatalogId(catalogId.longValue());
            // 分类名称
            ParsedStringTerms catalogNameAgg = catalogBucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            category.setCatalogName(catalogName);
            categories.add(category);
        }
        result.setCatalogs(categories);
        // 5、分页信息-页码
        result.setPageNum(param.getPageNum());
        // 6、分页信息-总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        // 7、分页信息-总页码
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
        // TODO 品牌 分类
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            // 构建面包屑导航功能
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
                // 编码
                String encode = null;
                try {
                    encode = URLEncoder.encode(attr, "UTF-8");
                    // 空格特殊处理
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
