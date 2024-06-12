package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class RamadanUssdConnData extends PersistenceObject{
	public Date dateTime;
	public double enqReqCount;
	public double enqRespCount;
	public double giftReqCount;
	public double giftRespCount;
	public double redReqCount;
	public double redRespCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getEnqReqCount() {
		return enqReqCount;
	}
	public void setEnqReqCount(double enqReqCount) {
		this.enqReqCount = enqReqCount;
	}
	public double getEnqRespCount() {
		return enqRespCount;
	}
	public void setEnqRespCount(double enqRespCount) {
		this.enqRespCount = enqRespCount;
	}
	public double getGiftReqCount() {
		return giftReqCount;
	}
	public void setGiftReqCount(double giftReqCount) {
		this.giftReqCount = giftReqCount;
	}
	public double getGiftRespCount() {
		return giftRespCount;
	}
	public void setGiftRespCount(double giftRespCount) {
		this.giftRespCount = giftRespCount;
	}
	public double getRedReqCount() {
		return redReqCount;
	}
	public void setRedReqCount(double redReqCount) {
		this.redReqCount = redReqCount;
	}
	public double getRedRespCount() {
		return redRespCount;
	}
	public void setRedRespCount(double redRespCount) {
		this.redRespCount = redRespCount;
	}
public RamadanUssdConnData()
{
	}
}
