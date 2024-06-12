package eg.com.vodafone.dao;

import eg.com.vodafone.model.XmlVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Create By: Radwa Osama
 * Date: 4/17/13, 9:18 PM
 */
@Repository
public class XmlVendorDao {

  private final static Logger logger = LoggerFactory.getLogger(XmlVendorDao.class);

  private final JdbcTemplate simpleJdbcTemplate;

  @Autowired
  public XmlVendorDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
    simpleJdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<XmlVendor> listXmlVendors() {
    return simpleJdbcTemplate.query("SELECT * FROM XML_VENDORS_LKP",
      new RowMapper<XmlVendor>() {
        @Override
        public XmlVendor mapRow(ResultSet resultSet, int i) throws SQLException {
          return new XmlVendor(
            resultSet.getLong("VENDOR_ID"),
            resultSet.getString("VENDOR_NAME")
          );
        }
      });
  }
}

