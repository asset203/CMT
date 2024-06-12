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
import com.itworx.vaspp.datacollection.util.Utils;

public class AirEventErrorConverter extends AbstractTextConverter {
	private Logger logger;
	private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;
public AirEventErrorConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside AirEventErrorConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("AirEventErrorConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line == null||line.contains("**** Starting new logfile. ****")||"".equals(line.trim()))
					continue;
						
					try{
						
					date= getDate(Utils.stringBetween(line, "[", "]"));
					
					String module=((line.split("Module:")[1]).split("/")[0]).trim();
					String event=((line.split("Event:")[1]).split("ID:")[0]).trim();
					String info=(line.split("Info:")[1]).trim();
		
					module = module.replaceAll(",", ";");
					event = event.replaceAll(",", ";");
					info = info.replaceAll(",",";");
					
					long currentCount=Long.parseLong (((line.split("Count:")[1]).split(" "))[1]);
					if(info.contains("Domain ="))
					{
						info=info.split("Domain =")[0];
					}
					String key=date+","+module+","+event+","+info;
					if(dateVsCount.containsKey(key))
					{
						Long count=dateVsCount.get(key);
						dateVsCount.remove(key);
						dateVsCount.put(key, new Long(count.longValue()+currentCount));
					}
					else
					{
						dateVsCount.put(key, new Long(currentCount));
					}
					
					}
					catch(Exception exc)
					{ logger.error(exc) ;
					exc.printStackTrace();
					continue ;
					}
				}
			}
		
		Iterator it =dateVsCount.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			
				
				outputStream.write(key+","+((Long)dateVsCount.get(key)).longValue());
				//System.out.println(key+","+((Long)dateVsCount.get(key)).longValue());
				outputStream.newLine();
			
		     
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("AirEventErrorConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("AirEventErrorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("AirEventErrorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("AirEventErrorConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\SourceCode\\DataCollection");
		AirEventErrorConverter s = new AirEventErrorConverter();
		File[] input = new File[5];
		input[0]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.0");
		input[1]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.1");
		input[2]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.2");
		input[3]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.3");
		input[4]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.4");
		//   s.convert(input,"Maha_Test");
		
		String info ="Failed to open RPC interface, Failed to open RPC channel, target is 172.30.202.176";
		System.out.println("info1 "+info);
		info.replaceAll(",",";");
		System.out.println("info2 "+info);
		info =info.replaceAll(",",";");
		System.out.println("info3 "+info);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");
	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
