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

public class CCNTEMPConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String, Double> DataVolumeSumMap = new HashMap<String, Double>();
	private Map<String, Double> FinalChargeSumMap = new HashMap<String, Double>();
	private Map<String, Long> CountMap = new HashMap<String, Long>();

	public CCNTEMPConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("Inside CCNTEMPConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));

			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("CCNTEMPConverter.convert() - converting file "
						+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				String dateTime = "";
				String key = "";

				while (inputStream.ready()) {

					line = inputStream.readLine();

					if (line.contains("|"))
						if (line.split("\\|").length >= 29) {
							try {

								dateTime = getDate(line.split("\\|")[4]);
								long tac = 0;
								if (!line.split("\\|")[24].equals("")) {
									if (line.split("\\|")[24].length() >= 8) {
										tac = Long
												.parseLong(line.split("\\|")[24]
														.substring(0, 8));
									}

								}

								key = dateTime + "," + tac;

								long volume = (!line.split("\\|")[14]
										.equals("")) ? Long.parseLong(line
										.split("\\|")[14]) : 0;
								if (DataVolumeSumMap.containsKey(key)) {
									Double item = DataVolumeSumMap.get(key);
									DataVolumeSumMap.remove(key);
									DataVolumeSumMap.put(key, item + volume);
								} else {
									DataVolumeSumMap.put(key,
											new Double(volume));
								}

								Double charge = (!line.split("\\|")[28]
										.equals("")) ? Double.parseDouble(line
										.split("\\|")[28]) : 0;
								if (FinalChargeSumMap.containsKey(key)) {
									Double item = FinalChargeSumMap.get(key);
									FinalChargeSumMap.remove(key);
									FinalChargeSumMap.put(key, item + charge);
								} else {
									FinalChargeSumMap.put(key, new Double(
											charge));
								}

								if (CountMap.containsKey(key)) {
									long item = CountMap.get(key);
									CountMap.remove(key);
									CountMap.put(key, item + 1);
								} else {
									CountMap.put(key, new Long(1));
								}

							} catch (ParseException exc) {
								logger.error(exc);
								exc.printStackTrace();
								continue;
							} catch (NumberFormatException exc) {
								logger.error(exc);
								exc.printStackTrace();
								continue;
							}
						} else {
							continue;
						}
				}
			}
			Iterator it1 = DataVolumeSumMap.keySet().iterator();
			Iterator it2 = FinalChargeSumMap.keySet().iterator();
			Iterator it3 = CountMap.keySet().iterator();
			while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
				Object key = it1.next();
				outputStream.write(key + "," + DataVolumeSumMap.get(key) + ","
						+ FinalChargeSumMap.get(key) + "," + CountMap.get(key));

				outputStream.newLine();
			}
			inputStream.close();

			outputStream.close();
			outputFiles[0] = outputFile;
			logger.debug("CCNTEMPConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("CCNTEMPConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CCNTEMPConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("CCNTEMPConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;

		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	public static void main(String ag[]) {
		try {

			PropertyReader
					.init("D:\\VNPP2011\\Trunk\\SourceCode\\DataCollection");
			CCNTEMPConverter s = new CCNTEMPConverter();
			File[] input = new File[1];
			input[0] = new File(
					"C:\\Documents and Settings\\islam.yousry\\Desktop\\New Text Document.txt");

			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}