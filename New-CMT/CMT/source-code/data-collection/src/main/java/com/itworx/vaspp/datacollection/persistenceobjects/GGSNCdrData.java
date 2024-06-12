package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class GGSNCdrData extends PersistenceObject{
	
	public Date dateTime;
	public String ssgn;
	public String rat;
	public String cc;		
	public String apn;
	public double dn;
	public double up;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getSsgn() {
		return ssgn;
	}
	public void setSsgn(String ssgn) {
		this.ssgn = ssgn;
	}
	public String getRat() {
		return rat;
	}
	public void setRat(String rat) {
		this.rat = rat;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public double getDn() {
		return dn;
	}
	public void setDn(double dn) {
		this.dn = dn;
	}
	public double getUp() {
		return up;
	}
	public void setUp(double up) {
		this.up = up;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public GGSNCdrData()
{}
}
