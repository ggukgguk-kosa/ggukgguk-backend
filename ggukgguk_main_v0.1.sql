DROP DATABASE IF EXISTS ggukgguk;
CREATE DATABASE ggukgguk;
USE ggukgguk;

CREATE TABLE `member` (
    `member_id`    VARCHAR(16)    NOT NULL    PRIMARY KEY,
    `member_pw`    VARCHAR(128)    NOT NULL,
    `member_name`    VARCHAR(16)    NOT NULL,
    `member_nickname`    VARCHAR(16)    NOT NULL,
    `member_email`    VARCHAR(128)    NOT NULL,
    `member_phone`    VARCHAR(12)    NOT NULL,
    `member_birth`    DATE    NOT NULL,
    `member_created_at`    DATETIME    NOT NULL    DEFAULT NOW(),
    `member_activated`    BOOLEAN    NOT NULL    DEFAULT TRUE    COMMENT '탈퇴처리 시 아이디를 제외하고 공백 지정 후 본 컬럼 true 업데이트',
    `member_is_admin`    BOOLEAN    NOT NULL    DEFAULT FALSE    COMMENT '관리자이면 true, 아니면 false'
);

INSERT INTO `member` VALUES('hong', '$2y$04$G92ppy9s0BVNuuqbLjo.k.4M.EiVMOId0Dm2hYUJgJe13a.pa0lzS',
								'홍길동', '길동길동', 'hong@tales.org', '01012341111', '1443-01-01',
								DEFAULT, DEFAULT, DEFAULT);
INSERT INTO `member` VALUES('admin', '$2y$04$G92ppy9s0BVNuuqbLjo.k.4M.EiVMOId0Dm2hYUJgJe13a.pa0lzS',
								'관리자', '관리자', 'admin@ggukgguk.online', '01012341111', '1998-04-08',
								DEFAULT, DEFAULT, TRUE);

CREATE TABLE `record` (
    `record_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `member_id`    VARCHAR(16)    NOT NULL,
    `record_comment`    VARCHAR(512)    NOT NULL,
    `record_created_at`    DATETIME    NOT NULL    DEFAULT NOW(),
    `media_file_id`    CHAR(36)    NULL    COMMENT 'UUID',
    `record_location_y`    FLOAT    NULL,
    `record_location_x`    FLOAT    NULL
);

CREATE TABLE `reply` (
    `reply_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `reply_content`    VARCHAR(256)    NOT NULL,
    `reply_date`    DATETIME    NOT NULL,
    `record_id`    INT    NOT NULL,
    `member_id`    VARCHAR(16)    NOT NULL,
    FOREIGN KEY (`record_id`) REFERENCES `record` (`record_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

CREATE TABLE `media_type` (
    `media_type_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `media_type_value`    CHAR(8)    NOT NULL    COMMENT 'video, image, audio',
    `media_type_comment`    varchar(36)    NULL
);

INSERT INTO `media_type` VALUES (1, 'video', '동영상 타입');
INSERT INTO `media_type` VALUES (2, 'image', '이미지 타입');
INSERT INTO `media_type` VALUES (3, 'audio', '오디오 타입');

CREATE TABLE `media_file` (
    `media_file_id`    CHAR(36)    NOT NULL    PRIMARY KEY    COMMENT 'UUID',
    `media_type_id`    INT    NOT NULL,
    `media_file_processed`    BOOLEAN NOT NULL DEFAULT FALSE,
     FOREIGN KEY (`media_type_id`) REFERENCES `media_type` (`media_type_id`)
);

CREATE TABLE `diary` (
    `diary_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `member_id`    VARCHAR(16)    NOT NULL,
    `diary_year`    CHAR(4)    NOT NULL,
    `diary_month`    CHAR(2)    NOT NULL,
    `diary_color`    CHAR(8)    NOT NULL,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

CREATE TABLE `diary_keyword` (
    `diary_keyword_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `diary_id`    INT    NOT NULL,
    `diary_keyword`    VARCHAR(16)    NOT NULL,
    FOREIGN KEY (`diary_id`) REFERENCES `diary` (`diary_id`)
);

CREATE TABLE `received_record` (
    `received_record_id`    INT    NOT NULL    AUTO_INCREMENT,
    `receiver_id`    VARCHAR(16)    NOT NULL,
    `received_record_accepted`    BOOLEAN    NOT NULL    DEFAULT FALSE,
    PRIMARY KEY (`received_record_id`, `receiver_id`),
    FOREIGN KEY (`received_record_id`) REFERENCES `record` (`record_id`),
    FOREIGN KEY (`receiver_id`) REFERENCES `member` (`member_id`)
);

CREATE TABLE `notification_type` (
    `notification_type_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `notification_type_value`    VARCHAR(16)    NOT NULL    COMMENT 'friend_new, friend_birthday, diary, record_reply, record_friend',
    `notification_type_comment`    VARCHAR(36)    NULL
);
INSERT INTO `notification_type` VALUES (1, 'friend_new', '친구 신청 알림');
INSERT INTO `notification_type` VALUES (2, 'friend_birthday', '친구 생일 알림');
INSERT INTO `notification_type` VALUES (3, 'diary', '월말결산 완료 알림');
INSERT INTO `notification_type` VALUES (4, 'record_reply', '댓글 등록 알림');
INSERT INTO `notification_type` VALUES (5, 'record_friend', '교환일기 등록 알림');

CREATE TABLE `notification` (
    `notification_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `receiver_id`    VARCHAR(16)    NOT NULL,
    `notification_type_id`    INT    NOT NULL,
    `notification_created_at`    DATETIME    NOT NULL    DEFAULT NOW(),
    `reference_id`    INT    NOT NULL,
    FOREIGN KEY (`receiver_id`) REFERENCES `member` (`member_id`),
    FOREIGN KEY (`notification_type_id`) REFERENCES `notification_type` (`notification_type_id`)
);

CREATE TABLE `notice` (
    `notice_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `notice_titile`    VARCHAR(32)    NOT NULL,
    `notice_content`    VARCHAR(512)    NOT NULL,
    `notice_created_at`    DATETIME    NOT NULL    DEFAULT NOW()
);

CREATE TABLE `batch_type` (
    `batch_type_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `batch_type_value`    VARCHAR(16)    NOT NULL,
    `batch_type_comment`    VARCHAR(36)    NULL
);
INSERT INTO `batch_type` VALUES (1, 'review_month', '월말 결산 배치작업');
INSERT INTO `batch_type` VALUES (2, 'detect_safe', '유해 콘텐츠 필터링 배치작업');
INSERT INTO `batch_type` VALUES (3, 'notify_birthday', '친구 생일 알림 배치작업');
INSERT INTO `batch_type` VALUES (4, 'suggest_memory', '추억 제안 배치작업');

CREATE TABLE `batch_status` (
    `batch_status_id`    INT    NOT NULL    PRIMARY KEY    AUTO_INCREMENT,
    `batch_status`    ENUM('done', 'running', 'error')    NOT NULL,
    `batch_status_created_at`    DATETIME    NOT NULL    DEFAULT NOW(),
    `batch_type_id`    INT    NOT NULL,
    FOREIGN KEY (`batch_type_id`) REFERENCES `batch_type` (`batch_type_id`)
);