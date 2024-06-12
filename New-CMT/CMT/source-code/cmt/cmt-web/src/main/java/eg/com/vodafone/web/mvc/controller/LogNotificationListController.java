package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.LogType;
import eg.com.vodafone.model.enums.NotificationType;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.DCLogNotificationService;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.DCLogNotificationForm;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.validator.DCLogNotificationValidator;
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
 * Date: 4/6/13
 * Time: 2:12 PM
 */
@Controller
@RequestMapping("/logNotification/*")
public class LogNotificationListController extends AbstractController {

    private final static Logger logger = LoggerFactory.getLogger(LogNotificationListController.class);
    private final static String SYSTEM_LIST_KEY = "systemList";
    private final static String ERROR_CODES_LIST_KEY = "errorCodes";
    private final static String SEARCH_BEAN_KEY = "dcLogNotificationForm";
    private final static String NOTIFICATION_TYPES_KEY = "notificationTypes";
    private final static String USERS_LIST_KEY = "usersList";
    private final static String PAGE_NOTIFY_LIST = "/logmanager/manageLogNotfList";
    @Autowired
    private DCLogNotificationService dcLogNotificationService;
    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;
    @Autowired
    private UserService userService;
    @Autowired
    private DCLogNotificationValidator dcLogNotificationValidator;

    @RequestMapping(value = "manageLists")
    public ModelAndView manageNotificationLists(
            @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm) {

        logger.info("Search filter:\nSystem Name:{}\nLog Type:{}\nError Code:{}",
                new Object[]{dcLogNotificationForm.getSystemName(),
                        dcLogNotificationForm.getLogType(), dcLogNotificationForm.getErrorCodeID()});

        return loadManageNotificationListModel(dcLogNotificationForm);
    }

    @RequestMapping(value = "findList")
    public ModelAndView findNotificationLists(
            @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm,
            BindingResult result) {

        logger.info("Search filter:\nSystem Name:{}\nLog Type:{}\nError Code:{}",
                new Object[]{dcLogNotificationForm.getSystemName(),
                        dcLogNotificationForm.getLogType(), dcLogNotificationForm.getErrorCodeID()});


        ModelAndView modelAndView = new ModelAndView(PAGE_NOTIFY_LIST);
        dcLogNotificationValidator.validateSearchForm(dcLogNotificationForm, result);
        if (result.hasErrors()) {
            logger.debug("validation errors exist {} ", result);
            loadLookupMaps(modelAndView);
            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
            return modelAndView;
        }


        return loadManageNotificationListModel(dcLogNotificationForm);
    }

    @RequestMapping(value = "deleteUsers", method = RequestMethod.POST)
    public ModelAndView deleteUserFromContactList(
            @ModelAttribute(value = "notificationID") String notificationID,
            @ModelAttribute(value = "deleteID") String deleteID) {

        DCLogNotificationForm dcLogNotificationForm = new DCLogNotificationForm();
        dcLogNotificationForm.setDcLogNotificationList(new DCLogNotificationList());
        dcLogNotificationForm.getDcLogNotificationList().setId(Integer.parseInt(notificationID));
        dcLogNotificationForm.setUserIDToDelete(deleteID);

        logger.debug("Received contact id/s as {} for notification list id: {}",
                dcLogNotificationForm.getUserIDToDelete(),
                dcLogNotificationForm.getDcLogNotificationList().getId());

        if (StringUtils.hasText(dcLogNotificationForm.getUserIDToDelete())) {
            String[] idList = dcLogNotificationForm.getUserIDToDelete().split(",");
            Integer[] idIntList = new Integer[idList.length];
            for (int i = 0; i < idList.length; i++) {
                idIntList[i] = Integer.parseInt(idList[i]);
            }
            logger.debug("List of IDs to delete: " + idIntList);

            List<Integer> resultValList =
                    dcLogNotificationService.deleteUsersFromContactList(idIntList);

            //Check result
            logger.debug("Operation result is {}", resultValList);

            boolean errorExist = false;
            for (int resultVal : resultValList) {
                if (resultVal != 1) {
                    errorExist = true;
                    break;
                }
            }
            ModelAndView modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);

            if (!errorExist) {
                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        "<div class=\"SuccessMsg\">User\\s were deleted successfully</div>");
            } else {
                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        "<div class=\"ErrorMsg\">The action has not been performed completely</div>");
            }

            logger.debug("Deletion done");


            return modelAndView;
        } else {
            throw new GenericException("Empty parameters received");
        }

    }

    @RequestMapping(value = "loadRegisteredUser",
            method = RequestMethod.POST)
    public ModelAndView loadRegisteredUserDetails(@ModelAttribute(value = "username") String username,
                                                  @ModelAttribute(value = "notificationListID") String notificationListID,
                                                  @ModelAttribute(value = "systemName") String systemName,
                                                  @ModelAttribute(value = "logType") String logType,
                                                  @ModelAttribute(value = "errorCodeID") String errorCodeID) {

        logger.debug("Values received to view registered user details: Username:{}, Notification list ID: {}, " +
                "System Name:{}, LogType:{}, Error Code ID:{}",
                new Object[]{username, notificationListID, systemName, logType, errorCodeID});

        ModelAndView modelAndView = new ModelAndView("/logmanager/addRegisteredUser");
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());

        if (StringUtils.hasText(username) && StringUtils.hasText(notificationListID)) {

            DCLogNotificationForm dcLogNotificationForm = new DCLogNotificationForm();
            dcLogNotificationForm.setDcLogNotificationList(
                    dcLogNotificationService.getNotificationListByID(
                            Integer.valueOf(notificationListID)));
            dcLogNotificationForm.setRegisteredUser(new User());
            dcLogNotificationForm.setSystemName(systemName);
            dcLogNotificationForm.setLogType(LogType.ERROR);
            dcLogNotificationForm.setErrorCodeID(Integer.valueOf(errorCodeID));

            User user = userService.findUserByUserName(username);
            if (user != null) {
                logger.debug("User found:\nID:{}\nUsername:{}\nEmail:{}\nMobile:{}",
                        new Object[]{user.getId(), user.getUsername(), user.getEmail(), user.getMobile()});

                //Check that the user is not already added to the contact list
                if (dcLogNotificationForm.getDcLogNotificationList() != null
                        && dcLogNotificationForm.getDcLogNotificationList().getContactsList() != null
                        && !dcLogNotificationForm.getDcLogNotificationList().getContactsList().isEmpty()) {
                    for (DCLogContacts contact : dcLogNotificationForm.getDcLogNotificationList().getContactsList()) {
                        if (contact.isRegisteredUser()
                                && contact.getRegisteredContact().getUsername().equals(user.getUsername())) {
                            logger.debug("the user name \"{}\" you entered already exists in the list", username);
                            modelAndView.addObject("VALIDATION_ERROR",
                                    "<div class=\"ErrorMsg\">The username you entered already exists in the list</div>");
                            dcLogNotificationForm.setRegisteredUser(new User());
                            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
                            return modelAndView;
                        }
                    }
                }

                dcLogNotificationForm.setRegisteredUser(user);
            } else {
                logger.debug("the user name \"{}\" you entered is not correct", username);
                modelAndView.addObject("VALIDATION_ERROR",
                        "<div class=\"ErrorMsg\">The username you entered does not exist</div>");
            }

            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
        } else {
            loadLookupMaps(modelAndView);
            modelAndView.addObject(SEARCH_BEAN_KEY, new DCLogNotificationForm());
            modelAndView.addObject("VALIDATION_ERROR",
                    "<div class=\"ErrorMsg\">No user was passed</div>");

        }
        return modelAndView;
    }

    @RequestMapping(value = "addRegisteredUser", method = RequestMethod.POST)
    public ModelAndView addRegisteredUser(
            @Valid @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm,
            BindingResult result) {

        dcLogNotificationForm.setRegisteredUserAction("true");

        ModelAndView modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);
        dcLogNotificationValidator.validate(dcLogNotificationForm, result);
        if (result.hasErrors()) {
            logger.error("Validation errors exist");
            modelAndView.addObject("VALIDATION_ERROR_ADD_REGISTERED", "true");
            return modelAndView;
        }

        if (dcLogNotificationForm.getDcLogNotificationList().getId() == 0) {
            dcLogNotificationForm.getDcLogNotificationList().setSystemName(dcLogNotificationForm.getSystemName());
            dcLogNotificationForm.getDcLogNotificationList().setLogType(LogType.ERROR.getValue());
            dcLogNotificationForm.getDcLogNotificationList().setErrorCode(new DCLogErrorCode());
            dcLogNotificationForm.getDcLogNotificationList().getErrorCode().setId(
                    dcLogNotificationForm.getErrorCodeID());
        }
        List<Integer> resultValList = dcLogNotificationService.addRegisteredUserToNotificationList(
                dcLogNotificationForm.getDcLogNotificationList(),
                dcLogNotificationForm.getErrorCodeID(),
                dcLogNotificationForm.getRegisteredUser(),
                dcLogNotificationForm.getNotificationType());

        clearForm(dcLogNotificationForm);
        modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);

        ///Check result
        logger.debug("Operation result is {}", resultValList);

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

    @RequestMapping(value = "addExternalUser", method = RequestMethod.POST)
    public ModelAndView addExternalUser(
            @Valid @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm,
            BindingResult result) {

        if (dcLogNotificationForm != null
                && dcLogNotificationForm.getDcLogNotificationList() != null) {


            dcLogNotificationForm.setRegisteredUserAction("false");

            ModelAndView modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);
            dcLogNotificationValidator.validate(dcLogNotificationForm, result);
            if (result.hasErrors()) {
                logger.error("Validation errors exist");
                modelAndView.addObject("VALIDATION_ERROR_ADD_EXT", "true");
                return modelAndView;
            }

            if (dcLogNotificationForm.getDcLogNotificationList().getId() == 0) {
                dcLogNotificationForm.getDcLogNotificationList().setSystemName(dcLogNotificationForm.getSystemName());
                dcLogNotificationForm.getDcLogNotificationList().setLogType(LogType.ERROR.getValue());
                dcLogNotificationForm.getDcLogNotificationList().setErrorCode(new DCLogErrorCode());
                dcLogNotificationForm.getDcLogNotificationList().getErrorCode().setId(
                        dcLogNotificationForm.getErrorCodeID());
            }

            List<Integer> resultValList = dcLogNotificationService.addExternalUserToNotificationList(
                    dcLogNotificationForm.getDcLogNotificationList(),
                    dcLogNotificationForm.getErrorCodeID(),
                    dcLogNotificationForm.getExternalUser(),
                    dcLogNotificationForm.getNotificationType());

            clearForm(dcLogNotificationForm);
            modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);

            //Check result
            logger.debug("Operation result is {}", resultValList);

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

        } else {
            throw new GenericException("Null parameters received ");
        }

    }

    @RequestMapping(value = "editRegUser/{contactID}", method = RequestMethod.GET)
    public ModelAndView goToEditRegisteredUser(@PathVariable String contactID) {
        logger.debug("ID for edit:" + contactID);
        ModelAndView modelAndView = new ModelAndView("/logmanager/editRegisteredUser");
        if (StringUtils.hasText(contactID)) {
            DCLogContacts dcLogContacts
                    = dcLogNotificationService.getContactByID(Integer.valueOf(contactID));
            if (dcLogContacts != null) {
                logger.debug("User object received: "
                        + ((dcLogContacts.getRegisteredContact() != null) ?
                        dcLogContacts.getRegisteredContact().getUsername() : null));

                DCLogNotificationForm dcLogNotificationForm = new DCLogNotificationForm();
                List<DCLogContacts> contacts = new ArrayList<DCLogContacts>();
                contacts.add(dcLogContacts);
                dcLogNotificationForm.setDcLogNotificationList(new DCLogNotificationList());
                dcLogNotificationForm.getDcLogNotificationList().setId(
                        dcLogContacts.getNotificationListID());
                dcLogNotificationForm.getDcLogNotificationList().setContactsList(contacts);
                dcLogNotificationForm.setUpdatedRegisteredUser(dcLogContacts.getRegisteredContact());
                dcLogNotificationForm.setNotificationType(dcLogContacts.getNotificationType());
                dcLogNotificationForm.setContactListID(String.valueOf(dcLogContacts.getId()));
                modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
                modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "editRegisteredUser", method = RequestMethod.POST)
    public ModelAndView editRegisteredUser(
            @Valid @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm,
            BindingResult result) {

        if (dcLogNotificationForm != null
                && dcLogNotificationForm.getUpdatedRegisteredUser() != null) {
            logger.debug("User object:\nid:{}\nusername:{}\nemail:{}\nmobile:{}\nNotification type:{}",
                    new Object[]{dcLogNotificationForm.getUpdatedRegisteredUser().getId(),
                            dcLogNotificationForm.getUpdatedRegisteredUser().getUsername(),
                            dcLogNotificationForm.getUpdatedRegisteredUser().getEmail(),
                            dcLogNotificationForm.getUpdatedRegisteredUser().getMobile(),
                            dcLogNotificationForm.getNotificationType()});


            dcLogNotificationForm.setRegisteredUserAction("true");

            ModelAndView modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);

            dcLogNotificationValidator.validateUpdatedUserObject(dcLogNotificationForm, result);
            if (result.hasErrors()) {
                logger.error("Validation errors exist");
                modelAndView.addObject("VALIDATION_ERROR_EDIT_REGISTERED", "true");
                return modelAndView;
            }

            List<Integer> resultValList =
                    dcLogNotificationService.updateRegisteredContact(dcLogNotificationForm.getUpdatedRegisteredUser(),
                            Integer.valueOf(dcLogNotificationForm.getContactListID()),
                            dcLogNotificationForm.getNotificationType());

            dcLogNotificationForm.setDcLogNotificationList(dcLogNotificationService.getNotificationListByID(
                    dcLogNotificationForm.getDcLogNotificationList().getId()));
            reLoadSearchForm(dcLogNotificationForm);
            clearForm(dcLogNotificationForm);
            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);

            //Check result
            logger.debug("Operation result is {}", resultValList);

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

    @RequestMapping(value = "editExtUser/{contactID}", method = RequestMethod.GET)
    public ModelAndView goToEditExternalUser(@PathVariable String contactID) {
        logger.debug("ID for edit:" + contactID);
        ModelAndView modelAndView = new ModelAndView("/logmanager/editExternalUser");
        if (StringUtils.hasText(contactID)) {
            DCLogContacts dcLogContacts
                    = dcLogNotificationService.getContactByID(Integer.valueOf(contactID));
            if (dcLogContacts != null) {
                logger.debug("User object received: "
                        + ((dcLogContacts.getExternalContact() != null) ?
                        dcLogContacts.getExternalContact().getUserName() : null));

                DCLogNotificationForm dcLogNotificationForm = new DCLogNotificationForm();
                List<DCLogContacts> contacts = new ArrayList<DCLogContacts>();
                contacts.add(dcLogContacts);
                dcLogNotificationForm.setDcLogNotificationList(new DCLogNotificationList());
                dcLogNotificationForm.getDcLogNotificationList().setId(
                        dcLogContacts.getNotificationListID());
                dcLogNotificationForm.getDcLogNotificationList().setContactsList(contacts);
                dcLogNotificationForm.setUpdatedExternalUser(dcLogContacts.getExternalContact());
                dcLogNotificationForm.setNotificationType(dcLogContacts.getNotificationType());
                dcLogNotificationForm.setContactListID(String.valueOf(dcLogContacts.getId()));
                modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
                modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "editExternalUser", method = RequestMethod.POST)
    public ModelAndView editExternalUser(
            @Valid @ModelAttribute(value = "dcLogNotificationForm") DCLogNotificationForm dcLogNotificationForm,
            BindingResult result) {

        if (dcLogNotificationForm != null
                && dcLogNotificationForm.getUpdatedExternalUser() != null) {
            logger.debug("User object:\nid:{}\nusername:{}\nemail:{}\nmobile:{}\nNotification type:{}",
                    new Object[]{dcLogNotificationForm.getUpdatedExternalUser().getId(),
                            dcLogNotificationForm.getUpdatedExternalUser().getUserName(),
                            dcLogNotificationForm.getUpdatedExternalUser().geteMail(),
                            dcLogNotificationForm.getUpdatedExternalUser().getMobileNum(),
                            dcLogNotificationForm.getNotificationType()});

            dcLogNotificationForm.setRegisteredUserAction("false");

            ModelAndView modelAndView = loadViewPageAfterUpdate(dcLogNotificationForm);
            dcLogNotificationValidator.validateUpdatedUserObject(dcLogNotificationForm, result);
            if (result.hasErrors()) {
                logger.error("Validation errors exist");
                modelAndView.addObject("VALIDATION_ERROR_EDIT_EXTERNAL", "true");
                return modelAndView;
            }

            DCLogContacts dcLogContacts = new DCLogContacts();
            dcLogContacts.setId(Integer.valueOf(dcLogNotificationForm.getContactListID()));
            dcLogContacts.setExternalContact(dcLogNotificationForm.getUpdatedExternalUser());
            dcLogContacts.setNotificationType(dcLogNotificationForm.getNotificationType());

            List<Integer> resultValList = dcLogNotificationService.updateExternalContact(dcLogContacts);

            dcLogNotificationForm.setDcLogNotificationList(dcLogNotificationService.getNotificationListByID(
                    dcLogNotificationForm.getDcLogNotificationList().getId()));
            reLoadSearchForm(dcLogNotificationForm);
            clearForm(dcLogNotificationForm);
            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);

            //Check result
            logger.debug("Operation result is {}", resultValList);

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

    private ModelAndView loadManageNotificationListModel(DCLogNotificationForm dcLogNotificationForm) {
        ModelAndView modelAndView = new ModelAndView(PAGE_NOTIFY_LIST);
        DCLogNotificationList dcLogNotificationList = null;
        if (dcLogNotificationForm == null) {
            dcLogNotificationForm = new DCLogNotificationForm();
        }

        if (StringUtils.hasText(dcLogNotificationForm.getSystemName())) {
            dcLogNotificationList =
                    dcLogNotificationService.getDCLogNotificationListByFilter(
                            dcLogNotificationForm.getSystemName(),
                            dcLogNotificationForm.getLogType().getValue(),
                            dcLogNotificationForm.getErrorCodeID());
        }


        logger.debug("Object received from search: {}",
                new Object[]{((dcLogNotificationForm.getDcLogNotificationList() != null) ?
                        "List ID: "
                                + dcLogNotificationForm.getDcLogNotificationList().getId() : "null object")});

        dcLogNotificationForm.setDcLogNotificationList(dcLogNotificationList);

        modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);
        modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
        modelAndView.addObject(ERROR_CODES_LIST_KEY, getErrorCodes());
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
        modelAndView.addObject(USERS_LIST_KEY, getAllRegisteredUsers());

        return modelAndView;
    }

    /**
     * Clears Add/Edit registered & external user forms
     *
     * @param dcLogNotificationForm updated Form Bean
     */
    private void clearForm(DCLogNotificationForm dcLogNotificationForm) {
        dcLogNotificationForm.setRegisteredUser(new User());
        dcLogNotificationForm.setUpdatedRegisteredUser(new User());
        dcLogNotificationForm.setExternalUser(new DCLogExternalContact());
        dcLogNotificationForm.setUpdatedExternalUser(new DCLogExternalContact());
        dcLogNotificationForm.setNotificationType("");
    }

    /**
     * Reload search form, i.e.:
     * system name, log type & error code
     *
     * @param dcLogNotificationForm updated form
     */
    private void reLoadSearchForm(DCLogNotificationForm dcLogNotificationForm) {

        dcLogNotificationForm.setSystemName(dcLogNotificationForm.getDcLogNotificationList().getSystemName());
        dcLogNotificationForm.setLogType(LogType.ERROR);
        if (dcLogNotificationForm.getDcLogNotificationList().getErrorCode() != null) {
            dcLogNotificationForm.setErrorCodeID(dcLogNotificationForm.getDcLogNotificationList().
                    getErrorCode().getId());
        }
    }

    /**
     * load maps into ModelAndView Object
     *
     * @param modelAndView loaded ModelAndView object
     */
    private void loadLookupMaps(ModelAndView modelAndView) {
        modelAndView.addObject(SYSTEM_LIST_KEY, getAllSystems());
        modelAndView.addObject(ERROR_CODES_LIST_KEY, getErrorCodes());
        modelAndView.addObject(NOTIFICATION_TYPES_KEY, getNotificationTypesMap());
        modelAndView.addObject(USERS_LIST_KEY, getAllRegisteredUsers());
    }

    /**
     * Directs to the main page
     *
     * @param dcLogNotificationForm user form bean
     * @return loaded ModelAndView with latest notification list
     */
    private ModelAndView loadViewPageAfterUpdate(
            DCLogNotificationForm dcLogNotificationForm) {
        ModelAndView modelAndView = new ModelAndView(PAGE_NOTIFY_LIST);
        loadLookupMaps(modelAndView);

        if (dcLogNotificationForm.getDcLogNotificationList() != null
                && dcLogNotificationForm.getDcLogNotificationList().getId() > 0) {
            dcLogNotificationForm.setDcLogNotificationList(
                    dcLogNotificationService.getNotificationListByID(
                            dcLogNotificationForm.getDcLogNotificationList().getId()));

        } else {
            dcLogNotificationForm.setDcLogNotificationList(
                    dcLogNotificationService.getDCLogNotificationListByFilter(
                            dcLogNotificationForm.getSystemName(),
                            dcLogNotificationForm.getLogType().getValue(),
                            dcLogNotificationForm.getErrorCodeID()
                    ));
        }

        if(dcLogNotificationForm.getDcLogNotificationList() != null){
            dcLogNotificationForm.setLogType(LogType.ERROR);
            if (dcLogNotificationForm.getDcLogNotificationList().getErrorCode() != null) {
                dcLogNotificationForm.setErrorCodeID(
                        dcLogNotificationForm.getDcLogNotificationList().getErrorCode().getId());
            }

            if(StringUtils.hasText(dcLogNotificationForm.getDcLogNotificationList().getSystemName())){
                dcLogNotificationForm.setSystemName(
                        dcLogNotificationForm.getDcLogNotificationList().getSystemName());
            }
        }

        modelAndView.addObject(SEARCH_BEAN_KEY, dcLogNotificationForm);

        return modelAndView;

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
     * Get All error codes
     *
     * @return all available error codes
     */
    private List<DCLogErrorCode> getErrorCodes() {
        return dcLogNotificationService.getAllDCLogErrorCodes();
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
