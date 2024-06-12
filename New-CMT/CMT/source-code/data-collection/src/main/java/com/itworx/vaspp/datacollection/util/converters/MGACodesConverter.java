/*
 * File:       MGACodesConverter.java
 * Date        Author          Changes
 * 18/04/2006  Nayera Mohamed  Created
 *
 * Converter class for converting MGA text input file into comma spearated format
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

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import java.util.Iterator;
import org.apache.log4j.Logger;

/*
 * The structure of the text input file is
 * 
 * lines of comma separated data
 */
public class MGACodesConverter extends AbstractTextConverter {
	static private Logger logger;

	private HashMap subscribers = new HashMap();

	private Date day;

	public MGACodesConverter() {
	}

	/**
	 * loop over input files, loop over lines in each file count incoming, check
	 * message events for each hour write output into one output file output
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
		logger = Logger.getLogger(systemName);
		logger
				.debug("MGACodessConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("MGACodessConverter.convert() - converting file "
						+ inputFiles[j].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("MGACodessConverter.convert() - "
						+ inputFiles[j].getName() + " converted");
			}
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			// write the total count of events from all files into one file
			this.writeEvents(outputStream);
			outputStream.close();
			outputFiles[0] = output;
		} catch (FileNotFoundException e) {
			logger.error("MGACodessConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("MGACodessConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	/**
	 * count traffic events into
	 * 
	 * @param inputStream -
	 *            input file
	 * 
	 * @exception IOException
	 *                if input file couldn't be opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	private void countEvents(BufferedReader inputStream) throws IOException,
			InputException {
		//inputStream.readLine();
		SimpleDateFormat frm = new SimpleDateFormat();
		// frm.applyPattern("yyMMddHHmmssSS");
		while (inputStream.ready()) {
			String line = inputStream.readLine();
			if (line.equals("")) {
				break;
			}
			try {
				String tokens[] = line.split(",");
				String trafficEvent = tokens[0];
				// String dateString = tokens[1];
				// Date date = frm.parse(dateString);
				if (trafficEvent.equals("203") || trafficEvent.equals("207")) {
					String subscriberID = tokens[2];
					String origin = this.extractNumber(tokens[6]);
					String destination = this.extractNumber(tokens[7]);
					if (this.subscribers.containsKey(subscriberID)) {
						HashMap shortCodes = (HashMap) subscribers
								.get(subscriberID);
						this.updateShortCodesMap(shortCodes, origin,
								destination);
					} else {
						HashMap shortCodes = new HashMap();
						subscribers.put(subscriberID, shortCodes);
						this.updateShortCodesMap(shortCodes, origin,
								destination);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error(
						"MGACodessConverter.countEvents() - invalid record "
								+ e);
			}
      catch (NullPointerException e) {
				logger.error(
						"MGACodessConverter.countEvents() - invalid record "
								+ e);
			}
		}
	}

	/**
	 * update the short codes map with the new originating and destination count
	 * 
	 * @param map -
	 *            the Short Codes HashMap
	 * @param origin -
	 *            the short code of the origin of the event
	 * @param destination -
	 *            the short code of the destination of the event
	 */
	private void updateShortCodesMap(HashMap map, String origin,
			String destination) {
		// update number of originating for this short code, which is the index
		// 0
		if (map.containsKey(origin)) {
			int[] events = (int[]) map.get(origin);
			events[0]++;
		} else {
			int[] events = new int[2];
			events[0]++;
			map.put(origin, events);
		}
		// update number of destination for this short code, which is the index
		// 1
		if (map.containsKey(destination)) {
			int[] events = (int[]) map.get(destination);
			events[1]++;
		} else {
			int[] events = new int[2];
			events[1]++;
			map.put(destination, events);
		}
	}

	/**
	 * extract the number from the address string
	 * 
	 * @param address -
	 *            the address string
	 * 
	 * @return String - the extracted number
	 */
	private String extractNumber(String address) {
		String number = null;
		int Dindex = address.indexOf("D");
		if (Dindex != -1) {
			number = address.substring(Dindex, address.length());
		}
		int Hindex = address.indexOf("H");
		if (Hindex != -1) {
			number = address.substring(Hindex, address.length());
		}
		return number;
	}

	/**
	 * loop over subscribers and short codes for each and write row for each
	 * subscriber-short code the structure of output is subscriberID,short
	 * code,destination count, originating count
	 * 
	 * @param outputStream -
	 *            input file
	 * 
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
	private void writeEvents(BufferedWriter outputStream) throws IOException {
		Iterator subscribersIterator = this.subscribers.keySet().iterator();
		while (subscribersIterator.hasNext()) {
			String subscriberID = (String) subscribersIterator.next();
			HashMap shortCodes = (HashMap) this.subscribers.get(subscriberID);
			Iterator shortCodesIterator = shortCodes.keySet().iterator();
			while (shortCodesIterator.hasNext()) {
				String shortCode = (String) shortCodesIterator.next();
				int[] events = (int[]) shortCodes.get(shortCode);
				outputStream.write(subscriberID + "," + shortCode + ","
						+ events[1] + "," + events[0]);
				outputStream.newLine();
			}
		}
	}

	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			MGACodesConverter s = new MGACodesConverter();
			File[] files = new File[1];
			files[0] = new File(
					"D:/Meeting Files_2_3/MGA_06052800010200.TLG");
			s.convert(files, "MGA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}