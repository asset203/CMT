package eg.com.vodafone.dao;

import eg.com.vodafone.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/19/13
 * Time: 10:38 PM
 */
@Repository
public class DCLogNotificationDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(DCLogNotificationDao.class);
    private final static String QUERY_ADD_NOTIFY_LIST = "INSERT INTO DC_LOG_NOTIFICATION_LIST " +
            "(ID,SYSTEM_NAME,DC_LOG_ERROR_CODES_ID,LOG_TYPE) VALUES ("
            + "SEQ_DC_LOG_NOTIFICATION_LIST.NEXTVAL, ?, ?, ?)";
    private final static String QUERY_ERROR_CODE_BY_ID = "SELECT * FROM DC_LOG_ERROR_CODES WHERE ID = ?";
    private final static String QUERY_ERROR_CODES = "SELECT * FROM DC_LOG_ERROR_CODES ORDER BY ERROR_CODE ASC";
    private final static String QUERY_UPDATE_CONTACTS = "INSERT INTO DC_LOG_CONTACTS (" +
            "ID,EXTERNAL_CONTACTS_ID,REGISTERED_CONTACT_ID,NOTIFICATION_TYPE,IS_EXISTING_USER, NOTIFICATION_LIST_ID) VALUES ("
            + "SEQ_DC_LOG_CONTACTS_PK.NEXTVAL, ?, ?, ?, ?, ?)";
    private final static String QUERY_DELETE_CONTACT = "DELETE DC_LOG_CONTACTS WHERE ID = ?";
    private static final String QUERY_NOTIFY_LIST =
            "SELECT DC_LOG_NOTIFICATION_LIST.ID, DC_LOG_NOTIFICATION_LIST.SYSTEM_NAME, DC_LOG_NOTIFICATION_LIST.LOG_TYPE, "
                    + "DC_LOG_NOTIFICATION_LIST.DC_LOG_ERROR_CODES_ID, DC_LOG_ERROR_CODES.ERROR_CODE, " +
                    "DC_LOG_ERROR_CODES.SHORT_DESC, DC_LOG_CONTACTS.ID as CONTACT_ID, "
                    + "DC_LOG_CONTACTS.IS_EXISTING_USER, DC_LOG_CONTACTS.NOTIFICATION_TYPE, DC_LOG_CONTACTS.NOTIFICATION_LIST_ID, "
                    + "USERS.ID AS USER_ID, USERS.USERNAME AS USER_USERNAME, "
                    + "USERS.EMAIL AS USER_EMAIL, USERS.MOBILE, DC_LOG_EXTERNAL_CONTACTS.ID AS EXT_ID, "
                    + "DC_LOG_EXTERNAL_CONTACTS.USERNAME AS EXT_USERNAME, "
                    + "DC_LOG_EXTERNAL_CONTACTS.EMAIL AS EXT_EMAIL, DC_LOG_EXTERNAL_CONTACTS.MOBILE_NUMBER "
                    + "FROM DC_LOG_NOTIFICATION_LIST "
                    + "LEFT OUTER JOIN DC_LOG_CONTACTS ON DC_LOG_NOTIFICATION_LIST.ID = DC_LOG_CONTACTS.NOTIFICATION_LIST_ID "
                    + "LEFT OUTER JOIN DC_LOG_ERROR_CODES ON DC_LOG_NOTIFICATION_LIST.DC_LOG_ERROR_CODES_ID = DC_LOG_ERROR_CODES.ID "
                    + "LEFT OUTER JOIN USERS ON DC_LOG_CONTACTS.REGISTERED_CONTACT_ID = USERS.ID "
                    + "LEFT OUTER JOIN DC_LOG_EXTERNAL_CONTACTS ON DC_LOG_CONTACTS.EXTERNAL_CONTACTS_ID = DC_LOG_EXTERNAL_CONTACTS.ID ";
    private static final String QUERY_CONTACT_LIST =
            "SELECT DC_LOG_CONTACTS.ID as CONTACT_ID, "
                    + "DC_LOG_CONTACTS.IS_EXISTING_USER, DC_LOG_CONTACTS.NOTIFICATION_TYPE, DC_LOG_CONTACTS.NOTIFICATION_LIST_ID, "
                    + "USERS.ID AS USER_ID, USERS.USERNAME AS USER_USERNAME, "
                    + "USERS.EMAIL AS USER_EMAIL, USERS.MOBILE, DC_LOG_EXTERNAL_CONTACTS.ID AS EXT_ID, "
                    + "DC_LOG_EXTERNAL_CONTACTS.USERNAME AS EXT_USERNAME, "
                    + "DC_LOG_EXTERNAL_CONTACTS.EMAIL AS EXT_EMAIL, DC_LOG_EXTERNAL_CONTACTS.MOBILE_NUMBER "
                    + "FROM DC_LOG_CONTACTS "
                    + "LEFT OUTER JOIN USERS ON DC_LOG_CONTACTS.REGISTERED_CONTACT_ID = USERS.ID "
                    + "LEFT OUTER JOIN DC_LOG_EXTERNAL_CONTACTS ON DC_LOG_CONTACTS.EXTERNAL_CONTACTS_ID = DC_LOG_EXTERNAL_CONTACTS.ID "
                    + "WHERE  DC_LOG_CONTACTS.ID = ? ";
    private final JdbcTemplate SIMPLE_JDBC_TEMPLATE;


    @Autowired
    public DCLogNotificationDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        SIMPLE_JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }

    public List<DCLogErrorCode> getAllDCLogErrorCodes() {
        return SIMPLE_JDBC_TEMPLATE.query(QUERY_ERROR_CODES,
                new RowMapper<DCLogErrorCode>() {
                    @Override
                    public DCLogErrorCode mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new DCLogErrorCode(
                                resultSet.getInt("ID"),
                                resultSet.getString("ERROR_CODE"),
                                resultSet.getString("DESCRIPTION"),
                                resultSet.getString("SHORT_DESC"));
                    }
                });
    }

    /**
     * Get Notification list contact details
     * given contact ID
     *
     * @param id contact id
     * @return
     */
    public DCLogContacts getContactByID(int id) {
        try {
            return SIMPLE_JDBC_TEMPLATE.query(
                    QUERY_CONTACT_LIST, new Object[]{id},
                    new ResultSetExtractor<DCLogContacts>() {
                        @Override
                        public DCLogContacts extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                            DCLogContacts dcLogContacts = new DCLogContacts();
                            if (resultSet.next()) {
                                dcLogContacts.setId(resultSet.getInt("CONTACT_ID"));
                                dcLogContacts.setNotificationType(resultSet.getString("NOTIFICATION_TYPE"));
                                dcLogContacts.setNotificationListID(resultSet.getInt("NOTIFICATION_LIST_ID"));
                                dcLogContacts.setRegisteredUser(Boolean.parseBoolean(
                                        resultSet.getString("IS_EXISTING_USER")));
                                if (dcLogContacts.isRegisteredUser()) {
                                    dcLogContacts.setRegisteredContact(new User());
                                    dcLogContacts.getRegisteredContact().setId(resultSet.getInt("USER_ID"));
                                    dcLogContacts.getRegisteredContact().setUsername(
                                            resultSet.getString("USER_USERNAME"));
                                    dcLogContacts.getRegisteredContact().setEmail(resultSet.getString("USER_EMAIL"));
                                    dcLogContacts.getRegisteredContact().setMobile(resultSet.getString("MOBILE"));
                                } else {
                                    dcLogContacts.setExternalContact(new DCLogExternalContact());
                                    dcLogContacts.getExternalContact().setId(resultSet.getInt("EXT_ID"));
                                    dcLogContacts.getExternalContact().setUserName(resultSet.getString("EXT_USERNAME"));
                                    dcLogContacts.getExternalContact().seteMail(resultSet.getString("EXT_EMAIL"));
                                    dcLogContacts.getExternalContact().setMobileNum(
                                            resultSet.getString("MOBILE_NUMBER"));
                                }
                            }

                            return dcLogContacts;
                        }
                    });


        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    public int updateContactNotificationType(DCLogContacts dcLogContacts) {
        LOGGER.debug("Contact ID:{}\nNotification Type:{}",
                dcLogContacts.getNotificationType(), dcLogContacts.getId());

        return SIMPLE_JDBC_TEMPLATE.update("UPDATE DC_LOG_CONTACTS SET NOTIFICATION_TYPE = ? WHERE ID = ?",
                dcLogContacts.getNotificationType(), dcLogContacts.getId());
    }

    public int updateExternalUser(DCLogExternalContact user) {
        LOGGER.debug("Update user with id {} & username {}, mail: {} , mobile: {}",
                new Object[]{user.getId(), user.getUserName(), user.geteMail(), user.getMobileNum()});
        return SIMPLE_JDBC_TEMPLATE.update("UPDATE DC_LOG_EXTERNAL_CONTACTS SET EMAIL = ? , MOBILE_NUMBER = ? " +
                "WHERE ID = ?", user.geteMail(), user.getMobileNum(), user.getId());
    }

    /**
     * Get full notification list when the user submit search request.
     *
     * @param systemName
     * @param logType
     * @param errorCodeID
     * @return
     */
    public DCLogNotificationList getDCLogNotificationListsByFilter(
            String systemName, String logType, int errorCodeID) {

        LOGGER.debug("values passed by search filter:\nSystem Name:{}\nLog Type:{}\nError Code ID:{}",
                new Object[]{systemName, logType, errorCodeID});

        List<Object> paramList = new ArrayList<Object>();
        paramList.add(systemName);
        paramList.add(logType);

        if (errorCodeID > 0) {
            paramList.add(errorCodeID);
        } else {
            paramList.add("-1");
        }

        LOGGER.debug("Param list:{}", paramList.toArray());
        StringBuffer query = new StringBuffer(QUERY_NOTIFY_LIST);
        query.append("WHERE DC_LOG_NOTIFICATION_LIST.SYSTEM_NAME = ?  AND DC_LOG_NOTIFICATION_LIST.LOG_TYPE = ?  ");

        query.append(" AND DC_LOG_NOTIFICATION_LIST.DC_LOG_ERROR_CODES_ID = ? ");

        query.append(" ORDER BY DC_LOG_NOTIFICATION_LIST.ID ");

        return loadNotificationListData(query.toString(), paramList.toArray());

    }

    /**
     * Get the error object given the error code ID
     *
     * @param errorCodeID
     * @return
     */
    public DCLogErrorCode getErrorCodeObjectByID(int errorCodeID) {
        try {
            return SIMPLE_JDBC_TEMPLATE.queryForObject(QUERY_ERROR_CODE_BY_ID,
                    new RowMapper<DCLogErrorCode>() {
                        @Override
                        public DCLogErrorCode mapRow(ResultSet resultSet, int i) throws SQLException {
                            return new DCLogErrorCode(
                                    resultSet.getInt("ID"),
                                    resultSet.getString("ERROR_CODE"),
                                    resultSet.getString("DESCRIPTION"),
                                    resultSet.getString("SHORT_DESC")
                            );
                        }
                    }, errorCodeID);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    /**
     * Retrieve External User ID
     *
     * @param userName
     * @param email
     * @param mobileNumber
     * @return
     */
    public int getExternalUserID(
            String userName, String email, String mobileNumber) {
        try {
            String query
                    = "SELECT ID FROM DC_LOG_EXTERNAL_CONTACTS WHERE USERNAME = '"
                    + userName + "'";
            if (StringUtils.hasText(email)) {
                query += " AND EMAIL = '" + email + "'";
            }
            if (StringUtils.hasText(mobileNumber)) {
                query += " AND MOBILE_NUMBER = '" + mobileNumber + "'";
            }
            LOGGER.debug("Final query: {}", query);

            return SIMPLE_JDBC_TEMPLATE.queryForInt(query);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return 0;
    }


    /**
     * Retrieve Simialr External User ID
     *
     * @param userName
     * @param email
     * @param mobileNumber
     * @return
     */
    public List<Integer> getSimilarExternalUserID(
            String userName, String email, String mobileNumber) {
        try {
            String query
                    = "SELECT ID FROM DC_LOG_EXTERNAL_CONTACTS WHERE USERNAME = '"
                    + userName + "'";
            if (StringUtils.hasText(email)) {
                query += " AND EMAIL = '" + email + "'";
            }
            if (StringUtils.hasText(mobileNumber)) {
                query += " AND MOBILE_NUMBER = '" + mobileNumber + "'";
            }
            LOGGER.debug("Final query: {}", query);

            List<Integer> similarExternalUsers
                    = SIMPLE_JDBC_TEMPLATE.query(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("ID");
                }
            });

            return similarExternalUsers;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    /**
     * Retrieve notification list by ID
     *
     * @param notificationListID
     * @return
     */
    public DCLogNotificationList getNotificationListByID(int notificationListID) {
        StringBuffer query = new StringBuffer(QUERY_NOTIFY_LIST);
        query.append("WHERE DC_LOG_NOTIFICATION_LIST.ID = ?  ORDER BY DC_LOG_NOTIFICATION_LIST.ID ");

        LOGGER.debug("Final Join Query: {}", query);

        return loadNotificationListData(query.toString(), notificationListID);
    }

    /**
     * Add new notification list in case the search result returned no list
     *
     * @param dcLogNotificationList
     * @return
     */
    public int addNewDCNotificationList(
            DCLogNotificationList dcLogNotificationList) {

        Object[] params = new Object[]{dcLogNotificationList.getSystemName(),
                ((dcLogNotificationList.getErrorCode() == null) ? "-1" : dcLogNotificationList.getErrorCode().getId()),
                dcLogNotificationList.getLogType()};

        return SIMPLE_JDBC_TEMPLATE.update(QUERY_ADD_NOTIFY_LIST, params);
    }

    /**
     * Add external user in external contacts table
     *
     * @param user
     * @return
     */
    public int addNewExternalUser(DCLogExternalContact user) {

        String QUERY_ADD_NEW_EXT_USER
                = "INSERT INTO DC_LOG_EXTERNAL_CONTACTS (ID,USERNAME";

        List<Object> queryParam = new ArrayList<Object>();
        String queryBody = "";
        String queryVar = "'" + user.getUserName() + "'";

        queryParam.add(user.getUserName());

        if (StringUtils.hasText(user.geteMail())) {
            queryBody += ", EMAIL";
            queryVar += ", '" + user.geteMail() + "'";
            queryParam.add(user.geteMail());
        }

        if (StringUtils.hasText(user.getMobileNum())) {
            queryBody += ", MOBILE_NUMBER";
            queryVar += ", '" + user.getMobileNum() + "'";
            queryParam.add(user.getMobileNum());
        }

        QUERY_ADD_NEW_EXT_USER += queryBody + ") VALUES (SEQ_DC_LOG_EXTERNAL_CONTACTS.NEXTVAL, " + queryVar + ')';

        LOGGER.debug("Final query:{}", QUERY_ADD_NEW_EXT_USER);

        return SIMPLE_JDBC_TEMPLATE.update(QUERY_ADD_NEW_EXT_USER);

    }

    /**
     * Add registered user to notification contacts list
     *
     * @param userID
     * @param notificationType
     * @param notificationListID
     * @return
     */
    public int addRegisteredUserToNotificationList(int userID, String notificationType, int notificationListID) {
        return SIMPLE_JDBC_TEMPLATE.update(
                QUERY_UPDATE_CONTACTS,
                null, userID, notificationType, "true", notificationListID);
    }

    /**
     * Add external user to notification contact list
     *
     * @param userID
     * @param notificationType
     * @param notificationListID
     * @return
     */
    public int addExternalUserToNotificationList(int userID, String notificationType, int notificationListID) {
        return SIMPLE_JDBC_TEMPLATE.update(
                QUERY_UPDATE_CONTACTS,
                userID, null, notificationType, "false", notificationListID);
    }

    /**
     * Delete User from notification contact list
     *
     * @param contactID
     * @return
     */
    public int deleteUserFromContactList(int contactID) {
        return SIMPLE_JDBC_TEMPLATE.update(QUERY_DELETE_CONTACT,
                contactID);
    }

    /**
     * Log EmptyResultDataAccessException exception
     */
    private void handleEmptyResultDataAccessException(EmptyResultDataAccessException emptyResultDataAccessException) {
        LOGGER.error("No result was returned & an EmptyResultDataAccessException was thrown:"
                + emptyResultDataAccessException);
    }

    /**
     * Performs a Join query to fill the notification list
     *
     * @param query
     * @param params
     * @return
     */
    private DCLogNotificationList loadNotificationListData(String query, Object... params) {

        LOGGER.debug("Query: {}, Param List: {}", query, params);

        return (DCLogNotificationList)
                SIMPLE_JDBC_TEMPLATE.query(query, params, new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        DCLogNotificationList dcLogNotificationListTemp = new DCLogNotificationList();
                        List<DCLogContacts> contactsList = new ArrayList<DCLogContacts>();
                        DCLogContacts contact = new DCLogContacts();

                        while (resultSet.next()) {

                            LOGGER.debug("NotificationListID:{}\nSystem Name:{}\nLOG_TYPE:{}\nDC_LOG_ERROR_CODES_ID:{}" +
                                    "\nERROR_CODE:{}\nContact ID:{}\nNOTIFICATION_TYPE:{}\nIsRegistered:{}\nReg ID:{}\n" +
                                    "Reg UserName:{}\nReg Mail:{}\nReg Mob:{}\nExt ID:{}\nExt UserName:{}\nExt Mail:{}" +
                                    "\nExt Mobile:{}",
                                    new Object[]{resultSet.getInt("ID"), resultSet.getString("SYSTEM_NAME"),
                                            resultSet.getString("LOG_TYPE"), resultSet.getInt("DC_LOG_ERROR_CODES_ID"),
                                            resultSet.getString("ERROR_CODE"), resultSet.getInt("CONTACT_ID"),
                                            resultSet.getString("NOTIFICATION_TYPE"),
                                            resultSet.getString("IS_EXISTING_USER"), resultSet.getInt("USER_ID"),
                                            resultSet.getString("USER_USERNAME"), resultSet.getString("USER_EMAIL"),
                                            resultSet.getString("MOBILE"), resultSet.getInt("EXT_ID"),
                                            resultSet.getString("EXT_USERNAME"), resultSet.getString("EXT_EMAIL"),
                                            resultSet.getString("MOBILE_NUMBER")});

                            dcLogNotificationListTemp.setId(resultSet.getInt("ID"));
                            dcLogNotificationListTemp.setSystemName(resultSet.getString("SYSTEM_NAME"));
                            dcLogNotificationListTemp.setLogType(resultSet.getString("LOG_TYPE"));
                            dcLogNotificationListTemp.setErrorCode(new DCLogErrorCode());
                            dcLogNotificationListTemp.getErrorCode().setId(resultSet.getInt("DC_LOG_ERROR_CODES_ID"));
                            dcLogNotificationListTemp.getErrorCode().setErrorCode(resultSet.getString("ERROR_CODE"));
                            dcLogNotificationListTemp.getErrorCode().setShortDesc(resultSet.getString("SHORT_DESC"));
                            //Fill contact object
                            if (resultSet.getInt("CONTACT_ID") > 0 && !resultSet.wasNull()) {
                                contact.setId(resultSet.getInt("CONTACT_ID"));
                                contact.setNotificationType(resultSet.getString("NOTIFICATION_TYPE"));
                                contact.setRegisteredUser(Boolean.parseBoolean(resultSet.getString("IS_EXISTING_USER")));
                                if (contact.isRegisteredUser()) {
                                    contact.setRegisteredContact(new User());
                                    contact.getRegisteredContact().setId(resultSet.getInt("USER_ID"));
                                    contact.getRegisteredContact().setUsername(resultSet.getString("USER_USERNAME"));
                                    contact.getRegisteredContact().setEmail(resultSet.getString("USER_EMAIL"));
                                    contact.getRegisteredContact().setMobile(resultSet.getString("MOBILE"));
                                } else {
                                    contact.setExternalContact(new DCLogExternalContact());
                                    contact.getExternalContact().setId(resultSet.getInt("EXT_ID"));
                                    contact.getExternalContact().setUserName(resultSet.getString("EXT_USERNAME"));
                                    contact.getExternalContact().seteMail(resultSet.getString("EXT_EMAIL"));
                                    contact.getExternalContact().setMobileNum(resultSet.getString("MOBILE_NUMBER"));
                                }
                                contactsList.add(contact);
                                contact = new DCLogContacts();
                            }

                        }
                        dcLogNotificationListTemp.setContactsList(contactsList);

                        return dcLogNotificationListTemp;
                    }
                });
    }


}
