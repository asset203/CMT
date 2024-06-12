package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VSSMFileSysData  extends PersistenceObject{
 public Date dateTime;
 public String fileSysName;
 public double utlizationMin;
 public double utlizationMax;
 public double utlizationAvg;
 
 public Date getDateTime() {
	return dateTime;
}
public void setDateTime(Date dateTime) {
	this.dateTime = dateTime;
}
public String getFileSysName() {
	return fileSysName;
}
public void setFileSysName(String fileSysName) {
	this.fileSysName = fileSysName;
}
public double getUtlizationMin() {
	return utlizationMin;
}
public void setUtlizationMin(double utlizationMin) {
	this.utlizationMin = utlizationMin;
}
public double getUtlizationMax() {
	return utlizationMax;
}
public void setUtlizationMax(double utlizationMax) {
	this.utlizationMax = utlizationMax;
}
public double getUtlizationAvg() {
	return utlizationAvg;
}
public void setUtlizationAvg(double utlizationAvg) {
	this.utlizationAvg = utlizationAvg;
}

}
