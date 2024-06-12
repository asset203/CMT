package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;
public class UssdGwSubCodeTransactionsData extends PersistenceObject{
	
    public Date date;
	
    public long responseNumberPerSubCode;
	
	public long requestNumberPerSubCode;
	
	public String subCode;
	 


	public long getResponseNumberPerShortCode() {
		return responseNumberPerSubCode;
	}

	public void setResponseNumberPerShortCode(long responseNumberPerSubCode) {
		this.responseNumberPerSubCode = responseNumberPerSubCode;
	}

	public long getRequestNumberPerShortCode() {
		return requestNumberPerSubCode;
	}

	public long getResponseNumberPerSubCode() {
		return responseNumberPerSubCode;
	}

	public void setResponseNumberPerSubCode(long responseNumberPerSubCode) {
		this.responseNumberPerSubCode = responseNumberPerSubCode;
	}

	public long getRequestNumberPerSubCode() {
		return requestNumberPerSubCode;
	}

	public void setRequestNumberPerSubCode(long requestNumberPerSubCode) {
		this.requestNumberPerSubCode = requestNumberPerSubCode;
	}

	public void setRequestNumberPerShortCode(long requestNumberPerSubCode) {
		this.requestNumberPerSubCode = requestNumberPerSubCode;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	



}
