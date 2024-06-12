package com.itworx.vaspp.datacollection.services;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.dao.KpiAlarmDao;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.SMSUtility;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author marwa.goda
 * @since 5/8/13
 */

public class KpiMessagingService {

    public static final String SYSTEM_SEPARATOR = System
            .getProperty("file.separator");
    public static final String HOST_NAME_KEY = "mail.smtp.host";
    public static final String FROM_KEY = "kpialarm.mail.from";
    public static final String FROM_NAME_KEY = "kpialarm.mail.from.nam";
    private static Logger logger;
    Properties properties = null;


    public KpiMessagingService() {
    }

    public KpiMessagingService(Logger logger) {

        try {
            this.logger = logger;
            SMSUtility.init();
        } catch (Exception e) {
            logger.error("KpiMessagingService() - " + e);
        }
    }

    private void load() {
        properties = new Properties();
        String filePath = PropertyReader.getMailConfigurationFilePath();
        String fileName = PropertyReader.getMailConfigurationFileName();

        try {
            properties.load(new FileInputStream(filePath + SYSTEM_SEPARATOR + fileName));
        } catch (Exception e) {
            logger
                    .debug("KpiMessagingService.load() - Unable To Open properties file: " + fileName);
        }
    }

    public MimeMessage createMimeMessage(Mail mailMessage) {

        this.load();

        javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            // MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);


            mimeMessage.setFrom(new InternetAddress(properties.getProperty(FROM_KEY)));
            mimeMessage.addRecipients(Message.RecipientType.TO,
                    convertStringArrayToAddresses(mailMessage.getToRecipients()));
            mimeMessage.setSubject(mailMessage.getSubject());
            mimeMessage.setContent(mailMessage.getContent(), "text/html");


        } catch (MessagingException e) {
            logger.error("KpiMessagingService.createMimeMessage() - " + e.getMessage());
        }

        return mimeMessage;
    }

    private InternetAddress[] convertStringArrayToAddresses(String[] toRecipients) {
        InternetAddress[] addresses = new InternetAddress[toRecipients.length];
        for (int index = 0; index < toRecipients.length; index++) {
            try {

                addresses[index] = new InternetAddress(toRecipients[index]);
            } catch (AddressException e) {
                logger.error("KpiMessagingService.convertStringArrayToAddresses() - " + e.getMessage());
            }
        }

        return addresses;
    }

    public void sendEmail(MimeMessage mimeMessage) {
        logger.info("KpiMessagingService.sendEmail() - start message sending");
        try {
            Transport.send(mimeMessage);
        } catch (Exception e) {
            logger.error("KpiMessagingService.sendEmail() - " + e.getMessage(), e);
        }
        logger.info("KpiMessagingService.sendEmail() - message has been sent");

    }

    public void sendSms(String smsText, String[] recipients) {
        logger.debug("KpiMessagingService.sendSms() - started sending '"
                + smsText + "' to :" + Arrays.toString(recipients));
        //Get PP connection
        Session ppSession = DataCollectionManager.getPersistenceManager().getNewSession();
        Connection ppConnection = null;
        if (ppSession != null) {
            try {
                ppConnection = ppSession.connection();
                SMSUtility.sendSMS(ppConnection, smsText, recipients);
                logger.debug("KpiMessagingService.sendSms() - finished sending SMS");

            } catch (ApplicationException e) {
                logger.error("KpiMessagingService.sendSms() - " + e.getMessage(), e);
            } finally {
                if (ppConnection != null) {
                    try {
                        ppConnection.close();
                        logger
                                .debug("KpiMessagingService.sendSms() - close Database Connection");
                    } catch (SQLException e) {
                        logger.error("KpiMessagingService.sendSms() - unable to close Database Connection");
                    }
                }

            }
        }
    }
}
