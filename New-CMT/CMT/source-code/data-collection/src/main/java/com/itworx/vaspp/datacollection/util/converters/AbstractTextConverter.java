package com.itworx.vaspp.datacollection.util.converters;

import java.io.File;
import java.util.HashMap;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;

public abstract class AbstractTextConverter implements TextConverter{

	private HashMap<String,String> parametersMap=null;
	public abstract File[] convert(File[] inputFiles, String systemName) throws ApplicationException, InputException;
	
	public HashMap<String, String> getParametersMap() {
		return parametersMap;
	}
	public void setParametersMap(HashMap<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}


}
