package com.liubin.gulimall.coupon.service.impl;

import com.liubin.common.dto.MemberPrice;
import com.liubin.common.dto.SkuReductionDto;
import com.liubin.gulimall.coupon.entity.MemberPriceEntity;
import com.liubin.gulimall.coupon.entity.SkuLadderEntity;
import com.liubin.gulimall.coupon.service.MemberPriceService;
import com.liubin.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.Query;

import com.liubin.gulimall.coupon.dao.SkuFullReductionDao;
import com.liubin.gulimall.coupon.entity.SkuFullReductionEntity;
import com.liubin.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionDto reductionDto) {
        // 1、保存满减打折
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionDto.getSkuId());
        skuLadderEntity.setFullCount(reductionDto.getFullCount());
        skuLadderEntity.setDiscount(reductionDto.getDiscount());
        skuLadderEntity.setAddOther(reductionDto.getCountStatus());
        if(reductionDto.getFullCount() > 0){
            skuLadderService.save(skuLadderEntity);
        }

        //2、sms_sku_full_reduction
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(reductionDto,reductionEntity);
        if(reductionEntity.getFullPrice().compareTo(new BigDecimal("0")) > 0){
            this.save(reductionEntity);
        }


        //3、sms_member_price
        List<MemberPrice> memberPrice = reductionDto.getMemberPrice();

        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(reductionDto.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item-> item.getMemberPrice().compareTo(new BigDecimal("0")) > 0).collect(Collectors.toList());

        memberPriceService.saveBatch(collect);
    }
}