package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.DCLogExternalContact;
import eg.com.vodafone.model.KPIContact;
import eg.com.vodafone.model.KPINotificationList;
import eg.com.vodafone.model.User;
import eg.com.vodafone.model.enums.NotificationType;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.KPINotificationService;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.KPINotificationForm;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.validator.KPINotificationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/27/13
 * Time: 12:02 PM
 */
@Controller
@RequestMapping("/kpiNotifications/*")
public class KPINotificationListController extends AbstractController {

    private final static Logger LOGGER
            = LoggerFactory.getLogger(KPINotificationListController.class);
    private final static String KPI_FORM_BEAN_NAME = "formBean";
    private final static String PAGE_MANAGE_KPI_LIST = "/kpi/manageKPIContacts";
    private final static String SYSTEM_LIST_KEY = "systemList";
    private final static String NODE_LIST_KEY = "nodeList";
    private final static String NOTIFICATION_TYPES_KEY = "notificationTypes";
    private final static String USERS_LIST_KEY = "usersList";
    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;
    @Autowired
    private UserService userService;
    @Autowired
    private KPINotificationService kpiNotificationService;
    @Autowired
    private KPINotificationValidator kpiNotificationValidator;

    /**
     * KPI main page for managing notification lists
     *
     * @return loaded Model & View with Search criteria
     */
    @RequestMapping(value = "manageLists")
    public ModelAndView begin() {
        LOGGER.debug("KPI main page for managing notification lists");
        ModelAndView modelAndView = new ModelAndView(PAGE_MANAGE_KPI_LIST);
        loadKPINotificationFormBean(modelAndView);
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
        ModelAndView modelAndView = new ModelAndView("/kpi/nodeList");
        KPINotificationForm formBean = new KPINotificationForm();
        if (StringUtils.hasText(systemName)) {
            formBean.setSystemName(systemName);
            modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
            modelAndView.addObject(NODE_LIST_KEY,
                    dataCollectionService.getSystemNodes(systemName,false ));
        }
        return modelAndView;
    }

    /**
     * Search for KPI notification list associated with the given node ID
     *
     * @param formBean user filled form
     * @return
     */
    @RequestMapping(value = "findList", method = RequestMethod.POST)
    public ModelAndView findNotificationList(
            @ModelAttribute(value = KPI_FORM_BEAN_NAME) KPINotificationForm formBean, BindingResult result) {
        LOGGER.debug("Values passed for search are system: {} and node ID: {}",
                formBean.getSystemName(), formBean.getNodeID());
        ModelAndView modelAndView = new ModelAndView(PAGE_MANAGE_KPI_LIST);
        modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
        loadKPINotificationFormBean(modelAndView);

        kpiNotificationValidator.validateSearchForm(formBean, result);
        if (result.hasErrors()) {
            return modelAndView;
        }

        formBean.setKpiNotificationList(
                kpiNotificationService.getKPINotificationListByNodeID(formBean.getNodeID()));
        modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
        loadKPINotificationFormBean(modelAndView);

        return modelAndView;
    }

    /**
     * Delete contact/s from the KPI notification list
     *
     * @param systemName
     * @return
     */
    @RequestMapping(value = "deleteUsers", method = RequestMethod.POST)
    public ModelAndView deleteUserFromContactList(
            @ModelAttribute("systemName") String systemName,
            @ModelAttribute("nodeID") int nodeID,
            @ModelAttribute("deleteID") String deleteID) {
        KPINotificationForm formBean = new KPINotificationForm();
        formBean.setSystemName(systemName);
        formBean.setNodeID(nodeID);
        formBean.setUserIDToDelete(deleteID);

        LOGGER.debug("Received contact id/s as {} ", formBean.getUserIDToDelete());

        if (StringUtils.hasText(formBean.getUserIDToDelete())) {
            String[] idList = formBean.getUserIDToDelete().split(",");
            Integer[] idIntList = new Integer[idList.length];
            for (int i = 0; i < idList.length; i++) {
                idIntList[i] = Integer.parseInt(idList[i]);
            }
            LOGGER.debug("List of IDs to delete: " + idIntList);

            List<Integer> resultValList =
                    kpiNotificationService.deleteUsersFromContactList(idIntList);

            //Check result
            LOGGER.debug("Operation result is {}", resultValList);

            boolean errorExist = false;
            for (int resultVal : resultValList) {
                if (resultVal != 1) {
                    errorExist = true;
                    break;
                }
            }
            ModelAndView modelAndView = loadViewPageAfterUpdate(formBean);

            if (!errorExist) {
                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        "<div class=\"SuccessMsg\">User\\s were deleted successfully</div>");
            } else {
                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        "<div class=\"ErrorMsg\">The action has not been performed completely</div>");
            }

            LOGGER.debug("Deletion done");


            return modelAndView;
        } else {
            throw new GenericException("Empty parameters received");
        }
    }

    /**
     * Load registered user details using Ajax
     *
     * @param username
     * @param systemName
     * @param nodeID
     * @return
     */
    @RequestMapping(value = "loadRegisteredUser",
            method = RequestMethod.POST)
    public ModelAndView loadRegisteredUserDetails(@ModelAttribute(value = "username") String username,
                                                  @ModelAttribute(value = "systemName") String systemName,
                                                  @ModelAttribute(value = "nodeID") String nodeID) {

        LOGGER.debug("Values received to view registered user details: Username:{},  " +
                "System Name:{}, Node ID:{}",
                new Object[]{username, systemName, nodeID});

        ModelAndView modelAndView = new ModelAndView("/kpi/addRegisteredUser");
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());

        if (StringUtils.hasText(username) && StringUtils.hasText(nodeID)) {

            KPINotificationForm kpiNotificationForm = new KPINotificationForm();
            kpiNotificationForm.setKpiNotificationList(
                    kpiNotificationService.getKPINotificationListByNodeID(Integer.valueOf(nodeID)));
            kpiNotificationForm.setRegisteredUser(new User());
            kpiNotificationForm.setSystemName(systemName);
            kpiNotificationForm.setNodeID(Integer.valueOf(nodeID));

            User user = userService.findUserByUserName(username);
            if (user != null) {
                LOGGER.debug("User found:\nID:{}\nUsername:{}\nEmail:{}\nMobile:{}",
                        new Object[]{user.getId(), user.getUsername(), user.getEmail(), user.getMobile()});

                //Check that the user is not already added to the contact list
                if (kpiNotificationForm.getKpiNotificationList() != null
                        && kpiNotificationForm.getKpiNotificationList().getContactList() != null
                        && !kpiNotificationForm.getKpiNotificationList().getContactList().isEmpty()) {

                    for (KPIContact contact : kpiNotificationForm.getKpiNotificationList().getContactList()) {
                        if (contact.isRegisteredUser()
                                && contact.getRegisteredContact().getUsername().equals(user.getUsername())) {
                            LOGGER.debug("the user name \"{}\" you entered already exists in the list", username);
                            modelAndView.addObject("VALIDATION_ERROR",
                                    "<div class=\"ErrorMsg\">The username you entered already exists in the list</div>");
                            kpiNotificationForm.setRegisteredUser(new User());
                            modelAndView.addObject(KPI_FORM_BEAN_NAME, kpiNotificationForm);
                            return modelAndView;
                        }
                    }
                }

                kpiNotificationForm.setRegisteredUser(user);
            } else {
                LOGGER.debug("the user name \"{}\" you entered is not correct", username);
                modelAndView.addObject("VALIDATION_ERROR",
                        "<div class=\"ErrorMsg\">The username you entered does not exist</div>");
            }

            modelAndView.addObject(KPI_FORM_BEAN_NAME, kpiNotificationForm);
        } else {
            modelAndView.addObject(KPI_FORM_BEAN_NAME, new KPINotificationForm());
            modelAndView.addObject("VALIDATION_ERROR",
                    "<div class=\"ErrorMsg\">No user was passed</div>");

        }
        return modelAndView;
    }

    /**
     * Add registered user to KPI notification list
     *
     * @param formBean
     * @param result
     * @return
     */
    @RequestMapping(value = "addRegisteredUser", method = RequestMethod.POST)
    public ModelAndView addRegisteredUser(
            @Valid @ModelAttribute(value = KPI_FORM_BEAN_NAME) KPINotificationForm formBean,
            BindingResult result) {

        formBean.setRegisteredUserAction("true");

        ModelAndView modelAndView = loadViewPageAfterUpdate(formBean);

        kpiNotificationValidator.validate(formBean, result);
        if (result.hasErrors()) {
            LOGGER.error("Validation errors exist");
            modelAndView.addObject("VALIDATION_ERROR_ADD_REGISTERED", "true");
            return modelAndView;
        }

        LOGGER.debug("User details for addition:\nUsername:{}\nMobile:{}\nEmail:{}\nNotify by:{}\nNode ID:{}",
                new Object[]{formBean.getRegisteredUser().getUsername(), formBean.getRegisteredUser().getMobile(),
                        formBean.getRegisteredUser().getEmail(), formBean.getNotificationType(), formBean.getNodeID()});

        List<Integer> resultValList = kpiNotificationService.addRegisteredUserToNotificationList(
                formBean.getRegisteredUser(), formBean.getNotificationType(), formBean.getNodeID());

        clearForm(formBean);
        modelAndView = loadViewPageAfterUpdate(formBean);

        ///Check result
        LOGGER.debug("Operation result is {}", resultValList);

        boolean errorExist = false;
        for (int resultVal : resultValList) {
            if (resultVal != 1) {
                errorExist = true;
                break;
            }
        }
        if (!errorExist) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User has been added to the list successfully</div>");
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }
        return modelAndView;

    }

    /**
     * Add external usr to KPI notification list
     *
     * @param formBean
     * @param result
     * @return
     */
    @RequestMapping(value = "addExternalUser", method = RequestMethod.POST)
    public ModelAndView addExternalUser(
            @Valid @ModelAttribute(value = KPI_FORM_BEAN_NAME) KPINotificationForm formBean,
            BindingResult result) {

        formBean.setRegisteredUserAction("false");
        ModelAndView modelAndView = loadViewPageAfterUpdate(formBean);

        kpiNotificationValidator.validate(formBean, result);
        if (result.hasErrors()) {
            LOGGER.error("Validation errors exist");
            modelAndView.addObject("VALIDATION_ERROR_ADD_EXT", "true");
            return modelAndView;
        }

        LOGGER.debug("External user details to add:\nUsername:{}\nMobile:{}\nEmail:{}\nNotify by:{}\nNode ID:{}",
                new Object[]{formBean.getExternalUser().getUserName(), formBean.getExternalUser().getMobileNum(),
                        formBean.getExternalUser().geteMail(), formBean.getNotificationType(), formBean.getNodeID()});

        List<Integer> resultValList = kpiNotificationService.addExternalUserToNotificationList(
                formBean.getExternalUser(), formBean.getNotificationType(), formBean.getNodeID());

        clearForm(formBean);
        loadViewPageAfterUpdate(formBean);
        modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);

        //Check result
        LOGGER.debug("Operation result is {}", resultValList);

        boolean errorExist = false;
        for (int resultVal : resultValList) {
            if (resultVal != 1) {
                errorExist = true;
                break;
            }
        }

        if (!errorExist) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User has been added to the list successfully</div>");
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }
        return modelAndView;

    }

    /**
     * load registered user contact
     *
     * @param contactID
     * @return ModelAndView Object
     */
    @RequestMapping(value = "editRegUser/{contactID}", method = RequestMethod.GET)
    public ModelAndView goToEditRegisteredUser(@PathVariable String contactID) {
        LOGGER.debug("ID for edit:" + contactID);
        ModelAndView modelAndView = new ModelAndView("/kpi/editRegisteredUser");
        if (StringUtils.hasText(contactID)) {
            KPIContact contact
                    = kpiNotificationService.getContactByID(Integer.valueOf(contactID));
            if (contact != null) {
                LOGGER.debug("User object received: "
                        + ((contact.getRegisteredContact() != null) ?
                        contact.getRegisteredContact().getUsername() : null));

                KPINotificationForm formBean = new KPINotificationForm();
                List<KPIContact> contacts = new ArrayList<KPIContact>();
                contacts.add(contact);
                formBean.setKpiNotificationList(new KPINotificationList());
                formBean.getKpiNotificationList().setNode(
                        kpiNotificationService.getNodeByContactID(contact.getId()));
                formBean.setSystemName(formBean.getKpiNotificationList().getNode().getSystemName());
                formBean.setNodeID(Integer.valueOf(
                        String.valueOf(formBean.getKpiNotificationList().getNode().getId())));
                formBean.getKpiNotificationList().setContactList(contacts);
                formBean.setUpdatedRegisteredUser(contact.getRegisteredContact());
                formBean.setNotificationType(contact.getNotificationType());
                formBean.setContactListID(String.valueOf(contact.getId()));
                modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
                modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "editRegisteredUser", method = RequestMethod.POST)
    public ModelAndView editRegisteredUser(
            @Valid @ModelAttribute(value = KPI_FORM_BEAN_NAME) KPINotificationForm formBean,
            BindingResult result) {

        if (formBean != null
                && formBean.getUpdatedRegisteredUser() != null) {
            LOGGER.debug("User object:\nid:{}\nusername:{}\nemail:{}\nmobile:{}\nNotification type:{}",
                    new Object[]{formBean.getUpdatedRegisteredUser().getId(),
                            formBean.getUpdatedRegisteredUser().getUsername(),
                            formBean.getUpdatedRegisteredUser().getEmail(),
                            formBean.getUpdatedRegisteredUser().getMobile(),
                            formBean.getNotificationType()});


            formBean.setRegisteredUserAction("true");

            ModelAndView modelAndView = loadViewPageAfterUpdate(formBean);

            kpiNotificationValidator.validateUpdatedUserObject(formBean, result);
            if (result.hasErrors()) {
                LOGGER.error("Validation errors exist");
                modelAndView.addObject("VALIDATION_ERROR_EDIT_REGISTERED", "true");
                return modelAndView;
            }

            List<Integer> resultValList =
                    kpiNotificationService.updateRegisteredContact(formBean.getUpdatedRegisteredUser(),
                            Integer.valueOf(formBean.getContactListID()),
                            formBean.getNotificationType());

            clearForm(formBean);
            loadViewPageAfterUpdate(formBean);
            modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);

            //Check result
            LOGGER.debug("Operation result is {}", resultValList);

            boolean errorExist = false;
            for (int resultVal : resultValList) {
                if (resultVal != 1) {
                    errorExist = true;
                    break;
                }
            }

            if (!errorExist) {
                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        "<div class=\"SuccessMsg\">User has been updated successfully</div>");
            } else {
                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        "<div class=\"ErrorMsg\">The action has not been performed</div>");
            }
            return modelAndView;

        } else {
            throw new GenericException("Null object received");
        }

    }

    /**
     * Load External user details for edit operation
     *
     * @param contactID
     * @return ModelAndView Object
     */
    @RequestMapping(value = "editExtUser/{contactID}", method = RequestMethod.GET)
    public ModelAndView goToEditExternalUser(@PathVariable String contactID) {
        LOGGER.debug("ID for edit:" + contactID);
        ModelAndView modelAndView = new ModelAndView("/kpi/editExternalUser");
        if (StringUtils.hasText(contactID)) {
            KPIContact contact
                    = kpiNotificationService.getContactByID(Integer.valueOf(contactID));
            if (contact != null) {
                LOGGER.debug("User object received: "
                        + ((contact.getExternalContact() != null) ?
                        contact.getExternalContact().getUserName() : null));

                KPINotificationForm formBean = new KPINotificationForm();
                List<KPIContact> contacts = new ArrayList<KPIContact>();
                contacts.add(contact);

                formBean.setKpiNotificationList(new KPINotificationList());
                formBean.getKpiNotificationList().setNode(
                        kpiNotificationService.getNodeByContactID(contact.getId()));
                formBean.setSystemName(formBean.getKpiNotificationList().getNode().getSystemName());
                formBean.setNodeID(Integer.valueOf(
                        String.valueOf(formBean.getKpiNotificationList().getNode().getId())));
                formBean.getKpiNotificationList().setContactList(contacts);
                formBean.setUpdatedExternalUser(contact.getExternalContact());
                formBean.setNotificationType(contact.getNotificationType());
                formBean.setContactListID(String.valueOf(contact.getId()));

                modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
                modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "editExternalUser", method = RequestMethod.POST)
    public ModelAndView editExternalUser(
            @Valid @ModelAttribute(value = KPI_FORM_BEAN_NAME) KPINotificationForm formBean,
            BindingResult result) {

        if (formBean != null
                && formBean.getUpdatedExternalUser() != null) {
            LOGGER.debug("User object:\nid:{}\nusername:{}\nemail:{}\nmobile:{}\nNotification type:{}",
                    new Object[]{formBean.getUpdatedExternalUser().getId(),
                            formBean.getUpdatedExternalUser().getUserName(),
                            formBean.getUpdatedExternalUser().geteMail(),
                            formBean.getUpdatedExternalUser().getMobileNum(),
                            formBean.getNotificationType()});

            formBean.setRegisteredUserAction("false");

            ModelAndView modelAndView = loadViewPageAfterUpdate(formBean);

            kpiNotificationValidator.validateUpdatedUserObject(formBean, result);
            if (result.hasErrors()) {
                LOGGER.error("Validation errors exist");
                modelAndView.addObject("VALIDATION_ERROR_EDIT_EXTERNAL", "true");
                return modelAndView;
            }
            KPIContact contact = new KPIContact();
            contact.setId(Integer.valueOf(formBean.getContactListID()));
            contact.setExternalContact(formBean.getUpdatedExternalUser());
            contact.setNotificationType(formBean.getNotificationType());

            List<Integer> resultValList = kpiNotificationService.updateExternalContact(contact, formBean.getNodeID());

            formBean.setKpiNotificationList(
                    kpiNotificationService.getKPINotificationListByNodeID(formBean.getNodeID()));

            clearForm(formBean);
            loadViewPageAfterUpdate(formBean);
            modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);

            //Check result
            LOGGER.debug("Operation result is {}", resultValList);

            boolean errorExist = false;
            for (int resultVal : resultValList) {
                if (resultVal != 1) {
                    errorExist = true;
                    break;
                }
            }

            if (!errorExist) {
                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        "<div class=\"SuccessMsg\">User has been updated successfully</div>");
            } else {
                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        "<div class=\"ErrorMsg\">The action has not been performed</div>");
            }
            return modelAndView;

        } else {
            throw new GenericException("Null object received");
        }

    }

    /**
     * Fill ModelAndView object with
     * the values needed for the main page display
     *
     * @param modelAndView
     */
    private void loadKPINotificationFormBean(ModelAndView modelAndView) {
        if (modelAndView == null) {
            modelAndView = new ModelAndView(PAGE_MANAGE_KPI_LIST);
        }
        modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
        modelAndView.addObject(USERS_LIST_KEY, getAllRegisteredUsers());
        if (modelAndView.getModel().get(KPI_FORM_BEAN_NAME) instanceof KPINotificationForm) {
            KPINotificationForm formBean = (KPINotificationForm) modelAndView.getModel().get(KPI_FORM_BEAN_NAME);
            if (StringUtils.hasText(formBean.getSystemName())) {
                modelAndView.addObject(NODE_LIST_KEY,
                        dataCollectionService.getSystemNodes(formBean.getSystemName(),false ));
            }
        } else {
            modelAndView.addObject(KPI_FORM_BEAN_NAME, new KPINotificationForm());
        }
    }

    /**
     * Reload page after add/update/delete operation
     *
     * @param formBean user form bean
     * @return loaded ModelAndView Object
     */
    private ModelAndView loadViewPageAfterUpdate(KPINotificationForm formBean) {
        ModelAndView modelAndView = new ModelAndView(PAGE_MANAGE_KPI_LIST);
        modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
        modelAndView.addObject(USERS_LIST_KEY, getAllRegisteredUsers());
        if (StringUtils.hasText(formBean.getSystemName())) {
            modelAndView.addObject(NODE_LIST_KEY,
                    dataCollectionService.getSystemNodes(formBean.getSystemName(), false));
        }
        //Load notification list
        if (formBean.getNodeID() > 0) {
            LOGGER.debug("Node ID found ({}) & will retrieve the update notification list", formBean.getNodeID());
            formBean.setKpiNotificationList(
                    kpiNotificationService.getKPINotificationListByNodeID(formBean.getNodeID()));
            modelAndView.addObject(KPI_FORM_BEAN_NAME, formBean);
        }

        return modelAndView;
    }

    /**
     * Clears all date from Registered/External user
     *
     * @param formBean
     */
    private void clearForm(KPINotificationForm formBean) {
        formBean.setRegisteredUser(new User());
        formBean.setUpdatedRegisteredUser(new User());
        formBean.setExternalUser(new DCLogExternalContact());
        formBean.setUpdatedExternalUser(new DCLogExternalContact());
        formBean.setNotificationType("");
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

    /**
     * puts  Notification Type enum values in a Map
     * for the checkbox option display
     *
     * @return map
     */
    private Map<String, String> getNotificationTypesMap() {
        Map<String, String> notificationTypes = new LinkedHashMap<String, String>();
        for (NotificationType type : NotificationType.values()) {
            notificationTypes.put(type.getNotificationTypeStr(), type.getNotificationTypeStr());
        }
        return notificationTypes;
    }

    /**
     * Get All system registered users
     *
     * @return all registered user in CMT system
     */
    private List<User> getAllRegisteredUsers() {
        return userService.getUserList();
    }


}
