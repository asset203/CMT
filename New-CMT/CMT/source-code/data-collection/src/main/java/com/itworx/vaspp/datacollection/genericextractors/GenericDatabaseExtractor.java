package com.itworx.vaspp.datacollection.genericextractors;


import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInput;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

public class GenericDatabaseExtractor implements GenericExtractor {

    private VInput input;

    public void init(VInput input) {
        this.input = input;
    }

    private Connection getConnection() {
        return null;
    }

    public ResultSet performExtractionProcess(GenericInputStructure currentInputStructure, Date targetDate) {
        return null;
    }

    public void clean() {
    }
}
