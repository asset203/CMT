/*
 * File:       VaspRequestsConverter.java
 * Date        Author          Changes
 * 24/07/2007  Eman El Rouby  Created
 *
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


public class VaspRequestsConverter extends AbstractTextConverter {
	private Logger logger;

	public VaspRequestsConverter() {
	}

	
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		String path = PropertyReader.getConvertedFilesPath();
		logger
				.debug("VaspRequestsConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try
		{
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("VaspRequestsConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
			
				Date datetime; 
				String egyptStandardTime = null; 
				long requestsTotal = 0 ;
				long requestsSucceeded= 0 ;
				long requestsQueued = 0 ;
				
				int PrevHour=0; 
				int currHour=0; 
				
				String lineTokens []; 
				
				boolean firstLine = true; 
				
				String currLineTokens [] = new String[ 50 ];
				String PrevLineTokens [] = new String[ 50 ]; 
				String firstofHourTokens [] = new String[ 50 ];
				
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					lineTokens = line.split("\t"); 
					
					// skip the first line of file "columns' headers"
					if (firstLine == true)
						{
							line = inputStream.readLine(); 
							lineTokens = line.split("\t"); 
						}
					// skip empty lines 
					if (line.equals("")) 
						{
							continue;
						}
					
					egyptStandardTime = parseEgyptSTDTime(lineTokens); 
					datetime = updateDateFormat(egyptStandardTime); 
					
					// check if this is the first data line, current hour is the same as the previous , 
					// diffenet otherwise . firsthour of the day is assigned the first data line. 
					if (firstLine == true)
					{	
						currHour = PrevHour = getHour(egyptStandardTime); 
						firstofHourTokens = (String[])lineTokens.clone();   
						firstLine = false; 
					} 
					else 
					{ 
						PrevHour = currHour; 
						currHour = getHour(egyptStandardTime);
					}
					
					// if current hour is different than the previous hour, this indicates the end of a day's record.
					// We get the first hour of the day ,  Start to parse and calculate the data and pass to the 
					// outputstream then assign the first hour of the new day to the dataline. 
 					if (currHour != PrevHour)
					{
				
						PrevLineTokens = parseData(PrevLineTokens); 
						firstofHourTokens = parseData(firstofHourTokens); 
						 
						//egyptStandardTime = parseEgyptSTDTime(firstofHourTokens); 
						//datetime = updateDateFormat(egyptStandardTime); 
						String vaspDateTime = writeDate(updateDateFormat(PrevLineTokens[0]));
						
						requestsTotal  =Long.parseLong(PrevLineTokens[22])- Long.parseLong(firstofHourTokens[22]); 
						requestsSucceeded = Long.parseLong(PrevLineTokens[20])- Long.parseLong(firstofHourTokens[20]);
						/**
	 					 * for change request " requestsQueued = sum of all hours'values "
	 					 */
						//requestsQueued = Long.parseLong(PrevLineTokens[18])- Long.parseLong(firstofHourTokens[18]);
						/**
	 					 * end of change request
	 					 */
						
						firstofHourTokens = (String[])lineTokens.clone();
						
					//	System.out.println(vaspDateTime+","+requestsTotal+","+requestsSucceeded+","+requestsQueued);
						outputStream.write(vaspDateTime+","+requestsTotal+","+requestsSucceeded+","+requestsQueued);
						outputStream.newLine(); 
						/**
	 					 * for change request " requestsQueued = sum of all hours'values "
	 					 */
						requestsQueued = 0;
						/**
	 					 * end of change request
	 					 */

					}
 					/**
 					 * for change request " requestsQueued = sum of all hours'values "
 					 */
 					currLineTokens = parseData(lineTokens); 
 					requestsQueued += Long.parseLong(currLineTokens[18]);
 					/**
 					 * end of change request
 					 */
					PrevLineTokens = (String[])lineTokens.clone();
				} 
				
				// parse and calculate the last day data and pass to the outputstream
				
					PrevLineTokens = parseData(PrevLineTokens); 
					firstofHourTokens = parseData(firstofHourTokens); 
					
					//egyptStandardTime = parseEgyptSTDTime(firstofHourTokens); 
					//datetime = updateDateFormat(egyptStandardTime);
					
					String vaspDateTime = writeDate(updateDateFormat(PrevLineTokens[0]));
					
					requestsTotal  =Long.parseLong(PrevLineTokens[22])- Long.parseLong(firstofHourTokens[22]); 	
					requestsSucceeded = Long.parseLong(PrevLineTokens[20])- Long.parseLong(firstofHourTokens[20]);
					/**
 					 * for change request " requestsQueued = sum of all hours'values "
 					 */
					//requestsQueued = Long.parseLong(PrevLineTokens[18])- Long.parseLong(firstofHourTokens[18]);
					/**
 					 * for change request " requestsQueued = sum of all hours'values "
 					 */
					
				//	System.out.println(vaspDateTime+","+requestsTotal+","+requestsSucceeded+","+requestsQueued);
					outputStream.write(vaspDateTime+","+requestsTotal+","+requestsSucceeded+","+requestsQueued);
					outputStream.newLine();

				
				outputStream.flush();
				outputStream.close();
			
				outputFiles[i] = output;
				logger.debug("VaspRequestsConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("VaspRequestsConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("VaspRequestsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("VaspRequestsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} 

	}
	
	/**
	 * 
	 * @param Data : Any String array of data line tokens where each entry is surrounded by quotes. 
	 * @return : same String array but with quotes removed from each entry. 
	 */
	private String[] parseData(String[] Data)
	{
		String Data2 [] = new String[ Data.length ]; 
		for(int i=0; i<Data.length ; i++)
		{
			Data2[i] = Data[i].substring(1); 
			Data2[i] = Data2[i].substring(0,Data2[i].indexOf('"')); 
		}
		return Data2; 
	}
	
	/**
	 * 
	 * @param Data : String array of data line tokens. 
	 * @return String containing the time field. 
	 */
	private String parseEgyptSTDTime(String[] Data )
	{
		Date dateTime; 

		SimpleDateFormat frm = new SimpleDateFormat();
		String EST;
	

		frm.applyPattern("MM/dd/yyyy HH:mm:ss.SSS");
		
		EST = Data[0]; 
		EST = EST.substring(1,EST.indexOf(".")); 
		
		try {
		
			dateTime = frm.parse(EST);
			EST = frm.format(dateTime); 
	 
		} 
		catch (Exception e)
		{
			
		}
		return EST; 
	}
	
	/**
	 * 
	 * @param Date : String Representing a date
	 * @return int : the hour of that date
	 */
	private int getHour(String Date)
	{
		int hour; 
		Date currentDate = null;
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH");
		try 
		{
			currentDate = frm.parse(Date); 
		} catch (Exception e)
		{
			
		}
		//System.out.println(currentDate.getHours()); 
		hour = currentDate.getHours(); 
		return hour; 
	}
	/**
	 * 
	 * @param day : the Date to be converted
	 * @return String : the Date String representation
	 */
	private String writeDate(Date day)
	{
		String date = null; 
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:00");
		try
		{
			date = frm.format(day); 
		}	catch (Exception e)
		{
			
		}
		return date; 
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
		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
		try {
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			throw new InputException("invalid date in input file ");
		}
	}
	// for testing
	public static void main(String ag[]) {
		/*try {
			PropertyReader
				.init("D:/sayed/WorkSpace/VAS Performance Portal/DataCollection/");
			VaspRequestsConverter s = new VaspRequestsConverter();
			File[] input = new File[1];
			input[0] = new File("D:/Eshraq/12-12-2007/oct/fout.12-10");
			s.convert(input,"VASP");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			VaspRequestsConverter s = new VaspRequestsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 19-07-2007.tsv");
			//input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 20-07-2007.tsv");
			//input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\VASP 21-07-2007.tsv");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}