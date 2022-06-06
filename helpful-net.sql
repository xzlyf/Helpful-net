-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: helpful-net
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text,
  `isTop` tinyint(1) DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'本站秉承相互帮助，不存在刷分、刷赞等违反互联网规定，所有流量均为真实用户',0,'2022-04-27 03:14:51'),(2,'本站与所有用户无任何利益关系，所有积分不能用金钱交易',0,'2022-04-27 03:15:55'),(3,'本站完全免费，不存在任何付费形式',0,'2022-04-27 03:16:13');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `done_user_id` int DEFAULT NULL COMMENT '任务完成者id',
  `from_user_id` int DEFAULT NULL COMMENT '任务创建者id',
  `task_id` int DEFAULT NULL COMMENT '任务id',
  `task_type` int DEFAULT NULL COMMENT '任务类型id',
  `task_pay` int DEFAULT NULL COMMENT '任务金额',
  `task_desc` varchar(100) DEFAULT NULL COMMENT '任务简述',
  `from_user_name` varchar(100) DEFAULT NULL COMMENT '任务创建姓名',
  `done_user_name` varchar(100) DEFAULT NULL COMMENT '任务完成者姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (69,'2022-05-25 10:01:23',2,25,42,2,1,'30秒教你做...','互助侠720071','管理员'),(70,'2022-05-25 10:01:49',2,25,47,2,1,'[悲鸣竞速]375秒欧....','互助侠720071','管理员'),(71,'2022-05-25 10:08:51',25,2,38,2,1,'“历史书太小 装','管理员','互助侠720071'),(72,'2022-05-25 11:51:16',25,2,39,2,1,'当 代 网 文 ','管理员','互助侠720071'),(73,'2022-05-25 11:51:21',25,2,40,2,1,'楪祈在浴室生唱罪','管理员','互助侠720071');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_type`
--

DROP TABLE IF EXISTS `task_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_type`
--

LOCK TABLES `task_type` WRITE;
/*!40000 ALTER TABLE `task_type` DISABLE KEYS */;
INSERT INTO `task_type` VALUES (1,'新用户登录'),(2,'观看任务'),(3,'点赞任务'),(4,'收藏任务'),(5,'投币任务'),(6,'关注任务');
/*!40000 ALTER TABLE `task_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taskfilter`
--

DROP TABLE IF EXISTS `taskfilter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taskfilter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_email` varchar(100) NOT NULL COMMENT '用户id',
  `task_id` int NOT NULL COMMENT '任务id，只存储做完的任务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与任务的过滤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taskfilter`
--

LOCK TABLES `taskfilter` WRITE;
/*!40000 ALTER TABLE `taskfilter` DISABLE KEYS */;
/*!40000 ALTER TABLE `taskfilter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasklist`
--

DROP TABLE IF EXISTS `tasklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasklist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_type` int NOT NULL COMMENT '任务类型',
  `task_pay` smallint NOT NULL COMMENT '任务金额',
  `task_from` int NOT NULL COMMENT '任务创建者id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1开启任务，0关闭任务',
  `is_hidden` tinyint(1) DEFAULT '0' COMMENT '1隐藏，0显示',
  `task_desc` text COMMENT '任务描述',
  `task_url` text COMMENT '任务链接',
  `task_mid` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务号。不可重复',
  `task_img` text COMMENT '封面',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tasklist_un` (`task_mid`),
  KEY `tasklist_type_FK` (`task_type`),
  CONSTRAINT `tasklist_type_FK` FOREIGN KEY (`task_type`) REFERENCES `task_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasklist`
--

LOCK TABLES `tasklist` WRITE;
/*!40000 ALTER TABLE `tasklist` DISABLE KEYS */;
INSERT INTO `tasklist` VALUES (42,2,1,25,'2022-05-15 07:44:07','2022-05-15 07:44:07',1,0,'30秒教你做蒜蓉小龙虾','https://href.li/?https://www.bilibili.com/video/BV1fr4y1W79k','BV1fr4y1W79k','http://i0.hdslb.com/bfs/archive/892d2a001adf8b863a4259ae31eee19a54e9af5b.jpg'),(47,2,1,25,'2022-05-15 14:21:21','2022-05-15 14:21:21',1,0,'[悲鸣竞速]375秒欧皇瞎子+heaton(修罗+剑魂）','https://href.li/?https://www.bilibili.com/video/BV1NY4y1k7Hv','BV1NY4y1k7Hv','http://i0.hdslb.com/bfs/archive/1d6357f13c5a415d75e483899db958cd2dfda59f.jpg'),(50,2,1,2,'2022-05-30 09:49:19','2022-05-30 09:49:19',1,0,'水下平行宇宙挑战京剧《刀马旦》高燃踩点，演绎跨越时空的灵魂碰撞。','https://href.li/?https://www.bilibili.com/video/BV1454y1o7z8','BV1454y1o7z8','http://i2.hdslb.com/bfs/archive/720bd0d8579bb5fee6d715b9576f87dd06e37191.jpg'),(52,2,1,2,'2022-05-30 09:49:49','2022-05-30 09:49:49',1,0,'可达鸭算是被媳妇玩明白了','https://href.li/?https://www.bilibili.com/video/BV13U4y1y7gE','BV13U4y1y7gE','http://i1.hdslb.com/bfs/archive/50f3072808f16c111f7cb5891c01989d18492fd4.jpg'),(53,2,1,25,'2022-06-02 05:26:56','2022-06-02 05:26:56',1,0,'【它更强了！也更憨了。。。】','https://href.li/?https://www.bilibili.com/video/BV1yA4y1d72F','BV1yA4y1d72F','http://i1.hdslb.com/bfs/archive/0c5fa0b4cf6882f1869dd24666c70f4af69fdbcd.jpg'),(54,2,1,25,'2022-06-02 05:27:04','2022-06-02 05:27:04',1,0,'一支来自10年前的年度混剪','https://href.li/?https://www.bilibili.com/video/BV15W4y1C7BB','BV15W4y1C7BB','http://i1.hdslb.com/bfs/archive/34f044df26666d5abdcc8ef4a1713659fcfe994d.jpg'),(55,2,1,25,'2022-06-02 05:27:11','2022-06-02 05:27:11',1,0,'《中国风rap》你听过王维的相思吗？','https://href.li/?https://www.bilibili.com/video/BV13Y411u72b','BV13Y411u72b','http://i0.hdslb.com/bfs/archive/7b9bb9b8dfed0326c1dc8918acb2fb7c43e045ea.jpg'),(56,2,1,25,'2022-06-02 05:27:18','2022-06-02 05:27:18',1,0,'睫毛弯弯，刑期加啊加','https://href.li/?https://www.bilibili.com/video/BV1vU4y1y7q9','BV1vU4y1y7q9','http://i0.hdslb.com/bfs/archive/ff93f1b30fb6dade4de4d47a943f496053702450.jpg'),(57,2,1,25,'2022-06-02 05:27:27','2022-06-02 05:27:27',1,0,'全明星⚡外婆的澎湖湾⚡','https://href.li/?https://www.bilibili.com/video/BV1gv4y1w7dE','BV1gv4y1w7dE','http://i0.hdslb.com/bfs/archive/a6ca622a482f3e0f25431ace8b15c54d5c42ffa4.jpg'),(58,2,1,25,'2022-06-02 05:27:36','2022-06-02 05:27:36',1,0,'这就是我\"萌妹以求\"的生活','https://href.li/?https://www.bilibili.com/video/BV1JW4y1C7h2','BV1JW4y1C7h2','http://i2.hdslb.com/bfs/archive/aa9069629d5bdd3a830806efd67a6b188d4d9a4f.jpg'),(59,2,1,25,'2022-06-02 05:28:02','2022-06-02 05:28:02',1,0,'“这大概就是美到窒息的感觉吧”','https://href.li/?https://www.bilibili.com/video/BV13A4y1Z7m2','BV13A4y1Z7m2','http://i2.hdslb.com/bfs/archive/b57df59dc02232e0d4b0c35d439b530f167ecf59.jpg'),(60,2,1,25,'2022-06-02 05:28:08','2022-06-02 05:28:08',1,0,'【洗脑循环】阿尼亚又来给你洗脑啦~哇酷哇酷☆','https://href.li/?https://www.bilibili.com/video/BV1jv4y1P7Bb','BV1jv4y1P7Bb','http://i2.hdslb.com/bfs/archive/12a9ae34c254047fda7255e74129731cb85b407e.jpg');
/*!40000 ALTER TABLE `tasklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `passwd` varchar(100) NOT NULL,
  `code` varchar(100) DEFAULT NULL,
  `mycode` varchar(100) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'管理员','11@qq.com','123456',NULL,'016716','2022-04-27 03:12:07','2022-05-21 08:47:40'),(25,'互助侠720071','22@qq.com','123456',NULL,'063283','2022-04-27 10:25:21','2022-04-27 10:25:21'),(26,'互助侠988779','33@qq.com','123456',NULL,'767213','2022-04-27 10:38:06','2022-04-27 10:38:06'),(39,'互助侠270169','sss@qq.com','123456',NULL,'753724','2022-04-27 12:08:27','2022-04-27 12:08:27'),(40,'互助侠450496','123@qq.com','123456',NULL,'919573','2022-04-27 15:29:02','2022-04-27 15:29:02'),(41,'互助侠356476','123adssadsad123@qq.com','123456',NULL,'363967','2022-05-10 08:04:01','2022-05-10 08:04:01'),(42,'互助侠112838','ggg@gmail.com','123456',NULL,'891882','2022-05-17 08:36:46','2022-05-17 08:36:46'),(43,'互助侠409125','ddasd@qq.com','123456',NULL,'075218','2022-05-17 12:16:00','2022-05-17 12:16:00'),(44,'互助侠257879','123456789@qq.com','123456',NULL,'692180','2022-05-18 08:14:33','2022-05-18 08:14:33'),(45,'互助侠314265','w2sd@qq.com','123456',NULL,'393677','2022-05-20 08:04:01','2022-05-20 08:04:01'),(46,'互助侠521408','ghsg@qq.com','111222',NULL,'172381','2022-05-20 08:48:27','2022-05-21 07:42:51');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` int NOT NULL AUTO_INCREMENT,
  `money` smallint NOT NULL,
  `user_id` int NOT NULL COMMENT '用户id',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid_un` (`user_id`),
  CONSTRAINT `wallet_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
INSERT INTO `wallet` VALUES (6,49,2,'2022-05-25 11:51:21'),(18,49,25,'2022-05-25 11:51:21'),(19,20,26,'2022-04-27 10:38:06'),(32,20,39,'2022-04-27 12:08:27'),(33,20,40,'2022-04-27 15:29:02'),(34,7,41,'2022-05-12 06:57:23'),(35,20,42,'2022-05-17 08:36:46'),(36,20,43,'2022-05-17 12:16:00'),(37,20,44,'2022-05-18 08:14:33'),(38,20,45,'2022-05-20 08:04:01'),(39,20,46,'2022-05-20 08:48:27');
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-06 17:12:08
