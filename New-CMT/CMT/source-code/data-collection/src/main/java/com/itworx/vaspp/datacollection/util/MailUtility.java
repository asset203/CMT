/*
 * Created on 02/12/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.itworx.vaspp.datacollection.util;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.objects.ByteArrayDataSource;
import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.objects.MailConfigration;

import java.util.*;


/**
 * @author Amal.Khalil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MailUtility
{
	
	static Logger logger = Logger.getLogger(MailUtility.class); 
	/**
	 * 
	 * @author eshraq.essam
	 * @param mail
	 * @param mailConfigration
	 */
    public static void sendMail(Mail mail,MailConfigration mailConfigration)
	 {
    	logger.debug("MailUtility.sendMail() - started");
	    Properties props = new Properties();
	    props.put("mail.host", mailConfigration.getHostName());
	    props.setProperty("mail.smtp.port", mailConfigration.getPortNo());
	    
	    try {
		       
		      Session mailConnection = Session.getInstance(props, null);
		      
		      Message msg = new MimeMessage(mailConnection);
		      
		      Address sendFrom = new InternetAddress(mail.getFromRecipient(), mail.getFromRecipientName());
		      
		      if(mail.getToRecipients()!=null)
		      {
		          Address sendTo[] = new Address[mail.getToRecipients().length];
		          for (int i = 0; i < mail.getToRecipients().length; i++) 
			      {
			          sendTo[i] =new InternetAddress(mail.getToRecipients()[i]);
			      }
		          msg.setRecipients(Message.RecipientType.TO, sendTo);
		      }
		      
		      if(mail.getCcRecipients()!=null)
		      {
		          Address sendToCC[] = new Address[mail.getCcRecipients().length];
		          for (int i = 0; i < mail.getCcRecipients().length; i++) 
			      {
			          sendToCC[i] =new InternetAddress(mail.getCcRecipients()[i]);
			      }
		          msg.setRecipients(Message.RecipientType.CC, sendToCC);
		      }
		      
		      if(mail.getBccRecipients()!=null)
		      {
		          Address sendToBCC[] = new Address[mail.getBccRecipients().length];
		          for (int i = 0; i < mail.getBccRecipients().length; i++) 
			      {
			          sendToBCC[i] =new InternetAddress(mail.getBccRecipients()[i]);
			      }
		          msg.setRecipients(Message.RecipientType.CC, sendToBCC);

		      }
		      
		      msg.setFrom(sendFrom);  
		      msg.setSubject(mail.getSubject());
		     // msg.setContent(mail.getContent(),"text/html");
		      msg.setDataHandler(new DataHandler(new ByteArrayDataSource(mail.getContent(), "text/html")));


		      
		      Transport.send(msg);
		      
		    }
		    catch (Exception e) {
		    	logger.error("MailUtility.sendMail() - failed",e); 
		    }
		    logger.debug("MailUtility.sendMail() - finsihed");
	 }
    
	public static void sendMail(MailConfigration mailConfig,String senderText,String senderEmail,String mailSubject,StringBuffer content,String[] toMails,String[] ccMails,String[] bccMails) throws Exception {
		Mail mail = new Mail();
		if(toMails != null && toMails.length > 0){
			mail.setToRecipients(toMails);
			mail.setBccRecipients(bccMails);
			mail.setCcRecipients(ccMails);
			mail.setSubject(mailSubject);
			mail.setFromRecipient(senderEmail);
			mail.setFromRecipientName(senderText);
			mail.setContent(content.toString());
			sendMail(mail, mailConfig);
		}else{
			logger.error("MailUtility.sendMail() - no mails passed to function toMails ["+toMails+"]");
		}
	}
	
    
    
    
    public static void main(String[] args) {
	/*	Mail mail= new Mail();
		String[] toRecipients = new String[2]; 
		toRecipients[0]="Ahmad.AbuShady@itworx.com";
		toRecipients[1]="Rania.Helal@itworx.com";
		String[] ccRecipients = new String[2]; 
		ccRecipients[0]="Riham.Fayez@itworx.com";
		ccRecipients[1]="eshraq.essam@itworx.com";
		String[] bccRecipients = new String[1]; 
		bccRecipients[0]="Riham.Fayez@itworx.com";
		mail.setContent("This mail for test");
		mail.setSubject("Hello All");
		mail.setFromRecipient("eshraq.essam@itworx.com");
		mail.setFromRecipientName("Eshraq");
		mail.setToRecipients(toRecipients);
		//mail.setBccRecipients(bccRecipients);
		mail.setCcRecipients(ccRecipients);
		MailConfigration mc = new MailConfigration();
		mc.setHostName("smtp_inout.itworx.com");
		mc.setPortNo("25");
		MailUtility.sendMail(mail, mc);*/
	}

}
