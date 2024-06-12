package com.itworx.vaspp.datacollection.genericextractors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInput;

/**
 * @author Basem.Deiaa
 *
 */
public interface GenericExtractor {
	void init(VInput input) throws ApplicationException, InputException;
    ResultSet performExtractionProcess(GenericInputStructure currentInputStructure,Date targetDate) throws ApplicationException, InputException, SQLException;
    void clean() throws ApplicationException;
}
