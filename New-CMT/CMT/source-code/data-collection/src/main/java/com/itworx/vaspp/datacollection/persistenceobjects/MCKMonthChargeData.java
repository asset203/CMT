package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MCKMonthChargeData extends PersistenceObject{
	public Date createTime;
	public Date chargeTime;
	public String levelID;
	public String funcName;
	public double status;
	

public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getChargeTime() {
		return chargeTime;
	}


	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}


	public String getLevelID() {
		return levelID;
	}


	public void setLevelID(String levelID) {
		this.levelID = levelID;
	}


	public String getFuncName() {
		return funcName;
	}


	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}


	public double getStatus() {
		return status;
	}


	public void setStatus(double status) {
		this.status = status;
	}


public MCKMonthChargeData()
{}
}
