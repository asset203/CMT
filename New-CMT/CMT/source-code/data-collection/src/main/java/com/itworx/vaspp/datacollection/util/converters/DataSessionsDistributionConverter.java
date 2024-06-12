package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class DataSessionsDistributionConverter extends AbstractTextConverter{
	
	private Logger logger;
	HashMap dataSessionsHashmap = new HashMap();
		
	class DataSessionsValues
	{
		public double avg2G;
		public double avg3G;
		public double avgMMS;
		public double avgInternet;
		public double avgWAP;
		public long count;
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("DataSessionsDistributionConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "DataSessionsDistributionConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "DataSessionsDistributionConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				while(inputStream.ready())
				{				
					line = inputStream.readLine();
					if(line == null || line == "" ){
						continue;
					}
					
					//System.out.println(line);
					UpdateValues(line);			
				}					
				inputStream.close();
			}
			WriteAll(outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("DataSessionsDistributionConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("DataSessionsDistributionConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("DataSessionsDistributionConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "DataSessionsDistributionConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Calculate the processing times for each hour
	 * 
	 * @param key
	 * @param data
	 * @throws ApplicationException 
	 * @throws InputException 
	 */
	public void UpdateValues(String line) throws ApplicationException, InputException
	{		
		String [] tokens = line.trim().split("[ ]+");
		
		if(tokens.length < 11 ){
			return;
		}
		
		String dateString = tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]+" "+tokens[5];
		//System.out.println(dateString);
		Date date = Utils.convertToDate(dateString, "E MMM dd HH:mm:ss yyyy");
		dateString = Utils.convertToDateString(date, "MM/dd/yyyy HH:00:00");
		//System.out.println(dateString);

		String avg2G = tokens[6];
		String avg3G = tokens[7];
		String avgMMS = tokens[8];
		String avgInternet = tokens[9]; 
		String avgWAP = tokens[10];
		
		//System.out.println(avg2G+" "+avg3G+" "+avgMMS+" "+avgInternet+" "+avgWAP);
		DataSessionsValues a= (DataSessionsValues)dataSessionsHashmap.get(dateString);
		
		if(a != null)
		{
			a.avg2G += Double.parseDouble(avg2G);
			a.avg3G += Double.parseDouble(avg3G);
			a.avgMMS += Double.parseDouble(avgMMS);
			a.avgInternet += Double.parseDouble(avgInternet);
			a.avgWAP += Double.parseDouble(avgWAP);
			a.count += 1;
			
			dataSessionsHashmap.put(dateString, a);
				
		}	else	{
			DataSessionsValues b = new DataSessionsValues();
			b.avg2G = Double.parseDouble(avg2G);
			b.avg3G = Double.parseDouble(avg3G);
			b.avgMMS = Double.parseDouble(avgMMS);
			b.avgInternet = Double.parseDouble(avgInternet);
			b.avgWAP = Double.parseDouble(avgWAP);
			b.count = 1;
			
			dataSessionsHashmap.put(dateString,b);			
		}
				
	}
	
	
	/**
	 * Extract the output from SMPP records Hashmap 
	 * and write it in the required format in the outputfile.
	 * 
	 * @param outputStream
	 */
	public void WriteAll(BufferedWriter outputStream)
	{
		Set keySet = dataSessionsHashmap.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{			
			for(int i=0;i<keyObject.length;i++)
			{
				DataSessionsValues value = (DataSessionsValues)dataSessionsHashmap.get(keyObject[i]);				
				String line = keyObject[i] +","+ value.avg2G / value.count +","+ value.avg3G / value.count +","+ value.avgMMS / value.count +","+ value.avgInternet / value.count +","+ value.avgWAP / value.count;
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();
			}
		}catch(IOException e)
		{
			System.out.println("error writing to file");
		}
	}
	
	/**
	 * format the date from "02/27/2008 11" to "02/27/2008 11:00:00",
	 *  to be the same format of date in hibarnate.
	 *  
	 * @param date
	 * @return
	 */
	public String FormatDateHour(String date)
	{
		date = date.substring(0,13);
		return date + ":00:00";
	}
	
	/**
	 * Format the date string from "27-02-08" to "02/27/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String FormatDate(String date)
	{
		return date.substring(3,5)+"/"+ date.substring(0,2)+"/"+"20"+date.substring(6,8);
	}
	
	/**
	 * Format the date string from "27/02/2008" to "02/27/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String FormatDate2(String date)
	{
		return date.substring(3,5)+"/" + date.substring(0,2) + "/" + date.substring(6,10);
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			DataSessionsDistributionConverter s = new DataSessionsDistributionConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\data_30_07.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
