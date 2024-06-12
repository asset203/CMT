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
import com.itworx.vaspp.datacollection.util.converters.DataSessionsDistributionConverter.DataSessionsValues;

public class CPUMemoryUtilizationConverter extends AbstractTextConverter{
	
	private Logger logger;
	
	HashMap cpuMemoryHashmap = new HashMap();
	
	class CpuMemoryValues
	{
		public String dateString;
		public double cpuUtil;
		public double memoryUtil;
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("CPUMemoryUtilizationConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "CPUMemoryUtilizationConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "CPUMemoryUtilizationConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				while(inputStream.ready())
				{				
					line = inputStream.readLine();
					if(line == null || line == "" ){
						continue;
					}
					
					UpdateValues(line);
				}
				inputStream.close();
			}			
			WriteAll(outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CPUMemoryUtilizationConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("CPUMemoryUtilizationConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("CPUMemoryUtilizationConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "CPUMemoryUtilizationConverter.convert() - finished converting input files Successfully Converted");
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
		String[] tokens = line.trim().split(",");
		String cpuUtil = tokens[2];
		String memoryUtil = tokens[4];
		
		
		String timeCode = "EET";

		
		if(tokens[0].indexOf("EEST") != -1)
			timeCode = "EEST";
		
//		String dateString = (tokens[0].trim()).substring(0, tokens[0].indexOf("EEST")) + tokens[0].substring(tokens[0].indexOf("EEST")+5, tokens[0].indexOf("EEST")+9).trim();
		String dateString = (tokens[0].trim()).substring(0, tokens[0].indexOf(timeCode)) + tokens[0].substring(tokens[0].indexOf(timeCode)+timeCode.length()+1, tokens[0].indexOf(timeCode)+timeCode.length()+5).trim();
		
		
		Date date = Utils.convertToDate(dateString, "E MMM dd HH:mm:ss yyyy");
		dateString = Utils.convertToDateString(date, "MM/dd/yyyy HH:00:00");
		//System.out.println(dateString);
		CpuMemoryValues a= (CpuMemoryValues)cpuMemoryHashmap.get(dateString);
		
		if(a != null)
		{
			if(a.cpuUtil < Double.parseDouble(cpuUtil))
			{
				a.cpuUtil = Double.parseDouble(cpuUtil);
			}
			if(a.memoryUtil < Double.parseDouble(memoryUtil))
			{
				a.memoryUtil = Double.parseDouble(memoryUtil);
			}
			cpuMemoryHashmap.put(dateString, a);
				
		}	else	{
			CpuMemoryValues b = new CpuMemoryValues();
			b.cpuUtil = Double.parseDouble(cpuUtil);
			b.memoryUtil = Double.parseDouble(memoryUtil);
			
			cpuMemoryHashmap.put(dateString,b);			
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
		Set keySet = cpuMemoryHashmap.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{			
			for(int i=0;i<keyObject.length;i++)
			{
				CpuMemoryValues value = (CpuMemoryValues)cpuMemoryHashmap.get(keyObject[i]);				
				String line = keyObject[i] +","+ value.cpuUtil +","+ value.memoryUtil;
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();
			}
		}catch(IOException e)
		{
			System.out.println("error writing to file");
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\VASPortalWF\\Source Code\\DataCollection");
			CPUMemoryUtilizationConverter s = new CPUMemoryUtilizationConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\cpu_090311");
			s.convert(input,"MSP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
