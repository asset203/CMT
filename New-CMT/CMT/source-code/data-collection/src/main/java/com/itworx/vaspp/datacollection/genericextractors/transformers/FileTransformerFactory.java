package com.itworx.vaspp.datacollection.genericextractors.transformers;

import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.InputStructureType;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/14/13
 * Time   : 7:59 AM
 */
public class FileTransformerFactory {

    public static InputFilesTransformer getFilesTransformer(VInputStructure inputStructure) {
        InputFilesTransformer inputFilesTransformer = null;
        int inputStructureType = inputStructure.getType();
        if(InputStructureType.GENERIC_INPUT.getTypeCode() == inputStructureType){
                int extractMethodType = ((GenericInputStructure)inputStructure).getExtractionMethod();

                if (extractMethodType == GenericInputStructure.DELIMITER_EXTRACTOR) {
                    inputFilesTransformer = new DelimiterTransformer();
                } else if (extractMethodType == GenericInputStructure.REGULAR_EXP_EXTRACTOR) {
                    inputFilesTransformer = new RegularExpressionTransformer();
                } else if (extractMethodType == GenericInputStructure.EXCEL_EXTRACTOR) {
                    inputFilesTransformer = new ExcelTransformer();
                } else if (extractMethodType == GenericInputStructure.JAVA_CODE_EXTRACTOR) {
                    inputFilesTransformer = new JavaCodeTransformer();
                }
                return inputFilesTransformer;
        }else if(InputStructureType.GENERIC_TEXT.getTypeCode() == inputStructureType ){
            inputFilesTransformer = new DelimitedTextFileTransformer();
        }  else if(InputStructureType.GENERIC_XML.getTypeCode() == inputStructureType){
            inputFilesTransformer = new XmlFileTransformer();
        }
        return inputFilesTransformer;
    }
}
