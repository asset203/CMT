/*
 * File:       TextConverter.java
 * Date        Author          Changes
 * 24/01/2006  Nayera Mohamed  Created
 *
 * Interface to be implemented by all Converter classes
 */

package com.itworx.vaspp.datacollection.util.converters;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import java.io.File;

public interface TextConverter {
	public File[] convert(File inputFiles[], String systemName)
			throws ApplicationException, InputException;

}