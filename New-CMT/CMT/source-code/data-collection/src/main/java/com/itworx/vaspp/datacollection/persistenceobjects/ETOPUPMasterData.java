package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class ETOPUPMasterData extends PersistenceObject{
	public String userId;
	public String geoDomainName;
public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGeoDomainName() {
		return geoDomainName;
	}
	public void setGeoDomainName(String geoDomainName) {
		this.geoDomainName = geoDomainName;
	}
public ETOPUPMasterData()
{}
}
