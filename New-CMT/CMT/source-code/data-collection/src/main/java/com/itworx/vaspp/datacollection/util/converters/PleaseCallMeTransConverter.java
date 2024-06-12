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

public class PleaseCallMeTransConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,CallMe> dateVsCount=new HashMap<String,CallMe>() ;
public PleaseCallMeTransConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside PleaseCallMeTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("PleaseCallMeTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String msisdn ="";
			String key ="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(" "))
				{
					try {
						
						date =getDate (line.split(" ")[0]);
						if(line.contains("Request:")&&line.split(" ")[2].contains("["))
						{
							msisdn =Utils.stringBetween(line.split(" ")[2], "[", "]").trim();
							key=date+","+msisdn;
							if(dateVsCount.containsKey(key))
							{
								CallMe obj=dateVsCount.get(key);
								obj.setaPartyCount(obj.getaPartyCount()+1);
								dateVsCount.put(key, obj);
							}
							else
							{
								CallMe obj= new CallMe();
								obj.setaPartyCount(1);
								dateVsCount.put(key, obj);
							}
						}
						if(line.contains("Request:") &&line.contains("#"))
						{
							msisdn=Utils.stringBetween(line, "Request:", "#").trim();
							if(msisdn.startsWith("0"))
								msisdn="2"+msisdn;
							else if (msisdn.startsWith("1"))
								msisdn="20"+msisdn;
							else if (!(msisdn.startsWith("0")||(msisdn.startsWith("1"))))
								msisdn="201"+msisdn;
							key=date+","+msisdn;
							
							if(dateVsCount.containsKey(key))
							{
								CallMe obj=dateVsCount.get(key);
								obj.setbPartyCount(obj.getbPartyCount()+1);
								dateVsCount.put(key, obj);
							}
							else
							{
								CallMe obj= new CallMe();
								obj.setbPartyCount(1);
								dateVsCount.put(key, obj);
							}
						}
						else if (line.contains("Response: Message sent successfully")&&line.split(" ")[2].contains("["))
						{
							msisdn =Utils.stringBetween(line.split(" ")[2], "[", "]").trim();
							key=date+","+msisdn;
							if(dateVsCount.containsKey(key))
							{
								CallMe obj=dateVsCount.get(key);
								obj.setSuccMessCount(obj.getSuccMessCount()+1);
								dateVsCount.put(key, obj);
							}
							/*else
							{
								CallMe obj= new CallMe();
								obj.setSuccMessCount(1);
								dateVsCount.put(key, obj);
							}*/
							
						}
					  } catch(ParseException exc){ logger.error(exc) ; continue ;}
			
					
				}
			}
		}
	
	inputStream.close();
	Iterator it=dateVsCount.keySet().iterator();
	while (it.hasNext())
	{
		Object key=it.next();
		CallMe obj=dateVsCount.get(key);
		outputStream.write(key.toString()+","+obj.getaPartyCount()+","+obj.getbPartyCount()+","+obj.getSuccMessCount());
		 outputStream.newLine();
		
	}
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("PleaseCallMeTransConverter.convert() - finished converting input files successfully ");

}
catch (FileNotFoundException e) {
		logger
				.error("PleaseCallMeTransConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("PleaseCallMeTransConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("PleaseCallMeTransConverter.convert() - finished converting input files successfully ");
	return outputFiles;
	
}
private String getDate(String line) throws ParseException {
String[] tokens = null;
Date date = new Date();
String dateString;
SimpleDateFormat inDateFormat = new SimpleDateFormat(
		"yyyy-MM-dd");
SimpleDateFormat outDateFormat = new SimpleDateFormat(
		"MM/dd/yyyy");


	date = inDateFormat.parse(line);
dateString = outDateFormat.format(date);
return dateString;

}
public static void main(String ag[]) {
try {
	
	PropertyReader.init("D:\\build\\phase10\\DataCollection");
	PleaseCallMeTransConverter s = new PleaseCallMeTransConverter();
	File[] input = new File[1];
	input[0]=new File("D:\\build\\phase10\\DataCollection\\browser_505_2011061006.log");
	   s.convert(input,"Maha_Test");
	System.out.println("finished ");
} catch (Exception e) {
	e.printStackTrace();
}
}
class CallMe
{
	long aPartyCount=0;
	long bPartyCount=0;
	long succMessCount=0;
	public long getSuccMessCount() {
		return succMessCount;
	}
	public void setSuccMessCount(long succMessCount) {
		this.succMessCount = succMessCount;
	}
	public long getaPartyCount() {
		return aPartyCount;
	}
	public void setaPartyCount(long aPartyCount) {
		this.aPartyCount = aPartyCount;
	}
	public long getbPartyCount() {
		return bPartyCount;
	}
	public void setbPartyCount(long bPartyCount) {
		this.bPartyCount = bPartyCount;
	}
}
}
