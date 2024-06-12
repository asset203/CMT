package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CCNCountersGenericData extends PersistenceObject{
	public Date dateTime; 
	public double apnConversionFailed;
	public double generalCongestionDataBase;
	public double generalCongestionDialogueOverload;
	public double requestRejectedOverload;
	public double primaryAccountFinderError;
	public double secondaryAccountFinderError;
	public double sdpLookupFailedSubscriberNotFound;
	public double accessFailureDatabase;
	
	public double capReceivedTotalInvalidRequests;
	
	public double cap2ReceivedTotalRequests;
	public double cap2ReceivedComponentRejects;
	public double cap2ReceivedErrorIndications;
	public double cap2SentTotalRequests;
	public double cap2SentComponentRejects;
	public double cap2SentErrorIndications;
	public double cap2ReceivedReturnedComponents;
	
	public double cdrOutputEncodedSuccessful;
	public double cdrOutputEncodedFailed;
	public double cdrOutputDataSentToPrimaryDisc;
	public double cdrOutputDataSentToSecondaryDisc;
	public double cdrOutputDataDiscarded;
	public double cdrOutputDataStoredOnDisc0;
	public double cdrOutputDataStoredOnDisc1;
	public double cdrOutputFilesGeneratedOnDisc0;
	public double cdrOutputFilesGeneratedOnDisc1;
	public double cdrOutputFilesSentToFTPDestination;
	public double cdrOutputFilesSentToFTPDestinationFailed;
	
	
	public double inapReceivedComponentRejects;
	public double inapReceivedErrorIndications;
	public double inapReceivedReturnedComponents;
	public double inapReceivedTotalInvalidRequests;
	public double inapReceivedTotalRequests;
	public double inapSentComponentRejects;
	public double inapSentErrorIndications;
	public double inapSentTotalRequests;

	
	public double relaySmsFirstInterrogationSuccessful;
	public double relaySmsFirstInterrogationCongestionRejected;
	public double relaySmsFirstInterrogationCongestionAllowed;
	public double relaySmsFirstInterrogationNoContactWithSDPRejected;
	public double relaySmsFirstInterrogationNoContactWithSDPAllowed;
	public double relaySmsFirstInterrogationInternalErrorRejected;
	public double relaySmsFinalReportSuccessful;
	public double relaySmsFinalReportNoContactWithSDP;
	public double relaySmsFinalReportInternalError;
	public double relaySmsFinalReportCongestion;
	
	public double getAccessFailureDatabase() {
		return accessFailureDatabase;
	}
	public void setAccessFailureDatabase(double accessFailureDatabase) {
		this.accessFailureDatabase = accessFailureDatabase;
	}
	public double getApnConversionFailed() {
		return apnConversionFailed;
	}
	public void setApnConversionFailed(double apnConversionFailed) {
		this.apnConversionFailed = apnConversionFailed;
	}
	public double getCap2ReceivedComponentRejects() {
		return cap2ReceivedComponentRejects;
	}
	public void setCap2ReceivedComponentRejects(double cap2ReceivedComponentRejects) {
		this.cap2ReceivedComponentRejects = cap2ReceivedComponentRejects;
	}
	public double getCap2ReceivedErrorIndications() {
		return cap2ReceivedErrorIndications;
	}
	public void setCap2ReceivedErrorIndications(double cap2ReceivedErrorIndications) {
		this.cap2ReceivedErrorIndications = cap2ReceivedErrorIndications;
	}
	public double getCap2ReceivedReturnedComponents() {
		return cap2ReceivedReturnedComponents;
	}
	public void setCap2ReceivedReturnedComponents(
			double cap2ReceivedReturnedComponents) {
		this.cap2ReceivedReturnedComponents = cap2ReceivedReturnedComponents;
	}
	public double getCap2ReceivedTotalRequests() {
		return cap2ReceivedTotalRequests;
	}
	public void setCap2ReceivedTotalRequests(double cap2ReceivedTotalRequests) {
		this.cap2ReceivedTotalRequests = cap2ReceivedTotalRequests;
	}
	public double getCap2SentComponentRejects() {
		return cap2SentComponentRejects;
	}
	public void setCap2SentComponentRejects(double cap2SentComponentRejects) {
		this.cap2SentComponentRejects = cap2SentComponentRejects;
	}
	public double getCap2SentErrorIndications() {
		return cap2SentErrorIndications;
	}
	public void setCap2SentErrorIndications(double cap2SentErrorIndications) {
		this.cap2SentErrorIndications = cap2SentErrorIndications;
	}
	public double getCap2SentTotalRequests() {
		return cap2SentTotalRequests;
	}
	public void setCap2SentTotalRequests(double cap2SentTotalRequests) {
		this.cap2SentTotalRequests = cap2SentTotalRequests;
	}
	public double getCapReceivedTotalInvalidRequests() {
		return capReceivedTotalInvalidRequests;
	}
	public void setCapReceivedTotalInvalidRequests(
			double capReceivedTotalInvalidRequests) {
		this.capReceivedTotalInvalidRequests = capReceivedTotalInvalidRequests;
	}
	public double getCdrOutputDataDiscarded() {
		return cdrOutputDataDiscarded;
	}
	public void setCdrOutputDataDiscarded(double cdrOutputDataDiscarded) {
		this.cdrOutputDataDiscarded = cdrOutputDataDiscarded;
	}
	public double getCdrOutputDataSentToPrimaryDisc() {
		return cdrOutputDataSentToPrimaryDisc;
	}
	public void setCdrOutputDataSentToPrimaryDisc(
			double cdrOutputDataSentToPrimaryDisc) {
		this.cdrOutputDataSentToPrimaryDisc = cdrOutputDataSentToPrimaryDisc;
	}
	public double getCdrOutputDataSentToSecondaryDisc() {
		return cdrOutputDataSentToSecondaryDisc;
	}
	public void setCdrOutputDataSentToSecondaryDisc(
			double cdrOutputDataSentToSecondaryDisc) {
		this.cdrOutputDataSentToSecondaryDisc = cdrOutputDataSentToSecondaryDisc;
	}
	public double getCdrOutputDataStoredOnDisc0() {
		return cdrOutputDataStoredOnDisc0;
	}
	public void setCdrOutputDataStoredOnDisc0(double cdrOutputDataStoredOnDisc0) {
		this.cdrOutputDataStoredOnDisc0 = cdrOutputDataStoredOnDisc0;
	}
	public double getCdrOutputDataStoredOnDisc1() {
		return cdrOutputDataStoredOnDisc1;
	}
	public void setCdrOutputDataStoredOnDisc1(double cdrOutputDataStoredOnDisc1) {
		this.cdrOutputDataStoredOnDisc1 = cdrOutputDataStoredOnDisc1;
	}
	public double getCdrOutputEncodedFailed() {
		return cdrOutputEncodedFailed;
	}
	public void setCdrOutputEncodedFailed(double cdrOutputEncodedFailed) {
		this.cdrOutputEncodedFailed = cdrOutputEncodedFailed;
	}
	public double getCdrOutputEncodedSuccessful() {
		return cdrOutputEncodedSuccessful;
	}
	public void setCdrOutputEncodedSuccessful(double cdrOutputEncodedSuccessful) {
		this.cdrOutputEncodedSuccessful = cdrOutputEncodedSuccessful;
	}
	public double getCdrOutputFilesGeneratedOnDisc0() {
		return cdrOutputFilesGeneratedOnDisc0;
	}
	public void setCdrOutputFilesGeneratedOnDisc0(
			double cdrOutputFilesGeneratedOnDisc0) {
		this.cdrOutputFilesGeneratedOnDisc0 = cdrOutputFilesGeneratedOnDisc0;
	}
	public double getCdrOutputFilesGeneratedOnDisc1() {
		return cdrOutputFilesGeneratedOnDisc1;
	}
	public void setCdrOutputFilesGeneratedOnDisc1(
			double cdrOutputFilesGeneratedOnDisc1) {
		this.cdrOutputFilesGeneratedOnDisc1 = cdrOutputFilesGeneratedOnDisc1;
	}
	public double getCdrOutputFilesSentToFTPDestination() {
		return cdrOutputFilesSentToFTPDestination;
	}
	public void setCdrOutputFilesSentToFTPDestination(
			double cdrOutputFilesSentToFTPDestination) {
		this.cdrOutputFilesSentToFTPDestination = cdrOutputFilesSentToFTPDestination;
	}
	public double getCdrOutputFilesSentToFTPDestinationFailed() {
		return cdrOutputFilesSentToFTPDestinationFailed;
	}
	public void setCdrOutputFilesSentToFTPDestinationFailed(
			double cdrOutputFilesSentToFTPDestinationFailed) {
		this.cdrOutputFilesSentToFTPDestinationFailed = cdrOutputFilesSentToFTPDestinationFailed;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getGeneralCongestionDataBase() {
		return generalCongestionDataBase;
	}
	public void setGeneralCongestionDataBase(double generalCongestionDataBase) {
		this.generalCongestionDataBase = generalCongestionDataBase;
	}
	public double getGeneralCongestionDialogueOverload() {
		return generalCongestionDialogueOverload;
	}
	public void setGeneralCongestionDialogueOverload(
			double generalCongestionDialogueOverload) {
		this.generalCongestionDialogueOverload = generalCongestionDialogueOverload;
	}
	public double getInapReceivedComponentRejects() {
		return inapReceivedComponentRejects;
	}
	public void setInapReceivedComponentRejects(double inapReceivedComponentRejects) {
		this.inapReceivedComponentRejects = inapReceivedComponentRejects;
	}
	public double getInapReceivedErrorIndications() {
		return inapReceivedErrorIndications;
	}
	public void setInapReceivedErrorIndications(double inapReceivedErrorIndications) {
		this.inapReceivedErrorIndications = inapReceivedErrorIndications;
	}
	public double getInapReceivedReturnedComponents() {
		return inapReceivedReturnedComponents;
	}
	public void setInapReceivedReturnedComponents(
			double inapReceivedReturnedComponents) {
		this.inapReceivedReturnedComponents = inapReceivedReturnedComponents;
	}
	public double getInapReceivedTotalInvalidRequests() {
		return inapReceivedTotalInvalidRequests;
	}
	public void setInapReceivedTotalInvalidRequests(
			double inapReceivedTotalInvalidRequests) {
		this.inapReceivedTotalInvalidRequests = inapReceivedTotalInvalidRequests;
	}
	public double getInapReceivedTotalRequests() {
		return inapReceivedTotalRequests;
	}
	public void setInapReceivedTotalRequests(double inapReceivedTotalRequests) {
		this.inapReceivedTotalRequests = inapReceivedTotalRequests;
	}
	public double getInapSentComponentRejects() {
		return inapSentComponentRejects;
	}
	public void setInapSentComponentRejects(double inapSentComponentRejects) {
		this.inapSentComponentRejects = inapSentComponentRejects;
	}
	public double getInapSentErrorIndications() {
		return inapSentErrorIndications;
	}
	public void setInapSentErrorIndications(double inapSentErrorIndications) {
		this.inapSentErrorIndications = inapSentErrorIndications;
	}
	public double getInapSentTotalRequests() {
		return inapSentTotalRequests;
	}
	public void setInapSentTotalRequests(double inapSentTotalRequests) {
		this.inapSentTotalRequests = inapSentTotalRequests;
	}
	public double getPrimaryAccountFinderError() {
		return primaryAccountFinderError;
	}
	public void setPrimaryAccountFinderError(double primaryAccountFinderError) {
		this.primaryAccountFinderError = primaryAccountFinderError;
	}
	public double getRelaySmsFinalReportCongestion() {
		return relaySmsFinalReportCongestion;
	}
	public void setRelaySmsFinalReportCongestion(
			double relaySmsFinalReportCongestion) {
		this.relaySmsFinalReportCongestion = relaySmsFinalReportCongestion;
	}
	public double getRelaySmsFinalReportInternalError() {
		return relaySmsFinalReportInternalError;
	}
	public void setRelaySmsFinalReportInternalError(
			double relaySmsFinalReportInternalError) {
		this.relaySmsFinalReportInternalError = relaySmsFinalReportInternalError;
	}
	public double getRelaySmsFinalReportNoContactWithSDP() {
		return relaySmsFinalReportNoContactWithSDP;
	}
	public void setRelaySmsFinalReportNoContactWithSDP(
			double relaySmsFinalReportNoContactWithSDP) {
		this.relaySmsFinalReportNoContactWithSDP = relaySmsFinalReportNoContactWithSDP;
	}
	public double getRelaySmsFinalReportSuccessful() {
		return relaySmsFinalReportSuccessful;
	}
	public void setRelaySmsFinalReportSuccessful(
			double relaySmsFinalReportSuccessful) {
		this.relaySmsFinalReportSuccessful = relaySmsFinalReportSuccessful;
	}
	public double getRelaySmsFirstInterrogationCongestionAllowed() {
		return relaySmsFirstInterrogationCongestionAllowed;
	}
	public void setRelaySmsFirstInterrogationCongestionAllowed(
			double relaySmsFirstInterrogationCongestionAllowed) {
		this.relaySmsFirstInterrogationCongestionAllowed = relaySmsFirstInterrogationCongestionAllowed;
	}
	public double getRelaySmsFirstInterrogationCongestionRejected() {
		return relaySmsFirstInterrogationCongestionRejected;
	}
	public void setRelaySmsFirstInterrogationCongestionRejected(
			double relaySmsFirstInterrogationCongestionRejected) {
		this.relaySmsFirstInterrogationCongestionRejected = relaySmsFirstInterrogationCongestionRejected;
	}
	public double getRelaySmsFirstInterrogationInternalErrorRejected() {
		return relaySmsFirstInterrogationInternalErrorRejected;
	}
	public void setRelaySmsFirstInterrogationInternalErrorRejected(
			double relaySmsFirstInterrogationInternalErrorRejected) {
		this.relaySmsFirstInterrogationInternalErrorRejected = relaySmsFirstInterrogationInternalErrorRejected;
	}
	public double getRelaySmsFirstInterrogationNoContactWithSDPAllowed() {
		return relaySmsFirstInterrogationNoContactWithSDPAllowed;
	}
	public void setRelaySmsFirstInterrogationNoContactWithSDPAllowed(
			double relaySmsFirstInterrogationNoContactWithSDPAllowed) {
		this.relaySmsFirstInterrogationNoContactWithSDPAllowed = relaySmsFirstInterrogationNoContactWithSDPAllowed;
	}
	public double getRelaySmsFirstInterrogationNoContactWithSDPRejected() {
		return relaySmsFirstInterrogationNoContactWithSDPRejected;
	}
	public void setRelaySmsFirstInterrogationNoContactWithSDPRejected(
			double relaySmsFirstInterrogationNoContactWithSDPRejected) {
		this.relaySmsFirstInterrogationNoContactWithSDPRejected = relaySmsFirstInterrogationNoContactWithSDPRejected;
	}
	public double getRelaySmsFirstInterrogationSuccessful() {
		return relaySmsFirstInterrogationSuccessful;
	}
	public void setRelaySmsFirstInterrogationSuccessful(
			double relaySmsFirstInterrogationSuccessful) {
		this.relaySmsFirstInterrogationSuccessful = relaySmsFirstInterrogationSuccessful;
	}
	public double getRequestRejectedOverload() {
		return requestRejectedOverload;
	}
	public void setRequestRejectedOverload(double requestRejectedOverload) {
		this.requestRejectedOverload = requestRejectedOverload;
	}
	public double getSdpLookupFailedSubscriberNotFound() {
		return sdpLookupFailedSubscriberNotFound;
	}
	public void setSdpLookupFailedSubscriberNotFound(
			double sdpLookupFailedSubscriberNotFound) {
		this.sdpLookupFailedSubscriberNotFound = sdpLookupFailedSubscriberNotFound;
	}
	public double getSecondaryAccountFinderError() {
		return secondaryAccountFinderError;
	}
	public void setSecondaryAccountFinderError(double secondaryAccountFinderError) {
		this.secondaryAccountFinderError = secondaryAccountFinderError;
	}
	
}
