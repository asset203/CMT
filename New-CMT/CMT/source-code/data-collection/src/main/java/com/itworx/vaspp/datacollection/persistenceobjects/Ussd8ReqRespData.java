package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class Ussd8ReqRespData  extends PersistenceObject{
	
public Date dateTime;
public Date getDateTime() {
	return dateTime;
}
public void setDateTime(Date dateTime) {
	this.dateTime = dateTime;
}
public String getSubCode() {
	return subCode;
}
public void setSubCode(String subCode) {
	this.subCode = subCode;
}
public long getRequests() {
	return requests;
}
public void setRequests(long requests) {
	this.requests = requests;
}
public long getResponses() {
	return responses;
}
public void setResponses(long responses) {
	this.responses = responses;
}
public String subCode;
public long requests;
public long responses;
	public Ussd8ReqRespData()
	{}

}
