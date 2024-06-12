package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsDeviceDetData extends PersistenceObject {
	public Date dateTime;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getHour() {
		return hour;
	}
	public void setHour(double hour) {
		this.hour = hour;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public double getMoSuccSms() {
		return moSuccSms;
	}
	public void setMoSuccSms(double moSuccSms) {
		this.moSuccSms = moSuccSms;
	}
	public double getMoTimeSms() {
		return moTimeSms;
	}
	public void setMoTimeSms(double moTimeSms) {
		this.moTimeSms = moTimeSms;
	}
	public double getCommentID() {
		return commentID;
	}
	public void setCommentID(double commentID) {
		this.commentID = commentID;
	}
	public double getMoDecSms() {
		return moDecSms;
	}
	public void setMoDecSms(double moDecSms) {
		this.moDecSms = moDecSms;
	}
	public double getMoUnknownSms() {
		return moUnknownSms;
	}
	public void setMoUnknownSms(double moUnknownSms) {
		this.moUnknownSms = moUnknownSms;
	}
	public double getMoMiscErrSms() {
		return moMiscErrSms;
	}
	public void setMoMiscErrSms(double moMiscErrSms) {
		this.moMiscErrSms = moMiscErrSms;
	}
	public double getMtSuccSms() {
		return mtSuccSms;
	}
	public void setMtSuccSms(double mtSuccSms) {
		this.mtSuccSms = mtSuccSms;
	}
	public double getMtTimeOutSms() {
		return mtTimeOutSms;
	}
	public void setMtTimeOutSms(double mtTimeOutSms) {
		this.mtTimeOutSms = mtTimeOutSms;
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
	public double getMoOtherMapErrCtr() {
		return moOtherMapErrCtr;
	}
	public void setMoOtherMapErrCtr(double moOtherMapErrCtr) {
		this.moOtherMapErrCtr = moOtherMapErrCtr;
	}
	public double getMoDiscNackCtr() {
		return moDiscNackCtr;
	}
	public void setMoDiscNackCtr(double moDiscNackCtr) {
		this.moDiscNackCtr = moDiscNackCtr;
	}
	public double getMoRejectedTprCtr() {
		return moRejectedTprCtr;
	}
	public void setMoRejectedTprCtr(double moRejectedTprCtr) {
		this.moRejectedTprCtr = moRejectedTprCtr;
	}
	public double getMoSysFailCtr() {
		return moSysFailCtr;
	}
	public void setMoSysFailCtr(double moSysFailCtr) {
		this.moSysFailCtr = moSysFailCtr;
	}
	public double getMoInvalidSmeAdd() {
		return moInvalidSmeAdd;
	}
	public void setMoInvalidSmeAdd(double moInvalidSmeAdd) {
		this.moInvalidSmeAdd = moInvalidSmeAdd;
	}
	public double getMoNotScSubs() {
		return moNotScSubs;
	}
	public void setMoNotScSubs(double moNotScSubs) {
		this.moNotScSubs = moNotScSubs;
	}
	public double getMoSmSecMinObj() {
		return moSmSecMinObj;
	}
	public void setMoSmSecMinObj(double moSmSecMinObj) {
		this.moSmSecMinObj = moSmSecMinObj;
	}
	public double getMoSmSecMaxObj() {
		return moSmSecMaxObj;
	}
	public void setMoSmSecMaxObj(double moSmSecMaxObj) {
		this.moSmSecMaxObj = moSmSecMaxObj;
	}
	public double getMtSystFailCtr() {
		return mtSystFailCtr;
	}
	public void setMtSystFailCtr(double mtSystFailCtr) {
		this.mtSystFailCtr = mtSystFailCtr;
	}
	public double getMoOtherMapErr() {
		return moOtherMapErr;
	}
	public void setMoOtherMapErr(double moOtherMapErr) {
		this.moOtherMapErr = moOtherMapErr;
	}
	public double getMtUniSubsCtr() {
		return mtUniSubsCtr;
	}
	public void setMtUniSubsCtr(double mtUniSubsCtr) {
		this.mtUniSubsCtr = mtUniSubsCtr;
	}
	public double getMtIllSubsctr() {
		return mtIllSubsctr;
	}
	public void setMtIllSubsctr(double mtIllSubsctr) {
		this.mtIllSubsctr = mtIllSubsctr;
	}
	public double getMtSubsBusyCtr() {
		return mtSubsBusyCtr;
	}
	public void setMtSubsBusyCtr(double mtSubsBusyCtr) {
		this.mtSubsBusyCtr = mtSubsBusyCtr;
	}
	public double getMtEqProtCtr() {
		return mtEqProtCtr;
	}
	public void setMtEqProtCtr(double mtEqProtCtr) {
		this.mtEqProtCtr = mtEqProtCtr;
	}
	public double getMtTcapAbortCtr() {
		return mtTcapAbortCtr;
	}
	public void setMtTcapAbortCtr(double mtTcapAbortCtr) {
		this.mtTcapAbortCtr = mtTcapAbortCtr;
	}
	public double getMtSccpAbortCtr() {
		return mtSccpAbortCtr;
	}
	public void setMtSccpAbortCtr(double mtSccpAbortCtr) {
		this.mtSccpAbortCtr = mtSccpAbortCtr;
	}
	public double getMtHomePalSusCtr() {
		return mtHomePalSusCtr;
	}
	public void setMtHomePalSusCtr(double mtHomePalSusCtr) {
		this.mtHomePalSusCtr = mtHomePalSusCtr;
	}
	public double getMtHomePlaTrust() {
		return mtHomePlaTrust;
	}
	public void setMtHomePlaTrust(double mtHomePlaTrust) {
		this.mtHomePlaTrust = mtHomePlaTrust;
	}
	public double getMtHoScSuspCtr() {
		return mtHoScSuspCtr;
	}
	public void setMtHoScSuspCtr(double mtHoScSuspCtr) {
		this.mtHoScSuspCtr = mtHoScSuspCtr;
	}
	public double getMtHomScraTrustCtr() {
		return mtHomScraTrustCtr;
	}
	public void setMtHomScraTrustCtr(double mtHomScraTrustCtr) {
		this.mtHomScraTrustCtr = mtHomScraTrustCtr;
	}
	public double getMtSmSecMinObj() {
		return mtSmSecMinObj;
	}
	public void setMtSmSecMinObj(double mtSmSecMinObj) {
		this.mtSmSecMinObj = mtSmSecMinObj;
	}
	public double getMtSmSecMaxObj() {
		return mtSmSecMaxObj;
	}
	public void setMtSmSecMaxObj(double mtSmSecMaxObj) {
		this.mtSmSecMaxObj = mtSmSecMaxObj;
	}
	public double hour;
	public String node;
	public double moSuccSms;
	public double moTimeSms;
	public double commentID;
	public double moDecSms;
	public double moUnknownSms;
	public double moMiscErrSms;
	public double mtSuccSms;
	public double mtTimeOutSms;
	public double mtAbsSub;
	public double mtMemFull;
	public double mtMiscErrSms;
	public double moOtherMapErrCtr;
	public double moDiscNackCtr;
	public double moRejectedTprCtr;
	public double moSysFailCtr;
	public double moInvalidSmeAdd;
	public double moNotScSubs;
	public double moSmSecMinObj;
	public double moSmSecMaxObj;
	public double mtSystFailCtr;
	public double moOtherMapErr;
	public double mtUniSubsCtr;
	public double mtIllSubsctr;
	public double mtSubsBusyCtr;
	public double mtEqProtCtr;
	public double mtTcapAbortCtr;
	public double mtSccpAbortCtr;
	public double mtHomePalSusCtr;
	public double mtHomePlaTrust;
	public double mtHoScSuspCtr;
	public double mtHomScraTrustCtr;
	public double mtSmSecMinObj;
	public double mtSmSecMaxObj;
public SmsDeviceDetData()
{}
}
