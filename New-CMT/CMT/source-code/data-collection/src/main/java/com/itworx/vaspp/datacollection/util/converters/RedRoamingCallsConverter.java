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
import com.itworx.vaspp.datacollection.util.Utils;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;



public class RedRoamingCallsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Long> dateVSCount=new HashMap<String,Long>() ;
public RedRoamingCallsConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside RedRoamingCallsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("RedRoamingCallsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String aServiceClass;
			String zoneID;
			String ucipStatus;
			String fstLegCallStatus;
			String sstLegStatus;
			String fstCallLegDuration;
			String sstLegCallDuration;
			String bridgeDuration;
			String smsToAStatus;
			String retryCount;
			String key="";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=35&&line.contains("\""))
				{
					try 
					{			
						if(line.split(",")[0].contains("\"")&&line.split(",")[7].contains("\"")&&line.split(",")[10].contains("\"")&&line.split(",")[8].contains("\"")&&line.split(",")[17].contains("\"")
								&&line.split(",")[25].contains("\"")&&line.split(",")[20].contains("\"")&&line.split(",")[28].contains("\"")&&line.split(",")[31].contains("\"")&&line.split(",")[32].contains("\"")&&line.split(",")[34].contains("\""))
						{
						 date =line.split(",")[0].split("\"")[1].split("\"")[0];
				         date = getDate(date);				        
				         aServiceClass= Utils.stringBetween(line.split(",")[7], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[7], "\"", "\""):"";
				         zoneID= Utils.stringBetween(line.split(",")[10], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[10], "\"", "\""):"";
				         ucipStatus= Utils.stringBetween(line.split(",")[8], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[8], "\"", "\""):"";
				         fstLegCallStatus= Utils.stringBetween(line.split(",")[17], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[17], "\"", "\""):"";
				         sstLegStatus= Utils.stringBetween(line.split(",")[25], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[25], "\"", "\""):"";
				         fstCallLegDuration= Utils.stringBetween(line.split(",")[20], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[20], "\"", "\""):"";
				         sstLegCallDuration= Utils.stringBetween(line.split(",")[28], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[28], "\"", "\""):"";
				         bridgeDuration= Utils.stringBetween(line.split(",")[31], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[31], "\"", "\""):"";
				         smsToAStatus= Utils.stringBetween(line.split(",")[32], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[32], "\"", "\""):"";
				         retryCount= Utils.stringBetween(line.split(",")[34], "\"", "\"")!=null?Utils.stringBetween(line.split(",")[34], "\"", "\""):"";
				         key =date+","+aServiceClass+","+zoneID+","+ucipStatus+","+fstLegCallStatus+","+sstLegStatus+","+
				         fstCallLegDuration+","+sstLegCallDuration+","+bridgeDuration+","+smsToAStatus+","+retryCount;
				         
				         if(dateVSCount.containsKey(key))
				         {
				        	 long count =dateVSCount.get(key);
				        	 dateVSCount.remove(key);
				        	 dateVSCount.put(key, count+1);
				         }
				         else
				         {
				        	 dateVSCount.put(key, new Long (1));
				         }
						}
					}
					catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
		}//end of files 
	
		Iterator it = dateVSCount.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			outputStream.write(key+","+dateVSCount.get(key));
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("RedRoamingCallsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("RedRoamingCallsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("RedRoamingCallsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("RedRoamingCallsConverter.convert() - finished converting input files successfully ");
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
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		RedRoamingCallsConverter s = new RedRoamingCallsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\Report_CDR_UCB_2011032917.csv");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
