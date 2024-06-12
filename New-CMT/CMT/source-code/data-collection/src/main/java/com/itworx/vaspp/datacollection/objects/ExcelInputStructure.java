/*
 * File:       ExcelInputStructure.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 20/02/2006  Nayera Mohamed  Updated to include horizontal flag
 *
 * Represent Excel Input Structure of input data from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

import java.util.HashMap;

public class ExcelInputStructure extends VInputStructure {
	private int skip;

	private boolean useSheetInData;
	private HashMap<String,String> parametersMap;
	private String[] sheetNames;

	private DataColumn[] columns;

	private boolean horizontal;

	public ExcelInputStructure() {
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getSkip() {
		return skip;
	}

	public void setSheetNames(String[] sheetNames) {
		this.sheetNames = sheetNames;
	}

	public String[] getSheetNames() {
		return sheetNames;
	}

	public void setColumns(DataColumn[] columns) {
		this.columns = columns;
	}

	public DataColumn[] getColumns() {
		return columns;
	}

	public void setUseSheetInData(boolean useSheetInData) {
		this.useSheetInData = useSheetInData;
	}

	public boolean isUseSheetInData() {
		return useSheetInData;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public boolean isHorizontal() {
		return horizontal;
	}
	public HashMap<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(HashMap<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}
}