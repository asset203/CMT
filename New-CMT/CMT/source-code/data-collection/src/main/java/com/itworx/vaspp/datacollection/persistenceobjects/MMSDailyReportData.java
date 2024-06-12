package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class MMSDailyReportData extends PersistenceObject{
	public Date dateTime;
	public String opco;
	public double totalMmManaged;
	public double mmsRejectedNoCredit;
	public double otherRejected;
	public double avgSizeOfTotalMmsKb;
	public double peakMmsPerSec;
	public double busyHourTotTrficPerc;
	public double Mm1Mo;
	public double avgSizeMmsMM1MoKb;
	public double uniqueUsers;
	public double Mm1MoToMm1Mt;
	public double Mm1MoToMm4;
	public double Mm1MoToMm7At;
	public double Mm1MoToLagency;
	public double Mm1MoToEmail;
	public double Mm4Mo;
	public double avgSizaOfMmsMm4Kb;
	public double mmsSentByYospace;
	public double mm7Ao;
public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getOpco() {
		return opco;
	}
	public void setOpco(String opco) {
		this.opco = opco;
	}
	public double getTotalMmManaged() {
		return totalMmManaged;
	}
	public void setTotalMmManaged(double totalMmManaged) {
		this.totalMmManaged = totalMmManaged;
	}
	public double getMmsRejectedNoCredit() {
		return mmsRejectedNoCredit;
	}
	public void setMmsRejectedNoCredit(double mmsRejectedNoCredit) {
		this.mmsRejectedNoCredit = mmsRejectedNoCredit;
	}
	public double getOtherRejected() {
		return otherRejected;
	}
	public void setOtherRejected(double otherRejected) {
		this.otherRejected = otherRejected;
	}
	public double getAvgSizeOfTotalMmsKb() {
		return avgSizeOfTotalMmsKb;
	}
	public void setAvgSizeOfTotalMmsKb(double avgSizeOfTotalMmsKb) {
		this.avgSizeOfTotalMmsKb = avgSizeOfTotalMmsKb;
	}
	public double getPeakMmsPerSec() {
		return peakMmsPerSec;
	}
	public void setPeakMmsPerSec(double peakMmsPerSec) {
		this.peakMmsPerSec = peakMmsPerSec;
	}
	public double getBusyHourTotTrficPerc() {
		return busyHourTotTrficPerc;
	}
	public void setBusyHourTotTrficPerc(double busyHourTotTrficPerc) {
		this.busyHourTotTrficPerc = busyHourTotTrficPerc;
	}
	public double getMm1Mo() {
		return Mm1Mo;
	}
	public void setMm1Mo(double mm1Mo) {
		Mm1Mo = mm1Mo;
	}
	public double getAvgSizeMmsMM1MoKb() {
		return avgSizeMmsMM1MoKb;
	}
	public void setAvgSizeMmsMM1MoKb(double avgSizeMmsMM1MoKb) {
		this.avgSizeMmsMM1MoKb = avgSizeMmsMM1MoKb;
	}
	public double getUniqueUsers() {
		return uniqueUsers;
	}
	public void setUniqueUsers(double uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}
	public double getMm1MoToMm1Mt() {
		return Mm1MoToMm1Mt;
	}
	public void setMm1MoToMm1Mt(double mm1MoToMm1Mt) {
		Mm1MoToMm1Mt = mm1MoToMm1Mt;
	}
	public double getMm1MoToMm4() {
		return Mm1MoToMm4;
	}
	public void setMm1MoToMm4(double mm1MoToMm4) {
		Mm1MoToMm4 = mm1MoToMm4;
	}
	public double getMm1MoToMm7At() {
		return Mm1MoToMm7At;
	}
	public void setMm1MoToMm7At(double mm1MoToMm7At) {
		Mm1MoToMm7At = mm1MoToMm7At;
	}
	public double getMm1MoToLagency() {
		return Mm1MoToLagency;
	}
	public void setMm1MoToLagency(double mm1MoToLagency) {
		Mm1MoToLagency = mm1MoToLagency;
	}
	public double getMm1MoToEmail() {
		return Mm1MoToEmail;
	}
	public void setMm1MoToEmail(double mm1MoToEmail) {
		Mm1MoToEmail = mm1MoToEmail;
	}
	public double getMm4Mo() {
		return Mm4Mo;
	}
	public void setMm4Mo(double mm4Mo) {
		Mm4Mo = mm4Mo;
	}
	public double getAvgSizaOfMmsMm4Kb() {
		return avgSizaOfMmsMm4Kb;
	}
	public void setAvgSizaOfMmsMm4Kb(double avgSizaOfMmsMm4Kb) {
		this.avgSizaOfMmsMm4Kb = avgSizaOfMmsMm4Kb;
	}
	public double getMmsSentByYospace() {
		return mmsSentByYospace;
	}
	public void setMmsSentByYospace(double mmsSentByYospace) {
		this.mmsSentByYospace = mmsSentByYospace;
	}
	public double getMm7Ao() {
		return mm7Ao;
	}
	public void setMm7Ao(double mm7Ao) {
		this.mm7Ao = mm7Ao;
	}
public MMSDailyReportData()
{}
}
