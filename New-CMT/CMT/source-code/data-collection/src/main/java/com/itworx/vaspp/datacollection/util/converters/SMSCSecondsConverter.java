/*
 * File: SMSCSecondsConverter.java
 * Date        Author          Changes
 * 18/05/2006  Nayera Mohamed  Created
 *
 * Creating comma separated input file for SMSC top 20 seconds input
 */
package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

public class SMSCSecondsConverter extends AbstractTextConverter {
	private Logger logger;
  private Calendar c = Calendar.getInstance();
  private long hourCount;
  private long maxHourCount;
  private Date maxHour;
	private Date day;
  private EventsComparator comparator = new EventsComparator();
  private Vector counts = new Vector();

	public SMSCSecondsConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		try {
			for (int j = 0; j < inputFiles.length; j++) {
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[j]));
				this.skip(6,inputStream);
				SimpleDateFormat frm = new SimpleDateFormat();
				frm.applyPattern("yyMMddHHmmss");
        Date date;
        int count = 0;
        Date lastDate=null;    
				while (inputStream.ready()) {
					String line = inputStream.readLine();
					if (line.equals("")){
						break;
          }
					try {
						if (line.startsWith("7,") ||line.startsWith("6,") || line.startsWith("5,") ||line.startsWith("3,")) {
              String tokens[] = line.split(",");
              String dateString = tokens[1].substring(0,12);
              date = frm.parse(dateString);
              if ( !date.equals(lastDate) && lastDate!=null) {
                this.updateSeconds(lastDate,count);
                count = 0;
              }
              count++;
              lastDate = date;
						}
					} catch (ParseException e) {
						e.printStackTrace();
						logger.error("invalid date in input file "
								+ inputFiles[j].getName());
					}
          catch (ArrayIndexOutOfBoundsException e) {
        	  e.printStackTrace();
						logger.error("invalid record in input file "
								+ e);
          }
          catch (NullPointerException e) {
        	  e.printStackTrace();
						logger.error("invalid record in input file "
								+ e);
          }
				}
        this.updateSeconds(lastDate,count);
				inputStream.close();
			}
			File output = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					output));
			writeEvents(outputStream);
			outputStream.close();
			outputFiles[0] = output;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return outputFiles;
	}

  private void updateSeconds(Date date, int count)
	{
    SecondEvent current = new SecondEvent(count,date);
    if ( counts.size() < 20 )
    {
      counts.add(current);
    }
    else
    {
      SecondEvent minCount = (SecondEvent) Collections.min(counts,comparator);
      if (current.getCount() > minCount.getCount())
      {
        counts.remove(minCount);
        counts.add(current);
      }
      else if (  minCount.equals(current))
      {
        counts.add(current);
      }
    }
	}
   /**
	 * 
	 * @param outputStream -
	 *            input file
	 *
	 * @exception IOException
	 *                if output file couldn't be opened
	 */
  private void writeEvents(BufferedWriter outputStream) throws IOException {
    SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
    Collections.sort(counts,comparator);
    int size = counts.size();
    for ( int i = size-1,j=0; j<20; j++,i-- ) 
    {
      SecondEvent event = (SecondEvent) counts.elementAt(i);
      outputStream.write(frm.format(event.getDate())+ ","+event.getCount() + ",");
      outputStream.newLine();
    }
 	}
  
  private void skip(int num, BufferedReader inputStream) throws IOException {
		for (int i = 0; i < num; i++) {
			inputStream.readLine();
    }
	}
  
  public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
			SMSCSecondsConverter s = new SMSCSecondsConverter();
			File[] files = new File[1];
			files[0] =new File("D:/Meeting Files_2_3/15_5/SMSC/060513_TLG.DMP;1");
      s.convert(files,"SMSC");
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
	}
}