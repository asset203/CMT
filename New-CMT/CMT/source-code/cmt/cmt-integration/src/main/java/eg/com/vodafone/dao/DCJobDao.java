package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.ZoneUtilizationRowMapper;
import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.JobExecutionZone;
import eg.com.vodafone.model.ZoneUtilization;
import org.quartz.JobDataMap;
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
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 5/10/13
 * Time: 5:01 PM
 */
@Repository
public class DCJobDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(DCJobDao.class);
    private static final String QUERY_JOBS = "SELECT % AS ZONE, $_JOB_DETAILS.*, $_TRIGGERS.TRIGGER_NAME, " +
            "$_CRON_TRIGGERS.CRON_EXPRESSION FROM $_JOB_DETAILS " +
            "LEFT OUTER JOIN $_TRIGGERS ON $_JOB_DETAILS.JOB_NAME = $_TRIGGERS.JOB_NAME " +
            "AND $_TRIGGERS.TRIGGER_NAME = $_JOB_DETAILS.JOB_NAME || '_TRIGGER'" +
            "LEFT OUTER JOIN $_CRON_TRIGGERS ON $_TRIGGERS.TRIGGER_NAME = $_CRON_TRIGGERS.TRIGGER_NAME " +
            "WHERE $_JOB_DETAILS.JOB_GROUP = 'VDataCollectionJob' ";
    private final JdbcTemplate simpleJDBCTemplate;


    @Autowired
    public DCJobDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        simpleJDBCTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Retrieve all Jobs' count from Quartz "QRTZ_JOB_DETAILS" table
     *
     * @param quartzTblPrefixes Quartz tables prefix
     * @return List with all available Jobs
     */
    public int getAllJobNameCount(Map<String, String> quartzTblPrefixes, String keyWord) {

        List<String> keyWordList = new ArrayList<String>();
        String keyWordSearch = "%";
        if(StringUtils.hasText(keyWord)){
            keyWordSearch = "%" + keyWord + "%";
        }
        for(Map.Entry<String, String> zone : quartzTblPrefixes.entrySet()){
                keyWordList.add(keyWordSearch);
        }

        String query = "SELECT COUNT(*) FROM (";
        query += constructQueryAllJobs(quartzTblPrefixes);
        query += ")";
        LOGGER.debug("Final query for count:{} ", query);
        int count = 0;
        try {
            count = simpleJDBCTemplate.queryForInt(query, keyWordList.toArray());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            LOGGER.error("No result was returned & an EmptyResultDataAccessException was thrown",
                    emptyResultDataAccessException.getMessage());
        }

        return count;
    }

    /**
     * Retrieve Jobs by index & perform sorting
     *
     * @param quartzTblPrefixes
     * @param sortType
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<String> getJobsByIndex(
            Map<String, String> quartzTblPrefixes, String keyWord,
            String sortType, int startIndex, int endIndex) {

        List<String> keyWordList = new ArrayList<String>();
        String keyWordSearch = "%";
        if(StringUtils.hasText(keyWord)){
            keyWordSearch = "%" + keyWord + "%";
        }

        for(Map.Entry<String, String> zone : quartzTblPrefixes.entrySet()){
            keyWordList.add(keyWordSearch);
        }

        if (!StringUtils.hasText(sortType)) {
            sortType = "ASC";
        }

        String query = "SELECT JOB_NAME FROM (SELECT JOB_NAME, ROWNUM AS RN FROM ( (";
        query += constructQueryAllJobs(quartzTblPrefixes);
        query += ") ORDER BY JOB_NAME " + sortType + " )) WHERE RN BETWEEN " + startIndex + " AND " + endIndex;

        LOGGER.debug("Complete query jobs by index query:{}", query);

        return simpleJDBCTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("JOB_NAME");
            }
        }, keyWordList.toArray());
    }

    /**
     * Get all job details from all available zones
     *
     * @param quartzTblPrefixes
     * @return
     */
    public List<Job> getAllJobs(Map<String, String> quartzTblPrefixes) {
        StringBuffer queryAllJobs = new StringBuffer(200);
        String query = "";
        List<Job> jobList = new ArrayList<Job>();
        Iterator<Map.Entry<String, String>> quartzTblPrefixesItr
                = quartzTblPrefixes.entrySet().iterator();
        while (quartzTblPrefixesItr.hasNext()) {
            Map.Entry<String, String> prefix = quartzTblPrefixesItr.next();
            LOGGER.debug("Get all jobs in Quartz table named: {}_JOB_DETAILS", prefix.getValue());
            query += StringUtils.replace(QUERY_JOBS, "$", prefix.getValue());
            query = StringUtils.replace(query, "%", prefix.getKey());
            if (quartzTblPrefixesItr.hasNext()) {
                query += " UNION ALL ";
            }
        }
        LOGGER.debug("final query: {}", query);
        jobList = simpleJDBCTemplate.query(query, new ResultSetExtractor<List<Job>>() {
            @Override
            public List<Job> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<Job> jobList = new ArrayList<Job>();
                Job job;
                JobDataMap jobDataMap = new JobDataMap();
                while (resultSet.next()) {
                    if (resultSet.getBlob("JOB_DATA") != null) {
                        ObjectInputStream ins = null;
                        try {
                            ByteArrayInputStream bais =
                                    new ByteArrayInputStream(resultSet.getBytes("JOB_DATA"));
                            ins = new ObjectInputStream(bais);

                            jobDataMap = (JobDataMap) ins.readObject();
                            ins.close();
                        } catch (IOException e) {
                            LOGGER.error("IOException", e);
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("ClassNotFoundException", e);
                        } finally {
                            if (ins != null) {
                                try {
                                    ins.close();
                                } catch (IOException e) {
                                    LOGGER.error("IOException", e);
                                }
                            }
                        }
                    }
                    job = new Job(
                            resultSet.getString("JOB_NAME"),
                            resultSet.getString("DESCRIPTION"),
                            resultSet.getString("CRON_EXPRESSION"),
                            jobDataMap,
                            resultSet.getInt("ZONE")
                    );
                    if (job != null) {
                        jobList.add(job);
                    }
                }
                return jobList;
            }
        });


        LOGGER.debug("final query for all jobs size is: {}", jobList.size());
        return jobList;
    }

    /**
     * Search for a job by a search key
     *
     * @param quartzTblPrefixes Quartz tables prefix
     * @param searchKey         search key
     * @return List of jobs returned from search query
     */
    public List<String> searchForJobByName(
            Map<String, String> quartzTblPrefixes, String searchKey) {
        StringBuffer queryAllJobs = new StringBuffer(200);
        int counter = 0;
        Object[] queryArgs = new Object[quartzTblPrefixes.size()];
        Iterator<Map.Entry<String, String>> quartzTblPrefixesItr
                = quartzTblPrefixes.entrySet().iterator();
        while (quartzTblPrefixesItr.hasNext()) {
            Map.Entry<String, String> prefix = quartzTblPrefixesItr.next();
            queryAllJobs.append("SELECT JOB_NAME FROM ").append(prefix.getValue()).
                    append("_JOB_DETAILS WHERE JOB_GROUP = 'VDataCollectionJob' AND JOB_NAME LIKE ? ");
            queryArgs[counter] = "%" + searchKey + "%";
            if (counter != quartzTblPrefixes.size() - 1) {
                queryAllJobs.append(" UNION ");
                counter++;
            }
        }
        LOGGER.debug("final query for all jobs is: {}", queryAllJobs);

        return simpleJDBCTemplate.query(queryAllJobs.toString(), queryArgs,
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString("JOB_NAME");
                    }
                });

    }

    /**
     * Load job data from Quartz tables using its job name
     *
     * @param quartzTblPrefixes Quartz tables prefix
     * @param jobName           job name to search for
     * @return full job Object
     */
    public Job getJobByName(Map<String, String> quartzTblPrefixes, String jobName) {
        LOGGER.debug("will check in all available zones & return the first one found for job {}", jobName);

        Job job;
        String query = "";
        Object[] queryArgs = new Object[quartzTblPrefixes.size()];
        Iterator<Map.Entry<String, String>> quartzTblPrefixesItr
                = quartzTblPrefixes.entrySet().iterator();
        int count = 0;
        while (quartzTblPrefixesItr.hasNext()) {
            Map.Entry<String, String> prefix = quartzTblPrefixesItr.next();
            LOGGER.debug("search for job: {} in Quartz table named: {}_JOB_DETAILS", jobName, prefix.getValue());
            query += StringUtils.replace(QUERY_JOBS + " AND $_JOB_DETAILS.JOB_NAME = ?", "$", prefix.getValue());
            query = StringUtils.replace(query, "%", prefix.getKey());
            if (quartzTblPrefixesItr.hasNext()) {
                query += " UNION ALL ";
            }
            queryArgs[count] = jobName;
            count++;

        }
        LOGGER.debug("final query: {} \nFinal Args: {}", query, queryArgs);

        job = simpleJDBCTemplate.query(query,
                queryArgs,
                new ResultSetExtractor<Job>() {
                    @Override
                    public Job extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        JobDataMap jobDataMap = new JobDataMap();
                        while (resultSet.next()) {

                            if (resultSet.getBlob("JOB_DATA") != null
                                    && StringUtils.hasText(resultSet.getString("JOB_NAME"))) {
                                ObjectInputStream ins = null;
                                try {
                                    ByteArrayInputStream bais =
                                            new ByteArrayInputStream(resultSet.getBytes("JOB_DATA"));
                                    ins = new ObjectInputStream(bais);

                                    jobDataMap = (JobDataMap) ins.readObject();
                                    ins.close();
                                } catch (IOException e) {
                                    LOGGER.error("IOException", e);
                                } catch (ClassNotFoundException e) {
                                    LOGGER.error("ClassNotFoundException", e);
                                } finally {
                                    if (ins != null) {
                                        try {
                                            ins.close();
                                        } catch (IOException e) {
                                            LOGGER.error("IOException", e);
                                        }
                                    }
                                }
                                Job job1 = new Job(
                                        resultSet.getString("JOB_NAME"),
                                        resultSet.getString("DESCRIPTION"),
                                        resultSet.getString("CRON_EXPRESSION"),
                                        jobDataMap,
                                        resultSet.getInt("ZONE")
                                );
                                return job1;
                            }else{
                                return null;
                            }
                        }
                        return null;
                    }
                });


        return job;
    }

    /**
     * Construct a UNION SQL query to select all job names
     * from all available zones
     *
     * @param quartzTblPrefixes
     * @return
     */
    private StringBuffer constructQueryAllJobs(Map<String, String> quartzTblPrefixes) {
        StringBuffer queryAllJobs = new StringBuffer(200);
        int counter = 0;
        Iterator<Map.Entry<String, String>> quartzTblPrefixesItr
                = quartzTblPrefixes.entrySet().iterator();
        while (quartzTblPrefixesItr.hasNext()) {
            Map.Entry<String, String> prefix = quartzTblPrefixesItr.next();
            queryAllJobs.append("SELECT JOB_NAME FROM ").append(prefix.getValue()).
                    append("_JOB_DETAILS WHERE JOB_GROUP = 'VDataCollectionJob' AND JOB_NAME LIKE ? ");
            if (counter != quartzTblPrefixes.size() - 1) {
                queryAllJobs.append(" UNION ");
                counter++;
            }
        }
        LOGGER.debug("final query for all jobs is: {}", queryAllJobs);
        return queryAllJobs;
    }
    
    
    
    
    public List<ZoneUtilization> getZoneUtilization(
            Map<String, JobExecutionZone> zones, Long nextFireTime) {
        
        StringBuffer queryForZone = new StringBuffer();
        int counter = 0;
        Object[] queryArgs = new Object[zones.size()];
        Iterator<Map.Entry<String, JobExecutionZone>> zoneItr
                = zones.entrySet().iterator();
        while (zoneItr.hasNext()) {
            
            Map.Entry<String, JobExecutionZone> zone = zoneItr.next();
            queryForZone.append("SELECT '").append(zone.getValue().getName()).append("' ZONE,")
                    .append("(SELECT COUNT(*) FROM ").append(zone.getValue().getQrtzTblPrefix())
                    .append("_FIRED_TRIGGERS) running, (SELECT COUNT(*) FROM  ")
                    .append(zone.getValue().getQrtzTblPrefix()).append("_TRIGGERS").append(" WHERE NEXT_FIRE_TIME >= ?)")
                    .append("next_run,").append(" (SELECT COUNT(*) FROM ").append(zone.getValue().getQrtzTblPrefix())
                    .append("_TRIGGERS) assigned FROM DUAL");
            queryArgs[counter] = nextFireTime;
            if (counter != zones.size() - 1) {
                queryForZone.append(" UNION ALL ");
                counter++;
            }
            
        }
        
        LOGGER.debug("final query for zone utilization : {}", queryForZone);

        return simpleJDBCTemplate.query(queryForZone.toString(), queryArgs, new ZoneUtilizationRowMapper());

    }

}
