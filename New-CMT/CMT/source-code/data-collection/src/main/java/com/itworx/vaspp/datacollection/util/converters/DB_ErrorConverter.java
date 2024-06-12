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
public class DB_ErrorConverter extends AbstractTextConverter {

	private Logger logger;
	
	
	/**
	 * 
	 */
	public DB_ErrorConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("DB_ErrorConverter - Starting Converting DBA Errors");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "DB_ErrorConverter.convert() - converting file "
				+ inputFiles[i].getName() );
				
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				
				
				String line = "";
				
				do{
					line = inputStream.readLine();
					if(line.startsWith("SQL>")){
						DatabaseUtils.skipSqlLines(inputStream);
					}
				}while(line.indexOf("no rows selected") < 0 && !line.startsWith("Object Type"));
				
				if(line.indexOf("no rows selected") >= 0){
					outputStream.write(",,,0,0,0,," + Utils.getYesterdaysDate());
					outputStream.newLine();
					outputStream.close();
					inputStream.close();
					outputFiles[i] = output;
					continue;
				}
				
				Utils.skip(3, inputStream);
				
				line = inputStream.readLine();
				Vector col = new Vector();
				
				while(line.indexOf("rows selected") < 0){
					ReadLine(inputStream, line, col);
					Utils.skip(1, inputStream);
					do{
					line = inputStream.readLine();
					}while(line.trim().length() < 1);
				}
				
				for(int k=0; k < col.size(); k++){
					outputStream.write((String)col.get(k));
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "DB_ErrorConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "DB_ErrorConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("DB_ErrorConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("DB_ErrorConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private void ReadLine(BufferedReader inputStream, String line, Vector col) throws IOException{
		String previousLine = line;
		line = inputStream.readLine();
		
		
		ArrayList arr1 = new ArrayList();
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Object_Type.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Object_Type.position"))));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Schema.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Schema.position"))));
		arr1.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Object_Name.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Object_Name.position"))));
		
		ArrayList arr2 = new ArrayList();
		arr2.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Sequence.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Sequence.position"))));
		
		ArrayList arr3 = new ArrayList();
		arr3.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Line.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Line.position"))));
		
		ArrayList arr4 = new ArrayList();
		arr4.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Position.bytes")) ,
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("db_Error.Position.position"))));
		
		String formatedLine = "";
		boolean lineStart = line.startsWith("ORA") || line.startsWith("DBV") || line.startsWith("EXP")
		 || line.startsWith("IMP") || line.startsWith("LDR") || line.startsWith("PL/SQL")
		  || line.startsWith("PLS") || line.startsWith("SQL*Loader") || line.startsWith("KUP")
		   || line.startsWith("NID") || line.startsWith("LCD") || line.startsWith("QSM")
		    || line.startsWith("RMAN") || line.startsWith("LRM") || line.startsWith("LFI")
		    || line.startsWith("AMD") || line.startsWith("TNS") || line.startsWith("NNC")
		    || line.startsWith("NNO") || line.startsWith("NNL") || line.startsWith("NPL")
		    || line.startsWith("NNF") || line.startsWith("NMP") || line.startsWith("NCR")
		    || line.startsWith("NZE") || line.startsWith("MOD") || line.startsWith("O2F")
		    || line.startsWith("O2I") || line.startsWith("O2U") || line.startsWith("PCB")
		    || line.startsWith("PCF") || line.startsWith("PCC") || line.startsWith("SQL")
		    || line.startsWith("AUD") || line.startsWith("IMG") || line.startsWith("VID")
		    || line.startsWith("DRG") || line.startsWith("LPX") || line.startsWith("LSX")
		    || line.startsWith("EPC");
		if(lineStart){
			formatedLine = DatabaseUtils.ExtractLineData(previousLine, arr1);
		}
		else{
			ArrayList lines = new ArrayList();
			lines.add(previousLine);
			do{
				lines.add(line);
				line = inputStream.readLine();
				lineStart = line.startsWith("ORA") || line.startsWith("DBV") || line.startsWith("EXP")
				 || line.startsWith("IMP") || line.startsWith("LDR") || line.startsWith("PL/SQL")
				  || line.startsWith("PLS") || line.startsWith("SQL*Loader") || line.startsWith("KUP")
				   || line.startsWith("NID") || line.startsWith("LCD") || line.startsWith("QSM")
				    || line.startsWith("RMAN") || line.startsWith("LRM") || line.startsWith("LFI")
				    || line.startsWith("AMD") || line.startsWith("TNS") || line.startsWith("NNC")
				    || line.startsWith("NNO") || line.startsWith("NNL") || line.startsWith("NPL")
				    || line.startsWith("NNF") || line.startsWith("NMP") || line.startsWith("NCR")
				    || line.startsWith("NZE") || line.startsWith("MOD") || line.startsWith("O2F")
				    || line.startsWith("O2I") || line.startsWith("O2U") || line.startsWith("PCB")
				    || line.startsWith("PCF") || line.startsWith("PCC") || line.startsWith("SQL")
				    || line.startsWith("AUD") || line.startsWith("IMG") || line.startsWith("VID")
				    || line.startsWith("DRG") || line.startsWith("LPX") || line.startsWith("LSX")
				    || line.startsWith("EPC");
			}while(!lineStart);
			
			formatedLine = DatabaseUtils.MergeLines(lines, arr1);
		}
		//the rest of the data
		formatedLine = formatedLine + "," + Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(previousLine, arr2));
		formatedLine = formatedLine + "," + Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(previousLine, arr3));
		formatedLine = formatedLine + "," + Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(previousLine, arr4));
		
		
		//get the rest of the text field
		previousLine = line;
		line = inputStream.readLine();
		if(line.trim().length() == 0){
			formatedLine = formatedLine + "," + previousLine.trim() + "," + Utils.getYesterdaysDate();
			col.add(formatedLine);
		}
		else{
			while(line.trim().length() != 0){
				previousLine = previousLine + line;
				line = inputStream.readLine();
			}
			formatedLine = formatedLine + "," + previousLine.trim() + "," + Utils.getYesterdaysDate();
			col.add(formatedLine);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*DB_ErrorConverter s = new DB_ErrorConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\dba_errors.res");
		s.convert(files,"");*/
	}

}
