package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsDailySwithcesHourlyData  extends PersistenceObject{
	public Date dateTime;
	public String hour;
	public double mscMoAttempt;
	public double mscMsmoAtt;
	public double mscMoSuccess;
	public double mtAttempts;
	public double mtSuccess;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public double getMscMoAttempt() {
		return mscMoAttempt;
	}
	public void setMscMoAttempt(double mscMoAttempt) {
		this.mscMoAttempt = mscMoAttempt;
	}
	public double getMscMsmoAtt() {
		return mscMsmoAtt;
	}
	public void setMscMsmoAtt(double mscMsmoAtt) {
		this.mscMsmoAtt = mscMsmoAtt;
	}
	public double getMscMoSuccess() {
		return mscMoSuccess;
	}
	public void setMscMoSuccess(double mscMoSuccess) {
		this.mscMoSuccess = mscMoSuccess;
	}
	public double getMtAttempts() {
		return mtAttempts;
	}
	public void setMtAttempts(double mtAttempts) {
		this.mtAttempts = mtAttempts;
	}
	public double getMtSuccess() {
		return mtSuccess;
	}
	public void setMtSuccess(double mtSuccess) {
		this.mtSuccess = mtSuccess;
	}
	public SmsDailySwithcesHourlyData()
{}
}
