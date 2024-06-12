package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsAOData extends PersistenceObject{
	public Date dateTime;
	public double moSuccSMS;
	public double moDecSMS;
	public double moUnknownSMS;
	public double moMiscErrorSMS;
	public double moTimeOutSMS;
	public double mtSuccSMS;
	public double mtAbsSub;
	public double mtMemFull;
	public double mtMiscErrSMS;
	public double mtTimeSMS;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getMoSuccSMS() {
		return moSuccSMS;
	}
	public void setMoSuccSMS(double moSuccSMS) {
		this.moSuccSMS = moSuccSMS;
	}
	public double getMoDecSMS() {
		return moDecSMS;
	}
	public void setMoDecSMS(double moDecSMS) {
		this.moDecSMS = moDecSMS;
	}
	public double getMoUnknownSMS() {
		return moUnknownSMS;
	}
	public void setMoUnknownSMS(double moUnknownSMS) {
		this.moUnknownSMS = moUnknownSMS;
	}
	public double getMoMiscErrorSMS() {
		return moMiscErrorSMS;
	}
	public void setMoMiscErrorSMS(double moMiscErrorSMS) {
		this.moMiscErrorSMS = moMiscErrorSMS;
	}
	public double getMoTimeOutSMS() {
		return moTimeOutSMS;
	}
	public void setMoTimeOutSMS(double moTimeOutSMS) {
		this.moTimeOutSMS = moTimeOutSMS;
	}
	public double getMtSuccSMS() {
		return mtSuccSMS;
	}
	public void setMtSuccSMS(double mtSuccSMS) {
		this.mtSuccSMS = mtSuccSMS;
	}
	public double getMtAbsSub() {
		return mtAbsSub;
	}
	public void setMtAbsSub(double mtAbsSub) {
		this.mtAbsSub = mtAbsSub;
	}
	public double getMtMemFull() {
		return mtMemFull;
	}
	public void setMtMemFull(double mtMemFull) {
		this.mtMemFull = mtMemFull;
	}
	public double getMtMiscErrSMS() {
		return mtMiscErrSMS;
	}
	public void setMtMiscErrSMS(double mtMiscErrSMS) {
		this.mtMiscErrSMS = mtMiscErrSMS;
	}
	public double getMtTimeSMS() {
		return mtTimeSMS;
	}
	public void setMtTimeSMS(double mtTimeSMS) {
		this.mtTimeSMS = mtTimeSMS;
	}
public SmsAOData()
{
	
}
}
