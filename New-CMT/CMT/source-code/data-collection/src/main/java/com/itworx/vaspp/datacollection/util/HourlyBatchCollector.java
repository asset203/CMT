/* 
 * File: BatchCollector.java
 * 
 * Date        Author          Changes
 * 
 * 13/04/2006  Nayera Mohamed  Created
 * 
 * Perform DataCollection Jobs for an interval of days
 */

package com.itworx.vaspp.datacollection.util;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
//import org.quartz.xml.JobSchedulingDataProcessor;

public class HourlyBatchCollector {

    public static final String FILE_SEPARATOR = System
            .getProperty("file.separator");
    private static final String lOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";

    public HourlyBatchCollector() {
    }

    /**
     * Create an appender using given System name
     *
     * @param systemName
     * @return Logger
     */
    private static Logger getSystemLogger(String systemName) {
        return DataCollectionManager.getSystemLogger(systemName);
    }

    /**
     * Collect all input related to system and nodes for each day in the
     * interval between startDate and endDate
     *
     * @param system    -
     *                  the system targeted for data collection.
     * @param startDate -
     *                  the start date for the interval targeted
     * @param endDate   -
     *                  the end date for the interval targeted
     */
    private static void collectHours(String system, Date startDate, Date endDate) {
        /**
         * Setting default time zone
         */
        JobManager.setAppDefaultTimeZone();

        System.out.println("collecting from " + startDate + " to" + endDate + " for system: " + system);
        Logger logger = getSystemLogger(system);
        logger.info("collecting from " + startDate + " to" + endDate + " for system: " + system);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        try {
            while (calendar.getTime().before(endDate)
                    || calendar.getTime().equals(endDate)) {
                System.out.println("Collecting for date " + calendar.getTime());
                try {
                    logger.info("HourlyBatchCollector.dispatchHourlyJob() - started dispatchHourlyJob( "
                            + system + "," + "system_nodes" + ")");
                    DataCollectionManager.dispatchHourlyJob(system, calendar.getTime());
                    logger.info("BatchCollector.dispatchHourlyJob() - finished dispatchHourlyJob( "
                            + system + "," + "system_nodes" + ")");
                } catch (Exception e) {
                    System.out.println("error running hourly job for day "
                            + calendar.getTime() + e);
                }
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }
        } catch (Exception e) {
            System.out.println("error running hourly job for day "
                    + startDate + e);
            logger.error("error running hourly job for system:" + system, e);
            System.exit(1);
        }
    }

    /**
     * Parse the string parameter into date and checks for valid format
     *
     * @param dateString -
     *                   the string parameter to be converted to date
     * @return Date - the parsed date from String
     */
    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat frm = new SimpleDateFormat();
            frm.applyPattern("dd/MM/yyyy:HH");
            Date date = frm.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.out
                    .println("Invalid Date Format, Format should be dd/MM/yyyy hh");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] arg) {

        if (arg.length != 4) {
            System.out.println("Usage Parameters:");
            System.out.println("");
            System.out
                    .println(" <<SystemName>> <<StartDate(dd/MM/yyyyHH)>> <<EndDate(dd/MM/yyyy HH)>> <<Path>>");
            System.exit(1);
        }

        try {

            DOMConfigurator.configure(arg[3] + FILE_SEPARATOR + lOG_CONF);

        } catch (Exception e) {
            System.out.println("CollectorScheduler.main() - " + e.getMessage());
            System.exit(1);
        }

        Date startDate = parseDate(arg[1]);
        Date endDate = parseDate(arg[2]);
        if (endDate.before(startDate)) {
            System.out.println("Invalid Parameters: start date must be earlier than end date");
            System.exit(1);
        }
        try {
            DataCollectionManager.init(arg[3]);
        } catch (Exception e) {
            System.out.println("Error Initiating DataCollection " + e);
            System.exit(1);
        }
        System.out.println("starting to collect data");
        collectHours(arg[0], startDate, endDate);
        //System.out.println(parseDate("15/08/2010:23"));
    }


}