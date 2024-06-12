package com.itworx.vaspp.datacollection.util.converters;

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

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;


import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VPNPMFStatisticsConverter extends AbstractTextConverter{
	private org.jdom.Document document;
	private Logger logger;
	  private Document xmlDocument;
	  private Map<String , List> countersKeyVsValues= new HashMap<String, List>() ;
	  public VPNPMFStatisticsConverter()
	  {
	  	
	  }
		public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException {
			logger = Logger.getLogger(systemName);
			logger
					.debug("Inside VPNPMFStatisticsConverter convert - started converting input files");
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
				  String date="";
				  String pgfStatistics="";
				  String procName="";
				
				  try
				  {
		        document = saxfac.build(inputFiles[i]);
				  }
				catch(JDOMParseException e)
				  {
						continue;
				  }
		        Element root = document.getRootElement();
		        Element mdElement = root.getChild("md");
		        Element mfhElement = root.getChild("mfh");	
		        Element cbtElement;
		        if(mfhElement!=null){
		        	cbtElement=mfhElement.getChild("cbt");
		        	if(cbtElement!=null){
		        if(cbtElement.getValue().toString().contains("."))
		       date=cbtElement.getValue().toString().split("\\.")[0];
		        else 
		        	date=cbtElement.getValue().toString();
				date=getDate(date);	
				if(mdElement!=null){
				List  miElements = mdElement.getChildren("mi");
				for (int mi=0;mi<miElements.size();mi++)
				{
					Element miElement=(Element)miElements.get(mi);
					List mtElements =miElement.getChildren("mt");
					List mvElements =miElement.getChildren("mv");
					if(mtElements.size()!=0){
					String countersNames[]= new String[mtElements.size()];
					for(int init =0;init<countersNames.length;init++)
					{
						countersNames[init]= ((Element)mtElements.get(init)).getValue();
						
					}	
					for(int mv=0;mv<mvElements.size();mv++)				
						{
						  Element mvElement =(Element)mvElements.get(mv);
						  Element moidElement =mvElement.getChild("moid");
						  if(moidElement!=null&&moidElement.getValue().contains("PgfStatistics="))
							  pgfStatistics=moidElement.getValue().toString().split("PgfStatistics=")[1].split(",")[0];
						  else
							  pgfStatistics="" ;
						  if(moidElement!=null&&moidElement.getValue().contains("Source ="))
							  procName=moidElement.getValue().toString().split("Source =")[1].trim();
						  else
							  procName="";
						 
						  List rElements =mvElement.getChildren("r");						 
						  for(int r=0;r<rElements.size();r++)
						  {
							 
							  String key=date+","+pgfStatistics+","+procName+","+countersNames[r];
							 
							  Element rElement =(Element)rElements.get(r);
							 
								double counterValue =Double.parseDouble(!rElement.getValue().toString().equalsIgnoreCase("")?rElement.getValue().toString():"0.0");								  
								  
								  if(countersKeyVsValues.containsKey(key))
								  {
									 List list = countersKeyVsValues.get(key);
									 list.add(counterValue);
							           countersKeyVsValues.put(key, list);
								  }
								  else
								  {
									  List list =new ArrayList();
									  list.add(counterValue);
									  countersKeyVsValues.put(key, list);
								  }
						  }
						}
					}
				}
			}}}}
			Iterator it=countersKeyVsValues.keySet().iterator();
			while(it.hasNext())
			{
				Object key=it.next();
				double minMaxAvg[]=getMinMaxAvg((List)countersKeyVsValues.get(key));
				outputStream.write(key+","+minMaxAvg[0]+","+minMaxAvg[1]+","+minMaxAvg[2]);
				 outputStream.newLine();
				
			}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("VPNPMFStatisticsConverter.convert() - finished converting input files successfully ");
	


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
				VPNPMFStatisticsConverter s = new VPNPMFStatisticsConverter();
				File[] input = new File[1];
				input[0]=new File("D:\\build\\phase10\\DataCollection\\A20110521.0000-0005_jambala_PgfStatistics.20110521000811");
								s.convert(input,"SDP");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		private double [] getMinMaxAvg(List data)
		{
			double min,max,avg;
			double any;
			double[] minMaxAvgArray = new double[3];
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
				avg = avg + element; 
			}	
			
			minMaxAvgArray[0]=min;
			minMaxAvgArray[1]=max;
			minMaxAvgArray[2]=avg / data.size();
			return minMaxAvgArray;
		}
}
