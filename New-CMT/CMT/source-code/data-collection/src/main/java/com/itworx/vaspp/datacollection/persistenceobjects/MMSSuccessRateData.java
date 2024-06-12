package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MMSSuccessRateData extends PersistenceObject {
	public String customerGroup;
	public Date reportingDate;
	public Date dateTime;
	public String target;
	public String result;
	public double value;
	
public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	public Date getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

public MMSSuccessRateData()
{}
}
