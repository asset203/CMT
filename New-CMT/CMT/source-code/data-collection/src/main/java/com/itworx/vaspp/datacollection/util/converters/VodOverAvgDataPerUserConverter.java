package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VodOverAvgDataPerUserConverter extends AbstractTextConverter{
	private Logger logger;
	private org.jdom.Document document;
	  private Document xmlDocument;
public VodOverAvgDataPerUserConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside VodOverAvgDataPerUserConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	
	BufferedWriter outputStream;
	
try
{	
	outputStream = new BufferedWriter(new FileWriter(outputFile));
	for (int i = 0; i < inputFiles.length; i++) {
		
		SAXBuilder saxfac = new SAXBuilder(
		"org.apache.xerces.parsers.SAXParser");
		saxfac.setValidation(false);
		  try { 
		    saxfac.setFeature("http://xml.org/sax/features/validation", false); 
		    saxfac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false); 
		    saxfac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); 
		    saxfac.setFeature("http://xml.org/sax/features/external-general-entities", false); 
		    saxfac.setFeature("http://xml.org/sax/features/external-parameter-entities", false); 
		  } 
		  catch (Exception e1) { 
		    e1.printStackTrace(); 
		  } 
        document = saxfac.build(inputFiles[i]);
        Element root = document.getRootElement();
        List mdElements = root.getChildren("row");
        String date=null;
        long  totalUniqueUsage=0;
        double   avgUsage=0.0;
        String key="";
        for(int r=0;r<mdElements.size();r++)
        {
        	Element row=(Element)mdElements.get(r);
        	List  miElements = row.getChildren("col");
        	
        	for(int c=0;c<miElements.size();c++)
        	{
        		Element cpl=(Element)miElements.get(c);
        		if(cpl.getAttributeValue("name").equalsIgnoreCase("Time"))
        		{
        			try
        		     {
        			  date=getDate(cpl.getValue()!=null?cpl.getValue():"");
         	         }catch(ParseException exc){ logger.error(exc) ; break ;}
        		}
        		try{	
        		 if (cpl.getAttributeValue("name").equalsIgnoreCase("Total Unique User"))
        		{
        			
        			 totalUniqueUsage=Long.parseLong(cpl.getValue()!=null?cpl.getValue():"0");   			 
        		}
        		 else if (cpl.getAttributeValue("name").equalsIgnoreCase("Average Usage in KB"))
        		 {
        			
        			 avgUsage=Double.parseDouble(cpl.getValue()!=null?cpl.getValue():"0.0");   			 
       		      }
        		
        		}catch(NumberFormatException ex)
	        	{
	        		logger.error(ex) ; continue ;
	        	}
        		
        	}
        	if(date!=null)
        	{
        	 key=date+","+totalUniqueUsage+","+avgUsage;
        	 outputStream.write(key);
			 outputStream.newLine();
        	}
        }
		
		
	}
	
	
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("VodOverAvgDataPerUserConverter.convert() - finished converting input files successfully ");



}
catch (Exception e) {
logger.error("ConfigReader.ConfigReader() : couldn't open xml input file  " + e);
throw new ApplicationException(e);
} 

return outputFiles;
}
public static void main(String ag[]) {
	try {
		PropertyReader
		.init("D:\\build\\phase10\\DataCollection");
		VodOverAvgDataPerUserConverter s = new VodOverAvgDataPerUserConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\Vodafone Overall 4 Average Data Per User.xml");
		//input[1]=new File("D:\\build\\VASPortal\\DataCollection\\sample2.xml");
		s.convert(input,"SDP");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
