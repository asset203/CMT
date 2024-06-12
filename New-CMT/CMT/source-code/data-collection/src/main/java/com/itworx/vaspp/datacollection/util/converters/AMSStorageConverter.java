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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class AMSStorageConverter extends AbstractTextConverter{
	private Logger logger;
	public AMSStorageConverter() 
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside AMSStorageConverter convert - started converting input files");
	   String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("AMSStorageConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            
				String line;
				String date = "";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains(","))
					{
						String columns []=line.split(",");
						if(!(columns.length<4))
						{
							try{
								date=getDate(columns[0]);
								//System.out.println("date "+date );
								long queueNumber=Long.parseLong(columns[1]!=null?columns[1]:"0");
								String queueName=columns[2]!=null?columns[2]:"";
								long queueCount=Long.parseLong(columns[3]!=null?columns[3]:"0");
								
								outputStream.write(date+","+queueNumber+","+queueName+","+queueCount);
								//System.out.println("the key "+date+","+queueNumber+","+queueName+","+queueCount);
								outputStream.newLine();
							  }
							  catch(ParseException exc){ logger.error(exc) ; continue ;}
							  catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
						}
					}else
					{continue;}
				}
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("AMSStorageConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("AMSStorageConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("AMSStorageConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			} 
			logger
					.debug("AMSStorageConverter.convert() - finished converting input files successfully ");
			return outputFiles;
}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:SS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:SS");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\pahse8\\DataCollection");
			AMSStorageConverter s = new AMSStorageConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\DataCollection\\Stats_20100930.out");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
