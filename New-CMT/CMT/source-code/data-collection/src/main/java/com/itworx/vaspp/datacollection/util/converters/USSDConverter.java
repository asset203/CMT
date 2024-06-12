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
import com.itworx.vaspp.datacollection.util.Utils;

public class USSDConverter extends AbstractTextConverter {
	private Logger logger;

	private String[] vodafoneKeysArr = null;
	private String[] mobinilKeysArr = null;
	private String[] etisalatKeysArr = null;
	/**
	 * loop over input files, loop over lines in each file count requestper vodafone ,
	 * ,request per mobinil , success requests ,fail request
	 * for each hour write output into one output file output
	 * file is placed on the configured converted file path
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
		String number;
		String dateTimeString = "";
		Date dateTime=new Date();
		SimpleDateFormat dateFormater = new SimpleDateFormat();
		HashMap records = new HashMap();
		USSDRecord currentRecord = new USSDRecord();
		logger = Logger.getLogger(systemName);
		String responseRegex="\\[op:32[;\\]].*$";
		logger
				.debug("USSDConverter.convert() - started converting input files ");
		try {
			String path = PropertyReader.getConvertedFilesPath();
			vodafoneKeysArr = PropertyReader.getVodafoneKeys().split(",");
			mobinilKeysArr = PropertyReader.getMobinilKeys().split(",");
			etisalatKeysArr = PropertyReader.getEtisalatKeys().split(",");
			
			File[] outputFiles = new File[1];
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			BufferedReader inputStream;
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("USSDConverter.convert() - converting file "
						+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				while (inputStream.ready()) {
					line = inputStream.readLine();
					lineTokens = line.split(" ");
					if (lineTokens.length > 2) {
						try {
						dateTimeString = lineTokens[0] + " "
						+ lineTokens[1].substring(0, 2);
							dateFormater.applyPattern("yyyy-MM-dd HH");
							dateTime = dateFormater.parse(dateTimeString);

						} catch (ParseException e) {
							logger
									.error(
											"com.itworx.vaspp.datacollection.util.converters.USSDConverter.convert()",
											e);
							// in case date is not in the correct format ignore this record
							continue;
						}
					}
					else 
						continue;
					if (records.containsKey(dateTime)) {
						currentRecord = (USSDRecord) records.get(dateTime);
					} else {
						currentRecord = new USSDRecord();
						records.put(dateTime, currentRecord);
					}

					//if (line.endsWith("Message sent successfully [op:32]")){
					if (line.matches("^.+Message sent successfully.+"+responseRegex)){
						currentRecord.successRequests++;
						continue;
					}
					//else if (line.endsWith("[op:32]"))
					else if (line.matches("^.+"+responseRegex))
					{
						currentRecord.unsuccessRequests++;
						continue;
					}
					
					if (line.endsWith("wait")) {
						if (inputStream.ready()
								//&& inputStream.readLine().equals(" [op:32]")) {
								&& inputStream.readLine().matches("^.+"+responseRegex)) {
							currentRecord.unsuccessRequests++;
							continue;
						}
					}
					if (line.endsWith("[op:1]")) {
						lineTokens = line.split(" ");
						if (lineTokens == null)
							continue;
						number = lineTokens[lineTokens.length - 2];
						
						/*if (number.startsWith("010")
								|| number.startsWith("2010")||number.startsWith("016")
								|| number.startsWith("2016"))
							currentRecord.vodafoneRequests++;
						else if (number.startsWith("012")
								|| number.startsWith("2012")||number.startsWith("018")
								|| number.startsWith("2018"))
							currentRecord.mobinilRequests++;
						else if(number.startsWith("011")||number.startsWith("2011"))
							currentRecord.etisalatRequets++;*/
						for(int x = 0;x<vodafoneKeysArr.length;x++)
						{
							if(number.startsWith(vodafoneKeysArr[x]))
							{
								currentRecord.vodafoneRequests++;
								break;
							}
						}
						for(int x = 0;x<mobinilKeysArr.length;x++)
						{
							if(number.startsWith(mobinilKeysArr[x]))
							{
								currentRecord.mobinilRequests++;
								break;
							}
						}
						for(int x = 0;x<etisalatKeysArr.length;x++)
						{
							if(number.startsWith(etisalatKeysArr[x]))
							{
								currentRecord.etisalatRequets++;
								break;
							}
						}
					}
				}
				inputStream.close();
				logger.debug("USSDConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}

			Set keys = records.keySet();
			dateFormater.applyPattern("MM/dd/yyyy HH:mm:ss");
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				Date key = (Date) iter.next();
				currentRecord = (USSDRecord) records.get(key);
				outputStream.write(dateFormater.format(key) + ",");
				outputStream.write(currentRecord.mobinilRequests + ",");
				outputStream.write(currentRecord.vodafoneRequests + ",");
				outputStream.write(currentRecord.etisalatRequets + ",");
				outputStream.write(currentRecord.successRequests + ",");
				outputStream.write(currentRecord.unsuccessRequests + ",");
				outputStream.write((currentRecord.successRequests + currentRecord.unsuccessRequests)+ "");
				outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0] = output;
			logger
					.debug("USSDConverter.convert() - finished converting input files successfully ");
			return outputFiles;

		} catch (FileNotFoundException e) {
			logger.error("USSDConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("USSDConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;

	}

	private class USSDRecord {
		public int vodafoneRequests = 0;

		public int mobinilRequests = 0;
		
		public int etisalatRequets = 0;
		
		public int successRequests = 0;

		public int unsuccessRequests = 0;
	}

	public static void main(String[] args) {
		/*try {
			
			String ss = "2009-09-22 14:25:51.976 [20102564486] -505- Response: Message sent successfully [op:32; dc:0; bc:25]";
			String ss2 = "2009-09-22 14:25:52.063 [20161736405] -505- Response: Please wait [op:32; dc:0; bc:11]";
			System.out.println(ss.matches("^.+Message sent successfully.+\\[op:32.+$"));
			System.out.println(ss2.matches("^.+Please wait.+\\[op:32.+$"));
			
			PropertyReader.init("D:\\VASPortalWF\\Source Code\\DataCollection");
			USSDConverter converter = new USSDConverter();
			File[] input = new File[1];
//			input[0] = new File("E:\\browser_505_2006061913.log");
//			input[1] = new File("E:\\browser_505_2006061914.log");
			input[0] = new File("D:\\browser_505_2009020409.log");

			File[] output = converter.convert(input, "SDP");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		String ss=" press 1 [op:2;ddd]";
		String regx="^.+\\[op:3?2[;\\]].*$";
		System.out.println(ss+" matches  = "+ss.matches(regx));

	}
}
