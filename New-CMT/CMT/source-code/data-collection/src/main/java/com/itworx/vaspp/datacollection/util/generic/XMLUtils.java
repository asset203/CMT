package com.itworx.vaspp.datacollection.util.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import com.itworx.vaspp.datacollection.common.ApplicationException;

import com.itworx.vaspp.datacollection.objects.TextInputStructure;
import com.itworx.vaspp.datacollection.objects.VInput;
import com.itworx.vaspp.datacollection.objects.VInputStructure;
import com.itworx.vaspp.datacollection.objects.VNode;
import com.itworx.vaspp.datacollection.objects.VScheduledNode;
import com.itworx.vaspp.datacollection.objects.VSystem;

import com.itworx.vaspp.datacollection.util.ConfigReader;
import com.itworx.vaspp.datacollection.util.ConfigWriter;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.DatabaseUtils;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class XMLUtils {

	private static Logger logger;

	public static void init(Logger currentLogger) {
		logger = currentLogger;
	}

	/**
	 * validating XML File against specified schema
	 * 
	 * @param fileURL -
	 *            the URL of the XML file
	 * @param schemaURL -
	 *            the URL of the XML Schema file
	 * 
	 * @exception ApplicationException
	 *                if XML File wasn't valid if error occured accessing XML
	 *                File
	 * 
	 */
	public static void validateDocument(String fileURL, String schemaURL)
			throws ApplicationException {
		try {
			logger.debug("XMLUtils.validateDocument() - starting validating xml document: "
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
			XMLValidator handler = new XMLValidator();
			builder2.setErrorHandler(handler);
			builder2.parse(fileURL);
			if (handler.validationError == true) {
				logger.error("XMLUtils.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ " "
								+ handler.saxParseException.getMessage());
				throw new ApplicationException("Invalid XML Document:"
						+ handler.validationError + " "
						+ handler.saxParseException.getMessage(), 1001);
			} else {
				logger.debug("XMLUtils.validateDocument() - XML Document is valid");
			}
		} catch (ParserConfigurationException e) {
			logger.error("XMLUtils.validateDocument() - error with xml parser:"
							+ e);
			throw new ApplicationException("error with xml parser:" + e);
		} catch (IOException e) {
			logger.error("XMLUtils.validateDocument() - couldn't access xml input file or schema:"
							+ e);
			throw new ApplicationException(
					"couldn't access xml input file or schema:" + e);
		} catch (SAXException e) {
			logger.error("XMLUtils.validateDocument() - XML Document has Error:"
							+ e);
			throw new ApplicationException("XML Document has Error:" + e);

		}
	}

	/**
	 * validating XML File against specified schema
	 * 
	 * @param fileURL -
	 *            the URL of the XML file
	 * @param schemaURL -
	 *            the URL of the XML Schema file
	 * 
	 * @exception ApplicationException
	 *                if XML File wasn't valid if error occured accessing XML
	 *                File
	 * 
	 */
	public static void validateDocument(InputSource inSource, String schemaURL)
			throws ApplicationException {
		try {
			logger.debug("XMLUtils.validateDocument() - starting validating xml input source: against schema:" + schemaURL);
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
			XMLValidator handler = new XMLValidator();
			builder2.setErrorHandler(handler);
			builder2.parse(inSource);
			if (handler.validationError == true) {
				logger.error("XMLUtils.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ " "
								+ handler.saxParseException.getMessage());
				throw new ApplicationException("Invalid XML Document:"
						+ handler.validationError + " "
						+ handler.saxParseException.getMessage(), 1001);
			} else {
				logger.debug("XMLUtils.validateDocument() - XML Document is valid");
			}
		} catch (ParserConfigurationException e) {
			logger.error("XMLUtils.validateDocument() - error with xml parser:"
							+ e);
			throw new ApplicationException("error with xml parser:" + e);
		} catch (IOException e) {
			logger.error("XMLUtils.validateDocument() - couldn't access xml input file or schema:"
							+ e);
			throw new ApplicationException(
					"couldn't access xml input file or schema:" + e);
		} catch (SAXException e) {
			logger.error("XMLUtils.validateDocument() - XML Document has Error:"
							+ e);
			throw new ApplicationException("XML Document has Error:" + e);

		}
	}

	public static void validateDocument(Document document,String schemaURL) throws ApplicationException {
		InputSource inputConfigSrc = new InputSource();
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		inputConfigSrc.setCharacterStream(new StringReader(outputter.outputString(document)));
		validateDocument(inputConfigSrc, schemaURL);
	}
	public static Document getXMLDocument(InputStream is){
		SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		Document document = null;
		try {
			builder.setValidation(false);
			builder.setEntityResolver(new EntityResolver(){
				public InputSource resolveEntity(String arg0, String arg1)
						throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}});
			builder.setIgnoringElementContentWhitespace(true);
			document = builder.build(is);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public static void writeXMLDocument(Document document,OutputStream outStream){
		try { 

			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			Format prettyFormat = Format.getPrettyFormat();
			//Format prettyFormat = Format.getRawFormat();
			prettyFormat.setIndent("\t");
			outputter.setFormat(prettyFormat); //may be Format.getPrettyFormat()
			outputter.output(document, outStream); 
		} catch (IOException e) {
			e.printStackTrace(); 
		} 
	}
	
	
	public static Element[] getElementChildren(Element element,String name){
		Element[] children = new Element[0];
		List childList = element.getChildren();
		ArrayList childArray = new ArrayList();
		for (Iterator i = childList.iterator(); i.hasNext();) {
			Element e = (Element)i.next();
			if(e.getName().equals(name))
				childArray.add(e);
		}
		return (Element[])childArray.toArray(children);
	}
	
	public static Element getElementChild(Element element,String name){
		Element e = null;
		List childList = element.getChildren();
		for (Iterator i = childList.iterator(); i.hasNext();) {
			Element ele = (Element)i.next();
			if(ele.getName().equals(name)){
				e = ele;		
				break;
			}
		}
		return e;
	} 

	private static Element[] getMatchedElements(Document document,String expression) throws ApplicationException{
		//sample expression "/quartz/job/job-detail/job-data-map/entry"
	    XPath xPath = null;
		Element[] elements = new Element[1];
		try {
			xPath = XPath.newInstance(expression);
			List nodes = xPath.selectNodes(document.getRootElement(), expression);
			elements = (Element[]) nodes.toArray(elements);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	    return elements;
	}
	
	public static void removeLoggerElement(Document logsDoc,String systemName){
		int currentIndex = 0;
		Element logsRoot = logsDoc.getRootElement();
		Element[] elements = getElementChildren(logsRoot,"logger");
		Element logger = null;
		for (int i=0;i<elements.length; i++) {
			Element currentChild = elements[i];
			currentIndex++;
			String loggerName = currentChild.getAttribute("name").getValue();
			if(loggerName.equalsIgnoreCase(systemName))
			{
				logger = currentChild;
				logsRoot.removeContent(logger);
				break;
			}
		}
	}
	
	public static void removeAppenderElement(Document logsDoc,String systemName){
		int currentIndex = 0;
		Element logsRoot = logsDoc.getRootElement();
		Element[] elements = getElementChildren(logsRoot,"appender");
		Element appender = null;
		for (int i = 0 ;i< elements.length;i++) {
			Element currentChild = elements[i];
			currentIndex++;
			String loggerName = currentChild.getAttribute("name").getValue();
			if(loggerName.equalsIgnoreCase(systemName+"Appender"))
			{
				appender = currentChild;
				logsRoot.removeContent(appender);
				break;
			}
		}
	}

	public static void removeJobElement(Document jobsDoc,String systemName,String nodeName){
		Element jobsRoot = jobsDoc.getRootElement();
		Element[] elements = getElementChildren(jobsRoot,"job");
		for (int i = 0;i<elements.length;i++) {
			Element jobDetail = getElementChild(elements[i],"job-detail");
			if(jobDetail==null)
				continue;
			Element jobDataMap = getElementChild(jobDetail,"job-data-map");
			if(jobDataMap==null)
				continue;
			Element[] mapEntries = getElementChildren(jobDataMap,"entry");
			if(mapEntries==null)
				continue;
			boolean neededSystem = false;
			boolean neededNode = false;
			for(int j = 0;j< mapEntries.length;j++){
				String key = getElementChild(mapEntries[j],"key").getValue().trim();
				String value = getElementChild(mapEntries[j],"value").getValue().trim();
				if("SystemName".equals(key) && systemName.equals(value))
					neededSystem = true;
				if("NodeName".equals(key) && nodeName.equals(value))
					neededNode = true;
			}
			
			if(neededSystem&&neededNode){
				jobsRoot.removeContent(elements[i]);
				break;
			}
		}
	}

	public static void removeSystemElement(Document configurationDoc,String systemName){
		Element confRoot = configurationDoc.getRootElement();
		Element systemsElement = getElementChild(confRoot,"Systems");
		Element[] elements = getElementChildren(systemsElement,"System");
		for(int i = 0;i < elements.length ;i++){
			boolean generic = false;
			String vsystemName = elements[i].getAttribute("name").getValue();
			Attribute genericAttr = elements[i].getAttribute("generic");
			if(genericAttr != null){
				generic = Boolean.parseBoolean(genericAttr.getValue());
			}
			if(vsystemName.equals(systemName)){
				systemsElement.removeContent(elements[i]);
				break;
			}
		}
	}
	
	public static void removeNodeElement(Document configurationDoc,String systemName,String nodeName){
		Element confRoot = configurationDoc.getRootElement();
		Element nodesElement = getElementChild(confRoot,"Nodes");
		Element[] elements = getElementChildren(nodesElement,"Node");

		for (int i = 0;i<elements.length;i++) {
			Element nodeElement = elements[i];
			// Get the name of the node
			String vnodeName = nodeElement.getAttribute("name").getValue();
			String vsystemName = nodeElement.getAttribute("system").getValue();
			if (vsystemName.equals(systemName) && vnodeName.equals(nodeName)) {
				nodesElement.removeContent(nodeElement);
				break;
			}
		}
	}
	
	public static Element createXMLElement(String velocityFile,String parameterName,Object parameter) throws ApplicationException{
		Properties p = new Properties();
		p.setProperty("file.resource.loader.path",PropertyReader.getVelocityTemplatesPath());
		Element clonedElement=null;
		
		try {
			Velocity.init(p);
		} catch (Exception e) {
			logger.error("XMLUtils.getXMLElement(): error while initializing Velocity " + e);
			throw new ApplicationException(""+e);
		}

		VelocityContext context = new VelocityContext();
		context.put(parameterName, parameter);
		
		StringWriter w = new StringWriter();
        
        try {
        	
			Velocity.mergeTemplate(velocityFile, "UTF-8",context, w );
	        String xmlString = w.toString();
	        
	        SAXBuilder newElementBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
	        InputSource validationSource = new InputSource();
	        validationSource.setCharacterStream(new StringReader(xmlString));
	        org.jdom.Document newElementDoc = null;
			newElementDoc = newElementBuilder.build(validationSource);
			clonedElement = (Element) newElementDoc.getRootElement().clone();
			
		} catch (JDOMException e) {
			logger.error("XMLUtils.createXMLElement() - JDOM Error while bulding XML node for ["+velocityFile+"] : " + e);
			throw new ApplicationException("JDOM Error while bulding XML node for ["+velocityFile+"] : " + e);
		} catch (IOException e) {
			logger.error("XMLUtils.createXMLElement() - IO Error while bulding XML node for ["+velocityFile+"] : " + e);
			throw new ApplicationException("IO Error while bulding XML node for ["+velocityFile+"] : " + e);
		}catch (ResourceNotFoundException e) {
			logger.error("XMLUtils.createXMLElement() - Resource needed for velocity not found ["+velocityFile+"] : " + e);
			throw new ApplicationException("Resource needed for velocity not found ["+velocityFile+"] : " + e);
		} catch (ParseErrorException e) {
			logger.error("XMLUtils.createXMLElement() - Parsing Error while bulding XML node for ["+velocityFile+"] : " + e);
			throw new ApplicationException("Parsing Error while bulding XML node for ["+velocityFile+"] : " + e);
		} catch (MethodInvocationException e) {
			logger.error("XMLUtils.createXMLElement() - Method Invocation Error while bulding XML node for ["+velocityFile+"] : " + e);
			throw new ApplicationException("Method Invocation Error while bulding XML node for ["+velocityFile+"] : " + e);
		} catch (Exception e) {
			logger.error("XMLUtils.createXMLElement() - Error occurred while creating XML element for ["+velocityFile+"] : " + e);
			throw new ApplicationException("Error occurred while creating XML element for ["+velocityFile+"] : " + e);
		}
        return clonedElement;
	}
	
	public static void addJobElement(Document jobsDoc, VNode node) throws ApplicationException {
		VScheduledNode scheduledNode = (VScheduledNode)node;
		if(scheduledNode.getCronExpression()== null || "".equals(scheduledNode.getCronExpression()))
			return;
		Element jobElement = XMLUtils.createXMLElement("job.vm", "node", node);
		Element jobsRoot = jobsDoc.getRootElement();
		Element[] jobElements = XMLUtils.getElementChildren(jobsRoot,"job");
		Element cleanerJob = null;
		if(jobElements.length > 1){
			String jobName = "";
			for (int i = 0; i < jobElements.length ; i++){
				Element currentJob = jobElements[i];
				Element jobDetail = getElementChild(currentJob,"job-detail");
				Element nameElement = getElementChild(jobDetail,"name");
				jobName = nameElement.getValue();
				if(jobName.trim().equals("Cleaner"))
				{
					cleanerJob = currentJob;
					break;
				}
			}
			if(cleanerJob != null){
				int cleanerIdx = jobsRoot.indexOf(cleanerJob);
				Text newLine = new Text("\n");
				jobsRoot.addContent(cleanerIdx,newLine);
				Namespace originalNS = jobsRoot.getNamespace();
				setElementNamespace(jobElement, originalNS);
				jobsRoot.addContent(cleanerIdx, jobElement);
			}else
				jobsRoot.addContent(jobElement);
		}
	}
	
	private static void setElementNamespace(Element element,Namespace namespace){
		element.setNamespace(namespace);
		List elements = element.getChildren();
		for(int i = 0;i<elements.size();i++){
			setElementNamespace((Element)elements.get(i), namespace);
		}
	}
	
	public static void addNodeElement(Document inputConfigDoc,	VNode node) throws ApplicationException {
		Element nodeElement = XMLUtils.createXMLElement("node.vm", "node", node);
		Element root = inputConfigDoc.getRootElement();
		Element nodesElement = root.getChild("Nodes");
		Text newLine = new Text("\n");
		nodesElement.addContent(newLine);
		nodesElement.addContent(nodeElement);
	}
	
	public static void addAppenderElement(Document logsDoc, VSystem system) throws ApplicationException {
		Element systemAppenderElement = XMLUtils.createXMLElement("log4jappender.vm", "system", system);
		Element logRoot = logsDoc.getRootElement();
		Element[] loggerElements = XMLUtils.getElementChildren(logRoot,"logger");
		Element firstloggerElement = loggerElements[0];
		int firstLoggerElementIdx = logRoot.indexOf(firstloggerElement);
		Text newLine = new Text("\n");
		logRoot.addContent(firstLoggerElementIdx,newLine);
		logRoot.addContent(firstLoggerElementIdx, systemAppenderElement);
	}
	
	public static void addLoggerElement(Document logsDoc, VSystem system) throws ApplicationException {
		Element systemLoggerElement = XMLUtils.createXMLElement("log4jlogger.vm", "system", system);
		Element logRoot = logsDoc.getRootElement();
		Element rootLoggerElement = XMLUtils.getElementChild(logRoot,"root");
		int rootLoggerIdx = logRoot.indexOf(rootLoggerElement);
		Text newLine = new Text("\n");
		logRoot.addContent(rootLoggerIdx,newLine);
		logRoot.addContent(rootLoggerIdx, systemLoggerElement);
	}
	
	public static void addSystemElement(Document inputConfigDoc, VSystem system) throws ApplicationException {
		Element systemElement = XMLUtils.createXMLElement("system.vm", "system", system);
		Element root = inputConfigDoc.getRootElement();
		Element systemsElement = root.getChild("Systems");
		Text newLine = new Text("\n");
		systemsElement.addContent(newLine);
		systemsElement.addContent(systemElement);
	}
	
	public static Element getMatchedElement(Document document,String expression) throws ApplicationException{
			//sample expression "/s:schedule/s:show[@date=$date]/g:guest"
	    XPath xPath = null;
	    Element element = null;
		try {
			xPath = XPath.newInstance(expression);
		    element = (Element) xPath.selectSingleNode(document);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	    return element;
	} 
	
	public static void main2(String[] args){
		try {
			//PropertyReader.init("D:\\Work\\My Eclipse Projects\\VASPortalWF\\Source Code\\DataCollection",DataCollectionManager.getLogger());
			PropertyReader.init("D:/Work/My Eclipse Projects/ProofOfConcepts/WebRoot",DataCollectionManager.getLogger());
			XMLUtils.init(DataCollectionManager.getLogger());
			ConfigReader reader = new ConfigReader(DataCollectionManager.getLogger());
			VSystem currentSystem = reader.getSystem("call_collect_service_sys");
			ConfigWriter writer = new ConfigWriter(DataCollectionManager.getLogger());
			writer.deleteSystem(currentSystem);
/*			VNode node = prepareTestNode();
			Element nodeElement = createXMLElement("node.vm", "node", node);
			XMLOutputter nodeOutputter = new XMLOutputter(Format.getPrettyFormat());
			nodeOutputter.output(nodeElement, System.out);
			System.out.println("----------------------------------------------");
			
			VSystem system = new VSystem();
			system.setName("SYSTEM_1");
			Element loggerElement = createXMLElement("log4jlogger.vm", "system", system);
			Element appenderElement = createXMLElement("log4jappender.vm", "system", system);
			nodeOutputter.output(loggerElement, System.out);
			
			System.out.println("----------------------------------------------");
			nodeOutputter.output(appenderElement, System.out);
			
			System.out.println("----------------------------------------------");
			
			Element jobElement = createXMLElement("job.vm", "node", node);
			nodeOutputter.output(jobElement, System.out);
			
			System.out.println("----------------------------------------------");
			
			VSystem sys = getSampleSystem();
			Element systemElement = createXMLElement("system.vm", "system", sys);
			nodeOutputter.output(systemElement, System.out);
			*/
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException {
		 processCCNData();
	}

	public static void processCCNData() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		String[] ccnCounterKeys = new String[]{
				"ApnConversion-Failed",
				"GeneralCongestion-DataBase",
				"GeneralCongestion-Dialogue-Overload",
				"RequestRejected-Overload",
				"Primary-Account-Finder-Error",
				"Secondary-Account-Finder-Error",
				"SDP-Lookup-Failed-Subscriber-Not-Found",
				"AccessFailure-Database",
				
				"CAP-Received-Total-Invalid-Requests",
				"CAPv2-Received-Total-Requests",
				"CAPv2-Received-Component-Rejects",
				"CAP2-Received-Error-Indications",
				"CAPv2-Sent-Total-Requests",
				"CAPv2-Sent-Component-Rejects",
				"CAPv2-Sent-Error-Indications",
				"CAPv2-Received-Returned-Components",
				
				"CdrOutput-Encoded-Successful",
				"CdrOutput-Encoded-Failed",
				"CdrOutput-Data-Sent-To-Primary-Disc",
				"CdrOutput-Data-Sent-To-Secondary-Disc",
				"CdrOutput-Data-Discarded",
				"CdrOutput-Data-Stored-On-Disc-0",
				"CdrOutput-Data-Stored-On-Disc-1",
				"CdrOutput-Files-Generated-On-Disc-0",
				"CdrOutput-Files-Generated-On-Disc-1",
				"CdrOutput-Files-Sent-To-FTP-Destination",
				"CdrOutput-Files-Sent-To-FTPDestination-Failed",
				
				"INAP-Received-Component-Rejects",
				"INAP-Received-Error-Indications",
				"INAP-Received-Returned-Components",
				"INAP-Received-Total-Invalid-Requests",
				"INAP-Received-Total-Requests",
				"INAP-Sent-Component-Rejects",
				"INAP-Sent-Error-Indications",
				"INAP-Sent-Total-Requests",
				
				"RelaySms-FirstInterrogation-Successful",
				"RelaySms-FirstInterrogation-Congestion-Rejected",
				"RelaySms-FirstInterrogation-Congestion-Allowed",
				"RelaySms-FirstInterrogation-NoContactWithSDPRejected",
				"RelaySms-FirstInterrogation-NoContactWithSDPAllowed",
				"RelaySms-FirstInterrogation-InternalError-Rejected",
				"RelaySms-FinalReport-Successful",
				"RelaySms-FinalReport-NoContactWithSDP",
				"RelaySms-FinalReport-InternalError",
				"RelaySms-FinalReport-Congestion",
				
				"Voice-Charging-FirstInterrogation-Successful",
				"Voice-Charging-FirstInterrogation-Congestion",
				"Voice-Charging-FirstInterrogation-NoContactWithSDPAllowed",
				"Voice-Charging-FirstInterrogation-NoContactWithSDPNotAllowed",
				"Voice-Charging-FirstInterrogation-InternalError",
				"Voice-Charging-IntermediateInterrogation-Successful",
				"Voice-Charging-IntermediateInterrogation-Congestion",
				"Voice-Charging-IntermediateInterrogation-NoContactWithSDPAllowed",
				"Voice-Charging-IntermediateInterrogation-NoContactWithSDPNotAllowed",
				"Voice-Charging-IntermediateInterrogation-InternalError",
				"Voice-Charging-FinalReport-Successful",
				"Voice-Charging-FinalReport-Congestion",
				"Voice-Charging-FinalReport-NoContactWithSDP",
				"Voice-Charging-FinalReport-InternalError",
				"Voice-Charging-Originating-Call",
				"Voice-Charging-Forwarding-Call",
				"Voice-Charging-Terminating-Call",
				"Voice-Charging-FLCDLocal-Call",
				"Voice-Charging-FLCDNon-Local-Call",
				
				"Messaging-Charging-FirstInterrogation-Successful",
				"Messaging-Charging-BalanceCheck-Successful",
				"Messaging-Charging-FirstInterrogation-Congestion",
				"Messaging-Charging-BalanceCheck-Congestion",
				"Messaging-Charging-FirstInterrogation-NoContactWithSDPAllowed",
				"Messaging-Charging-BalanceCheck-NoContactWithSDPAllowed",
				"Messaging-Charging-FirstInterrogation-NoContactWithSDPNot-Allowed",
				"Messaging-Charging-BalanceCheck-NoContactWithSDPNot-Allowed",
				"Messaging-Charging-FirstInterrogation-InternalError",
				"Messaging-Charging-BalanceCheck-InternalError",
				"Messaging-Charging-FinalReport-Successful",
				"Messaging-Charging-DirectDebit-Successful",
				"Messaging-Charging-FinalReport-NoContactWithSDP",
				"Messaging-Charging-DirectDebit-NoContactWithSDP",
				"Messaging-Charging-FinalReport-InternalError",
				"Messaging-Charging-DirectDebit-InternalError",
				"Messaging-Charging-FinalReport-Congestion",
				"Messaging-Charging-DirectDebit-Congestion",
				"Messaging-Charging-Originating-Sms",
				"Messaging-Charging-Terminating-Sms"

		};
		double[] ccnCounterValues = new double[ccnCounterKeys.length];
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver(){
			public InputSource resolveEntity(String arg0, String arg1)
					throws SAXException, IOException {
				return new InputSource(new StringReader(""));
			}});
		//org.w3c.dom.Document doc = builder.parse("c:\\A20090626.2055-2100_jambala_PlatformMeasures.20090626210046");
		org.w3c.dom.Document doc = builder.parse("c:\\A20090701.2020-2025_jambala_CcnCounters.20090701210011");
		XPathFactory factory = XPathFactory.newInstance();
		javax.xml.xpath.XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile("/mdc/md/mi/mv/moid");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String counterKey = "";
			double counterVal = 0;
			String counterTxt = currentNode.getTextContent();
			if(counterTxt!=null && counterTxt!= ""){
				counterTxt = counterTxt.replaceAll("\\s", "").replaceAll("\n", "").replaceAll("\r", "");
				String[] counterTxtArr = counterTxt.split("=|,");
				if(counterTxtArr.length == 4 && "_SYSTEM".equals(counterTxtArr[3])){
					counterKey = counterTxtArr[1];
					NodeList nodeChildren = currentNode.getParentNode().getChildNodes();
					//System.out.println(nodes.item(i).getParentNode().getChildNodes().item(3).getTextContent());
					for(int n=0;n<nodeChildren.getLength();n++){
						Node node = nodeChildren.item(n);
						if("r".equals(node.getNodeName())){
							String counterValTxt = node.getTextContent();
							if(!"".equals(counterValTxt)){
								counterVal = Double.parseDouble(counterValTxt); 
							}
							break;
						}
					}
					boolean updated = false;
					
					for(int idx = 0;idx<ccnCounterKeys.length;idx++){
						if(ccnCounterKeys[idx].equalsIgnoreCase(counterKey)){
							ccnCounterValues[idx] = ccnCounterValues[idx]+counterVal;
							updated=true;
							System.out.println(counterKey+"  --  "+counterVal);
							break;
						}
					}
					if(!updated){
						System.out.println(counterKey);
					}
					System.out.println(Arrays.toString(ccnCounterValues));
					//System.out.println(counterKey+"   "+counterVal);
				}
			}
		}
	}
	 
	public static VSystem getSampleSystem(){
		VSystem sys = null;
		try {
			PropertyReader.init("D:\\Work\\My Eclipse Projects\\VASPortalWF\\Source Code\\DataCollection",DataCollectionManager.getLogger());
			XMLUtils.init(DataCollectionManager.getLogger());
			ConfigReader cr = new ConfigReader(XMLUtils.logger);
			sys = cr.getSystem("test_generic_cpu_memory_sys");
//			sys.setDescription("sdghjhsdgjhsdgj\nkjsdhjkhsdkjhjds\tsdhgjsdh");
//			sys.setGeneric(true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sys;
	}
	
	
	public static VNode prepareTestNode(){
		VNode node = new VNode();
		node.setName("Node_1");
		node.setSystemName("SYSTEM_1");
		VInput[] inputs = new VInput[2];
		inputs[0] = new VInput();
		inputs[0].setId("id1");
		inputs[0].setInputName("inputName1");
		inputs[0].setInputName("inputName1");
		
		inputs[0].setPaths(new String[]{"path11","path12"});
		
		inputs[0].setServer("server1");
		inputs[0].setUser("user1");
		inputs[0].setPassword("password1");
		inputs[0].setAccessMethod("accessMethod1");
		
		VInputStructure[] inputStruct = new VInputStructure[2];
		inputStruct[0] = new TextInputStructure();
		inputStruct[1] = new TextInputStructure();
		inputStruct[0].setId("inputStruct11");
		inputStruct[1].setId("inputStruct12");
		inputs[0].setInputStructures(inputStruct);
		
		//---------------------------------------------
		
		inputs[1] = new VInput();
		inputs[1].setId("id2");
		inputs[1].setInputName("inputName2");
		inputs[1].setInputName("inputName2");
		
		inputs[1].setPaths(new String[]{"path21"});
		
		inputs[1].setServer("server2");
		inputs[1].setUser("user2");
		inputs[1].setPassword("password2");
		inputs[1].setAccessMethod("accessMethod2");
		
		VInputStructure[] inputStruct1 = new VInputStructure[1];
		inputStruct1[0] = new TextInputStructure();
		inputStruct1[0].setId("inputStruct21");
		inputs[1].setInputStructures(inputStruct1);
		
		node.setInputs(inputs);
		return node;
	}
}
