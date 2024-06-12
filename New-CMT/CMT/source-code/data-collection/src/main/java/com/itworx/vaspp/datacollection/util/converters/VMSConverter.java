/*
 * File:       VMSConverter.java
 * Date        Author          Changes
 * 05/03/2006  Nayera Mohamed  Created
 * 12/04/2006  Nayera Mohamed  Updated to read data of multiple days
 *
 * Converter class for converting VMS text input file into comma spearated format
 */
package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Iterator;
import org.apache.log4j.Logger;

/*
 * The structure of the text input file is
 *
 * lines of comma separated data
 */
public class VMSConverter extends AbstractTextConverter {
	static private Logger logger;

	//private int incoming[] = new int[24];
	//private int leftMessage[] = new int[24];
	//Map holding identifiers as keys and date as coressoponding login date_time
	private HashMap identifiers = new HashMap();

	private HashMap daysIncoming = new HashMap();

	private HashMap daysLeftMessages = new HashMap();

	public VMSConverter() {
	}

	/**
	 * loop over input files, loop over lines in each file
	 * count incoming, check message events for each hour
	 * write output into one output file
	 * output file is placed on the configured converted file path
	 *
	 * @param inputFiles - array of the input files to be converted
	 * @param systemName - name of targeted system for logging
	 *
	 * @exception ApplicationException
	 *       if input file couldn't be found
	 *       if input file couldn't be opened
	 * @exception InputException
	 *       if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("VMSConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("VMSConverter.convert() - converting file "
						+ inputFiles[j].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("VMSConverter.convert() - "
						+ inputFiles[j].getName() + " converted");
			}
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			//write the total count of events from all files into one file
			this.writeEvents(outputStream);
			outputStream.close();
			outputFiles[0] = output;
		} catch (FileNotFoundException e) {
			logger.error("VMSConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("VMSConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	/**
	 * count 001 and 012 events into arrays incoming, leftMessage for each hour
	 *
	 * @param inputStream - input file
	 *
	 * @exception IOException
	 *       if input file couldn't be opened
	 * @exception InputException
	 *       if ParseException occured
	 */
	private void countEvents(BufferedReader inputStream) throws IOException,
			InputException {
		//inputStream.readLine();
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("yyMMddHHmmss");
		while (inputStream.ready()) {
			String line = inputStream.readLine();
			if (line.equals("")){
				break;
      }
			try {
				if (line.startsWith("001")) { // login event, icoming count is incremented
					String tokens[] = line.split(" ");
					String dateString = tokens[2];
					Date date = frm.parse(dateString);
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					//get the integer for hour of this event
					int hour = c.get(Calendar.HOUR_OF_DAY);
					String dayString = this.getDayString(date);
					int[] incoming;
					Object incomingObject = this.daysIncoming.get(dayString);
					if (incomingObject != null) {
						incoming = (int[]) incomingObject;
					} else {
						incoming = new int[24];
						this.daysIncoming.put(dayString, incoming);
					}
					incoming[hour]++;
					//increase the incoming count for this hour
					String identifier = tokens[6];
					//add the identifier of this user to identify related 012 events
					identifiers.put(identifier, date);
				}

				if (line.startsWith("012")) { //check message event, leftMessage is incremented
					String tokens[] = line.split(" ");
					String identifier = tokens[6];
					Object dateObject = identifiers.get(identifier);
					/*check identifier of this event against identifiers of login events
					 * if found, leftMessage count of this hour is incremented
					 * the identifier is then removed from identifiers
					 * as repeated 012 events are ignored */
					if (dateObject != null) {
						Date date = (Date) dateObject;
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						//get the integer for hour of this event
						int hour = c.get(Calendar.HOUR_OF_DAY);

						String dayString = this.getDayString(date);
						int[] leftMessage;
						Object leftMessagesObject = this.daysLeftMessages
								.get(dayString);

						if (leftMessagesObject != null) {
							leftMessage = (int[]) leftMessagesObject;
						} else {
							leftMessage = new int[24];
							this.daysLeftMessages.put(dayString, leftMessage);
						}
						leftMessage[hour]++;
						identifiers.remove(identifier);
					}
				}
			} catch (ParseException e) {
				throw new InputException("invalid date in input file " + e);
			}
      catch (NullPointerException e) {
				throw new InputException("invalid record in input file " + e);
			}
      catch (ArrayIndexOutOfBoundsException e) {
				throw new InputException("invalid record in input file " + e);
			}
		}
	}

	/**
	 * loop over hours and write row for each into output file
	 * the structure of output is
	 * date,incoming count,left messages count, no messages count
	 *
	 * @param outputStream - input file
	 *
	 * @exception IOException
	 *       if output file couldn't be opened
	 */
	private void writeEvents(BufferedWriter outputStream) throws IOException {
		Iterator daysIterator = this.daysLeftMessages.keySet().iterator();
		while (daysIterator.hasNext()) {
			String dayString = (String) daysIterator.next();
			SimpleDateFormat frm = new SimpleDateFormat();
			frm.applyPattern("MM/dd/yyyy");
			try {
				Date day = frm.parse(dayString);
				int[] leftMessage = (int[]) this.daysLeftMessages
						.get(dayString);
				int[] incoming = (int[]) this.daysIncoming.get(dayString);
				for (int i = 0; i < incoming.length; i++) {
					Calendar c = Calendar.getInstance();
					c.setTime(day);
					c.set(Calendar.HOUR_OF_DAY, i);
					c.set(Calendar.MINUTE, 00);
					c.set(Calendar.SECOND, 00);
					Date vdate = c.getTime();
					frm.applyPattern("MM/dd/yyyy HH:mm");
					int no_msg = incoming[i] - leftMessage[i];
					if (!(incoming[i] == 0 && leftMessage[i] == 0)) {
						outputStream.write(frm.format(vdate) + ","
								+ incoming[i] + "," + leftMessage[i] + ","
								+ no_msg);
						outputStream.newLine();
					}
				}
			} catch (ParseException e) {
				logger.error("error parsing daystring" + e);
			}
		}
	}

	private String getDayString(Date date) {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy");
		String dateString = frm.format(date);
		return dateString;
	}

	public static void main(String ag[]) {
		try {
			//PropertyReader.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			VMSConverter s = new VMSConverter();
			//s.convert(new File("c:\\VMS_T1053289.030"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}