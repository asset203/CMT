package com.itworx.vaspp.datacollection.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dao.TrendAnalysisDAO;
import com.itworx.vaspp.datacollection.objects.VCounter;

public class TrendAnalysisManager {

//	public static final String FILE_SEPARATOR = System
//			.getProperty("file.separator");
//
//	private static final String lOG_CONF = "resources" + FILE_SEPARATOR
//			+ "configuration" + FILE_SEPARATOR + "log4j.xml";

	private static Logger mainlogger = Logger.getLogger("TrendAnalysis");

	private static String mainPath;

	public static void init(String path) throws ApplicationException {
		try {
			mainPath = path;
			PropertyReader.init(mainPath, mainlogger);
		} catch (ApplicationException e) {
			mainlogger
					.error("DataCollectionManager.init() - error initiating DataCollectionManager "
							+ e);
			throw new ApplicationException("" + e);
		}
	}

	private static void setLogger(Logger l) {
		mainlogger = l;
	}

	public static Logger getLogger() {
		return mainlogger;
	}

	public static void dispatchJob(Date queryDate) throws InputException,
			ApplicationException, ParseException {
		String counterId = "";
		TrendAnalysisConfigReader trendAnalysisConfigReader = null;
		try {
			trendAnalysisConfigReader = new TrendAnalysisConfigReader();
		} catch (Exception e) {
			mainlogger.error(
					"TrendAnalysisManager.dispatchJob() - Create new TrendAnalysisConfigReader - Exception: ", e);
			return;
		}
		VCounter[] vcounters;
		vcounters = trendAnalysisConfigReader.getCounters();
		/*added by suzan tadrous*/
		TrendAnalysisDAO trendAnalDAO = new TrendAnalysisDAO();
		try
		{
	 
		trendAnalDAO.deleteTableRows("all","all","all","TREND_ANALYSIS_PROPERTIES");
		trendAnalDAO.insertCounterProperties(vcounters);
		trendAnalDAO.deleteTableRows("all","all","all","LO_ANALYSIS_COUNTERS");
		trendAnalDAO.insertMappingValues(vcounters);
		}
		catch(Exception e)
		{
			mainlogger.error(
				"TrendAnalysisManager.dispatchJob() - Exception: ", e);}
		/**/
		DecimalFormat decimalFormatter = new DecimalFormat("#.##");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String targetDate = df.format(queryDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(queryDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String yesterday = df.format(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -6);
		String lastWeek = df.format(calendar.getTime());
		int year = Integer.parseInt(targetDate.substring(6, 10));
		int month = Integer.parseInt(targetDate.substring(0, 2)) - 1;
		int day = Integer.parseInt(targetDate.substring(3, 5));
		calendar.set(year, month - 1, 1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int newDay;
		if (day > maxDay) {
			newDay = maxDay;
		} else {
			newDay = day;
		}
		String lastMonth = month + "/" + newDay + "/" + year;
		lastMonth = df.format(df.parse(lastMonth)); 
		for (int i = 0; i < vcounters.length; i++) {
			try {
				VCounter vcounter = vcounters[i];
				boolean validQuery = TrendAnalysisUtil.validateQuery(vcounter
						.getId(), vcounter.getSql(), vcounter.getDateColumn());
				if (validQuery) {
					counterId = vcounter.getId();
					String[] queries = TrendAnalysisUtil.createQueries(vcounter
							.getSql(), targetDate, lastMonth);
					TrendAnalysisDAO trendAnalysisDAO = new TrendAnalysisDAO();
					ArrayList results = new ArrayList();
					String description = "";
					Double today = trendAnalysisDAO
							.retrieveQueryResults(queries[0]);
					if (today != null) {
						Double result;
						for (int j = 1; j < queries.length; j++) {
							result = null;
							Double oldValue = trendAnalysisDAO
									.retrieveQueryResults(queries[j]);
							if (oldValue != null) {
								Double dividend = oldValue;
								if (oldValue == 0.0) {
									dividend = 1.0;
								}
								result = ((today - oldValue) / dividend) * 100;
							} else {
								switch (j) {
								case 1:
									description = description
											+ "Please note that day "
											+ yesterday + " has no data.\n";
									break;
								case 2:
									description = description
											+ "Please note that day "
											+ lastWeek + " has no data.\n";
									break;
								case 3:
									description = description
											+ "Please note that day "
											+ lastMonth + " has no data.\n";
									break;
								}
							}
							Double formattedResult;
							if (result != null) {
								formattedResult = Double
										.parseDouble(decimalFormatter
												.format(result));
							} else {
								formattedResult = null;
							}
							results.add(j - 1, formattedResult);
						}
					} else {
						description = "Please note that day " + targetDate
								+ " has no data.\n";
						for (int j = 1; j < queries.length; j++) {
							results.add(j - 1, null);
						}

					}
					boolean existingRow = trendAnalysisDAO.retrieveTableRows(
							counterId, targetDate );
					if (existingRow) {
						trendAnalysisDAO.deleteTableRows(counterId, targetDate ,null ,"TREND_ANALYSIS_RESULTS");
					}
					trendAnalysisDAO.insertQueryResults(counterId, targetDate,
							results, description);
				}
			} catch (Exception e) {
				mainlogger.error(
						"TrendAnalysisManager.dispatchJob() - Exception: ", e);
				continue;
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			DOMConfigurator
//					.configure("C:\\Documents and Settings\\marwan.abdelhady\\Desktop\\Vodafone\\SVN\\SourceCode\\DataCollection"
//							+ FILE_SEPARATOR + lOG_CONF);
			TrendAnalysisManager engine = new TrendAnalysisManager();
			engine
					.init("C:\\Documents and Settings\\marwan.abdelhady\\Desktop\\Vodafone\\SVN\\SourceCode\\DataCollection");
			TrendAnalysisManager.dispatchJob(new Date("04/05/2010"));
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
