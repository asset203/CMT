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
import com.itworx.vaspp.datacollection.persistenceobjects.UssdShortCodesReqResData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;



public class USSDSohrtC2TransactionsConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,UssdShortCodesReqRes > dateVsCount=new HashMap<String,UssdShortCodesReqRes>() ;
public USSDSohrtC2TransactionsConverter()

{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDSohrtC2TransactionsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File output = new File(path, "UssdScReqResFile");

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(output));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDSohrtC2TransactionsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line="";
			String date = "";
			String shourtCode="";
			String key="";
			
			while (inputStream.ready()) {
				line=inputStream.readLine();
				if (line.equals("")) {
					continue;
				}else{
					if(line.contains(" "))
					{
						try{
						date=getDate(line.split(" ")[0]+" "+line.split(" ")[1]);
						if(line.contains("_"))
							shourtCode=line.split("-")[3];
						key = date +","+shourtCode;
						if (line.contains("Request:  [op:1]")) 
						{
						  if(dateVsCount.containsKey(key))
						  {
							  UssdShortCodesReqRes reqResp= (UssdShortCodesReqRes)dateVsCount.get(key);
							  reqResp.setEnquire(reqResp.getEnquire()+1);
							  dateVsCount.put(key, reqResp);
						  }
						  else
						  {
							  UssdShortCodesReqRes reqResp=new UssdShortCodesReqRes ();
							  reqResp.setEnquire(1);
							  dateVsCount.put(key, reqResp);
						  }
							
						}	
						else if(line.contains("Request:")&&(line.contains("[op:1]"))&&Utils.stringBetween(line, "Request:", "[op:1]").contains("#"))
						{
							 if(dateVsCount.containsKey(key))
							  {
								  UssdShortCodesReqRes reqResp= (UssdShortCodesReqRes)dateVsCount.get(key);
								  reqResp.setRedention((reqResp.getRedention()+1));
								  dateVsCount.put(key, reqResp);
							  }
							  else
							  {
								  UssdShortCodesReqRes reqResp=new UssdShortCodesReqRes ();
								  reqResp.setRedention(1);
								  dateVsCount.put(key, reqResp);
							  }
						}
						
						
						}catch(ParseException exc){ logger.error(exc) ; continue ;}
					}
					
				}
			}
		}//end of the files 
		Iterator it=dateVsCount.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			UssdShortCodesReqRes obj=(UssdShortCodesReqRes)dateVsCount.get(key);
			outputStream.write(key+","+obj.getEnquire()+","+obj.getRedention());					
			outputStream.newLine();
		}
	
     inputStream.close();
	
	outputStream.close();
	outputFiles[0]=output;
	logger.debug("USSDSohrtC2TransactionsConverter.convert() - "
			+ inputFiles[0].getName() + " converted");	
	
} catch (FileNotFoundException e) {
	logger.error("USSDSohrtC2TransactionsConverter.convert() - Input file not found " + e);
	throw new ApplicationException(e);
} catch (IOException e) {
	logger.error("USSDSohrtC2TransactionsConverter.convert() - Couldn't read input file"
			+ e);
	throw new ApplicationException(e);
}
logger.debug("USSDSohrtC2TransactionsConverter.convert() - finished converting input files successfully ");
return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		USSDSohrtC2TransactionsConverter s = new USSDSohrtC2TransactionsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\browser_2020_2011060111.log");
		s.convert(input,"ussd_sc");
		
	} catch (Exception e) {
		e.printStackTrace();
	}

}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
class UssdShortCodesReqRes
{
	long enquire =0;
	long redention=0;
	public long getEnquire() {
		return enquire;
	}
	public void setEnquire(long enquire) {
		this.enquire = enquire;
	}
	public long getRedention() {
		return redention;
	}
	public void setRedention(long redention) {
		this.redention = redention;
	}
}
}
