/* 
 * File:       InputData.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Class for holding the data read from input
 */

package com.itworx.vaspp.datacollection.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputData {
	private String inputID;
	private String lastCallClassName;
    private String systemName;
    private String nodeName;
    private boolean  extractDateMonthly;
    private String inputStructureId;
    private eg.com.vodafone.model.DataColumn[] header;
    private Date date;
    private String dateFormatInFileNamePattern;

    public String getDateFormatInFileNamePattern() {
        return dateFormatInFileNamePattern;
    }

    public void setDateFormatInFileNamePattern(String dateFormatInFileNamePattern) {
        this.dateFormatInFileNamePattern = dateFormatInFileNamePattern;
    }

    public String getLastCallClassName() {
		return lastCallClassName;
	}

	public void setLastCallClassName(String lastCallClassName) {
		this.lastCallClassName = lastCallClassName;
	}

	public boolean isExtractDateMonthly() {
		return extractDateMonthly;
	}

	public void setExtractDateMonthly(boolean extractDateMonthly) {
		this.extractDateMonthly = extractDateMonthly;
	}

	public InputData() {
	}

	public void setHeader(eg.com.vodafone.model.DataColumn[] header) {
		this.header = header;
	}

	public eg.com.vodafone.model.DataColumn[] getHeader() {
		return header;
	}

	public void setInputID(String inputID) {
		this.inputID = inputID;
	}

	public String getInputID() {
		return inputID;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat f=new SimpleDateFormat();
		Date d=f.parse("Mon Jan 15 00:00:00 EET 2007");
		System.out
				.println("com.itworx.vaspp.datacollection.util.PersistenceManager.main(): date = "+d);
	}

	public String getInputStructureId() {
		return inputStructureId;
	}

	public void setInputStructureId(String inputStructureId) {
		this.inputStructureId = inputStructureId;
	}
}