package com.liubin.gulimall.product.feign;

import com.liubin.common.dto.SkuReductionDto;
import com.liubin.common.dto.SpuBoundsDto;
import com.liubin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description coupon远程调用服务接口
 * @Author liubin
 * @Date 2021/3/19 16:14
 * @Version 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * @Author liubin
     * @Description
     * @Date 16:40 2021/3/19
     * @param spuBoundsDto
     * @return {@link com.liubin.common.utils.R}
     **/
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsDto spuBoundsDto);

    /**
     * @Author liubin
     * @Description  调用远程服务保存sku满减优惠信息
     * @Date 16:36 2021/3/19 
     * @param reductionDto
     * @return {@link R}
     **/
    @PostMapping("coupon/skufullreduction/saveSkuReduction")
    R saveSkuReduction(@RequestBody SkuReductionDto reductionDto);
}
