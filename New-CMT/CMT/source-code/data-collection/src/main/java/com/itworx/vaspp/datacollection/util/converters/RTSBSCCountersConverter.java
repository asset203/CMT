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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class RTSBSCCountersConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Double> sourceVScounterValue=new HashMap<String,Double>() ;
public RTSBSCCountersConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside RTSBSCCountersConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("RTSBSCCountersConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String date = "";
			try
			{
            if(inputFiles[i].getName().contains("."))
            {
            	if(inputFiles[i].getName().split("\\.")[0].contains("_"))
            	{
            		date=getDate (inputFiles[i].getName().split("\\.")[0].split("_")[inputFiles[i].getName().split("\\.")[0].split("_").length-1]);
            	}else
            	{
            		date=getDate (inputFiles[i].getName().split("\\.")[0]);
            	}
            	
            }
			}catch(ParseException exc){ logger.error(exc) ; continue ;}
			String line;
			String counterName="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(","))
				{
					String columns[]=line.split(",");
					if(columns[0].equalsIgnoreCase("1")||columns[0].equalsIgnoreCase("2")||columns[0].equalsIgnoreCase("5"))
					{
						if(columns[0].equalsIgnoreCase("1"))
							counterName="received_events";
						else if(columns[0].equalsIgnoreCase("2"))
							counterName="Sent_events";
						else if(columns[0].equalsIgnoreCase("5"))
							counterName="SMPC_UP_TIME";
						if(columns.length==2)
						{
							//sourceVScounterValue.put("Total", Double.parseDouble(columns[1]));
							outputStream.write(date+","+counterName+",Total,"+columns[1]);
							outputStream.newLine();
						}
						else if(columns.length>=3)
						{
							outputStream.write(date+","+counterName+",Total,"+columns[1]);
							outputStream.newLine();							
							int count=0;
							for(int l=2;l<columns.length;l++)
							{
								if(l+count<=columns.length-2)
								{
								outputStream.write(date+","+counterName+","+columns[l+count]+","+columns[l+1+count]);
								outputStream.newLine();
								count=count+1;
								}
							}
						}
						
					}else if (columns[0].equalsIgnoreCase("3")||columns[0].equalsIgnoreCase("6")||columns[0].equalsIgnoreCase("7"))
					{
						if(columns[0].equalsIgnoreCase("3"))
							counterName="BSC_up_time";
						else if(columns[0].equalsIgnoreCase("6"))
							counterName="BUFFERED_EVENTS";
						else if(columns[0].equalsIgnoreCase("7"))
							counterName="CONGESTED_BYTES";
						
						
						
						int count=0;
						for(int l=1;l<columns.length;l++)
						{
							
							
							//sourceVScounterValue.put(columns[l], Double.parseDouble(columns[l+1]));
							if(l+count<=columns.length-2)
							{
							outputStream.write(date+","+counterName+","+columns[l+count]+","+columns[l+1+count]);
							outputStream.newLine();
							count=count+1;
							}
						}
					} else if (columns[0].equalsIgnoreCase("4"))
					{
						counterName="BSC_LAC_UP_TIME";
						int count=0;
						for(int l=1;l<columns.length;l++)
						{
							
							
							//sourceVScounterValue.put(columns[l], Double.parseDouble(columns[l+1]));
							if(l+count<=columns.length-2)
							{								
							outputStream.write(date+","+counterName+","+columns[l+count]+"_"+columns[l+1+count]+","+columns[l+2+count]);
							outputStream.newLine();
							count=count+2;
							}
						}
					}
						
				}
				
			}
		}//
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("RTSBSCCountersConverter.convert() - finished converting input files successfully ");
		}
	catch (FileNotFoundException e) {
		logger
				.error("RTSBSCCountersConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("RTSBSCCountersConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("RTSBSCCountersConverter.convert() - finished converting input files successfully ");
	return outputFiles;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHH");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		RTSBSCCountersConverter s = new RTSBSCCountersConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\2011090613.reports.txt");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
