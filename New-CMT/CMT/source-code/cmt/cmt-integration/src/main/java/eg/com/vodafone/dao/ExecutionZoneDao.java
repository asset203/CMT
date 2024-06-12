package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.JobExecutionZoneMapper;
import eg.com.vodafone.model.JobExecutionZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author marwa.goda
 * @since 4/23/13
 */
@Repository
public class ExecutionZoneDao {
    private final static Logger logger = LoggerFactory.getLogger(SystemEventDao.class);
    private static final String SELECT_ALL_CLAUSE = "SELECT * FROM ";
    private static final String EXECUTION_ZONE = "EXECUTION_ZONE_LKP";
    private static final String QUERY_QUARTZ_TBL_PREFIXES =
            "SELECT ID, QRTZ_TABLE_PREFIX FROM EXECUTION_ZONE_LKP ORDER BY NAME ASC";
    private static final String QUERY_ZONE_ID =
            "SELECT ID FROM EXECUTION_ZONE_LKP ORDER BY ID";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExecutionZoneDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<JobExecutionZone> getExecutionZones() {
        final StringBuffer sqlQuery = new StringBuffer(SELECT_ALL_CLAUSE).append(EXECUTION_ZONE);
        List<JobExecutionZone> jobExecutionZones;

        try {
            jobExecutionZones = jdbcTemplate.query(sqlQuery.toString(), new JobExecutionZoneMapper());

        } catch (EmptyResultDataAccessException exception) {
            logger.error("jobDao.getJobById: No Result returned", exception);
            jobExecutionZones = null;
        }
        return jobExecutionZones;
    }


    /**
     * Retrieve all Quartz tables' prefixes
     *
     * @return all Quartz tables' prefixes
     */
    public Map<String, String> getZonesQuartzPrefixes(){
        return jdbcTemplate.query(QUERY_QUARTZ_TBL_PREFIXES, new ResultSetExtractor<Map<String, String>>() {
            @Override
            public Map<String, String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<String, String> zoneQuartzPrefixesMap = new TreeMap<String, String>();
                while(resultSet.next()){
                    zoneQuartzPrefixesMap.put(String.valueOf(resultSet.getInt("ID")),
                            resultSet.getString("QRTZ_TABLE_PREFIX"));
                }
                return zoneQuartzPrefixesMap;
            }
        });
    }

    public List<String> getZonesID(){
        return jdbcTemplate.query(QUERY_ZONE_ID, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return String.valueOf(resultSet.getInt("ID"));
            }
        });
    }
}
