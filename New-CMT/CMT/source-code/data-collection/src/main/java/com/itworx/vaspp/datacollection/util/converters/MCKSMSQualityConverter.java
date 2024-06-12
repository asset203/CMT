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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class MCKSMSQualityConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,String> IdVsDate=new HashMap<String,String>() ;
	private Map  <String ,String> address=new HashMap<String,String>() ;
	private Map  <String ,Long> keyVsCount=new HashMap<String,Long>() ;
public MCKSMSQualityConverter()
{
	
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside MCKSMSQualityConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		String adressess []=PropertyReader.getMCKAddress().split(",");	
		for(int i=0;i<adressess.length;i++)
		{			
			address.put(adressess[i], adressess[i]);
			
		}
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("MCKSMSQualityConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
        
			String line;
			String date = "";
			String transId="";
			String msgType="";
			String sourceAdd=null;
			String destAddress=null;
			String msgInfo="";
			String key="";
			boolean addFound=false;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("transactionID:")&&line.contains("Taking the message from sender queue.")&&line.contains("]")&&line.contains("["))
				{
					date=line.split("\\[")[1].split("\\]")[0];
					
					transId=line.split("transactionID:")[1].trim();
					try{
					date=getDate(getCurrentYear()+date);						
						
					if(!IdVsDate.containsKey(transId))
					{
						IdVsDate.put(transId, date);
					}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
					
				}
				else if(line.contains("SMPP_MESSAGE_HEAD:")&& line.contains("sequenceID:"))
				{
					String messageId=line.split("sequenceID:")[1].split("}")[0];
					if(IdVsDate.containsKey(messageId))
					{
						String respDate=IdVsDate.get(messageId);
						
						line = inputStream.readLine();
						if(line.contains("SMPP_DELIVER_SM_MESSAGE_BODY"))
						{
							
							while (inputStream.ready()&&!line.contains("source_addr:")&&!line.contains("destination_addr:")&&!line.contains("}")) {
								line=inputStream.readLine();								
							}
							if(line.contains("source_addr:"))
							{   
									sourceAdd=line.split("source_addr:")[1].trim();
							}
							while (inputStream.ready()&&!line.contains("destination_addr:")&&!line.contains("}")) {
								line=inputStream.readLine();								
							}
							if(line.contains("destination_addr:"))
							{   
									destAddress=line.split("destination_addr:")[1].trim();
									
							}
							
							if(destAddress!=null&&sourceAdd!=null)
							{
								if(!address.containsKey(sourceAdd)&&!address.containsKey(destAddress))
								{
										key =respDate+","+sourceAdd+","+destAddress;
										outputStream.write(key);										
										outputStream.newLine();
										destAddress=null;
										sourceAdd=null;
								}
							}
							IdVsDate.remove(messageId);
						}
						
					}
				}
			}
		}
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("MCKSMSQualityConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("MCKSMSQualityConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("MCKSMSQualityConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("MCKSMSQualityConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getCurrentYear() throws ParseException {
	String dateString;	
	SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy");		
		Calendar c= Calendar.getInstance() ;		
	    dateString = outDateFormat.format(c.getTime());
	    return dateString;

}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMM dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		MCKSMSQualityConverter s = new MCKSMSQualityConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\UniPortalCOMM.txt.7");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
