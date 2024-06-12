package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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

public class VPNPlatformMeasures extends AbstractTextConverter{

	private Logger logger;
	private final static String[] PLATFORM_MEASURES = new String[]{"CpuLoad","MemUsage","MaxCpuLoad"};
	private int[] platformMeasuresIndices = new int[PLATFORM_MEASURES.length];
	
	public File[] convert(File[] inputFiles, String systemName) throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("VPNPlatformMeasures.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		HashMap hourMap = new HashMap();
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "VPNPlatformMeasuresFile");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			for (int i = 0; i < inputFiles.length; i++)
			{
				String fileName = inputFiles[i].getName();
				Document doc;
				String dateHourString = fileName.substring(fileName.indexOf("A")+1,fileName.indexOf("A")+12).trim();
			
				domFactory.setNamespaceAware(false);
				DocumentBuilder builder = domFactory.newDocumentBuilder();
				builder.setEntityResolver(new EntityResolver(){
					public InputSource resolveEntity(String arg0, String arg1)
							throws SAXException, IOException {
						return new InputSource(new StringReader(""));
					}});
				if(!(inputFiles[i].length()==0)){
				 doc= builder.parse(inputFiles[i]);
				XPathExpression measureIdsExpr = xpath.compile("/mdc/md/mi/mt");
				Object measureIdsResult = measureIdsExpr.evaluate(doc, XPathConstants.NODESET);
				NodeList measureIdsNodes = (NodeList) measureIdsResult;
				for (int j = 0; j < measureIdsNodes.getLength(); j++) {
					Node currentNode = measureIdsNodes.item(j);
					String measureTxt = currentNode.getTextContent();
					if(measureTxt!=null && measureTxt!= ""){
						int measureIdx = Utils.indexOf(measureTxt, PLATFORM_MEASURES);
						if(measureIdx > -1){
							platformMeasuresIndices[measureIdx] = j;
						}
					}
				}
				
				XPathExpression measObjInstIdsExpr = xpath.compile("/mdc/md/mi/mv/moid");
				Object measObjInstIdsResult = measObjInstIdsExpr.evaluate(doc, XPathConstants.NODESET);
				NodeList measObjInstIdsNodes = (NodeList) measObjInstIdsResult;
				for (int j = 0; j < measObjInstIdsNodes.getLength(); j++) {
					Node currentNode = measObjInstIdsNodes.item(j);
					String measObjInstIdTxt = currentNode.getTextContent();
					if(measObjInstIdTxt!=null && measObjInstIdTxt!= ""){
						measObjInstIdTxt = measObjInstIdTxt.replaceAll("\\s", "").replaceAll("\n", "").replaceAll("\r", "");
						String[] measObjInstTxtArr = measObjInstIdTxt.split("=|,");
						if(measObjInstTxtArr.length == 4 && "DEFAULT".equals(measObjInstTxtArr[1])){
							NodeList nodeChildren = currentNode.getParentNode().getChildNodes();
							int curIdx = -1;
							//values arranged as PLATFORM_MEASURES field {"CpuLoad","MaxCpuLoad","MemUsage","MaxMemUsage"}
							double platformMeasuresValues[] = new double[PLATFORM_MEASURES.length];
							for(int n=0;n<nodeChildren.getLength();n++){
								Node measureNode = nodeChildren.item(n);
								if("r".equals(measureNode.getNodeName())){
									curIdx++;
									int neededIdx = indexOf(curIdx, platformMeasuresIndices);
									if(neededIdx > -1){
										String measureValTxt = measureNode.getTextContent();
										if(!"".equals(measureValTxt)){
											double measureVal = Double.parseDouble(measureValTxt);
											platformMeasuresValues[neededIdx] = measureVal;
										}
									}
								}
							}
							PlatformMeasures platformMeasures = null;
							String key = dateHourString+","+measObjInstTxtArr[3];
							if(hourMap.containsKey(key))
							{
								platformMeasures = (PlatformMeasures)hourMap.get(key);
							}
							else
							{
								platformMeasures = new PlatformMeasures();
								hourMap.put(key, platformMeasures);
							}
							platformMeasures.updateMeasures(platformMeasuresValues);
						}
					}
				}
			}
				}
			
			for (Iterator iter = hourMap.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				PlatformMeasures values = (PlatformMeasures)hourMap.get(key);
				String[] keyParts = key.split(",");
				Calendar cal = Calendar.getInstance();

				Date date = Utils.convertToDate(keyParts[0], "yyyyMMdd.HH");
				cal.setTime(date);
				cal.add(Calendar.HOUR, 2);
				String dateHour = Utils.convertToDateString(cal.getTime(), Utils.defaultFormat);
				/*String dateHour = Utils.convertToDateString(Utils.convertToDate(keyParts[0], "yyyyMMdd.HH"), Utils.defaultFormat);*/
				//System.out.print(dateHour+","+keyParts[1]+",");
				//System.out.println(dateHour+","+keyParts[1]+","+values.toString());
				outputStream.write(dateHour+","+keyParts[1]+","+values.toString());
				
				outputStream.newLine();
			}
			
			hourMap.clear();
			hourMap = null;
			outputStream.flush();
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("VPNPlatformMeasures.convert() - " + inputFiles[0].getName() + " converted");	
		} catch (FileNotFoundException e) {
			logger.error("VPNPlatformMeasures.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("VPNPlatformMeasures.convert() - Couldn't read input file " + e);
			throw new ApplicationException(e);
		} catch (ParserConfigurationException e) {
			logger.error("VPNPlatformMeasures.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (SAXException e) {
			logger.error("VPNPlatformMeasures.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (XPathExpressionException e) {
			logger.error("VPNPlatformMeasures.convert() - nvalid use of XPath expression " + e);
			throw new ApplicationException(e);
		}
		logger.debug("VPNPlatformMeasures.convert() - finished converting input files successfully ");
		return outputFiles;
	}
	private int indexOf(int val, int[] arr) {
		int exists = -1;
		for(int i = 0;i<arr.length;i++)
		{
			if(val== arr[i])
			{
				exists = i;
				break;
			}
		}
		return exists;
	}
	private class PlatformMeasures{
		public double minCpuLoad = 0;
		public double maxCpuLoad = 0;
		public double sumCpuLoad = 0;

        public double maxMaxCpuLoad = 0;
        public double sumMaxCpuLoad = 0;

		public double minMemUsage = 0;
		public double maxMemUsage = 0;
		public double sumMemUsage = 0;

		public double totalCount = 0;
		
		//platformMeasuresValues {"CpuLoad","MaxCpuLoad","MemUsage","MaxMemUsage"}
		public void updateMeasures(double[] platformMeasuresValues){
			
			double newCpuLoad =  platformMeasuresValues[0];
			double newMemUsage =  platformMeasuresValues[1];
			double newMaxCpuLoad =  platformMeasuresValues[2];

			minCpuLoad = (newCpuLoad<minCpuLoad || minCpuLoad == 0)?newCpuLoad:minCpuLoad;
			maxCpuLoad = (newCpuLoad>maxCpuLoad)?newCpuLoad:maxCpuLoad;
			sumCpuLoad += newCpuLoad;

            maxMaxCpuLoad = (newMaxCpuLoad>maxMaxCpuLoad)?newMaxCpuLoad:maxMaxCpuLoad;
            sumMaxCpuLoad += newMaxCpuLoad;

			
			minMemUsage = (newMemUsage<minMemUsage || minMemUsage == 0)?newMemUsage:minMemUsage;
			maxMemUsage = (newMemUsage>maxMemUsage)?newMemUsage:maxMemUsage;
			sumMemUsage += newMemUsage;
			
			totalCount++;
		}
		
		public String toString(){
			//System.out.println((sumCpuLoad/totalCount)+","+minCpuLoad+","+maxCpuLoad+","+(sumMemUsage/totalCount)+","+minMemUsage+","+maxMemUsage+",["+sumCpuLoad+"],["+sumMemUsage+"]"+totalCount);
			return (sumCpuLoad/totalCount)+","+minCpuLoad+","+maxCpuLoad+","+(sumMemUsage/totalCount)+","+minMemUsage+","+maxMemUsage+","+maxMaxCpuLoad+","+(sumMaxCpuLoad/totalCount);
		}
	}
	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\Projects 2014\\Vodafone\\SVN\\SourceCode\\vodafone-prod\\etc\\prodConfFiles\\dev_zone\\CMTDataCollection");
			VPNPlatformMeasures s = new VPNPlatformMeasures();
			File[] input = new File("D:\\Projects 2014\\Vodafone\\Worx\\Production Maintenance\\NEW_DCCN_CPU\\DCCN").listFiles();
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}