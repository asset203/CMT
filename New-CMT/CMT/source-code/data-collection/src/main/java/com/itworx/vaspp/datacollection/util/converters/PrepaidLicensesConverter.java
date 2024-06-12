/*
 * File:       PrepaidLicensesConverter.java
 * Date        Author          Changes
 * 19/06/2007  Eshraq Essam  Created
 *
 * Converter class for converting Prepaid Licenses text input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PrepaidLicensesConverter extends AbstractTextConverter {
	private Logger logger;

	public PrepaidLicensesConverter() {
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
		String path = PropertyReader.getConvertedFilesPath();
		logger
				.debug("PrepaidLicensesConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("PrepaidLicensesConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String date = this.getYesterdaysDate();
				this.skip(10,inputStream);
				String maxConcurrent="";
				String unreservedTokensInUse="";
				String hostName="";
				String numberOfTokensUsed="";
                String licenceStatus="";
				boolean flag = false;
                boolean activeLicense = false;
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					
					if (line.equals("") || line.indexOf(":")<0) 
					{
						continue;
					}
					String header = line.substring(0, line.lastIndexOf(":")).trim();
					if(header.startsWith("|- ")){
						header = header.substring(3);
					}
                    if(header.equalsIgnoreCase("License status")){
                        licenceStatus =line.substring(line.lastIndexOf(":")+1).trim();
                        if("Active".equalsIgnoreCase(licenceStatus)){
                            activeLicense = true;
                        }else
                        {
                            activeLicense =false;
                        }
                    }
					else if(activeLicense && header.equalsIgnoreCase("Maximum concurrent user(s)"))
					{
						maxConcurrent=line.substring(line.lastIndexOf(":")+1).trim();
					}else if(header.equalsIgnoreCase("Unreserved Tokens in use"))
					{
						unreservedTokensInUse = line.substring(line.lastIndexOf(":")+1).trim();
					}else if(header.equalsIgnoreCase("User name")&&line.substring(line.lastIndexOf(":")+1).trim().equals("sdpuser"))
					{
						flag=true;
					}else if(header.equalsIgnoreCase("Host name"))
					{
						hostName = line.substring(line.lastIndexOf(":")+1).trim();
					}else if(header.equalsIgnoreCase("Number of tokens used")&& flag==true)
					{
						numberOfTokensUsed = line.substring(line.lastIndexOf(":")+1).trim();
						outputStream.write(date);
						outputStream.write(",");
						outputStream.write(String.valueOf(Integer.parseInt(maxConcurrent)*1000));
						outputStream.write(",");
						outputStream.write(String.valueOf(Integer.parseInt(unreservedTokensInUse)*1000));
						outputStream.write(",");
						outputStream.write(hostName);
						outputStream.write(",");
						outputStream.write(String.valueOf(Integer.parseInt(numberOfTokensUsed)*1000));
						outputStream.newLine();
						flag = false;
					}else 
					{
						continue;
					}
					
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("PrepaidLicensesConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("PrepaidLicensesConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("PrepaidLicensesConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("PrepaidLicensesConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("PrepaidLicensesConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file" + e);
		}
		
	}

	/**
	 * extract the date from the header of the input file
	 * 
	 * @param //inputStream -
	 *            the input file
	 * 
	 * @return the extracted data
	 * @exception ParseException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private String getYesterdaysDate() throws IOException,ParseException
	{	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    GregorianCalendar gc = new GregorianCalendar();
	    java.sql.Date d 	=  	new java.sql.Date(System.currentTimeMillis());
	    gc.setTime(d);
	    //System.out.println("Input Date = " + sdf.format(d));
	    int dayBefore = gc.get(Calendar.DAY_OF_YEAR);
	    gc.roll(Calendar.DAY_OF_YEAR, -1);
	    int dayAfter = gc.get(Calendar.DAY_OF_YEAR);
	    if(dayAfter > dayBefore) {
	        gc.roll(Calendar.YEAR, -1);
	    }
	    gc.get(Calendar.DATE);
	    java.util.Date yesterday = gc.getTime();
	    String currentDate = sdf.format(yesterday);
	    //System.out.println("Yesterdays Date = " + sdf.format(yesterday));
	    
	    return currentDate;

	}
	
	/**
	 * 
	 * @param num
	 * @param inputStream
	 * @throws IOException
	 */
	private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
    }
  }


    public static void main(String ag[]) {
        try {
            PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode_\\DataCollection");
            PrepaidLicensesConverter s = new PrepaidLicensesConverter();
            File[] input = new File[1];
            input[0]=new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\prepaid licence\\lsmon.log");
            s.convert(input,"PrepaidLicenses");
            s.getYesterdaysDate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}