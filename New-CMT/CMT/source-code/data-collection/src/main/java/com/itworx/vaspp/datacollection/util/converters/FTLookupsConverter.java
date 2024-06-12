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


public class FTLookupsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Lookups> dateVSLookups=new HashMap<String,Lookups>() ;
public FTLookupsConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside FTLookupsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("FTLookupsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			long reqSum=0;
			long respSum=0;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				
				if(line.contains(",")&&line.split(",").length>=3&&!line.contains("DATE/TIME"))
				{
					try {
					date=getDate(line.split(",")[0]);
					reqSum=Long.parseLong(!line.split(",")[1].equalsIgnoreCase("")?line.split(",")[1] :"0");
					respSum=Long.parseLong(!line.split(",")[2].equalsIgnoreCase("")?line.split(",")[2] :"0");
					
				    if(dateVSLookups.containsKey(date))
				    {
				    	Lookups obj=dateVSLookups.get(date);
				    	obj.setReqSum(obj.getReqSum()+reqSum);
				    	obj.setRespSum(obj.getRespSum()+respSum);
				    	List reqs=obj.getRequests();
				    	reqs.add(reqSum);
				    	obj.setRequests(reqs);
				    	dateVSLookups.put(date, obj);
				    	
				    }else
				    {
				    	Lookups obj = new Lookups ();
				    	obj.setReqSum(reqSum);
				    	obj.setRespSum(respSum);
				    	List reqs=obj.getRequests();
				    	reqs.add(reqSum);
				    	obj.setRequests(reqs);
				    	dateVSLookups.put(date, obj);
				    }
					} catch(ParseException exc){ logger.error(exc) ; continue ;}
				      catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
				}
			}
		}//end of files
		Iterator it =dateVSLookups.keySet().iterator();
		while (it.hasNext())
		{
			Object key =it.next();
			Lookups obj=dateVSLookups.get(key);
			long maxReq=getMax(obj.getRequests());
			 outputStream.write(key+","+obj.getReqSum()+","+obj.getRespSum()+","+maxReq);
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("FTLookupsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("FTLookupsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("FTLookupsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("FTLookupsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
private long getMax(List data)
{
	long max;	
	Iterator listIterator = data.iterator(); 
	max=(Long)listIterator.next();	
	while(listIterator.hasNext())
	{
		long element = (Long)listIterator.next(); 		
		if(element>max)
		{
			max=element;
		}	
	}

	return max;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HHmm");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		FTLookupsConverter s = new FTLookupsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\FSC-AccountFinderClientIf_3.0_A_1-2011-09-11-0000.stat");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class Lookups 
{
	long reqSum=0;
	long respSum=0;
	List requests = new ArrayList();
	public long getReqSum() {
		return reqSum;
	}
	public void setReqSum(long reqSum) {
		this.reqSum = reqSum;
	}
	public long getRespSum() {
		return respSum;
	}
	public void setRespSum(long respSum) {
		this.respSum = respSum;
	}
	public List getRequests() {
		return requests;
	}
	public void setRequests(List requests) {
		this.requests = requests;
	}
}
}
