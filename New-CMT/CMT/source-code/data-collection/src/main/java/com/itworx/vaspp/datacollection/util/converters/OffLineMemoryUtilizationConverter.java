package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class OffLineMemoryUtilizationConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String, List> dateVSMemUtilization = new HashMap<String, List>();

	public OffLineMemoryUtilizationConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("Inside OffLineMemoryUtilizationConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			
			String firstFileName =  inputFiles[0].getName();
			String newStructureFileStarts = PropertyReader.getOfflineHWSystemNewStructureFileStarts();
			boolean newStructure = false;
			
			if(!Utils.isEmpty(newStructureFileStarts)){
				String[] fileStarts = newStructureFileStarts.toLowerCase().split(",");
				for(int s=0; s<fileStarts.length; s++){
					if(firstFileName.startsWith(fileStarts[s])){
						newStructure = true;
						break;
					}
				}
			}
			
			if(newStructure){
				convertNewStructure(inputFiles, inputStream, outputStream);
			} else {
				convertOldStructure(inputFiles, inputStream, outputStream);
			}
			outputStream.close();
			outputFiles[0] = outputFile;
			logger.debug("OffLineMemoryUtilizationConverter.convert() - finished converting input files successfully ");
		} catch (FileNotFoundException e) {
			logger.error("OffLineMemoryUtilizationConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("OffLineMemoryUtilizationConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		}
		logger.debug("OffLineMemoryUtilizationConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	private void convertNewStructure(File[] inputFiles, BufferedReader inputStream, BufferedWriter outputStream) throws FileNotFoundException, IOException {
		for (int i = 0; i < inputFiles.length; i++) {
			logger.debug("OffLineMemoryUtilizationConverter.convertNewStructure() - converting file " + inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line = null;
			String realStr = null;
			String freeStr = null;
			
			String date = "";
			String filename = "";
			double real = 0;
			double free = 0;
			String[] lineStats = null;

			if (inputFiles[i].getName().contains("_")) {
				filename = inputFiles[i].getName().split("_")[inputFiles[i].getName().split("_").length - 1].split(".txt")[0];
			}
			try {
				date = getDate(filename);
				while ((line = inputStream.readLine()) != null) {
					//Memory: 27641604K (27329636K) real, 32595836K (32088104K) virtual, 6634572K free  Page# 1/71
					line = line.toLowerCase();
					if (line.contains("memory:")
							&& line.contains("real")
							&& line.contains("free") 
							&& line.contains(",")) {
						lineStats = line.split(",");
						
						realStr = Utils.trim(lineStats[0].split(" ")[1]);
						real = getGValue(realStr);
						for(int j=1;j<lineStats.length;j++){
							if(lineStats[j].contains("free")){
								freeStr = Utils.trim(lineStats[j].split(" ")[1]);
								free = getGValue(freeStr);
								break;
							}
						}
						updateMapValues(date, free,real);
					}
				}
			} catch (ParseException exc) {
				logger.error("OffLineMemoryUtilizationConverter.convertNewStructure() - error happned while parsing line ("+line+")",exc);
				continue;
			} catch (NumberFormatException exc) {
				logger.error("OffLineMemoryUtilizationConverter.convertNewStructure() - number fiormat exception while parsing line ("+line+")",exc);
				continue;
			}
		}
		inputStream.close();
		writeOutput(outputStream);
	}

	private void convertOldStructure(File[] inputFiles, BufferedReader inputStream, BufferedWriter outputStream) throws FileNotFoundException, IOException {
		for (int i = 0; i < inputFiles.length; i++) {
			logger.debug("OffLineMemoryUtilizationConverter.convertOldStructure() - converting file " + inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));

			String line = null;
			String date = "";
			String filename = "";
			double real = 0;
			double free = 0;

			if (inputFiles[i].getName().contains("_")) {
				filename = inputFiles[i].getName().split("_")[inputFiles[i].getName().split("_").length - 1].split(".txt")[0];
			}
			try {
				date = getDate(filename);
				while ((line = inputStream.readLine()) != null) {
					if (line.contains("Memory:")
							&& line.contains("G real")
							&& (line.contains("M free") || line.contains("G free")) && line.contains(",")) {
						real = Double.parseDouble(line.split("G real")[0].split("Memory:")[1].trim());
						if (line.contains("M free")) {
							free = Double.parseDouble(line.split("M free")[0].split(",")[1].trim()) / 1024;
						} else {
							free = Double.parseDouble(line.split("G free")[0].split(",")[1].trim());
						}
						updateMapValues(date, free,real);
					}
				}
			} catch (ParseException exc) {
				logger.error("OffLineMemoryUtilizationConverter.convertOldStructure() - error happned while parsing line ("+line+")",exc);
				continue;
			} catch (NumberFormatException exc) {
				logger.error("OffLineMemoryUtilizationConverter.convertOldStructure() - number fiormat exception while parsing line ("+line+")",exc);
				continue;
			}
		}
		inputStream.close();
		writeOutput(outputStream);
	}

	private void updateMapValues(String date, double free, double real) {
		double utilization = 1 - (free / real);
		if (dateVSMemUtilization.containsKey(date)) {
			List utliList = dateVSMemUtilization.get(date);
			utliList.add(utilization);
			dateVSMemUtilization.put(date, utliList);
		} else {
			List utliList = new ArrayList();
			utliList.add(utilization);
			dateVSMemUtilization.put(date, utliList);
		}
	}

	private void writeOutput(BufferedWriter outputStream) throws IOException {
		Iterator it = dateVSMemUtilization.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			double minMaxAvg[] = minMaxAvg(dateVSMemUtilization.get(key));
			outputStream.write(key + "," + minMaxAvg[0] + "," + minMaxAvg[1] + "," + minMaxAvg[2]);
			outputStream.newLine();
		}
	}
	
	private double getGValue(String value){
		String valueStr = value.substring(0, value.length()-1);
		if(value.endsWith("k") || value.endsWith("K")){
			return Double.parseDouble(valueStr)/(1024*1024);
		} else if (value.endsWith("m") || value.endsWith("M")){
			return Double.parseDouble(valueStr)/1024;
		} else {
			return Double.parseDouble(valueStr);
		}
	}
	
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHH");
		SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:00:00");
		date = inDateFormat.parse(line.substring(0, 10));
		dateString = outDateFormat.format(date);
		return dateString;
	}

	private double[] minMaxAvg(List data) {
		double min, max, avg;
		double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator();
		any = (Double) listIterator.next();
		avg = any;
		max = avg;
		min = avg;

		while (listIterator.hasNext()) {
			double element = (Double) listIterator.next();

			if (element < min) {
				min = element;
			}
			if (element > max) {
				max = element;
			}
			avg = avg + element;
		}
		minMaxAvgArray[0] = min;
		minMaxAvgArray[1] = max;
		minMaxAvgArray[2] = avg / data.size();
		return minMaxAvgArray;
	}
	
	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode_\\DataCollection");
			OffLineMemoryUtilizationConverter s = new OffLineMemoryUtilizationConverter();
			File[] input = new File[1];
			//input[0]=new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\off_mem_utiliz_input_struct\\hwstate_rmdoffmed1_2011122313.txt");
			//input[0] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\OFF_LINE_HW_STATUS\\HP-UX_TOP_2012020909.txt");
			//input[0] = new File(
			//	"C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\offline_mediation_performance_monitoring_new_file_structure\\OFF_LINE_HW_STATUS_(OLD_NODE)_hwstate_hqoffmed-as1_2012030523.txt");
			input[0] = new File(
				"C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\offline_mediation_performance_monitoring_new_file_structure\\OFF_LINE_HW_STATUS_(NEW_NODE)_hwstate_hqoffmed2_201203061332.txt");

			s.convert(input, "Maha_Test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
