package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.PerformanceSnapshotData;
import com.itworx.vaspp.datacollection.util.BytesPropertyReader;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.FileFieldByte;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class PerformanceSnapshotConverter extends AbstractTextConverter {
	
	private Logger logger;

	public PerformanceSnapshotConverter() {
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
		logger.debug("PerformanceSnapshotConverter.convert() - started converting input files ");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles;
		try {
			ArrayList performanceSnapshotDataList;
			ArrayList outputFilesArray = new ArrayList();
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("PerformanceSnapshotConverter.convert() - converting file "
						+ inputFiles[i].getName());
				
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = null;
				
				performanceSnapshotDataList = new ArrayList();
				String line;
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("no rows selected")){
						break;
					}else if(line.trim().equals("SQL>")){
						DatabaseUtils.skipSqlLines(inputStream);
						continue;					
					}else if(line.contains("Instance Name")){
						Utils.skip(1, inputStream);
						this.readInstanceNameData(inputStream, performanceSnapshotDataList);					
					}else if(line.contains("Buffer Hit Ratio")){
						Utils.skip(1, inputStream);
						this.readBufferHitRatioData(inputStream, performanceSnapshotDataList);
					}else if(line.contains("Data Dict Cache Hit Ratio")){
						Utils.skip(1, inputStream);
						this.readDataDictData(inputStream, performanceSnapshotDataList);
					}else if(line.contains("Library Cache Hit Ratio")){
						Utils.skip(1, inputStream);
						this.readLibraryData(inputStream, performanceSnapshotDataList);
					}else if(line.contains("redo log space")){
						this.readRedoLogSpaceData(inputStream, performanceSnapshotDataList, line);
					}else if(line.contains("Tot SQL since startup")){
						Utils.skip(1, inputStream);
						this.readTotSQLData(inputStream, performanceSnapshotDataList);
					}else{
						continue;
					}	
				}
				
				if(performanceSnapshotDataList.size() > 0){
					outputStream = new BufferedWriter(new FileWriter(output));
					for (int j = 0; j < performanceSnapshotDataList.size(); j++) {
						PerformanceSnapshotData performanceData = (PerformanceSnapshotData)performanceSnapshotDataList.get(j);
						String outputLine = Utils.getYesterdaysDate()+","+
											performanceData.getInstanceName()+","+
											Utils.convertToDateString(performanceData.getOpenDate(),Utils.defaultFormat)+","+
											performanceData.getBufferHitRatio()+","+
											performanceData.getDataDictCacheHitRatio()+","+
											performanceData.getLibraryCacheHitRatio()+","+
											performanceData.getRedoLogSpaceRequests()+","+
											performanceData.getTotSql()+","+
											performanceData.getSqlExecutionNow();
						//System.out.println(outputLine);
						outputStream.write(outputLine);
						outputStream.newLine();
					}
					outputStream.close();
					inputStream.close();
					outputFilesArray.add(output);
					logger.debug("PerformanceSnapshotConverter.convert() - "
							+ inputFiles[i].getName() + " converted");	
				}
		
			}	
			// Adding the output files to the return variable
			outputFiles = new File[outputFilesArray.size()];
			for(int count = 0 ; count < outputFilesArray.size() ; count++){
				outputFiles[count] = (File) outputFilesArray.get(count);
			}
			
		} catch (FileNotFoundException e) {
			logger.error("PerformanceSnapshotConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("PerformanceSnapshotConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} 
		logger.debug("PerformanceSnapshotConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}
	
	/**
	 * extract data excluding the time string
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
	private void readInstanceNameData(BufferedReader inputStream, ArrayList performanceSnapshotDataList)
		throws IOException,InputException {
		String instanceName="";
		String openDate="";
		PerformanceSnapshotData performanceData;
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			StringTokenizer s = new StringTokenizer(line);
			instanceName = s.nextToken();
			openDate = s.nextToken();
			performanceData = new PerformanceSnapshotData();
			performanceData.setInstanceName(instanceName);
			performanceData.setOpenDate(Utils.convertToDate(openDate, "dd-MMM-yy"));
			performanceSnapshotDataList.add(performanceData);			
		}
	}
	
	/**
	 * extract data excluding the time string
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
	private void readBufferHitRatioData(BufferedReader inputStream, ArrayList performanceSnapshotDataList)
		throws IOException,InputException {
		String bufferHitRatio="";
		int i = 0;
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			StringTokenizer s = new StringTokenizer(line);
			bufferHitRatio = Utils.formatBigNumbers(s.nextToken());
			((PerformanceSnapshotData)performanceSnapshotDataList.get(i)).setBufferHitRatio(Long.parseLong(bufferHitRatio));
			i++;
		}
	}
	
	/**
	 * extract data excluding the time string
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
	private void readDataDictData(BufferedReader inputStream, ArrayList performanceSnapshotDataList)
		throws IOException,InputException {
		String dataDictCacheHitRatio="";
		int i = 0;
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			StringTokenizer s = new StringTokenizer(line);
			s.nextToken();
			s.nextToken();
			dataDictCacheHitRatio = Utils.formatBigNumbers(s.nextToken());
			((PerformanceSnapshotData)performanceSnapshotDataList.get(i)).setDataDictCacheHitRatio(Long.parseLong(dataDictCacheHitRatio));
			i++;
		}
	}
	
	/**
	 * extract data excluding the time string
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
	private void readLibraryData(BufferedReader inputStream, ArrayList performanceSnapshotDataList)
		throws IOException,InputException {
		String libraryCacheHitRatio="";
		int i = 0;
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			StringTokenizer s = new StringTokenizer(line);
			s.nextToken();
			s.nextToken();
			libraryCacheHitRatio = Utils.formatBigNumbers(s.nextToken());
			((PerformanceSnapshotData)performanceSnapshotDataList.get(i)).setLibraryCacheHitRatio(Long.parseLong(libraryCacheHitRatio));
			i++;
		}
	}
	
	/**
	 * extract data excluding the time string
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
	private void readRedoLogSpaceData(BufferedReader inputStream, ArrayList performanceSnapshotDataList, String line)
		throws IOException,InputException {
		String redoLogSpaceRequests="";
		ArrayList filedsInfo;
		int i = 0;
		String currentline;
		boolean firstTime = true;
		while (inputStream.ready()) 
		{
			if(firstTime){
				currentline = line;
				firstTime = false;				
			}else{
				currentline = inputStream.readLine();
			}
			
			if (currentline.equals("")|| (!currentline.contains("redo log space")&&!currentline.trim().equals("requests"))) 
			{
				return;	
			}else if (currentline.trim().equals("requests")) 
			{
				continue;
			}
			
			filedsInfo = new ArrayList();
			filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Redo_Log_Space_Requests.bytes")) ,
					Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Redo_Log_Space_Requests.position")),true));
			
			redoLogSpaceRequests = DatabaseUtils.ExtractLineData(currentline,filedsInfo);
			((PerformanceSnapshotData)performanceSnapshotDataList.get(i)).setRedoLogSpaceRequests(Long.parseLong(redoLogSpaceRequests));
			i++;		
		}
	}
	
	/**
	 * extract data excluding the time string
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
	private void readTotSQLData(BufferedReader inputStream, ArrayList performanceInfoDataList)
		throws IOException,InputException {
		int i = 0;
		String formatedLine;
		ArrayList filedsInfo;
		while (inputStream.ready()) 
		{
			String line = inputStream.readLine();
			
			if (line.equals("")) 
			{
				return;
			}
			filedsInfo = new ArrayList();
			filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Tot_Sql.bytes")) ,
					Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Tot_Sql.position")),true));
			filedsInfo.add(new FileFieldByte(Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Sql_Execution_Now.bytes")) ,
					Integer.parseInt(BytesPropertyReader.GetPropertyValue("perf_performance_snapshot.Sql_Execution_Now.position")),true));
			
			formatedLine = DatabaseUtils.ExtractLineData(line,filedsInfo);
			String[] tokens = formatedLine.split(",");
			((PerformanceSnapshotData)performanceInfoDataList.get(i)).setTotSql(Long.parseLong(tokens[0]));
			((PerformanceSnapshotData)performanceInfoDataList.get(i)).setSqlExecutionNow(Long.parseLong(tokens[1]));
			i++;
		}
	}

	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			PerformanceSnapshotConverter s = new PerformanceSnapshotConverter();
			File[] input = new File[4];
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_performance_snapshot_empty.res");
			input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_performance_snapshot_special_cases.res");
			input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_performance_snapshot_sql.res");
			input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_performance_snapshot.res");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
