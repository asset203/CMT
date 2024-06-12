package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class VPNXMLCompaniesConverter extends AbstractTextConverter {
	private Logger logger;
	private org.jdom.Document document;
	  private Document xmlDocument;
	 
	 
	  private Map<String , List> hours= new HashMap<String, List>() ;
	public VPNXMLCompaniesConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VPNXMLCompaniesConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
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
			Element mdElement = root.getChild("md");
			List  miElements = mdElement.getChildren("mi");
			String outDate="";
			Map<String , Long> CountersValuseresult;
		 
		
			Map<String , List> countersValues;
			for (int j=0;j<miElements.size();j++)
			{ 
				
				
				 List companiesId = new ArrayList();
				Element mi=(Element)miElements.get(j);
				Element mts =mi.getChild("mts");
			    outDate=getDate(mts.getValue());
				
				List mvElements=mi.getChildren("mv");
						
				for(int mv=0;mv<mvElements.size();mv++)
				{
					Element mvElement=(Element)mvElements.get(mv);
					
					Element moidElement=mvElement.getChild("moid");
					String moid =moidElement.getValue();
					String moidList[]=moid.split("stableKey=");
					/*
					 * [Code Review]
					 * Here there is apossibility for null pointer exception or index out of bounds 
					 */
					String companyId=moidList[1].split(",")[0];
					if(!companiesId.contains(companyId))
					   companiesId.add(companyId);
						
				}
								
				
				//filling the countesr and thier values
				
				if(hours.containsKey(outDate))
				{
					List existList=hours.get(outDate);
					for(int var=0;var<companiesId.size();var++)
					{  
						if(!existList.contains(companiesId.get(var)))
						{   
							existList.add(companiesId.get(var));
							
						}
					}
					
					hours.remove(outDate);
					hours.put(outDate, existList);
				}
				else
				{
				hours.put(outDate, companiesId);
				}
			
				
			}
			
			
			
			
		}
	
	Iterator myVeryOwnIterator =hours.keySet().iterator();
		while(myVeryOwnIterator.hasNext()){ 	
		
			Object key = myVeryOwnIterator.next();
			
			outputStream.write(key+","+((List)hours.get(key)).size());
			outputStream.newLine();
			
		}
		
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VPNXMLCompaniesConverter.convert() - finished converting input files successfully ");
		
	
	
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
				"MM/dd/yyyy");

		if (value != null)
			
		   
			date = inDateFormat.parse(value);
		

		dateString = outDateFormat.format(date);

		return dateString;

	}
/*
 * Validator class for XML File
 */
private class Validator extends DefaultHandler {
	public boolean validationError = false;

	public SAXParseException saxParseException = null;

	public void error(SAXParseException exception) throws SAXException {
		validationError = true;
		saxParseException = exception;
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		validationError = true;
		saxParseException = exception;
	}

	public void warning(SAXParseException exception) throws SAXException {
	}
}

public void validateXML( File inputFile) throws ApplicationException
{
	String schemaURL="D:\\build\\VASPortal\\DataCollection\\resources\\xmlSchemas\\VPN_SCHEMA.xsd";
	String fileURL="D:\\build\\VASPortal\\DataCollection\\resources//properties\\input_config.xml";
	try {
		SAXBuilder builder = new SAXBuilder(
	"org.apache.xerces.parsers.SAXParser");
	System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
	"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
	DocumentBuilderFactory factory = DocumentBuilderFactory
	.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaSource",
				schemaURL);
		DocumentBuilder builder2 = factory.newDocumentBuilder();
		Validator handler = new Validator();
		builder2.setErrorHandler(handler);
		builder2.parse(inputFile);
		if (handler.validationError == true) {
			logger
					.error("ConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
							+ handler.validationError
							+ " "
							+ handler.saxParseException.getMessage());
			throw new ApplicationException("Invalid XML Document:"
					+ handler.validationError + " "
					+ handler.saxParseException.getMessage(), 1001);
		} else {
			logger
					.debug("ConfigReader.validateDocument() - XML Document is valid");
		}
	} catch (ParserConfigurationException e) {
		logger
				.error("ConfigReader.validateDocument() - error with xml parser:"
						+ e);
		throw new ApplicationException("error with xml parser:" + e);
	} catch (IOException e) {
		logger
				.error("ConfigReader.validateDocument() - couldn't access xml input file or schema:"
						+ e);
		throw new ApplicationException(
				"couldn't access xml input file or schema:" + e);
	} catch (SAXException e) {
		logger
				.error("ConfigReader.validateDocument() - XML Document has Error:"
						+ e);
		throw new ApplicationException("XML Document has Error:" + e);
	}
	}
	public static void main(String ag[]) {
		try {
			PropertyReader
			.init("D:\\build\\VASPortal\\DataCollection");
			VPNXMLCompaniesConverter s = new VPNXMLCompaniesConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\samplese.xml");
			input[1]=new File("D:\\build\\VASPortal\\DataCollection\\sample2.xml");
			s.convert(input,"SDP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
