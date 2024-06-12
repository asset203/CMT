package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNVlrData extends PersistenceObject{
	public Date dateTime;
	public String transId;
	public String vlr;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getVlr() {
		return vlr;
	}
	public void setVlr(String vlr) {
		this.vlr = vlr;
	}
public EOCNVlrData()
{}
}
