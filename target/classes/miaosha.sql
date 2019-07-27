/*
 Navicat MySQL Data Transfer

 Source Server         : Main
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : miaosha

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 20/07/2019 11:20:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT 0 COMMENT '//1代表男性，2代表女性',
  `age` int(11) NOT NULL DEFAULT -1,
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `register_mode` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '//byphone,bywechat,byalipay',
  `third_party_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Register`(`telephone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (15, 'test1', 0, -1, '13566666666', 'by phone', '');
INSERT INTO `user_info` VALUES (16, 'test2', 0, -1, '13788888888', 'by phone', '');
INSERT INTO `user_info` VALUES (17, 'test1234', 0, -1, '13222222222', 'by phone', '');
INSERT INTO `user_info` VALUES (18, 'test719', 0, -1, '18777777777', 'by phone', '');
INSERT INTO `user_info` VALUES (19, 'test9', 0, -1, '15555555555', 'by phone', '');
INSERT INTO `user_info` VALUES (21, '????1', 0, -1, '17777777777', 'by phone', '');
INSERT INTO `user_info` VALUES (23, 'test213', 0, -1, '18999999999', 'by phone', '');

-- ----------------------------
-- Table structure for user_password
-- ----------------------------
DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_password
-- ----------------------------
INSERT INTO `user_password` VALUES (5, '$2a$10$BxigP9Wbo8lzpB6JZqzz3ugMia58VkgLZ9/g1RmeqdifbyD27wUGy', 15);
INSERT INTO `user_password` VALUES (6, '$2a$10$8CVYL4iHQrzzosMGzNfhquf/3pvHcBB14iiauNaTRIr5pJCvJcgB.', 16);
INSERT INTO `user_password` VALUES (7, '$2a$10$HWw32cIiLSNAbtL8A/AhzuvEZA8iuhyj0GK.jplY0PuAfxXNQYxlS', 17);
INSERT INTO `user_password` VALUES (8, '$2a$10$xAmV5JQChu6VcW5yJNKm0OfX5bmBGRvuhyiEFABXqIFuwk7guNQg.', 18);
INSERT INTO `user_password` VALUES (9, '$2a$10$BeXOzfhbRLkPfovgqT/3ueIdmNMdr2zvJJfHGijnCg8k7Wtv8hEgC', 19);
INSERT INTO `user_password` VALUES (10, '$2a$10$5qRnHGOgS/pELuIdLjSSHuHltNA6hl6im.t08MngSrmYFl.IeJlmu', 21);
INSERT INTO `user_password` VALUES (12, '$2a$10$tq1vNKBHTlJJmDj7wU3VzeeQzFFRynyYWBzaoMqmjp51OeuWimsry', 23);

SET FOREIGN_KEY_CHECKS = 1;
