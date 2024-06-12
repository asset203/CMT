/*
 * File:       RBTUsageSecondsConverter.java
 * Date        Author          Changes
 * 18/03/2007  Eshraq Essam  Created
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
import com.itworx.vaspp.datacollection.persistenceobjects.RBT_UsageInfo;
import com.itworx.vaspp.datacollection.util.PropertyReader;

/*
 * The structure of the text input file is lines of tab separated data, the
 * first 10 line is not data
 * 
 */

public class RBTUsageSecondsConverter extends AbstractTextConverter {
	private Logger logger;

	public RBTUsageSecondsConverter() {
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
		logger.debug("RBTUsageSecondsConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		
		int a = 0;
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("RBTUsageSecondsConverter.convert() - converting file "+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
				Vector topTenLines = new Vector();
				
				// scape 10 lines from the file
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				inputStream.readLine();
				while (inputStream.ready()) 
				{
					a=a+1;
					String line = inputStream.readLine();
					if (line.equals("")) 
					{
						break;
					}
					RBT_UsageInfo rbtUsageData = new RBT_UsageInfo();
					line = line.replace('\t', ',');
					String dateString = line.substring(0, line.indexOf(",", 0));
					line = line.substring(line.indexOf(",", 0));
					Date newDate = this.updateDateFormat(dateString);
					rbtUsageData.setDate(newDate);
					line = line.substring(line.indexOf(",", 0));
					rbtUsageData.setIam(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					rbtUsageData.setEarlyREL(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					rbtUsageData.setAcmACK(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					rbtUsageData.setAcmPosvAck(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					rbtUsageData.setPlayFileAck(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					rbtUsageData.setPlayFilePosvAck(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					line = line.substring(line.indexOf(",", 1));
					if(line.indexOf(",", 1)>=0)
						rbtUsageData.setRel(Long.parseLong(line.substring(1, line.indexOf(",", 1))));
					else
						rbtUsageData.setRel(Long.parseLong(line.substring(1)));
										
					topTenLines = compareIAM(topTenLines,rbtUsageData);
				
				}
				for (int j = 0; j < topTenLines.size(); j++) 
				{
					RBT_UsageInfo rbtUsageData = (RBT_UsageInfo)topTenLines.get(j);
					String line = 	this.readDate(rbtUsageData.getDate());
					line =	line + "," + rbtUsageData.getIam()+ "," + rbtUsageData.getEarlyREL()+ "," + rbtUsageData.getAcmACK();
					line =  line + "," + rbtUsageData.getAcmPosvAck() + "," + rbtUsageData.playFileAck;
					line =  line + "," + rbtUsageData.getPlayFilePosvAck() + "," + rbtUsageData.getRel();
					//System.out.println("line "+line);
					outputStream.write(line);
					outputStream.newLine();										
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("RBTUsageSecondsConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
		} catch (FileNotFoundException e) {
			logger.error("RBTUsageSecondsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("RBTUsageSecondsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}catch (ParseException e) {
				logger.error("RBTUsageSecondsConverter.convert() - error parsing date" + e);
				throw new InputException("invalid date in input file" + e);
			}
		logger
				.debug("RBTUsageSecondsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
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
		frm.applyPattern("dd-MM-yyyy HH:mm:ss");
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
	private String readDate(Date rbtUsageDate) throws IOException,ParseException 
	{	
		SimpleDateFormat frm 	= new SimpleDateFormat();
		frm.applyPattern("dd-MM-yyyy HH:mm:ss");
		String date 	= frm.format(rbtUsageDate);
		return date;
	}
	
	
	private Vector compareIAM(Vector topTenLines,RBT_UsageInfo newRBTUsageData) throws InputException 
	{
		Vector finalTopTenLines = new Vector();
		if(topTenLines.size()<= 0)
			finalTopTenLines = insertSorted(topTenLines,newRBTUsageData,null);
		else
		{
			RBT_UsageInfo maxRBTUsageIAM = (RBT_UsageInfo)topTenLines.firstElement();
			RBT_UsageInfo minRBTUsageIAM = (RBT_UsageInfo)topTenLines.lastElement();
			if(newRBTUsageData.getIam()>maxRBTUsageIAM.getIam())
				finalTopTenLines = insertSorted(topTenLines,newRBTUsageData,maxRBTUsageIAM);
			else if(topTenLines.size()== 10 && newRBTUsageData.getIam()<=minRBTUsageIAM.getIam())
				return topTenLines;
			else if(topTenLines.size()< 10 || newRBTUsageData.getIam()>minRBTUsageIAM.getIam())
				finalTopTenLines = insertSorted(topTenLines,newRBTUsageData,maxRBTUsageIAM);
			
		}
		return finalTopTenLines;
	}
	
	private Vector insertSorted(Vector topTenLines,RBT_UsageInfo newRBTUsageData,RBT_UsageInfo maxRBTUsageIAM) throws InputException 
	{
		Vector newTopTenLines = new Vector();
		if(topTenLines.size()<= 0)
			newTopTenLines.add(0,newRBTUsageData);
		else if(newRBTUsageData.getIam()>=maxRBTUsageIAM.getIam())
		{	
			newTopTenLines.add(0,newRBTUsageData);
			for (int j = 0; j < topTenLines.size(); j++) 
			{
				if(j+1<10)
					newTopTenLines.add(j+1,topTenLines.get(j));
			}
		}else if(newRBTUsageData.getIam()<maxRBTUsageIAM.getIam())	
		{
			RBT_UsageInfo minRBTUsageIAM = (RBT_UsageInfo)topTenLines.lastElement();
			if(minRBTUsageIAM.getIam()<newRBTUsageData.getIam())
			{
				int i;
				boolean flag = false;
				for (i = 0; i < topTenLines.size(); i++) 
				{
					RBT_UsageInfo tempRBTUsageInfo = (RBT_UsageInfo)topTenLines.get(i);
					if(tempRBTUsageInfo.getIam()>newRBTUsageData.getIam())
						newTopTenLines.add(i,tempRBTUsageInfo);
					else if(flag==false)
					{
						flag=true;
						newTopTenLines.add(i,newRBTUsageData);
					}
					if(flag==true)
					{
						if(i+1<10)
							newTopTenLines.add(i+1,tempRBTUsageInfo);
					}
				}
			}else
			{
				int i;
				for (i = 0; i < topTenLines.size(); i++) 
				{
					newTopTenLines.add(i,topTenLines.get(i));
				}
				newTopTenLines.add(i,newRBTUsageData);
			}
			
		}
					
		return newTopTenLines;
	}

	public static void main(String ag[]) {
		try {
		/*	String date="13-03-2007 20:00:01";
			RBTUsageSecondsConverter converter=new RBTUsageSecondsConverter();
			System.out.println("Date = "+converter.updateDateFormat(date));
			System.out.println("Date String = "+converter.readDate(converter.updateDateFormat(date)));*/
			
			PropertyReader.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal\\Phase II\\Source Code\\DataCollection");
			RBTUsageSecondsConverter s = new RBTUsageSecondsConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\Usage.2007031309RBT");
			input[1]=new File("D:\\Usage.2007031305RBT");
			s.convert(input,"RBT Usage");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}