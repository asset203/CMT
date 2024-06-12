package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OTASelectLangReqData extends PersistenceObject{
	
	public Date dateTime;
	public double toralReq;
	public double succReq;
	public double failedReq;
	public double avgReq;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getToralReq() {
		return toralReq;
	}
	public void setToralReq(double toralReq) {
		this.toralReq = toralReq;
	}
	public double getSuccReq() {
		return succReq;
	}
	public void setSuccReq(double succReq) {
		this.succReq = succReq;
	}
	public double getFailedReq() {
		return failedReq;
	}
	public void setFailedReq(double failedReq) {
		this.failedReq = failedReq;
	}
	public double getAvgReq() {
		return avgReq;
	}
	public void setAvgReq(double avgReq) {
		this.avgReq = avgReq;
	}
public OTASelectLangReqData()
{}
}
