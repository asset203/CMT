package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.XmlConverterElement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Create By: Radwa Osama
 * Date: 4/18/13, 7:25 AM
 */
public class XmlConverterElementRowMapper implements RowMapper<XmlConverterElement> {

  @Override
  public XmlConverterElement mapRow(ResultSet resultSet, int i) throws SQLException {

    return new XmlConverterElement(resultSet.getString("NAME"),
      resultSet.getInt("TYPE_CODE"));
  }
}
