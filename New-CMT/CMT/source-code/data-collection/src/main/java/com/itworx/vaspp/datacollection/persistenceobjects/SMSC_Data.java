/* 
 * File:       SMSC_Data.java
 * Date        Author          Changes
 * 20/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for SMSC Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class SMSC_Data extends PersistenceObject {

	public Date time;

	public double count;

	public String Sourcenode;

	public String org_des_flag;

	public String org_des;

	public SMSC_Data() {
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getCount() {
		return count;
	}

	public void setOrg_des_flag(String org_des_flag) {
		this.org_des_flag = org_des_flag;
	}

	public String getOrg_des_flag() {
		return org_des_flag;
	}

	public void setOrg_des(String org_des) {
		this.org_des = org_des;
	}

	public String getOrg_des() {
		return org_des;
	}

	public void setSourcenode(String Sourcenode) {
		this.Sourcenode = Sourcenode;
	}

	public String getSourcenode() {
		return Sourcenode;
	}
}