package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.dao.KpiAlarmDao;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.objects.NodeUtilization;
import com.itworx.vaspp.datacollection.objects.NotificationUserContact;
import com.itworx.vaspp.datacollection.objects.TrafficTableProperties;
import com.itworx.vaspp.datacollection.services.KpiMessagingService;
import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.SystemNode;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.enums.NodeInUse;
import org.apache.log4j.Logger;

import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author marwa.goda
 * @since 5/8/13
 */

public class NotificationManager {

    public static final int HUNDRED = 100;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT
            = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
    private Logger logger;
    KpiAlarmDao kpiAlarmDao;
    private KpiMessagingService kpiMessagingService;

    public NotificationManager() {
    }

    public NotificationManager(Logger logger) {
        this.logger = logger;
    }

    public void monitorNodeUtilization(String systemName, Date targetDate, boolean isHourly) {
        try {
            kpiAlarmDao = new KpiAlarmDao(logger);
            List<SystemNode> systemNodeList = kpiAlarmDao.getSystemNodesBySystemName(systemName);

            if (null != systemNodeList && !systemNodeList.isEmpty()) {
                for (SystemNode systemNode : systemNodeList) {

                    if (NodeInUse.YES.toString().equalsIgnoreCase(systemNode.getInUse())) {

                        long nodeId = systemNode.getSystemNodeId();
                        String nodeName = systemNode.getNodeName();

                        List<NodeUtilization> nodeUtilizationList = GetExceededNodeUtilizationMeasures(targetDate, nodeId, nodeName, isHourly);
                        if (nodeUtilizationList != null && !nodeUtilizationList.isEmpty()) {
                            prepareAndSendMessage(systemName, nodeId, nodeName, nodeUtilizationList);
                        }

                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exception occurred while trying to monitor KPI", ex);
        }
    }

    public void monitorNodeUtilization(VNode targetNode, Date targetDate, boolean isHourly) {
        try {
            if (targetNode != null) {
                logger.debug("Check KPI utilization for node:" + targetNode.getName()
                        + ", ID:" + targetNode.getId() + ", for target Date:" + targetDate
                        + ", isNode in Use?" + targetNode.getInUse());
            }
            kpiAlarmDao = new KpiAlarmDao(logger);
            if (NodeInUse.YES.toString().equalsIgnoreCase(targetNode.getInUse())) {
                logger.debug("Node is in use");
                long nodeId = targetNode.getId();
                String nodeName = targetNode.getName();

                List<NodeUtilization> nodeUtilizationList = GetExceededNodeUtilizationMeasures(targetDate, nodeId, nodeName, isHourly);
                logger.debug("Any of the records exceeded for date:" + targetDate + "?"
                        + ((nodeUtilizationList != null) ? nodeUtilizationList.size() : null));
                if (nodeUtilizationList != null && !nodeUtilizationList.isEmpty()) {
                    prepareAndSendMessage(targetNode.getSystemName(), nodeId, nodeName, nodeUtilizationList);
                }

            }
        } catch (Exception ex) {
            logger.error("Exception occurred while trying to monitor KPI", ex);
        }

    }

    private List<NodeUtilization> GetExceededNodeUtilizationMeasures(Date targetDate, long nodeId, String nodeName, boolean isHourly) {
        logger.debug("is kpiAlarmDao null?" + kpiAlarmDao);
        List<NodeProperties> nodePropertiesList = kpiAlarmDao.getSystemKpiDetails(nodeId);
        List<NodeUtilization> nodeUtilizationList = new ArrayList<NodeUtilization>();

        if (null != nodePropertiesList && !nodePropertiesList.isEmpty()) {

            logger.debug("Node properties not empty for node:" + nodeName);
            for (NodeProperties nodeProperties : nodePropertiesList) {
                List<TrafficTableProperties> trafficTableUtilizationList =
                        kpiAlarmDao.getTrafficTableNameUtilization(nodeProperties.getTrafficTableName(), nodeName, targetDate, isHourly);

                if (trafficTableUtilizationList != null && !trafficTableUtilizationList.isEmpty()) {
                    for (TrafficTableProperties trafficTableProperties : trafficTableUtilizationList) {
                        boolean isUtilizationExceedsThreshold =
                                checkUtilizationVersusThreshold(nodeProperties.getNotificationThreshold(), trafficTableProperties.getUtilization());
                        if (isUtilizationExceedsThreshold) {
                            NodeUtilization nodeUtilization = new NodeUtilization();

                            nodeUtilization.setTrafficTableName(nodeProperties.getTrafficTableName());
                            nodeUtilization.setNotificationThreshold(nodeProperties.getNotificationThreshold());
                            nodeUtilization.setTrafficTableUtilization(trafficTableProperties.getUtilization());
                            nodeUtilization.setTargetTime(trafficTableProperties.getDateTime());

                            nodeUtilizationList.add(nodeUtilization);
                        }
                    }

                } else {
                    logger.error("NotificationManager.GetExceededNodeUtilizationMeasures() -TrafficTable doesn't exist! OR no record found for " + nodeProperties.getTrafficTableName());
                }
            }
        }
        return nodeUtilizationList;
    }

    private boolean checkUtilizationVersusThreshold(double notificationThreshold, float trafficTableNameUtilization) {

        boolean isUtilizationExceedsThreshold = false;

        isUtilizationExceedsThreshold = (trafficTableNameUtilization * HUNDRED > notificationThreshold) ? true : false;

        return isUtilizationExceedsThreshold;
    }

    private void prepareAndSendMessage(String systemName, long nodeId, String nodeName, List<NodeUtilization> nodeUtilizationList) {

        List<NotificationUserContact> notificationUserContactList = kpiAlarmDao.getNodeContactList(nodeId);
        logger.debug("Found any configured notification list?"
                + ((notificationUserContactList != null) ? notificationUserContactList.size() : null));

        if (notificationUserContactList != null && !notificationUserContactList.isEmpty()) {
            String[] contactList = extractContactList(notificationUserContactList);
            String[] mobileNumberList = extractMobileNoList(notificationUserContactList);

            if (contactList != null && contactList.length > 0) {
                logger.debug("Email Contact list found");
                // send Email
                Mail mailMessage = prepareMailMessage(systemName, nodeId, nodeName, nodeUtilizationList, contactList);

                if (kpiMessagingService == null) {
                    kpiMessagingService = new KpiMessagingService(logger);
                }
                MimeMessage mimeMessage = kpiMessagingService.createMimeMessage(mailMessage);

                kpiMessagingService.sendEmail(mimeMessage);
            }

            if (mobileNumberList != null && mobileNumberList.length > 0) {
                logger.debug("Mobile Contact list found");
                if (kpiMessagingService == null) {
                    kpiMessagingService = new KpiMessagingService(logger);
                }
                // send SMS

                String SMSText = prepareSMSText(systemName, nodeId, nodeName, nodeUtilizationList);

                kpiMessagingService.sendSms(SMSText, mobileNumberList);
            }
        }
    }

    private String[] extractMobileNoList(List<NotificationUserContact> notificationUserContactList) {
        List<String> mobileNumberList = new ArrayList<String>();

        for (NotificationUserContact notificationUserContact : notificationUserContactList) {
            String notificationType = notificationUserContact.getNotificationType();
            if ("SMS".equalsIgnoreCase(notificationType) || "Email,SMS".equalsIgnoreCase(notificationType)) {
                mobileNumberList.add(notificationUserContact.getMobileNumber());

            }
        }
        return mobileNumberList.toArray(new String[mobileNumberList.size()]);
    }

    private String[] extractContactList(List<NotificationUserContact> notificationUserContactList) {
        List<String> contactList = new ArrayList<String>();

        for (NotificationUserContact notificationUserContact : notificationUserContactList) {
            String notificationType = notificationUserContact.getNotificationType();
            if ("email".equalsIgnoreCase(notificationType) || "Email,SMS".equalsIgnoreCase(notificationType)) {
                contactList.add(notificationUserContact.getEmail());

            }
        }
        return contactList.toArray(new String[contactList.size()]);
    }

    private Mail prepareMailMessage(String systemName, long nodeId, String nodeName, List<NodeUtilization> nodeUtilizationList, String[] nodeContactList) {

        Mail mailMessage = new Mail();
        if (null != nodeUtilizationList && !nodeUtilizationList.isEmpty()) {
            nodeUtilizationList = sortNodeUtilizationList(nodeUtilizationList);
            if (logger.isDebugEnabled()) {
                for (NodeUtilization nodeUtilization : nodeUtilizationList) {
                    logger.debug("Date:" + nodeUtilization.getTargetTime());
                }
            }

            StringBuffer messageContent = new StringBuffer("Hello,").append("<br><br>")
                    .append("Kindly be notified that the utilization of " + nodeName + " with id: " + nodeId + " that is associated with system: " + systemName)
                    .append("  has exceeded the configured threshold, details are as follows: <br><br>")
                    .append("<html><table style=\"color: #FFFFFF;font-family: 'Lato',Calibri,Arial,sans-serif;font-size: 16px;line-height: 19px;padding: 14px 15px;text-align: left;text-transform: capitalize;word-wrap: break-word;\"><thead style=\"background:#A91607;color:white;\">")
                    .append("<tr><th>Traffic Table Name</th>" +
                            "<th>Utilization</th>" +
                            "<th>Configured Threshold</th>" +
                            "<th>Date/Time</th>" +
                            "</tr>" +
                            "</thead><tbody style=\"color: black;\">");

            for (NodeUtilization nodeUtilization : nodeUtilizationList) {
                messageContent.append("<tr >" +
                        "<td style=\"border: 1px solid black;\">" + nodeUtilization.getTrafficTableName() + "</td>" +
                        "<td style=\"border: 1px solid black;\">" + nodeUtilization.getTrafficTableUtilization() + "</td>" +
                        "<td style=\"border: 1px solid black;\">" + nodeUtilization.getNotificationThreshold() + "</td>" +
                        "<td style=\"border: 1px solid black;\">" + nodeUtilization.getTargetTime() + "</td>" +
                        "</tr>");
            }
            messageContent.append("</tbody></table></html>");

            messageContent.append("<br><br><br>Thanks,<br>Capacity Management Tool");

            mailMessage.setToRecipients(nodeContactList);
            mailMessage.setSubject("[Capacity Management Tool] KPI Threshold notification");
            mailMessage.setContent(messageContent.toString());
        }


        return mailMessage;
    }

    private String prepareSMSText(String systemName, long nodeId, String nodeName, List<NodeUtilization> nodeUtilizationList) {


        StringBuffer messageContent = new StringBuffer("Utilization of Node: ").append(nodeName)
                .append(", id: ").append(nodeId)
                .append(" in System: ").append(systemName).append(" exceeded threshold");
        logger.debug("Message content:" + messageContent);

        return messageContent.toString();
    }

    /**
     * Sort NodeUtilization by date ASC
     *
     * @param nodeUtilizationList
     * @return
     */
    private List<NodeUtilization> sortNodeUtilizationList(List<NodeUtilization> nodeUtilizationList) {
        try {
            Collections.sort(nodeUtilizationList, new Comparator<NodeUtilization>() {

                public int compare(NodeUtilization o1, NodeUtilization o2) {
                    Date d1 = Calendar.getInstance().getTime(), d2 = Calendar.getInstance().getTime();
                    synchronized (SIMPLE_DATE_FORMAT) {
                        try {
                            d1 = SIMPLE_DATE_FORMAT.parse(o1.getTargetTime());
                            d2 = SIMPLE_DATE_FORMAT.parse(o2.getTargetTime());
                        } catch (ParseException e) {
                            logger.error(e);
                            e.printStackTrace();
                        }
                    }
                    return d1.compareTo(d2);
                }
            });
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return nodeUtilizationList;
    }
}
