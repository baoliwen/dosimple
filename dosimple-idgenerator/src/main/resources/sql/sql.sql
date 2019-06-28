CREATE TABLE `t_id_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biz_tag` varchar(45) NOT NULL,
  `max_id` bigint(20) NOT NULL,
  `step` int(6) NOT NULL,
  `biz_desc` varchar(1024) NOT NULL,
  `date_udpated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `biz_tag_UNIQUE` (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `t_id_generator` (`id`, `biz_tag`, `max_id`, `step`, `biz_desc`, `date_udpated`) VALUES ('1', 'pay_order', '1', '2000', '订单ID', '2018-01-01');

