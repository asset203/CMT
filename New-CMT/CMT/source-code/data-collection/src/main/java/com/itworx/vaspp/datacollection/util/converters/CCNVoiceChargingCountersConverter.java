package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class CCNVoiceChargingCountersConverter extends AbstractTextConverter {
	private Logger logger;
//	private static String[] VOICE_CHARGING_COUNTERS = null;
	private Map  <String ,CounterObj> dateVsObj=new HashMap<String,CounterObj>() ;
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
		logger.debug("CCNVoiceChargingCountersConverter.convert() - started converting input files ");
		File[] outputFiles = new File[1];
		//String ccnCounterString = PropertyReader.getVoiceChargingCounters();
		
		//VOICE_CHARGING_COUNTERS = ccnCounterString.split("\\|");
		HashMap<String, double[]> hourMap = new HashMap<String, double[]>();
		try {
			String path = PropertyReader.getConvertedFilesPath();			
			File output = new File(path, "CCNVoiceChargingCountersConverterFile");
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
							//int neededIdx = Utils.indexOf(measObjInstTxtArr[1], VOICE_CHARGING_COUNTERS);
							//if(neededIdx > -1){
								NodeList nodeChildren = currentNode.getParentNode().getChildNodes();
								for(int n=0;n <nodeChildren.getLength(); n++){
									Node measureNode = nodeChildren.item(n);
									if("r".equals(measureNode.getNodeName())){
										String measureValTxt = measureNode.getTextContent();
										if(!"".equals(measureValTxt)){
											String key=dateHourString+","+measObjInstTxtArr[1];
											double measureVal = Double.parseDouble(measureValTxt);
											if(dateVsObj.containsKey(key))
											{
												CounterObj obj=dateVsObj.get(key);
												obj.setRouting(obj.getRouting()+measureVal);
												obj.getMaxRouting().add(measureVal);
												dateVsObj.remove(key);
												dateVsObj.put(key, obj);
											}
											else
											{
												CounterObj obj=new CounterObj ();
												obj.setRouting(measureVal);
												obj.getMaxRouting().add(measureVal);
												dateVsObj.put(key, obj);
											}
											/*double measureVal = Double.parseDouble(measureValTxt);
											double measuresArr[] = null;
											if(hourMap.containsKey(dateHourString))
											{
												measuresArr = (double[])hourMap.get(dateHourString);
											}
											else
											{
												measuresArr = new double[VOICE_CHARGING_COUNTERS.length*2];
												hourMap.put(dateHourString, measuresArr);
											}
											measuresArr[neededIdx] += measureVal;
											double currentTopValue = measuresArr[VOICE_CHARGING_COUNTERS.length+neededIdx]; 
											measuresArr[VOICE_CHARGING_COUNTERS.length+neededIdx] = (measureVal > currentTopValue)? measureVal : currentTopValue;
											*/
										}
										break;
									}
								}
							//}
						}
					}
				}
			}
			for (Iterator iter = dateVsObj.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				CounterObj obj = (CounterObj)dateVsObj.get(key);
				double maxRouting =minMaxAvg(obj.getMaxRouting());
				Calendar cal = Calendar.getInstance();

				Date date = Utils.convertToDate(key.split(",")[0], "yyyyMMdd.HH");
				cal.setTime(date);
				cal.add(Calendar.HOUR, 2);
				String dateHour = Utils.convertToDateString(cal.getTime(), Utils.defaultFormat);
			//	String dateHour = Utils.convertToDateString(Utils.convertToDate(key.split(",")[0], "yyyyMMdd.HH"), Utils.defaultFormat);				
				outputStream.write(dateHour+","+key.split(",")[1]+","+((CounterObj)dateVsObj.get(key)).getRouting()+","+maxRouting);
				outputStream.newLine();
			}
			
			hourMap.clear();
			hourMap = null;
			outputStream.flush();
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CCNVoiceChargingCountersConverter.convert() - "+ inputFiles[0].getName() + " converted");	
		} catch (FileNotFoundException e) {
			logger.error("CCNVoiceChargingCountersConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CCNVoiceChargingCountersConverter.convert() - Couldn't read input file " + e);
			throw new ApplicationException(e);
		} catch (ParserConfigurationException e) {
			logger.error("CCNVoiceChargingCountersConverter.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (SAXException e) {
			logger.error("CCNVoiceChargingCountersConverter.convert() - Couldn't parse input file " + e);
			throw new ApplicationException(e);
		} catch (XPathExpressionException e) {
			logger.error("CCNVoiceChargingCountersConverter.convert() - Invalid use of XPath expression "	+ e);
			throw new ApplicationException(e);
		}
		
		logger.debug("CCNVoiceChargingCountersConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}	
	public static void main(String ag[]) {
		try {
			File xx = new File(".");
			System.out.println(xx.getCanonicalPath());
			PropertyReader.init(xx.getCanonicalPath());
			CCNVoiceChargingCountersConverter s = new CCNVoiceChargingCountersConverter();
			File[] input = new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120603_checking_data_loss_ccn_voice_charging_counters\\").listFiles();
			//input[0]=new File("D:\\CCN_New_1252847596993_228_A20090909.0455-0500_jambala_CcnCounters.20090909060134");
			/*input[1]=new File("D:\\build\\phase10\\DataCollection\\CCN_New_1252847596993_228_A20110512.0855-0600_jambala_CcnCounters.20110512072931");
			input[2]=new File("D:\\build\\phase10\\DataCollection\\CCN_New_1252847596993_228_A20110512.0820-0525_jambala_CcnCounters.20110512072931");
			input[3]=new File("D:\\build\\phase10\\DataCollection\\CCN_New_1252847596993_228_A20110512.0840-0445_jambala_CcnCounters.20110512072931");
			input[4]=new File("D:\\build\\phase10\\DataCollection\\CCN_New_1252847596993_228_A20110512.0855-0500_jambala_CcnCounters.20090909060134");*/
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	class CounterObj
	{
		public double routing=0;
		public double getRouting() {
			return routing;
		}
		public void setRouting(double routing) {
			this.routing = routing;
		}
		public List getMaxRouting() {
			return maxRouting;
		}
		public void setMaxRouting(List maxRouting) {
			this.maxRouting = maxRouting;
		}
		public List maxRouting = new ArrayList();
	}
	private double minMaxAvg(List data)
	{
		double min,max,avg;
		double any;
		
		Iterator listIterator = data.iterator(); 
		any=(Double)listIterator.next();
		avg =any;
		max = avg ;
		min = avg ;		
		while(listIterator.hasNext())
		{
			double element = (Double)listIterator.next(); 
		
			if(element<min)
			{
				min=element;
			}
			if(element>max)
			{
				max=element;
			}		
		}	
		return max;
	}
}
