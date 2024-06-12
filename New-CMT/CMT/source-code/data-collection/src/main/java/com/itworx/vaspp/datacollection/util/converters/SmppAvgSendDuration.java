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
import com.itworx.vaspp.datacollection.util.converters.SMPPLogsRequestConverter.SmppReq;



public class SmppAvgSendDuration extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> hourVsSmppDuration=new HashMap<String,List>();
public SmppAvgSendDuration()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside SmppAvgSendDuration convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("SmppAvgSendDuration.convert() - converting file "
							+ inputFiles[i].getName());			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String shortCode;
			String resultCode=" ";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("||"))
				{
					try 
					{
						
					    date =getDate(line.split("\\|\\|")[0]);
					    if(!line.contains("SarRefNum =")&&(line.split("\\|\\|")[2].trim().equalsIgnoreCase("R")))
					    {
					    	String prevLine =line;
					    	line=inputStream.readLine();
					    	line=prevLine+line;
					    }
					    if(line.split("\\|\\|")[2].trim().equalsIgnoreCase("R")&&line.split("\\|\\|").length>=10)
					    {   
					    	String firstDate=line.split("\\|\\|")[0];
					    	String secDate =line.split("\\|\\|")[9];
					    	long diff=getDateDiff(firstDate, secDate);
					    	
					    	if(hourVsSmppDuration.containsKey(date))
					    	{
					    		List list=hourVsSmppDuration.get(date);
					    		list.add(diff);
					    		hourVsSmppDuration.put(date, list);
					    		
					    	}
					    	else
					    	{
					    		List list=new ArrayList();
					    		list.add(diff);
					    		hourVsSmppDuration.put(date, list);
					    	}
					    	
					    }
                       } catch(ParseException exc){ logger.error(exc) ;continue ;}
		           }
			}
		}
		inputStream.close();		
		Iterator it=hourVsSmppDuration.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			double [] list=getMinMaxAvg(hourVsSmppDuration.get(key));
			outputStream.write(key+","+list[0]+","+list[1]+","+list[2]);
			 outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("SmppAvgSendDuration.convert() - finished converting input files successfully ");

		}
		catch (FileNotFoundException e) {
			logger
					.error("SmppAvgSendDuration.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("SmppAvgSendDuration.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("SmppAvgSendDuration.convert() - finished converting input files successfully ");
		return outputFiles;

		}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public long getDateDiff(String first,String second)
	{
		long sartDateDiff=0;
		SimpleDateFormat firstFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		SimpleDateFormat secondFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		Date date1= new Date();
		Date date2= new Date();
		try
		{
			date1 = firstFormat.parse(first);
		    date2 = secondFormat.parse(second);
		    sartDateDiff=Utils.getTimeDifferenceinMills(date1, date2);
		}catch(ParseException exc){ logger.error(exc) ;}
	return sartDateDiff;	
	}

	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase10\\DataCollection");
			SmppAvgSendDuration s = new SmppAvgSendDuration();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase10\\DataCollection\\SMPP_LOGS_1307019835335_20_SMPP_OPS.log.2011-06-01-19.log");
				
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	private double [] getMinMaxAvg(List data)
	{
		double min,max,avg;
		double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator(); 
		any=(Long)listIterator.next();
		avg =any;
		max = avg ;
		min = avg ;		
		while(listIterator.hasNext())
		{
			double element = (Long)listIterator.next(); 
		
			if(element<min)
			{
				min=element;
			}
			if(element>max)
			{
				max=element;
			}	
			avg = avg + element; 
		}	
		
		minMaxAvgArray[0]=min;
		minMaxAvgArray[1]=max;
		minMaxAvgArray[2]=avg / data.size();
		return minMaxAvgArray;
	}
}
