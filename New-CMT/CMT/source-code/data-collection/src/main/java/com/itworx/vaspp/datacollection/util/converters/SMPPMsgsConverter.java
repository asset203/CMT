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

public class SMPPMsgsConverter extends AbstractTextConverter {

	private Logger logger;

	/**
	 * loop over input file, loop over lines in each file read log lines of
	 * level info and concate the values of recieptTime(Log Time) , SentTime ,
	 * AppID , DstNumber , SrcNumber into on comma separated string then write
	 * to output output files are placed on the configured converted file path
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
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		File[] outputFiles = new File[inputFiles.length];
		try {
			String path = PropertyReader.getConvertedFilesPath();
			
			String line=null;
			String[] lineParts=null;
			String msgInfoPart=null;
			String appID=null;
			String srcNumber;
			String dstNumber;
			int subIndex;
			String sendTime=null;
			int status;
			int msgPartIndex=5;
			String respCommandStatus=null;
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("SMPPConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				while(inputStream.ready()){
					respCommandStatus=" ";
					status=-1;
					line=inputStream.readLine();
					//each line is comma separated
					lineParts=line.split(",");
					if(lineParts==null||lineParts.length<6)
						continue;
					// we should handle only lines contains both the word INFO and SMPP
					if(lineParts[1]==null||!(lineParts[1].matches("[\\s]*INFO"))||!(lineParts[2].matches("[\\s]*SMPP")))
						continue;
					
					if(lineParts[4].indexOf("Success")!=-1)
						status=1;
					else if (lineParts[4].indexOf("Fail")!=-1)
						status=0;
					
					if(lineParts[5].indexOf("Resp-CommandStatus")!=-1){
						msgPartIndex=6;
						String[] temp=lineParts[5].split("=");
						if(temp.length==2)
							respCommandStatus=temp[1].replace(" ","");
					}else
						msgPartIndex=5;
					
					// Get the substring contains information about the sent message
					msgInfoPart=lineParts[msgPartIndex];
					// Get the substring cpntains information about the sent message
					msgInfoPart=lineParts[msgPartIndex];
					if(msgInfoPart==null)
						continue;
					// Get the begining index of value of appId which is (startIndex of word AppID + length of the string ("AppID= "))
					subIndex=msgInfoPart.indexOf("AppID")+7;
					appID=msgInfoPart.substring(subIndex, msgInfoPart.indexOf(" ",subIndex));
					
					// Get the begining index of value of SrcNumber which is (startIndex of word SrcNumber + length of the string ("SrcNumber= "))
					subIndex=msgInfoPart.indexOf("SrcNumber")+11;
					srcNumber=msgInfoPart.substring(subIndex, msgInfoPart.indexOf(" ",subIndex));
					
					// Get the begining index of value of DstNumber which is (startIndex of word DstNumber + length of the string ("DstNumber= "))
					subIndex=msgInfoPart.indexOf("DstNumber")+11;
					dstNumber=msgInfoPart.substring(subIndex, msgInfoPart.indexOf(" ",subIndex));
					
					// Get the value of send time 
					sendTime=msgInfoPart.substring(2,21);
					
					
					// write to the output stream in the following order 
					// Send Time , Recieve Time , AppID , Src Number , Dest Number , Status , RESP_CMD_STATUS
					outputStream.write(sendTime+","+lineParts[0].substring(0,19)+","+appID+","+srcNumber+","+dstNumber+","+status+","+respCommandStatus);
					outputStream.newLine();
				}
				outputStream.close();
				outputFiles[i]=output;
			}
			
		} catch (FileNotFoundException e) {
			logger.error("SMPPConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
			// e.printStackTrace();
		} catch (IOException e) {
			logger.error("SMPPConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
			// e.printStackTrace();
		}
		return outputFiles;
	}

	public static void main(String[] args) {
		try {
			PropertyReader
			.init("D:\\Projects\\VAS Portal Project\\Phase II\\Source Code\\DataCollection");
			SMPPMsgsConverter s=new SMPPMsgsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Projects\\VAS Portal Project\\Phase II\\Docs\\Sample Files\\smpp log\\SMPP.log.2007-03-04-14.log");
			s.convert(input,"SDP");
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
