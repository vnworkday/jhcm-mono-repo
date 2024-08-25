CREATE TABLE `sequence`
(
  `name`  VARCHAR(128),
  `value` INT DEFAULT 0,
  PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Function to get next value
DELIMITER $$

CREATE FUNCTION get_next_sequence_value(seqName VARCHAR(50))
  RETURNS INT
  UNSIGNED
  NOT DETERMINISTIC
  MODIFIES SQL DATA
BEGIN
  DECLARE next_val INT;
  IF NOT EXISTS (SELECT * FROM `sequence` WHERE `name` = seqName) THEN
    INSERT INTO `sequence` (`name`, `value`) VALUES (seqName, 0);
END IF;
UPDATE `sequence`
SET `value` = `value` + 1
WHERE `name` = seqName;
SELECT `value`
INTO next_val
FROM `sequence`
WHERE `name` = seqName
  LIMIT 1;
RETURN next_val;
END$$

DELIMITER ;

CREATE TABLE `user`
(
  id            INT AUTO_INCREMENT,
  code          VARCHAR(16),
  cif_number    VARCHAR(8),
  username      VARCHAR(255),
  email         VARCHAR(255),
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  full_name     VARCHAR(255) GENERATED ALWAYS AS (TRIM(CONCAT(first_name, ' ', last_name))) STORED,
  status        VARCHAR(1) DEFAULT 'A' COMMENT 'A=Active, I=Inactive',
  created_at    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by    INT        DEFAULT 0 COMMENT '0=System',
  updated_by    INT        DEFAULT 0 COMMENT '0=System',
  version       INT        DEFAULT 0,
  change_status VARCHAR(1) DEFAULT 'A' COMMENT 'W=Waiting, A=Approved, R=Rejected',
  PRIMARY KEY (id),
  CONSTRAINT `user_code_unique` UNIQUE (code),
  CONSTRAINT `user_username_unique` UNIQUE (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;