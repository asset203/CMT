package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MCKSMSQualityData extends PersistenceObject{
	public Date smsTime;
	public String sourceAdd;
	public String destAdd;
public Date getSmsTime() {
		return smsTime;
	}
	public void setSmsTime(Date smsTime) {
		this.smsTime = smsTime;
	}
	public String getSourceAdd() {
		return sourceAdd;
	}
	public void setSourceAdd(String sourceAdd) {
		this.sourceAdd = sourceAdd;
	}
	public String getDestAdd() {
		return destAdd;
	}
	public void setDestAdd(String destAdd) {
		this.destAdd = destAdd;
	}
public MCKSMSQualityData()
{
	
}
}
