/**
 * 
 */
package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

/**
 * @author ahmad.abushady
 *
 */
public class MCKQueuedConverter  extends AbstractTextConverter {
	
	private Logger logger;
	
	
	public MCKQueuedConverter(){}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("MCKQueuedConverter - Starting Converting MCK Queued");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];


		
		try{
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "MCKQueuedConverter.convert() - converting file "
				+ inputFiles[i].getName() );
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));


				String Line = "";
				if(inputStream.ready())
					Line = inputStream.readLine(); //skips one line
				
				Line = inputStream.readLine();
				
				while(Line != null && !Line.equals("")){
					outputStream.write(Line);
					outputStream.newLine();
					Line = inputStream.readLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "MCKQueuedConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "MCKQueuedConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("MCKQueuedConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("MCKQueuedConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	
	
	
	/*
	 * for testing
	 *
	public static void main(String ag[]) {
		try {
			MCKQueuedConverter s = new MCKQueuedConverter();
			s.path = "c:\\converted";//
			File[] files = new File[1];
			files[0] = new File("c:\\bgw1.txt");
			s.convert(files,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
