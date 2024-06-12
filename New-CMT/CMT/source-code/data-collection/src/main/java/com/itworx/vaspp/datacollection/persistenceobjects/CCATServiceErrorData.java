package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class CCATServiceErrorData  extends PersistenceObject{
	public Date entryDate;
	public double errorId;
	public double srcId;
	public String msisdn;
	public String errorMessage;
	public String errorCode;
	public String processed;
	public Date processedDatw;
	public double perceivedSeverity;
public CCATServiceErrorData()
	{}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public double getErrorId() {
		return errorId;
	}
	public void setErrorId(double errorId) {
		this.errorId = errorId;
	}
	public double getSrcId() {
		return srcId;
	}
	public void setSrcId(double srcId) {
		this.srcId = srcId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getProcessed() {
		return processed;
	}
	public void setProcessed(String processed) {
		this.processed = processed;
	}
	public Date getProcessedDatw() {
		return processedDatw;
	}
	public void setProcessedDatw(Date processedDatw) {
		this.processedDatw = processedDatw;
	}
	public double getPerceivedSeverity() {
		return perceivedSeverity;
	}
	public void setPerceivedSeverity(double perceivedSeverity) {
		this.perceivedSeverity = perceivedSeverity;
	}
	
}
