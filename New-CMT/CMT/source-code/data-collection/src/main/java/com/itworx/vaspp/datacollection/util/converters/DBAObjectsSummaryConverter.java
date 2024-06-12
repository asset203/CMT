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
public class DBAObjectsSummaryConverter extends AbstractTextConverter {

	private Logger logger;

	
	
	/**
	 * 
	 */
	public DBAObjectsSummaryConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("DBAObjectsSummaryConverter - Starting Converting DBA Objects Summary");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "DBAObjectsSummaryConverter.convert() - converting file "
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
				}while(line.indexOf("no rows selected") < 0 && !line.startsWith("Owner"));
				
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
					
					line = ReadOwnerAllData(inputStream, line, col);
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
				
				logger.debug( "DBAObjectsSummaryConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "DBAObjectsSummaryConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("DBAObjectsSummaryConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("DBAObjectsSummaryConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private String ReadOwnerAllData(BufferedReader inputStream, String line, Vector col) throws IOException{
		String previousLine = line;
		line = inputStream.readLine();
		
		ArrayList arr1 = new ArrayList();
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.owner.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.owner.position"))));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.objectType.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.objectType.position"))));
		
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.objectCount.bytes")) ,
			Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_Objects_Summary.objectCount.position")),true));
	
		//get first line
		String formatedLine = "";
		if(DatabaseUtils.CheckNewLine(line) || line.startsWith("*")){
			formatedLine = DatabaseUtils.ExtractLineData(previousLine, arr1);
		}
		else{
			ArrayList lines = new ArrayList();
			lines.add(previousLine);
			do{
				lines.add(line);
				line = inputStream.readLine();
			}while(!DatabaseUtils.CheckNewLine(line) && !line.startsWith("*"));
			
			formatedLine = DatabaseUtils.MergeLines(lines, arr1);
		}
		
		formatedLine = formatedLine + "," +  Utils.getYesterdaysDate();
		col.add(formatedLine);
		
		//get the owner from the first line
		String Owner = formatedLine.substring(0,formatedLine.indexOf(','));
		arr1.remove(0);
		
		//get the rest of the data 
		while(!line.startsWith("*")){
			previousLine = line;
			line = inputStream.readLine();
			
			formatedLine = "";
			if(DatabaseUtils.CheckNewLine(line) || line.startsWith("*")){
				formatedLine = DatabaseUtils.ExtractLineData(previousLine, arr1);
			}
			else{
				ArrayList lines = new ArrayList();
				lines.add(previousLine);
				do{
					lines.add(line);
					line = inputStream.readLine();
				}while(!DatabaseUtils.CheckNewLine(line) && !line.startsWith("*"));
				formatedLine = DatabaseUtils.MergeLines(lines, arr1);
			}
			
			formatedLine = Owner + "," + formatedLine + "," +  Utils.getYesterdaysDate();
			col.add(formatedLine);
			
		}
		
		return line;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*DBAObjectsSummaryConverter s = new DBAObjectsSummaryConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\dba_obj_summary.res");
		s.convert(files,"");*/
		
	}

}
