package com.itworx.vaspp.datacollection.dao.mapper;

import com.itworx.vaspp.datacollection.objects.TrafficTableProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author marwa.goda
 * @since 5/12/13
 */
public class TrafficTablePropertiesMapper {


  public TrafficTableProperties mapRow(ResultSet resultSet) throws SQLException, ParseException {
    TrafficTableProperties trafficTableProperties = new TrafficTableProperties();

    trafficTableProperties.setUtilization(resultSet.getFloat("UTILIZATION"));

    trafficTableProperties.setDateTime(resultSet.getString("DATE_TIME"));

    return trafficTableProperties;
  }
}
