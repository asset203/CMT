/*
 * File: SMSCBinarySecondsConverter.java
 * Date        Author          Changes
 * 21/05/2006  Nayera Mohamed  Created
 *
 * Creating comma separated input file for SMSC input
 */
package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SMSCBinarySecondsConverter extends AbstractTextConverter
{
  private Logger logger;
  private Calendar c = Calendar.getInstance();
	private Date day;
  private EventsComparator comparator = new EventsComparator();
  private Vector counts = new Vector();
  private SimpleDateFormat frm = new SimpleDateFormat();

  
  public SMSCBinarySecondsConverter()
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
				.debug("SMSCBinarySecondsConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
//				logger.debug("SMSCBinaryConverter.convert() - converting file "
//						+ inputFiles[j].getName());
				DataInputStream inputStream = new DataInputStream(new FileInputStream(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
//				logger.debug("SMSCBinaryConverter.convert() - "
//						+ inputFiles[j].getName() + " converted");
			}
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			// write the total count of events from all files into one file
			this.writeEvents(outputStream);
			outputStream.close();
			outputFiles[0] = output;
		} catch (FileNotFoundException e) {
			logger.error("SMSCBinarySecondsConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("SMSCBinarySecondsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

  /**
	 * count traffic events into
	 * @param inputStream -
	 *            input file
	 * @exception IOException
	 *                if input file couldn't be opened
	 * @exception InputExceptio
   *
	 *                if ParseException occured
	 */
	private void countEvents(DataInputStream inputStream) throws IOException,
			InputException {
          frm.applyPattern("yyyyMMddHHmmss");
          Date date;
          int count = 0;
          Date lastDate=null;
          do{
            int b = inputStream.readUnsignedByte();
            if(b==0x74)  {
                this.skipMobileTerminating(inputStream);
                continue;
             }
            if( b==0x60 || b==0x64  || b==0x62)  { 
              int length=0;
              byte [] bytes =new byte[1];
              int b2 = inputStream.readUnsignedByte();
                if ( b2 == 0x81 ) {
                  int b3 = inputStream.readUnsignedByte();
                  length=b3;
                  bytes = new byte[length];
                }
                else if ( b2 == 0x82) {
                  int[] checks = new int[2];
                  checks[0] = inputStream.readUnsignedByte();
                  checks[1] = inputStream.readUnsignedByte();
                  if ( checks[0]>3)
                  {
                    logger.debug("error in input file!!!");
                    return;
                  }
                  length = checks[0]*256+checks[1];
                }
                else {
                  length = b2;
                }
                bytes = new byte[length];
                int read = inputStream.read(bytes,0,length);
                if ( read != length){
                  logger.debug("wrong number of bytes!!!!");
                  return;
                }
                
                int[] intBytes = this.getUnsigned(bytes);
                int offset=0;
                int start = offset+2;
                int dataLength = intBytes[offset+1];
                String timeStamp = new String(bytes,start,dataLength);
                try {
                  date = frm.parse(timeStamp);
                  c.setTime(date);
                  date = c.getTime();
                }
                catch(Exception e)
                {
                  logger.error("SMSCBinarySecondsConverter.readMobileOriginating() - invalid timestamp "+timeStamp);
                  return;
                }
                if ( !date.equals(lastDate) && lastDate != null) {
                  this.updateSeconds(lastDate,count);
                  count = 0;
                }
                count++;
                lastDate = date;
            }
          }while(inputStream.available()>0);
          this.updateSeconds(lastDate,count);
	}
	
	
	private void skipMobileTerminating(DataInputStream inputStream ) throws IOException
	  {
	    int length=0;
	    byte [] bytes =new byte[1];
	    int b2 = inputStream.readUnsignedByte();
	    //System.out.println("terminating code " + Integer.toHexString(b2) );
	      if ( b2 == 0x81 ) {
	        int b3 = inputStream.readUnsignedByte();
	        length=b3;
	        bytes = new byte[length];
	       
	      }
	      else if ( b2 == 0x82) {
	        int[] checks = new int[2];
	        checks[0] = inputStream.readUnsignedByte();
	        checks[1] = inputStream.readUnsignedByte();
	        if ( checks[0]>3) {
	          logger.debug("error in input file!!!");
	          return;
	        }
	        length = checks[0]*256+checks[1];
	        bytes = new byte[length];
	      }
	      else {
	        length = b2;
	      }
	      int read = inputStream.read(bytes,0,length);
	      if ( read != length){
	        logger.debug("wrong number of bytes!!!!");
	        return;
	      }
	  }
	
	
  private int[] getUnsigned(byte[] input)
  {
    int[] output = new int[input.length];
    for ( int i=0 ; i < input.length ; i++)
    {
      output[i] = (int) input[i] & 0xFF;
    }
    return output;
  }
  
  private void updateSeconds(Date date, int count)
	{
    SecondEvent current = new SecondEvent(count,date);
    if ( counts.size() < 20 )
    {
      counts.add(current);
    }
    else
    {
      SecondEvent minCount = (SecondEvent) Collections.min(counts,comparator);
      if (current.getCount() > minCount.getCount())
      {
        counts.remove(minCount);
        counts.add(current);
      }
      else if (  minCount.equals(current))
      {
        counts.add(current);
      }
    }
	}
  
  /**
	 * 
	 * @param outputStream -
	 *            input file
	 *
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
  private void writeEvents(BufferedWriter outputStream) throws IOException {
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
    Collections.sort(counts,comparator);
    int size = counts.size();
    for ( int i = size-1,j=0; j<20; j++,i-- ) 
    {
      SecondEvent event = (SecondEvent) counts.elementAt(i);
      outputStream.write(frm.format(event.getDate())+ ","+event.getCount() + ",");
      outputStream.newLine();
    }
 	}
  
  
  public static void main(String ag[]) {
	  try {
			PropertyReader
					.init("D:/sayed/WorkSpace/VAS Performance Portal/DataCollection/");
			SMSCBinarySecondsConverter s = new SMSCBinarySecondsConverter();
      //File folder = new File("D:/Meeting Files_2_3/15_5/R1/test");
      File[] files = new File[1];
      files[0] = new File("d:/temp/log_mo_mt_mbegvf-rtr01_20070522_095223_788.dat");
		s.convert(files, "SMSC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}