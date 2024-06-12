package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.dao.CMTDBConnectionPool;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.objects.MailConfigration;
import eg.com.vodafone.model.enums.NotificationType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 9/11/13
 * Time: 3:05 PM
 */
public class DCNotificationManager {

    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private final static String notificationListQuery = "SELECT DC_LOG_NOTIFICATION_LIST.SYSTEM_NAME,   " +
            "DC_LOG_CONTACTS.IS_EXISTING_USER, DC_LOG_CONTACTS.NOTIFICATION_TYPE, USERS.USERNAME AS USER_USERNAME,  " +
            "USERS.EMAIL AS USER_EMAIL, USERS.MOBILE,  " +
            "DC_LOG_EXTERNAL_CONTACTS.USERNAME AS EXT_USERNAME,  " +
            "DC_LOG_EXTERNAL_CONTACTS.EMAIL AS EXT_EMAIL, DC_LOG_EXTERNAL_CONTACTS.MOBILE_NUMBER  " +
            "FROM DC_LOG_NOTIFICATION_LIST  " +
            "LEFT OUTER JOIN DC_LOG_CONTACTS ON DC_LOG_NOTIFICATION_LIST.ID = DC_LOG_CONTACTS.NOTIFICATION_LIST_ID  " +
            "LEFT OUTER JOIN USERS ON DC_LOG_CONTACTS.REGISTERED_CONTACT_ID = USERS.ID  " +
            "LEFT OUTER JOIN DC_LOG_EXTERNAL_CONTACTS ON DC_LOG_CONTACTS.EXTERNAL_CONTACTS_ID = DC_LOG_EXTERNAL_CONTACTS.ID " +
            "WHERE DC_LOG_NOTIFICATION_LIST.SYSTEM_NAME = ";

    /**
     * @param systemName
     */
    public static void sendNotificationForJobCompletion(String systemName,
                                                        Mail mail, String smsBody) {
        Logger logger = DataCollectionManager.getSystemLogger(systemName);
        try {

            try {
                PropertyReader.init(".", logger);
            } catch (ApplicationException e) {
                logger.error("Exception thrown while trying to init property reader", e);
            }

            Map<String, List<String>> notificationMap;

            notificationMap = getSystemNotificationList(systemName);

            if (notificationMap != null && !notificationMap.isEmpty()) {
                if (notificationMap.containsKey(NotificationType.EMAIL.getNotificationTypeStr())) {
                    sendEmailNotification(notificationMap.get(NotificationType.EMAIL.getNotificationTypeStr()),
                            systemName, mail, logger);
                }

                if (notificationMap.containsKey(NotificationType.MOBILE.getNotificationTypeStr())) {
                    sendSMSlNotification(notificationMap.get(NotificationType.MOBILE.getNotificationTypeStr()),
                            systemName, smsBody, logger);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception thrown while trying to send notification list to system:" + systemName, ex);
        }
    }

    /**
     * @param systemName
     * @return map with contacts to send email or/& SMS
     */
    private static Map<String, List<String>> getSystemNotificationList(String systemName) {
        Map<String, List<String>> notificationMap = new HashMap<String, List<String>>();

        if (StringUtils.isNotEmpty(systemName)) {
            Logger logger = DataCollectionManager.getSystemLogger(systemName);
            Set<String> emailNotList = new TreeSet<String>();
            Set<String> smsNotList = new TreeSet<String>();
            Connection connection = null;
            Statement statement = null;
            ResultSet notificationListRS;
            try {
                connection = CMTDBConnectionPool.getConnection();
                statement
                        = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                String finalQuery = notificationListQuery + "'" + systemName + "'";
                logger.debug("Final query:\n" + finalQuery);
                notificationListRS = statement.executeQuery(finalQuery);

                String notificationType;
                boolean isRegUser;

                while (notificationListRS.next()) {

                    logger.info("Notification list query returned data");

                    if (notificationListRS.getString("IS_EXISTING_USER") != null) {
                        isRegUser = Boolean.parseBoolean(notificationListRS.getString("IS_EXISTING_USER"));
                    } else {
                        isRegUser = false;
                    }
                    notificationType = notificationListRS.getString("NOTIFICATION_TYPE");
                    if (notificationType != null) {
                        if (isRegUser) {
                            if (notificationType.contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                                if (notificationListRS.getString("USER_EMAIL") != null) {
                                    emailNotList.add(notificationListRS.getString("USER_EMAIL"));
                                }
                            }
                            if (notificationType.contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                                if (notificationListRS.getString("MOBILE") != null) {
                                    smsNotList.add(notificationListRS.getString("MOBILE"));
                                }
                            }
                        } else {
                            if (notificationType.contains(NotificationType.EMAIL.getNotificationTypeStr())) {
                                if (notificationListRS.getString("EXT_EMAIL") != null) {
                                    emailNotList.add(notificationListRS.getString("EXT_EMAIL"));
                                }
                            }
                            if (notificationType.contains(NotificationType.MOBILE.getNotificationTypeStr())) {
                                if (notificationListRS.getString("MOBILE_NUMBER") != null) {
                                    smsNotList.add(notificationListRS.getString("MOBILE_NUMBER"));
                                }
                            }
                        }
                    }
                }

                if (emailNotList.size() > 0) {
                    notificationMap.put(NotificationType.EMAIL.getNotificationTypeStr(),
                            new ArrayList<String>(emailNotList));
                }

                if (smsNotList.size() > 0) {
                    notificationMap.put(NotificationType.MOBILE.getNotificationTypeStr(),
                            new ArrayList<String>(smsNotList));
                }


                /**
                 * Debugging section
                 */
                if (logger.isDebugEnabled()) {
                    logger.debug("Email notification contact list:");
                    for (String contactEmail : emailNotList) {
                        logger.debug(contactEmail);
                    }
                    logger.debug("SMS notification contact list:");
                    for (String contactMobile : smsNotList) {
                        logger.debug(contactMobile);
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return notificationMap;
    }

    /**
     * Send an email to the list of contacts given
     *
     * @param emailList
     */
    private static void sendEmailNotification(List<String> emailList, String systemName, Mail mail, Logger logger) {
        if (emailList != null && emailList.size() > 0 && StringUtils.isNotEmpty(systemName)
                && mail != null && logger != null) {

            mail.setToRecipients(emailList.toArray(new String[emailList.size()]));
            MailConfigration mailConfiguration = new MailConfigration();
            mailConfiguration.setHostName(PropertyReader.getPropertyValue(MAIL_SMTP_HOST));
            mailConfiguration.setPortNo(PropertyReader.getPropertyValue(MAIL_SMTP_PORT));

            logger.info("Email notification list was found, proceeding with eMail sending for system: " + systemName
                    + "\nMail object info:\nFrom:" + mail.getFromRecipient() + "\nSubject:" + mail.getSubject()
                    + "\nBody:" + mail.getContent());

            MailUtility.sendMail(mail, mailConfiguration);
        }
    }

    /**
     * Send SMS to the list of contacts given
     *
     * @param smsList
     */
    private static void sendSMSlNotification(List<String> smsList, String systemName, String smsBody, Logger logger) {
        if (smsList != null && smsList.size() > 0 && StringUtils.isNotEmpty(systemName)
                && StringUtils.isNotEmpty(smsBody) && logger != null) {
            logger.info("SMS notification list was found, proceeding with SMS sending for system:" + systemName
                    + "\nSMS body:" + smsBody);
            Session session;
            Connection connection = null;
            try {
                PersistenceManager persistenceManager
                        = DataCollectionManager.getPersistenceManager();
                session = persistenceManager.getNewSession();
                if (session != null) {
                    connection = session.connection();
                    SMSUtility.sendSMS(connection,
                            "Execution has completed for system:" + systemName, smsList.toArray().toString());
                }
            } catch (ApplicationException e) {
                logger.error("Exception thrown while trying to send SMS", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }


}
