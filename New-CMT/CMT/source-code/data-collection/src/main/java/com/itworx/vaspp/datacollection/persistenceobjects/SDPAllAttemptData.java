package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SDPAllAttemptData extends PersistenceObject{
	public Date dateTime;
	public double originating;
	public double forwarded;
	public double terminating;
	public double roamingOrig;
	public double roamingForw;
	public double roamingTerm;
	public double ussdBasCall;
	public double origServCharg;
	public double termServCharg;
	public double voice;
	public double fax;
	public double data;
	public double unknown;
	public double sms;
	public double gprs;
	public double content;
	public double videoTelph;
	public double videoConf;
	public double cdrExt1;
	public double cdrExt2;
	public double cdrExt3;
	public double cdrExt4;
	public double cdrExt5;
	public double cdrExt6;
	public double cdrExt7;
	public double cdrExt8;
	public double cdrExt9;
	public double cdrExt10;
	public double succSetup;
	public double succReByExtNw;
	public double callForeInvChar;
	public double unsuccSerTermInt;
	public double unsuccConges;
	public double unsuccNormTermInt;
	public double unsuccOtherReason;
	public double unsuccNormTermExt;
	public double commChar;
	public double commIndiFound;
	public double subsNotFound;
	public double internamError;
	public double ratingFailed;
	public double cftCharCallUnsucc;
	public double cftCharCallgran;
						
public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getOriginating() {
		return originating;
	}

	public void setOriginating(double originating) {
		this.originating = originating;
	}

	public double getForwarded() {
		return forwarded;
	}

	public void setForwarded(double forwarded) {
		this.forwarded = forwarded;
	}

	public double getTerminating() {
		return terminating;
	}

	public void setTerminating(double terminating) {
		this.terminating = terminating;
	}

	public double getRoamingOrig() {
		return roamingOrig;
	}

	public void setRoamingOrig(double roamingOrig) {
		this.roamingOrig = roamingOrig;
	}

	public double getRoamingForw() {
		return roamingForw;
	}

	public void setRoamingForw(double roamingForw) {
		this.roamingForw = roamingForw;
	}

	public double getRoamingTerm() {
		return roamingTerm;
	}

	public void setRoamingTerm(double roamingTerm) {
		this.roamingTerm = roamingTerm;
	}

	public double getUssdBasCall() {
		return ussdBasCall;
	}

	public void setUssdBasCall(double ussdBasCall) {
		this.ussdBasCall = ussdBasCall;
	}

	public double getOrigServCharg() {
		return origServCharg;
	}

	public void setOrigServCharg(double origServCharg) {
		this.origServCharg = origServCharg;
	}

	public double getTermServCharg() {
		return termServCharg;
	}

	public void setTermServCharg(double termServCharg) {
		this.termServCharg = termServCharg;
	}

	public double getVoice() {
		return voice;
	}

	public void setVoice(double voice) {
		this.voice = voice;
	}

	public double getFax() {
		return fax;
	}

	public void setFax(double fax) {
		this.fax = fax;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}

	public double getUnknown() {
		return unknown;
	}

	public void setUnknown(double unknown) {
		this.unknown = unknown;
	}

	public double getSms() {
		return sms;
	}

	public void setSms(double sms) {
		this.sms = sms;
	}

	public double getGprs() {
		return gprs;
	}

	public void setGprs(double gprs) {
		this.gprs = gprs;
	}

	public double getContent() {
		return content;
	}

	public void setContent(double content) {
		this.content = content;
	}

	public double getVideoTelph() {
		return videoTelph;
	}

	public void setVideoTelph(double videoTelph) {
		this.videoTelph = videoTelph;
	}

	public double getVideoConf() {
		return videoConf;
	}

	public void setVideoConf(double videoConf) {
		this.videoConf = videoConf;
	}

	public double getCdrExt1() {
		return cdrExt1;
	}

	public void setCdrExt1(double cdrExt1) {
		this.cdrExt1 = cdrExt1;
	}

	public double getCdrExt2() {
		return cdrExt2;
	}

	public void setCdrExt2(double cdrExt2) {
		this.cdrExt2 = cdrExt2;
	}

	public double getCdrExt3() {
		return cdrExt3;
	}

	public void setCdrExt3(double cdrExt3) {
		this.cdrExt3 = cdrExt3;
	}

	public double getCdrExt4() {
		return cdrExt4;
	}

	public void setCdrExt4(double cdrExt4) {
		this.cdrExt4 = cdrExt4;
	}

	public double getCdrExt5() {
		return cdrExt5;
	}

	public void setCdrExt5(double cdrExt5) {
		this.cdrExt5 = cdrExt5;
	}

	public double getCdrExt6() {
		return cdrExt6;
	}

	public void setCdrExt6(double cdrExt6) {
		this.cdrExt6 = cdrExt6;
	}

	public double getCdrExt7() {
		return cdrExt7;
	}

	public void setCdrExt7(double cdrExt7) {
		this.cdrExt7 = cdrExt7;
	}

	public double getCdrExt8() {
		return cdrExt8;
	}

	public void setCdrExt8(double cdrExt8) {
		this.cdrExt8 = cdrExt8;
	}

	public double getCdrExt9() {
		return cdrExt9;
	}

	public void setCdrExt9(double cdrExt9) {
		this.cdrExt9 = cdrExt9;
	}

	public double getCdrExt10() {
		return cdrExt10;
	}

	public void setCdrExt10(double cdrExt10) {
		this.cdrExt10 = cdrExt10;
	}

	public double getSuccSetup() {
		return succSetup;
	}

	public void setSuccSetup(double succSetup) {
		this.succSetup = succSetup;
	}

	public double getSuccReByExtNw() {
		return succReByExtNw;
	}

	public void setSuccReByExtNw(double succReByExtNw) {
		this.succReByExtNw = succReByExtNw;
	}

	public double getCallForeInvChar() {
		return callForeInvChar;
	}

	public void setCallForeInvChar(double callForeInvChar) {
		this.callForeInvChar = callForeInvChar;
	}

	public double getUnsuccSerTermInt() {
		return unsuccSerTermInt;
	}

	public void setUnsuccSerTermInt(double unsuccSerTermInt) {
		this.unsuccSerTermInt = unsuccSerTermInt;
	}

	public double getUnsuccConges() {
		return unsuccConges;
	}

	public void setUnsuccConges(double unsuccConges) {
		this.unsuccConges = unsuccConges;
	}

	public double getUnsuccNormTermInt() {
		return unsuccNormTermInt;
	}

	public void setUnsuccNormTermInt(double unsuccNormTermInt) {
		this.unsuccNormTermInt = unsuccNormTermInt;
	}

	public double getUnsuccOtherReason() {
		return unsuccOtherReason;
	}

	public void setUnsuccOtherReason(double unsuccOtherReason) {
		this.unsuccOtherReason = unsuccOtherReason;
	}

	public double getUnsuccNormTermExt() {
		return unsuccNormTermExt;
	}

	public void setUnsuccNormTermExt(double unsuccNormTermExt) {
		this.unsuccNormTermExt = unsuccNormTermExt;
	}

	public double getCommChar() {
		return commChar;
	}

	public void setCommChar(double commChar) {
		this.commChar = commChar;
	}

	public double getCommIndiFound() {
		return commIndiFound;
	}

	public void setCommIndiFound(double commIndiFound) {
		this.commIndiFound = commIndiFound;
	}

	public double getSubsNotFound() {
		return subsNotFound;
	}

	public void setSubsNotFound(double subsNotFound) {
		this.subsNotFound = subsNotFound;
	}

	public double getInternamError() {
		return internamError;
	}

	public void setInternamError(double internamError) {
		this.internamError = internamError;
	}

	public double getRatingFailed() {
		return ratingFailed;
	}

	public void setRatingFailed(double ratingFailed) {
		this.ratingFailed = ratingFailed;
	}

	public double getCftCharCallUnsucc() {
		return cftCharCallUnsucc;
	}

	public void setCftCharCallUnsucc(double cftCharCallUnsucc) {
		this.cftCharCallUnsucc = cftCharCallUnsucc;
	}

	public double getCftCharCallgran() {
		return cftCharCallgran;
	}

	public void setCftCharCallgran(double cftCharCallgran) {
		this.cftCharCallgran = cftCharCallgran;
	}

public SDPAllAttemptData()
{
}
}
