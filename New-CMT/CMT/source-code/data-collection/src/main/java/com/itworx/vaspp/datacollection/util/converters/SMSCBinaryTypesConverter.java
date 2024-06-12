/*
 * File: SMSCBinaryTypesConverter.java
 * Date        Author          Changes
 * 03/05/2006  Nayera Mohamed  Created
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SMSCBinaryTypesConverter extends AbstractTextConverter
{
  private Logger logger;
  private HashMap days = new HashMap();
  private Calendar c = Calendar.getInstance();

  private SimpleDateFormat frm = new SimpleDateFormat();

  public SMSCBinaryTypesConverter()
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
				.debug("SMSCBinaryTypesConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
      frm.applyPattern("yyyyMMddHHmmss");
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("SMSCBinaryTypesConverter.convert() - converting file "
						+ inputFiles[j].getName());
				DataInputStream inputStream = new DataInputStream(new FileInputStream(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("SMSCBinaryTypesConverter.convert() - "
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
			logger.error("SMSCBinaryTypesConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("SMSCBinaryTypesConverter.convert() - finished converting input files successfully ");
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
          int length=0;
          byte [] bytes =new byte[1];         
          boolean done= false;
          do{
            int b = inputStream.readUnsignedByte();
            if( b==0x64 )  {
              //read trusted_mofsm_fields
              this.readMobileOriginating(inputStream);
            }
            if( b==0x60 || b==0x74)  {
               // System.out.println("*************************Terminating***************************" + b);
                this.skipMobileTerminating(inputStream);
              }
            if( b==0x62 )  {
            //  System.out.println("*************************status******************************" + b);
              skipStatusReport(inputStream);
            }
          }while(inputStream.available()>0); 
	}
  
  private void readMobileOriginating(DataInputStream inputStream ) throws IOException
  {   boolean done = false;
    int length=0;
    byte [] bytes =new byte[1];
    int b2 = inputStream.readUnsignedByte();
      if ( b2 == 0x81 ) {
        //System.out.println("Origin************81");
        int b3 = inputStream.readUnsignedByte();
        length=b3;
        bytes = new byte[length];
      }
      else if ( b2 == 0x82) {
        //System.out.println("Origin************82");
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
        //logger.error("error in input file b2 = "+Integer.toHexString(b2) +" at point "+Integer.toHexString(no_of_bytes - inputStream.available()));
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
      //System.out.println("length "+intBytes.length);
      String timeStamp = new String(bytes,start,dataLength);
      Date date;
      try
      {
        date = frm.parse(timeStamp);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        date = c.getTime();
      }
      catch (ParseException e)
      {
        logger.error("SMSCBinaryTypesConverter.readMobileOriginating() - invalid timestamp "+timeStamp);
        return;
      }
      //System.out.println(timeStamp);
      offset += dataLength+2;
      try{
      //start = this.skip(0xa3,offset,intBytes); 
      //start += 2;
      //start = this.skip(0x80,start,intBytes);
      //start+=2;
      start = this.skip(0xaf,offset,intBytes);
      int smsChoiceLength = intBytes[start+1];
      start++;
      if(intBytes[start]==0x81){
        start+=2;
      }
      else{
        start++;
      }
      if(intBytes[start]==0x80)  {
        start+=3;
      }
      start++;
      start = this.skip(0x84,start,intBytes);
      }
      catch(Exception e)
      {
        logger.error("SMSCBinaryTypesConverter.readMobileOriginating() - invalid record");
        return;
      }
      start++;
      if(intBytes[start]==0x01){
        start++;
      }
      else {
        start+=2;
      }
      int SMSDataCodingSchema = intBytes[start];
      String type = ""+SMSDataCodingSchema;
      this.countTypes(type,date);
  }
  
  private void countTypes(String type, Date day)
  {
    HashMap types;
    if ( days.containsKey(day))
    {
      types = (HashMap) days.get(day);
    }
    else
    {
      types = new HashMap();
      days.put(day,types);
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
  
  private void skipStatusReport(DataInputStream inputStream ) throws IOException
  {   boolean done = false;
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
        //logger.error("error in input file b2 = "+Integer.toHexString(b2) +" at point "+Integer.toHexString(no_of_bytes - inputStream.available()));
      }
      bytes = new byte[length];
      int read = inputStream.read(bytes,0,length);
      if ( read != length){
        logger.debug("wrong number of bytes!!!!");
        return;
      }
  }
  
  private void skipMobileTerminating(DataInputStream inputStream ) throws IOException
  {       boolean done = false;
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
  
  private int skip(int code, int offset , int[] input) throws Exception
   {
    int skipped=0;
    int start = offset;
    while ( start <input.length) {
      if (input[start]==code) {
        return start;
      }
      int length = input[start+1];
      start += length+2;
      skipped++;
    }
    return -1;
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
    Iterator daysIterator = days.keySet().iterator();
    while (daysIterator.hasNext()) {
      Date day =(Date) daysIterator.next();
      c.setTime(day);
      HashMap types = (HashMap) days.get(day);
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
					.init("D:/sayed/WorkSpace/VAS Performance Portal/DataCollection/");
			SMSCBinaryTypesConverter s = new SMSCBinaryTypesConverter();
    //File folder = new File("D:/Meeting Files_2_3/15_5/R1/test");
    File[] files = new File[1];
    files[0] = new File("d:/temp/log_mo_mt_mbegvf-rtr01_20070522_095223_788.dat");
		s.convert(files, "SMSC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}