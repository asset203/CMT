package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class VPNSMSCallsData extends PersistenceObject{
	
	public Date dateTime;
	public double oick;
	public double moSMSIST;
	public double res;
	public String ne;
	public double cFail;
	public double timeIni;
	public double TimeSub;
	public double OpsIni;
	public double OpsIab;
	public double OpsiRej;
	public double OpsSub;
	public double OpsAab;
	public double OpsARej;
	public double OprECal;
	public double OPRECF;
	public double ISTsEL;
	public double ISTDERR;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getOick() {
		return oick;
	}
	public void setOick(double oick) {
		this.oick = oick;
	}
	public double getMoSMSIST() {
		return moSMSIST;
	}
	public void setMoSMSIST(double moSMSIST) {
		this.moSMSIST = moSMSIST;
	}
	public double getRes() {
		return res;
	}
	public void setRes(double res) {
		this.res = res;
	}
	public String getNe() {
		return ne;
	}
	public void setNe(String ne) {
		this.ne = ne;
	}
	public double getcFail() {
		return cFail;
	}
	public void setcFail(double cFail) {
		this.cFail = cFail;
	}
	public double getTimeIni() {
		return timeIni;
	}
	public void setTimeIni(double timeIni) {
		this.timeIni = timeIni;
	}
	public double getTimeSub() {
		return TimeSub;
	}
	public void setTimeSub(double timeSub) {
		TimeSub = timeSub;
	}
	public double getOpsIni() {
		return OpsIni;
	}
	public void setOpsIni(double opsIni) {
		OpsIni = opsIni;
	}
	public double getOpsIab() {
		return OpsIab;
	}
	public void setOpsIab(double opsIab) {
		OpsIab = opsIab;
	}
	public double getOpsiRej() {
		return OpsiRej;
	}
	public void setOpsiRej(double opsiRej) {
		OpsiRej = opsiRej;
	}
	public double getOpsSub() {
		return OpsSub;
	}
	public void setOpsSub(double opsSub) {
		OpsSub = opsSub;
	}
	public double getOpsAab() {
		return OpsAab;
	}
	public void setOpsAab(double opsAab) {
		OpsAab = opsAab;
	}
	public double getOpsARej() {
		return OpsARej;
	}
	public void setOpsARej(double opsARej) {
		OpsARej = opsARej;
	}
	public double getOprECal() {
		return OprECal;
	}
	public void setOprECal(double oprECal) {
		OprECal = oprECal;
	}
	public double getOPRECF() {
		return OPRECF;
	}
	public void setOPRECF(double oPRECF) {
		OPRECF = oPRECF;
	}
	public double getISTsEL() {
		return ISTsEL;
	}
	public void setISTsEL(double iSTsEL) {
		ISTsEL = iSTsEL;
	}
	public double getISTDERR() {
		return ISTDERR;
	}
	public void setISTDERR(double iSTDERR) {
		ISTDERR = iSTDERR;
	}
	
	

}
