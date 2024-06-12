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

public class EOCNBrowsersResponsesConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String, Integer> responses = new HashMap<String, Integer>();
	private Map<String, Integer> errorCodes = new HashMap<String, Integer>();

	public EOCNBrowsersResponsesConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {

		logger = Logger.getLogger(systemName);
		logger.debug("Inside EOCNBrowserReasonsConverter convert - started converting input files");

		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			String line;
			String dateByHour = "";
			String prevHour = "";

			for (int i = 0; i < inputFiles.length; i++) {

				logger.debug("EOCNBrowserReasonsConverter.convert() - converting file "
						+ inputFiles[i].getName());
				System.out
						.println("EOCNBrowserReasonsConverter.convert() - converting file "
								+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));

				while (inputStream.ready()) {

					line = inputStream.readLine();
					String[] lineParts = null;

					if (line.contains("[")) {
						lineParts = line.split(" \\[");

						try {
							dateByHour = getDate(lineParts[0]);
						} catch (Exception exc) {
							//logger.error(exc);
							continue;
						}

						// If a new hour came //
						if (!prevHour.equals(dateByHour)) {
							addNewHourDetails(prevHour);
						}
						prevHour = dateByHour;
						// If a new hour came //

						if (line.contains("Response"))// the response
						{
							// get Error from the line//

							String StrAfterResp, errStr;
							String firstsubString = "Response: ";
							StrAfterResp = line.substring(line
									.indexOf(firstsubString)
									+ firstsubString.length());
							String secondSubString = " [";
							errStr = StrAfterResp.substring(0,
									StrAfterResp.indexOf(secondSubString));

							// get Error from the line//

							if (errorCodes.containsKey(errStr)) {

								Integer count = errorCodes.get(errStr);
								errorCodes.put(errStr,
										new Integer(count.intValue() + 1));
							} else {
								Integer count = new Integer(1);
								errorCodes.put(errStr, count);
							}

						} else {
							continue;
						}
					}
				}
				inputStream.close();
			}

			addNewHourDetails(dateByHour);
			Iterator<String> it = responses.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				Integer errorCount = responses.get(key);
				outputStream.write(key + "," + errorCount);
				outputStream.newLine();
			}
			inputStream.close();
			outputStream.close();
			outputFiles[0] = outputFile;

			logger.debug("EOCNBrowserReasonsConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("EOCNBrowserReasonsConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("EOCNBrowserReasonsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("EOCNBrowserReasonsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	private void addNewHourDetails(String dateByHour) {

		if (!responses.containsKey(dateByHour) && !dateByHour.equals("")) {
			Iterator<String> errors = errorCodes.keySet().iterator();

			while (errors.hasNext()) {
				String errorKey = errors.next();
				Integer count = errorCodes.get(errorKey);
				responses.put(dateByHour + "," + errorKey, count);
			}
			errorCodes.clear();
		}

	}

	public static void main(String ag[]) {

		try {
			String path = "D:\\VNPP\\phase8\\DataCollection\\";
			PropertyReader.init(path);
			PropertyReader.getUssdGWSubCodes();
			EOCNBrowsersResponsesConverter s = new EOCNBrowsersResponsesConverter();
			File[] input = new File[3];

			input[0] = new File(
					"D:\\VNPP\\phase8\\EOCNResponses\\trace2010090815.log");
			input[1] = new File(
					"D:\\VNPP\\phase8\\EOCNResponses\\trace2010090814.log");
			input[2] = new File(
					"D:\\VNPP\\phase8\\EOCNResponses\\trace2010090813.log");

			s.convert(input, "Test");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getDate(String line) throws ParseException {
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

	class ErrorCodeMapping {
		public String error = "";
		public int count = 0;

		public ErrorCodeMapping() {
		}

		public ErrorCodeMapping(String error, int count) {
			this.error = error;
			this.count = count;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}
