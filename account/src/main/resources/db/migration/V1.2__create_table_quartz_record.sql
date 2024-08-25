CREATE TABLE `quartz_record`
(
  `fire_instance_id`       VARCHAR(127),
  `job_name`               VARCHAR(255),
  `job_group`              VARCHAR(127),
  `job_data_map`           BLOB,
  `job_retry_max`          INT,
  `job_run_time`           BIGINT,
  `job_result`             BLOB,
  `trigger_name`           VARCHAR(127),
  `trigger_priority`       INT,
  `trigger_may_fire_again` BOOL,
  `trigger_state`          VARCHAR(15),
  `fire_time`              TIMESTAMP,
  `scheduled_fire_time`    TIMESTAMP,
  `complete_instruction`   VARCHAR(127),
  `created_at`             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at`             TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`fire_instance_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;