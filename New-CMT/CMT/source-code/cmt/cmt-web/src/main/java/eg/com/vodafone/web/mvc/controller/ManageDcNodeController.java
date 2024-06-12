package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.enums.NodeInUse;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcInputFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcNodeFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcSystemFormBean;
import eg.com.vodafone.web.mvc.validator.DcNodeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;

import static eg.com.vodafone.service.BusinessException.DUPLICATE_NODE_NAME;
import static eg.com.vodafone.service.BusinessException.EMPTY_SYSTEM;
import static eg.com.vodafone.web.mvc.util.CMTConstants.*;


/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/18/13
 * Time   : 9:55 AM
 */

@Controller
@RequestMapping("/manageNodes/*")
public class ManageDcNodeController extends AbstractController {

  @Autowired
  private DataCollectionSystemServiceInterface systemService;

  @Autowired
  private DcNodeValidator nodeValidator;

  @RequestMapping(value = "/editNode/{nodeName}", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView editNodeWithoutNodeName2(@PathVariable(value = "nodeName") String nodeName,
                                               HttpSession session,
                                               @RequestParam(value = "create", defaultValue = "false") String create,
                                               @RequestParam(value = "delete", defaultValue = "false") String delete) {
    return editNode(nodeName, session, create, delete);
  }

  @RequestMapping(value = "/editNode/", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView editNodeWithoutNodeName2(HttpSession session,
                                               @RequestParam(value = "create", defaultValue = "false") String create,
                                               @RequestParam(value = "delete", defaultValue = "false") String delete) {
    return editNode("", session, create, delete);
  }

  private ModelAndView editNode(String nodeName, HttpSession session, String create, String delete) {
    ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-node-info");
    if (org.apache.commons.lang.StringUtils.isNotEmpty(create) && TRUE.equalsIgnoreCase(create)) {
      modelAndView.addObject(CREATE_SUCSS_MSG_FLAG, true);
    }

    if (org.apache.commons.lang.StringUtils.isNotEmpty(delete) && TRUE.equalsIgnoreCase(delete)) {
      modelAndView.addObject(DELETE_SUCSS_MSG_FLAG, true);
    }
    DcSystemFormBean systemFormBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    DcNodeFormBean nodeBeanFromSession =
      (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
    VNode node = null;
    DcNodeFormBean dcNodeFormBean = null;
    if (systemFormBean.isEditMode() &&
      ((nodeBeanFromSession != null && nodeBeanFromSession.isEditMode())
        || nodeBeanFromSession == null
      )) {

      node = systemService.getNode(systemFormBean.getSystem().getName(), nodeName);

      dcNodeFormBean = prepareDcNodeFormBean(node);
      dcNodeFormBean.setSystemName(systemFormBean.getSystem().getName());
      dcNodeFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
      dcNodeFormBean.setNewSystem(!systemFormBean.isEditMode());
    } else if (!systemFormBean.isEditMode() && nodeBeanFromSession == null) {
      dcNodeFormBean = prepareDcNodeFormBean(systemFormBean.getNode(nodeName));
      dcNodeFormBean.setSystemName(systemFormBean.getSystem().getName());
      dcNodeFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
      dcNodeFormBean.setNewSystem(!systemFormBean.isEditMode());
    } else if (!systemFormBean.isEditMode() && nodeBeanFromSession != null) {   //from save in page add/edit input
      dcNodeFormBean = nodeBeanFromSession;
      DcInputFormBean inputFormBean = (DcInputFormBean) session.getAttribute(DC_INPUT_FORM_BEAN_NAME);
      if (inputFormBean != null) {  //added to maintain node name and description of not saved node page
        dcNodeFormBean.getNode().setName(inputFormBean.getNodeName());
        dcNodeFormBean.getNode().setDescription(inputFormBean.getNodeDescription());
      }
    } else if (systemFormBean.isEditMode() &&
      (nodeBeanFromSession != null && !nodeBeanFromSession.isEditMode())  //from "cancel in page add/edit input"
      ) {
      dcNodeFormBean = nodeBeanFromSession;
      DcInputFormBean inputFormBean = (DcInputFormBean) session.getAttribute(DC_INPUT_FORM_BEAN_NAME);
      if (inputFormBean != null) {  //added to maintain node name and description of not saved node page
        dcNodeFormBean.getNode().setName(inputFormBean.getNodeName());
        dcNodeFormBean.getNode().setDescription(inputFormBean.getNodeDescription());
      }
    } else {
      node = systemFormBean.getNode(nodeName);
      dcNodeFormBean = prepareDcNodeFormBean(node);
      dcNodeFormBean.setSystemName(systemFormBean.getSystem().getName());
      dcNodeFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
      dcNodeFormBean.setNewSystem(!systemFormBean.isEditMode());
    }
    session.removeAttribute(DC_INPUT_FORM_BEAN_NAME);
    session.setAttribute(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
    modelAndView.addObject(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
    return modelAndView;
  }

  @RequestMapping(value = "/defineDcNode", method = {RequestMethod.POST})
  public ModelAndView defineDcNode(DcSystemFormBean systemFormBean, HttpSession session) {
    ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-node-info");
    DcNodeFormBean dcNodeFormBean = prepareDcNodeFormBean();
    dcNodeFormBean.setSystemName(systemFormBean.getSystem().getName());
    dcNodeFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
    dcNodeFormBean.setNewSystem(!systemFormBean.isEditMode());
    session.setAttribute(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
    return modelAndView.addObject(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
  }

  @RequestMapping(value = "deleteNode/{nodeName}", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView deleteDataCollectionNode(@PathVariable(value = "nodeName") String nodeName, HttpSession session) {
    DcSystemFormBean systemFormBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    StringBuffer url = new StringBuffer(); //("/manageSystem/editSystem");
    if (systemFormBean.isEditMode()) {
      try {
        boolean deleteJobDone = systemService.deleteNode(nodeName, systemFormBean.getSystem().getName());
        url.append("/manageSystem/editSystem/").append(systemFormBean.getSystem().getName()).append("?delete_node=true");
        if (!deleteJobDone) {
          url.append("&delete_jobs_error=true");
        }
      } catch (BusinessException ex) {
        String message = "";
        if (ex.getCode() == EMPTY_SYSTEM) {
          message = "System must have at least one Input or Node";
        } else {
          message = "Error deleting node \'" + nodeName + "\' please refer to logs";
        }
        return new ModelAndView("/manageSystems/dc-system-info")
          .addObject(ERRORS, message)
          .addObject(DC_SYSTEM_FORM_BEAN_NAME, systemFormBean);

      } catch (Exception exception) {
        throw new GenericException("Error deleting data collection Node:" + exception.getMessage(),exception);
      }
    } else {
      systemFormBean.removeNode(nodeName);
      session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, systemFormBean);
      url.append("/manageSystem/editSystem?delete_node=true");
    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(url.toString(), true));
    return modelAndView;
  }

  @RequestMapping(value = "/saveDcNode", method = RequestMethod.POST)
  public ModelAndView saveNode(@Valid DcNodeFormBean dcNodeFormBean, BindingResult result, HttpSession session) {
    /*---------------validations-------------------*/
    DcNodeFormBean sessionNodeFormBean = (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
    DcSystemFormBean systemFormBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    String nodeFromSessionName = sessionNodeFormBean.getNode().getName();
    sessionNodeFormBean.getNode().setSystemName(systemFormBean.getSystem().getName());
    dcNodeFormBean.getNode().setInputsList(sessionNodeFormBean.getNode().getInputsList());
    sessionNodeFormBean.getNode().setName(dcNodeFormBean.getNode().getName());
    sessionNodeFormBean.getNode().setDescription(dcNodeFormBean.getNode().getDescription());
    session.setAttribute(DC_NODE_FORM_BEAN_NAME, sessionNodeFormBean);
    if (result.hasErrors()) {
      return new ModelAndView("/manageSystems/dc-node-info")
        .addObject(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
    }
    //Riham.Maksoud:Ticket# 4499 remove the condition as it sometimes return false and the unique node names is not validated
    //We need to validate unique node names in all cases.
   // if ((StringUtils.hasText(nodeFromSessionName) && nodeFromSessionName.equals(dcNodeFormBean.getNode().getName()))
    //  || !StringUtils.hasText(nodeFromSessionName)) {
    String error = validateUniqueNodeName(sessionNodeFormBean.getNode(), systemFormBean);
    if (StringUtils.hasText(error)) {
       return new ModelAndView("/manageSystems/dc-node-info")
         .addObject(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean)
         .addObject(ERRORS, error);
    }
    //}

    nodeValidator.validate(dcNodeFormBean, result);
    if (result.hasErrors()) {
      return new ModelAndView("/manageSystems/dc-node-info")
        .addObject(DC_NODE_FORM_BEAN_NAME, dcNodeFormBean);
    }

    /*--------------end validations-----------------*/
    if (systemFormBean.isEditMode()) {  //update in db
      sessionNodeFormBean.prepareNodeForSave();
      if (sessionNodeFormBean.isEditMode()) {
        systemService.updateNode(sessionNodeFormBean.getNode());
      } else {
        systemService.addNewNode(sessionNodeFormBean.getNode());
      }
    } else {  //update session object
      if (sessionNodeFormBean.isEditMode()) {
        systemFormBean.updateNode(nodeFromSessionName, dcNodeFormBean.getNode().getName(), dcNodeFormBean.getNode().getDescription());
      } else {
        sessionNodeFormBean.getNode().setInUse(NodeInUse.YES.toString());
        systemFormBean.getSystem().getNodesList().add(sessionNodeFormBean.getNode());
      }
      session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, systemFormBean);
    }
    ModelAndView modelAndView = null;

    if (systemFormBean.isEditMode()) {
      modelAndView = new ModelAndView(new RedirectView("/manageSystem/editSystem/" + systemFormBean.getSystem().getName() + "?create_node=true", true));
    } else {
      modelAndView = new ModelAndView(new RedirectView("/manageSystem/editSystem/" + systemFormBean.getSystem().getName() + "?create_node=true", true));
    }
    //session.removeAttribute(DC_NODE_FORM_BEAN_NAME);
    return modelAndView;
  }

  private DcNodeFormBean prepareDcNodeFormBean(VNode node) {
    DcNodeFormBean dcNodeFormBean = new DcNodeFormBean();
    VNode aNode = new VNode();
    aNode.setName(node.getName());
    aNode.setDescription(node.getDescription());
    aNode.setId(node.getId());
    aNode.setSystemName(node.getSystemName());
    aNode.setInUse(node.getInUse());
    aNode.setInputsList(node.getInputsList());
    dcNodeFormBean.setNode(aNode);
    dcNodeFormBean.setEditMode(true);
    return dcNodeFormBean;

  }

  private DcNodeFormBean prepareDcNodeFormBean() {
    DcNodeFormBean dcNodeFormBean = new DcNodeFormBean();
    dcNodeFormBean.setEditMode(false);
    dcNodeFormBean.setNode(new VNode());
    return dcNodeFormBean;

  }

  private String validateUniqueNodeName(VNode node, DcSystemFormBean systemBean) {
    String error = "";
    if (systemBean != null && systemBean.getSystem() != null) {
      try {
        systemService.validateUniqueNodeName(node, Arrays.asList(systemBean.getSystem().getNodes()));
      } catch (BusinessException ex) {
        if (ex.getCode() == DUPLICATE_NODE_NAME) {
          error = " Node name must be unique per system ";
        } else {
          error = "error while saving node . please refer to logs";
        }
      }
    }
    return error;
  }
}
