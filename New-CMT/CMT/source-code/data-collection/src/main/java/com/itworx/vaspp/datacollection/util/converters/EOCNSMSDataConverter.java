package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class EOCNSMSDataConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String, ECONSMSDataValue> dateVSTransactions = new HashMap<String, ECONSMSDataValue>();

	private static String RESALA_STR = "\u0627\u0644\u0631\u0633\u0627\u0644\u0629";
	private static String MEGA_STR = "\u0645\u064a\u062c\u0627";
	
	public EOCNSMSDataConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
			
		//update on production if it doesn't work directly to become either true or false
		
		logger = Logger.getLogger(systemName);
		logger.debug("Inside EOCNSMSDataConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("EOCNSMSDataConverter.convert() - converting file " + inputFiles[i].getName());

				inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(inputFiles[i]),"UTF8"));
				String line = null;
				String date = "";
				while ((line=inputStream.readLine())!= null) {
					if (line.contains(RESALA_STR)) {
						try {
							date = getDate(line.substring(0, 24));
							if (dateVSTransactions.containsKey(date)) {
								ECONSMSDataValue values = dateVSTransactions.get(date);
								dateVSTransactions.remove(date);
								values.incrementSMSCount();
								dateVSTransactions.put(date, values);
							} else {
								dateVSTransactions.put(date, new ECONSMSDataValue(1,0));
							}
						} catch (Exception e) {
							logger.error("EOCNSMSDataConverter.convert() - error while parsing date in line ("+line+")",e);
							continue;
						}
					} else if (line.contains(MEGA_STR)) {
						try {
							date = getDate(line.substring(0, 24));
							if (dateVSTransactions.containsKey(date)) {
								ECONSMSDataValue values = dateVSTransactions.get(date);
								dateVSTransactions.remove(date);
								values.incrementDataCount();
								dateVSTransactions.put(date, values);
							} else {
								dateVSTransactions.put(date, new ECONSMSDataValue(0,1));
							}
						} catch (Exception e) {
							logger.error("EOCNSMSDataConverter.convert() - error while parsing date in line ("+line+")",e);
							continue;
						}
					} else {
						continue;
					}
				}
				inputStream.close();
			}

			for(Entry<String, ECONSMSDataValue> entry: dateVSTransactions.entrySet()){
				outputStream.write(entry.getKey()+"," + entry.getValue().getValues());
				outputStream.newLine();
			}

			outputStream.close();
			outputFiles[0] = outputFile;
			logger.debug("EOCNSMSDataConverter.convert() - finished converting input files successfully ");
		} catch (FileNotFoundException e) {
			logger.error("EOCNSMSDataConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("EOCNSMSDataConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		}
		logger.debug("EOCNSMSDataConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private String getDate(String line) throws ParseException {
		Date date = null;
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:00:00");
		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;
	}

	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode_\\DataCollection\\");
			EOCNSMSDataConverter s = new EOCNSMSDataConverter();
			File[] input = new File[1];
			input[0] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_new_econ_dc_sms_data\\browser_511_2012021413.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ECONSMSDataValue{
		private int smsCount;
		private int dataCount;
		
		
		public ECONSMSDataValue(int smsCount, int dataCount) {
			this.smsCount = smsCount;
			this.dataCount = dataCount;
			
		}
		public int getSmsCount() {
			return smsCount;
		}
		public void setSmsCount(int smsCount) {
			this.smsCount = smsCount;
		}
		public int getDataCount() {
			return dataCount;
		}
		public void setDataCount(int dataCount) {
			this.dataCount = dataCount;
		}
		
		public void incrementSMSCount(){
			this.smsCount++;
		}
		
		public void incrementDataCount(){
			this.dataCount++;
		}
		
		public String getValues(){
			return this.smsCount+","+this.dataCount;
		}
	}

}
