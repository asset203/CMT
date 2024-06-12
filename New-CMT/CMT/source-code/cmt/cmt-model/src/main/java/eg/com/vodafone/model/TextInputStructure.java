/*
 * File:       TextInputStructure.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 18/03/2006  Nayera Mohamed  Updated to include extract date flag
 *
 * Represent Text Input Structure of input data from XML configuration
 */

package eg.com.vodafone.model;

import java.util.List;
import java.util.Map;

public class TextInputStructure extends VInputStructure {
	private Map<String,String> parametersMap;
	private String converter;



	public TextInputStructure() {
	}
	public void setConverter(String converter) {
		this.converter = converter;
	}

	public String getConverter() {
		return converter;
	}
	public Map<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}
}
