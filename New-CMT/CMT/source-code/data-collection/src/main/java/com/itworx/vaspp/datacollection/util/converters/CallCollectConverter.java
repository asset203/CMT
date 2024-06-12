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

public class CallCollectConverter extends AbstractTextConverter{
	private Logger logger;
public CallCollectConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside CallCollectConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("CallCollectConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				String columns[]=line.split(",");
				if(columns!=null&&columns.length>=19)
				{
					try
					{
						String part1=columns[0]!=null?columns[0].trim():"";
						String part2=columns[1]!=null?columns[1].trim():"";
						date =getDate(part1+" "+part2);
						long field1=columns[2]!=null?Long.parseLong(columns[2].trim()):0;
						long field2=columns[3]!=null?Long.parseLong(columns[3].trim()):0;
						long field3=columns[4]!=null?Long.parseLong(columns[4].trim()):0;
						long field4=columns[5]!=null?Long.parseLong(columns[5].trim()):0;
						long field5=columns[6]!=null?Long.parseLong(columns[6].trim()):0;
						String field6=columns[7]!=null?columns[7].trim():"";
						long field7=columns[8]!=null?Long.parseLong(columns[8].trim()):0;
						String field8=columns[9]!=null?columns[9].trim():"";
						String field9=columns[10]!=null?columns[10].trim():"";
						long field10=columns[11]!=null?Long.parseLong(columns[11].trim()):0;
						long field11=columns[12]!=null?Long.parseLong(columns[12].trim()):0;
						String field12=columns[13]!=null?columns[13].trim():"";
						String field13=columns[14]!=null?columns[14].trim():"";
						String field14=columns[15]!=null?columns[15].trim():"";
						String field15=columns[16]!=null?columns[16].trim():"";
						String field16=columns[17]!=null?columns[17].trim():"";
						String field17=columns[18]!=null?columns[18].trim():"";
						 outputStream.write(date+","+field1+","+field2+","+field3+","+field4+","+field5+","+field6
								 +","+field7+","+field8+","+field9+","+field10+","+field11+","+field12
								 +","+field13+","+field14+","+field15+","+field16+","+field17);
						/*System.out.println("the key "+date+","+field1+","+field2+","+field3+","+field4+","+field5+","+field6+","
								 +","+field7+","+field8+","+field9+","+field10+","+field11+","+field12
								 +","+field13+","+field14+","+field15+","+field16+field17);*/
						 outputStream.newLine();
						//System.out.println("date "+date);
					}
					 catch(ParseException exc){ logger.error(exc) ; continue ;}
					 catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
			    }
				
				else
				{
					continue;
				}
				
			}
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("CallCollectConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("CallCollectConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("CallCollectConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("CallCollectConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd HHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\DataCollection");
		CallCollectConverter s = new CallCollectConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\DataCollection\\CCIVR.mpsmok5-1.2010092600.cdr");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
