/* 
 * File:       MCKData.java
 * Date        Author          Changes
 * 27/02/2006  Nayera Mohamed  Created
 * 
 * Persistence class for MCK Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MCKData extends PersistenceObject {

	public Date hour;

	public long ussd_prov;

	public long ussd_retrv;

	public long ussd_no_calls;

	public long sms_new;

	public long sms_update;

	public long sms_succ_update;

	public long calls_busy;

	public long calls_no_reply;

	public long calls_uncon_unreach;

	public long calls_push;

	public long calls_notsub_bar_notavl;

	public MCKData() {
	}

	public void setUssd_prov(long ussd_prov) {
		this.ussd_prov = ussd_prov;
	}

	public long getUssd_prov() {
		return ussd_prov;
	}

	public void setUssd_retrv(long ussd_retrv) {
		this.ussd_retrv = ussd_retrv;
	}

	public long getUssd_retrv() {
		return ussd_retrv;
	}

	public void setUssd_no_calls(long ussd_no_calls) {
		this.ussd_no_calls = ussd_no_calls;
	}

	public long getUssd_no_calls() {
		return ussd_no_calls;
	}

	public void setSms_new(long sms_new) {
		this.sms_new = sms_new;
	}

	public long getSms_new() {
		return sms_new;
	}

	public void setSms_update(long sms_update) {
		this.sms_update = sms_update;
	}

	public long getSms_update() {
		return sms_update;
	}

	public void setSms_succ_update(long sms_succ_update) {
		this.sms_succ_update = sms_succ_update;
	}

	public long getSms_succ_update() {
		return sms_succ_update;
	}

	public void setCalls_busy(long calls_busy) {
		this.calls_busy = calls_busy;
	}

	public long getCalls_busy() {
		return calls_busy;
	}

	public void setCalls_no_reply(long calls_no_reply) {
		this.calls_no_reply = calls_no_reply;
	}

	public long getCalls_no_reply() {
		return calls_no_reply;
	}

	public void setCalls_uncon_unreach(long calls_uncon_unreach) {
		this.calls_uncon_unreach = calls_uncon_unreach;
	}

	public long getCalls_uncon_unreach() {
		return calls_uncon_unreach;
	}

	public void setCalls_push(long calls_push) {
		this.calls_push = calls_push;
	}

	public long getCalls_push() {
		return calls_push;
	}

	public void setCalls_notsub_bar_notavl(long calls_notsub_bar_notavl) {
		this.calls_notsub_bar_notavl = calls_notsub_bar_notavl;
	}

	public long getCalls_notsub_bar_notavl() {
		return calls_notsub_bar_notavl;
	}

	public void setHour(Date hour) {
		this.hour = hour;
	}

	public Date getHour() {
		return hour;
	}
}