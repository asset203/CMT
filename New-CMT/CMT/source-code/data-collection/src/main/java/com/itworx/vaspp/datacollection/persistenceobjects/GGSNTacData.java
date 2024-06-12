package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class GGSNTacData extends PersistenceObject{
	public Date dateTime;
	public double tac;
	public double upLink;
	public double downLink;
	public double count;
	public String marketingName;
	public String manufactureName;
	public String tacType;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTac() {
		return tac;
	}
	public void setTac(double tac) {
		this.tac = tac;
	}
	public double getUpLink() {
		return upLink;
	}
	public void setUpLink(double upLink) {
		this.upLink = upLink;
	}
	public double getDownLink() {
		return downLink;
	}
	public void setDownLink(double downLink) {
		this.downLink = downLink;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	public String getManufactureName() {
		return manufactureName;
	}
	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}
	public String getTacType() {
		return tacType;
	}
	public void setTacType(String tacType) {
		this.tacType = tacType;
	}
public GGSNTacData()
{}
}
