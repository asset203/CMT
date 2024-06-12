/*
 * File: PersistenceManager.java
 *
 * Date        Author          Changes
 *
 * 23/01/2006  Nayera Mohamed  Created
 * 08/20/2006  Nayera Mohamed  Upated to include update events table
 *
 * Class Responsible for Persisting Input Data to Reporting DB using hibernate
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.lastcall.LastCall;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.persistenceobjects.PersistenceObject;
import com.itworx.vaspp.datacollection.persistenceobjects.SDP_Subscribers;
import eg.com.vodafone.model.DataColumn;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PersistenceManager {

	private static Properties props;

	private static String filePath = PropertyReader.getPersistencPropertyPath();

	private static String mainFileName;

	private SessionFactory sessionFactory;

	private static Configuration conf;

    private SessionFactory cmtSchemaSessionFactory;

    private static Configuration cmtSchemaConf;

	static private Logger mainLogger = DataCollectionManager.getLogger();

	private int batchSize = 1000;
	
	private static String hiberConfigFile = PropertyReader
			.getHibernateConfigFile();

    private static String cmtSchemaHibernateConfigFile = PropertyReader
            .getCmtSchemaHibernateConfigFile();
	/**
	 * intiating PersistenceManager Loading hibernate configuration and mapping
	 * files
	 * 
	 * @exception ApplicationException
	 *                if an error occured loading files if HibernateException
	 *                occured
	 */
	public PersistenceManager() throws ApplicationException {
		mainLogger
				.debug("PersistenceManager() - initiating persistenceManager");
		props = new Properties();
		try {
			mainFileName = PropertyReader.getPersistencPropertyMainFileName();
			props.load(new FileInputStream(filePath + mainFileName));
			mainLogger.debug("PersistenceManager() - Hibernate:"
					+ hiberConfigFile);
			File hibernateConf = new File(hiberConfigFile);
			conf = new Configuration().configure(hibernateConf);
			String batchSizeStr = conf.getProperties().getProperty("hibernate.jdbc.batch_size");
			if(batchSizeStr != null){
				batchSize = Integer.parseInt(batchSizeStr);
			}
			File mappingConf = new File(PropertyReader.getMappingConfigFile());
			conf.addCacheableFile(mappingConf);
			sessionFactory = conf.buildSessionFactory();

            File cmtSchemaHibernateConf = new File(cmtSchemaHibernateConfigFile);
            cmtSchemaConf = new   Configuration().configure(cmtSchemaHibernateConf);
            cmtSchemaSessionFactory = cmtSchemaConf.buildSessionFactory();
		}
    catch (IOException e) {
			mainLogger
					.error("PS-1000 :PersistenceManager() - Error loading persistence properties files"
							, e);
			throw new ApplicationException("" + e);
		}
		mainLogger
				.debug("PersistenceManager() - finished initiating persistenceManager");

	}
	public PersistenceManager(boolean bd) throws ApplicationException {
		mainLogger
				.debug("PersistenceManager(boolean ) - initiating persistenceManager");
		//props = new Properties();
		try {
			//mainFileName = PropertyReader.getPersistencPropertyMainFileName();
		   //props.load(new FileInputStream(filePath + mainFileName));
			mainLogger.debug("PersistenceManager() - Hibernate:"
					+ hiberConfigFile);
			File hibernateConf = new File(hiberConfigFile);
			conf = new Configuration().configure(hibernateConf);
			/*File mappingConf = new File(PropertyReader.getMappingConfigFile());
			conf.addCacheableFile(mappingConf); */
			sessionFactory = conf.buildSessionFactory();

            File cmtSchemaHibernateConf = new File(cmtSchemaHibernateConfigFile);
            cmtSchemaConf = new   Configuration().configure(cmtSchemaHibernateConf);
            cmtSchemaSessionFactory = cmtSchemaConf.buildSessionFactory();
		}
    catch (Exception e) {
			mainLogger
					.error("PS-1000 :PersistenceManager(boolean ) - Error loading hiberConfigFile"
							, e);
			throw new ApplicationException("" + e);
		}
		mainLogger
				.debug("PersistenceManager() - finished initiating persistenceManager");

	}
	public void finalize() {
		this.sessionFactory.getCurrentSession().close();
		this.sessionFactory.close();
		this.conf = null;

        this.cmtSchemaSessionFactory.getCurrentSession().close();
        this.cmtSchemaSessionFactory.close();
        this.cmtSchemaConf = null;
	}
 

   /**
   * create object holding presistence attributes to be used by DAOs
   * @param inputData -
	 *            the input data that needs to be persisted
   * @return PersistenceAttributes -
   *            Object holding persistence attributes
	 */
   
  public PersistenceAttributes getPersistenceAttributes(InputData inputData) throws ApplicationException
  {
    Logger logger = Logger.getLogger(inputData.getSystemName());
    logger
				.debug("PersistenceManager.getPersistenceAttributes() - getting persistence attributes of input id:"
						+ inputData.getInputID());
		Session session = null;
		Transaction tx = null;
    try
    {
      session = sessionFactory.openSession();
			tx = session.beginTransaction();
      /*
			 * each input has a persistence class its name is specified in the
			 * application main properties file
			 */
			String className = props.getProperty(inputData.getInputID());
      if ( className==null)
      {
        logger
					.error("PS-2001 :PersistenceManager.getPersistenceAttributes() - Couldn't find appropriate Persistence Object class ");
        throw new ApplicationException("");
      }
      Class persistenceClass = Class.forName(className);
			/*
			 * each column name in the inputData header maps to a property in
			 * the persistence class, this mapping is loaded from a properties
			 * file the file name is the same as the input id
			 */
			Properties columnProps = new Properties();
			columnProps.load(new FileInputStream(filePath
					+ inputData.getInputID() + ".properties"));
      PersistenceAttributes attrs = new PersistenceAttributes(persistenceClass,columnProps,session);
      return attrs;

    }
    catch (FileNotFoundException e) {
			logger
					.error("PS-1001 :PersistenceManager.getPersistenceAttributes() - Couldn't find Persistence properties file for id "
							+ inputData.getInputID()+ " " , e);
			throw new ApplicationException("" + e);
		} catch (IOException e) {
			logger
					.error("PS-1002 :PersistenceManager.getPersistenceAttributes() - Couldn't read Persistence properties file for id "
							+ inputData.getInputID() + " " , e);
			throw new ApplicationException("" + e);
		} catch (ClassNotFoundException e) {
			logger
					.error("PS-2001 :PersistenceManager.getPersistenceAttributes() - Couldn't find appropriate Persistence Object class "
							, e);
			throw new ApplicationException("" + e);
		}
  }
  
  /**
	 * clear data and close session
	 * 
	 * @param attrs -
   *            Object holding persistence attributes
	 */
  public void clear(PersistenceAttributes attrs)
  {
    Session session = attrs.getSession();
    if (session != null && session.isOpen())
    {
      session.clear();
      session.close();
    }
  }
  
  
  public void persistObjects(InputData inputData,PersistenceAttributes attrs) throws ApplicationException {
	  this.persistObjects(inputData, attrs, false);
  }
	/**
	 * call update events then commit session data to Reporting DB
	 * 
	 * @param inputData -
	 *            the input data that needs to be persisted
	 * @param attrs -
   *            Object holding persistence attributes
	 * @exception ApplicationException
	 *                if couldn't find or read Persistence properties file for
	 *                id if couldn't find appropriate Persistence Object class
	 *                if HibernateException occured if createPersistenceObjects
	 *                returned exception
	 */
	public void persistObjects(InputData inputData,PersistenceAttributes attrs,boolean hourly) throws ApplicationException {
    Session session = null;
    Logger logger = Logger.getLogger(inputData.getSystemName());
  
    String tableName = "";
    logger
				.debug("PersistenceManager.persistObjects() - started persistObject for inputDate id:"
						+ inputData.getInputID());
		try {
      session = attrs.getSession();
      Class persistenceClass = attrs.getPersistenceClass();
      logger.info("Conf = "+conf);
      
//      logger.info("persistenceClass = "+persistenceClass);
//      if (persistenceClass != null) {
//				logger.info("persistenceClass.getName() = "
//						+ persistenceClass.getName());
//				if (persistenceClass.getName() != null) {
//					logger
//							.info("conf.getClassMapping(persistenceClass.getName()) = "
//									+ conf.getClassMapping(persistenceClass
//											.getName()));
//					if (conf.getClassMapping(persistenceClass.getName()) != null) {
//						logger
//								.info("conf.getClassMapping(persistenceClass.getName()).getTable() = "
//										+ conf.getClassMapping(
//												persistenceClass.getName())
//												.getTable());
//						if (conf.getClassMapping(persistenceClass.getName())
//		 						.getTable() != null)
//							logger
//									.info("conf.getClassMapping(persistenceClass.getName()).getTable().getName() = "
//											+ conf.getClassMapping(
//													persistenceClass.getName())
//													.getTable().getName());
//					}
//				}
//			}
      		if(conf==null || conf.getClassMapping(persistenceClass.getName())==null){
      			logger.info("Re-Intializing hibernate config"); 
      			if(hiberConfigFile==null)
      				hiberConfigFile = PropertyReader.getHibernateConfigFile();
      			File hibernateConf = new File(hiberConfigFile);
    			conf = new Configuration().configure(hibernateConf);
    		    File mappingConf = new File(PropertyReader.getMappingConfigFile());
 				conf.addCacheableFile(mappingConf);
 				logger.info("Class Mapping ="+conf.getClassMapping(persistenceClass.getName()));
    			
      		}
			tableName = conf.getClassMapping(persistenceClass.getName()).getTable()
					.getName();
			// update calendar events in DB
			if(hourly)
				this.updateHourlyEvents(attrs.session.connection(), inputData, tableName);
			else
				this.updateEvents(attrs.session.connection(), inputData, tableName);
		}   catch (HibernateException e) {
			logger
					.error("PS-2002 :PersistenceManager.persistObjects() - Error persisting data "
							, e);
			throw new ApplicationException("" + e);
		} finally {
			synchronized (this) {
				if (session.getTransaction() != null) {
					session.getTransaction().commit();
					
					
					
					this.refreshViews(attrs.session.connection(), inputData, tableName);
					logger
							.debug("PersistenceManager.persistObjects() - commiting transaction");
					
				}
				if (session != null) {
					session.close();
				}
			}
		}
		logger
				.debug("PersistenceManager.persistObjects() - finished persistObject for inputDate id:"
						+ inputData.getInputID());
	}

	/**
	 * loop over data rows, create persistence object for each and save to
	 * hibernate session
	 * 
	 * @param inputData -
	 *            the input data that needs to be persisted
	 * @param //columnProps -
	 *            properties object for this input
	 * @param //PersistenceClass -
	 *            persistance class
	 * @param //session -
	 *            hibernate session for saving object
	 * 
	 * @exception ApplicationException
	 *                if InstantiationException occured if
	 *                IllegalAccessException occured
	 * 
	 */
	
	public void createPersistenceObject(InputData inputData,String[] dataRow,
			PersistenceAttributes attrs)
			throws ApplicationException {
		Logger logger = Logger.getLogger(inputData.getSystemName());
		
		try {
			// loop over rows of data
				// create instance of persistence class associated with this
				// data
        Properties columnProps = attrs.getPersistenceProperties();
        Session session = attrs.getSession();
        Class PersistenceClass = attrs.getPersistenceClass();
        DataColumn[] columns = inputData.getHeader();
				PersistenceObject classObject = (PersistenceObject) PersistenceClass
						.newInstance();
				classObject.setNodeName(inputData.getNodeName());
				classObject.setSystemName(inputData.getSystemName());
				classObject.setDate(inputData.getDate());
				try { // loop over columns in this row
					for (int i = 0; i < columns.length; i++) {
						String colName = columns[i].getName();
						String fieldName = columnProps.getProperty(colName);
						String fieldType = columns[i].getType();					
						String value = dataRow[i];
                                                String valueTime;
						// System.out.println("Persestance Manager.createPersistenceObject : "+ i);
						//logger.debug("\ncolName : " + colName);
						//logger.debug("\nfieldName : " + fieldName);
						//logger.debug("\nfieldType : " + fieldType);
						//logger.debug("\nvalue : " + value);
						// set the field mapped to this column with the given
						// value
                                                //START ::::::: AWAD
                                                /*if(fieldType.equalsIgnoreCase("time")){
                                                   valueTime = dataRow[i-1] + " "+ dataRow[i];
                                                   value = valueTime;
                                                }*/
                                                // END ::::::: AWAD
                           
						this.setField(fieldName, fieldType, value, classObject,
								PersistenceClass, logger,columnProps);
					}
				} catch (NumberFormatException e) {
					logger
							.error("PS-3000: PersistenceManager.createPersistenceObjects() - Input Data format error"
									, e);
				}
				session.save(classObject);
		} catch (InstantiationException e) {
			logger
					.error("PS-2003: PersistenceManager.createPersistenceObjects() - Couldn't instantiate appropriate Persistence Object class "
							, e);
			throw new ApplicationException("" + e);
		} catch (IllegalAccessException e) {
			logger
					.error("PS-2003: PersistenceManager.createPersistenceObjects() - Couldn't access Persistance Object Field "
							, e);
			throw new ApplicationException("" + e);
		}
	}

	/**
	 * for each field call the function appropriate for this field type to set
	 * with the required value
	 * 
	 * @param fieldName -
	 *            name of the field
	 * @param fieldType -
	 *            type of the field
	 * @param value -
	 *            value to be set in this field
	 * @param classObject -
	 *            persistence object
	 * @param PersistenceClass -
	 *            class of persistenc object
	 * @param logger -
	 *            this system logger
	 * 
	 * @exception ApplicationException
	 *                if NoSuchFieldException occured if IllegalAccessException
	 *                occured
	 */
	private void setField(String fieldName, String fieldType, String value,
			Object classObject, Class PersistenceClass, Logger logger,Properties columnProperties)
			throws ApplicationException {
		try {
			/*System.out
					.println("com.itworx.vaspp.datacollection.util.PersistenceManager.setField() " + fieldName);
			System.out
			.println("com.itworx.vaspp.datacollection.util.PersistenceManager.setField() " + fieldType);
			System.out
			.println("com.itworx.vaspp.datacollection.util.PersistenceManager.setField() " + value);*/
			//System.out.println("**************** fieldName ="+fieldName+", fieldType ="+fieldType+", value ="+value+", classObject ="+classObject+", PersistenceClass ="+PersistenceClass+", columnProperties ="+columnProperties);
			if (fieldName == null || fieldName.equals("null")) {
        logger
					.error("PS-1003 :PersistenceManager.setField() - Couldn't find Persistence Object Field ");
        throw new ApplicationException("PS-1003 :Couldn't find Persistence Object Field ");
			}
			if (fieldType.equalsIgnoreCase("number")) {
				PersistenceClass.getField(fieldName).setLong(classObject,
						Long.parseLong(value));
			} else if (fieldType.equalsIgnoreCase("float")) {
				PersistenceClass.getField(fieldName).setDouble(classObject,
						Double.parseDouble(value));
			} else if (fieldType.equalsIgnoreCase("date")) {
				String row = value;
				java.util.Date d ;
				String format=columnProperties!=null?columnProperties.getProperty("DateFormat"):null;
				if(format!=null){
					SimpleDateFormat f = new SimpleDateFormat();
					f.applyPattern(format);
					d=f.parse(value);
				} else {
					d = new Date(value);// f.parse(dataRow[i]);
				}
				
				PersistenceClass.getField(fieldName).set(classObject, d);
				
			} else if (fieldType.equalsIgnoreCase("time")) {
				Time d = Time.valueOf(value);
				PersistenceClass.getField(fieldName).set(classObject, d);
				
			} else if (fieldType.equalsIgnoreCase("string")) {
				PersistenceClass.getField(fieldName).set(classObject, value);
			}
		} catch (NoSuchFieldException e) {
			logger
					.error("PS-1003 :PersistenceManager.setField() - Couldn't find Persistence Object Field "
							, e);
			throw new ApplicationException(e);
		} catch (IllegalAccessException e) {
			logger
					.error("PS-2004:PersistenceManager.setField() - Couldn't access Persistance Object Field "
							, e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger
			.error("PS-2004:PersistenceManager.setField() - Couldn't parse Persistance Date Field "
					, e);
			throw new ApplicationException( e);
		}
	}

	/**
	 * call the update_events stored procedure to update the events table with
	 * the date and system/node
	 * 
	 * @param //session -
	 *            hibernate session
	 * @param inputData -
	 *            the data persisted
	 * @param tableName -
	 *            the name mapped to the persisted object
	 * 
	 * @exception ApplicationException
	 *                if an SQLException occured while executing statement
	 * 
	 */
	public void updateEvents(Connection connection, InputData inputData,
			String tableName) throws ApplicationException {
		Logger logger = Logger.getLogger(inputData.getSystemName());
		try {
			String fmt="dd-MMM-yy";
			if(inputData.isExtractDateMonthly())
			 fmt=  "01-MMM-yy";
			String procedureName="update_events";
			SimpleDateFormat f = new SimpleDateFormat(fmt);
			
			StringBuffer s = f.format(inputData.getDate(), new StringBuffer(),
					new FieldPosition(f.DATE_FIELD));
			
			synchronized (this) {
				String callStatement = "call "+procedureName+"('" + s + "','"
						+ tableName + "','" + inputData.getNodeName() + "')";
				logger
						.debug("PersistenceManager.updateEvents() - updating events "
								+ callStatement);
				// System.out.println("statement "+callStatement);
				connection.createStatement().executeUpdate(
						callStatement);
				connection.commit();
				logger
						.debug("PersistenceManager.updateEvents() - events updated");
			}
		} catch (SQLException e) {
			logger
					.error("PS-4000 :PersistenceManager.updateEvents() - Error updating events "
							, e);
			throw new ApplicationException("" + e);
		}
	}
	
	public void updateGeneralEvents(Connection connection, InputData inputData) throws ApplicationException {
		Logger logger = Logger.getLogger(inputData.getSystemName());
		try {
			SimpleDateFormat f1 = new SimpleDateFormat("dd-MMM-yy");
			StringBuffer s1 = f1.format(inputData.getDate(),
					new StringBuffer(), new FieldPosition(f1.DATE_FIELD));
			synchronized (this) {
				String callStatement = "call UPDATE_GENERIC_EVENTS('" + s1	+ "','" + inputData.getInputID() + "','" + inputData.getNodeName() + "')";
				connection.createStatement().executeUpdate(callStatement);
				logger.debug("GenericPersistenceManager.updateEvents() - events updated");
			}
		} catch (SQLException e) {
			logger.error("GPM-4000 :PersistenceManager.updateGeneralEvents() - Error updating events " , e);
			throw new ApplicationException("" + e);
		}
	}
	
	public void updateHourlyEvents(Connection connection, InputData inputData,
			String tableName) throws ApplicationException {
		Logger logger = Logger.getLogger(inputData.getSystemName());
		try {
			
			String fmt =  "dd-MM-yyyy HH"; 
			String procedureName="update_hourly_events";
			SimpleDateFormat f = new SimpleDateFormat(fmt);
			StringBuffer s = f.format(inputData.getDate(), new StringBuffer(),
					new FieldPosition(f.DATE_FIELD));
			String nodeName=inputData.getNodeName();
			//if(inputData.getNodeName().equalsIgnoreCase(""))// throwing null pointer exception
            if(nodeName == null || "".equals(nodeName))
				{nodeName="all";}
			synchronized (this) {
				String dateStr="to_timestamp('"+s+"','dd-MM-yyyy HH24')";
				String callStatement = "call "+procedureName+"(" + dateStr+ ",'"
				+inputData.getSystemName()+ "','"+ tableName + "','" + nodeName + "')";
				
				logger
						.debug("PersistenceManager.updateEvents() - updating events "
								+ callStatement);
				// System.out.println("statement "+callStatement);
				
				connection.createStatement().executeUpdate(
						callStatement);
				connection.commit();
				logger
						.debug("PersistenceManager.updateHourlyEvents() - events updated");
			}
		} catch (SQLException e) {
			    logger.error("PS-4000 :PersistenceManager.updateHourlyEvents() - Error updating events ", e);
			    throw new ApplicationException(e);

		}
	}
	
	public void deleteOldData(Connection connection,String systemName ,String tableName ,Date date, String nodeName,String nodeColumnName,String dateColumnName, boolean hourly) throws ApplicationException {
		Logger logger = Logger.getLogger(systemName);
		String whereCond = "";	String nodeCond = ""; String dateCond = ""; String and = "";
		String fmt = (hourly) ? "dd/MM/yyyy HH" : "dd/MM/yyyy";
		SimpleDateFormat dayFormat = new SimpleDateFormat(fmt);
		StringBuffer sdate = dayFormat.format(date, new StringBuffer(), new FieldPosition(SimpleDateFormat.DATE_FIELD));
		try {
			synchronized (this) {
				if (!Utils.isEmpty(nodeColumnName) && !Utils.isEmpty(nodeName)) {
					nodeCond = nodeColumnName + "='" + nodeName + "'";
					whereCond += nodeCond;
					and = " AND ";
				}
				if (!Utils.isEmpty(dateColumnName)) {
					if(hourly){
						dateCond = dateColumnName + " = TO_DATE('" + sdate + "','dd/MM/yyyy HH24')";
					} else {
						dateCond = dateColumnName + " < (TO_DATE('"	+ sdate + "','dd/MM/yyyy')+1) AND "+ dateColumnName + " >= TO_DATE('" + sdate + "','dd/MM/yyyy')";
					}
					whereCond += and + dateCond;
				}
				if (!whereCond.equals(""))
					whereCond = " WHERE " + whereCond;
				String deleteStatement = "DELETE FROM " + tableName + whereCond;
				logger.debug("PersistenceManager.deleteOldData() - updating events " + deleteStatement);
				
				try{
					if(connection.isClosed()){
						connection = sessionFactory.openSession().connection();
					}
				}catch (Exception e) {
					logger.error("GPM-4000 :PersistenceManager.updateEvents() - Error while getting opened connection ", e);
				}
				
				connection.createStatement().executeUpdate(deleteStatement);
			}
		} catch (SQLException e) {
			logger.error("GPM-4000 :PersistenceManager.updateEvents() - Error updating events ", e);
			throw new ApplicationException("" + e);
		}
	}

	/**
	 * 
	 * @param //session
	 * @param inputData
	 * @param tableName
	 * @throws ApplicationException
	 */
	public void refreshViews(Connection connection, InputData inputData, 
				String tableName) throws ApplicationException {
			Logger logger = Logger.getLogger(inputData.getSystemName());
			try {
					synchronized (this) {
					String callStatement = "call REFRESH_VIEWS('" +tableName + "')";
					logger
							.debug("PersistenceManager.refreshViews() - REFRESH VIEWS "
									+ callStatement);
					// System.out.println("statement "+callStatement);
					
				connection.createStatement().executeUpdate(
							callStatement);
				connection.commit();
					logger
							.debug("PersistenceManager.refreshViews() - VIEWS REFRESHED");
				}
			} catch (SQLException e) {
				logger
						.error("PS-4000 :PersistenceManager.refreshViews() - Error REFRESHING VIEWS "
								, e);
				throw new ApplicationException("" + e);
			}
			
		
	}
		
		
	
	/**
	 *  create persistence object and save to hibernate session directly
	 * 
	 * @param obj -
	 *            the object to be persisted
	 * 
	 */
	public void savePersistenceObject(PersistenceObject obj){
		Logger logger = DataCollectionManager.getLogger();
		logger.debug("PersistenceManager.savePersistenceObject() - started savePersistenceObject("+obj+")");
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(obj);
		
		synchronized (this) {
			if (session.getTransaction() != null) {
				session.getTransaction().commit();
			}
			if (session != null) {
				session.close();
			}
		}
		logger.debug("PersistenceManager.savePersistenceObject() - finished savePersistenceObject("+obj+")");
	}
	
	public Session getNewSession(){
		Logger logger = DataCollectionManager.getLogger();
		try{
			return sessionFactory.openSession();
		}catch(Exception e){
			logger.error("PersistenceManager.getNewSession() - error while getting new session ",e);
		}
		return null;
	}

    public Session getNewCmtSchemaSession(){
        Logger logger = DataCollectionManager.getLogger();
        try{
            return cmtSchemaSessionFactory.openSession();
        }catch(Exception e){
            logger.error("PersistenceManager.getNewSession() - error while getting new session ",e);
        }
        return null;
    }
	
	public static void main(String arg[]) {
		try {
			PersistenceManager p = new PersistenceManager();
			SDP_Subscribers obj = new SDP_Subscribers();
			obj.setNodeName("testnode");
			obj.setSystemName("testsystem");
			InputData inputdata = new InputData();
			DataColumn d = new DataColumn("MSISDN Range From:", "string");
			DataColumn d2 = new DataColumn("MSISDN Range to:", "string");
			DataColumn[] header = new DataColumn[2];
			header[0] = d;
			header[1] = d2;
			inputdata.setHeader(header);
			Vector data = new Vector();
			String[] data1 = { "101400000", "101449999" };
			String[] data2 = { "161400000", "161449999" };
			data.addElement(data1);
			data.addElement(data2);
			//inputdata.setData(data);
			inputdata.setInputID("sdp_sub_input");
			//p.persistObject(inputdata);
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	public void executeSystemLastCall(Set<String> keySet, Map parameterMap, String systemName, Date targetDate) {
		Logger logger = Logger.getLogger(systemName);
		Session session=null;
		String lastCallClassName =null;
		try{
			session=getNewSession();
			Connection connection=session.connection();
			for(String key:keySet){
				lastCallClassName= "com.itworx.vaspp.datacollection.lastcall.";
			
				lastCallClassName += key;
				Class lastCallClass = Class.forName(lastCallClassName);
				LastCall lastCall = (LastCall)lastCallClass.newInstance();
				lastCall.execute(connection, parameterMap, systemName,targetDate);
			}
		} catch (Exception e) {
				logger.error("PersistenceManager.persistObjects() - error happned while executing last call (" + lastCallClassName + ")",e);
		} finally {
				logger.debug("PersistenceManager.persistObjects() - commiting transaction");
				if (session != null) {
					session.close();
				}
		}
	}
	
	public int getBatchSize() {
		return batchSize;
	}
}
