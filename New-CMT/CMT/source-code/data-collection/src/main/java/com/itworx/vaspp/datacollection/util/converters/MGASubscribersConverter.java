/*
 * File:       MGASubscribersConverter.java
 * Date        Author          Changes
 * 12/04/2006  Nayera Mohamed  Created
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
public class MGASubscribersConverter extends AbstractTextConverter {
	static private Logger logger;

	private HashMap subscriberEvents = new HashMap();

	private int unsuccessfulOperation = 0;

	private Date day;

	public MGASubscribersConverter() {
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
				.debug("MGASubscribersConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				logger
						.debug("MGASubscribersConverter.convert() - converting file "
								+ inputFiles[j].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("MGASubscribersConverter.convert() - "
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
			logger
					.error("MGASubscribersConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("MGASubscribersConverter.convert() - finished converting input files successfully ");
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
				if (trafficEvent.equals("210")) {
					this.unsuccessfulOperation++;
					continue;
				}
				String subscriberID = tokens[2];
				if (this.subscriberEvents.containsKey(subscriberID)) {
					int[] events = (int[]) subscriberEvents.get(subscriberID);
					int index = Integer.parseInt(trafficEvent.substring(1,
							trafficEvent.length()));
					events[index]++;
				} else {
					int[] events = new int[10];
					int index = Integer.parseInt(trafficEvent.substring(1,
							trafficEvent.length()));
					events[index]++;
					this.subscriberEvents.put(subscriberID, events);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error(
						"ArrayIndexOutOfBoundsException.countEvents() - invalid record "
								+ e);
			}
      catch (NullPointerException e) {
				logger.error(
						"ArrayIndexOutOfBoundsException.countEvents() - invalid record "
								+ e);
			}
		}
	}

	/**
	 * loop over subscribers events and write row for each into output file the
	 * structure of output is subscriberID,traffic event,count
	 * 
	 * @param outputStream -
	 *            input file
	 * 
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
	private void writeEvents(BufferedWriter outputStream) throws IOException {
		Iterator subscribersIterator = this.subscriberEvents.keySet()
				.iterator();
		while (subscribersIterator.hasNext()) {
			String subscriberID = (String) subscribersIterator.next();
			int[] events = (int[]) this.subscriberEvents.get(subscriberID);
			for (int i = 0; i < events.length; i++) {
				if (events[i] != 0) {
					outputStream.write(subscriberID + ",20" + i + ","
							+ events[i]);
					// outputStream.write(subscriberID+","+i+","+events[i]);
					outputStream.newLine();
				}
			}
		}
    if ( this.unsuccessfulOperation!= 0 ){
    outputStream.write("all,210," + this.unsuccessfulOperation);
    outputStream.newLine();
    }
	}

	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			MGASubscribersConverter s = new MGASubscribersConverter();
			File[] files = new File[1];
			files[0] = new File(
					"D:/Meeting Files_2_3/MGA_06052800010200.TLG");
			s.convert(files, "MGA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}