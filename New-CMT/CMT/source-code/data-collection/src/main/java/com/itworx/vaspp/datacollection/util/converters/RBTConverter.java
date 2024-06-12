/*
 * File:       RBTConverter.java
 * Date        Author          Changes
 * 29/01/2006  Nayera Mohamed  Created
 *
 * Converter class for converting RBT text input file into comma spearated format
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

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/*
 * The structure of the text input file is
 * 
 * Line 1 : empty Line 2 : file header Line 3 : empty Line 4 : column headers
 * Line 5 : spaces separated data Repeated Line 5
 * 
 */

public class RBTConverter extends AbstractTextConverter {
	private Logger logger;

	public RBTConverter() {
	}

	/**
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
		String path = PropertyReader.getConvertedFilesPath();
		logger
				.debug("RBTConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("RBTConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				Date date = this.readDate(inputStream);
				while (inputStream.ready()) {
					String line = inputStream.readLine();
					if (line.equals("")) {
						break;
					}
					StringTokenizer s = new StringTokenizer(line);
					String timeString = s.nextToken();
					String dateString = this.updateDateFormat(timeString, date);
					outputStream.write(dateString + ",");
					while (s.hasMoreTokens()) {
						outputStream.write(s.nextToken() + ",");
					}
					outputStream.newLine();
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("RBTConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("RBTConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("RBTConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("RBTConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("RBTConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file" + e);
		}
	}

	/**
	 * extract the date from the header of the input file
	 * 
	 * @param inputStream -
	 *            the input file
	 * 
	 * @return the extracted data
	 * @exception ParseException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private Date readDate(BufferedReader inputStream) throws IOException,
			ParseException {
		inputStream.readLine();
		String heading = inputStream.readLine();
		inputStream.readLine();
		inputStream.readLine();
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy");
		String dateString = heading.substring(heading.lastIndexOf(" "), heading
				.length());
		Date date = frm.parse(dateString);
		return date;
	}

	/**
	 * update the time string to hold the date as well
	 * 
	 * @param timeString -
	 *            the time string to be updated
	 * @param date -
	 *            the date for updating the time string with
	 * 
	 * @return String - the updated date string
	 * @exception ParseException
	 *                if format of time string was invalid
	 */
	private String updateDateFormat(String timeString, Date date)
			throws ParseException {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm = new SimpleDateFormat();
		frm.applyPattern("HH:mm:ss");
		Date d = frm.parse(timeString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, c2.get(Calendar.SECOND));
		Date newDate = c.getTime();
		frm.applyPattern("MM/dd/yyyy HH:mm");
		String newDateString = frm.format(newDate);
		return newDateString;
	}

	public static void main(String ag[]) {
		try {
			// PropertyReader.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			RBTConverter s = new RBTConverter();
			// s.convert(new File("c:\\sar_150106"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}