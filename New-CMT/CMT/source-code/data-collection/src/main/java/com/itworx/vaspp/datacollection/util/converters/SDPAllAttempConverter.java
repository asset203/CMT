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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class SDPAllAttempConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,AllAttempts> dateVSobjSum=new HashMap<String,AllAttempts>() ;
public SDPAllAttempConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside SDPAllAttempConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("SDPAllAttempConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line;
			String date = "";
			long Originating=0;
			long Forwarded=0;
			long Terminating=0;
			long Roamingoriginating=0;
			long Roamingforwarded=0;
			long Roamingterminating=0;
			long USSDbasedcallback=0;
			long Originatingservicecharging=0;
			long Terminatingservicecharging=0;
			long Voice=0;
			long Fax=0;
			long Data=0;
			long Unknown=0;
			long SMS=0;
			long GPRS=0;
			long Content=0;
			long Videotelephony=0;
			long Videoconference=0;
			long CDREXT1=0;
			long CDREXT2=0;
			long CDREXT3=0;
			long CDREXT4=0;
			long CDREXT5=0;
			long CDREXT6=0;
			long CDREXT7=0;
			long CDREXT8=0;
			long CDREXT9=0;
			long CDREXT10=0;
			long Successfulsetup=0;
			long SuccessfulreleasedEXTnetwork=0;
			long Callinvokedchargingcancelled=0;
			long Unsuccsystemtermination=0;
			long Unsuccessfulcongestion=0;
			long Unsuccessfulnormaltermination=0;
			long Unsuccessfulotherreason=0;
			long Unnormaltermination=0;
			long Communitycharging=0;
			long Communityindicatorfound=0;
			long Subscribernotfound=0;
			long Internalerror=0;
			long Ratingfailed=0;
			long CFTChargedUnsuccessfulSetup=0;
			long CFTChargedCallsExpiry=0;
			while (inputStream.ready()) {
				  line = inputStream.readLine();
				if(line.contains("Date"))
					continue;
				else
				{
					if(line.contains(",")&&line.split(",").length>=44)
					{
						String columns []=line.split(",");
						try {
						date=getDate(columns[0]);						
						Originating= Long.parseLong(!columns[1].equalsIgnoreCase("")?columns[1]:"0");
						Forwarded= Long.parseLong(!columns[2].equalsIgnoreCase("")?columns[2]:"0");
						Terminating=Long.parseLong(!columns[3].equalsIgnoreCase("")?columns[3]:"0");
						Roamingoriginating=Long.parseLong(!columns[4].equalsIgnoreCase("")?columns[4]:"0");
						Roamingforwarded=Long.parseLong(!columns[5].equalsIgnoreCase("")?columns[5]:"0");
						Roamingterminating=Long.parseLong(!columns[6].equalsIgnoreCase("")?columns[6]:"0");
						USSDbasedcallback=Long.parseLong(!columns[7].equalsIgnoreCase("")?columns[7]:"0");
						Originatingservicecharging=Long.parseLong(!columns[8].equalsIgnoreCase("")?columns[8]:"0");
						Terminatingservicecharging=Long.parseLong(!columns[9].equalsIgnoreCase("")?columns[9]:"0");
						Voice=Long.parseLong(!columns[10].equalsIgnoreCase("")?columns[10]:"0");
						Fax=Long.parseLong(!columns[11].equalsIgnoreCase("")?columns[11]:"0");
						Data=Long.parseLong(!columns[12].equalsIgnoreCase("")?columns[12]:"0");
						Unknown=Long.parseLong(!columns[13].equalsIgnoreCase("")?columns[13]:"0");
						SMS=Long.parseLong(!columns[14].equalsIgnoreCase("")?columns[14]:"0");
						GPRS=Long.parseLong(!columns[15].equalsIgnoreCase("")?columns[15]:"0");
						Content=Long.parseLong(!columns[16].equalsIgnoreCase("")?columns[16]:"0");
						Videotelephony=Long.parseLong(!columns[17].equalsIgnoreCase("")?columns[17]:"0");
						Videoconference=Long.parseLong(!columns[18].equalsIgnoreCase("")?columns[18]:"0");
						CDREXT1=Long.parseLong(!columns[19].equalsIgnoreCase("")?columns[19]:"0");
						CDREXT2=Long.parseLong(!columns[20].equalsIgnoreCase("")?columns[20]:"0");
						CDREXT3=Long.parseLong(!columns[21].equalsIgnoreCase("")?columns[21]:"0");
						CDREXT4=Long.parseLong(!columns[22].equalsIgnoreCase("")?columns[22]:"0");
						
						CDREXT5=Long.parseLong(!columns[23].equalsIgnoreCase("")?columns[23]:"0");
						CDREXT6=Long.parseLong(!columns[24].equalsIgnoreCase("")?columns[24]:"0");
						CDREXT7=Long.parseLong(!columns[25].equalsIgnoreCase("")?columns[25]:"0");
						CDREXT8=Long.parseLong(!columns[26].equalsIgnoreCase("")?columns[26]:"0");
						CDREXT9=Long.parseLong(!columns[27].equalsIgnoreCase("")?columns[27]:"0");
						CDREXT10=Long.parseLong(!columns[28].equalsIgnoreCase("")?columns[28]:"0");
						Successfulsetup=Long.parseLong(!columns[29].equalsIgnoreCase("")?columns[29]:"0");
						SuccessfulreleasedEXTnetwork=Long.parseLong(!columns[30].equalsIgnoreCase("")?columns[30]:"0");
						Callinvokedchargingcancelled=Long.parseLong(!columns[31].equalsIgnoreCase("")?columns[31]:"0");
						Unsuccsystemtermination=Long.parseLong(!columns[32].equalsIgnoreCase("")?columns[32]:"0");
						Unsuccessfulcongestion=Long.parseLong(!columns[33].equalsIgnoreCase("")?columns[33]:"0");
					    Unsuccessfulnormaltermination=Long.parseLong(!columns[34].equalsIgnoreCase("")?columns[34]:"0");
					    Unsuccessfulotherreason=Long.parseLong(!columns[35].equalsIgnoreCase("")?columns[35]:"0");
					    Unnormaltermination=Long.parseLong(!columns[36].equalsIgnoreCase("")?columns[36]:"0");
					    Communitycharging=Long.parseLong(!columns[37].equalsIgnoreCase("")?columns[37]:"0");
					    Communityindicatorfound=Long.parseLong(!columns[38].equalsIgnoreCase("")?columns[38]:"0");
					    Subscribernotfound=Long.parseLong(!columns[39].equalsIgnoreCase("")?columns[39]:"0");
					    Internalerror=Long.parseLong(!columns[40].equalsIgnoreCase("")?columns[40]:"0");
					    Ratingfailed=Long.parseLong(!columns[41].equalsIgnoreCase("")?columns[41]:"0");
					    CFTChargedUnsuccessfulSetup=Long.parseLong(!columns[42].equalsIgnoreCase("")?columns[42]:"0");
					    CFTChargedCallsExpiry=Long.parseLong(!columns[43].equalsIgnoreCase("")?columns[43]:"0");
						
						if(dateVSobjSum.containsKey(date))
						{
							AllAttempts obj=(AllAttempts)dateVSobjSum.get(date);
							obj.setOriginating(obj.getOriginating()+Originating);
							obj.setForward(obj.getForward()+Forwarded);
							obj.setTerminating(obj.getTerminating()+Terminating);
							obj.setRoamingoriginating(obj.getRoamingoriginating()+Roamingoriginating);
							obj.setRoamingforwarded(obj.getRoamingforwarded()+Roamingforwarded);
							obj.setRoamingterminating(obj.getRoamingterminating()+Roamingterminating);
							obj.setUSSDbasedcallback(obj.getUSSDbasedcallback()+USSDbasedcallback);
							obj.setOriginatingservicecharging(obj.getOriginatingservicecharging()+Originatingservicecharging);
							obj.setTerminatingservicecharging(obj.getTerminatingservicecharging()+Terminatingservicecharging);
							obj.setVoice(obj.getVoice()+Voice);
							obj.setFax(obj.getFax()+Fax);
							obj.setData(obj.getData()+Data);
							obj.setUnknown(obj.getUnknown()+Unknown);
							obj.setSMS(obj.getSMS()+SMS);
							obj.setGPRS(obj.getGPRS()+GPRS);
							obj.setContent(obj.getContent()+Content);
							obj.setVideotelephony(obj.getVideotelephony()+Videotelephony);
							obj.setVideoconference(obj.getVideoconference()+Videoconference);
							obj.setCDREXT1(obj.getCDREXT1()+CDREXT1);
							obj.setCDREXT2(obj.getCDREXT2()+CDREXT2);
							obj.setCDREXT3(obj.getCDREXT3()+CDREXT3);
							obj.setCDREXT4(obj.getCDREXT4()+CDREXT4);
							
							obj.setCDREXT5(obj.getCDREXT5()+CDREXT5);
							obj.setCDREXT6(obj.getCDREXT6()+CDREXT6);
							obj.setCDREXT7(obj.getCDREXT7()+CDREXT7);
							obj.setCDREXT8(obj.getCDREXT8()+CDREXT8);
							obj.setCDREXT9(obj.getCDREXT9()+CDREXT9);
							obj.setCDREXT10(obj.getCDREXT10()+CDREXT10);
							obj.setSuccessfulsetup(obj.getSuccessfulsetup()+Successfulsetup);
							obj.setSuccessfulreleasedEXTnetwork(obj.getSuccessfulreleasedEXTnetwork()+SuccessfulreleasedEXTnetwork);
							obj.setCallinvokedchargingcancelled(obj.getCallinvokedchargingcancelled()+Callinvokedchargingcancelled);
							obj.setUnsuccsystemtermination(obj.getUnsuccsystemtermination()+Unsuccsystemtermination);
							obj.setUnsuccessfulcongestion(obj.getUnsuccessfulcongestion()+Unsuccessfulcongestion);
							obj.setUnsuccessfulnormaltermination(obj.getUnsuccessfulnormaltermination()+Unsuccessfulnormaltermination);
							obj.setUnsuccessfulotherreason(obj.getUnsuccessfulotherreason()+Unsuccessfulotherreason);
							obj.setUnnormaltermination(obj.getUnnormaltermination()+Unnormaltermination);
							obj.setCommunitycharging(obj.getCommunitycharging()+Communitycharging);
							obj.setCommunityindicatorfound(obj.getCommunityindicatorfound()+Communityindicatorfound);
							obj.setSubscribernotfound(obj.getSubscribernotfound()+Subscribernotfound);
							obj.setInternalerror(obj.getInternalerror()+Internalerror);
							obj.setRatingfailed(obj.getRatingfailed()+Ratingfailed);
							obj.setCFTChargedUnsuccessfulSetup(obj.getCFTChargedUnsuccessfulSetup()+CFTChargedUnsuccessfulSetup);
							obj.setCFTChargedCallsExpiry(obj.getCFTChargedCallsExpiry()+CFTChargedCallsExpiry);
							dateVSobjSum.put(date, obj);
						}else
						{
							AllAttempts obj= new AllAttempts ();
							obj.setOriginating(Originating);
							obj.setForward(Forwarded);
							obj.setTerminating(Terminating);
							obj.setRoamingoriginating(Roamingoriginating);
							obj.setRoamingforwarded(Roamingforwarded);
							obj.setRoamingterminating(Roamingterminating);
							obj.setUSSDbasedcallback(USSDbasedcallback);
							obj.setOriginatingservicecharging(Originatingservicecharging);
							obj.setTerminatingservicecharging(Terminatingservicecharging);
							obj.setVoice(Voice);
							obj.setFax(Fax);
							obj.setData(Data);
							obj.setUnknown(Unknown);
							obj.setSMS(SMS);
							obj.setGPRS(GPRS);
							obj.setContent(Content);
							obj.setVideotelephony(Videotelephony);
							obj.setVideoconference(Videoconference);
							obj.setCDREXT1(CDREXT1);
							obj.setCDREXT2(CDREXT2);
							obj.setCDREXT3(CDREXT3);
							obj.setCDREXT4(CDREXT4);
							obj.setCDREXT5(CDREXT5);
							obj.setCDREXT6(CDREXT6);
							obj.setCDREXT7(CDREXT7);
							obj.setCDREXT8(CDREXT8);
							obj.setCDREXT9(CDREXT9);
							obj.setCDREXT10(CDREXT10);
							obj.setSuccessfulsetup(Successfulsetup);
							obj.setSuccessfulreleasedEXTnetwork(SuccessfulreleasedEXTnetwork);
							obj.setCallinvokedchargingcancelled(Callinvokedchargingcancelled);
							obj.setUnsuccsystemtermination(Unsuccsystemtermination);
							obj.setUnsuccessfulcongestion(Unsuccessfulcongestion);
							obj.setUnsuccessfulnormaltermination(Unsuccessfulnormaltermination);
							obj.setUnsuccessfulotherreason(Unsuccessfulotherreason);
							obj.setUnnormaltermination(Unnormaltermination);
							obj.setCommunitycharging(Communitycharging);
							obj.setCommunityindicatorfound(Communityindicatorfound);
							obj.setSubscribernotfound(Subscribernotfound);
							obj.setInternalerror(Internalerror);
							obj.setRatingfailed(Ratingfailed);
							obj.setCFTChargedUnsuccessfulSetup(CFTChargedUnsuccessfulSetup);
							obj.setCFTChargedCallsExpiry(CFTChargedCallsExpiry);
							dateVSobjSum.put(date, obj);
						}
						}catch(ParseException exc){ logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){ logger.error(exc) ; System.out.println("hi");continue ;}
					}
				}
			          }
			
		}
		Iterator it=dateVSobjSum.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			AllAttempts obj=(AllAttempts)dateVSobjSum.get(key);
			outputStream.write(key.toString()+","+obj.getOriginating()+","+obj.getForward()+","+obj.getTerminating()+","+obj.getRoamingoriginating()+","+obj.getRoamingforwarded()+","+obj.getRoamingterminating()+","+obj.getUSSDbasedcallback()+","+obj.getOriginatingservicecharging()+","+obj.getTerminatingservicecharging()+","+obj.getVoice()+","+
					obj.getFax()+","+obj.getData()+","+obj.getUnknown()+","+obj.getSMS()+","+obj.getGPRS()+","+obj.getContent()+","+obj.getVideotelephony()+","+obj.getVideoconference()+","+obj.getCDREXT1()+","+obj.getCDREXT2()+","+obj.getCDREXT3()+","+obj.getCDREXT4()+","+obj.getCDREXT5()+","+obj.getCDREXT6()+","+obj.getCDREXT7()+","+obj.getCDREXT8()+","+
					obj.getCDREXT9()+","+obj.getCDREXT10()+","+obj.getSuccessfulsetup()+","+obj.getSuccessfulreleasedEXTnetwork()+","+obj.getCallinvokedchargingcancelled()+","+obj.getUnsuccsystemtermination()+","+obj.getUnsuccessfulcongestion()+","+obj.getUnsuccessfulnormaltermination()+","+obj.getUnsuccessfulotherreason()+","+obj.getUnnormaltermination()+","+obj.getCommunitycharging()+","+
					obj.getCommunityindicatorfound()+","+obj.getSubscribernotfound()+","+obj.getInternalerror()+","+obj.getRatingfailed()+","+obj.getCFTChargedUnsuccessfulSetup()+","+obj.getCFTChargedCallsExpiry());
			System.out.println(key.toString()+","+obj.getOriginating()+","+obj.getForward()+","+obj.getTerminating()+","+obj.getRoamingoriginating()+","+obj.getRoamingforwarded()+","+obj.getRoamingterminating()+","+obj.getUSSDbasedcallback()+","+obj.getOriginatingservicecharging()+","+obj.getTerminatingservicecharging()+","+obj.getVoice()+","+
					obj.getFax()+","+obj.getData()+","+obj.getUnknown()+","+obj.getSMS()+","+obj.getGPRS()+","+obj.getContent()+","+obj.getVideotelephony()+","+obj.getVideoconference()+","+obj.getCDREXT1()+","+obj.getCDREXT2()+","+obj.getCDREXT3()+","+obj.getCDREXT4()+","+obj.getCDREXT5()+","+obj.getCDREXT6()+","+obj.getCDREXT7()+","+obj.getCDREXT8()+","+
					obj.getCDREXT9()+","+obj.getCDREXT10()+","+obj.getSuccessfulsetup()+","+obj.getSuccessfulreleasedEXTnetwork()+","+obj.getCallinvokedchargingcancelled()+","+obj.getUnsuccsystemtermination()+","+obj.getUnsuccessfulcongestion()+","+obj.getUnsuccessfulnormaltermination()+","+obj.getUnsuccessfulotherreason()+","+obj.getUnnormaltermination()+","+obj.getCommunitycharging()+","+
					obj.getCommunityindicatorfound()+","+obj.getSubscribernotfound()+","+obj.getInternalerror()+","+obj.getRatingfailed()+","+obj.getCFTChargedUnsuccessfulSetup()+","+obj.getCFTChargedCallsExpiry());
			outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("SDPAllAttempConverter.convert() - finished converting input files successfully ");

	
}
	catch (FileNotFoundException e) {
			logger
					.error("SDPAllAttempConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("SDPAllAttempConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("SDPAllAttempConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
	}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HHmm");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\SourceCode\\DataCollection");
		SDPAllAttempConverter s = new SDPAllAttempConverter();
		File[] input = new File[1];
		input[0]=new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase1\\Trunk\\Builds\\22-10-2012\\HQSDP55A_PSC-TrafficHandler_8.1_A_1_System.20121022_1100.stat");
		   s.convert(input,"Maha_Test");
		System.out.println("FINISHED ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class AllAttempts
{
	long originating =0;
	long Forward=0;
	long Terminating=0;
	long Roamingoriginating=0;
	long Roamingforwarded=0;
	long Roamingterminating=0;
	long USSDbasedcallback=0;
	long Originatingservicecharging=0;
	long Terminatingservicecharging=0;
	long Voice=0;
	long Fax=0;
	long Data=0;
	long Unknown=0;
	long SMS=0;
	long GPRS=0;
	long Content=0;
	long Videotelephony=0;
	long Videoconference=0;
	long CDREXT1=0;
	long CDREXT2=0;
	long CDREXT3=0;
	long CDREXT4=0;
	long CDREXT5=0;
	long CDREXT6=0;
	long CDREXT7=0;
	long CDREXT8=0;
	long CDREXT9=0;
	long CDREXT10=0;
	long Successfulsetup=0;
	public long getSuccessfulsetup() {
		return Successfulsetup;
	}

	public void setSuccessfulsetup(long successfulsetup) {
		Successfulsetup = successfulsetup;
	}

	public long getCDREXT5() {
		return CDREXT5;
	}

	public void setCDREXT5(long cDREXT5) {
		CDREXT5 = cDREXT5;
	}

	public long getCDREXT6() {
		return CDREXT6;
	}

	public void setCDREXT6(long cDREXT6) {
		CDREXT6 = cDREXT6;
	}

	public long getCDREXT7() {
		return CDREXT7;
	}

	public void setCDREXT7(long cDREXT7) {
		CDREXT7 = cDREXT7;
	}

	public long getCDREXT8() {
		return CDREXT8;
	}

	public void setCDREXT8(long cDREXT8) {
		CDREXT8 = cDREXT8;
	}

	public long getCDREXT9() {
		return CDREXT9;
	}

	public void setCDREXT9(long cDREXT9) {
		CDREXT9 = cDREXT9;
	}

	public long getCDREXT10() {
		return CDREXT10;
	}

	public void setCDREXT10(long cDREXT10) {
		CDREXT10 = cDREXT10;
	}

	public long getCDREXT3() {
		return CDREXT3;
	}

	public void setCDREXT3(long cDREXT3) {
		CDREXT3 = cDREXT3;
	}

	public long getCDREXT4() {
		return CDREXT4;
	}

	public void setCDREXT4(long cDREXT4) {
		CDREXT4 = cDREXT4;
	}

	public long getCDREXT2() {
		return CDREXT2;
	}

	public void setCDREXT2(long cDREXT2) {
		CDREXT2 = cDREXT2;
	}

	public long getCDREXT1() {
		return CDREXT1;
	}

	public void setCDREXT1(long cDREXT1) {
		CDREXT1 = cDREXT1;
	}

	public long getVideoconference() {
		return Videoconference;
	}

	public void setVideoconference(long videoconference) {
		Videoconference = videoconference;
	}

	public long getVideotelephony() {
		return Videotelephony;
	}

	public void setVideotelephony(long videotelephony) {
		Videotelephony = videotelephony;
	}

	public long getContent() {
		return Content;
	}

	public void setContent(long content) {
		Content = content;
	}

	public long getGPRS() {
		return GPRS;
	}

	public void setGPRS(long gPRS) {
		GPRS = gPRS;
	}

	public long getSMS() {
		return SMS;
	}

	public void setSMS(long sMS) {
		SMS = sMS;
	}

	public long getUnknown() {
		return Unknown;
	}

	public void setUnknown(long unknown) {
		Unknown = unknown;
	}

	public long getData() {
		return Data;
	}

	public void setData(long data) {
		Data = data;
	}

	public long getFax() {
		return Fax;
	}

	public void setFax(long fax) {
		Fax = fax;
	}

	public long getVoice() {
		return Voice;
	}

	public void setVoice(long voice) {
		Voice = voice;
	}

	public long getTerminatingservicecharging() {
		return Terminatingservicecharging;
	}

	public void setTerminatingservicecharging(long terminatingservicecharging) {
		Terminatingservicecharging = terminatingservicecharging;
	}

	public long getOriginatingservicecharging() {
		return Originatingservicecharging;
	}

	public void setOriginatingservicecharging(long originatingservicecharging) {
		Originatingservicecharging = originatingservicecharging;
	}

	public long getUSSDbasedcallback() {
		return USSDbasedcallback;
	}

	public void setUSSDbasedcallback(long uSSDbasedcallback) {
		USSDbasedcallback = uSSDbasedcallback;
	}

	public long getRoamingterminating() {
		return Roamingterminating;
	}

	public void setRoamingterminating(long roamingterminating) {
		Roamingterminating = roamingterminating;
	}

	public long getRoamingforwarded() {
		return Roamingforwarded;
	}

	public void setRoamingforwarded(long roamingforwarded) {
		Roamingforwarded = roamingforwarded;
	}

	public long getRoamingoriginating() {
		return Roamingoriginating;
	}

	public void setRoamingoriginating(long roamingoriginating) {
		Roamingoriginating = roamingoriginating;
	}

	public long getTerminating() {
		return Terminating;
	}

	public void setTerminating(long terminating) {
		Terminating = terminating;
	}

	public long getForward() {
		return Forward;
	}

	public void setForward(long forward) {
		Forward = forward;
	}

	public long getOriginating() {
		return originating;
	}

	public void setOriginating(long originating) {
		this.originating = originating;
	}
	long SuccessfulreleasedEXTnetwork=0;
	public long getSuccessfulreleasedEXTnetwork() {
		return SuccessfulreleasedEXTnetwork;
	}

	public void setSuccessfulreleasedEXTnetwork(long successfulreleasedEXTnetwork) {
		SuccessfulreleasedEXTnetwork = successfulreleasedEXTnetwork;
	}
	long Callinvokedchargingcancelled=0;
	public long getCallinvokedchargingcancelled() {
		return Callinvokedchargingcancelled;
	}

	public void setCallinvokedchargingcancelled(long callinvokedchargingcancelled) {
		Callinvokedchargingcancelled = callinvokedchargingcancelled;
	}
	long Unsuccsystemtermination=0;
	public long getUnsuccsystemtermination() {
		return Unsuccsystemtermination;
	}

	public void setUnsuccsystemtermination(long unsuccsystemtermination) {
		Unsuccsystemtermination = unsuccsystemtermination;
	}
	long Unsuccessfulcongestion=0;
	public long getUnsuccessfulcongestion() {
		return Unsuccessfulcongestion;
	}

	public void setUnsuccessfulcongestion(long unsuccessfulcongestion) {
		Unsuccessfulcongestion = unsuccessfulcongestion;
	}
	long Unsuccessfulnormaltermination=0;
	public long getUnsuccessfulnormaltermination() {
		return Unsuccessfulnormaltermination;
	}

	public void setUnsuccessfulnormaltermination(long unsuccessfulnormaltermination) {
		Unsuccessfulnormaltermination = unsuccessfulnormaltermination;
	}
	long Unsuccessfulotherreason=0;
	public long getUnsuccessfulotherreason() {
		return Unsuccessfulotherreason;
	}

	public void setUnsuccessfulotherreason(long unsuccessfulotherreason) {
		Unsuccessfulotherreason = unsuccessfulotherreason;
	}
	long Unnormaltermination=0;
	public long getUnnormaltermination() {
		return Unnormaltermination;
	}

	public void setUnnormaltermination(long unnormaltermination) {
		Unnormaltermination = unnormaltermination;
	}
	long Communitycharging=0;
	public long getCommunitycharging() {
		return Communitycharging;
	}

	public void setCommunitycharging(long communitycharging) {
		Communitycharging = communitycharging;
	}
	long Communityindicatorfound=0;
	public long getCommunityindicatorfound() {
		return Communityindicatorfound;
	}

	public void setCommunityindicatorfound(long communityindicatorfound) {
		Communityindicatorfound = communityindicatorfound;
	}
	long Subscribernotfound=0;
	public long getSubscribernotfound() {
		return Subscribernotfound;
	}

	public void setSubscribernotfound(long subscribernotfound) {
		Subscribernotfound = subscribernotfound;
	}
	long Internalerror=0;
	public long getInternalerror() {
		return Internalerror;
	}

	public void setInternalerror(long internalerror) {
		Internalerror = internalerror;
	}
	long Ratingfailed=0;
	public long getRatingfailed() {
		return Ratingfailed;
	}

	public void setRatingfailed(long ratingfailed) {
		Ratingfailed = ratingfailed;
	}
	long CFTChargedUnsuccessfulSetup=0;
	public long getCFTChargedUnsuccessfulSetup() {
		return CFTChargedUnsuccessfulSetup;
	}

	public void setCFTChargedUnsuccessfulSetup(long cFTChargedUnsuccessfulSetup) {
		CFTChargedUnsuccessfulSetup = cFTChargedUnsuccessfulSetup;
	}
	long CFTChargedCallsExpiry=0;
	public long getCFTChargedCallsExpiry() {
		return CFTChargedCallsExpiry;
	}

	public void setCFTChargedCallsExpiry(long cFTChargedCallsExpiry) {
		CFTChargedCallsExpiry = cFTChargedCallsExpiry;
	}
}
}
