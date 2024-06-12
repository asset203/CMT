package com.itworx.vaspp.datacollection.util.converters;

import java.awt.List;
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
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.USSDINVlrRequestsConverter.ReqRespCount;

public class ServicesRxConverter extends AbstractTextConverter{
	Logger logger;
	private Map  <String ,Long  > keyVsReqCount=new HashMap<String,Long>() ;
	private Map  <String ,Long > msisdnVsKey=new HashMap<String,Long>() ;
	private Map  <String ,ArrayList> KeyVsRespCount=new HashMap<String,ArrayList>() ;
	
public ServicesRxConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside ServicesRxConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("ServicesRxConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String msisdn ="";
			String key="";
			
			
			while (inputStream.ready()) {
				
				line = inputStream.readLine();
				
				if(line.contains(","))
				{
					try {
					date=getDate (line.split(",")[0]);
					if(line.contains("New AppRequest"))
					{
						 if(line.contains("]")&&line.contains("["))
						 {
					       msisdn= line.split(",")[2].split("\\[")[1].split("\\]")[0];
					     
					       if(msisdnVsKey.containsKey(msisdn+","+date))
					       {
					    	  long count= msisdnVsKey.get(msisdn+","+date);
					    	  msisdnVsKey.remove(msisdn+","+date);
					    	  msisdnVsKey.put(msisdn+","+date, count +1);
					       }
					       else
					       {
					    	   msisdnVsKey.put(msisdn+","+date, new Long(1));
					       }
					       if(keyVsReqCount.containsKey(date+","+msisdn))
					       {
					    	   long count=keyVsReqCount.get(date+","+msisdn);
					    	   keyVsReqCount.remove(date+","+msisdn);
					    	   keyVsReqCount.put(date+","+msisdn, count+1);
					       }
					       else
					       {
					    	   keyVsReqCount.put(date+","+msisdn, new Long (1));
					       }
						 }
						 
					}
					else if (line.contains("AppResponse")&&line.contains("status="))
					{
						if(line.contains("]")&&line.contains("["))
						 {
					       msisdn= line.split(",")[2].split("\\[")[1].split("\\]")[0];
					     
					       if(msisdnVsKey.containsKey(msisdn+","+date))
					       {
					    	   long count =msisdnVsKey.get(msisdn+","+date);
					    	   msisdnVsKey.remove(msisdn+","+date);
					    	   if(count >0)
					    	   {
					    		   msisdnVsKey.put(msisdn+","+date, count-1);
					    	   
					    	   String satus =line.split("status=")[1].split("\\'")[1].split("\\'")[0];
					    	   key=date+","+msisdn;
					    	   
					    		  if(KeyVsRespCount.containsKey(key))
					    		  {
					    			  ArrayList respCountList =(ArrayList)KeyVsRespCount.get(key);
					    			  boolean found= false;
					    			  for (int l=0;l<respCountList.size();l++)
					    			  {
					    				  if(((RespObject)respCountList.get(l)).getRespKey().equalsIgnoreCase(key+","+satus))
					    				  {
					    					  RespObject obj= (RespObject)respCountList.get(l);
					    					  obj.setRespCount(obj.getRespCount()+1);
					    					  respCountList.remove(obj);
					    					  respCountList.add(obj);
					    					  found=true;
					    					  break;
					    				  }					    				 
					    			  }
					    			  if(!found)
					    			  {
					    				  RespObject obj= new RespObject ();
					    				  obj.setRespKey(key+","+satus);
					    				  obj.setRespCount(1);
					    				  respCountList.add(obj);
					    			  }
					    			  KeyVsRespCount.remove(key);
					    			  KeyVsRespCount.put(key, respCountList);
					    		  }
					    		  else
					    		  {
					    			  ArrayList respList= new ArrayList();
					    			  RespObject obj = new RespObject();
					    			  obj.setRespCount(1);
					    			  obj.setRespKey(key+","+satus);
					    			  respList.add(obj);
					    			  KeyVsRespCount.put(key, respList);
					    			  
					    		  }
					    	   }
					       }
						 }
					}
					 }catch(ParseException exc){ logger.error(exc) ; continue ;}
				}else
				{continue;}
			}
		}//end of files 
		
		Iterator reqIt =keyVsReqCount.keySet().iterator();
		Iterator respIt=KeyVsRespCount.keySet().iterator();
		
		while(reqIt.hasNext())
		{
			Object reqKey=reqIt.next();
			long totalRespCount=0;
			long reqCount=keyVsReqCount.get(reqKey);
			if(KeyVsRespCount.containsKey(reqKey))
			{
				ArrayList respList=KeyVsRespCount.get(reqKey);
				for(int i=0;i<respList.size();i++)
				{
					totalRespCount=totalRespCount+((RespObject)respList.get(i)).getRespCount();
					outputStream.write(((RespObject)respList.get(i)).getRespKey()+","+((RespObject)respList.get(i)).getRespCount());					
					outputStream.newLine();
				}
				if(totalRespCount<reqCount)
				{
					long resultCount=reqCount-totalRespCount;
					outputStream.write(reqKey+","+"Null"+","+resultCount);					
					outputStream.newLine();
				}
				
			}else// it means req without respnses 
			{
				outputStream.write(reqKey+","+"Null"+","+keyVsReqCount.get(reqKey));					
				outputStream.newLine();
			}
			
		}
		
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("ServicesRxConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("ServicesRxConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("ServicesRxConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("ServicesRxConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		String path ="D:\\build\\phase9\\phase9Builds\\DataCollection";
		PropertyReader.init(path);
		ServicesRxConverter s = new ServicesRxConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\ipcconnector_2011022723.log");
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
			"MM/dd/yyyy");

	
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
class RespObject
{
String respKey="";
public String getRespKey() {
	return respKey;
}
public void setRespKey(String respKey) {
	this.respKey = respKey;
}
public long getRespCount() {
	return respCount;
}
public void setRespCount(long respCount) {
	this.respCount = respCount;
}
long respCount =0;
}
}
