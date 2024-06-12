package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class PerformanceSnapshotData extends PersistenceObject {
	
	public Date dateTime;
	
	public String instanceName;
	
	public Date openDate;
	
	public long bufferHitRatio;
	
	public long dataDictCacheHitRatio;
	
	public long libraryCacheHitRatio;
	
	public long redoLogSpaceRequests;
	
	public long totSql;
	
	public long sqlExecutionNow;

	public long getBufferHitRatio() {
		return bufferHitRatio;
	}

	public void setBufferHitRatio(long bufferHitRatio) {
		this.bufferHitRatio = bufferHitRatio;
	}

	public long getDataDictCacheHitRatio() {
		return dataDictCacheHitRatio;
	}

	public void setDataDictCacheHitRatio(long dataDictCacheHitRatio) {
		this.dataDictCacheHitRatio = dataDictCacheHitRatio;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public long getLibraryCacheHitRatio() {
		return libraryCacheHitRatio;
	}

	public void setLibraryCacheHitRatio(long libraryCacheHitRatio) {
		this.libraryCacheHitRatio = libraryCacheHitRatio;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public long getRedoLogSpaceRequests() {
		return redoLogSpaceRequests;
	}

	public void setRedoLogSpaceRequests(long redoLogSpaceRequests) {
		this.redoLogSpaceRequests = redoLogSpaceRequests;
	}

	public long getSqlExecutionNow() {
		return sqlExecutionNow;
	}

	public void setSqlExecutionNow(long sqlExecutionNow) {
		this.sqlExecutionNow = sqlExecutionNow;
	}

	public long getTotSql() {
		return totSql;
	}

	public void setTotSql(long totSql) {
		this.totSql = totSql;
	} 

}
