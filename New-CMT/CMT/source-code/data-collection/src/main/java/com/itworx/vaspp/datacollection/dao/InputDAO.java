/* 
 * File:       InputDAO.java
 * Date        Author          Changes
 * 16/01/2006  Nayera Mohamed  Created
 * 
 * 
 * Interface for retrieving Data from Input to be implemented by all DAOs
 */

package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import eg.com.vodafone.model.VInput;

import java.util.Date;

public interface InputDAO {
	void retrieveData(VInput input, Date targetDate)
			throws InputException, ApplicationException;
	void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException;
}