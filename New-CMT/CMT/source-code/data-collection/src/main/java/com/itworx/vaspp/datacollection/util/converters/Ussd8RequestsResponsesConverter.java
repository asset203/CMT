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
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;


public class Ussd8RequestsResponsesConverter extends AbstractTextConverter{
	
	
	private Logger logger;
	private Map<String, Entry> numOfResReqPerSubCode =  new HashMap<String, Entry>() ;
	private Map<String,String> msisdnVsSubCode =  new HashMap<String, String>() ; 
public Ussd8RequestsResponsesConverter()
{}

public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {

logger = Logger.getLogger(systemName);
logger
	.debug("Inside Ussd8RequestsResponsesConverter convert - started converting input files");

String path = PropertyReader.getConvertedFilesPath();
File[] outputFiles = new File[1];
//System.out.println("the file name "+inputFiles[0].getName());
File outputFile = new File(path, inputFiles[0].getName());

BufferedReader inputStream = null;
BufferedWriter outputStream;
String wholeSubCode=PropertyReader.getUssd8SubCodes();
String []subCodes=wholeSubCode.split(",");
List convertedSubCode=new ArrayList();
// Map reqTypesNumber=new HashMap();
for(int i=0;i<subCodes.length;i++)
{
convertedSubCode.add(subCodes[i]);

}


try {
outputStream = new BufferedWriter(new FileWriter(outputFile));
for (int i = 0; i < inputFiles.length; i++) {
	
	logger
			.debug("Ussd8RequestsResponsesConverter.convert() - converting file "
					+ inputFiles[i].getName());
   // System.out.println("Ussd8RequestsResponsesConverter.convert() - converting file "	+ inputFiles[i].getName());
   
	inputStream = new BufferedReader(new FileReader(inputFiles[i]));

	String line;
	String date = "";
	String msisdn=null;
	boolean validPairs=false;
	while (inputStream.ready()) {

		line = inputStream.readLine();
		if(!line.contains("-8-"))
		{continue;}
		else
		{
			try{
			date=getDate(line.split(" ")[0]+" "+line.split(" ")[1]);
			//System.out.println("date is :"+date);
			msisdn=((line.split(" ")[2]).split("\\[")[1]).split("\\]")[0];
			boolean found=false;
			String subCode="";
			String key="";
			if(line.contains("Request:"))
			{
				if((line.split(" ")[6]).split("#").length<1)
				subCode="Other";
				else
				subCode=(line.split(" ")[6]).split("#")[0];
				
				if(!convertedSubCode.contains(subCode))
					subCode="Other";
				//System.out.println("subCode:" +subCode);
				key=date+","+subCode;
				
				
					msisdnVsSubCode.put(msisdn,key);
					
					if(numOfResReqPerSubCode.containsKey(key))
					{
						Entry subCodeVsReq=(Entry)numOfResReqPerSubCode.get(key);
						Integer req=subCodeVsReq.getNumOfRequestsPerSubCode();
						subCodeVsReq.setNumOfRequestsPerSubCode(req+1);
						numOfResReqPerSubCode.remove(key);
						numOfResReqPerSubCode.put(key, subCodeVsReq);
						
					
					}
					else
					{
						Entry entry= new Entry();
						entry.setSubCode(subCode);
						entry.setNumOfRequestsPerSubCode(new Integer(1));
						entry.setNumOfResponsesPerSubCode(new Integer(0));
						numOfResReqPerSubCode.put(key, entry);
						
					}
					
					
					
					
			
					
				
			}
			else if(line.contains("Response:"))
			{
				if(msisdnVsSubCode.containsKey(msisdn))
				{
					key=msisdnVsSubCode.get(msisdn);
					msisdnVsSubCode.remove(msisdn);
					if(numOfResReqPerSubCode.containsKey(key))
					{
						Entry entry=numOfResReqPerSubCode.get(key);
						Integer resp=entry.getNumOfResponsesPerSubCode();
						entry.setNumOfResponsesPerSubCode(resp+1);
						numOfResReqPerSubCode.remove(key);
						numOfResReqPerSubCode.put(key, entry);
						
					}
					else
					{
						Entry entry = new Entry();
						entry.setNumOfResponsesPerSubCode(new Integer (1));
						numOfResReqPerSubCode.put(key, entry);
						
						
					}
				}
			}
			}catch(ParseException exc) { logger.error(exc) ; continue ;}
		}
	}
	inputStream.close();
	
	//System.out.println(inputFiles[0].getName());
	
	
}

			Iterator it=numOfResReqPerSubCode.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 Entry entry=numOfResReqPerSubCode.get(key);
				 Integer req=entry.getNumOfRequestsPerSubCode();
				 Integer resp=entry.getNumOfResponsesPerSubCode();
				 outputStream.write(key+","+req.longValue()+","+resp.longValue());
			    // System.out.println("the key "+key+","+((Integer)entry.getNumOfRequestsPerSubCode()).longValue()+","+((Integer)entry.getNumOfResponsesPerSubCode()).longValue());
				 outputStream.newLine();
				
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VSSMNetInitConverter.convert() - finished converting input files successfully ");

} catch (FileNotFoundException e) {
logger
		.error("Ussd8RequestsResponsesConverter.convert() - Input file not found "
				+ e);
throw new ApplicationException(e);
} catch (IOException e) {
logger
		.error("Ussd8RequestsResponsesConverter.convert() - Couldn't read input file"
				+ e);
throw new ApplicationException(e);
}
logger
	.debug("Ussd8RequestsResponsesConverter.convert() - finished converting input files successfully ");
return outputFiles;

}
private class Entry {
	Integer numOfRequestsPerSubCode;
	public Integer getNumOfRequestsPerSubCode() {
		return numOfRequestsPerSubCode;
	}
	public void setNumOfRequestsPerSubCode(Integer numOfRequestsPerSubCode) {
		this.numOfRequestsPerSubCode = numOfRequestsPerSubCode;
	}
	public Integer getNumOfResponsesPerSubCode() {
		return numOfResponsesPerSubCode;
	}
	public void setNumOfResponsesPerSubCode(Integer numOfResponsesPerSubCode) {
		this.numOfResponsesPerSubCode = numOfResponsesPerSubCode;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	Integer numOfResponsesPerSubCode;
	String subCode ; 
}
private String getDate(String line) throws ParseException {
	
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.mss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
	
		date = inDateFormat.parse(line);
	

	dateString = outDateFormat.format(date);

	return dateString;

}
public static void main(String ag[]) {
	
	
	
	try {
		String path = "D:\\build\\pahse8\\DataCollection\\";
		PropertyReader.init(path);
		PropertyReader.getUssdGWSubCodes();
		Ussd8RequestsResponsesConverter s = new Ussd8RequestsResponsesConverter();
		File[] input = new File[1];
		input[0]= new File("D:\\build\\pahse8\\DataCollection\\browser_8_2010081111.log") ;
		s.convert(input, "Test");

	} catch (Exception e) {
		e.printStackTrace();
	}

	

}
}
