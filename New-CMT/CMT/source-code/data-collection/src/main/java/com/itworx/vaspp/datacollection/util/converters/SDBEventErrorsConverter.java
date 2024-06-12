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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class SDBEventErrorsConverter extends AbstractTextConverter {
	private Logger logger;
	private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;
public SDBEventErrorsConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside SDBEventErrorsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("SDBEventErrorsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line == null||line.contains("****")||"".equals(line.trim()))
						continue;
				else
				{
					try{
						
					date= getDate(Utils.stringBetween(line, "[", "]"));
					
					String module=((line.split("Module:")[1]).split("/")[0]).trim();
					String event=((line.split("Event:")[1]).split("ID:")[0]).trim();
					String info=(line.split("Info:")[1]).trim();
					module.replaceAll(",", ";");
					event.replaceAll(",", ";");
					
					Long currentCount=Long.parseLong (((line.split("Count:")[1]).split(" "))[1]);
					if(info.contains("|"))
					{
						info=info.split("\\|")[0];
						
					}
					if(info.contains(","))
					{
						info=info.replace(",", ";");
					}
					String key=date+","+module+","+event+","+info;
					if(dateVsCount.containsKey(key))
					{
						Long count=dateVsCount.get(key);
						dateVsCount.remove(key);
						dateVsCount.put(key, count+currentCount);
					}
					else
					{
						dateVsCount.put(key, currentCount);
					}
					
					}
					catch(Exception exc)
					{ logger.error(exc) ;
					exc.printStackTrace();
					System.out.println("date "+line);
					System.out.println("Utils."+Utils.stringBetween(line, "[", "]"));
					continue ;}
				}
				}
			}
		
		Iterator it =dateVsCount.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			
				
				outputStream.write(key+","+((Long)dateVsCount.get(key)).longValue());
				//System.out.println(key+","+((Long)dateVsCount.get(key)).longValue());
				outputStream.newLine();
			
		     
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("AirEventErrorConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("AirEventErrorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("AirEventErrorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("SDBEventErrorsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		//PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
		PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
		SDBEventErrorsConverter s = new SDBEventErrorsConverter();
		File[] input = new File[1];
		//input[0]=new File("D:\\build\\VASPortal\\DataCollection\\EventLogFile.txt.0");
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\EventLogFile.txt.0");
		   s.convert(input,"Maha_Test");
		   System.out.println("test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
