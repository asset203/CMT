package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.SystemEventRowMapper;
import eg.com.vodafone.model.SystemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static eg.com.vodafone.dao.DaoConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/18/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SystemEventDao {


  private final JdbcTemplate jdbcTemplate;
  private final static Logger logger = LoggerFactory.getLogger(SystemEventDao.class);

  private static final SimpleDateFormat simpleDateFormat =
    new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

  @Autowired
  public SystemEventDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public int saveEvent(SystemEvent systemEvent) {
    return jdbcTemplate.update("INSERT INTO LO_EVENTS_LEVELS_DESC (ID, DATE_TIME, LEVEL_TYPE, SYSTEM_NAME,  " +
      "COMMENT_DESC) VALUES(SEQ_LO_EVENTS_LEVELS_DESC.NEXTVAL, ?, ?, ?, ?)",
      systemEvent.getDateTime(), systemEvent.getLevelType().getLevel().toUpperCase(),
      systemEvent.getSystemName(), systemEvent.getCommentDesc());
  }

  public int updateEvent(SystemEvent updatedSystemEvent) {
    return jdbcTemplate.update("UPDATE LO_EVENTS_LEVELS_DESC SET DATE_TIME = ?, LEVEL_TYPE = ?,  " +
      "SYSTEM_NAME = ?, COMMENT_DESC = ? WHERE ID = ?",
      updatedSystemEvent.getDateTime(), updatedSystemEvent.getLevelType().getLevel().toUpperCase(),
      updatedSystemEvent.getSystemName(), updatedSystemEvent.getCommentDesc(),
      updatedSystemEvent.getId());
  }


  public int deleteEvent(int systemEventID) {
    return jdbcTemplate.update("DELETE FROM LO_EVENTS_LEVELS_DESC WHERE ID = ?", systemEventID);
  }


  public List<SystemEvent> findSystemEvents(String systemName) {
    return jdbcTemplate.query("SELECT * FROM LO_EVENTS_LEVELS_DESC WHERE SYSTEM_NAME = ?",
      new Object[]{systemName}, new SystemEventRowMapper());
  }


  public SystemEvent findSystemEvent(String systemName, Date eventDate) {
    SystemEvent systemEvent = null;
    try {
      systemEvent = jdbcTemplate.queryForObject("" +
        "SELECT * FROM LO_EVENTS_LEVELS_DESC WHERE SYSTEM_NAME = ? AND DATE_TIME = ?",
        new Object[]{systemName, simpleDateFormat.format(eventDate)},
        new SystemEventRowMapper());

    } catch (EmptyResultDataAccessException exception) {
      logger.error("No result returned {} ", exception.getMessage());
      return null;
    }
    return systemEvent;
  }

  public boolean isSystemExist(String systemName) {
    boolean systemExist = false;
    if (null != systemName) {
      StringBuffer query = new StringBuffer(" SELECT COUNT (*) from ").append(SYSTEMS_TABLE_NAME).append(WHERE)
        .append(UPPER_FUNCTION).append("(").append(NAME_COLUMN).append(")").append(LIKE).append(" '").append(systemName.toUpperCase()).append("'");

      int count = jdbcTemplate.queryForInt(query.toString());
      systemExist = (count > 0);
    }


    return systemExist;

  }
}

