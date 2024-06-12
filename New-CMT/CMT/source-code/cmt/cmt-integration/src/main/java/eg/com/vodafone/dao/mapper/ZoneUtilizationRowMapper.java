/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.ZoneUtilization;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author mahmoud.awad
 */
public class ZoneUtilizationRowMapper implements RowMapper<ZoneUtilization>{

    @Override
    public ZoneUtilization mapRow(ResultSet rs, int i) throws SQLException {
        
        ZoneUtilization jobUtilization = new ZoneUtilization();
        jobUtilization.setZoneName(rs.getString("ZONE"));
        jobUtilization.setRunningZoneJobs(rs.getLong("RUNNING"));
        jobUtilization.setJobsTORun(rs.getLong("NEXT_RUN"));
        jobUtilization.setZoneJobs(rs.getLong("ASSIGNED"));
        
        return jobUtilization;
      
    } 
    
    
}
