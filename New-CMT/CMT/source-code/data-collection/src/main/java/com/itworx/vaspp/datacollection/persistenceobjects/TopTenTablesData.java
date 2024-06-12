package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class TopTenTablesData extends PersistenceObject{
	
	public Date date;
	
	public String commandType;
	
	public String objectName;
	
	public long numberOfExecutions;
	
	public long bufferGets;
	
	public long rowsProcessed;

	public long getBufferGets() {
		return bufferGets;
	}

	public void setBufferGets(long bufferGets) {
		this.bufferGets = bufferGets;
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getNumberOfExecutions() {
		return numberOfExecutions;
	}

	public void setNumberOfExecutions(long numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public long getRowsProcessed() {
		return rowsProcessed;
	}

	public void setRowsProcessed(long rowsProcessed) {
		this.rowsProcessed = rowsProcessed;
	}

}
