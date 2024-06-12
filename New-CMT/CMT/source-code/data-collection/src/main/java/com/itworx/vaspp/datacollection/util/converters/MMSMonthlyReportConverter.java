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
import java.util.List;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class MMSMonthlyReportConverter  extends AbstractTextConverter{
private Logger logger;
private List catObjects = new ArrayList();
public MMSMonthlyReportConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside MMSMonthlyReportConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		String months[]=null;
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("MMSMonthlyReportConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String currenetCtegory="";
			
			while (inputStream.ready()) {
				line = inputStream.readLine();
				
				if(line.contains("NAME"))
				{
					if(line.contains(","))
					{
						String MonthsList[]=line.split(",");
						months= new String[MonthsList.length-1];
						for(int mon=1;mon<MonthsList.length;mon++)
						{
							try
							{
								months[mon-1]=getDate(MonthsList[mon]);
								
							}
							catch(ParseException exc){ logger.error(exc) ; continue ;}
						}
					}
				}
				else 
				{
					
					if(line.contains(",")&&line.split(",").length>2)
					{
						if(line.split(",")[1].equalsIgnoreCase("."))
						{
						currenetCtegory=line.split(",")[0];
						
						}
						else if (line.contains("\"") &&line.contains("."))
						{
							String catName =line.split("\"")[1];
							if(catName!=null)
							{
								currenetCtegory=catName.replaceAll(",", ";");
								
							}
						}
						else 
						{
							Ctegory cat= new Ctegory();
							cat.setCatName(currenetCtegory);
							cat.setName(line.split(",")[0]);
							String actValu[]= new String[line.split(",").length-1];
							for(int val=1;val<line.split(",").length;val++)
							{
								actValu[val-1]=line.split(",")[val];
								
							}
							cat.setValues(actValu);
							catObjects.add(cat);
						}
						
					}
					
				}
			}
		}//file end 
		for(int cat=0;cat<catObjects.size();cat++)
		{
			Ctegory category =(Ctegory)catObjects.get(cat);
			for (int j=0;j<months.length;j++)
			{
				outputStream.write(months[j]+","+category.getCatName()+","+category.getName()+","+category.getValues()[j]);
				//System.out.println("key "+Months[j]+","+category.getCatName()+","+category.getName()+","+category.getValues()[j]);
				outputStream.newLine();
			}
		}
		 outputStream.write(1);
		 outputStream.newLine();
		
	
	inputStream.close();
	
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("MMSMonthlyReportConverter.convert() - finished converting input files successfully ");

	}
catch (FileNotFoundException e) {
		logger
				.error("MMSMonthlyReportConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("MMSMonthlyReportConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("MMSMonthlyReportConverter.convert() - finished converting input files successfully ");
	return outputFiles;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yy-MMM");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/00/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
		MMSMonthlyReportConverter s = new MMSMonthlyReportConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\Sep_MMS_Monthly_Report.csv");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
class Ctegory 
{
	public String catName="";
	public String name="";
	public String [] values;
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
}
}
