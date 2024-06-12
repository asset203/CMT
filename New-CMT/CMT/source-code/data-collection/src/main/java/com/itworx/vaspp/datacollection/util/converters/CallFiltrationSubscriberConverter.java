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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class CallFiltrationSubscriberConverter  extends AbstractTextConverter {
	private Logger logger;
	public CallFiltrationSubscriberConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
logger = Logger.getLogger(systemName);
logger.debug("CallFiltrationSubscriberConverter.convert() - started converting input files ");
String path = PropertyReader.getConvertedFilesPath();
File[] outputFiles = new File[inputFiles.length];
try {
	for (int i = 0; i < inputFiles.length; i++) 
	{
		logger.debug("CallFiltrationSubscriberConverter.convert() - converting file "
				+ inputFiles[i].getName());
		
		File output = new File(path, inputFiles[i].getName());
		BufferedReader inputStream = new BufferedReader(new FileReader(
				inputFiles[i]));
		BufferedWriter outputStream = new BufferedWriter(
				new FileWriter(output));
		String line;
		String date;
		String key;
		while (inputStream.ready()) {
			line = inputStream.readLine();
			if(line.contains("Period"))
			{
				continue; //to skip the header line
			}
			else
			{
				line=line.replace(',','\'');
				String columns[]=line.split(";");
				
				try{
					if(columns.length>=5)
					{
		
					date=getDate(columns[0].split("\"")[1]);
					String serviceName=columns[1].split("\"")[1];
					long totalSubs= Long.parseLong(columns[2].split("\"")[1]);
					long whiteListSubs=Long.parseLong(columns[3].split("\"")[1]);
					long blackListSubs=Long.parseLong(columns[4].split("\"")[1]);
					key=date+","+serviceName+","+totalSubs+","+whiteListSubs+","+blackListSubs;
					outputStream.write(key);
					outputStream.newLine();
					}
				   }
				catch(ParseException exc) { logger.error(exc) ; continue ;}
				catch(java.lang.NumberFormatException ex)
				{ logger.error(ex) ;continue ;}
				{continue;}
			}
		}
		outputStream.close();
		inputStream.close();
		outputFiles[i] = output;
		logger.debug("CallFiltrationSubscriberConverter.convert() - "
				+ inputFiles[i].getName() + " converted");	
	}								
} catch (FileNotFoundException e) {
	logger.error("CallFiltrationSubscriberConverter.convert() - Input file not found " + e);
	throw new ApplicationException(e);
} catch (IOException e) {
	logger.error("CallFiltrationSubscriberConverter.convert() - Couldn't read input file"
			+ e);
	throw new ApplicationException(e);
}
logger.debug("CallFiltrationSubscriberConverter.convert() - finished converting input files successfully ");
return outputFiles;
	}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			CallFiltrationSubscriberConverter s = new CallFiltrationSubscriberConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\vf_cf_hourly_report_201007.csv");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
}