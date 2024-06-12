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
import com.itworx.vaspp.datacollection.persistenceobjects.INAPInterfaceQualityData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class INAPInterfaceQualityConverter extends AbstractTextConverter{
	
	private Logger logger;
	
	private CounterComparator comparator = new CounterComparator();

	public INAPInterfaceQualityConverter() {
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
		logger.debug("INAPInterfaceQualityConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "INAPInterfaceQualityFile");
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
			String sdpId = "";
			Hashtable hoursTable = new Hashtable();
			Hashtable topsTable = new Hashtable();
			
			String[] tokens; 
			String[] checkingTokens;
			String[] counterTokens;
			
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("INAPInterfaceQualityConverter.convert() - converting file "
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
						this.calculateTopData(topsTable, hoursTable, lastDateHourString);
						topsTable.clear();						
					}
					// Initialize the checking parameters
					firstTime = false;
					lastDateHourString = dateHourString;
				}
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					
					if (line.equals("")) {
						continue;
					}else if(line.contains("<moid>CcnCounters=InapSdf-SDP")){	
						// the line may be like that "<moid>CcnCounters=InapSdf-SDP-13-Total-Requests, Source = _SYSTEM</moid>"
						if(line.indexOf("<moid>")<7)
							System.out.println("file "+fileName+" error "+line);
						tokens = line.substring(line.indexOf("<moid>")+6, line.indexOf("</moid>")).split(",");
						checkingTokens = tokens[1].split("=");
						if(checkingTokens[1].trim().equals("_SYSTEM")){
							//System.out.println(checkingTokens[1].trim());
							counterTokens = tokens[0].split("-");
							sdpId = counterTokens[2];
							previousCounter = counterTokens[3]+"-"+counterTokens[4];
							//System.out.println("sdpId ="+sdpId+", previousCounter ="+previousCounter);
							systemSource = true;
						}
					}else if(line.contains("<r>") && systemSource){
						// getting the total values
						Hashtable sdpTable = (Hashtable)hoursTable.get(dateHourString);
						INAPInterfaceQualityData fileData;
						TopCounter topCounter;
						if(sdpTable == null){
							sdpTable = new Hashtable();
							fileData = new INAPInterfaceQualityData();
							fileData.setSdpId(sdpId);
							fileData.setDateTime(dateHour);
							sdpTable.put(sdpId, fileData);
							hoursTable.put(dateHourString, sdpTable);
						}else{
							fileData = (INAPInterfaceQualityData)sdpTable.get(sdpId);
							if(fileData == null){
								fileData = new INAPInterfaceQualityData();
								fileData.setSdpId(sdpId);
								fileData.setDateTime(dateHour);
								sdpTable.put(sdpId, fileData);
							}
						}
						// getting the tops values
						Hashtable minsTable = (Hashtable) topsTable.get(sdpId);
						if(minsTable == null){
							minsTable = new Hashtable();
							topCounter = new TopCounter();
							topCounter.setSdpId(sdpId);
							minsTable.put(minString, topCounter);
							topsTable.put(sdpId, minsTable);
						}else{
							topCounter = (TopCounter) minsTable.get(minString);
							if(topCounter == null){
								topCounter = new TopCounter();
								topCounter.setSdpId(sdpId);
								minsTable.put(minString, topCounter);
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
			this.calculateTopData(topsTable, hoursTable, lastDateHourString);
			
			// Write the output file
			this.writeOutputData(hoursTable, outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("INAPInterfaceQualityConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("INAPInterfaceQualityConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("INAPInterfaceQualityConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("INAPInterfaceQualityConverter.convert() - finished converting input files successfully ");
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
	private void sumCounters(String previousCounter,String counterValue, INAPInterfaceQualityData fileData, TopCounter topCounter) {
		long counterLong = Long.parseLong(counterValue);
		//System.out.println("counterLong = "+counterLong);
		if (previousCounter.equals("Total-Requests")) {
			fileData.setTotalOfTotalRequests(fileData.getTotalOfTotalRequests() + counterLong);
			topCounter.setMaxValue(counterLong);			
		}else if (previousCounter.equals("Failed-Requests")) {
			fileData.setTotalOfFailedRequests(fileData.getTotalOfFailedRequests() + counterLong);
			topCounter.setCorrespondingValue1(counterLong);
		}else if (previousCounter.equals("Total-Results")) {
			fileData.setTotalOfTotalResults(fileData.getTotalOfTotalResults() + counterLong);
			topCounter.setCorrespondingValue2(counterLong);
		}else if (previousCounter.equals("Failed-Results")) {
			fileData.setTotalOfFailedResults(fileData.getTotalOfFailedResults() + counterLong);
			topCounter.setCorrespondingValue3(counterLong);
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
		Iterator sdpIterator;
		Hashtable sdpTable;
		INAPInterfaceQualityData fileData;
		String outputLine;
		
		while(hoursIterator.hasNext()){
			sdpTable = (Hashtable)hoursIterator.next();
			sdpIterator = sdpTable.values().iterator();
			while(sdpIterator.hasNext()){
				fileData = (INAPInterfaceQualityData)sdpIterator.next();
				Calendar cal = Calendar.getInstance();

				Date date = fileData.getDateTime();
				cal.setTime(date);
				cal.add(Calendar.HOUR, 2);
				String dateHour = Utils.convertToDateString(cal.getTime(), Utils.defaultFormat);
				outputLine = /*Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)*/dateHour+","+fileData.getTotalOfTotalRequests()+","+fileData.getTotalOfFailedRequests()+","+fileData.getTotalOfTotalResults()+","+fileData.getTotalOfFailedResults()+","+fileData.getTopOfTotalRequests()+","+fileData.getTopOfFailedRequests()+","+fileData.getTopOfTotalResults()+","+fileData.getTopOfFailedResults()+","+fileData.getSdpId();
				//System.out.println(outputLine);
				outputStream.write(outputLine);
				outputStream.newLine();				
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
	private void calculateTopData(Hashtable topsTable, Hashtable hoursTable, String lastDateHourString){
		//System.out.println("lastDateHourString : "+lastDateHourString);
		Iterator sdpIterator = topsTable.values().iterator();
		Hashtable minsTable;
		Hashtable sdpTable = (Hashtable)hoursTable.get(lastDateHourString);
		TopCounter topCounter;
		INAPInterfaceQualityData fileData;
		String sdpId;
		
		while(sdpIterator.hasNext()){
			minsTable = (Hashtable)sdpIterator.next();
			topCounter = (TopCounter) Collections.max(minsTable.values(), comparator);
			sdpId = topCounter.getSdpId(); 
			fileData = (INAPInterfaceQualityData) sdpTable.get(sdpId);
			fileData.setTopOfTotalRequests((double)topCounter.getMaxValue() / 300);
			fileData.setTopOfFailedRequests((double)topCounter.getCorrespondingValue1() / 300);
			fileData.setTopOfTotalResults((double)topCounter.getCorrespondingValue2() / 300);
			fileData.setTopOfFailedResults((double)topCounter.getCorrespondingValue3() / 300);
			/*if(sdpId.equals("6")){
				System.out.println("inside 6");
				System.out.println("topCounter.getMaxValue() = "+topCounter.getMaxValue()+" , top = "+(double)topCounter.getMaxValue() / 300);
				System.out.println("topCounter.getCorrespondingValue1() = "+topCounter.getCorrespondingValue1()+" , top = "+(double)topCounter.getCorrespondingValue1() / 300);
				System.out.println("topCounter.getCorrespondingValue2() = "+topCounter.getCorrespondingValue2()+" , top = "+(double)topCounter.getCorrespondingValue2() / 300);
				System.out.println("topCounter.getCorrespondingValue3() = "+topCounter.getCorrespondingValue3()+" , top = "+(double)topCounter.getCorrespondingValue3() / 300);
			}*/
		}
		
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\DataCollection");
			INAPInterfaceQualityConverter s = new INAPInterfaceQualityConverter();
			File[] input = new File[7];
			input[0]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0000-0005_jambala_CcnCounters");
			input[1]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0045-0050_jambala_CcnCounters");
			input[2]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0050-0055_jambala_CcnCounters");
			input[3]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0055-0100_jambala_CcnCounters");
			input[4]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0305-0310_jambala_CcnCounters");
			input[5]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0415-0420_jambala_CcnCounters");
			input[6]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20130623.0430-0435_jambala_CcnCounters");
			/*input[7]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0035-0040_jambala_CcnCounters");
			input[8]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0040-0045_jambala_CcnCounters");
			input[9]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0045-0050_jambala_CcnCounters");
			input[10]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0050-0055_jambala_CcnCounters");
			input[11]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0055-0100_jambala_CcnCounters");
			input[12]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0100-0105_jambala_CcnCounters");
			input[13]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0105-0110_jambala_CcnCounters");
			input[14]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0110-0115_jambala_CcnCounters");
			input[15]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0115-0120_jambala_CcnCounters");
			input[16]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0120-0125_jambala_CcnCounters");
			input[17]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0125-0130_jambala_CcnCounters");
			input[18]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0130-0135_jambala_CcnCounters");
			input[19]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0135-0140_jambala_CcnCounters");
			input[20]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0140-0145_jambala_CcnCounters");
			input[21]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0145-0150_jambala_CcnCounters");
			input[22]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0150-0155_jambala_CcnCounters");
			input[23]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\20-06-2013\\New Build\\ccn\\A20070605.0155-0200_jambala_CcnCounters");*/
			
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
