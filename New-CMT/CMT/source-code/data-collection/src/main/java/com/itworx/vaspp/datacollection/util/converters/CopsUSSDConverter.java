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
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class CopsUSSDConverter extends AbstractTextConverter {
	private Logger logger;

	private final String pukRequest = "PUK_REQ";

	/**
	 * loop over input files, loop over lines in each file count requestper
	 * vodafone , ,request per mobinil , success requests ,fail request for each
	 * hour write output into one output file output file is placed on the
	 * configured converted file path
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		String line;
		String[] lineTokens;
		String serviceType;
		String requestType;
		String dateTimeString = "";
		String lastDateTime = "";
		Date dateTime = new Date();
		SimpleDateFormat dateFormater = new SimpleDateFormat();
		dateFormater.applyPattern("yyyy-MM-dd HH");
		HashMap requestCounts = new HashMap();
		Integer requestCount;
		long totalRequests = 0;
		logger = Logger.getLogger(systemName);
		logger
				.debug("CopsUSSDConverter.convert() - started converting input files ");
		try {
			String path = PropertyReader.getConvertedFilesPath();
			//String path="D:\\converted";
			File[] outputFiles = new File[1];
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			BufferedReader inputStream;
			System.out
					.println("com.itworx.vaspp.datacollection.util.converters.CopsUSSDConverter.convert()");
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("CopsUSSDConverter.convert() - converting file "
						+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				dateTime = null;
				serviceType = null;
				requestCounts.clear();
				totalRequests = 0;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					lineTokens = line.split(" ");
					if (lineTokens.length > 3) {
						try {
							dateTimeString = lineTokens[0] + " "
									+ lineTokens[1].substring(0, 2);
//							dateFormater.applyPattern("yyyy-MM-dd HH");
//							dateTime = dateFormater.parse(dateTimeString);
							serviceType = lineTokens[3].replace("-", "");
						} catch (IndexOutOfBoundsException e) {
							logger
									.error(
											"com.itworx.vaspp.datacollection.util.converters.USSDConverter.convert()",
											e);
							// e.printStackTrace();
							continue;
						}
					}
					if("".equals(lastDateTime))
						lastDateTime=dateTimeString+"";
					else if(!dateTimeString.equals(lastDateTime)){
						try {
							dateTime = dateFormater.parse(lastDateTime);
						} catch (ParseException e) {
							continue;
						}
						writeCounts(outputStream, dateTime, requestCounts, serviceType, totalRequests);
						requestCounts.clear();
						serviceType="";
						totalRequests=0;
						lastDateTime=dateTimeString+"";
					}
					if (line.endsWith("[op:1]")) {
						totalRequests++;
						lineTokens = line.split(" ");
						if (lineTokens == null)
							continue;
						requestType = lineTokens[lineTokens.length - 2];
						if(requestType==null)
							continue;
						requestType = requestType.split(";")[0];
						if (!validateRequestType(requestType)) {
							continue;
						}
						// puk requests stored as [mobilenumber#]
						if (requestType.startsWith("010")
								|| requestType.startsWith("2010")
								|| requestType.startsWith("016")
								|| requestType.startsWith("2016")) {
							if (requestCounts.containsKey(pukRequest)) {
								requestCount = (Integer) requestCounts
										.get(pukRequest);
								requestCount = new Integer(requestCount
										.intValue() + 1);
								requestCounts.put(pukRequest, requestCount);
							} else {
								requestCounts.put(pukRequest, new Integer(1));
							}
						} else {
							
							if ("".equals(requestType) || requestType == null)
								continue;
							if (requestCounts.containsKey(requestType)) {
								requestCount = (Integer) requestCounts
										.get(requestType);
								requestCount = new Integer(requestCount
										.intValue() + 1);
								requestCounts.put(requestType, requestCount);
							} else {
								requestCounts.put(requestType, new Integer(1));
							}
						}
					}
				}
				try {
					dateTime = dateFormater.parse(lastDateTime);
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				inputStream.close();
				writeCounts(outputStream, dateTime, requestCounts, serviceType,
						totalRequests);
			}
			outputStream.close();		
			outputFiles[0] = output;
			logger
					.debug("CopsUSSDConverter.convert() - finished converting input files successfully ");
			return outputFiles;

		} catch (FileNotFoundException e) {
			logger.error("CopsUSSDConverter.convert() - Input file not found "
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger
					.error("CopsUSSDConverter.convert() - Couldn't read input file"
							+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;

	}

	private boolean validateRequestType(String requestType){
		if (requestType == null)
			return false;
		if (requestType.startsWith("010") || requestType.startsWith("2010")
				|| requestType.startsWith("016")
				|| requestType.startsWith("2016")) {
			if (requestType.matches("[*]?[2]?[0-9]{10}#"))
				return true;
		} else if (requestType.matches("[*]?[0-9]{1,3}#"))
			return true;
		return false;
	}
	private void writeCounts(BufferedWriter outputStream, Date dateTime,
			HashMap requestCounts, String serviceType, long totalRequests)
			throws IOException {
		Set keys = requestCounts.keySet();
		SimpleDateFormat dateFormater = new SimpleDateFormat();
		dateFormater.applyPattern("MM/dd/yyyy HH:mm:ss");
		String dateTimeString = dateFormater.format(dateTime);
		String requestType;
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			requestType = "" + iter.next();
			outputStream.write(dateTimeString + ",");
			outputStream.write(requestType + ",");
			outputStream.write(serviceType + ",");
			outputStream.write(((Integer) requestCounts.get(requestType))
					.intValue()
					+ "");
			outputStream.newLine();
		}
		// append line for total requests
		outputStream.write(dateTimeString + ",");
		outputStream.write("-1" + ",");
		outputStream.write(serviceType + ",");
		outputStream.write(totalRequests + "");
		outputStream.newLine();
	}

	public static void main(String[] args) {
		CopsUSSDConverter converter = new CopsUSSDConverter();
		File[] input = new File[1];
		input[0] = new File("D:\\browser_999_2007051314.log");
		//input[0] = new File("D:\\browser_888_2007041010.log");
		//input[1] = new File("D:\\browser_999_2007041010.log");
		try {
			File[] output = converter.convert(input, "SDP");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		CopsUSSDConverter converter = new CopsUSSDConverter();
//		String requestType="010#";
//		System.out.println(requestType+" is "+(converter.validateRequestType(requestType)?"valid":"Not Valid"));
	}
}
