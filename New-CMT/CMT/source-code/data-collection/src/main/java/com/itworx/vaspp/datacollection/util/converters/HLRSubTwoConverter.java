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
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class HLRSubTwoConverter  extends AbstractTextConverter {

	static private Logger logger;


	  private long allocatedSubscribers;
	  private long registeredSubscribers;
	  private long validSubscribers;

		public HLRSubTwoConverter() {
		}

		/**
		 * loop over input files, loop over lines in each file
		 * count incoming, check message events for each hour
		 * write output into one output file
		 * output file is placed on the configured converted file path
		 *
		 * @param inputFiles - array of the input files to be converted
		 * @param systemName - name of targeted system for logging
		 *
		 * @exception ApplicationException
		 *       if input file couldn't be found
		 *       if input file couldn't be opened
		 * @exception InputException
		 *       if ParseException occured
		 */
		public File[] convert(File[] inputFiles, String systemName)
				throws ApplicationException, InputException {
			logger = Logger.getLogger(systemName);
			logger
					.debug("HLRSubTwoConverter.convert() - started converting input files ");
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			try {
				for (int j = 0; j < inputFiles.length; j++) {
					logger.debug("HLRSubTwoConverter.convert() - converting file "
							+ inputFiles[j].getName());
					BufferedReader inputStream = new BufferedReader(new FileReader(
							inputFiles[j]));
					this.getSubscribers(inputStream,inputFiles[j].getName());      
					inputStream.close();
					logger.debug("HLRSubTwoConverter.convert() - "
							+ inputFiles[j].getName() + " converted");
				}
				File output = new File(path, "HLRSubTwoConverter.txt");
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(
						output));
			      checkCompleteInput();
			      //System.out.println( this.allocatedSubscribers+ "," + this.registeredSubscribers + ","	+ ( this.allocatedSubscribers - this.validSubscribers)+","+Utils.getYesterdaysDate() );
				outputStream.write( this.allocatedSubscribers+ "," + this.registeredSubscribers + ","
									+ ( this.allocatedSubscribers - this.validSubscribers)+","+Utils.getYesterdaysDate());
				outputStream.newLine();  
				outputStream.close();
				outputFiles[0] = output;
			} catch (FileNotFoundException e) {
				logger.error("HLRSubTwoConverter.convert() - Input file not found " + e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
			logger
					.debug("HLRSubTwoConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		}

	  /**
		 * get number of registered, allocated, notinvalid subscribers from file
	   * by refering to file name
		 *
		 * @param inputStream - input file
		 *
		 * @exception IOException
		 *       if input file couldn't be opened
		 * @exception InputException
		 *       if ParseException occured
		 */
		private void getSubscribers(BufferedReader inputStream,String fileName) throws IOException,
				InputException {
	  		try {
		        String lowerFileName = fileName.toLowerCase();
				if (lowerFileName.indexOf("reg") != -1) {
		          skip(8,inputStream);
		          String line = inputStream.readLine();
		          this.registeredSubscribers = this.getNumber(line,fileName);
				}
		        else if (lowerFileName.indexOf("alloc") != -1) {
		          skip(7,inputStream);
		          String line = inputStream.readLine();
		          this.allocatedSubscribers = this.getNumber(line,fileName);
				}
		        else if (lowerFileName.indexOf("invalid") != -1) {
		          skip(8,inputStream);
		          String line = inputStream.readLine();
		          this.validSubscribers = this.getNumber(line,fileName);
				}
			}
	      catch (NullPointerException e) {
					logger.error("HLRSubTwoConverter.getSubscribers() - Invalid input file " +e);
	        throw new InputException("Invalid input file " + e);
				}
	      catch (ArrayIndexOutOfBoundsException e) {
	        logger.error("HLRSubTwoConverter.getSubscribers() - Invalid record in input file " +e);
					throw new InputException("Invalid record in input file " + e);
				}
		}
	  
	  private void checkCompleteInput () throws InputException
	  {
	    if ( validSubscribers == 0 || registeredSubscribers == 0 || validSubscribers ==0 )
	    {
	      logger.error("HLRSubTwoConverter.getSubscribers() - Incomplete Input ");
				throw new InputException("Incomplete Input ");
	    }
	  }
	  private long getNumber(String line,String fileName) throws InputException
	  {
	    if (line.equals("")){
				logger.error("HLRSubTwoConverter.getSubscribers()- Invalid input file: "+fileName);
	      throw new InputException("Invalid input file: "+fileName);
	    }
	    String tokens[] = line.split(" ");
	    String num = tokens[0].substring(1);
	    try
	    {
	      long regNum = Integer.parseInt(num);
	      return regNum;
	    }
	    catch( NumberFormatException e)
	    {
	      logger.error("HLRSubTwoConverter.getSubscribers()- Invalid number in input file: "+fileName);
	      throw new InputException("Invalid number in input file: "+fileName);
	    }
	  }

	 	private void skip(int num, BufferedReader inputStream) throws IOException {
			for (int i = 0; i < num; i++) {
				inputStream.readLine();
	    }
		}
	  
		public static void main(String ag[]) {
			try {
				PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
				HLRSubTwoConverter s = new HLRSubTwoConverter();
				File[] input = new File[3];
				input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\capacity_alloc.txt");
				input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\capacity_invalid.txt");
				input[2]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\capacity_reg.txt");
				s.convert(input,"HLR");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
