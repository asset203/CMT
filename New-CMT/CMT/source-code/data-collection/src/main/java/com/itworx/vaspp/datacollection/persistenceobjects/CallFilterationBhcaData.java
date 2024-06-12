package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CallFilterationBhcaData extends PersistenceObject{
public CallFilterationBhcaData()
{}
public Date dateTime;
public long totalMtidpMessages;
public long totalMtidpMessagesSec;
public long totalIamMessages;
public long totalIamMessagesSec;
public Date getDateTime() {
	return dateTime;
}
public void setDateTime(Date dateTime) {
	this.dateTime = dateTime;
}
public long getTotalMtidpMessages() {
	return totalMtidpMessages;
}
public void setTotalMtidpMessages(long totalMtidpMessages) {
	this.totalMtidpMessages = totalMtidpMessages;
}
public long getTotalMtidpMessagesSec() {
	return totalMtidpMessagesSec;
}
public void setTotalMtidpMessagesSec(long totalMtidpMessagesSec) {
	this.totalMtidpMessagesSec = totalMtidpMessagesSec;
}
public long getTotalIamMessages() {
	return totalIamMessages;
}
public void setTotalIamMessages(long totalIamMessages) {
	this.totalIamMessages = totalIamMessages;
}
public long getTotalIamMessagesSec() {
	return totalIamMessagesSec;
}
public void setTotalIamMessagesSec(long totalIamMessagesSec) {
	this.totalIamMessagesSec = totalIamMessagesSec;
}
}
