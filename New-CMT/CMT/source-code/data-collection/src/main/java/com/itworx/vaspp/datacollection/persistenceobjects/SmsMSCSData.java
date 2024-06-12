package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsMSCSData extends PersistenceObject {
	public Date dateTime;
	public String smsNodeName;
	public String hour;
	public double mscMoSucc;
	public double mscMoAtt;
	public double mscMsmoAtt;
	public double mtAttempts;
	public double mtSuccess;

public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSmsNodeName() {
		return smsNodeName;
	}

	public void setSmsNodeName(String smsNodeName) {
		this.smsNodeName = smsNodeName;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public double getMscMoSucc() {
		return mscMoSucc;
	}

	public void setMscMoSucc(double mscMoSucc) {
		this.mscMoSucc = mscMoSucc;
	}

	public double getMscMoAtt() {
		return mscMoAtt;
	}

	public void setMscMoAtt(double mscMoAtt) {
		this.mscMoAtt = mscMoAtt;
	}

	public double getMscMsmoAtt() {
		return mscMsmoAtt;
	}

	public void setMscMsmoAtt(double mscMsmoAtt) {
		this.mscMsmoAtt = mscMsmoAtt;
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

public SmsMSCSData()
{}
}
