package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class AttemptsISTData extends PersistenceObject{
	public Date dateTime;
	public String switchs;
	public double ist;
	public double idpTimeOut;
	public double idpAttempts;
	public double idpRejected;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSwitchs() {
		return switchs;
	}
	public void setSwitchs(String switchs) {
		this.switchs = switchs;
	}
	public double getIst() {
		return ist;
	}
	public void setIst(double ist) {
		this.ist = ist;
	}
	public double getIdpTimeOut() {
		return idpTimeOut;
	}
	public void setIdpTimeOut(double idpTimeOut) {
		this.idpTimeOut = idpTimeOut;
	}
	public double getIdpAttempts() {
		return idpAttempts;
	}
	public void setIdpAttempts(double idpAttempts) {
		this.idpAttempts = idpAttempts;
	}
	public double getIdpRejected() {
		return idpRejected;
	}
	public void setIdpRejected(double idpRejected) {
		this.idpRejected = idpRejected;
	}
public AttemptsISTData()
{
}
}
