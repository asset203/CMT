package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 5/27/13
 * Time: 11:15 AM
 */
public class CMTDBConnectionPool {
    private static final String DB_DRIVER_KEY = "driver";
    private static final String DB_URL_KEY = "url";
    private static final String DB_USER_NAME_KEY = "username";
    private static final String DB_PASSWORD_KEY = "password";
    private static final String POOL_INITIAL_SIZE = "initialSize";
    private static final String POOL_MAX_IDLE_COUNT = "maxIdle";
    private static String logFileMaxSize = "1024KB";
    private static String logFileMaxCount = "20";
    private static Priority logFileRollingThreshold = Level.INFO;
    private static Logger LOGGER;
    private static String logFileName = "dbConnection";
    private static String logFilePath = "logs" + System
            .getProperty("file.separator") + logFileName + ".log";
    private static BasicDataSource dataSource;
    private static Connection connection;
    private static BasicDataSource tempDBDataSource;

    /**
     * Get dbConnection tracking logger
     *
     * @return
     */
    private static Logger getLogger() {

        if (LOGGER == null) {
            LOGGER = Logger.getLogger(logFileName);

            try {
                logFileMaxCount = PropertyReader.getPropertyValue("log.db.max.count");
                logFileMaxSize = PropertyReader.getPropertyValue("log.db.max.size");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                logFileRollingThreshold
                        = Level.toLevel(PropertyReader.getPropertyValue("log.db.rolling.threshold"));
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }

            RollingFileAppender rollingFileAppender = new RollingFileAppender();
            rollingFileAppender.setName(logFileName);
            rollingFileAppender.setFile(logFilePath);
            rollingFileAppender.setAppend(true);
            rollingFileAppender.setLayout(
                    new PatternLayout("%d{dd/MM/yyyy HH:mm:ss} %-5p %c %x - %m%n"));

            rollingFileAppender.setMaxFileSize(logFileMaxSize);

            try {
                rollingFileAppender.setMaxBackupIndex(Integer.parseInt(logFileMaxCount));
            } catch (Exception ex) {
                ex.printStackTrace();
                rollingFileAppender.setMaxBackupIndex(20);
            }

            rollingFileAppender.setThreshold(logFileRollingThreshold);

            rollingFileAppender.activateOptions();

            LOGGER.addAppender(rollingFileAppender);
        }

        return LOGGER;
    }

    /**
     * Get database data source
     *
     * @return
     */
    public static BasicDataSource getDataSource() {
        LOGGER = getLogger();
        try {
            if (dataSource == null) {
                LOGGER.info("New datasource initialized at time:"
                        + new Date());
                Properties props = new Properties();
                String dataSourcePropsFile
                        = PropertyReader.getDataSourcePropertiesFilePath();
                props.load(new FileInputStream(dataSourcePropsFile));

                dataSource = new BasicDataSource();
                dataSource.setDriverClassName(props.getProperty(DB_DRIVER_KEY));
                dataSource.setUrl(props.getProperty(DB_URL_KEY));
                dataSource.setUsername(props.getProperty(DB_USER_NAME_KEY));
                dataSource.setPassword(props.getProperty(DB_PASSWORD_KEY));
                dataSource.setTestWhileIdle(true);

                int poolInitSize = 1;
                int maxIdleCount = 1;
                try {
                    poolInitSize = Integer.parseInt(props.getProperty(POOL_INITIAL_SIZE));
                    maxIdleCount = Integer.parseInt(props.getProperty(POOL_MAX_IDLE_COUNT));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Pool init size:" + poolInitSize + ", max idle connection count:" + maxIdleCount);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error(e);
                }
                dataSource.setInitialSize(poolInitSize);
                dataSource.setMaxIdle(maxIdleCount);
            } else {
                LOGGER.info("Existing datasource was requested at time:" + new Date());
                return dataSource;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }

        return dataSource;
    }

    /**
     * Get connection pool
     *
     * @return
     */
    public static Connection getConnection() {
        LOGGER = getLogger();
        try {
            if (dataSource == null || (dataSource != null && connection == null)
                    || (dataSource != null && connection != null && connection.isClosed())) {
                LOGGER.info("No datasource created, creating new one...");
                dataSource = getDataSource();
                try {
                    connection = dataSource.getConnection();
                    LOGGER.info("New DB connection initialized at time:"
                            + new Date() + ", with status:" + connection.isClosed());
                } catch (SQLException e) {
                    LOGGER.equals(e);
                }
            } else {
                LOGGER.info("connection pool exist & active");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return connection;
    }

    /**
     * Close opened data connection
     */
    public static void closeConnection() {
        LOGGER = getLogger();
        try {
            if (dataSource != null) {
                LOGGER.info("Existing DB connection was requested for close at time:" + new Date());
                dataSource.close();
                LOGGER.info("Existing DB connection was requested for close with closure status:"
                        + dataSource.getConnection().isClosed());
            } else {
                LOGGER.info("Non initialized DB connection was requested for close at time:" + new Date());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            try {
                dataSource.close();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Get database connection to Temp database
     *
     * @return
     */
    public static Connection getTEMPDBConnection() {
        LOGGER = getLogger();
        try {
            if (tempDBDataSource == null) {
                LOGGER.info("New TEMP DB datasource initialized at time:"
                        + new Date());
                Properties props = new Properties();
                String dataSourcePropsFile
                        = PropertyReader.getDataSourcePropertiesFilePath();
                props.load(new FileInputStream(dataSourcePropsFile));

                tempDBDataSource = new BasicDataSource();
                tempDBDataSource.setDriverClassName(PropertyReader.getTempDBDriver());
                tempDBDataSource.setUrl(PropertyReader.getTempDBURL());
                tempDBDataSource.setUsername(PropertyReader.getTempDBUserName());
                tempDBDataSource.setPassword(PropertyReader.getTempDBPassword());
                tempDBDataSource.setTestWhileIdle(true);
                int poolInitSize = 1;
                int maxIdleCount = 1;
                try {
                    poolInitSize = Integer.parseInt(props.getProperty(POOL_INITIAL_SIZE));
                    maxIdleCount = Integer.parseInt(props.getProperty(POOL_MAX_IDLE_COUNT));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Pool init size:" + poolInitSize + ", max idle connection count:" + maxIdleCount);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error(e);
                }
                tempDBDataSource.setInitialSize(poolInitSize);
                tempDBDataSource.setMaxIdle(maxIdleCount);

            }
            if (tempDBDataSource != null) {
                LOGGER.info("New TEMP DB connection created at time:" + new Date());
                return tempDBDataSource.getConnection();
            }

        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }
}
