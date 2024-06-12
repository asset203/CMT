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

public class USSDGWTestFileConverter extends AbstractTextConverter{

	private Logger logger;

	public USSDGWTestFileConverter() {
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
		logger.debug("USSDGWTestFileConverter.convert() - started converting input files ");
		String outputLine="";
		try {
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					outputFile));
			BufferedReader inputStream;
			String[] data;
			Date date=new Date();
			  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			for (int i = 0; i < inputFiles.length; i++) 
			{
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String line;
				logger.debug("USSDGWTestFileConverter.convert() - converting file "
						+ inputFiles[i].getName());
			/*	connectorName = inputFiles[i].getName().
									substring(inputFiles[i].getName().indexOf("(")+1, inputFiles[i].getName().lastIndexOf(")"));
				*/
				while (inputStream.ready()) {
					line = inputStream.readLine();
					data=line.split(",");
				try {

						date=sdf1.parse(data[0]);
						
					} catch (ParseException e) 
					{
						
						continue;
					}
					
					if (line.trim().equals("")) {
						continue;
					}
					
					
					
					else if(line.contains("MSISDN")== true && line.contains("SC")== true)
					{
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
			logger.debug("USSDGWTestFileConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDGWTestFileConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			
		} catch (IOException e) {
			logger.error("USSDGWTestFileConverter.convert() - Couldn't read input file"
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
		String[] msisdnTokens;
		String[] scTokens;
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
		msisdnTokens=tokens[2].split(":");
	 
		//String msisdn = line.substring(line.indexOf("[")+1, line.indexOf("]"));
		String msisdn=msisdnTokens[2];
		scTokens=tokens[3].split(":");
		String sc=scTokens[1];
		String lineType="" ;
		for(int i=4;i<tokens.length;i++)
		{
			lineType=lineType+tokens[i]+" ";
		}
	
		outLineString=dateString+","+msisdn+","+sc+","+lineType.trim();
	//	System.out.println("************"+outLineString);
	
		return outLineString;

	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\VASPortalWF5_working\\VASPortalWF5\\Source Code\\DataCollection");
			USSDGWTestFileConverter s = new USSDGWTestFileConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\maha\\trace2010011403.log");
			input[1]=new File("D:\\maha\\trace2010011404.log");
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
