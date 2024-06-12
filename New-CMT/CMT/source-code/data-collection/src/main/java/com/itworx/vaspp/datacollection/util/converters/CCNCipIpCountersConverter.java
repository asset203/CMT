package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CCNCipIpCountersConverter extends AbstractTextConverter {
	private Logger logger;
	private static String[] CIP_IP_COUNTERS = null;
	/**
	 * Converting the input file to comma seperated file.
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("CCNCipIpCountersConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		String ccnCounterString = PropertyReader.getCipIpCounters();
		CIP_IP_COUNTERS = ccnCounterString.split("\\|");
		HashMap<String, double[]> hourMap = new HashMap<String, double[]>();
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "CCNCipIpConverterFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			for (int i = 0; i < inputFiles.length; i++)
			{
				String fileName = inputFiles[i].getName();
				String dateHourString = fileName.substring(fileName.indexOf("A")+1,fileName.indexOf("A")+12).trim();
			
				domFactory.setNamespaceAware(false);
				DocumentBuilder builder = domFactory.newDocumentBuilder();
				builder.setEntityResolver(new EntityResolver(){
					public InputSource resolveEntity(String arg0, String arg1)
							throws SAXException, IOException {
						return new InputSource(new StringReader(""));
					}});
				Document doc = builder.parse(inputFiles[i]);

				XPathExpression measObjInstIdsExpr = xpath.compile("/mdc/md/mi/mv/moid");
				Object measObjInstIdsResult = measObjInstIdsExpr.evaluate(doc, XPathConstants.NODESET);
				NodeList measObjInstIdsNodes = (NodeList) measObjInstIdsResult;
				for (int j = 0; j < measObjInstIdsNodes.getLength(); j++) {
					Node currentNode = measObjInstIdsNodes.item(j);
					String measObjInstIdTxt = currentNode.getTextContent();
					if(measObjInstIdTxt!=null && measObjInstIdTxt!= ""){
						measObjInstIdTxt = measObjInstIdTxt.replaceAll("\\s", "").replaceAll("\n", "").replaceAll("\r", "");
						String[] measObjInstTxtArr = measObjInstIdTxt.split("=|,");
						if(measObjInstTxtArr.length == 4 && "_SYSTEM".equals(measObjInstTxtArr[3])){
							int neededIdx = Utils.indexOfMatched(measObjInstTxtArr[1], CIP_IP_COUNTERS);
							if(neededIdx > -1){
								NodeList nodeChildren = currentNode.getParentNode().getChildNodes();
								for(int n=0;n <nodeChildren.getLength(); n++){
									Node measureNode = nodeChildren.item(n);
									if("r".equals(measureNode.getNodeName())){
										String measureValTxt = measureNode.getTextContent();
										if(!"".equals(measureValTxt)){
											Pattern pattern = Pattern.compile(CIP_IP_COUNTERS[neededIdx]);
											Matcher matcher = pattern.matcher(measObjInstTxtArr[1]);
											if(matcher.find()){
												String destinationRealm = matcher.group(1);
												String chargingContext = matcher.group(2);
												double measureVal = Double.parseDouble(measureValTxt);
												double measuresArr[] = null;
												String mapKey = dateHourString+","+destinationRealm+","+chargingContext;
												if(hourMap.containsKey(mapKey))
												{
													measuresArr = (double[])hourMap.get(mapKey);
												}
												else
												{
													measuresArr = new double[CIP_IP_COUNTERS.length];
													hourMap.put(mapKey, measuresArr);
												}
												measuresArr[neededIdx] += measureVal;
											}
										}
										break;
									}
								}
							}
						}
					}
				}
			}
			for (Iterator iter = hourMap.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				double[] measuresArr = (double[])hourMap.get(key);
				String[] keyParts = key.split(",");  
				String datePart = keyParts[0];
				String destinationRealm = keyParts[1];
				String chargingContext = keyParts[2];
				String dateHour = Utils.convertToDateString(Utils.convertToDate(datePart, "yyyyMMdd.HH"), Utils.defaultFormat);
				String measuresStr = Arrays.toString(measuresArr).replaceAll("(\\[)|(\\])|(\\s)", "");
				outputStream.write(dateHour+","+destinationRealm+","+chargingContext+","+measuresStr);
				outputStream.newLine();
			}
			
			hourMap.clear();
			hourMap = null;
			outputStream.flush();
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CCNCipIpCountersConverter.convert() - "+ inputFiles[0].getName() + " converted");	
		} catch (FileNotFoundException e) {
			logger.error("CCNCipIpCountersConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CCNCipIpCountersConverter.convert() - Couldn't read input file " + e);
			throw new ApplicationException(e);
		} catch (ParserConfigurationException e) {
			logger.error("CCNCipIpCountersConverter.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (SAXException e) {
			logger.error("CCNCipIpCountersConverter.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (XPathExpressionException e) {
			logger.error("CCNCipIpCountersConverter.convert() - Invalid use of XPath expression "	+ e);
			throw new ApplicationException(e);
		}
		
		logger.debug("CCNCipIpCountersConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}	
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\VASPortalWF5\\Source Code\\DataCollection");
			CCNCipIpCountersConverter s = new CCNCipIpCountersConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20090701.2020-2025_jambala_CcnCounters");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
