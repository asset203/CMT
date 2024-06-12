/* 
 * File:       RBT_Data.java
 * Date        Author          Changes
 * 19/03/2007  Eshraq Essam	   Created
 * 
 * Persistence class for RBT Usage Info
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class RBT_UsageInfo extends PersistenceObject {

	public Date dateTime;

	public long iam;

	public long earlyREL;

	public long acmACK;

	public long acmPosvAck;
	
	public long playFileAck;
	
	public long playFilePosvAck;
	
	public long rel;
	
	public RBT_UsageInfo() {
	}

	public long getAcmACK() {
		return acmACK;
	}

	public void setAcmACK(long acmACK) {
		this.acmACK = acmACK;
	}

	public long getAcmPosvAck() {
		return acmPosvAck;
	}

	public void setAcmPosvAck(long acmPosvAck) {
		this.acmPosvAck = acmPosvAck;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getEarlyREL() {
		return earlyREL;
	}

	public void setEarlyREL(long earlyREL) {
		this.earlyREL = earlyREL;
	}

	public long getIam() {
		return iam;
	}

	public void setIam(long iam) {
		this.iam = iam;
	}

	public long getPlayFileAck() {
		return playFileAck;
	}

	public void setPlayFileAck(long playFileAck) {
		this.playFileAck = playFileAck;
	}

	public long getPlayFilePosvAck() {
		return playFilePosvAck;
	}

	public void setPlayFilePosvAck(long playFilePosvAck) {
		this.playFilePosvAck = playFilePosvAck;
	}

	public long getRel() {
		return rel;
	}

	public void setRel(long rel) {
		this.rel = rel;
	}

	
}