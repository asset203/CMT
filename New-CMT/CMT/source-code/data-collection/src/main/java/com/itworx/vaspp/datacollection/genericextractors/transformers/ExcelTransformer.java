package com.itworx.vaspp.datacollection.genericextractors.transformers;

import eg.com.vodafone.model.GenericInputStructure;
import org.apache.log4j.Logger;

import java.io.File;

public class ExcelTransformer extends InputFilesTransformer{

    public void trasformFiles(File[] inputFiles ,GenericInputStructure inputStructure,Logger logger) {
    	logger
		.debug("ExcelTransformer.trasformFiles() - started transforming input files ");
    	for (int i = 0; i < inputFiles.length; i++) {
    		
    	}
    }
}
