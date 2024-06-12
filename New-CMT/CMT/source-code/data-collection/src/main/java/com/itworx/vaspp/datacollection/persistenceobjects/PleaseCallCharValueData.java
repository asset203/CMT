package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class PleaseCallCharValueData extends PersistenceObject{
	public Date datTime;
	public String charValue;
	public double count;
public Date getDatTime() {
		return datTime;
	}
	public void setDatTime(Date datTime) {
		this.datTime = datTime;
	}
	public String getCharValue() {
		return charValue;
	}
	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public PleaseCallCharValueData()
{
}
}
