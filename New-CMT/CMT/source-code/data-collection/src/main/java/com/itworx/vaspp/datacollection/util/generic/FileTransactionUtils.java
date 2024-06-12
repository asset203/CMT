package com.itworx.vaspp.datacollection.util.generic;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.file.ResourceManagerSystemException;
import org.apache.commons.transaction.util.Log4jLogger;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;

public class FileTransactionUtils {

	private static Logger logger;
	private static Log4jLogger loggerWrapper;
	
	public static void init(Logger currentLogger) {
		logger = currentLogger;
		loggerWrapper = new Log4jLogger(logger);
	}
	
	public static FileResourceManager getFileResourceManager(String transStorePath,String transWorkingPath){
		return new FileResourceManager(transStorePath, transWorkingPath , false, loggerWrapper);
	}
	public static String beginTransaction(FileResourceManager fileResourceManager) throws ApplicationException{
		String txId = "";
		try {
			fileResourceManager.start();
			txId = fileResourceManager.generatedUniqueTxId();
			fileResourceManager.startTransaction(txId);
		} catch (ResourceManagerSystemException e) {
			logger.error("FileTransactionUtils.beginTransaction() - error while starting FileResourceManeger :" + e);
			throw new ApplicationException("error while starting FileResourceManeger :" + e);
		} catch (ResourceManagerException e) {
			logger.error("FileTransactionUtils.beginTransaction() - error while start file transaction :" + e);
			throw new ApplicationException("error while start file transaction :" + e);
		}
		return txId;
	}

	public static InputStream getResourceInputStream(FileResourceManager fileResourceManager,String transactionId,String resourcePath) throws ApplicationException{
		InputStream inpStrm;
		try {
			inpStrm = fileResourceManager.readResource(transactionId,resourcePath);
		} catch (ResourceManagerException e) {
			logger.error("FileTransactionUtils.getResourceInputStream() - error while reading file ["+resourcePath+"] :" + e);
			throw new ApplicationException("error while reading file ["+resourcePath+"] :" + e);
		}
		return inpStrm;
	}
	
	public static OutputStream getResourceOutputStream(FileResourceManager fileResourceManager,String transactionId,String resourcePath) throws ApplicationException {
		OutputStream outStrm;
		try {
			outStrm = fileResourceManager.writeResource(transactionId,resourcePath);
		} catch (ResourceManagerException e) {
			logger.error("FileTransactionUtils.getResourceInputStream() - error while opening for writing file ["+resourcePath+"] :" + e);
			throw new ApplicationException("error while opening for writing file ["+resourcePath+"] :" + e);
		}
		return outStrm;
	}
	public static void commitTransaction(FileResourceManager fileResourceManager,String transactionId) throws ApplicationException{
		try {
			fileResourceManager.prepareTransaction(transactionId);
			fileResourceManager.commitTransaction(transactionId);
		} catch (ResourceManagerException e) {
			logger.error("FileTransactionUtils.commitTransaction() - error while commit file transaction :" + e);
			throw new ApplicationException("error while commit file transaction :" + e);
		}
	}
	
	public static void rollbackTransaction(FileResourceManager fileResourceManager,String transactionId) throws ApplicationException {
		try {
			fileResourceManager.rollbackTransaction(transactionId);
		} catch (ResourceManagerException e) {
			logger.error("FileTransactionUtils.rollbackTransaction() - error while rollback file transaction :" + e);
			throw new ApplicationException("error while rollback file transaction :" + e);
		}
	}


	
}
