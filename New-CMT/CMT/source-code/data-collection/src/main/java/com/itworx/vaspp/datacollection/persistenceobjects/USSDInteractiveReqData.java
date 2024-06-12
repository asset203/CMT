package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class USSDInteractiveReqData extends PersistenceObject{
	
	public Date dateTime;
	public String reqName;
	public String respName;
	public long count;
	
	
public Date getDateTime() {
		return dateTime;
	}


	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


	public String getReqName() {
		return reqName;
	}


	public void setReqName(String reqName) {
		this.reqName = reqName;
	}


	public String getRespName() {
		return respName;
	}


	public void setRespName(String respName) {
		this.respName = respName;
	}


	public long getCount() {
		return count;
	}


	public void setCount(long count) {
		this.count = count;
	}


public USSDInteractiveReqData()
	{}
}
