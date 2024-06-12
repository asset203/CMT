package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.DiameterInterfaceQualityData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class DiameterInterfaceQualityCoverter extends AbstractTextConverter{
	
	private Logger logger;

	private CounterComparator comparator = new CounterComparator();

	public DiameterInterfaceQualityCoverter() {
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
		logger.debug("DiameterInterfaceQualityCoverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "DiameterInterfaceQualityFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			boolean firstTime = true;
			boolean systemSource = false;
			String lastDateHourString = "";
			String dateHourString = "";
			String minString = "";
			Date dateHour = new Date();
			String line;
			String fileName;
			String previousCounter = "";
			String counterValue = "";
			String connectionId = "";
			String applicationName = "";
			Hashtable hoursTable = new Hashtable();
			Hashtable topsConnectionsTable = new Hashtable();
			
			String[] tokens; 
			String[] checkingTokens;
			String[] counterTokens;
			
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("DiameterInterfaceQualityCoverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				fileName = inputFiles[i].getName();
				// the file name in that format: "CCN_1206295415395_1_A20070605.0000-0005_jambala_CcnCounters" 
				// to "CCN_1206295415395_1_A20070605.0055-0100_jambala_CcnCounters" for one hour
				dateHourString = fileName.substring(fileName.indexOf("A")+1,fileName.indexOf("A")+12).trim();
				minString = fileName.substring(fileName.indexOf("A")+12,fileName.indexOf("A")+14).trim();
				dateHour = Utils.convertToDate(dateHourString, "yyyyMMdd.HH");
				// write the following for testing 
				/*if(!firstTime){
					Iterator topIterator = topsTable.values().iterator();
					Iterator minIterator;
					Hashtable minsTable;
					TopCounter topCounter;
					String outputLine;
					
					while(topIterator.hasNext()){
						minsTable = (Hashtable)topIterator.next();
						minIterator = minsTable.values().iterator();
						while(minIterator.hasNext()){
							topCounter = (TopCounter)minIterator.next();
							outputLine = topCounter.getMaxValue()+","+topCounter.getCorrespondingValue1()+","+topCounter.getCorrespondingValue2()+","+topCounter.getCorrespondingValue3()+","+topCounter.getSdpId();
							System.out.println(outputLine);			
						}
					}
				}
				System.out.println(dateHourString + "  ----  "+minString);*/
				// Check if that is new hour or not
				if(firstTime || (!firstTime && !dateHourString.equals(lastDateHourString)) ){
					if(!firstTime && !dateHourString.equals(lastDateHourString)){
						// calculate the tops of the counters
						this.calculateTopData(topsConnectionsTable, hoursTable, lastDateHourString);
						topsConnectionsTable.clear();						
					}
					// Initialize the checking parameters
					firstTime = false;
					lastDateHourString = dateHourString;
				}
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					
					if (line.equals("")) {
						continue;
					}else if(line.contains("<moid>CcnCounters=DiamAccess-AppReqReceived") || line.contains("<moid>CcnCounters=DiamAccess-AppReqAnswered")){
						// the line may be like that "<moid>CcnCounters=DiamAccess-AppReqReceived-172.18.171.8-SCAP, Source = _SYSTEM</moid>"
						tokens = line.substring(line.indexOf("<moid>")+6, line.indexOf("</moid>")).split(",");
						checkingTokens = tokens[1].split("=");
						if(checkingTokens[1].trim().equals("_SYSTEM")){
							counterTokens = tokens[0].split("-");
							connectionId = counterTokens[2];
							applicationName = counterTokens[3];
							previousCounter = counterTokens[0]+"-"+counterTokens[1];
							systemSource = true;
						}
					}else if(line.contains("<r>") && systemSource){
						// getting the total values
						Hashtable connectionsTable = (Hashtable)hoursTable.get(dateHourString);
						Hashtable applicationsTable;
						DiameterInterfaceQualityData fileData;
						if(connectionsTable == null){						
							connectionsTable = new Hashtable();							
							applicationsTable = new Hashtable();
							fileData = new DiameterInterfaceQualityData();
							fileData.setConnectionId(connectionId);
							fileData.setApplicationName(applicationName);
							fileData.setDateTime(dateHour);
							applicationsTable.put(applicationName, fileData);
							connectionsTable.put(connectionId, applicationsTable);
							hoursTable.put(dateHourString, connectionsTable);
						}else{
							applicationsTable = (Hashtable)connectionsTable.get(connectionId);
							if(applicationsTable == null){
								applicationsTable = new Hashtable();
								fileData = new DiameterInterfaceQualityData();
								fileData.setConnectionId(connectionId);
								fileData.setApplicationName(applicationName);
								fileData.setDateTime(dateHour);
								applicationsTable.put(applicationName, fileData);
								connectionsTable.put(connectionId, applicationsTable);
							}else{
								fileData = (DiameterInterfaceQualityData)applicationsTable.get(applicationName);
								if(fileData == null){
									fileData = new DiameterInterfaceQualityData();
									fileData.setConnectionId(connectionId);
									fileData.setApplicationName(applicationName);
									fileData.setDateTime(dateHour);
									applicationsTable.put(applicationName, fileData);
								}
							}
						}
						// getting the tops values						
						Hashtable topsApplicationsTable = (Hashtable) topsConnectionsTable.get(connectionId);
						Hashtable minsTable;
						TopCounter topCounter;
						if(topsApplicationsTable == null){
							topsApplicationsTable = new Hashtable();
							minsTable = new Hashtable();
							topCounter = new TopCounter();
							topCounter.setConnectionId(connectionId);
							topCounter.setApplicationName(applicationName);
							minsTable.put(minString, topCounter);
							topsApplicationsTable.put(applicationName, minsTable);
							topsConnectionsTable.put(connectionId, topsApplicationsTable);
						}else{
							minsTable = (Hashtable) topsApplicationsTable.get(applicationName);
							if(minsTable == null){
								minsTable = new Hashtable();
								topCounter = new TopCounter();
								topCounter.setConnectionId(connectionId);
								topCounter.setApplicationName(applicationName);
								minsTable.put(minString, topCounter);
								topsApplicationsTable.put(applicationName, minsTable);
							}else{
								topCounter = (TopCounter) minsTable.get(minString);
								if(topCounter == null){
									topCounter = new TopCounter();
									topCounter.setConnectionId(connectionId);
									topCounter.setApplicationName(applicationName);
									minsTable.put(minString, topCounter);
								}							
							}						
						}
						
						// putting the write value
						counterValue = line.substring(line.indexOf("<r>")+3,line.indexOf("</r>")).trim();
						this.sumCounters(previousCounter, counterValue, fileData, topCounter);
						systemSource = false;
					}
						
				}
				inputStream.close();
			}	
			// write the following for testing 
			/*if(!firstTime){
				Iterator topIterator = topsTable.values().iterator();
				Iterator minIterator;
				Hashtable minsTable;
				TopCounter topCounter;
				String outputLine;
				
				while(topIterator.hasNext()){
					minsTable = (Hashtable)topIterator.next();
					minIterator = minsTable.values().iterator();
					while(minIterator.hasNext()){
						topCounter = (TopCounter)minIterator.next();
						outputLine = topCounter.getMaxValue()+","+topCounter.getCorrespondingValue1()+","+topCounter.getCorrespondingValue2()+","+topCounter.getCorrespondingValue3()+","+topCounter.getSdpId();
						System.out.println(outputLine);			
					}
				}
			}*/
			// calculate the tops of the counters for the last hour
			this.calculateTopData(topsConnectionsTable, hoursTable, lastDateHourString);
			
			// Write the output file
			this.writeOutputData(hoursTable, outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("DiameterInterfaceQualityCoverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("DiameterInterfaceQualityCoverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("DiameterInterfaceQualityCoverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("DiameterInterfaceQualityCoverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}
	
	/**
	 * Proccessing the data to calculate the Totals, and puuting the values in topCounter records to enable processing them later
	 * and extract the tops from them.
	 * 
	 * @param previousCounter -
	 *            the indicator of that counter
	 * @param counterValue -
	 *            the counter value
	 * @param fileData -
	 *            the record that contains the current data
	 * @param topCounter -
	 *            the record that contains the current values to extract the tops from them later
	 */
	private void sumCounters(String previousCounter,String counterValue, DiameterInterfaceQualityData fileData, TopCounter topCounter) {
		long counterLong = Long.parseLong(counterValue);
		//System.out.println("counterLong = "+counterLong);
		if (previousCounter.equals("CcnCounters=DiamAccess-AppReqReceived")) {
			fileData.setTotalReceived(fileData.getTotalReceived() + counterLong);
			topCounter.setMaxValue(counterLong);			
		}else if (previousCounter.equals("CcnCounters=DiamAccess-AppReqAnswered")) {
			fileData.setTotalAnswered(fileData.getTotalAnswered() + counterLong);
			topCounter.setCorrespondingValue1(counterLong);
		}else{
			//System.out.println("something else:"+previousCounter);
		}
	}
	
	/**
	 * Write the data on the output file.
	 * 
	 * @param hoursTable -
	 *            the table that contains the sdps table per hour
	 * @param outputStream -
	 *            the output file
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private void writeOutputData(Hashtable hoursTable, BufferedWriter outputStream)
		throws IOException,InputException {
		
		Iterator hoursIterator = hoursTable.values().iterator();
		Iterator connectionsIterator;
		Iterator applicationsIterator;
		Hashtable connectionsTable;
		Hashtable applicationsTable;
		DiameterInterfaceQualityData fileData;
		String outputLine;
		
		while(hoursIterator.hasNext()){
			connectionsTable = (Hashtable)hoursIterator.next();
			connectionsIterator = connectionsTable.values().iterator();			
			while(connectionsIterator.hasNext()){
				applicationsTable = (Hashtable)connectionsIterator.next();
				applicationsIterator = applicationsTable.values().iterator();
				while(applicationsIterator.hasNext()){
					fileData = (DiameterInterfaceQualityData)applicationsIterator.next();
					Calendar cal = Calendar.getInstance();

					Date date = fileData.getDateTime();
					cal.setTime(date);
					cal.add(Calendar.HOUR, 2);
					String dateHour = Utils.convertToDateString(cal.getTime(), Utils.defaultFormat);
					outputLine = /*Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)*/dateHour+","+fileData.getTotalReceived()+","+fileData.getTotalAnswered()+","+fileData.getTopReceived()+","+fileData.getTopAnswered()+","+fileData.getConnectionId()+","+fileData.getApplicationName();
					//System.out.println(outputLine);
					outputStream.write(outputLine);
					outputStream.newLine();				
				}
			}
		}
	}
	
	/**
	 * Extract the tops values and calculate it per seconds then putting them in its record.
	 * 
	 * @param topsTable -
	 *            the table that contains the values in minutes to take the max of them
	 * @param hoursTable -
	 *            the table that cotains sdps table per each hour
	 * @param lastDateHourString -
	 *            the current hour that index the sdps table
	 */
	private void calculateTopData(Hashtable topsConnectionsTable, Hashtable hoursTable, String lastDateHourString){
		//System.out.println("lastDateHourString : "+lastDateHourString);
		Iterator connectionsIterator = topsConnectionsTable.values().iterator();
		Iterator applicationsIterator;
		Hashtable topsApplicationsTable;
		Hashtable minsTable;
		Hashtable connecHashtable = (Hashtable)hoursTable.get(lastDateHourString);
		Hashtable appHashtable;
		TopCounter topCounter;
		DiameterInterfaceQualityData fileData;
		String connectionId;
		String applicationName;
		
		while(connectionsIterator.hasNext()){
			topsApplicationsTable = (Hashtable)connectionsIterator.next();
			applicationsIterator = topsApplicationsTable.values().iterator();
			while(applicationsIterator.hasNext()){
				minsTable = (Hashtable)applicationsIterator.next();
				
				topCounter = (TopCounter) Collections.max(minsTable.values(), comparator);
				connectionId = topCounter.getConnectionId(); 
				applicationName = topCounter.getApplicationName(); 
				appHashtable = (Hashtable) connecHashtable.get(connectionId);
				fileData = (DiameterInterfaceQualityData) appHashtable.get(applicationName);
				fileData.setTopReceived((double)topCounter.getMaxValue() / 300);
				fileData.setTopAnswered((double)topCounter.getCorrespondingValue1() / 300);
				
			}
		}
		
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			DiameterInterfaceQualityCoverter s = new DiameterInterfaceQualityCoverter();
			File[] input = new File[24];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0000-0005_jambala_CcnCounters");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0005-0010_jambala_CcnCounters");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0010-0015_jambala_CcnCounters");
			input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0015-0020_jambala_CcnCounters");
			input[4]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0020-0025_jambala_CcnCounters");
			input[5]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0025-0030_jambala_CcnCounters");
			input[6]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0030-0035_jambala_CcnCounters");
			input[7]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0035-0040_jambala_CcnCounters");
			input[8]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0040-0045_jambala_CcnCounters");
			input[9]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0045-0050_jambala_CcnCounters");
			input[10]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0050-0055_jambala_CcnCounters");
			input[11]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0055-0100_jambala_CcnCounters");
			input[12]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0100-0105_jambala_CcnCounters");
			input[13]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0105-0110_jambala_CcnCounters");
			input[14]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0110-0115_jambala_CcnCounters");
			input[15]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0115-0120_jambala_CcnCounters");
			input[16]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0120-0125_jambala_CcnCounters");
			input[17]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0125-0130_jambala_CcnCounters");
			input[18]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0130-0135_jambala_CcnCounters");
			input[19]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0135-0140_jambala_CcnCounters");
			input[20]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0140-0145_jambala_CcnCounters");
			input[21]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0145-0150_jambala_CcnCounters");
			input[22]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0150-0155_jambala_CcnCounters");
			input[23]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0155-0200_jambala_CcnCounters");
			
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
