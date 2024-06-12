/*
 * File:       RBTUsageAirConverter.java
 * Date        Author          Changes
 * 18/03/2007  Eshraq Essam  Created
 *
 * Converter class for converting RBT Usage text input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

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

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

/*
 * The structure of the text input file is
 * 
 * Line 1 : empty Line 2 : file header Line 3 : empty Line 4 : column headers
 * Line 5 : spaces separated data Repeated Line 5
 * 
 */

public class RBTUsageInfoConverter extends AbstractTextConverter {
	private Logger logger;

	public RBTUsageInfoConverter() {
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
				.debug("RBTUsageAirConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("RBTUsageAirConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String date = this.readDate(inputStream);
				outputStream.write(date);
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					if (line.equals("") || line.indexOf(":")<0) 
					{
						break;
					}
					outputStream.write(",");
					String info = line.substring(line.lastIndexOf(":")+1, line.length()).trim();
					outputStream.write(info);
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("RBTUsageAirConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("RBTUsageAirConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("RBTUsageAirConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("RBTUsageAirConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("RBTUsageAirConverter.convert() - error parsing date" + e);
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
	private String readDate(BufferedReader inputStream) throws IOException,ParseException 
	{	
		String statisticsTime 	= inputStream.readLine();
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("HH dd-MM-yyyy");
		String dateString 		= statisticsTime.substring(statisticsTime.lastIndexOf(":")+1, statisticsTime.length());
		dateString 				= dateString.trim();
		Date date 				= frm.parse(dateString);
		String statisticsDate 	= frm.format(date);
		return statisticsDate;
	}


	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal\\Phase II\\Source Code\\DataCollection");
			RBTUsageInfoConverter s = new RBTUsageInfoConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Usage.2007031309RBT");
			s.convert(input,"RBT Usage");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}