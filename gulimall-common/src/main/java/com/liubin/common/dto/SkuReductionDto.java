package com.liubin.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 
 * @Author liubin
 * @Date 2021/3/19 16:29
 * @Version 1.0
 */
@Data
public class SkuReductionDto {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
