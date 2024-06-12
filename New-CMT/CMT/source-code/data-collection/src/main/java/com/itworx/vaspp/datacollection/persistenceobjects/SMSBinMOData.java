package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSBinMOData extends PersistenceObject{
	public Date dateTime;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String network;
	public String contry;
	public String originMSC;
	public double count;

	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getContry() {
		return contry;
	}
	public void setContry(String contry) {
		this.contry = contry;
	}
	public String getOriginMSC() {
		return originMSC;
	}
	public void setOriginMSC(String originMSC) {
		this.originMSC = originMSC;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public SMSBinMOData()
{
	}
}
