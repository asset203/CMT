package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class UssdShortCodesC2TransData extends PersistenceObject{
	public Date dateTime;
	public String shortCode;
	public double noOfEnquire;
	public double noOfRedention;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public double getNoOfEnquire() {
		return noOfEnquire;
	}
	public void setNoOfEnquire(double noOfEnquire) {
		this.noOfEnquire = noOfEnquire;
	}
	public double getNoOfRedention() {
		return noOfRedention;
	}
	public void setNoOfRedention(double noOfRedention) {
		this.noOfRedention = noOfRedention;
	}
public UssdShortCodesC2TransData()
{
	}
}
