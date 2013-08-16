CREATE TABLE `tb_mail` (
  `mail_id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(128) DEFAULT NULL,
  `recipient` varchar(128) DEFAULT NULL,
  `subject` varchar(128) DEFAULT NULL,
  `message` varchar(2048) DEFAULT NULL,
  `smtp_server` varchar(128) DEFAULT NULL,
  `smtp_port` varchar(128) DEFAULT NULL,
  `use_ssl` varchar(32) DEFAULT NULL,
  `id` varchar(128) DEFAULT NULL,
  `password` varbinary(256) DEFAULT NULL,
  `return_id` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `save_time` datetime DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
