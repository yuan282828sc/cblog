-- ----------------------------
-- Table structure for mapper.message
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` bigint(20) NOT NULL COMMENT '用户id 唯一',
  `nickname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '/images/avatar/default.jpg' COMMENT '头像',
  `sex` int(2) NOT NULL DEFAULT 1 COMMENT '性别 1：男 2：女',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(5) NOT NULL DEFAULT 1 COMMENT '-1：异常（拉黑，无效） 0:删除（注销）  1：正常',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `location` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '喵星球' COMMENT '所在地',
  `birthday` datetime NULL DEFAULT NULL COMMENT '用户生日',
  `channel_id` int(11) NOT NULL DEFAULT 1 COMMENT '注册渠道编号 默认1 自注册 2:qq 3:微博',
  `mobile` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `signature` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '这个人懒得留下签名' COMMENT '签名',
  `authority` int(1) NOT NULL DEFAULT 1 COMMENT '用户类型:(1.为普通用户,2.为管理员,3.为开发者)',
  `exp` int(9) NOT NULL DEFAULT 0 COMMENT '用户经验用于等级判断',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  UNIQUE INDEX `uid`(`uid`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10005 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mapper.message
-- ----------------------------
INSERT INTO `user` VALUES (10000, 1580265547356934382, '郁郁', '934cd11fd04135c255567551a5f82fef', '/images/avatar/1.jpg', 1, '0:0:0:0:0:0:0:1', '2020-01-13 20:58:16', '2020-02-22 15:19:26', 1, '2020-03-29 06:45:07', '北京', '2020-02-22 00:00:00', 1, '', '843762268@qq.com', '程序猿小哥哥！', '&#128516;&#128517;', 2, 50);
INSERT INTO `user` VALUES (10003, 1581908378401380633, 'Share_380633', '934cd11fd04135c255567551a5f82fef', '/images/avatar/default.jpg', 1, '127.0.0.1', '2020-02-17 10:59:38', NULL, 1, '2020-02-20 18:59:22', '喵星球', NULL, 1, NULL, '1582691494@qq.com', NULL, '这家伙很懒，还没有写个性签名', 1, 0);

-- ----------------------------
-- Table structure for user_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_relation`;
CREATE TABLE `user_relation`  (
  `id` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `rid` bigint(20) NOT NULL COMMENT '接受信息用户uid',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '关系类别  0：无关系 1：粉丝 2：朋友 ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改关系的时间  暂时默认粉丝关系',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_relation
-- ----------------------------
INSERT INTO `user_relation` VALUES (1, 1580265547356934382, 1581908378401380633, 1, '2020-02-25 12:55:45', NULL);
INSERT INTO `user_relation` VALUES (3, 1582196502455924141, 1580265547356934382, 1, '2020-02-28 12:40:57', NULL);
INSERT INTO `user_relation` VALUES (4, 1580265547356934382, 1582196502455924141, 1, '2020-03-04 11:46:08', NULL);

SET FOREIGN_KEY_CHECKS = 1;
