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
 * User: Alia
 * Date: 4/27/13
 * Time: 3:06 PM
 */
@Repository
public class KPINotificationDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(KPINotificationDao.class);
    private final static String QUERY_NOTIFICATION_LIST = "SELECT MD_KPI_CONTACTS.*, "
            + "USERS.ID AS USER_ID, USERS.USERNAME AS USER_USERNAME,USERS.EMAIL AS USER_EMAIL, USERS.MOBILE, "
            + "DC_LOG_EXTERNAL_CONTACTS.ID AS EXT_ID, DC_LOG_EXTERNAL_CONTACTS.USERNAME AS EXT_USERNAME, "
            + "DC_LOG_EXTERNAL_CONTACTS.EMAIL AS EXT_EMAIL,DC_LOG_EXTERNAL_CONTACTS.MOBILE_NUMBER, "
            + "MD_SYSTEM_NODE.SYSTEM_NODE_ID, MD_SYSTEM_NODE.NODE_NAME, MD_SYSTEM_NODE.IN_USE, "
            + " MD_SYSTEM_NODE.SYSTEM_NAME FROM MD_KPI_CONTACTS "
            + "LEFT OUTER JOIN USERS ON MD_KPI_CONTACTS.REGISTERED_CONTACT_ID = USERS.ID "
            + "LEFT OUTER JOIN DC_LOG_EXTERNAL_CONTACTS ON "
            + "MD_KPI_CONTACTS.EXTERNAL_CONTACTS_ID = DC_LOG_EXTERNAL_CONTACTS.ID "
            + "LEFT OUTER JOIN MD_SYSTEM_NODE ON MD_KPI_CONTACTS.NODE_ID = MD_SYSTEM_NODE.SYSTEM_NODE_ID "
            + "WHERE MD_KPI_CONTACTS.NODE_ID =  ? ";
    private final static String QUERY_CONTACT = "SELECT MD_KPI_CONTACTS.*, "
            + "USERS.ID AS USER_ID, USERS.USERNAME AS USER_USERNAME,USERS.EMAIL AS USER_EMAIL, USERS.MOBILE, "
            + "DC_LOG_EXTERNAL_CONTACTS.ID AS EXT_ID, DC_LOG_EXTERNAL_CONTACTS.USERNAME AS EXT_USERNAME, "
            + "DC_LOG_EXTERNAL_CONTACTS.EMAIL AS EXT_EMAIL,DC_LOG_EXTERNAL_CONTACTS.MOBILE_NUMBER, "
            + "MD_SYSTEM_NODE.SYSTEM_NODE_ID, MD_SYSTEM_NODE.NODE_NAME, MD_SYSTEM_NODE.IN_USE, "
            + " MD_SYSTEM_NODE.SYSTEM_NAME FROM MD_KPI_CONTACTS "
            + "LEFT OUTER JOIN USERS ON MD_KPI_CONTACTS.REGISTERED_CONTACT_ID = USERS.ID "
            + "LEFT OUTER JOIN DC_LOG_EXTERNAL_CONTACTS ON "
            + "MD_KPI_CONTACTS.EXTERNAL_CONTACTS_ID = DC_LOG_EXTERNAL_CONTACTS.ID "
            + "LEFT OUTER JOIN MD_SYSTEM_NODE ON MD_KPI_CONTACTS.NODE_ID = MD_SYSTEM_NODE.SYSTEM_NODE_ID "
            + "WHERE MD_KPI_CONTACTS.ID = ?";
    private final static String QUERY_UPDATE_CONTACTS = "INSERT INTO MD_KPI_CONTACTS (" +
            "ID,EXTERNAL_CONTACTS_ID,REGISTERED_CONTACT_ID,NOTIFICATION_TYPE,IS_EXISTING_USER, NODE_ID) VALUES ("
            + "SEQ_MD_KPI_CONTACTS.NEXTVAL, ?, ?, ?, ?, ?)";
    private final JdbcTemplate SIMPLE_JDBC_TEMPLATE;

    @Autowired
    public KPINotificationDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        SIMPLE_JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }

    /**
     * Retrieve KPI notification list by Node ID
     *
     * @param nodeID Node ID to retrieve equivalent notification list
     * @return KPI notification list
     */
    public KPINotificationList getKPINotificationListByNodeID(int nodeID) {
        LOGGER.debug("Node ID received is: {}", nodeID);
        return loadNotificationListData(QUERY_NOTIFICATION_LIST, nodeID);
    }

    /**
     * Get Notification list contact details
     * given contact ID
     *
     * @param contactID contact id
     * @return KPIContact object
     */
    public KPIContact getContactByID(int contactID) {
        try {
            return SIMPLE_JDBC_TEMPLATE.query(
                    QUERY_CONTACT, new Object[]{contactID},
                    new ResultSetExtractor<KPIContact>() {
                        @Override
                        public KPIContact extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                            KPIContact contact = new KPIContact();
                            if (resultSet.next()) {
                                contact.setId(resultSet.getInt("ID"));
                                contact.setNotificationType(resultSet.getString("NOTIFICATION_TYPE"));
                                contact.setRegisteredUser(Boolean.parseBoolean(
                                        resultSet.getString("IS_EXISTING_USER")));
                                if (contact.isRegisteredUser()) {
                                    contact.setRegisteredContact(new User());
                                    contact.getRegisteredContact().setId(resultSet.getInt("USER_ID"));
                                    contact.getRegisteredContact().setUsername(
                                            resultSet.getString("USER_USERNAME"));
                                    contact.getRegisteredContact().setEmail(resultSet.getString("USER_EMAIL"));
                                    contact.getRegisteredContact().setMobile(resultSet.getString("MOBILE"));
                                } else {
                                    contact.setExternalContact(new DCLogExternalContact());
                                    contact.getExternalContact().setId(resultSet.getInt("EXT_ID"));
                                    contact.getExternalContact().setUserName(resultSet.getString("EXT_USERNAME"));
                                    contact.getExternalContact().seteMail(resultSet.getString("EXT_EMAIL"));
                                    contact.getExternalContact().setMobileNum(
                                            resultSet.getString("MOBILE_NUMBER"));
                                }
                            }

                            return contact;
                        }
                    });


        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    /**
     * Delete User from notification contact list
     *
     * @param contactID
     * @return operation result
     */
    public int deleteUserFromContactList(int contactID) {
        return SIMPLE_JDBC_TEMPLATE.update("DELETE MD_KPI_CONTACTS WHERE ID = ?",
                contactID);
    }

    /**
     * Delete all contacts for the given node ID
     *
     * @param nodeID
     * @return
     */
    public int deleteNotificationList(long nodeID) {
        return SIMPLE_JDBC_TEMPLATE.update("DELETE MD_KPI_CONTACTS WHERE NODE_ID = ?", nodeID);
    }

    /**
     * Add external user in external contacts table
     *
     * @param user
     * @return operation result
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
     * @param nodeID
     * @return operation result
     */
    public int addRegisteredUserToNotificationList(int userID, String notificationType, int nodeID) {
        return SIMPLE_JDBC_TEMPLATE.update(
                QUERY_UPDATE_CONTACTS,
                null, userID, notificationType, "true", nodeID);
    }

    /**
     * Add external user to notification contact list
     *
     * @param userID
     * @param notificationType
     * @param nodeID
     * @return operation result
     */
    public int addExternalUserToNotificationList(int userID, String notificationType, int nodeID) {
        return SIMPLE_JDBC_TEMPLATE.update(
                QUERY_UPDATE_CONTACTS,
                userID, null, notificationType, "false", nodeID);
    }

    /**
     * Update contact's notification type
     *
     * @param contact KPIContact
     * @return operation result
     */
    public int updateContactNotificationType(KPIContact contact) {
        LOGGER.debug("Contact ID:{}\nNotification Type:{}",
                contact.getNotificationType(), contact.getId());

        return SIMPLE_JDBC_TEMPLATE.update("UPDATE MD_KPI_CONTACTS SET NOTIFICATION_TYPE = ? WHERE ID = ?",
                contact.getNotificationType(), contact.getId());
    }

    /**
     * Update External user email/mobile
     *
     * @param user External user object
     * @return operation result
     */
    public int updateExternalUser(DCLogExternalContact user) {
        LOGGER.debug("Update user with id {} & username {}, mail: {} , mobile: {}",
                new Object[]{user.getId(), user.getUserName(), user.geteMail(), user.getMobileNum()});
        return SIMPLE_JDBC_TEMPLATE.update("UPDATE DC_LOG_EXTERNAL_CONTACTS SET EMAIL = ? , MOBILE_NUMBER = ? " +
                "WHERE ID = ?", user.geteMail(), user.getMobileNum(), user.getId());
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
            }else{
                query += " AND EMAIL IS null";
            }
            if (StringUtils.hasText(mobileNumber)) {
                query += " AND MOBILE_NUMBER = '" + mobileNumber + "'";
            }else{
                query += " AND MOBILE_NUMBER IS null";
            }
            LOGGER.debug("Final query: {}", query);

            return SIMPLE_JDBC_TEMPLATE.queryForInt(query);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return 0;
    }

    /**
     * Retrieve External User ID
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

            LOGGER.debug("Found external user ID:{}", similarExternalUsers);
            if(similarExternalUsers != null && !similarExternalUsers.isEmpty()){
                return similarExternalUsers;
            }
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    /**
     * Retrieve the Node ID associated with the given contact ID
     *
     * @param contactID
     * @return Semi loaded VNode object
     */
    public VNode getNodeByContactID(int contactID) {
        try {
            String query = "SELECT MD_SYSTEM_NODE.SYSTEM_NODE_ID, MD_SYSTEM_NODE.SYSTEM_NAME " +
                    "FROM MD_KPI_CONTACTS " +
                    "LEFT OUTER JOIN MD_SYSTEM_NODE ON MD_KPI_CONTACTS.NODE_ID = MD_SYSTEM_NODE.SYSTEM_NODE_ID " +
                    "WHERE ID = ?";

            return SIMPLE_JDBC_TEMPLATE.queryForObject(query, new RowMapper<VNode>() {
                @Override
                public VNode mapRow(ResultSet resultSet, int i) throws SQLException {
                    VNode node = new VNode();
                    node.setId(resultSet.getInt("SYSTEM_NODE_ID"));
                    node.setSystemName(resultSet.getString("SYSTEM_NAME"));
                    return node;
                }
            }, contactID);

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            handleEmptyResultDataAccessException(emptyResultDataAccessException);
        }
        return null;
    }

    /**
     * Log EmptyResultDataAccessException exception
     */
    private void handleEmptyResultDataAccessException(EmptyResultDataAccessException emptyResultDataAccessException) {
        LOGGER.error("No result was returned & an EmptyResultDataAccessException was thrown:"
                + emptyResultDataAccessException);
    }

    /**
     * Wrap query ResultSet into KPINotification object
     *
     * @param query  search for KPI notification list query
     * @param params parameters needed to retrieve the KPI notification list
     * @return wrapped result set
     */
    private KPINotificationList loadNotificationListData(String query, Object... params) {

        LOGGER.debug("Query: {}, Param List: {}", query, params);

        return (KPINotificationList)
                SIMPLE_JDBC_TEMPLATE.query(query, params, new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        KPINotificationList notificationListTemp = new KPINotificationList();
                        List<KPIContact> contactsList = new ArrayList<KPIContact>();
                        KPIContact contact = new KPIContact();

                        while (resultSet.next()) {

                            LOGGER.debug("Contact ID:{}\nSystem Name:{}\nVNODE_ID:{}\nVNODE_NAME:{}" +
                                    "\nNOTIFICATION_TYPE:{}\nIsRegistered:{}\nReg ID:{}\n" +
                                    "Reg UserName:{}\nReg Mail:{}\nReg Mob:{}\nExt ID:{}\nExt UserName:{}\nExt Mail:{}" +
                                    "\nExt Mobile:{}",
                                    new Object[]{resultSet.getInt("ID"), resultSet.getString("SYSTEM_NAME"),
                                            resultSet.getInt("SYSTEM_NODE_ID"), resultSet.getString("NODE_NAME"),
                                            resultSet.getString("NOTIFICATION_TYPE"),
                                            resultSet.getString("IS_EXISTING_USER"), resultSet.getInt("USER_ID"),
                                            resultSet.getString("USER_USERNAME"), resultSet.getString("USER_EMAIL"),
                                            resultSet.getString("MOBILE"), resultSet.getInt("EXT_ID"),
                                            resultSet.getString("EXT_USERNAME"), resultSet.getString("EXT_EMAIL"),
                                            resultSet.getString("MOBILE_NUMBER")});

                            //Fill contact object
                            if (resultSet.getInt("ID") > 0 && !resultSet.wasNull()) {

                                if (resultSet.getInt("SYSTEM_NODE_ID") > 0 && !resultSet.wasNull()
                                        && notificationListTemp.getNode() == null) {
                                    notificationListTemp.setNode(new VNode());
                                    notificationListTemp.getNode().setId(resultSet.getInt("SYSTEM_NODE_ID"));
                                    notificationListTemp.getNode().setName(resultSet.getString("NODE_NAME"));
                                    notificationListTemp.getNode().setSystemName(resultSet.getString("SYSTEM_NAME"));
                                    notificationListTemp.getNode().setInUse(resultSet.getString("IN_USE"));
                                }
                                contact.setId(resultSet.getInt("ID"));
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
                                contact = new KPIContact();
                            }

                        }
                        notificationListTemp.setContactList(contactsList);

                        return notificationListTemp;
                    }
                });
    }

}
