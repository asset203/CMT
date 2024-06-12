package eg.com.vodafone.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import eg.com.vodafone.model.KPI;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created with IntelliJ IDEA.
 * User: tarek.moustafa
 * Date: 4/10/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class KPIRowMapper implements RowMapper<KPI> {
    @Override
    public KPI mapRow(ResultSet rs, int line) throws SQLException {
        KPI kpi = new KPI();
        kpi.setSystemNodeID(rs.getInt("SYSTEM_NODE_ID"));
        kpi.setPropertyName(rs.getString("PROPERTY_NAME"));
        kpi.setGrain(rs.getString("GRAIN"));
        kpi.setValue(rs.getLong("VALUE"));
        kpi.setTrafficTableName(rs.getString("TRAFFIC_TABLE_NAME"));
        kpi.setNotificationThreshold(rs.getString("NOTIFICATION_THRESHOLD"));
        return kpi;
    }
}


