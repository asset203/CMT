package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MMSMonthlyTransData extends PersistenceObject {
	public Date dateTime;
	public String catName;
	public String name;
	public String  value;
public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
public MMSMonthlyTransData()
{}
}
