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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class QATSelectLangSmsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Sms> dateVsSmsCount=new HashMap<String,Sms>() ;

public QATSelectLangSmsConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside QATSelectLangSmsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("QATSelectLangSmsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line;
			String date = "";
			String reqId="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.length()>23)
				{  
					try {
					   date=getDate(line.substring(0, 23));
					   if(line.contains("INCOMING SM"))
					   {
						   if(dateVsSmsCount.containsKey(date))
						   {
							   Sms obj=dateVsSmsCount.get(date);
							   long incomCout=obj.getIncomSmsCount();
							   obj.setIncomSmsCount(incomCout+1);
							   dateVsSmsCount.remove(date);
							   dateVsSmsCount.put(date, obj);
						   }
						   else
						   {
							   Sms obj= new Sms();
							   obj.setIncomSmsCount(1);
							   dateVsSmsCount.put(date, obj);
							  
						   }
					   }
					   else if(line.contains("SUBMIT SM"))
					   {
						   if(dateVsSmsCount.containsKey(date))
						   {
							   Sms obj=dateVsSmsCount.get(date);
							   long subCout=obj.getSubmitSmsCount();
							   obj.setSubmitSmsCount(subCout+1);
							   dateVsSmsCount.remove(date);
							   dateVsSmsCount.put(date, obj);
						   }else
						   {
							   Sms obj= new Sms();
							   obj.setSubmitSmsCount(1);
							   dateVsSmsCount.put(date, obj);
						   }
					   }
						   
			         	}catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}//end of the file 
		Iterator it=dateVsSmsCount.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 Sms smsObj=dateVsSmsCount.get(key);
			 outputStream.write(key+","+smsObj.getIncomSmsCount()+","+smsObj.getSubmitSmsCount());
			 outputStream.newLine();
		}
inputStream.close();
		//System.out.println("finished");
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("QATSelectLangSmsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("QATSelectLangSmsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("QATSelectLangSmsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("QATSelectLangSmsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
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
		//System.out.println(" date "+date);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
		QATSelectLangSmsConverter s = new QATSelectLangSmsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\SMS_traffic_20101021.txt");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public class Sms
{
	public long incomSmsCount=0;
	public long submitSmsCount=0;
	public long getIncomSmsCount() {
		return incomSmsCount;
	}
	public void setIncomSmsCount(long incomSmsCount) {
		this.incomSmsCount = incomSmsCount;
	}
	public long getSubmitSmsCount() {
		return submitSmsCount;
	}
	public void setSubmitSmsCount(long submitSmsCount) {
		this.submitSmsCount = submitSmsCount;
	}
}
}
