package com.itworx.vaspp.datacollection.dao.mapper;

import eg.com.vodafone.model.SystemNode;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author marwa.goda
 * @since 5/9/13
 */
public class SystemNodeRowMapper {


  public SystemNode mapRow(ResultSet resultSet) throws SQLException {
    SystemNode systemNode = new SystemNode();
    systemNode.setSystemNodeId(resultSet.getLong("SYSTEM_NODE_ID"));
    systemNode.setSystemName(resultSet.getString("SYSTEM_NAME"));
    systemNode.setNodeName(resultSet.getString("NODE_NAME"));
    systemNode.setInUse(resultSet.getString("IN_USE"));
    systemNode.setDescription(resultSet.getString("DESCRIPTION"));

    return systemNode;
  }
}
