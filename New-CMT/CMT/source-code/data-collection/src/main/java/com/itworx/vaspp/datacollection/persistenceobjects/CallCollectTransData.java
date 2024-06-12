package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CallCollectTransData extends PersistenceObject{
	public Date dateTime;
	public String msisdn;
	public double aPartyCount;
	public double bPartyCount;
	public double lowBalanceACount;
	public double lowBalanceBCount;
	public double oAnswerA;
	public double oAnswerB;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public double getaPartyCount() {
		return aPartyCount;
	}
	public void setaPartyCount(double aPartyCount) {
		this.aPartyCount = aPartyCount;
	}
	public double getbPartyCount() {
		return bPartyCount;
	}
	public void setbPartyCount(double bPartyCount) {
		this.bPartyCount = bPartyCount;
	}
	public double getLowBalanceACount() {
		return lowBalanceACount;
	}
	public void setLowBalanceACount(double lowBalanceACount) {
		this.lowBalanceACount = lowBalanceACount;
	}
	public double getLowBalanceBCount() {
		return lowBalanceBCount;
	}
	public void setLowBalanceBCount(double lowBalanceBCount) {
		this.lowBalanceBCount = lowBalanceBCount;
	}
	public double getoAnswerA() {
		return oAnswerA;
	}
	public void setoAnswerA(double oAnswerA) {
		this.oAnswerA = oAnswerA;
	}
	public double getoAnswerB() {
		return oAnswerB;
	}
	public void setoAnswerB(double oAnswerB) {
		this.oAnswerB = oAnswerB;
	}
public CallCollectTransData()
{
}
}
