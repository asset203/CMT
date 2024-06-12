package com.itworx.vaspp.datacollection.genericextractors;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dao.CMTDBConnectionPool;
import com.itworx.vaspp.datacollection.util.NamedParameterStatement;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.ExtractionField;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.utils.Utils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static eg.com.vodafone.utils.Utils.TEMP_TABLE_NAME_PLACEHOLDER;

public class TempDBTableOperator{

    private Connection mainConnection;
    private String insertSQLStr;
    private NamedParameterStatement insertNamedPreparedStmt;
    private  Statement extractionSqlStatement;
    private PreparedStatement insertPStmt;
    private ExtractionField[] extractionFields;
    private List<DataColumn> columns;
    private String tableName;
    private String tempDatabaseUsername;
    private int numberOfReadyInserts;
    private int batchSize;
    private Logger logger;
    private boolean tableCerated=false;

    public TempDBTableOperator(Logger logger,VInput input) throws ApplicationException, InputException
    {
    	this.logger = logger;
    	long rnd = RandomUtils.nextInt();
    	tableName = "TMB"+new Date().getTime()+"_"+rnd;
    	logger.info("TempDBTableOperator.TempDBTableOperator(logger) - starting initializing TempDBTableOperator for table ["
                +tableName+"] for system ("+input.getSystemName()+") and node ("+input.getNodeName()+") and input ("+input.getInputName()+")");
        mainConnection = CMTDBConnectionPool.getTEMPDBConnection();
    	logger.debug("TempDBTableOperator.TempDBTableOperator(logger) - finishing initializing TempDBTableOperator for table ["+tableName+"]");
    }

   /* public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setAddNodeName(boolean addNodeName) {
        this.addNodeName = addNodeName;
    } */

    public TempDBTableOperator(Logger logger) throws ApplicationException, InputException
    {
    	this.logger = logger;
        logger.info("TempDBTableOperator.TempDBTableOperator(logger) - starting initializing TempDBTableOperator");
        mainConnection = CMTDBConnectionPool.getTEMPDBConnection();
        try {
            tempDatabaseUsername = mainConnection.getMetaData().getUserName();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        logger.info("TempDBTableOperator.TempDBTableOperator(logger) - finishing initializing TempDBTableOperator");
    }
    public void createTempTable(VInputStructure inputStructure) throws ApplicationException {
        logger.debug("TempDBTableOperator.createTempDatabaseTable() - starting create table from extraction fields");
        this.columns = inputStructure.getColumnsList();
        long rnd = RandomUtils.nextInt();
        this.tableName ="TMB"+new Date().getTime()+"_"+rnd;
        Statement stmt =null;
        try{
            String tableCreateSql = Utils.getCreateTempTableStatement(this.tableName,this.columns,false);
            stmt = mainConnection.createStatement();
            logger.debug("TempDBTableOperator.createTempDatabaseTable() - try to execute create table sql extraction fields ["+tableCreateSql+"]");
            stmt.executeUpdate(tableCreateSql);
            tableCerated =true;
            logger.debug("TempDBTableOperator.createTempDatabaseTable() - temp table [" + this.tableName + "] created successfully");
            logger.debug("TempDBTableOperator.createTempDatabaseTable() - replacing temptable place holder in input structure extraction sql");
            logger.debug("Original SQL ::");
            logger.debug(inputStructure.getExtractionSql());
            inputStructure.setExtractionSql(inputStructure.getExtractionSql().replace(TEMP_TABLE_NAME_PLACEHOLDER,tempDatabaseUsername+'.'+this.tableName)) ;
            logger.debug("SQL After replacing ::");
            logger.debug(inputStructure.getExtractionSql());
        }catch(Exception e){
            logger.error("TempDBTableOperator.createTempDatabaseTable()- error occurred while create temp database table ("+tableName+")",e);
            throw new ApplicationException("TempDBTableOperator.createTempDatabaseTable() - error occurred while create temp database table : " +e);
        }
        finally {
            try {
                if(stmt!= null){
                    stmt.close();
                }
            }catch (SQLException e){
                logger.error("TempDBTableOperator.createTempDatabaseTable() - failed to close statement " +e);
            }
        }
        logger.debug("TempDBTableOperator.createTempDatabaseTable() - finishing create table from extraction fields");
    }

    public void createTempDatabaseTable(ExtractionField[] extractionFields) throws ApplicationException {
    	logger.debug("TempDBTableOperator.createTempDatabaseTable() - starting create table from extraction fields");
    	this.extractionFields = extractionFields;
        Statement stmt = null;
        try{
	        String tableCreateSql = "";
			if(extractionFields.length>0){
				tableCreateSql = "CREATE TABLE "+tableName+"(";
				for(int i = 0 ; i<extractionFields.length-1 ; i++) {
					if(extractionFields[i].isActive() == true){
						tableCreateSql += extractionFields[i].getIdentifier()+" "+getEquivalentSQLType(extractionFields[i].getType())+" ,";
					}
				}
				
				if(extractionFields[extractionFields.length-1].isActive() == true){
					tableCreateSql +=extractionFields[extractionFields.length-1].getIdentifier()+" "+getEquivalentSQLType(extractionFields[extractionFields.length-1].getType());
                }else{
					tableCreateSql = tableCreateSql.substring(0, tableCreateSql.length()-1);
                }
				tableCreateSql += ")";
			    stmt = mainConnection.createStatement();
				logger.debug("TempDBTableOperator.createTempDatabaseTable() - execute create table sql extraction fields ["+tableCreateSql+"]");
				stmt.executeUpdate(tableCreateSql);
				//stmt.close();
                tableCerated =true;
			}
        }catch(SQLException e){
        	logger.error("error occured while create temp database table ("+tableName+")",e);
        	throw new ApplicationException("error occured while create temp database table : "+e);       	
        }
        finally {
            try {
                if(stmt!= null ){ //&& !stmt.isClosed()
                    stmt.close();
                }
            }catch (SQLException e){
                logger.error("failed to close statement " +e);
            }
        }
    	logger.debug("TempDBTableOperator.createTempDatabaseTable() - finishing create table from extraction fields");
    }
    
    private String getEquivalentSQLType(String type)
    {
    	String sqlType = "";
    	if(type.equals("string")) {
    		sqlType = "VARCHAR2(1024)";
        }else if(type.equals("date")){
    		sqlType = "DATE";
        }else if(type.equals("number")){
    		sqlType = "NUMBER";
        }
    	return sqlType;
    }
    
    public void dropTempDatabaseTable(String tableName) throws ApplicationException {
    	logger.debug("TempDBTableOperator.dropTempDatabaseTable() - starting drop table ["+tableName+"]");
    	String truncSQL = "TRUNCATE TABLE " + tableName + " DROP STORAGE";
        String dropSQL = "DROP TABLE " + tableName + " ";
        Statement stmt = null;
    	try {
			stmt = mainConnection.createStatement();
			stmt.executeUpdate(truncSQL);
			logger.debug("TempDBTableOperator.dropTempDatabaseTable() - executing trunc table ["+tableName+"] sql ["+truncSQL+"]");
			stmt.executeUpdate(dropSQL);
			logger.debug("TempDBTableOperator.dropTempDatabaseTable() - executing drop table ["+tableName+"] sql ["+dropSQL+"]");
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
            try{
                 if(stmt !=null){
                     stmt.close();
                 }
            }catch (Exception e){
                logger.error("failed to close statement " +e);
            }
        }
		logger.debug("TempDBTableOperator.dropTempDatabaseTable() - finishing drop table ["+tableName+"]");
    }
    
	public void prepareInsertStatement() throws ApplicationException {
    	if(insertSQLStr == null || "".equals(insertSQLStr))
    	{
			String insertClause = "";
			String valuesClause = "";
			for (int i = 0; i < extractionFields.length; i++) {
				ExtractionField field = extractionFields[i];
				if(field.isActive())
				{
					insertClause += field.getIdentifier()+",";
					valuesClause += "?,";
				}
			}
			if(insertClause.endsWith(",")) {
				insertClause = insertClause.substring(0, insertClause.length()-1);
            }
			if(valuesClause.endsWith(",")){
				valuesClause = valuesClause.substring(0, valuesClause.length()-1);
            }
			
			insertSQLStr = "INSERT INTO "+tableName+" ("+insertClause+") VALUES ("+valuesClause+")";
    	}
    	
    	if(insertPStmt == null) {
    		try {
				insertPStmt = mainConnection.prepareStatement(insertSQLStr);
			} catch (SQLException e) {
				logger.error("sql error occured while preparing statement ",e);
				throw new ApplicationException("sql error occured while preparing statement " + e);
			}
		}

	}
    public void prepareInsertStatementUsingDataColumns() throws ApplicationException {
        if(!StringUtils.hasText(insertSQLStr))
        {
            StringBuffer insertClause = new StringBuffer();
            StringBuffer valuesClause = new StringBuffer();
            for (int i = 0; i < columns.size(); i++) {
                DataColumn column = columns.get(i);
                if(StringUtils.hasText(column.getSqlExpression()) )
                {
                    continue;
                }
                insertClause.append(column.getSrcColumn()).append(",");
                valuesClause.append(":").append(column.getSrcColumn()).append(",");

            }
            if(insertClause.toString().endsWith(",")){
                insertClause =new StringBuffer(insertClause.substring(0, insertClause.length()-1));
            }
            if(valuesClause.toString().endsWith(",")){
                valuesClause = new StringBuffer(valuesClause.substring(0, valuesClause.length()-1));
            }
            insertSQLStr = "INSERT INTO "+tableName+" ("+insertClause+") VALUES ("+valuesClause+")";
        }

        if(insertNamedPreparedStmt == null) {
            try {
                insertNamedPreparedStmt = new NamedParameterStatement(mainConnection, insertSQLStr);
            } catch (SQLException e) {
                logger.error("sql error occured while preparing statement ",e);
                throw new ApplicationException("sql error occured while preparing statement " + e);
            }
        }

    }
    public void substiuteDataInInsertQuery(String[] data) throws SQLException {
        logger.debug("TempDBTableOperator.substiuteDataInInsertQuery() - start");

            if(data != null && data.length > 0) {

                for(DataColumn column : columns){
                    if( StringUtils.hasText(column.getSqlExpression()) )
                    {
                        continue;
                    }
                    String columnData = data[column.getIndex()];
                    if(column.getTypeCode() == DataColumnType.NUMBER.getTypeCode() ){
                        Double value = null;
                        if(StringUtils.hasText(columnData)){
                            double d = Double.parseDouble(columnData);
                            value = Double.valueOf(d);
                        }
                        insertNamedPreparedStmt.setObject(column.getSrcColumn(),value);
                    }else if(column.getTypeCode() == DataColumnType.FLOAT.getTypeCode()){
                        Float value = null;
                        if(StringUtils.hasText(columnData)){
                            float f = Float.parseFloat(columnData);
                            value = Float.valueOf(f);
                        }
                        insertNamedPreparedStmt.setObject(column.getSrcColumn(),value);
                    }else if(column.getTypeCode() == DataColumnType.DATE.getTypeCode()){
                        String format = column.getDateFormat();
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        Date value = null;
                        java.sql.Timestamp dateTimeValue = null;
                        try {
                            value = sdf.parse(columnData.trim());
                            dateTimeValue = new java.sql.Timestamp(value.getTime());
                        } catch (ParseException e) {
                            logger.error("error parsing date value "+e);
                        }
                        insertNamedPreparedStmt.setTimestamp(column.getSrcColumn(), dateTimeValue);
                    }else {
                        insertNamedPreparedStmt.setString(column.getSrcColumn(),columnData);
                    }
                }
                insertNamedPreparedStmt.addBatch();
                numberOfReadyInserts++;
                if( numberOfReadyInserts>= batchSize){
                    executeBatch();
                }

            }
        logger.debug("TempDBTableOperator.insertData() - finish insert data");
    }

    public void executeBatch() throws SQLException {
        insertNamedPreparedStmt.executeBatch();
        insertNamedPreparedStmt.clearBatch();
        numberOfReadyInserts=0;
    }


    public void insertData(String[] data) throws Exception{
    	//logger.debug("TempDBTableOperator.insertData() - starting insert data");
    	int currentIdx = 1;
    	try {
			if(data != null && data.length > 0) {
				currentIdx = 1;
				for (int i=0; i<data.length; i++) {
		            String dataElementStr = data[i];
		            ExtractionField field = extractionFields[i];
		            if(field.isActive()) {
		            	if(field.getType().equals("number")) {
		            		Double value = null;
		            		if(!"".equals(dataElementStr)){
		            			double d = Double.parseDouble(dataElementStr);
		            			value = Double.valueOf(d);
		            		}
		            		insertPStmt.setDouble(currentIdx, value);
		            	} else if(field.getType().equals("string")) {
		            		String value = dataElementStr;
		            		insertPStmt.setString(currentIdx, value);
		            	} else if(field.getType().equals("date")) {
		            		String format = field.getDateFormat();
		            		SimpleDateFormat sdf = new SimpleDateFormat(format);
		            		Date value = null;
							value = sdf.parse(dataElementStr.trim());
		            		java.sql.Timestamp dateTimeValue = new java.sql.Timestamp(value.getTime());
		            		insertPStmt.setTimestamp(currentIdx, dateTimeValue);
		            	}
		            	currentIdx++;
		            }
		        }
				insertPStmt.executeUpdate();
			}
		} catch (SQLException e) {
			insertPStmt.clearParameters();
            logger.error(e);
			throw new ApplicationException("sql error occurred while inserting data "+e);
		} catch (Exception e){
			insertPStmt.clearParameters();
            logger.error(e);
			throw new ApplicationException("error occurred while inserting data "+e);
		}
    	//logger.debug("TempDBTableOperator.insertData() - finish insert data");
    }
    
    public void beginTransaction() throws SQLException {
        mainConnection.setAutoCommit(false);
    }
    
    public void commitTransaction() throws SQLException {
        mainConnection.commit();
        mainConnection.setAutoCommit(true);
    }
    
    public void rollbackTransaction() throws SQLException {
        mainConnection.rollback();
        mainConnection.setAutoCommit(true);
    }

    public ResultSet getResultSet(String extractionSQL) throws ApplicationException{
        extractionSqlStatement =null;
        logger.debug("TempDBTableOperator.getResultSet(), table name = " +tableName);
        logger.debug("TempDBTableOperator.getResultSet(), extractionSQL = " );
        logger.debug(extractionSQL);
        try{
            extractionSQL = extractionSQL.replace("${sourceTableName}", tableName);
            extractionSqlStatement = mainConnection.createStatement();
            return extractionSqlStatement.executeQuery(extractionSQL);
        }catch(SQLException e){
            throw new ApplicationException("error while retrieving result set from temporary table by sql["+extractionSQL+"] : "+e);
        }
       /*  javadoc : "if a statement is closed then its current result set is also closed"
            so closing this statement will cause "java.sql.SQLException: Closed ResultSet:"
            when someone (Generic Persistence manager in this case) try to access the returned result set
            //Refactor made to make the statement a member variable,
            and hence the reference is kept to use it in the "clean()" to close the statement.
       finally {
                try{
                    if(stmt!=null){
                        stmt.close();
                    }
                }catch (SQLException ex){
                    logger.error("failed to close statement " +ex);
                }
            } */
    }

    public void clean() throws ApplicationException{
    	logger.debug("TempDBTableOperator.clean() - starting clean");
    	try{
	   		dropTempDatabaseTable(tableName);
            tableCerated =false;
	    	insertSQLStr = "";
	    	if(insertPStmt != null){
	    		insertPStmt.close();
            }
    	}catch (SQLException e) {
    		throw new ApplicationException("error occurred while closing statement-"+e);
		}finally{

            try {
                if(insertNamedPreparedStmt!=null){
                    insertNamedPreparedStmt.close();
                }
            }catch (SQLException e){
                throw new ApplicationException("error occurred while closing statement-"+e);
            }finally {

                    try
                    {
                        if(extractionSqlStatement != null){
                            extractionSqlStatement.close();
                        }
                    }catch (Exception e){
                        logger.error("error occurred while closing extractionSqlStatement",e);
                    }
                    finally {

                            try {
                                mainConnection.rollback();
                                mainConnection.close();
                            } catch(Exception e) {
                                throw new ApplicationException("error occurred while rollback and closing connection-"+e);
                            }
                    }
            }



		}
		logger.debug("TempDBTableOperator.clean() - finishing clean");
    }
    
    public void dropTempTableAndCloseConnection() throws ApplicationException{
        logger.debug("TempDBTableOperator.dropTempTableAndCloseConnection() - start clean");
        try{
            if(tableCerated == true)
            {
                dropTempDatabaseTable(tableName);
                tableCerated =false;
            }
            insertSQLStr = "";
            if(insertPStmt != null && !insertPStmt.isClosed()){
                insertPStmt.close();
            }
        }catch (SQLException e) {
            throw new ApplicationException("error occurred while closing statement-"+e);
        }finally{
            try {
                if(mainConnection!=null&&!mainConnection.isClosed())
                {
                    mainConnection.rollback();
                    mainConnection.close();
                }
            } catch(Exception e) {
                throw new ApplicationException("error occurred while rollback and closing connection-"+e);
            } finally {
                try {
                    if(insertNamedPreparedStmt!=null){
                        insertNamedPreparedStmt.close();
                    }
                }catch (SQLException e){
                    throw new ApplicationException("error occurred while closing statement-"+e);
                }
            }
        }
        logger.debug("TempDBTableOperator.dropTempTableAndCloseConnection() - finish clean");
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
