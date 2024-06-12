package com.itworx.vaspp.datacollection.lastcall;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.objects.MailConfigration;
import com.itworx.vaspp.datacollection.objects.VPNXMLAlertData;
import com.itworx.vaspp.datacollection.util.MailUtility;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.SMSUtility;

public class VPNXMLCountersLastCall implements LastCall {
	private Mail mail;
	private MailConfigration mailConfg;

	public void execute(Connection connection, Map parameterMap,String systemName,
			Date targetDate) throws ApplicationException {
		Logger logger = Logger.getLogger(systemName);
		List vpnXMLResults = new ArrayList();
		Statement statement=null ;
		try {

			String fmt = "dd-MM-yyyy HH";
			SimpleDateFormat f = new SimpleDateFormat(fmt);
			StringBuffer s = f.format(targetDate, new StringBuffer(),
					new FieldPosition(f.DATE_FIELD));			
			String counterName[] = null;
			if (PropertyReader.getLastCallCounterName() != null
					&& PropertyReader.getLastCallCounterName().contains(",")) {
				counterName = PropertyReader.getLastCallCounterName()
						.split(",");

			} else				
				counterName = new String[]{PropertyReader.getLastCallCounterName()};

						
			s = f.format(targetDate, new StringBuffer(), new FieldPosition(
					f.DATE_FIELD));
			String counters = '\''+counterName[0]+'\'';
			if(counterName.length > 1){
				for (int i = 1; i < counterName.length; i++) {
					counters = counters + ",\\'" + counterName[i] + "\\'";
				}
			}
			/*String callStatement = "select * from DC_VPN_XML_COUNTERS  where (DATE_TIME=to_date("
					+ dateStr
					+ ",'dd-MM-yyyy HH24') OR DATE_TIME=to_date("
					+ yesterdayDate
					+ ",'dd-MM-yyyy HH24')) and COUNTER_NAME in ("
					+ counters
					+ ")";*/
			
			String callStatement = 
				" SELECT " +
				"   DATE_TIME, " +
				"	NODE_NAME, " +
				"   COUNTER_NAME, " +
				"    NVL(COUNTER_VALUE, 0) TODAY_VALUE, " +
				"    ( " +
				"        SELECT " +
				"            NVL(MAX(COUNTER_VALUE),0) " + 
				"        FROM " +
				"            DC_VPN_XML_COUNTERS yesterday_rec " + 
				"        WHERE today_rec.NODE_NAME = yesterday_rec.NODE_NAME " +
				"        AND today_rec.COUNTER_NAME = yesterday_rec.COUNTER_NAME " +
				"        AND yesterday_rec.DATE_TIME = today_rec.DATE_TIME-7 " +
				"    ) AS YESTERDAY_VALUE " +
				" FROM " +
				"    DC_VPN_XML_COUNTERS today_rec " +
				" WHERE DATE_TIME = TO_DATE('"+s+"','dd-MM-yyyy HH24') AND COUNTER_NAME IN ('VpnCallAttemptCounter')";
			logger.debug("VPNXMLCountersLastCall.LastCall() " + callStatement);
			//System.out.println(callStatement);
			statement =connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			logger.debug("VPNXMLCountersLastCall.LastCall() SQL: "
					+ callStatement);

			ResultSet results = (ResultSet) statement
					.executeQuery(callStatement);

			String smsText="Date: "+s+"\n";
			VPNXMLAlertData vpnXmlAlert=null;
			while (results.next()) {
				vpnXmlAlert= new VPNXMLAlertData();
				vpnXmlAlert.setDateTime(results.getDate(1));
				vpnXmlAlert.setNodeName(results.getString(2));
				vpnXmlAlert.setCounterName(results.getString(3));
				vpnXmlAlert.setTodayValue(results.getLong(4));
				vpnXmlAlert.setYesterdayValue(results.getLong(5));
				vpnXMLResults.add(vpnXmlAlert);
				smsText+= "Node: "+vpnXmlAlert.getNodeName()+"\nToday Value: "+vpnXmlAlert.getTodayValue()+"\nLast Week Value: "+vpnXmlAlert.getYesterdayValue()+"\n";	
				
			}

			
			sendMail(vpnXMLResults ,"VPN Hourly Call Attempts "+s);
			if(vpnXMLResults.size() > 0){
				logger.info("[VPNXMLCountersLastCall.LastCall()] - message :\n"+smsText);
				SMSUtility.init();
				SMSUtility.sendSMS(connection, smsText, PropertyReader.getVPNXmlMsisdns());
			}
		
		} catch (ApplicationException e) {
			logger.error("[VPNXMLCountersLastCall.LastCall()]:Exception:", e);
		
		} catch (SQLException e) {
			logger.error("[VPNXMLCountersLastCall.LastCall()]:Exception:", e);
		} catch (Exception e) {
			logger.error("[VPNXMLCountersLastCall.LastCall()]:Exception:", e);
		} finally {
			
				
				if (statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void sendMail(List vpnXMLList,String mailSubject) throws Exception {
		
		StringBuffer content = new StringBuffer(
				"Dear All\n, <br> "+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"Kindly find the data for system VPN XML Counters <br><br>" +
				"<table  border=\"1\" width=\"100%\" cellpadding=\"5px\" frame=\"box\">"
						+ "<tr height=\"30px\" style=\" vertical-align:top; "
						+ "font-size: 14px; color: #F0F0F0; font-family: verdana, helvetica, arial; "
						+ "font-weight: bold; background-color: #FF0000;\">");

		content.append( "<td>Node Name</td><td>Today Counter Value</td><td>Last Week Counter Value</td>");
		boolean haveData = false;

		/**/
		for (int i = 0; i < vpnXMLList.size(); i++) {
			VPNXMLAlertData vpnXMLAlert = (VPNXMLAlertData) vpnXMLList.get(i);
			haveData = true;
			content
					.append("<tr style = \"font-size: 12px; color: black; "
							+ " font-family: verdana, helvetica, arial; font-weight: normal;\" >"
							+ "<td>" + vpnXMLAlert.getNodeName() + "</td>"							
							+ "<td>" + vpnXMLAlert.getTodayValue() + "</td>"
							+ "<td>" + vpnXMLAlert.getYesterdayValue()
							+ "</td></tr>");
       
		}
		content.append("</table> <br> Thank you, <br>VNPP Team");

		if (haveData) {
			initMailConfig(mailSubject);
			mail.setContent(content.toString());
			MailUtility.sendMail(mail, mailConfg);
		}
	}
	
	
	private void initMailConfig(String subject) {
		mailConfg = new MailConfigration();

		// load properties
		mailConfg.setHostName(PropertyReader.getMailHostName());
		mailConfg.setPortNo(PropertyReader.getMailPort());

		mail = new Mail();

		// get a list of To,BCC,CC
		String toMail = PropertyReader.getVPNXmlToMail();
		String bccMail = PropertyReader.getVPNXmlBCCMail();
		String ccMail = PropertyReader.getVPNXmlCCMail();

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
		mail.setSubject(subject);
		mail.setFromRecipient(PropertyReader.getMailFrom());
		mail.setFromRecipientName(PropertyReader.getMailFromName());
	}

}
