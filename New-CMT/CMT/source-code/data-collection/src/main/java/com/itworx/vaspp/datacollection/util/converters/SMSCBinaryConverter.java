/*
 * File: SMSCBinaryConverter.java
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
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SMSCBinaryConverter extends AbstractTextConverter
{
  private Logger logger;
  private HashMap destinationsDays = new HashMap();
  private HashMap MODays = new HashMap();
  private HashMap deliveryDays = new HashMap();
  private Calendar c = Calendar.getInstance();
  private SimpleDateFormat frm = new SimpleDateFormat();
  
  private int timeOffset,timeDiff;
  long profilestart,profilestart2;

  
  public SMSCBinaryConverter()
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
				.debug("SMSCBinaryConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
      frm.applyPattern("yyyyMMddHHmmss");
			for (int j = 0; j < inputFiles.length; j++) {
				logger.debug("SMSCBinaryConverter.convert() - converting file "
						+ inputFiles[j].getName());
				DataInputStream inputStream = new DataInputStream(new FileInputStream(
						inputFiles[j]));
				// count the events in this file into the incoming,leftMessage
				// arrays
				this.countEvents(inputStream);
				inputStream.close();
				logger.debug("SMSCBinaryConverter.convert() - "
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
			logger.error("SMSCBinaryConverter.convert() - Input file not found "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		logger
				.debug("SMSCBinaryConverter.convert() - finished converting input files successfully ");
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
          do{
            int b = inputStream.readUnsignedByte();
            if( b==0x64 )  {
              //read trusted_mofsm_fields
              this.readMobileOriginating(inputStream);
            }
            if( b==0x60 || b==0x74)  {
              //System.out.println("*************************Terminating***************************" + b);
              this.skipMobileTerminating(inputStream);
            }
            if( b==0x62 )  {
             // System.out.println("*************************status******************************" + b);
              skipStatusReport(inputStream);
            }
          }while(inputStream.available()>0);
	}

  private void readMobileOriginating(DataInputStream inputStream ) throws IOException
  { 
    int length=0;
    byte [] bytes =new byte[1];
    //System.out.println("********************starting reading bytes***********************"+(profilestart = System.currentTimeMillis()));
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
      //System.out.println("********************finished reading bytes***********************"+(System.currentTimeMillis()-profilestart));
      if ( read != length){
        logger.debug("wrong number of bytes!!!!");
        return;
      }
      //System.out.println("*******************starting converting to unsigned*******************"+(profilestart = System.currentTimeMillis()));
      int[] intBytes = this.getUnsigned(bytes);
      //System.out.println("*******************finished converting to unsigned*******************"+(System.currentTimeMillis()-profilestart));
      int offset=0;
      int start = offset+2;
      int dataLength = intBytes[offset+1];
      //System.out.println("length "+intBytes.length);
      String timeStamp = new String(bytes,start,dataLength);
      Date date;
      Date day;
      int hour;
      //System.out.println("*******************started processing*******************"+ (profilestart = System.currentTimeMillis()));
      try
      { 
        //System.out.println("*******************started converting date*******************"+ (profilestart2 = System.currentTimeMillis()));
        date = frm.parse(timeStamp);
        //System.out.println("time" + timeStamp);
        c.setTime(date);
        //c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY)-1);
        hour = c.get(Calendar.HOUR_OF_DAY);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        c.set(Calendar.HOUR_OF_DAY, 00);
        day = c.getTime();
        //System.out.println("*******************finished converting date*******************"+ ( System.currentTimeMillis()-profilestart2));
        //System.out.println("*******************started count originating*******************"+ (profilestart2 = System.currentTimeMillis()));
        this.countOriginating(day,hour);
        //System.out.println("*******************finished count originating*******************"+ ( System.currentTimeMillis()-profilestart2));

      }
      catch (ParseException e)
      {
        logger.error("SMSCBinaryFailConverter.readMobileOriginating() - invalid timestamp "+timeStamp);
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
      }
      catch(Exception e)
      {
        logger.error("SMSCBinaryFailConverter.readMobileOriginating() - invalid record");
        return;
      }
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
//        int k=intBytes[start]&0x80;
//        if(k==0x80){
//          System.out.print ("replyPath=1");
//        }
//        else{
//          System.out.print ("replyPath=0");
//        }
        int k=intBytes[start]&0x20;
        if(k==0x20){
          countDelivery(day,hour);
        }
      }
      start++;
      if(intBytes[start]==0x81)
				{
					start++;
					if(intBytes[start]==0x01){
						start+=2;
          }
					else {
						start+=3;
          }
				}			
				if(intBytes[start]==0x82)
				{
					start++;
					String smsRecipient=""; String origin = "";
          int recipientLength = intBytes[start];
          start++;
					//System.out.print("first "+Integer.toHexString(intBytes[start])+" ");
          for(int s=1;s<recipientLength;s++)
					{
						int streamByte = intBytes[start+s];
            origin += streamByte;
						smsRecipient+= streamByte & 0x0F;
						smsRecipient+= ((streamByte)>>4 )&0xf;
					}
          //System.out.print("recipient " + smsRecipient);
          this.countDestination(smsRecipient,day,hour);
				}
        //System.out.println("*******************finished processing*******************"+(System.currentTimeMillis()-profilestart));
  }

  private void skipStatusReport(DataInputStream inputStream ) throws IOException
  {
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
  
  private void countOriginating(Date day, int hour)
	{
    long[] MO;
    if ( MODays.containsKey(day))
    {
      MO = (long[]) MODays.get(day);
    }
    else
    {
      MO = new long[24];
      MODays.put(day,MO);
    }
    MO[hour]++;
  }
  
  private void countDestination(String messageDestination,Date day, int hour)
	{
		String resolvedMsgDest = this.resolveDestination(messageDestination);
    //System.out.println(" " + resolvedMsgDest);
    HashMap[] destinations;
    if ( destinationsDays.containsKey(day))
    {
      destinations = (HashMap[]) destinationsDays.get(day);
    }
    else
    {
      destinations = new HashMap[24];
      destinationsDays.put(day,destinations);
    }
    if (destinations[hour] == null ) {
        destinations[hour] = new HashMap();
      }
      Long count = (Long) destinations[hour].get(resolvedMsgDest);
      if (count==null){
        destinations[hour].put(resolvedMsgDest,new Long(1));
      }
      else	{
        destinations[hour].put(resolvedMsgDest,new Long(count.longValue()+1));
      }
  }
  
   private String resolveDestination(String messageDestination)
  {
      if ( messageDestination.startsWith("012") || messageDestination.startsWith("2012")||messageDestination.startsWith("018") || messageDestination.startsWith("2018")){
        return "Mob_Mobinil";
      }
      else if ( messageDestination.startsWith("010")|| messageDestination.startsWith("2010")|| messageDestination.startsWith("016")|| messageDestination.startsWith("2016")){
        return "Mob_Vodafone";
      } else if ( messageDestination.startsWith("011")|| messageDestination.startsWith("2011")){
        return "Mob_Etisalat";
      }
      else if ( messageDestination.length() == 4 ){
        return messageDestination;
      }
      else if ( messageDestination.length() > 4 && messageDestination.length() <=7){
        return messageDestination.substring(0,5);
      }
      else {
        return "International";
      }
  }
  
  private void countDelivery(Date day,int hour)
	{
		long[] delivery;
    if ( deliveryDays.containsKey(day))
    {
      delivery = (long[]) deliveryDays.get(day);
    }
    else
    {
      delivery = new long[24];
      deliveryDays.put(day,delivery);
    }
    delivery[hour]++;
	}
  
  private void writeDaysEvents(HashMap days,BufferedWriter outputStream ,String type) throws IOException
  {
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
    Iterator daysIterator =days.keySet().iterator();
    while (daysIterator.hasNext()) {
      Date day = (Date) daysIterator.next();
      c.setTime(day);
      long[] counts = (long[]) days.get(day);
      for (int i=0; i<counts.length; i++)
      {
        if (counts[i] == 0) 
        {
          continue;
        }
        c.set(Calendar.HOUR_OF_DAY, i);
        outputStream.write(frm.format(c.getTime())+ "," +counts[i]+","+type+",Mobile,local");
        outputStream.newLine();
      }
    }
  }
  
  private void writeDestinations(HashMap days,BufferedWriter outputStream ) throws IOException
  {
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
    Iterator daysIterator =days.keySet().iterator();
    while (daysIterator.hasNext()) {
      Date day = (Date) daysIterator.next();
      c.setTime(day);
      HashMap[] destinations = ( HashMap[]) days.get(day);
      for ( int i=0; i < destinations.length; i++) {
        c.set(Calendar.HOUR_OF_DAY, i);
        if (destinations[i] == null ){
          continue;
        }
        Iterator destinationsIterator = destinations[i].keySet().iterator();
        while (destinationsIterator.hasNext()) {
          String destination = (String)destinationsIterator.next();
          long count = ((Long) destinations[i].get(destination)).longValue();
          outputStream.write(frm.format(c.getTime())+ "," +count+",D,"+destination+",local");
          outputStream.newLine();
        }
      }
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
    //System.out.println("******************started writing*******************"+System.currentTimeMillis());
    this.writeDaysEvents(MODays,outputStream,"O");
    this.writeDaysEvents(deliveryDays,outputStream,"O_Delv");
    this.writeDestinations(destinationsDays,outputStream);
    //System.out.println("******************finished writing*******************"+System.currentTimeMillis());
	}


//  public static void main(String ag[]) {
//		try {
//			PropertyReader
//					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
//			SMSCBinaryConverter s = new SMSCBinaryConverter();
//      File folder = new File("D:/Meeting Files_2_3/15_5/R1/test");
//      File[] files = folder.listFiles();
//			s.convert(files, "SMSC");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
  
    public static void main(String ag[]) {
  		try {
  			PropertyReader
  					.init("D:/sayed/WorkSpace/VAS Performance Portal/DataCollection/");
  			SMSCBinaryConverter s = new SMSCBinaryConverter();
        //File folder = new File("D:/Meeting Files_2_3/15_5/R1/test");
        File[] files = new File[1];
        files[0] = new File("d:/temp/log_mo_mt_mbegvf-rtr01_20070522_095223_788.dat");
  		s.convert(files, "SMSC");
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
    	
  	}

}