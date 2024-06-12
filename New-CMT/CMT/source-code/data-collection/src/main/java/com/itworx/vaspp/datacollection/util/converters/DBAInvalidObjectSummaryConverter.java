/*
 * File:       dbaInvalidObjectSummaryConverter.java
 * Date        Author          Changes
 * 18/11/2007  Eshraq Essam  Created
 * 06/12/2007  Eshraq Essam  Updated
 * Converter class for converting dba Invalid Object Summary text input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

public class DBAInvalidObjectSummaryConverter extends AbstractTextConverter {
	private Logger logger;

	public DBAInvalidObjectSummaryConverter() {
	}

	/**
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		String path = PropertyReader.getConvertedFilesPath();
		logger.debug("dbaInvalidObjectSummaryConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("dbaInvalidObjectSummaryConverter.convert() - converting file "+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				String yesterdaysDate = Utils.getYesterdaysDate();
				String previousLine = null;
				String newLine = null;
				Vector col = new Vector();
				ArrayList colArray = new ArrayList();
				String owner =null;
				String formatedLine=null;
				while (inputStream.ready()) {
					newLine = inputStream.readLine();
					
					if (newLine.trim().length() == 0
							|| newLine.trim().indexOf("-") == 0
							|| newLine.trim().indexOf("*") == 0
							|| newLine.trim().indexOf("Count:") == 0
							|| newLine.trim().indexOf("Grand Total:") == 0
							|| newLine.trim().indexOf("Owner") == 0
							|| newLine.trim().indexOf("SQL> spool off") == 0
							|| newLine.trim().endsWith("rows selected.") == true) {
						
							previousLine = newLine;
							continue;
						}
					
					//	check if no data 
					if (newLine.trim().indexOf("no rows selected") == 0) {
						outputStream.write(yesterdaysDate+",,,0");
						outputStream.newLine();
						outputStream.close();
						inputStream.close();
						outputFiles[i] = output;
						return outputFiles;
					}
				
					// check if file contian SQL or not
					if(newLine.trim().indexOf("SQL>") == 0)
					{
						DatabaseUtils.skipSqlLines(inputStream);
						continue;
					}
					
					int ownerPosition = Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Owner.bytes"));
					//	check is new line or not
					if(DatabaseUtils.CheckNewLine(newLine))
					{	// new line
						if(newLine.substring(0,ownerPosition)!=null 
								&& newLine.substring(0,ownerPosition).trim().length()!=0) // check has owner or not
						{	// has Owner
							formatedLine = DatabaseUtils.ExtractLineData(newLine,getdbaInvalidObjectSummaryStringInformation());
							formatedLine 		= formatedLine+ ","+ Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(newLine, getdbaInvalidObjectSummaryNumberInformation()));
							col.add(formatedLine);
							previousLine = newLine;
							colArray = new ArrayList();
							colArray.add(previousLine);
							owner = newLine.substring(0,ownerPosition).trim();
						}else { // no Owner
							colArray = new ArrayList();
							ArrayList stringColumns = new ArrayList();
							stringColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.bytes")),
									Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.position"))));
							formatedLine = DatabaseUtils.ExtractLineData(newLine,stringColumns);
							formatedLine = owner + "," + formatedLine + "," + Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(newLine, getdbaInvalidObjectSummaryNumberInformation()));
							col.add(formatedLine);
							colArray.add(newLine);
							previousLine = newLine;							
						}
					}else
					{ // not new line
						if(newLine.substring(0,ownerPosition).trim()!=null && 
								newLine.substring(0,ownerPosition).trim().length()!=0)
						{ // has owner
							    colArray.add(newLine);
							    formatedLine = DatabaseUtils.MergeLines(colArray,getdbaInvalidObjectSummaryStringInformation());
								formatedLine 		= formatedLine+ ","+ Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(colArray.get(0).toString(), getdbaInvalidObjectSummaryNumberInformation()));
								col.remove(col.size()-1);
								col.add(formatedLine);
								owner = formatedLine.substring(0, formatedLine.indexOf(","));
						}else
						{ // no owner
							    colArray.add(newLine);
								ArrayList stringColumns = new ArrayList();
								stringColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.bytes")),
										Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.position"))));
								formatedLine = DatabaseUtils.MergeLines(colArray,stringColumns);
								formatedLine 		=  owner + "," + formatedLine+ ","+ Utils.formatBigNumbers(DatabaseUtils.ExtractLineData(colArray.get(0).toString(), getdbaInvalidObjectSummaryNumberInformation()));
								col.remove(col.size()-1);
								col.add(formatedLine);
						}
					}
				}
				for (int k = 0; k < col.size(); k++) {
					outputStream.write(yesterdaysDate);
					outputStream.write(",");
				//	System.out.print(yesterdaysDate);
				//	System.out.print(",");
					outputStream.write((String) col.get(k));
				//	System.out.println((String) col.get(k));
					outputStream.newLine();
				}
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("dbaInvalidObjectSummaryConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
			logger.debug("dbaInvalidObjectSummaryConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("dbaInvalidObjectSummaryConverter.convert() - Input file not found "+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("dbaInvalidObjectSummaryConverter.convert() - Couldn't read input file"+ e);
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList getdbaInvalidObjectSummaryStringInformation() 
	{

		ArrayList stringColumns = new ArrayList();
		stringColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Owner.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Owner.position"))));
		stringColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Object_Type.position"))));
		return stringColumns;
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList getdbaInvalidObjectSummaryNumberInformation() 
	{

		ArrayList numberColumns = new ArrayList();
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Count.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("dba_invalid_obj_summary.Count.position")),true));
		return numberColumns;
	}

	public static void main(String ag[]) {
		try {
			
			PropertyReader
					.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Source Code\\DataCollection");
			DBAInvalidObjectSummaryConverter s = new DBAInvalidObjectSummaryConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\Copy of test");
			s.convert(input, "DB");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}