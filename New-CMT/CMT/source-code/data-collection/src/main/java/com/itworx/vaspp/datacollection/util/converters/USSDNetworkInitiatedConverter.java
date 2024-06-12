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
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDNetworkInitiatedConverter  extends AbstractTextConverter {
	private Logger logger;
	private Map<String , Long> numOfResReq = new HashMap<String, Long>() ;
public USSDNetworkInitiatedConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside USSDNetworkInitiatedConverter convert - started converting input files");
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
			long numberOfRequests=0;
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {

				line = inputStream.readLine();
				String shortCode = null;
				
				if (line.contains("-1-")&& line.contains("Sending SMPP request")) {
					try
					{
					  date = getDate(line);
					}
					catch(ParseException exc) { logger.error(exc) ; continue ;}
                    if(numOfResReq.containsKey(date))
                    {
                    	
                     Long number=numOfResReq.get(date);
                     number++;
					 numOfResReq.remove(date);
					 numOfResReq.put(date, number);
					}
                    else
                    { 
                    	numberOfRequests++;
                    	numOfResReq.put(date, new Long(numberOfRequests));
                    }
				
			}

			
		}
			
			
	}
		Iterator myVeryOwnIterator =numOfResReq.keySet().iterator();
	while(myVeryOwnIterator.hasNext()){ 					
		Object key = myVeryOwnIterator.next();
		long requestNumbers = numOfResReq.get(key);
			outputStream.write(key+","+requestNumbers);
			outputStream.newLine();
	}
	inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("USSDNetworkInitiatedConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("USSDNetworkInitiatedConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("USSDNetworkInitiatedConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("USSDNetworkInitiatedConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	
	
}
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
			date = inDateFormat.parse(tokens[0]);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\DataCollection");
			USSDNetworkInitiatedConverter s = new USSDNetworkInitiatedConverter();
			File[] input = new File[24];
			input[0]=new File("D:\\build\\DataCollection\\ipcconnector_2010041000.log");
			input[1]=new File("D:\\build\\DataCollection\\ipcconnector_2010041001.log");
			input[2]=new File("D:\\build\\DataCollection\\ipcconnector_2010041002.log");
			input[3]=new File("D:\\build\\DataCollection\\ipcconnector_2010041003.log");
			input[4]=new File("D:\\build\\DataCollection\\ipcconnector_2010041004.log");
			input[5]=new File("D:\\build\\DataCollection\\ipcconnector_2010041005.log");
			input[6]=new File("D:\\build\\DataCollection\\ipcconnector_2010041006.log");
			input[7]=new File("D:\\build\\DataCollection\\ipcconnector_2010041007.log");
			input[8]=new File("D:\\build\\DataCollection\\ipcconnector_2010041008.log");
			input[9]=new File("D:\\build\\DataCollection\\ipcconnector_2010041009.log");
			input[10]=new File("D:\\build\\DataCollection\\ipcconnector_2010041010.log");
			input[11]=new File("D:\\build\\DataCollection\\ipcconnector_2010041011.log");
			input[12]=new File("D:\\build\\DataCollection\\ipcconnector_2010041012.log");
			input[13]=new File("D:\\build\\DataCollection\\ipcconnector_2010041013.log");
			input[14]=new File("D:\\build\\DataCollection\\ipcconnector_2010041014.log");
			input[15]=new File("D:\\build\\DataCollection\\ipcconnector_2010041015.log");
			input[16]=new File("D:\\build\\DataCollection\\ipcconnector_2010041016.log");
			input[17]=new File("D:\\build\\DataCollection\\ipcconnector_2010041017.log");
			input[18]=new File("D:\\build\\DataCollection\\ipcconnector_2010041018.log");
			input[19]=new File("D:\\build\\DataCollection\\ipcconnector_2010041019.log");
			input[20]=new File("D:\\build\\DataCollection\\ipcconnector_2010041020.log");
			input[21]=new File("D:\\build\\DataCollection\\ipcconnector_2010041021.log");
			input[22]=new File("D:\\build\\DataCollection\\ipcconnector_2010041022.log");
			input[23]=new File("D:\\build\\DataCollection\\ipcconnector_2010041023.log");
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
