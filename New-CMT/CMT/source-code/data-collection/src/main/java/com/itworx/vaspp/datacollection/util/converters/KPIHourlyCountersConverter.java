package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


import java.util.List;
import java.util.Map;



import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;


import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class KPIHourlyCountersConverter extends AbstractTextConverter{
	private Logger logger;
	private org.jdom.Document document;
	  private Document xmlDocument;
	 
	public KPIHourlyCountersConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside KPIHourlyCountersConverter convert - started converting input files");
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
			Element metricsElements = root.getChild("metrics");
			List countersElement=metricsElements.getChildren("counter");
			String outDate="";
			
		 
		
			Map<String , List> countersValues;
			for (int j=0;j<countersElement.size();j++)
			{ 
				
				
				
				Element counterElement=(Element)countersElement.get(j);
				String counterName=counterElement.getAttributeValue("name");
				List itemsElement=counterElement.getChildren("items");
				if(itemsElement==null)
					continue;
				for(int item=0;item<itemsElement.size();item++)
				{ 
					Element itemElement= (Element)itemsElement.get(item);
						if(itemElement.getAttributeValue("intervalUnit").equalsIgnoreCase("hour"))
						{
								
						Element numbersElement=itemElement.getChild("numberList");
						if(numbersElement==null)
							continue;
						List numbersElements=numbersElement.getChildren("number");
						if(numbersElements==null)
							continue;
								
								for(int num=0;num<numbersElements.size();num++)
								{
									Element numberElement=(Element)numbersElements.get(num);
									String dateString=numberElement.getAttributeValue("timestamp");
									try{
										outDate =getDate(dateString);
										if(numberElement==null)
											 continue;
										Double CounterValue=Double.parseDouble(numberElement.getValue());
										outputStream.write(outDate+","+counterName+","+CounterValue.doubleValue());
										System.out.println("THE KEY "+outDate+","+counterName+","+CounterValue);
										outputStream.newLine();
									  }
									 catch(ParseException exc){ logger.error(exc) ; continue ;}
									 catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
									 
									 
									
										
								}
					}
			  }
	}
}
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("KPIHourlyCountersConverter.convert() - finished converting input files successfully ");
		
	
	
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
				"yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");
           date = inDateFormat.parse(value);
          dateString = outDateFormat.format(date);

		return dateString;

	}



	public static void main(String ag[]) {
		try {
			PropertyReader
			.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_2010\\Trunk\\SourceCode\\DataCollection");

			KPIHourlyCountersConverter s = new KPIHourlyCountersConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\ITWorx\\Projects\\VFE_VAS_Realtime_VNPP\\Trunk\\SourceCode\\Realtime_VNPP\\Builds\\.kpiCountersDb.xml");



			s.convert(input,"SDP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
