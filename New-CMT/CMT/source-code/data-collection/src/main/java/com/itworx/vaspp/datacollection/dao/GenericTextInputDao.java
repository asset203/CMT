package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.genericextractors.transformers.FileTransformerFactory;
import com.itworx.vaspp.datacollection.genericextractors.transformers.InputFilesTransformer;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.FileHandler;
import com.itworx.vaspp.datacollection.util.GenericPersistenceManager;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/10/13
 * Time   : 1:08 PM
 */
public class GenericTextInputDao implements InputDAO {

    protected Logger logger;

    protected GenericPersistenceManager persistenceManager;

    public GenericTextInputDao(GenericPersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }
    public void retrieveData(VInput input, Date targetDate) throws InputException, ApplicationException {
        retrieveData(input,targetDate,false);
    }
    public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
        retrieveData(input,targetDate,true);
    }

    protected void retrieveData(VInput input, Date targetDate,boolean isHourly) throws InputException, ApplicationException {
        //1- preparations
        logger = Logger.getLogger(input.getSystemName());        

        logger.debug("GenericInputDAO.retrieveData() - started retrieveData for input id:"
                + input.getId() + " targetDate:" + targetDate);


        //------------------------------------------
        InputData inputData = new InputData();
        String inputID = input.getId();
        inputData.setInputID(inputID);
        inputData.setNodeName(input.getNodeName());
        inputData.setSystemName(input.getSystemName());
        inputData.setDate(targetDate);
        inputData.setDateFormatInFileNamePattern
                (Utils.getDateFormatFromInputFileName(input.getOriginalInputName()));
        //----------------------------------
        File[] inputFiles = new FileHandler().getFiles(input);
        VInputStructure[] inputStructures = input.getInputStructures();
        InputFilesTransformer filesTransformer =null;
          try {
                for (int i = 0; i < inputStructures.length; i++) {
                        logger.info("GenericTextInputDao.retrieveData() - started retrieveData for input structure :"+inputStructures[i].getId()+" and inputId:" + input.getId() + " targetDate:" + targetDate);
                        //------------------------------------------
                         inputData.setInputStructureId(inputStructures[i].getId());
                        //----------------------------------
                        filesTransformer = FileTransformerFactory.getFilesTransformer(inputStructures[i]);
                        filesTransformer.setBatchSize(persistenceManager.getBatchSize());
                        filesTransformer.LoadFilesToTempTable(inputFiles,  inputStructures[i], logger);
                        persistenceManager.performInsertSelect(inputData,inputStructures[i],isHourly);
                        logger.info("GenericTextInputDao.retrieveData() - finished retrieveData for input structure :"+inputStructures[i].getId()+" and inputId:" + input.getId() + " targetDate:" + targetDate);
                }
        }
        catch(Exception e)
        {
            logger.error(e);
            logger.error("-"+ input.getNodeName() +"- GNR-1002:GenericTextInputDao.retrieveData() - error while extraction process : "
                                  , e);
            throw new InputException( e);
        }
        finally
        {
          filesTransformer.clean();
          persistenceManager.clear();
        }
        logger.debug("GenericInputDAO.retrieveData() - finish retrieveData for input id:"
                + input.getId() + " targetDate:" + targetDate);

    }
}
