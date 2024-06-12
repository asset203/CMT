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
import java.util.HashMap;

import java.util.Map;

import com.itworx.vaspp.datacollection.util.Utils;
import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class UssdGwTransactionsConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String, Entry> numOfResReqPerShortCode  = new HashMap<String, Entry>() ;
	private Map<String,String> msisdns =  new HashMap<String, String>() ; 

	public UssdGwTransactionsConverter() {

	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {

		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside UssdGwTransactions convert - started converting input files");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());
	
		BufferedReader inputStream = null;
		BufferedWriter outputStream;

		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("UssdGwTransactionsConverter.convert() - converting file "
								+ inputFiles[i].getName());

				//inputStream = new BufferedReader(new FileReader(inputFiles[i]));
                inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
                System.out.println("File [" + i +"]");
				String line;
				numOfResReqPerShortCode.clear() ;
				String date = "";
				while (inputStream.ready()) {

					line = inputStream.readLine();
					String shortCode = null;
					String msisdn=null;
					
					if (line.contains("SC:")) {
						try
						{
						  date = getDate(line);
						}
						catch(ParseException exc) { logger.error(exc) ; continue ;}
						
						String[] lineParts=line.split(",");
						if(lineParts==null||lineParts.length<4)
							continue;
						//shortCode = line.split(",")[3].split(":")[1];
						shortCode = lineParts[3].split(":")[1];
						msisdn=lineParts[2].split("MSISDN:")[1];
						if (shortCode.matches("[0-9]*")) {

							// Requests Per Short Code
							if (line.contains("SC")
									&& line
											.contains("New Mobile Initiated Dialogue")) {
								//msisdns.put(line.split(",")[2].split("MSISDN:")[1],date) ;
								msisdns.put(msisdn,date) ;
								if (numOfResReqPerShortCode
										.containsKey(date+","+shortCode) == true) {
									Entry e = (Entry) numOfResReqPerShortCode
											.get(date+","+shortCode);
									int newNumRequests = (((Entry) numOfResReqPerShortCode
											.get(date+","+shortCode))
											.getNumOfRequestsPerShortCode() != null) ? (((Entry) numOfResReqPerShortCode
											.get(date+","+shortCode))
											.getNumOfRequestsPerShortCode()
											.intValue() + 1)
											: new Integer(1);
									e.setNumOfRequestsPerShortCode(new Integer(
											newNumRequests));
									e.setDate(date);
								} else {
									numOfResReqPerShortCode.put(date+","+shortCode,
											new Entry(date,
													new Integer(1),
													new Integer(0)));
								}
							}
						
							// Rspones Per Short Code
							else if( line.contains("SC")
									&& line.contains("Close ussd dialogue."))
							{
								//if (msisdns.containsKey(line.split(",")[2].split("MSISDN:")[1])) {
								if (msisdns.containsKey(msisdn)) {
									//msisdns.remove(line.split(",")[2].split("MSISDN:")[1]);
									msisdns.remove(msisdn);
								if (numOfResReqPerShortCode
										.containsKey(date+","+shortCode) == true) {
									Entry e = (Entry) numOfResReqPerShortCode
											.get(date+","+shortCode);
									int newNumResponses = (((Entry) numOfResReqPerShortCode
											.get(date+","+shortCode))
											.getNumOfResponsesPerShortCode() != null) ? (((Entry) numOfResReqPerShortCode
											.get(date+","+shortCode))
											.getNumOfResponsesPerShortCode()
											.intValue() + 1)
											: new Integer(1);
									e
											.setNumOfResponsesPerShortCode(new Integer(
													newNumResponses));
									e.setDate(date);

								} else {
									numOfResReqPerShortCode.put(date+","+shortCode,
											new Entry(date,
													new Integer(0),
													new Integer(1)));
								}

							}}
						}
					} else {
						continue;
					}
				}
				inputStream.close();
				generateLines(outputStream);
				
			}

			outputStream.close();
			outputFiles[0] = outputFile;

		} catch (FileNotFoundException e) {
			logger
					.error("USSDConnectorsTransactionsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDConnectorsTransactionsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDConnectorsTransactionsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	/*****************************************************
	 * generate the lines in the file
	 * 
	 * @param outputStream
	 * @throws ApplicationException 
	 *****************************************************/
	private void generateLines(BufferedWriter outputStream) throws ApplicationException {

		for (Map.Entry<String, Entry> entry : numOfResReqPerShortCode
				.entrySet()) {

			String outLineString = entry.getValue().getDate()
					+ ","
					+ entry.getKey().split(",")[1]
					+ ","
					+ entry.getValue().getNumOfResponsesPerShortCode()
							.intValue()
					+ ","
					+ entry.getValue().getNumOfRequestsPerShortCode()
							.intValue() + "";
			try {
				//System.out.println(outLineString);
				outputStream.write(outLineString);
				outputStream.newLine();
			} catch (IOException exc) {
				logger.debug("Error while writing to outputStream" + exc);
				logger
				.error("USSDConnectorsTransactionsConverter.convert() - Input file not found "
						+ exc);
		         throw new ApplicationException(exc);
			}
		}

	}

	/**
	 * get date
	 * 
	 * @param line
	 *            - the line to extract date
	 * @throws ParseException 
	 */
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.mss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		if (line != null)
			tokens = line.split(",");
		    //System.out.println("Tokens[0]" + tokens[0] );
			date = inDateFormat.parse(tokens[0]);
		

		dateString = outDateFormat.format(date);

		return dateString;

	}

	private class Entry {
		Integer numOfRequestsPerShortCode;
		Integer numOfResponsesPerShortCode;
		// String shortCode = ;
		String date;

		public Entry(String date, Integer numOfRequestsPerShortCode,
				Integer numOfResponsesPerShortCode) {
			this.date = date;
			this.numOfRequestsPerShortCode = numOfRequestsPerShortCode;
			this.numOfResponsesPerShortCode = numOfResponsesPerShortCode;
		}

		public Integer getNumOfRequestsPerShortCode() {
			return numOfRequestsPerShortCode;
		}

		public void setNumOfRequestsPerShortCode(
				Integer numOfRequestsPerShortCode) {
			this.numOfRequestsPerShortCode = numOfRequestsPerShortCode;
		}

		public Integer getNumOfResponsesPerShortCode() {
			return numOfResponsesPerShortCode;
		}

		public void setNumOfResponsesPerShortCode(
				Integer numOfResponsesPerShortCode) {
			this.numOfResponsesPerShortCode = numOfResponsesPerShortCode;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			UssdGwTransactionsConverter s = new UssdGwTransactionsConverter();
			File[] input = new File[1];
			input[0]= new File("D:\\build\\VASPortal\\DataCollection\\trace20100227.log") ;
//			input[1] = new File("D:\\Deployment WorkSpace\\DataCollection\\trace2010011500.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
