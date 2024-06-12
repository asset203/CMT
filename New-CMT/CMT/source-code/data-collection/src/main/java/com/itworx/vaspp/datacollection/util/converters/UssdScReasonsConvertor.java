package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.UssdShortCodesReasonsData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class UssdScReasonsConvertor extends AbstractTextConverter{
	
	private Logger logger;

	public UssdScReasonsConvertor() {
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
		logger.debug("UssdScReasonsConvertor.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, inputFiles[0].getName());
			//BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output),"UTF8"));
			BufferedReader inputStream;
			Reader reader;
			String line;
			String tempLine;			
			String outputLine;
			HashMap responseReasonsMap = new HashMap();
			HashMap datesMap;
			UssdShortCodesReasonsData fileData;
			Set reasonsSet;
			Set datesSet;
			Object[] reasonKeys;
			Object[] datesKeys;
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("UssdScReasonsConvertor.convert() - converting file "
						+ inputFiles[i].getName());
				
				reader = new InputStreamReader(new FileInputStream(inputFiles[i]), "UTF-8"); 
				inputStream = new BufferedReader(reader);
		        System.out.println("File [" + i + "]");										
				while (inputStream.ready()) {
					tempLine = inputStream.readLine();
					if(tempLine==null||tempLine.equals(""))
					continue;					
						line = new String(tempLine.getBytes("UTF-8"), "UTF-8");
						String date = "";
						if(line.length() > 23){
							//  23 bits because the date in that format: "2007-12-09 11:00:00.403"
							date = line.substring(0,23);
							try{
							this.extractResponseReasons(line, responseReasonsMap);
							}catch (Exception ex)
							{
								 logger.error(ex) ; continue ;
							}
						/*}else{
							lineDateString = line;
						}
						
						if(this.CheckNewLine(lineDateString)){									
							
							this.extractResponseReasons(previousLine, responseReasonsMap);								
							//If new line initialize the previousLine
							previousLine = line;
						}else{
							previousLine += line;
						}*/
					}	
				}
				inputStream.close();
			}
			// extract Response Reason for last line
			//this.extractResponseReasons(previousLine, responseReasonsMap);
			
			// Write the output records
			reasonsSet = responseReasonsMap.keySet();
			reasonKeys = reasonsSet.toArray();			
			for(int j = 0 ; j < reasonKeys.length ; j++){
				datesMap = (HashMap)responseReasonsMap.get((String)reasonKeys[j]);
				datesSet = datesMap.keySet();
				datesKeys =datesSet.toArray();				
				for(int z = 0 ; z < datesKeys.length ; z++){
					fileData = (UssdShortCodesReasonsData)datesMap.get((String)datesKeys[z]);
					String reason =fileData.getResponseReason().replace(",", ".");
					outputLine = Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)+","+new String (reason.getBytes("UTF-8"),"UTF-8")+","+fileData.getNoOfReasons()+","+fileData.getShortCode();
					System.out.println(outputLine);
					outputLine = new String(outputLine.getBytes("UTF-8"), "UTF-8");
					outputStream.write(outputLine);
					outputStream.newLine();
				}
			}	
			outputStream.flush();
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("UssdScReasonsConvertor.convert() - "
					+ inputFiles[0].getName() + " converted");	
			
		} catch (FileNotFoundException e) {
			logger.error("UssdScReasonsConvertor.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("UssdScReasonsConvertor.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("UssdScReasonsConvertor.convert() - finished converting input files successfully ");
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
		if (line.equals("")||(!line.contains("Response:"))) {
			return;
		}else{	
			UssdShortCodesReasonsData fileData;
			HashMap dates;
			String dateHourString = line.substring(0,13).trim();
			String shortCode = line.split("-")[3];
			String responseRegex="^.+\\[op:32[;\\]].*$";
			String responseReason=null;
			// Check if it is response whose code [op:32]
			//if (line.endsWith("[op:32]")) {
			if (line.contains("Response:")&&line.matches(responseRegex)) {
				responseReason = line.substring(line.indexOf("Response:")+9,line.indexOf("[op:32")).trim();
			}else
			{
				responseReason = line.split("Response:")[1].trim();
			}
				
				
				//used in case of multi files with multi short codes like 777 858 888 ... 
				String reasonsKey = shortCode+","+responseReason;
				if(responseReasonsMap.containsKey(reasonsKey)){					
					dates = (HashMap)responseReasonsMap.get(reasonsKey);
					
					if(dates.containsKey(dateHourString)){
						//Update the already existed record
						fileData = (UssdShortCodesReasonsData) dates.get(dateHourString);
						fileData.setNoOfReasons(fileData.getNoOfReasons()+1);
						dates.put(dateHourString, fileData);
						//System.out.println(fileData.getShortCode()+":"+responseReason+":"+dateHourString+":"+fileData.getNoOfReasons());
					}else{
						//Inialize new record with the existed reason code but with new date_hour
						fileData = new UssdShortCodesReasonsData();
						String[] tokens = line.split("-");
						fileData.setShortCode(tokens[3].trim());
						fileData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
						fileData.setResponseReason(responseReason);
						fileData.setNoOfReasons(1);
						dates.put(dateHourString, fileData);
						//System.out.println(fileData.getShortCode()+":"+responseReason+":"+dateHourString+":"+fileData.getNoOfReasons());
					}
					responseReasonsMap.put(reasonsKey, dates);
				}else{
					//Inialize new record with new reason code
					fileData = new UssdShortCodesReasonsData();
					String[] tokens = line.split("-");
					fileData.setShortCode(tokens[3].trim());
					fileData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
					fileData.setResponseReason(responseReason);
					fileData.setNoOfReasons(1);
					dates = new HashMap();
					dates.put(dateHourString, fileData);
					responseReasonsMap.put(reasonsKey, dates);
					//System.out.println(fileData.getShortCode()+":"+responseReason+":"+dateHourString+":"+fileData.getNoOfReasons());
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
			
			PropertyReader.init("D:\\VFE_VAS_SOURCE_CODE\\DataCollection\\");
			UssdScReasonsConvertor s = new UssdScReasonsConvertor();
			File[] input = new File[1];
			input[0]=new File("D:\\VFE_VAS_SOURCE_CODE\\DataCollection\\USSDShort_1341391292170_1_browser_010_2012070210.log");
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120911.log");
			//input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\browser_777_2007120912.log");
			s.convert(input,"ussd_sc");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
