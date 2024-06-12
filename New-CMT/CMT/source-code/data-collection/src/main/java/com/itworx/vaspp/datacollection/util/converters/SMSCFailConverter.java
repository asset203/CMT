/*
 * File: SMSCFailConverter.java
 * Date        Author          Changes
 * 30/04/2006  Nayera Mohamed  Created
 *
 * Creating comma separated input file for SMSC input
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

public class SMSCFailConverter extends AbstractTextConverter
{
  private Logger logger;
  private HashMap dates = new HashMap();

  public SMSCFailConverter()
  {
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
				.debug("SMSCFailConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("SMSCFailConverter.convert() - converting file "
						+ inputFiles[j].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("SMSCFailConverter.convert() - "
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
			e.printStackTrace();
			logger.error("SMSCFailConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		logger
				.debug("SMSCFailConverter.convert() - finished converting input files successfully ");
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
	 * @exception InputExceptio
   *
	 *                if ParseException occured
	 */
	private void countEvents(BufferedReader inputStream) throws IOException,
			InputException {

    this.skip(6,inputStream);
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("yyMMddHH");
		while (inputStream.ready()) {
      
			String line = inputStream.readLine();
      //System.out.println("line "+line);
			if (line.equals("")) {
				break;
			}
			try {
// removed upon client request				
//				if (line.startsWith("6") ) {
//          //System.out.println("fail");
//          String tokens[] = line.split(",");
//          String trafficEvent = tokens[0];
//          String dateString = tokens[1].substring(0,8);
//          Date date = frm.parse(dateString);
//          String reason = tokens[10];
//          if (this.dates.containsKey(date)) {
//						HashMap reasons = (HashMap) dates.get(date);
//						this.updateReasonsMap(reasons,reason);
//					} else {
//						HashMap reasons = new HashMap();
//            reasons.put(reason,new Integer(1));
//						dates.put(date,reasons);
//					}
//				}
        if (line.startsWith("7,")) {
          //System.out.println("fail");
          String tokens[] = line.split(",");
          String trafficEvent = tokens[0];
          String dateString = tokens[1].substring(0,8);
          Date date = frm.parse(dateString);
					String reason = tokens[14];
          if (this.dates.containsKey(date)) {
						HashMap reasons = (HashMap) dates.get(date);
						this.updateReasonsMap(reasons,reason);
					} else {
						HashMap reasons = new HashMap();
            reasons.put(reason,new Integer(1));
						dates.put(date,reasons);
					}
				}
        if (line.startsWith("2,") ) {
        //System.out.println("expire");
          String tokens[] = line.split(",");
          String trafficEvent = tokens[0];
          String dateString = tokens[1].substring(0,8);
          Date date = frm.parse(dateString);
          String reason = "expired";
          if (this.dates.containsKey(date)) {
						HashMap reasons = (HashMap) dates.get(date);
						this.updateReasonsMap(reasons,reason);
					} else {
						HashMap reasons = new HashMap();
            reasons.put(reason,new Integer(1));
						dates.put(date,reasons);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error(
						"SMSCFailConverter.countEvents() - invalid date in input file "
								+ e);
			}
      catch (ArrayIndexOutOfBoundsException e) {
    	  e.printStackTrace();
						logger.error("invalid record in input file "
								+ e);
			}
      catch (NullPointerException e) {
    	  e.printStackTrace();
						logger.error("invalid record in input file "
								+ e);
       }
		}
	}

  /**
	 * update the reasons map with the new count
	 *
	 * @param map -
	 *            the reasons HashMap
	 * @param origin -
	 *            the failure reason
	 *
	 */
	private void updateReasonsMap(HashMap map, String reason) {
		if (map.containsKey(reason)) {
			int count = ((Integer)map.get(reason)).intValue();
			count++;
      map.put(reason,new Integer(count));
		} else {
			Integer count = new Integer(1);
			map.put(reason, count);
		}
	}

  private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
    }
	}

  /**
	 * loop over dates and and failure reasons for each and write row for each
	 * date-failure reason
   * the structure of output is date,count,failure reason
	 *
	 * @param outputStream -
	 *            input file
	 *
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
  private void writeEvents(BufferedWriter outputStream) throws IOException {
		Iterator datesIterator = this.dates.keySet().iterator();
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
    Calendar c = Calendar.getInstance();
		while (datesIterator.hasNext()) {
			Date date = (Date) datesIterator.next();
      c.setTime(date);
      c.set(Calendar.MINUTE, 00);
      c.set(Calendar.SECOND, 00);  
			HashMap reasons = (HashMap) this.dates.get(date);
			Iterator reasonsIterator = reasons.keySet().iterator();
			while (reasonsIterator.hasNext()) {
				String reason = (String) reasonsIterator.next();
				int count = ((Integer)reasons.get(reason)).intValue();
				outputStream.write(frm.format(date) + "," + count + ","	+ reason);
				outputStream.newLine();
			}
		}
	}

  public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			SMSCFailConverter s = new SMSCFailConverter();
			File[] files = new File[1];
			files[0] = new File(
					"D:/Meeting Files_2_3/15_5/SMSC/060513_TLG.DMP;1");
			s.convert(files, "SMSC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}