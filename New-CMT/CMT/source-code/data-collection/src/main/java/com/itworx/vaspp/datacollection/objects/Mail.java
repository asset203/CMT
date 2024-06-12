/*
 * Created on Dec 02, 2007
 *
 */
package com.itworx.vaspp.datacollection.objects;

/**
 * @author eshraq.essam
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mail {
    
    private String fromRecipient;
    
    private String fromRecipientName;
    
    private String[]toRecipients;
    
    private String[]ccRecipients;
    
    private String[]bccRecipients;
    
    private String subject;
    
    private String content;

    /**
     * @return Returns the bccRecipients.
     */
    public String[] getBccRecipients() {
        return bccRecipients;
    }
    /**
     * @param bccRecipients The bccRecipients to set.
     */
    public void setBccRecipients(String[] bccRecipients) {
        this.bccRecipients = bccRecipients;
    }
    /**
     * @return Returns the ccRecipients.
     */
    public String[] getCcRecipients() {
        return ccRecipients;
    }
    /**
     * @param ccRecipients The ccRecipients to set.
     */
    public void setCcRecipients(String[] ccRecipients) {
        this.ccRecipients = ccRecipients;
    }
    /**
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return Returns the fromRecipient.
     */
    public String getFromRecipient() {
        return fromRecipient;
    }
    /**
     * @param fromRecipient The fromRecipient to set.
     */
    public void setFromRecipient(String fromRecipient) {
        this.fromRecipient = fromRecipient;
    }
    /**
     * @return Returns the fromRecipientName.
     */
    public String getFromRecipientName() {
        return fromRecipientName;
    }
    /**
     * @param fromRecipientName The fromRecipientName to set.
     */
    public void setFromRecipientName(String fromRecipientName) {
        this.fromRecipientName = fromRecipientName;
    }
    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return Returns the toRecipients.
     */
    public String[] getToRecipients() {
        return toRecipients;
    }
    /**
     * @param toRecipients The toRecipients to set.
     */
    public void setToRecipients(String[] toRecipients) {
        this.toRecipients = toRecipients;
    }
}
