/* 
 * File:       InputException.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Representing Exceptions due to Input errors
 */
package com.itworx.vaspp.datacollection.common;

public class InputException extends Exception {
	protected int errorID;

	public InputException() {
		super();
	}

	public InputException(String msg) {
		super(msg);
	}

	public InputException(Throwable thr) {
		super(thr);
	}

	public void setErrorID(int errorID) {
		this.errorID = errorID;
	}

	public int getErrorID() {
		return errorID;
	}
}