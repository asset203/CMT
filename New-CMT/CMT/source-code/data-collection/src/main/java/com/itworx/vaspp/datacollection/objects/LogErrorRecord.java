/**
 * 
 */
package com.itworx.vaspp.datacollection.objects;

import java.util.Hashtable;


/**
 * @author ahmad.abushady
 * description:
 * this is a bean class that represents a log Error Record
 *
 */
public class LogErrorRecord {

	
	public String sysName = "";
	
	public String date_time;
	
	public String node= "";
	
	public String type;
	
	public String description = "";
	
	private String errorCode;
	
	

	private Hashtable errorTable = null;
	
	public long retry = 0;
	
	private boolean haveErrors = false;
	
	
	 
	
	public String toString() {
		// TODO Auto-generated method stub
		return sysName + "," + node + "," + date_time + "," + retry + "," + type + "," + description;
	}
	
	public LogErrorRecord(String sName, String date, String nName, String... errorCode){
		sysName = sName;
		date_time = date;
		node = nName;
		
		if(errorCode.length > 0){
			this.setErrorCode ( errorCode[0]);
		}
		else{
			this.setErrorCode("");
		}
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * to log errors that is unknown
	 * this could only happen if an error occured for the same node, but this node
	 * have two started jobs each in different day. then the converter will not know
	 * for which job to add this error. It will log this as unknown error to all 
	 * the jobs. In case any of these jobs ended succesfully without any reported
	 * errors this unknow error will be descarded for that job only.
	 * 
	 * @param err
	 */
	public void LogUnknownError(String err){
		if(errorTable == null){
			errorTable = new Hashtable();
		}
		String checkErr = (String)errorTable.get(err);
		if(checkErr == null){
			if(description.equals("")){
				description = err;
			}
			else description = description + " , " + err;
			errorTable.put(err, err);
		}
	}
	
	/**
	 * this function logs an error
	 * it checks if this error was reported before it descard it
	 * otherwise it add it to the description
	 * @param err
	 */
	public void LogError(String err){
		if(!haveErrors){
			if(errorTable == null){
				errorTable = new Hashtable();
			}
			haveErrors = true;
			// to check if there are unknow error is logged before with same meaning
			String checkErr = (String)errorTable.get(err);
			if(checkErr == null){
				if(errorTable.isEmpty()){
					description = description + err;
				}else{
					description = description + " , " + err;
				}		
			}			
			errorTable.put(err, err);
		}
		else{
			String checkErr = (String)errorTable.get(err);
			if(checkErr == null){
				description = description + " , " + err;
				errorTable.put(err, err);
			}
		}
	}
	
	public void incRetry(){
		retry++;
	}
	
	public void decRetry(){
		retry--;
	}
	
	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		if(node.equalsIgnoreCase("system")){
			this.node = node;
		}
	}

	public long getRetry() {
		return retry;
	}

	public void setRetry(long retry) {
		this.retry = retry;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	/**
	 * 
	 */
	public LogErrorRecord() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHaveErrors() {
		return haveErrors;
	}

}
