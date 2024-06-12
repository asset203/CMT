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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class EOCNVlrConvetter extends AbstractTextConverter{
private Logger logger;
public EOCNVlrConvetter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside EOCNVlrConvetter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("EOCNVlrConvetter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("MSG:")&&line.contains("SRI_SM Response.") &&line.contains("VLR:"))
				{
					if(line.contains(","))
					{
					String columns[]=line.split(",");
					String datePart=columns[0]!=null?columns[0]:"";
					 try
					 {
						 date =getDate(datePart);
						 //System.out.println("the date"+date);
						 String transId=columns[4]!=null?columns[4].split("TID:")[1]:"";
						 String vlr=columns[7]!=null?columns[7].split("VLR:'")[1]:"";
						 outputStream.write(date+","+transId+","+vlr);
						 //System.out.println("the key "+date+","+vlr+","+transId);
						 outputStream.newLine();
					 }
					 catch(ParseException exc){ logger.error(exc) ; continue ;}
					 }
				}
				
				else
				{
					continue;
				}
			}
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("EOCNVlrConvetter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("EOCNVlrConvetter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("EOCNVlrConvetter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("EOCNVlrConvetter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getDate(String line) throws ParseException {
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
		
		PropertyReader.init("D:\\build\\pahse8\\DataCollection");
		EOCNVlrConvetter s = new EOCNVlrConvetter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\DataCollection\\trace2010101001.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
