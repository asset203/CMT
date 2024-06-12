package com.itworx.vaspp.datacollection.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SMSLGPFileSplitter {
	
	private static void parseFile(File inputFile){
		
	} 
	
	
	public static void main(String[] args){
		String filePath = args[0];
		File file = new File(filePath);

		BufferedReader inputStream = null;
		BufferedWriter outputStream1 = null;
		BufferedWriter outputStream2 = null;
		BufferedWriter outputStream3 = null;
		BufferedWriter outputStream4 = null;

		try {
			inputStream =  new BufferedReader(new FileReader(file));
			outputStream1 = new BufferedWriter(new FileWriter(filePath+"_00_05"));
			outputStream2 = new BufferedWriter(new FileWriter(filePath+"_06_11"));
			outputStream3 = new BufferedWriter(new FileWriter(filePath+"_12_17"));
			outputStream4 = new BufferedWriter(new FileWriter(filePath+"_18_23"));

			String line;
			while ((line = inputStream.readLine()) != null) {
				String[] lineContent = line.split(",");
				if(lineContent.length < 1){
					continue;
				}
				int hour = getHour(lineContent[0]);
				if (hour == -1){
					continue;
				}
				if(hour <= 5){
					outputStream1.write(line+"\n");
				} else if(hour <= 11){
					outputStream2.write(line+"\n");
				} else if(hour <= 17){
					outputStream3.write(line+"\n");
				} else {
					outputStream4.write(line+"\n");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println(" error happned while closing main file");
					e.printStackTrace();
				}
			}
			
			if(outputStream1 != null){
				try {
					outputStream1.close();
				} catch (IOException e) {
					System.err.println(" error happned while closing file 1");
					e.printStackTrace();
				}
			}
			
			if(outputStream2 != null){
				try {
					outputStream2.close();
				} catch (IOException e) {
					System.err.println(" error happned while closing file 2");
					e.printStackTrace();
				}
			}
			
			if(outputStream3 != null){
				try {
					outputStream3.close();
				} catch (IOException e) {
					System.err.println(" error happned while closing file 3");
					e.printStackTrace();
				}
			}
			
			if(outputStream4 != null){
				try {
					outputStream4.close();
				} catch (IOException e) {
					System.err.println(" error happned while closing file 4");
					e.printStackTrace();
				}
			}
		}
	}

	private static int getHour(String dateStr) {
		int hour = -1;
		try {
			String[] dateContent = dateStr.split(" ");
			hour = Integer.parseInt(dateContent[1].split(":")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hour;
	}

}
