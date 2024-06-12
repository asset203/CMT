package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class ChargingInterModuleDate  extends PersistenceObject{
	public double moduleTypeId;
	public String moduleTypeDesc;
	public void setModuleTypeId( double moduleTypeId)
	{
		this.moduleTypeId=moduleTypeId;
	}
	public double getModuleTypeId()
	{
		return this.moduleTypeId;
	}
	public void setModuleTypeDesc(String moduleTypeDesc)
	{
		this.moduleTypeDesc=moduleTypeDesc;
	}
    public String getModuleTypeDesc()
    {
    	return this.moduleTypeDesc;
    }
}
