package com.itworx.vaspp.datacollection.genericextractors.transformers;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.genericextractors.TempDBTableOperator;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInputStructure;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.SQLException;

public abstract class InputFilesTransformer {

	protected TempDBTableOperator tmpDbTableOperator;
    protected int batchSize;
    protected long notValidData = 0;
    protected boolean  dataFound = false;

	public abstract void trasformFiles(File[] inputFiles,
			GenericInputStructure inputStructure, Logger logger) throws ApplicationException;

	public void setTmpDatabaseTableOperator(TempDBTableOperator tmpDbTableOperator) {
		this.tmpDbTableOperator = tmpDbTableOperator;
	}

	public TempDBTableOperator getTmpDatabaseOperator() {
		return tmpDbTableOperator;
	}
    public void setBatchSize(int size){
      this.batchSize = size;
    }
    public  void insertFileDataToTempTable(File aFiles, VInputStructure inputStructure, Logger logger) throws ApplicationException{}
    public  void LoadFilesToTempTable(File[] inputFiles, VInputStructure inputStructure, Logger logger) throws  ApplicationException {
        logger.debug("DelimiterTransformer.transformFiles() - started transforming input files ");
        try {
            tmpDbTableOperator =  new TempDBTableOperator(logger);
            tmpDbTableOperator.setBatchSize(this.batchSize);
            tmpDbTableOperator.createTempTable(inputStructure);

            for (int i = 0; i < inputFiles.length; i++) {
                tmpDbTableOperator.beginTransaction();
                logger.debug("DelimiterTransformer.transformFiles() - Done started transaction :: tmpDbTableOperator.beginTransaction()");
                tmpDbTableOperator.prepareInsertStatementUsingDataColumns();
                logger.debug("DelimiterTransformer.transformFiles() - Done Prepare insert statement :: tmpDbTableOperator.prepareInsertStatementUsingDataColumn()s");
                insertFileDataToTempTable(inputFiles[i], inputStructure, logger);
                logger.debug("DelimiterTransformer.transformFiles() - Done insert data :: DelimiterTransformer.insertFileDataToTempTable()");
                tmpDbTableOperator.commitTransaction();
                logger.debug("DelimiterTransformer.transformFiles() - Done Commit transaction :: tmpDbTableOperator.commitTransaction()");
                logger.debug("DelimiterTransformer.transformFiles() - "  + inputFiles[i].getName() + " transformed");
            }
            if(notValidData > 0){
                logger.warn("DelimiterTransformer.transformFiles() - failed to insert "+notValidData+" lines in file into temp database : ");
            }
            if( !dataFound){
                ApplicationException e = new ApplicationException("No data found in files");
                logger.error("DelimiterTransformer.transformFiles() - error ["+e+"]");
                throw e;
            }
            logger.debug("DelimiterTransformer.transformFiles() - finished transforming input files successfully ");
        }
        catch (SQLException e) {
            logger.error("DelimiterTransformer.transformFiles() - Couldn't insert data in table"
                    , e);
            throw new ApplicationException(e);
        } catch (Exception e) {
            logger.error("DelimiterTransformer.transformFiles() - Couldn't insert data in table"
                    , e);
            throw new ApplicationException("DelimiterTransformer.transformFiles(): Error occurred while loading file data to temp table" + e);
        }
    }
    public void clean() throws ApplicationException{
        tmpDbTableOperator.dropTempTableAndCloseConnection();
    }
}
