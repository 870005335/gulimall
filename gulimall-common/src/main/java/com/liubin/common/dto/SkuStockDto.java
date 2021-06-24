package com.liubin.common.dto;

import lombok.Data;

/**
 * @Description
 * @Author liubin
 * @Date 2021/4/9 16:16
 * @Version 1.0
 */
@Data
public class SkuStockDto {

    private Long skuId;

    private Boolean hasStock;
}
