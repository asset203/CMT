package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.NodeColumnType;
import org.apache.velocity.app.Velocity;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.Types;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static eg.com.vodafone.utils.Utils.*;
public class Utils {
	
	/**
	 * The default format for our database systems.
	 */
	public static String defaultFormat = "MM/dd/yyyy HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT ="MM/dd/yyyy";
    public final static String DATE_PLACE_HOLDER = "$date";
	
	
	/**
	 * Convert date from String to Date with the required format.
	 * 
	 * @param dateString - the date string to be converted
	 * @param //pattern - the date format
	 * @return Date - the converted date
	 * @exception InputException
	 *                if ParseException occured
	 */
	public static Date convertToDate(String dateString, String format) throws InputException {
		if(format == null){
			format = defaultFormat;
		}		
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern(format);
		try {
            return frm.parse(dateString);
		} catch (ParseException e) {
			throw new InputException("Invalid date in input file " + e);
		}
	}
	
	/**
	 * Convert date from Date to String with the required format.
	 * 
	 * @param date - the date to be converted
	 * @param ///pattern - the date format
	 * @return String - the converted date String
	 */
	public static String convertToDateString(Date date, String format){
		if(format == null){
			format = defaultFormat;
		}
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern(format);
        return frm.format(date);

	}
	
	public static String reFormatDateString(String dateString,String currentFormat,String newFormat) throws ParseException{
		Date date;
		try {
			date = Utils.convertToDate(dateString, currentFormat);
		} catch (InputException e) {
			throw new ParseException(dateString + e,0);
		}
		return Utils.convertToDateString(date, newFormat);
	}
	
	/**
	 * Format the big number from 123,456,789 to 123456789
	 * 
	 * @param number - the big number in format 123,456,789
	 * @return String - the formatted number in format 123456789
	 */
	public static String formatBigNumbers(String number){
        return number.replaceAll(",", "");
	}
	
	/**
	 * Format the big number from 123,456,789 to 123456789
	 * 
	 * @param //number - the big number in format 123,456,789
	 * @return String - the formatted number in format 123456789
	 */
	public static String formatDatabaseStringValues(String value){
        return value.replaceAll("'", "''");
	}
	
	/**
	 * Skip the required number of lines.
	 * 
	 * @param num - the number of lines to skip
	 * @param inputStream - the input file
	 * @exception IOException
	 *            if error occured while reading file
	 */
	public static void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
		}
	}
	
	/**
	 * Generate the yesterday Date.
	 * 
	 * @param //inputStream - the input file
	 * @return String - the yesterday date
	 * @exception ParseException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	public static String getYesterdaysDate() 
	{	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    GregorianCalendar gc = new GregorianCalendar();
	    java.sql.Date d 	=  	new java.sql.Date(System.currentTimeMillis());
	    gc.setTime(d);
	    int dayBefore = gc.get(Calendar.DAY_OF_YEAR);
	    gc.roll(Calendar.DAY_OF_YEAR, -1);
	    int dayAfter = gc.get(Calendar.DAY_OF_YEAR);
	    if(dayAfter > dayBefore) {
	        gc.roll(Calendar.YEAR, -1);
	    }
	    gc.get(Calendar.DATE);
	    java.util.Date yesterday = gc.getTime();
	    String currentDate = sdf.format(yesterday);
	    
	    return currentDate;

	}
	
	/**
	 * get the index of an object in array.
	 * 
	 * @param obj - the object to be get its index
	 * @param arr - array of objects to search in it
	 * @return int - index of object , otherwise -1
	 *  
	 */
	
	public static int indexOf(Object obj,Object[] arr)
	{
		int bExists = -1;
		for(int i = 0;i<arr.length;i++)
		{
			if(obj.equals(arr[i]))
			{
				bExists = i;
				break;
			}
		}
		return bExists;
	}
	
	/**
	 * get the index of an object in array.
	 * 
	 * @param str - the object to be get its index
	 * @param arr - array of objects to search in it
	 * @return int - index of object , otherwise -1
	 *  
	 */
	
	public static int ignoreCaseIndexOf(String str,String[] arr)
	{
		int bExists = -1;
		for(int i = 0;i<arr.length;i++)
		{
			if(str.equalsIgnoreCase(arr[i])){
				bExists = i;
				break;
			}
		}
		return bExists;
	}
	
	/**
	 * get the string between 2 strings.
	 * 
	 * @param mainStr - main String
	 * @param startStr - first string
	 * @return endStr - last index
	 *  
	 */
	public static String stringBetween(String mainStr,String startStr,String endStr)
	{
		String str = null;
		int startIdx = mainStr.indexOf(startStr)+startStr.length();
		if(startIdx==-1){
			return null;
        }
		String temp=mainStr.substring(startIdx);
		if(temp==null){
			return null;
        }
		int endIdx = temp.indexOf(endStr);
		if(endIdx != -1) {
				str = temp.substring(0,endIdx).trim();
        }
		return str;		
	}
	
	/**
	 * get the string from 1 string to end.
	 * 
	 * @param mainStr - main String
	 * @param startStr - first string
	 *  
	 */
	public static String stringFrom(String mainStr,String startStr)
	{
		String str = null;
		int startIdx = mainStr.indexOf(startStr)+startStr.length();
		if(startIdx==-1){
			return null;
        }
		String temp=mainStr.substring(startIdx);
		if(temp==null){
			return null;
        }
		str = temp.substring(0).trim();
		return str;		
	}

	/**
	 * get the index of an matched string in array of regular expressions.
	 * 
	 * @param str - the string to be get its index
	 * @param regExpStrArr - array of regular expression strings to search in it
	 * @return int - index of object , otherwise -1
	 */
	public static int indexOfMatched(String str, String[] regExpStrArr) {
		int matchedIdx = -1;
		for(int i = 0;i<regExpStrArr.length;i++){
			if(str.matches(regExpStrArr[i])){
				matchedIdx = i;
				break;
			}
		}
		
		return matchedIdx;
	}
	
	/**
	 * resolve the date variable in the file name
	 * 
	 * @param name -
	 *            the input name containing the date variable
	 * @param targetDate -
	 *            the date to replace the variable
	 * 
	 * @return String - the resolved name
	 */
	public static String resolveName(String name, Date targetDate) {
		//logger.debug("TextInputDAO.resolveName() - started resolveName (" + name + " , " + targetDate + ")");
		int dateBeginIndex = name.indexOf("$date");
		if (dateBeginIndex != -1) {
			int dateEndIndex = name.indexOf(')', dateBeginIndex);
			String format = name.substring(
					name.indexOf('(', dateBeginIndex) + 1, dateEndIndex);
			SimpleDateFormat f = new SimpleDateFormat();
			format = format.replaceAll("m", "M");
			f.applyPattern(format);
			StringBuffer s = f.format(targetDate, new StringBuffer(),
					new FieldPosition(SimpleDateFormat.DATE_FIELD));
			String dateString = s.toString();
			String newName = name.substring(0, dateBeginIndex) + dateString
					+ name.substring(dateEndIndex + 2, name.length());
			//logger.debug("TextInputDAO.resolveName() - resolved " + name + "to"	+ newName);
			return newName;
		}
		//logger.debug("TextInputDAO.resolveName() - finished resolveName (" + name + " , " + targetDate + ")");
		return name;
	}
    public static String getDateFormatFromInputFileName(String fileName)  {
        int dateBeginIndex = fileName.indexOf(DATE_PLACE_HOLDER);
        if (dateBeginIndex != -1) {
            int dateEndIndex = fileName.indexOf(')', dateBeginIndex);
             String formatFromFileName = fileName.substring(
                    fileName.indexOf('(', dateBeginIndex) + 1, dateEndIndex);
            //known issue: old systems used 'm' instated of 'M' for month
            // 'm' is for minutes, so we must replace it by 'M' to be able to parse date correctly.

            return formatFromFileName.replace('m','M');
        }
        return null;
    }
	
	/**
	 * resolve sql types to the xml predefined types for DataColumn
	 * 
	 * @param type -
	 *            the sql type of the field in ResultSet
	 * @return String - the resolved type
	 */
	public static String resolveSQLType(int type) {
		if (type == Types.CHAR || type == Types.LONGVARCHAR
				|| type == Types.VARCHAR) {
			return "string";
		} else if (type == Types.DATE || type == Types.TIMESTAMP
				|| type == Types.TIME) {
			return "date";
		} else if (type == Types.INTEGER || type == Types.TINYINT
				|| type == Types.BIGINT || type == Types.SMALLINT) {
			return "number";
		} else if (type == Types.DECIMAL || type == Types.FLOAT
				|| type == Types.NUMERIC || type == Types.DOUBLE) {
			return "float";
		} else {
			return "";
		}
	}
	
	public static long getTimeDifferenceinMills(Date date1,Date date2){
		Calendar cal = Calendar.getInstance(); 
		Calendar cal2=Calendar.getInstance(); 
		cal.setTime(date1);
		cal2.setTime(date2);
        return cal.getTimeInMillis()-cal2.getTimeInMillis();
	}
	
	public static void initVelocity() throws ApplicationException{
		Properties p = new Properties();
		p.setProperty("file.resource.loader.path",PropertyReader.getVelocityTemplatesPath());
		try {
			Velocity.init(p);
		} catch (Exception e) {
			throw new ApplicationException(""+e);
		}
	}


	public static java.sql.Date sqlDate(Date date) {
		return (date == null)?null:new java.sql.Date(date.getTime());
	}
	
	public static java.sql.Timestamp sqlTimestamp(Date date) {
		return (date == null)?null:new java.sql.Timestamp(date.getTime());
	}
	
	public static String[] split(String str, String regex){
		return (str == null)?null:str.split(regex);
	}
	
	public static <T> boolean exists(T obj, T[] collection) {
		boolean found = false;
		if (obj == null || collection == null) {
			return false;
		}
		for (T tmp : collection) {
			if (obj.equals(tmp)) {
				found = true;
				break;
			}
		}
		return found;
	}

	 public static String toString(Map<?,?> m) {
		 if(m== null){ return "["+m+"]";}
           StringBuilder sb = new StringBuilder("[");
	        String sep = "";
	        for (Object object : m.keySet()) {
	            sb.append(sep)
	              .append(object.toString())
	              .append(":")
	              .append(m.get(object).toString());
	            sep = ",";
	        }
	        return sb.append("]").toString();
	}

	public static boolean isEmpty(String str) {
		return (str == null || "".equals(trim(str)));
	}

	public static String trim(String str) {
		return(str == null)?null:str.trim();
	}

	public static boolean isEmptyValues(Map values) {
		if(values == null) {return true;}
		for(Object obj: values.values()){
			if(obj != null){
				return false;
			}
		}
		return true;
	}
	
	public static String trimWithLen(String text,int len) {
		if(len == -1) {return text;}
		if(text == null) {return null;}
		text = text.trim();
		if(text.length() <= len){
			return text;
        }else{
			return text.substring(0, len);
        }
	}
	
	public static String getInsertQuery(String mappedTable, DataColumn[] columns,
                                        String nodeColumn, String idColumn, String seqName, String nodeName, String indicator, String autoFilledDateColumn) {
		StringBuffer finalSql = new StringBuffer("INSERT INTO ");
		StringBuffer fields = new StringBuffer();
		StringBuffer valuesParams = new StringBuffer(); 
		if(!Utils.isEmpty(idColumn)){
			fields.append(idColumn);
			fields.append(",");
			seqName = (Utils.isEmpty(seqName))?("SEQ_"+mappedTable):seqName;
			valuesParams.append(seqName);
			valuesParams.append(".nextval");
			valuesParams.append(",");
		}
		for(DataColumn column: columns){
			if(isEmpty(column.getName())){
				fields.append(column.getSrcColumn());
			} else {
				fields.append(column.getName());
			}
			fields.append(" ,");
			valuesParams.append(indicator);
			if(!"?".equals(indicator)){
				valuesParams.append(column.getName());
			}
			valuesParams.append(" ,");
		}
		if(!Utils.isEmpty(nodeColumn)){
			fields.append(nodeColumn).append(" ,");
			valuesParams.append("'");
			valuesParams.append(nodeName);
			valuesParams.append("'").append(" ,");
		}
        if(!Utils.isEmpty(autoFilledDateColumn)) {
            fields.append(autoFilledDateColumn);
            valuesParams.append(indicator) ;
            if(!"?".equals(indicator)){
                valuesParams.append(autoFilledDateColumn);
            }
        }
		if(fields.length() > 0 && fields.lastIndexOf(",") == (fields.length()-1)){
			fields.deleteCharAt(fields.length()-1);

		}
        if(valuesParams.length()>0&& valuesParams.lastIndexOf(",") == (valuesParams.length()-1)){
              valuesParams.deleteCharAt(valuesParams.length()-1);
        }

		finalSql.append(mappedTable);
		finalSql.append("(");
		finalSql.append(fields);
		finalSql.append(" ) VALUES (");
		finalSql.append(valuesParams);
		finalSql.append(")");
		return  finalSql.toString();
	}
    private static String resolveDateInQuery(String query, Date targetDate,String dateFormatStr){
        if(!StringUtils.hasText(query)  || targetDate == null){
            return  query;
        }
        String javaFmt ="";
        if(!StringUtils.hasText(dateFormatStr)){
            javaFmt = DEFAULT_DATE_FORMAT;
        }
        else
        {
            if(dateFormatStr.contains("H")|| dateFormatStr.contains("h")
                   ||dateFormatStr.contains("K")|| dateFormatStr.contains("k")){
                javaFmt = "dd/MM/yyyy HH";
            }
            else
            {    javaFmt="dd/MM/yyyy";

            }
        }
        SimpleDateFormat simpleDateFormat    = new SimpleDateFormat(javaFmt);
        String formattedDate = simpleDateFormat.format(targetDate);
        query =  query.replace(TARGET_DATE_STRING_PLACEHOLDER,formattedDate);
        return  query;
    }

    private static List<DataColumn> orderColumnsByIndex(List<DataColumn> columns){
            if(columns==null ||columns.isEmpty()){
                return columns;
            }
        int maxIndex = 0;
        Map<Integer,DataColumn> columnIndexMap = new HashMap<Integer, DataColumn>();
        List<DataColumn>orderedColumns = new ArrayList<DataColumn>();
        for(DataColumn aColumn:columns){
            if(aColumn.getIndex() > maxIndex){
                maxIndex=aColumn.getIndex();
            }
            columnIndexMap.put(aColumn.getIndex(),aColumn);/*assuming no two columns have same index*/
        }

        for(int i=0;i<=maxIndex;i++){
            if(columnIndexMap.containsKey(i)){
                orderedColumns.add(columnIndexMap.get(i)) ;
            }
        }

        return orderedColumns;
    }
    private static void appendAutoFilledColumn(StringBuffer insertFieldsPart,StringBuffer selectPart,
                                               String columnNameToAppend,String columnValueToAppend){
        insertFieldsPart.append(columnNameToAppend).append(",");
        StringBuffer temp = new StringBuffer(",").append(columnValueToAppend).append(" AS ").append(columnNameToAppend).append(' ');
        int fromIndex =selectPart.toString().toUpperCase().indexOf(FROM_KEY_WORD) ;
        selectPart.replace(fromIndex-1,fromIndex,temp.toString());

    }
    public static String getInsertSelectQuery(VInputStructure inputStructure, InputData inputData) {
        StringBuffer finalSql = new StringBuffer("INSERT INTO ");
        StringBuffer insertFields = new StringBuffer();
        StringBuffer selectPart = new StringBuffer(
                resolveDateInQuery(inputStructure.getExtractionSql(),inputData.getDate(),inputStructure.getDateFormat()));
        List<DataColumn> orderedColumns = orderColumnsByIndex(inputStructure.getColumnsList());
        for(DataColumn column: orderedColumns){
            if(column.isActive()){
                insertFields.append(StringUtils.hasText(column.getName())?  column.getName():column.getSrcColumn());
                insertFields.append(',');
            }
        }

        if(inputStructure.getNodeColumnType() == NodeColumnType.CONFIGURABLE.getTypeCode()
                && StringUtils.hasText(inputData.getNodeName())){
            insertFields.append(NODE_NAME_COLUMN).append(",");
            StringBuffer nodePart = new StringBuffer(",\'").append(inputData.getNodeName()).append('\'').append(" AS ").append(NODE_NAME_COLUMN).append(' ');
            int fromIndex =selectPart.toString().toUpperCase().indexOf(FROM_KEY_WORD) ;
            selectPart.replace(fromIndex-1,fromIndex,nodePart.toString());
        }
        if(StringUtils.hasText(inputStructure.getAutoFilledDateColumn())){
            String format = inputData.getDateFormatInFileNamePattern();
            if(format == null|| "".equals(format)){
                format = DEFAULT_DATE_FORMAT;
            }
             StringBuffer value = new StringBuffer("to_date(\'")
                     .append(convertToDateString(inputData.getDate(),format)).append("\',\'")
                     .append(getEquivalentOracleFormat(format)).append("\')");

            appendAutoFilledColumn(insertFields, selectPart, inputStructure.getAutoFilledDateColumn(), value.toString());
        }

       if(insertFields.length() > 0 && insertFields.lastIndexOf(",")== (insertFields.length()-1) ){
                insertFields.deleteCharAt(insertFields.length()-1);
       }

        finalSql.append(inputStructure.getMappedTable());
        finalSql.append('(');
        finalSql.append(insertFields);
        finalSql.append(')');
        finalSql.append(selectPart);

        return  finalSql.toString();
    }
	
	public static Map<String, String> getQueryStringMap(String url) {
		if(isEmpty(url)){
			return null;
        }
	    String[] params = url.split("&");  
	    Map<String, String> map = new HashMap<String, String>();
	    String[] paramSplitted = null;
	    for (String param : params) {
	    	if(param.contains("=") 
	    			&& (paramSplitted = param.split("=")).length > 1){
	    		map.put(paramSplitted[0], paramSplitted[1]);
	    	}
	    }  
	    return map;  
	}  
	
	/**
    * Join all the elements of a string array into a single
    * String.
    * <p>
    * If the given array empty an empty string
    * will be returned.  Null elements of the array are allowed
    * and will be treated like empty Strings.
    *
    * @param array Array to be joined into a string.
    * @param delimiter String to place between array elements.
    * @return Concatenation of all the elements of the given array with the the delimiter in between.
    * @throws NullPointerException if array or delimiter is null.
    *
    * @since ostermillerutils 1.05.00
    */
   public static String join(String[] array, String delimiter){
       // Cache the length of the delimiter
       // has the side effect of throwing a NullPointerException if
       // the delimiter is null.
       int delimiterLength = delimiter.length();

       // Nothing in the array return empty string
       // has the side effect of throwing a NullPointerException if
       // the array is null.
       if (array.length == 0){ return "";}

       // Only one thing in the array, return it.
       if (array.length == 1){
           if (array[0] == null){ return "";}
           return array[0];
       }

       // Make a pass through and determine the size
       // of the resulting string.
       int length = 0;
       for (int i=0; i<array.length; i++){
           if (array[i] != null){ length+=array[i].length();}
           if (i<array.length-1) {length+=delimiterLength;    }
       }

       // Make a second pass through and concatenate everything
       // into a string buffer.
       StringBuffer result = new StringBuffer(length);
       for (int i=0; i<array.length; i++){
           if (array[i] != null) {result.append(array[i]); }
           if (i<array.length-1) {result.append(delimiter);}
       }

       return result.toString();
   }
   
   public static BufferedReader getGZIPAwareBufferdReader(File inputFile) throws IOException{
		String fileName = inputFile.getName();
		//if("gz".equals(fileName.substring(fileName.lastIndexOf('.')+1))){
       if(fileName.endsWith(".gz")){
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))));
		} else {
			return new BufferedReader(new FileReader(inputFile));
		}
	   
   }

    public static int getMaxIndex(DataColumn[] columns){
        int maxLen = 0;
        for(DataColumn column: columns){
            if(column.getIndex()>maxLen){
                maxLen = column.getIndex();
            }
        }
        return maxLen;
    }

    private static String getEquivalentOracleFormat(String javaDateFormat){
        String oracleFormat = javaDateFormat;
        oracleFormat = oracleFormat.replace('h','H');
        oracleFormat = oracleFormat.replace("HH","HH24");
        return oracleFormat;
    }
   public static void main(String[] args) {
		/*String date="Thu Feb  2 14:26:21 EET 2012";
		try {
			System.out.println(Utils.reFormatDateString(date, "EEE MMM  d hh:mm:ss z yyyy", "MM/dd/yyyy HH:00:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */

	}
}
