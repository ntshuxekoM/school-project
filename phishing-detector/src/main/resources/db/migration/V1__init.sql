CREATE TABLE `app_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `create_user_id` bigint DEFAULT NULL,
  `last_update_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3o1mwxh51k403bxcxc37r1cik` (`create_user_id`),
  KEY `FKhwoelj7lfkrh4ru6wj0h8ym72` (`last_update_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `black_list_site` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `site_name` varchar(120) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `create_user_id` bigint DEFAULT NULL,
  `last_update_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoqtr2qqejhjv9j2o05cnxeitu` (`url`),
  KEY `FK9of56kenggmlh7pkbdoqgqf5o` (`create_user_id`),
  KEY `FKhyk981kdvs7yjfrthgshamd20` (`last_update_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `email_content` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `cc_email` varchar(255) DEFAULT NULL,
  `content_mssg` varchar(5000) DEFAULT NULL,
  `error_message` varchar(5000) DEFAULT NULL,
  `from_email` varchar(255) DEFAULT NULL,
  `retry_count` int DEFAULT NULL,
  `run_date` datetime DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `to_email` varchar(255) DEFAULT NULL,
  `create_user_id` bigint DEFAULT NULL,
  `last_update_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1rbnlt79sqfjx2da21i4neikj` (`create_user_id`),
  KEY `FKl4vc9p19pwrtfyiybrc2cbulf` (`last_update_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `create_user_id` bigint DEFAULT NULL,
  `last_update_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm45ibgs8e9t1w7ljx2fursd94` (`create_user_id`),
  KEY `FKn860rs4owm3qnfmaibp2x4fcx` (`last_update_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- backend_db.`user` definition

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `cell_number` varchar(255) NOT NULL,
  `email` varchar(250) DEFAULT NULL,
  `enabled` bit(1) DEFAULT b'1',
  `id_number` varchar(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(120) DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  `create_user_id` bigint DEFAULT NULL,
  `last_update_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  KEY `FKn1eo3hcou22xdhm9vmll8oivg` (`create_user_id`),
  KEY `FKqdcdhuvbdp4a95f0bxvnugclw` (`last_update_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- backend_db.user_roles definition

CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKrhfovtciq1l558cw6udg0h0d3` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;