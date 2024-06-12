/*
 * File:       VoucherServerAirInfoConverter.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 *
 */

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
import java.util.Date;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VoucherServerAirInfoConverter extends AbstractTextConverter {
	private Logger logger;

	public VoucherServerAirInfoConverter() {
	}

	/**
	 * loop over input file, loop over lines 
	 * data concatenate into on comma separated string then write to
	 * output output files are placed on the configured converted file path
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("VoucherServerAirInfoConverter.convert() - started converting input files ");
		try {
				String path =PropertyReader.getConvertedFilesPath();
				String line;
				File[] outputFiles = new File[1];
				File output = new File(path, "VoucherServerAirInfoConverterOut.log");
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
				BufferedReader inputStream;
				inputStream = new BufferedReader(new FileReader(inputFiles[0]));
				//Ignore the first line
				if(inputStream.ready()) 
					inputStream.readLine();
				while (inputStream.ready()) 
				{
					line=inputStream.readLine();
					if (line.equals("") || line.indexOf(",")<0) 
					{
						continue;
					}
					String tokens[] = line.split(",");
					//outputStream.write(tokens[0].trim());
					Date dataTime				= this.updateDateFormat(tokens[0].trim());
					String voucherServerDate	= this.readDate(dataTime);
					outputStream.write(voucherServerDate);
					outputStream.write(",");
					outputStream.write(String.valueOf(inputFiles.length));
					outputStream.newLine();
					break;
				}
				inputStream.close();
				logger.debug("VoucherServerAirInfoConverter.convert() - "+ inputFiles[0].getName() + " converted");
			
			outputStream.close();
			outputFiles[0] = output;
			
			logger
					.debug("VoucherServerAirInfoConverter.convert() - finished converting input files successfully ");
			return outputFiles;

		} catch (FileNotFoundException e) {
			logger.error("VoucherServerAirInfoConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("VoucherServerAirInfoConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (ParseException e) {
			logger.error("VoucherServerAirInfoConverter.convert() - error parsing date"
					+ e);
			new ApplicationException("" + e);
		}catch (InputException e) {
			logger.error("VoucherServerAirInfoConverter.convert() - invalid date in input file"
					+ e);
			new ApplicationException("" + e);
		}
		return null;
	}
	

	/**
	 * @param dateString -
	 *            the date string to be converted
	 * 
	 * @return String - the date string converted to standard format
	 * @exception InputException
	 *                if ParseException occured
	 */
	private Date updateDateFormat(String dateString) throws InputException {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("yyyy-MM-dd-HHmm");
		try {
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			throw new InputException("invalid date in input file ");
		}
	}
	
	/**
	 * extract the date from the header of the input file
	 * 
	 * @param rbtUsageDate -
	 *            the date to be converted
	 * 
	 * @return String - the date string converted to standard format
	 */
	private String readDate(Date voucherServerDate) throws IOException,ParseException 
	{	
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm");
		String date 	= frm.format(voucherServerDate);
		return date;
	}
	
		
	// for testing
	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\VASPortalWF5\\Source Code\\DataCollection");
			VoucherServerAirInfoConverter s = new VoucherServerAirInfoConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-VsXmlRpc_2.0_A_1-2009-04-05-0000.stat");
			input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-VsXmlRpc_2.0_A_1-2009-04-05-0000.stat");
			
//			input[1]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\FSC-VsXmlRpc_2.0_A_1-2007-06-11-0005.stat");
			s.convert(input,"VoucherServer");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}