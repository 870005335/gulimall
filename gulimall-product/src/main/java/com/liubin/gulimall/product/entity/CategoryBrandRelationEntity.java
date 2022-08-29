package com.liubin.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 品牌分类关联
 * 
 * @author liubin
 * @email 870005335
 * @date 2022-08-12 15:55:29
 */
@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	/**
	 * 品牌id
	 */
	private Long brandId;

	/**
	 * 分类id
	 */
	private Long categoryId;

	@TableField(exist = false)
	private String brandName;

	@TableField(exist = false)
	private String categoryName;

}
