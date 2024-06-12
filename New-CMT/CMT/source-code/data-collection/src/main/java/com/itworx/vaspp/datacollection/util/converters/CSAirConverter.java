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

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

/*
 * The structure of the text input file is
 * 
 * Line 1 : start date Line 2 : empty Line 3 : end date Line 4 : empty Line 5 :
 * comma separated data Repeated..........
 * 
 */
public class CSAirConverter extends AbstractTextConverter {
	private Logger logger;

	public CSAirConverter() {
	}

	/**
	 * loop over input file, loop over lines 
	 * data concatenate into on comma separated string then write to
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
				.debug("CSAirConverter.convert() - started converting input files ");
		try {
			String path =PropertyReader.getConvertedFilesPath();
			//String path ="D:\\Converted";
			File[] outputFiles = new File[inputFiles.length];
			String line;
			for (int i = 0; i < inputFiles.length; i++) {
				File output = new File(path, inputFiles[i].getName());
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				//Ignore the first line
				if(inputStream.ready()) 
					inputStream.readLine();
				while (inputStream.ready()) {
					line=inputStream.readLine();
					outputStream.write(line);
					outputStream.newLine();
				}
				inputStream.close();
				outputStream.close();
				outputFiles[i] = output;
				logger.debug("CSAirConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("CSAirConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("CSAirConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("CSAirConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
			// e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	// for testing
	public static void main(String ag[]) {
		try {
			PropertyReader
			.init("D:\\Projects\\VAS Portal Project\\Phase II\\Source Code\\DataCollection");
			CSAirConverter s = new CSAirConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Config.xml");
			s.convert(input,"SDP");
			/*org.w3c.dom.Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("D:\\Copy of Config.xml"));
			org.w3c.dom.Element elem=doc.getDocumentElement();
			System.out.println(elem.getNodeName());
			CSAirConverter con=new CSAirConverter();
			String  s= "<800>727,2,0,0,217,2,0,0,119,6,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0</800>";
			String sub=con.subStringBetween(s,"<", ">");
			System.out.println(s);
			System.out.println(sub);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}