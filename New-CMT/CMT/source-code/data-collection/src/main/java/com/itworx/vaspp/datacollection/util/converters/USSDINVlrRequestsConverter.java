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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.USSDInteractiveRequestsConverter.RespCount;

public class USSDINVlrRequestsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,String  > transIdVsDataVlr=new HashMap<String,String>() ;
	private Map  <String ,ReqRespCount > dateVsRequestsResponses=new HashMap<String,ReqRespCount>() ;
	private Map  <String ,Long> keyVSDate=new HashMap<String,Long>() ;
	private Map  <String ,Long> netResult=new HashMap<String,Long>() ;
public USSDINVlrRequestsConverter()
{
	
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDINVlrRequestsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	String responseRegex="^.+\\[op:32[;\\]].*$";
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDINVlrRequestsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String vlrName="";
			String key="";
			String transId="";
			String reqName="";
			String respName="";
			boolean found=false;
			
			while (inputStream.ready()) {
				
				line = inputStream.readLine();
				if(line.contains(" "))
				{
					try
				  {
					date=getDate(line.split(" ")[0]+" "+line.split(" ")[1]);
					//System.out.println("date"+date);
				  }catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
				/*it is the beginning of the requests */
				if(line.contains("Request: 2*")&&line.contains("[op:1]")&&line.contains("MAP_HLR:"))
				{
				    key="";
					
					vlrName=(line.split("MAP_HLR:")[1]).split(",")[0];
					
					transId=((line.split("-868-")[1]).split("Request:")[0]).trim();
					key=date+","+vlrName+","+transId;
					if(!transIdVsDataVlr.containsKey(transId))
					{
						transIdVsDataVlr.put(transId, key);
					  if(!dateVsRequestsResponses.containsKey(key))
					  {
						  ReqRespCount obj= new ReqRespCount();
						  dateVsRequestsResponses.put(key, obj);
					  }
					
					}
				}/*the first req*/
				else if((line.contains("Request:")&&line.contains("[op:18]"))||(line.contains("Request:")&&line.contains("90"))
						||(line.contains("Request:")&&line.contains("407")))
				{
					transId=((line.split("-868-")[1]).split("Request:")[0]).trim();
					if(transIdVsDataVlr.containsKey(transId))
					{   
						key=transIdVsDataVlr.get(transId);
						reqName=(line.split("Request:")[1]).split("\\[op")[0].trim();
						if(reqName.contains(","))
						{
							reqName=reqName.replaceAll(",", ";");
						}
							
						if(dateVsRequestsResponses.containsKey(key))
						{
							ReqRespCount obj=dateVsRequestsResponses.get(key);
							obj.setReqName(reqName);
							dateVsRequestsResponses.remove(key);
							dateVsRequestsResponses.put(key, obj);
						}
						
					}
				}/*the respons*/
				else if(line.contains("Response:")&&line.contains("[op:32"))
				{
					transId=((line.split("-868-")[1]).split("Response:")[0]).trim();
					if(transIdVsDataVlr.containsKey(transId))
					{
						key=transIdVsDataVlr.get(transId);
						respName=(line.split("Response:")[1]).split("\\[")[0].trim();
						if(respName.contains(","))
						{
							
							respName=respName.replaceAll(",", ";");
						}
						
						if(dateVsRequestsResponses.containsKey(key))
						{
							ReqRespCount obj=dateVsRequestsResponses.get(key);
							if(!obj.getReqName().equalsIgnoreCase("NULL"))
							{obj.setResponseName(respName);
							dateVsRequestsResponses.remove(key);
							dateVsRequestsResponses.put(key, obj);
							}
						}
					
					}
				}
			}
			
		}
		Iterator it =dateVsRequestsResponses.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			ReqRespCount obj=dateVsRequestsResponses.get(key);
			String columns[]= key.toString().split(",");
			String keyName=columns[0]+","+columns[1]+","+obj.getReqName()+","+obj.getResponseName();
			if(netResult.containsKey(keyName))
			{
				Long value =netResult.get(keyName);
				netResult.remove(keyName);
				netResult.put(keyName,value+1);
			}
			else
			{
				netResult.put(keyName,new Long(1));
			}
			
			
			
		}
		Iterator it1  =netResult.keySet().iterator();
		while(it1.hasNext())
		{
			Object key=it1.next();
			outputStream.write(key+","+(Long)netResult.get(key).longValue());
			//System.out.println("key"+key+","+(Long)netResult.get(key).longValue());
			outputStream.newLine();
		}
		
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDINVlrRequestsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDINVlrRequestsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDINVlrRequestsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDINVlrRequestsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		String path ="D:\\build\\pahse8\\build\\DataCollection";
		PropertyReader.init(path);
		USSDINVlrRequestsConverter s = new USSDINVlrRequestsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\build\\DataCollection\\browser_868_2010101914.log");
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
			"yyyy-MM-dd HH:mm:ss.ms");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
class ReqRespCount
{
public String responseName="Null";
public String reqName="Null";

public String getReqName() {
	return reqName;
}
public void setReqName(String reqName) {
	this.reqName = reqName;
}



public String getResponseName() {
	return responseName;
}
public void setResponseName(String responseName) {
	this.responseName = responseName;
}



}







}

