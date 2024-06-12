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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDGenericConnectorConverter extends AbstractTextConverter{
	private Logger logger;
	private Map<String, Long> dateVSKeyCount = new HashMap<String, Long>();
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
logger = Logger.getLogger(systemName);
logger
		.debug("Inside USSDGenericConnectorConverter convert - started converting input files");
String path = PropertyReader.getConvertedFilesPath();
File[] outputFiles = new File[1];
File outputFile = new File(path, inputFiles[0].getName());

BufferedReader inputStream = null;
BufferedWriter outputStream;
try {
	outputStream = new BufferedWriter(new FileWriter(outputFile));
	for (int i = 0; i < inputFiles.length; i++) {
		logger.debug("USSDGenericConnectorConverter.convert() - converting file "
				+ inputFiles[i].getName());
		String shortCode= inputFiles[i].getName().substring(inputFiles[i].getName().indexOf("(")+1, inputFiles[i].getName().lastIndexOf(")"));
        
		inputStream = new BufferedReader(new FileReader(inputFiles[i]));
		String line;
		String date = "";
		String status = "";
		String key = "";
        
		while (inputStream.ready()) {
			line = inputStream.readLine();
			if(line.contains(",")&&line.split(",").length>=3&&line.split(",")[2].contains("AppResponse")&&line.split(",")[2].contains("STATUS"))
			{
				try{
				date=getDate (line.split(",")[0]);
				status=line.split("STATUS")[1].split(",")[1].split(";")[0];
				
				key=date+","+shortCode+","+status;
				if(dateVSKeyCount.containsKey(key))
				{
					long count=dateVSKeyCount.get(key);
					dateVSKeyCount.put(key, count+1);
				}else
				{
					dateVSKeyCount.put(key, new Long(1));
				}
				}catch (ParseException exc) {
					logger.error(exc);
					continue;
				}
			}
		}
		inputStream.close();
	}
	for (Entry<String, Long> counterEntry : dateVSKeyCount.entrySet()) {
		Object key = counterEntry.getKey();		
		outputStream.write(key + "," + counterEntry.getValue());
		outputStream.newLine();
	}

	outputStream.close();
	outputFiles[0] = outputFile;
	logger
			.debug("USSDGenericConnectorConverter.convert() - finished converting input files successfully ");

} catch (FileNotFoundException e) {
	logger.error("USSDGenericConnectorConverter.convert() - Input file not found "
			+ e);
	throw new ApplicationException(e);
} catch (IOException e) {
	logger
			.error("USSDGenericConnectorConverter.convert() - Couldn't read input file"
					+ e);
	throw new ApplicationException(e);
}
logger
		.debug("USSDGenericConnectorConverter.convert() - finished converting input files successfully ");
return outputFiles;

}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:SS.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\phase11\\DataCollection");
			USSDGenericConnectorConverter s = new USSDGenericConnectorConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\build\\phase11\\DataCollection\\ipcconnector_2011112213.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
