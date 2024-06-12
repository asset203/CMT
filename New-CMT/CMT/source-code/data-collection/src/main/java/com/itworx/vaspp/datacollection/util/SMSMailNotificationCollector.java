package com.itworx.vaspp.datacollection.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class SMSMailNotificationCollector {
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static final String lOG_CONF = "resources" + FILE_SEPARATOR
			+ "configuration" + FILE_SEPARATOR + "log4j.xml";

	public SMSMailNotificationCollector() {
	}

	public static void main(String[] arg) {
		for (int i = 0; i < arg.length; i++) {
			System.out.println(arg[i]);
		}
		
		boolean hourly = false;
		
		if (arg.length != 4) {
			System.out.println("Usage Parameters:(daily)");
			System.out.println(" <<StartDate(MM/dd/yyyy)>> <<EndDate(MM/dd/yyyy)>> <<Path>> d");
			System.out.println("");
			System.out.println("Usage Parameters:(hourly)");
			System.out.println(" <<StartDate(MM/dd/yyyy:HH)>> <<EndDate(MM/dd/yyyy:HH)>> <<Path>> h");
			System.exit(1);
		}
		
		String collectionType = arg[3];
		if("h".equals(collectionType)){
			hourly = true;
		}

		try {
			System.out.println(arg[2]);
			System.out.println(FILE_SEPARATOR);
			System.out.println(lOG_CONF);
			String configurationPath = arg[2] + FILE_SEPARATOR + lOG_CONF;
			System.out.println("configutation path for " + arg[2] + " = "
					+ configurationPath);
			DOMConfigurator.configure(configurationPath);
		} catch (Exception e) {
			System.out.println("CollectorScheduler.main() - " + e.getMessage());
		}

		Date startDate = null;
		Date endDate = null;
		if(hourly){
			startDate = parseHourlyDate(arg[0]);
			endDate = parseHourlyDate(arg[1]);			
		}else{
			startDate = parseDate(arg[0]);
			endDate = parseDate(arg[1]);
		}
		if (endDate.before(startDate)) {
			System.out.println("Invalid Parameters: start date must be earlier than end date");
			System.exit(1);
		}
		try {
			SMSMailNotificationManager.init(arg[2]);
		} catch (Exception e) {
			System.out.println("Error Initiating DataCollection " + e);
			System.exit(1);
		}
		System.out.println("starting to collect data");
		collectDays(startDate, endDate,hourly);
	}

	private static Date parseDate(String dateString) {
		try {
			SimpleDateFormat frm = new SimpleDateFormat();
			frm.setLenient(false);
			frm.applyPattern("MM/dd/yyyy");
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			System.out.println("Invalid Date. Format should be MM/dd/yyyy");
			System.exit(1);
		}
		return null;
	}
	
	private static Date parseHourlyDate(String dateString) {
		try {
			SimpleDateFormat frm = new SimpleDateFormat();
			frm.setLenient(false);
			frm.applyPattern("MM/dd/yyyy:HH");
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			System.out.println("Invalid Date. Format should be MM/dd/yyyy:HH");
			System.exit(1);
		}
		return null;
	}
	

	private static void collectDays(Date startDate, Date endDate,boolean hourly) {
		System.out.println("collecing from " + startDate + " to " + endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		SMSMailNotificationManager notificationManager = new SMSMailNotificationManager();
		while (calendar.getTime().before(endDate)
				|| calendar.getTime().equals(endDate)) {
			System.out.println("Collecting for date " + calendar.getTime());
			try {
				Logger logger = Logger.getLogger("SMSMailNotification");
				logger.info("SMSMailNotificationManager.dispatchJob() - started dispatchJob()");
				notificationManager.dispatchJob(calendar.getTime(),hourly,new PersistenceManager(true));
				logger.info("SMSMailNotificationManager.dispatchJob() - finished dispatchJob()");
			} catch (Exception e) {
				System.out.println("error running job for day " + calendar.getTime() + e);
			}
			if(hourly){
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}else{
				calendar.add(Calendar.DAY_OF_MONTH, 1);	
			}
		}
	}
}
