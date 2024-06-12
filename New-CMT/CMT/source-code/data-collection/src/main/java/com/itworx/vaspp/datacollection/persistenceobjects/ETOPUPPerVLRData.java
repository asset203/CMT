package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ETOPUPPerVLRData extends PersistenceObject{
	public Date dateTime;
	public String vlr;
	public double reqCount;
	public double respCount;
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
	public double getReqCount() {
		return reqCount;
	}
	public void setReqCount(double reqCount) {
		this.reqCount = reqCount;
	}
	public double getRespCount() {
		return respCount;
	}
	public void setRespCount(double respCount) {
		this.respCount = respCount;
	}
public ETOPUPPerVLRData()
{
}
}
