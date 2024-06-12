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
import com.itworx.vaspp.datacollection.util.converters.CCNEventChargingConverter.SummationObj;


public class ChargingInterfaceSquareLogsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,ReqResp> dateVSReqResp=new HashMap<String,ReqResp>() ;
public ChargingInterfaceSquareLogsConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside ChargingInterfaceSquareLogsConverterconvert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("ChargingInterfaceSquareLogsConverter.convert() - converting file "
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
					if(line.contains("Start OptIn Request  "))
					{
						if(dateVSReqResp.containsKey(date))
						{
							ReqResp reqresp=(ReqResp)dateVSReqResp.get(date);
							reqresp.setReqCount(reqresp.getReqCount()+1);
							dateVSReqResp.put(date, reqresp);
						}
						else
						{
							ReqResp reqresp= new ReqResp ();
							reqresp.setReqCount(1);
							dateVSReqResp.put(date, reqresp);
						}
					}
					else if (line.contains("Start Inquiry Request"))
					{
						if(dateVSReqResp.containsKey(date))
						{
							ReqResp reqresp=(ReqResp)dateVSReqResp.get(date);
							reqresp.setEnquiryReqCount(reqresp.getEnquiryReqCount()+1);
							dateVSReqResp.put(date, reqresp);
						}
						else
						{
							ReqResp reqresp= new ReqResp ();
							reqresp.setEnquiryReqCount(1);
							dateVSReqResp.put(date, reqresp);
						}
					}
					else if (line.contains("request finished in"))
					{
						if(dateVSReqResp.containsKey(date))
						{
						ReqResp reqresp=(ReqResp)dateVSReqResp.get(date);
						reqresp.setRespCount(reqresp.getRespCount()+1);
						dateVSReqResp.put(date, reqresp);
						}
						else
						{
						ReqResp reqresp= new ReqResp ();
						reqresp.setRespCount(1);
						dateVSReqResp.put(date, reqresp);
						}
					}
					
					
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}
		inputStream.close();
		Iterator it=dateVSReqResp.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 ReqResp obj=(ReqResp)dateVSReqResp.get(key);			
	    	outputStream.write(key+","+obj.getReqCount()+","+obj.getRespCount()+","+obj.getEnquiryReqCount());
			 outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("ChargingInterfaceSquareLogsConverter.convert() - finished converting input files successfully ");

	}
	catch (FileNotFoundException e) {
			logger
					.error("ChargingInterfaceSquareLogsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("ChargingInterfaceSquareLogsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("ChargingInterfaceSquareLogsConverter.convert() - finished converting input files successfully ");
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
		ChargingInterfaceSquareLogsConverter s = new ChargingInterfaceSquareLogsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\CHA_INT_SQR_LOG_1307017076692_1_square.log");
		   s.convert(input,"Maha_Test");
		System.out.println("FINISHED ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class ReqResp
{
	long reqCount=0;
	long respCount=0;
	long enquiryReqCount=0;
	public long getEnquiryReqCount() {
		return enquiryReqCount;
	}
	public void setEnquiryReqCount(long enquiryReqCount) {
		this.enquiryReqCount = enquiryReqCount;
	}
	public long getReqCount() {
		return reqCount;
	}
	public void setReqCount(long reqCount) {
		this.reqCount = reqCount;
	}
	public long getRespCount() {
		return respCount;
	}
	public void setRespCount(long respCount) {
		this.respCount = respCount;
	}
}
}
