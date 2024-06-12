/*
 * File:       BGWConverter.java
 * Date        Author          Changes
 * 07/03/2006  Nayera Mohamed  Created
 *
 * Converter class for converting BGW text input file into comma spearated format
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class BGWConverter extends AbstractTextConverter {
	private Logger logger;

	public BGWConverter() {
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
		logger
				.debug("BGWConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		try {
			String[] lines = new String[24];
			Arrays.fill(lines, "");
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("BGWConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				Date date = this.readDate(inputStream);
				this.skip(2, inputStream);
				this.readDataWithDate(inputStream, lines, date);
				this.skip(3, inputStream);
				this.readData(inputStream, lines);
				this.skip(3, inputStream);
				this.readData(inputStream, lines);
				this.skip(3, inputStream);
				this.readData(inputStream, lines);
				this.skip(3, inputStream);
				this.readData(inputStream, lines);
				this.skip(3, inputStream);
				this.readData(inputStream, lines);
				for (int j = 0; j < lines.length; j++) {
					if (lines[j] == null) {
						break;
					}
					outputStream.write(lines[j]);
					outputStream.newLine();
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("BGWConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("BGWConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("BGWConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("BGWConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("BGWConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file " + e);
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
		this.skip(1, inputStream);
		String heading = inputStream.readLine();
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy");
		String dateString = heading.substring(heading.lastIndexOf(" "), heading
				.length());
		Date date = frm.parse(dateString);
		return date;
	}

	private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
    }
	}

	/**
	 * extract data including the time string
	 * 
	 * @param inputStream -
	 *            the input file
	 * @param lines -
	 *            the arrays of lines to concatenate data
	 * @param date -
	 *            the date to use in data String
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private void readDataWithDate(BufferedReader inputStream, String[] lines,
			Date date) throws IOException, InputException {
		int i = 0;
		while (inputStream.ready()) {
			String line = inputStream.readLine();
			if (line.equals("")) {
				return;
      }
			StringTokenizer s = new StringTokenizer(line);
			String timeString = s.nextToken();
			try {
				lines[i] = lines[i] + this.updateDateFormat(timeString, date)
						+ ",";
			} catch (ParseException e) {
				throw new InputException("" + e);
			}
			while (s.hasMoreTokens()){
				lines[i] = lines[i] + s.nextToken() + ",";
      }
			i++;
		}
	}

	/**
	 * extract data execulding the time string
	 * 
	 * @param inputStream -
	 *            the input file
	 * @param lines -
	 *            the arrays of lines to concatenate data
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private void readData(BufferedReader inputStream, String[] lines)
			throws IOException {
		int i = 0;
		while (inputStream.ready()) {
			String line = inputStream.readLine();
			if (line.equals("")) {
				return;
      }
			StringTokenizer s = new StringTokenizer(line);
			s.nextToken();
			while (s.hasMoreTokens()) {
				lines[i] = lines[i] + s.nextToken() + ",";
      }
			i++;
		}
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
			BGWConverter s = new BGWConverter();
			// s.path = "c:\\converted";//
			File[] files = new File[1];
			files[0] = new File("c:\\bgw.txt");
			// s.convert(files);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}