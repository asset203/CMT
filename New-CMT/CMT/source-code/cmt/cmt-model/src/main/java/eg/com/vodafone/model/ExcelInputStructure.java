/*
 * File:       ExcelInputStructure.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 20/02/2006  Nayera Mohamed  Updated to include horizontal flag
 * 18/3/2013   basma alkerm    Updated replace sheetNames[] by ArrayList
 * 18/3/2013   basma alkerm    Updated  move dataColumns[] to super
 * Represent Excel Input Structure of input data from XML configuration
 */

package eg.com.vodafone.model;

import java.util.List;
import java.util.Map;

public class ExcelInputStructure extends VInputStructure {
	private int skip;
	private boolean useSheetInData;
	private Map<String,String> parametersMap;
	private List<String> sheetNamesList;
	private boolean horizontal;

	public ExcelInputStructure() {
	}
    public ExcelInputStructure (VInputStructure inputStructure) {
        super(inputStructure);
    }

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getSkip() {
		return skip;
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
	public Map<String, String> getParametersMap() {
		return parametersMap;
	}

    public List<String> getSheetNamesList() {
        return sheetNamesList;
    }

    public void setSheetNamesList(List<String> sheetNamesList) {
        this.sheetNamesList = sheetNamesList;
    }

    public void setParametersMap(Map<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}

    public String[] getSheetNames(){
        return sheetNamesList.toArray(new String[]{});
    }
}