/* 
 * File:       DBInputStructure.java
 * Date        Author          Changes
 * 21/01/2006  Nayera Mohamed  Created
 * 18/03/2006  Nayera Mohamed  Updated to include date format
 * 
 * Represent Database Input Structure of input data from XML configuration
 */
package com.itworx.vaspp.datacollection.objects;

public class DBInputStructure extends VInputStructure {
	private String driver;

	private String sqlStatement;

	private String dateFormat;

	public DBInputStructure() {
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}
}