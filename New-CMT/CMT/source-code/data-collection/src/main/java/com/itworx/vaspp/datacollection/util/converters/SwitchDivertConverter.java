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
import com.itworx.vaspp.datacollection.util.converters.ETOPUPPerVlrConverter.ReqResp;

public class SwitchDivertConverter extends AbstractTextConverter {
	private Logger logger;
	//private Map  <String ,Long> hourVSVSCount=new HashMap<String,Long>() ;
	private Map  <String ,Long> keyVSSumCount=new HashMap<String,Long>() ;
	public SwitchDivertConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SwitchDivertConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SwitchDivertConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	        	String line;
				String date = "";
				String key;
				String Switch="";
				String UserAction="";
				long announcementID=0;
				long divertCondition=0;
				//long sum=0;
				long count=0;
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("|")&&line.split("\\|").length >=9)
					{
					if(line.contains("|")&&line.split("\\|").length>= 11)
					{
						
						try
						{	date=getDate(line.split("\\|")[4]);
						    Switch=line.split("\\|")[3]!=null?line.split("\\|")[3]:"";
						    UserAction=line.split("\\|")[10]!=null?line.split("\\|")[10]:"";
						    String next;
						    if(UserAction.equalsIgnoreCase("User Connected"))
						    {
						    	if(line.split("\\|").length==12)
						    	{
						    	next=line.split("\\|")[11]!=null?line.split("\\|")[11]:"";
							    	if(next.contains("**"))
							    	{
							    		UserAction = "User Connected";
							    	}
							    	else
							    	{
							    		UserAction = "User Disconnected";
							    	}
						    	}else if (line.split("\\|").length==11)
						    	{
						    		 UserAction="User Disconnected";
						    	}
						    }
						    divertCondition=Long.parseLong(line.split("\\|")[7]!=null?line.split("\\|")[7]:"0");
						    announcementID=Long.parseLong(line.split("\\|")[8]!=null?line.split("\\|")[8]:"0");
						    //sum=Long.parseLong(line.split("\\|")[6]!=null?line.split("\\|")[6]:"0");
						    
					} catch(ParseException exc){ logger.error(exc) ; continue ;}
					  catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}
					else if((line.contains("|")&&line.split("\\|").length>= 9))
					{
						try
						{
							date=getDate(line.split("\\|")[3]);
						   Switch=line.split("\\|")[2]!=null?line.split("\\|")[2]:"";
						   UserAction=line.split("\\|")[8]!=null?line.split("\\|")[8]:"";
						   
						   if(UserAction.equalsIgnoreCase("User Connected"))
						   {
							   if(line.split("\\|").length==9)
							   {
							   UserAction="User Disconnected";
						       }else if (line.split("\\|").length==10)
						       {
						    	  String next=line.split("\\|")[9]!=null?line.split("\\|")[9]:"";
							    	if(next.contains("**"))
							    	{
							    		UserAction = "User Connected";
							    	}
							    	else
							    	{
							    		UserAction = "User Disconnected";
							    	}
						       }
						   }
						   divertCondition=-999;
						   announcementID=Long.parseLong(line.split("\\|")[6]!=null?line.split("\\|")[6]:"0");
						   //sum=Long.parseLong(line.split("\\|")[5]!=null?line.split("\\|")[5]:"0");
						} 
						catch(ParseException exc){ logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}
					
					key=date+","+Switch+","+UserAction+","+divertCondition+","+announcementID;					
					if(keyVSSumCount.containsKey(key))
					{
						long keycount=keyVSSumCount.get(key);					
						keyVSSumCount.remove(key);
						keyVSSumCount.put(key, keycount+1);
					}else
					{   
						 
						 keyVSSumCount.put(key, new Long(1));
					}
				
					
				
			}
					else
				{continue;}
			}
				
				}
		    inputStream.close();
          Iterator it=keyVSSumCount.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();									 
				 outputStream.write(key+","+keyVSSumCount.get(key));
				 outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("SwitchDivertConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("SwitchDivertConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("SwitchDivertConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("SwitchDivertConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss:SSSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			SwitchDivertConverter s = new SwitchDivertConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\cdr.log-20110301");
			   s.convert(input,"Maha_Test");		   
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
