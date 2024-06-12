package com.itworx.vaspp.datacollection.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class TrendAnalysisCollector {

	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static final String lOG_CONF = "resources" + FILE_SEPARATOR
			+ "configuration" + FILE_SEPARATOR + "log4j.xml";

	public TrendAnalysisCollector() {
	}

	/**
	 * Collect all input related to system and nodes for each day in the
	 * interval between startDate and endDate
	 * 
	 * @param system
	 *            - the system targeted for data collection.
	 * @param startDate
	 *            - the start date for the interval targeted
	 * @param endDate
	 *            - the end date for the interval targeted
	 * 
	 */
	private static void collectDays(Date startDate, Date endDate) {
		System.out.println("collecing from " + startDate + " to " + endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		while (calendar.getTime().before(endDate)
				|| calendar.getTime().equals(endDate)) {
			System.out.println("Collecting for date " + calendar.getTime());
			try {
				Logger logger = Logger.getLogger("TrendAnalysis");
				logger
						.info("TrendAnalysisCollector.dispatchJob() - started dispatchJob()");
				TrendAnalysisManager.dispatchJob(calendar.getTime());
				logger
						.info("TrendAnalysisCollector.dispatchJob() - finished dispatchJob()");
			} catch (Exception e) {
				System.out.println("error running job for day "
						+ calendar.getTime() + e);
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	/**
	 * Parse the string parameter into date and checks for valid format
	 * 
	 * 
	 * @param dateString
	 *            - the string parameter to be converted to date
	 * 
	 * @return Date - the parsed date from String
	 * 
	 */
	private static Date parseDate(String dateString) {
		try {
			SimpleDateFormat frm = new SimpleDateFormat();
			frm.setLenient(false);
			frm.applyPattern("MM/dd/yyyy");
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			System.out
					.println("Invalid Date. Format should be MM/dd/yyyy");
			System.exit(1);
		}
		return null;
	}

	public static void main(String[] arg) {
		for (int i = 0; i < arg.length; i++) {
			System.out.println(arg[i]);
		}
		if (arg.length != 3) {
			System.out.println("Usage Parameters:");
			System.out.println("");
			System.out
					.println(" <<StartDate(MM/dd/yyyy)>> <<EndDate(MM/dd/yyyy)>> <<Path>>");
			System.exit(1);
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
		if (arg[0].length() != 10 || arg[1].length() != 10) {
			System.out
					.println("Invalid Date Format, Format should be MM/dd/yyyy");
			System.exit(1);
		}
		Date startDate = parseDate(arg[0]);
		Date endDate = parseDate(arg[1]);
		if (endDate.before(startDate)) {
			System.out
					.println("Invalid Parameters: start date must be earlier than end date");
			System.exit(1);
		}
		try {
			TrendAnalysisManager.init(arg[2]);
		} catch (Exception e) {
			System.out.println("Error Initiating DataCollection " + e);
			System.exit(1);
		}
		System.out.println("starting to collect data");
		collectDays(startDate, endDate);
	}

}
