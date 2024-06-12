package com.itworx.vaspp.datacollection.util;

import org.apache.log4j.Logger;

public class TrendAnalysisUtil {

	private static Logger logger = Logger.getLogger("TrendAnalysis");

	public static boolean validateQuery(String id, String sql, String dateColumn) {
		logger.debug("TrendAnalysisUtil.validateQuery() - validating query");
		if (id != null && !id.trim().equals("") && sql != null
				&& !sql.trim().equals("") && dateColumn != null
				&& !dateColumn.trim().equals("")) {
			String sqlWithDate = sql.replaceAll("(\\s)*","");
			if (sql.toLowerCase().contains("select")
					&& sql.toLowerCase().contains("from")
					&& sql.toLowerCase().contains("where") && sqlWithDate.contains("to_date('$date(mm/dd/yyyy)$','mm/dd/yyyy')")) {

				String[] splittedSql = sql
						.split("to_date(\\s)*\\((\\s)*to_char(\\s)*\\((\\s)*");
				if (splittedSql.length == 2) {
					String sqlDateColumn = (splittedSql[1]
							.split("(\\s)*,(\\s)*'(\\s)*mm/dd/yyyy(\\s)*'(\\s)*\\)(\\s)*,(\\s)*'(\\s)*mm/dd/yyyy(\\s)*'(\\s)*\\)"))[0];

					String sqlValidation = (sql.toLowerCase()).split("from")[0];
					if (dateColumn.equalsIgnoreCase(sqlDateColumn)
							&& !sqlValidation.contains(",")
							&& !sqlValidation.contains("*")) {
						return true;
					} else {
						logger
								.debug("TrendAnalysisUtil.validateQuery() - Invalid query!");
						return false;
					}
				} else {
					logger
							.debug("TrendAnalysisUtil.validateQuery() - Invalid query! No date line with the proper format was found");
					return false;
				}
			}
			else{
				logger
				.debug("TrendAnalysisUtil.validateQuery() - Not a valid SELECT Query");
				return false;
			}
		} else {
			logger
					.debug("TrendAnalysisUtil.validateQuery() - Invalid counter element! Null or empty values");
			return false;
		}
	}

	public static String[] createQueries(String sql, String date, String newDate) {
		logger.debug("TrendAnalysisUtil.createQueries() - creating queries");
		String newSqlToday = sql
				.replaceFirst("[fF][rR][oO][mM]", "result from");
		newSqlToday = newSqlToday
				.replaceAll(
						"to_date(\\s)*\\((\\s)*'(\\s)*\\$(\\s)*date(\\s)*\\((\\s)*mm/dd/yyyy(\\s)*\\)(\\s)*\\$(\\s)*'(\\s)*,(\\s)*'(\\s)*mm/dd/yyyy(\\s)*'(\\s)*\\)",
						"to_date('\\$date(mm/dd/yyyy)\\$','mm/dd/yyyy')");
		String newSqlYesterday = newSqlToday
				.replaceAll(
						"to_date(\\s)*\\((\\s)*'(\\s)*\\$(\\s)*date(\\s)*\\((\\s)*mm/dd/yyyy(\\s)*\\)(\\s)*\\$(\\s)*'(\\s)*,(\\s)*'(\\s)*mm/dd/yyyy(\\s)*'(\\s)*\\)",
						"to_date('\\$date(mm/dd/yyyy)\\$','mm/dd/yyyy')-1");
		String newSqlWeekly = newSqlYesterday.replace(
				"to_date('$date(mm/dd/yyyy)$','mm/dd/yyyy')-1",
				"to_date('$date(mm/dd/yyyy)$','mm/dd/yyyy')-7");
		String newSqlMonthly = newSqlYesterday.replace(
				"to_date('$date(mm/dd/yyyy)$','mm/dd/yyyy')-1", "to_date('"
						+ newDate + "','mm/dd/yyyy')");
		newSqlToday = newSqlToday.replace("$date(mm/dd/yyyy)$", date);
		newSqlYesterday = newSqlYesterday.replace("$date(mm/dd/yyyy)$", date);
		newSqlWeekly = newSqlWeekly.replace("$date(mm/dd/yyyy)$", date);
		newSqlMonthly = newSqlMonthly.replace("$date(mm/dd/yyyy)$", date);

		logger.debug("TrendAnalysisUtil.createQueries() - Daily query: "
				+ newSqlYesterday);
		logger.debug("TrendAnalysisUtil.createQueries() - Weekly query: "
				+ newSqlWeekly);
		logger.debug("TrendAnalysisUtil.createQueries() - Monthly query: "
				+ newSqlMonthly);

		String[] queries = new String[4];
		queries[0] = newSqlToday;
		queries[1] = newSqlYesterday;
		queries[2] = newSqlWeekly;
		queries[3] = newSqlMonthly;
		return queries;
	}

}
