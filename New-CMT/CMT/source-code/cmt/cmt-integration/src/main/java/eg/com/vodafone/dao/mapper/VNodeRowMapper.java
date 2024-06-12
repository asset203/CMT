package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.VNode;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class VNodeRowMapper implements RowMapper<VNode> {
  @Override
  public VNode mapRow(ResultSet resultSet, int i) throws SQLException {
    try {
      VNode node = new VNode();
      node.setId(resultSet.getLong("SYSTEM_NODE_ID"));
      node.setName(resultSet.getString("NODE_NAME"));
      node.setSystemName(resultSet.getString("SYSTEM_NAME"));
      node.setDescription(resultSet.getString("DESCRIPTION"));
      node.setInUse(resultSet.getString("IN_USE"));
      return node;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
