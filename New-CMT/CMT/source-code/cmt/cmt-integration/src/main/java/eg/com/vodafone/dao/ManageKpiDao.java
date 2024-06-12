package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.NodePropertiesMapper;
import eg.com.vodafone.dao.mapper.SystemNodeRowMapper;
import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.SystemNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

import static eg.com.vodafone.dao.DaoConstants.*;

/**
 * @author marwa.goda
 * @since 4/29/13
 */
@Repository
public class ManageKpiDao {

  private JdbcTemplate jdbcTemplate;
  private final static Logger logger = LoggerFactory.getLogger(ManageKpiDao.class);


  private static final String SIX_COLUMNS_PLACEHOLDERS = " (?, ?, ?, ?, ?, ?) ";
  private static final String FOUR_COLUMNS_PLACEHOLDERS = " (?, ?, ?, ?) ";
  private static final String VNODE_TABLE = " VNODE ";
  private static final String MD_SYSTEM_NODE_COLUMN_NAMES = "  in_use = ? ";
  private static final String PROPERTY_NAME = "property_name";
  private static final String IN_USE = " in_use ";
  private static final String SYSTEM_NAME = "SYSTEM_NAME";


  @Autowired
  public ManageKpiDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<NodeProperties> getSystemKpiDetails(long systemNodeId) {

    StringBuffer getNodePropertiesQuery = new StringBuffer().append(SELECT_ALL_CLAUSE)
      .append(MD_NODE_PROPERTY).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK);

    List<NodeProperties> nodePropertiesList = null;
    try {
      nodePropertiesList = jdbcTemplate.query(getNodePropertiesQuery.toString(),
        new Object[]{systemNodeId}, new NodePropertiesMapper());

    } catch (EmptyResultDataAccessException exception) {
      logger.error("ManageKpiNotificationsDao.getSystemKpiDetails: No Result returned", exception);
      nodePropertiesList = null;
    }
    return nodePropertiesList;
  }

  public int addSystemKpiDetails(NodeProperties nodeProperties) {

    StringBuffer addNodePropertiesQuery = new StringBuffer().append(INSERT_CLAUSE)
      .append(MD_NODE_PROPERTY).append(VALUES).append(SIX_COLUMNS_PLACEHOLDERS);
    //String grain = mapGrainValue(nodeProperties.getGrain());
    int affectedRows = jdbcTemplate.update(addNodePropertiesQuery.toString(), new Object[]{
      nodeProperties.getNodeId(), nodeProperties.getPropertyName(), nodeProperties.getGrain(),
      nodeProperties.getValue(), nodeProperties.getTrafficTableName(), nodeProperties.getNotificationThreshold()
    });

    return affectedRows;
  }


  public int updateSystemNode(SystemNode systemNode) {
    StringBuffer updateSystemNodeQuery = new StringBuffer().append(UPDATE)
      .append(MD_SYSTEM_NODE).append(SET).append(MD_SYSTEM_NODE_COLUMN_NAMES).append(WHERE)
      .append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK);

    int affectedRows = jdbcTemplate.update(updateSystemNodeQuery.toString(), new Object[]{
      systemNode.getInUse(), systemNode.getSystemNodeId()
    });
    return affectedRows;
  }

  public int deleteSystemNodeProperties(int nodId, String propertyName) {
    StringBuffer deleteSystemNodesPropertiesQuery = new StringBuffer().append(DELETE_FROM_CLAUSE)
      .append(MD_NODE_PROPERTY).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK)
      .append(AND).append(PROPERTY_NAME).append(LIKE).append(QUESTION_MARK);

    int affectedRows = jdbcTemplate.update(deleteSystemNodesPropertiesQuery.toString(),
      new Object[]{nodId, propertyName});
    return affectedRows;
  }

  public String isSystemNodeInUse(long nodeId) {
    StringBuffer isSystemNodeInUseQuery = new StringBuffer().append(SELECT).append(IN_USE).append(FROM)
      .append(MD_SYSTEM_NODE).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK);
    String inUseValue = null;
    try {
      inUseValue = jdbcTemplate.queryForObject(isSystemNodeInUseQuery.toString(), new Object[]{nodeId}, String.class);
    } catch (EmptyResultDataAccessException exception) {
      logger.error("ManageKpiNotificationsDao.isSystemNodeInUse: No Result returned", exception);

    }
    return inUseValue;
  }

  public SystemNode getSystemNodeById(long nodeId) {
    StringBuffer getSystemNodeByIdQuery = new StringBuffer(SELECT_ALL_CLAUSE).append(MD_SYSTEM_NODE)
      .append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK);

    SystemNode systemNode = null;
    try {
      systemNode = jdbcTemplate.queryForObject(getSystemNodeByIdQuery.toString(),
        new Object[]{nodeId}, new SystemNodeRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("ManageKpiNotificationsDao.getSystemNodeById: No Result returned", exception);

    }
    return systemNode;
  }

  public int deleteAllSystemNodesProperties(long nodeId) {
    StringBuffer deleteAllSystemNodesPropertiesQuery = new StringBuffer().append(DELETE_FROM_CLAUSE)
      .append(MD_NODE_PROPERTY).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK);


    int affectedRows = jdbcTemplate.update(deleteAllSystemNodesPropertiesQuery.toString(),
      new Object[]{nodeId});
    return affectedRows;
  }

  public NodeProperties getNodePropertiesByIdAndName(int nodeId, String propertyName) {

    NodeProperties nodeProperties = null;

    StringBuffer getNodePropertiesByIdAndNameQuery = new StringBuffer(SELECT_ALL_CLAUSE)
      .append(MD_NODE_PROPERTY).append(WHERE).append(SYSTEM_NODE_ID).append(EQUALS).append(QUESTION_MARK)
      .append(AND).append(PROPERTY_NAME).append(EQUALS).append(QUESTION_MARK);

    try {
      nodeProperties = jdbcTemplate.queryForObject(getNodePropertiesByIdAndNameQuery.toString(),
        new Object[]{nodeId, propertyName}, new NodePropertiesMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("ManageKpiNotificationsDao.getNodePropertiesByIdAndName: No Result returned", exception);

    }
    return nodeProperties;
  }

  public List<SystemNode> getSystemNodesBySystemName(String systemName) {
    StringBuffer getSystemNodesBySystemNameQuery = new StringBuffer(SELECT_ALL_CLAUSE).append(MD_SYSTEM_NODE)
      .append(WHERE).append(SYSTEM_NAME).append(EQUALS).append(QUESTION_MARK);

    List<SystemNode> systemNodes = null;
    try {
      systemNodes = jdbcTemplate.query(getSystemNodesBySystemNameQuery.toString(),
        new Object[]{systemName}, new SystemNodeRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      logger.error("ManageKpiNotificationsDao.getSystemNodesBySystemName: No Result returned", exception);

    }
    return systemNodes;
  }

  public boolean isNodeHasConfiguredProperties(String systemName, long nodeId) {
    boolean isNodeHasConfiguredProperties = false;
    StringBuffer query = new StringBuffer("select count(property.system_node_id) from md_system_node node," + "" +
      " md_node_property property where " +
      " property.system_node_id = node.system_node_id and node.system_name = ").append(QUESTION_MARK)
      .append(AND).append(" node.system_node_id = ").append(QUESTION_MARK);


    int count = jdbcTemplate.queryForInt(query.toString(),
      new Object[]{systemName, nodeId});

    if (count > 0) {
      isNodeHasConfiguredProperties = true;
    } else {
      isNodeHasConfiguredProperties = false;
    }
    return isNodeHasConfiguredProperties;
  }

    public boolean isTableExist(String tableName) {
        if(!StringUtils.hasText(tableName)){
            logger.error("ManageKpiNotificationsDao.isTableExist: empty table name!");
            return false;
        }
        StringBuffer isTableExistQuery = new StringBuffer("SELECT COUNT(OBJECT_ID) FROM ALL_OBJECTS ")
                .append(" WHERE OBJECT_TYPE = 'TABLE' AND OBJECT_NAME= ? ");
        int existCount = 0;
        try {
            existCount = jdbcTemplate.queryForInt(isTableExistQuery.toString(),
                    new Object[]{tableName.toUpperCase()});
        } catch (EmptyResultDataAccessException exception) {
            logger.error("ManageKpiNotificationsDao.isTableExist: No Result returned", exception);

        } finally {
           return existCount>0;
        }
    }

}
