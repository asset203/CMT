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
import com.itworx.vaspp.datacollection.util.Utils;


public class USSDChargeConnectorConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateVSReqCount=new HashMap<String,Long>() ;
	private Map  <String ,Long> dateVSRespoCount=new HashMap<String,Long>() ;
	private Map  <String ,List> dateVSRespCount=new HashMap<String,List>() ;
	private Map  <String ,String> msisdnVSdate=new HashMap<String,String>() ;
public USSDChargeConnectorConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDChargeConnectorConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDChargeConnectorConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String msisdn=null;
			String respCode="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(","))
				{
					try{
					date=getDate(line.split(",")[0]);
					if(line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))						
						msisdn=Utils.stringBetween(line.split(",")[2], "[", "]");	
					if(line.contains("MSG:")&&line.contains("AppRequest")&&line.contains("UssdRechargeMain"))
					{
						
						if(!msisdnVSdate.containsKey(msisdn))
						{
							msisdnVSdate.put(msisdn, date);
						}
						if(dateVSReqCount.containsKey(date))
						{
							long count=dateVSReqCount.get(date);
							dateVSReqCount.put(date, count+1);
							
						}else
						{
							dateVSReqCount.put(date, new Long(1));
						}
						
					}
					else if(line.contains("AppResponse")&&line.contains("RespCode18="))
					{
						if(msisdnVSdate.containsKey(msisdn))
						{
							msisdnVSdate.remove(msisdn);
						    respCode=(line.split(",")[2].split("RespCode18=")[1]).split("RespCode19=")[0].trim();
						    if(respCode.contains("'")&&respCode.length()>2)
							  respCode=respCode.split("'")[1].split("'")[0];
						    else
						    {
						    	respCode="";						    	
						    }
						    if(dateVSRespCount.containsKey(date))
						    {
						    	List respList=dateVSRespCount.get(date);
						    	boolean found=false;
						    	for(int l=0;l<respList.size();l++)
						    	{
						    		if(((Response)respList.get(l)).getRespCode().equalsIgnoreCase(respCode))
						    		{
						    			((Response)respList.get(l)).setRespCount(((Response)respList.get(l)).getRespCount()+1);
						    			found=true;
						    			break;
						    		}
						    	}
						    	if(!found)
						    	{
						    		Response resp= new Response();
						    		resp.setRespCode(respCode);
						    		resp.setRespCount(1);
						    		respList.add(resp);
						    	}
						    	dateVSRespCount.put(date, respList);
						    }
						    else
						    {
						    	List respList= new ArrayList();
							    Response resp= new Response();
					    		resp.setRespCode(respCode);
					    		resp.setRespCount(1);
					    		respList.add(resp);
					    		dateVSRespCount.put(date, respList);
						    }
					    }
					}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
					
				}
			}
		}
	
		inputStream.close();
		Iterator it=dateVSRespCount.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			List respList =dateVSRespCount.get(key);
			long count =0;
			for(int l=0;l<respList.size();l++)
			{
			 Response resp=(Response)respList.get(l);
			 count=count+resp.getRespCount();
			 outputStream.write(key+","+resp.getRespCode()+","+resp.getRespCount()+","+resp.getRespCount());
			 outputStream.newLine();
			}
			dateVSRespoCount.put(key.toString(), count);
		}
		Iterator it2=dateVSReqCount.keySet().iterator();
		long reqCount=0;
		long respCount=0;
		while(it2.hasNext())
		{
			Object key=it2.next();
			reqCount=dateVSReqCount.get(key);
			if(dateVSRespoCount.containsKey(key))
			{
				respCount=dateVSRespoCount.get(key);
				if(reqCount>respCount)
				{
					long result =reqCount-respCount;
					 outputStream.write(key+","+" ,"+result+","+0);
					 outputStream.newLine();
				}
			}else
			{
				outputStream.write(key+","+" ,"+reqCount+","+0);
				 outputStream.newLine();
			}
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDChargeConnectorConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDChargeConnectorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDChargeConnectorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDChargeConnectorConverter.convert() - finished converting input files successfully ");
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
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase11\\DataCollection");
		USSDChargeConnectorConverter s = new USSDChargeConnectorConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\ESHRAQ_PROD\\ESHRAQ_PROD\\USSD_CH_CONN_1324209224209_4_ipcconnector_2011121809.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class Response
{
	public String respCode="";
	public long respCount=0;
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public long getRespCount() {
		return respCount;
	}
	public void setRespCount(long respCount) {
		this.respCount = respCount;
	}
}
}
