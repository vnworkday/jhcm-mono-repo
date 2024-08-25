CREATE TABLE `sequence`
(
  `name`  VARCHAR(128),
  `value` INT DEFAULT 0,
  PRIMARY KEY (`name`)
);

CREATE ALIAS generate_user_code AS
$$
  int get_next_sequence_value(String name) {
    int randomNumber;
    String url = "jdbc:h2:mem:j_account;DB_CLOSE_DELAY=-1;MODE=MySQL";
    String user = "sa";
    String password = "sa";
    java.sql.Connection conn = null;
    java.sql.PreparedStatement checkAndUpdateStmt = null;
    java.sql.PreparedStatement selectStmt = null;
    java.sql.ResultSet rs = null;
    try {
      conn = java.sql.DriverManager.getConnection(url, user, password);

// Attempt to update the sequence value, inserting if not exists
                checkAndUpdateStmt = conn.prepareStatement(
                "MERGE INTO sequence (name, value) " +
                "KEY (name) " +
                "VALUES (?, COALESCE((SELECT value FROM sequence WHERE name = ?) + 1, 1))"
                );
checkAndUpdateStmt.setString(1, "user_code");
      checkAndUpdateStmt.setString(2, "user_code");
      checkAndUpdateStmt.executeUpdate();

// Retrieve the updated sequence value
      selectStmt = conn.prepareStatement(
        "SELECT value FROM sequence WHERE name = ?");
      selectStmt.setString(1, "user_code");
      rs = selectStmt.executeQuery();
      if (rs.next()) {
        randomNumber = rs.getInt(1);
} else {
        throw new java.sql.SQLException("Failed to retrieve sequence value for: user_code");
}
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to generate user code", e);
} finally {
      try {
        if (rs != null) rs.close();
        if (selectStmt != null) selectStmt.close();
        if (checkAndUpdateStmt != null) checkAndUpdateStmt.close();
        if (conn != null) conn.close();
} catch (java.sql.SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to close resources", e);
}
    }
    return randomNumber;
}
$$;

CREATE TABLE `user`
(
  id            INT AUTO_INCREMENT,
  code          VARCHAR(16),
  cif_number    VARCHAR(8),
  username      VARCHAR(255),
  email         VARCHAR(255),
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  full_name     VARCHAR(255) GENERATED ALWAYS AS (TRIM(CONCAT(first_name, ' ', last_name))),
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
);