package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class BrokenJobsConverter extends AbstractTextConverter {
	
	private Logger logger;

	public BrokenJobsConverter() {
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
		logger.debug("BrokenJobsConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("BrokenJobsConverter.convert() - converting file "
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
						String outputLine = " ,0,"+Utils.getYesterdaysDate();
						//System.out.println(outputLine);
						outputStream.write(outputLine);
						outputStream.newLine();
						break;
					}else if(line.startsWith("SCHEMA_USER")){
						this.readData(inputStream, outputStream);
						break;
					}
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("BrokenJobsConverter.convert() - "
						+ inputFiles[i].getName() + " converted");	
			}								
		} catch (FileNotFoundException e) {
			logger.error("BrokenJobsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("BrokenJobsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("BrokenJobsConverter.convert() - finished converting input files successfully ");
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
		String line;
		String outputLine;
		String[] tokens;
		Date date;
		Date currentDate;
		
		Utils.skip(1, inputStream);
		while(inputStream.ready()){
			line = inputStream.readLine();
			if (line.equals("")) {
				return;
			}
			// Tokenizing the line using comma seperation
			tokens = line.split(",");
			// Prepare the date time
			date = Utils.convertToDate(tokens[2], "dd/MM/yyyy HH:mm:ss");
			// Prepare the output line and write it on the file
			outputLine = tokens[0]+","+tokens[1]+","+Utils.convertToDateString(date, Utils.defaultFormat);
			//System.out.println(outputLine);
			outputStream.write(outputLine);
			outputStream.newLine();
		}
		
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			BrokenJobsConverter s = new BrokenJobsConverter();
			File[] input = new File[3];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\broken_jobs_empty.res");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\broken_jobs_sql.res");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\broken_jobs.res");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
