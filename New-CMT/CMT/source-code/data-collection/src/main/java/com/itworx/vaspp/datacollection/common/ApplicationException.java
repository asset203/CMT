/* 
 * File:       ApplicationException.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Representing Exceptions due to application configuration errors
 */

package com.itworx.vaspp.datacollection.common;

public class ApplicationException extends Exception {
	protected int errorID;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(String msg, int code) {
		super("errorID:" + code + " " + msg);
		this.errorID = code;
	}

	public ApplicationException(Throwable thr) {
		super(thr);
	}

	public int getErrorID() {
		return errorID;
	}

	public void setErrorID(int errorID) {
		this.errorID = errorID;
	}
}