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
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;


public class CCNEventChargingConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,SummationObj> dateVSobj=new HashMap<String,SummationObj>() ;
	
public CCNEventChargingConverter()
{
	}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside CCNEventChargingConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("CCNEventChargingConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
           
			String line;
			String date = "";
			String teleServiceCode;
			String node;
			String serviceClass;
			String cdrType;
			String trafficCase;
			long volume=0;
			double charge=0.0;
			long interr=0;
			String shortCode;
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("|")&&line.split("\\|").length>=9)
				{
					try{
					date=getDate(line.split("\\|")[3]);
					teleServiceCode=line.split("\\|")[2]!=null?line.split("\\|")[2]:"";
					node=line.split("\\|")[8]!=null?line.split("\\|")[8]:"";
					serviceClass=line.split("\\|")[7]!=null?line.split("\\|")[7]:"";
					cdrType=line.split("\\|")[0]!=null?line.split("\\|")[0]:"";
					trafficCase=line.split("\\|")[1]!=null?line.split("\\|")[1]:"";
					if(teleServiceCode.trim().equalsIgnoreCase("USSD"))
					{
						shortCode=line.split("\\|")[line.split("\\|").length-1]!=null?line.split("\\|")[line.split("\\|").length-1]:"";
						if(shortCode.contains(";")&&shortCode.split(";").length!=0)	
							shortCode=shortCode.split(";")[0];	
						else if (shortCode.contains(";")&&shortCode.split(";").length==0)
							shortCode="";
					}else
					{
						shortCode="";
					}
					
					String key=date+","+teleServiceCode+","+node+","+serviceClass+","+cdrType+","+trafficCase+","+shortCode;
					if(line.split("\\|")[4].equalsIgnoreCase(""))
					{
						volume=0;
					}else
					{
					
					volume=Long.parseLong(line.split("\\|")[4]!=null?line.split("\\|")[4]:"0");
					}
					//System.out.println("line.split[5]"+line.split("\\|")[5]);
					if(line.split("\\|")[5].equalsIgnoreCase(""))
					{
						charge=0.0;}
					else{
						charge=Double.parseDouble((line.split("\\|")[5]!=null?line.split("\\|")[5]:"0.0"));
						}
					
					if(line.split("\\|")[6].equalsIgnoreCase("")){
							interr=0;
							//System.out.println("interr");
							}else{
								interr=Long.parseLong(line.split("\\|")[6]!=null?line.split("\\|")[6]:"0");
								}
					//System.out.println(key);
					if(dateVSobj.containsKey(key))
					{
						SummationObj obj=dateVSobj.get(key);
						obj.setVolume(obj.getVolume()+volume);
						obj.setCharge(obj.getCharge()+charge);
						obj.setInterrogation(obj.getInterrogation()+interr);
						obj.setCount(obj.getCount()+1);
						dateVSobj.remove(key);
						dateVSobj.put(key, obj);
					}else
					{
						SummationObj obj = new SummationObj ();
						obj.setVolume(volume);
						obj.setCharge(charge);
						obj.setInterrogation(interr);
						obj.setCount(1);
						dateVSobj.put(key, obj);
					}
				
					//System.out.println("serviceClass "+serviceClass);
					}catch(ParseException exc){ logger.error(exc) ; continue ;}
				     catch (NumberFormatException ex){continue ;}
				}
				
			}
		}//end of file
		Iterator it=dateVSobj.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 SummationObj obj=(SummationObj)dateVSobj.get(key);
			
	    	outputStream.write(key+","+obj.getVolume()+","+obj.getCharge()+","+obj.getInterrogation()+","+obj.getCount());
	    	
			 outputStream.newLine();
		}

	inputStream.close();
	
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("CCNEventChargingConverter.convert() - finished converting input files successfully ");

}
catch (FileNotFoundException e) {
		logger
				.error("CCNEventChargingConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("CCNEventChargingConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("CCNEventChargingConverter.convert() - finished converting input files successfully ");
	return outputFiles;
	
}

private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		CCNEventChargingConverter s = new CCNEventChargingConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\CCN_Capacity_3107201114_00162");
		   s.convert(input,"Maha_Test");
		System.out.println("FINISHED ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class SummationObj
{
	long volume=0;
	double charge=0;
	long Interrogation=0;
	long count=0;
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public long getInterrogation() {
		return Interrogation;
	}
	public void setInterrogation(long interrogation) {
		Interrogation = interrogation;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	}
}
