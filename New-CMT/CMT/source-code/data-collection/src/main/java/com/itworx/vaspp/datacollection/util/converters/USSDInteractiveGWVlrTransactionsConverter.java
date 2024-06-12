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
import com.itworx.vaspp.datacollection.util.Utils;

public class USSDInteractiveGWVlrTransactionsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long > dateVlrVsCount=new HashMap<String,Long>() ;
	private Map  <String ,Long > dateVlrRespVsCount=new HashMap<String,Long>() ;
	private Map  <String ,String  > transIdVsKey=new HashMap<String,String>() ;
	private Map  <String ,Long > respResults=new HashMap<String,Long>() ;
public USSDInteractiveGWVlrTransactionsConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDInteractiveGWVlrTransactionsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDInteractiveGWVlrTransactionsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			//inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
            
			String line;
			String date = "";
			String vlrName="";
			String transId="";
			String respKey="";
			String key="";
			while (inputStream.ready()) {
				String msisdn="";
				line = inputStream.readLine();
				if(line.contains("SC:868"))
				{
					if(line.contains(","))
					{
						try 
					
					{
						date=getDate(line.split(",")[0]);
						//getting the Vlr Line 
						if(line.contains("PSSR Indication: '2*"))
						{
							if(line.contains("HLR:")&&line.split("HLR:")[1].contains(","))
							{
								vlrName=line.split("HLR:")[1].split(",")[0].trim();
							}
							if(line.contains("TID:")&&line.split("TID:")[1].contains(","))
							{
								transId=line.split("TID:")[1].split(",")[0].trim();
							}
							key=date+","+vlrName;
							transIdVsKey.put(transId, key);
							if(dateVlrVsCount.containsKey(key))
							{
								Long count=dateVlrVsCount.get(key);
								dateVlrVsCount.remove(key);
								dateVlrVsCount.put(key, count+1);
							
							}else
							{
								dateVlrVsCount.put(key, new Long(1));
							}
							
						}//getting the response line
						else if(line.contains("USSD Error. Reason:")||line.contains("USSR Confirm:"))
						{
							if(line.contains("TID:")&&line.split("TID:")[1].contains(","))
							{
							transId=line.split("TID:")[1].split(",")[0].trim();
						
							if(transIdVsKey.containsKey(transId))
							{
								
								if(line.contains("USSD Error. Reason:"))
								{
									
									respKey=transIdVsKey.get(transId)+","+line.split("USSD Error. Reason:")[1].trim();
									
									
								}
								else if (line.contains("USSR Confirm:"))
								{
									String resp=Utils.stringBetween(line, "USSR Confirm: '" , "', Length");
									if(resp==null)
									   resp="Empty";
										
									
										
//									String resp="'";
//									
//									if(line.split("USSR Confirm:")[1].contains(","))
//									{
//										resp=line.split("USSR Confirm:")[1].split(",")[0].trim();
//										
//									}
									respKey=transIdVsKey.get(transId)+","+resp.replaceAll(",", ";");
								}
								
								if(dateVlrRespVsCount.containsKey(respKey))
								{
									Long responses =dateVlrRespVsCount.get(respKey);
									dateVlrRespVsCount.remove(respKey);
									dateVlrRespVsCount.put(respKey, responses+1);
								}
								else
								{
									dateVlrRespVsCount.put(respKey, new Long(1));
								}
								transIdVsKey.remove(transId);
							}
							else
							{continue;}
							}
						}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
					catch(ArrayIndexOutOfBoundsException exc){ logger.error(exc) ; continue ;}
					}
				}//END OF 868
				else
				{
					continue;
				}
			}
		}//end of the file 
		
		Iterator respIt =dateVlrRespVsCount.keySet().iterator();
		while(respIt.hasNext())
		{
			Object key=respIt.next();
			String columns []=key.toString().split(",");
			String generatedKey =columns[0]+","+columns[1];
			if(respResults.containsKey(generatedKey))
			{
				Long allCount=respResults.get(generatedKey);
				respResults.remove(generatedKey);
				respResults.put(generatedKey, allCount+dateVlrRespVsCount.get(key));
			}
			else
			{
				respResults.put(generatedKey, dateVlrRespVsCount.get(key));
			}
		}
		
		Iterator it =dateVlrRespVsCount.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			outputStream.write(key+","+dateVlrRespVsCount.get(key));
			outputStream.newLine();
		}
		Iterator vlrIt= dateVlrVsCount.keySet().iterator();
		while (vlrIt.hasNext())
		{
			Object key =vlrIt.next();
			if(respResults.containsKey(key))
			{
				Long  respCount =respResults.get(key);
				Long vlrCount =dateVlrVsCount.get(key);
				if(vlrCount>respCount)
				{
					Long resultCount =vlrCount-respCount;
					outputStream.write(key+","+"NULL"+","+resultCount);
					outputStream.newLine();
				}
			}else
			{
				outputStream.write(key+","+"NULL"+","+dateVlrVsCount.get(key));
				outputStream.newLine();
			}
		}
          inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDInteractiveGWVlrTransactionsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDInteractiveGWVlrTransactionsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDInteractiveGWVlrTransactionsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDInteractiveGWVlrTransactionsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		
	 	
	    String path ="D:\\build\\pahse8\\logmanager\\DataCollection";
		PropertyReader.init(path);
		USSDInteractiveGWVlrTransactionsConverter s = new USSDInteractiveGWVlrTransactionsConverter();
		File[] input = new File[1];
		
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\trace2010110317.log");
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
			"yyyy-MM-dd HH:mm:ms");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:00:00");

	
		date = inDateFormat.parse(line);
		
	dateString = outDateFormat.format(date);
	return dateString;

}
}
