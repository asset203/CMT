/*
 * File:       DBInputDAO.java
 * Date        Author          Changes
 * 16/01/2006  Nayera Mohamed  Created
 * 15/03/2006  Nayera Mohamed  Updated to support both SQL & Oracle DB
 *
 * Responsible for retrieving Data in case of DB Input
 */
package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.PersistenceAttributes;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.DBInputStructure;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.VInput;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class DBInputDAO implements InputDAO {
	protected Logger logger;
	protected PersistenceManager persistenceManager;
	Vector dateIndex = new Vector();

	public DBInputDAO(PersistenceManager persistenceManager) {
    this.persistenceManager = persistenceManager;
	}

	/**
	 * create connection to the input DB using the appropriate Driver
	 * 
	 * @param input -
	 *            The input object
	 * 
	 * @return Connection - The Connection created with input DB
	 * @exception InputException
	 *                if SQL error occured while creating connection
	 * @exception ApplicationException
	 *                if couldn't instantiate DB driver if error with DB driver
	 */
	protected Connection getConnection(VInput input) throws InputException,
			ApplicationException {
		logger
				.debug("DBInputDAO.getConnection() - started getConnection for input id="
						+ input.getId());
		String driverName = "";
		try {
			DBInputStructure inputStructure = (DBInputStructure) input
					.getInputStructure();
			driverName = inputStructure.getDriver();
			DriverManager.registerDriver((Driver) Class.forName(driverName)
					.newInstance());
			String DBURL = null;
			if (driverName.indexOf("oracle") != -1) {
                DBURL = input.getPaths()[0] + "@" + input.getServer() ;

                //this condition should be false for "Oracle-Rac" inputs
                if(!input.getInputName().startsWith("NON") ){
                        if(input.getInputName().startsWith("/")) {
                                DBURL = DBURL + input.getInputName();
                        }else
                        {
                                DBURL = DBURL + ":" + input.getInputName();
                        }
               }

			} else if (driverName.indexOf("SQLServerDriver") != -1) {
				DBURL = input.getPaths()[0] + "//" + input.getServer() + ";user="
						+ input.getUser() + ";password=" + input.getPassword()
						+ ";DatabaseName=" + input.getInputName();
			} else if (driverName.indexOf("mysql") != -1) {
				DBURL = input.getPaths()[0] + "//" + input.getServer() + ":3306/"
				+ input.getInputName();
			} else if (driverName.indexOf("sybase") != -1) {
				DBURL =input.getPaths()[0]+ input.getServer()+ "/" +input.getInputName();
			}
            else if(driverName.indexOf("db2") != -1){ /*Add by Basma.Alkerm, 8/4/2013*/
                /*
                * path = jdbc:db2:
                * server = <host>[:<port>]
                * input name =  <database_name>
                * */
                DBURL =input.getPaths()[0]+input.getServer()+"/"+input.getInputName();
            }
			System.out.println("DBInputDAO.getConnection() - DB url: " + DBURL);
			logger.debug("DBInputDAO.getConnection() - DB url: " + DBURL);
			if (DBURL == null) {
				throw new ApplicationException("-"+ input.getNodeName() +"- undefined DB Driver "
						+ driverName);
			}
			Connection connection = DriverManager.getConnection(DBURL, input
					.getUser(), input.getPassword());
			logger
					.debug("DBInputDAO.getConnection() - finished getConnection for input id="
							+ input.getId());
			return connection;
		} catch (InstantiationException e) {
			logger
					.error("-"+ input.getNodeName() +"- DB-1001: DBInputDAO.getConnection() - Couldn't instantiate DB driver: "
							+ driverName + ":" + e);
			e.printStackTrace();
			throw new ApplicationException("" + e);
		} catch (SQLException e) {
 
			logger
					.error("-"+ input.getNodeName() +"- DB-1002:DBInputDAO.getConnection() - SQL error while creating connection"
							+ e);
			e.printStackTrace();
			throw new InputException("" + e);
		} catch (IllegalAccessException e) {
			logger.error("-"+ input.getNodeName() +"- DB-1001:DBInputDAO.getConnection() - Error with DB driver "
					+ driverName + ":" + e);
			throw new ApplicationException("" + e);
		} catch (ClassNotFoundException e) {
			logger
					.error("-"+ input.getNodeName() +"- DB-1004: DBInputDAO.getConnection() - Couldn't find DB driver class "
							+ driverName + ":" + e);
			throw new ApplicationException("" + e);
		}
	}

	/**
	 * execute sql statement and retrieve input into InputData object
	 * 
	 * @param input -
	 *            The input object
	 * @param targetDate -
	 *            The target date for datacollection
	 * 
	 * @return InputData - The object holding data read from DB
	 * @exception InputException
	 * @exception ApplicationException
	 * 
	 */
	public void retrieveData(VInput input, Date targetDate)
			throws InputException, ApplicationException {
		retrieveData(input, targetDate, false);
	}
	
	/**
	 * execute sql statement and retrieve input into InputData object
	 * 
	 * @param input -
	 *            The input object
	 * @param targetDate -
	 *            The target date for datacollection
	 * 
	 * @return InputData - The object holding data read from DB
	 * @exception InputException
	 * @exception ApplicationException
	 * 
	 */
	protected void retrieveData(VInput input, Date targetDate,boolean hourly)
			throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger
				.debug("DBInputDAO.retrieveHourlyData() - started retrieveData for inputid:"
						+ input.getId() + " targetDate:" + targetDate);
       try{
            if(logger.isDebugEnabled()){
                logger.debug(" input id == " +input.getId());
                logger.debug("input structure id == " + input.getInputStructure().getId());
                logger.debug("input structure sql::" + input.getInputStructure().getExtractionSql());

            }
       }catch (Exception e){
           logger.error("error occurred while logging",e);
       }

		InputData inputData = new InputData();
		
		inputData.setInputID(input.getId());
		inputData.setNodeName(input.getNodeName());
		inputData.setSystemName(input.getSystemName());
		inputData.setDate(targetDate);
		Connection connection = null;
    PersistenceAttributes attrs=null;
		try {
      attrs = persistenceManager.getPersistenceAttributes(inputData);
			DBInputStructure inputStructure = (DBInputStructure) input
					.getInputStructure();
			inputData.setInputStructureId(inputStructure.getId());
			connection = this.getConnection(input);
			Statement statement = connection.createStatement();
			String sql = resolveDateVariable(inputStructure.getSqlStatement(),
					targetDate);
			ResultSet selectResults = statement.executeQuery(sql);

			// get header from node and set in input data object
			DataColumn[] header = this.getHeader(selectResults);
			inputData.setHeader(header);
			// read data from resultset and collect in vector then pass to input
			// data object
			Vector data = new Vector();
			int noOfColumns = header.length;
			boolean found = false;
			outer: while (selectResults.next()) {
				found = true;
				String dataRow[] = new String[noOfColumns];
				for (int i = 0; i < noOfColumns; i++) {
					dataRow[i] = selectResults.getString(i + 1);
					if (dataRow[i] == null) {
						logger
								.debug("DBInputDAO.retrieveData() - encountered null value");
						continue outer;
					}
				}
				for (int i = 0; i < dateIndex.size(); i++) {
					int index = ((Integer) dateIndex.get(i)).intValue();
					Date dateString = selectResults.getTimestamp(index + 1);
					if (dateString == null) {
						logger
								.debug("DBInputDAO.retrieveData() - encountered null value");
						continue outer;
					}
					// System.out.println("date"+ dateString);
					SimpleDateFormat f2 = new SimpleDateFormat();
					f2.applyPattern("MM/dd/yyyy HH:mm:ss");
					StringBuffer s = f2.format(dateString, new StringBuffer(),
							new FieldPosition(f2.DATE_FIELD));
					dataRow[index] = "" + s;
				}
				persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
			}
			if (!found) {
        logger.error("-"+ input.getNodeName() +"- DB-1000:DBInputDAO.retrieveData() - No data found in DB for given date for "+inputStructure.getId());
				throw new InputException(
						"-"+ input.getNodeName() +"- DBInputDAO.retrieveData() - no data found in DB for given date for "+inputStructure.getId());
			}
      else
        {
          persistenceManager.persistObjects(inputData,attrs,hourly);
        }
			//inputData.setData(data);
			connection.close();
		} catch (SQLException e) {
      logger.error("-"+ input.getNodeName() +"- DB-1005: DBInputDAO.retrieveData() - Error getting data of DB input "+inputData.getInputStructureId());
			logger
					.error("-"+ input.getNodeName() +"- DBInputDAO.retrieveData() - error getting data of DB input "+inputData.getInputStructureId(), e);
			throw new InputException("" + e);
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
        if ( attrs != null ) {
        persistenceManager.clear(attrs);
        }
			} catch (SQLException e) {
        logger.error("-"+ input.getNodeName() +"- DB-1006: DBInputDAO.retrieveData() - Couldn't close connection");
			}
		}
		logger
				.debug("DBInputDAO.retrieveData() - finished retrieveData for inputid:"
						+ input.getId() + " targetDate:" + targetDate);
	}

	/**
	 * create the header array of DataColumn from the ResultSet
	 * 
	 * @param selectResults -
	 *            the ResultSet Object containing Input Data
	 * 
	 * @return DataColumn[] - the array holding header data
	 * @exception InputException
	 *                if an error occured while retrieving metadata of ResultSet
	 */
	private DataColumn[] getHeader(ResultSet selectResults)
			throws InputException {
		logger.debug("started getHeader for DB Input");
		try {
			ResultSetMetaData metaData = selectResults.getMetaData();
			int noOfColumns = metaData.getColumnCount();
			DataColumn[] header = new DataColumn[noOfColumns];
			for (int i = 0; i < noOfColumns; i++) {
				DataColumn column = new DataColumn();
				column.setName(metaData.getColumnName(i + 1));
				column.setType(Utils.resolveSQLType(metaData.getColumnType(i + 1)));
				if (column.getType().equals("date")) {
					dateIndex.addElement(new Integer(i));
				}
				header[i] = column;
			}
			logger
					.debug("DBInputDAO.getHeader() - finished getHeader for DB Input");
			return header;
		} catch (SQLException e) {
			logger
					.error("DB-1005: DBInputDAO.getHeader() - Error getting header of DB input");
			throw new InputException("" + e);
		}
	}

	/**
	 * resolve the date variable in the query
	 * 
	 * @param sql -
	 *            the sql query containing the date variable
	 * @param targetDate -
	 *            the date to replace the variable
	 * @return String - the resolved query
	 */
	protected String resolveDateVariable(String sql, Date targetDate) {
		logger=Logger.getLogger(this.getClass());
		logger.debug("DBInputDAO.resolveDateVariable() - started resolveDate ("
				+ sql + " , " + targetDate + ")");
		String newSQL = sql;
		int dateBeginIndex;
		do {
			dateBeginIndex = newSQL.indexOf("$date");
			if (dateBeginIndex == -1) {
				break;
			}
			int dateEndIndex = newSQL.indexOf(")", dateBeginIndex);
			String format = newSQL.substring(newSQL
					.indexOf("(", dateBeginIndex) + 1, dateEndIndex);
			
			long date = targetDate.getTime();
			SimpleDateFormat f = new SimpleDateFormat();
			format = format.replaceAll("m", "M");
			
			f.applyPattern(format);
			StringBuffer s = f.format(targetDate, new StringBuffer(),
					new FieldPosition(f.DATE_FIELD));
			String dateString = s.toString();
			newSQL = newSQL.substring(0, dateBeginIndex) + dateString
					+ newSQL.substring(dateEndIndex + 2, newSQL.length());
		} while (dateBeginIndex != -1);
		logger.debug("resolved " + sql + "to" + newSQL);
		logger
				.debug("DBInputDAO.resolveDateVariable() - finished resolveDate ("
						+ newSQL + " , " + targetDate + ")");
		return newSQL;
	}

	public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
		retrieveData(input, targetDate, true);
	}
	
	public static void main(String[] args) {
		String query= "SELECT TO_DATE (Hour, 'yyyy-mm-dd HH24') AS HOUR,Channel AS CHANNEL,Number_Of_Requests AS NUMBER_OF_REQUESTS FROM RX_SUB_REQUESTS_VW WHERE Hour = '$date(yyyy-mm-dd HH)$'";
		DBInputDAO dao = new DBInputDAO(null);
		Calendar c= Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR,5);
		//String sql = dao.resolveDateVariable(query, c.getTime());
		System.out.println(query);
		//System.out.println(sql);
	}

}