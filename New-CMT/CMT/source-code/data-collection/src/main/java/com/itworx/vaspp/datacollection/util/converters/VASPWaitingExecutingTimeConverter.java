/*
 * File:       VASPWaitingExecutingTimeConverter.java
 * Date        Author          Changes
 * 24/07/2007  Eshraq Essam  Created
 *
 * Converter class for converting VASP Waiting Executing Time text input file into tab spearated format
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

public class VASPWaitingExecutingTimeConverter extends AbstractTextConverter {
	private Logger logger;

	public VASPWaitingExecutingTimeConverter() {
	}
	
	private class WaitingExecutingValues
	{
		public double maxWaitingTime;
		public double minWaitingTime;
		public double avgWaitingTime;
		public double maxExecutingTime;
		public double minExecutingTime;
		public double avgExecutingTime;
		public long count;
		
		private void initializeWaitingExecutionValues(double newExecutionTime, double newWaitingTime)
		{
			this.maxExecutingTime = this.minExecutingTime = this.avgExecutingTime = newExecutionTime;
			this.maxWaitingTime = this.minWaitingTime = this.avgWaitingTime = newWaitingTime;
			this.count = 1;
		}
		
		private void updateWaitingExecutionValues(double newExecutionTime, double newWaitingTime)
		{	
			if(this.maxExecutingTime < newExecutionTime)
				this.maxExecutingTime = newExecutionTime;
			
			if(this.minExecutingTime > newExecutionTime)
				this.minExecutingTime = newExecutionTime;
			
			this.avgExecutingTime += newExecutionTime;
			
			if(this.maxWaitingTime < newWaitingTime)
				this.maxWaitingTime = newWaitingTime;
			
			if(this.minWaitingTime > newWaitingTime)
				this.minWaitingTime = newWaitingTime;
			
			this.avgWaitingTime += newWaitingTime;
			
			this.count++;
			
		}

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
				.debug("VASPWaitingExecutingTimeConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("VASPWaitingExecutingTimeConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));

				Date previousDate			= null;
				boolean firstTime = true;
				WaitingExecutingValues waitingExecutingValues = new WaitingExecutingValues();
				
				inputStream.readLine();

				String newLine = null; 
				String[] lineTokens =null;
				Date dayDate	= new Date();
				
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					
					if (line.equals("")) 
					{
						continue;
					}
					lineTokens 	= line.split("\t");
					String date				= lineTokens[0].trim().substring(1, lineTokens[0].trim().lastIndexOf("\""));
					dayDate			= this.updateDateFormat(date);
					
					// check if the Hour changed or not
					if(!isHourChanged(previousDate, dayDate))
					{
						long requestExecutionTime	= Long.parseLong(lineTokens[11].trim().substring(1, lineTokens[11].trim().lastIndexOf("\"")));
						long requestWaitTime		= Long.parseLong(lineTokens[12].trim().substring(1, lineTokens[12].trim().lastIndexOf("\"")));
						
						if(firstTime){
							waitingExecutingValues.initializeWaitingExecutionValues(requestExecutionTime, requestWaitTime);
							firstTime = false;
						}else{
							waitingExecutingValues.updateWaitingExecutionValues(requestExecutionTime, requestWaitTime);	
						}											
						previousDate				= dayDate;
						
						if(inputStream.ready()==false)
						{
							//	parse and calculate the last day data and pass to the outputstream
							newLine 	= this.readTime(previousDate)+","+waitingExecutingValues.maxExecutingTime+","+
							waitingExecutingValues.minExecutingTime+","+
							waitingExecutingValues.avgExecutingTime / waitingExecutingValues.count+","+
							waitingExecutingValues.maxWaitingTime+","+
							waitingExecutingValues.minWaitingTime+","+
							waitingExecutingValues.avgWaitingTime / waitingExecutingValues.count;
							//System.out.println(newLine);
							outputStream.write(newLine);
							outputStream.newLine();
						}
					}else
					{
						newLine 	= this.readTime(previousDate)+","+waitingExecutingValues.maxExecutingTime+","+
						waitingExecutingValues.minExecutingTime+","+
						waitingExecutingValues.avgExecutingTime / waitingExecutingValues.count+","+
						waitingExecutingValues.maxWaitingTime+","+
						waitingExecutingValues.minWaitingTime+","+
						waitingExecutingValues.avgWaitingTime / waitingExecutingValues.count;						
						//System.out.println(newLine);
						outputStream.write(newLine); 
						outputStream.newLine();
						
						long requestExecutionTime	= Long.parseLong(lineTokens[11].trim().substring(1, lineTokens[11].trim().lastIndexOf("\"")));
						long requestWaitTime		= Long.parseLong(lineTokens[12].trim().substring(1, lineTokens[12].trim().lastIndexOf("\"")));
						waitingExecutingValues = new WaitingExecutingValues();
						
						waitingExecutingValues.initializeWaitingExecutionValues(requestExecutionTime, requestWaitTime);
						previousDate				= dayDate;

					}
					
				}
				outputStream.close();
			
				outputFiles[i] = output;
				logger.debug("VASPWaitingExecutingTimeConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			
		} catch (FileNotFoundException e) {
			logger.error("VASPWaitingExecutingTimeConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("VASPWaitingExecutingTimeConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("VASPWaitingExecutingTimeConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file" + e);
		}
		
		logger.debug("VASPWaitingExecutingTimeConverter.convert() - finished converting input files successfully ");
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
	private Date updateDateFormat(String dateString) throws InputException {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm:ss.SSS");
		try {
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			throw new InputException("invalid date in input file ");
		}
	}
	
	/**
	 * extract the date from the header of the input file
	 * 
	 * @param rbtUsageDate -
	 *            the date to be converted
	 * 
	 * @return String - the date string converted to standard format
	 */
	private String readTime(Date sdpLoadDate) throws IOException,ParseException 
	{	
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:00");
		String date 	= frm.format(sdpLoadDate);
		return date;
	}
	
	/**
	 * 
	 * @param previousDate
	 * @param newDate
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private boolean isHourChanged(Date previousDate,Date newDate) throws IOException,ParseException 
	{	
		if(previousDate==null)
			return false;
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH");
		String preDate 	= frm.format(previousDate);
		String nDate 	= frm.format(newDate);
		if(preDate.equals(nDate))
			return false;
		else
			return true;
	}


	public static void main(String ag[]) {
		
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			VASPWaitingExecutingTimeConverter s = new VASPWaitingExecutingTimeConverter();
			File[] input = new File[3];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 19-07-2007.tsv");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 20-07-2007.tsv");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 21-07-2007.tsv");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}