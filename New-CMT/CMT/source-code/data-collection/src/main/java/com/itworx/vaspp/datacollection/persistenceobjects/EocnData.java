package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class EocnData  extends PersistenceObject {
	public Date dateTime;
	public double requests;
	public double responses;
	public double succeededReq;
	public double hlrResp;
	public double maxRequests;
	public double averageHlrResponseTime;
	public double avgSuccessReqRespTime;
	/*public double successReqRespLT5;
	public double successReqRespBT5AND20; 
	
	public double successReqRespLT10;
	public double successReqRespBT10AND20; 
	public double successReqRespBT20AND30;
	public double successReqRespBT30AND60;
	public double successReqRespGT60;*/
	
	public double getAverageHlrResponseTime() {
		return averageHlrResponseTime;
	}
	public void setAverageHlrResponseTime(double averageHlrResponseTime) {
		this.averageHlrResponseTime = averageHlrResponseTime;
	}
	public double getSucceededReq() {
		return succeededReq;
	}
	public void setSucceededReq(double succeededReq) {
		this.succeededReq = succeededReq;
	}
	public double getHlrResp() {
		return hlrResp;
	}
	public void setHlrResp(double hlrResp) {
		this.hlrResp = hlrResp;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getRequests() {
		return requests;
	}
	public void setRequests(double requests) {
		this.requests = requests;
	}
	public double getResponses() {
		return responses;
	}
	public void setResponses(double responses) {
		this.responses = responses;
	}
	public EocnData()
	{}
	public double getMaxRequests() {
		return maxRequests;
	}
	public void setMaxRequests(double maxRequests) {
		this.maxRequests = maxRequests;
	}
	public double getAvgSuccessReqRespTime() {
		return avgSuccessReqRespTime;
	}
	public void setAvgSuccessReqRespTime(double avgSuccessReqRespTime) {
		this.avgSuccessReqRespTime = avgSuccessReqRespTime;
	}
/*	public double getSuccessReqRespLT10() {
		return successReqRespLT10;
	}
	public void setSuccessReqRespLT10(double successReqRespLT10) {
		this.successReqRespLT10 = successReqRespLT10;
	}
	public double getSuccessReqRespBT10AND20() {
		return successReqRespBT10AND20;
	}
	public void setSuccessReqRespBT10AND20(double successReqRespBT10AND20) {
		this.successReqRespBT10AND20 = successReqRespBT10AND20;
	}
	public double getSuccessReqRespBT20AND30() {
		return successReqRespBT20AND30;
	}
	public void setSuccessReqRespBT20AND30(double successReqRespBT20AND30) {
		this.successReqRespBT20AND30 = successReqRespBT20AND30;
	}
	public double getSuccessReqRespBT30AND60() {
		return successReqRespBT30AND60;
	}
	public void setSuccessReqRespBT30AND60(double successReqRespBT30AND60) {
		this.successReqRespBT30AND60 = successReqRespBT30AND60;
	}
	public double getSuccessReqRespGT60() {
		return successReqRespGT60;
	}
	public void setSuccessReqRespGT60(double successReqRespGT60) {
		this.successReqRespGT60 = successReqRespGT60;
	}
	public double getSuccessReqRespLT5() {
		return successReqRespLT5;
	}
	public void setSuccessReqRespLT5(double successReqRespLT5) {
		this.successReqRespLT5 = successReqRespLT5;
	}
	public double getSuccessReqRespBT5AND20() {
		return successReqRespBT5AND20;
	}
	public void setSuccessReqRespBT5AND20(double successReqRespBT5AND20) {
		this.successReqRespBT5AND20 = successReqRespBT5AND20;
	}
	
	*/
	
}
