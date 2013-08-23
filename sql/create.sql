/*
SQLyog Community v10.1 Beta1
MySQL - 5.5.21 : Database - mail
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `tb_mail` */

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
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_rcpt_alias` */

CREATE TABLE `tb_rcpt_alias` (
  `rcpt_id` int(11) NOT NULL AUTO_INCREMENT,
  `alias` varchar(128) NOT NULL,
  `rcpt_addr` varchar(128) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rcpt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
