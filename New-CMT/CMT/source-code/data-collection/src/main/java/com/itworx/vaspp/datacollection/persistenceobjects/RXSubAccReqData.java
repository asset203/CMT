package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;;
public class RXSubAccReqData   extends PersistenceObject{
	
	public Date dateTime;
	public double serviceClass;
	public double numberOfAcceptedRequests;
	public double subStatus;
	public String schemaID;
	public String schemaDescription;
	
	public RXSubAccReqData()
	{}
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	
	public double getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(double serviceClass) {
		this.serviceClass = serviceClass;
	}
	public double getNumberOfAcceptedRequests() {
		return numberOfAcceptedRequests;
	}
	public void setNumberOfAcceptedRequests(double numberOfAcceptedRequests) {
		this.numberOfAcceptedRequests = numberOfAcceptedRequests;
	}
	public double getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(double subStatus) {
		this.subStatus = subStatus;
	}
	public String getSchemaID() {
		return schemaID;
	}
	public void setSchemaID(String schemaID) {
		this.schemaID = schemaID;
	}
	public String getSchemaDescription() {
		return schemaDescription;
	}
	public void setSchemaDescription(String schemaDescription) {
		this.schemaDescription = schemaDescription;
	}
	



}
