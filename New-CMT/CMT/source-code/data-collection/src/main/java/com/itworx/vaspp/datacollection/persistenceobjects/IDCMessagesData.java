package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class IDCMessagesData extends PersistenceObject{
	public double trans;
	public String transName;
	public double transStatus;
	public String userSession;
	public Date trxDate;
public double getTrans() {
		return trans;
	}
	public void setTrans(double trans) {
		this.trans = trans;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public double getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(double transStatus) {
		this.transStatus = transStatus;
	}
	public String getUserSession() {
		return userSession;
	}
	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}
public IDCMessagesData()
{
	}
}
