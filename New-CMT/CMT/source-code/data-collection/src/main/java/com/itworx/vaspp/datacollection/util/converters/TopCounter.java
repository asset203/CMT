package com.itworx.vaspp.datacollection.util.converters;

public class TopCounter {
	
	private long maxValue;
	private long correspondingValue1;
	private long correspondingValue2;
	private long correspondingValue3;	
	private String sdpId;
	private String connectionId;
	private String applicationName;
	
	
	
	
	public String getApplicationName() {
		return applicationName;
	}



	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}



	public String getConnectionId() {
		return connectionId;
	}



	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}



	public long getCorrespondingValue1() {
		return correspondingValue1;
	}



	public void setCorrespondingValue1(long correspondingValue1) {
		this.correspondingValue1 = correspondingValue1;
	}



	public long getCorrespondingValue2() {
		return correspondingValue2;
	}



	public void setCorrespondingValue2(long correspondingValue2) {
		this.correspondingValue2 = correspondingValue2;
	}



	public long getCorrespondingValue3() {
		return correspondingValue3;
	}



	public void setCorrespondingValue3(long correspondingValue3) {
		this.correspondingValue3 = correspondingValue3;
	}



	public long getMaxValue() {
		return maxValue;
	}



	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}



	public boolean equals(TopCounter counter)
    {
      return counter.getMaxValue()==this.getMaxValue();
    }



	public String getSdpId() {
		return sdpId;
	}



	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	
}
