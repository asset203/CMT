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

public class UniqueMSISDNsConverter extends AbstractTextConverter{
	
	private Logger logger;
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("UniqueMSISDNsConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "UniqueMSISDNsConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
			String dateString ="";
			String outputLine = "";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "UniqueMSISDNsConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				while(inputStream.ready())
				{				
					line = inputStream.readLine();
					if(line == null || line == "" ){
						continue;
					}
					
					// extract the data from "71863 /export/yghallab/logs/MSISDN_List_node1_07_08_08.log"
					String[] tokens = line.trim().split(" ");
					dateString = tokens[1].substring(tokens[1].length()-12,tokens[1].indexOf(".log"));
					// write the output
					outputLine = FormatDate(dateString)+","+tokens[0];
					//System.out.println(outputLine);
					outputStream.write(outputLine);
					outputStream.newLine();
					
				}
				inputStream.close();
			}			
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("UniqueMSISDNsConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("UniqueMSISDNsConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("UniqueMSISDNsConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "UniqueMSISDNsConverter.convert() - finished converting input files Successfully Converted");
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
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			UniqueMSISDNsConverter s = new UniqueMSISDNsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\NumberOfUniqMsisdn.log");
			s.convert(input,"MSP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
