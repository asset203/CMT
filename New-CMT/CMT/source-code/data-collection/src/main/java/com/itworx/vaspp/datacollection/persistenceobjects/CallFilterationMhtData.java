package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CallFilterationMhtData  extends PersistenceObject{
public CallFilterationMhtData()
{}
public Date dateTime;
public long utilization;
public long mhtSec;
public long totalCallDuration;
public long totalCallsNo;
public Date getDateTime() {
	return dateTime;
}
public void setDateTime(Date dateTime) {
	this.dateTime = dateTime;
}
public long getUtilization() {
	return utilization;
}
public void setUtilization(long utilization) {
	this.utilization = utilization;
}
public long getMhtSec() {
	return mhtSec;
}
public void setMhtSec(long mhtSec) {
	this.mhtSec = mhtSec;
}
public long getTotalCallDuration() {
	return totalCallDuration;
}
public void setTotalCallDuration(long totalCallDuration) {
	this.totalCallDuration = totalCallDuration;
}
public long getTotalCallsNo() {
	return totalCallsNo;
}
public void setTotalCallsNo(long totalCallsNo) {
	this.totalCallsNo = totalCallsNo;
}
}
