package eg.com.vodafone.dao;

import eg.com.vodafone.model.DCLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/14/13
 * Time: 12:22 PM
 */
@Repository
public class DCLogEntryDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(DCLogEntryDao.class);
    /**
     * Initialize a mapper map to map sort fields to the actual DB column
     */
    private static final Map<String, String> PROPERTY_MAPPER;

    static {
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("var.id", "ID");
        tempMap.put("var.logDate", "LOG_DATE");
        tempMap.put("var.systemName", "SYSTEM_NAME");
        tempMap.put("var.nodeName", "NODE");
        tempMap.put("var.logType", "LOG_TYPE");
        tempMap.put("var.retrialCount", "RETRY");
        tempMap.put("var.errorCOde", "ERROR_CODE");
        tempMap.put("var.logEntryDescription", "DESCRIPTION");

        PROPERTY_MAPPER = Collections.unmodifiableMap(tempMap);
    }

    private final JdbcTemplate simpleJdbcTemplate;

    @Autowired
    public DCLogEntryDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        simpleJdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<DCLogEntry> selectAllDCLogEntries() {
        return simpleJdbcTemplate.query("SELECT * FROM DC_LOG_FILES_INFO",
                new RowMapper<DCLogEntry>() {
                    @Override
                    public DCLogEntry mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new DCLogEntry(
                                resultSet.getInt("ID"),
                                resultSet.getDate("LOG_DATE"),
                                resultSet.getString("SYSTEM_NAME"),
                                resultSet.getString("NODE"),
                                resultSet.getString("LOG_TYPE"),
                                resultSet.getString("ERROR_CODE"),
                                resultSet.getString("DESCRIPTION"),
                                resultSet.getInt("RETRY")
                        );
                    }
                });

    }

    /**
     * Get DC Logs by filter & paging indices
     *
     * @param systemName systemName
     * @param logType    logType
     * @param fromDate   fromDate
     * @param toDate     toDate
     * @param startIndex startIndex
     * @param endIndex   endIndex
     * @return Search result
     */
    public List<DCLogEntry> getFilteredResultsByPageIndex(
            String systemName, String logType, Date fromDate,
            Date toDate, String sortField, String sortType, int startIndex, int endIndex) {
        String sortField1 = sortField;
        String sortType1 = sortType;

        if (!StringUtils.hasText(sortField1)) {
            sortField1 = "dCLogEntry.ID";
        }

        if (sortType1 == null) {
            sortType1 = "DESC";
        }

        if (!StringUtils.hasText(sortField1)
                || !StringUtils.hasText(PROPERTY_MAPPER.get(sortField1))) {
            sortField1 = "var.logDate";
        }

        StringBuffer query = new StringBuffer("SELECT * FROM(SELECT ID, LOG_DATE, SYSTEM_NAME, NODE, ").append(
                "DESCRIPTION, RETRY, LOG_TYPE, ERROR_CODE, DENSE_RANK() OVER (ORDER BY ").append(PROPERTY_MAPPER.get(sortField1)).
                append(" ").append(sortType1).append(",ID ").append(sortType1).append(") AS DENSE_RANK ").append(
                "FROM DC_LOG_FILES_INFO WHERE ");

        constructFilterQuery(query, systemName, logType);
        query.append(" ORDER BY ").append(PROPERTY_MAPPER.get(sortField1)).
                append(" ").append(sortType1).append(") WHERE DENSE_RANK BETWEEN ? AND ?");

        LOGGER.debug("Final query: " + query);

        return simpleJdbcTemplate.query(query.toString(),
                new Object[]{systemName, logType, fromDate, toDate, startIndex, endIndex},
                new RowMapper<DCLogEntry>() {
                    @Override
                    public DCLogEntry mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new DCLogEntry(
                                resultSet.getInt("ID"),
                                resultSet.getDate("LOG_DATE"),
                                resultSet.getString("SYSTEM_NAME"),
                                resultSet.getString("NODE"),
                                resultSet.getString("LOG_TYPE"),
                                resultSet.getString("ERROR_CODE"),
                                resultSet.getString("DESCRIPTION"),
                                resultSet.getInt("RETRY")
                        );
                    }
                });
    }

    /**
     * Gets the count of the returned results
     *
     * @param systemName systemName
     * @param logType    logType
     * @param fromDate   fromDate
     * @param toDate     toDate
     * @return result count
     */
    public int getResultSetCount(String systemName,
                                 String logType, Date fromDate, Date toDate) {

        LOGGER.debug("Parameters received in DCLogEntryDao:" +
                "\nSystem Name={}\nLog Type={}\nFrom Date={}\nTo Date={}",
                new Object[]{systemName, logType, fromDate.toString(), toDate.toString()});

        StringBuffer query = new StringBuffer("SELECT COUNT(*) FROM DC_LOG_FILES_INFO WHERE ");
        constructFilterQuery(query, systemName, logType);

        LOGGER.debug("SQl query: {}", new Object[]{query});

        try {
            return simpleJdbcTemplate.queryForInt(query.toString(),
                    systemName, logType, fromDate, toDate);

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            LOGGER.error("No result was returned & an EmptyResultDataAccessException was thrown",
                    emptyResultDataAccessException.getMessage());
        }
        return 0;
    }

    /**
     * Construct StringBuffer query for selected filtered results
     *
     * @param systemName systemName
     * @param logType    logType
     */
    private void constructFilterQuery(StringBuffer query, String systemName, String logType) {
        StringBuffer systemQuery = new StringBuffer("SYSTEM_NAME = ?");
        StringBuffer logTypeQuery = new StringBuffer("LOG_TYPE = ?");

        if (systemName != null && systemName.equals("%")) {
            systemQuery = new StringBuffer("SYSTEM_NAME LIKE ?");
        }

        if (logType != null && logType.equals("%")) {
            logTypeQuery = new StringBuffer("LOG_TYPE LIKE ?");
        }

        query.append(systemQuery).append(" AND ").append(logTypeQuery).
                append(" AND LOG_DATE BETWEEN ? AND ?");
    }

}
