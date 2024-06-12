package com.itworx.vaspp.datacollection.logmanager;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.LogErrorRecord;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.DCLogContacts;
import eg.com.vodafone.model.DCLogExternalContact;
import eg.com.vodafone.model.User;
import eg.com.vodafone.model.enums.LogType;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class LogFilesDAO {

    static private Logger logger = Logger.getLogger("LogManager");
    private static PersistenceManager persistenceManager;

    /*
     * a method returns all ERROR_CODE from DC_LOG_ERROR_CODE table
     * case exist
     * null otherwise
     * */
    public static List<String> getErrorCode() {
        Connection connection = null;
        Session session = null;
        List<String> errorCodeList;
        ResultSet rs;

        errorCodeList = null;
        rs = null;

        try {
            session = getSession();
            if (session == null)
                return null;
            connection = session.connection();
            if (connection == null)
                return errorCodeList;
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT ERROR_CODE FROM DC_LOG_ERROR_CODES";
            System.out.println(sql);
            rs = statement.executeQuery(sql);
            errorCodeList = convertResultSetToList(rs, "ERROR_CODE");
            statement.close();
        } catch (SQLException e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.getErrorCode()]:Exception:"
                    , e);
        } catch (Exception e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.getErrorCode()]:Exception:"
                    , e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (session != null) {
                session.close();
            }
        }
        return errorCodeList;
    }

    /*
     * @param  systemName - target system name
     * @return result set containing all the contacts matching that systemName
     * null otherwise
     * */
    public static Map<String, Map<String, List<DCLogContacts>>> getSystemContactes(String systemName) {
        Connection connection = null;
        Session session = null;
        ResultSet sysContactsRS;
        Map<String, Map<String, List<DCLogContacts>>> result;

        sysContactsRS = null;
        result = null;

        if (systemName == null || systemName == "") {
            return result;
        }

        try {
            session = getSession();
            if (session == null)
                return result;
            connection = session.connection();
            if (connection == null)
                return result;
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            String sql;
            sql = "SELECT DLNL.*, DLEC.id DLECId, DLEC.ERROR_CODE DLECEC, DLEC.DESCRIPTION DLECDesc, " + "\n";
            sql += "DCLC.id DCLCId, DCLC.NOTIFICATION_TYPE DCLCNT, DCLC.IS_EXISTING_USER DCLCIEU,DCLC.NOTIFICATION_LIST_ID DCLCNLI, " + "\n";
            sql += "U.ID UserID, U.USERNAME UName,U.EMAIL UMail,U.MOBILE UMobile," + "\n";
            sql += "DCLEC.ID ExtUserID, DCLEC.USERNAME ExtUserName, DCLEC.EMAIL ExtUserMail, DCLEC.MOBILE_NUMBER ExtUserMobile" + "\n";
            sql += "FROM  DC_LOG_NOTIFICATION_LIST DLNL " + "\n";
            sql += "LEFT OUTER JOIN DC_LOG_ERROR_CODES DLEC" + "\n";
            sql += "ON DLNL.DC_LOG_ERROR_CODES_ID = DLEC.ID" + "\n";
            sql += "LEFT JOIN DC_LOG_CONTACTS DCLC" + "\n";
            sql += "ON DLNL.ID = DCLC.NOTIFICATION_LIST_ID" + "\n";
            sql += "LEFT JOIN USERS U" + "\n";
            sql += "ON DCLC.REGISTERED_CONTACT_ID = U.ID  " + "\n";
            sql += "LEFT JOIN DC_LOG_EXTERNAL_CONTACTS DCLEC" + "\n";
            sql += "ON DCLC.EXTERNAL_CONTACTS_ID = DCLEC.ID" + "\n";
            sql += "WHERE DLNL.SYSTEM_NAME = '" + Utils.formatDatabaseStringValues(systemName) + "'\n";
            sql += "AND DCLC.NOTIFICATION_TYPE IS NOT NULL AND DLNL.LOG_TYPE = '" + LogType.ERROR.getValue() + "'\n";
            System.out.println(sql);
            sysContactsRS = statement.executeQuery(sql);
            result = getSystemContactObjects(sysContactsRS);
            logger.info("contacts resultset size:" + ((result != null) ? result.size() : "null"));
            statement.close();
        } catch (SQLException e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.getSystemContactes()]:Exception:"
                    , e);
        } catch (Exception e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.getSystemContactes()]:Exception:"
                    , e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    /*
     * @param  systemName - target system name
     * @return a map of log types referencing a map of errorCode key and DCLogNotification List objects values
     * null otherwise
     * */
    private static Map<String, Map<String, List<DCLogContacts>>> getSystemContactObjects(ResultSet systemContactsRecs) {
        Map<String, Map<String, List<DCLogContacts>>> result
                = new HashMap<String, Map<String, List<DCLogContacts>>>();

        if (systemContactsRecs != null) {
            try {
                List<DCLogContacts> dcLogContactsList;
                DCLogContacts dcLogContacts;
                DCLogExternalContact externalContact;
                Map<String, List<DCLogContacts>> errorMap = new HashMap<String, List<DCLogContacts>>();
                User user;
                String logType, errorCode;
                boolean isRegistered;

                result.put(LogType.ERROR.getValue(), errorMap);

                while (systemContactsRecs.next()) {
                    logType = systemContactsRecs.getString("LOG_TYPE");
                    errorCode = systemContactsRecs.getString("DLECEC");
                    logger.debug("error code:" + errorCode);
                    if(errorCode == null){
                        errorCode = "-1";
                    }else{
                        errorCode = errorCode.trim();
                    }

                    isRegistered = Boolean.parseBoolean(systemContactsRecs.getString("DCLCIEU"));
                    if (isRegistered) {
                        user = new User(systemContactsRecs.getInt("UserID"),
                                systemContactsRecs.getString("UName"), "", 0,
                                systemContactsRecs.getString("UMail"),
                                systemContactsRecs.getString("UMobile"));
                        externalContact = null;
                    } else {
                        externalContact = new DCLogExternalContact(systemContactsRecs.getInt("ExtUserID"),
                                systemContactsRecs.getString("ExtUserName"),
                                systemContactsRecs.getString("ExtUserMail"),
                                systemContactsRecs.getString("ExtUserMobile"));
                        user = null;
                    }

                    dcLogContacts = new DCLogContacts();
                    dcLogContacts.setRegisteredUser(isRegistered);
                    dcLogContacts.setRegisteredContact(user);
                    dcLogContacts.setExternalContact(externalContact);
                    dcLogContacts.setNotificationType(systemContactsRecs.getString("DCLCNT"));
                    dcLogContactsList = new ArrayList<DCLogContacts>();

                    if (!result.get(LogType.ERROR.getValue()).containsKey(errorCode)) {
                        dcLogContactsList.add(dcLogContacts);
                        errorMap = result.get(LogType.ERROR.getValue());
                        errorMap.put(errorCode, dcLogContactsList);
                        result.put(LogType.ERROR.getValue(), errorMap);
                    } else if(result.get(LogType.ERROR.getValue()).containsKey(errorCode)){
                        dcLogContactsList = result.get(LogType.ERROR.getValue()).get(errorCode);
                        logger.debug("is dcLogContactsList null?" + ((dcLogContactsList == null)?null: dcLogContactsList));
                        if(dcLogContactsList == null
                                || (dcLogContactsList != null && dcLogContactsList.isEmpty())){
                            dcLogContactsList = new ArrayList<DCLogContacts>();
                        }
                        dcLogContactsList.add(dcLogContacts);
                        logger.debug("dcLogContactsList size:" + dcLogContactsList.size());
                        result.get(LogType.ERROR.getValue()).put(errorCode, dcLogContactsList);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (systemContactsRecs != null) {
                    try {
                        systemContactsRecs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(logger.isDebugEnabled()){
            if(result != null && !result.isEmpty()){
                for(Map.Entry<String, Map<String, List<DCLogContacts>>> entry : result.entrySet()){
                    logger.debug("Log type:" + entry.getKey());
                    for(Map.Entry<String, List<DCLogContacts>> entry2 : entry.getValue().entrySet()) {
                        logger.debug("error code: \"" + entry2.getKey() + "\" \nAfter trim:\""
                                + entry2.getKey().trim() + "\"");
                        for(DCLogContacts contacts : entry2.getValue()){
                            logger.debug("contact info:");
                            if(contacts.isRegisteredUser()){
                                logger.debug("Email:" + contacts.getRegisteredContact().getEmail()
                                        + "\nMobile:" + contacts.getRegisteredContact().getMobile()
                                        + "\nNotification type:" + contacts.getNotificationType());
                            }else{
                                logger.debug("Email:" + contacts.getExternalContact().geteMail()
                                        + "\nMobile:" + contacts.getExternalContact().getMobileNum()
                                        + "\nNotification type:" + contacts.getNotificationType());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /*
     * Converting a result set with the specified field name to
     * list case exists
     * null otherwise
     * */
    public static List<String> convertResultSetToList(ResultSet rs, String fieldName) {
        List<String> list;

        list = null;

        try {
            if (rs != null & rs.next()) {

                list = new ArrayList<String>();

                do {
                    list.add(rs.getString(fieldName));
                } while (rs.next());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static void JobStarted(Date date) {

        Connection connection = null;
        Session session = null;
        try {
            session = getSession();
            if (session == null)
                return;
            connection = session.connection();
            if (connection == null)
                return;
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            SimpleDateFormat smpl = new SimpleDateFormat("MM/dd/yyyy");

            String SQL = "SELECT DATE_TIME from DC_STARTED_JOB where DATE_TIME = to_date('" + smpl.format(date) + "','MM/DD/YYYY')";

            ResultSet resultSet = (ResultSet) statement.executeQuery(SQL);

            if (resultSet.next())
                return;


            resultSet.moveToInsertRow();
            resultSet.updateDate("DATE_TIME", new java.sql.Date(date.getTime()));
            resultSet.insertRow();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.saveLogFilesInfo()]:Exception:"
                    , e);
        } catch (Exception e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.saveLogFilesInfo()]:Exception:"
                    , e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (session != null) {
                session.close();
            }
        }

    }

    private static Session getSession() {
        if (persistenceManager == null) {
            try {
                persistenceManager = new PersistenceManager(true);
            } catch (ApplicationException e1) {
                logger.error("Exception : ", e1);
                return null;
            }
        }
        Session session = null;
        try {
            session = persistenceManager.getNewCmtSchemaSession();
            if (session == null) {
                logger.error("Can't pen new database session");
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            return null;
        }
        return session;

    }

    public static Connection getConnection() throws InputException,
            ApplicationException {

        logger.debug("DBInputDAO.getConnection() - started getConnection for input id=");
        String driverName = "";
        try {

            driverName = PropertyReader.getDriverName();
            DriverManager.registerDriver((Driver) Class.forName(driverName)
                    .newInstance());
            String DBURL = null;
            if (driverName.indexOf("oracle") != -1) {
                DBURL = PropertyReader.getOracleThinName();
            }
            logger.debug("DBInputDAO.getConnection() - DB url: " + DBURL);
            if (DBURL == null) {
                throw new ApplicationException(" undefined DB Driver "
                        + driverName);
            }
            Connection connection = DriverManager.getConnection(DBURL, PropertyReader.getUserName(), PropertyReader.getPassword());
            logger.debug("DBInputDAO.getConnection() - finished getConnection for input id=");
            return connection;
        } catch (InstantiationException e) {
            logger.error(" DB-1001: DBInputDAO.getConnection() - Couldn't instantiate DB driver: " + driverName + ":" + e);
            e.printStackTrace();
            throw new ApplicationException("" + e);
        } catch (SQLException e) {

            logger.error("DB-1002:DBInputDAO.getConnection() - SQL error while creating connection" + e);
            e.printStackTrace();
            throw new InputException("" + e);
        } catch (IllegalAccessException e) {
            //logger.error("-"+ input.getNodeName() +"- DB-1001:DBInputDAO.getConnection() - Error with DB driver "+ driverName + ":" + e);
            throw new ApplicationException("" + e);
        } catch (ClassNotFoundException e) {
            //logger.error("-"+ input.getNodeName() +"- DB-1004: DBInputDAO.getConnection() - Couldn't find DB driver class " + driverName + ":" + e);
            throw new ApplicationException("" + e);
        }
    }

    public static void main(String ag[]) {

        ArrayList logErrorRecordList = new ArrayList();
        LogErrorRecord l;
        for (int i = 0; i < 5; i++) {
            l = new LogErrorRecord();
            l.setDate_time("01/12/2007");
            l.setSysName("test_system");
            l.setNode("system");
            l.setType("error");
            l.setDescription("test_error_description");
            l.setRetry(2);
            logErrorRecordList.add(l);
        }

        LogFilesDAO s = new LogFilesDAO();
        s.saveLogFilesInfo(logErrorRecordList);


    }

    public void saveLogFilesInfo(ArrayList logErrorRecordList) {
        Connection connection = null;
        Session session = null;
        try {
            session = getSession();
            if (session == null)
                return;
            connection = session.connection();
            if (connection == null)
                return;
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            System.out.println("List size:" + logErrorRecordList.size());
            for (int i = 0; i < logErrorRecordList.size(); i++) {

                LogErrorRecord logErrorRecord = (LogErrorRecord) logErrorRecordList.get(i);
                System.out.println("Node:" + logErrorRecord.getNode() + "\nError Code:" + logErrorRecord.getErrorCode()
                        + "desc:" + logErrorRecord.getDescription() + "\n******************");
                String sql = "insert into DC_LOG_FILES_INFO(ID,LOG_DATE,SYSTEM_NAME,NODE ,LOG_TYPE,DESCRIPTION,RETRY, ERROR_CODE) values( SEQ_DC_LOG_FILES_INFO.nextval,";
                String errorCode;

                errorCode = logErrorRecord.getErrorCode();
                if (errorCode == null || errorCode == "") {
                    errorCode = "null";
                } else {
                    errorCode = "'" + Utils.formatDatabaseStringValues(errorCode) + "'";
                }

                sql = sql + "To_Date('" + logErrorRecord.getDate_time() + "','MM/DD/YYYY'),";
                sql = sql + "'" + Utils.formatDatabaseStringValues(logErrorRecord.getSysName()) + "',";
                sql = sql + "'" + Utils.formatDatabaseStringValues(logErrorRecord.getNode()) + "',";
                sql = sql + "'" + Utils.formatDatabaseStringValues(logErrorRecord.getType()) + "',";
                sql = sql + "'" + Utils.formatDatabaseStringValues(logErrorRecord.getDescription()) + "',";
                sql = sql + logErrorRecord.getRetry() + ",";
                sql = sql + errorCode + ")";
                //System.out.println(sql);
                statement.executeUpdate(sql);
            }

            statement.close();
        } catch (SQLException e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.saveLogFilesInfo()]:Exception:"
                    , e);
        } catch (Exception e) {
            logger.error(
                    "[com.itworx.vaspp.datacollection.logmanager.LogFilesDAO.saveLogFilesInfo()]:Exception:"
                    , e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (session != null) {
                session.close();
            }
        }

    }
}
