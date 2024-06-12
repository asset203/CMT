/**
 * 
 */
package com.itworx.vaspp.datacollection.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author ahmad.abushady
 *
 */
public class BytesPropertyReader {

	private static Properties prop;
	
	private static Logger logger = DataCollectionManager.getLogger();
	
	static public final String SYSTEM_SEPARATOR = System
	.getProperty("file.separator");
    
    static private final String FILE_NAME = "resources" + SYSTEM_SEPARATOR
	+ "properties" + SYSTEM_SEPARATOR + "DatabaseFieldsProperties.properties";
	
	
	/*
	 * Function used to get a value of a certain property
	 * it takes the name of the property and returns its value
	 * it also checks if the properties was not loaded before to load it.
	 */
	public static String GetPropertyValue(String name){
		if(prop == null)
			load();
		return prop.getProperty(name);
	}
	
	/*
	 * function responsible to load properties file
	 */
	private static void load(){
		prop = new Properties();
		try{
			prop.load(new FileInputStream(FILE_NAME));
		}catch (Exception e){
			logger.debug("BytesPropertyReader.load() - Unable To Open properties file: FieldsProperties.properties");
		}
	}

}
