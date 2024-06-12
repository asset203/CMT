package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EOCNRespTransVlrData extends PersistenceObject {
	public Date dateTime;	
	public String vlr;
	public String respCode;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getVlr() {
		return vlr;
	}
	public void setVlr(String vlr) {
		this.vlr = vlr;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public EOCNRespTransVlrData()
{}
}
