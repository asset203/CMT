package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.XmlConverterRowMapper;
import eg.com.vodafone.model.XmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Create By: Radwa Osama
 * Date: 4/17/13, 9:25 PM
 */
@Repository
public class XmlConverterDao {

  private final static Logger logger = LoggerFactory.getLogger(XmlConverterDao.class);

  private final JdbcTemplate simpleJdbcTemplate;

  @Autowired
  public XmlConverterDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
    simpleJdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<XmlConverter> listXmlConverterByVendorId(long vendorId) {

    String query = "SELECT * FROM XML_CONVERTORS_LKP WHERE VENDOR = ?";

    return simpleJdbcTemplate.query(query, new Object[]{vendorId}, new XmlConverterRowMapper());
  }

}
