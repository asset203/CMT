/**
 *
 */
package com.itworx.vaspp.datacollection.logmanager;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.LogErrorRecord;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.objects.MailConfigration;
import com.itworx.vaspp.datacollection.util.*;
import eg.com.vodafone.model.DCLogContacts;
import eg.com.vodafone.model.enums.LogType;
import eg.com.vodafone.model.enums.NotificationType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ahmad.abushady
 */
public class LogManager {

    public static final String FILE_SEPARATOR = System
            .getProperty("file.separator");
    private static final String lOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";
    private static Mail mail;
    private static MailConfigration mailConfg;
    private static Logger logger;

    /**
     * This method initialize the object, by getting the logger, and configuring
     * the mail that will be sent by loading its properties.
     */
    public static void init() {
        logger = Logger.getLogger("LogManager");

        mailConfg = new MailConfigration();
        // load properties
        mailConfg.setHostName(LogPropertiesReader
                .getPropertyValue("mail.hostname"));
        mailConfg.setPortNo(LogPropertiesReader.getPropertyValue("mail.port"));

        mail = new Mail();

        // get a list of To,BCC,CC
        String toMail = LogPropertiesReader.getPropertyValue("mail.toMail");
        String bccMail = LogPropertiesReader.getPropertyValue("mail.BCCMail");
        String ccMail = LogPropertiesReader.getPropertyValue("mail.CCMail");

        // split on ; to get an array of e-mails
        String[] toRecp = toMail.split(";");
        String[] ccRecp = null;
        if (!ccMail.equals(""))
            ccRecp = ccMail.split(";");

        String[] bccRecp = null;
        if (!bccMail.equals(""))
            bccRecp = bccMail.split(";");

        // configure the mail properties
        mail.setToRecipients(toRecp);
        mail.setBccRecipients(bccRecp);
        mail.setCcRecipients(ccRecp);
        mail.setSubject(LogPropertiesReader.getPropertyValue("mail.subject"));
        mail
                .setFromRecipient(LogPropertiesReader
                        .getPropertyValue("mail.from"));
        mail.setFromRecipientName(LogPropertiesReader
                .getPropertyValue("mail.from.name"));


    }

    public static void dispatchLogManagerJob(Date targetDate,
                                             String homeDirectory) throws Exception {
        String filePath = "/resources/configuration/ScheduledJobs.txt";
        PropertyReader.init(homeDirectory);
        LogManager.init();
        SMSUtility.init();
        String systemName, nodeName;
        List errors = null;
        List systemErrors = null;
        SimpleDateFormat smpl = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, List> systemVSerrors = new HashMap<String, List>();
        Map<String, List> systemVNodes = new HashMap<String, List>();
        LogFilesDAO.JobStarted(targetDate);
        File file = new File(homeDirectory + filePath);
        BufferedReader inputStream = new BufferedReader(new FileReader(file));
        while (inputStream.ready()) {
            String line = inputStream.readLine();
            if (line == null || "".equals(line) || !line.contains(","))
                continue;
            String[] lineParts = line.split(",");
            systemName = lineParts[0];
            nodeName = lineParts[1];
            List<String> nodes = systemVNodes.get(systemName);
            if (nodes == null)
                nodes = new ArrayList<String>();
            nodes.add(nodeName);
            systemVNodes.remove(systemName);
            systemVNodes.put(systemName, nodes);
        }
        Set<String> keys = systemVNodes.keySet();
        Iterator<String> keysIterator = keys.iterator();
        while (keysIterator.hasNext()) {
            systemName = keysIterator.next();
            systemErrors = new ArrayList();
            try {
                errors = LogManager.ParseLogs(homeDirectory, systemName, "All", smpl
                        .format(targetDate));
                systemErrors.addAll(errors);
                logger.info("Errors List for System : " + systemName + " = " + errors.size());

            } catch (Exception e) {
                e.printStackTrace();
            }

            systemVSerrors.put(systemName, systemErrors);
        }
        if (systemVSerrors != null && !systemVSerrors.isEmpty()) {
            Map<String, Map<String, List<DCLogContacts>>> errTypeCodecontacts;
            for (Map.Entry<String, List> errorList : systemVSerrors.entrySet()) {
                errTypeCodecontacts = LogFilesDAO.getSystemContactes(errorList.getKey());
                sendErrors(errorList.getKey(), errorList.getValue(), errTypeCodecontacts);
            }

        }


        inputStream.close();
    }

    /**
     * Handle errors and send them to concerned contacts using log type, and error code
     *
     * @param systemName
     * @param systemErrors
     * @param logTypeErrorContacts
     */
    public static void sendErrors(String systemName,
                                  List<LogErrorRecord> systemErrors, Map<String, Map<String,
            List<DCLogContacts>>> logTypeErrorContacts) {

        Map<String, Map<String, List<LogErrorRecord>>> filteredErrorRecords
                = new HashMap<String, Map<String, List<LogErrorRecord>>>();

        Map<String, List<LogErrorRecord>> tempLogErrorMap = new HashMap<String, List<LogErrorRecord>>();

        List<LogErrorRecord> collectiveLogErrors = new ArrayList<LogErrorRecord>();

        for (Map.Entry<String, Map<String, List<DCLogContacts>>> logContact : logTypeErrorContacts.entrySet()) {
            if (logContact.getKey().equals(LogType.ERROR.getValue())) {
                for (Map.Entry<String, List<DCLogContacts>> logContactErrorCode : logContact.getValue().entrySet()) {
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Checking contacts with error code: \"" + logContactErrorCode.getKey()
                                    + "\"\nAfter trim:\"" + logContactErrorCode.getKey().trim() + "\"\nNow looping in error codes");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    for (LogErrorRecord errorRecord : systemErrors) {
                        if (StringUtils.isNotEmpty(logContactErrorCode.getKey())) {
                            if (logContactErrorCode.getKey().equals("-1")) {
                                collectiveLogErrors.add(errorRecord);
                            } else if (logContactErrorCode.getKey().trim().equals(errorRecord.getErrorCode().trim())) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Error code matched:" + logContactErrorCode.getKey().trim());
                                }
                                collectiveLogErrors.add(errorRecord);
                            }
                        }
                    }
                    tempLogErrorMap.put(logContactErrorCode.getKey().trim(), collectiveLogErrors);
                    collectiveLogErrors = new ArrayList<LogErrorRecord>();
                }
            }

        }
        filteredErrorRecords.put(LogType.ERROR.getValue(), tempLogErrorMap);

        if (logger.isDebugEnabled()) {
            logger.debug("Full Filtered error map:" + filteredErrorRecords);
        }

        List<LogErrorRecord> errorRecordsList;
        for (Map.Entry<String, List<DCLogContacts>> logContact : logTypeErrorContacts.get(
                LogType.ERROR.getValue()).entrySet()) {
            errorRecordsList = tempLogErrorMap.get(logContact.getKey().trim());
            if (errorRecordsList != null) {
                constructContactsAndSendNotification(errorRecordsList, logContact.getValue());
            }
        }


    }

    /**
     * @param errorsToSend
     * @param allContacts
     */
    private static void constructContactsAndSendNotification(
            List<LogErrorRecord> errorsToSend, List<DCLogContacts> allContacts) {
        if (logger.isDebugEnabled()) {
            logger.debug("entered constructContactsAndSendNotification with errors list size: "
                    + ((errorsToSend != null) ? errorsToSend.size() : null) + "\nContact list size:"
                    + ((allContacts != null) ? allContacts.size() : null));
        }

        if (errorsToSend != null && !errorsToSend.isEmpty()) {
            List<String> mailRecipients = new ArrayList<String>();
            List<String> mobileRecipients = new ArrayList<String>();

            for (DCLogContacts contact : allContacts) {
                if (contact.getNotificationType() != null
                        && contact.getNotificationType().equalsIgnoreCase(
                        NotificationType.EMAIL.getNotificationTypeStr())
                        || contact.getNotificationType().toLowerCase().
                        contains(NotificationType.EMAIL.getNotificationTypeStr().toLowerCase())) {
                    if (contact.isRegisteredUser()) {
                        mailRecipients.add(contact.getRegisteredContact().getEmail());
                    } else {
                        mailRecipients.add(contact.getExternalContact().geteMail());
                    }
                }

                if (contact.getNotificationType() != null
                        && contact.getNotificationType().equalsIgnoreCase(
                        NotificationType.MOBILE.getNotificationTypeStr())
                        || contact.getNotificationType().toLowerCase().
                        contains(NotificationType.MOBILE.getNotificationTypeStr().toLowerCase())) {
                    if (contact.isRegisteredUser()) {
                        mobileRecipients.add(contact.getRegisteredContact().getMobile());
                    } else {
                        mobileRecipients.add(contact.getExternalContact().getMobileNum());
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Mail :" + mailRecipients + "\nMobile list:" + mobileRecipients);
            }

            if (mailRecipients != null && !mailRecipients.isEmpty()) {
                try {
                    LogManager.sendMail(errorsToSend, mailRecipients.toArray(new String[mailRecipients.size()]));
                } catch (Exception e) {
                    logger.error("Error occurred when send email & SMS", e);
                }
            }
            if (mobileRecipients != null && !mobileRecipients.isEmpty()) {
                try {
                    LogManager.sendSMS(errorsToSend, mobileRecipients.toArray(new String[mobileRecipients.size()]));
                } catch (Exception e) {
                    logger.error("Error occurred when send email & SMS", e);
                }
            }
        }
    }

    /**
     * This method takes an array list of logErrorRecords and put it in html
     * table format and send the mail with the table as the body of the mail. It
     * only sends the e-mail in case of any errors, otherwise it sends nothing.
     *
     * @param recordsArray
     * @param toRecipients
     * @throws Exception
     */
    public static synchronized void sendMail(List<LogErrorRecord> recordsArray, String... toRecipients)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending mail");
        }
        boolean hasBody = false;
        if (recordsArray != null && !recordsArray.isEmpty()) {
            StringBuffer content = new StringBuffer("<table style=\"color: #FFFFFF;font-family: 'Lato',Calibri,Arial,sans-serif;font-size: 16px;line-height: 19px;padding: 14px 15px;text-align: left;text-transform: capitalize;word-wrap: break-word;\"><thead style=\"background:#A91607;color:white;\"><tr>").
                    append("<th>System Name</th>")
                    .append("<th>Date</th>")
                    .append("<th>Node</th>")
                    .append("<th>Type</th>")
                    .append("<th>Error Code</th>")
                    .append("<th>Retry</th>")
                    .append("<th>Description</th>")
                    .append("</tr></thead><tbody style=\"color: black;\">");

            for (LogErrorRecord errorRecord : recordsArray) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Inside mail iteration:" + errorRecord.getDescription());
                }
                if (!StringUtils.isEmpty(errorRecord.getType())
                        && errorRecord.getType().equalsIgnoreCase(LogType.ERROR.getValue())) {
                    content.append("<tr><td style=\"border: 1px solid black;\">" + errorRecord.getSysName() + "</td>");
                    content.append("<td style=\"border: 1px solid black;\">" + errorRecord.getDate_time()
                            + "</td>").append("<td style=\"border: 1px solid black;\">" + errorRecord.getNode() + "</td>").
                            append("<td style=\"border: 1px solid black;\">" + errorRecord.getType() + "</td>").
                            append("<td style=\"border: 1px solid black;\">" + errorRecord.getErrorCode().trim() + "</td>").
                            append("<td style=\"border: 1px solid black;\">" + errorRecord.getRetry() + "</td>").
                            append("<td style=\"border: 1px solid black;\">");
                    if (!StringUtils.isEmpty(errorRecord.getDescription())) {
                        String[] desArr = errorRecord.getDescription().toString().split(",");
                        for (String descEntry : desArr) {
                            content.append(descEntry + "<br>");
                        }
                    }
                    content.append("</td></tr>");
                    hasBody = true;
                }

            }


            content.append("</tbody></table>");
            if (logger.isDebugEnabled()) {
                logger.debug("Mail content: \n" + content);
            }
            if (toRecipients.length > 0 && hasBody) {
                if (logger.isDebugEnabled()) {
                    logger.debug(toRecipients);
                }
                mail.setToRecipients(toRecipients);
            }
            mail.setContent(content.toString());

            if (!StringUtils.isEmpty(content.toString()) && hasBody) {
                MailUtility.sendMail(mail, mailConfg);
                if (logger.isDebugEnabled()) {
                    logger.debug("Mail sent");
                }
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Error log is empty");
            }
        }
    }

    /**
     * This method takes an array list of logErrorRecords and put it in SMS
     * table format and send the mail with the table as the body of the SMS. It
     * only sends the e-mail in case of any errors, otherwise it sends nothing.
     *
     * @param recordsArray
     * @param toRecipients
     * @throws Exception
     */
    public static synchronized void sendSMS(List<LogErrorRecord> recordsArray, String... toRecipients)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending SMS");
        }
        String line = "\n";
        String columnSeparator;

        StringBuffer content = new StringBuffer("Log Manager: Error Report " + line);

        columnSeparator = "   ";

        content.append("System Name" + columnSeparator
                + "Type" + columnSeparator
                + "ERROR Code" + line);

        boolean haveData = false;

        if (recordsArray != null && !recordsArray.isEmpty()) {
            for (LogErrorRecord errorRecord : recordsArray) {
                if (!StringUtils.isEmpty(errorRecord.getType())
                        && errorRecord.getType().equalsIgnoreCase(LogType.ERROR.getValue())) {
                    haveData = true;
                    content.append(errorRecord.getSysName()
                            + columnSeparator
                            + errorRecord.getType()
                            + columnSeparator
                            + errorRecord.getErrorCode().trim()
                            + line);
                        /*cause one record is sufficient for SMS as system sends only
                        System_Name	LOG_TYPE	ERROR_CODE*/
                    break;
                }

            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("No error log");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Final SMS body:\n" + content);
        }


        Connection connection = null;
        Session session = null;
        session = getSession();
        connection = session.connection();

        //mail.setContent(content.toString());
        if (connection != null && haveData) {
            //SQL Connection
            SMSUtility.sendSMS(connection, content.toString(), toRecipients);

            connection.close();
            session.close();
        }
    }

    private static Session getSession() {
        //if(persistenceManager==null){
        PersistenceManager persistenceManager;

        persistenceManager = null;
        try {
            persistenceManager = new PersistenceManager(true);
        } catch (ApplicationException e1) {
            logger.error("Exception : ", e1);
            return null;
        }
        //}
        Session session = null;
        try {
            session = persistenceManager.getNewSession();
            if (session == null) {
                logger.error("Can't pen new database session");
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            return null;
        }
        return session;

    }

    /* This method is the main method of the class where all the business is
        * done. It first initialize the class, then call the different components
        * responsible for parsing the logs. It calls the FtpLogHandler to extract
        * the first file from the log. Then calls the file extractor to put all the
        * data for the needed date in only one temp file. Then calls the parser to
        * parse the temp file for a certain node in a certain system. Then creates
        * the dao to insert the parsed information in the database. Finally it
        * calls sendMail method to send an e-mail of possible errors.
        *
        * @param path
        * @param systemName
        * @param nodeName
        * @param Date
        */
    public static synchronized ArrayList ParseLogs(String path,
                                                   String systemName, String nodeName, String Date) {
        File parserFile = null;
        try {

            System.out.println("Is log initialized?" + ((logger == null) ? null : logger.getAllAppenders()));
            System.out.println("Started Parsing Logs For System: '" + systemName
                    + "' Node: '" + nodeName + "' Date: " + Date);

            logger.info("Started Parsing Logs For System: '" + systemName
                    + "' Node: '" + nodeName + "' Date: " + Date);

            // create file handler and get the first file
            FtpLogHandler fHandler = new FtpLogHandler();
            File logFile = fHandler.readFile(systemName,
                    FtpLogHandler.defaultCount);
            // create file extractor to generate a file for the parser
            LogFilesExtractor fileExtractor = new LogFilesExtractor();
            parserFile = fileExtractor.extractFile(systemName, Date, logFile,
                    nodeName);
            if (parserFile == null)
                return new ArrayList();
            // create the parser
            SystemLogParser parser = new SystemLogParser();

            // get the array of objects from the parser by parsing the file
            ArrayList recordsArray = parser.parse(systemName, nodeName,
                    parserFile);

            // create the dao that will insert the data in the database
            LogFilesDAO fDAO = new LogFilesDAO();

            fDAO.saveLogFilesInfo(recordsArray);
            // sendMail(recordsArray);

            logger.info("Finished Parsing Logs For System: '" + systemName
                    + "' Node: '" + nodeName + "' Date: " + Date);
            return recordsArray;

        } catch (Exception e) {
            logger.error("LogManager.ParseLogs() - " + e);
            e.printStackTrace();
        } finally {
            // House cleaning
            if (parserFile != null) {
                parserFile.delete();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        logger = Logger.getLogger("LogManager");
        // TODO Auto-generated method stub
      /*  args = new String[]{"F:\\Work\\VFE_CMT_SVN\\SourceCode\\vodafone\\data-collection\\src\\main\\resources",
                "Siebel", "All",
                "01/01/2013"};*/
        if (args.length == 2) {
            String path = args[0];
            DOMConfigurator.configure(path + FILE_SEPARATOR + lOG_CONF);
            try {
                DataCollectionManager.init(path);
            } catch (ApplicationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            SimpleDateFormat smpl = new SimpleDateFormat("dd/MM/yyyy");
            Date targetDate = new Date();
            try {
                targetDate = smpl.parse(args[1]);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(1);
            }
            try {
                LogManager.dispatchLogManagerJob(targetDate, path);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (args.length == 4) {
            try {
                String path = args[0];
                init();
                PropertyReader.init(path);
                SMSUtility.init();
                DOMConfigurator.configure(path + FILE_SEPARATOR + lOG_CONF);
                logger.info("start sending email & SMS");
                Map<String, Map<String, List<DCLogContacts>>> errTypeCodecontacts;
                errTypeCodecontacts = LogFilesDAO.getSystemContactes(args[1]);
                if (errTypeCodecontacts != null && !errTypeCodecontacts.isEmpty()) {
                    sendErrors(args[1],
                            LogManager.ParseLogs(path, args[1], args[2], args[3]), errTypeCodecontacts);
                } else {
                    logger.info("No contact list loaded, hence no mail or SMS will be sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage Parameters:");
            System.out.println("");
            System.out
                    .println(" <<path>> <<System Name>> <<Node Name>> <<Date(dd/mm/yyyy)>>");
            System.out.println("OR");
            System.out
                    .println(" <<path>> <<Date(dd/mm/yyyy)>>");
            System.exit(1);
        }


        // For debugging or testing
        /*try{
            String path = "F:\\Work\\VFE_CMt_SVN\\SourceCode\\vodafone\\etc\\resources";
		    DOMConfigurator.configure(path + FILE_SEPARATOR + lOG_CONF);
		LogManager.ParseLogs(path, "VPN_XML", "All", "31/10/2012");
		}catch (Exception e){ e.printStackTrace(); }*/
    }

}
