package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.dao.mapper.MdKpiContactRowMapper;
import com.itworx.vaspp.datacollection.dao.mapper.NodePropertiesMapper;
import com.itworx.vaspp.datacollection.dao.mapper.SystemNodeRowMapper;
import com.itworx.vaspp.datacollection.dao.mapper.TrafficTablePropertiesMapper;
import com.itworx.vaspp.datacollection.objects.MdKpiContact;
import com.itworx.vaspp.datacollection.objects.NotificationUserContact;
import com.itworx.vaspp.datacollection.objects.TrafficTableProperties;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.SystemNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.itworx.vaspp.datacollection.dao.DaoConstants.*;

/**
 * @author marwa.goda
 * @since 5/7/13
 */

public class KpiAlarmDao {


    private static final String SYSTEM_NAME = "SYSTEM_NAME";
    private static final String DATE_ONLY = "YYYY-MM-DD";
    private static final String HOURLY_DATE = "YYYY-MM-DD HH24";
    private static final String DATE_WITH_HOURS = "dd/MM/yyyy HH24:MI:ss";
    private static final String HH24 = "'HH24'";
    private static final SimpleDateFormat DAILY_SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat HOURLY_SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH", Locale.US);
    private Logger logger;


    public KpiAlarmDao() {
    }

    public KpiAlarmDao(Logger logger) {
        this.logger = logger;
    }

    public boolean isTableNameAndColumnExist(String tableName, String columnName, Connection connection) {

        boolean isTableNameAndColumnExist = false;
        logger.info("KPIAlarmDao.isTableNameAndColumnExist() table: " + tableName + ", column: " + columnName);
        DatabaseMetaData metaData = null;
        ResultSet tables = null;
        ResultSet columns = null;
        try {
            if (connection == null) {
                logger.error("KPIAlarmDao.isTableNameAndColumnExist() - connection is null");
                return false;
            }
            if (tableName == null || columnName == null) {
                logger.error("KPIAlarmDao.isTableNameAndColumnExist() - invalid parameters");
                return false;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Check table in schema with user:" + connection.getMetaData().getUserName());
            }
            metaData = connection.getMetaData();
            tableName = tableName.toUpperCase();


            tables = metaData.getTables(null, connection.getMetaData().getUserName(), tableName, null);

            if (tables.next()) {
                //Table Exist
                columns = metaData.getColumns(null, connection.getMetaData().getUserName(),
                        tableName, columnName.toUpperCase());
                if (columns.next()) {
                    //Column in table exist
                    isTableNameAndColumnExist = true;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
            logger.error("KPIAlarmDao.isTableNameAndColumnExist() - " + e.getMessage());
        } finally {
            if (tables != null) {
                try {
                    tables.close();
                } catch (Exception e) {
                    logger.error("KPIAlarmDao.isTableNameAndColumnExist() failed to close tables result set", e);
                }
            }
            if (columns != null) {
                try {
                    columns.close();
                } catch (Exception e) {
                    logger.error("KPIAlarmDao.isTableNameAndColumnExist() failed to close columns result set", e);
                }
            }
        }
        return isTableNameAndColumnExist;
    }

    public List<TrafficTableProperties> getTrafficTableNameUtilization
            (String trafficTableName, String nodeName, Date targetDate, boolean isHourly) {

        logger.info("Received target date: " + targetDate);
        //1- Build Query
        StringBuffer getUtilizationQuery =
                new StringBuffer("Select UTILIZATION, to_char(date_time, '" + DATE_WITH_HOURS
                        + "') as date_time  from ").append(trafficTableName).append(WHERE);
        String dateStr = "";

        if (isHourly) {
            synchronized (HOURLY_SIMPLE_DATE_FORMAT) {
                dateStr = HOURLY_SIMPLE_DATE_FORMAT.format(targetDate);
            }
            getUtilizationQuery.append(" TRUNC(DATE_TIME, " + HH24 + ") = TO_TIMESTAMP('").append(dateStr).append("','").append(HOURLY_DATE).append("')");
        } else {
            synchronized (DAILY_SIMPLE_DATE_FORMAT) {
                dateStr = DAILY_SIMPLE_DATE_FORMAT.format(targetDate);
            }
            getUtilizationQuery.append(" DATE_TIME like to_date('").append(dateStr).append("','").append(DATE_ONLY).append("')");
        }

        getUtilizationQuery.append(AND).append(" UPPER( NODE_NAME ) ").append(EQUALS)
                .append(" '").append(StringUtils.upperCase(nodeName)).append("'  ORDER BY DATE_TIME ASC");
        logger.info("Parsed target date pattern:" + dateStr);
        logger.info("Final query:" + getUtilizationQuery.toString());

        //2- get query results
        List<TrafficTableProperties> utilizationList = null;
        Connection ppConnection = null;
        Session ppSession = null;
        Connection cmtConnection = null;
        Statement ppStatement = null;
        Statement cmtStatement = null;
        Statement statement = null;
        boolean trafficTableFound = false;
        try {
            ppSession = DataCollectionManager.getPersistenceManager().getNewSession();
            if (ppSession != null) {
                ppConnection = ppSession.connection();
                if (ppConnection != null) {
                    if (isTableNameAndColumnExist(trafficTableName, "UTILIZATION", ppConnection)) {
                        trafficTableFound = true;
                        logger.info("KpiAlarmDao.getTrafficTableNameUtilization: Traffic table [" + trafficTableName + "] was found in PPSchema");
                        ppStatement = ppConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                        statement = ppStatement;
                    }
                }
            }

            if (!trafficTableFound) {
                cmtConnection = CMTDBConnectionPool.getConnection();
                if (cmtConnection != null) {
                    if (isTableNameAndColumnExist(trafficTableName, "UTILIZATION", cmtConnection)) {
                        trafficTableFound = true;
                        logger.info("KpiAlarmDao.getTrafficTableNameUtilization: Traffic table [" + trafficTableName + "] was found in CMTSchema");
                        cmtStatement = cmtConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                        statement = cmtStatement;
                    }
                }
            }
            if (trafficTableFound) {
                ResultSet utilizationResultSet = statement.executeQuery(getUtilizationQuery.toString());

                //3- Process Query results
                TrafficTablePropertiesMapper trafficTablePropertiesMapper = new TrafficTablePropertiesMapper();
                while (utilizationResultSet.next()) {
                    if (utilizationList == null) {
                        utilizationList = new ArrayList<TrafficTableProperties>();
                    }
                    //Float utilization = utilizationResultSet.getFloat("UTILIZATION");
                    TrafficTableProperties trafficTableProperties = null;
                    try {
                        trafficTableProperties = trafficTablePropertiesMapper.mapRow(utilizationResultSet);
                    } catch (ParseException e) {
                        logger.error("KpiAlarmDao.getTrafficTableNameUtilization: ", e);
                        utilizationList = null;
                    }

                    if (trafficTableProperties != null) {
                        utilizationList.add(trafficTableProperties);
                    }
                }
            } else {
                logger.error("KpiAlarmDao.getTrafficTableNameUtilization: table [" + trafficTableName + "] was not found");
            }
        } catch (SQLException exception) {
            logger.error("KpiAlarmDao.getTrafficTableNameUtilization: ", exception);
            utilizationList = null;
        } finally {
            if (ppConnection != null) {
                try {
                    logger.debug("KPIAlarmDao.getTrafficTableNameUtilization() - close Database Connection");
                    ppConnection.close();
                } catch (SQLException e) {
                    logger.error("KPIAlarmDao.getTrafficTableNameUtilization() - unable to close Database Connection", e);
                }
            }
            if (ppSession != null && ppSession.isOpen()) {
                ppSession.close();
            }

            try {
                if (ppStatement != null) {
                    ppStatement.close();
                }
            } catch (SQLException e) {
                logger.error("KPIAlarmDao.getTrafficTableNameUtilization() - unable to close Database statement", e);
            }
            try {
                if (cmtStatement != null) {
                    cmtStatement.close();
                }
            } catch (SQLException e) {
                logger.error("KPIAlarmDao.getTrafficTableNameUtilization() - unable to close Database statement", e);
            }
        }
        return utilizationList;
    }

    public List<NotificationUserContact> getNodeContactList(long nodeId) {

        List<NotificationUserContact> notificationUserContactList = new ArrayList<NotificationUserContact>();
        StringBuffer getContactIdQuery = new StringBuffer("select * from md_kpi_contacts where node_id = " + nodeId);
        Connection connection = null;

        try {
            connection = CMTDBConnectionPool.getConnection();
            if (connection == null)
                return notificationUserContactList;

            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ResultSet contactIdResultSet = statement.executeQuery(getContactIdQuery.toString());
            List<MdKpiContact> mdKpiContacts = new ArrayList<MdKpiContact>();
            MdKpiContactRowMapper mdKpiContactRowMapper = new MdKpiContactRowMapper();
            while (contactIdResultSet.next()) {
                mdKpiContacts.add(mdKpiContactRowMapper.mapRow(contactIdResultSet));
            }
            statement.close();
            if (mdKpiContacts != null && !mdKpiContacts.isEmpty()) {
                for (MdKpiContact mdKpiContact : mdKpiContacts) {
                    NotificationUserContact notificationUserContact = null;

                    if ("true".equalsIgnoreCase(mdKpiContact.getExistingUser())) {
                        notificationUserContact = getRegisteredUserContact(mdKpiContact.getRegisteredContactId());
                    } else {
                        notificationUserContact = getExternalUserContact(mdKpiContact.getExternalContactId());
                    }
                    notificationUserContact.setNotificationType(mdKpiContact.getNotificationType());
                    notificationUserContactList.add(notificationUserContact);
                }
            }

        } catch (SQLException e) {
            logger.error("KpiAlarmDao.getNodeContactList: ", e);
            notificationUserContactList = null;
        }

        return notificationUserContactList;
    }

    private NotificationUserContact getExternalUserContact(int externalContactId) {
        StringBuffer getExternalUserEmailQuery = new StringBuffer("select email, mobile_number from dc_log_external_contacts where id = " + externalContactId);
        NotificationUserContact userContact = null;
        Connection connection = null;

        try {
            connection = CMTDBConnectionPool.getConnection();
            if (connection == null)
                return userContact;


            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ResultSet externalUserEmailResultSet = statement.executeQuery(getExternalUserEmailQuery.toString());
            while (externalUserEmailResultSet.next()) {
                userContact = new NotificationUserContact();
                userContact.setEmail(externalUserEmailResultSet.getString("email"));
                userContact.setMobileNumber(externalUserEmailResultSet.getString("mobile_number"));
            }
            statement.close();
        } catch (SQLException e) {
            logger.error("KpiAlarmDao.getExternalUserContact: ", e);
            userContact = null;
        }
        return userContact;
    }

    private NotificationUserContact getRegisteredUserContact(int registeredContactId) {
        StringBuffer getRegisteredUserContactQuery = new StringBuffer("select email, mobile from  users where id = " + registeredContactId);
        //return jdbcTemplate.queryForObject(getRegisteredUserEmailQuery.toString(), new Object[]{registeredContactId}, String.class);
        NotificationUserContact userContact = null;
        Connection connection = null;

        try {
            connection = CMTDBConnectionPool.getConnection();
            if (connection == null)
                return userContact;


            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ResultSet externalUserEmailResultSet = statement.executeQuery(getRegisteredUserContactQuery.toString());
            while (externalUserEmailResultSet.next()) {
                userContact = new NotificationUserContact();
                userContact.setEmail(externalUserEmailResultSet.getString("email"));
                userContact.setMobileNumber(externalUserEmailResultSet.getString("mobile"));
            }
            statement.close();
        } catch (SQLException e) {
            logger.error("KpiAlarmDao.getRegisteredUserContact: ", e);
            userContact = null;
        }
        return userContact;
    }

    public List<SystemNode> getSystemNodesBySystemName(String systemName) {
        StringBuffer getSystemNodesBySystemNameQuery = new StringBuffer(SELECT_ALL_CLAUSE).append(MD_SYSTEM_NODE)
                .append(WHERE).append(SYSTEM_NAME).append(EQUALS).append("'" + systemName + "'");

        List<SystemNode> systemNodes = null;
        Connection connection = null;
        try {
            connection = CMTDBConnectionPool.getConnection();
            if (connection == null)
                return systemNodes;


            Statement statement = null;

            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet systemNodesResultSet = statement.executeQuery(getSystemNodesBySystemNameQuery.toString());
            SystemNodeRowMapper systemNodeRowMapper = new SystemNodeRowMapper();
            while (systemNodesResultSet.next()) {
                if (systemNodes == null) {
                    systemNodes = new ArrayList<SystemNode>();
                }
                systemNodes.add(systemNodeRowMapper.mapRow(systemNodesResultSet));
            }
            statement.close();
        } catch (SQLException e) {
            logger.error("KpiAlarmDao.getSystemNodesBySystemName: ", e);
            systemNodes = null;
        }
        return systemNodes;
    }

    public List<NodeProperties> getSystemKpiDetails(long nodeId) {
        StringBuffer getNodePropertiesQuery = new StringBuffer().append(SELECT_ALL_CLAUSE)
                .append(MD_NODE_PROPERTY).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(nodeId);
        logger.debug(getNodePropertiesQuery);
        List<NodeProperties> nodePropertiesList = null;
        Connection connection;

        try {
            connection = CMTDBConnectionPool.getConnection();
            if (connection == null)
                return nodePropertiesList;


            Statement statement;

            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet nodePropertiesResultSet = statement.executeQuery(getNodePropertiesQuery.toString());
            NodePropertiesMapper nodePropertiesMapper = new NodePropertiesMapper();
            while (nodePropertiesResultSet.next()) {
                if (nodePropertiesList == null) {
                    nodePropertiesList = new ArrayList<NodeProperties>();
                }
                nodePropertiesList.add(nodePropertiesMapper.mapRow(nodePropertiesResultSet));
            }
            statement.close();
        } catch (SQLException e) {
            logger.error("KpiAlarmDao.getSystemKpiDetails: ", e);
            nodePropertiesList = null;
        }
        return nodePropertiesList;
    }
}
