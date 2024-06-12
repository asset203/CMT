package com.itworx.vaspp.datacollection.util.converters;

import java.awt.font.NumericShaper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class RTSEventsCountersConverter extends AbstractTextConverter{
	private Logger logger;
	private org.jdom.Document document;
	  private Document xmlDocument;
	  
	  private Map<String , Double> rKeyvsValue ;
	  private Map<String , Double> countersKeyvsValue ;
		public RTSEventsCountersConverter()
		{}
		public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException {
			logger = Logger.getLogger(systemName);
			logger
					.debug("Inside RTSEventsCountersConverter convert - started converting input files");
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());

			BufferedReader inputStream = null;
			BufferedWriter outputStream;
			
		try
		{	
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			String countersNames[]=PropertyReader.getRtsEventCounters().split(",");
			List counters = new ArrayList();
			for(int i=0;i<countersNames.length;i++)
			{
				counters.add(countersNames[i]);
			}
			
			for (int i = 0; i < inputFiles.length; i++) {
				 rKeyvsValue= new HashMap<String, Double>() ;
				 countersKeyvsValue= new HashMap<String, Double>() ;
				String date="";
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
				Element mfhElement = root.getChild("mfh");
				Element  cbtElements = mfhElement.getChild("cbt");
				
				try
				{date =getDate(cbtElements.getValue());}
				catch (ParseException ex)
				{  logger.error(ex) ; continue ;}
				Element mdElement = root.getChild("md");
				Element miElement=mdElement.getChild("mi");
				List mtList =miElement.getChildren("mt");
				Element  mvElement =miElement.getChild("mv");
				List rList         =mvElement.getChildren("r");
				for(int r=0;r<rList.size();r++)
				{
					Element rElement =(Element)rList.get(r);	
					try{
					rKeyvsValue.put(rElement.getAttributeValue("p"), Double.parseDouble((!rElement.getValue().equalsIgnoreCase(""))?rElement.getValue() :"0.0"));}
					catch (NumberFormatException ex){  logger.error(ex) ; continue ;}
				}
				for(int mt=0;mt<mtList.size();mt++)
				{
					Element mtElement =(Element)mtList.get(mt);
					if(counters.contains(mtElement.getValue()))
					{
						String mtp=mtElement.getAttribute("p").getValue();
						if(rKeyvsValue.containsKey(mtp))
						{
							countersKeyvsValue.put(mtElement.getValue(), rKeyvsValue.get(mtp));
							
						}
						
					}
				}
				Iterator it =countersKeyvsValue.keySet().iterator();
				while (it.hasNext())
				{
					Object key=it.next();
					outputStream.write(date+","+key+","+(double)countersKeyvsValue.get(key));
					outputStream.newLine();
				}
				
				
			}//end of files
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("RTSEventsCountersConverter.convert() - finished converting input files successfully ");
		
	
	
	}
	catch (Exception e) {
		logger.error("ConfigReader.ConfigReader() : couldn't open xml input file  " + e);
		throw new ApplicationException(e);
	} 
	
	return outputFiles;
	}
	
	private String getDate(String value) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		if (value != null)
			
		   
			date = inDateFormat.parse(value);
		

		dateString = outDateFormat.format(date);

		return dateString;

	}
	public static void main(String ag[]) {
		try {
			PropertyReader
			.init("D:\\build\\phase10\\DataCollection");
			RTSEventsCountersConverter s = new RTSEventsCountersConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\build\\phase10\\DataCollection\\A20110905.2000+0300-2100+0300");
			input[1]=new File("D:\\build\\phase10\\DataCollection\\Copy of A20110905.2000+0300-2100+0300");
			s.convert(input,"SDP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
