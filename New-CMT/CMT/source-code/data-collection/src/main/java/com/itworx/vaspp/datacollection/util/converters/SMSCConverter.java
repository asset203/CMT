/*
 * File: SMSCConverter.java
 * Date        Author          Changes
 * 23/03/2006  Nayera Mohamed  Created
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

public class SMSCConverter extends AbstractTextConverter {
	private Logger logger;

	private long MO_FTD[] = new long[24];

	private long MO_local[] = new long[24];

	private HashMap[] AO_local = new HashMap[24];

	private HashMap[] AO_FTD = new HashMap[24];

	private HashMap[] D_local = new HashMap[24];

	private HashMap[] D_FTD = new HashMap[24];

	private long MO_Del[] = new long[24];

	private long AO_Del[] = new long[24];

	private Date day;

	public SMSCConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				this.skip(6, inputStream);
				SimpleDateFormat frm = new SimpleDateFormat();
				frm.applyPattern("yyMMddHHmmss");
				Date date;
				String messageSource;
				String sourceNode;
				String messageDestination;
				String origAddress;
				String destinationLogicalSME;
				while (inputStream.ready()) {
					String line = inputStream.readLine();
					if (line.equals("")) {
						break;
					}
					try {

						if (line.startsWith("8,")) {
							String tokens[] = line.split(",");
							String dateString = tokens[1].substring(0, 12);
							date = frm.parse(dateString);
							day = date;
							messageSource = tokens[2];
							origAddress = tokens[3];
							sourceNode = tokens[30];
							this.countOriginating(messageSource, origAddress,
									sourceNode, date);
						}
						if (line.startsWith("5,")) {
							String tokens[] = line.split(",");
							String dateString = tokens[1].substring(0, 12);
							date = frm.parse(dateString);
							day = date;
							messageDestination = tokens[4];
							destinationLogicalSME = tokens[5];
							sourceNode = tokens[27];
							this.countDestination(messageDestination,
									destinationLogicalSME, sourceNode, date);
						}
						if (line.startsWith("3,")) {
							String tokens[] = line.split(",");
							String dateString = tokens[1].substring(0, 12);
							date = frm.parse(dateString);
							day = date;
							messageSource = tokens[2];
							messageDestination = tokens[4];
							destinationLogicalSME = tokens[5];
							this.countDelivery(messageSource, date);
							this.countDestination(messageDestination,
									destinationLogicalSME, "local", date);
						}
					} catch (ParseException e) {
						logger.error("invalid date in input file "
								+ inputFiles[j].getName());
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
						logger.error("invalid record in input file " + e);
					} catch (StringIndexOutOfBoundsException e) {
						e.printStackTrace();
						logger.error("invalid record in input file " + e);
					} catch (NullPointerException e) {
						e.printStackTrace();
						logger.error("invalid record in input file " + e);
					}
				}
				inputStream.close();

			}
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			writeEvents(outputStream);
			outputStream.close();
			outputFiles[0] = output;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}

		return outputFiles;

	}

	private void countOriginating(String messageSource, String origAddress,
			String sourceNode, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (messageSource.equals("0")) {
			if (matchesLocal(sourceNode)) {
				MO_local[hour]++;
			} else {
				MO_FTD[hour]++;
			}
		} else {
			// if messageSource is not equal 0 then the message is Application Originating
			// messages are counted for each application separately
			int numberIndex = origAddress
					.lastIndexOf(" ", origAddress.length());
			if (numberIndex < 0) {
				numberIndex = 2;
			}
			String appAddress = origAddress.substring(numberIndex, origAddress
					.length());
			appAddress = appAddress.trim();
			if (matchesLocal(sourceNode)) {
				if (AO_local[hour] == null) {
					AO_local[hour] = new HashMap();
				}
				Long count = (Long) AO_local[hour].get(appAddress);
				if (count == null) {
					AO_local[hour].put(appAddress, new Long(1));
				} else {
					AO_local[hour].put(appAddress, new Long(
							count.longValue() + 1));
				}
			} else {
				if (AO_FTD[hour] == null) {
					AO_FTD[hour] = new HashMap();
				}
				Long count = (Long) AO_FTD[hour].get(appAddress);
				if (count == null) {
					AO_FTD[hour].put(appAddress, new Long(1));
				} else {
					AO_FTD[hour].put(appAddress,
							new Long(count.longValue() + 1));
				}
			}
		}
	}

	private void countDestination(String messageDestination,
			String destinationLogicalSME, String sourceNode, Date date) {
		String resolvedMsgDest = this.resolveDestination(messageDestination,
				destinationLogicalSME);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (matchesLocal(sourceNode) || sourceNode.equals("local")) {
			if (D_local[hour] == null) {
				D_local[hour] = new HashMap();
			}
			Long count = (Long) D_local[hour].get(resolvedMsgDest);
			if (count == null) {
				D_local[hour].put(resolvedMsgDest, new Long(1));
			} else {
				D_local[hour].put(resolvedMsgDest, new Long(
						count.longValue() + 1));
			}
		} else {
			if (D_FTD[hour] == null) {
				D_FTD[hour] = new HashMap();
			}
			Long count = (Long) D_FTD[hour].get(resolvedMsgDest);
			if (count == null) {
				D_FTD[hour].put(resolvedMsgDest, new Long(1));
			} else {
				D_FTD[hour].put(resolvedMsgDest,
						new Long(count.longValue() + 1));
			}
		}
	}

	private boolean matchesLocal(String sourceNode) {
		try {
			if (sourceNode == null) {
				return true;
			}
			int length = sourceNode.length();
			if (length == 0) {
				return true;
			} else if (sourceNode.charAt(length - 12) == '2'
					&& sourceNode.charAt(length - 1) == 0
					&& sourceNode.charAt(length - 10) == 1) {
				return true;
			}
		} catch (StringIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	private void countDelivery(String messageSource, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (messageSource.equals("0")) {
			MO_Del[hour]++;
		} else {
			AO_Del[hour]++;
		}
	}

	private String resolveDestination(String messageDestination,
			String destinationLogicalSME) {

		int startIndex = messageDestination.indexOf(" ");
		if (startIndex == -1) {
			startIndex = 2;
		}
		String number = messageDestination.substring(startIndex + 1,
				messageDestination.length());
		number = number.trim();
		if (destinationLogicalSME.equals("0")) {
			if (number.startsWith("2012") || number.startsWith("2018")) {
				return "Mob_Mobinil";
			} else if (number.startsWith("2010") || number.startsWith("2016")) {
				return "Mob_Vodafone";
			} else if (number.startsWith("2011")) {
				return "Mob_Etisalat";
			} else {
				return "International";
			}
		} else {
			return number;
		}
	}

	/**
	 * 
	 * 
	 *
	 * @param outputStream -
	 *            input file
	 *
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
	private void writeEvents(BufferedWriter outputStream) throws IOException {

		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);

		for (int i = 0; i < 24; i++) {
			c.set(Calendar.HOUR_OF_DAY, i);
			if (this.MO_local[i] != 0) {
				outputStream.write(frm.format(c.getTime()) + ","
						+ this.MO_local[i] + ",O,Mobile,local");
				outputStream.newLine();
			}
			if (this.MO_FTD[i] != 0) {
				outputStream.write(frm.format(c.getTime()) + ","
						+ this.MO_FTD[i] + ",O,Mobile,FTD");
				outputStream.newLine();
			}
		}
		for (int i = 0; i < 24; i++) {
			if (this.AO_local[i] == null) {
				continue;
			}
			c.set(Calendar.HOUR_OF_DAY, i);
			Iterator AOIterator = this.AO_local[i].keySet().iterator();
			while (AOIterator.hasNext()) {
				String application = (String) AOIterator.next();
				long count = ((Long) AO_local[i].get(application)).longValue();
				outputStream.write(frm.format(c.getTime()) + "," + count
						+ ",O," + application + ",local");
				outputStream.newLine();
			}
		}
		for (int i = 0; i < 24; i++) {
			if (this.AO_FTD[i] == null) {
				continue;
			}
			c.set(Calendar.HOUR_OF_DAY, i);
			Iterator AOIterator = this.AO_FTD[i].keySet().iterator();
			while (AOIterator.hasNext()) {
				String application = (String) AOIterator.next();
				long count = ((Long) AO_FTD[i].get(application)).longValue();
				outputStream.write(frm.format(c.getTime()) + "," + count
						+ ",O," + application + ",FTD");
				outputStream.newLine();
			}
		}

		for (int i = 0; i < 24; i++) {
			if (this.D_FTD[i] == null) {
				continue;
			}
			c.set(Calendar.HOUR_OF_DAY, i);
			Iterator AOIterator = this.D_FTD[i].keySet().iterator();
			while (AOIterator.hasNext()) {
				String destination = (String) AOIterator.next();
				long count = ((Long) D_FTD[i].get(destination)).longValue();
				outputStream.write(frm.format(c.getTime()) + "," + count
						+ ",D," + destination + ",FTD");
				outputStream.newLine();
			}
		}

		for (int i = 0; i < 24; i++) {
			if (this.D_local[i] == null) {
				continue;
			}
			c.set(Calendar.HOUR_OF_DAY, i);
			Iterator AOIterator = this.D_local[i].keySet().iterator();
			while (AOIterator.hasNext()) {
				String destination = (String) AOIterator.next();
				long count = ((Long) D_local[i].get(destination)).longValue();
				outputStream.write(frm.format(c.getTime()) + "," + count
						+ ",D," + destination + ",local");
				outputStream.newLine();
			}
		}

		for (int i = 0; i < 24; i++) {
			c.set(Calendar.HOUR_OF_DAY, i);
			if (this.AO_Del[i] != 0) {
				outputStream.write(frm.format(c.getTime()) + ","
						+ this.AO_Del[i] + ",O_Delv,Application,DELIVERY");
				outputStream.newLine();
			}
			if (this.MO_Del[i] != 0) {
				outputStream.write(frm.format(c.getTime()) + ","
						+ this.MO_Del[i] + ",O_Delv,Mobile,DELIVERY");
				outputStream.newLine();
			}
		}
	}

	private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
		}
	}

	//  public static void main(String ag[]) {
	//		try {
	//			PropertyReader
	//					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
	//			SMSCConverter s = new SMSCConverter();
	//			File[] files = new File[1];
	//			files[0] =new File("D:/Meeting Files_2_3/060507_TLG.DMP;1");
	//      s.convert(files,"SMSC");
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void main(String ag[]) {
		try {
			PropertyReader.init("/export/home/vas_dev/DataCollection/");
			SMSCConverter s = new SMSCConverter();
			//File folder = new File("D:/Meeting Files_2_3/15_5/R1/test");
			File[] files = new File[1];
			files[0] = new File("060513_TLG.DMP;1");
			s.convert(files, "SMSC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}