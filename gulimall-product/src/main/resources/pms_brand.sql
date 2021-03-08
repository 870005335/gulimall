/*
 Navicat Premium Data Transfer

 Source Server         : mysql（docker）
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 192.168.1.96:3306
 Source Schema         : gulimall_pms

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 08/03/2021 19:07:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pms_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand`  (
  `brand_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名',
  `logo` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌logo地址',
  `descript` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  `show_status` tinyint(4) NULL DEFAULT NULL COMMENT '显示状态[0-不显示；1-显示]',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检索首字母',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_brand
-- ----------------------------
INSERT INTO `pms_brand` VALUES (1, '华为', 'https://gulimall-liubin.oss-cn-beijing.aliyuncs.com/2021-03-08/1439edbd-b8f2-475f-8d9c-c4e35dcd9c43_huawei.png', '华为', 1, 'H', 1);
INSERT INTO `pms_brand` VALUES (2, '小米', 'https://gulimall-liubin.oss-cn-beijing.aliyuncs.com/2021-03-08/56ed539d-690e-4978-842a-07cbc53d603d_xiaomi.png', '小米', 1, 'M', 2);
INSERT INTO `pms_brand` VALUES (3, 'OPPO', 'https://gulimall-liubin.oss-cn-beijing.aliyuncs.com/2021-03-08/03e4892e-9e01-41b9-89ef-96abf8d13e48_oppo.png', 'OPPO', 1, 'O', 3);
INSERT INTO `pms_brand` VALUES (4, 'APPLE', 'https://gulimall-liubin.oss-cn-beijing.aliyuncs.com/2021-03-08/28d9b174-342e-4c86-829c-be6f8c48b8af_apple.png', '苹果', 1, 'A', 4);

SET FOREIGN_KEY_CHECKS = 1;
