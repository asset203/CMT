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
import java.util.Set;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CPUUtilizationConverter extends AbstractTextConverter {

	private static final String _MANY = "\\s+";
	private static final String KERNEL = "kernel";
	private static final String COLON = ":";
	private static final String CPU_STATES = "cpu states";
	private static final String AVG = "avg";
	private static final String _ = " ";
	private static final String USER = "user";
	private static final String IDLE = "idle";
	private static final String SYS = "sys";
	
	
	
	private Logger logger;
	private Map<String, CPUUtilization> cpuMemoryHashmap = new HashMap<String, CPUUtilization>();
	private List<Double> idleList = new ArrayList<Double>();
	private List<Double> userList = new ArrayList<Double>();
	private List<Double> kernelList = new ArrayList<Double>();

	@Override
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("CPUUtilizationConverter - Starting");
		File[] outputFiles = new File[1];
		try {
			String path = PropertyReader.getConvertedFilesPath();
			String fileName = inputFiles[0].getName();

			File output = new File(path, fileName);
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			
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
			
			if(newStructure) {
				convertNewStructure(inputFiles);
			} else {
				convertOldStructure(inputFiles);
			}
			writeOutput(outputStream);
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CPUUtilizationConverter.convert() - "	+ inputFiles[0].getName() + " converted");
		} catch (FileNotFoundException e) {
			logger.error("CPUUtilizationConverter.convert() - input file not found" , e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CPUUtilizationConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		}
		logger.error("CPUUtilizationConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;
	}

	private void convertNewStructure(File[] inputFiles)	throws FileNotFoundException, IOException, ApplicationException,InputException {
		String fileName;
		BufferedReader inputStream;
		String line = "";
		String[] lineValues = null;
		Map<String,Integer> headerIndexes = new HashMap<String,Integer>(); 

		for (int i = 0; i < inputFiles.length; i++) {
			fileName = inputFiles[i].getName();
			String dateTime = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
			try {
				dateTime = toDate(dateTime);
			} catch (ParseException e) {
				logger.error("CPUUtilizationConverter.convert() - Couldn't parse date", e);
				continue;
			}

			logger.debug("CPUUtilizationConverter.convert() - converting file " + inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			headerIndexes.clear();
			
			//get the header indexes of (IDLE,USER,SYS)
			while((line = inputStream.readLine()) != null) {
				line = line.toLowerCase();
				if(line.startsWith(CPU_STATES)){
					if(!(headerIndexes = getHeadersIndexes(headerIndexes,inputStream.readLine().toLowerCase())).isEmpty()){
						break;
					}
				}
			}
			if(headerIndexes.isEmpty())
				continue;
			while ((line = inputStream.readLine()) != null) {
				if(!line.startsWith(AVG)){
					continue;
				}
				lineValues = line.replaceAll(_MANY, _).split(_);
				
				kernelList.add(getPercentageValue(lineValues[headerIndexes.get(SYS)]));
				userList.add(getPercentageValue(lineValues[headerIndexes.get(USER)]));
				idleList.add(getPercentageValue(lineValues[headerIndexes.get(IDLE)]));
			}
			CPUUtilization cpuUtilization = (CPUUtilization) cpuMemoryHashmap.get(dateTime);
			if (cpuUtilization == null) {
				cpuUtilization = new CPUUtilization();
			}
			if (idleList.size() <= 0 && userList.size() <= 0 && kernelList.size() <= 0) {
				continue;
			}
			updateMapValues(dateTime, cpuUtilization);
			clearLists();
			inputStream.close();
		}
	}

	private void convertOldStructure(File[] inputFiles)	throws FileNotFoundException, IOException, ApplicationException,InputException {
		String fileName;
		BufferedReader inputStream;
		String line = "";

		for (int i = 0; i < inputFiles.length; i++) {
			fileName = inputFiles[i].getName();
			String dateTime = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
			try {
				dateTime = toDate(dateTime);
			} catch (ParseException e) {
				logger.error("CPUUtilizationConverter.convert() - Couldn't parse date", e);
				continue;
			}

			logger.debug("CPUUtilizationConverter.convert() - converting file " + inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			while ((line = inputStream.readLine()) != null) {
				line = line.toLowerCase();
				if (line == "" || !line.contains(CPU_STATES)) {
					continue;
				}
				setValues(line);
			}
			CPUUtilization cpuUtilization = (CPUUtilization) cpuMemoryHashmap.get(dateTime);
			if (cpuUtilization == null) {
				cpuUtilization = new CPUUtilization();
			}
			if (idleList.size() <= 0 && userList.size() <= 0 && kernelList.size() <= 0) {
				continue;
			}
			updateMapValues(dateTime, cpuUtilization);
			clearLists();
			inputStream.close();
		}
	}

	public void setValues(String line)
			throws ApplicationException, InputException {
		// CPU states: 87.4% idle, 10.4% user, 2.1% kernel, 0.0% iowait, 0.0% swap
		String[] tokens = line.trim().split(COLON);
		if (tokens != null) {
			String[] results = tokens[1].split(",");
			if (results != null) {
				if (results[0].contains(IDLE)
						&& results[1].contains(USER)
						&& results[2].contains(KERNEL)) {
					String idleValue = results[0].substring(0,
							results[0].lastIndexOf("%")).trim();
					if (idleValue != null) {
						double idle = Double.parseDouble(idleValue);
						idleList.add(idle);
					}
					// }
					// if (results[1].contains("user")) {
					String userValue = results[1].substring(0,
							results[1].lastIndexOf("%")).trim();
					if (userValue != null) {
						double user = Double.parseDouble(userValue);
						userList.add(user);
					}
					// }
					// if (results[2].contains("kernel")) {
					String kernelValue = results[2].substring(0,
							results[2].lastIndexOf("%")).trim();
					if (kernelValue != null) {
						double kernel = Double.parseDouble(kernelValue);
						kernelList.add(kernel);
					}
				}
			}
		}
	}
	
	private void clearLists() {
		idleList.clear();
		userList.clear();
		kernelList.clear();
	}

	private double getPercentageValue(String value){
		Utils.trim(value);
		return Double.parseDouble(value.substring(0,value.lastIndexOf("%")));
	}
	
	private void updateMapValues(String dateTime, CPUUtilization cpuUtilization) {
		cpuUtilization.setIdleArray(minMaxAvg(idleList));
		cpuUtilization.setUserArray(minMaxAvg(userList));
		cpuUtilization.setSystemArray(minMaxAvg(kernelList));
		cpuMemoryHashmap.put(dateTime, cpuUtilization);
	}
	
	private Map<String, Integer> getHeadersIndexes(Map<String, Integer> headerIndexes, String headerLine) {
		String[] headerArr = null;
		String header = null;
		if(!Utils.isEmpty(headerLine)){
			headerLine = headerLine.toLowerCase();
			headerArr = headerLine.replaceAll(_MANY, _).split(_);
			for(int i=0; i<headerArr.length;i++){
				header = headerArr[i].trim();
				if(SYS.equals(header)){
					headerIndexes.put(SYS, i);
					continue;
				} else if (IDLE.equals(header)){
					headerIndexes.put(IDLE, i);
					continue;
				} else if (USER.equals(header)){
					headerIndexes.put(USER, i);
				}
			}
		}
		return headerIndexes;
	}


	/**
	 * Extract the output from SMPP records Hashmap and write it in the required
	 * format in the outputfile.
	 * 
	 * @param outputStream
	 */
	public void writeOutput(BufferedWriter outputStream) {
		Set keySet = cpuMemoryHashmap.keySet();
		Object[] keyObject = keySet.toArray();

		try {
			for (int i = 0; i < keyObject.length; i++) {
				CPUUtilization cpuUtilization = (CPUUtilization) cpuMemoryHashmap
						.get(keyObject[i]);
				String line = keyObject[i] + ","
						+ cpuUtilization.getIdleArray()[0] + ","
						+ cpuUtilization.getIdleArray()[1] + ","
						+ cpuUtilization.getIdleArray()[2] + ","
						+ cpuUtilization.getUserArray()[0] + ","
						+ cpuUtilization.getUserArray()[1] + ","
						+ cpuUtilization.getUserArray()[2] + ","
						+ cpuUtilization.getSystemArray()[0] + ","
						+ cpuUtilization.getSystemArray()[1] + ","
						+ cpuUtilization.getSystemArray()[2];
				outputStream.write(line);
				outputStream.newLine();
			}
		} catch (IOException e) {
			logger.error("CPUUtilizationConverter.convert() - Error" + e);
		}
	}

	private String toDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHH");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line.substring(0, 10));
		dateString = outDateFormat.format(date);
		return dateString;

	}

	private double[] minMaxAvg(List data) {
		double min, max, avg;
		Double any;
		double[] minMaxAvgArray = new double[3];
		if (data.size() > 0) {
			Iterator listIterator = data.iterator();
			any = (Double) listIterator.next();
			avg = any.doubleValue();
			max = avg;
			min = avg;
			while (listIterator.hasNext()) {
				Double element = (Double) listIterator.next();

				if (element.doubleValue() < min) {
					min = element.doubleValue();
				}
				if (element.doubleValue() > max) {
					max = element.doubleValue();
				}
				avg = avg + element.doubleValue();

			}

			minMaxAvgArray[0] = min;
			minMaxAvgArray[1] = max;
			minMaxAvgArray[2] = avg / data.size();
		}

		return minMaxAvgArray;
	}

	public class CPUUtilization {
		private double[] idleArray;
		private double[] userArray;
		private double[] systemArray;

		public double[] getIdleArray() {
			return idleArray;
		}

		public void setIdleArray(double[] idleArray) {
			this.idleArray = idleArray;
		}

		public double[] getUserArray() {
			return userArray;
		}

		public void setUserArray(double[] userArray) {
			this.userArray = userArray;
		}

		public double[] getSystemArray() {
			return systemArray;
		}

		public void setSystemArray(double[] systemArray) {
			this.systemArray = systemArray;
		}
	}

	public static void main(String[] args) throws Exception {
		try {

			PropertyReader
					.init("D:\\CMT");
			CPUUtilizationConverter converter = new CPUUtilizationConverter();
			File[] input = new File[1];
			//input[0] = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\work\\VNPP\\Vodafone\\offline_mediation_performance_monitoring_new_file_structure\\OFF_LINE_HW_STATUS_(OLD_NODE)_hwstate_hqoffmed-as1_2012030523.txt");
			input[0] = new File("D:\\Work\\CMT\\2017_Oct_DCHQRATEAPP1-5min_ramutil.txt");
			
			converter.convert(input, "MSP");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
