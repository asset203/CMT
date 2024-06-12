package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.UserRowMapper;
import eg.com.vodafone.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/6/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao {
  private final static Logger logger = LoggerFactory.getLogger(UserDao.class);
  private final JdbcTemplate jdbcTemplate;


  @Autowired
  public UserDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<Integer> saveUser(User user) {
    List<Integer> resultList = new ArrayList<Integer>();
    resultList.add(jdbcTemplate.update("INSERT INTO USERS (ID, USERNAME, PASSWORD,  EMAIL, FIRST_LOGIN, MOBILE , APPS_TO_ACCESS ) VALUES(USER_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?,?)",
      new Object[]{user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstLogin(), user.getMobile() , user.getAppsToAccess()}));
    resultList.add(jdbcTemplate.update("INSERT INTO AUTHORITIES (AUTHORITY, USER_ID) VALUES(?, USER_ID_SEQ.CURRVAL)",
      new Object[]{user.getAuthority()}));
    return resultList;
  }


  public List<Integer> updateUser(User user) {
    List<Integer> resultList = new ArrayList<Integer>();
    resultList.add(jdbcTemplate.update("UPDATE AUTHORITIES SET AUTHORITY = ? WHERE USER_ID = ?",
      new Object[]{user.getAuthority(), user.getId()}));
    resultList.add(jdbcTemplate.update("UPDATE USERS SET USERNAME = ?, PASSWORD = ?,  EMAIL = ?, FIRST_LOGIN = ?, MOBILE = ? , APPS_TO_ACCESS = ?  WHERE ID = ?",
      new Object[]{user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstLogin(), user.getMobile(), user.getAppsToAccess()  , user.getId()}));

    return resultList;
  }


  public int deleteUser(User user) {
    return jdbcTemplate.update("DELETE FROM USERS WHERE ID = ?",
      new Object[]{user.getId()});
  }

  public List<User> findAllUsers() {
    return jdbcTemplate.query("SELECT U.USERNAME, PASSWORD,  EMAIL, ID, FIRST_LOGIN, MOBILE , APPS_TO_ACCESS ,AUTHORITY FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID",
      new UserRowMapper());
  }

  public User findUserByUsernameAndPassword(String username, String password) {
    User user;
    try {
      user = jdbcTemplate
        .queryForObject("SELECT  U.USERNAME, PASSWORD,  EMAIL, ID, FIRST_LOGIN, MOBILE, APPS_TO_ACCESS, AUTHORITY FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID AND U.USERNAME = ? AND PASSWORD= ?",
          new Object[]{username, password},
          new UserRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("No Result returned", exception);
      user = null;
    }


    return user;
  }

  public User findUserById(int userId) {
    User user;
    try {
      user = jdbcTemplate
        .queryForObject("SELECT  U.USERNAME, PASSWORD,  EMAIL, ID, FIRST_LOGIN, MOBILE,APPS_TO_ACCESS, AUTHORITY FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID AND U.ID = ?",
          new Object[]{userId},
          new UserRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("No Result returned", exception);
      user = null;
    }
    return user;
  }

  public User findUserByUsername(String username) {
    User user;
    try {
      user = jdbcTemplate
        .queryForObject("SELECT  U.USERNAME, PASSWORD,  EMAIL, ID, FIRST_LOGIN, MOBILE,APPS_TO_ACCESS, AUTHORITY FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID AND U.USERNAME = ?",
          new Object[]{username},
          new UserRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("No Result returned", exception);
      user = null;
    }
    return user;
  }

  public User findUserByMobile(String mobile) {
    User user = null;
    try {
      user = jdbcTemplate
        .queryForObject("SELECT  U.USERNAME, U.PASSWORD,  U.EMAIL, U.ID, U.FIRST_LOGIN, U.MOBILE, U.APPS_TO_ACCESS, AUTH.AUTHORITY " +
          "FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID AND U.MOBILE = ? ",
          new Object[]{mobile},
          new UserRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("No Result returned", exception);
      user = null;
    }
    return user;
  }

  public String getUserRole(String userName) {
    String userRole = null;
    try {
      userRole = jdbcTemplate
        .queryForObject("SELECT AUTH.AUTHORITY FROM USERS U, AUTHORITIES AUTH WHERE U.ID = AUTH.USER_ID AND U.USERNAME = ? ",
          new Object[]{userName},
          String.class);
    } catch (EmptyResultDataAccessException exception) {
      logger.error("No Result returned", exception);
      userRole = null;
    }
    return userRole;
  }
}