package com.itworx.vaspp.datacollection.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

public class FtpLogHandler {
	
	private Logger logger;
	
	/**
	 * The default count.
	 */
	public static int defaultCount = 0;

	public FtpLogHandler() {
	}
	
	/**
	 * Read the specified log file.
	 * 
	 * @param systemName - the system name which is part of file name
	 * @param count - the count of the file if it is archived
	 * @return File - the specified log file
	 */
	public File readFile(String systemName, int count){
		
		String fileName = null;
		logger = Logger.getLogger("LogManager");
		// Prepare the file name
		if(count == defaultCount){
			fileName = systemName+".log";
		}else{
			fileName = systemName+".log."+count;
		}
		
		// Read the source location
		String targetLocation = PropertyReader.getLogFilesPath();
		// Read the file
		File localFile = new File(targetLocation, fileName);
		logger.debug("FtpLogHandler.readFile() - read successfully the file: "
			+ fileName + " from location: " + targetLocation);
		
		return localFile;
	}

	public static void main(String arg[]) {
		FtpLogHandler f = new FtpLogHandler();

		try {
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			File output = f.readFile("bgw", defaultCount);
			//System.out.println("Name:"+output.getName()+", Path:"+output.getAbsolutePath());
			BufferedReader inputStream = new BufferedReader(new FileReader(output));
			/*if(inputStream.ready()){
				System.out.println(inputStream.readLine());
			}else{
				System.out.println("no data");
			}*/
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


}
