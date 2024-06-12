package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.SystemNode;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author marwa.goda
 * @since 4/30/13
 */
public class SystemNodeRowMapper implements RowMapper<SystemNode> {

  @Override
  public SystemNode mapRow(ResultSet resultSet, int i) throws SQLException {
    SystemNode systemNode = new SystemNode();
    systemNode.setSystemNodeId(resultSet.getLong("SYSTEM_NODE_ID"));
    systemNode.setSystemName(resultSet.getString("SYSTEM_NAME"));
    systemNode.setNodeName(resultSet.getString("NODE_NAME"));
    systemNode.setInUse(resultSet.getString("IN_USE"));
    systemNode.setDescription(resultSet.getString("DESCRIPTION"));

    return systemNode;
  }
}
