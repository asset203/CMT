/*
 * File:       PerformanceSGAFreePoolConverter.java
 * Date        Author          	Changes
 * 21/11/2007  Eshraq Essam  	Created
 *
 * Converter class for converting Performance SGA Free Pool text input file into comma spearated format
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
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class PerformanceSGAFreePoolConverter extends AbstractTextConverter {
	private Logger logger;

	public PerformanceSGAFreePoolConverter() {
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
		logger.debug("PerformanceSGAFreePoolConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("PerformanceSGAFreePoolConverter.convert() - converting file "+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String yesterdaysDate = Utils.getYesterdaysDate();
				String newLine = null;
				while (inputStream.ready()) {
					newLine = inputStream.readLine();
					
					if (newLine.trim().length() == 0
							|| newLine.trim().indexOf("-") == 0
							|| newLine.trim().indexOf("Grand Total") == 0
							|| newLine.trim().indexOf("Pool") == 0
							|| newLine.trim().indexOf("SQL> spool off") == 0) {
							continue;
						}
					
					//	check if no data 
					if (newLine.trim().indexOf("no rows selected") == 0) {
						outputStream.close();
						inputStream.close();
						outputFiles[i] = output;
						return outputFiles;
					}
				
					// check if file contian SQL or not
					if(newLine.trim().indexOf("SQL>") == 0)
					{
						DatabaseUtils.skipSqlLines(inputStream);
						continue;
					}
					
					String pool			= newLine.substring(0, newLine.indexOf("free memory")).trim();
					String name			= "free memory";
					String poolInfo		= newLine.substring(newLine.lastIndexOf("free memory")+name.length()).trim();
					poolInfo			= poolInfo.replaceAll(",", "");
					String poolSizes[] 	= poolInfo.split(" ");
					String line 		= yesterdaysDate + "," + pool + "," + name ;
					for (int j = 0; j < poolSizes.length; j++) {
						if (poolSizes[j].trim().length()!=0) {
							line = line + "," + poolSizes[j].trim();
						}
					}
					outputStream.write(line);
					outputStream.newLine();
				}				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("PerformanceSGAFreePoolConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
			logger.debug("PerformanceSGAFreePoolConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("PerformanceSGAFreePoolConverter.convert() - Input file not found "+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("PerformanceSGAFreePoolConverter.convert() - Couldn't read input file"+ e);
			throw new ApplicationException(e);
		}
	}
	
	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Source Code\\DataCollection");
			PerformanceSGAFreePoolConverter s = new PerformanceSGAFreePoolConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Docs\\Requirements\\DataCollection Files\\DB_Files\\perf_sga_free_pool.res");
			s.convert(input, "DB");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}