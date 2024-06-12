package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.dao.CMTDBConnectionPool;
import eg.com.vodafone.dao.DataCollectionDao;
import eg.com.vodafone.dao.SystemDao;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.DataCollectionService;
import eg.com.vodafone.service.impl.DataCollectionSystemService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/31/13
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseConfigReader {

    private static SystemDao systemDao;
    private static DataCollectionDao dataCollectionDao;
    private static DataCollectionServiceInterface dataCollectionService;
    private static DataCollectionSystemServiceInterface systemService;


    /**
     * initiating ConfigReader Loading database properties
     *
     * @param logger -
     *               the name of the logger for the targeted system
     * @throws com.itworx.vaspp.datacollection.common.ApplicationException
     *          if an error occured while reading data source properties file
     */
    public static void init(Logger logger) throws ApplicationException {
        logger.debug("initiating DatabaseConfigReader: reading system data from DB");
        if (systemDao == null) {
            systemDao = new SystemDao(CMTDBConnectionPool.getDataSource());
        }
        if (dataCollectionDao == null) {
            dataCollectionDao = new DataCollectionDao(CMTDBConnectionPool.getDataSource());
        }
        if (systemService == null) {
            systemService = new DataCollectionSystemService();
            systemService.setSystemDao(systemDao);
        }

        if (dataCollectionService == null) {
            dataCollectionService = new DataCollectionService();
            dataCollectionService.setDataCollectionDao(dataCollectionDao);
        }
    }

    public static VNode getNode(String systemName, String nodeName, Logger logger) throws ApplicationException {
        init(logger);
        logger.debug("DatabaseConfigReader.getNode() - start  getNode(" + systemName + "," + nodeName + ")");
        if (StringUtils.isBlank(systemName) || StringUtils.isBlank(nodeName)) {
            logger.error(" Invalid parameters,DatabaseConfigReader.getNode() - finish");
            throw new ApplicationException("Invalid parameters,DatabaseConfigReader.getNode()");
        }
        try {
            VNode node = systemService.getNode(systemName, nodeName);
            if (node == null) {
                logger.error("DCF - 2001: DatabaseConfigReader.getNode() - Node not found for name:" + systemName);
                throw new ApplicationException("Node not found for name:" + systemName);
            }
            for (int j = 0; j < node.getInputsList().size(); j++) {
                node.getInputsList().get(j).setInputStructuresList
                        (getInputStructures(node.getInputsList().get(j).getInputStructureIds(), logger));
            }
            logger.debug("DatabaseConfigReader.getNode() - finish getNode(" + systemName + "," + nodeName + ")");
            return node;
        } catch (Exception e) {
            logger.error(" DatabaseConfigReader.getNode() - failed", e);
            throw new ApplicationException("DatabaseConfigReader.getNode() - failed" + e);
        }
    }

    public static VSystem getSystem(String systemName, Logger logger) throws ApplicationException {
        init(logger);
        logger.debug("DatabaseConfigReader.getSystem() - start  getSystem(" + systemName + ")");
        if (StringUtils.isBlank(systemName)) {
            logger.error(" Invalid parameters,DatabaseConfigReader.getSystem() - finish");
            throw new ApplicationException("Invalid parameters,DatabaseConfigReader.getSystem()");
        }
        try {
            VSystem system = systemService.getSystem(systemName, true);   //get 'active' nodes only

            if (!system.getNodesList().isEmpty()) {
                for (int i = 0; i < system.getNodesList().size(); i++) {
                    for (int j = 0; j < system.getNodesList().get(i).getInputsList().size(); j++) {
                        system.getNodesList().get(i).getInputsList().get(j).setInputStructuresList
                                (getInputStructures(system.getNodesList().get(i).getInputsList().get(j).getInputStructureIds(), logger));
                    }
                }
            }
            if (!system.getInputsList().isEmpty()) {
                for (int i = 0; i < system.getInputsList().size(); i++) {
                    system.getInputsList().get(i).setInputStructuresList
                            (getInputStructures(system.getInputsList().get(i).getInputStructureIds(), logger));
                }
            }
            logger.debug("DatabaseConfigReader.getSystem() - finish  getSystem(" + systemName + ")");
            return system;
        } catch (Exception e) {
            logger.error(" DatabaseConfigReader.getSystem() - failed", e);
            throw new ApplicationException("DatabaseConfigReader.getSystem() - failed" + e);
        }
    }

    private static List<VInputStructure> getInputStructures(List<String> ids, Logger logger) {
        List<VInputStructure> inputStructuresList = new ArrayList<VInputStructure>();
        if (ids == null || ids.isEmpty()) {
            logger.error("DatabaseConfigReader.getInputStructures(), empty ids list - no data collections loaded !");
            return inputStructuresList;
        }
        for (int i = 0; i < ids.size(); i++) {
            VInputStructure inputStructure = dataCollectionService.getDataCollection(ids.get(i));

            if (inputStructure != null) {
                try{
                if(logger.isDebugEnabled()){
                    logger.debug("---DatabaseConfigReader.getInputStructures : inputStructure id =" +inputStructure.getId()+", Sql:: ");
                    logger.debug(inputStructure.getExtractionSql());
                }
                }catch (Exception e)
                {
                    logger.error("error occurred while logging",e);
                }
                inputStructuresList.add(inputStructure);
            }
            else
            {
                if(logger.isDebugEnabled()){
                    logger.debug("---DatabaseConfigReader.getInputStructures : inputStructure id =" +ids.get(i)+" was not found! ");
                }
            }
        }
        return inputStructuresList;
    }
}
