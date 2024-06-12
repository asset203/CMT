package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VRBTNetworkUtilizationData extends PersistenceObject{
	public Date dateTime;
	public String site;
	public double dpc;
	
	public double opc;
	public double ports;
	public double totalCalls;
	public double successfulCalls;
	public double failedCalls;
	public double meanHoldTime;
	public double earlanges;
	public double utilizationInPercent;
	public double asr;
	
	public VRBTNetworkUtilizationData()
	{}
	public double getDpc() {
		return dpc;
	}
	public void setDpc(double dpc) {
		this.dpc = dpc;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	public double getOpc() {
		return opc;
	}
	public void setOpc(double opc) {
		this.opc = opc;
	}
	public double getPorts() {
		return ports;
	}
	public void setPorts(double ports) {
		this.ports = ports;
	}
	public double getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(double totalCalls) {
		this.totalCalls = totalCalls;
	}
	public double getSuccessfulCalls() {
		return successfulCalls;
	}
	public void setSuccessfulCalls(double successfulCalls) {
		this.successfulCalls = successfulCalls;
	}
	public double getFailedCalls() {
		return failedCalls;
	}
	public void setFailedCalls(double failedCalls) {
		this.failedCalls = failedCalls;
	}
	public double getMeanHoldTime() {
		return meanHoldTime;
	}
	public void setMeanHoldTime(double meanHoldTime) {
		this.meanHoldTime = meanHoldTime;
	}
	public double getEarlanges() {
		return earlanges;
	}
	public void setEarlanges(double earlanges) {
		this.earlanges = earlanges;
	}
	public double getUtilizationInPercent() {
		return utilizationInPercent;
	}
	public void setUtilizationInPercent(double utilizationInPercent) {
		this.utilizationInPercent = utilizationInPercent;
	}
	public double getAsr() {
		return asr;
	}
	public void setAsr(double asr) {
		this.asr = asr;
	}
	
}
