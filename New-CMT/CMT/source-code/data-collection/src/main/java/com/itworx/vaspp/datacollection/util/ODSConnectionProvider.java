package com.itworx.vaspp.datacollection.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;

/**
 *
 * @author Basem.Deiaa
 */
public class ODSConnectionProvider implements ConnectionProvider {

    OracleDataSource ods;

    public void configure(Properties props) throws HibernateException {
        try {
            String remoteONSConfig = PropertyReader.getONSConfig();
            ods = new OracleDataSource();
            ods.setURL(props.getProperty("hibernate.connection.url"));
            ods.setUser(props.getProperty("hibernate.connection.username"));
            ods.setPassword(props.getProperty("hibernate.connection.password"));
            ods.setONSConfiguration(remoteONSConfig);
            ods.setConnectionCachingEnabled(true);
            ods.setFastConnectionFailoverEnabled(true);
         } catch (SQLException ex) {
            throw new HibernateException(ex);
        }
    }

    public Connection getConnection() throws SQLException {
        return ods.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void close() throws HibernateException {
        try {
            ods.close();
        } catch (SQLException ex) {
            throw new HibernateException(ex);
        }
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

}
