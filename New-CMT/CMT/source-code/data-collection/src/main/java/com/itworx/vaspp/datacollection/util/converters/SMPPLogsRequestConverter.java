package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SMPPLogsRequestConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,SmppReq> hourVsSmppReq=new HashMap<String,SmppReq>();
public SMPPLogsRequestConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside SMPPLogsRequestConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("SMPPLogsRequestConverter.convert() - converting file "
							+ inputFiles[i].getName());			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String date = "";
			String shortCode;
			String resultCode=" ";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("||"))
				{
					try 
					{
						String arr []=line.split("\\|\\|");
					    date =getDate(line.split("\\|\\|")[0]);
					    if(line.split("\\|\\|")[2].trim().equalsIgnoreCase("R"))
					    {
					    	
							    if(hourVsSmppReq.containsKey(date))
							    {
							    	SmppReq obj=hourVsSmppReq.get(date);
							    	obj.setTotlaReq(obj.getTotlaReq()+1);
							    	if(line.split("\\|\\|")[5].trim().equalsIgnoreCase("SUCCESS"))
							    		obj.setSuccReq(obj.getSuccReq()+1);
							    	if(!line.split("\\|\\|")[5].trim().equalsIgnoreCase("SUCCESS"))
							    		obj.setFailedReq(obj.getFailedReq()+1);
							    	if(!line.contains("SarRefNum ="))
							    		line=inputStream.readLine();
							    	if(line.contains("NBTrials = 0"))							    	
							    	 obj.setFirstRetrieval(obj.getFirstRetrieval()+1);		    	
							    	
							    	if(line.contains("NBTrials = 1"))
							    		obj.setSecondRetrieval(obj.getSecondRetrieval()+1);
							    	if(line.contains("NBTrials = 2"))
							    		obj.setThirdRetrieval(obj.getThirdRetrieval()+1);
							    	if(line.contains("NBTrials = 3"))
							    		obj.setFourthRetrieval(obj.getFourthRetrieval()+1);
							    	if(line.contains("NBTrials = 4"))
							    		obj.setFifthRetrieval(obj.getFifthRetrieval()+1);
							    	
							    	hourVsSmppReq.put(date, obj);
							    }
							    else
							    {
							    	SmppReq obj=new SmppReq();
							    	obj.setTotlaReq(1);
							    	if(line.split("\\|\\|")[5].trim().equalsIgnoreCase("SUCCESS"))
							    		obj.setSuccReq(1);
							    	if(!line.split("\\|\\|")[5].trim().equalsIgnoreCase("SUCCESS"))
							    		obj.setFailedReq(1);
							    	if(!line.contains("SarRefNum ="))
							    		line=inputStream.readLine();
							    	if(line.contains("NBTrials = 0"))
							    		obj.setFirstRetrieval(1);
							    	if(line.contains("NBTrials = 1"))
							    		obj.setSecondRetrieval(1);
							    	if(line.contains("NBTrials = 2"))
							    		obj.setThirdRetrieval(1);
							    	if(line.contains("NBTrials = 3"))
							    		obj.setFourthRetrieval(1);
							    	if(line.contains("NBTrials = 4"))
							    		obj.setFifthRetrieval(1);
							    	
							    	hourVsSmppReq.put(date, obj);
							    }
					    }
					   
					} catch(ParseException exc){ logger.error(exc); continue ;
					
					}
				}
			}
		}
	
	
	
	
	inputStream.close();
	Iterator it=hourVsSmppReq.keySet().iterator();
	while(it.hasNext())
	{
		Object key =it.next();
		SmppReq req= hourVsSmppReq.get(key);
		outputStream.write(key+","+req.getTotlaReq()+","+req.getSuccReq()+","+req.getFailedReq()+","+req.getFirstRetrieval()+","+req.getSecondRetrieval()+","+req.getThirdRetrieval()+","+req.getFourthRetrieval()+","+req.getFifthRetrieval());
		outputStream.newLine();
		
	}
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("SMPPLogsRequestConverter.convert() - finished converting input files successfully ");

	}
	catch (FileNotFoundException e) {
		logger
				.error("SMPPLogsRequestConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("SMPPLogsRequestConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("SMPPLogsRequestConverter.convert() - finished converting input files successfully ");
	return outputFiles;

	}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss.SSSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		SMPPLogsRequestConverter s = new SMPPLogsRequestConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\SMPP_LOGS_1307019835335_20_SMPP_OPS.log.2011-06-01-19.log");
			
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
class SmppReq
{
	long totlaReq=0;
	long succReq=0;
	long failedReq=0;
	long firstRetrieval=0;
	long secondRetrieval=0;
	long thirdRetrieval=0;
	long fourthRetrieval=0;
	long fifthRetrieval=0;
	public long getTotlaReq() {
		return totlaReq;
	}
	public void setTotlaReq(long totlaReq) {
		this.totlaReq = totlaReq;
	}
	public long getSuccReq() {
		return succReq;
	}
	public void setSuccReq(long succReq) {
		this.succReq = succReq;
	}
	public long getFailedReq() {
		return failedReq;
	}
	public void setFailedReq(long failedReq) {
		this.failedReq = failedReq;
	}
	public long getFirstRetrieval() {
		return firstRetrieval;
	}
	public void setFirstRetrieval(long firstRetrieval) {
		this.firstRetrieval = firstRetrieval;
	}
	public long getSecondRetrieval() {
		return secondRetrieval;
	}
	public void setSecondRetrieval(long secondRetrieval) {
		this.secondRetrieval = secondRetrieval;
	}
	public long getThirdRetrieval() {
		return thirdRetrieval;
	}
	public void setThirdRetrieval(long thirdRetrieval) {
		this.thirdRetrieval = thirdRetrieval;
	}
	public long getFourthRetrieval() {
		return fourthRetrieval;
	}
	public void setFourthRetrieval(long fourthRetrieval) {
		this.fourthRetrieval = fourthRetrieval;
	}
	public long getFifthRetrieval() {
		return fifthRetrieval;
	}
	public void setFifthRetrieval(long fifthRetrieval) {
		this.fifthRetrieval = fifthRetrieval;
	}
}
}
