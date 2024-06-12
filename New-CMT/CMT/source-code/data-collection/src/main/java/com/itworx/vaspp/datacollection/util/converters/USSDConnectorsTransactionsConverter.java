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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDConnectorsTransactionsConverter extends AbstractTextConverter{

	private Logger logger;

	public USSDConnectorsTransactionsConverter() {
	}

	public int request=0;
	public int response=0;
	public String dateString;
	public String connectorName;
	public int succeedResponses=0; 
	public int otherResponses=0 ; 
	private HashMap<String, Entry> map = new HashMap<String, Entry>() ;
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
	
		logger = Logger.getLogger(systemName);
		logger.debug("USSDConnectorsTransactionsConverter.convert() - started converting input files ");
		String outputLine="";
		try {
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					outputFile));
			BufferedReader inputStream;
			for (int i = 0; i < inputFiles.length; i++) 
			{
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				logger.debug("USSDConnectorsTransactionsConverter.convert() - converting file "
						+ inputFiles[i].getName());
				connectorName = inputFiles[i].getName().
									substring(inputFiles[i].getName().indexOf("(")+1, inputFiles[i].getName().lastIndexOf(")"));
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if (line.trim().equals("")) {
						continue;
					}else if(line.contains("AppResponse")== false && line.contains("begin SRV_SUB_INTERFACE")== false){
						continue;
					}else {
						
						outputLine=	this.readData(line, outputStream);
						continue;
					}
				}
				
				//iterate on map which contains all data
				Iterator myVeryOwnIterator = map.keySet().iterator();
				ArrayList dataList=new ArrayList<String>();
				while(myVeryOwnIterator.hasNext())
				{ 
					Object key = myVeryOwnIterator.next();
					Object value = map.get(key);
					Entry entry=(Entry)value;
				//	System.out.println("****all*"+key.toString()+","+entry.getNumOfResponses()+","+entry.getNumOfRequests()+","+connectorName+","+entry.getSucceedResponses()+","+entry.getOtherResponses());
					outputStream.write(key.toString()+","+entry.getNumOfResponses()+","+entry.getNumOfRequests()+","+connectorName+","+entry.getSucceedResponses()+","+entry.getOtherResponses());
					outputStream.newLine();
				}	
				map.clear();
				inputStream.close();

			}		
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("USSDConnectorsTransactionsConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDConnectorsTransactionsConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			e.printStackTrace();
			
		} catch (IOException e) {
			logger.error("USSDConnectorsTransactionsConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * extract data.
	 * 
	 * @param inputStream -
	 *            the input file
	 * @param lines -
	 *            the arrays of lines to concatenate data
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private String readData(String line, BufferedWriter outputStream)
		throws IOException,InputException {
	
	
		String[] tokens;
		Date date=new Date();
		String outLineString;
		tokens = line.split(",");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      
	try {
		  //parse date string
			date=sdf1.parse(tokens[0]);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:00:00");
		//formate date
		dateString=sdf.format(date);
	
		
	//check if map contains that date
	if(map.containsKey(dateString))
	{

	
		Entry e=new Entry();
		//count requests
		if(tokens[2].contains("begin"))
		{
			
			long x=map.get(dateString).getNumOfRequests()+1;
			map.get(dateString).setNumOfRequests(x);
			map.put(dateString, map.get(dateString));
		
		
			
			//count responses
		}else if(tokens[2].contains("AppResponse"))
		{
			long x=map.get(dateString).getNumOfResponses()+1;
			map.get(dateString).setNumOfResponses(x);
			map.put(dateString, map.get(dateString));
			
		
	
		}
		int index;
		String[] errorArray;
		String[] errors;
		
		if(tokens[2].contains("AppResponse")&& tokens[2].contains("Request is processed successfuly"))
		{
			/*errorArray=tokens[2].split(":");
			errors=errorArray[2].split(" ");
			//count number of succeed responses (lines that contain ErrCode=0)
			if(errors[0].equals("0"))
			{*/
				
				long x=map.get(dateString).getSucceedResponses()+1;
				map.get(dateString).setSucceedResponses(x);
				map.put(dateString, map.get(dateString));
	   //}
			//count number of other responses (lines that contain ErrCode not = 0)
		}else if(tokens[2].contains("AppResponse")&& !tokens[2].contains("Request is processed successfuly"))
			{
				 long x=map.get(dateString).getOtherResponses()+1;
					map.get(dateString).setOtherResponses(x);
					map.put(dateString, map.get(dateString));
			}

		


		
	}
	//map doesn't contain that date
	else	
	{
		
		Entry e=new Entry();
		if(tokens[2].contains("begin"))
		{
			e.setNumOfRequests(1);
			
		}else if(tokens[2].contains("AppResponse"))
		{
			e.setNumOfResponses(1);
		}
		int index;
		String[] errorArray;
		String[] errors;
		if(tokens[2].contains("AppResponse")&& tokens[2].contains("Request is processed successfuly"))
		{
			/*errorArray=tokens[2].split(":");
			errors=errorArray[2].split(" ");
			System.out.println("***Codeeror***"+errors[0]);
			if(errors[0].equals("0"))
			{
				e.setSucceedResponses(1);
			}*/}
			else if(tokens[2].contains("AppResponse")&& !tokens[2].contains("Request is processed successfuly"))
			{
				 e.setOtherResponses(1);
			}

		

		map.put(dateString, e);
	}
	

		outLineString=dateString;
		return outLineString;

	}
	
	public static void main(String ag[]) {
		try {
			String ws= "D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\SourceCode\\DataCollection\\";
			//PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			PropertyReader.init(ws);
			USSDConnectorsTransactionsConverter s = new USSDConnectorsTransactionsConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\maha\\sara osama\\ipcconnector_2010010600.log");
			//input[1]=new File("D:\\maha\\sara osama\\ipcconnector_2010010523.log");
			//input[0]=new File("D:\\build\\VASPortal\\DataCollection\\ipcconnector_2010010513.log");
			input[0]=new File("D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\Internal Builds\\Temp\\USSD_Connectors_1283774098854_1_(RASEED_EXTRA_3)_ipcconnector_2010082911.log");
			//File test = new File("D:\\maha\\ipcconnector_2010010513.log");
	//		test.
		//	USSD_Connectors_1265196053042_1-RASEED_EXTRA_3-ipcconnector_2010010509.log
		//	input[0]=new File("D:\\maha\\ipcconnector_2010010513.log");
			//input[1]=new File("D:\\maha\\maha_file2.res");
			//input[2]=new File("D:\\maha\\maha_file3.res");
			/*input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits.res");		
			*/
			s.convert(input,"USSD_Connectors");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private class Entry {
		long numOfRequests;
		long numOfResponses;
		long succeedResponses;
		long otherResponses;
		public long getNumOfRequests() {
			return numOfRequests;
		}
		public void setNumOfRequests(long numOfRequests) {
			this.numOfRequests = numOfRequests;
		}
		public long getNumOfResponses() {
			return numOfResponses;
		}
		public void setNumOfResponses(long numOfResponses) {
			this.numOfResponses = numOfResponses;
		}
		public long getSucceedResponses() {
			return succeedResponses;
		}
		public void setSucceedResponses(long succeedResponses) {
			this.succeedResponses = succeedResponses;
		}
		public long getOtherResponses() {
			return otherResponses;
		}
		public void setOtherResponses(long otherResponses) {
			this.otherResponses = otherResponses;
		}
	

	}


}
