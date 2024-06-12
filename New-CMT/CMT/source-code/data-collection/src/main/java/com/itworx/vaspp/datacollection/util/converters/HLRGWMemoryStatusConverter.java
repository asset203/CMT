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
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class HLRGWMemoryStatusConverter extends AbstractTextConverter{
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		Logger logger = Logger.getLogger(systemName);
		Map<String, Memory> dateVSMemory = new HashMap<String, Memory>();
		logger
				.debug("Inside HLRGWMemoryStatusConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("HLRGWMemoryStatusConverter.convert() - converting file "
								+ inputFiles[i].getName());
				String filedate = "";
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				if (inputFiles[i].getName().contains("_")
						&& inputFiles[i].getName().contains(".")) {
					filedate = (inputFiles[i].getName().split("_")[inputFiles[i]
							.getName().split("_").length - 1]).split("\\.")[0];

				}
				String line;
				String date;
				long freeMem = 0;
				long  freeSwap = 0;

				try {
					while ((line = inputStream.readLine()) != null) {

						if (!(line.contains("date:")
								&& line.contains("FreeMem:") && line
								.contains("freeSWAP:")&& line
								.contains("total:"))) {
							continue;
						} else {
							try {
								date = getDate(filedate
										+ " "
										+ line.split("date:")[1]
												.split("FreeMem:")[0].trim());
								
								freeMem = Long.parseLong(!(line.split("FreeMem:")[1].split("freeSWAP:")[0].trim().equalsIgnoreCase("")) ? line.split("FreeMem:")[1].split("freeSWAP:")[0].trim() : "0");
								freeSwap = Long.parseLong(!(line.split("freeSWAP:")[1].split("total:")[0].trim().equalsIgnoreCase("")) ? line.split("freeSWAP:")[1].split("total:")[0].trim() : "0");
								
								if (dateVSMemory.containsKey(date)) {
									
									 Memory mem = dateVSMemory.get(date);
									 mem.getFreeMem().add(freeMem);
									 mem.getFreeSwap().add(freeSwap);									
									 dateVSMemory.put(date, mem);
									

								} else {
									
									 Memory mem = new Memory();
									 mem.getFreeMem().add(freeMem);
									 mem.getFreeSwap().add(freeSwap);									
									 dateVSMemory.put(date, mem);
									 
								}

							} catch (ParseException exc) {
								logger.error(exc);
								continue;
							} catch (NumberFormatException exc) {
								logger.error(exc);
								continue;
							}

						}
					}
				} catch (Exception e) {
					logger.error(e);
				} finally {
					inputStream.close();
				}
			}// end of the for loop
			
			  for (Entry<String, Memory> memEntry : dateVSMemory.entrySet()) {
			  Object key = memEntry.getKey(); Memory mem = memEntry.getValue();
			  double freeMemList[] = minMaxAvg(mem.getFreeMem()); 
			  double freeSwapList[]= minMaxAvg(mem.freeSwap);
			  double avgMemUtilization=freeMemList[2]/32000000;
			  outputStream.write(key + "," +
					  freeMemList[0] + "," + freeMemList[1] + "," + freeMemList[2] + "," +
					  freeSwapList[0] + "," + freeSwapList[1] + "," + freeSwapList[2] + "," +avgMemUtilization);
			  outputStream.newLine(); }
			 
			outputFiles[0] = outputFile;
			logger
					.debug("HLRGWMemoryStatusConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger
					.error("HLRGWMemoryStatusConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("HLRGWMemoryStatusConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger
							.error("HLRGWMemoryStatusConverter.convert() - Couldn't read input file"
									+ e);
					throw new ApplicationException(e);
				}
			}
		}
		logger
				.debug("HLRGWMemoryStatusConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private double[] minMaxAvg(List data) {
		double min, max, avg;
		double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator();
		any = (Long) listIterator.next();
		avg = any;
		max = avg;
		min = avg;

		while (listIterator.hasNext()) {
			double element = (Long) listIterator.next();

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

	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyyMMdd HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\phase11\\DataCollection");
			HLRGWMemoryStatusConverter s = new HLRGWMemoryStatusConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\build\\phase11\\DataCollection\\VMMemory_20111221.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class Memory
	{
		private List freeMem= new ArrayList();
		private List freeSwap= new ArrayList();
		public List getFreeMem() {
			return freeMem;
		}
		public void setFreeMem(List freeMem) {
			this.freeMem = freeMem;
		}
		public List getFreeSwap() {
			return freeSwap;
		}
		public void setFreeSwap(List freeSwap) {
			this.freeSwap = freeSwap;
		}
	}
}
