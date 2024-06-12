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
import com.itworx.vaspp.datacollection.persistenceobjects.LibCacheSizeData;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class LibCacheSizeConverter extends AbstractTextConverter {

	private Logger logger;

	public LibCacheSizeConverter() {
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
		logger.debug("LibCacheSizeConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles;
		try {
			ArrayList libCacheSizeDataList;
			ArrayList outputFilesArray = new ArrayList();
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("LibCacheSizeConverter.convert() - converting file "
						+ inputFiles[i].getName());
				
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = null;
				
				libCacheSizeDataList = new ArrayList();
				String line;
				int order = 0;
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if (line.equals("")) {
						continue;
					}else if(line.equals("SQL>")){
						DatabaseUtils.skipSqlLines(inputStream);
						continue;
					}else if(line.contains("no rows selected")){
						break;
					}else if(line.startsWith("SUM(")){
						this.readData(inputStream, libCacheSizeDataList, order);
						order++;
					}
				}
				if(libCacheSizeDataList.size() > 0){
					outputStream = new BufferedWriter(new FileWriter(output));
					for (int j = 0; j < libCacheSizeDataList.size(); j++) {
						LibCacheSizeData libCacheSizeData = (LibCacheSizeData)libCacheSizeDataList.get(j);
						String outputLine = Utils.getYesterdaysDate()+","+
											libCacheSizeData.getSumShMemOfObjectCache()+","+
											libCacheSizeData.getSumShMemOfSqlArea()+","+
											libCacheSizeData.getSumShMemOfUsersOpening();
						//System.out.println(outputLine);
						outputStream.write(outputLine);
						outputStream.newLine();
					}
					outputStream.close();
					inputStream.close();
					outputFilesArray.add(output);
					logger.debug("LibCacheSizeConverter.convert() - "
							+ inputFiles[i].getName() + " converted");	
				}
			}	
			// Adding the output files to the return variable
			outputFiles = new File[outputFilesArray.size()];
			for(int count = 0 ; count < outputFilesArray.size() ; count++){
				outputFiles[count] = (File) outputFilesArray.get(count);
			}
			
		} catch (FileNotFoundException e) {
			logger.error("LibCacheSizeConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("LibCacheSizeConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("LibCacheSizeConverter.convert() - finished converting input files successfully ");
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
	private void readData(BufferedReader inputStream, ArrayList libCacheSizeDataList, int order)
		throws IOException,InputException {
		
		int i = 0;
		Utils.skip(1, inputStream);		
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			
			if( order == 0 ){
				// Intialize new object and put the first value
				LibCacheSizeData libCacheSizeData = new LibCacheSizeData();
				libCacheSizeData.setSumShMemOfObjectCache(Long.parseLong(line.trim()));
				libCacheSizeDataList.add(libCacheSizeData);
			}else if( order == 1 ){
				// Getting the existing objects and set the second value 
				((LibCacheSizeData)libCacheSizeDataList.get(i)).setSumShMemOfSqlArea(Long.parseLong(line.trim()));
				i++;
			}else if( order == 2 ){
				// Getting the existing objects and set the third value
				((LibCacheSizeData)libCacheSizeDataList.get(i)).setSumShMemOfUsersOpening(Long.parseLong(line.trim()));
				i++;
			}		
		}
	}
	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			LibCacheSizeConverter s = new LibCacheSizeConverter();
			File[] input = new File[3];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\lib_cache_size_empty.res");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\lib_cache_size_sql.res");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\lib_cache_size.res");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
