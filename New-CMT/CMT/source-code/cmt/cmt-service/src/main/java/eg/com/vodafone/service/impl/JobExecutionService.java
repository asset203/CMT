package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.ExecutionZoneDao;
import eg.com.vodafone.model.JobExecutionZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author marwa.goda
 * @since 4/23/13
 */
@Service
public class JobExecutionService {

    @Autowired
    private ExecutionZoneDao executionZoneDao;

    /**
     * Retrieve all JobExecutionZone
     *
     * @return Map with Zone ID & Zone details
     */
    public Map<String, JobExecutionZone> getExecutionZones() {
        List<JobExecutionZone> jobExecutionZoneList
                = executionZoneDao.getExecutionZones();
        Map<String, JobExecutionZone> jobExStringListMap
                = new TreeMap<String, JobExecutionZone>();
        if(jobExecutionZoneList != null && !jobExecutionZoneList.isEmpty()){
            for(JobExecutionZone jobExecutionZone : jobExecutionZoneList){
                jobExStringListMap.put(
                        String.valueOf(jobExecutionZone.getId()), jobExecutionZone);
            }
        }
        return jobExStringListMap;
    }

    /**
     * Retrieve all Quartz tables' prefixes
     *
     * @return all Quartz tables' prefixes
     */
    public Map<String, String> getZonesQuartzPrefixes(){
        return executionZoneDao.getZonesQuartzPrefixes();
    }

    public List<String> getZonesID(){
        return executionZoneDao.getZonesID();
    }
}
