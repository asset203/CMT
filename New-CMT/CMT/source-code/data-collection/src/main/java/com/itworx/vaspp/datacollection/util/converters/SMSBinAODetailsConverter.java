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

public class SMSBinAODetailsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;
	public SMSBinAODetailsConverter()
	{
		
	}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {

		logger = Logger.getLogger(systemName);

		logger
				.debug("Inside SMSBinAODetailsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path,  "SMSAO"+inputFiles[0].getName());
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

		            	if(line.contains("receivedSubmitSmWithCountryAndNetworkInfo = {") ||
		            			line.contains("receivedDeliverSmWithCountryAndNetworkInfo = {")||
		            			line.contains("receivedNotificationWithCountryAndNetworkInfo = {")||
		            			line.contains("amsDeliveryAttemptForAoMtWithCountryAndNetworkInfo = {"))
		            	{
		            		 
		 					String date = "";
		 					String applicationName  ="";
		 					String key="";
		 					String terminatingNw ="";
		 					String terminatingCont ="";
		 					String gsmAdd="";
		 					String msgHeader;
		 					String subResult="";
		 					String deliveryResult="";
		 					String numberOfPrevAtt="";
		            		msgHeader=line.split("\\=")[0].trim().split(" ")[line.split("\\=")[0].trim().split(" ").length-1];
		            		
		            		while(line!=null &&!line.contains("timestamp =")&&!line.contains("submissionResult =")&&!line.contains("applicationName =")&&!line.contains("recipientAddress =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
		            		try
		            		{	if(line!=null&&line.contains("timestamp ="))
		            			{
		            		    date = getDate (line.split("timestamp =")[1].trim().split("\'")[1].split("\'")[0].split("\\+")[0]);
		            		    }
		            		 }catch(ParseException exc){ logger.error(exc) ; continue ;}
		            		 catch(java.lang.ArrayIndexOutOfBoundsException e){continue;};
		            		 while(line!=null &&!line.contains("submissionResult =")&&!line.contains("applicationName =")&&!line.contains("recipientAddress =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
		            		 if(line!=null&&line.contains("submissionResult ="))
		            		 {
		            			 subResult= line.split("submissionResult =")[1].trim();
		            			 if(subResult.contains(","))
		            				 subResult=subResult.replaceAll(",", "_");
		            				 
		            		 }
		            		
		            		 while(line!=null &&!line.contains("applicationName =")&&!line.contains("recipientAddress =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
		            		 if(line!=null &&line.contains("applicationName ="))
		            		 {
		            			 applicationName=line.split("applicationName =")[1].split("\\'")[1].split("\\'")[0];
		            			 if(applicationName.contains(","))
		            				 applicationName=applicationName.replace(",", "_");
		            		 }
		            		 
		            		 while(line!=null &&!line.contains("recipientAddress =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
		            		 if(line!=null &&line.contains("recipientAddress ="))
		            		 {
		            			 while(line!=null &&!line.contains("gsmAddress =")&&!line.contains("country =")&&!line.contains("network =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
				            	   {line =inputStream.readLine();}
		            			 if(line!=null &&line.contains("gsmAddress ="))
			            		 {
		            				 if(line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].contains("alphanumeric"))
		            				 {
		            					 gsmAdd= line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0];
		            				 }else
		            				 {
		            					 gsmAdd= line.split("gsmAddress =")[1].split("\\'")[1].split("\\'")[0].split(" ")[1].substring(0, 7);
		            				 }
		            				if(gsmAdd.contains(","))
		            					gsmAdd =gsmAdd.replaceAll(",", "_");
			            		 }
		            			 while(line!=null &&!line.contains("country =")&&!line.contains("network =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
				            	   {line =inputStream.readLine();}
		            			 if(line!=null &&line.contains("country ="))
			            		 {
		            				 terminatingCont= line.split("country =")[1].split("\\'")[1].split("\\'")[0];
		            				 
			            		 }
		            			 
		            			 while(line!=null &&!line.contains("network =")&&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
				            	   {line =inputStream.readLine();}
		            			 if(line!=null &&line.contains("network ="))
			            		 {
		            				 terminatingNw= line.split("network =")[1].split("\\'")[1].split("\\'")[0];
		            				 
			            		 }
		            		 }
		            		 while(line!=null &&!line.contains("deliveryResult =")&&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
			            		{line =inputStream.readLine();}
                            if(line!=null&&line.contains("deliveryResult ="))
                            {
                            	deliveryResult=line.split("deliveryResult =")[1].trim();
                            }
		            		 
                            while(line!=null &&!line.contains("numberOfPreviousAttempts =")&&!line.contains("File:"))
		            		{line =inputStream.readLine();}
                            if(line!=null&&line.contains("numberOfPreviousAttempts ="))
                            {
                            	numberOfPrevAtt=line.split("numberOfPreviousAttempts =")[1].trim();
                            }
                            
                            key=date+","+msgHeader+","+subResult+","+applicationName+","+gsmAdd+","+terminatingCont+","+terminatingNw+","+deliveryResult+","+numberOfPrevAtt;
                            if(dateVsCount.containsKey(key))
                            {
                            	long count =dateVsCount.get(key);
                            	dateVsCount.put(key, count+1);
                            }else
                            {
                            	dateVsCount.put(key, new Long (1));
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
					.error("SMSBinAODetailsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch(IOException e)
	    {
	    	e.printStackTrace();
	    }

		logger
				.debug("SMSBinAODetailsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}
	public static void  main(String ag[])
	{

		 try {
	            String path ="D:\\ITWorx\\Projects\\VFE_VAS_VNPP_2011_Phase2\\Trunk\\SourceCode\\DataCollection";
	    		PropertyReader.init(path);
	    		SMSBinAODetailsConverter s = new SMSBinAODetailsConverter();
	    		File[] input = new File[1];
	    		input[0]=new File("D:\\ITWorx\\Projects\\VFE_VAS_VNPP_2011_Phase2\\Trunk\\SourceCode\\Deployment\\04-09-2011\\log_mo_mt_mbegvf-rtr02_20110904_104532_008.datc");
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
