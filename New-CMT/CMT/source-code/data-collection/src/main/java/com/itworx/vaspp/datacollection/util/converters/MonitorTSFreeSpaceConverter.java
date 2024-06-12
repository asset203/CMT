/*
 * File:       MonitorTSFreeSpaceConverter.java
 * Date        Author          Changes
 * 19/11/2007  Eshraq Essam  Created
 *
 * Converter class for converting Monitor TS Free Space text input file into comma spearated format
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

public class MonitorTSFreeSpaceConverter extends AbstractTextConverter {
	private Logger logger;

	public MonitorTSFreeSpaceConverter() {
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
		logger.debug("MonitorTSFreeSpaceConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("MonitorTSFreeSpaceConverter.convert() - converting file "+ inputFiles[i].getName());
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
				while (inputStream.ready()) {
					newLine = inputStream.readLine();
					
					if (newLine.trim().length() == 0
							|| newLine.trim().indexOf("-") == 0
							|| newLine.trim().indexOf("Tablespace") == 0
							|| newLine.trim().endsWith("rows selected.")== true
							|| newLine.trim().indexOf("SQL> spool off") == 0) {
						
							previousLine = newLine;
							continue;
						}
					
					//	check if no data 
					if (newLine.trim().indexOf("no rows selected") == 0) {
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
					
					
					//	check is new line or not
					if(DatabaseUtils.CheckNewLine(newLine))
					{	// new line
							// has Owner
							String formatedLine = DatabaseUtils.ExtractLineData(newLine,getdbaInvalidObjectSummaryStringInformation());
							formatedLine 		= formatedLine+ ","+ DatabaseUtils.ExtractLineData(newLine, getdbaInvalidObjectSummaryNumberInformation());
							col.add(formatedLine);
							previousLine = newLine;
							colArray = new ArrayList();
							colArray.add(previousLine);
					}else
					{ // not new line
						colArray.add(newLine);
						String formatedLine = DatabaseUtils.MergeLines(colArray,getdbaInvalidObjectSummaryStringInformation());
						formatedLine 		= formatedLine+ ","+ DatabaseUtils.ExtractLineData(colArray.get(0).toString(), getdbaInvalidObjectSummaryNumberInformation());
						col.remove(col.size()-1);
						col.add(formatedLine);
						previousLine = newLine;	
					}
				}
				for (int k = 0; k < col.size(); k++) {
					outputStream.write(yesterdaysDate);
					outputStream.write(",");
					outputStream.write((String) col.get(k));
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				logger.debug("MonitorTSFreeSpaceConverter.convert() - "+ inputFiles[i].getName() + " converted");
			}
			logger.debug("MonitorTSFreeSpaceConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("MonitorTSFreeSpaceConverter.convert() - Input file not found "+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("MonitorTSFreeSpaceConverter.convert() - Couldn't read input file"+ e);
			throw new ApplicationException(e);
		}
	}
	
	private ArrayList getdbaInvalidObjectSummaryStringInformation() 
	{

		ArrayList stringColumns = new ArrayList();
		stringColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Tablespace.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Tablespace.position"))));
		return stringColumns;
	}
	
	private ArrayList getdbaInvalidObjectSummaryNumberInformation() 
	{

		ArrayList numberColumns = new ArrayList();
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Pcs.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Pcs.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.SizeMb.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.SizeMb.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.LrgMB.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.LrgMB.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.FreeMb.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.FreeMb.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.%Free.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.%Free.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Used.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.Used.position")),true));
		numberColumns.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.%Used.bytes")),
				Integer.parseInt(BytesPropertyReader.GetPropertyValue("monitor_TS_free_space.%Used.position")),true));
		return numberColumns;
	}

	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Source Code\\DataCollection");
			MonitorTSFreeSpaceConverter s = new MonitorTSFreeSpaceConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_III\\Docs\\Requirements\\DataCollection Files\\DB_Files\\monitor_TS_free_space.res");
			s.convert(input, "DB");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}