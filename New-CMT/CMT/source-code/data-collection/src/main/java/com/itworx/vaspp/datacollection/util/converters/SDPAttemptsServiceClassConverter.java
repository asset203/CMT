package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SDPAttemptsServiceClassConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String, AttemptsCount> attemptsList = new HashMap<String, AttemptsCount>();

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SDPAttemptsServiceClassConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SDPAttemptsServiceClassConverter.convert() - converting file "
								+ inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				System.out.println("File [" + i + "]");
				String line;
				String date = "";
				String msisdn = null;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if (line.contains(",")) {
						if (line.contains("Date")) {
							continue;
						}
						String[] splitedLine = line.split(",");
						if (splitedLine != null && splitedLine.length > 0 && splitedLine.length >= 52) {
							try {
								date = getDate(splitedLine[0]);
								String serviceId = splitedLine[1];
								if(!isValidValue(serviceId)){
									continue;
								}
								long id = Long.parseLong(serviceId);
								if (attemptsList.containsKey(id + "_" + date)) {
									AttemptsCount object = attemptsList.get(id + "_" + date);
									if (serviceId != null) {
										if (object.serviceClassId == id
												&& object.dateTime.equals(date)) {
											try {
												if (isValidValue(splitedLine[2])) {
													object.originatingAttempts += Long
															.parseLong(splitedLine[2]);
												}
												if (isValidValue(splitedLine[3])) {
													object.terminatingAttempts += Long
															.parseLong(splitedLine[3]);
												}
												if (isValidValue(splitedLine[4])) {
													object.roamOrigAttem += Long
															.parseLong(splitedLine[4]);
												}
												if (isValidValue(splitedLine[5])) {
													object.roamTermAttem += Long
															.parseLong(splitedLine[5]);
												}
												if (isValidValue(splitedLine[6])) {
													object.OrigiServChargAttemp += Long
															.parseLong(splitedLine[6]);
												}
												if (isValidValue(splitedLine[7])) {
													object.termiServCharAttem += Long
															.parseLong(splitedLine[7]);
												}
												if (isValidValue(splitedLine[8])) {
													object.origUnsuccNTermEXT += Long
															.parseLong(splitedLine[8]);
												}
												if (isValidValue(splitedLine[9])) {
													object.termChargingTermEXT += Long
															.parseLong(splitedLine[9]);
												}
												if (isValidValue(splitedLine[10])) {
													object.roamOrigUnsuccNTermEXT += Long
															.parseLong(splitedLine[10]);
												}
												if (isValidValue(splitedLine[11])) {
													object.roamTermUnsuccessNTermEXT += Long
															.parseLong(splitedLine[11]);
												}
												if (isValidValue(splitedLine[12])) {
													object.origChargingTermEXT += Long
															.parseLong(splitedLine[12]);
												}
												if (isValidValue(splitedLine[13])) {
													object.termChargingTermEXT += Long
															.parseLong(splitedLine[13]);
												}
												if (isValidValue(splitedLine[14])) {
													object.origNTermINT += Long
															.parseLong(splitedLine[14]);
												}
												if (isValidValue(splitedLine[15])) {
													object.termNTermINT += Long
															.parseLong(splitedLine[15]);
												}

												if (isValidValue(splitedLine[16])) {
													object.roamNTermINT += Long
															.parseLong(splitedLine[16]);
												}
												if (isValidValue(splitedLine[17])) {
													object.roamTUNTINT += Long
															.parseLong(splitedLine[17]);
												}
												if (isValidValue(splitedLine[18])) {
													object.origServCharNTINT += Long
															.parseLong(splitedLine[18]);
												}
												if (isValidValue(splitedLine[19])) {
													object.termServCharNTINT += Long
															.parseLong(splitedLine[19]);
												}
												if (isValidValue(splitedLine[20])) {
													object.origCongestionEXT += Long
															.parseLong(splitedLine[20]);
												}
												if (isValidValue(splitedLine[21])) {
													object.origunsuccCongestionEXT += Long
															.parseLong(splitedLine[21]);
												}
												if (isValidValue(splitedLine[22])) {
													object.roamorigCongestionEXT += Long
															.parseLong(splitedLine[22]);
												}
												if (isValidValue(splitedLine[23])) {
													object.roamTermCongestionEXT += Long
															.parseLong(splitedLine[23]);
												}
												if (isValidValue(splitedLine[24])) {
													object.origServCongestionEXT += Long
															.parseLong(splitedLine[24]);
												}
												if (isValidValue(splitedLine[25])) {
													object.termServCongestionEXT += Long
															.parseLong(splitedLine[25]);
												}
												if (isValidValue(splitedLine[26])) {
													object.origOtherReasonEXT += Long
															.parseLong(splitedLine[26]);
												}
												if (isValidValue(splitedLine[27])) {
													object.termOtherReasonEXT += Long
															.parseLong(splitedLine[27]);
												}
												if (isValidValue(splitedLine[28])) {
													object.roamOtherReasonEXT += Long
															.parseLong(splitedLine[28]);
												}
												if (isValidValue(splitedLine[29])) {
													object.roamTermOtherReasonEXT += Long
															.parseLong(splitedLine[29]);
												}
												if (isValidValue(splitedLine[30])) {
													object.origServOtherReasonEXT += Long
															.parseLong(splitedLine[30]);
												}
												if (isValidValue(splitedLine[31])) {
													object.terServOtherReasonEXT += Long
															.parseLong(splitedLine[31]);
												}
												if (isValidValue(splitedLine[32])) {
													object.origTermINT += Long
															.parseLong(splitedLine[32]);
												}
												if (isValidValue(splitedLine[33])) {
													object.termTerminaINT += Long
															.parseLong(splitedLine[33]);
												}
												if (isValidValue(splitedLine[34])) {
													object.roamTerminationINT += Long
															.parseLong(splitedLine[34]);
												}
												if (isValidValue(splitedLine[35])) {
													object.roamTermTerminationINT += Long
															.parseLong(splitedLine[35]);
												}
												if (isValidValue(splitedLine[36])) {
													object.origSysTerminationINT += Long
															.parseLong(splitedLine[36]);
												}
												if (isValidValue(splitedLine[37])) {
													object.termSysTerminationINT += Long
															.parseLong(splitedLine[37]);
												}
												if (isValidValue(splitedLine[38])) {
													object.originatingVoiceFaxData += Long
															.parseLong(splitedLine[38]);
												}
												if (isValidValue(splitedLine[39])) {
													object.terminatingVoiceFaxData += Long
															.parseLong(splitedLine[39]);
												}
												if (isValidValue(splitedLine[40])) {
													object.originatingSMS += Long
															.parseLong(splitedLine[40]);
												}
												if (isValidValue(splitedLine[41])) {
													object.terminatingSMS += Long
															.parseLong(splitedLine[41]);
												}
												if (isValidValue(splitedLine[42])) {
													object.originatingGPRS += Long
															.parseLong(splitedLine[42]);
												}
												if (isValidValue(splitedLine[43])) {
													object.terminatingGPRS += Long
															.parseLong(splitedLine[43]);
												}
												if (isValidValue(splitedLine[44])) {
													object.originatingContent += Long
															.parseLong(splitedLine[44]);
												}
												if (isValidValue(splitedLine[45])) {
													object.terminatingContent += Long
															.parseLong(splitedLine[45]);
												}
												if (isValidValue(splitedLine[46])) {
													object.originatingVideo += Long
															.parseLong(splitedLine[46]);
												}
												if (isValidValue(splitedLine[47])) {
													object.terminatingVideo += Long
															.parseLong(splitedLine[47]);
												}
												if (isValidValue(splitedLine[48])) {
													object.originatingUnknown += Long
															.parseLong(splitedLine[48]);
												}
												if (isValidValue(splitedLine[49])) {
													object.terminatingUnknown += Long
															.parseLong(splitedLine[49]);
												}
												if (isValidValue(splitedLine[50])) {
													object.originatingSCAPv2 += Long
															.parseLong(splitedLine[50]);
												}
												if (isValidValue(splitedLine[51])) {
													object.terminatingSCAPv2 += Long
															.parseLong(splitedLine[51]);
												}
                                                if (isValidValue(splitedLine[52])) {
                                                    object.originatingGy += Long
                                                            .parseLong(splitedLine[52]);
                                                }
												attemptsList.remove(id + "_" + date);
											} catch (NumberFormatException ex) {
												ex.printStackTrace();
											}
										} else {
											object = fillAttemptObject(object,
													splitedLine, date);
										}
										attemptsList.put(id + "_" + date, object);
									}
								} else {
									AttemptsCount object = new AttemptsCount();
									object = fillAttemptObject(object,
											splitedLine, date);
									attemptsList.put(id + "_" + date, object);
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
				}
			}
			inputStream.close();
			Iterator it = attemptsList.keySet().iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				AttemptsCount object = attemptsList.get(obj);
				outputStream
						.write(object.dateTime + "," + object.serviceClassId
								+ "," + object.originatingAttempts + ","
								+ object.terminatingAttempts + ","
								+ object.roamOrigAttem + ","
								+ object.roamTermAttem + ","
								+ object.OrigiServChargAttemp + ","
								+ object.termiServCharAttem + ","
								+ object.origUnsuccNTermEXT + ","
								+ object.termUnsuccNTermEXT + ","
								+ object.roamOrigUnsuccNTermEXT + ","
								+ object.roamTermUnsuccessNTermEXT + ","
								+ object.origChargingTermEXT + ","
								+ object.termChargingTermEXT + ","
								+ object.origNTermINT + ","
								+ object.termNTermINT + ","
								+ object.roamNTermINT + ","
								+ object.roamTUNTINT + ","
								+ object.origServCharNTINT + ","
								+ object.termServCharNTINT + ","
								+ object.origCongestionEXT + ","
								+ object.origunsuccCongestionEXT + ","
								+ object.roamorigCongestionEXT + ","
								+ object.roamTermCongestionEXT + ","
								+ object.origServCongestionEXT + ","
								+ object.termServCongestionEXT + ","
								+ object.origOtherReasonEXT + ","
								+ object.termOtherReasonEXT + ","
								+ object.roamOtherReasonEXT + ","
								+ object.roamTermOtherReasonEXT + ","
								+ object.origServOtherReasonEXT + ","
								+ object.terServOtherReasonEXT + ","
								+ object.origTermINT + ","
								+ object.termTerminaINT + ","
								+ object.roamTerminationINT + ","
								+ object.roamTermTerminationINT + ","
								+ object.origSysTerminationINT + ","
								+ object.termSysTerminationINT + ","
								+ object.originatingVoiceFaxData + ","
								+ object.terminatingVoiceFaxData + ","
								+ object.originatingSMS + ","
								+ object.terminatingSMS + ","
								+ object.originatingGPRS + ","
								+ object.terminatingGPRS + ","
								+ object.originatingContent + ","
								+ object.terminatingContent + ","
								+ object.originatingVideo + ","
								+ object.terminatingVideo + ","
								+ object.originatingUnknown + ","
								+ object.terminatingUnknown + ","
								+ object.originatingSCAPv2 + ","
								+ object.terminatingSCAPv2 + ","
								+ object.originatingGy);
				outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0] = outputFile;
			logger
					.debug("RamaUssdConnectorConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger
					.error("RamaUssdConnectorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("RamaUssdConnectorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("RamaUssdConnectorConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	private AttemptsCount fillAttemptObject(AttemptsCount object,
			String[] splitedLine, String date) throws NumberFormatException {
		object.dateTime = date;
		if (isValidValue(splitedLine[1])) {
			object.serviceClassId = Long.parseLong(splitedLine[1]);
		}
		if (isValidValue(splitedLine[2])) {
			object.originatingAttempts = Long.parseLong(splitedLine[2]);
		}
		if (isValidValue(splitedLine[3])) {
			object.terminatingAttempts = Long.parseLong(splitedLine[3]);
		}
		if (isValidValue(splitedLine[4])) {
			object.roamOrigAttem = Long.parseLong(splitedLine[4]);
		}
		if (isValidValue(splitedLine[5])) {
			object.roamTermAttem = Long.parseLong(splitedLine[5]);
		}
		if (isValidValue(splitedLine[6])) {
			object.OrigiServChargAttemp = Long.parseLong(splitedLine[6]);
		}
		if (isValidValue(splitedLine[7])) {
			object.termiServCharAttem = Long.parseLong(splitedLine[7]);
		}
		if (isValidValue(splitedLine[8])) {
			object.origUnsuccNTermEXT = Long.parseLong(splitedLine[8]);
		}
		if (isValidValue(splitedLine[9])) {
			object.termChargingTermEXT = Long.parseLong(splitedLine[9]);
		}
		if (isValidValue(splitedLine[10])) {
			object.roamOrigUnsuccNTermEXT = Long.parseLong(splitedLine[10]);
		}
		if (isValidValue(splitedLine[11])) {
			object.roamTermUnsuccessNTermEXT = Long.parseLong(splitedLine[11]);
		}
		if (isValidValue(splitedLine[12])) {
			object.origChargingTermEXT = Long.parseLong(splitedLine[12]);
		}
		if (isValidValue(splitedLine[13])) {
			object.termChargingTermEXT = Long.parseLong(splitedLine[13]);
		}
		if (isValidValue(splitedLine[14])) {
			object.origNTermINT = Long.parseLong(splitedLine[14]);
		}
		if (isValidValue(splitedLine[15])) {
			object.termNTermINT = Long.parseLong(splitedLine[15]);
		}
		if (isValidValue(splitedLine[16])) {
			object.roamNTermINT = Long.parseLong(splitedLine[16]);
		}
		if (isValidValue(splitedLine[17])) {
			object.roamTUNTINT = Long.parseLong(splitedLine[17]);
		}
		if (isValidValue(splitedLine[18])) {
			object.origServCharNTINT = Long.parseLong(splitedLine[18]);
		}
		if (isValidValue(splitedLine[19])) {
			object.termServCharNTINT = Long.parseLong(splitedLine[19]);
		}
		if (isValidValue(splitedLine[20])) {
			object.origCongestionEXT = Long.parseLong(splitedLine[20]);
		}
		if (isValidValue(splitedLine[21])) {
			object.origunsuccCongestionEXT = Long.parseLong(splitedLine[21]);
		}
		if (isValidValue(splitedLine[22])) {
			object.roamorigCongestionEXT = Long.parseLong(splitedLine[22]);
		}
		if (isValidValue(splitedLine[23])) {
			object.roamTermCongestionEXT = Long.parseLong(splitedLine[23]);
		}
		if (isValidValue(splitedLine[24])) {
			object.origServCongestionEXT = Long.parseLong(splitedLine[24]);
		}
		if (isValidValue(splitedLine[25])) {
			object.termServCongestionEXT = Long.parseLong(splitedLine[25]);
		}
		if (isValidValue(splitedLine[26])) {
			object.origOtherReasonEXT = Long.parseLong(splitedLine[26]);
		}
		if (isValidValue(splitedLine[27])) {
			object.termOtherReasonEXT = Long.parseLong(splitedLine[27]);
		}
		if (isValidValue(splitedLine[28])) {
			object.roamOtherReasonEXT = Long.parseLong(splitedLine[28]);
		}
		if (isValidValue(splitedLine[29])) {
			object.roamTermOtherReasonEXT = Long.parseLong(splitedLine[29]);
		}
		if (isValidValue(splitedLine[30])) {
			object.origServOtherReasonEXT = Long.parseLong(splitedLine[30]);
		}
		if (isValidValue(splitedLine[31])) {
			object.terServOtherReasonEXT = Long.parseLong(splitedLine[31]);
		}
		if (isValidValue(splitedLine[32])) {
			object.origTermINT = Long.parseLong(splitedLine[32]);
		}
		if (isValidValue(splitedLine[33])) {
			object.termTerminaINT = Long.parseLong(splitedLine[33]);
		}
		if (isValidValue(splitedLine[34])) {
			object.roamTerminationINT = Long.parseLong(splitedLine[34]);
		}
		if (isValidValue(splitedLine[35])) {
			object.roamTermTerminationINT = Long.parseLong(splitedLine[35]);
		}
		if (isValidValue(splitedLine[36])) {
			object.origSysTerminationINT = Long.parseLong(splitedLine[36]);
		}
		if (isValidValue(splitedLine[37])) {
			object.termSysTerminationINT = Long.parseLong(splitedLine[37]);
		}
		if (isValidValue(splitedLine[38])) {
			object.originatingVoiceFaxData = Long.parseLong(splitedLine[38]);
		}
		if (isValidValue(splitedLine[39])) {
			object.terminatingVoiceFaxData = Long.parseLong(splitedLine[39]);
		}
		if (isValidValue(splitedLine[40])) {
			object.originatingSMS = Long.parseLong(splitedLine[40]);
		}
		if (isValidValue(splitedLine[41])) {
			object.terminatingSMS = Long.parseLong(splitedLine[41]);
		}
		if (isValidValue(splitedLine[42])) {
			object.originatingGPRS = Long.parseLong(splitedLine[42]);
		}
		if (isValidValue(splitedLine[43])) {
			object.terminatingGPRS = Long.parseLong(splitedLine[43]);
		}
		if (isValidValue(splitedLine[44])) {
			object.originatingContent = Long.parseLong(splitedLine[44]);
		}
		if (isValidValue(splitedLine[45])) {
			object.terminatingContent = Long.parseLong(splitedLine[45]);
		}
		if (isValidValue(splitedLine[46])) {
			object.originatingVideo = Long.parseLong(splitedLine[46]);
		}
		if (isValidValue(splitedLine[47])) {
			object.terminatingVideo = Long.parseLong(splitedLine[47]);
		}
		if (isValidValue(splitedLine[48])) {
			object.originatingUnknown = Long.parseLong(splitedLine[48]);
		}
		if (isValidValue(splitedLine[49])) {
			object.terminatingUnknown = Long.parseLong(splitedLine[49]);
		}
		if (isValidValue(splitedLine[50])) {
			object.originatingSCAPv2 = Long.parseLong(splitedLine[50]);
		}
		if (isValidValue(splitedLine[51])) {
			object.terminatingSCAPv2 = Long.parseLong(splitedLine[51]);
		}
        if (isValidValue(splitedLine[52])) {
            object.originatingGy = Long.parseLong(splitedLine[52]);
        }
		return object;
	}

	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	private boolean isValidValue(String value) {
		if (value != null && !value.equals("")) {
			return true;
		}
		return false;
	}

	public static void main(String ag[]) {
		try {

			PropertyReader
					.init("D:\\Projects\\VNNP_\\SourceCode\\DataCollection");
			SDPAttemptsServiceClassConverter s = new SDPAttemptsServiceClassConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\Projects\\VNNP_\\SourceCode\\DataCollection\\ZASDP45A_PSC-TrafficHandler_8.1_A_1_ServiceClass.20110808_2300.stat");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class AttemptsCount {
		public String dateTime;
		public long serviceClassId;
		public long originatingAttempts;
		public long terminatingAttempts;
		public long roamOrigAttem;
		public long roamTermAttem;
		public long OrigiServChargAttemp;
		public long termiServCharAttem;
		public long origUnsuccNTermEXT;
		public long termUnsuccNTermEXT;
		public long roamOrigUnsuccNTermEXT;
		public long roamTermUnsuccessNTermEXT;
		public long origChargingTermEXT;
		public long termChargingTermEXT;
		public long origNTermINT;
		public long termNTermINT;
		public long roamNTermINT;
		public long roamTUNTINT;
		public long origServCharNTINT;
		public long termServCharNTINT;
		public long origCongestionEXT;
		public long origunsuccCongestionEXT;
		public long roamorigCongestionEXT;
		public long roamTermCongestionEXT;
		public long origServCongestionEXT;
		public long termServCongestionEXT;
		public long origOtherReasonEXT;
		public long termOtherReasonEXT;
		public long roamOtherReasonEXT;
		public long roamTermOtherReasonEXT;
		public long origServOtherReasonEXT;
		public long terServOtherReasonEXT;
		public long origTermINT;
		public long termTerminaINT;
		public long roamTerminationINT;
		public long roamTermTerminationINT;
		public long origSysTerminationINT;
		public long termSysTerminationINT;
		public long originatingVoiceFaxData;
		public long terminatingVoiceFaxData;
		public long originatingSMS;
		public long terminatingSMS;
		public long originatingGPRS;
		public long terminatingGPRS;
		public long originatingContent;
		public long terminatingContent;
		public long originatingVideo;
		public long terminatingVideo;
		public long originatingUnknown;
		public long terminatingUnknown;
		public long originatingSCAPv2;
		public long terminatingSCAPv2;
		public long originatingGy;
	}
}
