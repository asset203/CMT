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
//import org.apache.poi.hssf.record.NumberFormatIndexRecord;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class CallCollectNavCounterConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> keyVsCount=new HashMap<String,Long>() ;
	public CallCollectNavCounterConverter()
{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside CallCollectNavCounterConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("CallCollectNavCounterConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String key="";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains(",")&&line.split(",").length>=9)
					{
						try
						{
						date = getDate(line.split(",")[0]+" "+line.split(",")[1]);
						String debitReason=line.split(",")[5].trim();
						long debitAmmount=Long.parseLong(line.split(",")[7].trim());
						long resultCode =Long.parseLong(line.split(",")[8].trim());
						key=date+","+debitReason+","+debitAmmount+","+resultCode;
						//System.out.println("key "+key);
						if(keyVsCount.containsKey(key))
						{
							long count=keyVsCount.get(key);
							keyVsCount.remove(key);
							keyVsCount.put(key, count+1);
						}
						else
						{
							keyVsCount.put(key, new Long(1));
						}
						} catch(ParseException exc){ logger.error(exc) ;exc.printStackTrace(); continue ;}
						catch(NumberFormatException exc){ logger.error(exc) ;exc.printStackTrace(); continue ;}
					}
					else
					{continue;}
				}
			}
			Iterator it=keyVsCount.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 outputStream.write(key+","+keyVsCount.get(key));
				 outputStream.newLine();
			}
	inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("CallCollectNavCounterConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("CallCollectNavCounterConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("CallCollectNavCounterConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("CallCollectNavCounterConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
	
	}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyyMMdd HHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
			CallCollectNavCounterConverter s = new CallCollectNavCounterConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\CCIVR.mpszah7-1.ucip2010111100.cdr");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
