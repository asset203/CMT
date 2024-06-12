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
public class PerformanceSharedPoolMemoryConverter extends AbstractTextConverter {

	private Logger logger;
	
	/**
	 * 
	 */
	public PerformanceSharedPoolMemoryConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("PerformanceSharedPoolMemoryConverter - Starting Converting Performance Shared Pool Memory");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "PerformanceSharedPoolMemoryConverter.convert() - converting file "
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
				}while(line.indexOf("no rows selected") < 0 && !line.startsWith("Shared Pool"));
				
				if(line.indexOf("no rows selected") >= 0){
					outputStream.close();
					inputStream.close();
					outputFiles[i] = output;
					continue;
				}
				
				
				
				Utils.skip(1, inputStream);
				
				line = inputStream.readLine();
				Vector col = new Vector();
				
				while(line.indexOf("------------") < 0){
					while(!DatabaseUtils.CheckNewLine(line)){
						line = inputStream.readLine();
					}
					line = ReadLine(inputStream, line, col);
				}
				
				for(int k=0; k < col.size(); k++){
					outputStream.write((String)col.get(k));
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "PerformanceSharedPoolMemoryConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "PerformanceSharedPoolMemoryConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("PerformanceSharedPoolMemoryConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("PerformanceSharedPoolMemoryConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private String ReadLine(BufferedReader inputStream, String line, Vector col) throws IOException{
		String formatedString = "";
		
		ArrayList arr1 = new ArrayList();
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.poolsize.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.poolsize.position")),true));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.freebytes.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.freebytes.position")),true));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.percentfree.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_shared_pool_mem.percentfree.position")),true));
		
		formatedString = DatabaseUtils.ExtractLineData(line, arr1);
		
		formatedString = formatedString  + "," + Utils.getYesterdaysDate();
		col.add(formatedString);
		
		return inputStream.readLine();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*PerformanceSharedPoolMemoryConverter s = new PerformanceSharedPoolMemoryConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\perf_shared_pool_memory.res");
		s.convert(files,"");*/
		
	}

}
