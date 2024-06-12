package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class BTConnectorsConverter extends AbstractTextConverter{

	private Logger logger;
	private List<String>lineKeys = new ArrayList<String>();
	private List<String> phons = new ArrayList<String>();
	private Map<String,Long> requestsCount = new HashMap<String, Long>();
	private Map<String,Long> responsesCount = new HashMap<String, Long>();
	private Map<String,Long> unhandeledRequestsCount = new HashMap<String, Long>();
	
	private static String MSG_TOKEN="MSG";
	private static String NEW_REQUEST_TOKEN ="New AppRequest";
	private static String RESPONSE_TOKEN="AppResponse";
	private static String STATUS_TOKEN="REQUEST_STATUS";
	
	@Override
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		//1- start logging
		logger = Logger.getLogger(systemName);
		logger.debug("Inside BTConnectorsConverter convert - started converting input files");
		
		//2- create output files array
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		
		
		//3- streams to be used
		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		File outputFile = new File(path, inputFiles[0].getName() + "_" + systemName + "_UCIPResponse_" + new Date().getTime());
		
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("BTConnectorsConverter() - Start  converting file "+ inputFiles[i].getName());
				
				//inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
				String line;
				String currentDate =""; 
				String datePhoneKey;
				//4- start parsing the file
				while (inputStream.ready()) {
					line = inputStream.readLine();
					
					//4.1 Ignore unneeded lines
					if(!line.contains(MSG_TOKEN))//ignore lines doesn't contain word "MSG" 
						continue;
					if(!line.contains(NEW_REQUEST_TOKEN) && !line.contains(RESPONSE_TOKEN)) //ignore  lines doesn't contain new requests nor response
						continue;
					
					//4.2 extract date (to hours only)
					try {
						currentDate =getDate( line.split(",")[0]);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//4.3 extract phone number
					String phone = line.substring(line.indexOf("[")+1, line.indexOf("]"));//TODO: handel out of bound exception
					datePhoneKey = currentDate+","+phone;
					
					//4.4 process lines containing "New Request"
					if(line.contains(NEW_REQUEST_TOKEN)){
						
						phons.add(datePhoneKey);
					}
					
					//4.5 process lines containing "AppResponse"
					else if(line.contains(RESPONSE_TOKEN) && line.contains(STATUS_TOKEN)){
						String statusPart = line.substring(line.indexOf(STATUS_TOKEN));
						String statusCode = 
							statusPart.substring(statusPart.indexOf(",")+1, statusPart.indexOf(";"));
						String lineKey = currentDate+","+statusCode+",-";
						if(lineKeys.contains(lineKey))
						{
							long responseCount = responsesCount.get(lineKey);
							responseCount ++;	
							responsesCount.put(lineKey, responseCount);
						}
						else
						{
							lineKeys.add(lineKey);
							responsesCount.put(lineKey, new Long(1));
							requestsCount.put(lineKey, new Long(0));
							
						}
						
						if(phons.contains(datePhoneKey))
						{
							phons.remove(datePhoneKey);
							long requestCount = requestsCount.get(lineKey);
							requestCount++;
							requestsCount.put(lineKey, requestCount);
						}
					}				
				}
				
				inputStream.close();
				
				logger.debug("BTConnectorsConverter() - Finish converting file "+ inputFiles[i].getName());
			}
			//5-count unhandeledRequests
			if(!phons.isEmpty())
			{
				Iterator<String> itr = phons.iterator();
				while(itr.hasNext()){
					String datePhone = itr.next();
					String date = datePhone.split(",")[0];
					if(unhandeledRequestsCount.containsKey(date))
					{
						long count = unhandeledRequestsCount.get(date);
						count++;
						unhandeledRequestsCount.put(date, count);
					}else{
						unhandeledRequestsCount.put(date, new Long(1));
					}
				}
			}
			
			//6- write results to output file
			Iterator<String> it = lineKeys.iterator();
			while(it.hasNext())
			{
				String lineKey = it.next();
				String date = lineKey.split(",")[0];
				String responseStatus = lineKey.split(",")[1];
				String requestCount = Long.toString(requestsCount.get(lineKey));
				String responsCount = Long.toString(responsesCount.get(lineKey));
				
				outputStream.write(date +"," +responseStatus+","+requestCount+","+responsCount);
				outputStream.newLine();
			}
			
			for(Entry<String,Long> entry : unhandeledRequestsCount.entrySet()){
				
				outputStream.write(entry.getKey() +"," +" "+","+entry.getValue()+","+"0");
				outputStream.newLine();
			}
			
			outputStream.close();
			
			//7- clearing results to start parsing next file 
			phons.clear();
			lineKeys.clear();
			requestsCount.clear();
			responsesCount.clear();
			unhandeledRequestsCount.clear();
			
			outputFiles[0] = outputFile;
			logger.debug("BTConnectorsConverter.convert() - finished converting input files successfully ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputFiles;
	}
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:SS.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String[] args){
		
		try {

			PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode\\DataCollection");
			BTConnectorsConverter basmaConverter = new BTConnectorsConverter();
			File[] input = new File[11];
			input[0] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011613.log");
			input[1] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011614.log");
			input[2] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011615.log");
			input[3] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011616.log");
			input[4] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011617.log");
			input[5] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011618.log");
			input[6] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011619.log");
			input[7] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011620.log");
			input[8] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011621.log");
			input[9] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011622.log");
			input[10] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120116_bt_connectors\\ipcconnector_2012011623.log");
			
			basmaConverter.convert(input, "Testing");
			
			System.out.println("DONE...");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
