/*
 * File:       VoucherServerAirConverter.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam	   Created
 *
 * Converter class for converting Voucher Server text input file into comma spearated format
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
import com.itworx.vaspp.datacollection.util.Utils;

public class VoucherServerAirConverter extends AbstractTextConverter {
	private Logger logger;
	
	//the following arrays contains the extracted columns for each needed information
	//needed field will represent the sum of all corresponding fields 

	private static String[] vchrDetailsIn = new String[]{"/VoucherAdmin:GetVoucherDetails_2.1:In","/VoucherAdmin:GetVoucherDetails_2.2:In","/VoucherAdmin:GetVoucherDetails_2.4:In"};
	private static String[] reserveVchrIn = new String[]{"/VoucherUsage:ReserveVoucher_2.1:In","/VoucherUsage:ReserveVoucher_2.2:In", "/VoucherUsage:ReserveVoucher_2.4:In"};
	private static String[] reserveVchrOutSuccess = new String[]{"/VoucherUsage:ReserveVoucher_2.1:Out:Success","/VoucherUsage:ReserveVoucher_2.2:Out:Success", "/VoucherUsage:ReserveVoucher_2.4:Out:Success"};
	private static String[] endReserveCommitOutSuccess = new String[]{"/VoucherUsage:EndReservation_2.1:Commit:Out:Success","/VoucherUsage:EndReservation_2.2:Commit:Out:Success", "/VoucherUsage:EndReservation_2.4:Commit:Out:Success"};
	private static String[] endReserveRollbackInValid = new String[]{"/VoucherUsage:EndReservation_2.1:Rollback:In:Valid","/VoucherUsage:EndReservation_2.2:Rollback:In:Valid", "/VoucherUsage:EndReservation_2.4:Rollback:In:Valid"};

	public VoucherServerAirConverter() {
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
		logger.debug("VoucherServerAirConverter.convert() - started converting input files ");
		try {
			String path =PropertyReader.getConvertedFilesPath();
			String line;
			File[] outputFiles = new File[1];
			File output = new File(path, "VoucherServerAirConverterOut.log");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			
			// header array
			String[] headers = null;
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				//Ignore the first line
				if(inputStream.ready())
				{
					headers = inputStream.readLine().split(",");
				}
				while (inputStream.ready()) 
				{
					line=inputStream.readLine();
					if (line.equals("") || line.indexOf(",")<0) 
					{
						continue;
					}
					String tokens[] = line.split(",");
					Date dateTime = this.updateDateFormat(tokens[0].trim());
					String dateString =this.readDate(dateTime);
					outputStream.write(dateString);
					outputStream.write(",");
					//outputStream.write(tokens[7].trim());
					outputStream.write(getNeededColumnValue(vchrDetailsIn, headers, tokens)+"");
					outputStream.write(",");
					//outputStream.write(tokens[66].trim());
					outputStream.write(getNeededColumnValue(reserveVchrIn, headers, tokens)+"");
					outputStream.write(",");
					//outputStream.write(tokens[67].trim());
					outputStream.write(getNeededColumnValue(reserveVchrOutSuccess, headers, tokens)+"");
					outputStream.write(",");
					//outputStream.write(tokens[70].trim());
					outputStream.write(getNeededColumnValue(endReserveCommitOutSuccess, headers, tokens)+"");
					outputStream.write(",");
					//outputStream.write(tokens[71].trim());
					outputStream.write(getNeededColumnValue(endReserveRollbackInValid, headers, tokens)+"");
					outputStream.newLine();
				}
				inputStream.close();
				logger.debug("VoucherServerAirConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			outputStream.close();
			outputFiles[0] = output;
			logger
					.debug("VoucherServerAirConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("VoucherServerAirConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("VoucherServerAirConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}catch (ParseException e) {
			logger.error("VoucherServerAirConverter.convert() - error parsing date"
					+ e);
			new ApplicationException("" + e);
		}catch (InputException e) {
			logger.error("VoucherServerAirConverter.convert() - invalid date in input file"
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
		
	/**
	 * get the needed column value by summing up all its erpresented column
	 * 
	 * @param neededColumns -
	 *            the needed column represented by its coressponding columns
	 * @param headers -
	 *            the headers of the columns in the file 
	 * @param values -
	 *            the Array of all values           
	 * @return String - the date string converted to standard format
	 */
	private int getNeededColumnValue(String[] neededColumnKeys,String[] headers , String[] values)
	{
		int returnedValue = 0;
		if(neededColumnKeys != null && neededColumnKeys.length > 0)
		{
			for(int i = 0;i<neededColumnKeys.length;i++)
			{
				int idx = Utils.indexOf(neededColumnKeys[i],headers);
				if(idx != -1)
				{
					try
					{
						String value = values[idx].trim();
						returnedValue += Integer.parseInt(value);
					}
					catch(Exception e)
					{
						logger.error("VoucherServerAirConverter.getNeededColumnValue() - value cannot be parsed"
								+ e);
						new ApplicationException("" + e);
					}
				}
			}
		}
		return returnedValue;
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\Projects\\VNPP");
			VoucherServerAirConverter s = new VoucherServerAirConverter();
			File[] input = new File[1];
			
			//input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-VsXmlRpc_2.0_A_1-2009-04-05-0000.stat");
			input[0]=new File("D:\\Projects\\VNPP\\FSC-VsXmlRpc_2.0_A_1-2011-02-04-0300.stat");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}