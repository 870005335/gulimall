package com.liubin.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author liubin
 * @Date 2021/4/10 21:59
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2Vo {

    private String catalogId;

    private List<Catalog3Vo> catalog3List;

    private String id;

    private String name;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catalog3Vo {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
