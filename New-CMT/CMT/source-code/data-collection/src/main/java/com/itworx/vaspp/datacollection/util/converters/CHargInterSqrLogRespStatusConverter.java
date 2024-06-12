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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;


public class CHargInterSqrLogRespStatusConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateVSResp=new HashMap<String,Long>() ;
	private Map  <String ,List> dateVSRespTimeDiff=new HashMap<String,List>() ;
public CHargInterSqrLogRespStatusConverter()
{
	}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside CHargInterSqrLogRespStatusConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("CHargInterSqrLogRespStatusConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(","))
				{
					try {
						date=getDate(line.split(",")[0]);
					
						if((line.contains("response STATUS")||line.contains("response Status"))&&line.split(",").length>=3)
						{
							String respStatus=line.split(",")[2];
							if(respStatus.contains(";"))
								respStatus=respStatus.split(";")[0];
							String key=date+","+respStatus;
							line = inputStream.readLine();
							
							if(line.contains("request finished in"))
							{
								
								long respTime=Long.parseLong(line.split("request finished in")[1].split("msec")[0].trim());
								
								if(dateVSRespTimeDiff.containsKey(key))
								{
									List list=dateVSRespTimeDiff.get(key);
									list.add(respTime);
									dateVSRespTimeDiff.put(key, list);
								}
								else
								{
									List list= new ArrayList();
									list.add(respTime);
									dateVSRespTimeDiff.put(key, list);
								}
								
							
							
						if(dateVSResp.containsKey(key))
						{
							long count =dateVSResp.get(key);
							dateVSResp.put(key, count+1);
						}
						else
						{
							dateVSResp.put(key, new Long(1));
						}
						}
						}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				      catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
			inputStream.close();
			Iterator it =dateVSResp.keySet().iterator();
			while(it.hasNext())
			{
				Object key =it.next();
				double avg=getAvg(dateVSRespTimeDiff.get(key));
				
				outputStream.write(key+","+dateVSResp.get(key)+","+avg);
				 outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("CHargInterSqrLogRespStatusConverter.convert() - finished converting input files successfully ");

		
	}
		catch (FileNotFoundException e) {
				logger
						.error("CHargInterSqrLogRespStatusConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("CHargInterSqrLogRespStatusConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("CHargInterSqrLogRespStatusConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"dd-MM-yyyy:HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase10\\DataCollection");
			CHargInterSqrLogRespStatusConverter s = new CHargInterSqrLogRespStatusConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase10\\DataCollection\\CHA_INT_SQR_LOG_1307017076692_1_square.log");
			   s.convert(input,"Maha_Test");
			System.out.println("FINISHED ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double getAvg(List data)
	{
		double min,max,avg;
		double any;
		
		Iterator listIterator = data.iterator(); 
		any=(Long)listIterator.next();
		avg =any;
		max = avg ;
		min = avg ;		
		while(listIterator.hasNext())
		{
			double element = (Long)listIterator.next(); 
		
			if(element<min)
			{
				min=element;
			}
			if(element>max)
			{
				max=element;
			}	
			avg = avg + element; 
		}	
		
		return avg / data.size();
	}
}
