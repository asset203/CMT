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
public class FileIOEffeciencyConverter extends AbstractTextConverter {

	private Logger logger;
	
	
	/**
	 * 
	 */
	public FileIOEffeciencyConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("FileIOEffeciencyConverter - Starting Converting Performance File IO Efficiency");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "FileIOEffeciencyConverter.convert() - converting file "
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
				}while(line.indexOf("no rows selected") < 0 && !line.startsWith("Tablespace"));
				
				if(line.indexOf("no rows selected") >= 0){
					outputStream.close();
					inputStream.close();
					outputFiles[i] = output;
					continue;
				}
				
				
				
				Utils.skip(1, inputStream);
				
				line = inputStream.readLine();
				Vector col = new Vector();
				
				while(line.indexOf("rows selected") < 0){
					
					do{
						if(line.trim().length() > 0){
							line = ReadLine(inputStream, line, col);
						}
						else line = inputStream.readLine();
					}while(line.trim().length() < 1 && line.indexOf("rows selected") < 0);
				}
				
				for(int k=0; k < col.size(); k++){
					outputStream.write((String)col.get(k));
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "FileIOEffeciencyConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "FileIOEffeciencyConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("FileIOEffeciencyConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("FileIOEffeciencyConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private String ReadLine(BufferedReader inputStream, String line, Vector col) throws IOException{
		String previousLine = line;
		line = inputStream.readLine();
		
		ArrayList arr1 = new ArrayList();
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.tablespace.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.tablespace.position"))));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.filename.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.filename.position"))));
		
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.readsrights.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.readsrights.position")),true));
		
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.effeciency.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("file_io_effeciency.effeciency.position")),true));
		
		
		String formatedLine = "";
		if(DatabaseUtils.CheckNewLine(line) || line.length() < 1){
			formatedLine = DatabaseUtils.ExtractLineData(previousLine, arr1);
		}
		else{
			ArrayList lines = new ArrayList();
			lines.add(previousLine);
			while(line.length() > 1 && !DatabaseUtils.CheckNewLine(line) && line.indexOf("rows selected") < 0){
				lines.add(line);
				line = inputStream.readLine();
				//System.out.println(line.length());
			}
			formatedLine = DatabaseUtils.MergeLines(lines, arr1);
		}
		formatedLine = formatedLine + "," +  Utils.getYesterdaysDate();
		col.add(formatedLine);
		return line;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*FileIOEffeciencyConverter s = new FileIOEffeciencyConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\perf_file_io_efficiency.res");
		s.convert(files,"");*/
	}

}
