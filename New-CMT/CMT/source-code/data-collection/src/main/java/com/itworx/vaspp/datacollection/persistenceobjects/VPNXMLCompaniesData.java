package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class VPNXMLCompaniesData  extends PersistenceObject{
	public Date dateTime;
    public double companies;
	
	
	public void setDateTime (Date dateTime)
	{
		this.dateTime=dateTime;
	}
	public Date getDateTime()
	{
		return this.dateTime;
    }
	
	public void setCompanies(double companies)
	{
		this.companies=companies;
	}
	public double getCompanies()
	{
		return this.companies;
	}
}
