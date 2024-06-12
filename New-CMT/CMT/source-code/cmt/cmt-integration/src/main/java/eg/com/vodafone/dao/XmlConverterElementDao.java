package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.XmlConverterElementRowMapper;
import eg.com.vodafone.model.XmlConverterElement;
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
 * Date: 4/18/13, 7:23 AM
 */
@Repository
public class XmlConverterElementDao {

  private final static Logger logger = LoggerFactory.getLogger(XmlConverterDao.class);

   private final JdbcTemplate simpleJdbcTemplate;

   @Autowired
   public XmlConverterElementDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
     simpleJdbcTemplate = new JdbcTemplate(dataSource);
   }

  public List<XmlConverterElement> listXmlConverterElementByConverterId(long converterId) {

    String query = "SELECT * FROM XML_CONVERTER_ELEMENT WHERE CONVERTER_ID = ? ORDER BY RANK";

    return simpleJdbcTemplate.query(query, new Object[]{converterId}, new XmlConverterElementRowMapper());
  }
}
