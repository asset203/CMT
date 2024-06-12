package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class SmppQuotaData  extends PersistenceObject{

	
	
	public Date dateTime;
	public String schemaName;
	public String applicationID;
	public String applicationName;
	public double  threshold;
	public double appQuota;
	public double  noOfAllowedSMS;
	public double  detailCount;
	public double  firstTime;
	public double secondTime;
	public double thirdTrial;
	public double fourthTrial;
	public double fifthTrial;
	
	public SmppQuotaData()
	{}
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getAppQuota() {
		return appQuota;
	}

	public void setAppQuota(double appQuota) {
		this.appQuota = appQuota;
	}

	public double getNoOfAllowedSMS() {
		return noOfAllowedSMS;
	}

	public void setNoOfAllowedSMS(double noOfAllowedSMS) {
		this.noOfAllowedSMS = noOfAllowedSMS;
	}

	public double getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(double detailCount) {
		this.detailCount = detailCount;
	}

	public double getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(double firstTime) {
		this.firstTime = firstTime;
	}

	public double getSecondTime() {
		return secondTime;
	}

	public void setSecondTime(double secondTime) {
		this.secondTime = secondTime;
	}

	public double getThirdTrial() {
		return thirdTrial;
	}

	public void setThirdTrial(double thirdTrial) {
		this.thirdTrial = thirdTrial;
	}

	public double getFourthTrial() {
		return fourthTrial;
	}

	public void setFourthTrial(double fourthTrial) {
		this.fourthTrial = fourthTrial;
	}

	public double getFifthTrial() {
		return fifthTrial;
	}

	public void setFifthTrial(double fifthTrial) {
		this.fifthTrial = fifthTrial;
	}

	
}
