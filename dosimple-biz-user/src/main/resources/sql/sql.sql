CREATE DATABASE `stark_dev` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(25) NOT NULL COMMENT '主键',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `password` varchar(40) NOT NULL COMMENT '密码',
  `salt` varchar(10) NOT NULL COMMENT '盐',
  `session_token` varchar(40) NOT NULL COMMENT 'session令牌',
  `date_created` datetime NOT NULL COMMENT '创建时间（也就是注册时间）',
  `date_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='【用户模块】用户帐号';
SELECT * FROM stark_dev.user;