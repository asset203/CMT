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
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class RamaUssdConnectorConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,ReqRespCount> dateVSReqRespCounts=new HashMap<String,ReqRespCount>() ;
	private Map  <String ,String> msisdnVSgift=new HashMap<String,String>() ;
	private Map  <String ,String> msisdnVSenquire=new HashMap<String,String>() ;
	private Map  <String ,String> msisdnVSred=new HashMap<String,String>() ;
public RamaUssdConnectorConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside RamaUssdConnectorConverterconvert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("RamaUssdConnectorConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String msisdn=null;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=3)
				{
					try{
					    date=getDate(line.split(",")[0]);
					   if(line.split(",")[2].contains("begin RAMADAN_PROMO.USSD_Enquire_Gifts")&&line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))
					   {
						   msisdn=line.split("\\[")[1].split("\\]")[0];
						   if(!msisdnVSenquire.containsKey(msisdn))
						   {
							   msisdnVSenquire.put(msisdn, date);
						   }
						   if(dateVSReqRespCounts.containsKey(date))
						   {
							   ReqRespCount obj=dateVSReqRespCounts.get(date);
							   obj.setEnquireReqCount(obj.getEnquireReqCount()+1);
							   dateVSReqRespCounts.put(date, obj);
						   }
						   else
						   {
							   ReqRespCount obj=new ReqRespCount();
							   obj.setEnquireReqCount(1);
							   dateVSReqRespCounts.put(date, obj);
						   }
					   }
					   else if(line.split(",")[2].contains("begin RAMADAN_PROMO.USSD_Assign_Gift")&&line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))
					   {
						   msisdn=line.split("\\[")[1].split("\\]")[0];
						   if(!msisdnVSgift.containsKey(msisdn))
						   {
							   msisdnVSgift.put(msisdn, date);
						   }
						   if(dateVSReqRespCounts.containsKey(date))
						   {
							   ReqRespCount obj=dateVSReqRespCounts.get(date);
							   obj.setGiftReqCount(obj.getGiftReqCount()+1);
							   dateVSReqRespCounts.put(date, obj);
						   }
						   else
						   {
							   ReqRespCount obj=new ReqRespCount();
							   obj.setGiftReqCount(1);
							   dateVSReqRespCounts.put(date, obj);
						   }
					   }
					   else if(line.split(",")[2].contains("begin RAMADAN_PROMO.USSD_Redeem_Gift")&&line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))
					   {
						   msisdn=line.split("\\[")[1].split("\\]")[0];
						  
						   if(!msisdnVSred.containsKey(msisdn))
						   {
							   msisdnVSred.put(msisdn, date);
						   }
						   if(dateVSReqRespCounts.containsKey(date))
						   {
							   ReqRespCount obj=dateVSReqRespCounts.get(date);
							   obj.setRedReqCount(obj.getRedReqCount()+1);
							   dateVSReqRespCounts.put(date, obj);
						   }
						   else
						   {
							   ReqRespCount obj=new ReqRespCount();
							   obj.setRedReqCount(1);
							   dateVSReqRespCounts.put(date, obj);
						   }
					   }
					   else if(line.split(",")[2].contains("AppResponse")&&line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))
					   {
						   msisdn=line.split("\\[")[1].split("\\]")[0];
						  if(msisdnVSenquire.containsKey(msisdn))
						  {
							  String respdate=msisdnVSenquire.get(msisdn);
							  msisdnVSenquire.remove(msisdn);
							  if(dateVSReqRespCounts.containsKey(respdate))
							  {
								  ReqRespCount obj= dateVSReqRespCounts.get(respdate);
								  obj.setEnquireRespCount(obj.getEnquireRespCount()+1);
								  dateVSReqRespCounts.put(respdate, obj);
								  
							  }else 
							  {   
								  ReqRespCount obj= new ReqRespCount ();
								  obj.setEnquireRespCount(1);
								  dateVSReqRespCounts.put(respdate, obj);
							  }
						  }
						  if(msisdnVSgift.containsKey(msisdn))
						  {
							  String respDate=msisdnVSgift.get(msisdn);
							  msisdnVSgift.remove(msisdn);
							  if(dateVSReqRespCounts.containsKey(respDate))
							  {
								  ReqRespCount obj= dateVSReqRespCounts.get(respDate);
								  obj.setGiftRespCount(obj.getGiftRespCount()+1);
								  dateVSReqRespCounts.put(respDate, obj);
								  
							  }
							  else
							  {   
								  ReqRespCount obj=new ReqRespCount ();
								  obj.setGiftRespCount(1);
								  dateVSReqRespCounts.put(respDate, obj);
							  }
						  }
						  if(msisdnVSred.containsKey(msisdn))
						  {
							  String respData=msisdnVSred.get(msisdn);
							  msisdnVSred.remove(msisdn);
							  if(dateVSReqRespCounts.containsKey(respData))
							  {
								  ReqRespCount obj= dateVSReqRespCounts.get(respData);
								  obj.setRedRespCount(obj.getRedRespCount()+1);
								  dateVSReqRespCounts.put(respData, obj);
							  }
							  else
							  {   
								  ReqRespCount obj=new ReqRespCount ();
								  obj.setRedRespCount(obj.getRedRespCount()+1);
								  dateVSReqRespCounts.put(respData, obj);
							  }
						  }
					   }
					 
					
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}inputStream.close();
		Iterator it=dateVSReqRespCounts.keySet().iterator();
		while(it.hasNext())
		{
		  Object obj=it.next();
		  ReqRespCount reqObj= dateVSReqRespCounts.get(obj);
		  outputStream.write(obj+","+reqObj.getEnquireReqCount()+","+reqObj.getEnquireRespCount()+","+reqObj.getGiftReqCount()+","+reqObj.getGiftRespCount()+","+reqObj.getRedReqCount()+","+reqObj.getRedRespCount());
		  outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("RamaUssdConnectorConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("RamaUssdConnectorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("RamaUssdConnectorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("RamaUssdConnectorConverter.convert() - finished converting input files successfully ");
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
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		RamaUssdConnectorConverter s = new RamaUssdConnectorConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2011073020.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class ReqRespCount
{
	long enquireRespCount=0;
	long enquireReqCount=0;
	long giftRespCount=0;
	long giftReqCount=0;
	long redRespCount=0;
	long redReqCount=0;
	public long getEnquireRespCount() {
		return enquireRespCount;
	}
	public void setEnquireRespCount(long enquireRespCount) {
		this.enquireRespCount = enquireRespCount;
	}
	public long getEnquireReqCount() {
		return enquireReqCount;
	}
	public void setEnquireReqCount(long enquireReqCount) {
		this.enquireReqCount = enquireReqCount;
	}
	public long getGiftRespCount() {
		return giftRespCount;
	}
	public void setGiftRespCount(long giftRespCount) {
		this.giftRespCount = giftRespCount;
	}
	public long getGiftReqCount() {
		return giftReqCount;
	}
	public void setGiftReqCount(long giftReqCount) {
		this.giftReqCount = giftReqCount;
	}
	public long getRedRespCount() {
		return redRespCount;
	}
	public void setRedRespCount(long redRespCount) {
		this.redRespCount = redRespCount;
	}
	public long getRedReqCount() {
		return redReqCount;
	}
	public void setRedReqCount(long redReqCount) {
		this.redReqCount = redReqCount;
	}
	
}
}
