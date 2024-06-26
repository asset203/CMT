package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.SMPPReceiverConverter.SMPPValues;

public class TopHandsetsConverter extends AbstractTextConverter{
	
	private Logger logger;
	HashMap msisdnsHashmap = new HashMap();

	class SMPPValues
	{
		public long msisdns;
		public long messages;
		public String shortCode;
		public String application;
		public String dateString;
		public HashMap distincitMsisdn;
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("TopHandsetsConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "TopHandsetsConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
			String dateString ="";
			String outputLine = "";
			String fileName;
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "TopHandsetsConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				fileName = inputFiles[i].getName();
				
				// extract the date from file name "topHandSets1_07_27_08.log"
				dateString = fileName.substring(fileName.length()- 12 , fileName.indexOf(".log"));
				dateString = FormatDate(dateString);
				
				while(inputStream.ready())
				{				
					line = inputStream.readLine();
					if(line == null || line == "" ){
						continue;
					}
					
					// extract the data from " 937 Nokia3250"
					String[] tokens = line.trim().split(" ");
					// write the output
					if(tokens != null && tokens.length == 2)
					{
						outputLine = dateString+","+tokens[1]+","+tokens[0];
						//System.out.println(outputLine);
						outputStream.write(outputLine);
						outputStream.newLine();
					}
					
				}
				inputStream.close();
			}
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("TopHandsetsConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("TopHandsetsConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("TopHandsetsConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "TopHandsetsConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Format the date string from "07_16_08" to "07/16/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String FormatDate(String date)
	{
		date = date.replaceAll("_", "/");
		return date.substring(0,6)+"20" + date.substring(6,8);
	}

	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\Basem\\Deployment - 15_1_2009\\Production Package\\Source Code\\DataCollection");
			TopHandsetsConverter s = new TopHandsetsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Basem\\Deployment - 15_1_2009\\Production Package\\Source Code\\DataCollection\\resources\\ftpfolder\\topHandSets1_03_10_09.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
