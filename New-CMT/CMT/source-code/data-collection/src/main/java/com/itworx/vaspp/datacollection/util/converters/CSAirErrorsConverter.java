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
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CSAirErrorsConverter  extends AbstractTextConverter{

	public static String[] WANTEDSTRS = new String[]{".+((Max No Connections Reached)|(Max no of connections reached)).+",".+(Reject due to overload).+",".+(SDP Error).+",".+(Internal Error).+"};

	Logger logger;
	public File[] convert(File[] inputFiles, String systemName) throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		File[] outputFiles = new File[1];
		String path = PropertyReader.getConvertedFilesPath();
		HashMap hourMap = new HashMap();
		try {
			File output = new File(path, "DCSAirErrors.log");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			for (int i = 0; i < inputFiles.length; i++) {
				//for every file update hours Map values
				updateVariables(inputFiles[i], hourMap);
			}
			
			//write the values of hours Map to the output file
			for (Iterator iter = hourMap.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				double[] valuesArr = (double[])hourMap.get(key);
				outputStream.write(key+","+valuesArr[0]+","+valuesArr[1]+","+valuesArr[2]+","+valuesArr[3]);
				System.out.println(key+","+valuesArr[0]+","+valuesArr[1]+","+valuesArr[2]+","+valuesArr[3]);
				outputStream.newLine();
			}
			outputStream.flush();
			hourMap.clear();
			hourMap = null;
			outputFiles[0] = output;
		}
		catch (FileNotFoundException e) {
			logger.error("CSAirErrorsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CSAirErrorsConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}
		return outputFiles;
	}
	public void updateVariables(File newFile,HashMap hourMap) throws ApplicationException,FileNotFoundException,IOException
	{
		String line="";
		BufferedReader inputStream = new BufferedReader(new FileReader(newFile));
		String curKey = null;
		while(inputStream.ready()){
			line=inputStream.readLine();
			if(line=="")
				continue;
			if(line.startsWith("*"))
				continue;
			line = line.trim();
			//get dayHour Key from date string
			String tmp = Utils.stringBetween(line,"[","]");
			if(tmp != null)
			{
				curKey = getKey(tmp);
				//update hours Map with the current line 
				updateMap(curKey,hourMap,line);
			}
		}
		inputStream.close();
	}
	
	private void updateMap(String curKey, HashMap hourMap, String line) {
		int updatedValueIndex = -1;
		//if any string from string array exists in the line then we will update values array according to found string
		for(int i = 0; i < CSAirErrorsConverter.WANTEDSTRS.length;i++)
		{

			//if(line.indexOf(CSAirErrorsConverter.WANTEDSTRS[i])> -1)
			if(line.matches(CSAirErrorsConverter.WANTEDSTRS[i]))
			{
				//variable used to update values array according to it
				updatedValueIndex = i;
				break;
			}
		}
//		if(updatedValueIndex > -1)
		if(true)
		{
			double[] hourValue = null;
			//if there is values for current day hour then get it
			if(hourMap.containsKey(curKey))
			{
				hourValue = (double[])hourMap.get(curKey);
			}
			else//init new values array
			{
				hourValue = new double[]{0,0,0,0};
			}
			//update values array according to selected index 
			if(updatedValueIndex > -1)
				hourValue[updatedValueIndex]++;
			//update the map 
	        hourMap.put(curKey, hourValue);
		}
	}
	
	private String getKey(String dateValue) {
		//20080906 17:41:24.374983
		String key = ":00:00";
		String day = dateValue.substring(6,8);
		String month = dateValue.substring(4,6);
		String year = dateValue.substring(0,4);
		String hour = dateValue.substring(9,11);
		key = month+"/"+day+"/"+year+" "+hour+key;
		return key;
	}
	public static void main(String[] args) {
		try {
				PropertyReader
			.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\SourceCode\\DataCollection");
				CSAirErrorsConverter s =new CSAirErrorsConverter();
				File[] input = new File[5];
				input[0]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.0");
				input[1]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.1");
				input[2]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.2");
				input[3]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.3");
				input[4]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\10-09-2012\\event.log.4");
//			input[1]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\FSC-AirXmlRpc_2.0_A_1-2008-07-29-0000.stat");

			s.convert(input,"MSP");
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}