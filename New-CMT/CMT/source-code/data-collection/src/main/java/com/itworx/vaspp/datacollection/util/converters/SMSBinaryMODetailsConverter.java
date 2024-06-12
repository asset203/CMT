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
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class SMSBinaryMODetailsConverter extends AbstractTextConverter{
public SMSBinaryMODetailsConverter()
{
}
private Logger logger;
private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;

public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {

	logger = Logger.getLogger(systemName);

	logger
			.debug("Inside SMSBinaryMODetailsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path,  "SMSMO"+inputFiles[0].getName());
	BufferedWriter outputStream;
	BufferedReader  inputStream;
	try{
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {

			   String command = "./tp_log.pl"+" "+inputFiles[i];

	          /* Process p =Runtime.getRuntime().exec(command);
	           p.waitFor();	*/
	          // BufferedReader  inputStream = new BufferedReader(new FileReader(PropertyReader.getImportedFilesPath()+"/"+inputFiles[i].getName()+"c"));
	           inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	           String line;
	          
	            while(inputStream.ready())
	            {
	            	line= inputStream.readLine();
	            	String date = "";	 					
 					String key="";		
 					String msgHeader;	
 					String origMsc="";
 					String prevAttempts="";
 					String rejCause="";
 					String subResult="";
 					String termNet="";
 					String termCountry="";
 					String origmsisdnRange="";
 					String termMsisdnRange="";
 					String delivResult="";
 					msgHeader=line.split("\\=")[0].trim().split(" ")[line.split("\\=")[0].trim().split(" ").length-1];
	            	if(line.contains("trustedMoFwdSmWithCountryAndNetworkInfo = {") ||
	            			line.contains("suspectMoFwdSmWithCountryAndNetworkInfo = {")
	            			)
	            	{
	            		while(line!=null &&!line.contains("timestamp =")&&!line.contains("rejectInfo =")&&!line.contains("rejectCause =")&&!line.contains(" responseInfo =")&&!line.contains("submissionResult =")&&!line.contains("sccpCgPa")&&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
	            		{line =inputStream.readLine();}
	            		try
	            		{	if(line!=null&&line.contains("timestamp ="))
	            			{
	            		    date = getDate (line.split("timestamp =")[1].trim().split("\'")[1].split("\'")[0].split("\\+")[0]);
	            		    }
	            		 }catch(ParseException exc){ logger.error(exc) ; continue ;}
	            		 catch(java.lang.ArrayIndexOutOfBoundsException e){continue;};
	            		 while(line!=null &&!line.contains("rejectInfo =")&&!line.contains("rejectCause =")&&!line.contains(" responseInfo =")&&!line.contains("submissionResult =")&&!line.contains("sccpCgPa")&&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("rejectInfo ="))
	            		 {
	            			 while(line!=null &&!line.contains("rejectCause =")&&!line.contains(" responseInfo =")&&!line.contains("submissionResult =")&&!line.contains("sccpCgPa")&&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains("rejectCause =")){
	            				
	            				 rejCause=line.split("rejectCause =")[1].trim();
	            			 }
	            			
	            		 }
	            		 while(line!=null &&!line.contains(" responseInfo =")&&!line.contains("submissionResult =")&&!line.contains("sccpCgPa")&&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains(" responseInfo ="))
	            		 {
	            			 while(line!=null &&!line.contains("submissionResult =")&&!line.contains("sccpCgPa")&&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains("submissionResult =")){
	            				
	            				 subResult=line.split("submissionResult =")[1].trim();
	            			 }
	            			
	            		 }
	            		 
	            		 /*******************************/
	            		 boolean CgPafound=false;
		            	 boolean CdPafound=false;
		            	 while(line!=null &&!line.contains("mapMsisdn =")&&!line.contains("smsRecipient =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
	            		// while(line!=null&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{
		            			
		            		
		            		if(line!=null &&line.contains("sccpCgPa")&&!CgPafound)
		            		{
		            			CgPafound=true;
		            			 while(line!=null &&!line.contains("sccpAddress =")&&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
				            		{line =inputStream.readLine();} 
		            			 if(line!=null&&line.contains("sccpAddress ="))
		            			 {
		            				 while(line!=null &&!line.contains("addressInformation =")&&!line.contains("sccpCdPa")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
					            		{line =inputStream.readLine();} 
		            				 if(line!=null&&line.contains("addressInformation ="))
		            				 {
		            					 origMsc=line.split("addressInformation =")[1].trim();
		            				 }
		            			 }
		            			 
		            		}
		            		
		            		 if(line!=null&&line.contains("sccpCdPa")&&!CdPafound)
		            		 {
		            			 CdPafound=true;
		            			 while(line!=null &&!line.contains("country =")&&!line.contains("network =")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
				            		{line =inputStream.readLine();}
		            			 if(line!=null &&line.contains("country ="))
		            			 {
		            				 termCountry=line.split("country =")[1].trim();
		            			 }
		            			 while(line!=null &&!line.contains("network =")&&!line.contains("mapMsisdn =")&&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
				            		{line =inputStream.readLine();}
		            			 if(line!=null &&line.contains("network ="))
		            			 {
		            				 termNet=line.split("network =")[1].trim();
		            			 }
		            		 }
		            		 line =inputStream.readLine();
		            		}
	            		 /**********************************/
	            		
	            		
	            		 while(line!=null &&!line.contains("mapMsisdn =")&&!line.contains("smsRecipient =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null &&line.contains("mapMsisdn ="))
	            		 {
	            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("smsRecipient =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains("gsmAddress ="))
	            			 {
	            				 origmsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);          				 
	            				             				 
	            			 }
	            		 }
	            		 while(line!=null &&!line.contains("smsRecipient =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("smsRecipient ="))
	            		 {
	            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();} 
	            			 if(line!=null&&line.contains("gsmAddress ="))
	            			 {
	            				 if(line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[0].contains("abbreviated"))
	            				 
	            					 termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1];
	            				 else
	            				 termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);          				 
	            				 
	            				             				 
	            			 }
	            		 }
	            		 while(line!=null&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("mtFwdSmToMscResponseInfo ="))
	            		 {
	            			 while(line!=null&&!line.contains(" deliveryResult =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains(" deliveryResult ="))
	            			 {
	            				 delivResult=line.split(" deliveryResult =")[1].trim();
	            			 }
	            		 }
	            		 key=date+","+msgHeader+","+rejCause+","+subResult+","+origMsc+","+termCountry+","+termNet+","+origmsisdnRange+","+termMsisdnRange+","+delivResult+","+prevAttempts;
	            		
	            			 
	            			 
	            		 if(dateVsCount.containsKey(key))
	            		 {
	            			 long count=dateVsCount.get(key);
	            			 dateVsCount.put(key, count+1);
	            		 }else
	            		 {
	            			 dateVsCount.put(key, new Long (1));
	            		 }
	            	}
	            	else if (line.contains("amsDeliveryAttemptForMoAtWithCountryAndNetworkInfo = {"))
	            	{
	            		while(line!=null &&!line.contains("timestamp =")&&!line.contains("originatorAddress =")&&!line.contains("gsmAddress =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
	            		{line =inputStream.readLine();}
	            		try
	            		{	if(line!=null&&line.contains("timestamp ="))
	            			{
	            		    date = getDate (line.split("timestamp =")[1].trim().split("\'")[1].split("\'")[0].split("\\+")[0]);
	            		    }
	            		 }catch(ParseException exc){ logger.error(exc) ; continue ;}
	            		 catch(java.lang.ArrayIndexOutOfBoundsException e){continue;};
	            		 while(line!=null &&!line.contains("originatorAddress =")&&!line.contains("gsmAddress =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("originatorAddress ="))
	            		 {
	            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null &&line.contains("gsmAddress ="))
	            			 {
	            				 origmsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);
	            				 
	            			 }
	            			 
	            		 }
	            		 while(line!=null &&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("recipientAddress ="))
	            		 {
	            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains("gsmAddress ="))
	            			 {
	            				 
	            				 if(line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[0].contains("abbreviated"))
	            					 termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1];
	            				 else	            					 
	            				 termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);
	            			 }
	            			 while(line!=null &&!line.contains("country =")&&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null &&line.contains("country ="))
	            			 {
	            				 termCountry=line.split("country =") [1].trim();
	            			 }
	            			 while(line!=null &&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null &&line.contains("network ="))
	            			 {
	            				 termNet=line.split("network =") [1].trim();
	            			 }
	            		 }
	            		 
	            		 while(line!=null &&!line.contains("originatedMscAddress =")&&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("originatedMscAddress ="))
	            		 {
	            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null && line.contains("gsmAddress ="))
	            			 {
	            				 origMsc=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1];
	            			 }
	            		 }
	            		 while(line!=null &&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("mtFwdSmToMscResponseInfo ="))
	            		 {
	            			 while(line!=null &&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
	            			 if(line!=null&&line.contains(" deliveryResult ="))
	            			 {
	            				 delivResult=line.split(" deliveryResult =")[1].trim();
	            			 }
	            		 }
	            		 while(line!=null &&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
	            		 if(line!=null&&line.contains("numberOfPreviousAttempts ="))
	            		 {
	            			 prevAttempts=line.split("numberOfPreviousAttempts =") [1].trim();
	            		 }
	            		 key=date+","+msgHeader+","+rejCause+","+subResult+","+origMsc+","+termCountry+","+termNet+","+origmsisdnRange+","+termMsisdnRange+","+delivResult+","+prevAttempts;
	            		 
	            		 if(dateVsCount.containsKey(key))
	            		 {
	            			 long count=dateVsCount.get(key);
	            			 dateVsCount.put(key, count+1);
	            		 }else
	            		 {
	            			 dateVsCount.put(key, new Long (1));
	            		 }
	            	}
	         	else if (line.contains("amsDeliveryAttemptForMoMtWithCountryAndNetworkInfo"))
	            	{
	         		while(line!=null &&!line.contains("timestamp =")&&!line.contains("originatorAddress =")&&!line.contains("gsmAddress =")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
            		{line =inputStream.readLine();}
            		try
            		{	if(line!=null&&line.contains("timestamp ="))
            			{
            		    date = getDate (line.split("timestamp =")[1].trim().split("\'")[1].split("\'")[0].split("\\+")[0]);
            		    }
            		 }catch(ParseException exc){ logger.error(exc) ; continue ;}
            		 catch(java.lang.ArrayIndexOutOfBoundsException e){continue;};
            		 while(line!=null &&!line.contains("originatorAddress =")&&!line.contains("gsmAddress =")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
             		{line =inputStream.readLine();}
            		 if(line!=null&&line.contains("originatorAddress ="))
            		 {
            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("network =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                  		{line =inputStream.readLine();} 
            			 if(line!=null&&line.contains("gsmAddress ="))
            			 {
            				 origmsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);
            			 }
            			
         				 while(line!=null &&!line.contains("network =")&&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                 		{line =inputStream.readLine();} 
         				 if(line!=null&&line.contains("network ="))
         				 {
         					 if(line.split("network =")[1].trim().split("\\'")[1].split("\\'")[0].equalsIgnoreCase("EG-Vodafone"))
         						 
         						 
         					 {
         						 while(line!=null &&!line.contains("recipientAddress =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                          		{line =inputStream.readLine();} 
         						 if(line!=null&&line.contains("recipientAddress ="))
         						 {
         							while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                              		{line =inputStream.readLine();} 
         							if(line!=null&&line.contains("gsmAddress ="))
         							{
         								
         								if(line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[0].contains("abbreviated"))
         									termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1];
         								else
       	            				    termMsisdnRange=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0,7);
         							}
         							while(line!=null &&!line.contains("country =")&&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                              		{line =inputStream.readLine();} 
         							if(line!=null&&line.contains("country ="))
         							{
         								termCountry=line.split("country =") [1].trim();
         							}
         							while(line!=null &&!line.contains("network =")&&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                              		{line =inputStream.readLine();} 
         							if(line!=null&&line.contains("network ="))
         							{
         								termNet=line.split("network =")[1].trim();
         							}
         							
         							
         						 }
         						while(line!=null &&!line.contains("originatedMscAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                          		{line =inputStream.readLine();} 
         						if(line!=null &&line.contains("originatedMscAddress ="))
         						{
         							while(line!=null &&!line.contains(" gsmAddress =")&&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                              		{line =inputStream.readLine();}
         							if(line!=null &&line.contains(" gsmAddress ="))
         							{
         								origMsc=line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1];
         							}
         						}
         						while(line!=null &&!line.contains("outboundMt =")&&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                          		{line =inputStream.readLine();} 
         						if(line!=null&&line.contains("outboundMt ="))
         						{
         							while(line!=null &&!line.contains("sriSmResponseInfo =")&&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                              		{line =inputStream.readLine();} 
         							if(line!=null &&line.contains("sriSmResponseInfo ="))
         							{
         								while(line!=null &&!line.contains("queryResult =")&&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                                  		{line =inputStream.readLine();} 
         								if(line!=null&&line.contains("queryResult ="))
         								{
         									if(line.split("queryResult =")[1].trim().equalsIgnoreCase("success(0)"))
         									{
         										while(line!=null &&!line.contains("mtFwdSmToMscResponseInfo =")&&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                                          		{line =inputStream.readLine();} 
         										if(line!=null&&line.contains("mtFwdSmToMscResponseInfo ="))
         										{
         											while(line!=null &&!line.contains(" deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                                              		{line =inputStream.readLine();} 
         											if(line!=null&&line.contains(" deliveryResult ="))
         											{
         												delivResult=line.split(" deliveryResult =")[1].trim();
         											}
         										}
         									}
         									else
         									{
         										delivResult=line.split("queryResult =")[1].trim();
         									}
         								}
         							}
         						}
         						while(line!=null &&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
                          		{line =inputStream.readLine();} 
         						if(line!=null&&line.contains("numberOfPreviousAttempts ="))
         						{
         							 prevAttempts=line.split("numberOfPreviousAttempts =") [1].trim();
         						}
         						 key=date+","+msgHeader+","+rejCause+","+subResult+","+origMsc+","+termCountry+","+termNet+","+origmsisdnRange+","+termMsisdnRange+","+delivResult+","+prevAttempts;
         						 
        	            		 if(dateVsCount.containsKey(key))
        	            		 {
        	            			 long count=dateVsCount.get(key);
        	            			 dateVsCount.put(key, count+1);
        	            		 }else
        	            		 {
        	            			 dateVsCount.put(key, new Long (1));
        	            		 }
         						
         					 }
         				 }
            		 }
            		 
            	
            		 
            		 
            		 
	            	}

	            
	            }
	            inputStream.close();
		}//end of files
		Iterator it=dateVsCount.keySet().iterator();

		while(it.hasNext())
		{
			Object key=it.next();
			long count=dateVsCount.get(key);			
			outputStream.write(key+","+count);
			outputStream.newLine();

		}
		outputStream.close();
		outputFiles[0]=outputFile;

   }/*catch(InterruptedException  e)
   {
	   e.printStackTrace();
   }*/
	catch(IllegalThreadStateException  e)
   {
	   e.printStackTrace();
   }

   catch (FileNotFoundException e) {
		logger
				.error("SMSBinaryMODetailsConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch(IOException e)
    {
    	e.printStackTrace();
    }

	logger
			.debug("SMSBinaryMODetailsConverter.convert() - finished converting input files successfully ");
	return outputFiles;
}
public static void  main(String ag[])
{

	 try {
            String path ="D:\\build\\phase10\\DataCollection";
    		PropertyReader.init(path);
    		SMSBinaryMODetailsConverter s = new SMSBinaryMODetailsConverter();
    		File[] input = new File[1];
    		input[0]=new File("D:\\build\\phase10\\DataCollection\\log_mo_mt_hq-rtr03_20110908_070115_158.datc");
    		s.convert(input,"Maha_Test");
            System.out.println("finish file");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");


	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
