package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VPNCompanyData extends PersistenceObject{
	
	public Date time;
	
	public long companies;

	public long getCompanies() {
		return companies;
	}

	public void setCompanies(long companies) {
		this.companies = companies;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
