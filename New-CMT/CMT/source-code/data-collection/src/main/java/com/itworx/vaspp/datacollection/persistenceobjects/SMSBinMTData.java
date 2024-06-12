package com.itworx.vaspp.datacollection.persistenceobjects;


import com.itworx.vaspp.datacollection.persistenceobjects.PersistenceObject;
import java.util.*;
public class SMSBinMTData extends PersistenceObject{
	public Date dateTime;
	public String network;
	public String country;
	public String terminatedMSC;
	public double count;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTerminatedMSC() {
		return terminatedMSC;
	}
	public void setTerminatedMSC(String terminatedMSC) {
		this.terminatedMSC = terminatedMSC;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public SMSBinMTData()
	{
		
	}
}
