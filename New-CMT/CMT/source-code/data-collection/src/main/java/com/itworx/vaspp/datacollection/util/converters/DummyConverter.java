/*
 * File:       DummyConverter.java
 * Date        Author          Changes
 * 24/05/2006  Nayera Mohamed  Created
 *
 * dummy class for input files which are comma separated already
 */

package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.*;

import org.apache.log4j.Logger;

/*
 * The structure of the text input file is
 * 
 * Line 1 : start date Line 2 : empty Line 3 : end date Line 4 : empty Line 5 :
 * comma separated data Repeated..........
 * 
 */
public class DummyConverter extends AbstractTextConverter {
	private Logger logger;

	public DummyConverter() {
	}

	/**
	 * loop over input file, loop over lines in each file read start date, end
	 * date , data concatenate into on comma separated string then write to
	 * output output files are placed on the configured converted file path
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("DummyConverter.convert() - sending original input files ");
			return inputFiles;
	}

	// for testing
	public static void main(String ag[]) {
		try {
			PropertyReader.init("");
			SDPConverter s = new SDPConverter();
			// s.convert(new File("sdp1.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}