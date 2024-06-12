package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNRespTransData  extends PersistenceObject{
	public Date dateTime;
	public String respCode;
	public String transId;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
public EOCNRespTransData()
{}
}
