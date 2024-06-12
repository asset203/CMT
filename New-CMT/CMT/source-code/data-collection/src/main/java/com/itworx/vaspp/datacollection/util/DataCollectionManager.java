/* 
 * File: DataCollectionManager.java
 * 
 * Date        Author          Changes
 * 
 * 16/01/2006  Nayera Mohamed  Created
 * 13/04/2006  Nayera Mohamed  Updated with collectSystemAndNodesInputs
 * 
 * Entry point for DataCollection, responsible for initiating configuration reader, persistenceManager 
 * then dispatching jobs
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dao.InputDAO;
import com.itworx.vaspp.datacollection.dao.InputDAOFactory;
import com.itworx.vaspp.datacollection.persistenceobjects.SystemState;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;
import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DataCollectionManager {

    public static final String FILE_SEPARATOR = System
            .getProperty("file.separator");
    static private PersistenceManager persistenceManager;
    private static Logger mainlogger = Logger.getLogger("SchedulerLogger");
    private static String mainPath;
    private static String logFileMaxSize = "1024KB";
    private static String logFileMaxCount = "20";
    private static Priority logFileRollingThreshold = Level.INFO;
    private static Priority logFileConsoleThreshold = Level.INFO;

    /**
     * initate Data Collection Manager setting the path for the application
     * intiating PropertyReader and PersistenceManager
     *
     * @param path -
     *             the path from where the application is running
     * @throws ApplicationException if an error occured initialing PropertyReader or
     *                              PersistenceManager
     */
    public static void init(String path) throws ApplicationException {
        try {
            mainPath = path;
            PropertyReader.init(mainPath);
            persistenceManager = new PersistenceManager();
            /**
             * Setting default time zone
             */
            JobManager.setAppDefaultTimeZone();

        } catch (ApplicationException e) {
            mainlogger.error("DataCollectionManager.init() - error initiating DataCollectionManager ", e);
            throw new ApplicationException("" + e);
        }
    }

    public static PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public static void kill() {
        persistenceManager = null;
    }

    public static Logger getLogger() {
        return mainlogger;
    }

    private static void setLogger(Logger l) {
        mainlogger = l;
    }

    /**
     * Create an appender using given System name
     *
     * @param systemName
     * @return Logger
     */
    public static Logger getSystemLogger(String systemName) {
        if (systemName.contains("(")) {
            systemName = systemName.substring(0, systemName.indexOf("("));
        }
        String filePath = "logs" + FILE_SEPARATOR + systemName + ".log";
        Logger logger = LogManager.getLogger(systemName);

        if(logger == null || (logger != null && !logger.getAllAppenders().hasMoreElements())){
            System.out.println("Logger was not found for system:" + systemName);
            //LogManager.resetConfiguration();
            try {
                logFileMaxCount = PropertyReader.getPropertyValue("log.max.count");
                logFileMaxSize = PropertyReader.getPropertyValue("log.max.size");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Level rollLevel = Level.INFO;
            try {
                rollLevel = Level.toLevel(PropertyReader.getPropertyValue("log.rolling.threshold"));
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }

            logFileRollingThreshold = rollLevel;

            RollingFileAppender rollingFileAppender =
                    new RollingFileAppender();
            rollingFileAppender.setName(systemName);
            rollingFileAppender.setFile(filePath);
            rollingFileAppender.setAppend(true);
            rollingFileAppender.setLayout(
                    new PatternLayout("%d{dd/MM/yyyy HH:mm:ss} %-5p %c %x - %m%n"));
            rollingFileAppender.setMaxFileSize(logFileMaxSize);

            try {
                rollingFileAppender.setMaxBackupIndex(Integer.parseInt(logFileMaxCount));
            } catch (Exception e) {
                e.printStackTrace();
                rollingFileAppender.setMaxBackupIndex(20);
            }

            rollingFileAppender.setThreshold(logFileRollingThreshold);
            try {
                rollingFileAppender.setThreshold(logFileRollingThreshold);
            } catch (Exception e) {
                e.printStackTrace();
                rollingFileAppender.setThreshold(Level.INFO);
            }

            rollingFileAppender.activateOptions();


            ConsoleAppender consoleAppender = new ConsoleAppender();
            consoleAppender.setLayout(new PatternLayout("%d{dd/MM/yyyy HH:mm:ss} %-5p %c %x - %m%n"));
            consoleAppender.setTarget("System.out");
            consoleAppender.setThreshold(logFileConsoleThreshold);
            try {
                logFileConsoleThreshold
                        = Level.toLevel(PropertyReader.getPropertyValue("log.console.threshold"));
            } catch (Exception ex) {
                ex.printStackTrace();
                consoleAppender.setThreshold(Level.INFO);
            }
            consoleAppender.activateOptions();

            logger.addAppender(rollingFileAppender);
            logger.addAppender(consoleAppender);
        }

        return logger;
    }

    /**
     * initiate Data Collection job for the given system if nodeName is system
     * then system inputs are collected if nodeName is a specific node then node
     * inputs are collected The ConfigReader is requested to retrieve the input
     * data then passed to CollectSystemInputs or collectNodeInputs
     *
     * @param systemName -
     *                   the name of the system (same name as in xml file).
     * @param nodeName   -
     *                   the name of the system node (same name as in xml file).
     * @param targetDate -
     *                   the date for data to be collected.
     * @throws InputException       returned from collectSystemInputs or collectNodeInputs
     * @throws ApplicationException returned from collectSystemInputs or collectNodeInputs
     */
    public static void dispatchJob(String systemName, String nodeName, Date targetDate) throws InputException, ApplicationException {
        SimpleDateFormat frm = new SimpleDateFormat();
        frm.applyPattern("MM/dd/yyyy HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(targetDate);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        targetDate = c.getTime();

        // boolean flag to indicate if the job is hourly or daily while monitoring threshold
        final boolean isHourly = false;

        Logger logger = getSystemLogger(systemName);
        NotificationManager notificationManager = new NotificationManager(logger);
        //ConfigReader configReader = new ConfigReader(logger);
        logger.info("DataCollectionManager.dispatchJob() - started dispatchJob( " + systemName + "," + nodeName + "," + targetDate + ")");

        if (nodeName.equals("system")) {
            logger.debug("DataCollectionManager.dispatchJob() - started Data Collection for system specific data of: " + systemName);
            VSystem targetSystem = DatabaseConfigReader.getSystem(systemName, logger);
            collectSystemInputs(targetSystem, targetDate);
        } else if (nodeName.equals("system_nodes")) {
            logger.debug("DataCollectionManager.dispatchJob() - started Data Collection for all system nodes and inputs: " + systemName);
            VSystem targetSystem = DatabaseConfigReader.getSystem(systemName, logger);
            collectSystemAndNodesInputs(targetSystem, targetDate);

            notificationManager.monitorNodeUtilization(systemName, targetDate, isHourly);
        } else {
            logger.debug("DataCollectionManager.dispatchJob() - started Data Collection for node specific data of: " + systemName + " node:" + nodeName);
            VNode targetNode = DatabaseConfigReader.getNode(systemName, nodeName, logger);
                collectNodeInputs(targetNode, targetDate);

                notificationManager.monitorNodeUtilization(targetNode, targetDate, isHourly);

        }


        logger.info("DataCollectionManager.dispatchJob() - finished dispatchJob( " + systemName + "," + nodeName + "," + targetDate + ")");
    }// dispatchJob

    /**
     * initiate Data Collection job for the given system if nodeName is system
     * then system inputs are collected if nodeName is a specific node then node
     * inputs are collected The ConfigReader is requested to retrieve the input
     * data then passed to CollectSystemInputs or collectNodeInputs
     *
     * @param systemName   -
     *                     the name of the system (same name as in xml file).
     * @param //nodeName   -
     *                     the name of the system node (same name as in xml file).
     * @param //targetDate -
     *                     the date for data to be collected.
     * @throws InputException       returned from collectSystemInputs or collectNodeInputs
     * @throws ApplicationException returned from collectSystemInputs or collectNodeInputs
     */

    public static Map getNodesSystemsStructures(String systemName) throws InputException, ApplicationException {
        Map<String, List> systVsStruct = new HashMap<String, List>();
        Logger logger = getSystemLogger(systemName);
        logger.info("DataCollectionManager.getNodesSystemsStructures() - started getNodesSystemsStructures( " + systemName + ")");
        //ConfigReader configReader = new ConfigReader(logger);
        VSystem targetSystem = DatabaseConfigReader.getSystem(systemName, logger);
        VInput[] systemInputs = targetSystem.getInputs();
        VNode[] systemNodes = targetSystem.getNodes();
        List structures = new ArrayList();
        List systemStructures = new ArrayList();
    /*if(systemNodes.length==0)
      {*/
        if (systemInputs.length > 0) {
            for (int i = 0; i < systemInputs.length; i++) {
                if (!systemInputs[i].isPerNode()) {//it means per node
                    for (int j = 0; j < systemInputs[i].getInputStructures().length; j++) {
                        systemStructures.add(systemInputs[i].getInputStructures()[j].getId());
                    }
                }
            }
            if (systemStructures.size() != 0) {
                systVsStruct.put("all", systemStructures);
            }
        }
    /*}
      else//getting the structures from the nodes
      {*/
        for (int k = 0; k < systemNodes.length; k++) {
            List nodeStructures = new ArrayList();
            for (int l = 0; l < systemNodes[k].getInputs().length; l++) {
                if (!systemNodes[k].getInputs()[l].isPerNode() == false) { //it means persystem
                    for (int m = 0; m < systemNodes[k].getInputs()[l].getInputStructures().length; m++) {
                        if (!structures.contains(systemNodes[k].getInputs()[l].getInputStructures()[m].getId())) {
                            nodeStructures.add(systemNodes[k].getInputs()[l].getInputStructures()[m].getId());
                        }
                    }
                }
            }
            systVsStruct.put(systemNodes[k].getName(), nodeStructures);
        }
        //}/**/
        //systVsStruct.put(systemName, structures);
        return systVsStruct;
    }

    public static void dispatchHourlyJob(String systemName, Date targetDate) throws InputException, ApplicationException {
        SimpleDateFormat frm = new SimpleDateFormat();
        frm.applyPattern("MM/dd/yyyy HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(targetDate);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        targetDate = c.getTime();

        // boolean flag to indicate if the job is hourly or daily while monitoring threshold
        final boolean isHourly = true;

        Logger logger = getSystemLogger(systemName);
        NotificationManager notificationManager = new NotificationManager(logger);
        logger.info("DataCollectionManager.dispatchHourlyJob() - started dispatchHourlyJob( " + systemName + "," + targetDate + ")");
        //ConfigReader configReader = new ConfigReader(logger);
        VSystem targetSystem = DatabaseConfigReader.getSystem(systemName, logger);
        VInput[] systemInputs = targetSystem.getInputs();
        VNode[] systemNodes = targetSystem.getNodes();
        boolean allInputsCollected = true;
        Map<String, String> LastCallMap = new HashMap<String, String>();
        if (systemNodes != null && systemNodes.length != 0) {
            for (int j = 0; j < systemNodes.length; j++) {
                logger.info("DataCollectionManager.dispatchHourlyJob() - started collect system (" + systemName + ") node (" + systemNodes[j].getName() + ")");
                VInput[] nodeInputs = systemNodes[j].getInputs();
                for (int i = 0; i < nodeInputs.length; i++) {
                    if (nodeInputs[i].isPerNode()) {
                        try {
                            InputDAO inputDAO = InputDAOFactory.getInputDAO(nodeInputs[i].getType(), persistenceManager);
                            nodeInputs[i].setInputName(Utils.resolveName(nodeInputs[i].getHourlyName(), targetDate));
                            inputDAO.retrieveHourlyData(nodeInputs[i], targetDate);
                            VInputStructure[] inputStructures = nodeInputs[i].getInputStructures();
                            if (inputStructures != null) {
                                for (int s = 0; s < inputStructures.length; s++) {
                                    VInputStructure struct = inputStructures[s];
                                    if (struct.getLastCallClassName() != null)
                                        LastCallMap.put(struct.getLastCallClassName(), struct.getLastCallClassName());
                                }
                            }
                        } catch (Exception e) {
                            logger.error("DataCollectionManager.dispatchHourlyJob() - error collecting node data " + systemNodes[j].getName() + ":", e);
                            allInputsCollected = false;
                        }
                    }
                }
                logger.info("DataCollectionManager.dispatchHourlyJob() - finished collect system (" + systemName + ") node (" + systemNodes[j].getName() + ")");
            }
        }
        logger.info("the allInputsCollected :" + allInputsCollected);
//		if(allInputsCollected)
//			logger
//					.info("DataCollectionManager.dispatchHourlyJob() - finished collectSystemNodes( "
//							+ systemName + "," + targetDate + ")");
//			else
//				throw new InputException("Failed to collect data for nodes of system : "+systemName);


        for (int i = 0; i < systemInputs.length; i++) {
            if (!systemInputs[i].isPerNode()) {
                try {
                    InputDAO inputDAO = InputDAOFactory.getInputDAO(systemInputs[i].getType(), persistenceManager);
                    systemInputs[i].setInputName(Utils.resolveName(systemInputs[i].getHourlyName(), targetDate));
                    inputDAO.retrieveHourlyData(systemInputs[i], targetDate);

                    VInputStructure[] inputStructures = systemInputs[i].getInputStructures();
                    if (inputStructures != null) {
                        for (int s = 0; s < inputStructures.length; s++) {
                            VInputStructure struct = inputStructures[s];
                            if (struct.getLastCallClassName() != null) {
                                LastCallMap.put(struct.getLastCallClassName(), struct.getLastCallClassName());
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("DataCollectionManager.collectSystemAndNodesInputs() - error collecting System Input" + systemInputs[i] + " : ", e);
                    allInputsCollected = false;
                }
            }
        }
        persistenceManager.executeSystemLastCall(LastCallMap.keySet(), null, systemName, targetDate);

        if (allInputsCollected)
            logger.info("DataCollectionManager.dispatchHourlyJob() - finished collectSystemNodes( " + systemName + "," + targetDate + ")");
        else
            throw new InputException("Failed to collect data for nodes of system : " + systemName);

        logger.info("DataCollectionManager.dispatchHourlyJob() - finished dispatchHourlyJob( " + systemName + "," + targetDate + ")");

        notificationManager.monitorNodeUtilization(systemName, targetDate, isHourly);
    }

    public static void dispatchHourlyJobForNodes(String systemName, Date targetDate, String nodeName) throws InputException, ApplicationException {
        SimpleDateFormat frm = new SimpleDateFormat();
        frm.applyPattern("MM/dd/yyyy HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(targetDate);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        targetDate = c.getTime();

        // boolean flag to indicate if the job is hourly or daily while monitoring threshold
        final boolean isHourly = true;

        Logger logger = getSystemLogger(systemName);
        NotificationManager notificationManager = new NotificationManager(logger);
        logger.info("DataCollectionManager.dispatchHourlyJobForNodes() - started dispatchHourlyJobForNodes( " + systemName + "," + targetDate + "," + nodeName + ")");
        //ConfigReader configReader = new ConfigReader(logger);
        VSystem targetSystem = DatabaseConfigReader.getSystem(systemName, logger);
        VInput[] systemInputs = targetSystem.getInputs();
        VNode[] systemNodes = targetSystem.getNodes();
        boolean allInputsCollected = true;

        //if (!nodeName.equalsIgnoreCase("all")) {
        if (!"all".equalsIgnoreCase(nodeName)) {
            for (int j = 0; j < systemNodes.length; j++) {
                if (systemNodes[j].getName().equalsIgnoreCase(nodeName)) {
                    VInput[] nodeInputs = systemNodes[j].getInputs();
                    for (int i = 0; i < nodeInputs.length; i++) {
                        if (nodeInputs[i].isPerNode()) {
                            try {
                                InputDAO inputDAO = InputDAOFactory.getInputDAO(nodeInputs[i].getType(), persistenceManager);
                                nodeInputs[i].setInputName(Utils.resolveName(nodeInputs[i].getHourlyName(), targetDate));
                                inputDAO.retrieveHourlyData(nodeInputs[i], targetDate);
                            } catch (Exception e) {
                                logger.error("DataCollectionManager.dispatchHourlyJobForNodes - error collecting node data" + systemNodes[j].getName() + ":", e);
                                allInputsCollected = false;
                            }
                        }
                    }
                    notificationManager.monitorNodeUtilization(systemNodes[j], targetDate, isHourly);
                }
            }
            logger.info("the allInputsCollected :" + allInputsCollected);
      /*if(allInputsCollected)
           logger
               .info("DataCollectionManager.dispatchHourlyJobForNodes - finished collectSystemNodes( "
                   + systemName + "," + targetDate + ")");
         else
             throw new InputException("Failed to collect data for nodes of system : "+systemName);
         */
        } else {
            for (int i = 0; i < systemInputs.length; i++) {
                if (!systemInputs[i].isPerNode()) {
                    try {
                        InputDAO inputDAO = InputDAOFactory.getInputDAO(systemInputs[i].getType(), persistenceManager);
                        systemInputs[i].setInputName(Utils.resolveName(systemInputs[i].getHourlyName(), targetDate));
                        inputDAO.retrieveHourlyData(systemInputs[i], targetDate);
                    } catch (Exception e) {
                        logger.error("DataCollectionManager.dispatchHourlyJobForNodes - error collecting System Input" + systemInputs[i] + " : ", e);
                        allInputsCollected = false;
                    }
                }
            }
      /*if(allInputsCollected)
           logger
               .info("DataCollectionManager.dispatchHourlyJobForNodes - finished collectSystemNodes( "
                   + systemName + "," + targetDate + ")");
           else
             throw new InputException("Failed to collect data for nodes of system : "+systemName);
         */
            logger.info("DataCollectionManager.dispatchHourlyJobForNodes - finished dispatchHourlyJobForNodes( " + systemName + "," + targetDate + ")");
        }
    }

    /**
     * logs current system status such as memory, cpu
     *
     * @throws ApplicationException returned from collectSystemInputs or collectNodeInputs
     */
    private static String getDate(String datestr) throws ParseException {

        Date date = new Date();
        String dateString;
        SimpleDateFormat inDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.s");
        SimpleDateFormat outDateFormat = new SimpleDateFormat(
                "MM/dd/yyyy HH");


        date = inDateFormat.parse(datestr);
        dateString = outDateFormat.format(date);
        return dateString;

    }

    public static List getHourlyEvent(String systemName, String dateTime, String nodeName) {
        List result = new ArrayList();
        try {

            Session session = persistenceManager.getNewSession();
            if (session == null) {
                mainlogger.error("Can't pen new database session");
                return result;
            }
            Connection connection = session.connection(); //getConnection();

            Statement statement = connection.createStatement();
            String sql = "select " + "*" + " from DC_HOURLY_EVENTS where System='" + systemName + "'" + "and EVENT_DATE = to_date('" + dateTime + "','MM/dd/yyyy HH24') and node_name='" + nodeName + "'";


            ResultSet selectResults = statement.executeQuery(sql);
            while (selectResults.next()) {
                String event_date = (selectResults.getTimestamp(1)).toString();

                try {
                    event_date = getDate(event_date);
                } catch (Exception e) {
                    mainlogger.info(e);
                    continue;
                }
                String system = selectResults.getString(2);
                String struct = selectResults.getString(3);
                String node = selectResults.getString(4);
                result.add(event_date + "," + system + "," + struct + "," + node);

            }
            try {
                connection.close();
                session.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Connection getConnection() throws InputException,
            ApplicationException {

        mainlogger.debug("DBInputDAO.getConnection() - started getConnection for input id=");
        String driverName = "";
        try {

            driverName = "oracle.jdbc.driver.OracleDriver";
            DriverManager.registerDriver((Driver) Class.forName(driverName)
                    .newInstance());
            String DBURL = null;
            if (driverName.indexOf("oracle") != -1) {
                DBURL = "jdbc:oracle:thin:@viper:1521:ER5";
            }
            mainlogger.debug("DBInputDAO.getConnection() - DB url: " + DBURL);
            if (DBURL == null) {
                throw new ApplicationException(" undefined DB Driver "
                        + driverName);
            }
            Connection connection = DriverManager.getConnection(DBURL, "VFE_VAS_PP3", "VFE_VAS_PP3");
            mainlogger.debug("DBInputDAO.getConnection() - finished getConnection for input id=");
            return connection;
        } catch (InstantiationException e) {
            mainlogger.error(" DB-1001: DBInputDAO.getConnection() - Couldn't instantiate DB driver: " + driverName + ":", e);
            e.printStackTrace();
            throw new ApplicationException("" + e);
        } catch (SQLException e) {
            mainlogger.error("DB-1002:DBInputDAO.getConnection() - SQL error while creating connection", e);
            e.printStackTrace();
            throw new InputException("" + e);
        } catch (IllegalAccessException e) {
            //logger.error("-"+ input.getNodeName() +"- DB-1001:DBInputDAO.getConnection() - Error with DB driver "+ driverName + ":" + e);
            throw new ApplicationException("" + e);
        } catch (ClassNotFoundException e) {
            //logger.error("-"+ input.getNodeName() +"- DB-1004: DBInputDAO.getConnection() - Couldn't find DB driver class " + driverName + ":" + e);
            throw new ApplicationException("" + e);
        }
    }

    public static void logCurrentSystemState() throws ApplicationException {
        mainlogger.debug("DataCollectionManager.logCurrentSystemState() - started logCurrentSystemState()");

        Timestamp currentTime = new Timestamp(new Date().getTime());
        Sigar sigar = null;
        long processID = -1;
        try {
            sigar = new Sigar();
            processID = sigar.getPid();
            ProcState procState = sigar.getProcState(processID);
/*
        ProcState procState = sigar.getProcState(pid);

		System.out.println(procState.getName() + " Process state : " + procState.getState());
		System.out.println(procState.getName() + " Process processor : " + procState.getProcessor());
		System.out.println(procState.getName() + " Process nice : " + procState.getNice());
		System.out.println(procState.getName() + " Process Ppid : " + procState.getPpid());
		System.out.println(procState.getName() + " Process priority : " + procState.getPriority());
		System.out.println(procState.getName() + " Process threads : " + procState.getThreads());
		System.out.println(procState.getName() + " Process Tty : " + procState.getTty());

		System.out.println();
		
		ProcState procState2 = sigar.getProcState(procState.getPpid());
		System.out.println(procState2.getName() + " Process state : " + procState2.getState());
		System.out.println(procState2.getName() + " Process processor : " + procState2.getProcessor());
		System.out.println(procState2.getName() + " Process nice : " + procState2.getNice());
		System.out.println(procState2.getName() + " Process Ppid : " + procState2.getPpid());
		System.out.println(procState2.getName() + " Process priority : " + procState2.getPriority());
		System.out.println(procState2.getName() + " Process threads : " + procState2.getThreads());
		System.out.println(procState2.getName() + " Process Tty : " + procState2.getTty());
		
		System.out.println();
		
		Mem mem = sigar.getMem();
		System.out.println("Memory actual free : "+mem.getActualFree());
		System.out.println("Memory actual used : "+mem.getActualUsed());
		System.out.println("Memory free : "+mem.getFree());
		System.out.println("Memory free percent : "+mem.getFreePercent());
		System.out.println("Memory RAM : "+mem.getRam());
		System.out.println("Memory total : "+mem.getTotal());
		System.out.println("Memory used : "+mem.getUsed());
		System.out.println("Memory used percent : "+mem.getUsedPercent());
		
		System.out.println();
		Cpu cpu = sigar.getCpu();
		System.out.println("CPU Idle : "+cpu.getIdle());
		System.out.println("CPU Irq : "+cpu.getIrq());
		System.out.println("CPU Nice : "+cpu.getNice());
		System.out.println("CPU SoftIrq : "+cpu.getSoftIrq());
		System.out.println("CPU Stolen : "+cpu.getStolen());
		System.out.println("CPU Sys : "+cpu.getSys());
		System.out.println("CPU Total : "+cpu.getTotal());
		System.out.println("CPU User : "+cpu.getUser());
		System.out.println("CPU Wait : "+cpu.getWait());

		System.out.println();
		
		ProcMem procMem = sigar.getProcMem(196);
		System.out.println("Process resident memory : " + procMem.getResident());
		System.out.println("Process shared memory : " + procMem.getShare());
		System.out.println("Process virtual memory : " + procMem.getSize());
		
		System.out.println(sigar.getProcTime(196));
		System.out.println(sigar.getProcCpu(196));
				
				*/

            ProcMem procMem = sigar.getProcMem(processID);
            SystemState systemState = new SystemState();

            systemState.setDateTime(currentTime);
            systemState.setProcessMemory(procMem.getSize());
            systemState.setProcessResidentMemory(procMem.getResident());
            systemState.setProcessCpuUsage(procState.getProcessor());

            persistenceManager.savePersistenceObject(systemState);


        } catch (SigarException e) {
            mainlogger.error("DataCollectionManager.logCurrentSystemState() - error occured while using sigar for [" + processID + "] ", e);
        } finally {
            try {
                if (sigar != null) {
                    sigar.close();
                    sigar = null;
                }
            } catch (Exception e) {
                mainlogger.error("DataCollectionManager.logCurrentSystemState() - error occured while closing sigar for [" + processID + "] ", e);
            }
        }

        mainlogger.debug("DataCollectionManager.logCurrentSystemState() - finished logCurrentSystemState()");
    }

    /**
     * Collect input related to system object with flag perNode false
     * InputDAOFactory is called to retrieve the appropriate InputDAO InputData
     * is retreived and passed to persistenceManager persistenceManager persist
     * the Data into reporting DB
     *
     * @param system     -
     *                   the system targeted for data collection.
     * @param targetDate -
     *                   the date for data to be collected.
     * @throws InputException       returned from inputDAO.retrieveData
     * @throws ApplicationException returned from inputDAO.retrieveData or
     *                              persistenceManager.persistObject
     */
    private static void collectSystemInputs(VSystem system, Date targetDate)
            throws InputException, ApplicationException {
        Logger logger = getSystemLogger(system.getName());
        logger
                .info("DataCollectionManager.collectSystemInputs() - started collectSystemInputs( "
                        + system.getName() + "," + targetDate + ")");
        VInput[] systemInputs = system.getInputs();
        boolean allInputsCollected = true;
        for (int i = 0; i < systemInputs.length; i++) {
            if (!systemInputs[i].isPerNode()) {
                try {
                    InputDAO inputDAO = InputDAOFactory.getInputDAO(systemInputs[i]
                            .getType(), persistenceManager);
                    systemInputs[i].setOriginalInputName(systemInputs[i].getInputName());
                    systemInputs[i].setInputName(Utils.resolveName(systemInputs[i].getInputName(), targetDate));
                    inputDAO.retrieveData(systemInputs[i], targetDate);
                } catch (Exception e) {
                    logger.error("DataCollectionManager.collectSystemInputs() - Failed to collect input : " + systemInputs[i].getId() + " , System : " + system.getName() + " , Date : " + targetDate, e);
                    allInputsCollected = false;
                }
            }
        }
        if (allInputsCollected)
            logger
                    .info("DataCollectionManager.collectSystemInputs() - finished collectSystemInputs( "
                            + system.getName() + "," + targetDate + ")");
        else
            throw new InputException("Failed to collect data for inputs of system : " + system.getName());
    }

    /**
     * Collect all input related to system and nodes InputDAOFactory is called
     * to retrieve the appropriate InputDAO InputData is retreived and passed to
     * persistenceManager persistenceManager persist the Data into reporting DB
     *
     * @param system     -
     *                   the system targeted for data collection.
     * @param targetDate -
     *                   the date for data to be collected.
     * @throws InputException       returned from inputDAO.retrieveData
     * @throws ApplicationException returned from inputDAO.retrieveData or
     *                              persistenceManager.persistObject
     */
    private static void collectSystemAndNodesInputs(VSystem system,
                                                    Date targetDate) {
        Logger logger = getSystemLogger(system.getName());
        logger
                .info("DataCollectionManager.collectSystemAndNodesInputs() - started collectSystemAndNodesInputs( "
                        + system.getName() + "," + targetDate + ")");
        VInput[] systemInputs = system.getInputs();
        VNode[] systemNodes = system.getNodes();
        if (systemNodes != null && systemNodes.length != 0) {
            for (int j = 0; j < systemNodes.length; j++) {
                try {
                    collectNodeInputs(systemNodes[j], targetDate);
                } catch (Exception e) {
                    logger
                            .error("DataCollectionManager.collectSystemAndNodesInputs() - error collecting node data"
                                    + systemNodes[j].getName() + ":", e);
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < systemInputs.length; i++) {
            if (!systemInputs[i].isPerNode()) {
                try {
                    InputDAO inputDAO = InputDAOFactory
                            .getInputDAO(systemInputs[i].getType(), persistenceManager);
                    systemInputs[i].setOriginalInputName(systemInputs[i].getInputName());
                    systemInputs[i].setInputName(Utils.resolveName(systemInputs[i].getInputName(), targetDate));
                    inputDAO.retrieveData(systemInputs[i], targetDate);
                } catch (Exception e) {
                    logger
                            .error("DataCollectionManager.collectSystemAndNodesInputs() - error collecting System Input"
                                    + ":", e);
                    e.printStackTrace();
                }
            }
        }
        logger
                .info("DataCollectionManager.collectSystemAndNodesInputs() - finished collectSystemAndNodesInputs( "
                        + system.getName() + "," + targetDate + ")");
    }

    /**
     * Collect input related to node object with flag perNode true
     * InputDAOFactory is called to retrieve the appropriate InputDAO InputData
     * is retreived and passed to persistenceManager  persist
     * the Data into reporting DB
     *
     * @param node       -
     *                   the node targeted for data collection.
     * @param targetDate -
     *                   the date for data to be collected.
     * @throws InputException       returned from inputDAO.retrieveData
     * @throws ApplicationException returned from inputDAO.retrieveData or
     *                              persistenceManager.persistObject
     */

    private static void collectNodeInputs(VNode node, Date targetDate)
            throws InputException, ApplicationException {
        Logger logger = getSystemLogger(node.getSystemName());
        logger
                .info("DataCollectionManager.collectNodeInputs() - started collectNodeInputs( "
                        + node.getName() + "," + targetDate + ")");
        VInput[] nodeInputs = node.getInputs();
        boolean allInputsCollected = true;
        for (int i = 0; i < nodeInputs.length; i++) {
            if (nodeInputs[i].isPerNode()) {
                try {
                    InputDAO inputDAO = InputDAOFactory.getInputDAO(nodeInputs[i]
                            .getType(), persistenceManager);
                    nodeInputs[i].setOriginalInputName(nodeInputs[i].getInputName());
                    nodeInputs[i].setInputName(Utils.resolveName(nodeInputs[i].getInputName(), targetDate));
                    inputDAO.retrieveData(nodeInputs[i], targetDate);
                } catch (Exception e) {
                    logger.error("DataCollectionManager.collectNodeInputs() - Error Collecting input : " + nodeInputs[i].getId() + " , node : " + node.getName() + ", Date : " + targetDate, e);
                    allInputsCollected = false;
                }
            }
        }
        if (allInputsCollected)
            logger
                    .info("DataCollectionManager.collectNodeInputs() - finished collectNodeInputs( "
                            + node.getName() + "," + targetDate + ")");
        else
            throw new InputException("Failed to collect data for inputs of node : " + node.getName());
    }

    // for testing
    public static void main(String arg[]) {
        try {

      /*System.setProperty("log4j.logger.net.sf.hibernate.type","debug");
         System.setProperty("log4j.logger.org.hibernate.SQL","DEBUG, SQL_APPENDER");
         System.setProperty("log4j.additivity.org.hibernate.SQL","false");
         System.setProperty("log4j.logger.org.hibernate.type", "TRACE");*/

      /*Properties logProperties = new Properties();
         logProperties.load(new FileInputStream("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\configuration\\log4j.properties"));
         PropertyConfigurator.configure(logProperties);
         mainlogger = Logger.getRootLogger();*/

            //DOMConfigurator.configure("D:\\vodafone\\etc\\resources\\resources\\configuration\\log4j.xml");
            DOMConfigurator.configure("D:\\Projects src\\SourceCode\\vodafone\\etc\\resources\\resources\\configuration\\log4j.xml");

            //DOMConfigurator.configure("F:\\Work\\VFE_CMT_SVN\\SourceCode\\vodafone\\etc\\resources\\resources\\configuration\\log4j.xml");

            DataCollectionManager engine = new DataCollectionManager();

            // engine.init("D:\\ITWorx\\Projects\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection");

            //engine.init("D:\\vodafone\\etc\\resources");

            engine.init("D:\\Projects src\\SourceCode\\vodafone\\etc\\resources");

            //engine.init("F:\\Work\\VFE_CMT_SVN\\SourceCode\\vodafone\\etc\\resources");


            SimpleDateFormat frm = new SimpleDateFormat();
            /*frm.applyPattern("dd/MM/yyyy HH");
            Date date = frm.parse("13/07/2011 11");
            engine.dispatchJob("RX","system_nodes",date); */

           /*frm.applyPattern("dd/MM/yyyy");
            Date date = frm.parse("01/06/2011");
            engine.dispatchJob("USSDShortCodes_C2","system_nodes",date); */

            frm.applyPattern("dd/MM/yyyy");
            Date date = frm.parse("30/05/2013");
            //engine.dispatchJob("Wincash_TRX","system",date);
            engine.dispatchJob("sys_db_1", "system_nodes", date);

            /*frm.applyPattern("dd/MM/yyyy");
            Date date = frm.parse("29/07/2011");
            engine.dispatchJob("BUZZ","system_nodes",date);*/

            //engine.dispatchJob("sys_xml_ven", "system", date);
            //engine.dispatchJob("sys_xml_ven","system",date) ;
            /*Date date = frm.parse("01/04/2013");
            engine.dispatchJob("3alnota_new_system", "N1",date);*/
            //engine.dispatchJob("System_DC_XML","Node2",date);
            //engine.dispatchJob("System_execution","3rd_node",date);
            // engine.dispatchJob("SYS_Simple_xml","system_nodes",date) ;  sys_xml_ven
            // engine.dispatchJob("MMS","system",date) ;//"MM" "09"

      /*start:  added by Basma */
      /* 1- DB Direct access = true
        engine.dispatchJob("VF_CDR_RECON_PROCESS","system_nodes",date); //"dd/MM/yyyy"  "25/07/2010"
      */
      /*2- DB direct access =false
         engine.dispatchJob("NEW_MINI_CALL","system_nodes",date);//("01/01/2011")"11/01/2011" "dd/MM/yyyy"
      */
      /*3- text direct mapping =true
         engine.dispatchJob("EOCN_DATA_CCN","system_nodes",date); //"dd/MM/yyyy"  "23/10/2012"
      */
      /*4- text direct mapping = false
        engine.dispatchJob("EOCN_BROWSERS","system_nodes",date) ;//"yyyy/MM/dd HH"  "2012/12/05 08"
      */
      /*Excel
           engine.dispatchJob("MMS","system_nodes",date); //("09") "MM"
           engine.dispatchJob("MMS","system",date) ;
      */

      /*end:  added by Basma */
            //----------------------------------------------------------------------------
            // engine.dispatchJob("IVR_forecast","system",new
            // Date());//,"D:\\jdev9051\\jdev\\mywork\\myworkspace\\VFE_VAS_Performance_Portal");
            // engine.dispatchJob("RBT","RBT1",new Date("03/11/2006"));
            //engine.dispatchJob("MCK","system",new Date("06/14/2006"));
//			engine.dispatchJob("USSD","system",new Date("06/22/2006"));

//			engine.dispatchJob("ussd_868","system_nodes", new Date("02/04/2009"));

//			engine.dispatchJob("sdp_links_utilization","system_nodes", new Date("07/01/2008"));
//			engine.dispatchJob("sdp_links_utilization","system_nodes", new Date("07/02/2008"));
//			engine.dispatchJob("sdp_links_utilization","system_nodes", new Date("07/03/2008"));


//			engine.dispatchJob("MSP","system_nodes", new Date("06/30/2008"));  //data session distribution
//			engine.dispatchJob("MSP","system_nodes", new Date("07/30/2008"));  //Unique MSISDNs
//			engine.dispatchJob("MSP","system_nodes", new Date("07/27/2008"));  //Top Handsets,Top Websites
//			engine.dispatchJob("Mediation","system_nodes", new Date("07/26/2008"));  //Corrupted Files
//			engine.dispatchJob("Mediation","system_nodes", new Date("08/26/2008"));  //CPU Memory Utilization

      /*			engine.dispatchJob("Requests858","system_nodes", new Date("02/03/2009"));
            engine.dispatchJob("Requests300","system_nodes", new Date("02/03/2009"));
            engine.dispatchJob("Requests301","system_nodes", new Date("02/03/2009"));
      */
            //engine.dispatchJob("Family","system_nodes", new Date("03/17/2009"));

            //to run USSDShortCodes
      /*engine.dispatchJob("USSDShortCodes","system_nodes", new Date("02/03/2009"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("04/10/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("12/13/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("12/09/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("12/10/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("02/04/2009"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("04/10/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("04/11/2007"));
         engine.dispatchJob("USSDShortCodes","system_nodes", new Date("04/12/2007"));
         */
            //engine.dispatchJob("VASP_Middleware","system_nodes", new Date("04/01/2009"));

//			engine.dispatchJob("VASP", "system_nodes", new Date("07/19/2007"));

//			engine.dispatchJob("NEW_CS_error", "system_nodes", new Date("05/16/2009"));


      /*
         engine.dispatchJob("OMN", "system_nodes", new Date("05/25/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("05/26/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("05/27/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("05/28/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("05/29/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/01/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/02/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/03/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/04/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/05/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/06/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/07/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/08/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/09/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/10/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/11/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/12/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/13/2009"));
         engine.dispatchJob("OMN", "system_nodes", new Date("06/14/2009"));
       */

//			engine.dispatchJob("VBUS", "system_nodes", new Date("01/01/2009"));
      /*
         engine.dispatchJob("call_collect_service_sys","system_nodes", new Date("05/24/2009"));
         engine.dispatchJob("call_collect_service_sys","system_nodes", new Date("05/25/2009"));
         engine.dispatchJob("call_collect_service_sys","system_nodes", new Date("05/26/2009"));
         engine.dispatchJob("call_collect_service_sys","system_nodes", new Date("07/29/2009"));

         engine.dispatchJob("RX_sub_acc","system_nodes", new Date("11/10/2009"));
         engine.dispatchJob("RX_unsub_acc","system_nodes", new Date("11/10/2009"));*/


      /*
         SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy HH");
         Date d = new Date();
         try{
           d = s.parse("10/25/2009 19");
         }catch(Exception e){}

         engine.dispatchHourlyJob("call_collect_service_sys", d); */
            //			engine.dispatchJob("call_collect_service_sys","system_nodes" ,new Date("10/25/2009"));


/*			Date d1 = new Date("07/03/2008");
      engine.dispatchJob("DTMS","system_nodes", d1);
      LogFilesDAO.JobStarted(d1);
      SimpleDateFormat smpl = new SimpleDateFormat("dd/MM/yyyy");
      CollectorScheduler.setHomeDirectory("D:\\VASPortalWF5\\Source Code\\DataCollection");
      LogManager.ParseLogs(CollectorScheduler.getHomeDirectory(), "DTMS", "system_nodes", smpl.format(new Date()));
      MM/dd/yyyy HH:mm
      */

            //DataCollectionManager.dispatchHourlyJob("NEW_MINI_CALL", new Date("1/11/2011 14:00"));

            //DataCollectionManager.dispatchHourlyJob("IVR_SHORT_CODES", new Date("1/11/2011"));
            //DataCollectionManager.dispatchJob("IVR_SHORT_CODES","system_nodes" ,new Date("11/21/2011"));
            //DataCollectionManager.dispatchJob("VF_CDR_RECON_PROCESS", "NODE_1", new Date("07/25/2010"));

            //DataCollectionManager.dispatchJob("BT_CONNECTORS", "NODE_1", new Date("01/10/2012"));

//			DataCollectionManager.dispatchJob("SDP_EXTENDED_STATS", "system_nodes", new Date("01/29/2012"));


            // DataCollectionManager.dispatchJob("NEW_ETOPUP","ETOPUP1",new Date("1/15/2011"));


      /*	DataCollectionManager.dispatchJob("DTMS","system_nodes",
             new Date("07/05/2008"));
         DataCollectionManager.dispatchJob("DTMS","system_nodes",
             new Date("07/07/2008"));*/
      /*engine.dispatchJob("DTMS","system_nodes", new Date("07/05/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/06/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/07/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/08/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/09/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/10/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/11/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/12/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/13/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/14/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/15/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/16/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/17/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/18/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/19/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/20/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/21/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/22/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/23/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/24/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/25/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/26/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/27/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/28/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/29/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/30/2008"));
            engine.dispatchJob("DTMS","system_nodes", new Date("07/31/2008"));

            engine.dispatchJob("DTMS","system_nodes", new Date("05/05/2009"));
      */
//			engine.dispatchJob("BT","system_nodes", new Date("06/04/2009"));

            //this is for platform measures
            //engine.dispatchJob("CCN_New","system_nodes", new Date("06/26/2009"));
            //this is for ccn counters
//			engine.dispatchJob("CCN_New","system_nodes", new Date("06/05/2007"));
            //engine.dispatchJob("CCN_New","system_nodes", new Date("09/09/2009"));

//			engine.dispatchJob("sdp_snapshot", "system_nodes", new Date("08/30/2009"));

            //engine.dispatchJob("VPN_New","system_nodes", new Date("08/16/2009"));
            //engine.dispatchJob("RBT","system",new Date("05/07/2006"));
            //engine.dispatchJob("HLR","HLR A",new Date("06/15/2006"));
            //engine.dispatchJob("VMS","VMS2",new Date("04/08/2006"));
            //engine.dispatchJob("SDP","SDP 1",new Date("03/19/2006"));
            //engine.dispatchJob("IVR_forecast","system",new Date("12/21/2005"));
            //engine.dispatchJob("SDP","system",new Date("05/15/2006"));
            //engine.dispatchJob("MGA", "MGA1", new Date("04/06/2006"));
            //engine.dispatchJob("SMSC", "SMSC1", new Date("05/07/2006"));


      /*
          * Date d1 = new Date("07/03/2008");
          * engine.dispatchJob("DTMS","system_nodes", d1);
          * LogFilesDAO.JobStarted(d1); SimpleDateFormat smpl = new
          * SimpleDateFormat("dd/MM/yyyy");
          * CollectorScheduler.setHomeDirectory
          * ("D:\\VASPortalWF5\\Source Code\\DataCollection");
          * LogManager.ParseLogs(CollectorScheduler.getHomeDirectory(),
          * "DTMS", "system_nodes", smpl.format(new Date()));
          */


            //DataCollectionManager.dispatchJob("NEW_ETOPUP", "ETOPUP1", new Date("1/15/2011"));


      /*
          * DataCollectionManager.dispatchJob("DTMS","system_nodes", new
          * Date("07/05/2008"));
          * DataCollectionManager.dispatchJob("DTMS","system_nodes", new
          * Date("07/07/2008"));
          */
      /*
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/05/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/06/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/07/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/08/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/09/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/10/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/11/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/12/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/13/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/14/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/15/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/16/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/17/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/18/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/19/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/20/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/21/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/22/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/23/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/24/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/25/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/26/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/27/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/28/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/29/2008")); engine.dispatchJob("DTMS","system_nodes",
          * new Date("07/30/2008"));
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("07/31/2008"));
          *
          * engine.dispatchJob("DTMS","system_nodes", new
          * Date("05/05/2009"));
          */
            // engine.dispatchJob("BT","system_nodes", new Date("06/04/2009"));

            // this is for platform measures
            // engine.dispatchJob("CCN_New","system_nodes", new
            // Date("06/26/2009"));
            // this is for ccn counters
            // engine.dispatchJob("CCN_New","system_nodes", new
            // Date("06/05/2007"));
            // engine.dispatchJob("CCN_New","system_nodes", new
            // Date("09/09/2009"));

            // engine.dispatchJob("sdp_snapshot", "system_nodes", new
            // Date("08/30/2009"));

            // engine.dispatchJob("VPN_New","system_nodes", new
            // Date("08/16/2009"));
            // engine.dispatchJob("RBT","system",new Date("05/07/2006"));
            // engine.dispatchJob("HLR","HLR A",new Date("06/15/2006"));
            // engine.dispatchJob("VMS","VMS2",new Date("04/08/2006"));
            // engine.dispatchJob("SDP","SDP 1",new Date("03/19/2006"));
            // engine.dispatchJob("IVR_forecast","system",new
            // Date("12/21/2005"));
            // engine.dispatchJob("SDP","system",new Date("05/15/2006"));
            // engine.dispatchJob("MGA", "MGA1", new Date("04/06/2006"));
            // engine.dispatchJob("SMSC", "SMSC1", new Date("05/07/2006"));

            //engine.dispatchJob("OFFLINE_NO_CDRS","system_nodes",new Date("01/25/2012"));
            //engine.dispatchHourlyJob("RX_ACC_TYPE", new Date("02/07/2012 19:00"));
            //engine.dispatchHourlyJob("RX_ACC_TYPE", new Date("02/07/2012 20:00"));

            //engine.dispatchHourlyJob("EOCN_SMS_DATA", new Date("02/14/2012 13:00"));

            //engine.dispatchHourlyJob("SD_HTTP_REQS", new Date("02/14/2012 21:00"));
            //engine.dispatchHourlyJob("call_collect_service_sys", new Date("02/13/2012 17:00"));

            //engine.dispatchJob("OFF_LINE_HW_STATUS", "system_nodes", new Date("03/04/2012"));
            //engine.dispatchJob("BILLING_RATING", "system_nodes", new Date("03/12/2012"));
            //engine.dispatchJob("SMS_TEKELEC_QUEUES", "system_nodes", new Date("03/23/2012"));

            //engine.dispatchJob("SADM", "system_nodes", new Date("03/23/2012"));

            //engine.dispatchJob("TIBCO_HW", "system_nodes", new Date("04/03/2012"));
            //	engine.dispatchJob("RISK_MGMT", "system_nodes", new Date("06/14/2012"));

            //	DataCollectionManager.dispatchHourlyJob("HOURLY_USSDShortCodes", new Date("7/02/2012 10:00"));

            //	DataCollectionManager.dispatchHourlyJob("RX", new Date("5/17/2012 10:00"));


            //	DataCollectionManager.dispatchJob("EMM_HW", "system_nodes", new Date("09/25/2012"));


            //	DataCollectionManager.dispatchJob("Siebel_TRX", "system_nodes", new Date("12/19/2012"));


            //		DataCollectionManager.dispatchHourlyJob("RISK_MGMT", new Date("01/02/2013 13:00"));

            //DataCollectionManager.dispatchJob("EOCN_BROWSERS", "system_nodes", new Date("10/18/2010"));
            //DataCollectionManager.dispatchJob("GGSN", "system_nodes", new Date("12/03/2012"));


            //DataCollectionManager.dispatchJob("IRS_AIR_Latency", "system_nodes", new Date("12/04/2011"));

//			persistenceManager.executeTest();

            //	DataCollectionManager.dispatchHourlyJob("RISK_MGMT", new Date("01/15/2013 11:00"));


      /*DataCollectionManager.dispatchJob("RISK_MGMT", "system_nodes", new Date("1/5/2013"));
         DataCollectionManager.dispatchJob("RISK_MGMT", "system_nodes", new Date("1/6/2013"));
         DataCollectionManager.dispatchJob("RISK_MGMT", "system_nodes", new Date("1/7/2013"));
         DataCollectionManager.dispatchJob("RISK_MGMT", "system_nodes", new Date("1/8/2013"));
         DataCollectionManager.dispatchJob("CMS_ADAPTOR", "system_nodes", new Date("12/3/2012"));*/


        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (InputException e) {
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/ catch (Exception e) {
            e.printStackTrace();
        }

    }

}