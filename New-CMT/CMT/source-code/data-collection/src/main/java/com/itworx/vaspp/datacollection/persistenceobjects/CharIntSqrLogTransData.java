package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CharIntSqrLogTransData extends PersistenceObject{
	public Date dateTime;
	public double reqCount;
	public double respCount;
	public double inquiryReqCount;
						
public double getInquiryReqCount() {
		return inquiryReqCount;
	}

	public void setInquiryReqCount(double inquiryReqCount) {
		this.inquiryReqCount = inquiryReqCount;
	}

public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

public CharIntSqrLogTransData()
{
	}
}
