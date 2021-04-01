

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `aid` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章编号',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章标题',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `create_time` datetime NOT NULL COMMENT '文章创建时间',
  `browse_num` int(9) NOT NULL DEFAULT  0 COMMENT '文章浏览量',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章内容',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态  -1：已删除 0：成功发布 1：待审核  2：不通过',
  `top` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶 0：否，1：是',
  `article_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '文章类型  0：普通帖子，1：精贴  2：公告',
  `open_procedure` tinyint(4) NOT NULL DEFAULT 0 COMMENT '公开方式  0：公开，1：私密  ',
  `review_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否支持评论  0：否，1：是',
  `comment_num` int(9) NOT NULL DEFAULT 0 COMMENT '评论数',
  `temp_file` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '临时文件',
  `update_time` datetime NULL DEFAULT NULL COMMENT '时间权重  用于排行显示',
  PRIMARY KEY (`aid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10086 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, '留言板', 111, '2020-02-19 19:09:37', 7, '绑定私信', 0, 0, 0, 1, 1, 0, NULL, '2020-03-05 13:01:03');
INSERT INTO `article` VALUES (10080, '发生的方式', 1580265547356934382, '2020-03-05 13:26:53', 16, '第三方第三', 0, 1, 0, 0, 1, 0, '[]', '2020-03-05 13:30:08');
INSERT INTO `article` VALUES (10081, '胜多负少', 1580265547356934382, '2020-03-05 13:27:05', 16, '发顺丰', 0, 1, 1, 0, 1, 0, '[]', '2020-03-28 18:03:18');
INSERT INTO `article` VALUES (10082, '水电费水电费', 1580265547356934382, '2020-03-05 13:27:19', 8, '发送到发多少', 0, 0, 1, 0, 1, 0, '[]', '2020-03-28 18:02:55');
INSERT INTO `article` VALUES (10083, '发送到发送到', 1580265547356934382, '2020-03-05 13:27:28', 4, '放松放松的', 0, 0, 0, 0, 1, 0, '[]', '2020-03-28 18:04:26');
INSERT INTO `article` VALUES (10084, '好久没搞搞啦', 1580265547356934382, '2020-03-28 17:47:37', 5, '滋滋滋', 0, 0, 0, 0, 1, 0, '[]', '2020-03-28 17:58:43');

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
  `id` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论编号',
  `aid` int(9) UNSIGNED NOT NULL COMMENT '文章编号',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `create_time` datetime NOT NULL COMMENT '评论创建时间',
  `comment_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '评论类型（0：普通评论 1：神评论）',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `love_num` int(5) NOT NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  CONSTRAINT `article_comment_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `article` (`aid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1427 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_comment
-- ----------------------------
INSERT INTO `article_comment` VALUES (1403, 10081, 1580265547356934382, '2020-03-05 13:27:43', 0, '<img src=\"http://localhost:8080/layui/images/face/69.gif\" alt=\"[话筒]\">', 0);
INSERT INTO `article_comment` VALUES (1404, 10082, 1580265547356934382, '2020-03-05 13:27:50', 0, '水电费', 0);
INSERT INTO `article_comment` VALUES (1405, 10081, 1580265547356934382, '2020-03-05 13:28:06', 0, '水电费', 0);
INSERT INTO `article_comment` VALUES (1406, 10080, 1580265547356934382, '2020-03-05 13:28:13', 0, '胜多负少', 0);
INSERT INTO `article_comment` VALUES (1407, 10080, 1580265547356934382, '2020-03-05 13:28:29', 0, '胜多负少', 0);
INSERT INTO `article_comment` VALUES (1408, 10080, 1582196502455924141, '2020-03-05 13:30:08', 0, '胜多负少', 0);
INSERT INTO `article_comment` VALUES (1409, 10081, 1580265547356934382, '2020-03-28 17:47:51', 0, '是的', 0);
INSERT INTO `article_comment` VALUES (1410, 10081, 1580265547356934382, '2020-03-28 17:51:52', 0, 'sss', 0);
INSERT INTO `article_comment` VALUES (1411, 10081, 1580265547356934382, '2020-03-28 17:51:53', 0, 's', 0);
INSERT INTO `article_comment` VALUES (1412, 10081, 1580265547356934382, '2020-03-28 17:51:55', 0, 's', 0);
INSERT INTO `article_comment` VALUES (1413, 10081, 1580265547356934382, '2020-03-28 17:51:57', 0, 's', 0);
INSERT INTO `article_comment` VALUES (1414, 10081, 1580265547356934382, '2020-03-28 17:51:58', 0, 's', 0);
INSERT INTO `article_comment` VALUES (1415, 10081, 1580265547356934382, '2020-03-28 17:52:00', 0, 's', 0);
INSERT INTO `article_comment` VALUES (1416, 10081, 1580265547356934382, '2020-03-28 17:54:32', 0, 'ss', 0);
INSERT INTO `article_comment` VALUES (1417, 10081, 1580265547356934382, '2020-03-28 17:54:35', 0, 'sss', 0);
INSERT INTO `article_comment` VALUES (1418, 10081, 1580265547356934382, '2020-03-28 17:54:46', 0, 'sss', 0);
INSERT INTO `article_comment` VALUES (1419, 10084, 1580265547356934382, '2020-03-28 17:56:55', 0, 'dsf', 0);
INSERT INTO `article_comment` VALUES (1420, 10084, 1580265547356934382, '2020-03-28 17:58:43', 0, '<img src=\"http://localhost:8080/layui/images/face/2.gif\" alt=\"[哈哈]\">', 0);
INSERT INTO `article_comment` VALUES (1421, 10082, 1580265547356934382, '2020-03-28 17:58:53', 0, '<img src=\"http://localhost:8080/layui/images/face/16.gif\" alt=\"[太开心]\">', 0);
INSERT INTO `article_comment` VALUES (1422, 10082, 1580265547356934382, '2020-03-28 18:02:00', 0, '<img src=\"http://localhost:8080/layui/images/face/48.gif\" alt=\"[伤心]\">', 0);
INSERT INTO `article_comment` VALUES (1423, 10082, 1580265547356934382, '2020-03-28 18:02:55', 0, '<img src=\"http://localhost:8080/layui/images/face/32.gif\" alt=\"[困]\">', 0);
INSERT INTO `article_comment` VALUES (1424, 10081, 1580265547356934382, '2020-03-28 18:03:13', 0, '<img src=\"http://localhost:8080/layui/images/face/59.gif\" alt=\"[草泥马]\">', 0);
INSERT INTO `article_comment` VALUES (1425, 10081, 1580265547356934382, '2020-03-28 18:03:18', 0, '<img src=\"http://localhost:8080/layui/images/face/45.gif\" alt=\"[怒骂]\">', 0);
INSERT INTO `article_comment` VALUES (1426, 10083, 1580265547356934382, '2020-03-28 18:04:26', 0, '<img src=\"http://localhost:8080/layui/images/face/51.gif\" alt=\"[兔子]\">', 0);

-- ----------------------------
-- Table structure for article_favorite
-- ----------------------------
DROP TABLE IF EXISTS `article_favorite`;
CREATE TABLE `article_favorite`  (
  `id` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏文章id',
  `aid` int(9) UNSIGNED NOT NULL COMMENT '文章编号',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `create_time` datetime NOT NULL COMMENT '点赞创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态  0：没收藏 1：收藏 ',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE,
  CONSTRAINT `collect_blog_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `article` (`aid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `article_comment_like`;
CREATE TABLE `article_comment_like`  (
  `id` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞id',
  `acid` int(9) UNSIGNED NOT NULL COMMENT '该评论id',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `create_time` datetime NOT NULL COMMENT '点赞创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态  0：没点赞 1：点赞 ',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `acid`(`acid`) USING BTREE,
  CONSTRAINT `love_record_ibfk_3` FOREIGN KEY (`acid`) REFERENCES `article_comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `aid` int(9) UNSIGNED NOT NULL COMMENT '文章编号',
  `uid` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建用户uid',
  `rid` bigint(20) NOT NULL COMMENT '接受信息用户uid',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '消息类别  0：评论文章  1：评论回复 2：私信 3：系统消息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类内容',
  `read_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否已读 0：未读 1：已读',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE,
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `article` (`aid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (82, 1, 1580265547356934382, 1582196502455924141, 2, '2020-03-05 13:28:52', '请输入内容胜多负少', 1);
INSERT INTO `message` VALUES (83, 1, 1580265547356934382, 1582196502455924141, 2, '2020-03-05 13:28:56', '请输入内容胜多负少', 1);
INSERT INTO `message` VALUES (84, 10080, 1582196502455924141, 1580265547356934382, 0, '2020-03-05 13:30:08', '胜多负少', 1);


-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `fid` int(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文件编号',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件标题',
  `uid` bigint(20) NOT NULL COMMENT '创建用户uid',
  `up_time` datetime NOT NULL COMMENT '文件上传时间',
  `download_num` int(9) NOT NULL DEFAULT  0 COMMENT '文件下载量',
  `path` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态  -1：已删除 0：成功上传 1：待审核  2：不通过',
  `top` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶 0：否，1：是',
  `file_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '文件类型  0：普通文件，1：精品文件  ',
  `open_procedure` tinyint(4) NOT NULL DEFAULT 0 COMMENT '公开方式  0：公开，1：私密  ',
  `download_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否支持下载  0：否，1：是',
  `temp_file` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '临时文件',
  `update_time` datetime NULL DEFAULT NULL COMMENT '时间权重  用于排行显示',
  PRIMARY KEY (`fid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `fid`(`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10086 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
