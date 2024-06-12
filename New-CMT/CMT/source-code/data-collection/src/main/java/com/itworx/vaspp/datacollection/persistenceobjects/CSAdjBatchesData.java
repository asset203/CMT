package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CSAdjBatchesData extends PersistenceObject {

	public Date dateTime;
	public long adjFile;
	public long adjReq;
	public long promFile;
	public long promReq;
	public long refillFile;
	public long refillReq;
	public long getAdjFile() {
		return adjFile;
	}
	public void setAdjFile(long adjFile) {
		this.adjFile = adjFile;
	}
	public long getAdjReq() {
		return adjReq;
	}
	public void setAdjReq(long adjReq) {
		this.adjReq = adjReq;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getPromFile() {
		return promFile;
	}
	public void setPromFile(long promFile) {
		this.promFile = promFile;
	}
	public long getPromReq() {
		return promReq;
	}
	public void setPromReq(long promReq) {
		this.promReq = promReq;
	}
	public long getRefillFile() {
		return refillFile;
	}
	public void setRefillFile(long refillFile) {
		this.refillFile = refillFile;
	}
	public long getRefillReq() {
		return refillReq;
	}
	public void setRefillReq(long refillReq) {
		this.refillReq = refillReq;
	}

}
