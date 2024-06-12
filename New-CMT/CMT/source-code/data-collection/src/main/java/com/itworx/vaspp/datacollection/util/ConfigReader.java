/*
 * File: ConfigReader.java
 *
 * Date        Author          Changes
 *
 * 16/01/2006  Nayera Mohamed  Created
 * 30/01/2006  Nayera Mohamed  Updated to include DataColumn index
 * 20/02/2006  Nayera Mohamed  Updated to include ExcelInputStructure horizontal flag
 * 18/03/2006  Nayera Mohamed  Updated to include TextInputStructure extract date flag
 *
 * responsible for parsing xml data from configuration file and mapping to java object
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.generic.XMLUtils;
import com.itworx.vaspp.datacollection.objects.DataColumn;
import com.itworx.vaspp.datacollection.objects.VNode;
import com.itworx.vaspp.datacollection.objects.VInput;
import com.itworx.vaspp.datacollection.objects.VScheduledNode;
import com.itworx.vaspp.datacollection.objects.VSystem;
import com.itworx.vaspp.datacollection.objects.VInputStructure;
import com.itworx.vaspp.datacollection.objects.ExcelInputStructure;
import com.itworx.vaspp.datacollection.objects.TextInputStructure;
import com.itworx.vaspp.datacollection.objects.DBInputStructure;
import com.itworx.vaspp.datacollection.objects.GenericInputStructure;

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

public class ConfigReader {
	private org.jdom.Document document;

	private static final String systemSeparator = System
			.getProperty("file.separator");

	private Logger logger;

	/**
	 * intiating ConfigReader Loading and validating XML File
	 * 
	 * @param logger -
	 *            the name of the logger for the targeted system
	 * @exception ApplicationException
	 *                if an error occured while reading XML File if XML File
	 *                wasn't valid
	 */
	public ConfigReader(Logger logger) throws ApplicationException {
		try {
			this.logger = logger;
			logger
					.debug("initating ConfigReader: reading system data from xml");
			String xmlPath = PropertyReader.getInputConfigFilePath();
			String xmlName = PropertyReader.getInputConfigFileName();
			String schemaPath = PropertyReader.getInputConfigSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();
			String fileURL = xmlPath + systemSeparator + xmlName;
			File configFile = new File(fileURL);
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			validateDocument(fileURL, schemaURL);
			document = builder.build(configFile);
		} catch (IOException e) {
			logger.error("ConfigReader.ConfigReader() : couldn't open xml input file  " + e);
			throw new ApplicationException(e);
		} catch (JDOMException e) {
			logger.error("ConfigReader.ConfigReader() : Invalid Input XML" + e);
			throw new ApplicationException("Invalid Input XML" + e, 1001);
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
	private void validateDocument(String fileURL, String schemaURL)
			throws ApplicationException {

		try {
			logger
					.debug("ConfigReader.validateDocument() - starting validating xml document: "
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
						.error("ConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ "\n line number : "+handler.saxParseException.getLineNumber()
								+"\n"
								+ handler.saxParseException.getMessage());
				System.out.println("ConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ "\n line number : "+handler.saxParseException.getLineNumber()
								+"\n"
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
			e.printStackTrace();
			throw new ApplicationException("XML Document has Error:" + e  );
			
		}
	}

	/**
	 * parse XML for inputs of this system or system node and map to java
	 * objects
	 * 
	 * @param inputsElement -
	 *            the XML Element for tag <inputs> associated to this
	 *            system/node
	 * @param systemName -
	 *            the name of the targeted system
	 * @param nodeName -
	 *            the name of the targeted node or empty string if the target is
	 *            system
	 * 
	 * @return VInput[] - array of input objects for this system/node
	 * @throws ApplicationException 
	 */
	private VInput[] getInputs(Element inputsElement, String systemName,
			String nodeName) throws ApplicationException {
		logger.debug("ConfigReader.getInputs() - started getInputs for system:"
				+ systemName + " node:" + nodeName);
		Vector vinputs = new Vector();
		List inputsElements = inputsElement.getChildren();

		for (Iterator i = inputsElements.iterator(); i.hasNext();) {
			VInput vinput = new VInput();
			vinput.setNodeName(nodeName);
			vinput.setSystemName(systemName);
			Element inputElement = (Element) i.next();
			if (inputElement.getChild("pernode") != null) {
				vinput.setPerNode(true);
				/**
	             * @author basem.deiaa
	             * the following line was modified in June. 4 2009
	             * to update input with its id to be updated/removed in configuration GUI  
	             */
				vinput.setId(inputElement.getAttribute("id").getValue());
				/**
				 * end
				 */
				vinputs.addElement(vinput);
				continue;
			}
			if (inputElement.getChild("persystem") != null) {
				vinput.setPerNode(false);
				vinputs.addElement(vinput);
				continue;
			}
			if (!nodeName.equals("")) {
				vinput.setPerNode(true);
			}
			vinput.setId(inputElement.getAttribute("id").getValue());
			vinput.setInputName(inputElement.getChild("input_name").getValue());
			vinput.setOriginalInputName(vinput.getInputName());
			Element hourlyNameElement = inputElement.getChild("hourly_name");
			if(hourlyNameElement != null){
				vinput.setHourlyName(hourlyNameElement.getValue());
			}else{
				vinput.setHourlyName(inputElement.getChild("input_name").getValue());
			}
			/**
             * @author basem.deiaa
             * the following block was modified in Apr. 8 2009
             * and used to read all paths of the input
             * if just only one \<path\> tag then init array with one element
             * if more than one \<path\> tag inside onother \<paths\> tags then init array with all paths 
             */
			String[] paths = null;
			if(inputElement.getChild("path")!= null)
			{
				paths = new String[1];
				paths[0] = inputElement.getChild("path").getValue();
				vinput.setPaths(paths);
			}
			else if(inputElement.getChild("paths")!= null)
			{
				Element pathsElement = inputElement.getChild("paths");
				List pathElements = pathsElement.getChildren();
				ArrayList pathsArrList = new ArrayList(); 
				for (Iterator itr = pathElements.iterator(); itr.hasNext();){
					Element currentPath = (Element)itr.next();
					pathsArrList.add(currentPath.getValue());
				}
				if(pathsArrList.size()>0)
				{
					paths = new String[pathsArrList.size()];
					pathsArrList.toArray(paths);
					vinput.setPaths(paths);
				}
			}
			
			vinput.setServer(inputElement.getChild("server").getValue());
			vinput.setUser(inputElement.getChild("user").getValue());
			vinput.setPassword(inputElement.getChild("password").getValue());
			vinput.setAccessMethod(inputElement.getChild("access_method")
					.getValue());
			List structures = inputElement.getChildren("structure");

			VInputStructure[] inputStructures = new VInputStructure[structures
					.size()];
			if (structures.size() == 1) {
				String structureID = ((Element) structures.get(0))
						.getAttribute("id").getValue();
				inputStructures[0] = getInputStructure(structureID);
				inputStructures[0].setId(structureID);
				if (inputStructures[0] instanceof ExcelInputStructure) {
					vinput.setType(VInput.EXCEL_INPUT);
				} else if (inputStructures[0] instanceof DBInputStructure) {
					if(inputStructures[0].isDirectMapping())
						vinput.setType(VInput.DIRECT_DB_INPUT);
					else
						vinput.setType(VInput.DB_INPUT);
				} else if (inputStructures[0] instanceof TextInputStructure) {
					if(inputStructures[0].isDirectMapping())
						vinput.setType(VInput.DIRECT_TEXT_INPUT);
					else
						vinput.setType(VInput.TEXT_INPUT);
				} else if (inputStructures[0] instanceof GenericInputStructure) {
					vinput.setType(VInput.GENERIC_INPUT);
				}
				vinput.setInputStructures(inputStructures);
			} else {
				for (int j = 0; j < structures.size(); j++) {
					String structureID = ((Element) structures.get(j))
							.getAttribute("id").getValue();
					inputStructures[j] = (TextInputStructure) getInputStructure(structureID);
					inputStructures[j].setId(structureID);
					if (!(inputStructures[j] instanceof TextInputStructure)) {
						logger
								.error("CF - 1005: ConfigReader.getInputs() -Invalid XML - multiple inputstructures for input id is only supported for text input");
						return null;
					}
				}
				if (inputStructures[0] instanceof TextInputStructure) {
					if(inputStructures[0].isDirectMapping())
						vinput.setType(VInput.DIRECT_TEXT_INPUT);
					else
						vinput.setType(VInput.TEXT_INPUT);
				}
				vinput.setInputStructures(inputStructures);
			}
			vinputs.addElement(vinput);

			logger.debug("ConfigReader.getInputs() - found inupt: id:"
					+ vinput.getId() + " type:" + vinput.getType()
					+ " pernode:" + vinput.isPerNode());
		}
		VInput[] vinputsArraytmp = new VInput[0];
		VInput[] vinputsArray = (VInput[]) vinputs.toArray(vinputsArraytmp);

		logger
				.debug("ConfigReader.getInputs() - finished getInputs for system:"
						+ systemName + " node:" + nodeName);
		return vinputsArray;
	}

	/**
	 * parse XML for InputStructure of this input and map to java objects
	 * according to the type of input one of the following methods is called
	 * getExcelInputStructure | getTextInputStructure | getDBInputStructure
	 * 
	 * @param id -
	 *            the id of the targeted Input
	 * 
	 * @return VInputStructure - object of the inputstructure of this input
	 *         Wrapping either ExcelInputStructure | TextInputStructure |
	 *         DBInputStructure | GenericInputStructure
	 * @throws ApplicationException 
	 */
	private VInputStructure getInputStructure(String id) throws ApplicationException {
		logger
				.debug("ConfigReader.getInputStructure() - started getInputStructure with id:"
						+ id);

		VInputStructure inputStructure = null;
		Element root = document.getRootElement();
		Element inputStructuresElement = root.getChild("input_structures");
		List inputStructures = inputStructuresElement
				.getChildren("input_structure");

		for (Iterator i = inputStructures.iterator(); i.hasNext();) {
			Element inputStructureElement = (Element) i.next();
			String structureID = inputStructureElement.getAttribute("id")
					.getValue();
			if (!id.equals(structureID)) {
				continue;
			}
			
			List choices = inputStructureElement.getChildren();
			Element choice = (Element) choices.get(0);
			if (choice.getName().equals("Excel")) {
				inputStructure = getExcelInputStructure(choice);
			} else if (choice.getName().equals("Text")) {
				inputStructure = getTextInputStructure(choice);
			} else if (choice.getName().equals("DB")) {
				inputStructure = getDBInputStructure(choice);
			} else if (choice.getName().equals("Generic")) {
				inputStructure = getGenericInputStructure(choice);
			}
			inputStructure.setId(structureID);
		}
		logger
				.debug("ConfigReader.getInputStructure() - finished getInputStructure id"
						+ id);
		return inputStructure;
	}

	/**
	 * parse XML for DBInputStructure and map to java objects
	 * 
	 * @param inputStructureTypeElement -
	 *            the XML Element for tag <DB>
	 * 
	 * @return DBInputStructure - object of the DBInputStructure of this element
	 */
	private DBInputStructure getDBInputStructure(
			Element inputStructureTypeElement) {
		logger
				.debug("ConfigReader.getDBInputStructure() - started getDBInputStructure");
		DBInputStructure inputStructure = new DBInputStructure();
		// get sql statement
		String mappedTableName = inputStructureTypeElement.getAttributeValue("mappedTable");
		inputStructure.setMappedTable(mappedTableName);
		String nodeColumn = inputStructureTypeElement.getAttributeValue("nodeColumn");
		inputStructure.setNodeColumn(nodeColumn);
		String idColumn = inputStructureTypeElement.getAttributeValue("idColumn");
		inputStructure.setIdColumn(idColumn);
		String dateColumn = inputStructureTypeElement.getAttributeValue("dateColumn");
		inputStructure.setDateColumn(dateColumn);
		String seqName = inputStructureTypeElement.getAttributeValue("seqName");
		inputStructure.setSeqName(seqName);
		String isDirectMapping=inputStructureTypeElement.getAttributeValue("DirectMapping");
		if(isDirectMapping!=null&&"true".equals(isDirectMapping)){
			inputStructure.setDirectMapping(true);
		}
		String isUserUpdateEvents = inputStructureTypeElement.getAttributeValue("useUpdateEvents");
		if(isUserUpdateEvents!=null&&"true".equals(isUserUpdateEvents)){
			inputStructure.setUseUpdateEvent(true);
		}
		
		String sql = inputStructureTypeElement.getChild("sql").getValue();
		inputStructure.setSqlStatement(sql);
		String dateFormat = inputStructureTypeElement.getChild("sql")
				.getAttribute("dateFormat").getValue();
		inputStructure.setDateFormat(dateFormat);
		// get driver
		String driver = inputStructureTypeElement.getChild("driver").getValue();
		inputStructure.setDriver(driver);
		Element columnsElement = inputStructureTypeElement.getChild("tcolumns");
		if(columnsElement != null){
			List columns = columnsElement.getChildren("tcolumn");
			DataColumn[] dataColumns = new DataColumn[columns.size()];
			int j = 0;
			for (Iterator i = columns.iterator(); i.hasNext();) {
				Element columnElement = (Element) i.next();
				DataColumn dataColumn = new DataColumn();
				dataColumn.setName(columnElement.getAttribute("name").getValue());
				dataColumn.setSrcColumn(columnElement.getAttribute("srcColumn").getValue());
				dataColumns[j] = dataColumn;
				j++;
			}
			inputStructure.setColumns(dataColumns);
		}
		logger.debug("ConfigReader.getDBInputStructure() - finished getDBInputStructure");
		return inputStructure;
	}

	/**
	 * parse XML for TextInputStructure and map to java objects
	 * 
	 * @param inputStructureTypeElement -
	 *            the XML Element for tag <Text>
	 * 
	 * @return TextInputStructure - object of the TextInputStructure of this
	 *         element
	 */
	private TextInputStructure getTextInputStructure(
			Element inputStructureTypeElement) {
		logger
				.debug("ConfigReader.getTextInputStructure() - started getTextInputStructure");
		TextInputStructure inputStructure = new TextInputStructure();
		
		String mappedTableName = inputStructureTypeElement.getAttributeValue("mappedTable");
		inputStructure.setMappedTable(mappedTableName);
		String nodeColumn = inputStructureTypeElement.getAttributeValue("nodeColumn");
		inputStructure.setNodeColumn(nodeColumn);
		String idColumn = inputStructureTypeElement.getAttributeValue("idColumn");
		inputStructure.setIdColumn(idColumn);
		String dateColumn = inputStructureTypeElement.getAttributeValue("dateColumn");
		inputStructure.setDateColumn(dateColumn);
		String seqName = inputStructureTypeElement.getAttributeValue("seqName");
		inputStructure.setSeqName(seqName);
		String isDirectMapping=inputStructureTypeElement.getAttributeValue("DirectMapping");
		if(isDirectMapping!=null&&"true".equals(isDirectMapping)){
			inputStructure.setDirectMapping(true);
		}
		String isUserUpdateEvents = inputStructureTypeElement.getAttributeValue("useUpdateEvents");
		if(isUserUpdateEvents!=null&&"true".equals(isUserUpdateEvents)){
			inputStructure.setUseUpdateEvent(true);
		}
		
		// get Converter
		String converter = inputStructureTypeElement.getChild("converter")
				.getValue();
		inputStructure.setConverter(converter);
		// get columns
		if (inputStructureTypeElement.getChild("extract_date") == null) {
			inputStructure.setExtractDate(false);
		} else {
			inputStructure.setExtractDate(true);
			String format = inputStructureTypeElement.getChild("extract_date")
					.getAttribute("format").getValue();
			inputStructure.setDateFormat(format);
		}/*added by suzan tadrous */
		if (inputStructureTypeElement.getChild("extract_date_monthly") == null) {
			inputStructure.setExtractDatemonthly(false);
		} else {
			inputStructure.setExtractDatemonthly(true);
			
		}
		
		if (inputStructureTypeElement.getChild("last_call_class") == null) {
			inputStructure.setLastCallClassName(null);
		} else {
			String lastCallClassName = inputStructureTypeElement.getChild("last_call_class").getAttribute("name").getValue();
			inputStructure.setLastCallClassName(lastCallClassName);
		}
		
		HashMap<String,String> parametersMap= null;
		if(inputStructureTypeElement.getChild("Parameters-map")!=null)
		{
			parametersMap=new HashMap<String ,String>();
			Element parameterMapElement = inputStructureTypeElement.getChild("Parameters-map");
			List parametersLis=parameterMapElement.getChildren("parameter");
			for(int i=0;i<parametersLis.size();i++)
			{
				Element key=((Element)parametersLis.get(i)).getChild("key");
				Element value=((Element)parametersLis.get(i)).getChild("value");
				parametersMap.put(key.getValue(), value.getValue());
			}
		}
		inputStructure.setParametersMap(parametersMap);
		Element columnsElement = inputStructureTypeElement.getChild("tcolumns");
		List columns = columnsElement.getChildren("tcolumn");
		DataColumn[] dataColumns = new DataColumn[columns.size()];
		int j = 0;
		for (Iterator i = columns.iterator(); i.hasNext();) {
			Element columnElement = (Element) i.next();
			DataColumn dataColumn = new DataColumn();
			dataColumn.setName(columnElement.getAttribute("name").getValue());
			dataColumn.setType(columnElement.getAttribute("type").getValue());
			try {
				dataColumn.setIndex(columnElement.getAttribute("index")
						.getIntValue());
			} catch (DataConversionException e) {
				// no need to handle as this exception will always be discovered
				// earlier by xml validation
			}
			dataColumns[j] = dataColumn;
			j++;
		}
		inputStructure.setColumns(dataColumns);
		logger
				.debug("ConfigReader.getTextInputStructure() - finished getTextInputStructure");
		return inputStructure;
	}

	/**
	 * parse XML for ExcelInputStructure and map to java objects
	 * 
	 * @param inputStructureTypeElement -
	 *            the XML Element for tag <Excel>
	 * 
	 * @return ExcelInputStructure - object of the ExcelInputStructure of this
	 *         element
	 */
	private ExcelInputStructure getExcelInputStructure(
			Element inputStructureTypeElement) {
		logger
				.debug("ConfigReader.getExcelInputStructure() - started getExcelInputStructure");
		ExcelInputStructure inputStructure = new ExcelInputStructure();
		
		// get skip
		String skipStr = inputStructureTypeElement.getChild("skip").getValue();
		int skipNo = Integer.parseInt(skipStr);
		inputStructure.setSkip(skipNo);
		// get horizontal
		if (inputStructureTypeElement.getChild("horizontal") != null) {
			inputStructure.setHorizontal(true);
		} else {
			inputStructure.setHorizontal(false);
		}
		// get sheets
		Element sheetsElement = inputStructureTypeElement.getChild("sheets");
		boolean useInData = Boolean.valueOf(
				sheetsElement.getAttribute("use_in_data").getValue())
				.booleanValue();
		inputStructure.setUseSheetInData(useInData);
		List sheets = sheetsElement.getChildren("sheet_name");
		String[] sheetNames = new String[sheets.size()];
		int j = 0;
		for (Iterator i = sheets.iterator(); i.hasNext();) {
			Element sheetElement = (Element) i.next();
			sheetNames[j] = sheetElement.getValue();
			j++;
		}
		inputStructure.setSheetNames(sheetNames);

		// get columns
		HashMap<String,String> parametersMap= null;
		if(inputStructureTypeElement.getChild("Parameters-map")!=null)
		{
			parametersMap=new HashMap<String ,String>();
			Element parameterMapElement = inputStructureTypeElement.getChild("Parameters-map");
			List parametersLis=parameterMapElement.getChildren("parameter");
			for(int i=0;i<parametersLis.size();i++)
			{
				Element key=((Element)parametersLis.get(i)).getChild("key");
				Element value=((Element)parametersLis.get(i)).getChild("value");
				parametersMap.put(key.getValue(), value.getValue());
			}
		}
		inputStructure.setParametersMap(parametersMap);
		Element columnsElement = inputStructureTypeElement.getChild("columns");
		List columns = columnsElement.getChildren("column");
		DataColumn[] dataColumns = new DataColumn[columns.size()];
		j = 0;
		for (Iterator i = columns.iterator(); i.hasNext();) {
			Element columnElement = (Element) i.next();
			DataColumn dataColumn = new DataColumn();
			dataColumn.setName(columnElement.getAttribute("name").getValue());
			dataColumn.setType(columnElement.getAttribute("type").getValue());
			try {
				dataColumn.setIndex(columnElement.getAttribute("index")
						.getIntValue());
			} catch (DataConversionException e) {
				// no need to handle as this exception will always be discovered
				// earlier by xml validation
			}
			dataColumns[j] = dataColumn;
			j++;
		}
		inputStructure.setColumns(dataColumns);
		logger
				.debug("ConfigReader.getExcelInputStructure() - finished getExcelInputStructure");
		return inputStructure;
	}
	
	/**
	 * parse XML for GenericInputStructure and map to java objects
	 * 
	 * @param inputStructureTypeElement - the XML Element for tag <Generic>
	 * 
	 * @return DynamicInputStructure - object of the DynamicInputStructure of this element
	 * @throws ApplicationException 
	 */
	private GenericInputStructure getGenericInputStructure(Element inputStructureTypeElement) throws ApplicationException{
		logger.debug("ConfigReader.getGenericInputStructure() - started getGenericInputStructure");

		GenericInputStructure inputStructure = null;
		String controlFileName = inputStructureTypeElement.getChild("control_file").getValue();
		ControlFileReader controlFileReader = new ControlFileReader(controlFileName,logger);
		inputStructure = controlFileReader.getGenericInputStructure();
		
		if (inputStructureTypeElement.getChild("extract_date") == null) {
			inputStructure.setExtractDate(false);
		} else {
			inputStructure.setExtractDate(true);
			String format = inputStructureTypeElement.getChild("extract_date")
					.getAttribute("format").getValue();
			inputStructure.setDateFormat(format);
		}

		logger.debug("ConfigReader.getDynamicInputStructure() - finished getGenericInputStructure");
		return inputStructure;
	}
	/**
	 * parse XML for System and map to java objects
	 * 
	 * @param systemName -
	 *            the name of the system to be mapped to attribute 'name' of XML
	 *            tag <System>
	 * 
	 * @return VSystem - System object for this name
	 */
	public VSystem getSystem(String systemName) throws ApplicationException {
		boolean found = false;
		logger.debug("ConfigReader.getSystem() - started getSystem("
				+ systemName + ")");
		VSystem system = new VSystem();
		Element root = document.getRootElement();
		Element systemsElement = root.getChild("Systems");
		List systems = systemsElement.getChildren("System");

		for (Iterator i = systems.iterator(); i.hasNext();) {
			Element systemElement = (Element) i.next();
			// Get the name of the system
			String name = systemElement.getAttribute("name").getValue();
			if (!name.equals(systemName)) {
				continue;
			}
			found = true;
			Attribute generic = systemElement.getAttribute("generic");
			if(generic!=null)
				system.setGeneric(Boolean.parseBoolean(generic.getValue()));
			Element descriptionElement = systemElement.getChild("description");
			if(descriptionElement != null)
				system.setDescription(descriptionElement.getValue());
			system.setName(name);
			VNode[] systemNodes = getNodes(systemName);
			system.setNodes(systemNodes);
			Element inputsElement = systemElement.getChild("inputs");
			VInput[] inputs = getInputs(inputsElement, systemName, "");
			system.setInputs(inputs);
		}
		logger.debug("ConfigReader.getSystem() - finished getSystem("
				+ systemName + ")");
		if (found) {
			return system;
		} else {
			logger
					.error("CF - 2000: ConfigReader.getSystem() - system not found for name:"
							+ systemName);
			throw new ApplicationException("system not found for name:"
					+ systemName);
		}
	}

	/**
	 * parse XML for System Node and map to java objects
	 * 
	 * @param systemName -
	 *            the name of the system to be mapped to attribute 'system' of
	 *            XML tag <Node>
	 * @param nodeName -
	 *            the name of the node to be mapped to attribute 'name' of XML
	 *            tag <Node>
	 * 
	 * @return VNode - Node object for this sytem name and node name
	 */
	public VNode getNode(String systemName, String nodeName)
			throws ApplicationException {
		boolean found = false;
		logger.debug("ConfigReader.getNode() - started getNode(" + systemName
				+ "," + nodeName + ")");
		VNode systemNode = new VNode();
		Element root = document.getRootElement();
		Element nodesElement = root.getChild("Nodes");
		List vNodes = nodesElement.getChildren("Node");

		for (Iterator i = vNodes.iterator(); i.hasNext();) {
			Element nodeElement = (Element) i.next();
			// Get the name of the node
			String vNodeName = nodeElement.getAttribute("name").getValue();
			String vSystemName = nodeElement.getAttribute("system").getValue();
			if (!(vSystemName.equals(systemName) && vNodeName.equals(nodeName))) {
				continue;
			}
			found = true;
			systemNode.setSystemName(systemName);
			systemNode.setName(vNodeName);
			Element inputsElement = nodeElement.getChild("inputs");
			VInput[] inputs = getInputs(inputsElement, systemName, nodeName);
			systemNode.setInputs(inputs);
		}
		logger.debug("ConfigReader.getNode() - finished getNode(" + systemName
				+ "," + nodeName + ")");

		if (found) {
			return systemNode;
		} else {
			logger.error("CF - 2001: ConfigReader.getNode() - Node not found for name:"
					+ systemName);
			throw new ApplicationException("Node not found for name:"
					+ systemName);
		}

	}

	/**
	 * parse XML for System Nodes and map to java objects
	 * 
	 * @param systemName -
	 *            the name of the system to be mapped to attribute 'system' of
	 *            XML tag <Node>
	 * 
	 * @return VNode[] - Array of Nodes of given system
	 */
	public VScheduledNode[] getNodes(String systemName) throws ApplicationException {
		VScheduledNode[] nodesArray = new VScheduledNode[0];
		boolean found = false;
		logger.debug("ConfigReader.getNodes() - started getNodes(" + systemName
				+ ")");
		Vector nodesVector = new Vector();
		Element root = document.getRootElement();
		Element nodesElement = root.getChild("Nodes");
		List vNodes = nodesElement.getChildren("Node");

		for (Iterator i = vNodes.iterator(); i.hasNext();) {
			Element nodeElement = (Element) i.next();
			// Get the name of the node
			String name = nodeElement.getAttribute("name").getValue();
			String vSystemName = nodeElement.getAttribute("system").getValue();
			if (!vSystemName.equals(systemName)) {
				continue;
			}
			found = true;
			VScheduledNode systemNode = new VScheduledNode();
			systemNode.setSystemName(systemName);
			systemNode.setName(name);
			Element inputsElement = nodeElement.getChild("inputs");
			VInput[] inputs = getInputs(inputsElement, systemName, name);
			systemNode.setInputs(inputs);
			systemNode.setCronExpression(getNodeCronExpression(systemName,name));
			nodesVector.addElement(systemNode);
		}
		nodesArray = (VScheduledNode[]) nodesVector.toArray(nodesArray);
		logger.debug("ConfigReader.getNodes() - finished getNodes("
				+ systemName + ")");

			return nodesArray;
		}

	private String getNodeCronExpression(String systemName, String nodeName) {
		String cronExpression = "";
		InputStream jobsInpStream = null;
		try {
			String  jobsFilePath = PropertyReader.getApplicationPath() + File.separator + "resources" + File.separator + PropertyReader.getJobsFileRelativeURL();
			jobsInpStream = new FileInputStream(jobsFilePath);
			Document jobsDoc = XMLUtils.getXMLDocument(jobsInpStream);
			Element jobsRoot = jobsDoc.getRootElement();
			Element[] elements = XMLUtils.getElementChildren(jobsRoot,"job");
			for (int i = 0;i<elements.length;i++) {
				Element jobDetail = XMLUtils.getElementChild(elements[i],"job-detail");
				Element jobDataMap = XMLUtils.getElementChild(jobDetail,"job-data-map");
				Element[] mapEntries = XMLUtils.getElementChildren(jobDataMap,"entry"); 
				boolean neededSystem = false;
				boolean neededNode = false;
				for(int j = 0;j< mapEntries.length;j++){
					String key = XMLUtils.getElementChild(mapEntries[j],"key").getValue().trim();
					String value = XMLUtils.getElementChild(mapEntries[j],"value").getValue().trim();
					if("SystemName".equals(key) && systemName.equals(value))
						neededSystem = true;
					if("NodeName".equals(key) && nodeName.equals(value))
						neededNode = true;
				}
				if(neededSystem&&neededNode){
					Element triggerElement = XMLUtils.getElementChild(elements[i], "trigger");
					Element cronElement = null;
					Element cronExprElement = null;
					if(triggerElement != null)
						cronElement = XMLUtils.getElementChild(triggerElement, "cron");
					if(cronElement != null)
						cronExprElement = XMLUtils.getElementChild(cronElement, "cron-expression");
					if(cronExprElement != null)
						cronExpression = cronExprElement.getValue().trim();
					break;
				}
			}

		} catch (FileNotFoundException e) {
			logger.error("ConfigReader.getNodeCronExpression() : couldn't open xml input file for system ["+systemName+"] and  node ["+nodeName+"] :"+ e);
		}catch(Exception e){
			//logger.error("ConfigReader.getNodeCronExpression() : error occured while get cron expression for system ["+systemName+"] and  node ["+nodeName+"] : "+ e);
		} 
		finally{
			if(jobsInpStream != null)
				try {
					jobsInpStream.close();
				} catch (IOException e) {
					logger.error("ConfigReader.getNodeCronExpression() : error occured while closing jobs file for system ["+systemName+"] and  node ["+nodeName+"] : "+ e);				} 
		}
		return cronExpression;
	}

	/**
	 * @param generic-
	 * 			if generic specified with true the returned systems only will be generic system
	 * 			else if generic was false then all systems will be returned
	 * @return all systems in the data collection
	 * @throws ApplicationException
	 */
	public ArrayList getDCSystemsOverview(boolean generic) throws ApplicationException{
		ArrayList allSystems = new ArrayList();
		logger.debug("ConfigReader.getDCSystems() - started getDCSystems()");
		Element root = document.getRootElement();
		Element systemsElement = root.getChild("Systems");
		List systemsList = systemsElement.getChildren("System");
		for (Iterator i = systemsList.iterator(); i.hasNext();) {
			Element systemElement = (Element) i.next();
			// Get the name of the system
			String name = systemElement.getAttribute("name").getValue();
			
			Element descriptionElement = systemElement.getChild("description");
			String description = "";
			if(descriptionElement != null)
				description = descriptionElement.getValue();
			
			Attribute genericAttribute = systemElement.getAttribute("generic");
			boolean isGenericSystem = false;
			if(genericAttribute != null)
				isGenericSystem = Boolean.parseBoolean(genericAttribute.getValue());
			
			if(generic)
			{
				if(isGenericSystem)
					allSystems.add(new String[]{name,description});
			}else
				allSystems.add(new String[]{name,description});
		}
		logger.debug("ConfigReader.getDCSystems() - finished getDCSystems()");
		return allSystems;
	}
	
	public boolean isDCSystemsExist(String systemName) throws ApplicationException{
		logger.debug("ConfigReader.isDCSystemsExist() - started isDCSystemsExist()");
		Element root = document.getRootElement();
		Element systemsElement = root.getChild("Systems");
		List systemsList = systemsElement.getChildren("System");
		boolean systemExists = false;
		for (Iterator i = systemsList.iterator(); i.hasNext();) {
			Element systemElement = (Element) i.next();
			// Get the name of the system
			String name = systemElement.getAttribute("name").getValue();
			if(!name.equals(systemName)){
				continue;
			}
			systemExists = true;
		}
		logger.debug("ConfigReader.isDCSystemsExist() - finished isDCSystemsExist()");
		return systemExists;
	}
	
	/**
	 * @param inputType -
	 * 		integer representing input type of needed input structures 
	 * @return all input structures in the data collection
	 * @throws ApplicationException
	 */
	public VInputStructure[] getDCInputStructures(int inputType) throws ApplicationException {
		logger.debug("ConfigReader.getDCInputStructures() - started getDCInputStructures()");
		VInputStructure[] inputStructures = new VInputStructure[]{};
		Vector allInputStructures = new Vector();
		Element root = document.getRootElement();
		Element inputStructuresElement = root.getChild("input_structures");
		List inputStructuresList = inputStructuresElement.getChildren("input_structure");

		for (Iterator i = inputStructuresList.iterator(); i.hasNext();) {
			Element inputStructureElement = (Element) i.next();
			String structureID = inputStructureElement.getAttribute("id").getValue();
			Element choice = ((Element)inputStructureElement.getChildren().get(0));
			String structureType = choice.getName();
			if(inputType == -1){
				VInputStructure inpStructure = getInputStructure(structureID);
				inpStructure.setId(structureID);
				allInputStructures.add(inpStructure);
			}else if(inputType == VInput.TEXT_INPUT && structureType.equals("Text")){
				VInputStructure inpStructure = getTextInputStructure(choice);
				inpStructure.setId(structureID);
				allInputStructures.add(inpStructure);
			}else if(inputType == VInput.DB_INPUT && structureType.equals("DB")){
				VInputStructure inpStructure = getDBInputStructure(choice);
				inpStructure.setId(structureID);
				allInputStructures.add(inpStructure);
			}else if(inputType == VInput.EXCEL_INPUT && structureType.equals("Excel")){
				VInputStructure inpStructure = getExcelInputStructure(choice);
				inpStructure.setId(structureID);
				allInputStructures.add(inpStructure);
			}else if(inputType == VInput.GENERIC_INPUT && structureType.equals("Generic")){
				VInputStructure inpStructure = getGenericInputStructure(choice);
				inpStructure.setId(structureID);
				allInputStructures.add(inpStructure);
			}
			
		}
		inputStructures = (VInputStructure[])allInputStructures.toArray(inputStructures);
		logger.debug("ConfigReader.getDCInputStructures() - finish getDCInputStructures()");
		return inputStructures;
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
	
	
	public static void main(String[] args) {
		try {
			PropertyReader.init("D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\Production Deployments\\Round 4\\Data Collection\\Old Packages\\DataCollection\\");
			ConfigReader reader=new ConfigReader(Logger.getLogger("main"));
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
