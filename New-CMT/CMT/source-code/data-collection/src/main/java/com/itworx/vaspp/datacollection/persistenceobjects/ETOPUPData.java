package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class ETOPUPData extends PersistenceObject{
	
	public Date transTime;
	
	public String strVouch;
	
	public String idErrorLevel;
	
	public double idErrorCode;
	
	public String strInformation;
	
	public String parentRole;
	
	public double getIdErrorCode() {
		return idErrorCode;
	}
	public void setIdErrorCode(double idErrorCode) {
		this.idErrorCode = idErrorCode;
	}
	public String getIdErrorLevel() {
		return idErrorLevel;
	}
	public void setIdErrorLevel(String idErrorLevel) {
		this.idErrorLevel = idErrorLevel;
	}
	public String getStrVouch() {
		return strVouch;
	}
	public void setStrVouch(String strVouch) {
		this.strVouch = strVouch;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getParentRole() {
		return parentRole;
	}
	public void setParentRole(String parentRole) {
		this.parentRole = parentRole;
	}
	public String getStrInformation() {
		return strInformation;
	}
	public void setStrInformation(String strInformation) {
		this.strInformation = strInformation;
	}
}
