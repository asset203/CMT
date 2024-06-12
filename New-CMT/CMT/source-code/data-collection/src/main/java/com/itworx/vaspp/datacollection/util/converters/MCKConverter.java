/*
 * File:       MCKConverter.java
 * Date        Author          Changes
 * 27/02/2006  Nayera Mohamed  Created
 *
 * Converter class for converting MCK text input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.log4j.Logger;

/*
 * The structure of the text input file is lines of tab separated data, the
 * first line is column names
 */

public class MCKConverter extends AbstractTextConverter {
	private Logger logger;

	public MCKConverter() {
	}

	/**
	 * loop over input files loop over lines in input file, replace tabs with
	 * commas write to output files output files are placed on the configured
	 * converted file path
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("MCKConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("MCKConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				inputStream.readLine();
				while (inputStream.ready()) {
					String line = inputStream.readLine();
					if (line.equals("")) {
						break;
					}
					line = line.replace('\t', ',');
					String dateString = line.substring(0, line.indexOf(",", 0));
					String newDate = this.updateDateFormat(dateString);
					line = line.replaceFirst(dateString, newDate);
					outputStream.write(line, 0, line.length());
					outputStream.newLine();
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("MCKConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
		} catch (FileNotFoundException e) {
			logger.error("MCKConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("MCKConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("MCKConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	/**
	 * @param dateString -
	 *            the date string to be converted
	 * 
	 * @return String - the date string converted to standard format
	 * @exception InputException
	 *                if ParseException occured
	 */
	private String updateDateFormat(String dateString) throws InputException {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("dd-M-yyyy H:");
		try {
			Date date = frm.parse(dateString);
			frm.applyPattern("MM/dd/yyyy HH:mm");
			String newDate = frm.format(date);
			return newDate;
		} catch (ParseException e) {
			throw new InputException("invalid date in input file ");
		}
	}

	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			MCKConverter s = new MCKConverter();
      File[] f = new File[1];
      f[0] = new File("C:/Documents and Settings/nayera.mohamed/Desktop/MCK20060524.txt");
			s.convert(f,"mck");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}