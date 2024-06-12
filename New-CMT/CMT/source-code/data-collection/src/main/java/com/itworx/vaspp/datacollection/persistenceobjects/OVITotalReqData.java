package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OVITotalReqData extends PersistenceObject{
	public Date dateTime;
	public String serviceName;
	public double paymentStatusId;
	public String paymentStatusDesc;
	public double count;
	public double minProcTime;
	public double maxProcTime;
	public double avgProcTime;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public double getPaymentStatusId() {
		return paymentStatusId;
	}
	public void setPaymentStatusId(double paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}
	public String getPaymentStatusDesc() {
		return paymentStatusDesc;
	}
	public void setPaymentStatusDesc(String paymentStatusDesc) {
		this.paymentStatusDesc = paymentStatusDesc;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public double getMinProcTime() {
		return minProcTime;
	}
	public void setMinProcTime(double minProcTime) {
		this.minProcTime = minProcTime;
	}
	public double getMaxProcTime() {
		return maxProcTime;
	}
	public void setMaxProcTime(double maxProcTime) {
		this.maxProcTime = maxProcTime;
	}
	public double getAvgProcTime() {
		return avgProcTime;
	}
	public void setAvgProcTime(double avgProcTime) {
		this.avgProcTime = avgProcTime;
	}
public OVITotalReqData()
{
}
}
