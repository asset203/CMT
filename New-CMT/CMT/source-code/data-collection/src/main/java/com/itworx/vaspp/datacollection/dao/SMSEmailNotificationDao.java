package com.itworx.vaspp.datacollection.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.util.NamedParameterStatement;
import com.itworx.vaspp.datacollection.util.Utils;

public class SMSEmailNotificationDao {
	
	private static final String AND = ") AND ";
	private static final String COMMA = ",";
	private static final String QUATE = "'";
	private static final String COMMA_QUATE = ",'";
	private static final String DD = "dd";
	private static final String HH = "hh";
	private static final String NODE_NAME_IN = " NODE_NAME IN (";
	private static final String _TRUNC_DATE_TIME_ = " TRUNC(DATE_TIME,'";
	private static final String _WHERE_ = " WHERE ";
	private static final String VALIDATE_SELECT_FROM_ = "SELECT COUNT(0) AS REC_COUNT FROM ";
	
	
	private final Connection connection; 
	private final Logger logger;
	
	public SMSEmailNotificationDao(Connection connection, Logger logger) {
		this.connection = connection;
		this.logger = logger;
	}

	public boolean validateTableData(String tableName, String[] nodes, Date queryDate,boolean hourly) {
		logger.debug("SMSEmailNotificationDao.validateTableData() - started");
		String query = createValidationQuery(tableName, nodes, hourly);
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt = connection.prepareStatement(query);
			stmt.setTimestamp(1, Utils.sqlTimestamp(queryDate));
			rset = stmt.executeQuery();
			Long recordCount = 0L;
			while(rset.next()){
				recordCount = rset.getLong("REC_COUNT");
			}
			if(recordCount.equals(0L)){
				logger.debug("SMSEmailNotificationDao.validateTableData() - finished & returned false");
				return false;
			}
		} catch (SQLException e) {
			logger.error("SMSEmailNotificationDao.validateTableData() - executing query for table ["+tableName+"] and date ["+queryDate+"]",e);
			return false;
		} finally {
			try {
				if(rset != null){
					rset.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error("SMSEmailNotificationDao.validateTableData() - error while closing resultset & statement for table ["+tableName+"] and date "+queryDate+"]",e);
			}
		}
		logger.debug("SMSEmailNotificationDao.validateTableData() - finished & returned true");
		return true;
	}



	public Object executeQuery(String sql, Map<String,Object> params) {
		logger.debug("SMSEmailNotificationDao.executeQuery() - started with sql ["+sql+"] and parameters ["+Utils.toString(params)+"]");
		NamedParameterStatement stmt = null;
		ResultSet rset = null;
		Object record = null;
		try {
			stmt = new NamedParameterStatement(connection, sql);
			if(params != null && !params.isEmpty()){
				for(String paramName: params.keySet()){
					if(stmt.hasParameter(paramName)){
						stmt.setObject(paramName, params.get(paramName));
					}
				}
			}
			rset = stmt.executeQuery();
			while(rset.next()){
				record = rset.getObject(1);
				break;
			}
		} catch (SQLException e) {
			logger.error("SMSEmailNotificationDao.executeQuery() - executing query ["+sql+"] and params ["+Utils.toString(params)+"]",e);
			return null;
		} finally {
			try {
				if(rset != null){
					rset.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error("SMSEmailNotificationDao.executeQuery() - error while closing resultset & statement for query ["+sql+"] and params ["+Utils.toString(params)+"]",e);
			}
		}
		logger.debug("SMSEmailNotificationDao.executeQuery() - finished with sql ["+sql+"] and parameters ["+Utils.toString(params)+"] & returned ["+record+"]");
		return record;
	}
	
	private String createValidationQuery(String tableName, String[] nodes, boolean hourly) {
		StringBuilder query = new StringBuilder(VALIDATE_SELECT_FROM_);
		query.append(tableName);
		StringBuilder nodesCond = new StringBuilder();
		if(nodes != null && nodes.length > 0){
			nodesCond.append(NODE_NAME_IN);
			for(String node: nodes){
				nodesCond.append(COMMA_QUATE);
				nodesCond.append(node);
				nodesCond.append(QUATE); 
			}
			int commaIdx = nodesCond.indexOf(COMMA);
			if(commaIdx != -1){
				nodesCond = nodesCond.replace(commaIdx, commaIdx+1, "");
			}
			nodesCond.append(AND);
		}
		String format = (hourly)?HH:DD;
		query.append(_WHERE_);
		query.append(nodesCond);
		query.append( _TRUNC_DATE_TIME_ );
		query.append(format);
		query.append("') = TRUNC(?,'");
		query.append(format);
		query.append("')");
		
		logger.debug("SMSEmailNotificationDao.validateTableData() - creating sql ["+query+"]");
		return query.toString();
	}
}
