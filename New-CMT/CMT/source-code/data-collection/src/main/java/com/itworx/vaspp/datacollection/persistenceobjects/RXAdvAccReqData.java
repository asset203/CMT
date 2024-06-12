package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class RXAdvAccReqData  extends PersistenceObject{
	
	public Date dateTime;
	public double serviceClass;
	public double noOfAcceptedReq;
	public double tier;
	public String schemaID;
	public String schemaDescription;

public RXAdvAccReqData()
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
	public double getNoOfAcceptedReq() {
		return noOfAcceptedReq;
	}
	public void setNoOfAcceptedReq(double noOfAcceptedReq) {
		this.noOfAcceptedReq = noOfAcceptedReq;
	}
	public double getTier() {
		return tier;
	}
	public void setTier(double tier) {
		this.tier = tier;
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
