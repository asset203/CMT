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

public class AIRRefillTransConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;
public AIRRefillTransConverter()
{
	}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside AIRRefillTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("AIRRefillTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String key;
			String date ="";
			String service="";
			long transCount=0;
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=5)
				{
					try{
						date = getDate (line.split(",")[3]);
						
						service=line.split(",")[4]!=null?line.split(",")[4]:"";
						key =date +","+service;
						//System.out.println("date "+key);
						if(dateVsCount.containsKey(key))
						{
							long count =dateVsCount.get(key);
							dateVsCount.remove(key);
							dateVsCount.put(key, count +1);
						}else
						{
							dateVsCount.put(key, new Long (1));
						}
					}
				catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
inputStream.close();
Iterator it=dateVsCount.keySet().iterator();

while(it.hasNext())
{
	Object key=it.next();				
	outputStream.write(key+","+dateVsCount.get(key));
	System.out.println(key+","+dateVsCount.get(key));
	outputStream.newLine();
	
}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("AIRRefillTransConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("AIRRefillTransConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("AIRRefillTransConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("AIRRefillTransConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\DataCollection");
		AIRRefillTransConverter s = new AIRRefillTransConverter();
		File[] input = new File[1];
		input[0]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\13-12-2012\\PPAS-PAYMENT_2012_12_12.TXT");
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
			"yyyyMMdd HH:mm:ss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
