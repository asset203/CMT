package com.itworx.vaspp.datacollection.objects;

/**
 * @author marwa.goda
 * @since 5/8/13
 */
public class NotificationUserContact {

  String notificationType;
  String email;


  String mobileNumber;

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
}
