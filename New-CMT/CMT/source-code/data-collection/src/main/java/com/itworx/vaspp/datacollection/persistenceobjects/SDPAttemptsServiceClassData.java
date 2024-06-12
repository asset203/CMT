package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SDPAttemptsServiceClassData extends PersistenceObject{
	
	public Date dateTime;
	public double serviceClassId;
	public double originatingAttempts;
	public double terminatingAttempts;
	public double roamOrigAttem;
	public double roamTermAttem;
	public double OrigiServChargAttemp;
	public double termiServCharAttem;
	public double origUnsuccNTermEXT;
	public double termUnsuccNTermEXT;
	public double roamOrigUnsuccNTermEXT;
	public double roamTermUnsuccessNTermEXT;
	public double origChargingTermEXT;
	public double termChargingTermEXT;
	public double origNTermINT;
	public double termNTermINT;
	public double roamNTermINT;
	public double roamTUNTINT;
	public double origServCharNTINT;
	public double termServCharNTINT;
	public double origCongestionEXT;
	public double origunsuccCongestionEXT;
	public double roamorigCongestionEXT;
	public double roamTermCongestionEXT;
	public double origServCongestionEXT;
	public double termServCongestionEXT;
	public double origOtherReasonEXT;
	public double termOtherReasonEXT;
	public double roamOtherReasonEXT;
	public double roamTermOtherReasonEXT;
	public double origServOtherReasonEXT ;
	public double terServOtherReasonEXT;
	public double origTermINT;
	public double termTerminaINT;
	public double roamTerminationINT;
	public double roamTermTerminationINT;
	public double origSysTerminationINT;
	public double termSysTerminationINT;
	public double originatingVoiceFaxData;
	public double terminatingVoiceFaxData;
	public double originatingSMS;
	public double terminatingSMS;
	public double originatingGPRS;
	public double terminatingGPRS;
	public double originatingContent;
	public double terminatingContent;
	public double originatingVideo;
	public double terminatingVideo;
	public double originatingUnknown;
	public double terminatingUnknown;
	public double originatingSCAPv2;
	public double terminatingSCAPv2;
	public double originatingGy;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getServiceClassId() {
		return serviceClassId;
	}
	public void setServiceClassId(double serviceClassId) {
		this.serviceClassId = serviceClassId;
	}
	public double getOriginatingAttempts() {
		return originatingAttempts;
	}
	public void setOriginatingAttempts(double originatingAttempts) {
		this.originatingAttempts = originatingAttempts;
	}
	public double getTerminatingAttempts() {
		return terminatingAttempts;
	}
	public void setTerminatingAttempts(double terminatingAttempts) {
		this.terminatingAttempts = terminatingAttempts;
	}
	public double getRoamOrigAttem() {
		return roamOrigAttem;
	}
	public void setRoamOrigAttem(double roamOrigAttem) {
		this.roamOrigAttem = roamOrigAttem;
	}
	public double getRoamTermAttem() {
		return roamTermAttem;
	}
	public void setRoamTermAttem(double roamTermAttem) {
		this.roamTermAttem = roamTermAttem;
	}
	public double getOrigiServChargAttemp() {
		return OrigiServChargAttemp;
	}
	public void setOrigiServChargAttemp(double origiServChargAttemp) {
		OrigiServChargAttemp = origiServChargAttemp;
	}
	public double getTermiServCharAttem() {
		return termiServCharAttem;
	}
	public void setTermiServCharAttem(double termiServCharAttem) {
		this.termiServCharAttem = termiServCharAttem;
	}
	public double getOrigUnsuccNTermEXT() {
		return origUnsuccNTermEXT;
	}
	public void setOrigUnsuccNTermEXT(double origUnsuccNTermEXT) {
		this.origUnsuccNTermEXT = origUnsuccNTermEXT;
	}
	public double getTermUnsuccNTermEXT() {
		return termUnsuccNTermEXT;
	}
	public void setTermUnsuccNTermEXT(double termUnsuccNTermEXT) {
		this.termUnsuccNTermEXT = termUnsuccNTermEXT;
	}
	public double getRoamOrigUnsuccNTermEXT() {
		return roamOrigUnsuccNTermEXT;
	}
	public void setRoamOrigUnsuccNTermEXT(double roamOrigUnsuccNTermEXT) {
		this.roamOrigUnsuccNTermEXT = roamOrigUnsuccNTermEXT;
	}
	public double getRoamTermUnsuccessNTermEXT() {
		return roamTermUnsuccessNTermEXT;
	}
	public void setRoamTermUnsuccessNTermEXT(double roamTermUnsuccessNTermEXT) {
		this.roamTermUnsuccessNTermEXT = roamTermUnsuccessNTermEXT;
	}
	public double getOrigChargingTermEXT() {
		return origChargingTermEXT;
	}
	public void setOrigChargingTermEXT(double origChargingTermEXT) {
		this.origChargingTermEXT = origChargingTermEXT;
	}
	public double getTermChargingTermEXT() {
		return termChargingTermEXT;
	}
	public void setTermChargingTermEXT(double termChargingTermEXT) {
		this.termChargingTermEXT = termChargingTermEXT;
	}
	public double getOrigNTermINT() {
		return origNTermINT;
	}
	public void setOrigNTermINT(double origNTermINT) {
		this.origNTermINT = origNTermINT;
	}
	public double getTermNTermINT() {
		return termNTermINT;
	}
	public void setTermNTermINT(double termNTermINT) {
		this.termNTermINT = termNTermINT;
	}
	public double getRoamNTermINT() {
		return roamNTermINT;
	}
	public void setRoamNTermINT(double roamNTermINT) {
		this.roamNTermINT = roamNTermINT;
	}
	public double getRoamTUNTINT() {
		return roamTUNTINT;
	}
	public void setRoamTUNTINT(double roamTUNTINT) {
		this.roamTUNTINT = roamTUNTINT;
	}
	public double getOrigServCharNTINT() {
		return origServCharNTINT;
	}
	public void setOrigServCharNTINT(double origServCharNTINT) {
		this.origServCharNTINT = origServCharNTINT;
	}
	public double getTermServCharNTINT() {
		return termServCharNTINT;
	}
	public void setTermServCharNTINT(double termServCharNTINT) {
		this.termServCharNTINT = termServCharNTINT;
	}
	public double getOrigCongestionEXT() {
		return origCongestionEXT;
	}
	public void setOrigCongestionEXT(double origCongestionEXT) {
		this.origCongestionEXT = origCongestionEXT;
	}
	public double getOrigunsuccCongestionEXT() {
		return origunsuccCongestionEXT;
	}
	public void setOrigunsuccCongestionEXT(double origunsuccCongestionEXT) {
		this.origunsuccCongestionEXT = origunsuccCongestionEXT;
	}
	public double getRoamorigCongestionEXT() {
		return roamorigCongestionEXT;
	}
	public void setRoamorigCongestionEXT(double roamorigCongestionEXT) {
		this.roamorigCongestionEXT = roamorigCongestionEXT;
	}
	public double getRoamTermCongestionEXT() {
		return roamTermCongestionEXT;
	}
	public void setRoamTermCongestionEXT(double roamTermCongestionEXT) {
		this.roamTermCongestionEXT = roamTermCongestionEXT;
	}
	public double getOrigServCongestionEXT() {
		return origServCongestionEXT;
	}
	public void setOrigServCongestionEXT(double origServCongestionEXT) {
		this.origServCongestionEXT = origServCongestionEXT;
	}
	public double getTermServCongestionEXT() {
		return termServCongestionEXT;
	}
	public void setTermServCongestionEXT(double termServCongestionEXT) {
		this.termServCongestionEXT = termServCongestionEXT;
	}
	public double getOrigOtherReasonEXT() {
		return origOtherReasonEXT;
	}
	public void setOrigOtherReasonEXT(double origOtherReasonEXT) {
		this.origOtherReasonEXT = origOtherReasonEXT;
	}
	public double getTermOtherReasonEXT() {
		return termOtherReasonEXT;
	}
	public void setTermOtherReasonEXT(double termOtherReasonEXT) {
		this.termOtherReasonEXT = termOtherReasonEXT;
	}
	public double getRoamOtherReasonEXT() {
		return roamOtherReasonEXT;
	}
	public void setRoamOtherReasonEXT(double roamOtherReasonEXT) {
		this.roamOtherReasonEXT = roamOtherReasonEXT;
	}
	public double getRoamTermOtherReasonEXT() {
		return roamTermOtherReasonEXT;
	}
	public void setRoamTermOtherReasonEXT(double roamTermOtherReasonEXT) {
		this.roamTermOtherReasonEXT = roamTermOtherReasonEXT;
	}
	public double getOrigServOtherReasonEXT() {
		return origServOtherReasonEXT;
	}
	public void setOrigServOtherReasonEXT(double origServOtherReasonEXT) {
		this.origServOtherReasonEXT = origServOtherReasonEXT;
	}
	public double getTerServOtherReasonEXT() {
		return terServOtherReasonEXT;
	}
	public void setTerServOtherReasonEXT(double terServOtherReasonEXT) {
		this.terServOtherReasonEXT = terServOtherReasonEXT;
	}
	public double getOrigTermINT() {
		return origTermINT;
	}
	public void setOrigTermINT(double origTermINT) {
		this.origTermINT = origTermINT;
	}
	public double getTermTerminaINT() {
		return termTerminaINT;
	}
	public void setTermTerminaINT(double termTerminaINT) {
		this.termTerminaINT = termTerminaINT;
	}
	public double getRoamTerminationINT() {
		return roamTerminationINT;
	}
	public void setRoamTerminationINT(double roamTerminationINT) {
		this.roamTerminationINT = roamTerminationINT;
	}
	public double getRoamTermTerminationINT() {
		return roamTermTerminationINT;
	}
	public void setRoamTermTerminationINT(double roamTermTerminationINT) {
		this.roamTermTerminationINT = roamTermTerminationINT;
	}
	public double getOrigSysTerminationINT() {
		return origSysTerminationINT;
	}
	public void setOrigSysTerminationINT(double origSysTerminationINT) {
		this.origSysTerminationINT = origSysTerminationINT;
	}
	public double getTermSysTerminationINT() {
		return termSysTerminationINT;
	}
	public void setTermSysTerminationINT(double termSysTerminationINT) {
		this.termSysTerminationINT = termSysTerminationINT;
	}
	public double getOriginatingVoiceFaxData() {
		return originatingVoiceFaxData;
	}
	public void setOriginatingVoiceFaxData(double originatingVoiceFaxData) {
		this.originatingVoiceFaxData = originatingVoiceFaxData;
	}
	public double getTerminatingVoiceFaxData() {
		return terminatingVoiceFaxData;
	}
	public void setTerminatingVoiceFaxData(double terminatingVoiceFaxData) {
		this.terminatingVoiceFaxData = terminatingVoiceFaxData;
	}
	public double getOriginatingSMS() {
		return originatingSMS;
	}
	public void setOriginatingSMS(double originatingSMS) {
		this.originatingSMS = originatingSMS;
	}
	public double getTerminatingSMS() {
		return terminatingSMS;
	}
	public void setTerminatingSMS(double terminatingSMS) {
		this.terminatingSMS = terminatingSMS;
	}
	public double getOriginatingGPRS() {
		return originatingGPRS;
	}
	public void setOriginatingGPRS(double originatingGPRS) {
		this.originatingGPRS = originatingGPRS;
	}
	public double getTerminatingGPRS() {
		return terminatingGPRS;
	}
	public void setTerminatingGPRS(double terminatingGPRS) {
		this.terminatingGPRS = terminatingGPRS;
	}
	public double getOriginatingContent() {
		return originatingContent;
	}
	public void setOriginatingContent(double originatingContent) {
		this.originatingContent = originatingContent;
	}
	public double getTerminatingContent() {
		return terminatingContent;
	}
	public void setTerminatingContent(double terminatingContent) {
		this.terminatingContent = terminatingContent;
	}
	public double getOriginatingVideo() {
		return originatingVideo;
	}
	public void setOriginatingVideo(double originatingVideo) {
		this.originatingVideo = originatingVideo;
	}
	public double getTerminatingVideo() {
		return terminatingVideo;
	}
	public void setTerminatingVideo(double terminatingVideo) {
		this.terminatingVideo = terminatingVideo;
	}
	public double getOriginatingUnknown() {
		return originatingUnknown;
	}
	public void setOriginatingUnknown(double originatingUnknown) {
		this.originatingUnknown = originatingUnknown;
	}
	public double getTerminatingUnknown() {
		return terminatingUnknown;
	}
	public void setTerminatingUnknown(double terminatingUnknown) {
		this.terminatingUnknown = terminatingUnknown;
	}
	public double getOriginatingSCAPv2() {
		return originatingSCAPv2;
	}
	public void setOriginatingSCAPv2(double originatingSCAPv2) {
		this.originatingSCAPv2 = originatingSCAPv2;
	}
	public double getTerminatingSCAPv2() {
		return terminatingSCAPv2;
	}
	public void setTerminatingSCAPv2(double terminatingSCAPv2) {
		this.terminatingSCAPv2 = terminatingSCAPv2;
	}
	public double getOriginatingGy() {
		return originatingGy;
	}
	public void setOriginatingGy(double originatingGy) {
		this.originatingGy = originatingGy;
	}
	

}
