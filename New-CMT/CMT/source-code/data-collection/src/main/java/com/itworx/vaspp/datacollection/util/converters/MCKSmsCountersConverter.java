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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class MCKSmsCountersConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,String> IdVsDate=new HashMap<String,String>() ;
	private Map  <String ,Long> keyVsCount=new HashMap<String,Long>() ;
public MCKSmsCountersConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside MCKSmsCountersConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("MCKSmsCountersConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
        
			String line;
			String date = "";
			String transId="";
			String msgType="";
			String sourceAdd="";
			String msgInfo="";
			String key="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("transactionID:")&&line.contains("Taking the message from sender queue.")&&line.contains("]")&&line.contains("["))
				{
					date=line.split("\\[")[1].split("\\]")[0];
					
					transId=line.split("transactionID:")[1].trim();
					try{
					date=getDate(getCurrentYear()+date);	
					if(!IdVsDate.containsKey(transId))
					{
						IdVsDate.put(transId, date);
					}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
					
				}
				else if(line.contains("SMPP_MESSAGE_HEAD:")&& line.contains("sequenceID:"))
				{
					String messageId=line.split("sequenceID:")[1].split("}")[0];
					if(IdVsDate.containsKey(messageId))
					{
						String respDate=IdVsDate.get(messageId);
						IdVsDate.remove(messageId);
						line = inputStream.readLine();
						if(line.contains("SMPP_SUBMIT_SM_MESSAGE_BODY")||line.contains("SMPP_REPLACE_SM_MESSAGE_BODY")||line.contains("SMPP_DELIVER_SM_MESSAGE_BODY"))
						{
							if(line.contains("SMPP_SUBMIT_SM_MESSAGE_BODY"))
							msgType="SUBMIT";
							else if (line.contains("SMPP_REPLACE_SM_MESSAGE_BODY"))
							msgType="REPLACE";
							else if (line.contains("SMPP_DELIVER_SM_MESSAGE_BODY"))
								msgType="DELIVER";
							while (inputStream.ready()&&!line.contains("source_addr:")&&!line.contains("source_addr :")&&!line.contains("}")) {
								
								line=inputStream.readLine();								
							}
							if(line.contains("source_addr:")||line.contains("source_addr :"))
							{
								sourceAdd=line.split(":")[1].trim();
								if(!(sourceAdd.equalsIgnoreCase("777")||sourceAdd.equalsIgnoreCase("9771")||sourceAdd.equalsIgnoreCase("9772")||sourceAdd.equalsIgnoreCase("9773")||sourceAdd.equalsIgnoreCase("9774")))
									sourceAdd="Other";	
								if((sourceAdd.equalsIgnoreCase("9772")||sourceAdd.equalsIgnoreCase("9773")||sourceAdd.equalsIgnoreCase("9774"))&&msgType.equalsIgnoreCase("SUBMIT"))
									msgInfo=" ";
								if((sourceAdd.equalsIgnoreCase("9771")||sourceAdd.equalsIgnoreCase("777")||sourceAdd.equalsIgnoreCase("9772")||sourceAdd.equalsIgnoreCase("9773")||sourceAdd.equalsIgnoreCase("9774"))&&msgType.equalsIgnoreCase("REPLACE"))										
									     msgInfo="Notification";
									    
								
								while (inputStream.ready()&&!line.contains("short_messageString:")&&!line.contains("short_message:")&&!line.contains("}")) {
									line=inputStream.readLine();								
								}
								if(line.contains("short_messageString:"))
								{
									
									if(line.contains("Thank you for activating MCK."))
									{
										if((sourceAdd.equalsIgnoreCase("9771")||sourceAdd.equalsIgnoreCase("777"))&&msgType.equalsIgnoreCase("SUBMIT"))
											msgInfo="New Activation";
										
									}
									else if (line.contains("You can call the number again."))
									{
										if(sourceAdd.equalsIgnoreCase("Other"))
											msgInfo="Back To Air";
									}
									else
									{
										if((sourceAdd.equalsIgnoreCase("9771")||sourceAdd.equalsIgnoreCase("777"))&&msgType.equalsIgnoreCase("SUBMIT"))
											
											msgInfo="Notification";
											
										else if(sourceAdd.equalsIgnoreCase("Other")&&(!line.contains("Vodafone Notification:")))
											msgInfo=" ";
									}
									
								}
								else if (line.contains("short_message:"))
								{
									line=inputStream.readLine();
									if (line.contains("You can call the number again."))
									{
										if(sourceAdd.equalsIgnoreCase("Other"))
											msgInfo="Back To Air";
									}else 
									{
										if(sourceAdd.equalsIgnoreCase("Other")&&(!line.contains("Vodafone Notification:")))
											msgInfo=" ";
									}
								}
								
								key=respDate+","+sourceAdd+","+msgType+","+msgInfo;
								if(keyVsCount.containsKey(key))
								{
									long count =keyVsCount.get(key);
									keyVsCount.put(key, count+1);
								}else
								{
									keyVsCount.put(key, new Long(1));
								}	
							}
							
						}
						
						
					}
				}
			}
		}
		Iterator it=keyVsCount.keySet().iterator();
		while(it.hasNext())
		{
			Object key =it.next();
			outputStream.write(key+","+keyVsCount.get(key));
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("MCKSmsCountersConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("MCKSmsCountersConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("MCKSmsCountersConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("MCKSmsCountersConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getCurrentYear() throws ParseException {
	String dateString;	
	SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy");		
		Calendar c= Calendar.getInstance() ;		
	    dateString = outDateFormat.format(c.getTime());
	    return dateString;

}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMM dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		MCKSmsCountersConverter s = new MCKSmsCountersConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\UniPortalCOMM.txt.7");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
