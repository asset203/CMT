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

public class PleaseCallMeChargedTransConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,PleaseCall> dateVsCount=new HashMap<String,PleaseCall>() ;
	private Map  <String ,String> msisdnVsdate=new HashMap<String,String>() ;
public PleaseCallMeChargedTransConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside PleaseCallMeChargedTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("PleaseCallMeChargedTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String msisdn ="";
			String key ="";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=3)
				{
					try 
					{
					date =getDate(line.split(",")[0]);
					if(line.split(",")[2].contains("["))
					{
						msisdn =Utils.stringBetween(line.split(",")[2], "[", "]");
					}else
						continue;
					
					date=date+","+msisdn;
					
				    if(line.contains("AppRequest:")&&line.contains("SERVICE_PARAM_3$505"))
				    {
				    	msisdnVsdate.put(msisdn, date);
				    	if(dateVsCount.containsKey(date))
				    	{
				    		PleaseCall obj=dateVsCount.get(date);
				    		obj.setChargedTransCount(obj.getChargedTransCount()+1);
				    		dateVsCount.put(date, obj);
				    	}else
				    	{
				    		PleaseCall obj= new PleaseCall();
				    		obj.setChargedTransCount(1);
				    		dateVsCount.put(date, obj);
				    	}
				    }
				    else if (line.contains("AppResponse ErrCode:0"))
				    {
				    	if(msisdnVsdate.containsKey(msisdn))
				    	{
				    		msisdnVsdate.remove(msisdn);
					    	if(dateVsCount.containsKey(date))
					    	{
					    		PleaseCall obj=dateVsCount.get(date);
					    		obj.setSuccessTransCount(obj.getSuccessTransCount()+1);
					    		dateVsCount.put(date, obj);
					    	}
					    	else
					    	{
					    		PleaseCall obj= new PleaseCall();
					    		obj.setSuccessTransCount(1);
					    		dateVsCount.put(date, obj);
					    	}
				    	}
				    }
					}  catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
		inputStream.close();	
		Iterator it =dateVsCount.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			PleaseCall obj=dateVsCount.get(key);
			outputStream.write(key.toString()+","+obj.getChargedTransCount()+","+obj.getSuccessTransCount());
			 outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("PleaseCallMeChangedTransConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("PleaseCallMeChangedTransConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("PleaseCallMeChangedTransConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("PleaseCallMeChangedTransConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\ITWorx\\Projects\\VFE_VAS_VNPP_2011_Phase2\\Trunk\\SourceCode\\DataCollection");
		PleaseCallMeChargedTransConverter s = new PleaseCallMeChargedTransConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\ITWorx\\Projects\\VFE_VAS_VNPP_2011_Phase2\\Trunk\\SourceCode\\Deployment\\21-06-2011\\files\\pleaseCallMeCharge\\ipcconnector_2011062020.log");
		   s.convert(input,"Maha_Test");
		System.out.println("finished ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class PleaseCall
{
	long chargedTransCount=0;
	long successTransCount=0;
	public long getChargedTransCount() {
		return chargedTransCount;
	}
	public void setChargedTransCount(long chargedTransCount) {
		this.chargedTransCount = chargedTransCount;
	}
	public long getSuccessTransCount() {
		return successTransCount;
	}
	public void setSuccessTransCount(long successTransCount) {
		this.successTransCount = successTransCount;
	}
	
}
}
