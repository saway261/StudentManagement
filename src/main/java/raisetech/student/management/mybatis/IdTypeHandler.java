package raisetech.student.management.mybatis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import raisetech.student.management.data.Id;

public class IdTypeHandler extends BaseTypeHandler<Id> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Id parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setInt(i, parameter.getValue());
  }

  @Override
  public Id getNullableResult(ResultSet rs, String columnName) throws SQLException {
    int value = rs.getInt(columnName);
    return rs.wasNull() ? null : new Id(value);
  }

  @Override
  public Id getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    int value = rs.getInt(columnIndex);
    return rs.wasNull() ? null : new Id(value);
  }

  @Override
  public Id getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
    int value = cs.getInt(columnIndex);
    return cs.wasNull() ? null : new Id(value);
  }
}

