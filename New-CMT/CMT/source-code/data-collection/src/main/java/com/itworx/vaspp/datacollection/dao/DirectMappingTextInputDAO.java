package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dynacode.DynaCode;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.*;
import com.itworx.vaspp.datacollection.util.converters.AbstractTextConverter;
import com.itworx.vaspp.datacollection.util.converters.TextConverter;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.TextInputStructure;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DirectMappingTextInputDAO extends TextInputDAO {

	private static DynaCode dynaCode = new DynaCode();
	
	public DirectMappingTextInputDAO(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public void retrieveData(VInput input, Date targetDate, boolean hourly) throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("DirectMappingTextInputDAO.retrieveData() - started retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);
		Session session = null;
		Connection outConnection = null;
		NamedParameterStatement preparedStmt = null;
		VInputStructure[] inputStructures = input.getInputStructures();
		int batchSize = persistenceManager.getBatchSize();
		try {
			File[] originalInputFiles = new FileHandler().getFiles(input);
			for (int z = 0; z < inputStructures.length; z++) {
				boolean foundData = false;
				try {
					session = persistenceManager.getNewSession();
					outConnection = session.connection();
					outConnection.setAutoCommit(false);
					
					TextInputStructure inputStructure = (TextInputStructure) inputStructures[z];
					
					InputData inputData = new InputData();
					
					String dateFormat=inputStructure.getDateFormat();
					
					String inputID = inputStructure.getId();
					inputID = inputID.substring(0, inputID.lastIndexOf("_struct"));
					inputData.setInputStructureId(inputStructure.getId());
					
					inputData.setInputID(inputID);
					inputData.setNodeName(input.getNodeName());
					inputData.setSystemName(input.getSystemName());
					inputData.setDate(targetDate);
					
					
					String tableName = inputStructure.getMappedTable();
					String nodeColumn = inputStructure.getNodeColumn();
					String idColumn = inputStructure.getIdColumn();
					String dateColumn = inputStructure.getDateColumn();
					
					// get header from node and set in input data object
					DataColumn[] header = inputStructure.getColumns();
					inputData.setHeader(header);
					String converterName = inputStructure.getConverter();
					TextConverter converter = DynaCodeUtils.getDynaTextConverter(dynaCode, converterName);
					if(converter instanceof AbstractTextConverter){
						((AbstractTextConverter)converter).setParametersMap((HashMap<String,String>)inputStructure.getParametersMap());
					}
					File[] inputFiles = converter.convert(originalInputFiles, input.getSystemName());
					
					String namedSql = Utils.getInsertQuery(tableName, header,nodeColumn, idColumn,
                            inputStructure.getSeqName(),inputData.getNodeName(), ":",null );
					logger.info( "DirectMappingTextInputDAO.retrieveData(),INSERT SQL :: " );
                    logger.info(namedSql);
                    preparedStmt = new NamedParameterStatement(outConnection, namedSql);
					
					if(inputStructure.isUseUpdateEvent()){
						if(hourly){
							persistenceManager.updateHourlyEvents(outConnection, inputData, tableName);
						} else {
							persistenceManager.updateEvents(outConnection, inputData, tableName);
						}
					} else {
						persistenceManager.deleteOldData(outConnection, input.getSystemName(), tableName, targetDate, input.getNodeName(), nodeColumn, dateColumn, hourly);
						if(hourly){
							persistenceManager.updateHourlyEvents(outConnection, inputData,tableName);
						} else {
							persistenceManager.updateGeneralEvents(outConnection, inputData);
						}
					}
					int noOfColumns = header.length;
					int maxColumnLen = getMaxIndex(header);
					int rowsCounter = 0;
					for (int j = 0; j < inputFiles.length; j++) {
                        logger.info("DirectMappingTextInputDAO.retrieveData(): Start Parsing file [ "+inputFiles[j].getName()+" ]");
						Reader reader = new InputStreamReader(new FileInputStream(inputFiles[j]), "UTF-8"); 
						BufferedReader inputStream = new BufferedReader(reader);
						if(inputStructure.getLastCallClassName()!=null) {
							inputData.setLastCallClassName(inputStructure.getLastCallClassName());
						}
						if (inputStructure.isExtractDate()) {
							if(inputStructure.isExtractDateMonthly()) {
								inputData.setExtractDateMonthly(true);
							}
							int dateIndex = -1;
							// find the index of the column of type date
							for (int w = 0; w < noOfColumns; w++) {
								if (header[w].getType().equals("date")) {
									dateIndex = w;
								}
							}
							
							SimpleDateFormat f = new SimpleDateFormat(inputStructure.getDateFormat());
							f.applyPattern(inputStructure.getDateFormat());
							
							String dataLine = null;
							while ((dataLine = inputStream.readLine()) != null) {
								String[] dataLineSplit = dataLine.split(",");
								if (dataLineSplit.length == 0 || dataLineSplit.length < noOfColumns || noOfColumns < maxColumnLen) {
									break;
								}
								if(dateIndex!=-1) {
									if(!dateMatches(input,targetDate,f,dataLineSplit[dateIndex],hourly,inputData.isExtractDateMonthly())){
										continue;
									}
								}
								assignStatementParameters(preparedStmt, dateFormat, header,dataLineSplit);
								rowsCounter++;
								foundData = true;
								if (rowsCounter >= batchSize) {
									preparedStmt.executeBatch();
									preparedStmt.clearBatch();
									rowsCounter = 0;
								}
								foundData=true;
							}
						} else {
							String dataLine = null;
							while ((dataLine = inputStream.readLine()) != null) {
								dataLine=new String (dataLine.getBytes("UTF-8"), "UTF-8");
								String[] dataLineSplit = dataLine.split(",");
								if (dataLineSplit.length == 0 || dataLineSplit.length < noOfColumns  || noOfColumns < maxColumnLen) {
									break;
								}
								assignStatementParameters(preparedStmt, dateFormat, header, dataLineSplit);

                                rowsCounter++;
								foundData = true;
								if (rowsCounter >= batchSize) {
									preparedStmt.executeBatch();
									preparedStmt.clearBatch();
									rowsCounter = 0;
								}
							}
						}
						inputStream.close();
                        logger.info("DirectMappingTextInputDAO.retrieveData(): Finish Parsing file [ "+inputFiles[j].getName()+" ]");
					}
					if ( foundData == false ) {
						outConnection.rollback();
						logger.error("DirectMappingTextInputDAO.retrieveData() for node " + input.getNodeName() + " - No data found in Text for given date [" + Utils.convertToDateString(targetDate, Utils.defaultFormat) + "] for input id [" + input.getId() + "]");
						if(inputStructures.length == 1)
							throw new InputException("DirectMappingTextInputDAO.retrieveData() for node " + input.getNodeName() + " - no data found in Text for given date [" + Utils.convertToDateString(targetDate, Utils.defaultFormat) + "] for input id [" + input.getId() + "]");
					} else {
						preparedStmt.executeBatch();
						preparedStmt.clearBatch();
						outConnection.commit();
			        }
					persistenceManager.refreshViews(outConnection, inputData, tableName);
				} catch (SQLException e) {
					logger.error("DirectMappingTextInputDAO.retrieveData() for node " + input.getNodeName() + " - Error getting data of Test input", e);
					throw new InputException( "DirectMappingTextInputDAO.retrieveData() for node " + input.getNodeName() + "- Error getting data of Text input - " + e);
				}  finally {
					if (preparedStmt != null) {
						try {
							preparedStmt.close();
						} catch (Exception e) {
							logger.error("DirectMappingTextInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close DWH statement", e);
						}
					}
					if (outConnection != null) {
						try {
							if (!outConnection.isClosed()) {
								outConnection.close();
							}
						} catch (Exception e) {
							logger.error("DirectMappingTextInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close DWH connection", e);
						}
					}
					if(session != null){
						try {
							if (session.isConnected()) {
								session.close();
							}
						} catch (Exception e) {
							logger.error("DirectMappingTextInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close DWH session", e);
						}
					}
				}
			}
        }catch (Exception e) {
			logger.error("-"+ input.getNodeName() + "- DMTEXT-1005: DirectMappingTextInputDAO.retrieveData() - Error getting data of Text input", e);
			throw new InputException("-" + input.getNodeName() + "- DMTEXT-1005: DirectMappingTextInputDAO.retrieveData() - Error getting data of Text input - " + e);
		}
		logger.debug("DirectMappingTextInputDAO.retrieveData() - finished retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);
	}

	private void assignStatementParameters(NamedParameterStatement preparedStmt,String dateFormat,
                                           DataColumn[] columns, String[] dataRow) throws InputException, SQLException {

        if(Utils.isEmpty(dateFormat)){
			dateFormat = "MM/dd/yyyy HH:mm:ss";
		}

		for (DataColumn column : columns) {
			String columnValue = dataRow[column.getIndex()];
			String columnName = column.getName();
            if (columnValue == null || "".equals(columnValue)) {
				if ("string".equals(column.getType())) {
					preparedStmt.setString(columnName, null);
				} else if ("date".equals(column.getType())) {
					preparedStmt.setDate(columnName, null);
				} else if ("number".equals(column.getType())) {
					preparedStmt.setNull(columnName, Types.BIGINT);
				} else if ("float".equals(column.getType())) {
					preparedStmt.setNull(columnName, Types.DECIMAL);
				} else {
					preparedStmt.setString(columnName, null);
				}
			} else {
				if ("date".equalsIgnoreCase(column.getType())) {
					Date dateValue = Utils.convertToDate(columnValue,dateFormat);
					preparedStmt.setObject(columnName, Utils.sqlTimestamp(dateValue));
				} else if("number".equalsIgnoreCase(column.getType())
                        || "float".equals(column.getType())){
                    /*should use "Types.BIGINT" for type "number"
                    and "Types.DECIMAL" for type "float",
                    But currently defined systems use "number"  as column type for columns who's values could  containing decimal point
                    So will use "Types.DECIMAL" always  to avoid parseException Exception.*/
                        //Double val = Double.parseDouble(columnValue);
                        preparedStmt.setObject(columnName, columnValue,Types.DECIMAL);
				}else{
                        preparedStmt.setObject(columnName, columnValue);
                 }

		    }
        }
		preparedStmt.addBatch();
	}
	
	private int getMaxIndex(DataColumn[] columns){
		int maxLen = 0;
		for(DataColumn column: columns){
			if(column.getIndex()>maxLen){
				maxLen = column.getIndex(); 
			}
		}
		return maxLen;
	}
}
