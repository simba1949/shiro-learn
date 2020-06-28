/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.8.6
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 192.168.8.6:3306
 Source Schema         : db_shiro

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 28/06/2020 08:03:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permission_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限资源',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission_name`(`permission_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (4, '/permission/1');
INSERT INTO `t_permission` VALUES (1, '/permission/1.jsp');
INSERT INTO `t_permission` VALUES (5, '/permission/2');
INSERT INTO `t_permission` VALUES (2, '/permission/2.jsp');
INSERT INTO `t_permission` VALUES (6, '/permission/3');
INSERT INTO `t_permission` VALUES (3, '/permission/3.jsp');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin');
INSERT INTO `t_role` VALUES (2, 'guest');
INSERT INTO `t_role` VALUES (3, 'ui');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(0) NOT NULL COMMENT '角色id',
  `permission_id` int(0) NOT NULL COMMENT '权限资源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色-权限资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (1, 1, 1);
INSERT INTO `t_role_permission` VALUES (2, 1, 2);
INSERT INTO `t_role_permission` VALUES (3, 1, 3);
INSERT INTO `t_role_permission` VALUES (4, 2, 2);
INSERT INTO `t_role_permission` VALUES (5, 3, 2);
INSERT INTO `t_role_permission` VALUES (6, 3, 3);
INSERT INTO `t_role_permission` VALUES (7, 1, 4);
INSERT INTO `t_role_permission` VALUES (8, 1, 5);
INSERT INTO `t_role_permission` VALUES (9, 1, 6);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '盐',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '55400b77b019f7a8e6bee1d335b798a4', 'c0096fa2-3');
INSERT INTO `t_user` VALUES (2, 'guest', '45154888f3460ca36fe1e85269faf7ba', '3de0f7c6-5');
INSERT INTO `t_user` VALUES (3, '3', '617a2b5dfa88c67bb147f843372b4f8c', 'c3f8cfbb-7');
INSERT INTO `t_user` VALUES (4, '4', 'ce6c5f162381701d5500495cb2fc8fa3', '38a617d7-9');
INSERT INTO `t_user` VALUES (5, 'test', '1c20cf5dfdccd4c1a22336679652f55f', 'a3fe728d-a');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `role_id` int(0) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1, 1);
INSERT INTO `t_user_role` VALUES (2, 1, 2);
INSERT INTO `t_user_role` VALUES (3, 1, 3);
INSERT INTO `t_user_role` VALUES (4, 2, 2);
INSERT INTO `t_user_role` VALUES (5, 3, 2);
INSERT INTO `t_user_role` VALUES (6, 3, 3);

SET FOREIGN_KEY_CHECKS = 1;
