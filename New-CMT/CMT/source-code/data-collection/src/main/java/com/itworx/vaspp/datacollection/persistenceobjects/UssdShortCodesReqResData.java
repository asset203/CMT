package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class UssdShortCodesReqResData extends PersistenceObject {

    public Date dateTime;
    public double noOfRequests;
    public double noOfResponses;
    public String shortCode;
    /**
     * Added by Alia.Adel on 2014.01.06 based on Lina's request
     */
    public String subCode;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getNoOfRequests() {
        return noOfRequests;
    }

    public void setNoOfRequests(double noOfRequests) {
        this.noOfRequests = noOfRequests;
    }

    public double getNoOfResponses() {
        return noOfResponses;
    }

    public void setNoOfResponses(double noOfResponses) {
        this.noOfResponses = noOfResponses;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
}