package com.itworx.vaspp.datacollection.persistenceobjects;

import com.itworx.vaspp.datacollection.persistenceobjects.PersistenceObject;
import java.util.*;
public class USSDDiamConnectData extends PersistenceObject{
	public Date dateTime;
	public String shortCode;
	public String resultCode;
	public double reqNumber;
	public double respNumber;
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
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public double getReqNumber() {
		return reqNumber;
	}
	public void setReqNumber(double reqNumber) {
		this.reqNumber = reqNumber;
	}
	public double getRespNumber() {
		return respNumber;
	}
	public void setRespNumber(double respNumber) {
		this.respNumber = respNumber;
	}
public USSDDiamConnectData()
{
	}
}
