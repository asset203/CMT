package com.itworx.vaspp.datacollection.util;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

/**
 * class to encapsulate the different application properties that will be
 * constant on the application level
 * 
 * @author elsayed.hassan
 */
public class LogPropertiesReader {
 
    private static Properties properties = null;
    
    static private Logger logger = Logger.getLogger("LogManager");
    
    static public final String SYSTEM_SEPARATOR = System
	.getProperty("file.separator");
    
    static private final String FILE_NAME = "resources" + SYSTEM_SEPARATOR
	+ "properties" + SYSTEM_SEPARATOR + "LogProperties.properties";

//    private static ConnectionPool connectionPool = null;
    private static DataSource connectionPool = null;

    /**
	 * Function used to get a value of a certain property
	 * it takes the name of the property and returns its value
	 * it also checks if the properties was not loaded before to load it.
	 * 
	 * @param name - key of property
	 * @return String - value of property
	 */
	public static String getPropertyValue(String name){
		if(properties == null)
			load();
		return properties.getProperty(name);
	}
	
	/**
	 * function responsible to load properties file
	 */
	private static void load(){
		properties = new Properties();
		try{
			properties.load(new FileInputStream(FILE_NAME));
		}catch (Exception e){
			logger.debug("LogPropertiesReader.load() - Unable To Open properties file: LogProperties.properties");
		}
	}
    
    /**
     * Method to return the system connection pool
     * 
     * @return An OracleConnectionCacheImpl instance
     * @throws SQLException
     */
 /*   public static ConnectionPool getConnectionPool1()
            throws SQLException {
    	
         synchronized (LogPropertiesReader.class) 
         {
        	 if (connectionPool == null) 
            {
            	String dataBaseUserName = LogPropertiesReader.getPropertyValue("dataBase.userName");
            	String dataBasepPassword = LogPropertiesReader.getPropertyValue("dataBase.password");
                String maxNoConnections = LogPropertiesReader.getPropertyValue("dataBase.maxConnections");
                String dataBaseURL = LogPropertiesReader.getPropertyValue("dataBase.URL");

                connectionPool = new ConnectionPool();
                connectionPool.setPoolName("connectionPool");
                connectionPool.setURL(dataBaseURL);
                connectionPool.setUser(dataBaseUserName);
                connectionPool.setPassword(dataBasepPassword);
                connectionPool.setMaxLimit(Integer
                        .parseInt(maxNoConnections));
                return connectionPool;
            }else
            {
            	return connectionPool;
            }
            	
        }  
    }*/
    
    /**
     * Method to return the system connection pool
     * 
     * @return An DataSource instance
     * @throws SQLException
     */
	public static DataSource getConnectionPool() throws SQLException {

		synchronized (LogPropertiesReader.class) {
			if (connectionPool == null) {
				String dataBaseUserName = LogPropertiesReader.getPropertyValue("dataBase.userName");
				String dataBasepPassword = LogPropertiesReader.getPropertyValue("dataBase.password");
				String maxNoConnections = LogPropertiesReader.getPropertyValue("dataBase.maxConnections");
				String dataBaseURL = LogPropertiesReader.getPropertyValue("dataBase.URL");
				String remoteONSConfig = PropertyReader.getONSConfig();

				connectionPool = new OracleDataSource();

				Properties cacheProps = new Properties();
				cacheProps.setProperty("MaxLimit", Integer.parseInt(maxNoConnections)+"");
				((OracleDataSource)connectionPool).setConnectionCacheProperties(cacheProps);
				((OracleDataSource)connectionPool).setURL(dataBaseURL);
				((OracleDataSource)connectionPool).setUser(dataBaseUserName);
				((OracleDataSource)connectionPool).setPassword(dataBasepPassword);
				((OracleDataSource)connectionPool).setConnectionCachingEnabled(true);
				((OracleDataSource)connectionPool).setONSConfiguration(remoteONSConfig);
				((OracleDataSource)connectionPool).setFastConnectionFailoverEnabled(true);
				((OracleDataSource)connectionPool).setConnectionCacheName("connectionPool");
				
				return connectionPool;
			} else {
				return connectionPool;
			}

		}
	}

	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if(connectionPool != null)
			((OracleDataSource)connectionPool).close();
	}
    

    public static void setConnectionPool(
    		DataSource connectionPool) {
    	LogPropertiesReader.connectionPool = connectionPool;
    }
    

    
	
}
