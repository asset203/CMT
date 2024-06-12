package com.itworx.vaspp.datacollection.genericextractors.transformers;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import eg.com.vodafone.model.GenericInputStructure;
import org.apache.log4j.Logger;

import java.io.File;

public class JavaCodeTransformer extends InputFilesTransformer{

    public void trasformFiles(File[] inputFiles ,GenericInputStructure inputStructure,Logger logger) throws ApplicationException{
    	logger
		.debug("JavaCodeTransformer.trasformFiles() - started transforming input files ");
    	for (int i = 0; i < inputFiles.length; i++) {
    		
    	}
    }
}
