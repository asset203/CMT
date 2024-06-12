/**
 * 
 */
package com.itworx.vaspp.datacollection.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author ahmad.abushady
 *
 */
public class DatabaseUtils {
	
	/*
	 * This Method is used to Check if the line received is a new line or not
	 * returns true if it is a new line, false otherwise
	 * it takes a string as a parameter
	 * it simply checks if there is a whole number in the line
	 * i.e. " 33 " or " 33,999 "
	 * the number should be preceded and ended by at least one space
	 */
	/**
	 * Checks if a line is new or not
	 * 
	 * @param line - the line to be checked
	 * @return boolean - whether or not it is new
	 */
	public static boolean CheckNewLine(String line){
		return line.matches(".*[\\s]([\\d](\\,[\\d])?)+[\\s].*");
	}
	
	
	/*
	 * This Method is used to merge two lines together in case a field is continued
	 * in the next line
	 * it takes the 2 lines as parameter
	 * and array containing information about the fields that need to be extracted
	 * then it merges the two line and return the fields separated with comma
	 */
	/**
	 * Merge two line into a single line, and extract data from it
	 * 
	 * @param lines - Arraylist containing a list of lines to extract data from
	 * @param arr - Arraylist containing information about the data that will be extracted
	 * @return String - the value of the extracted fields separated with comma
	 */
	public static String MergeLines(ArrayList lines, ArrayList arr){
		String result = "";
		if(arr.size() >= 1){
			FileFieldByte x = (FileFieldByte)arr.get(0);
			String SubResult="";
			for(int i=0; i < lines.size(); i++){
				SubResult = SubResult + ((String)lines.get(i)).substring(x.getPosition(), x.getPosition() + x.getBytes());
			}
			result = SubResult.trim();
			if(x.getisNum()){
				result = Utils.formatBigNumbers(result);
			}
		}
		
		for(int i=1; i < arr.size(); i++){
			FileFieldByte x = (FileFieldByte)arr.get(i);
			String SubResult = "";
			for(int j=0; j < lines.size(); j++){
				SubResult = SubResult + ((String)lines.get(j)).substring(x.getPosition(), x.getPosition() + x.getBytes());
			}
			if(!x.getisNum()){
				result = result + "," + SubResult.trim();
			}
			else result = result + "," + Utils.formatBigNumbers(SubResult.trim());
		}
		return result;
	}
	/*
	 * This Method is used to extract data from line that have fields with
	 * specific number of bytes
	 * it takes the line as a parameter, and an array containing info about data to
	 * extract (i.e. number of bytes of the field, and the starting position of the
	 * field in the line, note that the line start at position 0 and end at
	 * length - 1)
	 * it then returns the extracted data comma separated
	 * it removes any white space before or after the extracted data from the field
	 * i.e. "ABCDEFG     " will be extracted as "ABCDEFG"
	 * and " ABC DEF    " will be extracted as "ABC DEF" 
	 * the commented main function below contains an example of how to use
	 * the function
	 * NOTE: Please do not try to use this function to extract numbers that are
	 * comma separated because it will cause a problem parsing the result returned
	 * because the result is comma separated.
	 * Instead you can extract each number separtly
	 */
	/**
	 * Extracts fields from a line
	 * 
	 * @param line1 - the line that contains the data
	 * @param arr - arraylist containing the information about the fields that need to be extracted
	 * @return String - the extracted data separated with comma
	 */
	public static String ExtractLineData(String line1, ArrayList arr){
		String result = "";
		if(arr.size() >= 1){
			FileFieldByte x = (FileFieldByte) arr.get(0);
			if(!x.getisNum()){
				result = line1.substring(x.getPosition(), x.getPosition() + x.getBytes()).trim();
			}
			else{
				result = Utils.formatBigNumbers(line1.substring(x.getPosition(), x.getPosition() + x.getBytes()).trim());
			}
		}
		for(int i=1; i < arr.size(); i++){
			FileFieldByte x = (FileFieldByte) arr.get(i);
			if(!x.getisNum()){
				result = result + "," + line1.substring(x.getPosition(), x.getPosition() + x.getBytes()).trim();
			}
			else{
				result = result + "," + Utils.formatBigNumbers(line1.substring(x.getPosition(), x.getPosition() + x.getBytes()).trim());
			}
		}
		return result;
	}
	
	/**
	 * Skip sql lines.
	 * 
	 * @param inputStream - the input file
	 * @exception IOException
	 *            if error occured while reading file
	 */
	public static void skipSqlLines(BufferedReader inputStream) throws IOException {
		String line;
		do{
			line = inputStream.readLine();
		}while(!( line.startsWith("SQL>") && line.endsWith("/") ) &&  !line.matches("([\\s])*([\\d])+([\\s])*/") && inputStream.ready() );
	}
	
/*	
	public static void main(String ag[]) {
		ArrayList myArr1 = new ArrayList();
		myArr1.add("JAVA CLASSABCDE VFE               /105207cd_XmlJavaTypeAdapterDE    1,321        0        0                                                      ");
		myArr1.add("FGHIJKLMNOPQRST                   ABCD                                                                                                           ");
		myArr1.add("UVWX                                                                                                                                             ");
		
		ArrayList myArr = new ArrayList();
		//myArr to extract non number fields
		myArr.add(new FileFieldByte(15,0));
		myArr.add(new FileFieldByte(17,16));
		myArr.add(new FileFieldByte(30,34));
		myArr.add(new FileFieldByte(8,65,true));
		myArr.add(new FileFieldByte(8,74,true));
		myArr.add(new FileFieldByte(8,83,true));
		
		System.out.println(DatabaseUtils.MergeLines(myArr1, myArr));
		
	}
*/

}
