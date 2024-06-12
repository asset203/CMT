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
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
public class VRSHFCDDataConverter extends AbstractTextConverter{
	private Logger logger;
	private Map<String , Integer> VRSHFCDData = new HashMap<String, Integer>() ;
	private Map<String,Integer> durationSum=new HashMap<String ,Integer>();
	public VRSHFCDDataConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("VRSHFCDDataConverter.convert() - started converting input files ");
		try {
			String dateLine="";
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(
				outputFile));
		BufferedReader inputStream = null;
		for (int i = 0; i < inputFiles.length; i++) 
		{
			logger.debug("VRSHFCDDataConverter.convert() - converting file "
					+ inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line;
			String date="";
			while (inputStream.ready()) {					
				line = inputStream.readLine();
				if (line.trim().equals("")) {
					continue;
				}
				
				else 
				{
					String[] tokens= line.split(",");
					
					
						try
					    {
						  date = getDate(line);
						}
						catch(ParseException exc)
						{ 
							logger.error(exc) ; continue ;
					    }
						if(!tokens[4].contains("0900"))
						{
							String key=date+","+tokens[4].trim()+","+tokens[5]+","+tokens[8].trim();
							if(VRSHFCDData.containsKey(key))
							{
								Integer calls=VRSHFCDData.get(key);
								calls++;
								VRSHFCDData.remove(key);
								VRSHFCDData.put(key, calls);
							}
							else
							{
								VRSHFCDData.put(key, new Integer (1));
							}
							if(durationSum.containsKey(key))
							{
								Integer duration =durationSum.get(key);
								duration=duration+new Integer(Integer.parseInt(tokens[11].trim()));
								durationSum.remove(key);
								durationSum.put(key, duration);
							}
							else
							{
								durationSum.put(key,new Integer(Integer.parseInt(tokens[11].trim())));
							}
						}
						
				}
				}
		
		}
	
	Iterator myVeryOwnIterator =VRSHFCDData.keySet().iterator();
	while(myVeryOwnIterator.hasNext()){ 					
		Object key = myVeryOwnIterator.next();
		//long requestNumbers = numOfCalls.get(key);
		logger.debug("write in the converter");
			outputStream.write(key+","+VRSHFCDData.get(key)+","+durationSum.get(key));
			outputStream.newLine();
	}
	inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("VRSHFCDDataConverter.convert() - finished converting input files successfully ");
	return outputFiles;
	
		}catch (FileNotFoundException e) {
		logger.error("VRSHFCDDataConverter.convert() - Input file not found " + e);
		new ApplicationException("" + e);
	} catch (IOException e) {
		logger.error("VRSHFCDDataConverter.convert() - Couldn't read input file"
				+ e);
		new ApplicationException("" + e);
	}
	return null;
	
}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		if (line != null)
			tokens = line.split(",");
			date = inDateFormat.parse(tokens[0]+tokens[1]);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\SourceCode\\DataCollection");
			VRSHFCDDataConverter s = new VRSHFCDDataConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\SourceCode\\DataCollection\\2010040709.cdr");
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}

