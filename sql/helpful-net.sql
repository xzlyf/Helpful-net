-- `helpful-net`.notice definition

CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text,
  `isTop` tinyint(1) DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `helpful-net`.`order` definition

CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `task_id` int NOT NULL COMMENT '任务id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatet_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';


-- `helpful-net`.task_type definition

CREATE TABLE `task_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务类型';


-- `helpful-net`.taskfilter definition

CREATE TABLE `taskfilter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `task_id` int NOT NULL COMMENT '任务id，只存储做完的任务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与任务的过滤表';


-- `helpful-net`.`user` definition

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
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- `helpful-net`.tasklist definition

CREATE TABLE `tasklist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_type` int NOT NULL COMMENT '任务类型',
  `task_pay` smallint NOT NULL COMMENT '任务金额',
  `task_from` int NOT NULL COMMENT '任务创建者id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `wallet_id` int NOT NULL COMMENT '任务创建者钱包id',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1开启任务，0关闭任务',
  `is_hidden` tinyint(1) DEFAULT '0' COMMENT '1隐藏，0显示',
  `task_desc` text COMMENT '任务描述',
  `task_url` text COMMENT '任务链接',
  `task_mid` text COMMENT '任务号',
  PRIMARY KEY (`id`),
  KEY `tasklist_type_FK` (`task_type`),
  CONSTRAINT `tasklist_type_FK` FOREIGN KEY (`task_type`) REFERENCES `task_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务列表';


-- `helpful-net`.wallet definition

CREATE TABLE `wallet` (
  `id` int NOT NULL AUTO_INCREMENT,
  `money` smallint NOT NULL,
  `user_id` int NOT NULL COMMENT '用户id',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid_un` (`user_id`),
  CONSTRAINT `wallet_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='钱包表';