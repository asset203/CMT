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

public class EtopupMasterConverter extends AbstractTextConverter{
	private Logger logger;
public EtopupMasterConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside EtopupMasterConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("EtopupMasterConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String key;
			String userId="";
			String geograDomainName="";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(",")&&line.split(",").length>=11)
				{
					
					
				
					

					userId=line.split(",")[0]!=null?line.split(",")[0]:"";
					geograDomainName=line.split(",")[11]!=null?line.split(",")[11]:"";
					//System.out.println("geograDomainName "+geograDomainName);
					key=userId+","+geograDomainName;
					//System.out.println("key "+key);
					outputStream.write(key);					
					outputStream.newLine();
					
				}
			}
		}
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("EtopupMasterConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("EtopupMasterConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("EtopupMasterConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("EtopupMasterConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
		EtopupMasterConverter s = new EtopupMasterConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\20110115_Master003.csv");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

}
