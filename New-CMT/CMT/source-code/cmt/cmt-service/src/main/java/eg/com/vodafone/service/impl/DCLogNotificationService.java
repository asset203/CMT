package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.DCLogNotificationDao;
import eg.com.vodafone.dao.UserDao;
import eg.com.vodafone.model.*;
import eg.com.vodafone.service.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/6/13
 * Time: 1:59 PM
 */
@Service
@Transactional(readOnly = true)
public class DCLogNotificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DCLogNotificationService.class);
    @Autowired
    private DCLogNotificationDao dcLogNotificationDao;
    @Autowired
    private UserDao userDao;

    public List<DCLogErrorCode> getAllDCLogErrorCodes() {
        return dcLogNotificationDao.getAllDCLogErrorCodes();
    }

    public DCLogContacts getContactByID(int contactID) {
        LOGGER.debug("Received contact ID:{}", contactID);
        return dcLogNotificationDao.getContactByID(contactID);
    }

    public DCLogNotificationList getNotificationListByID(int notificationListID) {
        LOGGER.debug("Received contact ID:{}", notificationListID);
        DCLogNotificationList dcLogNotificationList
                = dcLogNotificationDao.getNotificationListByID(notificationListID);
        dcLogNotificationList.setContactsList(sortContactList(dcLogNotificationList.getContactsList()));
        return dcLogNotificationList;
    }

    public DCLogNotificationList getDCLogNotificationListByFilter(
            String systemName, String logType, int errorCodeID) {

        DCLogNotificationList dcLogNotificationList
                = dcLogNotificationDao.getDCLogNotificationListsByFilter(
                systemName, logType, errorCodeID);
        logNotificationList(dcLogNotificationList);

        dcLogNotificationList.setContactsList(sortContactList(dcLogNotificationList.getContactsList()));

        return dcLogNotificationList;
    }

    @Transactional(readOnly = false)
    public List<Integer> updateExternalContact(DCLogContacts dcLogContacts) {

        List<Integer> resultList = new ArrayList<Integer>();
        int externalUserID = dcLogNotificationDao.getExternalUserID(dcLogContacts.getExternalContact().getUserName(),
                dcLogContacts.getExternalContact().geteMail(), dcLogContacts.getExternalContact().getMobileNum());

        if(externalUserID > 0){
            resultList.add(dcLogNotificationDao.deleteUserFromContactList(dcLogContacts.getId()));
            resultList.add(dcLogNotificationDao.addExternalUserToNotificationList(externalUserID,
                    dcLogContacts.getNotificationType(), dcLogContacts.getNotificationListID()));
        }else{
            resultList.add(dcLogNotificationDao.updateContactNotificationType(dcLogContacts));
            resultList.add(dcLogNotificationDao.updateExternalUser(dcLogContacts.getExternalContact()));
        }

        return resultList;
    }

    @Transactional(readOnly = false)
    public List<Integer> addRegisteredUserToNotificationList(DCLogNotificationList dcLogNotificationList,
                                                             int errorCodeID, User user,
                                                             String notificationType) {
        List<Integer> result = new ArrayList<Integer>();
        if (dcLogNotificationList.getId() == 0) {

            /**
             * Checking if list is empty or not.
             * In case no list is returned, then this is a new list that needs to be created
             */

            DCLogNotificationList dcLogNotificationListObj = new DCLogNotificationList();
            dcLogNotificationListObj.setId(-1);
            dcLogNotificationListObj.setSystemName(dcLogNotificationList.getSystemName());
            dcLogNotificationListObj.setLogType(dcLogNotificationList.getLogType());
            if (errorCodeID > 0) {
                dcLogNotificationListObj.setErrorCode(
                        dcLogNotificationDao.getErrorCodeObjectByID(errorCodeID));
            }

            result.add(dcLogNotificationDao.addNewDCNotificationList(dcLogNotificationListObj));

            //Get The newly Created Notification List
            dcLogNotificationList
                    = dcLogNotificationDao.getDCLogNotificationListsByFilter(
                    dcLogNotificationList.getSystemName(),
                    dcLogNotificationList.getLogType(), dcLogNotificationList.getErrorCode().getId());

            LOGGER.debug("a new list was created with following ID:{}", dcLogNotificationList.getId());

        }

        if (dcLogNotificationList.getId() > 0) {

            User orgUser = userDao.findUserById(user.getId());
            orgUser.setEmail(user.getEmail());
            orgUser.setMobile(user.getMobile());

            result = userDao.updateUser(orgUser);

            result.add(dcLogNotificationDao.addRegisteredUserToNotificationList(
                    user.getId(), notificationType, dcLogNotificationList.getId()));

        } else {
            throw new BusinessException("Failed to add a new notification list");
        }

        return result;

    }

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
            DCLogContacts dcLogContacts = new DCLogContacts();
            dcLogContacts.setId(contactListID);
            dcLogContacts.setNotificationType(notificationType);
            result.add(dcLogNotificationDao.updateContactNotificationType(dcLogContacts));
        } else {
            LOGGER.error("User with ID {} and name {} was not found in DB",
                    updatedUser.getId(), updatedUser.getUsername());
            throw new BusinessException("User: " + updatedUser.getUsername() + " was not found in DB");
        }


        return result;
    }

    @Transactional(readOnly = false)
    public List<Integer> addExternalUserToNotificationList(DCLogNotificationList dcLogNotificationList,
                                                           int errorCodeID,
                                                           DCLogExternalContact user,
                                                           String notificationType) {
        List<Integer> resultList = new ArrayList<Integer>();
        if (dcLogNotificationList.getId() == 0) {

            /**
             * Checking if list is empty or not.
             * In case no list is returned, then this is a new list that needs to be created
             */

            DCLogNotificationList dcLogNotificationListObj = new DCLogNotificationList();
            dcLogNotificationListObj.setId(-1);
            dcLogNotificationListObj.setSystemName(dcLogNotificationList.getSystemName());
            dcLogNotificationListObj.setLogType(dcLogNotificationList.getLogType());
            if (errorCodeID > 0) {
                dcLogNotificationListObj.setErrorCode(
                        dcLogNotificationDao.getErrorCodeObjectByID(errorCodeID));
            }

            resultList.add(dcLogNotificationDao.addNewDCNotificationList(dcLogNotificationListObj));

            //Get The newly Created Notification List
            dcLogNotificationList
                    = dcLogNotificationDao.getDCLogNotificationListsByFilter(
                    dcLogNotificationList.getSystemName(),
                    dcLogNotificationList.getLogType(), dcLogNotificationList.getErrorCode().getId());

            LOGGER.debug("a new list was created with following ID:{}", dcLogNotificationList.getId());

        }

        if (dcLogNotificationList.getId() > 0) {
            if (user != null) {
                /**
                 * If External User was added before in other lists, then just refer to the ID
                 */
                List<Integer> externalUserID = dcLogNotificationDao.getSimilarExternalUserID(
                        user.getUserName(), user.geteMail(), user.getMobileNum());

                int userID = 0;
                if(externalUserID != null && !externalUserID.isEmpty()){
                    userID = externalUserID.get(0);
                }

                if (userID > 0) {
                    user.setId(userID);
                } else {
                    resultList.add(dcLogNotificationDao.addNewExternalUser(user));
                    LOGGER.debug("Done calling add external user method. Now will try to get added user ID");

                    userID = dcLogNotificationDao.getExternalUserID(user.getUserName(),
                            user.geteMail(), user.getMobileNum());
                }
                LOGGER.debug("External user ID is: {}", userID);
                if (userID > 0) {
                    resultList.add(dcLogNotificationDao.addExternalUserToNotificationList(
                            userID, notificationType, dcLogNotificationList.getId()));
                } else {
                    throw new BusinessException("The external user was not added & " +
                            "hence can't add it to the notification list");
                }

            } else {
                throw new BusinessException("No user was passed to adding new external user method");
            }

        } else {
            throw new BusinessException("Failed to add a new notification list");
        }

        return resultList;
    }

    @Transactional(readOnly = false)
    public List<Integer> deleteUsersFromContactList(Integer[] contactsID) {
        List<Integer> resultList = new ArrayList<Integer>();
        if (contactsID != null && contactsID.length > 0) {
            LOGGER.debug("List received for deletion has size:" + contactsID.length);
            int result;
            for (int contactID : contactsID) {
                result = dcLogNotificationDao.deleteUserFromContactList(contactID);
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
     * Get the ID of external contact
     * using his information
     *
     * @param userName     external contact user name
     * @param email        external contact email
     * @param mobileNumber external contact mobile number
     * @return 0 in case no contact was found, external contact id otherwise
     */
    public int getExternalUserID(String userName, String email, String mobileNumber) {
        return dcLogNotificationDao.getExternalUserID(userName, email, mobileNumber);
    }

    /**
     * Get the similar list of IDs of external contact
     * using his information
     *
     * @param userName     external contact user name
     * @param email        external contact email
     * @param mobileNumber external contact mobile number
     * @return null in case no contact was found, similar external contact id list otherwise
     */
    public List<Integer> getSimilarExternalUserID(
            String userName, String email, String mobileNumber) {
        return dcLogNotificationDao.getSimilarExternalUserID(userName, email, mobileNumber);
    }




    /**
     * Utility method to log received List<DCLogNotificationList>
     *
     * @param dcLogNotificationList Notification list to be logged
     */
    private void logNotificationList(DCLogNotificationList dcLogNotificationList) {
        try {
            if (dcLogNotificationList != null) {

                LOGGER.debug("Notification List ID:{}\nSystem Name:{}\nLog Type:{}",
                        new Object[]{dcLogNotificationList.getId(),
                                dcLogNotificationList.getSystemName(),
                                dcLogNotificationList.getLogType()});
                if (dcLogNotificationList.getErrorCode() != null) {
                    LOGGER.debug("Error Code ID:{}\nError Code:{}\nError Code Description:{}",
                            new Object[]{dcLogNotificationList.getErrorCode().getId(),
                                    dcLogNotificationList.getErrorCode().getErrorCode(),
                                    dcLogNotificationList.getErrorCode().getDescription()});
                }
                if (dcLogNotificationList.getContactsList() != null
                        && dcLogNotificationList.getContactsList().size() > 0) {
                    for (int j = 0; j < dcLogNotificationList.getContactsList().size(); j++) {
                        LOGGER.debug("Contact ID:{}\nContact notification type:{}\nContact isRegisteredUser?{}",
                                new Object[]{dcLogNotificationList.getContactsList().get(j).getId(),
                                        dcLogNotificationList.getContactsList().get(j).getNotificationType(),
                                        dcLogNotificationList.getContactsList().get(j).isRegisteredUser()});

                        if (dcLogNotificationList.getContactsList().get(j).isRegisteredUser()
                                && dcLogNotificationList.getContactsList().
                                get(j).getRegisteredContact() != null) {
                            LOGGER.debug("\nUsername:{}\neMail:{}\nMobile:{}",
                                    new Object[]{dcLogNotificationList.getContactsList().get(j).
                                            getRegisteredContact().getUsername(),
                                            dcLogNotificationList.getContactsList().get(j).
                                                    getRegisteredContact().getEmail(),
                                            dcLogNotificationList.getContactsList().get(j).
                                                    getRegisteredContact().getMobile()});
                        } else if (dcLogNotificationList.getContactsList().get(j).getExternalContact() != null) {
                            LOGGER.debug("\nUsername:{}\nEmail:{}\nMobile:{}",
                                    new Object[]{dcLogNotificationList.getContactsList().get(j).
                                            getExternalContact().getUserName(),
                                            dcLogNotificationList.getContactsList().get(j).
                                                    getExternalContact().geteMail(),
                                            dcLogNotificationList.getContactsList().get(j).
                                                    getExternalContact().getMobileNum()});
                        }
                    }
                }

            }
        } catch (Exception ex) {
            LOGGER.error("Exception thrown while logging notification list", ex);
        }
    }

    /**
     * Sort contact list by username
     *
     * @param contactList
     * @return
     */
    private List<DCLogContacts> sortContactList(List<DCLogContacts> contactList) {
        Collections.sort(contactList, new Comparator<DCLogContacts>() {
            public int compare(DCLogContacts o1, DCLogContacts o2) {
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
