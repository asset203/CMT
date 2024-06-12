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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMInterfaceConverter.Interface;

public class EOCNResponseConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateRespDescVSCount=new HashMap<String,Long>() ;
	public EOCNResponseConverter ()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside EOCNResponseConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("EOCNResponseConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String key="";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.equalsIgnoreCase(""))
					{continue;}
					String []columns= line.split(",");	
					if(columns[4]!=null)
					{
						try
						{
							String dateLine;
							String response="";
							String description="";
							if(columns[4].contains("\""))
							 dateLine=(columns[4].split("\"")[1]).split("\"")[0];
							else dateLine=columns[4];
							date =getDate(dateLine);
//							if(date.equalsIgnoreCase("09/22/2010 07:00:00"))
//							{
//								System.out.println("line is "+line.toString());
//								break;
//							}
							if(columns[8]!=null)
							response=columns[8].contains("\"")?columns[8].split("\"")[1].split("\"")[0]:columns[8];
							if(columns[9]!=null)
								description=columns[9].contains("\"")?columns[9].split("\"")[1].split("\"")[0]:columns[9];
								key=date+","+response+","+description;
							if(dateRespDescVSCount.containsKey(key))
							{
								Long count=dateRespDescVSCount.get(key);
								dateRespDescVSCount.remove(key);
								dateRespDescVSCount.put(key, count+1);
							}
							else
							{
								dateRespDescVSCount.put(key, new Long(1));
							}
							
						}
						catch(ParseException exc){ logger.error(exc) ; continue ;}
					}
					else
					{
						continue;
					}
					}
			}
			Iterator it =dateRespDescVSCount.keySet().iterator();
			while (it.hasNext())
			{
				Object key=it.next();
			    outputStream.write(key+","+dateRespDescVSCount.get(key));
			    //System.out.println("the key is "+key+","+dateRespDescVSCount.get(key));
				outputStream.newLine();
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("EOCNResponseConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("EOCNResponseConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("EOCNResponseConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("EOCNResponseConverter.convert() - finished converting input files successfully ");
			return outputFiles;
}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\pahse8\\DataCollection");
			EOCNResponseConverter s = new EOCNResponseConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\DataCollection\\history201009220620.log.processed");
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
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
}
