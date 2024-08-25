package io.github.ntduycs.jhcm.base.domain.handler;

import io.github.ntduycs.jhcm.base.domain.enums.Status;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Status.class)
public class StatusTypeHandler extends BaseTypeHandler<Status> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Status parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter != null) {
      ps.setString(i, parameter.getCode());
    } else {
      ps.setNull(i, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public Status getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return rs.wasNull() ? null : Status.fromCode(value);
  }

  @Override
  public Status getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return rs.wasNull() ? null : Status.fromCode(value);
  }

  @Override
  public Status getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return cs.wasNull() ? null : Status.fromCode(value);
  }
}
