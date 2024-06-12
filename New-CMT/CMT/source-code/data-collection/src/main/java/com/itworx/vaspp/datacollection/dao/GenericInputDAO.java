package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.genericextractors.GenericExtractor;
import com.itworx.vaspp.datacollection.genericextractors.GenericExtractorFactory;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.GenericPersistenceManager;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Date;

public class GenericInputDAO implements InputDAO  {
	
	private Logger logger;
	
	private GenericPersistenceManager persistenceManager;
	
	private GenericExtractor genericExtractor;
	
	public GenericInputDAO(GenericPersistenceManager persistenceManager){
		this.persistenceManager = persistenceManager;
	}
	
	public void retrieveData(VInput input, Date targetDate) throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger
				.debug("GenericInputDAO.retrieveData() - started retrieveData for input id:"
						+ input.getId() + " targetDate:" + targetDate);
		VInputStructure[] inputStructures = input.getInputStructures();
		InputData inputData = new InputData();
		inputData.setInputID(input.getId());
		inputData.setNodeName(input.getNodeName());
		inputData.setSystemName(input.getSystemName());
		inputData.setDate(targetDate);
		ResultSet extractionResultSet = null;
		try
		{
			for (int i = 0; i < inputStructures.length; i++) {
				GenericInputStructure currentInputStructure = (GenericInputStructure)inputStructures[i];
				genericExtractor = GenericExtractorFactory.getGenericExtractor(currentInputStructure);
				genericExtractor.init(input);
				extractionResultSet = genericExtractor.performExtractionProcess(currentInputStructure,targetDate);
				persistenceManager.persistResultSet(inputData,extractionResultSet, currentInputStructure.getOutputMapping(),false);
				extractionResultSet.close();
				genericExtractor.clean();
			}
		}
		catch (ApplicationException e) {
			logger
					.error("-"+ input.getNodeName() +"- GNR-1001:GenericInputDAO.retrieveData() - error while extraction process : "
							, e);
			throw new InputException( e);
		}
		catch(Exception e)
		{
			logger
			.error("-"+ input.getNodeName() +"- GNR-1002:GenericInputDAO.retrieveData() - error while extraction process : "
					, e);
			throw new InputException(e);
		}
		finally
	    {
               genericExtractor.clean();
               persistenceManager.clear();
	    }
		logger.debug("GenericInputDAO.retrieveData() - End retrieveData for input id:"
				+ input.getId() + " targetDate:" + targetDate);
	}
	
	public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger
				.debug("GenericInputDAO.retrieveHourlyData() - started retrieveData for inputid:"
						+ input.getId() + " targetDate:" + targetDate);
		VInputStructure[] inputStructures = input.getInputStructures();
		InputData inputData = new InputData();
		inputData.setInputID(input.getId());
		inputData.setNodeName(input.getNodeName());
		inputData.setSystemName(input.getSystemName());
		inputData.setDate(targetDate);
		ResultSet extractionResultSet = null;
		try
		{
			for (int i = 0; i < inputStructures.length; i++) {
				logger.info("GenericInputDAO.retrieveHourlyData() - started retrieveData for input structure :"+inputStructures[i].getId()+" and inputid:" + input.getId() + " targetDate:" + targetDate);
				GenericInputStructure currentInputStructure = (GenericInputStructure)inputStructures[i];
				genericExtractor = GenericExtractorFactory.getGenericExtractor(currentInputStructure);
				genericExtractor.init(input);
				extractionResultSet = genericExtractor.performExtractionProcess(currentInputStructure,targetDate);
				persistenceManager.persistResultSet(inputData,extractionResultSet, currentInputStructure.getOutputMapping(),true);
				extractionResultSet.close();
				genericExtractor.clean();
				logger.info("GenericInputDAO.retrieveHourlyData() - finished retrieveData for input structure :"+inputStructures[i].getId()+" and inputid:" + input.getId() + " targetDate:" + targetDate);
			}
		}
		catch (ApplicationException e) {
			logger
					.error("-"+ input.getNodeName() +"- GNR-1001:GenericInputDAO.retrieveHourlyData() - error while extraction process : "
							, e);
			throw new InputException("" + e);
		}
		catch(Exception e)
		{
			logger
			.error("-"+ input.getNodeName() +"- GNR-1002:GenericInputDAO.retrieveHourlyData() - error while extraction process : "
					, e);
			throw new InputException("" + e);
		}
        finally
        {    try {
            if(extractionResultSet!=null){
                extractionResultSet.close();
            }
        }catch (Exception e) {
            logger.error("Failed to close result set ",e);
        }finally {
            genericExtractor.clean();
            persistenceManager.clear();
        }
        }
		logger.debug("GenericInputDAO.retrieveHourlyData() - End retrieveData for inputid:"
				+ input.getId() + " targetDate:" + targetDate);
	}

}
