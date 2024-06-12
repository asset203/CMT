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

public class ETOPUPTransConverter extends AbstractTextConverter {
	private Logger logger;
public ETOPUPTransConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside ETOPUPTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("ETOPUPTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String key;
			String date = "";
			String errorReason="";
			String status="";
			String userId="";
			String senderCate="";
			String source="";
			String channelType="";
			String type="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=40)
				{
					try {
					date =getDate(line.split(",")[3]);
				
					errorReason=line.split(",")[40]!=null?line.split(",")[40]:"";
					status=line.split(",")[33]!=null?line.split(",")[33]:"";
					userId=line.split(",")[9]!=null?line.split(",")[9]:"";
					senderCate=line.split(",")[13]!=null?line.split(",")[13]:"";
					source=line.split(",")[1]!=null?line.split(",")[1]:"";
					channelType=line.split(",")[8]!=null?line.split(",")[8]:"";
					type=line.split(",")[5]!=null?line.split(",")[5]:"";
					key=date+","+errorReason+","+status+","+userId+","+senderCate+","+source+","+channelType+","+type;
					outputStream.write(key);					
					outputStream.newLine();
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("ETOPUPTransConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("ETOPUPTransConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("ETOPUPTransConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("ETOPUPTransConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
		ETOPUPTransConverter s = new ETOPUPTransConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\20110123_Transactions001.csv");
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
			"dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
