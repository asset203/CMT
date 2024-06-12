package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.XmlConverter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Create By: Radwa Osama
 * Date: 4/17/13, 9:31 PM
 */
public class XmlConverterRowMapper implements RowMapper<XmlConverter> {

  @Override
  public XmlConverter mapRow(ResultSet resultSet, int i) throws SQLException {
    return new XmlConverter(
      resultSet.getLong("ID"),
      resultSet.getString("CONVERTOR_NAME")
    );
  }
}