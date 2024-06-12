/*
 * File: TrendAnalysisConfigReader.java
 *
 * Date        Author            Changes
 *
 * 23/05/2006  Marwan Abdelhady  Created

 *
 * responsible for parsing xml data from configuration file and mapping to java object
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.VCounter;

import java.io.*;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class TrendAnalysisConfigReader {
	private org.jdom.Document document;

	private static final String systemSeparator = System
			.getProperty("file.separator");

	private Logger logger = Logger.getLogger("TrendAnalysis");

	/**
	 * intiating TrendAnalysisConfigReader Loading and validating XML File
	 * 
	 * @param logger
	 *            - the name of the logger for the targeted system
	 * @exception ApplicationException
	 *                if an error occured while reading XML File if XML File
	 *                wasn't valid
	 */
	public TrendAnalysisConfigReader() throws ApplicationException {
		try {
			logger
					.debug("initating TrendAnalysisConfigReader: reading system data from xml");
			// PropertyReader.init("C:\\Documents and Settings\\marwan.abdelhady\\Desktop\\Vodafone\\SVN\\SourceCode\\DataCollection",logger);
			String xmlPath = PropertyReader.getTrendAnalysisConfigFilePath();
			String xmlName = PropertyReader.getTrendAnalysisConfigFileName();
			String schemaPath = PropertyReader
					.getTrendAnalysisConfigSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();
			String fileURL = xmlPath + systemSeparator + xmlName;
			File configFile = new File(fileURL);
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			validateDocument(fileURL, schemaURL);
			document = builder.build(configFile);
		} catch (IOException e) {
			logger
					.error("TrendAnalysisConfigReader.TrendAnalysisConfigReader() : couldn't open xml input file  "
							+ e);
			throw new ApplicationException(e);
		} catch (JDOMException e) {
			logger
					.error("TrendAnalysisConfigReader.TrendAnalysisConfigReader() : Invalid Input XML"
							+ e);
			throw new ApplicationException("Invalid Input XML" + e, 1001);
		}
	}

	/**
	 * validating XML File against specified schema
	 * 
	 * @param fileURL
	 *            - the URL of the XML file
	 * @param schemaURL
	 *            - the URL of the XML Schema file
	 * 
	 * @exception ApplicationException
	 *                if XML File wasn't valid if error occured accessing XML
	 *                File
	 * 
	 */
	private void validateDocument(String fileURL, String schemaURL)
			throws ApplicationException {

		try {
			logger
					.debug("TrendAnalysisConfigReader.validateDocument() - starting validating xml document: "
							+ fileURL + " against schema:" + schemaURL);
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
			builder2.parse(fileURL);
			if (handler.validationError == true) {
				logger
						.error("TrendAnalysisConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ " "
								+ handler.saxParseException.getMessage());
				// throw new ApplicationException("Invalid XML Document:"
				// + handler.validationError + " "
				// + handler.saxParseException.getMessage(), 1001);
			} else {
				logger
						.debug("TrendAnalysisConfigReader.validateDocument() - XML Document is valid");
			}
		} catch (ParserConfigurationException e) {
			logger
					.error("TrendAnalysisConfigReader.validateDocument() - error with xml parser:"
							+ e);
			// throw new ApplicationException("error with xml parser:" + e);
		} catch (IOException e) {
			logger
					.error("TrendAnalysisConfigReader.validateDocument() - couldn't access xml input file or schema:"
							+ e);
			// throw new ApplicationException(
			// "couldn't access xml input file or schema:" + e);
		} catch (SAXException e) {
			logger
					.error("TrendAnalysisConfigReader.validateDocument() - XML Document has Error:"
							+ e);
			// throw new ApplicationException("XML Document has Error:" + e);
		}
	}

	/**
	 * parse XML for counters of this trend_analysis and map to java objects
	 * 
	 * 
	 * @return VCounter[] - array of counter objects
	 * @throws ApplicationException
	 */
	public VCounter[] getCounters() {
		logger
				.debug("TrendAnalysisConfigReader.getCounters() - started getCounters");
		Vector<VCounter> vcounters = new Vector<VCounter>();
		Element trendAnalysisElement = document.getRootElement();
		List counterElements = trendAnalysisElement.getChildren();

		for (Iterator i = counterElements.iterator(); i.hasNext();) {
			try {
				VCounter vcounter = new VCounter();
				Element counterElement = (Element) i.next();
				if ("counter".equals(counterElement.getName())) {
					if (counterElement.getAttribute("id") != null) {
						vcounter.setId(counterElement.getAttribute("id")
								.getValue());
					}
					if (counterElement.getChild("sql") != null) {
						vcounter.setSql(counterElement.getChild("sql")
								.getValue().toString()
								.replaceAll("(\\s)+", " "));
					}
					if (counterElement.getChild("Date_Column") != null) {
						vcounter.setDateColumn(counterElement.getChild(
								"Date_Column").getValue().toString());
					}
					/*added by suzan Tadrous */
					if (counterElement.getAttribute("name") != null) {
						vcounter.setName(counterElement.getAttribute("name")
								.getValue());
					}
					if (counterElement.getAttribute("error") != null) {
						vcounter.setError(counterElement.getAttribute("error")
								.getValue());
					}
					if (counterElement.getAttribute("warning") != null) {
						vcounter.setWarning(counterElement.getAttribute("warning")
								.getValue());
					}
					if (counterElement.getAttribute("normal") != null) {
						vcounter.setNormal(counterElement.getAttribute("normal")
								.getValue());
					}
					
					/**/
					boolean duplicate = false;
					for (int j = 0; j < vcounters.size(); j++) {
						if(vcounter.getId() == null
								&& vcounters.get(j).getId() == null) {
							duplicate = true;
							logger
									.debug("TrendAnalysisConfigReader.getCounters() - duplicate counter Id: "
											+ vcounter.getId());
						} else if(vcounter.getId() != null
								&& vcounters.get(j).getId() != null){
						
							if (vcounter.getId().equalsIgnoreCase(
									vcounters.get(j).getId())) {
								duplicate = true;
								logger
										.debug("TrendAnalysisConfigReader.getCounters() - duplicate counter Id: "
												+ vcounter.getId());
							}
						}
					}
					if (!duplicate) {
						vcounters.addElement(vcounter);
						logger
								.debug("TrendAnalysisConfigReader.getCounters() - found counter: id:"
										+ vcounter.getId());
					}

				} else {
					logger
							.debug("TrendAnalysisConfigReader.getCounters() - Wrong tag name");
					continue;
				}
			} catch (Exception e) {
				logger
						.error(
								"TrendAnalysisConfigReader.getCounters() - Exception: id:",
								e);
			}
		}
		VCounter[] vcountersArraytmp = new VCounter[0];
		VCounter[] vcountersArray = (VCounter[]) vcounters
				.toArray(vcountersArraytmp);

		logger
				.debug("TrendAnalysisConfigReader.getCounters() - finished getCounters");
		return vcountersArray;
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

}
