package com.itworx.vaspp.datacollection.dao.mapper;

import eg.com.vodafone.model.NodeProperties;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author marwa.goda
 * @since 5/9/13
 */
public class NodePropertiesMapper {


  public static final String SYSTEM_NODE_ID = "system_node_id";
  public static final String PROPERTY_NAME = "property_name";
  public static final String GRAIN = "grain";
  public static final String VALUE = "value";
  public static final String TRAFFIC_TABLE_NAME = "traffic_table_name";
  public static final String NOTIFICATION_THRESHOLD = "notification_threshold";


  public NodeProperties mapRow(ResultSet resultSet) throws SQLException {

    NodeProperties nodeProperties = new NodeProperties();

    nodeProperties.setNodeId(resultSet.getInt(SYSTEM_NODE_ID));
    nodeProperties.setPropertyName(resultSet.getString(PROPERTY_NAME));
    nodeProperties.setGrain(resultSet.getString(GRAIN));
    nodeProperties.setValue(resultSet.getLong(VALUE));
    nodeProperties.setTrafficTableName(resultSet.getString(TRAFFIC_TABLE_NAME));
    nodeProperties.setNotificationThreshold(Double.parseDouble(resultSet.getString(NOTIFICATION_THRESHOLD)));

    return nodeProperties;
  }
}
