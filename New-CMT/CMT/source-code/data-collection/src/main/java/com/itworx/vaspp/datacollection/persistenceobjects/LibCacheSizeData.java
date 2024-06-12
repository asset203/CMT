package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class LibCacheSizeData extends PersistenceObject{
	
	public Date date;
	
	public long sumShMemOfObjectCache;
	
	public long sumShMemOfSqlArea;
	
	public long sumShMemOfUsersOpening;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getSumShMemOfObjectCache() {
		return sumShMemOfObjectCache;
	}

	public void setSumShMemOfObjectCache(long sumShMemOfObjectCache) {
		this.sumShMemOfObjectCache = sumShMemOfObjectCache;
	}

	public long getSumShMemOfSqlArea() {
		return sumShMemOfSqlArea;
	}

	public void setSumShMemOfSqlArea(long sumShMemOfSqlArea) {
		this.sumShMemOfSqlArea = sumShMemOfSqlArea;
	}

	public long getSumShMemOfUsersOpening() {
		return sumShMemOfUsersOpening;
	}

	public void setSumShMemOfUsersOpening(long sumShMemOfUsersOpening) {
		this.sumShMemOfUsersOpening = sumShMemOfUsersOpening;
	}

}
