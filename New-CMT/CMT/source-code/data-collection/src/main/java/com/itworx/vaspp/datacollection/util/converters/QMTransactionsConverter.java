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

public class QMTransactionsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,QMTransactions> keyVsSummCoun=new HashMap<String,QMTransactions>() ;
	public QMTransactionsConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside QMTransactionsConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("QMTransactionsConverter.convert() - converting file "
								+ inputFiles[i].getName());
				//System.out.println("inputFiles[i].getName() "+inputFiles[i].getName());
				
				
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String cc="";
				String line;
				String key="";
				String date = "";
				String packageId="";
				String barrierType="";	
				
				String apn="";
				double durationVolume=0.0;
				double dataVolume=0.0;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("|")&&line.split("\\|").length>=49)
					{
						try
						{
							
							date =getDate(line.split("\\|")[4]);
							apn=line.split("\\|")[5]!=null?line.split("\\|")[5]:"";
							cc=line.split("\\|")[30]!=null?line.split("\\|")[30]:"";
							packageId=line.split("\\|")[48]!=null?line.split("\\|")[48]:"";
							barrierType=line.split("\\|")[21]!=null?line.split("\\|")[21]:"";
							
							
							key=date+","+apn+","+cc+","+packageId+","+barrierType;
							
							durationVolume=Double.parseDouble(line.split("\\|")[11]!=null?line.split("\\|")[11]:"0.0");
							dataVolume=Double.parseDouble(line.split("\\|")[14]!=null?line.split("\\|")[14]:"0.0");
						   if(keyVsSummCoun.containsKey(key))
						   {
							   QMTransactions obj=keyVsSummCoun.get(key);
							   obj.setDurationVolume(obj.getDurationVolume()+durationVolume);
							   obj.setDataVolume(obj.getDataVolume()+dataVolume);
							   obj.setCount(obj.getCount()+1);
							   keyVsSummCoun.remove(key);
							   keyVsSummCoun.put(key, obj);
							   
						   }else
						   {
							   QMTransactions obj= new QMTransactions();
							   obj.setDurationVolume(durationVolume);
							   obj.setDataVolume(dataVolume);
							   obj.setCount(1);
							   keyVsSummCoun.put(key, obj);
							   
						   }
						
							
						} catch(ParseException exc){logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){logger.error(exc) ; continue ;}
					
					}else
					{continue;}
				}
			}
		inputStream.close();
		Iterator it1 = keyVsSummCoun.keySet().iterator();
		while ( it1.hasNext()) {
			Object key = it1.next();	
			outputStream.write(key + "," +((QMTransactions) keyVsSummCoun.get(key)).getDurationVolume() + ","
					+ ((QMTransactions) keyVsSummCoun.get(key)).getDataVolume() + "," +((QMTransactions) keyVsSummCoun.get(key)).getCount());
			outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("QMTransactionsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("QMTransactionsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("QMTransactionsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("QMTransactionsConverter.convert() - finished converting input files successfully ");
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
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			QMTransactionsConverter s = new QMTransactionsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\CCN-QM-23012011082800908");
			   s.convert(input,"Maha_Test");
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
class QMTransactions
{
	double  durationVolume=0.0;
	double  dataVolume=0.0;
	long count=0;
	public double getDurationVolume() {
		return durationVolume;
	}
	public void setDurationVolume(double durationVolume) {
		this.durationVolume = durationVolume;
	}
	public double getDataVolume() {
		return dataVolume;
	}
	public void setDataVolume(double dataVolume) {
		this.dataVolume = dataVolume;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
}
}
