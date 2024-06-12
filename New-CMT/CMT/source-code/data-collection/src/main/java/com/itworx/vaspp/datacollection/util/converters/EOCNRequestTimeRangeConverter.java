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
import com.itworx.vaspp.datacollection.util.converters.EOCNConverter.DateDetails;
import com.itworx.vaspp.datacollection.util.converters.OTASelectLanguageRequest.Requests;

public class EOCNRequestTimeRangeConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,String> mobileNumberVsDate=new HashMap<String,String>() ;
	private Map  <String ,List> hourVsDiffList=new HashMap<String,List>() ;
public EOCNRequestTimeRangeConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
logger = Logger.getLogger(systemName);
logger.debug("Inside EOCNRequestTimeRangeConverter convert - started converting input files");

String path = PropertyReader.getConvertedFilesPath();
File[] outputFiles = new File[1];
HashMap parameters=this.getParametersMap();

//
File outputFile = new File(path, inputFiles[0].getName());
BufferedReader inputStream = null;
BufferedWriter outputStream;
try {
outputStream = new BufferedWriter(new FileWriter(outputFile));
String line;
String date= "";
String dateFrom="";
String dateTo="";
String msisdn="";
String prevHour = null;
for (int i = 0; i < inputFiles.length; i++) {

	logger.debug("EOCNRequestTimeRangeConverter.convert() - converting file "
			+ inputFiles[i].getName());
	
	inputStream = new BufferedReader(new FileReader(inputFiles[i]));

					while (inputStream.ready()) {
						line = inputStream.readLine();
						if (line.contains("MSG:")) {
							if(line.contains(","))
							{  try{
								date= getDate(line.split(",")[0]);
								if(line.contains("New Network Initiated Dialogue"))
									{
								       if(line.split(",").length>3)
								       {
								    	   if(line.contains("MSISDN:")){
								    	 msisdn=  line.split(",")[2].split("MSISDN:")[1];
								    	 if(msisdn!="")
								    	 {
								    		 mobileNumberVsDate.put(msisdn, line.split(",")[0]);
								    	 }}
								       }
									}
								else if(line.contains("USSN Confirm"))
								{
									 if(line.split(",").length>3)
									 {
										 if(line.contains("MSISDN:")){
										 msisdn=  line.split(",")[2].split("MSISDN:")[1];
										 if(mobileNumberVsDate.containsKey(msisdn))
										 {
											dateFrom= mobileNumberVsDate.get(msisdn);
											mobileNumberVsDate.remove(msisdn);
											dateTo=line.split(",")[0];
											SimpleDateFormat inDateFormat = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss.SSS");
											Date date1= new Date();
											Date date2= new Date();
											date1 = inDateFormat.parse(dateTo);
											date2 = inDateFormat.parse(dateFrom);
											long diff=Utils.getTimeDifferenceinMills(date1, date2);//getDiff(dateTo,dateFrom);
											if(hourVsDiffList.containsKey(date))
											{
												List diffList=hourVsDiffList.get(date);
												diffList.add(diff);
												hourVsDiffList.remove(date);
												hourVsDiffList.put(date, diffList);
											}
											else
											{
												List diffList= new ArrayList();
												diffList.add(diff);
												hourVsDiffList.put(date, diffList);
											}
										 }
									 }}
								}
								
							    }catch(ParseException exc){ logger.error(exc) ; continue ;}
							}
						}
						else{continue;}
					}
}//end of the file
                   Iterator it=parameters.keySet().iterator();
                  
							while(it.hasNext())
							{
								Object key=it.next();
								String value[]=((String)parameters.get(key)).split(",");
								long op1=0;
								long op2=0;
								if(value.length==1)
								{
									op1=Long.parseLong(value[0]+"000");
								}else if(value.length==2&&value[0].equalsIgnoreCase(""))
								{
									op1=Long.parseLong(value[1]+"000");
								}else if(value.length==2)
								{
									op1=Long.parseLong(value[0]+"000");
								    op2=Long.parseLong(value[1]+"000");
								}
								Iterator it1=hourVsDiffList.keySet().iterator();
								while(it1.hasNext())
								{
									 long reqCount=0;
									Object dateKey=it1.next();
									List diffList=hourVsDiffList.get(dateKey);
									for(int i=0;i<diffList.size();i++)
									{
										
									if(value.length==1)
										{
												//long op=Long.parseLong(value[0]+"000");
												if((Long)diffList.get(i) > op1)
													{reqCount=reqCount+1;}
										}
									else if(value.length==2&&value[0].equalsIgnoreCase(""))
									       {
												//long op=Long.parseLong(value[1]+"000");
												if((Long)diffList.get(i)<= op1)
												{reqCount=reqCount+1;}
									       }
									else if(value.length==2)
											{
												/*long op1=Long.parseLong(value[0]+"000");
												long op2=Long.parseLong(value[1]+"000");*/
												if(op1 < (Long)diffList.get(i)&&(Long)diffList.get(i)<= op2)
												{reqCount=reqCount+1;}
									        }
									}
									outputStream.write(dateKey+","+key+","+reqCount);
								    outputStream.newLine();
								}
								
							}
						
				inputStream.close();
				outputStream.close();
				outputFiles[0] = outputFile;

						logger.debug("EOCNRequestTimeRangeConverter.convert() - finished converting input files successfully ");
						
						} catch (FileNotFoundException e) {
						logger.error("EOCNRequestTimeRangeConverter.convert() - Input file not found " + e);
						throw new ApplicationException(e);
						} catch (IOException e) {
						logger.error("EOCNRequestTimeRangeConverter.convert() - Couldn't read input file"
								+ e);
						throw new ApplicationException(e);
						}
						logger.debug("EOCNRequestTimeRangeConverter.convert() - finished converting input files successfully ");
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
	dateString = outDateFormat.format(date);
	return dateString;

}
/*private long getDiff(String date1Str, String  date2Str) throws ParseException {
	
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
}*/
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
		EOCNRequestTimeRangeConverter s = new EOCNRequestTimeRangeConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\trace2010091510.log");
		 s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
