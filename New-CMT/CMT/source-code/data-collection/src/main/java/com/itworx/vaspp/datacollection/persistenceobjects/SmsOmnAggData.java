package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmsOmnAggData extends PersistenceObject{
	public Date dateTime;
	public double smsId;
	public double getSmsId() {
		return smsId;
	}
	public void setSmsId(double smsId) {
		this.smsId = smsId;
	}
	public String srcOpId;
	public String dstOpId;
	public String srcEsmeId;
	public String dstEsmId;
	public String srcNetId;
	public String dstNetId;
	public String srcProtId;
	public String dstProtId;
	public String isTransactional;
	public String isRegist;
	public String isReceip;
	public String isFirstTimeDel;
	public String xi1;
	public String x12;
	public String xi3;
	public String x14;
	public String hour;
	public String min;
	public String sec;
	public String dateid;
	public String fwdErriErrVal;
	public String resultId;
	public String reqDuration;
	public String respDuration;
	public String msgDuration;
	public String delAttDuration;
	public String delvAttCounter;
	public String msgCount;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getSrcOpId() {
		return srcOpId;
	}
	public void setSrcOpId(String srcOpId) {
		this.srcOpId = srcOpId;
	}
	public String getDstOpId() {
		return dstOpId;
	}
	public void setDstOpId(String dstOpId) {
		this.dstOpId = dstOpId;
	}
	public String getSrcEsmeId() {
		return srcEsmeId;
	}
	public void setSrcEsmeId(String srcEsmeId) {
		this.srcEsmeId = srcEsmeId;
	}
	public String getDstEsmId() {
		return dstEsmId;
	}
	public void setDstEsmId(String dstEsmId) {
		this.dstEsmId = dstEsmId;
	}
	public String getSrcNetId() {
		return srcNetId;
	}
	public void setSrcNetId(String srcNetId) {
		this.srcNetId = srcNetId;
	}
	public String getDstNetId() {
		return dstNetId;
	}
	public void setDstNetId(String dstNetId) {
		this.dstNetId = dstNetId;
	}
	public String getSrcProtId() {
		return srcProtId;
	}
	public void setSrcProtId(String srcProtId) {
		this.srcProtId = srcProtId;
	}
	public String getDstProtId() {
		return dstProtId;
	}
	public void setDstProtId(String dstProtId) {
		this.dstProtId = dstProtId;
	}
	public String getIsTransactional() {
		return isTransactional;
	}
	public void setIsTransactional(String isTransactional) {
		this.isTransactional = isTransactional;
	}
	public String getIsRegist() {
		return isRegist;
	}
	public void setIsRegist(String isRegist) {
		this.isRegist = isRegist;
	}
	public String getIsReceip() {
		return isReceip;
	}
	public void setIsReceip(String isReceip) {
		this.isReceip = isReceip;
	}
	public String getIsFirstTimeDel() {
		return isFirstTimeDel;
	}
	public void setIsFirstTimeDel(String isFirstTimeDel) {
		this.isFirstTimeDel = isFirstTimeDel;
	}
	public String getXi1() {
		return xi1;
	}
	public void setXi1(String xi1) {
		this.xi1 = xi1;
	}
	public String getX12() {
		return x12;
	}
	public void setX12(String x12) {
		this.x12 = x12;
	}
	public String getXi3() {
		return xi3;
	}
	public void setXi3(String xi3) {
		this.xi3 = xi3;
	}
	public String getX14() {
		return x14;
	}
	public void setX14(String x14) {
		this.x14 = x14;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getSec() {
		return sec;
	}
	public void setSec(String sec) {
		this.sec = sec;
	}
	
	public String getDateid() {
		return dateid;
	}
	public void setDateid(String dateid) {
		this.dateid = dateid;
	}
	public String getFwdErriErrVal() {
		return fwdErriErrVal;
	}
	public void setFwdErriErrVal(String fwdErriErrVal) {
		this.fwdErriErrVal = fwdErriErrVal;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public String getReqDuration() {
		return reqDuration;
	}
	public void setReqDuration(String reqDuration) {
		this.reqDuration = reqDuration;
	}
	public String getRespDuration() {
		return respDuration;
	}
	public void setRespDuration(String respDuration) {
		this.respDuration = respDuration;
	}
	public String getMsgDuration() {
		return msgDuration;
	}
	public void setMsgDuration(String msgDuration) {
		this.msgDuration = msgDuration;
	}
	public String getDelAttDuration() {
		return delAttDuration;
	}
	public void setDelAttDuration(String delAttDuration) {
		this.delAttDuration = delAttDuration;
	}
	public String getDelvAttCounter() {
		return delvAttCounter;
	}
	public void setDelvAttCounter(String delvAttCounter) {
		this.delvAttCounter = delvAttCounter;
	}
	public String getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(String msgCount) {
		this.msgCount = msgCount;
	}
public SmsOmnAggData()
{}
}
