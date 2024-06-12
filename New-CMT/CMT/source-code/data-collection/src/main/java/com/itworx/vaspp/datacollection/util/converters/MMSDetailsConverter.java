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


public class MMSDetailsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,MMS> dateVSobjSum=new HashMap<String,MMS>() ;
public MMSDetailsConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside MMSDetailsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("MMSDetailsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String key;
			String date = "";	
			long OIWTYPE;
			long DIWTYPE;
			long SUCCESS_INDICATOR;
			long ERROR_CAUSE;
			long MESSAGE_CLASS;
			long SERVICE_TYPE;
			long SENDER_CHARGING_TYPE;
			long RECIPIENT_CHARGING_TYPE;
			long SENDER_PREPAID_STATUS;
			long RECIPIENT_PREPAID_STATUS;
			long CHARGED_PARTY;
			long DELIVERY_REPORT;
			//String ORIG_IMSI;
			String ORIG_MMSC;
			//String DEST_IMSI;
			String DEST_MMSC;
			long DELIVERY_STATUS;
			long MESSAGE_SIZE;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=50)
				{
					if(line.split(",")[6].contains("\""))
					{
						try{
						date=getDate(line.split(",")[6].split("\"")[1].split("\"")[0]);
						OIWTYPE=Long.parseLong(!(line.split(",")[4].equalsIgnoreCase(""))?line.split(",")[4]:"0");
						DIWTYPE=Long.parseLong(!(line.split(",")[5].equalsIgnoreCase(""))?line.split(",")[5]:"0");
						SUCCESS_INDICATOR=Long.parseLong(!(line.split(",")[7].equalsIgnoreCase(""))?line.split(",")[7]:"0");
						ERROR_CAUSE=Long.parseLong(!(line.split(",")[8].equalsIgnoreCase(""))?line.split(",")[8]:"0");
						MESSAGE_CLASS=Long.parseLong(!(line.split(",")[12].equalsIgnoreCase(""))?line.split(",")[12]:"0");
						SERVICE_TYPE=Long.parseLong(!(line.split(",")[13].equalsIgnoreCase(""))?line.split(",")[13]:"0");
						SENDER_CHARGING_TYPE=Long.parseLong(!(line.split(",")[15].equalsIgnoreCase(""))?line.split(",")[15]:"0");
						RECIPIENT_CHARGING_TYPE=Long.parseLong(!(line.split(",")[16].equalsIgnoreCase(""))?line.split(",")[16]:"0");
						SENDER_PREPAID_STATUS=Long.parseLong(!(line.split(",")[17].equalsIgnoreCase(""))?line.split(",")[17]:"0");
						RECIPIENT_PREPAID_STATUS=Long.parseLong(!(line.split(",")[18].equalsIgnoreCase(""))?line.split(",")[18]:"0");
						CHARGED_PARTY=Long.parseLong(!(line.split(",")[19].equalsIgnoreCase(""))?line.split(",")[19]:"0");
						DELIVERY_REPORT=Long.parseLong(!(line.split(",")[21].equalsIgnoreCase(""))?line.split(",")[21]:"0");
						/*ORIG_IMSI=(!(line.split(",")[38].equalsIgnoreCase(""))?line.split(",")[38]:" ");
						if(ORIG_IMSI.contains("\""))
							ORIG_IMSI=ORIG_IMSI.split("\"")[1].split("\"")[0];*/
						ORIG_MMSC=(!(line.split(",")[40].equalsIgnoreCase(""))?line.split(",")[40]:" ");
						if(ORIG_MMSC.contains("\""))
							ORIG_MMSC=ORIG_MMSC.split("\"")[1].split("\"")[0];
					/*	DEST_IMSI=(!(line.split(",")[41].equalsIgnoreCase(""))?line.split(",")[41]:" ");
						if(DEST_IMSI.contains("\""))
							DEST_IMSI=DEST_IMSI.split("\"")[1].split("\"")[0];*/
						DEST_MMSC=(!(line.split(",")[43].equalsIgnoreCase(""))?line.split(",")[43]:" ");
						if(DEST_MMSC.contains("\""))
							DEST_MMSC=DEST_MMSC.split("\"")[1].split("\"")[0];
						DELIVERY_STATUS=Long.parseLong(!(line.split(",")[48].equalsIgnoreCase(""))?line.split(",")[48]:"0");
						key=date+","+OIWTYPE+","+DIWTYPE+","+SUCCESS_INDICATOR+","+ERROR_CAUSE+","+MESSAGE_CLASS+","+SERVICE_TYPE+","+SENDER_CHARGING_TYPE+","+RECIPIENT_CHARGING_TYPE
						+","+SENDER_PREPAID_STATUS+","+RECIPIENT_PREPAID_STATUS+","+CHARGED_PARTY+","+DELIVERY_REPORT+","/*+ORIG_IMSI+","*/+ORIG_MMSC+","+/*DEST_IMSI+","+*/DEST_MMSC+","+DELIVERY_STATUS;
						MESSAGE_SIZE=Long.parseLong(!(line.split(",")[49].equalsIgnoreCase(""))?line.split(",")[49]:"0");
						if(dateVSobjSum.containsKey(key))
						{
							MMS obj=dateVSobjSum.get(key);
							obj.setMessageSize(obj.getMessageSize()+MESSAGE_SIZE);
							obj.setCount(obj.getCount()+1);
							dateVSobjSum.put(key, obj);
						}else
							
						{   MMS obj=new MMS();
							obj.setMessageSize(MESSAGE_SIZE);
						    obj.setCount(1);
						    dateVSobjSum.put(key, obj);
							
						}
						 
						} catch(ParseException exc){ logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}
				}
			}
		}
		inputStream.close();
		Iterator it=dateVSobjSum.keySet().iterator();
		while (it.hasNext())
		{
			Object key =it.next();
			MMS obj=(MMS)dateVSobjSum.get(key);
			outputStream.write(key+","+obj.getMessageSize()+","+obj.getCount());			
			outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("MMSDetailsConverter.convert() - finished converting input files successfully ");

		}
		catch (FileNotFoundException e) {
			logger
					.error("USSDSubCallsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("MMSDetailsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("MMSDetailsConverter.convert() - finished converting input files successfully ");
		return outputFiles;

		}
		private String getDate(String line) throws ParseException {
			String[] tokens = null;
			Date date = new Date();
			String dateString;
			SimpleDateFormat inDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat outDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy HH:00:00");

			
				date = inDateFormat.parse(line);
			dateString = outDateFormat.format(date);
			return dateString;

		}
		public static void main(String ag[]) {
			try {
				
				PropertyReader.init("D:\\build\\phase10\\DataCollection");
				MMSDetailsConverter s = new MMSDetailsConverter();
				File[] input = new File[1];
				input[0]=new File("D:\\build\\phase10\\DataCollection\\BF_prepost_20110818105054_52_0877.DAT");
			//input[1]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2010112814.log");		
				   s.convert(input,"Maha_Test");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		class MMS
		{
			long messageSize=0;
			long count=0;
			public long getMessageSize() {
				return messageSize;
			}
			public void setMessageSize(long messageSize) {
				this.messageSize = messageSize;
			}
			public long getCount() {
				return count;
			}
			public void setCount(long count) {
				this.count = count;
			}
		}
}
