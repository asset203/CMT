package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.dynacode.DynaCode;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.DynaCodeUtils;
import com.itworx.vaspp.datacollection.util.FileHandler;
import com.itworx.vaspp.datacollection.util.GenericPersistenceManager;
import com.itworx.vaspp.datacollection.util.converters.TextConverter;
import eg.com.vodafone.model.GenericXmlInputStructure;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.XmlDataCollectionConverters;

import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 5/7/13
 * Time   : 11:35 AM
 */
public class XmlInputDao extends GenericTextInputDao {
    private static DynaCode dynaCode = new DynaCode();

    public XmlInputDao(GenericPersistenceManager genericPersistenceManager) {
        super(genericPersistenceManager);
    }

    public void retrieveData(VInput input, Date targetDate) throws InputException, ApplicationException {
        retrieveData(input,targetDate,false);
    }
    public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
        retrieveData(input,targetDate,true);
    }

    protected void retrieveData(VInput input, Date targetDate,boolean isHourly) throws InputException, ApplicationException {
        //logger = DataCollectionManager.getSystemLogger(input.getSystemName());
        logger = DataCollectionManager.getSystemLogger(input.getSystemName());
        logger.debug("XmlInputDao.retrieveData() -  started  for input id:" + input.getId() +" targetDate:" + targetDate);
        VInputStructure[] inputStructures = input.getInputStructures();
        if(((GenericXmlInputStructure)inputStructures[0]).isSimple()){
            logger.debug("XmlInputDao.retrieveData()  - xml type = simple - calling parent method GenericTextInputDao.retrieveData()");
             super.retrieveData(input,targetDate,isHourly);
        }
        else
        {
             logger.debug("XmlInputDao.retrieveData()  - xml type = vendor based - calling executeVendorConverter()");
               executeVendorConverter(input,targetDate,isHourly);
        }
        logger.debug("XmlInputDao.retrieveData() -  finish  for input id:" + input.getId() +" targetDate:" + targetDate);
    }
    protected void executeVendorConverter(VInput input, Date targetDate,boolean isHourly) throws InputException, ApplicationException{

        logger.debug("XmlInputDao.executeVendorConverter() - started  for input id:" + input.getId() + " targetDate:" + targetDate);
        VInputStructure[] inputStructures = input.getInputStructures();
        try {
                File[] originalInputFiles = new FileHandler().getFiles(input);
                InputData inputData = new InputData();
                inputData.setInputID(input.getId());
                inputData.setNodeName(input.getNodeName());
                inputData.setSystemName(input.getSystemName());
                inputData.setDate(targetDate);

                for (int z = 0; z < inputStructures.length; z++) {
                        GenericXmlInputStructure inputStructure = (GenericXmlInputStructure) inputStructures[z];

                        inputData.setInputStructureId(inputStructure.getId());
                        inputData.setHeader(inputStructure.getColumns());

                         //1- get converter class name
                        String converterName = XmlDataCollectionConverters.getConverterClass(inputStructure.getConverterId());
                        logger.debug("XmlInputDao.executeVendorConverter() - started DynaCodeUtils.getDynaTextConverter,converter name ["+converterName+"]");
                        TextConverter converter = DynaCodeUtils.getDynaTextConverter(dynaCode, converterName);
                        logger.debug("XmlInputDao.executeVendorConverter() - finish DynaCodeUtils.getDynaTextConverter,converter name ["+converterName+"]");
                         //2- execute converter
                        logger.debug("XmlInputDao.executeVendorConverter() - started  converter.convert");
                        File[] outputFiles = converter.convert(originalInputFiles, input.getSystemName());
                        logger.debug("XmlInputDao.executeVendorConverter() - finish  converter.convert");
                         //3- persist converter output
                        logger.debug("XmlInputDao.executeVendorConverter() - started  persistenceManager.persistConverterResults");
                        persistenceManager.persistConverterResults(outputFiles,inputStructure,inputData,logger,isHourly);
                    logger.debug("XmlInputDao.executeVendorConverter() - finish  persistenceManager.persistConverterResults");
                }
        }catch (Exception e) {
            logger.error("-"+ input.getId() + "XmlInputDao.executeVendorConverter() - Error getting data of xml input", e);
            throw new InputException("-" + input.getId() + "XmlInputDao.executeVendorConverter() - Error getting data of xml input - " + e);
        }
        finally {
            persistenceManager.clear();
        }
        logger.debug("XmlInputDao.executeVendorConverter() - finished  for input id:" + input.getId() + " targetDate:" + targetDate);
    }
}
