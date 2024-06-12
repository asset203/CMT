package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsDailySwithcesData  extends PersistenceObject{
	public Date dateTime;
	public double mscMoAtt;
	public  double mscMsmoAttempt;
	public double mscMoSucc;
	public double mtAttempts;
	public double mtSuccess;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}	
	public double getMscMoAtt() {
		return mscMoAtt;
	}
	public void setMscMoAtt(double mscMoAtt) {
		this.mscMoAtt = mscMoAtt;
	}
	public double getMscMsmoAttempt() {
		return mscMsmoAttempt;
	}
	public void setMscMsmoAttempt(double mscMsmoAttempt) {
		this.mscMsmoAttempt = mscMsmoAttempt;
	}
	public double getMscMoSucc() {
		return mscMoSucc;
	}
	public void setMscMoSucc(double mscMoSucc) {
		this.mscMoSucc = mscMoSucc;
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
public SmsDailySwithcesData()
{}
}
