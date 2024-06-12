package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BuzzDownloadsData extends PersistenceObject{
	public Date dateTime;
	public long blackberryDownload;
	public long symbianDownload;
	public long jmeDownload;
	public long androidDownload;
	public long iphoneDownload;
	public long totalDownload;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getBlackberryDownload() {
		return blackberryDownload;
	}
	public void setBlackberryDownload(long blackberryDownload) {
		this.blackberryDownload = blackberryDownload;
	}
	public long getSymbianDownload() {
		return symbianDownload;
	}
	public void setSymbianDownload(long symbianDownload) {
		this.symbianDownload = symbianDownload;
	}
	public long getJmeDownload() {
		return jmeDownload;
	}
	public void setJmeDownload(long jmeDownload) {
		this.jmeDownload = jmeDownload;
	}
	public long getAndroidDownload() {
		return androidDownload;
	}
	public void setAndroidDownload(long androidDownload) {
		this.androidDownload = androidDownload;
	}
	public long getIphoneDownload() {
		return iphoneDownload;
	}
	public void setIphoneDownload(long iphoneDownload) {
		this.iphoneDownload = iphoneDownload;
	}
	public long getTotalDownload() {
		return totalDownload;
	}
	public void setTotalDownload(long totalDownload) {
		this.totalDownload = totalDownload;
	}

}
