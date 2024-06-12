/* 
 * File:       VMSData.java
 * Date        Author          Changes
 * 01/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for VMS Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CopsUSSD888Data extends PersistenceObject {

	public Date date;

	public String requestType;
	public String serviceType;
	public long requestCount;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

}