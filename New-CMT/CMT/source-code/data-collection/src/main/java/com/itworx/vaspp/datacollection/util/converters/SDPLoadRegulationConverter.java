/*
 * File:       SDPLoadRegulationConverter.java
 * Date        Author          Changes
 * 12/07/2007  Eshraq Essam  Created
 *
 * Converter class for converting SDP Load Regulation log input file into comma spearated format
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

public class SDPLoadRegulationConverter extends AbstractTextConverter {
	private Logger logger;

	public SDPLoadRegulationConverter() {
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
				.debug("SDPLoadRegulationConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("SDPLoadRegulationConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				 
				int noOfCalls				= 0;
				int noOfRejected			= 0;
				float previousRejected		= -1;
				int counterOfOccurrences 	= 0;
				int maxCalls				= 0;
				int rejectedOfMaxCalls	= 0;
				String date;
				String timeOfMaxCalls		= null;
				Date previousDate			= null;
			
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					
					String newLine = null; 
					
					if (line.equals("") || line.indexOf("Calls")<0) 
					{
						continue;
					}
					
					date					= line.substring(line.indexOf("[")+1, line.lastIndexOf("]")).trim();
					Date dayDate			= this.updateDateFormat(date);
					String dayDateString 	= this.readTime(dayDate);
					// check if the date changed or not
					if(!isDateChanged(previousDate, dayDate))
					{
						counterOfOccurrences++;
						int newCalls			= Integer.parseInt((line.substring(line.lastIndexOf(":")+1)).trim());
						float newRejected		= Float.parseFloat((line.substring(line.indexOf("Rejected:")+9,line.indexOf("Calls:"))).trim())/10;
						noOfCalls 				= noOfCalls + newCalls;
						// get max calls per day
						maxCalls				= this.getMaxCalls(maxCalls, newCalls);
						//	check if max calls changed so get the rejected calls of  the max
						if(newCalls==maxCalls)
							rejectedOfMaxCalls	= Math.round(this.getRejectedOfMaxCalls(previousRejected, newRejected));
						// get the max calls time
						timeOfMaxCalls			= this.getMaxCallsDate(maxCalls, newCalls,timeOfMaxCalls, dayDateString);
												
						if(previousRejected != -1 && newRejected!=0)
						{
							noOfRejected = noOfRejected+(Math.round(newRejected)-Math.round(previousRejected));
						}
						
						previousRejected = newRejected;
						previousDate = dayDate;
						if(inputStream.ready()==false)
						{
							newLine 	= this.readDate(previousDate)+","+noOfCalls+","+noOfRejected+","+counterOfOccurrences+",";
							newLine 	= newLine+maxCalls+","+rejectedOfMaxCalls+","+timeOfMaxCalls;
							//System.out.println(newLine);
							//System.out.println(timeOfMaxCalls);
							outputStream.write(newLine);
							outputStream.newLine();
						}
					}else
					{
						newLine = this.readDate(previousDate)+","+noOfCalls+","+noOfRejected+","+counterOfOccurrences+",";
						newLine = newLine+maxCalls+","+rejectedOfMaxCalls+","+timeOfMaxCalls;
						counterOfOccurrences	= 0 + 1;
						int newCalls			= Integer.parseInt((line.substring(line.lastIndexOf(":")+1)).trim());
						noOfCalls				= 0 + newCalls;
						float newRejected		= Float.parseFloat((line.substring(line.indexOf("Rejected:")+9,line.indexOf("Calls:"))).trim())/10;
						previousRejected		= -1;
						noOfRejected 			= 0;

						if(newRejected!=0)
						{
							noOfRejected = noOfRejected+(Math.round(newRejected)-Math.round(previousRejected));
						}
						previousRejected = newRejected;
						maxCalls				= 0;
						maxCalls				= this.getMaxCalls(maxCalls, newCalls);
						if(newCalls==maxCalls)
							rejectedOfMaxCalls	= Math.round(this.getRejectedOfMaxCalls(previousRejected, newRejected));
						previousDate 			= dayDate;
						timeOfMaxCalls			= this.getMaxCallsDate(maxCalls, newCalls,timeOfMaxCalls, dayDateString);
						//System.out.println(newLine);
						//System.out.println(timeOfMaxCalls); 
						outputStream.write(newLine); 
						outputStream.newLine();
					}
					
				}
				outputStream.close();
			
				outputFiles[i] = output;
				logger.debug("SDPLoadRegulationConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("SDPLoadRegulationConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("SDPLoadRegulationConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("SDPLoadRegulationConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("SDPLoadRegulationConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file" + e);
		}
		
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
		frm.applyPattern("yyyyMMdd HH:mm:ss");
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
		frm.applyPattern("HH:mm:ss");
		String date 	= frm.format(sdpLoadDate);
		return date;
	}
	
	/**
	 * 
	 * @param sdpLoadDate
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private String readDate(Date sdpLoadDate) throws IOException,ParseException 
	{	
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy");
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
	private boolean isDateChanged(Date previousDate,Date newDate) throws IOException,ParseException 
	{	
		if(previousDate==null)
			return false;
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy");
		String preDate 	= frm.format(previousDate);
		String nDate 	= frm.format(newDate);
		if(preDate.equals(nDate))
			return false;
		else
			return true;
	}
	
	/**
	 * 
	 * @param oldCalls
	 * @param newCalls
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	private String getMaxCallsDate(int oldCalls, int newCalls, String oldDate, String newDate)
	{	
		
		if(newCalls >= oldCalls)
			return newDate;
		else
			return oldDate;
	}
	
	/**
	 * 
	 * @param oldCalls
	 * @param newCalls
	 * @return
	 */
	private int getMaxCalls(int oldCalls, int newCalls)
	{	
		if(newCalls>oldCalls)
			return newCalls;
		else
			return oldCalls;
	}
	
	/**
	 * 
	 * @param previousRejected
	 * @param newRejected
	 * @return
	 */
	private float getRejectedOfMaxCalls(float previousRejected, float newRejected)
	{	
		if(previousRejected==-1)
			return 0;
		else
			return newRejected - previousRejected;
	}
	

	public static void main(String ag[]) {
		try {
			
			String path = "D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\";
			PropertyReader.init(path);
			SDPLoadRegulationConverter s = new SDPLoadRegulationConverter();
			File[] input = new File[1];
			input[0]=new File(path+"cFSCInapContainer.log.0 SDP 41");
			s.convert(input,"SDP");
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}