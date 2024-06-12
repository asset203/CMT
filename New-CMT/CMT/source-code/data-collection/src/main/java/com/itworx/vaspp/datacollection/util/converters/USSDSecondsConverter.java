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
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDSecondsConverter extends AbstractTextConverter{
	private Logger logger;

	private EventsComparator comparator = new EventsComparator();

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
		Date dateTime=null;
		Date oldDateTime=null;
		String dateTimeString="";
		Vector records = new Vector();
		SimpleDateFormat dateFormater = new SimpleDateFormat();
		int count = 0;
		logger = Logger.getLogger(systemName);
		logger
				.debug("USSDConverter.convert() - started converting input files ");
		try {
			String path =PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			BufferedReader inputStream;
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("USSDConverter.convert() - converting file "
						+ inputFiles[i].getName());
				//System.out.println("USSDConverter.convert() - converting file "	+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				while (inputStream.ready()) {
					line = inputStream.readLine();
					lineTokens = line.split(" ");
					if (lineTokens!=null&&lineTokens.length > 2&&(line.contains("Request:")&&line.contains("[op:1]"))) {
						try {
						dateTimeString = lineTokens[0] + " "
								+ lineTokens[1].substring(0, 8);
						
							dateFormater.applyPattern("yyyy-MM-dd HH:mm:ss");
							dateTime = dateFormater.parse(dateTimeString);
							if(oldDateTime!=null&&!oldDateTime.equals(dateTime)) {
								updateSeconds(records,oldDateTime,count);
								count=0;
							}
						} catch (Exception x) {
							logger
							.error(
									"com.itworx.vaspp.datacollection.util.converters.USSDConverter.convert()",
									x);
					x.printStackTrace();
						}
						oldDateTime=dateTime;
						count++;
					}
					
				}
				updateSeconds(records,oldDateTime,count);
				inputStream.close();
				logger.debug("USSD Seconds Converter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
		
			dateFormater.applyPattern("MM/dd/yyyy HH:mm:ss");
			for (int i = 0; i < records.size(); i++) {
				SecondEvent record=(SecondEvent)records.get(i);
				
				outputStream.write(dateFormater.format(record.getDate())+","+record.getCount());
				outputStream.newLine();
				
				
			}
			outputStream.close();
			outputFiles[0] = output;
			logger
					.debug("USSDConverter.convert() - finished converting input files successfully ");
			return outputFiles;

		} catch (FileNotFoundException e) {
			logger.error("USSDConverter.convert() - Input file not found " + e);
			e.printStackTrace();
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("USSDConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("USSDConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;

	}

	private void updateSeconds(Vector records, Date date, int count) {
		SecondEvent current = new SecondEvent(count, date);
		if (records.size() < 1000) {
			records.add(current);
		} else {
			SecondEvent minCount = (SecondEvent) Collections.min(records,
					comparator);
			if (current.getCount() > minCount.getCount()) {
				records.remove(minCount);
				records.add(current);
			} else if (minCount.equals(current)) {
				records.add(current);
			}
		}
	}
	
	
	public static void main(String[] args) {
		USSDSecondsConverter converter = new USSDSecondsConverter();
		File[] input = new File[1]; 
		input[0] = new File("D:\\USSD_1167293582040_1_browser_505_2006122700.log");
	//	input[1] = new File("E:\\browser_505_2006061914.log");
		try {
			File[] output = converter.convert(input, "SDB");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
