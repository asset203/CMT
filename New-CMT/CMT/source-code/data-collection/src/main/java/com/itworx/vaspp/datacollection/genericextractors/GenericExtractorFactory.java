package com.itworx.vaspp.datacollection.genericextractors;


import eg.com.vodafone.model.GenericInputStructure;

/**
 * @author Basem.Deiaa
 * factory class to generate generic extractor according to extraction method
 */
public class GenericExtractorFactory {
    /**
     * method to generate generic extractor according to extraction method
     * if database return Generic Database Extractor and
     * if others return Generic Files Extractor
     * 
     * @param inputStructure
     * 
     * @return Generic Extractor object 
     */
	public static GenericExtractor getGenericExtractor(GenericInputStructure inputStructure) {
    	GenericExtractor genericExtractor;
    	int type = inputStructure.getExtractionMethod();
		if (type == GenericInputStructure.DATABASE_EXTRACTOR) {
			genericExtractor = new GenericDatabaseExtractor();
		} else {
			genericExtractor = new GenericFilesExtractor();
		}
		return genericExtractor;
    }
}
