package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CFSubscriptionData extends PersistenceObject{
	public Date dateTime;
	public double serviceId;
	public double status;
	public double packageId;
	public double autoRenew;
	public double custTypeId;
	public double defaultActionCode;
	public double defNotifFlag;
	public double defInlistActionCode;
	public double defInlistNotiFlag;
	public double listStatus;
	public double count;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getServiceId() {
		return serviceId;
	}
	public void setServiceId(double serviceId) {
		this.serviceId = serviceId;
	}
	public double getStatus() {
		return status;
	}
	public void setStatus(double status) {
		this.status = status;
	}
	public double getPackageId() {
		return packageId;
	}
	public void setPackageId(double packageId) {
		this.packageId = packageId;
	}
	public double getAutoRenew() {
		return autoRenew;
	}
	public void setAutoRenew(double autoRenew) {
		this.autoRenew = autoRenew;
	}
	public double getCustTypeId() {
		return custTypeId;
	}
	public void setCustTypeId(double custTypeId) {
		this.custTypeId = custTypeId;
	}
	public double getDefaultActionCode() {
		return defaultActionCode;
	}
	public void setDefaultActionCode(double defaultActionCode) {
		this.defaultActionCode = defaultActionCode;
	}
	public double getDefNotifFlag() {
		return defNotifFlag;
	}
	public void setDefNotifFlag(double defNotifFlag) {
		this.defNotifFlag = defNotifFlag;
	}
	public double getDefInlistActionCode() {
		return defInlistActionCode;
	}
	public void setDefInlistActionCode(double defInlistActionCode) {
		this.defInlistActionCode = defInlistActionCode;
	}
	public double getDefInlistNotiFlag() {
		return defInlistNotiFlag;
	}
	public void setDefInlistNotiFlag(double defInlistNotiFlag) {
		this.defInlistNotiFlag = defInlistNotiFlag;
	}
	public double getListStatus() {
		return listStatus;
	}
	public void setListStatus(double listStatus) {
		this.listStatus = listStatus;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
public CFSubscriptionData()
{
	}
}
