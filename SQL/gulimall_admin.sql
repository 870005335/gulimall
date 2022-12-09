/*
 Navicat Premium Data Transfer

 Source Server         : mysql（docker）
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.56.102:3306
 Source Schema         : gulimall_admin

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 22/09/2022 10:54:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `BLOB_DATA` blob NULL,
                                       PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                       INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                       CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
                                   `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                   `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                   `CALENDAR` blob NOT NULL,
                                   PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                       CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `FIRED_TIME` bigint(13) NOT NULL,
                                        `SCHED_TIME` bigint(13) NOT NULL,
                                        `PRIORITY` int(11) NOT NULL,
                                        `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                        `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                        `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                        `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                        PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
                                     `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `JOB_DATA` blob NULL,
                                     PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
                                     INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
                                     INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', NULL, 'io.renren.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B4C000A757064617465446174657400104C6A6176612F7574696C2F446174653B4C0007757064617465727400104C6A6176612F6C616E672F4C6F6E673B78720022696F2E72656E72656E2E636F6D6D6F6E2E656E746974792E42617365456E74697479FB83923222FF87B90200034C000A6372656174654461746571007E000B4C000763726561746F7271007E000C4C0002696471007E000C78707372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001835C205F68787372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700ECF9F6107B456017371007E00110ECF9F6107B4564C740008746573745461736B74000E3020302F3330202A202A202A203F74000672656E72656E740025E69C89E58F82E6B58BE8AF95EFBC8CE5A49AE4B8AAE58F82E695B0E4BDBFE794A86A736F6E737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0012000000007371007E000F7708000001835C205F68787371007E00110ECF9F6107B456017800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
                               `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
                                             `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                             `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                             PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
                                         `CHECKIN_INTERVAL` bigint(13) NOT NULL,
                                         PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('RenrenScheduler', 'Maktub1663728152467', 1663815254249, 15000);

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `REPEAT_COUNT` bigint(7) NOT NULL,
                                         `REPEAT_INTERVAL` bigint(12) NOT NULL,
                                         `TIMES_TRIGGERED` bigint(10) NOT NULL,
                                         PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                         CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
                                          `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                          `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                          `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                          `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                          `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                          `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                          `INT_PROP_1` int(11) NULL DEFAULT NULL,
                                          `INT_PROP_2` int(11) NULL DEFAULT NULL,
                                          `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
                                          `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
                                          `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
                                          `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
                                          `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                          `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                          PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                          CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
                                  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
                                  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
                                  `PRIORITY` int(11) NULL DEFAULT NULL,
                                  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `START_TIME` bigint(13) NOT NULL,
                                  `END_TIME` bigint(13) NULL DEFAULT NULL,
                                  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
                                  `JOB_DATA` blob NULL,
                                  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
                                  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', 'TASK_1067246875800000076', 'DEFAULT', NULL, 1663727400000, -1, 5, 'PAUSED', 'CRON', 1663726924000, 0, NULL, 2, 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B4C000A757064617465446174657400104C6A6176612F7574696C2F446174653B4C0007757064617465727400104C6A6176612F6C616E672F4C6F6E673B78720022696F2E72656E72656E2E636F6D6D6F6E2E656E746974792E42617365456E74697479FB83923222FF87B90200034C000A6372656174654461746571007E000B4C000763726561746F7271007E000C4C0002696471007E000C78707372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001835C205F68787372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700ECF9F6107B456017371007E00110ECF9F6107B4564C740008746573745461736B74000E3020302F3330202A202A202A203F74000672656E72656E740025E69C89E58F82E6B58BE8AF95EFBC8CE5A49AE4B8AAE58F82E695B0E4BDBFE794A86A736F6E737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0012000000007371007E000F7708000001835C205F68787371007E00110ECF9F6107B456017800);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
                                 `id` bigint(20) NOT NULL COMMENT 'id',
                                 `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
                                 `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
                                 `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
                                 `status` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '任务状态  0：暂停  1：正常',
                                 `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                 `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                 `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                                 `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES (1067246875800000076, 'testTask', 'renren', '0 0/30 * * * ?', 0, '有参测试，多个参数使用json', 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`  (
                                     `id` bigint(20) NOT NULL COMMENT 'id',
                                     `job_id` bigint(20) NOT NULL COMMENT '任务id',
                                     `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
                                     `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
                                     `status` tinyint(3) UNSIGNED NOT NULL COMMENT '任务状态    0：失败    1：成功',
                                     `error` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败信息',
                                     `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
                                     `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_job_id`(`job_id`) USING BTREE,
                                     INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `pid` bigint(20) NULL DEFAULT NULL COMMENT '上级ID',
                             `pids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所有上级ID，用逗号分开',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
                             `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
                             `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                             `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                             `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_pid`(`pid`) USING BTREE,
                             INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1067246875800000062, 1067246875800000063, '1067246875800000066,1067246875800000063', '技术部', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000063, 1067246875800000066, '1067246875800000066', '长沙分公司', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000064, 1067246875800000066, '1067246875800000066', '上海分公司', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000065, 1067246875800000064, '1067246875800000066,1067246875800000064', '市场部', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000066, 0, '0', '人人开源集团', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000067, 1067246875800000064, '1067246875800000066,1067246875800000064', '销售部', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dept` VALUES (1067246875800000068, 1067246875800000063, '1067246875800000066,1067246875800000063', '产品部', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型ID',
                                  `dict_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
                                  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
                                  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                                  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `uk_dict_type_value`(`dict_type_id`, `dict_value`) USING BTREE,
                                  INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1160061112075464705, 1160061077912858625, '男', '0', '', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_data` VALUES (1160061146967879681, 1160061077912858625, '女', '1', '', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_data` VALUES (1160061190127267841, 1160061077912858625, '保密', '2', '', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_data` VALUES (1225814069634195457, 1225813644059140097, '公告', '0', '', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_data` VALUES (1225814107559092225, 1225813644059140097, '会议', '1', '', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_data` VALUES (1225814271879340034, 1225813644059140097, '其他', '2', '', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
                                  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
                                  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                                  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1160061077912858625, 'gender', '性别', '', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_dict_type` VALUES (1225813644059140097, 'notice_type', '站内通知-类型', '', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');

-- ----------------------------
-- Table structure for sys_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_error`;
CREATE TABLE `sys_log_error`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `request_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
                                  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
                                  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
                                  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
                                  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP',
                                  `error_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常信息',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '异常日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_error
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `operation` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '用户操作   0：用户登录   1：用户退出',
                                  `status` tinyint(3) UNSIGNED NOT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
                                  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
                                  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP',
                                  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_status`(`status`) USING BTREE,
                                  INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_login
-- ----------------------------
INSERT INTO `sys_log_login` VALUES (1572469883690442753, 0, 1, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', 1067246875800000001, '2022-09-21 14:16:50');
INSERT INTO `sys_log_login` VALUES (1572773575383736321, 0, 1, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', 1067246875800000001, '2022-09-22 10:23:36');

-- ----------------------------
-- Table structure for sys_log_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_operation`;
CREATE TABLE `sys_log_operation`  (
                                      `id` bigint(20) NOT NULL COMMENT 'id',
                                      `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
                                      `request_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
                                      `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
                                      `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
                                      `request_time` int(10) UNSIGNED NOT NULL COMMENT '请求时长(毫秒)',
                                      `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
                                      `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP',
                                      `status` tinyint(3) UNSIGNED NOT NULL COMMENT '状态  0：失败   1：成功',
                                      `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                      `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                      `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_operation
-- ----------------------------
INSERT INTO `sys_log_operation` VALUES (1572515880470732802, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"商品系统\",\"url\":\"\",\"type\":0,\"icon\":\"icon-skin\",\"permissions\":\"\",\"sort\":4,\"createDate\":null,\"parentName\":\"一级菜单\"}', 58, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:19:36');
INSERT INTO `sys_log_operation` VALUES (1572516133949300738, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572515880357486593,\"children\":[],\"name\":\"分类维护\",\"url\":\"product/category\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"商品系统\"}', 24, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:20:37');
INSERT INTO `sys_log_operation` VALUES (1572516754119090177, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572515880357486593,\"children\":[],\"name\":\"品牌管理\",\"url\":\"product/brand\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"商品系统\"}', 27, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:23:05');
INSERT INTO `sys_log_operation` VALUES (1572517005357899778, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572515880357486593,\"children\":[],\"name\":\"平台属性\",\"url\":\"\",\"type\":0,\"icon\":\"icon-setting\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"商品系统\"}', 28, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:24:05');
INSERT INTO `sys_log_operation` VALUES (1572517154553487361, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517005299179522,\"children\":[],\"name\":\"属性分组\",\"url\":\"product/attr-group\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"平台属性\"}', 26, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:24:40');
INSERT INTO `sys_log_operation` VALUES (1572517406081703937, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517005299179522,\"children\":[],\"name\":\"规格参数\",\"url\":\"product/base-attr\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"平台属性\"}', 26, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:25:40');
INSERT INTO `sys_log_operation` VALUES (1572517508691156994, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517005299179522,\"children\":[],\"name\":\"销售属性\",\"url\":\"product/sale-attr\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"平台属性\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:26:05');
INSERT INTO `sys_log_operation` VALUES (1572517643571585025, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517005299179522,\"children\":[],\"name\":\"商品维护\",\"url\":\"product/spu\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', 28, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:26:37');
INSERT INTO `sys_log_operation` VALUES (1572517726316814337, '修改', '/gulimall-admin/sys/menu', 'PUT', '{\"id\":1572517643508670465,\"pid\":1572515880357486593,\"children\":[],\"name\":\"商品维护\",\"url\":\"product/spu\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"商品系统\"}', 41, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:26:57');
INSERT INTO `sys_log_operation` VALUES (1572517876787470337, '修改', '/gulimall-admin/sys/menu', 'PUT', '{\"id\":1572517643508670465,\"pid\":1572515880357486593,\"children\":[],\"name\":\"商品维护\",\"url\":\"\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"商品系统\"}', 28, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:27:32');
INSERT INTO `sys_log_operation` VALUES (1572518861912039425, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572515880357486593,\"children\":[],\"name\":\"优惠营销\",\"url\":\"\",\"type\":0,\"icon\":\"icon-Dollar-circle-fill\",\"permissions\":\"\",\"sort\":5,\"createDate\":null,\"parentName\":\"一级菜单\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-21 17:31:27');
INSERT INTO `sys_log_operation` VALUES (1572773686159499265, '修改', '/gulimall-admin/sys/menu', 'PUT', '{\"id\":1572518861857513473,\"pid\":0,\"children\":[],\"name\":\"优惠营销\",\"url\":\"\",\"type\":0,\"icon\":\"icon-Dollar-circle-fill\",\"permissions\":\"\",\"sort\":5,\"createDate\":null,\"parentName\":\"一级菜单\"}', 30, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:24:02');
INSERT INTO `sys_log_operation` VALUES (1572775006295711745, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"库存系统\",\"url\":\"\",\"type\":0,\"icon\":\"icon-home\",\"permissions\":\"\",\"sort\":6,\"createDate\":null,\"parentName\":\"一级菜单\"}', 24, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:29:17');
INSERT INTO `sys_log_operation` VALUES (1572775201859330049, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"订单系统\",\"url\":\"\",\"type\":0,\"icon\":\"icon-transaction\",\"permissions\":\"\",\"sort\":7,\"createDate\":null,\"parentName\":\"一级菜单\"}', 24, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:30:04');
INSERT INTO `sys_log_operation` VALUES (1572775340762095617, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"用户系统\",\"url\":\"\",\"type\":0,\"icon\":\"icon-team\",\"permissions\":\"\",\"sort\":8,\"createDate\":null,\"parentName\":\"一级菜单\"}', 35, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:30:37');
INSERT INTO `sys_log_operation` VALUES (1572776180507893761, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"内容管理\",\"url\":\"\",\"type\":0,\"icon\":\"icon-folder\",\"permissions\":\"\",\"sort\":9,\"createDate\":null,\"parentName\":\"一级菜单\"}', 33, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:33:57');
INSERT INTO `sys_log_operation` VALUES (1572776279946452994, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"优惠券管理\",\"url\":\"coupon/coupon\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"优惠营销\"}', 31, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:34:21');
INSERT INTO `sys_log_operation` VALUES (1572776370820243457, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"发放记录\",\"url\":\"coupon/history\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"优惠营销\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:34:42');
INSERT INTO `sys_log_operation` VALUES (1572776459198423042, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"专题活动\",\"url\":\"coupon/subject\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"优惠营销\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:35:03');
INSERT INTO `sys_log_operation` VALUES (1572776536382005249, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"秒杀活动\",\"url\":\"coupon/seckill\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"优惠营销\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:35:22');
INSERT INTO `sys_log_operation` VALUES (1572776642678251521, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"积分维护\",\"url\":\"coupon/bounds\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":4,\"createDate\":null,\"parentName\":\"优惠营销\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:35:47');
INSERT INTO `sys_log_operation` VALUES (1572776720843300865, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"满减折扣\",\"url\":\"coupon/full\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":5,\"createDate\":null,\"parentName\":\"优惠营销\"}', 22, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:36:06');
INSERT INTO `sys_log_operation` VALUES (1572776892155453442, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775006241185793,\"children\":[],\"name\":\"仓库维护\",\"url\":\"ware/ware-info\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"库存系统\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:36:47');
INSERT INTO `sys_log_operation` VALUES (1572776962451988481, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775006241185793,\"children\":[],\"name\":\"库存工作单\",\"url\":\"ware/task\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"库存系统\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:37:03');
INSERT INTO `sys_log_operation` VALUES (1572777035072167938, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775006241185793,\"children\":[],\"name\":\"商品库存\",\"url\":\"ware/sku\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"库存系统\"}', 27, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:37:21');
INSERT INTO `sys_log_operation` VALUES (1572777205792923649, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775201808998401,\"children\":[],\"name\":\"订单查询\",\"url\":\"order/order\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"订单系统\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:38:01');
INSERT INTO `sys_log_operation` VALUES (1572777270607503361, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775201808998401,\"children\":[],\"name\":\"退货单处理\",\"url\":\"order/return\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"订单系统\"}', 25, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:38:17');
INSERT INTO `sys_log_operation` VALUES (1572777380556988418, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775201808998401,\"children\":[],\"name\":\"等级规则\",\"url\":\"order/settings\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"订单系统\"}', 24, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:38:43');
INSERT INTO `sys_log_operation` VALUES (1572777456255787009, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775201808998401,\"children\":[],\"name\":\"支付流水查询\",\"url\":\"order/payment\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"订单系统\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:39:01');
INSERT INTO `sys_log_operation` VALUES (1572777546416545794, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775201808998401,\"children\":[],\"name\":\"退款流水查询\",\"url\":\"order/refund\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":4,\"createDate\":null,\"parentName\":\"订单系统\"}', 22, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:39:23');
INSERT INTO `sys_log_operation` VALUES (1572777625797943298, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775340694986754,\"children\":[],\"name\":\"会员列表\",\"url\":\"member/member\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"用户系统\"}', 20, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:39:41');
INSERT INTO `sys_log_operation` VALUES (1572777733092433921, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775340694986754,\"children\":[],\"name\":\"会员等级\",\"url\":\"member/level\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"用户系统\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:40:07');
INSERT INTO `sys_log_operation` VALUES (1572778497609195521, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775340694986754,\"children\":[],\"name\":\"积分变化\",\"url\":\"member/growth\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"用户系统\"}', 22, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:43:09');
INSERT INTO `sys_log_operation` VALUES (1572778589690945538, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775340694986754,\"children\":[],\"name\":\"统计信息\",\"url\":\"member/statistics\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"用户系统\"}', 18, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:43:31');
INSERT INTO `sys_log_operation` VALUES (1572778701934714881, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572776180428201985,\"children\":[],\"name\":\"首页推荐\",\"url\":\"content/index\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"内容管理\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:43:58');
INSERT INTO `sys_log_operation` VALUES (1572778770285092865, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572776180428201985,\"children\":[],\"name\":\"分类热门\",\"url\":\"content/category\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"内容管理\"}', 20, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:44:14');
INSERT INTO `sys_log_operation` VALUES (1572778868230479873, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572776180428201985,\"children\":[],\"name\":\"评论管理\",\"url\":\"content/comments\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"内容管理\"}', 24, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:44:38');
INSERT INTO `sys_log_operation` VALUES (1572778992881000449, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517643508670465,\"children\":[],\"name\":\"spu管理\",\"url\":\"product/spu\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"商品维护\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:45:07');
INSERT INTO `sys_log_operation` VALUES (1572779119200854018, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517643508670465,\"children\":[],\"name\":\"发布商品\",\"url\":\"product/spu-add\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"商品维护\"}', 27, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:45:37');
INSERT INTO `sys_log_operation` VALUES (1572779217330790402, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572517643508670465,\"children\":[],\"name\":\"商品管理\",\"url\":\"product/manager\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":2,\"createDate\":null,\"parentName\":\"商品维护\"}', 19, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:46:01');
INSERT INTO `sys_log_operation` VALUES (1572779404136701954, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775006241185793,\"children\":[],\"name\":\"采购单维护\",\"url\":\"\",\"type\":0,\"icon\":\"icon-wrench\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"库存系统\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:46:45');
INSERT INTO `sys_log_operation` VALUES (1572779502065311745, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572779404077981697,\"children\":[],\"name\":\"采购需求\",\"url\":\"ware/purchase-item\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":0,\"createDate\":null,\"parentName\":\"采购单维护\"}', 20, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:47:09');
INSERT INTO `sys_log_operation` VALUES (1572779575281082370, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572779404077981697,\"children\":[],\"name\":\"采购单\",\"url\":\"ware/purchase\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"采购单维护\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:47:26');
INSERT INTO `sys_log_operation` VALUES (1572779846006628354, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572775006241185793,\"children\":[],\"name\":\"会员价格\",\"url\":\"coupon/member-price\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":6,\"createDate\":null,\"parentName\":\"库存系统\"}', 23, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:48:31');
INSERT INTO `sys_log_operation` VALUES (1572780060385894402, '保存', '/gulimall-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1572518861857513473,\"children\":[],\"name\":\"每日秒杀\",\"url\":\"coupon/seckill-session\",\"type\":0,\"icon\":\"icon-right-circle\",\"permissions\":\"\",\"sort\":6,\"createDate\":null,\"parentName\":\"优惠营销\"}', 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 1, 'admin', 1067246875800000001, '2022-09-22 10:49:22');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `pid` bigint(20) NULL DEFAULT NULL COMMENT '上级ID，一级菜单为0',
                             `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                             `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
                             `permissions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
                             `type` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
                             `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                             `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
                             `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                             `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                             `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_pid`(`pid`) USING BTREE,
                             INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1067246875800000002, 0, '权限管理', NULL, NULL, 0, 'icon-safetycertificate', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000003, 1067246875800000055, '新增', NULL, 'sys:user:save,sys:dept:list,sys:role:list', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000004, 1067246875800000055, '修改', NULL, 'sys:user:update,sys:dept:list,sys:role:list', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000005, 1067246875800000055, '删除', NULL, 'sys:user:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000006, 1067246875800000055, '导出', NULL, 'sys:user:export', 1, NULL, 4, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000007, 1067246875800000002, '角色管理', 'sys/role', NULL, 0, 'icon-team', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000008, 1067246875800000007, '查看', NULL, 'sys:role:page,sys:role:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000009, 1067246875800000007, '新增', NULL, 'sys:role:save,sys:menu:select,sys:dept:list', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000010, 1067246875800000007, '修改', NULL, 'sys:role:update,sys:menu:select,sys:dept:list', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000011, 1067246875800000007, '删除', NULL, 'sys:role:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000012, 1067246875800000002, '部门管理', 'sys/dept', NULL, 0, 'icon-apartment', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000014, 1067246875800000012, '查看', NULL, 'sys:dept:list,sys:dept:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000015, 1067246875800000012, '新增', NULL, 'sys:dept:save', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000016, 1067246875800000012, '修改', NULL, 'sys:dept:update', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000017, 1067246875800000012, '删除', NULL, 'sys:dept:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000025, 1067246875800000035, '菜单管理', 'sys/menu', NULL, 0, 'icon-unorderedlist', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000026, 1067246875800000025, '查看', NULL, 'sys:menu:list,sys:menu:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000027, 1067246875800000025, '新增', NULL, 'sys:menu:save', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000028, 1067246875800000025, '修改', NULL, 'sys:menu:update', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000029, 1067246875800000025, '删除', NULL, 'sys:menu:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000030, 1067246875800000035, '定时任务', 'job/schedule', NULL, 0, 'icon-dashboard', 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000031, 1067246875800000030, '查看', NULL, 'sys:schedule:page,sys:schedule:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000032, 1067246875800000030, '新增', NULL, 'sys:schedule:save', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000033, 1067246875800000030, '修改', NULL, 'sys:schedule:update', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000034, 1067246875800000030, '删除', NULL, 'sys:schedule:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000035, 0, '系统设置', NULL, NULL, 0, 'icon-setting', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000036, 1067246875800000030, '暂停', NULL, 'sys:schedule:pause', 1, NULL, 4, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000037, 1067246875800000030, '恢复', NULL, 'sys:schedule:resume', 1, NULL, 5, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000038, 1067246875800000030, '立即执行', NULL, 'sys:schedule:run', 1, NULL, 6, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000039, 1067246875800000030, '日志列表', NULL, 'sys:schedule:log', 1, NULL, 7, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000040, 1067246875800000035, '参数管理', 'sys/params', '', 0, 'icon-fileprotect', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000041, 1067246875800000035, '字典管理', 'sys/dict-type', NULL, 0, 'icon-golden-fill', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000042, 1067246875800000041, '查看', NULL, 'sys:dict:page,sys:dict:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000043, 1067246875800000041, '新增', NULL, 'sys:dict:save', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000044, 1067246875800000041, '修改', NULL, 'sys:dict:update', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000045, 1067246875800000041, '删除', NULL, 'sys:dict:delete', 1, NULL, 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000046, 0, '日志管理', NULL, NULL, 0, 'icon-container', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000047, 1067246875800000035, '文件上传', 'oss/oss', 'sys:oss:all', 0, 'icon-upload', 4, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000048, 1067246875800000046, '登录日志', 'sys/log-login', 'sys:log:login', 0, 'icon-filedone', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000049, 1067246875800000046, '操作日志', 'sys/log-operation', 'sys:log:operation', 0, 'icon-solution', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000050, 1067246875800000046, '异常日志', 'sys/log-error', 'sys:log:error', 0, 'icon-file-exception', 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000051, 1067246875800000053, 'SQL监控', '{{ window.SITE_CONFIG[\"apiURL\"] }}/druid/sql.html', NULL, 0, 'icon-database', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000053, 0, '系统监控', NULL, NULL, 0, 'icon-desktop', 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000055, 1067246875800000002, '用户管理', 'sys/user', NULL, 0, 'icon-user', 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000056, 1067246875800000055, '查看', NULL, 'sys:user:page,sys:user:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000057, 1067246875800000040, '新增', NULL, 'sys:params:save', 1, NULL, 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000058, 1067246875800000040, '导出', NULL, 'sys:params:export', 1, NULL, 4, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000059, 1067246875800000040, '查看', '', 'sys:params:page,sys:params:info', 1, NULL, 0, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000060, 1067246875800000040, '修改', NULL, 'sys:params:update', 1, NULL, 2, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1067246875800000061, 1067246875800000040, '删除', '', 'sys:params:delete', 1, '', 3, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1156748733921165314, 1067246875800000053, '接口文档', '{{ window.SITE_CONFIG[\"apiURL\"] }}/doc.html', '', 0, 'icon-file-word', 1, 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');
INSERT INTO `sys_menu` VALUES (1572515880357486593, 0, '商品系统', '', '', 0, 'icon-skin', 4, 1067246875800000001, '2022-09-21 17:19:36', 1067246875800000001, '2022-09-21 17:19:36');
INSERT INTO `sys_menu` VALUES (1572516133882191873, 1572515880357486593, '分类维护', 'product/category', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-21 17:20:37', 1067246875800000001, '2022-09-21 17:20:37');
INSERT INTO `sys_menu` VALUES (1572516754060369922, 1572515880357486593, '品牌管理', 'product/brand', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-21 17:23:05', 1067246875800000001, '2022-09-21 17:23:05');
INSERT INTO `sys_menu` VALUES (1572517005299179522, 1572515880357486593, '平台属性', '', '', 0, 'icon-setting', 2, 1067246875800000001, '2022-09-21 17:24:05', 1067246875800000001, '2022-09-21 17:24:05');
INSERT INTO `sys_menu` VALUES (1572517154490572802, 1572517005299179522, '属性分组', 'product/attr-group', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-21 17:24:40', 1067246875800000001, '2022-09-21 17:24:40');
INSERT INTO `sys_menu` VALUES (1572517406018789377, 1572517005299179522, '规格参数', 'product/base-attr', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-21 17:25:40', 1067246875800000001, '2022-09-21 17:25:40');
INSERT INTO `sys_menu` VALUES (1572517508619853826, 1572517005299179522, '销售属性', 'product/sale-attr', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-21 17:26:05', 1067246875800000001, '2022-09-21 17:26:05');
INSERT INTO `sys_menu` VALUES (1572517643508670465, 1572515880357486593, '商品维护', '', '', 0, 'icon-right-circle', 3, 1067246875800000001, '2022-09-21 17:26:37', 1067246875800000001, '2022-09-21 17:27:32');
INSERT INTO `sys_menu` VALUES (1572518861857513473, 0, '优惠营销', '', '', 0, 'icon-Dollar-circle-fill', 5, 1067246875800000001, '2022-09-21 17:31:27', 1067246875800000001, '2022-09-22 10:24:02');
INSERT INTO `sys_menu` VALUES (1572775006241185793, 0, '库存系统', '', '', 0, 'icon-home', 6, 1067246875800000001, '2022-09-22 10:29:17', 1067246875800000001, '2022-09-22 10:29:17');
INSERT INTO `sys_menu` VALUES (1572775201808998401, 0, '订单系统', '', '', 0, 'icon-transaction', 7, 1067246875800000001, '2022-09-22 10:30:04', 1067246875800000001, '2022-09-22 10:30:04');
INSERT INTO `sys_menu` VALUES (1572775340694986754, 0, '用户系统', '', '', 0, 'icon-team', 8, 1067246875800000001, '2022-09-22 10:30:37', 1067246875800000001, '2022-09-22 10:30:37');
INSERT INTO `sys_menu` VALUES (1572776180428201985, 0, '内容管理', '', '', 0, 'icon-folder', 9, 1067246875800000001, '2022-09-22 10:33:57', 1067246875800000001, '2022-09-22 10:33:57');
INSERT INTO `sys_menu` VALUES (1572776279883538434, 1572518861857513473, '优惠券管理', 'coupon/coupon', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:34:21', 1067246875800000001, '2022-09-22 10:34:21');
INSERT INTO `sys_menu` VALUES (1572776370753134594, 1572518861857513473, '发放记录', 'coupon/history', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:34:42', 1067246875800000001, '2022-09-22 10:34:42');
INSERT INTO `sys_menu` VALUES (1572776459143897089, 1572518861857513473, '专题活动', 'coupon/subject', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:35:03', 1067246875800000001, '2022-09-22 10:35:03');
INSERT INTO `sys_menu` VALUES (1572776536323284993, 1572518861857513473, '秒杀活动', 'coupon/seckill', '', 0, 'icon-right-circle', 3, 1067246875800000001, '2022-09-22 10:35:22', 1067246875800000001, '2022-09-22 10:35:22');
INSERT INTO `sys_menu` VALUES (1572776642619531266, 1572518861857513473, '积分维护', 'coupon/bounds', '', 0, 'icon-right-circle', 4, 1067246875800000001, '2022-09-22 10:35:47', 1067246875800000001, '2022-09-22 10:35:47');
INSERT INTO `sys_menu` VALUES (1572776720780386306, 1572518861857513473, '满减折扣', 'coupon/full', '', 0, 'icon-right-circle', 5, 1067246875800000001, '2022-09-22 10:36:06', 1067246875800000001, '2022-09-22 10:36:06');
INSERT INTO `sys_menu` VALUES (1572776892105121793, 1572775006241185793, '仓库维护', 'ware/ware-info', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:36:47', 1067246875800000001, '2022-09-22 10:36:47');
INSERT INTO `sys_menu` VALUES (1572776962393268226, 1572775006241185793, '库存工作单', 'ware/task', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:37:03', 1067246875800000001, '2022-09-22 10:37:03');
INSERT INTO `sys_menu` VALUES (1572777034988281858, 1572775006241185793, '商品库存', 'ware/sku', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:37:21', 1067246875800000001, '2022-09-22 10:37:21');
INSERT INTO `sys_menu` VALUES (1572777205746786306, 1572775201808998401, '订单查询', 'order/order', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:38:01', 1067246875800000001, '2022-09-22 10:38:01');
INSERT INTO `sys_menu` VALUES (1572777270544588802, 1572775201808998401, '退货单处理', 'order/return', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:38:17', 1067246875800000001, '2022-09-22 10:38:17');
INSERT INTO `sys_menu` VALUES (1572777380489879553, 1572775201808998401, '等级规则', 'order/settings', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:38:43', 1067246875800000001, '2022-09-22 10:38:43');
INSERT INTO `sys_menu` VALUES (1572777456197066753, 1572775201808998401, '支付流水查询', 'order/payment', '', 0, 'icon-right-circle', 3, 1067246875800000001, '2022-09-22 10:39:01', 1067246875800000001, '2022-09-22 10:39:01');
INSERT INTO `sys_menu` VALUES (1572777546362019842, 1572775201808998401, '退款流水查询', 'order/refund', '', 0, 'icon-right-circle', 4, 1067246875800000001, '2022-09-22 10:39:23', 1067246875800000001, '2022-09-22 10:39:23');
INSERT INTO `sys_menu` VALUES (1572777625735028737, 1572775340694986754, '会员列表', 'member/member', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:39:41', 1067246875800000001, '2022-09-22 10:39:41');
INSERT INTO `sys_menu` VALUES (1572777733037907970, 1572775340694986754, '会员等级', 'member/level', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:40:07', 1067246875800000001, '2022-09-22 10:40:07');
INSERT INTO `sys_menu` VALUES (1572778497546280961, 1572775340694986754, '积分变化', 'member/growth', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:43:09', 1067246875800000001, '2022-09-22 10:43:09');
INSERT INTO `sys_menu` VALUES (1572778589640613889, 1572775340694986754, '统计信息', 'member/statistics', '', 0, 'icon-right-circle', 3, 1067246875800000001, '2022-09-22 10:43:31', 1067246875800000001, '2022-09-22 10:43:31');
INSERT INTO `sys_menu` VALUES (1572778701855023106, 1572776180428201985, '首页推荐', 'content/index', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:43:58', 1067246875800000001, '2022-09-22 10:43:58');
INSERT INTO `sys_menu` VALUES (1572778770238955521, 1572776180428201985, '分类热门', 'content/category', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:44:14', 1067246875800000001, '2022-09-22 10:44:14');
INSERT INTO `sys_menu` VALUES (1572778868163371009, 1572776180428201985, '评论管理', 'content/comments', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:44:38', 1067246875800000001, '2022-09-22 10:44:38');
INSERT INTO `sys_menu` VALUES (1572778992822280194, 1572517643508670465, 'spu管理', 'product/spu', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:45:07', 1067246875800000001, '2022-09-22 10:45:07');
INSERT INTO `sys_menu` VALUES (1572779119137939457, 1572517643508670465, '发布商品', 'product/spu-add', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:45:37', 1067246875800000001, '2022-09-22 10:45:37');
INSERT INTO `sys_menu` VALUES (1572779217276264450, 1572517643508670465, '商品管理', 'product/manager', '', 0, 'icon-right-circle', 2, 1067246875800000001, '2022-09-22 10:46:01', 1067246875800000001, '2022-09-22 10:46:01');
INSERT INTO `sys_menu` VALUES (1572779404077981697, 1572775006241185793, '采购单维护', '', '', 0, 'icon-wrench', 3, 1067246875800000001, '2022-09-22 10:46:45', 1067246875800000001, '2022-09-22 10:46:45');
INSERT INTO `sys_menu` VALUES (1572779502010785794, 1572779404077981697, '采购需求', 'ware/purchase-item', '', 0, 'icon-right-circle', 0, 1067246875800000001, '2022-09-22 10:47:09', 1067246875800000001, '2022-09-22 10:47:09');
INSERT INTO `sys_menu` VALUES (1572779575222362114, 1572779404077981697, '采购单', 'ware/purchase', '', 0, 'icon-right-circle', 1, 1067246875800000001, '2022-09-22 10:47:26', 1067246875800000001, '2022-09-22 10:47:26');
INSERT INTO `sys_menu` VALUES (1572779845947908098, 1572775006241185793, '会员价格', 'coupon/member-price', '', 0, 'icon-right-circle', 6, 1067246875800000001, '2022-09-22 10:48:31', 1067246875800000001, '2022-09-22 10:48:31');
INSERT INTO `sys_menu` VALUES (1572780060335562753, 1572518861857513473, '每日秒杀', 'coupon/seckill-session', '', 0, 'icon-right-circle', 6, 1067246875800000001, '2022-09-22 10:49:22', 1067246875800000001, '2022-09-22 10:49:22');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
                            `id` bigint(20) NOT NULL COMMENT 'id',
                            `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
                            `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                            `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_params
-- ----------------------------
DROP TABLE IF EXISTS `sys_params`;
CREATE TABLE `sys_params`  (
                               `id` bigint(20) NOT NULL COMMENT 'id',
                               `param_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数编码',
                               `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
                               `param_type` tinyint(3) UNSIGNED NULL DEFAULT 1 COMMENT '类型   0：系统参数   1：非系统参数',
                               `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                               `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                               `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                               `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `uk_param_code`(`param_code`) USING BTREE,
                               INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_params
-- ----------------------------
INSERT INTO `sys_params` VALUES (1067246875800000073, 'CLOUD_STORAGE_CONFIG_KEY', '{\"type\":1,\"qiniuDomain\":\"http://test.oss.renren.io\",\"qiniuPrefix\":\"upload\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"qiniuBucketName\":\"renren-oss\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\"}', 0, '云存储配置信息', 1067246875800000001, '2022-09-21 02:19:29', 1067246875800000001, '2022-09-21 02:19:29');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
                             `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                             `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                             `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_dept_id`(`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_data_scope`;
CREATE TABLE `sys_role_data_scope`  (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                        `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
                                        `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                        `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色数据权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_data_scope
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_role_id`(`role_id`) USING BTREE,
                                  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
                                  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_role_id`(`role_id`) USING BTREE,
                                  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色用户关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                             `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
                             `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
                             `head_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
                             `gender` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
                             `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                             `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                             `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
                             `super_admin` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
                             `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：停用   1：正常',
                             `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
                             `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
                             `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uk_username`(`username`) USING BTREE,
                             INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '管理员', NULL, 0, 'root@renren.io', '13612345678', NULL, 1, 1, 1067246875800000001, '2022-09-21 02:19:28', 1067246875800000001, '2022-09-21 02:19:28');

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
                                   `id` bigint(20) NOT NULL COMMENT 'id',
                                   `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                   `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户token',
                                   `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
                                   `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
                                   UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1572469883841437697, 1067246875800000001, 'e2ac548e9a1ae19a42b9f91ca0724d7a', '2022-09-22 22:23:36', '2022-09-22 10:23:36', '2022-09-21 14:16:50');

SET FOREIGN_KEY_CHECKS = 1;
