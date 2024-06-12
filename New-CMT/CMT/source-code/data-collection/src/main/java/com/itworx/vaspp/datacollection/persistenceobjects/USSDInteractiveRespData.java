package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class USSDInteractiveRespData   extends PersistenceObject{
	public Date dateTime;
	public String respName;
	public long respCount;
	public USSDInteractiveRespData()
	{}
	
	public Date getDateTime() {
		return dateTime;
	}
	public String getRespName() {
		return respName;
	}
	public void setRespName(String respName) {
		this.respName = respName;
	}
	public long getRespCount() {
		return respCount;
	}
	public void setRespCount(long respCount) {
		this.respCount = respCount;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
