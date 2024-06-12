package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ECNSubscribersConverter extends AbstractTextConverter{
	private Logger logger;
public ECNSubscribersConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside ECNSubscribersConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		String date = "";
		
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("ECNSubscribersConverter.convert() - converting file "
							+ inputFiles[i].getName());
			long count=0;
			//System.out.println("inputFiles[i].getName() "+inputFiles[i].getName());
			
			date=inputFiles[i].getName().split("_")[2]+"_"+inputFiles[i].getName().split("_")[3]+"_"+(inputFiles[i].getName().split("_")[4]).split(".TXT")[0];			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			try
			 {
                  date=getDate(date);
                  
			 }catch(ParseException exc){logger.error(exc) ; continue ;}
			String line;
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=3)
				{
					int binary =Integer.parseInt(line.split(",")[3]) ;
					    String binarysTR =Integer.toBinaryString(binary);
					    String firstDigit=binarysTR.substring(0,1);
					   
					   if(binarysTR.substring(0,1).equalsIgnoreCase("1"))
					    	count=count+1;
					   else continue;		  
				
				}
			}
			outputStream.write(date + "," +count);
			outputStream.newLine();
		}
		
		    inputStream.close();
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("ECNSubscribersConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("ECNSubscribersConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("ECNSubscribersConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("ECNSubscribersConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
	}
private String getDate(String line) throws ParseException {
	Date date = new Date();
	String dateString;

	SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy_MM_dd");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
		ECNSubscribersConverter s = new ECNSubscribersConverter();
		File[] input = new File[2];
		input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\AccGroup_SDP17_2011_02_28.TXT");
		input[1]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\AccGroup_SDP17_2011_03_2.TXT");
		
		   s.convert(input,"Maha_Test");
		System.out.println("finished");
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
}
