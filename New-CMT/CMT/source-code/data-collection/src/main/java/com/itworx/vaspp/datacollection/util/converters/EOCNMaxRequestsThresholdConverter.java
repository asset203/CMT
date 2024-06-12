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

public class EOCNMaxRequestsThresholdConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateVSRequests=new HashMap<String,Long>() ;
public EOCNMaxRequestsThresholdConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside EOCNMaxRequestsThresholdConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("EOCNMaxRequestsThresholdConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if (line.contains("MSG:")) {
					if (line.contains("New Network Initiated Dialogue"))
					{
						String columns []=line.split(",");
						
						try{
						date =getDate(columns[0]);
						if(dateVSRequests.containsKey(date))
						{
							Long reqCount=dateVSRequests.get(date);
							dateVSRequests.remove(date);
							dateVSRequests.put(date , reqCount+1);
						}else
						{
							dateVSRequests.put(date , new Long(1));
						}
						//System.out.println("date"+date);
						}  catch(ParseException exc){ logger.error(exc) ; continue ;}
					}
					
				}
				else
				{
					continue;
				}
				}
			}
		String date ;
		String dates []=null;
		Iterator it=dateVSRequests.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 date=key.toString();
			 dates=date.split(":");
	    	 outputStream.write(dates[0]+":00:00,"+dates[1]+","+dates[2]+","+dateVSRequests.get(key).longValue());
	    	//System.out.println("key is  "+dates[0]+":00:00,"+dates[1]+","+dates[2]+","+dateVSRequests.get(key).longValue());
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("EOCNMaxRequestsThresholdConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("EOCNMaxRequestsThresholdConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("EOCNMaxRequestsThresholdConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("EOCNMaxRequestsThresholdConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}

private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\DataCollection");
		EOCNMaxRequestsThresholdConverter s = new EOCNMaxRequestsThresholdConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\DataCollection\\trace2010081219.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}

}
