/* 
 * File:       SDP_CALLS.java
 * Date        Author          Changes
 * 24/01/2006  Nayera Mohamed  Created
 * 
 * Persistence class for SDP CALLS Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SDP_CALLS extends PersistenceObject {

	public long org_atmp;
	
	public long trm_atmp;

	public long org_succ;
	
	public long trm_succ;

	public long org_unsucc_4cases;

	public long org_unsucc_cong;

	public Date start_date_time;

	public Date end_date_time;

	public long rm_org_atmp;

	public long rm_trm_atmp;

	public long rm_org_succ;

	public long rm_trm_succ;

	public long rm_org_unsucc_4cases;

	public long rm_org_unsucc_cong;

	public long rm_trm_unsucc_4cases;

	public long rm_trm_unsucc_cong;

	public long org_bar_no_serv;

	public long org_bar_no_actv_srv;

	public long org_bar_bylist;

	public long org_bar_acc_low;

	public long rm_org_bar_no_srv;

	public long rm_org_bar_no_actv_srv;

	public long rm_org_bar_bylist;

	public long rm_org_bar_acc_low;

	public long trm_bar_no_pasv_srv;

	public long trm_bar_no_actv_srv;

	public long trm_bar_acc_low;
	
	public long trm_bar_bylist;
	
	public long rm_trm_bar_no_pasv_srv;

	public long rm_trm_bar_no_actv_srv;

	public long rm_trm_bar_acc_low;

	public long rm_trm_bar_bylist;

	public long acc_adj_gprs_cdr;

	public long acc_adj_cdr_ext1;
	
	public long serviceClass;
	
	public long callBackAttempts;
	
	public long callBackSuccAttempts;

	public String nodeName = super.nodeName;

	public SDP_CALLS() {

	}

	public void setOrg_atmp(long org_atmp) {
		this.org_atmp = org_atmp;
	}

	public long getOrg_atmp() {
		return org_atmp;
	}

	public void setOrg_succ(long org_succ) {
		this.org_succ = org_succ;
	}

	public long getOrg_succ() {
		return org_succ;
	}

	public void setOrg_unsucc_4cases(long org_unsucc_4cases) {
		this.org_unsucc_4cases = org_unsucc_4cases;
	}

	public long getOrg_unsucc_4cases() {
		return org_unsucc_4cases;
	}

	public void setOrg_unsucc_cong(long org_unsucc_cong) {
		this.org_unsucc_cong = org_unsucc_cong;
	}

	public long getOrg_unsucc_cong() {
		return org_unsucc_cong;
	}

	public void setStart_date_time(Date start_date_time) {
		this.start_date_time = start_date_time;
	}

	public Date getStart_date_time() {
		return start_date_time;
	}

	public void setEnd_date_time(Date end_date_time) {
		this.end_date_time = end_date_time;
	}

	public Date getEnd_date_time() {
		return end_date_time;
	}


	public void setRm_org_atmp(long rm_org_atmp) {
		this.rm_org_atmp = rm_org_atmp;
	}

	public long getRm_org_atmp() {
		return rm_org_atmp;
	}

	public void setRm_trm_atmp(long rm_trm_atmp) {
		this.rm_trm_atmp = rm_trm_atmp;
	}

	public long getRm_trm_atmp() {
		return rm_trm_atmp;
	}

	public void setRm_org_succ(long rm_org_succ) {
		this.rm_org_succ = rm_org_succ;
	}

	public long getRm_org_succ() {
		return rm_org_succ;
	}

	public void setRm_trm_succ(long rm_trm_succ) {
		this.rm_trm_succ = rm_trm_succ;
	}

	public long getRm_trm_succ() {
		return rm_trm_succ;
	}

	public void setRm_org_unsucc_4cases(long rm_org_unsucc_4cases) {
		this.rm_org_unsucc_4cases = rm_org_unsucc_4cases;
	}

	public long getRm_org_unsucc_4cases() {
		return rm_org_unsucc_4cases;
	}

	public void setRm_org_unsucc_cong(long rm_org_unsucc_cong) {
		this.rm_org_unsucc_cong = rm_org_unsucc_cong;
	}

	public long getRm_org_unsucc_cong() {
		return rm_org_unsucc_cong;
	}

	public void setRm_trm_unsucc_4cases(long rm_trm_unsucc_4cases) {
		this.rm_trm_unsucc_4cases = rm_trm_unsucc_4cases;
	}

	public long getRm_trm_unsucc_4cases() {
		return rm_trm_unsucc_4cases;
	}

	public void setRm_trm_unsucc_cong(long rm_trm_unsucc_cong) {
		this.rm_trm_unsucc_cong = rm_trm_unsucc_cong;
	}

	public long getRm_trm_unsucc_cong() {
		return rm_trm_unsucc_cong;
	}

	public void setOrg_bar_no_serv(long org_bar_no_serv) {
		this.org_bar_no_serv = org_bar_no_serv;
	}

	public long getOrg_bar_no_serv() {
		return org_bar_no_serv;
	}

	public void setOrg_bar_no_actv_srv(long org_bar_no_actv_srv) {
		this.org_bar_no_actv_srv = org_bar_no_actv_srv;
	}

	public long getOrg_bar_no_actv_srv() {
		return org_bar_no_actv_srv;
	}

	public void setOrg_bar_bylist(long org_bar_bylist) {
		this.org_bar_bylist = org_bar_bylist;
	}

	public long getOrg_bar_bylist() {
		return org_bar_bylist;
	}

	public void setOrg_bar_acc_low(long org_bar_acc_low) {
		this.org_bar_acc_low = org_bar_acc_low;
	}

	public long getOrg_bar_acc_low() {
		return org_bar_acc_low;
	}

	public void setRm_org_bar_no_srv(long rm_org_bar_no_srv) {
		this.rm_org_bar_no_srv = rm_org_bar_no_srv;
	}

	public long getRm_org_bar_no_srv() {
		return rm_org_bar_no_srv;
	}

	public void setRm_org_bar_no_actv_srv(long rm_org_bar_no_actv_srv) {
		this.rm_org_bar_no_actv_srv = rm_org_bar_no_actv_srv;
	}

	public long getRm_org_bar_no_actv_srv() {
		return rm_org_bar_no_actv_srv;
	}

	public void setRm_org_bar_bylist(long rm_org_bar_bylist) {
		this.rm_org_bar_bylist = rm_org_bar_bylist;
	}

	public long getRm_org_bar_bylist() {
		return rm_org_bar_bylist;
	}

	public void setRm_org_bar_acc_low(long rm_org_bar_acc_low) {
		this.rm_org_bar_acc_low = rm_org_bar_acc_low;
	}

	public long getRm_org_bar_acc_low() {
		return rm_org_bar_acc_low;
	}

	public void setTrm_bar_no_pasv_srv(long trm_bar_no_pasv_srv) {
		this.trm_bar_no_pasv_srv = trm_bar_no_pasv_srv;
	}

	public long getTrm_bar_no_pasv_srv() {
		return trm_bar_no_pasv_srv;
	}

	public void setTrm_bar_no_actv_srv(long trm_bar_no_actv_srv) {
		this.trm_bar_no_actv_srv = trm_bar_no_actv_srv;
	}

	public long getTrm_bar_no_actv_srv() {
		return trm_bar_no_actv_srv;
	}

	public void setTrm_bar_acc_low(long trm_bar_acc_low) {
		this.trm_bar_acc_low = trm_bar_acc_low;
	}

	public long getTrm_bar_acc_low() {
		return trm_bar_acc_low;
	}

	public void setRm_trm_bar_no_pasv_srv(long rm_trm_bar_no_pasv_srv) {
		this.rm_trm_bar_no_pasv_srv = rm_trm_bar_no_pasv_srv;
	}

	public long getRm_trm_bar_no_pasv_srv() {
		return rm_trm_bar_no_pasv_srv;
	}

	public void setRm_trm_bar_no_actv_srv(long rm_trm_bar_no_actv_srv) {
		this.rm_trm_bar_no_actv_srv = rm_trm_bar_no_actv_srv;
	}

	public long getRm_trm_bar_no_actv_srv() {
		return rm_trm_bar_no_actv_srv;
	}

	public void setRm_trm_bar_acc_low(long rm_trm_bar_acc_low) {
		this.rm_trm_bar_acc_low = rm_trm_bar_acc_low;
	}

	public long getRm_trm_bar_acc_low() {
		return rm_trm_bar_acc_low;
	}

	public void setRm_trm_bar_bylist(long rm_trm_bar_bylist) {
		this.rm_trm_bar_bylist = rm_trm_bar_bylist;
	}

	public long getRm_trm_bar_bylist() {
		return rm_trm_bar_bylist;
	}

	public void setAcc_adj_gprs_cdr(long acc_adj_gprs_cdr) {
		this.acc_adj_gprs_cdr = acc_adj_gprs_cdr;
	}

	public long getAcc_adj_gprs_cdr() {
		return acc_adj_gprs_cdr;
	}

	public void setAcc_adj_cdr_ext1(long acc_adj_cdr_ext1) {
		this.acc_adj_cdr_ext1 = acc_adj_cdr_ext1;
	}

	public long getAcc_adj_cdr_ext1() {
		return acc_adj_cdr_ext1;
	}

	public long getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(long serviceClass) {
		this.serviceClass = serviceClass;
	}

	public long getTrm_succ() {
		return trm_succ;
	}

	public void setTrm_succ(long trm_succ) {
		this.trm_succ = trm_succ;
	}

	public long getCallBackAttempts() {
		return callBackAttempts;
	}

	public void setCallBackAttempts(long callBackAttempts) {
		this.callBackAttempts = callBackAttempts;
	}

	public long getCallBackSuccAttempts() {
		return callBackSuccAttempts;
	}

	public void setCallBackSuccAttempts(long callBackSuccAttempts) {
		this.callBackSuccAttempts = callBackSuccAttempts;
	}

	public long getTrm_bar_bylist() {
		return trm_bar_bylist;
	}

	public void setTrm_bar_bylist(long trm_bar_bylist) {
		this.trm_bar_bylist = trm_bar_bylist;
	}

	public long getTrm_atmp() {
		return trm_atmp;
	}

	public void setTrm_atmp(long trm_atmp) {
		this.trm_atmp = trm_atmp;
	}

}