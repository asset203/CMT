package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.GenericMapping;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.DataColumnType;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

import static com.itworx.vaspp.datacollection.util.Utils.convertToDateString;

public class GenericPersistenceManager {

	static private Logger mainLogger = DataCollectionManager.getLogger();
	private static final  String NODE_LABEL =" and node : ";
	private PersistenceManager persistenceManager;
	
	private Session currentSession = null;
	Connection connection = null;

    private Session currentCmtSession = null;
    Connection cmtSchemaConnection = null;

	public GenericPersistenceManager(PersistenceManager persistenceManager) throws ApplicationException {
		mainLogger
				.debug("GenericPersistenceManager.GenericPersistenceManager() - initiating GenericPersistenceManager");
		this.persistenceManager = persistenceManager;
		mainLogger.debug("GenericPersistenceManager.GenericPersistenceManager() - finished initiating GenericPersistenceManager");
	}

	public void persistResultSet(InputData inputData, ResultSet resultSet,
			GenericMapping outputMapping,boolean hourly) {
		mainLogger.info("GenericPersistenceManager.persistResultSet() - start persistResultSet(system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
		currentSession = persistenceManager.getNewSession();
		connection = currentSession.connection();
        PreparedStatement pstmt=null;
		try {
			try{
				if(connection.isClosed()){
					connection = persistenceManager.getNewSession().connection();
					mainLogger.info("GenericPersistenceManager.persistResultSet() - getting new connection and its status closed and creating new and its status ("+connection.isClosed()+")one (system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
				}
			} catch(SQLException e){
				mainLogger.error("GenericPersistenceManager.persistResultSet() - error while checking connection status for (system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )",e);
				throw new ApplicationException("error while checking connection status"+ e);
			}

			if (resultSet != null) {
				mainLogger.info("GenericPersistenceManager.persistResultSet() - started to insert resultset for (system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
				connection.setAutoCommit(false);
				deleteOldData(connection, inputData, outputMapping, hourly);
				mainLogger.info("GenericPersistenceManager.persistResultSet() - old data deleted for (system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
				
				ResultSetMetaData metaData = resultSet.getMetaData();
				String insertSQL = getInsertStatement(metaData, outputMapping);
			    pstmt = connection.prepareStatement(insertSQL);

				int noOfColumns = metaData.getColumnCount();
				boolean hasRows = false;
				int rowsCounter=0;
				int validRows = 0;
				int batchSize=persistenceManager.getBatchSize();
				while (resultSet.next()) {
					String tmp = "";
					boolean validRow = true;
					int idx = 1;
					for (int i = 0; i < noOfColumns; i++) {
						String resultFieldName = metaData.getColumnName(i + 1);
						String outColName = outputMapping
								.getOutputColumnName(resultFieldName);
						if (outColName != null) {
							tmp = tmp + "[" + outColName + "="
									+ resultSet.getString(resultFieldName)
									+ "]";
							String resultFieldType = Utils
									.resolveSQLType(metaData
											.getColumnType(i + 1));

							if (resultFieldType.equals("float")) {
								pstmt.setDouble(idx, resultSet
										.getFloat(resultFieldName));
							} else if (resultFieldType.equals("number")) {
								pstmt.setLong(idx, resultSet
										.getLong(resultFieldName));
							} else if (resultFieldType.equals("date")) {
								String dateColumn = outputMapping
										.getDateColumnName();
								String currentDateColumn = outputMapping
										.getOutputColumnName(resultFieldName);
								if (dateColumn != null
										&& currentDateColumn.equals(dateColumn)) {
									Calendar c1 = Calendar.getInstance();
									c1.setTime(resultSet
											.getTimestamp(resultFieldName));
									
									c1.set(Calendar.MINUTE, 0);
									c1.set(Calendar.SECOND, 0);
									c1.set(Calendar.MILLISECOND, 0);
									Calendar c2 = Calendar.getInstance();
									c2.setTime(inputData.getDate());
									
									c2.set(Calendar.MINUTE, 0);
									c2.set(Calendar.SECOND, 0);
									c2.set(Calendar.MILLISECOND, 0);
									if(!hourly){
										c1.set(Calendar.HOUR_OF_DAY, 0);
										c2.set(Calendar.HOUR_OF_DAY, 0);
									}
									if (c1.compareTo(c2) != 0) {
										validRow = false;
									}
								}
								pstmt.setTimestamp(idx, resultSet
										.getTimestamp(resultFieldName));
							} else if (resultFieldType.equals("string")) {
								pstmt.setString(idx, resultSet
										.getString(resultFieldName));
							}
							idx++;
						}
					}
					if (outputMapping.getNodeColumnName() != null) {
						pstmt.setString(idx, inputData.getNodeName());
					}
					if (validRow) {
						//pstmt.executeUpdate();
						validRows++;
						pstmt.addBatch();
						hasRows = true;
					}
					// System.out.println("inserted data : "+tmp);
					rowsCounter++;
					if(rowsCounter>=batchSize){
						pstmt.executeBatch();
						pstmt.clearBatch();
						rowsCounter=0;
                        validRows =0;
					}
					mainLogger.debug("GenericPersistenceManager.persistResultSet() - finished to insert resultSet inserted : ["
                            +validRows+"] out of : ["+rowsCounter+"] for (system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
				}
				if(!hasRows){

					ApplicationException e = new ApplicationException("no valid rows found in temporary result set");
					mainLogger.error("GenericPersistenceManager.persistResultSet() - ", e);
					throw e;
				}
				pstmt.executeBatch();
				pstmt.clearBatch();
				if(!hourly){
					persistenceManager.updateGeneralEvents(connection, inputData);
                }
				connection.commit();
				persistenceManager.refreshViews(connection, inputData, outputMapping.getTableName());
				connection.setAutoCommit(true);
			}
		} catch (Exception ex) {
			mainLogger.error("GenericPersistenceManager.persistResultSet() - error occurred : ", ex);
            try{
				connection.rollback();
			} catch(Exception e){
				mainLogger.error("GenericPersistenceManager.persistResultSet() - error occurred while rollback transaction ", e);
			}
            mainLogger.error("GenericPersistenceManager.persistResultSet() - error occurred while persisting ResultSet ", ex);
		}
        finally {
                try {
                   if(pstmt!=null){
                        pstmt.close();
                   }
                } catch (SQLException e){
                    mainLogger.error("failed to close result Statement" + e );
                }
                finally
                {
                    try {
                        if(resultSet!=null){
                            resultSet.close();
                        }
                    }catch (Exception e) {
                        mainLogger.error("Failed to close result set ",e);
                    }
                }
        }
		mainLogger.info("GenericPersistenceManager.persistResultSet() - Finish persistResultSet(system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
	}

	private void deleteOldData(Connection connection, InputData inputData,
			GenericMapping mapping, boolean hourly) throws ApplicationException {
		persistenceManager.deleteOldData(connection, 
				inputData.getSystemName(),
				mapping.getTableName() ,
				inputData.getDate(), 
				inputData.getNodeName(), 
				mapping.getNodeColumnName(), 
				mapping.getDateColumnName(), 
				hourly);
	}
	

	/**
	 * clear data and close session
	 * 
	 * @param //attrs
	 *            - Object holding persistence attributes
	 */
	public void clear() {
		mainLogger.info("GenericPersistenceManager.clear() - start clearing sessions");
		if (currentSession != null && currentSession.isOpen()) {
			if (currentSession.getTransaction() != null
					&& currentSession.getTransaction().isActive()) {
				currentSession.clear();
			}
			if (currentSession.isOpen()) {
				currentSession.close();
			}
		}

        if (currentCmtSession != null && currentCmtSession.isOpen()) {
            if (currentCmtSession.getTransaction() != null
                    && currentCmtSession.getTransaction().isActive()) {
                currentCmtSession.clear();
            }
            if (currentCmtSession.isOpen()) {
                currentCmtSession.close();
            }
        }
		mainLogger.info("GenericPersistenceManager.clear() - finish clearing sessions");
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
			mainLogger.error("GenericPersistenceManager.clear() - error while closing connection",e);
		}
        try {
            if(cmtSchemaConnection != null && !cmtSchemaConnection.isClosed()){
                cmtSchemaConnection.close();
            }
        } catch (SQLException e) {
            mainLogger.error("GenericPersistenceManager.clear() - error while closing connection",e);
        }
	}

	private String getInsertStatement(ResultSetMetaData metaData,
			GenericMapping outputMapping) throws SQLException {

		String insertClause = "";
		String valuesClause = "";
		int noOfColumns = metaData.getColumnCount();

		for (int i = 0; i < noOfColumns; i++) {
			String resultFieldName = metaData.getColumnName(i + 1);
			String outColName = outputMapping
					.getOutputColumnName(resultFieldName);
			if (outColName != null) {
				insertClause += outColName + ",";
				valuesClause += "?,";
			}
		}
		if (insertClause.endsWith(",")){
			insertClause = insertClause.substring(0, insertClause.length() - 1);
        }
		if (valuesClause.endsWith(",")){
			valuesClause = valuesClause.substring(0, valuesClause.length() - 1);
        }
		if (outputMapping.getNodeColumnName() != null) {
			insertClause += "," + outputMapping.getNodeColumnName();
			valuesClause += ",?";
		}
		return "INSERT INTO " + outputMapping.getTableName() + " ("
				+ insertClause + ") VALUES (" + valuesClause + ")";
	}
	


    public int getBatchSize() {
        return persistenceManager.getBatchSize();
    }

    public void performInsertSelect(InputData inputData,VInputStructure inputStructure,
                                    boolean hourly) throws ApplicationException, InputException {
        Logger logger = Logger.getLogger(inputData.getSystemName());
        logger.info("GenericPersistenceManager.performInsertSelect() - start performInsertSelect(system:"+inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+" )");
        String insertSelectQuery = Utils.getInsertSelectQuery
                (inputStructure, inputData);
        currentCmtSession   = persistenceManager.getNewCmtSchemaSession();
        cmtSchemaConnection = currentCmtSession.connection();

        Statement s=null;
        try {

            if(cmtSchemaConnection != null && cmtSchemaConnection.getMetaData() != null){
                if(logger.isDebugEnabled()){
                    logger.debug("DB connection username: " + cmtSchemaConnection.getMetaData().getUserName());
                }
            }
            cmtSchemaConnection.setAutoCommit(false);
             //1- truncate old data
            if(inputStructure.isTruncateBeforeInsertion()){
                persistenceManager.deleteOldData(cmtSchemaConnection, inputData.getSystemName(),
                        inputStructure.getMappedTable(), inputData.getDate(), inputData.getNodeName(),
                        inputStructure.getNodeColumn(),inputStructure.getDateColumn() , hourly);
            }
            //2-    insert new data
            s = cmtSchemaConnection.createStatement();
            logger.debug("================================= SQL STATEMENT=======================");
            logger.debug(insertSelectQuery);
            logger.debug("=======================================================================");
            int numberOfRecordsInserted = s.executeUpdate(insertSelectQuery);

            //3- if no data inserted, roll back deleting old data
            if(numberOfRecordsInserted == 0){
                try {
                    if(s != null ){
                        s.close();
                    }
                } catch (SQLException e) {
                    mainLogger.error("failed to close statement",e);
                }
                cmtSchemaConnection.rollback();
                logger.error("GenericPersistenceManager.performInsertSelect() for node " +
                        inputData.getNodeName() + " - No data found in Text for given date ["
                        + convertToDateString(inputData.getDate(), Utils.defaultFormat) + "] for input id [" +
                        inputData.getInputID() + "] for input structure id["+inputStructure.getId()+"]");
                throw new InputException("GenericPersistenceManager.performInsertSelect() for node " +
                        inputData.getNodeName() + " - No data found in Text for given date ["
                        + convertToDateString(inputData.getDate(), Utils.defaultFormat) + "] for input id [" +
                        inputData.getInputID() + "] for input structure id["+inputStructure.getId()+"]");
            }
            //4- if data inserted, execute update events and  commit
            else{
                if(hourly){
                    persistenceManager.updateHourlyEvents(cmtSchemaConnection, inputData, inputStructure.getMappedTable());
                } else{
                    persistenceManager.updateGeneralEvents(cmtSchemaConnection, inputData);
                }
                cmtSchemaConnection.commit();
                cmtSchemaConnection.setAutoCommit(true);
            }
             //5-
             persistenceManager.refreshViews(cmtSchemaConnection, inputData, inputStructure.getMappedTable());
        } catch (Exception e) {
            boolean logException = true;
            if(e.getCause() instanceof SQLException)
            {
                SQLException sqlException =(SQLException) e.getCause();
                String exMsg=sqlException.getMessage();
                  if(  "42000".equalsIgnoreCase( sqlException.getSQLState()) &&
                          (null!= exMsg && exMsg.contains("\"DATE_TIME\": invalid identifier") && exMsg.contains("UPDATE_HOURLY_EVENTS")))
                    {
                        logger.debug("========== Update Hourly Events known Error ==========");
                        logger.debug(" stored procedure [update_hourly_events] attempted to delete data from dc table[" +
                                inputStructure.getMappedTable() +"] and table do not have [DATE_TIME] column");
                        logException=false;
                    }
            }
            try{
                logger.debug("========= Rolling Back Transaction============");
                cmtSchemaConnection.rollback();
            } catch(Exception ex){
                logger.error("GenericPersistenceManager.persistResultSet() - error occurred while rollback transaction ", ex);
            }
            if(logException)
            {
                logger.error("Failed to insert data for  input structure: "+inputStructure.getId()
                    +" ,input: "+ inputData.getInputID()+" ,node: "+inputData.getNodeName()+" ,system: "+inputData.getSystemName());
                logger.error(e);
                throw new ApplicationException(e);
            }
        }finally {
             try {
                 if(s != null ){
                     s.close();
                 }
             } catch (SQLException e) {
                 logger.error("failed to close statement",e);
             }
        }
        logger.info("GenericPersistenceManager.performInsertSelect() - finish persistResultSet(system:"
                +inputData.getSystemName()+NODE_LABEL+inputData.getNodeName()+ "Input: "+ inputData.getInputID()+" )");
    }

    private boolean saveFileDataToOutputTable(File aFile,InputData inputData,VInputStructure inputStructure,NamedParameterStatement preparedStmt) throws IOException, SQLException, InputException {
        int noOfColumns = inputData.getHeader().length;
        int maxColumnIndex =  Utils.getMaxIndex(inputData.getHeader());
        int rowsCounter = 0;
        boolean dataInserted = false;
        BufferedReader inputStream = Utils.getGZIPAwareBufferdReader( aFile);
        String dataLine = null;
        while ((dataLine = inputStream.readLine()) != null) {
            dataLine=new String (dataLine.getBytes("UTF-8"), "UTF-8");
            String[] dataLineSplit = dataLine.split(",");
            if (dataLineSplit.length == 0 || dataLineSplit.length < noOfColumns  || noOfColumns < maxColumnIndex) {
                break;
            }
            assignStatementParameters(preparedStmt, inputStructure.getColumns(), dataLineSplit,
                    inputStructure.getAutoFilledDateColumn(),inputData.getDate() );
            rowsCounter++;

            if (rowsCounter >= getBatchSize()) {
                preparedStmt.executeBatch();
                preparedStmt.clearBatch();
                rowsCounter = 0;
            }
            dataInserted =true;
        }
        inputStream.close();
        preparedStmt.executeBatch();
        preparedStmt.clearBatch();
        return dataInserted;
    }
    public void persistConverterResults(File[] outputFiles,VInputStructure inputStructure,InputData inputData,Logger logger,boolean isHourly ) throws ApplicationException, InputException {
        NamedParameterStatement preparedStmt = null;
        try {
            //1- get connection and prepare insert statement
            currentCmtSession = persistenceManager.getNewCmtSchemaSession();
            cmtSchemaConnection = currentCmtSession.connection();
            cmtSchemaConnection.setAutoCommit(false);
            if(cmtSchemaConnection != null && cmtSchemaConnection.getMetaData() != null){
                if(logger.isDebugEnabled()){
                    logger.debug("DB connection username: " + cmtSchemaConnection.getMetaData().getUserName());
                }
            }
                String namedSql = Utils.getInsertQuery(inputStructure.getMappedTable(), inputStructure.getColumns(),
                        inputStructure.getNodeColumn(), inputStructure.getIdColumn(), inputStructure.getSeqName(),
                        inputData.getNodeName(), ":",inputStructure.getAutoFilledDateColumn() );
                preparedStmt = new NamedParameterStatement(cmtSchemaConnection, namedSql);

            //2-delete old data
               if(inputStructure.isTruncateBeforeInsertion()){
                    persistenceManager.deleteOldData(cmtSchemaConnection, inputData.getSystemName(),
                            inputStructure.getMappedTable(), inputData.getDate(), inputData.getNodeName(),
                            inputStructure.getNodeColumn(),inputStructure.getDateColumn() , isHourly);
                }
            //3- persist new data
                boolean dataInserted =false;
                for (int j = 0; j < outputFiles.length; j++) {
                         dataInserted = saveFileDataToOutputTable
                                (outputFiles[j],inputData,inputStructure,preparedStmt);
                }
                if(dataInserted){
                    //5- execute update events and commit
                        if(isHourly){
                            persistenceManager.updateHourlyEvents(cmtSchemaConnection, inputData, inputStructure.getMappedTable());
                        } else{
                            persistenceManager.updateGeneralEvents(cmtSchemaConnection, inputData);
                        }
                    cmtSchemaConnection.commit();
                }
                else{
                    //5- OR rollback
                    cmtSchemaConnection.rollback();
                    logger.error("GenericPersistenceManager.persistConverterResults() for input "
                            + inputData.getInputID() + " - No data found in xml for given date ["
                            + Utils.convertToDateString(inputData.getDate(), Utils.defaultFormat) + "] for input id ["
                            + inputData.getInputID() + "]");
                }
            //6- execute refresh views
            persistenceManager.refreshViews(cmtSchemaConnection, inputData, inputStructure.getMappedTable());

        }catch (SQLException e) {
            try{
                logger.debug("========= Rolling Back Transaction============");
                cmtSchemaConnection.rollback();
            } catch(Exception ex){
                logger.error("GenericPersistenceManager.persistConverterResults() - error occurred while rollback transaction ", ex);
            }
            String exMsg=e.getMessage();
            if("42000".equalsIgnoreCase( e.getSQLState()) &&
                    (null!= exMsg && exMsg.contains("\"DATE_TIME\": invalid identifier")
                            && exMsg.contains("UPDATE_HOURLY_EVENTS")))
                {
                    logger.info("========== Update Hourly Events known Error =========="
                    + "\n stored procedure [update_hourly_events] attempted to delete data from dc table[" +
                            inputStructure.getMappedTable() +"] and table do not have [DATE_TIME] column");
                }
                else
                {
                    logger.error("GenericPersistenceManager.persistConverterResults() for node " + inputData.getNodeName()
                            + " - Error getting data of xml input", e);
                    throw new ApplicationException( "GenericPersistenceManager.persistConverterResults() for input "
                            + inputData.getInputID() + "- Error getting data of xml input - " + e);
                }
        }catch (Exception e) {
            try{
                logger.debug("========= Rolling Back Transaction============");
                cmtSchemaConnection.rollback();
            } catch(Exception ex){
                logger.error("GenericPersistenceManager.persistConverterResults() - error occurred while rollback transaction ", ex);
            }
            logger.error("GenericPersistenceManager.persistConverterResults() for node " + inputData.getNodeName() + " - Error getting data of xml input", e);
            throw new InputException( "GenericPersistenceManager.persistConverterResults() for input " + inputData.getInputID() + "- Error getting data of xml input - " + e);
        }
        finally {
            if (preparedStmt != null) {
                try {
                    preparedStmt.close();
                } catch (Exception e) {
                    logger.error("GenericPersistenceManager.persistConverterResults() - input : " +  inputData.getInputID() + " Couldn't close preparedStmt statement", e);
                }
            }
        }
    }
    private  void assignStatementParameters(NamedParameterStatement preparedStmt,
                                            DataColumn[] columns, String[] dataRow, String autoFilledDateColumnName, Date targetDate) throws InputException, SQLException {

        for (DataColumn column : columns) {
            String columnValue = dataRow[column.getIndex()];
            String columnName = column.getName();
            if (columnValue == null) {
                if (DataColumnType.DATE.getTypeCode() == column.getTypeCode()) {
                    preparedStmt.setDate(columnName, null);
                } else if (DataColumnType.NUMBER.getTypeCode() == column.getTypeCode()) {
                    preparedStmt.setNull(columnName, Types.BIGINT);
                } else if (DataColumnType.FLOAT.getTypeCode() == column.getTypeCode()) {
                    preparedStmt.setNull(columnName, Types.DECIMAL);
                } else {
                    preparedStmt.setString(columnName, null);
                }
            } else {
                if (DataColumnType.DATE.getTypeCode() == column.getTypeCode()) {
                    Date dateValue = Utils.convertToDate
                            (columnValue,StringUtils.hasText(column.getDateFormat())?column.getDateFormat():"MM/dd/yyyy HH:mm:ss");
                    preparedStmt.setObject(columnName, Utils.sqlTimestamp(dateValue));
                }else if(DataColumnType.NUMBER.getTypeCode() == column.getTypeCode())
                {
                     preparedStmt.setObject(columnName,columnValue,Types.BIGINT);

                } else if(DataColumnType.FLOAT.getTypeCode() == column.getTypeCode()){
                    preparedStmt.setObject(columnName,columnValue,Types.FLOAT);
                }
                else if(DataColumnType.STRING.getTypeCode() == column.getTypeCode()){
                    preparedStmt.setString(columnName,columnValue);
                }
                else {
                    preparedStmt.setObject(columnName, columnValue);
                }
            }
        }
        if(!Utils.isEmpty(autoFilledDateColumnName)){
            preparedStmt.setObject(autoFilledDateColumnName, Utils.sqlTimestamp(targetDate));
        }
        preparedStmt.addBatch();
    }
}
