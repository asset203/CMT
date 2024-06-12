/**
 * 
 */
package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

/**
 * @author ahmad.abushady
 *
 */
public class SDPSnapeshotConverter extends AbstractTextConverter {

	private class SDPRecord {
		private String MISDNRange;
		private String ServiceClass;
		private int BalanceRange;
		
		private long Cvalidity = 0;
		private long Cgrace = 0;
		private long Cafter = 0;
		private long Call = 0;
		
		public SDPRecord(String MRange, String SClass, int BRange){
			MISDNRange = MRange;
			ServiceClass = SClass;
			BalanceRange = BRange;
		}
		
		public void incCval(){
			Cvalidity++;
			Call++;
		}
		
		public void incCgrace(){
			Cgrace++;
			Call++;
		}
		
		public void incCafter(){
			Cafter++;
			Call++;
		}
		
		public void incCAll(){
			Call++;
		}

		public void setMISDNRange(String range) {
			MISDNRange = range;
		}
		
		public String getString(){
			return MISDNRange + "," + ServiceClass + "," +
			BalanceRange + "," + Cvalidity + "," + Cgrace + "," + 
			Cafter + "," + Call;
		}

		public String getMISDNRange() {
			return MISDNRange;
		}
		
	}
	
	private Logger logger;

	
	
	/**
	 * 
	 */
	public SDPSnapeshotConverter() {
	}

	/* (non-Javadoc)
	 * @see com.itworx.vaspp.datacollection.util.converters.TextConverter#convert(java.io.File[], java.lang.String)
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		// TODO Auto-generated method stub
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("SDPSnapeshotConverter - Starting Converting SDP Snapeshot");
		
		String path = PropertyReader.getConvertedFilesPath();
                //String path = "D:\\Work\\CMT New Envir\\converted";
		File[] outputFiles = new File[inputFiles.length];
		
		try{
			
			
			
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "SDPSnapeshotConverter.convert() - converting file "
				+ inputFiles[i].getName() );
				
				File output = new File(path, inputFiles[i].getName());
				//BufferedReader inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				BufferedReader inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				
				Hashtable MISDNRange = new Hashtable();
				Hashtable Records = new Hashtable();
				
				String line = inputStream.readLine();
				
				while(line != null){
					/*if(line.length() > 0 &&
							//added to prevent parsing lines that have characters in it.
							!line.matches(".*[a-zA-Z].*"))*/
                                            if(line.length() > 0)
						ReadLine(inputStream, line, MISDNRange, Records);
					line = inputStream.readLine();
				}
				line = inputStream.readLine();
				Enumeration col = Records.elements();
				
				String CurrentRange = "";
				String CacheRange = "";
				
				String theDate = Utils.getYesterdaysDate();
				
				while(col.hasMoreElements()){
					SDPRecord myRec = (SDPRecord) col.nextElement();
					String tempRange = myRec.getMISDNRange();
					if(CacheRange.compareTo(tempRange) == 0){
						myRec.setMISDNRange(CurrentRange);
						//System.out.println(myRec.getString() + "," + theDate);
						outputStream.write(myRec.getString() + "," + theDate);
					}
					else{
						CacheRange = tempRange;
						CurrentRange = (String) MISDNRange.get(CacheRange+"S");
						CurrentRange = CurrentRange + "," + (String) MISDNRange.get(CacheRange + "E");
						myRec.setMISDNRange(CurrentRange);
						//System.out.println(myRec.getString() + "," + theDate);
						outputStream.write(myRec.getString() + "," + theDate);
					}
					outputStream.newLine();
				}
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "SDPSnapeshotConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "SDPSnapeshotConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("SDPSnapeshotConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("SDPSnapeshotConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
	}
	
	private void ReadLine(BufferedReader inputStream, String line,
			Hashtable MISDNRange, Hashtable Records) throws IOException{
		String [] columns = line.split(",");
		if(columns.length > 4){
			String MRange = columns[0].substring(1, 4);
			int BRange = columns[1].startsWith("0")? 0 : 1;
			BRange = columns[1].startsWith("-") ? 0 : BRange;
			String Sclass = columns[2];
			int indicator = 3;
			
			String key = MRange + Sclass + BRange;
			
			//check whether in validity,grace,after grace, other
			//0,0 = Validity
			//0,1 = Grace
			//1,1 = After Grace
			//1,0 is undefined

			String ind = columns[3] + columns[4];
			if(ind.compareTo("00") == 0)
				indicator = 0;
			else if(ind.compareTo("01") == 0)
				indicator = 1;
			else if(ind.compareTo("11") == 0)
				indicator = 2;
			
			//set the global MISDN Start and end ranges
			if(MISDNRange.get(MRange + "S") == null){
				if(Integer.parseInt(columns[0].substring(4, 5)) > 4){
					MISDNRange.put(MRange + "S", MRange + "5");
					MISDNRange.put(MRange + "E", MRange + "9");
				}
				else{
					MISDNRange.put(MRange + "S", MRange + "0");
					MISDNRange.put(MRange + "E", MRange + "4");
				}
			}
			else{
				if(Integer.parseInt(columns[0].substring(4, 5)) > 
				Integer.parseInt(((String)MISDNRange.get( MRange + "E")).substring(3))){
					MISDNRange.remove(MRange + "E");
					MISDNRange.put(MRange + "E", MRange + "9");
				}
			}
			
			//create/modify records
			SDPRecord rec = (SDPRecord) Records.get(key);
			if(rec == null){
				SDPRecord a = new SDPRecord(MRange,Sclass,BRange);
				if(indicator == 0)
					a.incCval();
				else if(indicator == 1)
					a.incCgrace();
				else if(indicator == 2)
					a.incCafter();
				else a.incCAll();
				
				Records.put(key, a);
			}
			else{
				if(indicator == 0)
					rec.incCval();
				else if(indicator == 1)
					rec.incCgrace();
				else if(indicator == 2)
					rec.incCafter();
				else rec.incCAll();
			}
		}
		else{
			logger.debug( "SDPSnapeshotConverter.convert() - Record Does not contain enough information. + " 
					+ line);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*SDPSnapeshotConverter s = new SDPSnapeshotConverter();
		s.path = "c:\\converted";
		File[] files = new File[1];
		files[0] = new File("c:\\BALANCE.TXT");
		s.convert(files,"");*/
		
		try {
			
			PropertyReader.init("D:\\VASPortalWF5\\Source Code\\DataCollection");
			SDPSnapeshotConverter s = new SDPSnapeshotConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Work\\CMT New Envir\\BALANCES58_20171225.TXT");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
