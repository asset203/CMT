/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.model;

import java.io.Serializable;

/**
 *
 * @author mahmoud.awad
 */
public class ZoneUtilization implements Serializable{
    
    private String zoneName;
    private long zoneJobs;
    private long runningZoneJobs;
    private long jobsTORun;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public long getZoneJobs() {
        return zoneJobs;
    }

    public void setZoneJobs(long zoneJobs) {
        this.zoneJobs = zoneJobs;
    }

    public long getRunningZoneJobs() {
        return runningZoneJobs;
    }

    public void setRunningZoneJobs(long runningZoneJobs) {
        this.runningZoneJobs = runningZoneJobs;
    }

    public long getJobsTORun() {
        return jobsTORun;
    }

    public void setJobsTORun(long jobsTORun) {
        this.jobsTORun = jobsTORun;
    }
    
    
}
