package com.itworx.vaspp.datacollection.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class VNotificationCounter {
	private Map<String, VNotificationSql> sql = new HashMap<String, VNotificationSql>();

	public Map<String, VNotificationSql> getSql() {
		return sql;
	}

	public void setSql(Map<String, VNotificationSql> sql) {
		this.sql = sql;
	}

	private String notificationSms;
	private String failureSms;
	private String failureMail;
	private String emailSender;
	private String smsSender;
	private String subject;
	private String[] users = null;
	private boolean compound = true;

	public String[] getUsers() {
		return users;
	}

	public void setUsers(String[] users) {
		this.users = users;
	}

	public String[] getCcUsers() {
		return ccUsers;
	}

	public void setCcUsers(String[] ccUsers) {
		this.ccUsers = ccUsers;
	}

	public String[] getFailureUsers() {
		return failureUsers;
	}

	public void setFailureUsers(String[] failureUsers) {
		this.failureUsers = failureUsers;
	}

	private String[] ccUsers = null;
	private String[] failureUsers = null;
	private String id;
	private boolean enabled;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNotificationSms() {
		return notificationSms;
	}

	public void setNotificationSms(String notificationSms) {
		this.notificationSms = notificationSms;
	}

	public String getFailureSms() {
		return failureSms;
	}

	public void setFailureSms(String failureSms) {
		this.failureSms = failureSms;
	}

	public String getFailureMail() {
		return failureMail;
	}

	public void setFailureMail(String failureMail) {
		this.failureMail = failureMail;
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	public String getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(String smsSender) {
		this.smsSender = smsSender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isCompound() {
		return compound;
	}

	public void setCompound(boolean compound) {
		this.compound = compound;
	}
	
	

}
