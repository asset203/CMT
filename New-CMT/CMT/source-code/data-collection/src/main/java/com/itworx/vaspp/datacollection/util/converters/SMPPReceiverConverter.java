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

public class SMPPReceiverConverter extends AbstractTextConverter{
	
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
		logger.debug("SMPPReceiverConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "SMPPReceiverConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "SMPPReceiverConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				String dummy="";
				if(inputStream.ready())
					dummy = inputStream.readLine();
				while((inputStream.ready()) || (dummy!="" && dummy!=null))
				{				
					line = "";
					do
					{			
						line = line + dummy;		
						dummy = inputStream.readLine();

					}while(inputStream.ready()&&(dummy.length()<=23||!isdate(dummy.substring(0,23))));
					
					line.trim();
					
					if(line.length()>=23 && isdate(line.substring(0,23)))
					{
						UpdateValues(line);
					}
				}
				inputStream.close();
			}
			
			WriteAll(outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("SMPPReceiverConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("SMPPReceiverConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("SMPPReceiverConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "SMPPReceiverConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Calculate the processing times for each hour
	 * 
	 * @param key
	 * @param data
	 * @throws ApplicationException 
	 */
	public void UpdateValues(String line) throws ApplicationException
	{		
		String applicationId = line.substring(line.indexOf("AppID ||")+8,line.indexOf("|| ShortCode")).trim();
		String shortCode = line.substring(line.indexOf("ShortCode ||")+12,line.indexOf("|| MSISDN")).trim();
		String msisdn = line.substring(line.indexOf("MSISDN ||")+9,line.indexOf("|| ShortMessage")).trim();
		String dateString = FormatDate(line.substring(0, 13));
		String key = dateString +"*"+ applicationId +"*"+ shortCode;
		String msisdnKey = dateString +"*"+ applicationId +"*"+ shortCode + "*" + msisdn;
		//System.out.println(dateString+" "+applicationId + " "+shortCode +" "+ msisdn);
		SMPPValues a= (SMPPValues)msisdnsHashmap.get(key);
		
		if(a != null)
		{
			a.messages += 1;
			String storedMsisdn = (String) a.distincitMsisdn.get(msisdnKey);
			if(storedMsisdn == null){
				a.msisdns += 1;
				a.distincitMsisdn.put(msisdnKey, msisdn);
			}
			
			msisdnsHashmap.put(key, a);
				
		}	else	{
			SMPPValues b = new SMPPValues();
			b.msisdns = 1;
			b.messages = 1;
			b.application = applicationId;
			b.shortCode = shortCode;
			b.dateString = dateString;
			b.distincitMsisdn = new HashMap();
			b.distincitMsisdn.put(msisdnKey, msisdn);
			msisdnsHashmap.put(key,b);			
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
		Set keySet = msisdnsHashmap.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{			
			for(int i=0;i<keyObject.length;i++)
			{
				SMPPValues value = (SMPPValues)msisdnsHashmap.get(keyObject[i]);				
				String line = value.dateString +","+ value.application +","+ value.shortCode +","+ value.msisdns +","+ value.messages;
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
	 * Format the date string from "27/02/2008 11" to "02/27/2008 11:00:00".
	 * 
	 * @param date
	 * @return String
	 */
	public String FormatDate(String date)
	{
		return date.substring(3,5)+"/" + date.substring(0,2) + "/" + date.substring(6,13)+ ":00:00";
	}
	
	/**
	 * Check if that is new line or not.
	 * 
	 * @param line
	 * @return boolean
	 */
	public boolean isdate(String line)
	{
		//The checking format is "27/02/2008 11:00:00.403"
		return line.matches("\\d\\d\\p{Punct}\\d\\d\\p{Punct}\\d\\d\\d\\d\\p{Space}\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d");
	}

	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			SMPPReceiverConverter s = new SMPPReceiverConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-07-22-08.log");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-07-22-09.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
