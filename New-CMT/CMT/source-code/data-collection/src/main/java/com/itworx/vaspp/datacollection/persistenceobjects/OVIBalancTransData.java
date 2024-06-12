package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class OVIBalancTransData extends PersistenceObject{
	public Date dateTime;
	public double remoteServcerTypeId;
	public String transTypeName;
	public double remoteServerRespCode;
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
	public double getRemoteServcerTypeId() {
		return remoteServcerTypeId;
	}
	public void setRemoteServcerTypeId(double remoteServcerTypeId) {
		this.remoteServcerTypeId = remoteServcerTypeId;
	}
	public String getTransTypeName() {
		return transTypeName;
	}
	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}
	public double getRemoteServerRespCode() {
		return remoteServerRespCode;
	}
	public void setRemoteServerRespCode(double remoteServerRespCode) {
		this.remoteServerRespCode = remoteServerRespCode;
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
public OVIBalancTransData()
{
}
}
