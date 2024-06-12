package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.VSystem;
import eg.com.vodafone.model.enums.NodeInUse;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.*;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.util.GridConstants;
import eg.com.vodafone.web.mvc.util.GridUtils;
import eg.com.vodafone.web.mvc.validator.DcSystemValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import java.util.List;

import static eg.com.vodafone.service.BusinessException.DUPLICATE_SYSTEM_NAME;
import static eg.com.vodafone.web.mvc.util.CMTConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/15/13
 * Time   : 8:49 AM
 */
@Controller
@RequestMapping("/manageSystem/*")
public class ManageDcSystemController extends AbstractGridBasedController {

  @Autowired
  private DataCollectionSystemServiceInterface systemService;

  @Autowired
  private DcSystemValidator systemValidator;

  private static final String SYSTEM_GRID_XML_CONFIG = "eg/com/vodafone/grids/dc-system-grid.xml";
  private final int DEFAULT_START_INDEX = 1;


  @RequestMapping(value = "list", method = {RequestMethod.GET})
  public ModelAndView listDataCollectionSystem(DcSystemSearchCriteria searchCriteria, HttpSession session,
                                               @RequestParam(value = "create", defaultValue = "false") String create,
                                               @RequestParam(value = "delete", defaultValue = "false") String delete,
                                               @RequestParam(value = "delete_jobs_error", defaultValue = "false") String deleteJobError) {

    ModelAndView modelAndView = new ModelAndView("/manageSystems/manage-system-main");
    if (StringUtils.isNotEmpty(create) && TRUE.equalsIgnoreCase(create)) {
      modelAndView.addObject(CREATE_SUCSS_MSG_FLAG, true);
    } else if (StringUtils.isNotEmpty(delete) && TRUE.equalsIgnoreCase(delete)) {
      modelAndView.addObject(DELETE_SUCSS_MSG_FLAG, true);
    }

    if (StringUtils.isNotEmpty(deleteJobError) && TRUE.equalsIgnoreCase(deleteJobError)) {
      modelAndView.addObject(DELETE_JOB_ERROR_FLAG, true);
    }
    session.removeAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    int total = getCount(searchCriteria);

    if (total > 0) {

      modelAndView.addObject(GRID_RESULTS_MODEL_NAME,
        getResultsList(searchCriteria, DEFAULT_START_INDEX, GridConstants.GRID_DEFAULT_NO_ROWS_PER_PAGE));

      modelAndView.addObject(GRID_MODEL_NAME,
        createSystemGridBean(searchCriteria, total));
    }

    modelAndView.addObject("searchCriteria", searchCriteria);

    return modelAndView;
  }

  @RequestMapping(value = "deleteSystem/{systemName}", method = RequestMethod.GET)
  public ModelAndView deleteDataCollectionSystem(GridBean gridBean,
                                                 @PathVariable(value = "systemName") String systemName) {
    boolean deleteJobsDone = false;
    try {
      deleteJobsDone = systemService.deleteSystem(systemName.trim());

    } catch (BusinessException exception) {

      ModelAndView modelAndView = new ModelAndView("/manageSystems/manage-system-main");
      modelAndView.addObject(GRID_RESULTS_MODEL_NAME,
        getResultsList(gridBean.getSearchCriteria(),
          gridBean.getPaginationBean().getStartRowIndex(),
          gridBean.getPaginationBean().getEndRowIndex()));

      modelAndView.addObject(GRID_MODEL_NAME, gridBean);
      modelAndView.addObject("searchCriteria", new DcSystemSearchCriteria());
      return modelAndView;

    } catch (Exception exception) {
      throw new GenericException("Error deleting data collection system");
    }
    StringBuffer viewUrl = new StringBuffer("/manageSystem/list?delete=true");
    if (!deleteJobsDone) {
      viewUrl.append("&delete_jobs_error=true");
    }
    return new ModelAndView(new RedirectView(viewUrl.toString(), true));
  }

    @RequestMapping(value = "activateNode/{nodeName}",method = RequestMethod.GET)
    public  ModelAndView activateNode(@PathVariable(value="nodeName")String nodeName,HttpSession session){

        DcSystemFormBean systemFormBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        StringBuffer url = new StringBuffer("/manageSystem/editSystem") ;
        if(systemFormBean.isEditMode()){
                url.append("/").append(systemFormBean.getSystem().getName());
                systemService.activateNode(nodeName, systemFormBean.getSystem().getName());
        } else {
               systemFormBean.updateNodeStatus(nodeName, NodeInUse.YES.toString());
               session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME,systemFormBean);
        }
        return new ModelAndView(new RedirectView(url.toString(),true));
    }
    @RequestMapping(value = "deActivateNode/{nodeName}",method = RequestMethod.GET)
    public  ModelAndView deActivateNode(@PathVariable(value="nodeName")String nodeName,HttpSession session){
        DcSystemFormBean systemFormBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        StringBuffer url = new StringBuffer("/manageSystem/editSystem");
        if(systemFormBean.isEditMode()){
            url.append("/").append(systemFormBean.getSystem().getName());
            systemService.deactivateNode(nodeName, systemFormBean.getSystem().getName());
        }
        else
        {
            systemFormBean.updateNodeStatus(nodeName, NodeInUse.NO.toString());
            session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, systemFormBean);
        }
        return new ModelAndView(new RedirectView(url.toString(),true));
    }
  @RequestMapping(value = "/editSystem", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView editDcSystem1(HttpSession session,
                                    @RequestParam(value = "create_node", defaultValue = "false") String createNode,
                                    @RequestParam(value = "delete_node", defaultValue = "false") String deleteNode,
                                    @RequestParam(value = "create_input", defaultValue = "false") String createInput,
                                    @RequestParam(value = "delete_input", defaultValue = "false") String deleteInput,
                                    @RequestParam(value = "delete_jobs_error", defaultValue = "false") String deleteJobError) {
    ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-system-info");
    if (StringUtils.isNotEmpty(createNode) && TRUE.equalsIgnoreCase(createNode)) {
      modelAndView.addObject("create_node", true);
    } else if (StringUtils.isNotEmpty(deleteNode) && TRUE.equalsIgnoreCase(deleteNode)) {
      modelAndView.addObject("delete_node", true);
    } else if (StringUtils.isNotEmpty(createInput) && TRUE.equalsIgnoreCase(createInput)) {
      modelAndView.addObject("create_input", true);
    } else if (StringUtils.isNotEmpty(deleteInput) && TRUE.equalsIgnoreCase(deleteInput)) {
      modelAndView.addObject("delete_input", true);

    }
    if (StringUtils.isNotEmpty(deleteJobError) && TRUE.equalsIgnoreCase(deleteJobError)) {
      modelAndView.addObject(DELETE_JOB_ERROR_FLAG, true);
    }
    DcSystemFormBean sessionBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    if(sessionBean == null)
    {
       sessionBean = prepareDcSystemFormBean();
    }
    else
    {
        DcInputFormBean inputFormBean = (DcInputFormBean) session.getAttribute(DC_INPUT_FORM_BEAN_NAME);
        if (!sessionBean.isEditMode()) {
          if (inputFormBean != null) {
            sessionBean.getSystem().setName(inputFormBean.getSystemName());
            sessionBean.getSystem().setDescription(inputFormBean.getSystemDescription());
          } else {
            DcNodeFormBean nodeFormBean = (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
            if (nodeFormBean != null) {
              sessionBean.getSystem().setDescription(nodeFormBean.getSystemDescription());
              sessionBean.getSystem().setName(nodeFormBean.getSystemName());
              session.removeAttribute(DC_NODE_FORM_BEAN_NAME);
            }
          }
        }
        session.removeAttribute(DC_NODE_FORM_BEAN_NAME);
        session.removeAttribute(DC_INPUT_FORM_BEAN_NAME);
    }
    return modelAndView.addObject(DC_SYSTEM_FORM_BEAN_NAME, sessionBean);
  }

  @RequestMapping(value = "/editSystem/{systemName}", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView editDcSystem(@PathVariable(value = "systemName") String systemName, HttpSession session,
                                   @RequestParam(value = "create_node", defaultValue = "false") String createNode,
                                   @RequestParam(value = "delete_node", defaultValue = "false") String deleteNode,
                                   @RequestParam(value = "create_input", defaultValue = "false") String createInput,
                                   @RequestParam(value = "delete_input", defaultValue = "false") String deleteInput,
                                   @RequestParam(value = "delete_jobs_error", defaultValue = "false") String deleteJobError) {
    ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-system-info");
    if (StringUtils.isNotEmpty(createNode) && TRUE.equalsIgnoreCase(createNode)) {
      modelAndView.addObject("create_node", true);
    } else if (StringUtils.isNotEmpty(deleteNode) && TRUE.equalsIgnoreCase(deleteNode)) {
      modelAndView.addObject("delete_node", true);
    } else if (StringUtils.isNotEmpty(createInput) && TRUE.equalsIgnoreCase(createInput)) {
      modelAndView.addObject("create_input", true);
    } else if (StringUtils.isNotEmpty(deleteInput) && TRUE.equalsIgnoreCase(deleteInput)) {
      modelAndView.addObject("delete_input", true);
    }
    if (StringUtils.isNotEmpty(deleteJobError) && TRUE.equalsIgnoreCase(deleteJobError)) {
      modelAndView.addObject(DELETE_JOB_ERROR_FLAG, true);
    }
    DcSystemFormBean sessionBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    if ((sessionBean != null && sessionBean.isEditMode())
      || sessionBean == null) {
      VSystem system = systemService.getSystem(systemName, false);
      DcSystemFormBean dcSystemFormBean = prepareDcSystemFormBean(system);
      session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);
      modelAndView.addObject(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);
    } else {
      DcInputFormBean inputFormBean = (DcInputFormBean) session.getAttribute(DC_INPUT_FORM_BEAN_NAME);
      if (inputFormBean != null) {
        sessionBean.getSystem().setName(inputFormBean.getSystemName());
        sessionBean.getSystem().setDescription(inputFormBean.getSystemDescription());
      } else {
        DcNodeFormBean nodeFormBean = (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
        if (nodeFormBean != null) {
          sessionBean.getSystem().setDescription(nodeFormBean.getSystemDescription());
          sessionBean.getSystem().setName(nodeFormBean.getSystemName());
          session.removeAttribute(DC_NODE_FORM_BEAN_NAME);
        }
      }
      modelAndView.addObject(DC_SYSTEM_FORM_BEAN_NAME, sessionBean);
    }
    session.removeAttribute(DC_NODE_FORM_BEAN_NAME);
    session.removeAttribute(DC_INPUT_FORM_BEAN_NAME);
    return modelAndView;
  }

  @RequestMapping(value = "/defineDcSystem", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView defineDcSystem(HttpSession session) {
    DcSystemFormBean dcSystemFormBean = prepareDcSystemFormBean();
    session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);
    return new ModelAndView("/manageSystems/dc-system-info").addObject(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);
  }

  @RequestMapping(value = "/saveDcSystem", method = {RequestMethod.POST})
  public ModelAndView saveSystem(@Valid DcSystemFormBean dcSystemFormBean,
                                 BindingResult result,
                                 HttpSession session) {
    DcSystemFormBean sessionBean =
      (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    sessionBean.getSystem().setName(dcSystemFormBean.getSystem().getName());
    sessionBean.getSystem().setDescription(dcSystemFormBean.getSystem().getDescription());
    session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, sessionBean);
    dcSystemFormBean.getSystem().setNodesList(sessionBean.getSystem().getNodesList());
    dcSystemFormBean.getSystem().setInputsList(sessionBean.getSystem().getInputsList());
    if (result.hasErrors()) {
      return new ModelAndView("/manageSystems/dc-system-info")
        .addObject(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);

    }
    systemValidator.validate(dcSystemFormBean, result);
    if (result.hasErrors()) {
      return new ModelAndView("/manageSystems/dc-system-info")
        .addObject(DC_SYSTEM_FORM_BEAN_NAME, dcSystemFormBean);
    }
    if (sessionBean.isEditMode()) {
      systemService.updateSystemDescription(sessionBean.getSystem().getName(), sessionBean.getSystem().getDescription());
    } else {
      sessionBean.prepareSystemForSave();
      try {
        systemService.addNewSystem(sessionBean.getSystem());
      } catch (BusinessException ex) {
        String message = "";
        if (ex.getCode() == DUPLICATE_SYSTEM_NAME) {
          message = "System name is used by another system";
        } else {
          message = "Error while saving system, please refer to logs";
        }
        return new ModelAndView("/manageSystems/dc-system-info")
          .addObject(DC_SYSTEM_FORM_BEAN_NAME, sessionBean)
          .addObject(ERRORS, message);
      }
    }
    session.removeAttribute(DC_SYSTEM_FORM_BEAN_NAME);
    return new ModelAndView(new RedirectView("/manageSystem/list?create=true", true));
  }

  @Override
  public int getCount(Object... args) {
    DcSystemSearchCriteria dcSystemSearchCriteria = (DcSystemSearchCriteria) args[0];
    String keyword = dcSystemSearchCriteria.getKeyword();
    if (StringUtils.isNotEmpty(keyword) && !StringUtils.isWhitespace(keyword)) {
      return systemService.searchSystems(keyword.trim()).size();
    } else {
      return systemService.listAllSystems().size();
    }
  }

  @Override
  public List<DcSystemGridEntry> getResultsList(SearchCriteria searchCriteria, int startRowIndex,
                                                int endRowIndex) {
    List<String> result = null;
    DcSystemSearchCriteria dcSystemSearchCriteria = (DcSystemSearchCriteria) searchCriteria;
    String keyword = dcSystemSearchCriteria.getKeyword();
    if (StringUtils.isNotEmpty(keyword) && !StringUtils.isWhitespace(keyword)) {
      result = systemService.searchSystems(keyword.trim(), startRowIndex, endRowIndex);
    } else {
      result = systemService.listSystems(startRowIndex, endRowIndex);
    }
    if (result != null && !result.isEmpty()) {
      return createDcSystemGridEntry(result);
    } else {
      return new ArrayList<DcSystemGridEntry>();
    }
  }

  private List<DcSystemGridEntry> createDcSystemGridEntry(List<String> entryList) {

    List<DcSystemGridEntry> systems = new ArrayList<DcSystemGridEntry>(entryList.size());

    for (String entry : entryList) {
      systems.add(createDcSystemGridEntry(entry));
    }

    return systems;
  }

  private DcSystemGridEntry createDcSystemGridEntry(String entry) {
    return new DcSystemGridEntry(entry);
  }

  private GridBean createSystemGridBean(SearchCriteria searchCriteria, int total) {
    GridBean gridBean = GridUtils.createGridBean(total);
    gridBean.setSearchCriteria(searchCriteria);

    GridUserInterface gridUserInterface = GridUtils.createGridUserInterface(SYSTEM_GRID_XML_CONFIG);
    gridBean.setGridUserInterface(gridUserInterface);
    GridUtils.populateSortColumns(gridBean);

    return gridBean;
  }

  private DcSystemFormBean prepareDcSystemFormBean(VSystem system) {
    DcSystemFormBean dcSystemFormBean = new DcSystemFormBean();
    dcSystemFormBean.setSystem(system);
    dcSystemFormBean.setEditMode(true);
    return dcSystemFormBean;

  }

  private DcSystemFormBean prepareDcSystemFormBean() {
    DcSystemFormBean dcSystemFormBean = new DcSystemFormBean();
    dcSystemFormBean.setEditMode(false);
    return dcSystemFormBean;

  }

}
