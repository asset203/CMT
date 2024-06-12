package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class OMNTransactionsCounterData extends PersistenceObject{
	
	public Date dateTime;
	public double threadCount;
	public double incomingMO;
	public double successIncomingMO;
	public double failedIncomingMO;
	public double successIncomingMONotVirus;
	public double successMOVirus;	
	public double totalMO;
	public double successMO;
	public double failedMO;
	public double totalMT;
	public double successMT;
	public double failedMT;
	public double totalSRI;
	public double successSRI;
	public double failedSRI;
	public double state0;
	public double state1;
	public double state2;
	public double state3;
	public double state4;
	public double state5;
	public double state6;
	public double state7;
	public double state8;
	public double successMessages;
	public double ftdMessages;
	public double totalMTDeliveryAttempts;
	public double successMTDeliveryAttempts;	
	public double totalDeliveryAttempts;
	public double successMODTP;
	public double moDTPState0;
	public double moDTPState1;
	public double moDTPState2;
	public double moDTPState3;
	public double moDTPState4;
	public double moDTPState5;
	public double moDTPState6;
	public double moDTPState7;
	public double moDTPState8;
	public double moDTPFTDMessages;
	public double totalMODTPDeliveryAttempts;
	public double successMODTPDeliveryAttempts;
		
	
	public double getMoDTPState0() {
		return moDTPState0;
	}
	public void setMoDTPState0(double moDTPState0) {
		this.moDTPState0 = moDTPState0;
	}
	public double getMoDTPState1() {
		return moDTPState1;
	}
	public void setMoDTPState1(double moDTPState1) {
		this.moDTPState1 = moDTPState1;
	}
	public double getMoDTPState2() {
		return moDTPState2;
	}
	public void setMoDTPState2(double moDTPState2) {
		this.moDTPState2 = moDTPState2;
	}
	public double getMoDTPState3() {
		return moDTPState3;
	}
	public void setMoDTPState3(double moDTPState3) {
		this.moDTPState3 = moDTPState3;
	}
	public double getMoDTPState4() {
		return moDTPState4;
	}
	public void setMoDTPState4(double moDTPState4) {
		this.moDTPState4 = moDTPState4;
	}
	public double getMoDTPState5() {
		return moDTPState5;
	}
	public void setMoDTPState5(double moDTPState5) {
		this.moDTPState5 = moDTPState5;
	}
	public double getMoDTPState6() {
		return moDTPState6;
	}
	public void setMoDTPState6(double moDTPState6) {
		this.moDTPState6 = moDTPState6;
	}
	public double getMoDTPState7() {
		return moDTPState7;
	}
	public void setMoDTPState7(double moDTPState7) {
		this.moDTPState7 = moDTPState7;
	}
	public double getMoDTPState8() {
		return moDTPState8;
	}
	public void setMoDTPState8(double moDTPState8) {
		this.moDTPState8 = moDTPState8;
	}
	public double getSuccessMODTP() {
		return successMODTP;
	}
	public void setSuccessMODTP(double successMODTP) {
		this.successMODTP = successMODTP;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getFailedIncomingMO() {
		return failedIncomingMO;
	}
	public void setFailedIncomingMO(double failedIncomingMO) {
		this.failedIncomingMO = failedIncomingMO;
	}
	public double getFailedMO() {
		return failedMO;
	}
	public void setFailedMO(double failedMO) {
		this.failedMO = failedMO;
	}
	public double getFailedMT() {
		return failedMT;
	}
	public void setFailedMT(double failedMT) {
		this.failedMT = failedMT;
	}
	public double getFailedSRI() {
		return failedSRI;
	}
	public void setFailedSRI(double failedSRI) {
		this.failedSRI = failedSRI;
	}
	public double getFtdMessages() {
		return ftdMessages;
	}
	public void setFtdMessages(double ftdMessages) {
		this.ftdMessages = ftdMessages;
	}
	public double getIncomingMO() {
		return incomingMO;
	}
	public void setIncomingMO(double incomingMO) {
		this.incomingMO = incomingMO;
	}
	public double getState0() {
		return state0;
	}
	public void setState0(double state0) {
		this.state0 = state0;
	}
	public double getState1() {
		return state1;
	}
	public void setState1(double state1) {
		this.state1 = state1;
	}
	public double getState2() {
		return state2;
	}
	public void setState2(double state2) {
		this.state2 = state2;
	}
	public double getState3() {
		return state3;
	}
	public void setState3(double state3) {
		this.state3 = state3;
	}
	public double getState4() {
		return state4;
	}
	public void setState4(double state4) {
		this.state4 = state4;
	}
	public double getState5() {
		return state5;
	}
	public void setState5(double state5) {
		this.state5 = state5;
	}
	public double getState6() {
		return state6;
	}
	public void setState6(double state6) {
		this.state6 = state6;
	}
	public double getState7() {
		return state7;
	}
	public void setState7(double state7) {
		this.state7 = state7;
	}
	public double getState8() {
		return state8;
	}
	public void setState8(double state8) {
		this.state8 = state8;
	}
	public double getSuccessIncomingMO() {
		return successIncomingMO;
	}
	public void setSuccessIncomingMO(double successIncomingMO) {
		this.successIncomingMO = successIncomingMO;
	}
	public double getSuccessIncomingMONotVirus() {
		return successIncomingMONotVirus;
	}
	public void setSuccessIncomingMONotVirus(double successIncomingMONotVirus) {
		this.successIncomingMONotVirus = successIncomingMONotVirus;
	}
	public double getSuccessMessages() {
		return successMessages;
	}
	public void setSuccessMessages(double successMessages) {
		this.successMessages = successMessages;
	}
	public double getSuccessMO() {
		return successMO;
	}
	public void setSuccessMO(double successMO) {
		this.successMO = successMO;
	}
	public double getSuccessMOVirus() {
		return successMOVirus;
	}
	public void setSuccessMOVirus(double successMOVirus) {
		this.successMOVirus = successMOVirus;
	}
	public double getSuccessMT() {
		return successMT;
	}
	public void setSuccessMT(double successMT) {
		this.successMT = successMT;
	}
	public double getSuccessMTDeliveryAttempts() {
		return successMTDeliveryAttempts;
	}
	public void setSuccessMTDeliveryAttempts(double successMTDeliveryAttempts) {
		this.successMTDeliveryAttempts = successMTDeliveryAttempts;
	}
	public double getSuccessSRI() {
		return successSRI;
	}
	public void setSuccessSRI(double successSRI) {
		this.successSRI = successSRI;
	}
	public double getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(double threadCount) {
		this.threadCount = threadCount;
	}
	public double getTotalDeliveryAttempts() {
		return totalDeliveryAttempts;
	}
	public void setTotalDeliveryAttempts(double totalDeliveryAttempts) {
		this.totalDeliveryAttempts = totalDeliveryAttempts;
	}
	public double getTotalMO() {
		return totalMO;
	}
	public void setTotalMO(double totalMO) {
		this.totalMO = totalMO;
	}
	public double getTotalMT() {
		return totalMT;
	}
	public void setTotalMT(double totalMT) {
		this.totalMT = totalMT;
	}
	public double getTotalMTDeliveryAttempts() {
		return totalMTDeliveryAttempts;
	}
	public void setTotalMTDeliveryAttempts(double totalMTDeliveryAttempts) {
		this.totalMTDeliveryAttempts = totalMTDeliveryAttempts;
	}
	public double getTotalSRI() {
		return totalSRI;
	}
	public void setTotalSRI(double totalSRI) {
		this.totalSRI = totalSRI;
	}
	public double getMoDTPFTDMessages() {
		return moDTPFTDMessages;
	}
	public void setMoDTPFTDMessages(double moDTPFTDMessages) {
		this.moDTPFTDMessages = moDTPFTDMessages;
	}
	public double getSuccessMODTPDeliveryAttempts() {
		return successMODTPDeliveryAttempts;
	}
	public void setSuccessMODTPDeliveryAttempts(double successMODTPDeliveryAttempts) {
		this.successMODTPDeliveryAttempts = successMODTPDeliveryAttempts;
	}
	public double getTotalMODTPDeliveryAttempts() {
		return totalMODTPDeliveryAttempts;
	}
	public void setTotalMODTPDeliveryAttempts(double totalMODTPDeliveryAttempts) {
		this.totalMODTPDeliveryAttempts = totalMODTPDeliveryAttempts;
	}

	
}
