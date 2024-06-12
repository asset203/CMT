package com.itworx.vaspp.datacollection.dao.mapper;

import com.itworx.vaspp.datacollection.objects.MdKpiContact;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author marwa.goda
 * @since 5/8/13
 */
public class MdKpiContactRowMapper {
  private static final String NODE_ID = "NODE_ID";
  private static final String ID = "ID";
  private static final String EXISTING_USR = "IS_EXISTING_USER";
  private static final String EXTERNAL_CONTACT_ID = "EXTERNAL_CONTACTS_ID";
  private static final String REGISTERED_CONTACT_ID = "REGISTERED_CONTACT_ID";
  private static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";

  public MdKpiContact mapRow(ResultSet resultSet) throws SQLException {
    MdKpiContact mdKpiContact = new MdKpiContact();

    mdKpiContact.setNodeId(resultSet.getInt(NODE_ID));
    mdKpiContact.setId(resultSet.getInt(ID));
    mdKpiContact.setExistingUser(resultSet.getString(EXISTING_USR));
    mdKpiContact.setExternalContactId(resultSet.getInt(EXTERNAL_CONTACT_ID));
    mdKpiContact.setRegisteredContactId(resultSet.getInt(REGISTERED_CONTACT_ID));
    mdKpiContact.setNotificationType(resultSet.getString(NOTIFICATION_TYPE));

    return mdKpiContact;
  }
}
