package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BuzzUniqueByClientData extends PersistenceObject{
	
	public Date dateTime;
	public long jmeUser;
	public long symbianUser;
	public long androidUser;
	public long iosUser;
	public long blackberryUser;
	public double jmePercentage;
	public double symbianPercentage;
	public double androidPercentage;
	public double iosPercentage;
	public double blackberryPercentage;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getJmeUser() {
		return jmeUser;
	}
	public void setJmeUser(long jmeUser) {
		this.jmeUser = jmeUser;
	}
	public long getSymbianUser() {
		return symbianUser;
	}
	public void setSymbianUser(long symbianUser) {
		this.symbianUser = symbianUser;
	}
	public long getAndroidUser() {
		return androidUser;
	}
	public void setAndroidUser(long androidUser) {
		this.androidUser = androidUser;
	}
	public long getIosUser() {
		return iosUser;
	}
	public void setIosUser(long iosUser) {
		this.iosUser = iosUser;
	}
	public long getBlackberryUser() {
		return blackberryUser;
	}
	public void setBlackberryUser(long blackberryUser) {
		this.blackberryUser = blackberryUser;
	}
	public double getJmePercentage() {
		return jmePercentage;
	}
	public void setJmePercentage(double jmePercentage) {
		this.jmePercentage = jmePercentage;
	}
	public double getSymbianPercentage() {
		return symbianPercentage;
	}
	public void setSymbianPercentage(double symbianPercentage) {
		this.symbianPercentage = symbianPercentage;
	}
	public double getAndroidPercentage() {
		return androidPercentage;
	}
	public void setAndroidPercentage(double androidPercentage) {
		this.androidPercentage = androidPercentage;
	}
	public double getIosPercentage() {
		return iosPercentage;
	}
	public void setIosPercentage(double iosPercentage) {
		this.iosPercentage = iosPercentage;
	}
	public double getBlackberryPercentage() {
		return blackberryPercentage;
	}
	public void setBlackberryPercentage(double blackberryPercentage) {
		this.blackberryPercentage = blackberryPercentage;
	}
	

}
