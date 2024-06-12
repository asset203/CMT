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

public class EOCNResponseTransConverter  extends AbstractTextConverter{
	private Logger logger;
public EOCNResponseTransConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside EOCNResponseTransConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("EOCNResponseTransConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.equalsIgnoreCase(""))
				{continue;}
				String []columns= line.split(",");	
				if(columns.length>=28){
				if(columns[4]!=null)
				{
					try
					{
						String dateLine;
						String response="";
						String transId="";
						if(columns[4].contains("\""))
						 dateLine=(columns[4].split("\"")[1]).split("\"")[0];
						else dateLine=columns[4];
						date =getDate(dateLine);
						if(columns[8]!=null)
							response=columns[8].contains("\"")?columns[8].split("\"")[1].split("\"")[0]:columns[8];
						if(columns[27]!=null)
							transId=columns[27].contains("\"")?columns[27].split("\"")[1].split("\"")[0]:columns[27];
						if(response!=null&&!"0".equals(response.trim())){
							outputStream.write(date+","+response+","+transId);
						//System.out.println("the key is "+date+","+response+","+transId);
						outputStream.newLine();
						}
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
			}}
		}
			} 
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("EOCNResponseTransConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("EOCNResponseTransConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("EOCNResponseTransConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("EOCNResponseTransConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		String path = "D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\SourceCode\\DataCollection";
		//PropertyReader.init("D:\\build\\pahse8\\DataCollection");
		PropertyReader.init(path);
		EOCNResponseTransConverter s = new EOCNResponseTransConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\history201010190710.log.processed");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
