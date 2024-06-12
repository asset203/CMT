/* 
 * File:       InputDAOFactory.java
 * Date        Author          Changes
 * 16/01/2006  Nayera Mohamed  Created
 * 
 * Class Responsible for returning the appropriate input DAO for the type of input
 */
package com.itworx.vaspp.datacollection.dao;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.GenericPersistenceManager;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import eg.com.vodafone.model.enums.InputStructureType;
import org.apache.log4j.Logger;

public class InputDAOFactory {
    private static final ThreadLocal<GenericPersistenceManager> genericPersistenceManagerGenerator =
            new ThreadLocal<GenericPersistenceManager>(){
                @Override
                protected GenericPersistenceManager initialValue()
                {
                    try {
                        PersistenceManager persistenceManager = new PersistenceManager();
                        return new  GenericPersistenceManager(persistenceManager);
                    } catch (ApplicationException e) {
                        Logger logger = Logger.getLogger("InputDAOFactory");
                        logger.error("InputDAOFactory.genericPersistenceManagerGenerator.initialValue Failed",e);
                    }
                    return null;
                }
            };

	public InputDAOFactory() {
	}

	public static InputDAO getInputDAO(int type, PersistenceManager persistenceManager) throws ApplicationException {
		InputDAO inputDAO;
        if(type ==InputStructureType.DB.getTypeCode()){
			inputDAO = new DBInputDAO(persistenceManager);
		} else if (type == InputStructureType.EXCEL.getTypeCode()) {
			inputDAO = new ExcelInputDAO(persistenceManager);
		} else if (type == InputStructureType.TEXT.getTypeCode()){
			inputDAO = new TextInputDAO(persistenceManager);
		} else if (type == InputStructureType.DIRECT_DB.getTypeCode()
                || type == InputStructureType.GENERIC_DB.getTypeCode()){
			inputDAO = new DirectMappingDBInputDAO(persistenceManager);
		} else if (type == InputStructureType.DIRECT_TEXT.getTypeCode()){
			inputDAO = new DirectMappingTextInputDAO(persistenceManager);
        }else if(type == InputStructureType.GENERIC_TEXT.getTypeCode()){
            inputDAO = new GenericTextInputDao(getGeneericPersistanceManagerNewInstance());
		}else if(type == InputStructureType.GENERIC_XML.getTypeCode()){
            inputDAO = new XmlInputDao(getGeneericPersistanceManagerNewInstance());
        }else {
			inputDAO = new GenericInputDAO(getGeneericPersistanceManagerNewInstance());
		}
		return inputDAO;
	}

    private static GenericPersistenceManager getGeneericPersistanceManagerNewInstance() throws ApplicationException {
        GenericPersistenceManager persistenceManager= genericPersistenceManagerGenerator.get();
        if(persistenceManager==null){
            throw new ApplicationException("GenericInputDAO:GenericPersistenceManager = null ");
        }
        return persistenceManager;
    }
}