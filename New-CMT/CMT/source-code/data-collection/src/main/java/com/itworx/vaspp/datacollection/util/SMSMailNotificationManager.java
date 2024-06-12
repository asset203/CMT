package com.itworx.vaspp.datacollection.util;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.hibernate.Session;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dao.SMSEmailNotificationDao;
import com.itworx.vaspp.datacollection.objects.MailConfigration;
import com.itworx.vaspp.datacollection.objects.VNotificationCounter;
import com.itworx.vaspp.datacollection.objects.VNotificationSql;
import com.itworx.vaspp.datacollection.objects.VNotificationUser;

public class SMSMailNotificationManager {
	
	private static final String FAILURE_STR = "[Failure]";
	private static final String SMS_DATE_TITLE_STR = "Date : ";
	private static final String VELOCITY_LOGGING_TAG_STR = "SMSMailNotificationManager.dispatchJob";
	private static final String ENCODING_STR = "UTF-8";
	
	private static final String DATE_PARAM = "targetDate";
	private static final String COUNTER_TITLE = "counterTitles";
	private static final String COUNTER_VALUES = "counterValues";
	private static final String USER_ATTR_EMAIL = "EMAIL";
	private static final String USER_ATTR_MOBILE = "MOBILE";
	private static final String MAIL_TEMPLATE_FILE_NAME = "notificationMail.vm";
	private static final String _ = " ";
	
	private final static Logger logger = Logger.getLogger("SMSMailNotification");
	
	private static PersistenceManager _persistenceManager;
	
	private static String mainPath;
	
	private static MailConfigration mailConfg;
	
	
	public static void init(String path,PersistenceManager persistenceManager) throws ApplicationException {
		init(path);
		_persistenceManager = persistenceManager;
	}
	
	public static void init(String path) throws ApplicationException {
		try {
			mainPath = path;
			PropertyReader.init(mainPath);
			Utils.initVelocity();
			SMSUtility.init();
			
			mailConfg = new MailConfigration();
			mailConfg.setHostName(PropertyReader.getMailHostName());
			mailConfg.setPortNo(PropertyReader.getMailPort());
		} catch (ApplicationException e) {
			logger.error("DataCollectionManager.init() - error initiating DataCollectionManager " + e);
			throw e;
		}
	}

	
	
	public void dispatchJob(Date queryDate, boolean hourly) throws InputException, ApplicationException, ParseException{
		dispatchJob(queryDate, hourly, SMSMailNotificationManager._persistenceManager);
	}
	
	public void dispatchJob(Date queryDate, boolean hourly,PersistenceManager persistenceManager) throws InputException, ApplicationException, ParseException {
		logger.debug("SMSMailNotificationManager.dispatchJob() - started");
		SMSMailNotificationConfigReader smsMailNotificationConfigReader = null;
		Session session = null;
		Connection connection = null;
		try {
			smsMailNotificationConfigReader = new SMSMailNotificationConfigReader();
		} catch (Exception e) {
			logger.error("SMSMailNotificationManager.dispatchJob() - Create new SMSMailNotificationConfigReader - Exception: ", e);
			return;
		}
		try {
			session = persistenceManager.getNewSession();
			connection = session.connection();
			SMSEmailNotificationDao notificationDao = new SMSEmailNotificationDao(connection,logger);
			
			Map<String,VNotificationUser> users = smsMailNotificationConfigReader.getVNotificationUsers();
			Map<String,VNotificationCounter> counters = smsMailNotificationConfigReader.getVNotificationCounters();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(DATE_PARAM, Utils.sqlTimestamp(queryDate));
			boolean unitFailure = false;
			boolean sqlFailure = false;
			for(Entry<String, VNotificationCounter> counterEntry:counters.entrySet()) {
				Map<String,Object> values = new HashMap<String,Object>();
				Map<String,String> titles = new HashMap<String,String>();
				List <String >validSql= new ArrayList<String >();
				VNotificationCounter counter=counterEntry.getValue();
				Map<String,VNotificationSql> sqlMap=counter.getSql();
				sqlFailure = false;
				unitFailure = false;
				
				logger.debug("SMSMailNotificationManager.dispatchJob() - starting execution counter ["+counter.getId()+"]");
				
				Object queryResult = null;
				for(Entry<String, VNotificationSql> sqlEntry:sqlMap.entrySet()) {
					VNotificationSql vsql=sqlEntry.getValue();
					String tableName=vsql.getValidationTable();
					String nodes[]=vsql.getValidationNodes();
					titles.put(vsql.getId(), vsql.getTitle());
					
					logger.debug("SMSMailNotificationManager.dispatchJob() - starting execution counter ["+counter.getId()+"] & sql ["+vsql.getId()+"]");					
					if(notificationDao.validateTableData(tableName,nodes,queryDate,hourly)) {
						validSql.add(vsql.getId());
						queryResult = notificationDao.executeQuery(vsql.getSql(),params);
						if(queryResult != null) {
							values.put(vsql.getId(), queryResult);
						}
					} else {
						sqlFailure = true;
						if(counter.isCompound()) {
							unitFailure = true;
							break;
						}
					}
					logger.debug("SMSMailNotificationManager.dispatchJob() - finished execution counter ["+counter.getId()+"] & sql ["+vsql.getId()+"] & returns ["+queryResult+"]");
				}
				
				if(unitFailure){
					failureNotify(connection, queryDate, users, counter,hourly);
				} else {
					if(sqlFailure){
						if(values.isEmpty()&&validSql.isEmpty()){
							failureNotify(connection, queryDate, users, counter,hourly);
						}else if (!values.isEmpty())
						{
							statusNotify(connection, queryDate, users, values, titles, counter,hourly);
						}
					}else if(!values.isEmpty()){
						statusNotify(connection, queryDate, users, values, titles, counter,hourly);
					}
				}
				
				//logger.info("SMSMailNotificationManager.dispatchJob() - counter ["+counter.getId()+"] has no data to send");
				
				
				logger.debug("SMSMailNotificationManager.dispatchJob() - finished execution counter ["+counter.getId()+"]");
			}
		} catch(Exception e){
			logger.error("SMSMailNotificationManager.dispatchJob() - error happned while execution",e);
		} finally {
			try {
				if(session != null && session.isOpen())
			    {
			      session.clear();
			      session.close();
			    }
				if(connection != null && !connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("SMSMailNotificationManager.dispatchJob() - failed to close session/connection",e);
			} catch (Exception e2) {
				logger.error("SMSMailNotificationManager.dispatchJob() - failed to close session/connection",e2);
			}
		}
		logger.debug("SMSMailNotificationManager.dispatchJob() - finished");
	}

	private void statusNotify(Connection connection, Date queryDate,
			Map<String, VNotificationUser> users, Map<String, Object> values,
			Map<String, String> titles, VNotificationCounter counter,boolean hourly) {
		
		VelocityContext context = new VelocityContext();
		context.put(COUNTER_TITLE, titles);
		context.put(COUNTER_VALUES, values);
		
		String currentDateStr = Utils.convertToDateString(queryDate,(hourly)?"dd-MM-yyyy HH":"dd-MM-yyyy");

		StringWriter mailWriter = new StringWriter();
		StringWriter smsWriter = new StringWriter();
		
		String[] statusUsers = getUserAttributeValues(users,counter.getUsers(),USER_ATTR_EMAIL);
		
		String[] ccedUsers = getUserAttributeValues(users,counter.getCcUsers(),USER_ATTR_EMAIL);
		try {
			Velocity.mergeTemplate(MAIL_TEMPLATE_FILE_NAME, ENCODING_STR,context, mailWriter );
			sendMail(counter.getEmailSender(), PropertyReader.getMailFrom(), counter.getSubject()+_+currentDateStr, mailWriter.getBuffer(), statusUsers , ccedUsers );
		} catch (Exception e1) {
			logger.error("SMSMailNotificationManager.notifyStatus() - failed map the parameters to mail velocity file",e1);
		}
		
		
		String[] mobileNumbers = getUserAttributeValues(users, counter.getUsers(),USER_ATTR_MOBILE);
		
		try {
			Velocity.evaluate(context, smsWriter, VELOCITY_LOGGING_TAG_STR, counter.getNotificationSms());
			sendSMS(connection, counter.getSmsSender(), SMS_DATE_TITLE_STR+currentDateStr+"\n"+ smsWriter.toString() , mobileNumbers);
		} catch (IOException e1) {
			logger.error("SMSMailNotificationManager.notifyStatus() - failed map the parameters to notification sms velocity template",e1);
		}
	}

	private void failureNotify(Connection connection, Date queryDate, Map<String, VNotificationUser> users, VNotificationCounter counter,boolean hourly) {
		String currentDateStr = Utils.convertToDateString(queryDate,(hourly)?"dd-MM-yyyy HH":"dd-MM-yyyy");
		StringBuffer mailContent = new StringBuffer(counter.getFailureMail().trim());
		String smsContent = counter.getFailureSms();
		String[] failureEmails = getUserAttributeValues(users,counter.getFailureUsers(),USER_ATTR_EMAIL);
	
		String[] failureMobileNumbers = getUserAttributeValues(users,counter.getFailureUsers(),USER_ATTR_MOBILE);
		
		sendSMS(connection, counter.getSmsSender(), SMS_DATE_TITLE_STR+currentDateStr+"\n"+ smsContent.trim(), failureMobileNumbers);
		sendMail(counter.getEmailSender(), PropertyReader.getMailFrom(), counter.getSubject()+_+currentDateStr+FAILURE_STR, mailContent, failureEmails, null);
	}
	
	
	
	
	private static void sendMail(String senderText,String fromMail ,String subject,StringBuffer mailBuffer, String[] toMails, String[] ccMails) {
		try {
			MailUtility.sendMail(mailConfg, senderText, fromMail, subject, mailBuffer,toMails , ccMails , null);
		} catch (Exception e) {
			logger.error("SMSMailNotificationManager.dispatchJob() - failed to send mail",e);
		}
	}
	
	
	private void sendSMS(Connection connection,String sender, String smsStr, String[] mobileNumbers) {
		try {
			SMSUtility.sendSMS(connection, smsStr, sender,mobileNumbers);
		} catch (Exception e) {
			logger.error("SMSMailNotificationManager.sendSMS() - failed to send sms",e);
		}
	}
	
	private String[] getUserAttributeValues(Map<String ,VNotificationUser> vUsers,String []counterUsers,String attrbute)	{
		List<String> lst = new ArrayList<String>();
		for(int i=0;i<counterUsers.length;i++) {
			if(vUsers.containsKey(counterUsers[i])){
				String userId = counterUsers[i];
				String value = null;
				if(USER_ATTR_EMAIL.equals(attrbute)){
					value = vUsers.get(userId).getEmail();
				} else if(USER_ATTR_MOBILE.equals(attrbute)) {
					value = vUsers.get(userId).getMobileNumber();
				}
				
				if(!Utils.isEmpty(value)){
					lst.add(value);
				}
			}
		}
		return lst.toArray(new String[lst.size()]);
	}
	
	
	public static void main(String[] args) throws ApplicationException, InputException, ParseException{
		SMSMailNotificationManager.init("D:\\build\\phase11\\DataCollection");
		SMSMailNotificationManager notificationManager = new SMSMailNotificationManager();
		notificationManager.dispatchJob(new Date("11/14/2011"), true,new PersistenceManager(false));
		System.out.println("finished");
	}

}
