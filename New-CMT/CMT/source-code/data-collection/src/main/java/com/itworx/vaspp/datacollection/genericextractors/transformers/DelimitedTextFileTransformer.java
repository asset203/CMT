package com.itworx.vaspp.datacollection.genericextractors.transformers;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.GenericTextInputStructure;
import eg.com.vodafone.model.VInputStructure;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/9/13
 * Time   : 10:20 AM
 */
public class DelimitedTextFileTransformer extends InputFilesTransformer {

    private String[] getDataToBuildRow(BufferedReader inputStream,GenericTextInputStructure inputStructure,
                                       int numberOfPlainColumns, Logger logger ) throws IOException {
        logger.debug("DelimitedTextFileTransformer.getDataToBuildRow() started");
        String[] rawData = null;
        String[] dataToBuildRow = new String[numberOfPlainColumns];
        String line = inputStream.readLine();
        logger.debug(" Line = "+line);
        if(StringUtils.hasText(line)){
            rawData = line.split(inputStructure.getDelimiter());

            if(rawData!=null ){    //&& rawData.length >= numberOfPlainColumns

                //* step:5
                for(int j = 0 ; j < inputStructure.getColumns().length ; j++){
                    DataColumn field = inputStructure.getColumns()[j];
                    if( StringUtils.hasText(field.getSqlExpression())){
                        continue;
                    }
                    int fieldIndex = field.getIndex();
                    if(fieldIndex>=rawData.length){
                        dataToBuildRow[fieldIndex] = null;   //consider missing values to be null
                    }
                    else
                    {
                        dataToBuildRow[fieldIndex] =rawData[fieldIndex].trim();
                        if(!StringUtils.hasText(rawData[fieldIndex])){
                            dataToBuildRow[fieldIndex] = (field.getDefaultValue() != null) ? field.getDefaultValue() : null;
                            //we could just use "DEFAULT" keyword and oracle will insert defaults
                            //rawData[fieldIndex] = "DEFAULT" ;
                        }
                    }
                }
            }

        }
        logger.debug("DelimitedTextFileTransformer.getDataToBuildRow() finish");
        return dataToBuildRow;
    }
    public void insertFileDataToTempTable(File aFiles, VInputStructure inputStructure, Logger logger) throws ApplicationException {
        logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable() started");
        BufferedReader  inputStream=null;
        GenericTextInputStructure txtInputStructure = (GenericTextInputStructure) inputStructure;
        int numberOfPlainColumns ;
        int numberOfSqlColumns=0;
        for(DataColumn col:inputStructure.getColumnsList()){
            if(StringUtils.hasText(col.getSqlExpression())){
                numberOfSqlColumns++;
            }
        }
        numberOfPlainColumns = inputStructure.getColumns().length-numberOfSqlColumns;
       try{
                inputStream = Utils.getGZIPAwareBufferdReader(aFiles);
                String[] rawData = null;
                int ignoredLines = txtInputStructure.getIgnoredLinesCount();
                if(ignoredLines > 0){
                    logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable() skipping (ignore) "+ignoredLines+" lines");
                    Utils.skip(ignoredLines,inputStream);
                }

                if(inputStream.ready() && txtInputStructure.isUseHeaders())
                {
                    inputStream.readLine();

                }
                while (inputStream.ready()) {
                    rawData= getDataToBuildRow(inputStream,txtInputStructure,numberOfPlainColumns,logger);
                    if(rawData!=null){
                        try{
                            logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable():valid line");
                            logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable(): start  tmpDbTableOperator.substiuteDataInInsertQuery()");
                            tmpDbTableOperator.substiuteDataInInsertQuery(rawData);
                            dataFound=true;
                            logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable(): done  tmpDbTableOperator.substiuteDataInInsertQuery()");
                        }
                        catch(Exception e){
                            logger.error(e);
                            notValidData++;
                        }
                    }else
                    {
                        logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable():NOT valid line");
                        notValidData++;
                    }
                }
                tmpDbTableOperator.executeBatch();
       }catch (FileNotFoundException e) {
           logger.error("DelimiterTransformer.insertFileDataToTempTable() - Input file not found " , e);
           throw new ApplicationException(e);
       } catch (IOException e) {
           logger.error("DelimiterTransformer.insertFileDataToTempTable() - Couldn't read input file"
                   , e);
           throw new ApplicationException(e);
       } catch (SQLException e) {
           logger.error("DelimiterTransformer.insertFileDataToTempTable() - Couldn't insert data in table"
                   , e);
           throw new ApplicationException(e);
       }
       catch (Exception e){
           logger.error("DelimiterTransformer.insertFileDataToTempTable() - Couldn't insert data in table"
                   , e);
           throw new ApplicationException(e);
       }
        finally{
           IOUtils.closeQuietly(inputStream);
       }
        logger.debug("DelimitedTextFileTransformer.insertFileDataToTempTable() finish");
    }



    @Override
    public void trasformFiles(File[] inputFiles, GenericInputStructure inputStructure, Logger logger) throws ApplicationException {
        /* null implementation just to be a 'InputFilesTransformer' */
    }

    public static void main(String[] args){
        String test = "item1 | item2 | | item4 | ";
        String arr[] = test.split("\\|");
        for(String s:arr){
            System.out.println(s);
        }
    }

}
