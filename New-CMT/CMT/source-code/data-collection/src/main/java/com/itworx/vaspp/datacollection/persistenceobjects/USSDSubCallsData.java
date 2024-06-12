package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class USSDSubCallsData extends PersistenceObject{
	public Date dateTime;
	public String shortCode;
	public String subCode;
	public String respReason;
	public double noOfReq;
	public double NoOfResp;
public double getNoOfResp() {
		return NoOfResp;
	}
	public void setNoOfResp(double noOfResp) {
		NoOfResp = noOfResp;
	}
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getRespReason() {
		return respReason;
	}
	public void setRespReason(String respReason) {
		this.respReason = respReason;
	}
	public double getNoOfReq() {
		return noOfReq;
	}
	public void setNoOfReq(double noOfReq) {
		this.noOfReq = noOfReq;
	}
	
public USSDSubCallsData()
{
}
}
