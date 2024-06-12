package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class CDRReconProccData extends PersistenceObject{
	public Date dateTime;
	public String sourceId;
	public double streamId;
	public double totalStreamRecordsIn;
	public double avgProccTime;

public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public double getStreamId() {
		return streamId;
	}

	public void setStreamId(double streamId) {
		this.streamId = streamId;
	}

	public double getTotalStreamRecordsIn() {
		return totalStreamRecordsIn;
	}

	public void setTotalStreamRecordsIn(double totalStreamRecordsIn) {
		this.totalStreamRecordsIn = totalStreamRecordsIn;
	}

	public double getAvgProccTime() {
		return avgProccTime;
	}

	public void setAvgProccTime(double avgProccTime) {
		this.avgProccTime = avgProccTime;
	}

public CDRReconProccData()
{
}
}
