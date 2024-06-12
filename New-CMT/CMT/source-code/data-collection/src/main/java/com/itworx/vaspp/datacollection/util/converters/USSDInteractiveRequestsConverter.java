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

import com.itworx.vaspp.datacollection.util.converters.VSSMDiskConverter.Device;

public class USSDInteractiveRequestsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List > dateVsRequestsVSresponse=new HashMap<String,List>() ;
	private Map  <String ,Long > dateVsRequests=new HashMap<String,Long>() ;
	private Map  <String ,String  > mobileVsData=new HashMap<String,String>() ;
public USSDInteractiveRequestsConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDInteractiveRequests convert - started converting input files");
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
					.debug("USSDInteractiveRequests.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			
			while (inputStream.ready()) {
				String msisdn="";
				line = inputStream.readLine();
				if(line == null||line.equals("")||line.length()<24){
					continue;
				}
				try{
					
				//msisdn=(line.split("\\[")[1]).split("\\]")[0].toString()!=null?(line.split("\\[")[1]).split("\\]")[0].toString():"";
				msisdn=Utils.stringBetween(line, "[", "]");
				if(msisdn==null)
					continue;
				/*requests*/
				
				String reqName = Utils.stringBetween(line, "Request:", "[op");
				if(reqName!=null)
					reqName=reqName.trim();
				if(reqName!=null&&(line.endsWith("[op:18]")||"407".equals(reqName)||"90".equals(reqName)))
				{
					date=getDate(line.substring(0,24));
						if(reqName.contains(","))
						{
							reqName=reqName.replaceAll(",", ";");
							
							
						}
						
						String key=date+","+reqName;
						mobileVsData.put(msisdn, key);
							
						  boolean found =false;
							if(dateVsRequests.containsKey(key))
							{
								Long reqCount=(Long)dateVsRequests.get(key);
								dateVsRequests.remove(key);
								dateVsRequests.put(key, reqCount+1);
							}
							else
							{
								dateVsRequests.put(key, new Long(1));
							}
						
					
					
					}else if(line.contains("Response:")&&line.matches(responseRegex))/*counting the responses*/
					{
						if(mobileVsData.containsKey(msisdn))
						{   
							
							boolean found = false;
							String messahes[]=line.split("-868-");
							String respMessage=(messahes[1].split("\\[")[0]).split("Response:")[1];
							if(respMessage.contains(","))
								respMessage=respMessage.replace(",", ";");
							String responseKey=mobileVsData.get(msisdn);
							mobileVsData.remove(msisdn);
							if(dateVsRequestsVSresponse.containsKey(responseKey))
							{
								List arryList=dateVsRequestsVSresponse.get(responseKey);
								for(int obj=0;obj<arryList.size();obj++)
								{
									RespCount resp=(RespCount)arryList.get(obj);
									if(resp.getResponseName().equalsIgnoreCase(respMessage))
									{
										resp.setResponseCount(resp.getResponseCount()+1);
										arryList.remove(resp);
										arryList.add(resp);
										found= true;
										break;
									}
								}
								if(!found)
								{
									RespCount resp = new RespCount ();
									resp.setResponseCount(1);
									resp.setResponseName(respMessage);
									arryList.add(resp);
								}
								dateVsRequestsVSresponse.remove(responseKey);
								dateVsRequestsVSresponse.put(responseKey, arryList);
							
								
							}
							else
							{
								List obj = new ArrayList();
								RespCount resp = new RespCount ();
								resp.setResponseCount(1);
								resp.setResponseName(respMessage);
								obj.add(resp);
								dateVsRequestsVSresponse.put(responseKey, obj);
								
							}
						}
					}
				}
				catch(ParseException exc){ logger.error(exc) ; continue ;}
				catch(ArrayIndexOutOfBoundsException exc){ logger.error(exc) ; continue ;}
				
			}
		}//end of the file
		Iterator it =dateVsRequests.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			Long reqCount=dateVsRequests.get(key);
			if(dateVsRequestsVSresponse.containsKey(key))
			{
				long count=0;
				List respList=dateVsRequestsVSresponse.get(key);
				for(int index=0;index<respList.size();index++)
				{
					count=count+((RespCount)respList.get(index)).getResponseCount();
				}
				if(count==reqCount.longValue())/*it means this requests has all it's responses */
				{
					for(int index=0;index<respList.size();index++){
					outputStream.write(key+","+((RespCount)respList.get(index)).getResponseName()+","+((RespCount)respList.get(index)).getResponseCount());
					outputStream.newLine();
					}
				}
				else if(count<reqCount.longValue())/*this means there is request without responses */
				{
					for(int index=0;index<respList.size();index++){
						outputStream.write(key+","+((RespCount)respList.get(index)).getResponseName()+","+((RespCount)respList.get(index)).getResponseCount());
						outputStream.newLine();
						}
					long result =reqCount-count;
					outputStream.write(key+","+"Null"+","+result);
					outputStream.newLine();
				}
			}else/*requests has no responses */
			{
				outputStream.write(key+","+"Null"+","+reqCount);
				outputStream.newLine();
			}
			   
			
		     
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDInteractiveRequests.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDInteractiveRequests.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDInteractiveRequests.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDInteractiveRequests.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		
	 	//PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
	    String path ="D:\\build\\pahse8\\build\\DataCollection";
		PropertyReader.init(path);
		USSDInteractiveRequestsConverter s = new USSDInteractiveRequestsConverter();
		File[] input = new File[1];
		//input[0]=new File("D:\\build\\VASPortal\\DataCollection\\browser_868_2010070619.log");
		input[0]=new File("D:\\build\\pahse8\\build\\DataCollection\\browser_868_2010102506.log");
		s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	/*
	String line = "2010-07-12 08:00:06.887 [602022025577454] -868- Request: 90 [op:64]";
	String date = line.substring(0,23);
	String name = Utils.stringBetween(line, "Request:", "[op");
	System.out.println(date);
	System.out.println(name);
	
	
	String reqName=",,";
	reqName=reqName.split(",")[0];
	System.out.println(reqName);*/
	
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
class RespCount
{
public String responseName="Null";
public long responseCount=0;

public String getResponseName() {
	return responseName;
}
public void setResponseName(String responseName) {
	this.responseName = responseName;
}
public long getResponseCount() {
	return responseCount;
}
public void setResponseCount(long responseCount) {
	this.responseCount = responseCount;
}


}

}
