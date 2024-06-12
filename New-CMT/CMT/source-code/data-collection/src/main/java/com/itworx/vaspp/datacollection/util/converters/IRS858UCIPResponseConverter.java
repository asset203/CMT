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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class IRS858UCIPResponseConverter extends AbstractTextConverter {

	private static final String OUTPUT_DATE_FORMAT = "MM/dd/yyyy HH:00:00";
	private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
	private static final String SEP = ",";
	private static final int MSISDN_TO_LENGTH = 7;
	private static final String DATE_START_INDICATOR = "[";
	private static final String DATE_END_INDICATOR = "]";
	private static final String RESPONSE_INDICATOR = "Response: ";
	private static final String METHOD_INDICATOR = "Method: ";
	private static final String AIR_IP_INDICATOR = "AIR IP: http://";
	private static final String MSISDN_INDICATOR = "MSISDN: ";

	@Override
	public File[] convert(File[] inputFiles, String systemName) throws ApplicationException, InputException {
		Logger logger = Logger.getLogger(systemName);
		Map<String, Long> dateVSKeyCount = new HashMap<String, Long>();
		logger.debug("IRS858UCIPResponseConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName() + "_" + systemName + "_UCIPResponse_" + new Date().getTime());

		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("IRS858UCIPResponseConverter.convert() - converting file " + inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				String date = "";
				String msisdn = "";
				String prefix = "";
				String airIP = "";
				String transactionType = "";
				String response = "";
				String key = "";
				try {
					while ((line = inputStream.readLine()) != null) {
						try{
							if(!(line.startsWith(DATE_START_INDICATOR) && line.contains(DATE_END_INDICATOR))){
								continue;
							}
							
							date = getDate(Utils.stringBetween(line, DATE_START_INDICATOR, DATE_END_INDICATOR));
							
							if (!(line.contains(MSISDN_INDICATOR) 
									&& line.contains(AIR_IP_INDICATOR) 
									&& line.contains(METHOD_INDICATOR) 
									&& line.contains(RESPONSE_INDICATOR)
									&& !Utils.isEmpty(date))) {
								continue;
							}
							
							msisdn = Utils.stringBetween(line, MSISDN_INDICATOR, SEP);
							prefix = Utils.trimWithLen(msisdn, MSISDN_TO_LENGTH);
							airIP = Utils.stringBetween(line, AIR_IP_INDICATOR, ":");
							transactionType = Utils.stringBetween(line, METHOD_INDICATOR, SEP);
							response = Utils.stringFrom(line, RESPONSE_INDICATOR);
							
							key = date + SEP + prefix + SEP + airIP + SEP + transactionType + SEP + response;
							if (dateVSKeyCount.containsKey(key)) {
								long count = dateVSKeyCount.get(key);
								dateVSKeyCount.put(key, count + 1);
							} else {
								dateVSKeyCount.put(key, new Long(1));
							}
						} catch (ParseException e) {
							logger.error("IRS858UCIPResponseConverter.convert() - parsing exception occcurred wbile parsing line : ["+line+DATE_END_INDICATOR,e);
							continue;
						}
					}
				} catch (Exception e) {
					logger.error("IRS858UCIPResponseConverter.convert() - error occcurred wbile parsing ",e);
				} finally {
					inputStream.close();
				}
			}
			
			writeMapAndClear(dateVSKeyCount, outputStream);
			outputFiles[0] = outputFile;
			logger.debug("IRS858UCIPResponseConverter.convert() - finished converting input files successfully ");
		} catch (FileNotFoundException e) {
			logger.error("IRS858UCIPResponseConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("IRS858UCIPResponseConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("IRS858UCIPResponseConverter.convert() - Couldn't read input file" , e);
					throw new ApplicationException(e);
				}
			}
		}
		logger.debug("IRS858UCIPResponseConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	private void writeMapAndClear(Map<String, Long> map,
			BufferedWriter outputStream) throws IOException {
		for (Entry<String, Long> counterEntry : map.entrySet()) {
			Object key = counterEntry.getKey();
			outputStream.write(key + SEP + counterEntry.getValue());
			outputStream.newLine();
		}
		map.clear();
	}
	
	private String getDate(String dateStr) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT);
		SimpleDateFormat outDateFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
		date = inDateFormat.parse(dateStr);
		dateString = outDateFormat.format(date);
		return dateString;
	}
	
	public static void main(String arg[]) {
		try {

			PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode\\DataCollection");
			IRS858UCIPResponseConverter s = new IRS858UCIPResponseConverter();
			File[] input = new File[1];
			input[0] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\868_IRS_UCIP_RESPONSE\\irs_log.txt");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
