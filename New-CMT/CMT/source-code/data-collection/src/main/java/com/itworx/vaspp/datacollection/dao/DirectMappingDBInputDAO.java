package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.NamedParameterStatement;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.DBInputStructure;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.model.enums.InputStructureType;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DirectMappingDBInputDAO extends DBInputDAO {

	public DirectMappingDBInputDAO(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public void retrieveData(VInput input, Date targetDate,boolean hourly)
			throws InputException, ApplicationException {


        this.logger = Logger.getLogger(input.getSystemName());

		logger.debug("DirectMappingDBInputDAO.retrieveData() - started retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);
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
		Session session = null;
		Connection connection = null;
		Connection outConnection=null;
		Statement statement = null;
		ResultSet selectResults = null;
		NamedParameterStatement preparedStmt=null;
		try {
			DBInputStructure inputStructure = (DBInputStructure) input.getInputStructure();
            connection = getConnection(input);
			statement = connection.createStatement();
			String sql = resolveDateVariable(inputStructure.getSqlStatement(), targetDate);
			selectResults = statement.executeQuery(sql);
			// get header from node and set in input data object
			int batchSize=persistenceManager.getBatchSize();
			// read data from result set and collect in vector then pass to input data object
			boolean foundData = false;
			try {

                if(inputStructure.getType() == InputStructureType.GENERIC_DB.getTypeCode()){
                    session = persistenceManager.getNewCmtSchemaSession();
                    outConnection = session.connection();
                }
                else
                {
                    session = persistenceManager.getNewSession();
                    outConnection = session.connection();
                }
				outConnection.setAutoCommit(false);
			} catch (Exception e1) {
				logger.error("DirectMappingDBInputDAO.retrieveData() - error while getting new connection",e1);
				throw new InputException("-" + input.getNodeName() + "- Unable to open connection to the database - "+ e1.getMessage());
			}
			String tableName = inputStructure.getMappedTable();
			String nodeColumn = inputStructure.getNodeColumn();
			String idColumn = inputStructure.getIdColumn();
			String dateColumn = inputStructure.getDateColumn();
			
			DataColumn[] columns = inputStructure.getColumns();
            if(inputStructure.getType() == InputStructureType.GENERIC_DB.getTypeCode()){
                 //1- delete old data
                   if(inputStructure.isTruncateBeforeInsertion())
                   {
                       persistenceManager.deleteOldData(outConnection, input.getSystemName(),
                               tableName, targetDate, input.getNodeName(),
                               nodeColumn, inputStructure.getDateColumn(), hourly);
                   }
                //2-call update events
                if(hourly){
                    persistenceManager.updateHourlyEvents(outConnection, inputData, tableName);
                }
                else{
                    persistenceManager.updateGeneralEvents(outConnection, inputData);
                }
            }
            else
            {
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
            }
            String namedSql = Utils.getInsertQuery(tableName,columns,nodeColumn,
                    idColumn,inputStructure.getSeqName(),inputData.getNodeName(),":",null);
			preparedStmt = new NamedParameterStatement(outConnection, namedSql);
			int ignoredLines = 0;
			Map valuesMap = new HashMap();
			int rowsCounter=0;
			outer: while (selectResults.next()) {
				valuesMap.clear();

				for(DataColumn column: columns){
				Object columnValue = selectResults.getObject(column.getSrcColumn());
					valuesMap.put(column.getName(), columnValue);
				}
				updateStatementParameters(preparedStmt,columns,valuesMap);


				preparedStmt.addBatch();
				rowsCounter++;
				foundData = true;
				if(rowsCounter>=batchSize){
					preparedStmt.executeBatch();
					preparedStmt.clearBatch();
					rowsCounter=0;
				}
			}
			
			if(ignoredLines > 0){
				logger.error("DirectMappingDBInputDAO.retrieveData() for node "+ input.getNodeName() + " - there is ("+ignoredLines+") ignored in DB records for given date [" + Utils.convertToDateString(targetDate, Utils.defaultFormat) + "] for input id [" + input.getId() + "]");
			}
			
			if (!foundData) {
				outConnection.rollback();
				logger.error("DirectMappingDBInputDAO.retrieveData() for node "+ input.getNodeName() + " - No data found in DB for given date [" + Utils.convertToDateString(targetDate, Utils.defaultFormat) + "] for input id [" + input.getId() + "]");
				throw new InputException("DirectMappingDBInputDAO.retrieveData() for node "+ input.getNodeName() + " - no data found in DB for given date [" + Utils.convertToDateString(targetDate, Utils.defaultFormat) + "] for input id [" + input.getId() + "]");
			} else {
				preparedStmt.executeBatch();
				preparedStmt.clearBatch();
				outConnection.commit();
			} 
			persistenceManager.refreshViews(outConnection, inputData, tableName);
		} catch (SQLException e) {
			logger.error("DirectMappingDBInputDAO.retrieveData() for node "+input.getNodeName()+" - Error getting data of DB input",e);
			throw new InputException("DirectMappingDBInputDAO.retrieveData() for node "+ input.getNodeName() + "- Error getting data of DB input - " + e);
		} catch (Exception e){
			logger.error("-" + input.getNodeName() + "- DMDB-1005: DirectMappingDBInputDAO.retrieveData() - Error getting data of DB input",e);
			throw new InputException("-" + input.getNodeName() + "- DMDB-1005: DirectMappingDBInputDAO.retrieveData() - Error getting data of DB input - " + e);			
		} finally {
			if(selectResults != null){
				try{
					selectResults.close();
				} catch (Exception e) {
					logger.error("DirectMappingDBInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close input resultset",e);		
				}
			}
			if(statement != null){
				try {
					statement.close();
				} catch (Exception e) {
					logger.error("DirectMappingDBInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close input statement",e);
				}
			}
			if (connection != null){
				try{
					if(!connection.isClosed()) {
						connection.close();
					}
				} catch (Exception e){
					logger.error("DirectMappingDBInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close input connection",e);		
				}
			}
			
			if(preparedStmt != null){
				try{
					preparedStmt.close();
				} catch(Exception e){
					logger.error("DirectMappingDBInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close DWH statement",e);
				}
			}
			if (outConnection != null){
				try{
					if(!outConnection.isClosed()) {
						outConnection.close();
					}
				}catch (Exception e){
					logger.error("DirectMappingDBInputDAO.retrieveData() - node : " + input.getNodeName() + " Couldn't close DWH connection",e);		
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
		logger.debug("DirectMappingDBInputDAO.retrieveData() - finished retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);

	}

	private void updateStatementParameters(NamedParameterStatement preparedStmt,DataColumn[] columns, Map valuesMap) throws SQLException {
		for(DataColumn column: columns){
			String columnName = column.getName();
			Object value = valuesMap.get(columnName);
			if(value == null){
                if(DataColumnType.STRING.getName().equals(column.getType())){
					preparedStmt.setString(columnName, null);
                } else if(DataColumnType.DATE.getName().equals(column.getType())){
					preparedStmt.setDate(columnName, null);
                } else if(DataColumnType.NUMBER.getName().equals(column.getType())){
					preparedStmt.setNull(columnName, Types.BIGINT);
                } else if (DataColumnType.FLOAT.getName().equals(column.getType())){
					preparedStmt.setNull(columnName, Types.DECIMAL);
				} else {
					preparedStmt.setString(columnName, null);
				}
			}else{
				preparedStmt.setObject(columnName,value);
			}
		}
	}
}
