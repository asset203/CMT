package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.DBInputStructure;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.InputAccessMethod;
import eg.com.vodafone.model.enums.InputStructureType;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcInputFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcNodeFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcSystemFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.FileType;
import eg.com.vodafone.web.mvc.validator.DcInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static eg.com.vodafone.service.BusinessException.EMPTY_NODE;
import static eg.com.vodafone.service.BusinessException.EMPTY_SYSTEM;
import static eg.com.vodafone.web.mvc.util.CMTConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/19/13
 * Time   : 9:19 AM
 */
@Controller
@RequestMapping("/manageInput/*")
public class ManageDcInputController extends AbstractController {

    @Autowired
    DataCollectionServiceInterface dataCollectionService;
    @Autowired
    DcInputValidator inputValidator;
    @Autowired
    private DataCollectionSystemServiceInterface systemService;

    private final static String ORACLE_RAC_INPUT_NAME ="NON";

    @RequestMapping(value = "/defineNodeInput", method = RequestMethod.POST)
    public ModelAndView defineNodeInput(DcNodeFormBean nodeFormBean, HttpSession session) {
        DcInputFormBean inputFormBean = new DcInputFormBean();
        inputFormBean.setSystemName(nodeFormBean.getSystemName());
        inputFormBean.setSystemDescription(nodeFormBean.getSystemDescription());
        inputFormBean.setNewSystem(nodeFormBean.isNewSystem());
        inputFormBean.setNodeName(nodeFormBean.getNode().getName());
        inputFormBean.setNodeDescription(nodeFormBean.getNode().getDescription());
        inputFormBean.setNewNode(!nodeFormBean.isEditMode());

        inputFormBean.setNodeEnabled(true);
        prepareDcInputFormBean(inputFormBean);
        ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-input-info");
        modelAndView.addObject(DC_INPUT_FORM_BEAN_NAME, inputFormBean);
        session.setAttribute(DC_INPUT_FORM_BEAN_NAME, inputFormBean);
        return modelAndView;
    }

    @RequestMapping(value = "/defineSystemInput", method = RequestMethod.POST)
    public ModelAndView defineSystemInput(DcSystemFormBean systemFormBean, HttpSession session) {
        DcInputFormBean inputFormBean = new DcInputFormBean();
        inputFormBean.setSystemName(systemFormBean.getSystem().getName());
        inputFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
        inputFormBean.setNewSystem(!systemFormBean.isEditMode());
        inputFormBean.setNodeEnabled(false);

        prepareDcInputFormBean(inputFormBean);
        ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-input-info");
        modelAndView.addObject(DC_INPUT_FORM_BEAN_NAME, inputFormBean);
        session.setAttribute(DC_INPUT_FORM_BEAN_NAME, inputFormBean);
        return modelAndView;
    }

    @RequestMapping(value = "/editNodeInput/{inputName}", method = {RequestMethod.POST})
    public ModelAndView editNodeInput(@PathVariable(value = "inputName") String inputName,
                                      DcNodeFormBean nodeFormBean, HttpSession session) {
        DcNodeFormBean sessionNodeFormBean =
                (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
        VInput inputObject = sessionNodeFormBean.getInput(inputName);
        DcInputFormBean dcInputFormBean = prepareDcInputFormBean(inputObject);
        setParentInfo(dcInputFormBean, nodeFormBean);
        ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-input-info");
        modelAndView.addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
        return modelAndView;
    }

    @RequestMapping(value = "/editSystemInput/{inputName}", method = {RequestMethod.POST})
    public ModelAndView editSystemInput(@PathVariable(value = "inputName") String inputName,
                                        DcSystemFormBean systemFormBean, HttpSession session) {
        DcSystemFormBean sessionSystemFormBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        VInput inputObject = sessionSystemFormBean.getInput(inputName);
        DcInputFormBean dcInputFormBean = prepareDcInputFormBean(inputObject);
        setParentInfo(dcInputFormBean, systemFormBean);
        ModelAndView modelAndView = new ModelAndView("/manageSystems/dc-input-info");
        modelAndView.addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
        return modelAndView;
    }

    @RequestMapping(value = "/saveSystemInput", method = RequestMethod.POST)
    public ModelAndView saveSystemDcInput(@Valid DcInputFormBean dcInputFormBean, BindingResult result, HttpSession session) {
      /*------------- validations----------------------*/
        /*if(result.hasErrors()){
            fillBeanOptionsLists(dcInputFormBean);
            return new ModelAndView("/manageSystems/dc-input-info").
                    addObject(DC_INPUT_FORM_BEAN_NAME,dcInputFormBean );
        } */
        DcNodeFormBean nodeBean =
                (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
        DcSystemFormBean systemBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        ModelAndView validationResult = validateInputForm(dcInputFormBean, result, nodeBean, systemBean);
        if (validationResult != null) {
            return validationResult;
        }
   /*--------------------------------end validations-------------------*/
        //session.removeAttribute(DC_INPUT_FORM_BEAN_NAME);
        VInput inputObj = fillVInputObject(dcInputFormBean);
        inputObj.setSystemName(systemBean.getSystem().getName());

        if (dcInputFormBean.isNewSystem()) {
            if (dcInputFormBean.isEditMode()) {
                systemBean.replaceInput(dcInputFormBean.getInputNameBeforeChange(), inputObj);
            } else {
                systemBean.addInput(inputObj);
            }
            return new ModelAndView(new RedirectView("/manageSystem/editSystem?create_input=true", true));
        } else {
            if (dcInputFormBean.isEditMode()) {
                systemService.updateInput(inputObj);
            } else {
                systemService.addNewInput(inputObj);
            }
            StringBuffer viewUrl = new StringBuffer("/manageSystem/editSystem/")
                    .append(systemBean.getSystem().getName()).append("?create_input=true");
            return new ModelAndView(new RedirectView(viewUrl.toString(), true));
        }
    }

    @RequestMapping(value = "/saveNodeInput", method = RequestMethod.POST)
    public ModelAndView saveNodeInput(@Valid DcInputFormBean dcInputFormBean, BindingResult result, HttpSession session) {
      /*------------- validations----------------------*/
        DcNodeFormBean nodeBean =
                (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
        DcSystemFormBean systemBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        ModelAndView validationResult = validateInputForm(dcInputFormBean, result, nodeBean, systemBean);
        if (validationResult != null) {
            return validationResult;
        }
   /*--------------------------------end validations-------------------*/
        VInput inputObj = fillVInputObject(dcInputFormBean);
        inputObj.setSystemName(systemBean.getSystem().getName());
        inputObj.setNodeName(nodeBean.getNode().getName());
        StringBuffer viewUrl = new StringBuffer("/manageNodes/editNode/");
        if (dcInputFormBean.isNewNode() || dcInputFormBean.isNewSystem()) {
            if (dcInputFormBean.isEditMode()) {
                nodeBean.replaceInput(inputObj, dcInputFormBean.getInputNameBeforeChange());
            } else {
                nodeBean.getNode().getInputsList().add(inputObj);
            }
            session.setAttribute(DC_NODE_FORM_BEAN_NAME, nodeBean);
        } else {
            if (dcInputFormBean.isEditMode()) {
                systemService.updateInput(inputObj);
            } else {
                systemService.addNewInput(inputObj);
            }
            viewUrl.append(nodeBean.getNode().getName());
        }
        viewUrl.append("?create=true");
        return new ModelAndView(new RedirectView(viewUrl.toString(), true));
    }

    @RequestMapping(value = "deleteNodeInput/{inputName}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteNodeInput(@PathVariable(value = "inputName") String inputName, HttpSession session) {
        DcNodeFormBean nodeFormBean =
                (DcNodeFormBean) session.getAttribute(DC_NODE_FORM_BEAN_NAME);
        DcSystemFormBean systemFormBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        if (systemFormBean.isEditMode() && nodeFormBean.isEditMode()) {
            try {
                systemService.deleteInput(inputName, nodeFormBean.getNode().getName(), systemFormBean.getSystem().getName());

            } catch (BusinessException exception) {
                String message = "";
                if (exception.getCode() == EMPTY_NODE) {
                    message = "Node must have at least one input ";
                } else {
                    message = "Error deleting Input \'" + inputName + "\' please refer to logs";
                }
                return new ModelAndView("/manageSystems/dc-node-info")
                        .addObject(ERRORS, message)
                        .addObject(DC_NODE_FORM_BEAN_NAME, nodeFormBean);

            } catch (Exception exception) {
                throw new GenericException("Error deleting data collection Input");
            }
        } else {
            nodeFormBean.deleteInput(inputName);
            session.setAttribute(DC_NODE_FORM_BEAN_NAME, nodeFormBean);
        }
        StringBuffer viewUrl = new StringBuffer("/manageNodes/editNode/");
        if (nodeFormBean != null && StringUtils.hasText(nodeFormBean.getNode().getName())) {
            viewUrl.append('/').append(nodeFormBean.getNode().getName());
        }
        //StringBuffer viewUrl = new StringBuffer("/manageNodes/editNode/").append(systemName).append('/').append(nodeName);
        viewUrl.append("?delete=true");
        return new ModelAndView(new RedirectView(viewUrl.toString(), true));
    }

    @RequestMapping(value = "deleteSystemInput/{inputName}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteSystemInput(@PathVariable(value = "inputName") String inputName, HttpSession session) {
        StringBuffer viewUrl = new StringBuffer("/manageSystem/editSystem/");
        DcSystemFormBean systemFormBean =
                (DcSystemFormBean) session.getAttribute(DC_SYSTEM_FORM_BEAN_NAME);
        if (systemFormBean.isEditMode()) {
            try {
                viewUrl.append(systemFormBean.getSystem().getName());
                systemService.deleteInput(inputName, null, systemFormBean.getSystem().getName());

            } catch (BusinessException exception) {
                String message = "";
                if (exception.getCode() == EMPTY_SYSTEM) {
                    message = "System must have at least one input or Node";
                } else {
                    message = "Error deleting Input \'" + inputName + "\' please refer to logs";
                }
                return new ModelAndView("/manageSystems/dc-system-info")
                        .addObject(ERRORS, message)
                        .addObject(systemFormBean);

            } catch (Exception exception) {
                throw new GenericException("Error deleting data collection Input");
            }
        } else {
            systemFormBean.deleteInput(inputName);
            session.setAttribute(DC_SYSTEM_FORM_BEAN_NAME, systemFormBean);
        }
        viewUrl.append("?delete_input=true");
        return new ModelAndView(new RedirectView(viewUrl.toString(), true));
    }

    @RequestMapping(value = "/refreshDataCollectionList", method = RequestMethod.POST)
    public ModelAndView refreshDataCollectionList(DcInputFormBean dcInputFormBean) {
        dcInputFormBean.setCurrentDataCollectionType(-1);
        dcInputFormBean.setDataCollections(new ArrayList<String>());
        fillBeanOptionsLists(dcInputFormBean);
        dcInputFormBean.setEnableAddDataCollection(true);
        return new ModelAndView("/manageSystems/dc-input-info").addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
    }

    @RequestMapping(value = "/addDataCollection/{dataCollectionName}", method = RequestMethod.POST)
    public ModelAndView addDataCollection(@PathVariable(value = "dataCollectionName") String dataCollectionName,
                                          @ModelAttribute(value = DC_INPUT_FORM_BEAN_NAME) DcInputFormBean dcInputFormBean) {
        if (dcInputFormBean.getDataCollections() == null) {
            dcInputFormBean.setDataCollections(new ArrayList<String>());
        }
        VInputStructure inputStructure = null;
        try {
            inputStructure = dataCollectionService.getDataCollection(dataCollectionName.trim());
        } catch (BusinessException e) {
            fillBeanOptionsLists(dcInputFormBean);
            return new ModelAndView("/manageSystems/dc-input-info")
                    .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean)
                    .addObject(INVALID_DC_NAME, "[" + dataCollectionName + "] is Invalid data collection name!");
        }
        dcInputFormBean.getDataCollections().add(dataCollectionName.trim());
        dcInputFormBean.setCurrentDataCollectionType(inputStructure.getType());
        fillBeanOptionsLists(dcInputFormBean);
        if ((dcInputFormBean.getInputAccessMethod() == InputAccessMethod.DB_ACCESS)
                || (dcInputFormBean.getInputAccessMethod() != InputAccessMethod.DB_ACCESS
                && dcInputFormBean.getFileType() == FileType.Excel)) {
            dcInputFormBean.setEnableAddDataCollection(false);
        } else {
            dcInputFormBean.setEnableAddDataCollection(true);
        }
        return new ModelAndView("/manageSystems/dc-input-info")
                .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
    }

    @RequestMapping(value = "/addPath/{newPath}", method = RequestMethod.POST)
    public ModelAndView addPath(@PathVariable(value = "newPath") String newPath, DcInputFormBean dcInputFormBean) {
        if (dcInputFormBean.getPathsList() == null) {
            dcInputFormBean.setPathsList(new ArrayList<String>());
        }
        newPath = newPath.replace(',', '/');
        newPath = newPath.replace(';', ' ');
        if (!dcInputFormBean.getPathsList().contains(newPath.trim())) {
            dcInputFormBean.getPathsList().add(newPath.trim());
        }
        fillBeanOptionsLists(dcInputFormBean);
        return new ModelAndView("/manageSystems/dc-input-info")
                .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
    }

    @RequestMapping(value = "/removeDataCollection/{dataCollectionName}", method = RequestMethod.POST)
    public ModelAndView removeDataCollection(@PathVariable(value = "dataCollectionName") String dataCollectionName,
                                             DcInputFormBean dcInputFormBean) {
        if (dcInputFormBean.getDataCollections() != null
                && !dcInputFormBean.getDataCollections().isEmpty()
                && dcInputFormBean.getDataCollections().contains(dataCollectionName.trim())) {
            dcInputFormBean.getDataCollections().remove(dataCollectionName.trim());
            if (dcInputFormBean.getDataCollections().isEmpty()) {
                dcInputFormBean.setCurrentDataCollectionType(-1);
                dcInputFormBean.setEnableAddDataCollection(true);
            } else {
                if (dcInputFormBean.getInputAccessMethod() != InputAccessMethod.DB_ACCESS
                        && dcInputFormBean.getFileType() != FileType.Excel) {
                    dcInputFormBean.setEnableAddDataCollection(true);
                } else {
                    dcInputFormBean.setEnableAddDataCollection(false);
                }
            }
        }
        fillBeanOptionsLists(dcInputFormBean);
        return new ModelAndView("/manageSystems/dc-input-info")
                .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
    }

    @RequestMapping(value = "/removePath/{path}", method = RequestMethod.POST)
    public ModelAndView removePath(@PathVariable(value = "path") String path,
                                   DcInputFormBean dcInputFormBean) {
        if (dcInputFormBean.getPathsList() != null && !dcInputFormBean.getPathsList().isEmpty()) {
            path = path.replace(',', '/');
            path = path.replace(';', ' ');
            if (dcInputFormBean.getPathsList().contains(path.trim()))
                dcInputFormBean.getPathsList().remove(path.trim());
        }
        fillBeanOptionsLists(dcInputFormBean);
        return new ModelAndView("/manageSystems/dc-input-info")
                .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
    }

    private void fillBeanOptionsLists(DcInputFormBean dcInputFormBean) {
        dcInputFormBean.setFileTypeOptions(getFileTypeOptions());
        dcInputFormBean.setDataBaseTypeOptions(getDatabaseTypeOptions());
        dcInputFormBean.setAccessMethodOptions(getAccessMethodOptions());
        setDataCollectionNamesList(dcInputFormBean);
    }

    private DcInputFormBean prepareDcInputFormBean(VInput inputObject) {

        DcInputFormBean dcInputFormBean = new DcInputFormBean();
        dcInputFormBean.setInputName(inputObject.getId());
        dcInputFormBean.setInputNameBeforeChange(inputObject.getId());
        if (inputObject.getInputId() > 0) {
            dcInputFormBean.setInputID(inputObject.getInputId());
        } else {
            dcInputFormBean.setInputID(-1);
        }
        if (inputObject.getNodeId() > 0) {
            dcInputFormBean.setNodeId(inputObject.getNodeId());
        } else {
            dcInputFormBean.setNodeId(-1);
        }
        dcInputFormBean.setInputAccessMethod(InputAccessMethod.getAccessMethod(inputObject.getAccessMethod()));
        dcInputFormBean.setDataCollections(inputObject.getInputStructureIds());
        dcInputFormBean.setNodeEnabled(inputObject.isPerNode());

        VInputStructure inputStructure = dataCollectionService.getDataCollection(inputObject.getInputStructureIds().get(0));
        dcInputFormBean.setCurrentDataCollectionType(inputStructure.getType());

        if (InputAccessMethod.DB_ACCESS.equals(dcInputFormBean.getInputAccessMethod())) {
            dcInputFormBean.setEnableAddDataCollection(false);
            if(inputObject.getInputName().startsWith(ORACLE_RAC_INPUT_NAME)){
                dcInputFormBean.setTns(inputObject.getServer());
                dcInputFormBean.setDataBaseType(DataBaseType.ORACLE_RAC);
            }
            else
            {    DBInputStructure dbInputStructure =
                    (DBInputStructure) dataCollectionService.getDataCollection(inputObject.getInputStructureIds().get(0));
                dcInputFormBean.setDataBaseType(DataBaseType.getDataBaseType(dbInputStructure.getDbType()));
                dcInputFormBean.setDataBaseName(inputObject.getInputName());
                String[] serverPort = inputObject.getServer().split(":");
                dcInputFormBean.setServer(serverPort[0]);
                if (serverPort.length == 2) {
                    dcInputFormBean.setPort(inputObject.getServer().split(":")[1]);
                }
            }

        } else {
            dcInputFormBean.setFileNamePattern(inputObject.getInputName());
            dcInputFormBean.setServer(inputObject.getServer());
            dcInputFormBean.setPathsList(inputObject.getPathsList());
            dcInputFormBean.setEnableAddDataCollection(true);
            if (inputStructure.getType() == InputStructureType.EXCEL.getTypeCode()) {
                dcInputFormBean.setFileType(FileType.Excel);
                dcInputFormBean.setEnableAddDataCollection(false);
            } else if (inputStructure.getType() == InputStructureType.GENERIC_XML.getTypeCode()) {
                dcInputFormBean.setFileType(FileType.XML);
            } else {
                dcInputFormBean.setFileType(FileType.Text);
            }
        }
        dcInputFormBean.setUserName(inputObject.getUser());
        dcInputFormBean.setPassword(inputObject.getPassword());
        dcInputFormBean.setInputObject(inputObject);
        dcInputFormBean.setEditMode(true);
        fillBeanOptionsLists(dcInputFormBean);
        return dcInputFormBean;
    }

    private DcInputFormBean prepareDcInputFormBean(DcInputFormBean dcInputFormBean) {
        //DcInputFormBean dcInputFormBean = new DcInputFormBean();
        dcInputFormBean.setEditMode(false);
        dcInputFormBean.setInputObject(new VInput());
        dcInputFormBean.setCurrentDataCollectionType(-1);
        dcInputFormBean.setEnableAddDataCollection(true);
        dcInputFormBean.setInputAccessMethod(InputAccessMethod.DB_ACCESS);
        dcInputFormBean.setDataBaseType(DataBaseType.ORACLE);
        fillBeanOptionsLists(dcInputFormBean);
        return dcInputFormBean;
    }

    private VInput fillVInputObject(DcInputFormBean formBean) {
        VInput inputObject = new VInput();
        if (formBean.getInputID() > 0) {
            inputObject.setInputId(formBean.getInputID());
        }
        //Riham.Maksoud:Ticket# 4499 remove the spaces before saving input name
        if(formBean.getInputName() !=null)
            inputObject.setId(formBean.getInputName().trim());
        inputObject.setAccessMethod(formBean.getInputAccessMethod().getName());

        if (InputAccessMethod.DB_ACCESS.equals(formBean.getInputAccessMethod())) {
            List<String> paths = new ArrayList<String>();
            paths.add(formBean.getDataBaseType().getPath());
            if (DataBaseType.ORACLE_RAC.equals(formBean.getDataBaseType())) {
                inputObject.setInputName(ORACLE_RAC_INPUT_NAME);
                inputObject.setServer(formBean.getTns());
            }
            else
            {
                inputObject.setInputName(formBean.getDataBaseName());
                /**
                 * This section was added by Alia.Adel on 20130916
                 * since MySQL connection already adds a default port 3306 in execution
                 */
                inputObject.setServer(formBean.getServer());
                if(StringUtils.hasText(formBean.getPort())){
                    StringBuffer serverPort
                            = new StringBuffer(formBean.getServer()).append(':').append(formBean.getPort());
                    inputObject.setServer(serverPort.toString());
                }
            }
            //Added by Alia.Adel on 2013.06.24 to fix issue#396877
            inputObject.setPathsList(paths);
        } else {
            inputObject.setServer(formBean.getServer());
            inputObject.setInputName(formBean.getFileNamePattern());
            inputObject.setPathsList(formBean.getPathsList());
        }
        inputObject.setUser(formBean.getUserName());
        inputObject.setPassword(formBean.getPassword());
        inputObject.setInputStructureIds(formBean.getDataCollections());
        if (!inputObject.getInputStructureIds().isEmpty()) {
            VInputStructure inputStructure = dataCollectionService.getDataCollection(inputObject.getInputStructureIds().get(0));
            inputObject.setType(inputStructure.getType());
        }
        inputObject.setPerNode(formBean.isNodeEnabled());
        if (formBean.getNodeId() > 0) {
            inputObject.setNodeId(formBean.getNodeId());
        }
        return inputObject;
    }

    private String validateUniqueInputName(String inputName, DcNodeFormBean nodeBean, DcSystemFormBean systemBean) {
        String error = "";
        if (nodeBean != null) {
            if (nodeBean.getNode() != null && nodeBean.getNode().getInputsList() != null
                    && !nodeBean.getNode().getInputsList().isEmpty()) {
                List<String> nodeInputs = nodeBean.getNode().getInputsNames();
                for (String input : nodeInputs) {
                    if (inputName.trim().equalsIgnoreCase(input)) {
                        error = " Input name must be unique per Node ";
                        break;
                    }
                }
            }
        } else {
            if (systemBean != null && systemBean.getSystem() != null
                    && systemBean.getSystem().getInputsList() != null
                    && !systemBean.getSystem().getInputsList().isEmpty()) {

                List<String> systemInputs = systemBean.getSystem().getInputsNames();
                for (String input : systemInputs) {
                    if (inputName.trim().equalsIgnoreCase(input)) {
                        error = " Input name must be unique per system";
                        break;
                    }
                }
            }
        }
        return error;
    }

    private void setDataCollectionNamesList(DcInputFormBean inputFormBean) {
        if (inputFormBean.getCurrentDataCollectionType() >= 1) {
            inputFormBean.setDataCollectionNames(
                    dataCollectionService.listDataCollectionsByType(
                            InputStructureType.getInputStructureType(inputFormBean.getCurrentDataCollectionType()), inputFormBean.isNodeEnabled()));
        } else {
            if (inputFormBean.getInputAccessMethod() == null) {
                inputFormBean.setDataCollectionNames(dataCollectionService.listAllDataCollections(inputFormBean.isNodeEnabled()));
            } else if (inputFormBean.getInputAccessMethod() == InputAccessMethod.DB_ACCESS) {
                if (inputFormBean.getDataBaseType() != null) {
                    if(inputFormBean.getDataBaseType() == DataBaseType.ORACLE
                            || inputFormBean.getDataBaseType() == DataBaseType.ORACLE_RAC)
                    {
                          List<Long> dbTypes = new ArrayList<Long>();
                        dbTypes.add(DataBaseType.ORACLE.getTypeCode());
                        dbTypes.add(DataBaseType.ORACLE_RAC.getTypeCode());
                        inputFormBean.setDataCollectionNames
                                (dataCollectionService.getDataBaseDataCollection(dbTypes,
                                        inputFormBean.isNodeEnabled()));
                    }
                    else
                    {
                        inputFormBean.setDataCollectionNames
                                (dataCollectionService.getDataBaseDataCollection(inputFormBean.getDataBaseType(),
                                        inputFormBean.isNodeEnabled()));
                    }
                } else {
                    inputFormBean.setDataCollectionNames(dataCollectionService.listAllDatabaseDataCollections(inputFormBean.isNodeEnabled()));
                }
                if (inputFormBean.getPathsList() != null && !inputFormBean.getPathsList().isEmpty()) {
                    inputFormBean.getPathsList().clear();
                }
            } else {
                if (inputFormBean.getFileType() == FileType.Excel) {
                    inputFormBean.setDataCollectionNames(dataCollectionService.listDataCollectionsByType(InputStructureType.EXCEL, inputFormBean.isNodeEnabled()));
                } else if (inputFormBean.getFileType() == FileType.XML) {
                    inputFormBean.setDataCollectionNames(dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_XML, inputFormBean.isNodeEnabled()));
                } else if (inputFormBean.getFileType() == FileType.Text) {
                    List<InputStructureType> types = new ArrayList<InputStructureType>();
                    types.add(InputStructureType.GENERIC_TEXT);
                    types.add(InputStructureType.DIRECT_TEXT);
                    types.add(InputStructureType.TEXT);
                    inputFormBean.setDataCollectionNames(dataCollectionService.listDataCollectionsByType(types, inputFormBean.isNodeEnabled()));
                } else {
                    inputFormBean.setDataCollectionNames(dataCollectionService.listAllNonDataBaseDataCollection(inputFormBean.isNodeEnabled()));
                }
            }
        }

        if (inputFormBean.getDataCollections() != null && !inputFormBean.getDataCollections().isEmpty()) {
            for (String dataCollection : inputFormBean.getDataCollections()) {
                if (inputFormBean.getDataCollectionNames().contains(dataCollection)) {
                    inputFormBean.getDataCollectionNames().remove(dataCollection);
                }
            }
        }
    }

    private Map<String, String> getFileTypeOptions() {
        Map<String, String> fileTypes = new LinkedHashMap<String, String>();
        for (FileType type : FileType.values()) {
            fileTypes.put(type.toString(), type.getLabel());
        }
        return fileTypes;
    }

    private Map<String, String> getDatabaseTypeOptions() {
        Map<String, String> databaseTypes = new LinkedHashMap<String, String>();
        for (DataBaseType type : DataBaseType.values()) {
            databaseTypes.put(type.toString(), type.toString());
        }
        return databaseTypes;
    }

    private Map<String, String> getAccessMethodOptions() {
        Map<String, String> accessMethodOptions = new LinkedHashMap<String, String>();
        for (InputAccessMethod type : InputAccessMethod.values()) {
            accessMethodOptions.put(type.toString(), type.getName());
        }
        return accessMethodOptions;
    }

    private void setParentInfo(DcInputFormBean inputFormBean, DcNodeFormBean nodeFormBean) {
        inputFormBean.setNodeName(nodeFormBean.getNode().getName());
        inputFormBean.setNodeDescription(nodeFormBean.getNode().getDescription());
        inputFormBean.setNewNode(!nodeFormBean.isEditMode());

        inputFormBean.setSystemName(nodeFormBean.getSystemName());
        inputFormBean.setSystemDescription(nodeFormBean.getSystemDescription());
        inputFormBean.setNewSystem(nodeFormBean.isNewSystem());
    }

    private void setParentInfo(DcInputFormBean inputFormBean, DcSystemFormBean systemFormBean) {
        inputFormBean.setSystemName(systemFormBean.getSystem().getName());
        inputFormBean.setSystemDescription(systemFormBean.getSystem().getDescription());
        inputFormBean.setNewSystem(!systemFormBean.isEditMode());
    }

    private ModelAndView validateInputForm(DcInputFormBean dcInputFormBean, BindingResult result, DcNodeFormBean nodeBean, DcSystemFormBean systemBean) {

        inputValidator.validate(dcInputFormBean, result);
       /* if (result.hasErrors()) {
            fillBeanOptionsLists(dcInputFormBean);
            return new ModelAndView("/manageSystems/dc-input-info").
                    addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
        } */

        if ((StringUtils.hasText(dcInputFormBean.getInputNameBeforeChange()) && !dcInputFormBean.getInputNameBeforeChange().equals(dcInputFormBean.getInputName()))
                || !StringUtils.hasText(dcInputFormBean.getInputNameBeforeChange())) {
            String error = validateUniqueInputName(dcInputFormBean.getInputName(), nodeBean, systemBean);
            if (StringUtils.hasText(error)) {
                fillBeanOptionsLists(dcInputFormBean);
                return new ModelAndView("/manageSystems/dc-input-info")
                        .addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean)
                        .addObject(ERRORS, error);
            }
        }
        if (result.hasErrors()) {
            fillBeanOptionsLists(dcInputFormBean);
            return new ModelAndView("/manageSystems/dc-input-info").
                    addObject(DC_INPUT_FORM_BEAN_NAME, dcInputFormBean);
        }
        return null;
    }
}
