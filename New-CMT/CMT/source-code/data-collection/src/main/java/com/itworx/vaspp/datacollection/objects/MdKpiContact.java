package com.itworx.vaspp.datacollection.objects;

/**
 * @author marwa.goda
 * @since 5/8/13
 */
public class MdKpiContact {


  int id;
  int externalContactId;
  int registeredContactId;
  String notificationType;
  String isExistingUser;
  int nodeId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getExternalContactId() {
    return externalContactId;
  }

  public void setExternalContactId(int externalContactId) {
    this.externalContactId = externalContactId;
  }

  public int getRegisteredContactId() {
    return registeredContactId;
  }

  public void setRegisteredContactId(int registeredContactId) {
    this.registeredContactId = registeredContactId;
  }

  public String getExistingUser() {
    return isExistingUser;
  }

  public void setExistingUser(String existingUser) {
    isExistingUser = existingUser;
  }

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public int getNodeId() {
    return nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

}
