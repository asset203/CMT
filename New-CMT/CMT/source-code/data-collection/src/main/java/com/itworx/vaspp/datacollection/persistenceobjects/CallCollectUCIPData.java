package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CallCollectUCIPData extends PersistenceObject{
	public Date dateTime;
	public String payloadNumber;
	public String depitReason;
	public String depitAmount;
	public String resultCode;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getPayloadNumber() {
		return payloadNumber;
	}
	public void setPayloadNumber(String payloadNumber) {
		this.payloadNumber = payloadNumber;
	}
	public String getDepitReason() {
		return depitReason;
	}
	public void setDepitReason(String depitReason) {
		this.depitReason = depitReason;
	}
	public String getDepitAmount() {
		return depitAmount;
	}
	public void setDepitAmount(String depitAmount) {
		this.depitAmount = depitAmount;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public CallCollectUCIPData()
{
	}
}
