package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class CopsUSSDSecondsConverter extends AbstractTextConverter {
	private Logger logger;

	private EventsComparator comparator = new EventsComparator();

	/**
	 * loop over input files, loop over lines in each file count requestper
	 * vodafone , ,request per mobinil , success requests ,fail request for each
	 * hour write output into one output file output file is placed on the
	 * configured converted file path
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
		String line;
		String[] lineTokens;
		Date dateTime=null;
		Date oldDateTime=null;
		String dateTimeString="";
		SimpleDateFormat dateFormater = new SimpleDateFormat();
		int count = 0;
		logger = Logger.getLogger(systemName);
		logger
				.debug("CopsUSSDSecondsConverter.convert() - started converting input files ");
		try {
			String path =PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			BufferedReader inputStream;
			String fileType = null;
			for (int i = 0; i < inputFiles.length; i++) 
			{
				
				logger.debug("CopsUSSDSecondsConverter.convert() - converting file "+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				Vector records = new Vector();
				while (inputStream.ready()) 
				{
					
					line = inputStream.readLine();
					lineTokens = line.split(" ");
					if (line.indexOf("888")>0)
						fileType = "888";
					else if(line.indexOf("999")>0)
						fileType = "999";
						
					if (lineTokens!=null&&lineTokens.length > 2&&(line.endsWith("[op:1]")/*||line.endsWith("[op:32]")*/)) {
						try 
						{
							dateTimeString = lineTokens[0] + " "
									+ lineTokens[1].substring(0, 8);
							
								dateFormater.applyPattern("yyyy-MM-dd HH:mm:ss");
								dateTime = dateFormater.parse(dateTimeString);
								if(oldDateTime!=null&&!oldDateTime.equals(dateTime)) {
									updateSeconds(records,oldDateTime,count);
									count=0;
								}
							} catch (Exception x) 
							{
								logger
								.error(
										"com.itworx.vaspp.datacollection.util.converters.CopsUSSDSecondsConverter.convert()",
										x);
								x.printStackTrace();
							}
							oldDateTime=dateTime;
							count++;
						}
				}
				updateSeconds(records,oldDateTime,count);
				inputStream.close();
				logger.debug("USSD Seconds Converter SECOND.convert() - "
						+ inputFiles[i].getName() + " converted");
				dateFormater.applyPattern("MM/dd/yyyy HH:mm:ss");
			//	System.out.println(records.size()); 
				this.sort(records) ; 
				for (int j = 0;  ( j < records.size() && j < 20 )  ; j++) 
				{
					SecondEvent record=(SecondEvent)records.get(j);
					outputStream.write(dateFormater.format(record.getDate())+","+record.getCount()+","+fileType);
					//System.out.println(dateFormater.format(record.getDate())+","+record.getCount()+","+fileType);
					outputStream.newLine();
				}
			
			}
	
			outputStream.close();
			outputFiles[0] = output;
			logger
					.debug("CopsUSSDSecondsConverter.convert() - finished converting input files successfully ");
			return outputFiles;

		} catch (FileNotFoundException e) {
			logger.error("CopsUSSDSecondsConverter.convert() - Input file not found " + e);
			e.printStackTrace();
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("CopsUSSDSecondsConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CopsUSSDSecondsConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;


	}
	
	/**
	 * 
	 * @param records
	 */
	private void sort(Vector records)
	{ 
		SecondEvent x , y ; 
		
		 for (int i=0; i<records.size()-1; i++)
		      for (int j=records.size()-1; j>i; j--)
		      {
		    	  x = (SecondEvent)records.elementAt(j-1); 
		    	  y = (SecondEvent)records.elementAt(j); 
		    	  if (x.getCount()< y.getCount()) 
		    	  {
			          SecondEvent tmp = (SecondEvent) records.elementAt(j-1);
			          records.setElementAt(records.elementAt(j), j-1);
			          records.setElementAt(tmp, j);
		    	  }
		      }
	}

	/**
	 * 
	 * @param records
	 * @param date
	 * @param count
	 */
	private void updateSeconds(Vector records, Date date, int count) {
		SecondEvent current = new SecondEvent(count, date);
		// if there is still less than a 20 , add the record 
		if (records.size() < 20) {
			records.add(current);
		} else {
			// get the least count , if we add one that is  <= to the min  ... replace
			SecondEvent minCount = (SecondEvent) Collections.min(records,comparator);
			if (current.getCount() > minCount.getCount()) {
				records.remove(minCount);
				records.add(current);
			}else if (minCount.equals(current)) {
				records.add(current);
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			CopsUSSDSecondsConverter s = new CopsUSSDSecondsConverter();
			File[] input = new File[6];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_888_2007041010.log");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_888_2007041110.log");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_888_2007041210.log");
			input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_999_2007041010.log");
			input[4]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_999_2007041110.log");
			input[5]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_999_2007041210.log");
			s.convert(input,"USSD");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




