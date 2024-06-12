package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SwitchDivertData extends PersistenceObject{
	public Date dateTime;
	public String Switch;
	public String userAction;
	public double divertCond;
	public double annotationID;
	public double userActionCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSwitch() {
		return Switch;
	}
	public void setSwitch(String switch1) {
		Switch = switch1;
	}
	public String getUserAction() {
		return userAction;
	}
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	public double getDivertCond() {
		return divertCond;
	}
	public void setDivertCond(double divertCond) {
		this.divertCond = divertCond;
	}
	public double getAnnotationID() {
		return annotationID;
	}
	public void setAnnotationID(double annotationID) {
		this.annotationID = annotationID;
	}
	
	public double getUserActionCount() {
		return userActionCount;
	}
	public void setUserActionCount(double userActionCount) {
		this.userActionCount = userActionCount;
	}
public SwitchDivertData()
{}
}
