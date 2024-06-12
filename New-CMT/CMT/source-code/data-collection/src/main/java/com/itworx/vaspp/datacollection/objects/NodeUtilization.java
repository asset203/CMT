package com.itworx.vaspp.datacollection.objects;

import java.util.Date;

/**
 * @author marwa.goda
 * @since 5/8/13
 */
public class NodeUtilization {


  private String trafficTableName;
  private double notificationThreshold;
  private float trafficTableUtilization;
  private String targetTime;

  public void setTrafficTableName(String trafficTableName) {
    this.trafficTableName = trafficTableName;
  }

  public void setNotificationThreshold(double notificationThreshold) {
    this.notificationThreshold = notificationThreshold;
  }

  public void setTrafficTableUtilization(float trafficTableUtilization) {
    this.trafficTableUtilization = trafficTableUtilization;
  }

  public double getNotificationThreshold() {
    return notificationThreshold;
  }

  public float getTrafficTableUtilization() {
    return trafficTableUtilization;
  }

  public String getTrafficTableName() {
    return trafficTableName;
  }

  public String getTargetTime() {
    return targetTime;
  }

  public void setTargetTime(String targetTime) {
    this.targetTime = targetTime;
  }

}
