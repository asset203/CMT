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


public class USSDSubCallsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> hourVsReqRespObject=new HashMap<String,List>();
	private Map  <String ,String> msisdnVSkey= new HashMap<String,String>();
	private Map  <String ,Long> restRequestCounts= new HashMap<String,Long>();
	private Map  <String ,Request> requestsWithoutResponses=new HashMap<String,Request>();
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside USSDSubCallsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("USSDSubCallsConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            
				String line;
				String date = "";
				String shortCode;
				String subCode;
				String msisdn;
				String respReason;
				String key;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("["))
						try{
						 date=getDate(line.split("\\[")[0].trim());
				         } catch(ParseException exc){ logger.error(exc) ; continue ;}
				         shortCode=line.split("\\[")[1].split("-")[1].split("-")[0];
					if(line.contains("Request:")&&line.split("Request:")[1].contains("#")&&line.contains("[")&&line.split("\\[")[1].contains("-"))
					{
						
						if(Utils.stringBetween(line, "Request:", "#").contains("*"))
						{
							subCode=Utils.stringBetween(line, "Request:", "#").split("\\*")[0];
						}
						else
						{
							subCode="null";
						}
						
						msisdn=line.split("\\[")[1].split("\\]")[0];
						key=date+","+shortCode+","+subCode;
						if(!msisdnVSkey.containsKey(msisdn))
						{
							msisdnVSkey.put(msisdn, key);
						}
						if(!hourVsReqRespObject.containsKey(key))
						{
							List list= new ArrayList();
							ReqResp obj= new ReqResp();
							list.add(obj);							
							hourVsReqRespObject.put(key, list);
							
						}
						if(!requestsWithoutResponses.containsKey(msisdn))
						{
							Request obj= new Request();
							obj.setShortCode(shortCode);
							obj.setSubCode(subCode);
							obj.setDate(date);
							requestsWithoutResponses.put(msisdn, obj);
						}
					}
					else if (line.contains("Response:")&&line.contains("[")&&line.split("Response:")[1].contains("["))
					{
						msisdn=line.split("\\[")[1].split("\\]")[0];
						respReason=line.split("Response:")[1].split("\\[")[0].trim();
						if(respReason.contains(","))
							respReason=respReason.replaceAll(",", " ");
						if(msisdnVSkey.containsKey(msisdn))
						{
							String respKey=msisdnVSkey.get(msisdn);
							
							msisdnVSkey.remove(msisdn);
							requestsWithoutResponses.remove(msisdn);
							boolean found =false;
							if(hourVsReqRespObject.containsKey(respKey))
							{
								List  list=hourVsReqRespObject.get(respKey);
								for(int l=0;l<list.size();l++)
								{
									ReqResp obj=(ReqResp)list.get(l);
									
									if(obj.getRespReason()==null ||obj.getRespReason().equalsIgnoreCase(respReason))
									{
										obj.setReqCount(obj.getReqCount()+1);	
										obj.setRespCount(obj.getRespCount()+1);
										obj.setRespReason(respReason);
										found=true;
										break;
									}
								}
								
								if(!found)
								{
									ReqResp obj= new ReqResp();
									obj.setRespCount(1);
									obj.setReqCount(1);
									obj.setRespReason(respReason);
									list.add(obj);
									
								}
								hourVsReqRespObject.put(respKey, list);
							}
						}else//responses without requests 
						{
							boolean found=false;
							String respWithuotKey=date+","+shortCode+", ";
							if(hourVsReqRespObject.containsKey(respWithuotKey))
							{
								List list =hourVsReqRespObject.get(respWithuotKey);
								for(int l=0;l<list.size();l++)
								{
									ReqResp obj=(ReqResp)list.get(l);
									if(obj.getRespReason().equalsIgnoreCase(respReason))
									{
											
										obj.setRespCount(obj.getRespCount()+1);										
										found=true;
										break;
									}
								}
								
								if(!found)
								{
									ReqResp obj= new ReqResp();
									obj.setRespCount(1);
									obj.setRespReason(respReason);
									list.add(obj);
									
								}
								hourVsReqRespObject.put(respWithuotKey, list);
							}else
							{
								List list = new ArrayList();
								ReqResp obj= new ReqResp();
								obj.setRespCount(1);
								obj.setRespReason(respReason);
								list.add(obj);
								hourVsReqRespObject.put(respWithuotKey, list);
							}
						}
						
					}
				}
			}	
			inputStream.close();
			Iterator it2=requestsWithoutResponses.keySet().iterator();
			while (it2.hasNext()){
			Object msisdn =it2.next();
			Request req=requestsWithoutResponses.get(msisdn);
			String key =req.getDate()+","+req.getShortCode()+","+req.getSubCode();
					if(restRequestCounts.containsKey(key))
					{
						long count =restRequestCounts.get(key);
						restRequestCounts.put(key, count+1);
					}else
					{
						restRequestCounts.put(key, new Long (1));
					}
			}
			Iterator it=hourVsReqRespObject.keySet().iterator();
			while (it.hasNext())
			{
				Object key=it.next();
				List list=hourVsReqRespObject.get(key);
				for (int l=0;l<list.size();l++)
				{
					ReqResp obj=(ReqResp)list.get(l);
					if(obj.getRespReason()!=null)
					{
						outputStream.write(key+","+obj.getRespReason()+","+obj.getReqCount()+","+obj.getRespCount());
						 outputStream.newLine();
					}					
				}
			}
			Iterator it3=restRequestCounts.keySet().iterator();
			while (it3.hasNext())
			{
				Object key =it3.next();
				outputStream.write(key+","+" ,"+restRequestCounts.get(key)+",0");
				 outputStream.newLine();
			}
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("USSDSubCallsConverter.convert() - finished converting input files successfully ");

			}
			catch (FileNotFoundException e) {
				logger
						.error("USSDSubCallsConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("USSDSubCallsConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("USSDSubCallsConverter.convert() - finished converting input files successfully ");
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
					
					PropertyReader.init("D:\\build\\phase10\\DataCollection");
					USSDSubCallsConverter s = new USSDSubCallsConverter();
					File[] input = new File[1];
					input[0]=new File("D:\\build\\phase10\\DataCollection\\browser_506_2011082412.log");
				//input[1]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2010112814.log");		
					   s.convert(input,"Maha_Test");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
public USSDSubCallsConverter()
{
}
class ReqResp
{
	
	String respReason;
	long reqCount=0;
	long respCount=0;
	
	
	public String getRespReason() {
		return respReason;
	}
	public void setRespReason(String respReason) {
		this.respReason = respReason;
	}
	public long getReqCount() {
		return reqCount;
	}
	public void setReqCount(long reqCount) {
		this.reqCount = reqCount;
	}
	public long getRespCount() {
		return respCount;
	}
	public void setRespCount(long respCount) {
		this.respCount = respCount;
	}
}
class Request
{
	String shortCode;
	String subCode;
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	long reqCount;
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public long getReqCount() {
		return reqCount;
	}
	public void setReqCount(long reqCount) {
		this.reqCount = reqCount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	String date;
	
}
}
