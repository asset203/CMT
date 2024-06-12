package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CallCollNavCountData extends PersistenceObject{
	public Date dateTime;
	public String debitReason;
	public double debitAmmount;
	public double resultCode;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDebitReason() {
		return debitReason;
	}
	public void setDebitReason(String debitReason) {
		this.debitReason = debitReason;
	}
	public double getDebitAmmount() {
		return debitAmmount;
	}
	public void setDebitAmmount(double debitAmmount) {
		this.debitAmmount = debitAmmount;
	}
	public double getResultCode() {
		return resultCode;
	}
	public void setResultCode(double resultCode) {
		this.resultCode = resultCode;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public CallCollNavCountData()
	{}

}
