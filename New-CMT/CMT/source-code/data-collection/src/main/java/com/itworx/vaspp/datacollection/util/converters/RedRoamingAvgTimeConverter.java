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

public class RedRoamingAvgTimeConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,ArrayList> startTimeDiff=new HashMap<String,ArrayList>() ;
	private Map  <String ,ArrayList> fstLegTimeDiff=new HashMap<String,ArrayList>() ;
	private Map  <String ,ArrayList> sndLegTimeDiff=new HashMap<String,ArrayList>() ;
public RedRoamingAvgTimeConverter()
{
	}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside RedRoamingAvgTimeConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("RedRoamingAvgTimeConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String dateStr;
			String startTime;
			String fstTimePart1;
			String fstTimePart2;
			String sndTimePart1;
			String sndTimePart2;
			long sndTimeDiff;
			long fstTimeDiff;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=30 &&line.split(",")[0].contains("\"")&&line.split(",")[13].contains("\"")
						&&line.split(",")[19].contains("\"")&&line.split(",")[21].contains("\"")&&line.split(",")[27].contains("\"")&&line.split(",")[29].contains("\""))
				{
					try {
						   dateStr =Utils.stringBetween(line.split(",")[0], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[0], "\"", "\""):"";
							startTime=Utils.stringBetween(line.split(",")[13], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[13], "\"", "\""):"";
							sndTimePart2 =Utils.stringBetween(line.split(",")[29], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[29], "\"", "\""):"";
							sndTimePart1=Utils.stringBetween(line.split(",")[27], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[27], "\"", "\""):"";
							fstTimePart1=Utils.stringBetween(line.split(",")[19], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[19], "\"", "\""):"";
							fstTimePart2=Utils.stringBetween(line.split(",")[21], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[21], "\"", "\""):"";
							 date=getDate (dateStr);
							 long sartDateDiff=getDateDiff(startTime,dateStr);							
                          
							if(startTimeDiff.containsKey(date))
							{
								ArrayList objList=startTimeDiff.get(date);
								objList.add(sartDateDiff);
								startTimeDiff.remove(date);
								startTimeDiff.put(date, objList);
							}
							else
							{ 
								ArrayList objList= new ArrayList();
								objList.add(sartDateDiff);
								startTimeDiff.put(date, objList);
							}
							if(fstTimePart2.equalsIgnoreCase("0")&&(!fstTimePart1.equalsIgnoreCase("0")))
							{
								fstTimeDiff=-999;
							}
							else
							{
								fstTimeDiff=getDateDiff(fstTimePart2,fstTimePart1);
							}
							
							if(fstLegTimeDiff.containsKey(date))
							{
								ArrayList list=fstLegTimeDiff.get(date);
								list.add(fstTimeDiff);
								fstLegTimeDiff.remove(date);
								fstLegTimeDiff.put(date, list);
							}
							else
							{
								ArrayList list= new ArrayList();
								list.add(fstTimeDiff);
								fstLegTimeDiff.put(date, list);
								
							}
							
							if(sndTimePart2.equalsIgnoreCase("0") &&(!sndTimePart1.equalsIgnoreCase("0")))
							{
								sndTimeDiff=-999;
							}
							else 
							{
								sndTimeDiff=getDateDiff(sndTimePart2,sndTimePart1);
							}
							
							
							if(sndLegTimeDiff.containsKey(date))
							{
								ArrayList list=sndLegTimeDiff.get(date);
								list.add(sndTimeDiff);
								sndLegTimeDiff.remove(date);
								sndLegTimeDiff.put(date, list);
							}
							else
							{
								ArrayList list= new ArrayList();
								list.add(sndTimeDiff);
								sndLegTimeDiff.put(date, list);
							}
							
				      }catch(ParseException exc){ logger.error(exc) ; continue ;}
					
					///System.out.println("date "+date );
				}
			}
		}//end of files 
		Iterator it = startTimeDiff.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			double startTime[]=minMaxAvg(startTimeDiff.get(key));						
			double fstTime[]=minMaxAvg(fstLegTimeDiff.get(key));
			double sndTime []=minMaxAvg(sndLegTimeDiff.get(key));
			outputStream.write(key+","+startTime[0]+","+startTime[1]+","+startTime[2]+","+fstTime[0]+","+fstTime[1]+","+fstTime[2]+","+sndTime[0]+","+sndTime[1]+","+sndTime[2]);
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("RedRoamingAvgTimeConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("RedRoamingAvgTimeConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("RedRoamingAvgTimeConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("RedRoamingAvgTimeConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public long getDateDiff(String first,String second)
{
	long sartDateDiff=0;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
	"yyyyMMddHHmmss");
	Date date1= new Date();
	Date date2= new Date();
	try
	{
		date1 = inDateFormat.parse(first);
	    date2 = inDateFormat.parse(second);
	    sartDateDiff=Utils.getTimeDifferenceinMills(date1, date2);
	}catch(ParseException exc){ logger.error(exc) ;}
return sartDateDiff;	
}

public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		RedRoamingAvgTimeConverter s = new RedRoamingAvgTimeConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\Report_CDR_UCB_2011032917.csv");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
private double[] minMaxAvg(List data)
{
	double min,max,avg;
	double any;
	double[] minMaxAvgArray = new double[3];
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
		avg = avg + element ; 
	
	}

	minMaxAvgArray[0] = min ; 
	minMaxAvgArray[1] = max ;
	minMaxAvgArray[2] = avg / data.size() ;
	return minMaxAvgArray;
}

}
