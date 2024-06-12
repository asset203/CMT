package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.Ussd868ReqResData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

/*
 * - the log file associated with this class writes data for every requested 777/868 services and 
 *  this services in some times like 868 has sub code like 868#1,868#2
 * - this class is responsible of converting 777/868 log files to retrieve
 * the following aggregated values : 
 * 1- count of requests
 * 2- count of responses
 * grouped by every date-hour , every subCode like 1,2,3 in 868
 * 
 * Sample Input : 868
 * Date phone no  -shortCode- type : subcode(#/*)bla bla [op:(1/32)]
 * 2009-02-04 08:59:59.319 [20104960743] -868- Request: 3#;602022034265211;20105996340 [op:1]
 * 2009-02-04 08:59:59.388 [20104960743] -868- Response: Your request is in progress [op:32]
 * 
 * Sample Input : 777
 * Date phone no  -shortCode- type : bla bla [op:(1/32)]
 * 2007-12-09 11:59:08.532 [20107084521] -777- Request: ;602022014932161 [op:1]
 * 2007-12-09 12:00:08.570 [20107084521] -777- Response: You do not have any pending missed calls. [op:32]
 * 
 * in 777 no sub code so in 777 we wil put subCode = ""
 *  
 * */

public class UssdScReqResWithSubCodesConverter extends AbstractTextConverter{
	
	
	private Logger logger;
	//used to hold every phone number and its request short code
	private HashMap phoneRequestSubCodes = new HashMap();
	private HashMap hoursMap = new HashMap();
	
	private String[] subCodes = null;
	
	public UssdScReqResWithSubCodesConverter() {
		subCodes = PropertyReader.get868RequestsSubCodes().split(",");
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
		logger.debug("UssdScReqResWithSubCodesConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "UssdScReqResFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			boolean firstTime = true;
			
			subCodes = PropertyReader.get868RequestsSubCodes().split(",");
			
			String lastDateHourString = "";
			String dateHourString = "";
			String line;
			String previousLine = "";
			String outputLine;

			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("UssdScReqResWithSubCodesConverter.convert() - converting file " + inputFiles[i].getName());
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
							dateHourString = lineDateString.substring(0,13).trim();
							if(firstTime || (!firstTime && !dateHourString.equals(lastDateHourString)) ){
								if(!firstTime && !dateHourString.equals(lastDateHourString)){
									//count Update Requests or Responses Counts of current Day and key
									this.countRequestsResponses(previousLine);
								}
								// Initialize the checking parameters
								firstTime = false;
								lastDateHourString = dateHourString;
							}else{
								this.countRequestsResponses(previousLine);								
							}
							previousLine = line;

						}else{
							previousLine += line;
						}
					}
				}
				inputStream.close();
			}	
			// Write the last previous record data
			this.countRequestsResponses(previousLine);

			for (Iterator iter = hoursMap.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Ussd868ReqResData val = ((Ussd868ReqResData)hoursMap.get(key));
				String subCode = (key.split(",").length == 2) ? key.split(",")[1] : "Others";
				//note : we must put ,- in the end of string to make split process performed right because if subCode is empty split will fail if no ,-
				outputLine = Utils.convertToDateString(val.getDateTime(), Utils.defaultFormat)+","+val.getNoOfRequests()+","+val.getNoOfResponses()+","+val.getShortCode()+","+subCode+",-";
				outputStream.write(outputLine);
				//System.out.println("the line :"+outputLine);
				outputStream.newLine();
			}			
			outputStream.close();
			outputFiles[0] = output;

			logger.debug("UssdScReqResWithSubCodesConverter.convert() - " + inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("UssdScReqResWithSubCodesConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("UssdScReqResWithSubCodesConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}
		logger.debug("UssdScReqResWithSubCodesConverter.convert() - finished converting input files successfully ");
		
		phoneRequestSubCodes.clear();
		phoneRequestSubCodes = null;
		
		hoursMap.clear();
		hoursMap = null;
		
		return outputFiles;
	}
	
	/**
	 * extract data.
	 * 
	 * @param inputStream -
	 *            the input file
	 * @param lines -
	 *            the arrays of lines to concatenate data
	 * @throws InputException 
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private void countRequestsResponses(String line) throws InputException {
		String phoneNo = Utils.stringBetween(line, "[", "]");
		String dateHourString = getDateHourString(line);
		String responseRegex="^.+\\[op:3?2[;\\]].*$";
		if(phoneNo != null && !phoneNo.equals(""))
		{
			if (line.contains("Request:")&&line.contains("[op:1]")) {
				String subCode = getSubCode(line);
				if(subCode==null)
					subCode="";
				String hoursMapKey = dateHourString+","+subCode;

				phoneRequestSubCodes.put(phoneNo,subCode);
				
				if(hoursMap.containsKey(hoursMapKey))
				{
					Ussd868ReqResData reqResData = (Ussd868ReqResData)hoursMap.get(dateHourString+","+subCode);
					reqResData.setNoOfRequests(reqResData.getNoOfRequests()+1);
				}
				else
				{
					Ussd868ReqResData reqResData = new Ussd868ReqResData();
					reqResData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
					String[] tokens = line.split("-");
					reqResData.setShortCode(Long.parseLong(tokens[3]));
					reqResData.setNoOfRequests(1);
					hoursMap.put(hoursMapKey, reqResData);
				}
			}
			//else if (line.endsWith("[op:32]")) {
			else if (line.matches(responseRegex)) {
				String subCode = (String) phoneRequestSubCodes.get(phoneNo);
				if(subCode != null)
				{
					if(hoursMap.get(dateHourString+","+subCode) != null)
					{
						Ussd868ReqResData reqResData = (Ussd868ReqResData)hoursMap.get(dateHourString+","+subCode);
						reqResData.setNoOfResponses(reqResData.getNoOfResponses()+1);
						phoneRequestSubCodes.remove(phoneNo);
					}
				}
				/*else
				{
					//System.out.println("Null Subcode");
					//System.out.println(line);
					if(hoursMap.containsKey(dateHourString+","))
					{
						Ussd868ReqResData reqResData = (Ussd868ReqResData)hoursMap.get(dateHourString+",");
						reqResData.setNoOfResponses(reqResData.getNoOfResponses()+1);
					}
					else
					{
						Ussd868ReqResData reqResData = new Ussd868ReqResData();
						reqResData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
						String[] tokens = line.split("-");
						reqResData.setShortCode(Long.parseLong(tokens[3]));
						reqResData.setNoOfResponses(1);
						hoursMap.put(dateHourString+",", reqResData);
					}
				}*/
			}else{
				//System.out.println("something else:"+line);
			}
		}

	}
	
	
	private String getDateHourString(String line)
	{
		return line.substring(0, 13);
	}
	private String getSubCode(String line)
	{
		String subCode = "";
		String requestDesc = Utils.stringBetween(line, "Request: ", " [op:1]");
		
		if(requestDesc != null && requestDesc.length() > 1)
		{
			for(int i = 0;i<subCodes.length;i++)
			{
				if(requestDesc.startsWith(subCodes[i]+"#") || requestDesc.startsWith(subCodes[i]+"*"))
				{
					subCode = subCodes[i];
					break;
				}
			}
		}
		return subCode;
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
			
			PropertyReader.init("D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection");
			UssdScReqResWithSubCodesConverter s = new UssdScReqResWithSubCodesConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\TEST.log");
//			input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120911.log");
//			input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_858_2009020319.log");
//			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120912.log");
			s.convert(input,"ussd_sc");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			PropertyReader.init("D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\");
			UssdScReqResWithSubCodesConverter s = new UssdScReqResWithSubCodesConverter();
			String line = "2007-12-09 11:59:08.532 [20107084521] -777- Request: ;602022014932161 [op:1]";
			String subCode = s.getSubCode(line);
			System.out.println("subCode = "+subCode);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}

}
