package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class UssdScTopSecConverter extends AbstractTextConverter{
	
	private Logger logger;
	
	private EventsComparator comparator = new EventsComparator();

	public UssdScTopSecConverter() {
	}

	/**
	 * Converting the input file to comma seperated file.
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("UssdScTopSecConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "UssdScTopSecFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			boolean firstTime = true;
			String lastDateHourString = "";
			String dateHourString = "";
			String line;
			String previousLine = "";
			String outputLine;
			Date dateTime=null;
			Date oldDateTime=null;
			int count = 0;
			String shortCode = null;
			Vector records = new Vector();
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("UssdScTopSecConverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
						
				while (inputStream.ready()) {
					line = inputStream.readLine();
					
					if (line.equals("")) {
						continue;
					}else{
						String lineDateString = "";
						if(line.length() > 23){
							//  23 bits because the date in that format: "2007-12-09 11:00:00.403"
							lineDateString = line.substring(0,23);
						}else{
							lineDateString = line;
						}
						
						if(this.CheckNewLine(lineDateString)){
							
							if (line.contains("Request:")&&line.contains("[op:1]")) {
								
							
								dateHourString = lineDateString.substring(0,13).trim();
								
								if(firstTime || (!firstTime && !dateHourString.equals(lastDateHourString)) ){
									if(!firstTime && !dateHourString.equals(lastDateHourString)){
										
										// Write the previous top 10 records data per second
										updateSeconds(records,oldDateTime,count);
										count=0;
										this.sort(records) ;
										for (int j = 0;(j < records.size() && j<10); j++) 
										{
											SecondEvent record=(SecondEvent)records.get(j);
											outputLine = Utils.convertToDateString(record.getDate(), Utils.defaultFormat)+","+record.getCount()+","+shortCode;
											//System.out.println(outputLine);
											outputStream.write(outputLine);
											outputStream.newLine();
										}
										
									}			
									// Initialize the checking parameters
									firstTime = false;
									lastDateHourString = dateHourString;
									records = new Vector();
									String[] tokens = line.split("-");
									shortCode = tokens[3];
									oldDateTime=null;
								}
								
								//if (line.endsWith("[op:1]")) {			
									String dateTimeString = line.substring(0,19).trim();
									dateTime = Utils.convertToDate(dateTimeString, "yyyy-MM-dd HH:mm:ss");
									if(oldDateTime!=null&&!oldDateTime.equals(dateTime)) {
										updateSeconds(records,oldDateTime,count);
										count=0;
									}
									oldDateTime=dateTime;
									count++;
								//}
							}
							// If new line initialize the previousLine
							previousLine = line;	
						}else{
							previousLine += line;
						}
						
					}
						
				}
				inputStream.close();
			}	
			/*if (previousLine.endsWith("[op:1]")) {			
				String dateTimeString = previousLine.substring(0,19).trim();
				dateTime = Utils.convertToDate(dateTimeString, "yyyy-MM-dd HH:mm:ss");
				if(oldDateTime!=null&&!oldDateTime.equals(dateTime)) {
					updateSeconds(records,oldDateTime,count);
					count=0;
				}
				oldDateTime=dateTime;
				count++;
			}*/
			// Write the previous top 10 records data per second for last hour
			updateSeconds(records,oldDateTime,count);
			this.sort(records) ;
			for (int j = 0; (j < records.size() && j<10) ; j++) 
			{
				SecondEvent record=(SecondEvent)records.get(j);
				try{
				outputLine = Utils.convertToDateString(record.getDate(), Utils.defaultFormat)+","+record.getCount()+","+shortCode;
				//System.out.println(outputLine);
				outputStream.write(outputLine);
				outputStream.newLine();
				}catch(Exception e){}
			}
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("UssdScTopSecConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("UssdScTopSecConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("UssdScTopSecConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("UssdScTopSecConverter.convert() - finished converting input files successfully ");
		return outputFiles;
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
		if (records.size() < 10) {
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
	
	/**
	 * Checks if a line is new or not
	 * 
	 * @param line - the line to be checked
	 * @return boolean - whether or not it is new
	 */
	private boolean CheckNewLine(String lineDateString){
		return lineDateString.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d");
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			UssdScTopSecConverter s = new UssdScTopSecConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120911.log");
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120912.log");
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_868_2007121614.log");
			s.convert(input,"ussd_sc");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
