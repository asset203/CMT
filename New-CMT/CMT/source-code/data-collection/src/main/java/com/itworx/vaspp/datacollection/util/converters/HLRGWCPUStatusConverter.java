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
import com.itworx.vaspp.datacollection.objects.VNotificationCounter;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class HLRGWCPUStatusConverter extends AbstractTextConverter {
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		Logger logger = Logger.getLogger(systemName);
		Map<String, CPU> dateVSCPU = new HashMap<String, CPU>();
		logger
				.debug("Inside HLRGWCPUStatusConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("HLRGWCPUStatusConverter.convert() - converting file "
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
				double usr = 0.0;
				double sys = 0.0;
				double wio = 0.0;
				double idle = 0.0;
				try {
					while ((line = inputStream.readLine()) != null) {

						if (!(line.contains("date:") && line.contains("usr:")
								&& line.contains("sys:")
								&& line.contains("wio:") && line
								.contains("idle:"))) {
							continue;
						} else {
							try {
								date = getDate(filedate
										+ " "
										+ line.split("date:")[1].split("usr:")[0]
												.trim());
								usr = Double
										.parseDouble(!(line.split("usr:")[1]
												.split("sys:")[0].trim()
												.equalsIgnoreCase("")) ? line
												.split("usr:")[1].split("sys:")[0]
												.trim()
												: "0.0");
								sys = Double
										.parseDouble(!(line.split("sys:")[1]
												.split("wio:")[0].trim()
												.equalsIgnoreCase("")) ? line
												.split("sys:")[1].split("wio:")[0]
												.trim()
												: "0.0");
								wio = Double
										.parseDouble(!(line.split("wio:")[1]
												.split("idle:")[0].trim()
												.equalsIgnoreCase("")) ? line
												.split("wio:")[1]
												.split("idle:")[0].trim()
												: "0.0");
								if (line.split("idle:").length > 1) {
									idle = Double.parseDouble(!(line
											.split("idle:")[1].trim()
											.equalsIgnoreCase("")) ? line
											.split("idle:")[1].trim() : "0.0");
								}
								if (dateVSCPU.containsKey(date)) {
									CPU cpu = dateVSCPU.get(date);
									cpu.getUsrList().add(usr);
									cpu.getSysList().add(sys);
									cpu.getIdleList().add(idle);
									cpu.getWioList().add(wio);
									dateVSCPU.put(date, cpu);

								} else {
									CPU cpu = new CPU();
									cpu.getUsrList().add(usr);
									cpu.getSysList().add(sys);
									cpu.getIdleList().add(idle);
									cpu.getWioList().add(wio);
									dateVSCPU.put(date, cpu);
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
			for (Entry<String, CPU> memEntry : dateVSCPU.entrySet()) {
				Object key = memEntry.getKey();
				CPU cpu = memEntry.getValue();
				double sysList[] = minMaxAvg(cpu.getSysList());
				double idleList[] = minMaxAvg(cpu.getIdleList());
				double usrList[] = minMaxAvg(cpu.getUsrList());
				double wioList[] = minMaxAvg(cpu.getWioList());
				outputStream.write(key + "," + sysList[0] + "," + sysList[1]
						+ "," + sysList[2] + "," + idleList[0] + ","
						+ idleList[1] + "," + idleList[2] + "," + usrList[0]
						+ "," + usrList[1] + "," + usrList[2] + ","
						+ wioList[0] + "," + wioList[1] + "," + wioList[2]);
				outputStream.newLine();
			}
			outputFiles[0] = outputFile;
			logger
					.debug("HLRGWCPUStatusConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger
					.error("HLRGWCPUStatusConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("HLRGWCPUStatusConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger
							.error("HLRGWCPUStatusConverter.convert() - Couldn't read input file"
									+ e);
					throw new ApplicationException(e);
				}
			}
		}
		logger
				.debug("HLRGWCPUStatusConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private double[] minMaxAvg(List data) {
		double min, max, avg;
		Double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator();
		any = (Double) listIterator.next();
		avg = any.doubleValue();
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
			HLRGWCPUStatusConverter s = new HLRGWCPUStatusConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\build\\phase11\\DataCollection\\CPU_20111220.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class CPU {
		private List usrList = new ArrayList();
		private List sysList = new ArrayList();
		private List wioList = new ArrayList();
		private List idleList = new ArrayList();

		public List getUsrList() {
			return usrList;
		}

		public void setUsrList(List usrList) {
			this.usrList = usrList;
		}

		public List getSysList() {
			return sysList;
		}

		public void setSysList(List sysList) {
			this.sysList = sysList;
		}

		public List getWioList() {
			return wioList;
		}

		public void setWioList(List wioList) {
			this.wioList = wioList;
		}

		public List getIdleList() {
			return idleList;
		}

		public void setIdleList(List idleList) {
			this.idleList = idleList;
		}
	}
}
