DROP DATABASE IF EXISTS ggukgguk;
CREATE DATABASE ggukgguk;
USE ggukgguk;

CREATE TABLE `member_tb` (
    `member_id`          varchar(20)             NOT NULL  PRIMARY KEY,
    `member_pw`          varchar(128)            NOT NULL,
    `member_name`        varchar(20)             NOT NULL,
    `member_birth`       date                    NOT NULL,
    `member_nickname`    varchar(20)             NOT NULL,
    `member_email`       varchar(128)            NOT NULL,
    `member_type`        char(1)                 NOT NULL,
    `member_comment`        varchar(512)            NULL,
    `member_created_at`  datetime   DEFAULT now()  NULL
);
INSERT INTO `member_tb` VALUES ('hong','1234','홍길동','2000-01-01', '길동짱짱맨','hong@tales.com', 'M', '테스트용 계정입니다.', default);