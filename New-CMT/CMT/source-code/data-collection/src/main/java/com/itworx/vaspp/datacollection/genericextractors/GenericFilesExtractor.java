package com.itworx.vaspp.datacollection.genericextractors;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.genericextractors.transformers.FileTransformerFactory;
import com.itworx.vaspp.datacollection.genericextractors.transformers.InputFilesTransformer;
import com.itworx.vaspp.datacollection.util.FileHandler;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInput;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.ResultSet;
import java.util.Date;

public class GenericFilesExtractor implements GenericExtractor {

	private VInput input;
	private Logger logger;
	private TempDBTableOperator tmpDBTableOperator;
	private boolean tableCreated=false;

	public void init(VInput input) throws ApplicationException, InputException {
		this.input = input;
		logger = Logger.getLogger(input.getSystemName());
		tmpDBTableOperator = new TempDBTableOperator(logger,input);
	}

	public ResultSet performExtractionProcess(GenericInputStructure currentInputStructure, Date targetDate)	throws ApplicationException, InputException{
		logger.info("GenericFilesExtractor.performExtractionProcess() - "
                +"start performing extraction for input structure ["+currentInputStructure.getId()+"] in date ["+targetDate+"]");
        InputFilesTransformer inputFilesTransformer = FileTransformerFactory.getFilesTransformer(currentInputStructure);

		File[] originalInputFiles = new FileHandler().getFiles(input);
		
		tmpDBTableOperator.createTempDatabaseTable(currentInputStructure.getExtractionFields());
        inputFilesTransformer.setTmpDatabaseTableOperator(tmpDBTableOperator);

		tableCreated = true;

        inputFilesTransformer.trasformFiles(originalInputFiles,currentInputStructure, logger);
        logger.debug("GenericFilesExtractor.performExtractionProcess() - CALLING [ tmpDBTableOperator.getResultSet ]");
        ResultSet rset = tmpDBTableOperator.getResultSet(currentInputStructure
				.getExtractionSQL());
		
		logger.info("GenericFilesExtractor.performExtractionProcess() -"
                +"finishing performing extraction for input structure ["+currentInputStructure.getId()+"] in date ["+targetDate+"]");
		return rset;
	}



	public void clean() throws ApplicationException{
		if (tableCreated == true){
			tmpDBTableOperator.clean();
			tableCreated=false;
		}
	}
}
