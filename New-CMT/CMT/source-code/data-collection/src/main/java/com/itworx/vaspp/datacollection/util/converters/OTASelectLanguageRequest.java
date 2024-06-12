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
import java.util.Calendar;
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
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class OTASelectLanguageRequest extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> dateVsTimeDiff=new HashMap<String,List>() ;
	private Map  <String ,String> reqIdVSDate=new HashMap<String,String>() ;
	private Map<String ,Requests> dateVsRequests= new HashMap<String,Requests>();
public OTASelectLanguageRequest()
{
	
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside OTASelectLanguageRequest convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("OTASelectLanguageRequest.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line;
			String date = "";
			String reqId="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.length()>23)
				{
					try
					{
						date =getDate(line.substring(0, 23));
						if(line.contains("doGet: Request"))
						{
							if(dateVsRequests.containsKey(date))
							{
								Requests req=dateVsRequests.get(date);
								long reqCount=req.getTotalRequests();
								dateVsRequests.remove(date);
								req.setTotalRequests(reqCount+1);
								dateVsRequests.put(date , req);
							}
							else
							{ 
								Requests req=new Requests();
								req.setTotalRequests(1);
								dateVsRequests.put(date , req);
							}
							reqId=Utils.stringBetween(line, "Request [", "],");
							if(reqId!=null)
							{
								reqIdVSDate.put(reqId, line.substring(0, 23));
							}
						}
						else if(line.contains("notify: Request [") &&line.contains("was completed successfully"))
						{
							if(dateVsRequests.containsKey(date))
							{
								Requests req=dateVsRequests.get(date);
								long reqCount=req.getSuccessRequests();
								dateVsRequests.remove(date);
								req.setSuccessRequests(reqCount+1);
								dateVsRequests.put(date , req);
							}
							else
							{
								Requests req=new Requests();
								req.setSuccessRequests(1);
								dateVsRequests.put(date , req);
							}
							//String any =line.split("notify: Request \\[")[1];
							reqId=line.split("notify: Request \\[")[1].split("\\]")[0];//Utils.stringBetween(line, "Request [", "]");
							if(reqId!=null)
							{
								if(reqIdVSDate.containsKey(reqId))
								{
									String dat=reqIdVSDate.get(reqId);
									reqIdVSDate.remove(reqId);
									long diff=getDiff(line.substring(0, 23),dat);
									if(dateVsTimeDiff.containsKey(date))
									{
										List diffList=dateVsTimeDiff.get(date);
										diffList.add(diff);
										dateVsTimeDiff.remove(date);
										dateVsTimeDiff.put(date, diffList);
									}
									else
									{
										List diffList= new ArrayList();
										diffList.add(diff);
										dateVsTimeDiff.put(date, diffList);
									}
								}
							}
						}
						else if(line.contains("notify: Request") &&(!line.contains("was completed successfully")))
						{
							if(dateVsRequests.containsKey(date))
							{
								Requests req=dateVsRequests.get(date);
								long reqCount=req.getFailedRequests();
								dateVsRequests.remove(date);
								req.setFailedRequests(reqCount+1);
								dateVsRequests.put(date , req);
							}
							else
							{
								Requests req=new Requests();
								req.setFailedRequests(1);
								dateVsRequests.put(date , req);
							}
							reqId=line.split("notify: Request \\[")[1].split("\\]")[0];
							if(reqId!=null)
							{
								if(reqIdVSDate.containsKey(reqId))
								{
									String dat=reqIdVSDate.get(reqId);
									reqIdVSDate.remove(reqId);
									long diff=getDiff(line.substring(0, 23),dat);
									if(dateVsTimeDiff.containsKey(date))
									{
										List diffList=dateVsTimeDiff.get(date);
										diffList.add(diff);
										dateVsTimeDiff.remove(date);
										dateVsTimeDiff.put(date, diffList);
									}
									else
									{
										List diffList= new ArrayList();
										diffList.add(diff);
										dateVsTimeDiff.put(date, diffList);
									}
								}
							}
						}
					
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
				
			}
		}
		Iterator it=dateVsRequests.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 Requests req=dateVsRequests.get(key);
			 if(dateVsTimeDiff.containsKey(key))
			 {
				
				 double avg =getAvg(dateVsTimeDiff.get(key));
				 avg=avg/1000;
				 outputStream.write(key+","+req.getTotalRequests()+","+req.getSuccessRequests()+","+req.getFailedRequests()+","+avg);
				 outputStream.newLine();
			 }
			 else/*it means there is request come but have not responses by new date */
			 {	
				 outputStream.write(key+","+req.getTotalRequests()+","+req.getSuccessRequests()+","+req.getFailedRequests()+","+0.0);
				 outputStream.newLine(); 
			 }
			 
		}
		//System.out.println("finish");
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("OTASelectLanguageRequest.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("OTASelectLanguageRequest.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("OTASelectLanguageRequest.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("OTASelectLanguageRequest.convert() - finished converting input files successfully ");
		return outputFiles;
		
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
		//System.out.println(" date "+date);
	dateString = outDateFormat.format(date);
	return dateString;

}
private double  getAvg(List list)
{
	Long sum=new Long (0);
	if(list!=null)
	{
	for(int i=0;i<list.size();i++)
	{
		sum=sum +(Long)list.get(i);
	}
	}
   double avg =sum/list.size();
	return avg;
}
private long getDiff(String date1Str, String  date2Str) throws ParseException {
	
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss.SSS");
	Date date1= new Date();
	Date date2= new Date();
	date1 = inDateFormat.parse(date1Str);
	date2 = inDateFormat.parse(date2Str);
	Calendar cal = Calendar.getInstance(); 
	Calendar cal2=Calendar.getInstance(); 
	cal.setTime(date1);
	cal2.setTime(date2);
	long diff=cal.getTimeInMillis()-cal2.getTimeInMillis();
	return diff;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
		OTASelectLanguageRequest s = new OTASelectLanguageRequest();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\LSS_req_20101103.txt");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public class Requests
{
public long totalRequests=0;
public long getTotalRequests() {
	return totalRequests;
}
public void setTotalRequests(long totalRequests) {
	this.totalRequests = totalRequests;
}
public long getSuccessRequests() {
	return successRequests;
}
public void setSuccessRequests(long successRequests) {
	this.successRequests = successRequests;
}
public long getFailedRequests() {
	return failedRequests;
}
public void setFailedRequests(long failedRequests) {
	this.failedRequests = failedRequests;
}
public long successRequests=0;
public long failedRequests=0;
}
}
