package com.itworx.vaspp.datacollection.persistenceobjects;


public class VPNCountersNameData extends PersistenceObject{
	
	public long counterId;
	
	public String counterName;

	public long getCounterId() {
		return counterId;
	}

	public void setCounterId(long counterId) {
		this.counterId = counterId;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

}
