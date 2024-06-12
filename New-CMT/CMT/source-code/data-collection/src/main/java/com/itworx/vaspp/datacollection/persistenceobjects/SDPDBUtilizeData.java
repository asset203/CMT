package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SDPDBUtilizeData extends PersistenceObject{
	public Date dateTime;
	public String  premAllSize;
	public String  premInUseSize;
	public String  premInUseHighWater;
	public String  tempAllocateSize;
	public String  tempInUseSize;
	public String  tempInUseHighWater;

public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getPremAllSize() {
		return premAllSize;
	}

	public void setPremAllSize(String premAllSize) {
		this.premAllSize = premAllSize;
	}

	public String getPremInUseSize() {
		return premInUseSize;
	}

	public void setPremInUseSize(String premInUseSize) {
		this.premInUseSize = premInUseSize;
	}

	public String getPremInUseHighWater() {
		return premInUseHighWater;
	}

	public void setPremInUseHighWater(String premInUseHighWater) {
		this.premInUseHighWater = premInUseHighWater;
	}

	public String getTempAllocateSize() {
		return tempAllocateSize;
	}

	public void setTempAllocateSize(String tempAllocateSize) {
		this.tempAllocateSize = tempAllocateSize;
	}

	public String getTempInUseSize() {
		return tempInUseSize;
	}

	public void setTempInUseSize(String tempInUseSize) {
		this.tempInUseSize = tempInUseSize;
	}

	public String getTempInUseHighWater() {
		return tempInUseHighWater;
	}

	public void setTempInUseHighWater(String tempInUseHighWater) {
		this.tempInUseHighWater = tempInUseHighWater;
	}

public SDPDBUtilizeData()
{
}
}
