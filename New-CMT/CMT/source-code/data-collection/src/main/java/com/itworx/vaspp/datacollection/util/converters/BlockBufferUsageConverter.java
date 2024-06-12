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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.BytesPropertyReader;
import com.itworx.vaspp.datacollection.util.FileFieldByte;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

/**
 * @author ahmad.abushady
 *
 */
public class BlockBufferUsageConverter extends AbstractTextConverter {

	private Logger logger;
	
	/**
	 * 
	 */
	public BlockBufferUsageConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("BlockBufferUsageConverter - Starting Converting Block Buffer Usage");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "BlockBufferUsageConverter.convert() - converting file "
				+ inputFiles[i].getName() );
				
				
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				File output = new File(path, inputFiles[i].getName());
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				
				
				String line = "";
				
				do{
					line = inputStream.readLine();
					if(line.startsWith("SQL>")){
						DatabaseUtils.skipSqlLines(inputStream);
					}
				}while(line.indexOf("no rows selected") < 0 && !line.startsWith("Block Status"));
				
				if(line.indexOf("no rows selected") >= 0){
					outputStream.close();
					inputStream.close();
					outputFiles[i] = output;
					continue;
				}
				
				
				
				Utils.skip(1, inputStream);
				
				line = inputStream.readLine();
				Vector col = new Vector();
				
				while(!line.startsWith("Grand Total:")){
					
					line = ReadLine(inputStream, line, col);
					while(!line.matches(".*[a-zA-Z].*")){
						line = inputStream.readLine();
					}
				}
				
				for(int k=0; k < col.size(); k++){
					outputStream.write((String)col.get(k));
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "BlockBufferUsageConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "BlockBufferUsageConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("BlockBufferUsageConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("BlockBufferUsageConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private String ReadLine(BufferedReader inputStream, String line, Vector col) throws IOException{
		String formatedString = "";
		
		if(line.startsWith("Free")){
			formatedString = "Free"  + "," + Utils.formatBigNumbers(line.substring(4).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		else if(line.startsWith("Available")){
			formatedString = "Available" + "," + Utils.formatBigNumbers(line.substring(9).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		else if(line.startsWith("Being Modified")){
			formatedString = "Being Modified"  + "," + Utils.formatBigNumbers(line.substring(14).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		else if(line.startsWith("Not Modified")){
			formatedString = "Not Modified"  + "," + Utils.formatBigNumbers(line.substring(12).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		else if(line.startsWith("Being Read")){
			formatedString = "Being Read"  + "," + Utils.formatBigNumbers(line.substring(10).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		else if(line.startsWith("Other")){
			formatedString = "Other"  + "," + Utils.formatBigNumbers(line.substring(5).trim())  + "," + Utils.getYesterdaysDate();
			col.add(formatedString);
		}
		
		return inputStream.readLine();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*BlockBufferUsageConverter s = new BlockBufferUsageConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\perf_db_block_buffer_usage.res");
		s.convert(files,"");*/
		
	}

}
