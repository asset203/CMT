package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class MckHttpReqsConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String,Long> results = new HashMap<String, Long>();
	
	
	public String getLineKey(String line) throws ParseException
	{
		String lineKey="";
		
		String[] lineFields = line.split(",");
		
		if(lineFields.length <12) //invalid line...Ignore it
			return lineKey;
		
		String dateTimePart = lineFields[8];
		String dateTime = getDate(dateTimePart.substring(dateTimePart.indexOf("=")+1 ).trim());
		
		String callForwardCondition = lineFields[9].substring(lineFields[9].indexOf("=")+1).trim();
		
		String clir = lineFields[7].substring(lineFields[7].indexOf("=")+1).trim();
		
		String requestStatus = lineFields[10].substring(lineFields[10].indexOf("=")+1).trim();
		
		lineKey = dateTime+","+callForwardCondition+","+clir+","+requestStatus;
		
		return lineKey;
	}
	@Override
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		
		//1- start logging
		logger = Logger.getLogger(systemName);
		logger.debug("Inside MckHttpReqsConverter - started converting input files");
		

		//2- create output files array
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		
		//3- streams to be used
		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		
		try {
			File outputFile;
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("MckHttpReqsConverter() - Start  converting file "+ inputFiles[i].getName());
				
				outputFile = new File(path, inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				
				//4- start parsing the file
				while (inputStream.ready()) {
					line = inputStream.readLine();
					//4.1 get lineKey
					String lineKey =getLineKey(line);
					
					//4.2 Ignore invalid lines 
					if(lineKey.equals(""))
						continue;
					//4.3 increment count for existing keys
					if(results.containsKey(lineKey))
					{
						Long count = results.get(lineKey);
						count++;
						results.put(lineKey, count);
					}
					//4.5 add new entry for new keys
					else
					{
						results.put(lineKey, new Long(1));
					}	
				}
				inputStream.close();
				
				//5- write results to output file
				outputStream = new BufferedWriter(new FileWriter(outputFile));
				for(Entry<String,Long> entry: results.entrySet()){
					outputStream.write(entry.getKey()+","+entry.getValue());
					outputStream.newLine();
				}
				
				outputStream.close();
				outputFiles[i] = outputFile;
				
				//6- clearing results to start parsing next file 
				results.clear();
				
				logger.debug("MckHttpReqsConverter() - Finish converting file "+ inputFiles[i].getName());
			}
			logger.debug("MckHttpReqsConverter.convert() - finished converting input files successfully ");
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return outputFiles;
	}
	
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	
	public static void main(String[] args){
		
		try {

			PropertyReader.init("D:\\VNPP_PROJ\\SourceCode\\DataCollection");
			MckHttpReqsConverter basmaConverter = new MckHttpReqsConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\VNPP_PROJ\\SourceCode\\DataCollection\\mck_sample_log.txt");
			basmaConverter.convert(input, "Testing");
			
			System.out.println("DONE...");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
