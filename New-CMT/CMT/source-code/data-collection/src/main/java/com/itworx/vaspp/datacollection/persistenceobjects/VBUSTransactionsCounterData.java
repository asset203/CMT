package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VBUSTransactionsCounterData extends PersistenceObject{
	
	public Date dateTime;
	public String sdpName;
	public String hlrName;
	public double barTransCount;
	public double unbarTransCount;
	public double barSuccTransCount;
	public double barFailTransCount;
	public double unbarSuccTransCount;
	public double unbarFailTransCount;
	public double barSuccRetryCount;
	public double barFailRetryCount;
	public double unbarSuccRetryCount;
	public double unbarFailRetryCount;
	public double maxBarTransTime;
	public double minBarTransTime;
	public double avgBarTransTime;
	public double maxUnbarTransTime;
	public double minUnbarTransTime;
	public double avgUnbarTransTime;
	
	public double getAvgBarTransTime() {
		return avgBarTransTime;
	}
	public void setAvgBarTransTime(double avgBarTransTime) {
		this.avgBarTransTime = avgBarTransTime;
	}
	public double getAvgUnbarTransTime() {
		return avgUnbarTransTime;
	}
	public void setAvgUnbarTransTime(double avgUnbarTransTime) {
		this.avgUnbarTransTime = avgUnbarTransTime;
	}
	public double getBarFailRetryCount() {
		return barFailRetryCount;
	}
	public void setBarFailRetryCount(double barFailRetryCount) {
		this.barFailRetryCount = barFailRetryCount;
	}
	public double getBarFailTransCount() {
		return barFailTransCount;
	}
	public void setBarFailTransCount(double barFailTransCount) {
		this.barFailTransCount = barFailTransCount;
	}
	public double getBarSuccRetryCount() {
		return barSuccRetryCount;
	}
	public void setBarSuccRetryCount(double barSuccRetryCount) {
		this.barSuccRetryCount = barSuccRetryCount;
	}
	public double getBarSuccTransCount() {
		return barSuccTransCount;
	}
	public void setBarSuccTransCount(double barSuccTransCount) {
		this.barSuccTransCount = barSuccTransCount;
	}
	public double getBarTransCount() {
		return barTransCount;
	}
	public void setBarTransCount(double barTransCount) {
		this.barTransCount = barTransCount;
	}
	public String getHlrName() {
		return hlrName;
	}
	public void setHlrName(String hlrName) {
		this.hlrName = hlrName;
	}
	public double getMaxBarTransTime() {
		return maxBarTransTime;
	}
	public void setMaxBarTransTime(double maxBarTransTime) {
		this.maxBarTransTime = maxBarTransTime;
	}
	public double getMaxUnbarTransTime() {
		return maxUnbarTransTime;
	}
	public void setMaxUnbarTransTime(double maxUnbarTransTime) {
		this.maxUnbarTransTime = maxUnbarTransTime;
	}
	public double getMinBarTransTime() {
		return minBarTransTime;
	}
	public void setMinBarTransTime(double minBarTransTime) {
		this.minBarTransTime = minBarTransTime;
	}
	public double getMinUnbarTransTime() {
		return minUnbarTransTime;
	}
	public void setMinUnbarTransTime(double minUnbarTransTime) {
		this.minUnbarTransTime = minUnbarTransTime;
	}
	public String getSdpName() {
		return sdpName;
	}
	public void setSdpName(String sdpName) {
		this.sdpName = sdpName;
	}
	public double getUnbarFailRetryCount() {
		return unbarFailRetryCount;
	}
	public void setUnbarFailRetryCount(double unbarFailRetryCount) {
		this.unbarFailRetryCount = unbarFailRetryCount;
	}
	public double getUnbarFailTransCount() {
		return unbarFailTransCount;
	}
	public void setUnbarFailTransCount(double unbarFailTransCount) {
		this.unbarFailTransCount = unbarFailTransCount;
	}
	public double getUnbarSuccRetryCount() {
		return unbarSuccRetryCount;
	}
	public void setUnbarSuccRetryCount(double unbarSuccRetryCount) {
		this.unbarSuccRetryCount = unbarSuccRetryCount;
	}
	public double getUnbarSuccTransCount() {
		return unbarSuccTransCount;
	}
	public void setUnbarSuccTransCount(double unbarSuccTransCount) {
		this.unbarSuccTransCount = unbarSuccTransCount;
	}
	public double getUnbarTransCount() {
		return unbarTransCount;
	}
	public void setUnbarTransCount(double unbarTransCount) {
		this.unbarTransCount = unbarTransCount;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
