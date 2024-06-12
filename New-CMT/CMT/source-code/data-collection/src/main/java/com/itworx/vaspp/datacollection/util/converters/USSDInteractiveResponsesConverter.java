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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class USSDInteractiveResponsesConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long > dateVsResponse=new HashMap<String,Long>() ;
	private Map mobileNumbers=new HashMap();
public USSDInteractiveResponsesConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("InsideUSSDInteractiveResponses convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());
	String responseRegex="^.+\\[op:32[;\\]].*$";
	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDInteractiveResponses.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("Request:")&&line.contains("[op:18]"))
				{
					//continue;
					mobileNumbers.put((line.split("\\[")[1]).split("\\]")[0].toString(),"Request");
					
				}
				else 
				{
					if(line.contains("Response:")&&line.matches(responseRegex))
					{  
						if(mobileNumbers.containsKey((line.split("\\[")[1]).split("\\]")[0].toString())){
							mobileNumbers.remove((line.split("\\[")[1]).split("\\]")[0].toString());
						try{
						String columns[]=line.split(" ");
						if(!(columns.length<6)){
						date=getDate(columns[0]+" "+columns[1]);
						String messahes[]=line.split("-868-");
						String respMessage=(messahes[1].split("\\[")[0]).split("Response:")[1];
						if(respMessage.contains(","))
							respMessage=respMessage.replace(",", ";");
						  boolean found =false;
						  String key=date+","+respMessage;
						if(dateVsResponse.containsKey(key))
						{
							Long count =dateVsResponse.get(key);
							dateVsResponse.remove(key);
							dateVsResponse.put(key, new Long(count.longValue()+1));
							
						}
						else
						{
							
							dateVsResponse.put(key,new Long (1));
						}
						}
						}
				
					catch(ParseException exc){ logger.error(exc) ; continue ;}
					}
					}
					
				}
			}
		}
		Iterator it =dateVsResponse.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			
				
				outputStream.write(key+","+((Long)dateVsResponse.get(key)).longValue());
				//System.out.println(key+","+((Long)dateVsResponse.get(key)).longValue());
				outputStream.newLine();
			
			
		     
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDInteractiveResponses.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDInteractiveResponses.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDInteractiveResponses.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDInteractiveResponses.convert() - finished converting input files successfully ");
		return outputFiles;
		}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
		USSDInteractiveResponsesConverter s = new USSDInteractiveResponsesConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\VASPortal\\DataCollection\\browser_868_2010071208.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.ms");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}

}
