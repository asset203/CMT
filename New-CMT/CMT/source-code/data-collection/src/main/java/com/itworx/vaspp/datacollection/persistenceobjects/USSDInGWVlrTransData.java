package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class USSDInGWVlrTransData extends PersistenceObject{
	public Date dateTime;
	public String vlrName;
	public String respName;
	public double count;
    public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getVlrName() {
		return vlrName;
	}
	public void setVlrName(String vlrName) {
		this.vlrName = vlrName;
	}
	public String getRespName() {
		return respName;
	}
	public void setRespName(String respName) {
		this.respName = respName;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public USSDInGWVlrTransData()
{}
}
