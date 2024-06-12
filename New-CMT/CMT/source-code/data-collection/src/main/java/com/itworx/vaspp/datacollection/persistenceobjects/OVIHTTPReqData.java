package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OVIHTTPReqData extends PersistenceObject{
	public Date dateTime;
	public String httpMethodName;
	public double httpPaymentStatusId;
	public String httpPaStatusDesc;
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
	public String getHttpMethodName() {
		return httpMethodName;
	}
	public void setHttpMethodName(String httpMethodName) {
		this.httpMethodName = httpMethodName;
	}
	public double getHttpPaymentStatusId() {
		return httpPaymentStatusId;
	}
	public void setHttpPaymentStatusId(double httpPaymentStatusId) {
		this.httpPaymentStatusId = httpPaymentStatusId;
	}
	public String getHttpPaStatusDesc() {
		return httpPaStatusDesc;
	}
	public void setHttpPaStatusDesc(String httpPaStatusDesc) {
		this.httpPaStatusDesc = httpPaStatusDesc;
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
public OVIHTTPReqData()
{
}
}
