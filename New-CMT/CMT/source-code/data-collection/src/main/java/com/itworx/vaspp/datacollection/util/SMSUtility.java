package com.itworx.vaspp.datacollection.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;

public class SMSUtility {
	
	static Logger logger = Logger.getLogger(SMSUtility.class); 
	
	private static String appId = null;
	private static String originator = null;
	private static int messageType = 0;
	private static int originType = 0;
	private static int langId = 0;
	private static int nTrails = 0;
	private static int priority = 0;
	private static String dblinkName = null;
	
	public static void init(){
		appId = PropertyReader.getSMSAppId();
		originator = PropertyReader.getSMSOriginator();
		messageType = PropertyReader.getSMSMessageType();
		originType = PropertyReader.getSMSOriginatorType();
		langId = PropertyReader.getSMSLanguageId();
		nTrails = PropertyReader.getSMSTrailsNumber();
		priority = PropertyReader.getSMSPriority();
		dblinkName = PropertyReader.getSMSDBLinkName();
	}
	
	public static void sendSMS(Connection connection,String messageText,String[] msisdns) throws ApplicationException{
		sendSMS(connection, messageText, SMSUtility.originator, msisdns);
	}
	
	public static void sendSMS(Connection connection,String messageText,String msisdn) throws ApplicationException{
		sendSMS(connection, messageText, SMSUtility.originator, msisdn);
	}
	
	public static void sendSMS(Connection connection,String messageText,String origin,String[] msisdns) throws ApplicationException{
		if(msisdns != null && msisdns.length > 0){
			for(String msisdn: msisdns){
				sendSMS(connection,messageText,origin,msisdn);
			}
		} else {
			logger.error("SMSUtility.sendSMS(multiple smss) - no msisdns passed to functions msisdn ["+msisdns+"]");
		}
	}
	
	public static void sendSMS(Connection connection,String messageText,String origin,String msisdn ) throws ApplicationException{
		logger.debug("SMSUtility.sendSMS() - started");
		if(msisdn != null && !"".equals(msisdn.trim())){
			messageText = messageText.replaceAll("\t", "");
			CallableStatement stmt = null;
			try{
				logger.info("SMSUtility.sendSMS() - sending sms ["+messageText+"] to ["+msisdn+"] with (appId:"+appId+",origin:"+origin+",messageType:"+messageType+",originType:"+originType+",langId:"+langId+",nTrails:"+nTrails+",priority:"+priority+")");
				stmt = connection.prepareCall("begin sendsms@"+dblinkName+"(?, ?, ?, ?, ?, ?, ?, ?, ?); end;");
				//begin sendsms(:appId, :originator, :destination, :messageText, :messageType, :originType, :langId, :nTrails, :priority); end
				//begin sendsms('VASP_ALERT', 'Vodafone', '20168759006', 'Test_TEST', 0, 2, 0, 0, 0); end
				stmt.setObject(1,appId);
				stmt.setObject(2, origin);
				stmt.setObject(3,msisdn);
				stmt.setString(4, messageText);
				stmt.setObject(5,messageType);
				stmt.setObject(6,originType);
				stmt.setObject(7,langId);
				stmt.setObject(8,nTrails);
				stmt.setObject(9,priority);
				stmt.execute();
			} catch(SQLException e){
				logger.error("SMSUtility.sendSMS() - failed to send sms ["+messageText+"] to ["+msisdn+"]",e);
				throw new ApplicationException(e);
			} finally {
				try {
					if(stmt != null)
						stmt.close();
				} catch(SQLException e){
					throw new ApplicationException(e) ;
				}
			}
		} else {
			logger.error("SMSUtility.sendSMS(one sms) - no msisdn passed to functions msisdn ["+msisdn+"]");
		}
		logger.debug("SMSUtility.sendSMS() - finsihed");
	}
	
	public static void sendSMSInternally(Connection connection,String messageText,String origin,String msisdn ) throws ApplicationException{
		logger.debug("SMSUtility.sendSMS() - started");
		if(msisdn != null && !"".equals(msisdn.trim())){
			messageText = messageText.replaceAll("\t", "");
			CallableStatement stmt = null;
			try{
				logger.info("SMSUtility.sendSMS() - sending sms ["+messageText+"] to ["+msisdn+"] with (appId:"+appId+",origin:"+origin+",messageType:"+messageType+",originType:"+originType+",langId:"+langId+",nTrails:"+nTrails+",priority:"+priority+")");
				stmt = connection.prepareCall("begin sendsms(?, ?, ?, ?, ?, ?, ?, ?, ?); end;");
				//begin sendsms(:appId, :originator, :destination, :messageText, :messageType, :originType, :langId, :nTrails, :priority); end
				//begin sendsms('VASP_ALERT', 'Vodafone', '20168759006', 'Test_TEST', 0, 2, 0, 0, 0); end
				stmt.setObject(1,appId);
				stmt.setObject(2, origin);
				stmt.setObject(3,msisdn);
				stmt.setString(4, messageText);
				stmt.setObject(5,messageType);
				stmt.setObject(6,originType);
				stmt.setObject(7,langId);
				stmt.setObject(8,nTrails);
				stmt.setObject(9,priority);
				stmt.execute();
				
			} catch(SQLException e){
				logger.error("SMSUtility.sendSMS() - failed to send sms ["+messageText+"] to ["+msisdn+"]",e);
				throw new ApplicationException(e);
			} finally {
				try {
					if(stmt != null)
						stmt.close();
				} catch(SQLException e){
					throw new ApplicationException(e) ;
				}
			}
		} else {
			logger.error("SMSUtility.sendSMS(one sms) - no msisdn passed to functions msisdn ["+msisdn+"]");
		}
		logger.debug("SMSUtility.sendSMS() - finsihed");
	}
}


