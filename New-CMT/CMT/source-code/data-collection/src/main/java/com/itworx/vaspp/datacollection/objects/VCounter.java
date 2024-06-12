/* 
 * File:       VCounter.java
 * Date        Author            Changes
 * 23/05/2010  Marwan Abdelhady  Created
 * 
 * Represent Counter from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

public class VCounter{
	private String id;
	private String name;
	private String error;
	

	private String warning;
	private String normal;
	private String sql;
	
	private String dateColumn;

	private int daily;

	private int weekly;
	
	private int monthly;
	
	public VCounter() {
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setDateColumn(String dateColumn) {
		this.dateColumn = dateColumn;
	}

	public String getDateColumn() {
		return dateColumn;
	}

	public void setDaily(int daily) {
		this.daily = daily;
	}

	public int getDaily() {
		return daily;
	}

	public void setWeekly(int weekly) {
		this.weekly = weekly;
	}

	public int getWeekly() {
		return weekly;
	}
	
	public void setMonthly(int monthly) {
		this.monthly = monthly;
	}

	public int getMonthly() {
		return monthly;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}
}