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
import com.itworx.vaspp.datacollection.util.converters.HuaweiIVRCPUStatusConverter.CpuStatus;

public class GGSNCdrConverter extends AbstractTextConverter{
	private Logger logger;
	public GGSNCdrConverter()
	{}
	private Map  <String ,GgsnCdr> keyVsggsn=new HashMap<String,GgsnCdr>() ;
	
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside GGSNCdrConverterconvert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			String cc="";
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("GGSNCdrConverter.convert() - converting file "
								+ inputFiles[i].getName());
				//System.out.println("inputFiles[i].getName() "+inputFiles[i].getName());
				
				cc=inputFiles[i].getName().split("_")[1];
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	           
				String line;
				String key="";
				String date = "";
				String ssgn="";
				String rat="";	
				
				String apn="";
				double dn=0.0;
				double up=0.0;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("|")&&line.split("\\|").length>=24)
					{
						try
						{
							
							date =getDate(line.split("\\|")[4]);
							ssgn=line.split("\\|")[13]!=null?line.split("\\|")[13]:"";
						  if(ssgn.length()==8)
						      ssgn=getIpFromHex(ssgn);
						  else
							  continue;
							rat=line.split("\\|")[23]!=null?line.split("\\|")[23]:"";
							apn=line.split("\\|")[5]!=null?line.split("\\|")[5]:"";
							key=date+","+ssgn+","+rat+","+cc+","+apn;
							dn=Double.parseDouble(line.split("\\|")[21]!=null?line.split("\\|")[21]:"0.0");
							up=Double.parseDouble(line.split("\\|")[20]!=null?line.split("\\|")[20]:"0.0");
						    if(keyVsggsn.containsKey(key))
						    {
						    	GgsnCdr ggsn=keyVsggsn.get(key);
						    	ggsn.setDn(ggsn.getDn()+dn);
						    	ggsn.setUp(ggsn.getUp()+up);
						    	ggsn.setCount(ggsn.getCount()+1);
						    	keyVsggsn.remove(key);
						    	keyVsggsn.put(key,ggsn);
						    }
						    else
						    {
						    	GgsnCdr ggsn= new GgsnCdr();
						    	ggsn.setDn(dn);
						    	ggsn.setUp(up);
						    	ggsn.setCount(1);
						    	keyVsggsn.put(key,ggsn);
						    }
						   
							
						} catch(ParseException exc){logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){logger.error(exc) ; continue ;}
					
					}else
					{continue;}
				}
			}
		inputStream.close();
	   Iterator it1 = keyVsggsn.keySet().iterator();
		while ( it1.hasNext()) {
			Object key = it1.next();
			
			outputStream.write(key + "," +((GgsnCdr) keyVsggsn.get(key)).getDn() + ","
					+ ((GgsnCdr) keyVsggsn.get(key)).getUp()+ "," +((GgsnCdr) keyVsggsn.get(key)).getCount());

			outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("GGSNCdrConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("GGSNCdrConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("GGSNCdrConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("GGSNCdrConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;

		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	private String getIpFromHex(String name)
	{
		int part1 =Integer.parseInt(name.substring(0, 2), 16);
		int part2 =Integer.parseInt(name.substring(2, 4), 16);
		int part3 =Integer.parseInt(name.substring(4, 6), 16);
		int part4 =Integer.parseInt(name.substring(6,8), 16);
		return part1+"."+part2+"."+part3+"."+part4;
	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			GGSNCdrConverter s = new GGSNCdrConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\IPC_PRE_2011011812");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class GgsnCdr 
	{
		double dn =0.0;
		double up=0.0;
		long count=0;
		public double getDn() {
			return dn;
		}
		public void setDn(double dn) {
			this.dn = dn;
		}
		public double getUp() {
			return up;
		}
		public void setUp(double up) {
			this.up = up;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
	}
}
