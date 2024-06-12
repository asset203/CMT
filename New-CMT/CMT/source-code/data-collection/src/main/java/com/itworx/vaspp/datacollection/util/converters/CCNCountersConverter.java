package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.CCNCountersData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CCNCountersConverter extends AbstractTextConverter{

	private Logger logger;

	public CCNCountersConverter() {
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
		logger.debug("CCNCountersConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "CCNCountersFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			boolean systemSource = false;
			String dateHourString = "";
			Date dateHour = new Date();
			String line;
			String fileName;
			String previousCounter = "";
			String counterValue = "";
			Hashtable hoursTable = new Hashtable();

			String[] tokens;
			String[] checkingTokens;
			String[] counterTokens;


			for (int i = 0; i < inputFiles.length; i++)
			{
				logger.debug("CCNCountersConverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				fileName = inputFiles[i].getName();
				// the file name in that format: "CCN_1206295415395_1_A20070605.0000-0005_jambala_CcnCounters"
				// to "CCN_1206295415395_1_A20070605.0055-0100_jambala_CcnCounters" for one hour
				dateHourString = fileName.substring(fileName.indexOf("A")+1,fileName.indexOf("A")+12).trim();
				dateHour = Utils.convertToDate(dateHourString, "yyyyMMdd.HH");

				while (inputStream.ready()) {
					line = inputStream.readLine();

					if (line.equals("")) {
						continue;
					}else if(line.contains("_SYSTEM")){
						// the line may be like that "<moid>CcnCounters=RelayEvent-DirectDebit-Successful, Source = _SYSTEM</moid>"
						tokens = line.substring(line.indexOf("<moid>")+6, line.indexOf("</moid>")).split(",");
						checkingTokens = tokens[1].split("=");
						if(checkingTokens[1].trim().equals("_SYSTEM")){
							counterTokens = tokens[0].split("=");
							previousCounter = counterTokens[1].trim();
							systemSource = true;
						}
					}else if(line.contains("<r>") && systemSource){
						// getting the total values
						CCNCountersData fileData = (CCNCountersData)hoursTable.get(dateHourString);
						if(fileData == null){
							fileData = new CCNCountersData();
							fileData.setDateTime(dateHour);
							hoursTable.put(dateHourString, fileData);
						}

						// putting the write value
						counterValue = line.substring(line.indexOf("<r>")+3,line.indexOf("</r>")).trim();
						this.sumCounters(previousCounter, counterValue, fileData);
						systemSource = false;
					}

				}
				inputStream.close();
			}

			// Write the output file
			this.writeOutputData(hoursTable, outputStream);

			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CCNCountersConverter.convert() - "
					+ inputFiles[0].getName() + " converted");

		} catch (FileNotFoundException e) {
			logger.error("CCNCountersConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CCNCountersConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("CCNCountersConverter.convert() - finished converting input files successfully ");
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
	private void sumCounters(String previousCounter,String counterValue, CCNCountersData fileData) {
		long counterLong = Long.parseLong(counterValue);
		//System.out.println("counterLong = "+counterLong);
		if (previousCounter.equals("GeneralCongestion-Overload")) {
			fileData.setTotalOfGeneralCongestionOverload(fileData.getTotalOfGeneralCongestionOverload() + counterLong);
		}else if (previousCounter.equals("RequestRejected-Overload")) {
			fileData.setTotalOfRequestRejectedOverload(fileData.getTotalOfRequestRejectedOverload() + counterLong);
		}else if (previousCounter.equals("Primary-Account-Finder-Error")) {
			fileData.setTotalOfPrimaryAccountFinderError(fileData.getTotalOfPrimaryAccountFinderError() + counterLong);
		}else if (previousCounter.equals("Secondary-Account-Finder-Error")) {
			fileData.setTotalOfSecondaryAccountFinderError(fileData.getTotalOfSecondaryAccountFinderError() + counterLong);
		}else if (previousCounter.equals("RelayContent-FirstInterrogation-Successful")) {
			fileData.setTotalOfRelayContentFirstInterrogationSuccessful(fileData.getTotalOfRelayContentFirstInterrogationSuccessful() + counterLong);
		}else if (previousCounter.equals("RelayContent-FirstInterrogation-Congestion")) {
			fileData.setTotalOfRelayContentFirstInterrogationCongestion(fileData.getTotalOfRelayContentFirstInterrogationCongestion() + counterLong);
		}else if (previousCounter.equals("RelayContent-FirstInterrogation-NoContactWithSDP")) {
			fileData.setTotalOfRelayContentFirstInterrogationNoContactWithSDP(fileData.getTotalOfRelayContentFirstInterrogationNoContactWithSDP() + counterLong);
		}else if (previousCounter.equals("RelayContent-FirstInterrogation-InternalError")) {
			fileData.setTotalOfRelayContentFirstInterrogationInternalError(fileData.getTotalOfRelayContentFirstInterrogationInternalError() + counterLong);
		}else if (previousCounter.equals("RelayContent-IntermediateInterrogation-Successful")) {
			fileData.setTotalOfRelayContentIntermediateInterrogationSuccessful(fileData.getTotalOfRelayContentIntermediateInterrogationSuccessful() + counterLong);
		}else if (previousCounter.equals("RelayContent-IntermediateInterrogation-NoContactWithSDP")) {
			fileData.setTotalOfRelayContentIntermediateInterrogationNoContactWithSDP(fileData.getTotalOfRelayContentIntermediateInterrogationNoContactWithSDP() + counterLong);
		}else if (previousCounter.equals("RelayContent-IntermediateInterrogation-Congestion")) {
			fileData.setTotalOfRelayContentIntermediateInterrogationCongestion(fileData.getTotalOfRelayContentIntermediateInterrogationCongestion() + counterLong);
		}else if (previousCounter.equals("RelayContent-IntermediateInterrogation-InternalError")) {
			fileData.setTotalOfRelayContentIntermediateInterrogationInternalError(fileData.getTotalOfRelayContentIntermediateInterrogationInternalError() + counterLong);
		}else if (previousCounter.equals("RelayContent-FinalReport-Successful")) {
			fileData.setTotalOfRelayContentFinalReportSuccessful(fileData.getTotalOfRelayContentFinalReportSuccessful() + counterLong);
		}else if (previousCounter.equals("RelayContent-FinalReport-NoContactWithSDP")) {
			fileData.setTotalOfRelayContentFinalReportNoContactWithSDP(fileData.getTotalOfRelayContentFinalReportNoContactWithSDP() + counterLong);
		}else if (previousCounter.equals("RelayContent-FinalReport-Congestion")) {
			fileData.setTotalOfRelayContentFinalReportCongestion(fileData.getTotalOfRelayContentFinalReportCongestion() + counterLong);
		}else if (previousCounter.equals("RelayContent-FinalReport-InternalError")) {
			fileData.setTotalOfRelayContentFinalReportInternalError(fileData.getTotalOfRelayContentFinalReportInternalError() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectDebit-Successful")) {
			fileData.setTotalOfRelayEventDirectDebitSuccessful(fileData.getTotalOfRelayEventDirectDebitSuccessful() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectDebit-Congestion")) {
			fileData.setTotalOfRelayEventDirectDebitCongestion(fileData.getTotalOfRelayEventDirectDebitCongestion() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectDebit-NoContactWithSDP")) {
			fileData.setTotalOfRelayEventDirectDebitNoContactWithSDP(fileData.getTotalOfRelayEventDirectDebitNoContactWithSDP() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectDebit-InternalError")) {
			fileData.setTotalOfRelayEventDirectDebitInternalError(fileData.getTotalOfRelayEventDirectDebitInternalError() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectCredit-Successful")) {
			fileData.setTotalOfRelayEventDirectCreditSuccessful(fileData.getTotalOfRelayEventDirectCreditSuccessful() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectCredit-Congestion")) {
			fileData.setTotalOfRelayEventDirectCreditCongestion(fileData.getTotalOfRelayEventDirectCreditCongestion() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectCredit-NoContactWithSDP")) {
			fileData.setTotalOfRelayEventDirectCreditNoContactWithSDP(fileData.getTotalOfRelayEventDirectCreditNoContactWithSDP() + counterLong);
		}else if (previousCounter.equals("RelayEvent-DirectCredit-InternalError")) {
			fileData.setTotalOfRelayEventDirectCreditInternalError(fileData.getTotalOfRelayEventDirectCreditInternalError() + counterLong);
		}else if (previousCounter.equals("DiamAccess-AppReqRejected-Due-To-Overload")) {
			fileData.setTotalOfDiamAccessAppReqRejectedDueToOverload(fileData.getTotalOfDiamAccessAppReqRejectedDueToOverload() + counterLong);
			fileData.setTopOfDiamAccessAppReqRejectedDueToOverload(this.getMax(fileData.getTopOfDiamAccessAppReqRejectedDueToOverload(), counterLong));
		}else if (previousCounter.equals("DiamAccess-ConnReqRejected-Due-To-Overload")) {
			fileData.setTotalOfDiamAccessConnReqRejectedDueToOverload(fileData.getTotalOfDiamAccessConnReqRejectedDueToOverload() + counterLong);
			fileData.setTopOfDiamAccessConnReqRejectedDueToOverload(this.getMax(fileData.getTopOfDiamAccessConnReqRejectedDueToOverload(), counterLong));
		}
		else if (previousCounter.equals("Account-Finder-Cache-Miss")) {
			fileData.setTotalAccountFinderCacheMiss(fileData.getTotalAccountFinderCacheMiss()+counterLong);
		}
		else if (previousCounter.equals("Account-Finder-Cache-Hit")) {
			fileData.setTotalAccountFinderCacheHit(fileData.getTotalAccountFinderCacheHit()+counterLong);
		}
		else if (previousCounter.equals("Account-Finder-Cache-Error")) {
			fileData.setTotalAccountFinderCacheError(fileData.getTotalAccountFinderCacheError()+counterLong);
		}
	}

	/**
	 * Return the max value.
	 *
	 * @param firstCounter -
	 *            the firstCounter
	 * @param secondCounter -
	 *            the secondCounter
	 * @return maxCounter -
	 * 			  the max of them
	 */
	private double getMax(double firstCounter, double secondCounter){

		double maxCounter;
		if(firstCounter >= secondCounter){
			maxCounter = firstCounter;
		}else{
			maxCounter = secondCounter;
		}
		return maxCounter;
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
		CCNCountersData fileData;
		String outputLine;

		while(hoursIterator.hasNext()){
			fileData = (CCNCountersData)hoursIterator.next();
			Calendar cal = Calendar.getInstance();

			Date date = fileData.getDateTime();
			cal.setTime(date);
			cal.add(Calendar.HOUR, 2);
			String dateHour = Utils.convertToDateString(cal.getTime(), Utils.defaultFormat);
			outputLine = /*Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)*/dateHour+","+fileData.getTotalOfGeneralCongestionOverload()+","+fileData.getTotalOfRequestRejectedOverload()+","+fileData.getTotalOfPrimaryAccountFinderError()+","+fileData.getTotalOfSecondaryAccountFinderError()+","+fileData.getTotalOfRelayContentFirstInterrogationSuccessful()+","+fileData.getTotalOfRelayContentFirstInterrogationCongestion()+","+fileData.getTotalOfRelayContentFirstInterrogationNoContactWithSDP()+","+fileData.getTotalOfRelayContentFirstInterrogationInternalError()
						+","+fileData.getTotalOfRelayContentIntermediateInterrogationSuccessful()+","+fileData.getTotalOfRelayContentIntermediateInterrogationNoContactWithSDP()+","+fileData.getTotalOfRelayContentIntermediateInterrogationCongestion()+","+fileData.getTotalOfRelayContentIntermediateInterrogationInternalError()+","+fileData.getTotalOfRelayContentFinalReportSuccessful()+","+fileData.getTotalOfRelayContentFinalReportNoContactWithSDP()+","+fileData.getTotalOfRelayContentFinalReportCongestion()+","+fileData.getTotalOfRelayContentFinalReportInternalError()
						+","+fileData.getTotalOfRelayEventDirectDebitSuccessful()+","+fileData.getTotalOfRelayEventDirectDebitCongestion()+","+fileData.getTotalOfRelayEventDirectDebitNoContactWithSDP()+","+fileData.getTotalOfRelayEventDirectDebitInternalError()+","+fileData.getTotalOfRelayEventDirectCreditSuccessful()+","+fileData.getTotalOfRelayEventDirectCreditCongestion()+","+fileData.getTotalOfRelayEventDirectCreditNoContactWithSDP()+","+fileData.getTotalOfRelayEventDirectCreditInternalError()
						+","+fileData.getTotalOfDiamAccessAppReqRejectedDueToOverload()+","+fileData.getTotalOfDiamAccessConnReqRejectedDueToOverload()
						+","+(double)(fileData.getTopOfDiamAccessAppReqRejectedDueToOverload() / 300)
						+","+(double)(fileData.getTopOfDiamAccessConnReqRejectedDueToOverload() / 300)+","+fileData.getTotalAccountFinderCacheMiss()+","+fileData.getTotalAccountFinderCacheHit()+","+fileData.getTotalAccountFinderCacheError();
			//System.out.println(outputLine);
			outputStream.write(outputLine);
			outputStream.newLine();
		}

	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\phase10\\DataCollection");
			CCNCountersConverter s = new CCNCountersConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase10\\DataCollection\\A20110512.1420-1425_jambala_CcnCounters.20110512142855");
			/*input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0005-0010_jambala_CcnCounters");
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
*/
			s.convert(input,"Database");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
