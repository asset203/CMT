package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CFCDRAddData extends PersistenceObject{
	public Date dateTime;
	public String sysNumber;
	public double lineNumber;
	public String callType;
	public String mscGt;
	public String action;
	public String serviceType;
	public String field1;
	public String field2;
	public double listTypeId;
	public double packageId;
	public double count;
	public double serviceCode;
public double getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(double serviceCode) {
		this.serviceCode = serviceCode;
	}
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getSysNumber() {
		return sysNumber;
	}
	public void setSysNumber(String sysNumber) {
		this.sysNumber = sysNumber;
	}
	public double getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(double lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getMscGt() {
		return mscGt;
	}
	public void setMscGt(String mscGt) {
		this.mscGt = mscGt;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public double getListTypeId() {
		return listTypeId;
	}
	public void setListTypeId(double listTypeId) {
		this.listTypeId = listTypeId;
	}
	public double getPackageId() {
		return packageId;
	}
	public void setPackageId(double packageId) {
		this.packageId = packageId;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public CFCDRAddData()
{
	}
}
