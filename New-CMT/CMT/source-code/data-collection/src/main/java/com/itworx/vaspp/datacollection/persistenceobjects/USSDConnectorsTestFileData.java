package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;
public class USSDConnectorsTestFileData extends PersistenceObject{
	
	public String dateTime;
	
	public String connectorName;
	 
	public String msisdn;
	
	public String lineType;
	
	


	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}





}
