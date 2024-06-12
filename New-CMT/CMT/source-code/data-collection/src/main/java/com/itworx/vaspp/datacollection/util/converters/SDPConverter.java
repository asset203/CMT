/*
 * File:       SDPConverter.java
 * Date        Author          Changes
 * 24/01/2006  Nayera Mohamed  Created
 *
 * Converter class for converting SDP text input file into comma spearated format
 */

package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

/*
 * The structure of the text input file is
 * 
 * Line 1 : start date Line 2 : empty Line 3 : end date Line 4 : empty Line 5 :
 * comma separated data Repeated..........
 * 
 */
public class SDPConverter extends AbstractTextConverter {
	private Logger logger;

	public SDPConverter() { 
	}

	/**
	 * loop over input file, loop over lines in each file read start date, end
	 * date , data concatenate into on comma separated string then write to
	 * output output files are placed on the configured converted file path
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("SDPConverter.convert() - started converting input files ");
		try {
			String path =PropertyReader.getConvertedFilesPath();
			//String path ="D:\\Converted";
			File[] outputFiles = new File[inputFiles.length];
			boolean insideReportTag=false;
			boolean insideStatisticReportsTag=false;
			String line="";
			String serviceClass="";
			String startDate;
			String endDate;
			String data;
			String record ;
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("SDPConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
								
				insideReportTag=false;
				insideStatisticReportsTag=false;
				startDate="";
				endDate="";
				serviceClass="";
				data="";
				/*
				 * loop over lines in input file read start date, end date ,
				 * data concatenate into on comma separated string then write to
				 * output
				 */
				while (inputStream.ready()) {
					line=inputStream.readLine();
					if(line.startsWith("<StatisticReports>"))
						insideStatisticReportsTag=true;
					else if(insideStatisticReportsTag&&line.startsWith("<Report")){
						insideReportTag=true;
						startDate=subStringBetween(line, "Start=\"", "\" Stop");
						endDate=subStringBetween(line, "Stop=\"", "\">");
					} else if(line.startsWith("</Report>")){
						insideReportTag=false;
					} else if (insideReportTag) {
						// get the name of the service class
						serviceClass=subStringBetween(line,"<",">");
						if (!"-1".equals(serviceClass)&&isInteger(serviceClass)) {
							data = subStringBetween(line, "<" + serviceClass
									+ ">", "</" + serviceClass + ">");
							
							if(data.contains(","))
							{
								String colums[]=data.split(",");
								data=null;
								for(int d=0;d<colums.length;d++)
								{
									if(colums[d].equalsIgnoreCase(""))
									{
										data=data+","+"0";
									}
									else
									{
										data=data+","+colums[d];
									}
								} 
								data=data.split("null,")[1];
								
								
							
							}
							
							record = startDate + "," + endDate + "," + serviceClass+ "," + data;
							//System.out.println("record "+record.split(",").length);
							outputStream.write(record, 0, record.length());
							outputStream.newLine();
						
						}
					}else if(line.startsWith("</StatisticReports>"))
						insideStatisticReportsTag=false;
				}
				inputStream.close();
				outputStream.close();
				outputFiles[i] = output;
				logger.debug("SDPConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("SDPConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("SDPConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("SDPConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method to get substring betwwen two strings from an orginal string  
	 * @param original The orginal string conatins the start and end and in between substring 
	 * @param start The start string 
	 * @param end The end String
	 * @return The substring in between start and end strings 
	 */
	private String subStringBetween(String original,String start,String end) {
		int startIndex=original.indexOf(start);
		int endIndex=original.indexOf(end);
		CharSequence sub;
		if(startIndex==-1||endIndex==-1||(startIndex+start.length()>endIndex))
			return "";
		else {
			try {
				sub=original.subSequence(startIndex+start.length(), endIndex);
				return sub.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}
	
	
	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
/**
 * 
 * @author elsayed.hassan
 * SAX Handler class that should handle sax events for
 * the SDP input file
 */
class SDPSAXHandler extends DefaultHandler {

	private BufferedWriter outputStream;
	
	private StringBuffer textBuffer=null;

	private String startDate="";
	
	private String endDate="";
	
	private String serviceClass="";
	/*
	 * boolean variable indicates whether the parser 
	 * inside the tag <Report> or not in order to start
	 * collecting data
	 */
	private boolean readyToCollect=false;
	/**
	 * Helper method to flush the contents of the textBuffer
	 * to the output stream
	 *
	 */
	private void flushBuffer() {
		if(textBuffer!=null) {
			try {
				if(!"".equals(startDate)&&!"".equals(endDate)&&!"".equals(serviceClass)) {
					outputStream.write(startDate+","+endDate+","+textBuffer+","+serviceClass);
					outputStream.newLine();
				}
				serviceClass="";
			//	outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textBuffer=null;
		}
	}
	
	public BufferedWriter getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(BufferedWriter outputStream) {
		this.outputStream = outputStream;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length);
		if (textBuffer == null) {
		textBuffer = new StringBuffer(s);
		} else {
		textBuffer.append(s);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		String name = localName;
		if("".equals(name))name =qName;
		if("Report".equals(name))
			readyToCollect=false;
		else if(readyToCollect) {
			serviceClass=name;
			flushBuffer();
		}
	}

	public void error(SAXParseException e) throws SAXException {
		
	}

	public void fatalError(SAXParseException e) throws SAXException {
		super.fatalError(e);
	}

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String name = localName;
		if("".equals(name))name =qName;
		textBuffer=null;
		if("Report".equals(name)) {
			readyToCollect=true;
			if (attrs != null&&attrs.getLength()==2) {
				startDate=attrs.getValue(0);
				endDate=attrs.getValue(1);
			} else {
				startDate="";
				endDate="";
			}
		}
		
	}

	public void warning(SAXParseException arg0) throws SAXException {
	
	}
	
}

	// for testing
	public static void main(String ag[]) {
		try {
			PropertyReader
			.init("D:\\build\\pahse8\\logmanager\\DataCollection");
			SDPConverter s = new SDPConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\Config.cfg");
			s.convert(input,"SDP");
			System.out.println("finishde");
			
			//System.out.println("len "+any.split(",").length);
			/*org.w3c.dom.Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("D:\\Copy of Config.xml"));
			org.w3c.dom.Element elem=doc.getDocumentElement();
			System.out.println(elem.getNodeName());
			SDPConverter con=new SDPConverter();
			String  s= "<800>727,2,0,0,217,2,0,0,119,6,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0</800>";
			String sub=con.subStringBetween(s,"<", ">");
			System.out.println(s);
			System.out.println(sub);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}