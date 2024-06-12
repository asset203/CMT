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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class CCNCashingConverter extends AbstractTextConverter {
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		Logger logger = Logger.getLogger(systemName);
		logger
				.debug("Inside CCNCashingConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());
		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("CCNCashingConverter.convert() - converting file "
						+ inputFiles[i].getName());
				// CAP_CCN_CashingHH-ddMMyy
				String line;
				String date = "";
				try {
					if (inputFiles[i].getName().contains("-")
							&& inputFiles[i].getName().contains("Cashing"))
						date = getDate(inputFiles[i].getName().split("_")[inputFiles[i]
								.getName().split("_").length - 1]
								.split("Cashing")[1]);

				} catch (ParseException exc) {
					logger.error(exc);
					continue;
				}
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				boolean cashingFound = false;
				boolean cashSizeFound = false;
				boolean cashEntryFound = false;
				String CCNCashing = null;
				String cashSize = null;
				long cashEntry = 0;
				long totalMsisdn = 0;
				String key = null;
				while (inputStream.ready()) {
					 cashSizeFound = false;
					 cashEntryFound = false;
					line = inputStream.readLine();
					if (line != null && line.contains("::::::::::::::")) {
						line = inputStream.readLine();
						if (line != null && line.contains("cashing")) {
							CCNCashing = "";
							CCNCashing = line.split("cashing")[0].trim();

							line = inputStream.readLine();
							if (line != null && line.contains("::::::::::::::")) {
								cashingFound = true;
							}
						}
					} else if (cashingFound) {
						cashSize = "";
						cashEntry = 0;
						totalMsisdn = 0;
						key = "";
						cashingFound = false;
						if (line != null
								&& line
										.contains("Ccn-Account-Finder-Cache-Size:")) {
							cashSize = line
									.split("Ccn-Account-Finder-Cache-Size:")[1]
									.trim();
							cashSizeFound = true;
						}
						line = inputStream.readLine();
						try {
							if (line != null
									&& line
											.contains("Ccn-Account-Finder-Cache-Entries:")) {

								if (line
										.split("Ccn-Account-Finder-Cache-Entries:").length != 0) {
									cashEntry = Long
											.parseLong((!line
													.split("Ccn-Account-Finder-Cache-Entries:")[1]
													.trim().equals("")) ? line
													.split("Ccn-Account-Finder-Cache-Entries:")[1]
													.trim()
													: "0");
									totalMsisdn = cashEntry * 100;
									cashEntryFound = true;
								}

							}
						} catch (NumberFormatException exc) {
							logger.error(exc);
						}
						if (cashEntryFound && cashSizeFound) {
							cashEntryFound = false;
							cashSizeFound = false;
							key = date + "," + CCNCashing + "," + cashSize
									+ "," + cashEntry + "," + totalMsisdn;
							
							outputStream.write(key);
							outputStream.newLine();
						}
					}
				}
				inputStream.close();
			}

			outputStream.close();
			outputFiles[0] = outputFile;
			logger
					.debug("CCNCashingConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger
					.error("CCNCashingConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("CCNCashingConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("CCNCashingConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("HH-ddMMyy");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\phase10\\DataCollection");
			CCNCashingConverter s = new CCNCashingConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\build\\phase10\\DataCollection\\CAP_CCN_Cashing13-220911");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
