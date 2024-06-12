package eg.com.vodafone.web.mvc.validator;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 4/28/13
 * Time: 2:23 PM
 */
@Component
public class KPINotificationValidator implements Validator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(KPINotificationValidator.class);
    @Autowired
    private UserService userService;
    @Autowired
    private KPINotificationService kpiNotificationService;
    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;
    @Autowired
    private RegexValidator regexValidator;

    @Override
    public boolean supports(Class<?> aClass) {
        return KPINotificationForm.class.isAssignableFrom(aClass);
    }

    /**
     * Validates KPI notification list search form
     *
     * @param bean
     * @param errors
     */
    public void validateSearchForm(Object bean, Errors errors) {
        if (!(bean instanceof KPINotificationForm)) {
            throw new GenericException("Invalid form bean type");
        }
        KPINotificationForm formBean = (KPINotificationForm) bean;
        if (!StringUtils.hasText(formBean.getSystemName())) {
            errors.rejectValue("systemName",
                    CMTConstants.FIELD_REQUIRED);
        } else {
            List<String> systemNames = dataCollectionService.listAllSystems();
            if (!systemNames.contains(formBean.getSystemName())) {
                errors.rejectValue("systemName",
                        CMTConstants.FIELD_INVALID);
            }
        }

        if (formBean.getNodeID() <= 0) {
            errors.rejectValue("nodeID",
                    CMTConstants.FIELD_REQUIRED);
        }
    }

    @Override
    public void validate(Object bean, Errors errors) {

        if (!(bean instanceof KPINotificationForm)) {
            throw new GenericException("Invalid form bean type");
        }
        KPINotificationForm formBean = (KPINotificationForm) bean;

        if (StringUtils.hasText(formBean.getRegisteredUserAction())
                && formBean.getRegisteredUserAction().equals("true")) {
            LOGGER.debug("Validating registered User");
            /**
             * Validates that the username is not null & that it's registered
             */
            if (formBean.getRegisteredUser() != null) {
                //Validate username
                if (StringUtils.hasText(formBean.getRegisteredUser().getUsername())) {
                    //Check that the user exists
                    User user = userService.findUserByUserName(formBean.getRegisteredUser().getUsername());
                    if (user == null) {
                        errors.rejectValue("registeredUser.username",
                                CMTConstants.FIELD_NOT_EXIST,
                                "<label class='error'>The username you entered does not exist</label>");
                    }
                }

                if(StringUtils.hasText(formBean.getRegisteredUser().getEmail())
                        && formBean.getRegisteredUser().getEmail().length() > 50){
                    errors.rejectValue("registeredUser.email", CMTConstants.EMAIL_FIELD_SIZE_INVALID);
                }

                if (StringUtils.hasText(formBean.getNotificationType())) {
                    if (formBean.getNotificationType().equals(NotificationType.EMAIL.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                        regexValidator.validate(formBean.getRegisteredUser().getEmail(), errors,
                                "registeredUser.email", CMTConstants.EMAIL_PATTERN);
                    }

                    if (formBean.getNotificationType().equals(NotificationType.MOBILE.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                        if(!StringUtils.hasText(formBean.getRegisteredUser().getMobile())){
                            errors.rejectValue("registeredUser.mobile", CMTConstants.FIELD_REQUIRED);
                        }else{
                            regexValidator.validate(formBean.getRegisteredUser().getMobile(), errors,
                                    "registeredUser.mobile", CMTConstants.MOBILE_PATTERN,
                                    "Pattern.mobileNum",
                                    "<label class='error'>Mobile number must be a valid Egyptian mobile number.</label>");
                        }
                    }
                }

            } else {
                throw new GenericException("Registered User object is null");
            }

        } else if (formBean.getRegisteredUserAction().equals("false")) {
            if (formBean.getExternalUser() != null) {
                LOGGER.debug("Validating external User");

                //Validate that the added user does not exist in the list

                List<Integer> similarExternalUserIDList =
                        kpiNotificationService.getSimilarExternalUserID(
                                formBean.getExternalUser().getUserName(),
                                formBean.getExternalUser().geteMail(),
                                formBean.getExternalUser().getMobileNum());

                if (similarExternalUserIDList != null && !similarExternalUserIDList.isEmpty()) {
                    KPINotificationList kpiNotificationList
                            = kpiNotificationService.getKPINotificationListByNodeID(formBean.getNodeID());
                    if (kpiNotificationList != null && kpiNotificationList.getContactList() != null
                            && !kpiNotificationList.getContactList().isEmpty()
                            && similarExternalUserIDList != null && !similarExternalUserIDList.isEmpty()) {
                        for (KPIContact contact : kpiNotificationList.getContactList()) {
                            if(!contact.isRegisteredUser()){
                                for(int contactID : similarExternalUserIDList){
                                    if (!contact.isRegisteredUser()
                                            && contact.getExternalContact() != null
                                            && contact.getExternalContact().getId() == contactID) {
                                        errors.rejectValue("externalUser.userName", "external.duplicate");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if(StringUtils.hasText(formBean.getExternalUser().geteMail())
                        && formBean.getExternalUser().geteMail().length() > 50){
                    errors.rejectValue("externalUser.eMail", CMTConstants.EMAIL_FIELD_SIZE_INVALID);
                }

                if (StringUtils.hasText(formBean.getNotificationType())) {
                    if (formBean.getNotificationType().equals(NotificationType.EMAIL.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                        regexValidator.validate(formBean.getExternalUser().geteMail(), errors,
                                "externalUser.eMail", CMTConstants.EMAIL_PATTERN);
                    }

                    if (formBean.getNotificationType().equals(NotificationType.MOBILE.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                        if(!StringUtils.hasText(formBean.getExternalUser().getMobileNum())){
                            errors.rejectValue("externalUser.mobileNum", CMTConstants.FIELD_REQUIRED);
                        }else{
                            regexValidator.validate(formBean.getExternalUser().getMobileNum(), errors,
                                    "externalUser.mobileNum", CMTConstants.MOBILE_PATTERN,
                                    "Pattern.mobileNum",
                                    "<label class='error'>Mobile number must be a valid Egyptian mobile number.</label>");
                        }
                    }
                }

            } else {
                throw new GenericException("External User object is null");
            }

        } else {
            throw new GenericException("Invalid user type");
        }

    }

    public void validateUpdatedUserObject(Object bean, Errors errors) {
        if (!(bean instanceof KPINotificationForm)) {
            throw new GenericException("Invalid form bean type");
        }
        KPINotificationForm formBean = (KPINotificationForm) bean;
        if (StringUtils.hasText(formBean.getRegisteredUserAction())
                && formBean.getRegisteredUserAction().equals("true")) {
            LOGGER.debug("Validating registered User");
            /**
             * Validates that the username is not null & that it's registered
             */
            if (formBean.getUpdatedRegisteredUser() != null) {
                if (StringUtils.hasText(formBean.getContactListID())) {
                    KPIContact contact
                            = kpiNotificationService.getContactByID(Integer.valueOf(formBean.getContactListID()));
                    if (contact != null && contact.getRegisteredContact() != null) {
                        //Validate username & Check that the user exists
                        if (StringUtils.hasText(formBean.getUpdatedRegisteredUser().getUsername())
                                && contact.getRegisteredContact() == null) {
                            errors.rejectValue("updatedRegisteredUser.username",
                                    CMTConstants.FIELD_NOT_EXIST,
                                    "<label class='error'>The username you entered does not exist</label>");
                        }

                        if (contact.getRegisteredContact() != null
                                && StringUtils.hasText(contact.getRegisteredContact().getEmail())
                                && contact.getRegisteredContact().getEmail().equals(
                                formBean.getUpdatedRegisteredUser().getEmail())
                                && StringUtils.hasText(contact.getRegisteredContact().getMobile())
                                && contact.getRegisteredContact().getMobile().
                                equals(formBean.getUpdatedRegisteredUser().getMobile())
                                && StringUtils.hasText(formBean.getNotificationType())
                                && formBean.getNotificationType().equals(contact.getNotificationType())) {
                            errors.rejectValue("noUpdate", "user.not.updated");
                        }

                    } else {
                        throw new GenericException("Registered User object is null");
                    }
                }

                if(StringUtils.hasText(formBean.getUpdatedRegisteredUser().getEmail())
                        && formBean.getUpdatedRegisteredUser().getEmail().length() > 50){
                    errors.rejectValue("updatedRegisteredUser.email", CMTConstants.EMAIL_FIELD_SIZE_INVALID);
                }

                if (StringUtils.hasText(formBean.getNotificationType())) {
                    if (formBean.getNotificationType().equals(NotificationType.EMAIL.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                        regexValidator.validate(formBean.getUpdatedRegisteredUser().getEmail(), errors,
                                "updatedRegisteredUser.email", CMTConstants.EMAIL_PATTERN);
                    }

                    if (formBean.getNotificationType().equals(NotificationType.MOBILE.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                        if(!StringUtils.hasText(formBean.getUpdatedRegisteredUser().getMobile())){
                            errors.rejectValue("updatedRegisteredUser.mobile", CMTConstants.FIELD_REQUIRED);
                        }else{
                            regexValidator.validate(formBean.getUpdatedRegisteredUser().getMobile(), errors,
                                    "updatedRegisteredUser.mobile", CMTConstants.MOBILE_PATTERN,
                                    "Pattern.mobileNum",
                                    "<label class='error'>Mobile number must be a valid Egyptian mobile number.</label>");
                        }
                    }
                }

            } else {
                throw new GenericException("Registered User object is null");
            }

        } else if (formBean.getRegisteredUserAction().equals("false")) {
            if (formBean.getUpdatedExternalUser() != null) {
                LOGGER.debug("Validating external User");
                if (StringUtils.hasText(formBean.getContactListID())) {
                    KPIContact contact
                            = kpiNotificationService.getContactByID(Integer.valueOf(formBean.getContactListID()));

                    if (formBean.getUpdatedExternalUser().getUserName().equalsIgnoreCase(
                            contact.getExternalContact().getUserName())
                            && formBean.getUpdatedExternalUser().geteMail().equalsIgnoreCase(
                            contact.getExternalContact().geteMail())
                            && formBean.getUpdatedExternalUser().getMobileNum().equalsIgnoreCase(
                            contact.getExternalContact().getMobileNum())
                            && StringUtils.hasText(formBean.getNotificationType())
                            && formBean.getNotificationType().equals(contact.getNotificationType())) {
                        errors.rejectValue("noUpdate", "user.not.updated");
                    }

                    List<Integer> similarExternalUserIDList =
                            kpiNotificationService.getSimilarExternalUserID(
                                    formBean.getUpdatedExternalUser().getUserName(),
                                    formBean.getUpdatedExternalUser().geteMail(),
                                    formBean.getUpdatedExternalUser().getMobileNum());

                    KPINotificationList kpiNotificationList
                            = kpiNotificationService.getKPINotificationListByNodeID(formBean.getNodeID());

                    if(kpiNotificationList != null && kpiNotificationList.getContactList() != null
                            && !kpiNotificationList.getContactList().isEmpty()
                            && similarExternalUserIDList != null && !similarExternalUserIDList.isEmpty()){
                        for(KPIContact kpiContact : kpiNotificationList.getContactList()){
                            if(!kpiContact.isRegisteredUser()){
                                for(int contactID : similarExternalUserIDList){
                                    if(contactID != contact.getExternalContact().getId()
                                            && kpiContact.getExternalContact().getId()
                                            != contact.getExternalContact().getId()
                                            && contactID == kpiContact.getExternalContact().getId()){
                                        errors.rejectValue("updatedExternalUser.userName",
                                                "external.duplicate");
                                    }

                                }
                            }
                        }
                    }

                } else {
                    throw new GenericException("External User object is null");
                }

                if(StringUtils.hasText(formBean.getUpdatedExternalUser().geteMail())
                        && formBean.getUpdatedExternalUser().geteMail().length() > 50){
                    errors.rejectValue("updatedExternalUser.eMail", CMTConstants.EMAIL_FIELD_SIZE_INVALID);
                }

                if (StringUtils.hasText(formBean.getNotificationType())) {
                    if (formBean.getNotificationType().equals(NotificationType.EMAIL.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                        regexValidator.validate(formBean.getUpdatedExternalUser().geteMail(), errors,
                                "updatedExternalUser.eMail", CMTConstants.EMAIL_PATTERN);
                    }

                    if (formBean.getNotificationType().equals(NotificationType.MOBILE.getNotificationTypeStr())
                            || formBean.getNotificationType().contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                        if(!StringUtils.hasText(formBean.getUpdatedExternalUser().getMobileNum())){
                            errors.rejectValue("updatedExternalUser.mobileNum", CMTConstants.FIELD_REQUIRED);
                        }else{
                            regexValidator.validate(formBean.getUpdatedExternalUser().getMobileNum(), errors,
                                    "updatedExternalUser.mobileNum", CMTConstants.MOBILE_PATTERN,
                                    "Pattern.mobileNum",
                                    "<label class='error'>Mobile number must be a valid Egyptian mobile number.</label>");
                        }
                    }
                }

            } else {
                throw new GenericException("External User object is null");
            }

        } else {
            throw new GenericException("Invalid user type");
        }


    }
}
