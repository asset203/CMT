package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SquareTransactionsData extends PersistenceObject{
	public Date dateTime;
	public double ussdReq;
	public double avgProccTime;
	public double noOfSentSms;
	public double noOfProcReq ;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getUssdReq() {
		return ussdReq;
	}
	public void setUssdReq(double ussdReq) {
		this.ussdReq = ussdReq;
	}
	public double getAvgProccTime() {
		return avgProccTime;
	}
	public void setAvgProccTime(double avgProccTime) {
		this.avgProccTime = avgProccTime;
	}
	public double getNoOfSentSms() {
		return noOfSentSms;
	}
	public void setNoOfSentSms(double noOfSentSms) {
		this.noOfSentSms = noOfSentSms;
	}
	public double getNoOfProcReq() {
		return noOfProcReq;
	}
	public void setNoOfProcReq(double noOfProcReq) {
		this.noOfProcReq = noOfProcReq;
	}
public SquareTransactionsData()
{
	}
}
