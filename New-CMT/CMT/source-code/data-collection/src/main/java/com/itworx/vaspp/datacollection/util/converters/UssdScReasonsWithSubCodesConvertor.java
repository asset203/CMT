package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.Ussd777ReasonsData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

/*
 * - the log file associated with this class writes data for every requested 777/868 services and 
 *  this services in some times like 868 has sub code like 868#1,868#2
 * - this class is responsible of converting 777/868 log files to retrieve
 * the following aggregated values : 
 * 1- response reason
 * 2- count of response reasons
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
 * */

public class UssdScReasonsWithSubCodesConvertor extends AbstractTextConverter{
	
	private Logger logger;
	//used to hold phone numbers and its request sub key
	private HashMap phoneRequestSubCodes = new HashMap();
	
	private String[] subCodes = null;
	
	public UssdScReasonsWithSubCodesConvertor() {
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
		logger.debug("UssdScReasonsWithSubCodesConvertor.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "UssdScReasonsFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line;
			String previousLine = "";
			String outputLine;
			HashMap responseReasonsMap = new HashMap();
			HashMap datesMap;
			Ussd777ReasonsData fileData;
			Set reasonsSet;
			Set datesSet;
			Object[] reasonKeys;
			Object[] datesKeys;
			
			subCodes = PropertyReader.get868RequestsSubCodes().split(",");
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("UssdScReasonsWithSubCodesConvertor.convert() - converting file " + inputFiles[i].getName());
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
							this.extractResponseReasons(previousLine, responseReasonsMap);								
							//If new line initialize the previousLine
							previousLine = line;
						}else{
							previousLine += line;
						}
					}	
				}
				inputStream.close();
			}
			// extract Response Reason for last line
			this.extractResponseReasons(previousLine, responseReasonsMap);
			
			// Write the output records
			reasonsSet = responseReasonsMap.keySet();
			reasonKeys = reasonsSet.toArray();			
			for(int j = 0 ; j < reasonKeys.length ; j++){
				datesMap = (HashMap)responseReasonsMap.get((String)reasonKeys[j]);
				datesSet = datesMap.keySet();
				datesKeys =datesSet.toArray();				
				for(int z = 0 ; z < datesKeys.length ; z++){
					String dateMapKey = (String)datesKeys[z]; 
					fileData = (Ussd777ReasonsData)datesMap.get(dateMapKey);
					String subCode = (dateMapKey.split(",").length == 2) ? dateMapKey.split(",")[1] : "Others";
					//note : we must put ,- in the end of string to make split process performed right because if subCode is empty split will fail if no ,-
					outputLine = Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)+","+fileData.getResponseReason().replace(",", ".")+","+fileData.getNoOfReasons()+","+fileData.getShortCode()+","+subCode+",-";
					//System.out.println(outputLine);
					outputStream.write(outputLine);
					outputStream.newLine();
				}
			}			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("UssdScReasonsWithSubCodesConvertor.convert() - "
					+ inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("UssdScReasonsWithSubCodesConvertor.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("UssdScReasonsWithSubCodesConvertor.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("UssdScReasonsWithSubCodesConvertor.convert() - finished converting input files successfully ");
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
	private void extractResponseReasons(String line, HashMap responseReasonsMap) throws InputException {
		if (line.equals("")) {
			return;
		}else{	
			String responseRegex="^.+\\[op:3?2[;\\]].*$";
			String phoneNo = Utils.stringBetween(line, "[", "]");
			String dateHourString = line.substring(0,13).trim();
			// Check if it is response whose code [op:32]
			if (line.contains("Request:")&&line.contains("[op:1]")) {
				String subCode = getSubCode(line);
				if(subCode==null)
					subCode="";
				phoneRequestSubCodes.put(phoneNo,subCode);
			}
			//else if(line.endsWith("[op:32]")) {
			else if(line.contains("Response")&&(line.matches(responseRegex))) {
				String subCode = (String) phoneRequestSubCodes.remove(phoneNo);
				if(subCode==null)
					return;
				
				String dateMapKey = dateHourString+","+subCode;
				
				Ussd777ReasonsData fileData;
				HashMap dates;
				
				String responseReason = "";
				if(line.contains("[op:32"))
					responseReason =line.substring(line.indexOf("Response:")+9,line.indexOf("[op:32")).trim();
				else
					responseReason =line.substring(line.indexOf("Response:")+9,line.indexOf("[op:2")).trim();
				if(responseReasonsMap.containsKey(responseReason)){					
					dates = (HashMap)responseReasonsMap.get(responseReason);
					
					if(dates.containsKey(dateMapKey)){
						//Update the already existed record
						fileData = (Ussd777ReasonsData) dates.get(dateMapKey);
						fileData.setNoOfReasons(fileData.getNoOfReasons()+1);
						dates.put(dateMapKey, fileData);
					}else{
						//Inialize new record with the existed reason code but with new date_hour
						fileData = new Ussd777ReasonsData();
						String[] tokens = line.split("-");
						fileData.setShortCode(Long.parseLong(tokens[3]));
						fileData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
						fileData.setResponseReason(responseReason);
						fileData.setNoOfReasons(1);
						dates.put(dateMapKey, fileData);
					}
					responseReasonsMap.put(responseReason, dates);
				}else{
					
					//Inialize new record with new reason code
					fileData = new Ussd777ReasonsData();
					String[] tokens = line.split("-");
					fileData.setShortCode(Long.parseLong(tokens[3]));
					fileData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
					fileData.setResponseReason(responseReason);
					fileData.setNoOfReasons(1);
					dates = new HashMap();
					dates.put(dateMapKey, fileData);
					responseReasonsMap.put(responseReason, dates);
				}
			}
		}	
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
			String path= "D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\";
			
			PropertyReader.init(path);
			UssdScReasonsWithSubCodesConvertor s = new UssdScReasonsWithSubCodesConvertor();
			File[] input = new File[1];
			//input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_868_2009020409.log");
			//input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_868_2009020409_test.log");
			//input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120912.log");
			input[0]=new File("D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\Production Builds\\Round5 - USSD Updates\\browser_868_2010091304.log");
			s.convert(input,"ussd_sc");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		String line = "2009-02-04 07:59:59.511 [20109467280] -868- Response: You will receive SMS only if you are in validity with balance more than 19PT [op:32;]";
		System.out.println(line.matches("^.+\\[op:32;.+$"));
*/
	}
}
