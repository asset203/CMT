package com.itworx.vaspp.datacollection.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.VCounter;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class TrendAnalysisDAO {

	private static Logger logger = Logger.getLogger("TrendAnalysis");

	private static Connection mainConnection;

	private static Properties properties = null;

	static public final String SYSTEM_SEPARATOR = System
			.getProperty("file.separator");

	static private final String FILE_NAME = "resources" + SYSTEM_SEPARATOR
			+ "configuration" + SYSTEM_SEPARATOR + "app_config.properties";

	// private static ConnectionPool connectionPool = null;
	private static DataSource connectionPool = null;

	/**
	 * Function used to get a value of a certain property it takes the name of
	 * the property and returns its value it also checks if the properties was
	 * not loaded before to load it.
	 * 
	 * @param name
	 *            - key of property
	 * @return String - value of property
	 */
	public static String getPropertyValue(String name) {
		if (properties == null)
			load();
		return properties.getProperty(name);
	}

	/**
	 * function responsible to load properties file
	 */
	private static void load() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(FILE_NAME));
		} catch (Exception e) {
			logger
					.debug("TrendAnalysisDAO.load() - Unable To Open properties file: app_config.properties");
		}
	}

	// /**
	// * Method to return the system connection pool
	// *
	// * @return An DataSource instance
	// * @throws SQLException
	// */
	// public static DataSource getConnectionPool() throws SQLException {
	//
	// synchronized (TrendAnalysisDAO.class) {
	// if (connectionPool == null) {
	// String dataBaseUserName = TrendAnalysisDAO
	// .getPropertyValue("trendAnalysis.userName");
	// String dataBasepPassword = TrendAnalysisDAO
	// .getPropertyValue("trendAnalysis.password");
	// String maxNoConnections = TrendAnalysisDAO
	// .getPropertyValue("trendAnalysis.maxConnections");
	// String dataBaseURL = TrendAnalysisDAO
	// .getPropertyValue("trendAnalysis.URL");
	// String remoteONSConfig = TrendAnalysisDAO
	// .getPropertyValue("ons.config");
	//
	// connectionPool = new OracleDataSource();
	//
	// Properties cacheProps = new Properties();
	// cacheProps.setProperty("MaxLimit", Integer
	// .parseInt(maxNoConnections)
	// + "");
	// ((OracleDataSource) connectionPool)
	// .setConnectionCacheProperties(cacheProps);
	// ((OracleDataSource) connectionPool).setURL(dataBaseURL);
	// ((OracleDataSource) connectionPool).setUser(dataBaseUserName);
	// ((OracleDataSource) connectionPool)
	// .setPassword(dataBasepPassword);
	// ((OracleDataSource) connectionPool)
	// .setConnectionCachingEnabled(true);
	// ((OracleDataSource) connectionPool)
	// .setONSConfiguration(remoteONSConfig);
	// //
	// ((OracleDataSource)connectionPool).setFastConnectionFailoverEnabled(true);
	// ((OracleDataSource) connectionPool)
	// .setConnectionCacheName("connectionPool");
	//
	// return connectionPool;
	// } else {
	// return connectionPool;
	// }
	//
	// }
	// }

	public static Connection getConnection(Logger logger)
			throws ApplicationException, InputException {
		logger
				.debug("TrendAnalysisDAO.getConnection(logger) - starting Database Connection");
		String url = PropertyReader.getTrendAnalysisURL();
		String driverName = PropertyReader.getTrendAnalysisDriver();
		String userName = PropertyReader.getTrendAnalysisUserName();

		String password = PropertyReader.getTrendAnalysisPassword();

		try {
			Class.forName(driverName).newInstance();
			mainConnection = DriverManager.getConnection(url, userName,
					password);
			return mainConnection;
		} catch (InstantiationException e) {
			logger.debug("TrendAnalysisDAO.getConnection(logger) - Exception ",
					e);
			throw new ApplicationException("" + e);
		} catch (SQLException e) {
			logger.debug("TrendAnalysisDAO.getConnection(logger) - Exception ",
					e);
			throw new InputException("" + e);
		} catch (IllegalAccessException e) {
			logger.debug("TrendAnalysisDAO.getConnection(logger) - Exception ",
					e);
			throw new ApplicationException("" + e);
		} catch (ClassNotFoundException e) {
			logger.debug("TrendAnalysisDAO.getConnection(logger) - Exception ",
					e);
			throw new ApplicationException("" + e);
		}
	}

	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (connectionPool != null)
			((OracleDataSource) connectionPool).close();
	}

	public static void setConnectionPool(DataSource connectionPool) {
		TrendAnalysisDAO.connectionPool = connectionPool;
	}

	public Double retrieveQueryResults(String sql) throws Exception {
		Double result = null;
		Connection connection = null;
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next() && resultSet.getObject("result") != null) {
				result = resultSet.getDouble("result");
			} else {
				result = null;
			}
			statement.close();
		} catch (SQLException e) {
//			logger.error("TrendAnalysisDAO.retrieveQueryResults(): " + sql
//					+ " Exception:", e);
			//e.printStackTrace();
			throw e;
		} catch (Exception e) {
//			logger.error("TrendAnalysisDAO.retrieveQueryResults(): " + sql
//					+ " Exception:", e);
//			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.retrieveQueryResults(sql) - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return result;

	}

	public void insertQueryResults(String counterId, String date,
			ArrayList queries, String description) {
		Connection connection = null;
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			String daily;
			String weekly;
			String monthly;
			if (queries.get(0) == null) {
				daily = null;
			} else {
				daily = queries.get(0).toString();
			}
			if (queries.get(1) == null) {
				weekly = null;
			} else {
				weekly = queries.get(1).toString();
			}
			if (queries.get(2) == null) {
				monthly = null;
			} else {
				monthly = queries.get(2).toString();
			}
			String sql = "insert into TREND_ANALYSIS_RESULTS(ID,COUNTER_ID,DATE_TIME,DAILY ,WEEKLY,MONTHLY,DESCRIPTION) values( SEQ_TREND_ANALYSIS_RESULTS.nextval,";
			sql = sql + "'" + counterId + "',";
			sql = sql + "To_Date('" + date + "','MM/DD/YYYY'),";
			sql = sql + daily + ",";
			sql = sql + weekly + ",";
			sql = sql + monthly + ",";
			sql = sql + "'" + description + "')";
			// System.out.println(sql);
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.insertQueryResults() - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/*added by Suzan Tdrous */
	public void insertMappingValues(VCounter[] vcounterArray) {
		
		Connection connection = null;
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			for(int i=0;i<vcounterArray.length;i++)
			{
				
				String sql = "insert into LO_ANALYSIS_COUNTERS(COUNTER_ID,COUNTER_NAME) values(";
				sql = sql + "'" + vcounterArray[i].getId() + "',";
				sql = sql + "'" + vcounterArray[i].getName() + "')";
				// System.out.println(sql);
				statement.executeUpdate(sql);
				
			}
			statement.close();
			
		} catch (SQLException e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.insertQueryResults() - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	public void insertCounterProperties(VCounter[] vcounterArray) {
		
		Connection connection = null;
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			for(int i=0;i<vcounterArray.length;i++)
			{
				
				String sql = "insert into TREND_ANALYSIS_PROPERTIES(COUNTER_ID,ERROR ,WARNNING,NORMAL) values(";
				sql = sql + "'" + vcounterArray[i].getId() + "',";
				sql = sql + "'" + vcounterArray[i].getError() + "',";
				sql = sql + "'" + vcounterArray[i].getWarning() + "',";
				sql = sql + "'" + vcounterArray[i].getNormal() + "')";
				 //System.out.println(sql);
				statement.executeUpdate(sql);
				
			}
			statement.close();
			
		} catch (SQLException e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("TrendAnalysisDAO.insertQueryResults():Exception:", e);
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.insertQueryResults() - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/**/
	public boolean retrieveTableRows(String counterId, String date) throws Exception {
		boolean result;
		Connection connection = null;
		String sql = "select ID as result from TREND_ANALYSIS_RESULTS where COUNTER_ID = '"+counterId+"' and DATE_TIME = to_date('"+date+"', 'MM/dd/yyyy')";
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next() && resultSet.getObject("result") != null) {
				result = true;
			} else {
				result = false;
			}
			statement.close();
		} catch (SQLException e) {
			logger.error("TrendAnalysisDAO.retrieveTableRows(): " + sql
					+ " Exception:", e);
			e.printStackTrace();
			// result = null;
			throw e;
		} catch (Exception e) {
			logger.error("TrendAnalysisDAO.retrieveTableRows(): " + sql
					+ " Exception:", e);
			e.printStackTrace();
			// result = null;
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.retrieveTableRows() - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return result;
	}
	
	public void deleteTableRows(String counterId, String date, String all ,String tableName) throws Exception {
		
		Connection connection = null;
		String sql ="";
		if(all==null)
		sql= "delete from "+tableName+" where COUNTER_ID = '"+counterId+"' and DATE_TIME = to_date('"+date+"', 'MM/dd/yyyy')";
		else
			sql= "delete from "+tableName;
		try {
			connection = TrendAnalysisDAO.getConnection(logger);
			Statement statement = connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			statement.executeQuery(sql);
			statement.close();
		} catch (SQLException e) {
			logger.error("TrendAnalysisDAO.deleteTableRows(): " + sql
					+ " Exception:", e);
			e.printStackTrace();
			// result = null;
			throw e;
		} catch (Exception e) {
			logger.error("TrendAnalysisDAO.deleteTableRows(): " + sql
					+ " Exception:", e);
			e.printStackTrace();
			// result = null;
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
					logger
					.debug("TrendAnalysisDAO.deleteTableRows() - close Database Connection");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
}
