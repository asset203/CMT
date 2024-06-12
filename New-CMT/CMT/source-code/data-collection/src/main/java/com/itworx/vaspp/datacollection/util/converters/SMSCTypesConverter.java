/*
 * File: SMSCTypesConverter.java
 * Date        Author          Changes
 * 30/04/2006  Nayera Mohamed  Created
 * 16/05/2006  Nayera Mohamed  Updated to count SMSC local events only
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

public class SMSCTypesConverter extends AbstractTextConverter
{
  private Logger logger;
  private HashMap dates = new HashMap();
  private Calendar c = Calendar.getInstance();

  public SMSCTypesConverter()
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
				.debug("SMSCTypesConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("SMSCTypesConverter.convert() - converting file "
						+ inputFiles[j].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("SMSCTypesConverter.convert() - "
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
			logger.error("SMSCTypesConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		logger
				.debug("SMSCTypesConverter.convert() - finished converting input files successfully ");
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
			if (line.equals("")) {
				break;
			}
			try {
				if (line.startsWith("8,") ) {
          String tokens[] = line.split(",");
          String dateString = tokens[1].substring(0,8);
          Date date = frm.parse(dateString);
          Calendar c = Calendar.getInstance();
          c.setTime(date);
          c.set(Calendar.MINUTE, 00);
          c.set(Calendar.SECOND, 00);
          date = c.getTime();
          String type = tokens[21];
          String sourceNode = tokens[30];
          //The messages coming from FTD should not be counted
          if ( matchesLocal(sourceNode) ) {
            this.countTypes(type,date);
          }
				}
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error(
						"SMSCTypesConverter.countEvents() - invalid date in input file "
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
  
  private boolean  matchesLocal(String sourceNode)
  {
    try
    {
      if ( sourceNode == null )
      {
        return true;
      }
      int length = sourceNode.length();
      if ( length == 0)
      {
        return true;
      }
      else if ( sourceNode.charAt(length-12)=='2' && sourceNode.charAt(length-1)==0 && sourceNode.charAt(length-10)==1)
      {
        return true;
      }
    }
    catch (StringIndexOutOfBoundsException e)
    {
      return false;
    }
    return false;
  }
  private void countTypes(String type, Date day)
  {
    HashMap types;
    if ( dates.containsKey(day))
    {
      types = (HashMap) dates.get(day);
    }
    else
    {
      types = new HashMap();
      dates.put(day,types);
    }
    if (types.containsKey(type)) {
        long count = ((Long)types.get(type)).longValue();
        count++;
        types.put(type,new Long(count));
      } else {
        Long count = new Long(1);
        types.put(type,count);
      } 
  }
  
  private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
    }
	}

  /**
	 * loop types and write row for each
   * the structure of output is count,type
	 * @param outputStream -
	 *            input file
	 *
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
  private void writeEvents(BufferedWriter outputStream) throws IOException {
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
    Iterator daysIterator = dates.keySet().iterator();
    while (daysIterator.hasNext()) {
      Date day =(Date) daysIterator.next();
      c.setTime(day);
      HashMap types = (HashMap) dates.get(day);
      Iterator typesIterator = types.keySet().iterator();
      while (typesIterator.hasNext()) {
        String type = (String) typesIterator.next();
        long count = ((Long)types.get(type)).longValue();
        outputStream.write(frm.format(c.getTime())+ ","+count + ","	+ type+",");
        outputStream.newLine();
      }
    }
	}

  public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			SMSCTypesConverter s = new SMSCTypesConverter();
			File[] files = new File[1];
			files[0] = new File(
					"D:\\Meeting Files_2_3\\060207_TLG.DMP");
			s.convert(files, "SMSC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}