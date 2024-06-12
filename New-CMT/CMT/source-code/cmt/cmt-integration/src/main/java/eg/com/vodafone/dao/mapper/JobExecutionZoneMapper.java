package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.JobExecutionZone;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author marwa.goda
 * @since 4/23/13
 */
public class JobExecutionZoneMapper implements RowMapper<JobExecutionZone> {
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String IP = "IP";

    @Override
    public JobExecutionZone mapRow(ResultSet resultSet, int i) throws SQLException {
        JobExecutionZone jobZone = new JobExecutionZone();

        jobZone.setId(resultSet.getInt(ID));
        jobZone.setName(resultSet.getString(NAME));
        jobZone.setIp(resultSet.getString(IP));
        jobZone.setPort(resultSet.getInt("PORT"));
        jobZone.setUserName(resultSet.getString("USERNAME"));
        jobZone.setPassword(resultSet.getString("PASSWORD"));
        jobZone.setDcPath(resultSet.getString("DC_PATH"));
        jobZone.setQrtzTblPrefix(resultSet.getString("QRTZ_TABLE_PREFIX"));

        return jobZone;
    }
}
