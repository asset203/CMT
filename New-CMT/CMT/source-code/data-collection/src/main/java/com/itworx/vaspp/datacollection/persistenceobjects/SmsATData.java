package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsATData extends PersistenceObject{
	public Date dateTime;
	public String time;
	public double moSuccSms;
	public double MoDecSMS;
	public double moUnknownSMS;
	public double moMiscErrSMS;
	public double moTimeOutSMS;
	public double mtSuccSMS;
	public double mtAbsSub;
	public double mtMemFull;
	public double mtMiscErrSms;
	public double mtTimeSMS;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getMoSuccSms() {
		return moSuccSms;
	}
	public void setMoSuccSms(double moSuccSms) {
		this.moSuccSms = moSuccSms;
	}
	public double getMoDecSMS() {
		return MoDecSMS;
	}
	public void setMoDecSMS(double moDecSMS) {
		MoDecSMS = moDecSMS;
	}
	public double getMoUnknownSMS() {
		return moUnknownSMS;
	}
	public void setMoUnknownSMS(double moUnknownSMS) {
		this.moUnknownSMS = moUnknownSMS;
	}
	public double getMoMiscErrSMS() {
		return moMiscErrSMS;
	}
	public void setMoMiscErrSMS(double moMiscErrSMS) {
		this.moMiscErrSMS = moMiscErrSMS;
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
	public double getMtMiscErrSms() {
		return mtMiscErrSms;
	}
	public void setMtMiscErrSms(double mtMiscErrSms) {
		this.mtMiscErrSms = mtMiscErrSms;
	}
	public double getMtTimeSMS() {
		return mtTimeSMS;
	}
	public void setMtTimeSMS(double mtTimeSMS) {
		this.mtTimeSMS = mtTimeSMS;
	}
public SmsATData()
{
	}
}
