package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class USSDInVlrRequestsData extends PersistenceObject{
	public Date dateTime;
	public String vlrName;
	public String reqName;
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
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public USSDInVlrRequestsData()
{}
}
