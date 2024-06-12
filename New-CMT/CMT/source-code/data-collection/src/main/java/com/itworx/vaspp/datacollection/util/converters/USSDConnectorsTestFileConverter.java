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
import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDConnectorsTestFileConverter extends AbstractTextConverter{

	private Logger logger;

	public USSDConnectorsTestFileConverter() {
	}

	public int request=0;
	public int response=0;
	public String dateString;
	public String connectorName;
	public int succeedResponses=0; 
	public int otherResponses=0 ; 
	
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
	
		logger = Logger.getLogger(systemName);
		logger.debug("USSDConnectorsTestFileConverter.convert() - started converting input files ");
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
				logger.debug("USSDConnectorsTestFileConverter.convert() - converting file "
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
						outputStream.write(outputLine);
						outputStream.newLine();
						continue;
					}
				}
			/*	outputStream.write(outputLine);
				outputStream.newLine();
				dateString="";
				response=0;
				request=0;
				connectorName="";
				succeedResponses=0;
				otherResponses=0;*/
				inputStream.close();

			}		
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("USSDConnectorsTestFileConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDConnectorsTestFileConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			
		} catch (IOException e) {
			logger.error("USSDConnectorsTestFileConverter.convert() - Couldn't read input file"
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
		String outLineString;
		tokens = line.split(",");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	try {
			date=sdf1.parse(tokens[0]);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
		dateString=sdf.format(date);
		String msisdn = line.substring(line.indexOf("[")+1, line.indexOf("]"));
		String lineType = tokens[2];
				
		outLineString=dateString+","+msisdn+","+connectorName+","+lineType;
		
		//System.out.println(outLineString);
		return outLineString;

	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal6\\Source Code\\DataCollection");
			USSDConnectorsTestFileConverter s = new USSDConnectorsTestFileConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\ITWorx\\Projects\\VNPP_Phase 7\\USSD log files\\USSD connector logs\\USSD_Connectors_1265196053042_1_(RASEED_EXTRA_3)_ipcconnector_2010010509.log");
		//	File test = new File("D:\\maha\\ipcconnector_2010010513.log");
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


}
