CREATE TABLE `title`
(
  `id`          INT AUTO_INCREMENT,
  `code`        VARCHAR(16),
  `name`        VARCHAR(255),
  `description` VARCHAR(1024) DEFAULT '',
  `status`      VARCHAR(1)    DEFAULT 'A' COMMENT 'A=Active, I=Inactive',
  `created_at`  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by`  INT,
  `updated_by`  INT,
  `version`     INT           DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `title_name_unique` UNIQUE (`name`)
);

ALTER TABLE `user`
  ADD COLUMN `title_id` INT;