package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MckUssdData extends PersistenceObject {
	
	public Date dateTime;
	
	public double ussdProv;

	public double ussdRetrv;

	public double ussdNoCalls;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getUssdNoCalls() {
		return ussdNoCalls;
	}

	public void setUssdNoCalls(double ussdNoCalls) {
		this.ussdNoCalls = ussdNoCalls;
	}

	public double getUssdProv() {
		return ussdProv;
	}

	public void setUssdProv(double ussdProv) {
		this.ussdProv = ussdProv;
	}

	public double getUssdRetrv() {
		return ussdRetrv;
	}

	public void setUssdRetrv(double ussdRetrv) {
		this.ussdRetrv = ussdRetrv;
	}

	
}
