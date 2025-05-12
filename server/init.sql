-- DROP DATABASE IF EXISTS `friendsync`;

-- CREATE DATABASE `friendsync`;

-- friendsync.user definition

CREATE TABLE
    `user` (
        `id` bigint NOT NULL AUTO_INCREMENT,
        `username` varchar(100) DEFAULT NULL,
        `userAccount` varchar(100) DEFAULT NULL,
        `userPassword` varchar(100) DEFAULT NULL,
        `avatarUrl` varchar(100) DEFAULT NULL,
        `gender` tinyint DEFAULT NULL,
        `profile` varchar(100) DEFAULT NULL,
        `phone` varchar(100) DEFAULT NULL,
        `email` varchar(100) DEFAULT NULL,
        `userStatus` varchar(100) DEFAULT NULL,
        `createTime` timestamp NULL DEFAULT NULL,
        `updateTime` timestamp NULL DEFAULT NULL,
        `isDelete` tinyint DEFAULT NULL,
        `userRole` int DEFAULT NULL,
        `planetCode` varchar(100) DEFAULT NULL,
        `tags` varchar(100) DEFAULT NULL,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- friendsync.conversation definition

CREATE TABLE `conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` enum('CHAT','TEAM','ROOM') DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  `information` varchar(100) DEFAULT NULL,
  `license` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- friendsync.user_conversation definition

CREATE TABLE `user_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `conversation_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- friendsync.chat_messages definition

CREATE TABLE `chat_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conversation_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT NULL,
  `msg_content` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;