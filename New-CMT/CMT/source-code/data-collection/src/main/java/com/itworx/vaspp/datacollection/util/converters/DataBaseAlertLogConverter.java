/*
 * File:       DataBaseAlertLogConverter.java
 * Date        Author          	Changes
 * 25/11/2007  Eshraq Essam  	Created
 *
 * Converter class for converting DataBase Alert Log input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class DataBaseAlertLogConverter extends AbstractTextConverter {
	private Logger logger;

	public DataBaseAlertLogConverter() {
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
		logger.debug("DataBaseAlertLogConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("DataBaseAlertLogConverter.convert() - converting file "+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String newLine 	= null;
				boolean lineFlag= false;
				boolean dateFlag= false;
				String date		= null;
				while (inputStream.ready()) {
					newLine = inputStream.readLine();
					
					if (newLine.trim().length() == 0) {
						continue;
					}
					
					// Cheak is that new line or not
					if(lineFlag==false) {
						dateFlag = isDate(newLine);
					}
					
					//	Cheak if the line contian date or not
					if (dateFlag==true) 
					{
						date 		= Utils.convertToDateString(Utils.convertToDate(newLine.trim(), "EEE MMM dd HH:mm:ss yyyy"), Utils.defaultFormat);
						lineFlag	= true;
						dateFlag	= false;
					}else
					{
						if (newLine.indexOf("ORA") == 0) 
						{
							String[]errorLine 	= newLine.split(":");
							String line			= date ;
							String errorDesc = "";
							String oraCode = "";
							for (int j = 0; j < errorLine.length; j++) {
								
								if (errorLine[j].trim().length()!=0) {
									oraCode		= errorLine[0].trim();
									if(j!=0)
									{
										if(errorDesc.equals(""))
											errorDesc	= errorLine[j].trim().replaceAll(",", ":");
										else
											errorDesc	= errorDesc + ":" + errorLine[j].trim().replaceAll(",", ":");
									}
										
								}
							}
							line = line + "," + oraCode + "," + errorDesc;
							outputStream.write(line);
							//System.out.println(line);
							outputStream.newLine();
							
						}else {
							lineFlag	= false;
							continue;
						}
					}
				}				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("DataBaseAlertLogConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
			logger.debug("DataBaseAlertLogConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("DataBaseAlertLogConverter.convert() - Input file not found "+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("DataBaseAlertLogConverter.convert() - Couldn't read input file"+ e);
			throw new ApplicationException(e);
		}
	}
	
	private boolean isDate(String line) {
		boolean isDateFlag = false;
		try {
			Utils.convertToDate(line.trim(),"EEE MMM dd HH:mm:ss yyyy");
			isDateFlag = true;
		} catch (InputException e) {
			return isDateFlag;
		}
		return isDateFlag;
	}
		
	
	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Source Code\\DataCollection");
			DataBaseAlertLogConverter s = new DataBaseAlertLogConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Docs\\Requirements\\DataCollection Files\\DB_Files\\alert_ETOPPRD2.log");
			s.convert(input, "DB");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}