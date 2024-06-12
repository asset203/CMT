/*
 * File:       TextInputStructure.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 18/03/2006  Nayera Mohamed  Updated to include extract date flag
 *
 * Represent Text Input Structure of input data from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

import java.util.HashMap;

public class TextInputStructure extends VInputStructure {
	// first column name then column type
	private DataColumn[] columns;
	private HashMap<String,String> parametersMap;
	

	private String converter;

	private boolean extractDate;
	private boolean extractDatemonthly;

	public boolean isExtractDatemonthly() {
		return extractDatemonthly;
	}

	public void setExtractDatemonthly(boolean extractDatemonthly) {
		this.extractDatemonthly = extractDatemonthly;
	}

	private String dateFormat;

	public TextInputStructure() {
	}

	public void setColumns(DataColumn[] columns) {
		this.columns = columns;
	}

	public DataColumn[] getColumns() {
		return columns;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	public String getConverter() {
		return converter;
	}

	public void setExtractDate(boolean extractDate) {
		this.extractDate = extractDate;
	}

	public boolean isExtractDate() {
		return extractDate;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}
	public HashMap<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(HashMap<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}
}
