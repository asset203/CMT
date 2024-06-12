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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDConnectorsResponseStatusConverter extends AbstractTextConverter
{

	private Logger logger;

	public USSDConnectorsResponseStatusConverter() {
	}

	public String dateString;
	public String connectorName;

	HashMap<String, Integer> map = new HashMap<String, Integer>();
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
	
		logger = Logger.getLogger(systemName);
		logger.debug("USSDConnectorsResponseStatusConverter.convert() - started converting input files ");
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
				logger.debug("USSDConnectorsResponseStatusConverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
						connectorName = inputFiles[i].getName().
								substring(inputFiles[i].getName().indexOf("(")+1, inputFiles[i].getName().lastIndexOf(")"));
		
				String line;
				while (inputStream.ready()) {					
					line = inputStream.readLine();
					if (line.trim().equals("")) {
						continue;
					}
					else if(line.contains("AppResponse")== false && line.contains("begin SRV_SUB_INTERFACE")== false){
						continue;
					}else {
						outputLine=	this.readData(line, outputStream);
						continue;
					}
				}
				
			//iterate on map that contains data	
			Iterator myVeryOwnIterator = map.keySet().iterator();
			String[] out;

				while(myVeryOwnIterator.hasNext()){ 					
					Object key = myVeryOwnIterator.next();
					Object value = map.get(key);
					out=key.toString().split(",");				
				//	System.out.println("aaaaaaaal***"+out[0]+","+connectorName+","+out[1]+","+value.toString());
					outputStream.write(out[0]+","+connectorName+","+out[1]+","+value.toString());
					outputStream.newLine();
				}
				connectorName="";
				map.clear();	
				inputStream.close();
			}		
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("USSDConnectorsResponseStatusConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDConnectorsResponseStatusConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
		} catch (IOException e) {
			logger.error("USSDConnectorsResponseStatusConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
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
		String outLineString="nothing";
		String[] myTokens;
		tokens = line.split(",");
		String[] statusArray;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	try {
		//parse date string
			date=sdf1.parse(tokens[0]);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:00:00");
		//format date
		dateString=sdf.format(date);
		String mapKey;
		if(tokens[2].contains("AppResponse"))
		{
			myTokens=tokens[2].split("status=");
			if(myTokens==null||myTokens.length<2)
				return "";
			statusArray=myTokens[1].split("'");
		//mapKey =date+response status
			mapKey=dateString.concat(","+statusArray[1]);
			//
            if (map.containsKey(mapKey)) 
            {
                // get number of occurrences for this key
                // increment it 
                // and put back again 
            	map.put(mapKey, map.get(mapKey) + 1);
       
            } else {
                // this is first time we see this key, set value '1'
            	map.put(mapKey, 1);
          
            }

		}
		outLineString=connectorName;
		return outLineString;
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\VASPortalWF5_working\\VASPortalWF5\\Source Code\\DataCollection");
			USSDConnectorsResponseStatusConverter s = new USSDConnectorsResponseStatusConverter();
			File[] input = new File[2];
	
			//input[0]=new File("D:\\maha\\sara osama\\maha.log");
			input[0]=new File("D:\\maha\\sara osama\\ipcconnector_2010010600.log");
			input[1]=new File("D:\\maha\\sara osama\\ipcconnector_2010010523.log");
			//input[1]=new File("D:\\maha\\ipcconnector_2010092610.log");
			//input[2]=new File("D:\\maha\\maha_file3.res");
			/*input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits.res");		
			*/
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
