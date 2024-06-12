package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.VInputRowMapper;
import eg.com.vodafone.dao.mapper.VNodeRowMapper;
import eg.com.vodafone.dao.mapper.VSystemRowMapper;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eg.com.vodafone.dao.DaoConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Author: basma.alkerm
 * Date  : 3/13/13
 * Time  : 9:41 AM
 */
@Repository
public class SystemDao {


  private final SimpleJdbcInsert insertInputInputStructs;
  private final SimpleJdbcInsert insertNode;
  private final SimpleJdbcInsert insertPath;
  private final SimpleJdbcInsert insertSystem;
  private final NamedParameterJdbcTemplate queryTemplate;


  private final static Logger logger = LoggerFactory.getLogger(SystemDao.class);

  @Autowired
  public SystemDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {

    insertSystem = new SimpleJdbcInsert(dataSource)
      .withTableName(SYSTEMS_TABLE_NAME);

    insertNode = new SimpleJdbcInsert(dataSource)
      .withTableName(NODES_TABLE_NAME)
      .usingGeneratedKeyColumns("SYSTEM_NODE_ID");

    insertPath = new SimpleJdbcInsert(dataSource)
      .withTableName("INPUT_PATH")
      .usingGeneratedKeyColumns("PATH_ID");

    insertInputInputStructs = new SimpleJdbcInsert(dataSource)
      .withTableName(INPUT_INPUT_STRUCTRUES_TABLE_NAME);

    queryTemplate = new NamedParameterJdbcTemplate(dataSource);
  }


  public void saveSystemNameAndDescription(String systemName, String description) {
    //1- insert system
    logger.debug("Saving System name{}", systemName);
    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put("NAME", systemName.trim());
    params.put("DESCRIPTION", description);
    insertSystem.execute(params);
    logger.debug("System {} successfully saved", systemName);
  }

  public void saveNodes(List<VNode> nodes) {

    for (int i = 0; i < nodes.size(); i++) {
      long id = saveNodeNameAndDescription(nodes.get(i));
      List<VInput> nodeInputs = nodes.get(i).getInputsList();
      saveInputs(nodeInputs, id);
    }
  }

  public long saveNodeNameAndDescription(VNode node) {
    logger.debug("saving node {}", node.getName());
    Map<String, Object> nodeParameters = new HashMap<String, Object>(3);
    nodeParameters.put("SYSTEM_NAME", node.getSystemName().trim());
    nodeParameters.put("NODE_NAME", node.getName().trim());
    nodeParameters.put("DESCRIPTION", node.getDescription());
    nodeParameters.put("IN_USE", "Y");//by defualt set 'in use' flag to true for new nodes
    Number nodeId = insertNode.executeAndReturnKey(nodeParameters);
    logger.debug("node {} saved with id {}", node.getName(), nodeId.longValue());
    return nodeId.longValue();
  }

  public void saveInputs(List<VInput> inputs, long nodeId) {

    for (int i = 0; i < inputs.size(); i++) {
      inputs.get(i).setNodeId(nodeId);
      if (nodeId > 0) {
        inputs.get(i).setPerNode(true);
      }
      saveInput(inputs.get(i));
    }
  }

  public void saveInput(VInput input) {
    logger.debug("saving Input {} , node {}", input.getId(), input.getNodeName());
    MapSqlParameterSource inputParameters = new MapSqlParameterSource("SYSTEM_NAME", input.getSystemName().trim());
    inputParameters.addValue("NODE_ID", input.getNodeId());
    inputParameters.addValue("NODE_NAME", StringUtils.hasText(input.getNodeName())?input.getNodeName().trim():"");
    inputParameters.addValue("PER_NODE", input.isPerNode() ? "Y" : "N");
    inputParameters.addValue("ORIGINAL_INPUT_NAME", input.getOriginalInputName());
    inputParameters.addValue("INPUT_NAME", input.getInputName().trim());
    inputParameters.addValue("HOURLY_NAME", input.getInputName());
    inputParameters.addValue("SERVER", input.getServer());
    inputParameters.addValue("USER_NAME", input.getUser());
    inputParameters.addValue("PSWD", input.getPassword());
    inputParameters.addValue("ACCESS_METHOD", input.getAccessMethod());
    inputParameters.addValue("INPUT_TYPE", input.getType());
    inputParameters.addValue("ID", input.getId());
    StringBuffer query = new StringBuffer("INSERT INTO ")
      .append(INPUTS_TABLE_NAME)
      .append("(NODE_NAME,SYSTEM_NAME,NODE_ID,PER_NODE,ORIGINAL_INPUT_NAME,INPUT_NAME,HOURLY_NAME,SERVER,USER_NAME,PSWD,ACCESS_METHOD,INPUT_TYPE,ID) ")
      .append("VALUES (:NODE_NAME,:SYSTEM_NAME,:NODE_ID,:PER_NODE,:ORIGINAL_INPUT_NAME,:INPUT_NAME,:HOURLY_NAME,:SERVER,:USER_NAME,:PSWD,:ACCESS_METHOD,:INPUT_TYPE,:ID)");
    queryTemplate.update(query.toString(), inputParameters);
    //TODO:ask about index out of Bound exception
    //Number inputId = insertInput.executeAndReturnKey(inputParameters);
    long inputId = getInputId(input.getId(), input.getNodeName(), input.getSystemName());
    input.setInputId(inputId);
    saveInputPaths(inputId, input.getPathsList());
    logger.debug("input paths saved");
    List<String> inputStructures = input.getInputStructureIds();
    saveInputStructuresToInput(inputId, inputStructures);

  }

  private void saveInputPaths(long inputId, List<String> inputPaths) {
    if (inputPaths == null || inputPaths.isEmpty()) {
      logger.debug("no paths for input  id {} to insert", inputId);
      return;
    }
    logger.debug("adding paths for input id {}", inputId);
    for (int x = 0; x < inputPaths.size(); x++) {
      Map<String, Object> pathParameters = new HashMap<String, Object>(2);
      pathParameters.put("INPUT_ID", inputId);
      pathParameters.put("PATH", inputPaths.get(x));
      insertPath.execute(pathParameters);
    }
  }

  private void saveInputStructuresToInput(Long inputId, List<String> inputStructureIds) {
    if (inputStructureIds == null || inputStructureIds.isEmpty()) {
      logger.debug("no input structures for input  id {} to insert", inputId);
      return;
    }
    //1-insert to "INPUT_INPUT_STRUCTURES" table
    Map<String, Object> inputInputStructsParams;
    logger.debug("adding input_structures to Input{}", inputId);
    for (int i = 0; i < inputStructureIds.size(); i++) {
      inputInputStructsParams = new HashMap<String, Object>(2);
      inputInputStructsParams.put("INPUT_STRUCT_ID", inputStructureIds.get(i));
      inputInputStructsParams.put("INPUT_ID", inputId);
      logger.debug(" adding input structure {}", inputStructureIds.get(i));
      insertInputInputStructs.execute(inputInputStructsParams);
    }
  }

  public boolean isSystemNameUsed(String systemName) {
    logger.debug("check system name {} is used", systemName);
    StringBuffer query = new StringBuffer("SELECT COUNT(NAME)  FROM ")
      .append(SYSTEMS_TABLE_NAME)
      .append(" WHERE upper(NAME) = upper(:NAME) ");
    MapSqlParameterSource params = new MapSqlParameterSource("NAME", systemName.trim());
    int isExist = queryTemplate.queryForInt(query.toString(), params);
    return (isExist > 0);
  }

  public List<String> getAllSystems() {

    return queryTemplate.queryForList("SELECT NAME  FROM " + SYSTEMS_TABLE_NAME, new MapSqlParameterSource(), String.class);
  }

  public VSystem getSystem(String systemName) {
    logger.debug("getting system {} ", systemName);
    StringBuffer queryStr = new StringBuffer("SELECT NAME , DESCRIPTION FROM ")
      .append(SYSTEMS_TABLE_NAME)
      .append(" WHERE upper(NAME) = upper(:NAME)");
    MapSqlParameterSource params = new MapSqlParameterSource("NAME", systemName.trim());

    VSystem system;
    try {

      system = queryTemplate.queryForObject(queryStr.toString(), params, new VSystemRowMapper());
    } catch (EmptyResultDataAccessException e) {
      logger.debug("system with name {} was not found", systemName);
      return null;
    }
    return system;
  }

  public void updateSystemDescription(String systemName, String description) {
    logger.debug("updating description for system{}", systemName);
    SqlParameterSource namedParameters = new MapSqlParameterSource("NAME", systemName.trim())
      .addValue("DESCRIPTION", description);
    StringBuffer queryStr = new StringBuffer(" UPDATE ").append(SYSTEMS_TABLE_NAME)
      .append(" SET DESCRIPTION = :DESCRIPTION")
      .append(" WHERE upper(NAME) = upper(:NAME)");
    queryTemplate.update(queryStr.toString(), namedParameters);
  }

  public void deleteSystem(String systemName) {
    VSystem systemToDelete = getSystem(systemName);
    deleteSystem(systemToDelete);
  }

  public void deleteSystem(VSystem system) {
    logger.debug("delete system {} ", system.getName());
    SqlParameterSource namedParameters = new MapSqlParameterSource("NAME", system.getName().trim());
    StringBuffer queryStr = new StringBuffer(" DELETE FROM ").append(SYSTEMS_TABLE_NAME)
      .append(" WHERE upper(NAME) = upper(:NAME)");
    queryTemplate.update(queryStr.toString(), namedParameters);
  }

  public List<VNode> getSystemNodes(String systemName, Boolean activeNodesOnly) {
    logger.debug("getting nodes for system {}", systemName);
    StringBuffer queryStr = new StringBuffer("SELECT * FROM ").append(NODES_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim());
      if(activeNodesOnly){
          queryStr.append(" AND IN_USE =:IN_USE");
          namedParameters.addValue("IN_USE","Y") ;
      }
    List<VNode> nodes = queryTemplate.query(queryStr.toString(), namedParameters, new VNodeRowMapper());
    for (int i = 0; i < nodes.size(); i++) {
      List<VInput> inputs = getInputs(systemName, nodes.get(i).getId());
      nodes.get(i).setInputsList(inputs);
    }
    logger.debug("number fo nodes = {}", nodes.size());
    return nodes;
  }

  public VNode getNode(String systemName, String nodeName) {
    logger.debug("getting node {} ,system {} ", nodeName, systemName);
    StringBuffer sql = new StringBuffer("SELECT * FROM ").append(NODES_TABLE_NAME)
      .append(" WHERE upper(NODE_NAME) = upper(:NODE_NAME) AND upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("NODE_NAME", nodeName.trim())
            .addValue("SYSTEM_NAME", systemName.trim());

    VNode node;
    try {
      node = queryTemplate.queryForObject(sql.toString(), namedParameters, new VNodeRowMapper());
    } catch (EmptyResultDataAccessException exception) {
      return null;
    }
    // List<VInput> inputs = getInputs(systemName,node.getId());
    //node.setInputsList(inputs);
    return node;
  }

  public long getNodeId(String systemName, String nodeName) {
    logger.debug("getting node {} ,system {} ", nodeName, systemName);
    StringBuffer sql = new StringBuffer("SELECT SYSTEM_NODE_ID  FROM ").append(NODES_TABLE_NAME)
      .append(" WHERE upper(NODE_NAME) = upper(:NODE_NAME) AND upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("NODE_NAME", nodeName.trim())
            .addValue("SYSTEM_NAME", systemName.trim());

    long nodeId;
    try {
      nodeId = queryTemplate.queryForLong(sql.toString(), namedParameters);
    } catch (EmptyResultDataAccessException exception) {
      return -1;
    }
    // List<VInput> inputs = getInputs(systemName,node.getId());
    //node.setInputsList(inputs);
    return nodeId;
  }

  public void updateNode(long nodeId, String newName, String newDescription) {
    logger.debug("updating node {},new name {}", nodeId, newName);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NODE_ID", nodeId)
      .addValue("NODE_NAME", newName.trim())
      .addValue("DESCRIPTION", newDescription);
    StringBuffer queryStr = new StringBuffer("UPDATE ").append(NODES_TABLE_NAME)
      .append(" SET NODE_NAME = :NODE_NAME , DESCRIPTION = :DESCRIPTION ")
      .append(" WHERE SYSTEM_NODE_ID = :SYSTEM_NODE_ID");
    queryTemplate.update(queryStr.toString(), namedParameters);
  }

  public void updateNodeInputs(long nodeId, String newName) {
    logger.debug("updating inputs node name {}", newName);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("NODE_ID", nodeId)
      .addValue("NODE_NAME", newName.trim());
    StringBuffer queryStr = new StringBuffer("UPDATE ").append(INPUTS_TABLE_NAME)
      .append(" SET NODE_NAME = :NODE_NAME ")
      .append(" WHERE NODE_ID = :NODE_ID");
    queryTemplate.update(queryStr.toString(), namedParameters);
  }

  public void deleteNode(VNode node) {
    if (node == null) {
      logger.debug("delete node called with node = null");
      return;
    }
    logger.debug("delete node {} of system {} ", node.getName(), node.getSystemName());
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME", node.getSystemName().trim())
      .addValue("NODE_NAME", node.getName().trim());
    StringBuffer queryStr = new StringBuffer("DELETE FROM ").append(NODES_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME) ")
      .append(" AND upper(NODE_NAME) = upper(:NODE_NAME) ");
    queryTemplate.update(queryStr.toString(), namedParameters);
  }

  public VInput getInput(String inputId, String nodeName, String systemName) {
    logger.debug("get input {} ,for node {}", inputId, nodeName);
    StringBuffer sql = new StringBuffer("SELECT * FROM ").append(INPUTS_TABLE_NAME)
            .append(" WHERE ID = :ID AND upper(NODE_NAME) = upper(:NODE_NAME) AND upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME", systemName)
      .addValue("NODE_NAME", nodeName)
      .addValue("ID", inputId);
    try {
      return queryTemplate.queryForObject(sql.toString(), namedParameters, new VInputRowMapper());
    } catch (EmptyResultDataAccessException e) {
      logger.debug("input was not found");
      return null;
    }
  }

  public long getInputId(String inputId, String nodeName, String systemName) {
    logger.debug("get input Id,input name = {}", inputId);
    StringBuffer sql = new StringBuffer("SELECT INPUT_ID FROM ").append(INPUTS_TABLE_NAME)
            .append(" WHERE ID =:ID  AND upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim())
      .addValue("ID", inputId);
    if (StringUtils.isEmpty(nodeName)) {
      sql.append(" AND NODE_NAME IS NULL");
    } else {
      sql.append(" AND upper(NODE_NAME)= upper(:NODE_NAME)");
      namedParameters.addValue("NODE_NAME", nodeName.trim());
    }
    try {

      return queryTemplate.queryForLong(sql.toString(), namedParameters);
    } catch (EmptyResultDataAccessException e) {
      logger.debug("long with name {} was not found", inputId);
      return -1;
    }

  }

  /* public List<VInput> getInputs(String systemName,long nodeId){
    logger.debug("get inputs for node {},system {}",nodeId,systemName);
    String nodeCondition="";
    if(nodeId == -1){
        nodeCondition= " AND NODE_ID IS NULL";

    }
    else
    {
        nodeCondition = " AND NODE_ID = "+nodeId;
    }
    StringBuffer sql = new StringBuffer(" SELECT * FROM ")
            .append(INPUTS_TABLE_NAME)
            .append(" WHERE SYSTEM_NAME = :SYSTEM_NAME ")
            .append(nodeCondition);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME",systemName);
    List<VInput> inputs = queryTemplate.query(sql.toString() ,namedParameters,new VInputRowMapper());

    for(int i=0;i<inputs.size();i++){
          inputs.get(i).setPathsList(getInputPaths(inputs.get(i).getInputId()));
          inputs.get(i).setInputStructureIds(getInputInputStructures(inputs.get(i).getInputId()));
    }
    return inputs;
}  */
  public List<VInput> getInputs(String systemName, long nodeId) {
    logger.debug("get inputs for node {},system {}", nodeId, systemName);
    String nodeCondition = " AND NODE_ID = " + nodeId;
    /* if(nodeId == -1){
        nodeCondition= " AND NODE_ID IS NULL";

    }
    else
    {
        nodeCondition = " AND NODE_ID = "+nodeId;
    }*/
    StringBuffer sql = new StringBuffer(" SELECT * FROM ")
      .append(INPUTS_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME) ")
      .append(nodeCondition);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim());
    List<VInput> inputs = queryTemplate.query(sql.toString(), namedParameters, new VInputRowMapper());

    for (int i = 0; i < inputs.size(); i++) {
      inputs.get(i).setPathsList(getInputPaths(inputs.get(i).getInputId()));
      inputs.get(i).setInputStructureIds(getInputInputStructures(inputs.get(i).getInputId()));
    }
    return inputs;
  }

  private List<String> getInputPaths(long inputId) {
    logger.debug("get paths fro input {}", inputId);
    StringBuffer query = new StringBuffer("SELECT PATH FROM ").append(INPUT_PATHS_TABLE_NAME)
      .append(" WHERE INPUT_ID = :INPUT_ID ");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("INPUT_ID", inputId);
    return queryTemplate.queryForList(query.toString(), namedParameters, String.class);
  }

  private List<String> getInputInputStructures(long inputId) {
    StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID FROM ")
      .append(INPUT_INPUT_STRUCTRUES_TABLE_NAME)
      .append(" WHERE INPUT_ID = :INPUT_ID");
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("INPUT_ID", inputId);
    return queryTemplate.queryForList(query.toString(), namedParameters, String.class);

  }

  public void updateInput(VInput inputToUpdate) {
    logger.debug("update input {},id= {}", inputToUpdate.getId(), inputToUpdate.getInputId());
    MapSqlParameterSource params = new MapSqlParameterSource("INPUT_ID", inputToUpdate.getInputId())
      .addValue("ID", inputToUpdate.getId())
      .addValue("INPUT_NAME", inputToUpdate.getInputName().trim())
      .addValue("ORIGINAL_INPUT_NAME", inputToUpdate.getOriginalInputName())
      .addValue("HOURLY_NAME", inputToUpdate.getInputName())
      .addValue("ACCESS_METHOD", inputToUpdate.getAccessMethod())
      .addValue("SERVER", inputToUpdate.getServer())
      .addValue("USER_NAME", inputToUpdate.getUser())
      .addValue("PSWD", inputToUpdate.getPassword());

    StringBuffer query = new StringBuffer("UPDATE ").append(INPUTS_TABLE_NAME).append(" SET ")
      .append("ID = :ID ,")
      .append("INPUT_NAME = :INPUT_NAME,")
      .append("ORIGINAL_INPUT_NAME = :ORIGINAL_INPUT_NAME ,")
      .append("HOURLY_NAME = :HOURLY_NAME,")
      .append("ACCESS_METHOD = :ACCESS_METHOD,")
      .append("SERVER = :SERVER,")
      .append("USER_NAME = :USER_NAME,")
      .append("PSWD = :PSWD ")
      .append(" WHERE INPUT_ID = :INPUT_ID");
    queryTemplate.update(query.toString(), params);

    updateInputPaths(inputToUpdate.getInputId(), inputToUpdate.getPathsList());

    updateInputInputStructs(inputToUpdate.getInputId(), inputToUpdate.getInputStructureIds());

  }

  private void updateInputPaths(long inputId, List<String> paths) {
    deleteInputPaths(inputId);
    saveInputPaths(inputId, paths);

  }

  private void updateInputInputStructs(long inputId, List<String> inputStructuresIds) {
    deleteInputInputStructures(inputId);
    saveInputStructuresToInput(inputId, inputStructuresIds);
  }

  public void removeInputStructureFromInput(String inputStructureId, long inputId) {

    MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId).addValue("INPUT_ID", inputId);
    StringBuffer query = new StringBuffer("DELETE FROM ").append(INPUT_INPUT_STRUCTRUES_TABLE_NAME)
      .append(" WHERE INPUT_ID = :INPUT_ID")
      .append(" AND upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
    queryTemplate.update(query.toString(), params);
  }

  public void deleteInput(long inputId) {
    logger.debug("delete input {} ", inputId);
    //1- delete input paths
    deleteInputPaths(inputId);

    //2- delete from input_input_structures
    deleteInputInputStructures(inputId);

    //3- delete from input
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("INPUT_ID", inputId);
    StringBuffer query = new StringBuffer("DELETE FROM ").append(INPUTS_TABLE_NAME).append(" WHERE INPUT_ID = :INPUT_ID");
    queryTemplate.update(query.toString(), namedParameters);

  }

  private void deleteInputPaths(long inputId) {
    logger.debug("delete paths for input {}", inputId);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("INPUT_ID", inputId);
    StringBuffer query = new StringBuffer("DELETE FROM ").append(INPUT_PATHS_TABLE_NAME).append(" WHERE INPUT_ID= :INPUT_ID ");
    queryTemplate.update(query.toString(), namedParameters);
  }

  private void deleteInputInputStructures(long inputId) {
    logger.debug("delete input structures for input {}", inputId);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("INPUT_ID", inputId);
    StringBuffer query = new StringBuffer("DELETE FROM ").append(INPUT_INPUT_STRUCTRUES_TABLE_NAME)
            .append(" WHERE INPUT_ID= :INPUT_ID");
    queryTemplate.update(query.toString(), namedParameters);
  }

  public void deleteInputs(List<VInput> inputs) {
    if (inputs.isEmpty()) {
      return;
    }
    if (inputs.size() > 100) {
      for (int j = 0; j < inputs.size(); j++) {
        deleteInput(inputs.get(j).getInputId());
      }
      return;
    }
    List<Long> ids = new ArrayList<Long>();
    for (int i = 0; i < inputs.size(); i++) {
      ids.add(inputs.get(i).getInputId());
    }

    //1- delete input paths
    MapSqlParameterSource namedParameters = new MapSqlParameterSource("IDs", ids);
    StringBuffer deletePath = new StringBuffer("DELETE FROM ").append(INPUT_PATHS_TABLE_NAME).append(" WHERE INPUT_ID IN (:IDs) ");
    queryTemplate.update(deletePath.toString(), namedParameters);

    //2- delete from input_input_structures
    StringBuffer deleteInputIds = new StringBuffer("DELETE FROM ").append(INPUT_INPUT_STRUCTRUES_TABLE_NAME).append(" WHERE INPUT_ID IN (:IDs)");
    queryTemplate.update(deleteInputIds.toString(), namedParameters);

    //3- delete from input
    StringBuffer deleteInput = new StringBuffer("DELETE FROM ").append(INPUTS_TABLE_NAME).append(" WHERE INPUT_ID IN (:IDs)");
    queryTemplate.update(deleteInput.toString(), namedParameters);

  }

  public List<String> searchSystems(String keyword, int startIndex, int endIndex) {

    if(keyword.indexOf('_') > -1){
      keyword = keyword.replace("_","\\_");
    }
    StringBuffer query = new StringBuffer("SELECT NAME FROM ( SELECT NAME ,")
      .append("DENSE_RANK() OVER( ORDER BY NAME ) AS RANK FROM ").append(SYSTEMS_TABLE_NAME)
      .append(" WHERE upper(NAME) like upper(:KEY_WORD)");
      if(keyword.indexOf('_') > -1){
          query.append(" ESCAPE '\\' ");
      }
      query.append(" ) WHERE RANK BETWEEN :START_IDX AND :END_IDX");

    MapSqlParameterSource params = new MapSqlParameterSource("KEY_WORD", '%' + keyword.trim() + '%')
      .addValue("START_IDX", startIndex).addValue("END_IDX", endIndex);

    return queryTemplate.queryForList(query.toString(), params, String.class);
  }

  public List<String> searchSystems(String keyword) {
    if(keyword.indexOf('_') > -1){
        keyword = keyword.replace("_","\\_");
    }
    StringBuffer query = new StringBuffer("SELECT NAME FROM ").append(SYSTEMS_TABLE_NAME)
      .append(" WHERE upper(NAME) like upper(:KEY_WORD) ");
    if(keyword.indexOf('_') > -1){
        query.append(" ESCAPE '\\' ");
    }
    MapSqlParameterSource params = new MapSqlParameterSource("KEY_WORD", '%' + keyword.trim() + '%');
    return queryTemplate.queryForList(query.toString(), params, String.class);
  }

  public List<String> listSystems(int startIndex, int endIndex) {
    StringBuffer query = new StringBuffer("SELECT NAME FROM ( SELECT NAME ,")
      .append("DENSE_RANK() OVER( ORDER BY NAME ) AS RANK FROM ").append(SYSTEMS_TABLE_NAME)
      .append(" ) WHERE RANK BETWEEN :START_IDX AND :END_IDX");
    MapSqlParameterSource params = new MapSqlParameterSource("END_IDX", endIndex).addValue("START_IDX", startIndex);
    return queryTemplate.queryForList(query.toString(), params, String.class);
  }

  public int countSystemNodes(String systemName) {
    StringBuffer query = new StringBuffer("SELECT COUNT (NODE_NAME) FROM ").append(NODES_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME)");
    MapSqlParameterSource params = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim());
    try {

      return queryTemplate.queryForInt(query.toString(), params);
    } catch (EmptyResultDataAccessException e) {
      logger.debug("Failed to get nodes count for system {}", systemName);
      throw new DataRetrievalFailureException("Failed to get nodes count for system", e);
    }
  }

  public int countSystemInputs(String systemName) {
    StringBuffer query = new StringBuffer("SELECT COUNT (ID) FROM ").append(INPUTS_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME) AND NODE_NAME IS NULL");
    MapSqlParameterSource params = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim());
    try {

      return queryTemplate.queryForInt(query.toString(), params);
    } catch (EmptyResultDataAccessException e) {
      logger.debug("Failed to get inputs count for system {}", systemName);
      throw new DataRetrievalFailureException("Failed to get inputs count for system", e);
    }
  }

  public int countNodeInputs(String systemName, String nodeName) {
    StringBuffer query = new StringBuffer("SELECT COUNT (ID) FROM ").append(INPUTS_TABLE_NAME)
      .append(" WHERE upper(SYSTEM_NAME) = upper(:SYSTEM_NAME) AND  upper(NODE_NAME) = upper(:NODE_NAME)");
    MapSqlParameterSource params = new MapSqlParameterSource("SYSTEM_NAME", systemName.trim())
      .addValue("NODE_NAME", nodeName.trim());
    try {
      return queryTemplate.queryForInt(query.toString(), params);
    } catch (EmptyResultDataAccessException e) {
      logger.debug("Failed to get inputs count for node {}", nodeName);
      throw new DataRetrievalFailureException("Failed to get inputs count for node", e);
    }
  }


    public int updateNodeStatus(String nodeName,String systemName,String newStatus){
        StringBuffer query = new StringBuffer("UPDATE ").append(NODES_TABLE_NAME)
                .append(" SET  IN_USE = :IN_USE WHERE  SYSTEM_NAME  = :SYSTEM_NAME AND NODE_NAME =:NODE_NAME") ;
        MapSqlParameterSource params = new MapSqlParameterSource("SYSTEM_NAME", systemName)
                .addValue("NODE_NAME",nodeName)
                .addValue("IN_USE",newStatus);
      return  queryTemplate.update(query.toString(),params);
    }
}
