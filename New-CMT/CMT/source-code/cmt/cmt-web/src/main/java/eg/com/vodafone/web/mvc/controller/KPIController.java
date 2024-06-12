package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.SystemNode;
import eg.com.vodafone.model.enums.KpiNodeGrain;
import eg.com.vodafone.model.enums.NodeInUse;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.ManageKpiService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.model.kpiNotifications.SystemKpiModel;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.util.ControllersUtil;
import eg.com.vodafone.web.mvc.validator.KPIValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author marwa.goda
 * @since 4/29/13
 */
@Controller
@RequestMapping("/manageKpis/*")
public class KPIController extends AbstractController{

  private static final String SYSTEM_KPIS_VIEW = "/kpi/systemKpis";
  private static final String SYSTEM_KPI_DETAILS_VIEW = "/kpi/systemKpiDetails";
  private static final String NODE_PROPERTIES_VIEW = "/kpi/nodeProperties";
  private static final String PROPERTIES_LIST_VIEW = "/kpi/propertiesList";
  private static final String KPI_DETAILS_VIEW = "/kpi/kpiDetails";
  private static final String SYSTEM_NODES_MODEL_ATTRIBUTE = "nodeList";
  private static final String SYSTEM_KPI_MODEL_ATTRIBUTE = "formBean";
  private static final String NODE_PROPERTIES_MODEL_ATTRIBUTE = "nodeProperties";
  private static final String NODE_PROPERTIES_LIST_MODEL_ATTRIBUTE = "nodePropertiesList";
  private static final String NODE_NAME_MODEL_ATTRIBUTE = "nodeName";
  private static final int ZERO = 0;
  private static final String KPI_NOT_FOUND_MSG = "<div class=\"ErrorMsg\">KPI not found!</div>";
  private static final String KPI_SAVED_MSG = "<div class=\"SuccessMsg\">KPI saved successfully!</div>";
  private static final String KPI_NOT_SAVED_MSG = "<div class=\"ErrorMsg\">unfortunately KPI not saved!</div>";
  private static final String DELETE_SUCCESS_MSG = "<div class=\"SuccessMsg\">Node properties deleted successfully!</div>";
  private static final String DELETE_ERROR_MSG = "<div class=\"ErrorMsg\">unfortunately node properties hasn't been deleted !</div>";

  private static final String DELETE_UNFOUND_MSG = "<div class=\"ErrorMsg\">no properties found for the selected node !</div>";
  private static final String NO_CONFIGURED_THRESHOLD_MSG = "<div class=\"ErrorMsg\">Selected node doesn't have a configured threshold</div> ";
  private static final String SYSTEM_LIST_KEY = "systemList";

  private final static Logger LOGGER
    = LoggerFactory.getLogger(KPIController.class);
  public static final String SELECT_GRAIN = "Select Grain";
  public static final String SYSTEM_NODE_DOES_NOT_EXIST_MSG = "System node doesn't exist";
  public static final String ERRORS_LIST = "errorsList";

  @Autowired
  KPIValidator kpiValidator;
  @Autowired
  ManageKpiService manageKpiNotificationsService;
  @Autowired
  private DataCollectionSystemServiceInterface dataCollectionService;
  private static final String SYSTEM_NODE_MODEL_ATTRIBUTE = "systemNode";
  private static final String ADD_NEW_SYSTEM_KPI_VIEW = "/kpi/addNewSystemKpi";
  private static final String NODE_PROPERTIES_UPDATE_FAILURE = "<div class=\"ErrorMsg\">unfortunately node properties hasn't been updated !</div>";


  @RequestMapping(value = "systemKpis", method = RequestMethod.GET)
  public ModelAndView listSystemKPIs() {
    ModelAndView modelAndView = new ModelAndView(SYSTEM_KPIS_VIEW);
    loadKPINotificationsModelBean(modelAndView);
    modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, new ArrayList<SystemNode>());
    modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, new SystemKpiModel());
    return modelAndView;
  }

  /**
   * Retrieves all nodes associated with the given system.
   *
   * @param systemName the given system name to retrieve its nodes
   * @return ModelAndView object
   */
  @RequestMapping(value = "loadNodes", method = RequestMethod.POST)
  public ModelAndView loadNodeList(@ModelAttribute(value = "systemName") String systemName) {
    LOGGER.debug("Loading node list for received system: {}",
      systemName);
    ModelAndView modelAndView = new ModelAndView("/kpi/systemNodeList");
    SystemKpiModel systemKpiModel = new SystemKpiModel();

    if (StringUtils.hasText(systemName)) {
      systemKpiModel.setSystemName(systemName);
      List<SystemNode> systemNodes = manageKpiNotificationsService.getSystemNodesBySystemName(systemName);
      modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, systemKpiModel);
      modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE,
        systemNodes);
    }
    return modelAndView;
  }

  @RequestMapping(value = "showThresholdAndKpis", method = RequestMethod.POST)
  public ModelAndView listNodeProperties(@ModelAttribute(SYSTEM_KPI_MODEL_ATTRIBUTE) SystemKpiModel systemKpiModel, BindingResult result) {

    kpiValidator.validateSearchForm(systemKpiModel, result);
    ModelAndView modelAndView = populateSystemKpiModelAndView(SYSTEM_KPIS_VIEW, systemKpiModel);

    if (!result.hasErrors()) {
      modelAndView = populateKpiDetails(modelAndView, systemKpiModel);
    }

    return modelAndView;
  }

  @RequestMapping(value = "initNewKpi", method = RequestMethod.GET)
  public ModelAndView initNewKpi() {


    ModelAndView modelAndView = null;
    SystemKpiModel systemKpiModel = null;


    modelAndView = new ModelAndView(ADD_NEW_SYSTEM_KPI_VIEW);

    systemKpiModel = new SystemKpiModel();
    systemKpiModel.setNodeProperties(new NodeProperties());
    systemKpiModel.setNodePropertiesList(new ArrayList<NodeProperties>());


    modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, systemKpiModel);
    modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
    modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, new ArrayList<SystemNode>());


    return modelAndView;
  }

  @RequestMapping(value = "editKpi/{nodeID}", method = RequestMethod.GET)
  public ModelAndView initEditKpi(@PathVariable String nodeID) {


    ModelAndView modelAndView = null;
    SystemKpiModel systemKpiModel = null;


    SystemNode systemNode = manageKpiNotificationsService.getSystemNodeById(Long.parseLong(nodeID));
    if (systemNode != null) {
      systemKpiModel = convertToModelBean(systemNode);

      modelAndView = new ModelAndView(SYSTEM_KPI_DETAILS_VIEW);
      modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, systemKpiModel);
      modelAndView = populateKpiDetails(modelAndView, systemKpiModel);

    } else {
      modelAndView = new ModelAndView(SYSTEM_KPIS_VIEW);

      modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, KPI_NOT_FOUND_MSG);
    }

    return modelAndView;
  }

  @RequestMapping(value = "updateKpi", method = RequestMethod.POST)
  public ModelAndView updateKpi(@ModelAttribute(SYSTEM_KPI_MODEL_ATTRIBUTE) SystemKpiModel systemKpiModel, BindingResult result) {
    ModelAndView modelAndView;

    List<NodeProperties> updatedNodePropList = deleteEmptyNodeProperties(systemKpiModel.getNodePropertiesList());

    systemKpiModel.setNodePropertiesList(updatedNodePropList);

    Validator defaultValidator = Validation.buildDefaultValidatorFactory().getValidator();
    KPIValidator.convert(result, defaultValidator.validate(systemKpiModel));
    kpiValidator.validate(systemKpiModel, result);

    SystemNode node = manageKpiNotificationsService.getSystemNodeById(systemKpiModel.getNodeID());

    if (!result.hasErrors() && null != node) {

      modelAndView = updateSystemNode(systemKpiModel, node);
    } else {
      modelAndView = populateSystemKpiModelAndView(SYSTEM_KPI_DETAILS_VIEW, systemKpiModel);
    }
    // add grid error to a list to be displayed as grouped errors
    modelAndView.addObject(ERRORS_LIST, result.getAllErrors());
    return modelAndView;
  }

  @RequestMapping(value = "saveNewKpi", method = RequestMethod.POST)
  public ModelAndView saveNewKpi(@ModelAttribute(SYSTEM_KPI_MODEL_ATTRIBUTE) SystemKpiModel systemKpiModel, BindingResult result) {
    ModelAndView modelAndView;
    List<NodeProperties> updatedNodePropList = deleteEmptyNodeProperties(systemKpiModel.getNodePropertiesList());

    modelAndView = populateSystemKpiModelAndView(ADD_NEW_SYSTEM_KPI_VIEW, systemKpiModel);

    systemKpiModel.setNodePropertiesList(updatedNodePropList);

    Validator defaultValidator = Validation.buildDefaultValidatorFactory().getValidator();
    KPIValidator.convert(result, defaultValidator.validate(systemKpiModel));
    kpiValidator.validate(systemKpiModel, result);

    // add grid error to a list to be displayed as grouped errors
    List<ObjectError> errorList = result.getAllErrors();
    modelAndView.addObject(ERRORS_LIST, errorList);


    kpiValidator.validateSearchForm(systemKpiModel, result);
    SystemNode node = manageKpiNotificationsService.getSystemNodeById(systemKpiModel.getNodeID());
    if (!result.hasErrors() && null != node) {
      modelAndView = updateSystemNode(systemKpiModel, node);

    } else if (null == node) {

      modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, SYSTEM_NODE_DOES_NOT_EXIST_MSG);

    }

    return modelAndView;
  }


  @RequestMapping(value = "deleteAllNodeProperties/{nodeId}/{systemName}", method = RequestMethod.GET)
  public ModelAndView deleteAllNodeProperties(@PathVariable String nodeId, @PathVariable String systemName) {
    ModelAndView modelAndView;
    SystemKpiModel systemKpiModel = new SystemKpiModel();
    systemKpiModel.setNodeID(Long.parseLong(nodeId));
    systemKpiModel.setSystemName(systemName);

    SystemNode node = manageKpiNotificationsService.getSystemNodeById(Long.parseLong(nodeId));

    modelAndView = populateSystemKpiModelAndView(SYSTEM_KPIS_VIEW, systemKpiModel);
    if (null != node) {
      int affectedRows = manageKpiNotificationsService.deleteAllSystemNodesProperties(node.getSystemNodeId(), true);

      if (affectedRows > ZERO) {

        modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY, DELETE_SUCCESS_MSG);
      } else {

        modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, DELETE_ERROR_MSG);
      }
    } else {

      modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, DELETE_UNFOUND_MSG);
    }


    return modelAndView;
  }


  @RequestMapping(value = "verifyNoThresholdExists", method = RequestMethod.GET)
  public
  @ResponseBody
  boolean verifyNoThresholdExists(@RequestParam String systemName, @RequestParam String nodeId) {
    return manageKpiNotificationsService.isNodeHasConfiguredProperties(systemName, Long.parseLong(nodeId));
  }


    @RequestMapping(value = "isNodeUsed", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean isNodeUsed( @RequestParam String nodeId) {
        return NodeInUse.YES.toString().equals(manageKpiNotificationsService.isSystemNodeInUse(Integer.parseInt(nodeId.trim())));
    }

  @ModelAttribute(value = "grain")
  public Map<String, String> getGrainValue() {
    Map<String, String> grain = new LinkedHashMap<String, String>();
    grain.put("-1", SELECT_GRAIN);
    grain.put(KpiNodeGrain.Daily.toString(), KpiNodeGrain.Daily.name());
    grain.put(KpiNodeGrain.Hourly.toString(), KpiNodeGrain.Hourly.name());
    return grain;
  }

  private SystemKpiModel convertToModelBean(SystemNode systemNode) {
    SystemKpiModel systemKpiModel = new SystemKpiModel();
    //kpiNotificationForm.setNodeID(vNode.getId());
    systemKpiModel.setSystemName(systemNode.getSystemName());
    systemKpiModel.setNodeID(systemNode.getSystemNodeId());

    return systemKpiModel;
  }

  private List<NodeProperties> deleteEmptyNodeProperties(List<NodeProperties> nodePropertiesList) {
    List<NodeProperties> updatedList = new ArrayList<NodeProperties>();
    if (nodePropertiesList != null && !nodePropertiesList.isEmpty()) {
      for (NodeProperties nodeProperties : nodePropertiesList) {
        if (nodeProperties != null) {
          if (StringUtils.hasText(nodeProperties.getPropertyName()) || StringUtils.hasText(nodeProperties.getTrafficTableName())
            || StringUtils.hasText(nodeProperties.getGrain())) {
            updatedList.add(nodeProperties);
          }
        }
      }
      /* for (NodeProperties nodeProperties : nodePropertiesList) {
       if (nodeProperties != null && nodeProperties.getPropertyName() != null && nodeProperties.getTrafficTableName() != null
         && nodeProperties.getGrain() != null) {
         updatedList.add(nodeProperties);
       }

     } */
    }
    return updatedList;
  }

  private ModelAndView updateSystemNode(SystemKpiModel systemKpiModel, SystemNode node) {
    ModelAndView modelAndView;
    modelAndView = new ModelAndView(SYSTEM_KPIS_VIEW);

    //update systemNode
    node.setInUse(mapInUseBooleanToString(systemKpiModel.getInUse()));
    int affectedRows = manageKpiNotificationsService.updateSystemNode(node);
    boolean isUpdated = ControllersUtil.isSuccessfulTransaction(affectedRows);
    if (isUpdated) {


      // update Node properties
      boolean nodePropertiesUpdateSuccess = updateNodeProperties(systemKpiModel);
      if (!nodePropertiesUpdateSuccess) {
        modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, NODE_PROPERTIES_UPDATE_FAILURE);
      } else {
        systemKpiModel.setNodePropertiesList(manageKpiNotificationsService.getSystemKpiDetails(systemKpiModel.getNodeID()));
      }


      modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY, KPI_SAVED_MSG);
      modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, systemKpiModel);
      modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
      modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, manageKpiNotificationsService.getSystemNodesBySystemName(systemKpiModel.getSystemName()));
    } else {
      modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, KPI_NOT_SAVED_MSG);
    }
    return modelAndView;
  }


  private boolean updateNodeProperties(SystemKpiModel systemKpiModel) {

    manageKpiNotificationsService.deleteAllSystemNodesProperties(systemKpiModel.getNodeID(), false);
    int nodePropertiesEntries = ZERO;
    int affectedRows = ZERO;
    if (null != systemKpiModel.getNodePropertiesList() && !systemKpiModel.getNodePropertiesList().isEmpty()) {
      nodePropertiesEntries = systemKpiModel.getNodePropertiesList().size();


      for (NodeProperties nodeProperties : systemKpiModel.getNodePropertiesList()) {
        if (isNodePropertiesHasValues(nodeProperties)) {
          nodeProperties.setNodeId(systemKpiModel.getNodeID());
          int updateResult = manageKpiNotificationsService.addSystemKpiDetails(nodeProperties);
          affectedRows += updateResult;
        } else {
          systemKpiModel.getNodePropertiesList().remove(nodeProperties);
          nodePropertiesEntries--;
        }
      }
    }
    Boolean updatedSuccessfully = (nodePropertiesEntries == affectedRows) ? true : false;
    return updatedSuccessfully;
  }

  private boolean isNodePropertiesHasValues(NodeProperties nodeProperties) {
    Boolean validEntry = false;

    if (nodeProperties.getPropertyName() != null && nodeProperties.getTrafficTableName() != null
      && nodeProperties.getGrain() != null) {
      validEntry = true;
    }
    return validEntry;
  }

  private ModelAndView populateSystemKpiModelAndView(String viewName, SystemKpiModel systemKpiModel) {

    ModelAndView modelAndView = new ModelAndView(viewName);

    modelAndView.addObject(SYSTEM_KPI_MODEL_ATTRIBUTE, systemKpiModel);
    modelAndView.addObject(SYSTEM_LIST_KEY, dataCollectionService.listAllSystems());
    if (null != systemKpiModel) {
      modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, manageKpiNotificationsService.getSystemNodesBySystemName(systemKpiModel.getSystemName()));
      if (systemKpiModel.getNodeID() > ZERO) {
        SystemNode systemNode = manageKpiNotificationsService.getSystemNodeById(systemKpiModel.getNodeID());
        modelAndView.addObject(SYSTEM_NODE_MODEL_ATTRIBUTE, systemNode);
      }

    }

    //modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, new ArrayList<VNode>());
    return modelAndView;
  }

  private ModelAndView populateNodePropertiesModelAndView(String nodePropertiesView, NodeProperties nodeProperties) {
    ModelAndView modelAndView = new ModelAndView(nodePropertiesView);

    modelAndView.addObject(NODE_PROPERTIES_MODEL_ATTRIBUTE, nodeProperties);
    return modelAndView;
  }

  private ModelAndView populateKpiDetails(ModelAndView modelAndView, SystemKpiModel systemKpiModel) {
    List<NodeProperties> nodePropertiesList = manageKpiNotificationsService.getSystemKpiDetails(systemKpiModel.getNodeID());
    if (nodePropertiesList == null || (nodePropertiesList != null && nodePropertiesList.isEmpty())) {
      modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, NO_CONFIGURED_THRESHOLD_MSG);
    }
    systemKpiModel.setNodePropertiesList(nodePropertiesList);
    modelAndView.addObject(NODE_PROPERTIES_LIST_MODEL_ATTRIBUTE, nodePropertiesList);
    SystemNode systemNode = manageKpiNotificationsService.getSystemNodeById(systemKpiModel.getNodeID());
    systemKpiModel.setInUse(mapInUseStringToBoolean(systemNode.getInUse()));
    modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, manageKpiNotificationsService.getSystemNodesBySystemName(systemKpiModel.getSystemName()));
    modelAndView.addObject(SYSTEM_NODE_MODEL_ATTRIBUTE, systemNode);
    return modelAndView;
  }

  private Boolean mapInUseStringToBoolean(String inUse) {
    boolean isInUse = false;
    if (NodeInUse.YES.toString().equalsIgnoreCase(inUse)) {
      isInUse = true;
    } else if (NodeInUse.NO.toString().equalsIgnoreCase(inUse)) {
      isInUse = false;
    }
    return isInUse;
  }

  private String mapInUseBooleanToString(Boolean inUse) {
    String inUseString = (inUse) ? NodeInUse.YES.toString() : NodeInUse.NO.toString();
    return inUseString;
  }

  private void loadKPINotificationsModelBean(ModelAndView modelAndView) {
    if (modelAndView == null) {
      modelAndView = new ModelAndView(SYSTEM_KPIS_VIEW);
    }
    modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
  }

  /**
   * Get All system names
   *
   * @return all available system names
   */
  private List<String> getAllSystems() {
    List<String> systemNames
      = dataCollectionService.listAllSystems();
    if (systemNames == null || systemNames.isEmpty()) {
      throw new GenericException("System list size is Zero");
    }

    return systemNames;
  }
}
