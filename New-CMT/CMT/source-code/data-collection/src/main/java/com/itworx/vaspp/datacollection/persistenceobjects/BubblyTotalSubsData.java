package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class BubblyTotalSubsData extends PersistenceObject{
	public Date dateTime;
	public double status;
	public double privacy;
	public double productId;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getStatus() {
		return status;
	}
	public void setStatus(double status) {
		this.status = status;
	}
	public double getPrivacy() {
		return privacy;
	}
	public void setPrivacy(double privacy) {
		this.privacy = privacy;
	}
	public double getProductId() {
		return productId;
	}
	public void setProductId(double productId) {
		this.productId = productId;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public BubblyTotalSubsData()
{
}
}
