package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.VNotificationCounter;
import com.itworx.vaspp.datacollection.objects.VNotificationSql;
import com.itworx.vaspp.datacollection.objects.VNotificationUser;

public class SMSMailNotificationConfigReader {

	private static final String SEPARATOR = ",";

	private static final String FALSE = "false";

	private static final String TRUE = "true";

	private static final String USER = "user";

	private static final String SQL_VALIDATION_NODES = "validation-nodes";

	private static final String SQL_VALIDATION_TABLE = "validation-table";

	private static final String SQL_TITLE = "title";

	private static final String SQL = "sql";

	private static final String CNTR = "counter";

	private static final String CNTR_SUBJECT = "subject";

	private static final String CNTR_SMS_SENDER = "sms-sender";

	private static final String CNTR_EMAIL_SENDER = "email-sender";

	private static final String CNTR_FAILURE_USERS = "failure-users";

	private static final String CNTR_CC_USERS = "cc-users";

	private static final String CNTR_USERS = "users";

	private static final String CNTR_COMPOUND = "compound";

	private static final String CNTR_ENABLED = "enabled";

	
	private static final String USER_MOBILE = "mobile";

	private static final String USER_EMAIL = "email";

	private static final String ID = "id";


	private static final String FAILURE_MAIL = "failure-mail";

	private static final String FAILURE_SMS = "failure-sms";

	private static final String NOTIFICATION_SMS = "notification-sms";

	
	private org.jdom.Document document;

	private static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");
	private static final Logger logger = Logger.getLogger("SMSMailNotification");

	public SMSMailNotificationConfigReader() throws ApplicationException {
		try {
			logger
					.debug("initating SMSMailNotificationConfigReader: reading system data from xml");

			String xmlPath = PropertyReader
					.getSMSMailNotificationConfigFilePath();
			String xmlName = PropertyReader
					.getSMSMailNotificationConfigFileName();
			String schemaPath = PropertyReader
					.getSMSMailNotificationConfigSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();
			String fileURL = xmlPath + SYSTEM_SEPARATOR + xmlName;
			File configFile = new File(fileURL);
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			validateDocument(fileURL, schemaURL);
			document = builder.build(configFile);
		} catch (IOException e) {
			logger
					.error("SMSMailNotificationConfigReader.SMSMailNotificationConfigReader() : couldn't open xml input file  "
							+ e);
			throw new ApplicationException(e);
		} catch (JDOMException e) {
			logger
					.error("SMSMailNotificationConfigReader.SMSMailNotificationConfigReader() : Invalid Input XML"
							+ e);
			throw new ApplicationException("Invalid Input XML" + e, 1001);
		}
	}

	private void validateDocument(String fileURL, String schemaURL)
			throws ApplicationException {

		try {
			logger
					.debug("SMSMailNotificationConfigReader.validateDocument() - starting validating xml document: "
							+ fileURL + " against schema:" + schemaURL);
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
					"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			factory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			factory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					schemaURL);
			DocumentBuilder builder2 = factory.newDocumentBuilder();
			Validator handler = new Validator();
			builder2.setErrorHandler(handler);
			builder2.parse(fileURL);
			if (handler.validationError == true) {
				logger.error("SMSMailNotificationConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
								+ handler.validationError
								+ " "
								+ handler.saxParseException.getMessage());
				throw new ApplicationException("Invalid XML Document:"
						+ handler.validationError + " "
						+ handler.saxParseException.getMessage());
			} else {
				logger
						.debug("SMSMailNotificationConfigReader.validateDocument() - XML Document is valid");
			}
		} catch (ParserConfigurationException e) {
			logger
					.error("SMSMailNotificationReader.validateDocument() - error with xml parser:"
							+ e);
			throw new ApplicationException("SMSMailNotificationReader.validateDocument() - error with xml parser:"+e);

		} catch (IOException e) {
			logger
					.error("SMSMailNotificationConfigReader.validateDocument() - couldn't access xml input file or schema:"
							+ e);
			throw new ApplicationException("SMSMailNotificationConfigReader.validateDocument() - couldn't access xml input file or schema:"+e);
		} catch (SAXException e) {
			logger
					.error("SMSMailNotificationConfigReader.validateDocument() - XML Document has Error:"
							+ e);
			throw new ApplicationException("SMSMailNotificationConfigReader.validateDocument() - XML Document has Error:"+e);

		}
	}

	private class Validator extends DefaultHandler {
		public boolean validationError = false;

		public SAXParseException saxParseException = null;

		public void error(SAXParseException exception) throws SAXException {
			validationError = true;
			saxParseException = exception;
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			validationError = true;
			saxParseException = exception;
		}

		public void warning(SAXParseException exception) throws SAXException {
		}
	}

	public Map<String, VNotificationUser> getVNotificationUsers() {
		logger.debug("SMSMailNotification.getCounters() - started getVNotificationUsers");
		Element SMSMailNotificationElement = document.getRootElement();
		List userList = SMSMailNotificationElement.getChildren(USER);	
		Map<String, VNotificationUser> vUsersList = new HashMap<String, VNotificationUser>();
		for (int i = 0; i < userList.size(); i++) {
			VNotificationUser user = new VNotificationUser();
			Element userElement = (Element) userList.get(i);
			if (isNotNull (userElement,ID,true)){
				user.setId(Utils.trim(userElement.getAttribute(ID).getValue()));
			}
			if (isNotNull (userElement ,USER_EMAIL,true)){
				user.setEmail(Utils.trim(userElement.getAttribute(USER_EMAIL).getValue()));
			}
			if (isNotNull (userElement ,USER_MOBILE,true)){
				user.setMobileNumber(Utils.trim(userElement.getAttribute(USER_MOBILE)
						.getValue()));
			}
			vUsersList.put(user.getId(), user);
		}
		logger
				.debug("SMSMailNotificationConfigReader.getCounters() - finished getVNotificationUsers");
		return vUsersList;
	}

	public Map<String, VNotificationCounter> getVNotificationCounters() throws ApplicationException {
		logger
				.debug("SMSMailNotification.getCounters() - started getVNotificationCounters");
		Element SMSMailNotificationElement = document.getRootElement();
		Map<String, VNotificationUser> users=getVNotificationUsers();
		List countersList = SMSMailNotificationElement.getChildren(CNTR);
		Map<String, VNotificationCounter> countersMap = new HashMap<String, VNotificationCounter>();
		for (int i = 0; i < countersList.size(); i++) {
			VNotificationCounter counter = new VNotificationCounter();
			Element counterElement = (Element) countersList.get(i);
			if (isNotNull(counterElement ,CNTR_ENABLED ,true)&& TRUE.equalsIgnoreCase(counterElement.getAttribute(CNTR_ENABLED).getValue())) {
				if (isNotNull(counterElement ,CNTR_ENABLED ,true)
						&& FALSE.equalsIgnoreCase(counterElement.getAttribute(CNTR_COMPOUND).getValue())) {
					counter.setCompound(false);
					}				
				if (isNotNull(counterElement,CNTR_USERS,true)) {
						counter.setUsers(counterElement.getAttribute(CNTR_USERS)
								.getValue().split(SEPARATOR));					
					boolean validUsers=validateUsers(users,counter.getUsers());
					if(!validUsers)
					{
						logger
						.error("SMSMailNotification.getCounters() - List of Counter Users "+Arrays.toString(counter.getUsers())+" not exists");
						continue;
					}					
				}
				if (isNotNull(counterElement,CNTR_CC_USERS,true)) {
						counter.setCcUsers(counterElement.getAttribute(CNTR_CC_USERS)
								.getValue().split(SEPARATOR));
					
					boolean validUsers=validateUsers(users,counter.getCcUsers());
					if(!validUsers)
					{
						logger
						.error("SMSMailNotification.getCounters() - List of Counter ccedUsers "+Arrays.toString(counter.getCcUsers())+" not exists");
						continue;
					}
				}
                   
				if (isNotNull(counterElement,CNTR_FAILURE_USERS ,true)) {
					
						counter.setFailureUsers(counterElement.getAttribute(CNTR_FAILURE_USERS)
								.getValue().split(SEPARATOR));
					
					boolean validUsers=validateUsers(users,counter.getFailureUsers());
					if(!validUsers) {
						logger
						.error("SMSMailNotification.getCounters() - List of Counter failedUsers "+Arrays.toString(counter.getFailureUsers())+" not exists");
						continue;
					}
				}
				if (isNotNull(counterElement,NOTIFICATION_SMS,false)) {
					counter.setNotificationSms(Utils.trim(counterElement.getChild(
							NOTIFICATION_SMS).getValue()));
				}
				if (isNotNull(counterElement ,FAILURE_SMS ,false)) {
					counter.setFailureSms(Utils.trim(counterElement
							.getChild(FAILURE_SMS).getValue()));
				}
				if (isNotNull(counterElement ,FAILURE_MAIL,false)) {
					counter.setFailureMail(Utils.trim(counterElement.getChild(
							FAILURE_MAIL).getValue()));
				}
				if (isNotNull(counterElement , FAILURE_MAIL,false)) {
					counter.setFailureMail(Utils.trim(counterElement.getChild(
							FAILURE_MAIL).getValue().toString()));
				}
				if (isNotNull(counterElement , CNTR_EMAIL_SENDER ,true)) {
					counter.setEmailSender(Utils.trim(counterElement.getAttribute(
							CNTR_EMAIL_SENDER).getValue()));
				}
				if (isNotNull(counterElement ,CNTR_SMS_SENDER,true)) {
					counter.setSmsSender(Utils.trim(counterElement.getAttribute(
							CNTR_SMS_SENDER).getValue()));
				}
				if (isNotNull(counterElement , CNTR_SUBJECT ,true)) {
					counter.setSubject(Utils.trim(counterElement.getAttribute(CNTR_SUBJECT)
							.getValue()));
				}
				if (isNotNull(counterElement ,ID,true)) {
					counter.setId(Utils.trim(counterElement.getAttribute(ID).getValue()));
				}
			
				List sqlList = counterElement.getChildren(SQL);
				Map<String, VNotificationSql> sqlMap = new HashMap<String, VNotificationSql>();
				
				if (sqlList != null && !sqlList.isEmpty()) {
					for (int j = 0; j < sqlList.size(); j++) {
						VNotificationSql sql = new VNotificationSql();
						Element sqlElement = (Element) sqlList.get(j);
						if (isNotNull(sqlElement,ID,true)){
							if(sqlMap.containsKey(sqlElement.getAttribute(ID).getValue()))
									{
								logger.error("SMSMailNotification.getCounters() Failed the sql :"+sqlElement.getAttribute(ID).getValue()+" Duplicated inside counter  "+counter.getId());
								throw new ApplicationException("SMSMailNotification.getCounters() Failed the sql :"+sqlElement.getAttribute(ID).getValue()+" Duplicated inside counter  "+counter.getId());							
									}
							else{
							sql.setId(Utils.trim(sqlElement.getAttribute(ID).getValue()));
							}
						}
						if(sqlElement.getValue()!=null){
							if(!sqlElement.getValue().contains(":targetDate"))
							{
								logger.error("SMSMailNotification.getCounters() Failed the sql :"+sql.getId()+"Has dos't contains target date  ");
								throw new ApplicationException("SMSMailNotification.getCounters() Failed the sql :"+sql.getId()+"Has dosn't contains target date  ");
								
							}
							sql.setSql(Utils.trim(sqlElement.getValue()));
						}
						if (isNotNull(sqlElement,SQL_TITLE,true)){
							sql.setTitle(Utils.trim(sqlElement.getAttribute(SQL_TITLE)
									.getValue()));
						}
						if (isNotNull(sqlElement,SQL_VALIDATION_TABLE ,true)){
							sql.setValidationTable(Utils.trim(sqlElement.getAttribute(
									SQL_VALIDATION_TABLE).getValue()));
						}
						
						if (isNotNull(sqlElement ,SQL_VALIDATION_NODES,true)) {
							String nodesValue = sqlElement.getAttribute(SQL_VALIDATION_NODES).getValue();
							if(!Utils.isEmpty(nodesValue)){								
									sql.setValidationNodes(sqlElement.getAttribute(SQL_VALIDATION_NODES).getValue().split(SEPARATOR));								
							}
						}
						sqlMap.put(sql.getId(), sql);
					}
					
					   counter.setSql(sqlMap);
					

				}
			}// end of enebled counters
			
			countersMap.put(counter.getId(), counter);
			
		}
		logger.debug("SMSMailNotification.getCounters() - finished getVNotificationCounters");
		return countersMap;
	}
	public boolean validateUsers(Map<String ,VNotificationUser> vUsers,String []counterUsers) {
		Set<String> set = new HashSet<String>(Arrays.asList(counterUsers));
        if(vUsers.keySet().containsAll(set)){
        	return true ;
        } else {
        	return false;
        }
	}
	public boolean isNotNull(Element counrtElement,String attributeName ,boolean isAttribute){
		if(isAttribute) {
			if(counrtElement.getAttribute(attributeName) != null){
				return true;
			} else{
				return false;
			}
		} else {
			if(counrtElement.getChild(attributeName) != null) {
				return true;
			}
			else {
				return false; 
			}
		}
	
	}
	
}
