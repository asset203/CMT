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

public class BatchCollector {

    public static final String FILE_SEPARATOR = System
            .getProperty("file.separator");
    private static final String lOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";

    public BatchCollector() {
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
    private static void collectDays(String system, Date startDate, Date endDate) {

        /**
         * Setting default time zone
         */
        JobManager.setAppDefaultTimeZone();

        System.out.println("collecting from " + startDate + " to" + endDate + " for system: " + system);
        Logger logger = getSystemLogger(system);
        try {
            logger.info("collecting from " + startDate + " to" + endDate + " for system: " + system);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            String[] systemNodes = null;
            if (system.contains("(")) {
                String nodes = Utils.stringBetween(system, "(", ")");
                systemNodes = nodes.split(",");
                system = system.substring(0, system.indexOf('('));
            }
            while (calendar.getTime().before(endDate)
                    || calendar.getTime().equals(endDate)) {
                System.out.println("Collecting for date " + calendar.getTime());
                try {
                    logger.info("BatchCollector.dispatchJob() - started dispatchJob( "
                            + system + "," + "system_nodes" + ")");
                    if (systemNodes == null || systemNodes.length == 0) {
                        DataCollectionManager.dispatchJob(system, "system_nodes",
                                calendar.getTime());
                    } else {
                        for (int i = 0; i < systemNodes.length; i++) {
                            DataCollectionManager.dispatchJob(system, systemNodes[i],
                                    calendar.getTime());
                        }
                    }
                    logger.info("BatchCollector.dispatchJob() - finished dispatchJob( "
                            + system + "," + "system_nodes" + ")");
                } catch (Exception e) {
                    System.out.println("error running job for day "
                            + calendar.getTime() + e);
                    logger.error("error running job for day "
                            + calendar.getTime(), e);
                    System.exit(1);

                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception ex) {
            logger.error("General exception thrown while collecting days:" + ex.getMessage(), ex);
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
            frm.applyPattern("dd/MM/yyyy");
            Date date = frm.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.out
                    .println("Invalid Date Format, Format should be dd/MM/yyyy");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] arg) {
    /*	arg[0]="EOCN";
        arg[1]="12/09/2010";
		arg[2]="12/09/2010";
		arg[3]="D:\\build\\pahse8\\DataCollection";*/

        if (arg.length != 4) {
            System.out.println("Usage Parameters:");
            System.out.println("");
            System.out
                    .println(" <<SystemName>> <<StartDate(dd/MM/yyyy)>> <<EndDate(dd/MM/yyyy)>> <<Path>>");
            System.exit(1);
        }
		
		/*try {
			JobSchedulingDataProcessor p = new org.quartz.xml.JobSchedulingDataProcessor();
			p.processFile(arg[3] + "/resources/configuration/jobs.xml");
			Map jobsMap = p.getScheduledJobs();
			Iterator iterator = jobsMap.keySet().iterator();
			while (iterator.hasNext()) {
				Job job = (Job) jobsMap.get(iterator.next());
				System.out.println(job.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(true)
			return ;*/
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
        if ("EOCNCTEMP".equalsIgnoreCase(arg[0])) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR, 0);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(endDate);
            calendar2.set(Calendar.HOUR, 23);
            while (calendar.getTime().before(calendar2.getTime()) || calendar.getTime().equals(calendar2.getTime())) {
                try {
                    DataCollectionManager.dispatchJob("EOCNC", "system_nodes",
                            calendar.getTime());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.exit(1);
                }
                calendar.add(calendar.HOUR, 1);
            }
        } else {
            collectDays(arg[0], startDate, endDate);
        }
    }


}