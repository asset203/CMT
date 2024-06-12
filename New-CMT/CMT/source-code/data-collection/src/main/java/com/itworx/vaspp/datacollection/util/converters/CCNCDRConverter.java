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

public class CCNCDRConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String, Double> DataVolumeSumMap = new HashMap<String, Double>();
	private Map<String, Double> FinalChargeSumMap = new HashMap<String, Double>();
	private Map<String, Long> CountMap = new HashMap<String, Long>();

	public CCNCDRConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("Inside CCNCDRConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;

		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));

			for (int i = 0; i < inputFiles.length; i++) {

				logger.debug("CCNCDRConverter.convert() - converting file "
						+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));

				String line;
				String dateTime = "";
				String key = "";

				while (inputStream.ready()) {

					line = inputStream.readLine();

					if (line.contains("|"))
						if (line.split("\\|").length >= 73) {
							try {

								dateTime = getDate(line.split("\\|")[4]);
								String nodeId = (!line.split("\\|")[27]
										.equals("")) ? line.split("\\|")[27]
										: "";

								String apn = (!line.split("\\|")[5].equals("")) ? line
										.split("\\|")[5] : "";

								String chargingChar = (!line.split("\\|")[30]
										.equals("")) ? line.split("\\|")[30]
										: "";

								long serviceClass = (!line.split("\\|")[72]
										.equals("")) ? Long.parseLong(line
										.split("\\|")[72]) : 0;

								String bearerType = (!line.split("\\|")[21]
										.equals("")) ? line.split("\\|")[21]
										: "";

								key = dateTime + "," + nodeId + "," + apn + ","
										+ chargingChar + "," + serviceClass
										+ "," + bearerType;

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
				System.out.println(key + "," + DataVolumeSumMap.get(key) + ","
						+ FinalChargeSumMap.get(key) + "," + CountMap.get(key));
				outputStream.newLine();
			}
			inputStream.close();

			outputStream.close();
			outputFiles[0] = outputFile;
			logger.debug("CCNCDRConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("CCNCDRConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CCNCDRConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("CCNCDRConverter.convert() - finished converting input files successfully ");
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
					.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\DataCollection");
			CCNCDRConverter s = new CCNCDRConverter();
			File[] input = new File[13];
			input[0] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335712");
			input[1] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335713");
			input[2] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335714");
			input[3] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335715");
			input[4] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335716");
			input[5] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132335717");
			input[6] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540396");
			input[7] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540397");
			input[8] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540398");
			input[9] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540399");
			input[10] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540400");
			input[11] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-10122012132540401");
			input[12] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\09-12-2012\\Hourly\\ccn\\CCN-DATA-101220121350007822");

			s.convert(input, "Maha_Test");
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}