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
import com.sun.mail.iap.Response;


public class USSDDiameterConnector extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> hourVsReqRespObject=new HashMap<String,List>();
	private Map  <String ,Request> requestsWithoutResponses=new HashMap<String,Request>();
	private Map  <String ,String> msisdnVSdate;
	private Map  <String ,String> msisdnVShourtCodes;
	/*private Map  <String ,List> reqVSHour=new HashMap<String,List>() ;
	private Map  <String ,String> msisdnVSShortCode=new HashMap<String,String>() ;
	private Map  <String ,List> responseVsHour=new HashMap<String,List>() ;
	private Map  <String ,List> responseWorkArround=new HashMap<String,List>() ;
	private Map  <String ,Long> responses=new HashMap<String,Long>() ;
	private Map  <String ,Long> requests=new HashMap<String,Long>() ;*/

public USSDDiameterConnector()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDDiameterConnector convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("USSDDiameterConnector.convert() - converting file "
							+ inputFiles[i].getName());
			msisdnVSdate=new HashMap<String,String>() ;
			msisdnVShourtCodes=new HashMap<String,String>() ;
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String shortCode;
			String resultCode=" ";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				
				if(line.contains(",")&&line.split(",").length>=3)
				{
					try
					{
						
					    date=getDate(line.split(",")[0]);	
					   // System.out.println(date);
					    String msisdn =Utils.stringBetween(line.split(",")[2], "[", "]");
					  
					   if (line.contains("AppRequest:"))
					    {
						  
						  if(line.contains("SERVICE_PARAM_3$"))
						  {
							  shortCode=Utils.stringBetween(line.split(",")[2], "SERVICE_PARAM_3$", ";");
						  }
						  else
						  {
							  shortCode=" "; 
						  }
						  if(!msisdnVSdate.containsKey(msisdn))
						   {
							  msisdnVSdate.put(msisdn, msisdn+","+date);
						   }
						 
						  boolean hourNotFound=false;
						  if(hourVsReqRespObject.containsKey(date))
						  {
							  List objs=hourVsReqRespObject.get(date);
							  boolean found =false;
							  for(int k=0;k<objs.size();k++)
							  {
								  ReqResp reqresp=  (ReqResp)objs.get(k);
								  if(reqresp.getShortCode()!=null&&reqresp.getShortCode().equalsIgnoreCase(shortCode))
								  {
									  /*reqresp.setReqCount(reqresp.getReqCount()+1);
									  objs.remove(k);
									  objs.add(reqresp);*/
									  msisdnVShourtCodes.put(msisdn, shortCode);
									  found=true;
									  break;
								  }
							  }
							  if(!found)
							  {
								  msisdnVShourtCodes.put(msisdn, shortCode);
								 /* ReqResp reqresp= new ReqResp ();
								  reqresp.setReqCount(1);
								  reqresp.setShortCode(shortCode);
								  objs.add(reqresp);*/
							  }
							  hourVsReqRespObject.remove(date);
							  hourVsReqRespObject.put(date, objs);
						  }
						  else
						  {
							  List objs= new ArrayList();
							  ReqResp reqresp= new ReqResp ();
							  reqresp.setReqCount(1);
							  reqresp.setShortCode(shortCode);
							  objs.add(reqresp);
							  hourVsReqRespObject.put(date, objs);
							  hourNotFound=true;
						  }
						 if(!hourNotFound)
						 {
							 
							 if(!requestsWithoutResponses.containsKey(msisdn))
							  {
								  Request req= new Request();
								  req.setReqCount(1);
								  req.setShortCode(shortCode);
								  req.setDate(date);
								  requestsWithoutResponses.put(msisdn, req);
							  }
						 }
					   }
					    else if (line.contains("Request to Diameter server failed") ||line.contains("AppResponse ErrCode:"))
					    {
					    	if(line.contains("Request to Diameter server failed")&&line.contains("Result-Code :"))
					    	{
					    		resultCode=line.split("Result-Code :")[1].trim();
					    	}
					    	else if(line.contains("AppResponse ErrCode:")&&line.contains("ErrMessage:"))
					    	{
					    		resultCode=line.split("AppResponse ErrCode:")[1].split("ErrMessage:")[0].trim();
					    	}
					    	if(msisdnVSdate.containsKey(msisdn))//responses with requests
					    	{
					    		if(hourVsReqRespObject.containsKey(date))
					    		{   
					    			String comparedShortCode=null;
					    			List objs=hourVsReqRespObject.get(date);
					    			if(msisdnVShourtCodes.containsKey(msisdn)){
					    				comparedShortCode=msisdnVShourtCodes.get(msisdn);
					    				msisdnVShourtCodes.remove(msisdn);
					    				requestsWithoutResponses.remove(msisdn);
					    				}
					    			boolean found=false;
					    			boolean responsewithoutreqbeforeme=true;
					    			for(int k=0;k<objs.size();k++)
					    			{
					    				ReqResp reqresp=(ReqResp)objs.get(k);
					    				 if(reqresp.getResultCode()==null&&reqresp.getReqCount()!=0&&comparedShortCode==null)//new resp obj
					    				{
					    					reqresp.setResultCode(resultCode);
					    					reqresp.setRespCount(reqresp.getRespCount()+1);
					    					objs.remove(k);
					    					objs.add(reqresp);
					    					break;
					    				}
					    				
					    				
					    				
					    				 
					    				 else if (reqresp.getShortCode()!=null&&reqresp.getShortCode().equalsIgnoreCase(comparedShortCode)&&reqresp.getResultCode()!=null&&reqresp.getResultCode().equalsIgnoreCase(resultCode))
					    				 {
					    					found=true;
					    					 reqresp.setReqCount(reqresp.getReqCount()+1);
					    					 reqresp.setRespCount(reqresp.getRespCount()+1);
					    					 objs.remove(k);
						    				 objs.add(reqresp);
						    				 break;
					    				 }
					    				 else if((!found)&&(k==objs.size()-1)&&reqresp.getShortCode()!=null&&reqresp.getShortCode().equalsIgnoreCase(comparedShortCode)&&reqresp.getResultCode()!=null&&(!reqresp.getResultCode().equalsIgnoreCase(resultCode)))
					    				 {
					    					 ReqResp obj= new ReqResp();
					    					 obj.setReqCount(1);
					    					 obj.setRespCount(1);
					    					 obj.setResultCode(resultCode);
					    					 obj.setShortCode(comparedShortCode);
					    					 objs.add(obj);
						    				 break;
					    				 }
					    				 else if((k==objs.size()-1)&&!(comparedShortCode!=null&&reqresp.getShortCode()!=null&&reqresp.getShortCode().equalsIgnoreCase(comparedShortCode)&&reqresp.getReqCount()==0))
					    				 {
					    					 ReqResp reqobj= new ReqResp();
					    					 reqobj.setReqCount(1);
					    					 reqobj.setShortCode(comparedShortCode);
					    					 reqobj.setRespCount(1);
					    					 reqobj.setResultCode(resultCode);
					    					 objs.add(reqobj);
					    					 break;
					    				 }
					    				 else if((!found)&&(k==objs.size()-1)&&reqresp.getResultCode().equalsIgnoreCase(resultCode)&&(!reqresp.getShortCode().equalsIgnoreCase(comparedShortCode)))
					    				 {
					    					 ReqResp obj= new ReqResp();
					    					 obj.setReqCount(1);
					    					 obj.setRespCount(1);
					    					 obj.setResultCode(resultCode);
					    					 obj.setShortCode(comparedShortCode);
					    					 objs.add(obj);
						    				 break;
					    				 }
					    				 else if(reqresp.getShortCode()==null&&reqresp.getReqCount()==0&&reqresp.getResultCode().equalsIgnoreCase(resultCode)&&comparedShortCode==null)
					    				 {
					    					 				    					
						    				 reqresp.setRespCount(reqresp.getRespCount()+1);
						    				 objs.remove(k);
						    				 objs.add(reqresp);
						    				 break;
					    				 }
					    				 else if(reqresp.getShortCode()==null&&reqresp.getReqCount()==0&&(!reqresp.getResultCode().equalsIgnoreCase(resultCode))&&comparedShortCode==null)
					    				 {
					    					
					    					ReqResp reqobj= new ReqResp();
					    					reqobj.setResultCode(resultCode);
					    					reqobj.setRespCount(reqobj.getRespCount()+1);
						    				objs.add(reqobj);
						    				 break;
					    				 }
					    				 else if(reqresp.getResultCode()!=null&&reqresp.getResultCode().equalsIgnoreCase(resultCode)&&reqresp.getReqCount()!=0&&comparedShortCode==null)
						    				{
						    					reqresp.setRespCount(reqresp.getRespCount()+1);
						    					objs.remove(k);
						    					objs.add(reqresp);
						    					break;
						    				}
					    			}
					    			
					    			hourVsReqRespObject.put(date, objs);
					    		}
					    		else//it means response with diff hour in the same file
					    		{
					    			List objs= new ArrayList();
					    			ReqResp reqResp= new ReqResp();
					    			reqResp.setRespCount(1);
					    			
					    			reqResp.setResultCode(resultCode);
					    			objs.add(reqResp);
					    			hourVsReqRespObject.put(date, objs);
					    		}
					    		msisdnVSdate.remove(msisdn);
					    	}
					    	else//responses without requests 
					    	{
					    		if(hourVsReqRespObject.containsKey(date))
					    		{
					    			List obj=hourVsReqRespObject.get(date);
					    			boolean fount = false;
					    			for(int k=0;k<obj.size();k++)
					    			{
					    				ReqResp res=(ReqResp)obj.get(k);
					    				if(res.getResultCode()!=null&&res.getResultCode().equalsIgnoreCase(resultCode)&&res.getReqCount()==0)
					    				{
					    					fount=true;
					    				    res.setRespCount(res.getRespCount()+1);
					    				    obj.remove(k);
					    				    obj.add(res);
					    					break;
					    				}
					    			}
					    			if(!fount )
					    			{
					    				ReqResp reqResp= new ReqResp();
						    			reqResp.setRespCount(1);
						    			reqResp.setResultCode(resultCode);
						    			obj.add(reqResp);
					    			}
					    			hourVsReqRespObject.put(date, obj);
					    		}
					    		else
					    		{
					    		List objs= new ArrayList();
				    			ReqResp reqResp= new ReqResp();
				    			reqResp.setRespCount(1);
				    			reqResp.setResultCode(resultCode);
				    			objs.add(reqResp);
				    			hourVsReqRespObject.put(date, objs);
					    		}
					    	}
					    }
					} catch(ParseException exc){ /*logger.error(exc) ;*/ continue ;}
				}
			}
		
	
}
		
		Iterator it2=requestsWithoutResponses.keySet().iterator();
		List reqList = new ArrayList();
		while(it2.hasNext())
		{
			Object key=it2.next();
		//	System.out.println(key.toString());
			Request req1=(Request)requestsWithoutResponses.get(key);			
			reqList.add(req1);
		}
       for(int i=0;i<reqList.size();i++)
       {
    	   Request req1=(Request)reqList.get(i);
    	   long req=0;
    	   boolean found =false;
    	   for(int j=i+1;j<reqList.size();j++)
    	   {
    		   Request req2=(Request)reqList.get(j);
    		   if(req2.getDate().equalsIgnoreCase(req1.getDate())&&req2.getShortCode().equalsIgnoreCase(req1.getShortCode()))
    		   {
    			   req=req1.getReqCount()+req2.getReqCount();
    			   found=true;
    			   reqList.remove(j);
    		   }
    	   }
    	   if(found)
    	   {
    		   found= false;
    		     outputStream.write(req1.getDate()+","+req1.getShortCode()+", ,"+req+",0");
				 outputStream.newLine();
    	   }
    	   else
    	   {
    		   outputStream.write(req1.getDate()+","+req1.getShortCode()+", ,"+req1.getReqCount()+",0");
				 outputStream.newLine();
    	   }
    	   
       }
		Iterator it=hourVsReqRespObject.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			List objs=hourVsReqRespObject.get(key);
			for(int k=0;k<objs.size();k++)
			{
				ReqResp reqresp=(ReqResp)objs.get(k);
				if(reqresp.getShortCode()==null)
					reqresp.setShortCode(" ");
				if(reqresp.getResultCode()==null)
					reqresp.setResultCode(" ");
				outputStream.write(key+","+reqresp.getShortCode()+","+reqresp.getResultCode()+","+reqresp.getReqCount()+","+reqresp.getRespCount());
				 outputStream.newLine();
			}
			 
		}
		
			
		
inputStream.close();

outputStream.close();
outputFiles[0]=outputFile;
logger.debug("USSDDiameterConnector.convert() - finished converting input files successfully ");

}
catch (FileNotFoundException e) {
	logger
			.error("USSDDiameterConnector.convert() - Input file not found "
					+ e);
	throw new ApplicationException(e);
} catch (IOException e) {
	logger
			.error("USSDDiameterConnector.convert() - Couldn't read input file"
					+ e);
	throw new ApplicationException(e);
}
logger
		.debug("USSDDiameterConnector.convert() - finished converting input files successfully ");
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
		USSDDiameterConnector s = new USSDDiameterConnector();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2011052515.log");
	//input[1]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2010112814.log");		
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class ReqResp
{
	String shortCode;
	String resultCode;
	long reqCount;
	long respCount;
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
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
