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

public class IVRCallsConverter extends AbstractTextConverter {
	private static final String SPACE = " ";
	private static final String BT = "BT";
	private static final String BE = "BE";
	private Logger logger;
	private Map<String, Long> dateVSTransCount = new HashMap<String, Long>();
	private SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:00:00");
	
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("Inside IVRCallsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("IVRCallsConverter.convert() - converting file "
						+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				String date = "";
				String transType = "";
				String key = "";
				String[] lineArr = null;
				while ((line = inputStream.readLine()) != null) {
					if (line.contains(SPACE) 
							&& (lineArr = line.split(SPACE)).length >= 3) {
						try {
							date = getDate(lineArr[0] + SPACE + lineArr[1]);
							transType = lineArr[2];
							if(!BT.equalsIgnoreCase(transType) && !BE.equalsIgnoreCase(transType))
								continue;
							
							key = date + "," + transType;
							if (dateVSTransCount.containsKey(key)) {
								long count = dateVSTransCount.get(key);
								dateVSTransCount.put(key, count + 1);
							} else {
								dateVSTransCount.put(key, new Long(1));
							}
						} catch (ParseException exc) {
							logger.error(exc);
							continue;
						}
					}
				}
				inputStream.close();
			}
			for (Entry<String, Long> counterEntry : dateVSTransCount.entrySet()) {
				Object key = counterEntry.getKey();
				outputStream.write(key + "," + counterEntry.getValue());
				outputStream.newLine();
			}

			outputStream.close();
			outputFiles[0] = outputFile;
			logger
					.debug("IVRCallsConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("IVRCallsConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("IVRCallsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("IVRCallsConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode_\\DataCollection");
			IVRCallsConverter s = new IVRCallsConverter();
			File[] input = new File[1];
			input[0] = new File(
					"C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120612_update_ivr_calls_868_to_become_only_for_BE_BT\\868 IVR calls.txt");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
