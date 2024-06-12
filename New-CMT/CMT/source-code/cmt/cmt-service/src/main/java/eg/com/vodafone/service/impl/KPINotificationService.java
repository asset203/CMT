package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.KPINotificationDao;
import eg.com.vodafone.dao.UserDao;
import eg.com.vodafone.model.*;
import eg.com.vodafone.service.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/27/13
 * Time: 4:47 PM
 */
@Service
@Transactional(readOnly = true)
public class KPINotificationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(KPINotificationService.class);
    @Autowired
    private KPINotificationDao kpiNotificationDao;
    @Autowired
    private UserDao userDao;

    /**
     * Get Contact by ID
     *
     * @param contactID
     * @return KPI Contact
     */
    public KPIContact getContactByID(int contactID) {
        LOGGER.debug("Received contact ID: {}", contactID);
        return kpiNotificationDao.getContactByID(contactID);
    }

    /**
     * Retrieve Notification list by ID
     *
     * @param nodeID
     * @return KPI notification list
     */
    public KPINotificationList getKPINotificationListByNodeID(int nodeID) {
        LOGGER.debug("Received node ID:{}", nodeID);
        KPINotificationList kpiNotificationList = kpiNotificationDao.getKPINotificationListByNodeID(nodeID);
        kpiNotificationList.setContactList(sortContactList(kpiNotificationList.getContactList()));
        return kpiNotificationList;
    }

    /**
     * Add External user to KPI notification list
     *
     * @param user
     * @param notificationType
     * @param nodeID
     * @return Operation result
     */
    @Transactional(readOnly = false)
    public List<Integer> addExternalUserToNotificationList(
            DCLogExternalContact user, String notificationType, int nodeID) {
        if (user != null && StringUtils.hasText(notificationType) && nodeID > 0) {
            LOGGER.debug("Received external user details for addition:\nUsername:{}" +
                    "\nEmail:{}\nMobile:{}\nNotify by:{}\nNode ID:{}",
                    new Object[]{
                            user.getUserName(), user.geteMail(),
                            user.getMobileNum(), notificationType, nodeID});

            List<Integer> resultList = new ArrayList<Integer>();
            /**
             * If External User was added before in other lists, then just refer to the ID
             */
            int userID = 0;
            List<Integer> userIDList = kpiNotificationDao.getSimilarExternalUserID(
                    user.getUserName(), user.geteMail(), user.getMobileNum());

            if(userIDList != null && !userIDList.isEmpty()){
                userID = userIDList.get(0);
            }


            if (userID > 0) {
                LOGGER.debug("This external user was added in a previous list & hence no new user will be added");
                resultList.add(kpiNotificationDao.addExternalUserToNotificationList(
                        userID, notificationType, nodeID));
                return resultList;
            } else {
                resultList.add(kpiNotificationDao.addNewExternalUser(user));
                LOGGER.debug("Done calling add external user method. Now will try to get added user ID");

                int extUserID = kpiNotificationDao.getExternalUserID(
                        user.getUserName(), user.geteMail(), user.getMobileNum());
                if (extUserID > 0) {
                    resultList.add(kpiNotificationDao.addExternalUserToNotificationList(
                            extUserID, notificationType, nodeID));
                    return resultList;
                } else {
                    throw new BusinessException("The external user was not added & " +
                            "hence can't add it to the notification list");
                }
            }
        } else {
            throw new BusinessException("Values received are either null or empty");
        }
    }

    /**
     * Get Similar External User
     *
     * @param userName
     * @param email
     * @param mobileNumber
     * @return
     */
    public List<Integer> getSimilarExternalUserID(
            String userName, String email, String mobileNumber){
            return kpiNotificationDao.getSimilarExternalUserID(userName, email, mobileNumber);
    }

    /**
     * Add Registered user to KPI notification list
     *
     * @param user
     * @param notificationType
     * @param nodeID
     * @return Operation result
     */
    @Transactional(readOnly = false)
    public List<Integer> addRegisteredUserToNotificationList(User user, String notificationType, int nodeID) {
        if (user != null && StringUtils.hasText(notificationType) && nodeID > 0) {
            LOGGER.debug("Received registered user details for addition:\nUsername:{}" +
                    "\nEmail:{}\nMobile:{}\nNotify by:{}\nNode ID:{}",
                    new Object[]{
                            user.getUsername(), user.getEmail(),
                            user.getMobile(), notificationType, nodeID});

            List<Integer> resultList = new ArrayList<Integer>();
            LOGGER.debug("Retrieve original user");
            User orgUser = userDao.findUserById(user.getId());
            if (orgUser != null) {
                orgUser.setEmail(user.getEmail());
                orgUser.setMobile(user.getMobile());

                resultList = userDao.updateUser(orgUser);

                resultList.add(kpiNotificationDao.addRegisteredUserToNotificationList(
                        user.getId(), notificationType, nodeID));

                return resultList;
            } else {
                throw new BusinessException("No registered user exists with ID:" + user.getId());
            }

        } else {
            throw new BusinessException("Values received are either null or empty");
        }
    }

    /**
     * Update external contact email/mobile
     *
     * @param kpiContact KPI contact
     * @return operation result
     */
    @Transactional(readOnly = false)
    public List<Integer> updateExternalContact(KPIContact kpiContact, int nodeID) {

        List<Integer> resultList = new ArrayList<Integer>();
        int externalUserID = kpiNotificationDao.getExternalUserID(kpiContact.getExternalContact().getUserName(),
                kpiContact.getExternalContact().geteMail(), kpiContact.getExternalContact().getMobileNum());

        if(externalUserID > 0){
            LOGGER.info("Another user was found with the same information & hence updating ID in contact: {}",
                    externalUserID);
            resultList.add(kpiNotificationDao.deleteUserFromContactList(kpiContact.getId()));
            resultList.add(kpiNotificationDao.addExternalUserToNotificationList(
                    externalUserID, kpiContact.getNotificationType(), nodeID));
        }else{
            resultList.add(kpiNotificationDao.updateContactNotificationType(kpiContact));
            resultList.add(kpiNotificationDao.updateExternalUser(kpiContact.getExternalContact()));
        }

        return resultList;
    }

    /**
     * Update registered user email/mobile/notification type
     *
     * @param updatedUser
     * @param contactListID
     * @param notificationType
     * @return operation result
     */
    @Transactional(readOnly = false)
    public List<Integer> updateRegisteredContact(User updatedUser,
                                                 int contactListID, String notificationType) {

        LOGGER.debug("updatedUser:\n Username:{}\neMail:{}\nMobile:{}",
                new Object[]{updatedUser.getUsername(),
                        updatedUser.getEmail(), updatedUser.getMobile()});

        List<Integer> result = new ArrayList<Integer>();
        User user = userDao.findUserById(updatedUser.getId());

        if (user != null) {
            LOGGER.debug("User is retrieved from DB with username:{}", user.getUsername());

            user.setEmail(updatedUser.getEmail());
            user.setMobile(updatedUser.getMobile());

            LOGGER.debug("User after setting updated values:\nUsername:{}\neMail:{}\nMobile:{}",
                    new Object[]{user.getUsername(), user.getEmail(), user.getMobile()});

            result.addAll(userDao.updateUser(user));

            KPIContact kpiContact = new KPIContact();
            kpiContact.setId(contactListID);
            kpiContact.setNotificationType(notificationType);

            result.add(kpiNotificationDao.updateContactNotificationType(kpiContact));
        } else {
            LOGGER.error("User with ID {} and name {} was not found in DB",
                    updatedUser.getId(), updatedUser.getUsername());
            throw new BusinessException("User: " + updatedUser.getUsername() + " was not found in DB");
        }

        return result;
    }

    /**
     * Remove user from notification list
     *
     * @param contactsID
     * @return Operation result
     */
    @Transactional(readOnly = false)
    public List<Integer> deleteUsersFromContactList(Integer[] contactsID) {
        List<Integer> resultList = new ArrayList<Integer>();
        if (contactsID != null && contactsID.length > 0) {
            LOGGER.debug("List received for deletion has size:" + contactsID.length);
            int result;
            for (int contactID : contactsID) {
                result = kpiNotificationDao.deleteUserFromContactList(contactID);
                LOGGER.debug("Deletion of user with ID {} has result of: {}",
                        contactID, result);
                resultList.add(result);
            }
        } else {
            throw new BusinessException("The list received for deletion is null or empty");
        }
        return resultList;
    }

    /**
     * Delete all contacts for teh given node ID
     *
     * @param nodeID
     * @return
     */
    @Transactional(readOnly = false)
    public int deleteNotificationList(long nodeID) {
        return kpiNotificationDao.deleteNotificationList(nodeID);
    }

    /**
     * Get the ID of external contact
     * using his information
     *
     * @param userName     external contact user name
     * @param email        external contact email
     * @param mobileNumber external contact mobile number
     * @return 0 in case no contact was found, external contact id otherwise
     */
    public int getExternalUserID(String userName, String email, String mobileNumber) {
        return kpiNotificationDao.getExternalUserID(userName, email, mobileNumber);
    }

    /**
     * Retrieve the Node ID associated with the given contact ID
     *
     * @param contactID
     * @return Semi loaded VNode object
     */
    public VNode getNodeByContactID(int contactID) {
        return kpiNotificationDao.getNodeByContactID(contactID);
    }


    /**
     * Sort KPI contact list by username
     *
     * @param contactList
     * @return
     */
    private List<KPIContact> sortContactList(List<KPIContact> contactList){
        Collections.sort(contactList, new Comparator<KPIContact>() {
            public int compare(KPIContact o1, KPIContact o2) {
                String userName1, userName2;
                if (o1.isRegisteredUser()) {
                    userName1 = o1.getRegisteredContact().getUsername();
                } else {
                    userName1 = o1.getExternalContact().getUserName();
                }

                if (o2.isRegisteredUser()) {
                    userName2 = o2.getRegisteredContact().getUsername();
                } else {
                    userName2 = o2.getExternalContact().getUserName();
                }
                return userName1.toLowerCase().compareTo(userName2.toLowerCase());
            }
        });
        return contactList;
    }

}
