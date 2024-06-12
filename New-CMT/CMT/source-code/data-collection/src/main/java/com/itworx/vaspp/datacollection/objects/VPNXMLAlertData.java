package com.itworx.vaspp.datacollection.objects;

import java.util.Date;

public class VPNXMLAlertData {
	private Date dateTime;
	private String nodeName;
	private String counterName;
	private long todayValue = 0;

	public long getTodayValue() {
		return todayValue;
	}

	public void setTodayValue(long todayValue) {
		this.todayValue = todayValue;
	}

	public long getYesterdayValue() {
		return yesterdayValue;
	}

	public void setYesterdayValue(long yesterdayValue) {
		this.yesterdayValue = yesterdayValue;
	}

	private long yesterdayValue = 0;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

}
