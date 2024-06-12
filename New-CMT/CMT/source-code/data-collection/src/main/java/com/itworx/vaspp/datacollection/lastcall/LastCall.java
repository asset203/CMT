package com.itworx.vaspp.datacollection.lastcall;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.InputData;
public interface LastCall {
	void execute(Connection connection,Map parameterMap,String systemName ,Date targetDate)throws ApplicationException;
}
