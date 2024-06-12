/*
 * File:       VoucherServerAirTopSecondsConverter.java
 * Date        Author          Changes
 * 26/06/2007  Eshraq Essam  Created
 *
 * Converter class for converting RBT Usage text input file into comma spearated format
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
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.VoucherServerTopSec;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

/*
 * The structure of the text input file is lines of tab separated data, the
 * 
 */

public class VoucherServerAirTopSecondsConverter extends AbstractTextConverter {
	private Logger logger;

	private static String[] vchrDetailsIn = new String[]{"/VoucherAdmin:GetVoucherDetails_2.1:In","/VoucherAdmin:GetVoucherDetails_2.2:In"};
	private static String[] reserveVchrIn = new String[]{"/VoucherUsage:ReserveVoucher_2.1:In","/VoucherUsage:ReserveVoucher_2.2:In"};
	private static String[] reserveVchrOutSuccess = new String[]{"/VoucherUsage:ReserveVoucher_2.1:Out:Success","/VoucherUsage:ReserveVoucher_2.2:Out:Success"};
	private static String[] endReserveCommitOutSuccess = new String[]{"/VoucherUsage:EndReservation_2.1:Commit:Out:Success","/VoucherUsage:EndReservation_2.2:Commit:Out:Success"};
	private static String[] endReserveRollbackInValid = new String[]{"/VoucherUsage:EndReservation_2.1:Rollback:In:Valid","/VoucherUsage:EndReservation_2.2:Rollback:In:Valid"};

	public VoucherServerAirTopSecondsConverter() {
	}

	/**
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
		logger.debug("VoucherServerAirTopSecondsConverter.convert() - started converting input files ");
		int a = 0;
		try {
			String path =PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File output = new File(path, "VoucherServerAirTopSecondsConverterOut.log");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			Vector topLines = new Vector();
			
			//header array
			String[] headers = null;
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("VoucherServerAirTopSecondsConverter.convert() - converting file "+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				//	Ignore the first line
				if(inputStream.ready())
				{
					headers = inputStream.readLine().split(",");
				}
				
				while (inputStream.ready()) 
				{
					a=a+1;
					String line = inputStream.readLine();
					if (line.equals("") || line.indexOf(",")<0) 
					{
						continue;
					}
					VoucherServerTopSec voucherServerData 	= new VoucherServerTopSec();
					String tokens[] 						= line.split(",");
					Date dataTime 							= this.updateDateFormat(tokens[0].trim());
					String voucherServerDate				= this.readDate(dataTime);
					
					voucherServerData.setDateTime(voucherServerDate);
					
					//voucherServerData.setGetVoucherDetails(Long.parseLong(tokens[7].trim()));
					voucherServerData.setGetVoucherDetails(Long.parseLong(getNeededColumnValue(vchrDetailsIn, headers, tokens)+""));
					
					//voucherServerData.setReserveVoucherIn(Long.parseLong(tokens[66].trim()));
					voucherServerData.setReserveVoucherIn(Long.parseLong(getNeededColumnValue(reserveVchrIn, headers, tokens)+""));
					
					//voucherServerData.setReserveVoucherOutSuccess(Long.parseLong(tokens[67].trim()));
					voucherServerData.setReserveVoucherOutSuccess(Long.parseLong(getNeededColumnValue(reserveVchrOutSuccess, headers, tokens)+""));
					
					//voucherServerData.setEndReservationCommitOutSuccess(Long.parseLong(tokens[70].trim()));
					voucherServerData.setEndReservationCommitOutSuccess(Long.parseLong(getNeededColumnValue(endReserveCommitOutSuccess, headers, tokens)+""));
					
					//voucherServerData.setEndReservationRollbackInValid(Long.parseLong(tokens[71].trim()));
					voucherServerData.setEndReservationRollbackInValid(Long.parseLong(getNeededColumnValue(endReserveRollbackInValid, headers, tokens)+""));
										
					topLines = compareReserveVoucher(topLines,voucherServerData);
				
				}
				
				inputStream.close();
				logger.debug("VoucherServerAirTopSecondsConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
			
			for (int j = 0; j < topLines.size(); j++) 
			{
				VoucherServerTopSec voucherServerData = (VoucherServerTopSec)topLines.get(j);
				String line = 	voucherServerData.getDateTime();
				double voucherDetails = new Double(voucherServerData.getGetVoucherDetails()).doubleValue()/new Double(60).doubleValue();
				double reserveVoucherIn = new Double(voucherServerData.getReserveVoucherIn()).doubleValue()/new Double(60).doubleValue();
				double reserveVoucherOutSuccess = new Double(voucherServerData.getReserveVoucherOutSuccess()).doubleValue()/new Double(60).doubleValue();
				double endReservationCommitOutSuccess = new Double(voucherServerData.getEndReservationCommitOutSuccess()).doubleValue()/new Double(60).doubleValue();
				double endReservationRollbackInValid = new Double(voucherServerData.getEndReservationRollbackInValid()).doubleValue()/new Double(60).doubleValue();
				
				line =	line + "," + String.valueOf(round(voucherDetails, 3)) + "," + String.valueOf(round(reserveVoucherIn, 3));
				line =  line + "," + String.valueOf(round(reserveVoucherOutSuccess, 3)) + "," + String.valueOf(round(endReservationCommitOutSuccess, 3));
				line =  line + "," + String.valueOf(round(endReservationRollbackInValid, 3));
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();										
			}
			
			
			outputStream.close();
			outputFiles[0] = output;
			logger
				.debug("VoucherServerAirTopSecondsConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("VoucherServerAirTopSecondsConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("VoucherServerAirTopSecondsConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (ParseException e) {
			logger.error("VoucherServerAirTopSecondsConverter.convert() - error parsing date"
					+ e);
			new ApplicationException("" + e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @param decimalPlace
	 * @return
	 */
	  double round(double value, int decimalPlace) 
	  {
		    double power_of_ten = 1;
		    while (decimalPlace-- > 0)
		       power_of_ten *= 10.0;
		    return Math.round(value * power_of_ten) 
		       / power_of_ten;
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
		frm.applyPattern("MM/dd/yyyy HH:mm:01");
		String date 	= frm.format(voucherServerDate);
		return date;
	}
	
	
	private Vector compareReserveVoucher(Vector topLines,VoucherServerTopSec newVoucherServerData) throws InputException 
	{
		Vector finalTopLines = new Vector();
		if(topLines.size()<= 0)
			finalTopLines = insertSorted(topLines,newVoucherServerData,null);
		else
		{
			VoucherServerTopSec maxVoucherServerReserveVoucherIn = (VoucherServerTopSec)topLines.firstElement();
			VoucherServerTopSec minVoucherServerReserveVoucherIn = (VoucherServerTopSec)topLines.lastElement();
			if(newVoucherServerData.getReserveVoucherIn()>maxVoucherServerReserveVoucherIn.getReserveVoucherIn())
				finalTopLines = insertSorted(topLines,newVoucherServerData,maxVoucherServerReserveVoucherIn);
			else if(topLines.size()== 100 && newVoucherServerData.getReserveVoucherIn()<=minVoucherServerReserveVoucherIn.getReserveVoucherIn())
				return topLines;
			else if(topLines.size()< 100 || newVoucherServerData.getReserveVoucherIn()>minVoucherServerReserveVoucherIn.getReserveVoucherIn())
				finalTopLines = insertSorted(topLines,newVoucherServerData,maxVoucherServerReserveVoucherIn);
			
		}
		return finalTopLines;
	}
	
	private Vector insertSorted(Vector topLines,VoucherServerTopSec newVoucherServerData,VoucherServerTopSec maxVoucherServerReserveVoucherIn) throws InputException 
	{
		Vector newTopLines = new Vector();
		if(topLines.size()<= 0)
			newTopLines.add(0,newVoucherServerData);
		else if(newVoucherServerData.getReserveVoucherIn()>=maxVoucherServerReserveVoucherIn.getReserveVoucherIn())
		{	
			newTopLines.add(0,newVoucherServerData);
			for (int j = 0; j < topLines.size(); j++) 
			{
				if(j+1<100)
					newTopLines.add(j+1,topLines.get(j));
			}
		}else if(newVoucherServerData.getReserveVoucherIn()<maxVoucherServerReserveVoucherIn.getReserveVoucherIn())	
		{
			VoucherServerTopSec minVoucherServerReserveVoucherIn = (VoucherServerTopSec)topLines.lastElement();
			if(minVoucherServerReserveVoucherIn.getReserveVoucherIn()<newVoucherServerData.getReserveVoucherIn())
			{
				int i;
				boolean flag = false;
				for (i = 0; i < topLines.size(); i++) 
				{
					VoucherServerTopSec tempVoucherServerReserveVoucherIn = (VoucherServerTopSec)topLines.get(i);
					if(tempVoucherServerReserveVoucherIn.getReserveVoucherIn()>newVoucherServerData.getReserveVoucherIn())
						newTopLines.add(i,tempVoucherServerReserveVoucherIn);
					else if(flag==false)
					{
						flag=true;
						newTopLines.add(i,newVoucherServerData);
					}
					if(flag==true)
					{
						if(i+1<100)
							newTopLines.add(i+1,tempVoucherServerReserveVoucherIn);
					}
				}
			}else
			{
				int i;
				for (i = 0; i < topLines.size(); i++) 
				{
					newTopLines.add(i,topLines.get(i));
				}
				newTopLines.add(i,newVoucherServerData);
			}
			
		}
					
		return newTopLines;
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
						logger.error("VoucherServerAirTopSecondsConverter.getNeededColumnValue() - value cannot be parsed"
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
			
			PropertyReader.init("D:\\VASPortalWF5\\Source Code\\DataCollection");
			VoucherServerAirTopSecondsConverter s = new VoucherServerAirTopSecondsConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-VsXmlRpc_2.0_A_1-2009-04-05-0000.stat");
			input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-VsXmlRpc_2.0_A_1-2009-04-05-0000.stat");
			
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}