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


public class CallCollectNavTransConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateVsCount=new HashMap<String,Long>() ;
public CallCollectNavTransConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside CallCollectNavTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("CallCollectNavTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String msisdn ="";
			String key ="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=8)
				{
					try {
					date =getDate(line.split(",")[0]);
					msisdn=line.split(",")[4].trim();
					if(msisdn.startsWith("0"))
						msisdn="2"+msisdn;
					else if (msisdn.startsWith("1"))
						msisdn="20"+msisdn;
					else if (!(msisdn.startsWith("0")||(msisdn.startsWith("1"))))
						msisdn="201"+msisdn;
					key=date+","+msisdn;
					
					if(line.split(",")[5].trim().equalsIgnoreCase("FreeTrial") &&line.split(",")[8].trim().equalsIgnoreCase("000000"))
					{
						if(dateVsCount.containsKey(key))
						{
							long count =dateVsCount.get(key);
							dateVsCount.put(key, count+1);
						}
						else
						{
							dateVsCount.put(key,new Long (1));
						}
					}
					}  catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
		inputStream.close();
		Iterator it=dateVsCount.keySet().iterator();
		while (it.hasNext())
		{
			Object obj=it.next();
			outputStream.write(obj.toString()+","+dateVsCount.get(obj));
			 outputStream.newLine();
			
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("CallCollectNavTransConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("CallCollectNavTransConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("CallCollectNavTransConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("CallCollectNavTransConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		CallCollectNavTransConverter s = new CallCollectNavTransConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\CALL_COLL_NAV_1307967705844_1_CCIVR.mpsram2-1.ucip2011061314.cdr");
		   s.convert(input,"Maha_Test");
		System.out.println("finished ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
