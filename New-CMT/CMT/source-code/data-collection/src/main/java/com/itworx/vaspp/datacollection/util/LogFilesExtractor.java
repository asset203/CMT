package com.itworx.vaspp.datacollection.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;

public class LogFilesExtractor {
	
	private Logger logger;

	public LogFilesExtractor() {
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
	public File extractFile(String systemName, String dateString, File logFile, String nodeName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger("LogManager");
		logger.debug("LogFilesExtractor.extractFile() - started extracting the data related to the date:"+dateString+" ,for the system:"+systemName);
		
		String path = PropertyReader.getConvertedLogFilesPath();
		File tempOutputFile = new File(path, "temp_"+nodeName+"_"+logFile.getName());
		File outputFile = new File(path, nodeName+"_"+logFile.getName());
		File currentFile = null;
		File previousFile = null;
		BufferedReader inputStream = null;
		int count = 0;
		try {
			ArrayList filesList = new ArrayList();
			do{ 
				if(count == 0){
					currentFile = logFile;
				}else{
					currentFile = previousFile;
				}
				String line = "";
				String firstDateString = "";
				try{
					inputStream = new BufferedReader(new FileReader(currentFile));
					// Loop till reading new line
					do{
						line = inputStream.readLine();
						if(line==null)
							continue;
						System.out.println(line);
						if(line.length() > 19){
							//  19 bits because the date in that format: "03/06/2007 09:30:08"
							firstDateString = line.substring(0,19);
						}else{
							firstDateString = line;
						}
					}while(!this.CheckNewLogLine(firstDateString) && inputStream.ready());
					inputStream.close();
				}catch(FileNotFoundException e){
					// To handle the case of required date existed in one log file which is not started with "started dispatchJob"
					//System.out.println("break exception");
					break;
				}
				

				// Compare the date of current new line and the specified date
				int compareValue = this.CompareDates(dateString, firstDateString.substring(0,10));
				// the specified date is not existed in the current file
				if(compareValue < 0){
					count++;
					FtpLogHandler f = new FtpLogHandler();
					previousFile = f.readFile(systemName, count);
					continue;
				}else{
					// the specified date start is not/is existed in the current file, we'll check the previous one
					if(compareValue == 0 ){
						filesList.add(currentFile);
						count++;
						FtpLogHandler f = new FtpLogHandler();
						previousFile = f.readFile(systemName, count);
						continue;
					} // the specified date is existed and started in the current file
					else{
						filesList.add(currentFile);
						break;
					}
				}
			}// Maximum archived files is 20
			while(count <= 20);
			
			BufferedWriter tempOutputStream = null;
			// Initialize the outputstream
			if( filesList.size() > 0){
				tempOutputStream = new BufferedWriter(new FileWriter(tempOutputFile));
				this.exctractFilesData(filesList, tempOutputStream, dateString);
				tempOutputStream.close();
				logger.debug("LogFilesExtractor.extractFile() - extracted successfully the data related to the date:"+dateString+" ,for the system:"+systemName);
				// Remove unstarted batch lines from the top of the file
				return this.removeBatchLinesInTop(tempOutputFile, outputFile);				
			}								
		} catch (FileNotFoundException e) {
			logger.error("LogFilesExtractor.extractFile() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("LogFilesExtractor.extractFile() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("LogFilesExtractor.extractFile() - extracting the logs related to date:"+dateString+",for the system:"+systemName+" successfully ");
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
	private void exctractFilesData(ArrayList filesList, BufferedWriter outputStream, String dateString)
		throws IOException,InputException {
		
		BufferedReader inputStream = null;
		String line;
		String previousLine = "";
		boolean requiredDate = false;
		boolean Startappend = true;
		boolean firstFileFirstOutputLine = true;
		
		for (int i = (filesList.size()-1); i >= 0 ; i--) 
		{
			inputStream = new BufferedReader(new FileReader((File)filesList.get(i)));
			logger.debug("LogFilesExtractor.extractFile() - extract data from file: "+ ((File)filesList.get(i)).getName());

			while (inputStream.ready()) {
				
				line = inputStream.readLine();
				
				String tempLine = line.trim();
				
				if (line.equals("") || tempLine.startsWith("at") || tempLine.startsWith("ORA")) {
					Startappend = false;
				}
				
				String lineDateString = "";
				if(line.length() > 19){
					//  19 bits because the date in that format: "03/06/2007 09:30:08"
					lineDateString = line.substring(0,19);
				}else{
					lineDateString = line;
				}
				
				if(this.CheckNewLogLine(lineDateString)){
					Startappend = true;
					if(!firstFileFirstOutputLine && requiredDate){
						// Write the previous line that contain the required date
						outputStream.write(previousLine);
						outputStream.newLine();
					}
					
					if( this.CompareDates(lineDateString.substring(0, 10), dateString) == 0){ 
						// If new line and contain the required date
						if(firstFileFirstOutputLine){
							// First time i found the specified date
							firstFileFirstOutputLine = false;
						}
						
						requiredDate = true;
						previousLine = line;
					}else{
						// If new line and not contain the required date
						previousLine = "";
						requiredDate = false;
					}
				}else{
					if(Startappend){
						// Append the current line if only it relates to line which contain the required date
						previousLine += line;
					}
				}
				
			}
			
		}
		// To write the last output line after all files finished
		if(!firstFileFirstOutputLine && requiredDate){
			// Write the previous line that contain the required date
			outputStream.write(previousLine);
			outputStream.newLine();
		}
		
	}
	
	/**
	 * remove unstarted batch lines.
	 * 
	 * @param tempOutputFile -
	 *            the temp output file
	 * @param outputFile -
	 *            the output file
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private File removeBatchLinesInTop(File tempOutputFile, File outputFile)
		throws IOException,InputException {
		
		// Checking strings
		String batchCollectorStart = "BatchCollector.dispatchJob() - started dispatchJob";
		String batchCollectorEnd = "BatchCollector.dispatchJob() - finished dispatchJob";
		
		// refers to find "BatchCollector.dispatchJob() - started dispatchJob"
		boolean isBatchStarted = false;
		// refer to find "BatchCollector.dispatchJob() - finished dispatchJob"
		boolean isBatchFinished = false;
		
		BufferedReader inputStream = null;		
		BufferedWriter outputStream = null;
		String line;
		
		// start 
		inputStream = new BufferedReader(new FileReader(tempOutputFile));
		logger.debug("LogFilesExtractor.extractFile() - remove batch data from the top of file: "+ tempOutputFile.getName());

		while (inputStream.ready()) {
			
			line = inputStream.readLine();
			
			if (line.contains(batchCollectorStart) ) {
				isBatchStarted = true;
			}else if (line.contains(batchCollectorEnd)){
				isBatchFinished = true;
				break;
			}
		}
			
		if(isBatchFinished == true && isBatchStarted == false){
			// return new file doesn't contain the previous lines
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			while (inputStream.ready()) {				
				line = inputStream.readLine();
				outputStream.write(line);
				outputStream.newLine();				
			}		
			outputStream.close();
			return outputFile;
		}else if ((isBatchFinished == true && isBatchStarted == true)){
			// return the same file
			return tempOutputFile;
			
		}
		// return the same file if no batch runs are existed
		return tempOutputFile;	
	}
	
	/**
	 * Compare dates.
	 * 
	 * @param dateString -
	 *            the original date
	 * @param firstDateString -
	 *            the compared date
	 * @return int - 0 represents that the two dates equals
	 * 				-1 represents that the date is before the firstDate
	 * 				+1 represents that the date is after the firstDate
	 * @throws InputException - if ParseException occured
	 */
	private int CompareDates(String dateString, String firstDateString) throws InputException{
		
		Date date = Utils.convertToDate(dateString, "dd/MM/yyyy");
		Date firstDate = Utils.convertToDate(firstDateString, "dd/MM/yyyy");
		return date.compareTo(firstDate);
	}
	
	/**
	 * Checks if a line is new or not
	 * 
	 * @param line - the line to be checked
	 * @return boolean - whether or not it is new
	 */
	private boolean CheckNewLogLine(String lineDateString){
		return lineDateString.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d");
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			FtpLogHandler f = new FtpLogHandler();
			File logFile = f.readFile("sdp", FtpLogHandler.defaultCount);
			LogFilesExtractor s = new LogFilesExtractor();
			s.extractFile("sdp","15/01/2008" ,logFile, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
