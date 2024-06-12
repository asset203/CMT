package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNCountersData extends PersistenceObject{

	public Date dateTime;
	
	public long totalOfGeneralCongestionOverload;	
	public long totalOfRequestRejectedOverload;
	public long totalOfPrimaryAccountFinderError ;
	public long totalOfSecondaryAccountFinderError ;
	public long totalOfRelayContentFirstInterrogationSuccessful;
	public long totalOfRelayContentFirstInterrogationCongestion ;
	public long totalOfRelayContentFirstInterrogationNoContactWithSDP ;
	public long totalOfRelayContentFirstInterrogationInternalError;
	public long totalOfRelayContentIntermediateInterrogationSuccessful;
	public long totalOfRelayContentIntermediateInterrogationNoContactWithSDP;
	public long totalOfRelayContentIntermediateInterrogationCongestion;
	public long totalOfRelayContentIntermediateInterrogationInternalError;
	public long totalOfRelayContentFinalReportSuccessful;
	public long totalOfRelayContentFinalReportNoContactWithSDP;
	public long totalOfRelayContentFinalReportCongestion;
	public long totalOfRelayContentFinalReportInternalError;
	public long totalOfRelayEventDirectDebitSuccessful;
	public long totalOfRelayEventDirectDebitCongestion;
	public long totalOfRelayEventDirectDebitNoContactWithSDP;
	public long totalOfRelayEventDirectDebitInternalError;
	public long totalOfRelayEventDirectCreditSuccessful;
	public long totalOfRelayEventDirectCreditCongestion;
	public long totalOfRelayEventDirectCreditNoContactWithSDP;
	public long totalOfRelayEventDirectCreditInternalError;
	public long totalOfDiamAccessAppReqRejectedDueToOverload;
	public long totalOfDiamAccessConnReqRejectedDueToOverload;
	public double topOfDiamAccessAppReqRejectedDueToOverload;
	public double topOfDiamAccessConnReqRejectedDueToOverload;
	public long totalAccountFinderCacheMiss;
	public long totalAccountFinderCacheHit;
	public long totalAccountFinderCacheError;
	
	
	
	public long getTotalAccountFinderCacheMiss() {
		return totalAccountFinderCacheMiss;
	}
	public void setTotalAccountFinderCacheMiss(long totalAccountFinderCacheMiss) {
		this.totalAccountFinderCacheMiss = totalAccountFinderCacheMiss;
	}
	public long getTotalAccountFinderCacheHit() {
		return totalAccountFinderCacheHit;
	}
	public void setTotalAccountFinderCacheHit(long totalAccountFinderCacheHit) {
		this.totalAccountFinderCacheHit = totalAccountFinderCacheHit;
	}
	public long getTotalAccountFinderCacheError() {
		return totalAccountFinderCacheError;
	}
	public void setTotalAccountFinderCacheError(long totalAccountFinderCacheError) {
		this.totalAccountFinderCacheError = totalAccountFinderCacheError;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getTotalOfGeneralCongestionOverload() {
		return totalOfGeneralCongestionOverload;
	}
	public void setTotalOfGeneralCongestionOverload(
			long totalOfGeneralCongestionOverload) {
		this.totalOfGeneralCongestionOverload = totalOfGeneralCongestionOverload;
	}
	public long getTotalOfPrimaryAccountFinderError() {
		return totalOfPrimaryAccountFinderError;
	}
	public void setTotalOfPrimaryAccountFinderError(
			long totalOfPrimaryAccountFinderError) {
		this.totalOfPrimaryAccountFinderError = totalOfPrimaryAccountFinderError;
	}
	public long getTotalOfRelayContentFinalReportCongestion() {
		return totalOfRelayContentFinalReportCongestion;
	}
	public void setTotalOfRelayContentFinalReportCongestion(
			long totalOfRelayContentFinalReportCongestion) {
		this.totalOfRelayContentFinalReportCongestion = totalOfRelayContentFinalReportCongestion;
	}
	public long getTotalOfRelayContentFinalReportInternalError() {
		return totalOfRelayContentFinalReportInternalError;
	}
	public void setTotalOfRelayContentFinalReportInternalError(
			long totalOfRelayContentFinalReportInternalError) {
		this.totalOfRelayContentFinalReportInternalError = totalOfRelayContentFinalReportInternalError;
	}
	public long getTotalOfRelayContentFinalReportNoContactWithSDP() {
		return totalOfRelayContentFinalReportNoContactWithSDP;
	}
	public void setTotalOfRelayContentFinalReportNoContactWithSDP(
			long totalOfRelayContentFinalReportNoContactWithSDP) {
		this.totalOfRelayContentFinalReportNoContactWithSDP = totalOfRelayContentFinalReportNoContactWithSDP;
	}
	public long getTotalOfRelayContentFinalReportSuccessful() {
		return totalOfRelayContentFinalReportSuccessful;
	}
	public void setTotalOfRelayContentFinalReportSuccessful(
			long totalOfRelayContentFinalReportSuccessful) {
		this.totalOfRelayContentFinalReportSuccessful = totalOfRelayContentFinalReportSuccessful;
	}
	public long getTotalOfRelayContentFirstInterrogationCongestion() {
		return totalOfRelayContentFirstInterrogationCongestion;
	}
	public void setTotalOfRelayContentFirstInterrogationCongestion(
			long totalOfRelayContentFirstInterrogationCongestion) {
		this.totalOfRelayContentFirstInterrogationCongestion = totalOfRelayContentFirstInterrogationCongestion;
	}
	public long getTotalOfRelayContentFirstInterrogationInternalError() {
		return totalOfRelayContentFirstInterrogationInternalError;
	}
	public void setTotalOfRelayContentFirstInterrogationInternalError(
			long totalOfRelayContentFirstInterrogationInternalError) {
		this.totalOfRelayContentFirstInterrogationInternalError = totalOfRelayContentFirstInterrogationInternalError;
	}
	public long getTotalOfRelayContentFirstInterrogationNoContactWithSDP() {
		return totalOfRelayContentFirstInterrogationNoContactWithSDP;
	}
	public void setTotalOfRelayContentFirstInterrogationNoContactWithSDP(
			long totalOfRelayContentFirstInterrogationNoContactWithSDP) {
		this.totalOfRelayContentFirstInterrogationNoContactWithSDP = totalOfRelayContentFirstInterrogationNoContactWithSDP;
	}
	public long getTotalOfRelayContentFirstInterrogationSuccessful() {
		return totalOfRelayContentFirstInterrogationSuccessful;
	}
	public void setTotalOfRelayContentFirstInterrogationSuccessful(
			long totalOfRelayContentFirstInterrogationSuccessful) {
		this.totalOfRelayContentFirstInterrogationSuccessful = totalOfRelayContentFirstInterrogationSuccessful;
	}
	public long getTotalOfRelayContentIntermediateInterrogationCongestion() {
		return totalOfRelayContentIntermediateInterrogationCongestion;
	}
	public void setTotalOfRelayContentIntermediateInterrogationCongestion(
			long totalOfRelayContentIntermediateInterrogationCongestion) {
		this.totalOfRelayContentIntermediateInterrogationCongestion = totalOfRelayContentIntermediateInterrogationCongestion;
	}
	public long getTotalOfRelayContentIntermediateInterrogationInternalError() {
		return totalOfRelayContentIntermediateInterrogationInternalError;
	}
	public void setTotalOfRelayContentIntermediateInterrogationInternalError(
			long totalOfRelayContentIntermediateInterrogationInternalError) {
		this.totalOfRelayContentIntermediateInterrogationInternalError = totalOfRelayContentIntermediateInterrogationInternalError;
	}
	public long getTotalOfRelayContentIntermediateInterrogationNoContactWithSDP() {
		return totalOfRelayContentIntermediateInterrogationNoContactWithSDP;
	}
	public void setTotalOfRelayContentIntermediateInterrogationNoContactWithSDP(
			long totalOfRelayContentIntermediateInterrogationNoContactWithSDP) {
		this.totalOfRelayContentIntermediateInterrogationNoContactWithSDP = totalOfRelayContentIntermediateInterrogationNoContactWithSDP;
	}
	public long getTotalOfRelayContentIntermediateInterrogationSuccessful() {
		return totalOfRelayContentIntermediateInterrogationSuccessful;
	}
	public void setTotalOfRelayContentIntermediateInterrogationSuccessful(
			long totalOfRelayContentIntermediateInterrogationSuccessful) {
		this.totalOfRelayContentIntermediateInterrogationSuccessful = totalOfRelayContentIntermediateInterrogationSuccessful;
	}
	public long getTotalOfRelayEventDirectCreditCongestion() {
		return totalOfRelayEventDirectCreditCongestion;
	}
	public void setTotalOfRelayEventDirectCreditCongestion(
			long totalOfRelayEventDirectCreditCongestion) {
		this.totalOfRelayEventDirectCreditCongestion = totalOfRelayEventDirectCreditCongestion;
	}
	public long getTotalOfRelayEventDirectCreditInternalError() {
		return totalOfRelayEventDirectCreditInternalError;
	}
	public void setTotalOfRelayEventDirectCreditInternalError(
			long totalOfRelayEventDirectCreditInternalError) {
		this.totalOfRelayEventDirectCreditInternalError = totalOfRelayEventDirectCreditInternalError;
	}
	public long getTotalOfRelayEventDirectCreditNoContactWithSDP() {
		return totalOfRelayEventDirectCreditNoContactWithSDP;
	}
	public void setTotalOfRelayEventDirectCreditNoContactWithSDP(
			long totalOfRelayEventDirectCreditNoContactWithSDP) {
		this.totalOfRelayEventDirectCreditNoContactWithSDP = totalOfRelayEventDirectCreditNoContactWithSDP;
	}
	public long getTotalOfRelayEventDirectCreditSuccessful() {
		return totalOfRelayEventDirectCreditSuccessful;
	}
	public void setTotalOfRelayEventDirectCreditSuccessful(
			long totalOfRelayEventDirectCreditSuccessful) {
		this.totalOfRelayEventDirectCreditSuccessful = totalOfRelayEventDirectCreditSuccessful;
	}
	public long getTotalOfRelayEventDirectDebitCongestion() {
		return totalOfRelayEventDirectDebitCongestion;
	}
	public void setTotalOfRelayEventDirectDebitCongestion(
			long totalOfRelayEventDirectDebitCongestion) {
		this.totalOfRelayEventDirectDebitCongestion = totalOfRelayEventDirectDebitCongestion;
	}
	public long getTotalOfRelayEventDirectDebitInternalError() {
		return totalOfRelayEventDirectDebitInternalError;
	}
	public void setTotalOfRelayEventDirectDebitInternalError(
			long totalOfRelayEventDirectDebitInternalError) {
		this.totalOfRelayEventDirectDebitInternalError = totalOfRelayEventDirectDebitInternalError;
	}
	public long getTotalOfRelayEventDirectDebitNoContactWithSDP() {
		return totalOfRelayEventDirectDebitNoContactWithSDP;
	}
	public void setTotalOfRelayEventDirectDebitNoContactWithSDP(
			long totalOfRelayEventDirectDebitNoContactWithSDP) {
		this.totalOfRelayEventDirectDebitNoContactWithSDP = totalOfRelayEventDirectDebitNoContactWithSDP;
	}
	public long getTotalOfRelayEventDirectDebitSuccessful() {
		return totalOfRelayEventDirectDebitSuccessful;
	}
	public void setTotalOfRelayEventDirectDebitSuccessful(
			long totalOfRelayEventDirectDebitSuccessful) {
		this.totalOfRelayEventDirectDebitSuccessful = totalOfRelayEventDirectDebitSuccessful;
	}
	public long getTotalOfRequestRejectedOverload() {
		return totalOfRequestRejectedOverload;
	}
	public void setTotalOfRequestRejectedOverload(
			long totalOfRequestRejectedOverload) {
		this.totalOfRequestRejectedOverload = totalOfRequestRejectedOverload;
	}
	public long getTotalOfSecondaryAccountFinderError() {
		return totalOfSecondaryAccountFinderError;
	}
	public void setTotalOfSecondaryAccountFinderError(
			long totalOfSecondaryAccountFinderError) {
		this.totalOfSecondaryAccountFinderError = totalOfSecondaryAccountFinderError;
	}
	public double getTopOfDiamAccessAppReqRejectedDueToOverload() {
		return topOfDiamAccessAppReqRejectedDueToOverload;
	}
	public void setTopOfDiamAccessAppReqRejectedDueToOverload(
			double topOfDiamAccessAppReqRejectedDueToOverload) {
		this.topOfDiamAccessAppReqRejectedDueToOverload = topOfDiamAccessAppReqRejectedDueToOverload;
	}
	public double getTopOfDiamAccessConnReqRejectedDueToOverload() {
		return topOfDiamAccessConnReqRejectedDueToOverload;
	}
	public void setTopOfDiamAccessConnReqRejectedDueToOverload(
			double topOfDiamAccessConnReqRejectedDueToOverload) {
		this.topOfDiamAccessConnReqRejectedDueToOverload = topOfDiamAccessConnReqRejectedDueToOverload;
	}
	public long getTotalOfDiamAccessAppReqRejectedDueToOverload() {
		return totalOfDiamAccessAppReqRejectedDueToOverload;
	}
	public void setTotalOfDiamAccessAppReqRejectedDueToOverload(
			long totalOfDiamAccessAppReqRejectedDueToOverload) {
		this.totalOfDiamAccessAppReqRejectedDueToOverload = totalOfDiamAccessAppReqRejectedDueToOverload;
	}
	public long getTotalOfDiamAccessConnReqRejectedDueToOverload() {
		return totalOfDiamAccessConnReqRejectedDueToOverload;
	}
	public void setTotalOfDiamAccessConnReqRejectedDueToOverload(
			long totalOfDiamAccessConnReqRejectedDueToOverload) {
		this.totalOfDiamAccessConnReqRejectedDueToOverload = totalOfDiamAccessConnReqRejectedDueToOverload;
	}
	
	
	
	
}
