package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class ChargingErrorCodeDate extends PersistenceObject {
	public double errorCode;
	public String errorDescription;
	public void setErrorCode(double errorCode)
	{
		this.errorCode=errorCode;
	}
	public double getErrorCode()
	{
		return this.errorCode;
	}
	public void setErrorDescription(String errorDescription)
	{
		this.errorDescription=errorDescription;
	}
	public String getErrorDescription()
	{
		return this.errorDescription;
	}
	
	
	
	
	
	

}
