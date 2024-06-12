/* 
 * File:       IVRData.java
 * Date        Author          Changes
 * 14/02/2006  Nayera Mohamed  Created
 * 
 * Persistence class for IVR Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class IVRData extends PersistenceObject {
	public long id;

	public Date time;

	public String mo;

	public String name;

	public double directly_connected_transits;

	public double msc_count;

	public double msc_calls;

	public double traffic;

	public double transit_calls;

	public double msc_unsucc_calls;

	public double transit_unsucc_calls;

	public double success_rate;

	public double answered_calls;

	public double asr;

	public double mht;

	public IVRData() {
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setMo(String mo) {
		this.mo = mo;
	}

	public String getMo() {
		return mo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDirectly_connected_transits(
			double directly_connected_transits) {
		this.directly_connected_transits = directly_connected_transits;
	}

	public double getDirectly_connected_transits() {
		return directly_connected_transits;
	}

	public void setMsc_count(double msc_count) {
		this.msc_count = msc_count;
	}

	public double getMsc_count() {
		return msc_count;
	}

	public void setMsc_calls(double msc_calls) {
		this.msc_calls = msc_calls;
	}

	public double getMsc_calls() {
		return msc_calls;
	}

	public void setTraffic(double traffic) {
		this.traffic = traffic;
	}

	public double getTraffic() {
		return traffic;
	}

	public void setTransit_calls(double transit_calls) {
		this.transit_calls = transit_calls;
	}

	public double getTransit_calls() {
		return transit_calls;
	}

	public void setMsc_unsucc_calls(double msc_unsucc_calls) {
		this.msc_unsucc_calls = msc_unsucc_calls;
	}

	public double getMsc_unsucc_calls() {
		return msc_unsucc_calls;
	}

	public void setTransit_unsucc_calls(double transit_unsucc_calls) {
		this.transit_unsucc_calls = transit_unsucc_calls;
	}

	public double getTransit_unsucc_calls() {
		return transit_unsucc_calls;
	}

	public void setSuccess_rate(double success_rate) {
		this.success_rate = success_rate;
	}

	public double getSuccess_rate() {
		return success_rate;
	}

	public void setAnswered_calls(double answered_calls) {
		this.answered_calls = answered_calls;
	}

	public double getAnswered_calls() {
		return answered_calls;
	}

	public void setAsr(double asr) {
		this.asr = asr;
	}

	public double getAsr() {
		return asr;
	}

	public void setMht(double mht) {
		this.mht = mht;
	}

	public double getMht() {
		return mht;
	}

}