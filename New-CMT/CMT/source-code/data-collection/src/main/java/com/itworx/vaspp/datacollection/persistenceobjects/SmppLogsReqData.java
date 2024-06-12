package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SmppLogsReqData extends PersistenceObject{
	public Date dateTime;
	public double totalReq;
	public double succReq;
	public double FailedReq;
	public double succFromFirstRet;
	public double succFromSecondRet;
	public double succFromThirdRet;
	public double succFromFourthRet;
	public double succFromFifthRet;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTotalReq() {
		return totalReq;
	}
	public void setTotalReq(double totalReq) {
		this.totalReq = totalReq;
	}
	public double getSuccReq() {
		return succReq;
	}
	public void setSuccReq(double succReq) {
		this.succReq = succReq;
	}
	public double getFailedReq() {
		return FailedReq;
	}
	public void setFailedReq(double failedReq) {
		FailedReq = failedReq;
	}
	public double getSuccFromFirstRet() {
		return succFromFirstRet;
	}
	public void setSuccFromFirstRet(double succFromFirstRet) {
		this.succFromFirstRet = succFromFirstRet;
	}
	public double getSuccFromSecondRet() {
		return succFromSecondRet;
	}
	public void setSuccFromSecondRet(double succFromSecondRet) {
		this.succFromSecondRet = succFromSecondRet;
	}
	public double getSuccFromThirdRet() {
		return succFromThirdRet;
	}
	public void setSuccFromThirdRet(double succFromThirdRet) {
		this.succFromThirdRet = succFromThirdRet;
	}
	public double getSuccFromFourthRet() {
		return succFromFourthRet;
	}
	public void setSuccFromFourthRet(double succFromFourthRet) {
		this.succFromFourthRet = succFromFourthRet;
	}
	public double getSuccFromFifthRet() {
		return succFromFifthRet;
	}
	public void setSuccFromFifthRet(double succFromFifthRet) {
		this.succFromFifthRet = succFromFifthRet;
	}
public SmppLogsReqData()
{
}
}
