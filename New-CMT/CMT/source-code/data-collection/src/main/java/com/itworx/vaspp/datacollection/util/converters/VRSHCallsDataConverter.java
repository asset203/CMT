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
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
public class VRSHCallsDataConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String , Integer> numOfCalls = new HashMap<String, Integer>() ;
	private Map<String,String> mobileVSService=new HashMap<String ,String>();
public VRSHCallsDataConverter()
{}

public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger.debug("VRSHCallsDataConverter.convert() - started converting input files ");
	try {
		String dateLine="";
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());
	BufferedWriter outputStream = new BufferedWriter(new FileWriter(
			outputFile));
	BufferedReader inputStream = null;
	for (int i = 0; i < inputFiles.length; i++) 
	{
		logger.debug("VRSHCallsDataConverter.convert() - converting file "
				+ inputFiles[i].getName());
		inputStream = new BufferedReader(new FileReader(inputFiles[i]));
		String line;
		String date="";
		while (inputStream.ready()) {					
			line = inputStream.readLine();
			if (line.trim().equals("")) {
				continue;
			}
			
			else 
			{
				
				String[] tokens= line.split(",");
				
				{
					try
				    {
					  date = getDate(line);
					}
					catch(ParseException exc) { logger.error(exc) ; continue ;}
					
					
					if(tokens[7].contains("hotline")||tokens[7].contains("VRS"))
						
					{   
						String serviceType=tokens[7];
						if(serviceType.contains("hotline"))
							serviceType="hotline";
						else if(serviceType.contains("VRS"))
							serviceType="VRS";
						if(tokens[4].contains("0900")){
						mobileVSService.put(tokens[6],date+","+serviceType);}
						
					}
					else{
						if(!tokens[4].contains("0900"))
					      {
							  if(mobileVSService.containsKey(tokens[6]))
							  {         
								  if(mobileVSService.get(tokens[6]).split(",")[0].equalsIgnoreCase(date)){
									 
								  String key=mobileVSService.get(tokens[6])+","+tokens[5];
								  mobileVSService.remove(tokens[6]);
									if(numOfCalls.containsKey(key))
									{
										Integer count=numOfCalls.get(key);
										count++;
										numOfCalls.remove(key);
										numOfCalls.put(key, count);
									}
									else
									{
									numOfCalls.put(key, new Integer(1));
									}
									}
							  }
						
					}}
					
				//else continue;
				}
			}
		
		}
	}
	Iterator myVeryOwnIterator =numOfCalls.keySet().iterator();
	while(myVeryOwnIterator.hasNext()){ 					
		Object key = myVeryOwnIterator.next();
		//long requestNumbers = numOfCalls.get(key);
		logger.debug("write in the converter");
			outputStream.write(key+","+numOfCalls.get(key));
			outputStream.newLine();
	}
	inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDNetworkInitiatedConverter.convert() - finished converting input files successfully ");
	return outputFiles;
	
	}catch (FileNotFoundException e) {
		logger.error("VRSHCallsDataConverter.convert() - Input file not found " + e);
		new ApplicationException("" + e);
	} catch (IOException e) {
		logger.error("VRSHCallsDataConverter.convert() - Couldn't read input file"
				+ e);
		new ApplicationException("" + e);
	}
	return null;
	
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	if (line != null)
		tokens = line.split(",");
		date = inDateFormat.parse(tokens[0]+tokens[1]);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\SourceCode\\DataCollection");
		VRSHCallsDataConverter s = new VRSHCallsDataConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\SourceCode\\DataCollection\\2010040709.cdr");
		s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
