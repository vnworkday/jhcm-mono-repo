package io.github.ntduycs.jhcm.base.domain.handler;

import io.github.ntduycs.jhcm.base.domain.enums.ChangeStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ChangeStatus.class)
public class ChangeStatusTypeHandler extends BaseTypeHandler<ChangeStatus> {
  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, ChangeStatus parameter, JdbcType jdbcType) throws SQLException {
    if (parameter != null) {
      ps.setString(i, parameter.getCode());
    } else {
      ps.setNull(i, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public ChangeStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return rs.wasNull() ? null : ChangeStatus.fromCode(value);
  }

  @Override
  public ChangeStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return rs.wasNull() ? null : ChangeStatus.fromCode(value);
  }

  @Override
  public ChangeStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return cs.wasNull() ? null : ChangeStatus.fromCode(value);
  }
}
