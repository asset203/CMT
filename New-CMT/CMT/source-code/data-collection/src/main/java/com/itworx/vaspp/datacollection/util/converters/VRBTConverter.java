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
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class VRBTConverter extends AbstractTextConverter{
	private Logger logger;
public VRBTConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside VRBTConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("VRBTConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String  channels []=null; 
			String requests[]=null ;
			while (inputStream.ready())
			{
				line = inputStream.readLine();
				if(line.contains("date"))
				{
					
					 channels =line.split(",");
					 continue;
				}
				else
				{   
					 requests=line.split(",");
					if(requests.length==0)
						 continue;
					try{
					date=getDate(requests[0]);
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
					
					
				}
				
			
					
			//if((requests.length==channels.length)&&(channels.length==6)){
				
				for(int j=1;j<channels.length;j++)
				{   
					long req=0;
					try{if(requests[j]!=null&&!(requests[j].length()==0)&&!(requests[j].trim().equalsIgnoreCase("")))
						req=Long.parseLong(requests[j]);}
					  catch(java.lang.NumberFormatException ex)
					   { logger.error(ex) ;continue ;}
					  catch(java.lang.IndexOutOfBoundsException ex)
						{ }
					 outputStream.write(date+","+channels[j]+","+req);
					//System.out.println("the line:" +date+","+channels[j]+","+req);
					 outputStream.newLine();
				}
				/*}
			else
			{
				logger.error("VRBTSubChannelsConverter.convert() - converting file The Channels number should not be grater or less than 5");
			}*/
			
				
				
			}
		}
	
	inputStream.close();
	
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("VRBTConverter.convert() - finished converting input files successfully ");

}
catch (FileNotFoundException e) {
		logger
				.error("VRBTConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("VRBTConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("VRBTConverter.convert() - finished converting input files successfully ");
	return outputFiles;
	
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\DataCollection");
		VRBTConverter s = new VRBTConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\DataCollection\\Vodafone Day wise Activation Low Balance Report 20100902.csv");
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
			"dd-MMM-yy");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
