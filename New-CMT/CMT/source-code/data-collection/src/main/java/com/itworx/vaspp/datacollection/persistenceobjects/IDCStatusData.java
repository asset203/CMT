package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class IDCStatusData extends PersistenceObject{
	public double trans;
	public double transStatus;
	public Date trxDate;
	public String userSession;
public double getTrans() {
		return trans;
	}
	public void setTrans(double trans) {
		this.trans = trans;
	}
	public double getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(double transStatus) {
		this.transStatus = transStatus;
	}
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
public IDCStatusData()
{}

}
