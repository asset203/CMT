/* 
 * File:       SDP_Subscribers.java
 * Date        Author          Changes
 * 24/01/2006  Nayera Mohamed  Created
 * 
 * Persistence class for SDP Subscribers Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;

public class SDP_Subscribers extends PersistenceObject {

	public String sdp_name;

	public String msisdn_from;

	public String msisdn_to;

	// public long prepaid_scp;
	public long prepaid_validity;

	public long prepaid_grace;

	public long prepaid_after_grace;

	public long prepaid_not_active;

	// public long control_scp;
	public long control_active;

	public long control_not_active;

	public SDP_Subscribers() {
	}

	public void setMsisdn_from(String msisdn_from) {
		this.msisdn_from = msisdn_from;
	}

	public String getMsisdn_from() {
		return msisdn_from;
	}

	public void setMsisdn_to(String msisdn_to) {
		this.msisdn_to = msisdn_to;
	}

	public String getMsisdn_to() {
		return msisdn_to;
	}

	public void setPrepaid_validity(long prepaid_validity) {
		this.prepaid_validity = prepaid_validity;
	}

	public long getPrepaid_validity() {
		return prepaid_validity;
	}

	public void setPrepaid_grace(long prepaid_grace) {
		this.prepaid_grace = prepaid_grace;
	}

	public long getPrepaid_grace() {
		return prepaid_grace;
	}

	public void setPrepaid_after_grace(long prepaid_after_grace) {
		this.prepaid_after_grace = prepaid_after_grace;
	}

	public long getPrepaid_after_grace() {
		return prepaid_after_grace;
	}

	public void setPrepaid_not_active(long prepaid_not_active) {
		this.prepaid_not_active = prepaid_not_active;
	}

	public long getPrepaid_not_active() {
		return prepaid_not_active;
	}

	public void setControl_active(long control_active) {
		this.control_active = control_active;
	}

	public long getControl_active() {
		return control_active;
	}

	public void setControl_not_active(long control_not_active) {
		this.control_not_active = control_not_active;
	}

	public long getControl_not_active() {
		return control_not_active;
	}

	public void setSdp_name(String sdp_name) {
		this.sdp_name = sdp_name;
	}

	public String getSdp_name() {
		return sdp_name;
	}
}