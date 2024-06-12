package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.BytesPropertyReader;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.FileFieldByte;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class FileWaitsConverter extends AbstractTextConverter {
	private Logger logger;

	public FileWaitsConverter() {
	}

	/**
	 * Converting the input file to comma seperated file.
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("FileWaitsConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("FileWaitsConverter.convert() - converting file "
						+ inputFiles[i].getName());
				
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String line;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if (line.equals("")) {
						continue;
					}else if(line.equals("SQL>")){
						DatabaseUtils.skipSqlLines(inputStream);
						continue;
					}else if(line.contains("no rows selected")){
						// Prepare the no row data 
						String outputLine = Utils.getYesterdaysDate()+", ,0,0,0.0";
						//System.out.println(outputLine);
						outputStream.write(outputLine);
						outputStream.newLine();
						break;
					}else if(line.startsWith("File #")){
						this.readData(inputStream, outputStream);
						break;
					}
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("FileWaitsConverter.convert() - "
						+ inputFiles[i].getName() + " converted");	
			}								
		} catch (FileNotFoundException e) {
			logger.error("FileWaitsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("FileWaitsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("FileWaitsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
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
	private void readData(BufferedReader inputStream, BufferedWriter outputStream)
		throws IOException,InputException {
		// Intialize the variables for new file
		String newLine;
		String outputLine;
		ArrayList linesArray = new ArrayList();
		boolean firstLine = true;
		String[] tokens;
		// Prepare the fileds information
		ArrayList filedsInfo = new ArrayList();
		
		filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.fileName.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.fileName.position"))));
		filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.waits.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.waits.position")),true));
		filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.time.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.time.position")),true));
		filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.avgTime.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_file_waits.avgTime.position")),true));
		
		// Start read lines
		Utils.skip(1, inputStream);
		while(inputStream.ready()){
			newLine = inputStream.readLine();
			if (newLine.equals("")) {
				break;
			}
			
			if(firstLine){
				linesArray.add(newLine);
				firstLine = false;
				continue;
			}
			
			// Check is new line or not
			if(DatabaseUtils.CheckNewLine(newLine))
			{
				// Format the previous line(s) and write it on file
				String formattedLine = DatabaseUtils.MergeLines(linesArray,filedsInfo);
				// Tokenizing the line using comma seperation
				tokens = formattedLine.split(",");		
				if(!tokens[1].equals("0")){
					outputLine = Utils.getYesterdaysDate()+","+
								tokens[0]+","+
								tokens[1]+","+
								tokens[2]+","+
								tokens[3];
					//System.out.println(outputLine);
					outputStream.write(outputLine);
					outputStream.newLine();
				}				
				
				// Intilaize the new line(s)
				linesArray = new ArrayList();
				linesArray.add(newLine);				
			}else{
				// Add the remaining line
				linesArray.add(newLine);
				continue;
			}
		}
		// Format the previous line(s) and write it on file
		String formattedLine = DatabaseUtils.MergeLines(linesArray,filedsInfo);
		// Tokenizing the line using comma seperation
		tokens = formattedLine.split(",");		
		if(!tokens[1].equals("0")){
			outputLine = Utils.getYesterdaysDate()+","+
						tokens[0]+","+
						tokens[1]+","+
						tokens[2]+","+
						tokens[3];
			//System.out.println(outputLine);
			outputStream.write(outputLine);
			outputStream.newLine();
		}		
		
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			FileWaitsConverter s = new FileWaitsConverter();
			File[] input = new File[4];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits_empty.res");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits_special_cases.res");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits_sql.res");
			input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits.res");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
