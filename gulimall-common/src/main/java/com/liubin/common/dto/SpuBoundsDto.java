package com.liubin.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author liubin
 * @Date 2021/3/19 16:19
 * @Version 1.0
 */
@Data
public class SpuBoundsDto {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;
}
