/* 
 * File: PropertyReader.java
 * 
 * Date        Author          Changes
 * 
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Responsible for Reading configuration from properties file
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.generic.XMLUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

  static private String mainPath;

  static private Properties props;

  static private Logger logger = null;

  static public final String SYSTEM_SEPARATOR = System
    .getProperty("file.separator");

  static private final String FILE_NAME = "resources" + SYSTEM_SEPARATOR
    + "configuration" + SYSTEM_SEPARATOR + "app_config.properties";
  private static final String MAIL_CONFIGURATION_PATH = "kpialarm.props.path";
  private static final String MAIL_CONFIGURATION_FILE_NAME = "kpialarm.props.main_file";

  public static void init(String absolutePath) throws ApplicationException {
    //updated by basem to handle the case of init the class from data collection
    init(absolutePath, DataCollectionManager.getLogger());
  }

  /**
   * added by basem to be used by VASPortal Application
   *
   * @param absolutePath
   * @param logger
   * @throws ApplicationException
   */
  public static void init(String absolutePath, Logger logger) throws ApplicationException {
    XMLUtils.init(logger);
    PropertyReader.logger = logger;
    logger.debug("PropertyReader.init() - initating propertyReader");
    mainPath = absolutePath;
    props = new Properties();
    try {
      props.load(new FileInputStream(mainPath + SYSTEM_SEPARATOR
        + FILE_NAME));
    } catch (FileNotFoundException e) {
      // e.printStackTrace();
      logger
        .error("PR - 1000: PropertyReader.init() - could not find application properties file "
          + e);
      throw new ApplicationException(e);
    } catch (IOException e) {
      // e.printStackTrace();
      logger
        .error("PR - 1001: PropertyReader.init() - could not read application properties file "
          + e);
      throw new ApplicationException(e);
    }
  }

  private static String getCompletePath(String path) {
    return mainPath + SYSTEM_SEPARATOR + path;
  }

  public static String getImportedFilesPath() {
    return getCompletePath(props.getProperty("imported_files.path"));
  }

  public static String getAccessClass(String accessMethod) {
    return props.getProperty(accessMethod);
  }

  public static String getTrendAnalysisConfigFilePath() {
    return getCompletePath(props.getProperty("Trend_Analysis_Config_File.Path"));
  }

  public static String getTrendAnalysisConfigFileName() {
    return props.getProperty("Trend_Analysis_Config_File.Name");
  }

  public static String getTrendAnalysisConfigSchemaPath() {
    return getCompletePath(props.getProperty("Trend_Analysis_Config_File.Schema"));
  }

  public static String getInputConfigFilePath() {
    return getCompletePath(props.getProperty("Input_Config_File.Path"));
  }

  public static String getInputConfigFileName() {
    return props.getProperty("Input_Config_File.Name");
  }

  public static String getInputConfigSchemaPath() {
    return getCompletePath(props.getProperty("Input_Config_File.Schema"));
  }

  public static String getConvertedFilesPath() {
    return getCompletePath(props.getProperty("converted_files.path"));
  }

  public static String getPersistencPropertyPath() {
    return getCompletePath(props.getProperty("persistence.props.path"));
  }

  public static String getPersistencPropertyMainFileName() {
    return props.getProperty("persistence.props.main_file");
  }

  public static String getHibernateConfigFile() {
    return getCompletePath(props
      .getProperty("persistence.hibernate_config"));
  }

  public static String getMappingConfigFile() {
    return getCompletePath(props.getProperty("persistence.mapping_config"));
  }
    public static String getCmtSchemaHibernateConfigFile() {
        return getCompletePath(props
                .getProperty("persistence.cmtSchema.hibernate_config"));
    }

  public static int getTimeOffset() {
    String offset = props.getProperty("time_offset");
    int offsetInt = Integer.parseInt(offset);
    return offsetInt;
  }

  public static String getCSNodesNumbers() {
    return props.getProperty("cs.nodes.numbers");
  }

  public static String getScriptsPath() {
    return props.getProperty("scripts_files.path");
  }

  public static String getLogFilesPath() {
    return props.getProperty("log_files.path");
  }

  public static String getConvertedLogFilesPath() {
    return props.getProperty("converted_log_files.path");
  }

  public static String getVodafoneKeys() {
    return props.getProperty("vodafone.keys");
  }

  public static String getMobinilKeys() {
    return props.getProperty("mobinil.keys");
  }

  public static String getEtisalatKeys() {
    return props.getProperty("etisalat.keys");
  }

  public static String get868RequestsSubCodes() {
    return props.getProperty("request868.subCodes");
  }

  public static String getControlFilesPath() {
    return getCompletePath(props.getProperty("control_files.path"));
  }

  public static String getControlFilesSchemaPath() {
    return getCompletePath(props.getProperty("control_files.schema"));
  }

  public static String getTempDBDriver() {
    return props.getProperty("tempdb.driver");
  }

  public static String getTempDBURL() {
    return props.getProperty("tempdb.url");
  }

  public static String getTempDBUserName() {
    return props.getProperty("tempdb.userName");
  }

  public static String getTempDBPassword() {
    return props.getProperty("tempdb.password");
  }

  public static String getTrendAnalysisDriver() {
    return props.getProperty("trendAnalysis.driver");
  }

  public static String getTrendAnalysisURL() {
    return props.getProperty("trendAnalysis.URL");
  }


  public static String getTrendAnalysisUserName() {
    return props.getProperty("trendAnalysis.userName");
  }

  public static String getTrendAnalysisPassword() {
    return props.getProperty("trendAnalysis.password");
  }

  public static String getVelocityTemplatesPath() {
    return getCompletePath(props.getProperty("velocity.templates.path"));
  }

  public static String getTransactionStorePath() {
    return getCompletePath(props.getProperty("trans_store.path"));
  }

  public static String getTransactionWorkingPath() {
    return getCompletePath(props.getProperty("trans_working.path"));
  }

  public static String getConfigurationBackupPath() {
    return getCompletePath(props.getProperty("conf_backup.path"));
  }

  public static String getJobsFileRelativeURL() {
    return props.getProperty("jobs_file.url");
  }

  public static String getLogsFileRelativeURL() {
    return props.getProperty("logs_file.url");
  }

  public static String getInputConfigFileRelativeURL() {
    return props.getProperty("inputConfig_file.url");
  }

  public static String getApplicationPath() {
    return mainPath;
  }

  public static boolean isStopDCInMultiPathsError() {
    boolean isStopDC = true;
    String isStopDCStr = props.getProperty("multiPaths.stopDCInMultiPathsError");
    if (isStopDCStr != null)
      isStopDC = Boolean.parseBoolean(isStopDCStr);
    return isStopDC;
  }

  public static String getCCNCounters() {
    return props.getProperty("ccn.counters");
  }
	public static String getCCNFirstList()
	{
		return props.getProperty("ccn.first.list");
		
	}
	public static String getCCNLinesCount()
	{
		return props.getProperty("ccn.lines.count");
		
	}

  public static String getINAPCipCounters() {
    return props.getProperty("inapCip.counters");
  }

  public static String getCipIpCounters() {
    return props.getProperty("cipip.counters");
  }

  public static String getVoiceChargingCounters() {
    return props.getProperty("voiceCharging.counters");
  }

  public static String getMessagingChargingCounters() {
    return props.getProperty("messagingCharging.counters");
  }

  public static int getHourlyDataCollectionLagRange() {
    int lagRange = -1;
    if (props.getProperty("hourlyDataCollection.lag") != null) {
      try {
        lagRange = Integer.parseInt(props.getProperty("hourlyDataCollection.lag"));
      } catch (Exception ex) {
        logger.error("PR - 1002: PropertyReader.getHourlyDataCollectionLagRange - could parse hourlyDataCollection.lag property and take default [-1] "
          + ex);
      }
    }
    return lagRange;
  }

  public static String getONSConfig() {
    return props.getProperty("ons.config");
  }

  public static String getUssdConnectorsSystemName() {
    return props.getProperty("ussd_connectors.system.name");
  }

  public static String getUssdBrowsersSystemName() {
    return props.getProperty("ussd_browsers.system.name");
  }

  public static String getUssdConnC2SystemName() {

    return props.getProperty("ussd_conn_c2.system.name");
  }

  public static String getUssdGWSubCodes() {
    return props.getProperty("ussd_connectors_gw_subcodes");
  }

  public static String getUssd8SubCodes() {
    return props.getProperty("ussd_8_req_resp_subcodes");
  }

  public static String getDriverName() {
    return props.getProperty("driver.name");
  }

  public static String getOracleThinName() {
    return props.getProperty("oracle.thin.name");
  }

  public static String getUserName() {

    return props.getProperty("user.name");
  }

  public static String getPassword() {
    return props.getProperty("user.password");
  }

  public static String getBinaryFilePath() {
    return getCompletePath(props.getProperty("binary_files.path"));
  }

  public static String getPerlFileName() {
    return getCompletePath(props.getProperty("perl_file.name"));
  }

  public static String getMCKAddress() {
    return props.getProperty("mck_sms.address");
  }

  public static String getRtsEventCounters() {
    return props.getProperty("rtsEventCounters");
  }

  public static String getLastCallCounterName() {
    return props.getProperty("last_call_counter_name");
  }

  public static String getSMSAppId() {
    return props.getProperty("sms_app_id");
  }

  public static String getSMSOriginator() {
    return props.getProperty("sms_originator");
  }

  public static int getSMSMessageType() {
    int messageType = 0;
    try {
      messageType = Integer.parseInt(props.getProperty("sms_message_type"));
    } catch (Exception e) {
      logger.error("ApplicationProperties.getSMSMessageType() - could not parse [sms_message_type] property and take default [" + messageType + "]", e);
    }
    return messageType;
  }

  public static int getSMSOriginatorType() {
    int originType = 2;
    try {
      originType = Integer.parseInt(props.getProperty("sms_originator_type"));
    } catch (Exception e) {
      logger.error("ApplicationProperties.getSMSOriginatorType() - could not parse [sms_originator_type] property and take default [" + originType + "]", e);
    }
    return originType;
  }

  public static int getSMSLanguageId() {
    int langId = 0;
    try {
      langId = Integer.parseInt(props.getProperty("sms_lang_id"));
    } catch (Exception e) {
      logger.error("ApplicationProperties.getSMSLanguageId() - could not parse [sms_lang_id] property and take default [" + langId + "]", e);
    }
    return langId;
  }

  public static int getSMSTrailsNumber() {
    int nTrails = 0;
    try {
      nTrails = Integer.parseInt(props.getProperty("sms_ntrails"));
    } catch (Exception e) {
      logger.error("ApplicationProperties.getSMSTrailsNumber() - could not parse [sms_ntrails] property and take default [" + nTrails + "]", e);
    }
    return nTrails;
  }

  public static int getSMSPriority() {
    int priority = 0;
    try {
      priority = Integer.parseInt(props.getProperty("sms_priority"));
    } catch (Exception e) {
      logger.error("ApplicationProperties.getSMSPriority() - could not parse [sms_priority] property and take default [" + priority + "]", e);
    }
    return priority;
  }

  public static String getSMSDBLinkName() {
    return props.getProperty("sms_dblink");
  }

  public static String getMailHostName() {
    return props.getProperty("vpnxml.mail.hostname");
  }

  public static String getMailPort() {
    return props.getProperty("vpnxml.mail.port");
  }

  public static String getVPNXmlToMail() {
    return props.getProperty("vpnxml.mail.toMail");
  }

  public static String getVPNXmlBCCMail() {
    return props.getProperty("vpnxml.mail.BCCMail");
  }

  public static String getVPNXmlCCMail() {
    return props.getProperty("vpnxml.mail.CCMail");
  }

  public static String getMailFrom() {
    return props.getProperty("vpnxml.mail.from");
  }

  public static String getMailFromName() {
    return props.getProperty("vpnxml.mail.from.name");
  }

  public static String[] getVPNXmlMsisdns() {
    String msisdnStr = props.getProperty("vpnxml.msisdns");
    if (msisdnStr != null && !"".equals(msisdnStr.trim())) {
      if (msisdnStr.contains(","))
        return msisdnStr.split(",");
      else
        return new String[]{msisdnStr};
    }
    return null;
  }

  public static String getSMSMailNotificationConfigFilePath() {
    return getCompletePath(props.getProperty("SmsMail_Notif_Config_File.Path"));
  }

  public static String getSMSMailNotificationConfigFileName() {
    return props.getProperty("SmsMail_Notif_Config_File.Name");
  }

  public static String getSMSMailNotificationConfigSchemaPath() {
    return getCompletePath(props.getProperty("SmsMail_Notif_Config_File.Schema"));
  }

  public static String getSMSMailNotificationURL() {
    return props.getProperty("SMSMailNotification.URL");
  }

  public static String getSMSMailNotificationDriver() {
    return props.getProperty("SMSMailNotification.driver");
  }

  public static String getSMSMailNotificationUserName() {
    return props.getProperty("SMSMailNotification.userName");
  }

  public static String getSMSMailNotificationPassword() {
    return props.getProperty("SMSMailNotification.password");
  }

  public static String getEOCNResala() {
    return props.getProperty("econ_resala");
  }

  public static String getEOCNMega() {
    return props.getProperty("econ_mega");
  }

  public static String getNodeManipulateSystems() {
    return props.getProperty("node_manipulate.systems");
  }

  public static String getPathManipulateSystems() {
    return props.getProperty("path_manipulate.systems");
  }

  public static String getOfflineHWSystemNewStructureNodes() {
    return props.getProperty("offline_hw.new_structure.nodes");
  }

  public static String getOfflineHWSystemNewStructureFileStarts() {
    return props.getProperty("offline_hw.new_structure.file_starts");
  }

  public static String getPropertyValue(String propertyKey) {
    return props.getProperty(propertyKey);
  }

  public static String getDataSourcePropertiesFilePath() {
    return mainPath + SYSTEM_SEPARATOR + props.getProperty("dataSource_properties_file");
  }


  public static String getMailConfigurationFilePath() {
    return getCompletePath(props.getProperty(MAIL_CONFIGURATION_PATH));
  }

  public static String getMailConfigurationFileName() {
    return props.getProperty(MAIL_CONFIGURATION_FILE_NAME);
  }

}
