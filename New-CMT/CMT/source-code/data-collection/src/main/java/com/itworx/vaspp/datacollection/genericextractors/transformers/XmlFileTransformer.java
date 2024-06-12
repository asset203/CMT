package com.itworx.vaspp.datacollection.genericextractors.transformers;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.Utils;
import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.VInputStructure;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 5/7/13
 * Time   : 9:05 AM
 */
public class XmlFileTransformer extends InputFilesTransformer
{

    public  void insertFileDataToTempTable(File aFiles, VInputStructure inputStructure, Logger logger)
            throws ApplicationException{
       logger.debug("XmlFileTransformer.insertFileDataToTempTable start");
        BufferedReader inputStream=null;
        String [] rawData = null;
        int numberOfPlainColumns ;
        int numberOfSqlColumns=0;
        for(DataColumn col:inputStructure.getColumnsList()){
            if(StringUtils.hasText(col.getSqlExpression())){
                numberOfSqlColumns++;
            }
        }
        logger.debug("XmlFileTransformer.insertFileDataToTempTable numberOfSqlColumns = " +numberOfSqlColumns);

        numberOfPlainColumns = inputStructure.getColumns().length-numberOfSqlColumns;
        logger.debug("XmlFileTransformer.insertFileDataToTempTable numberOfPlainColumns = " +numberOfPlainColumns);
        try {
            //1-get inputStream
            inputStream = Utils.getGZIPAwareBufferdReader(aFiles);
            logger.debug("XmlFileTransformer.insertFileDataToTempTable inputStream initialized");
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(inputStream);
            //2-skip startDoc and root
            while (xmlEventReader.hasNext()) {

                XMLEvent documentStart = xmlEventReader.nextEvent();
                if (documentStart.isStartDocument()) {
                    continue;
                }
                if (documentStart.isStartElement()) {
                    xmlEventReader.nextEvent();
                    break;
                }
            }
            //3-reading file nodes
            while (xmlEventReader.hasNext()) {
                XMLEvent aNode = xmlEventReader.nextEvent();
                if (aNode.isStartElement()) {
                    String startNodeName = aNode.asStartElement().getName().getLocalPart();
                    rawData = new String[inputStructure.getColumns().length];
                    int i=0;
                    while (xmlEventReader.hasNext()) {
                        XMLEvent event = xmlEventReader.nextEvent();
                        if (event.isEndElement()) {
                            String name = event.asEndElement().getName().getLocalPart();
                            if (startNodeName.equals(name)) {
                                if (rawData!=null && rawData.length >=numberOfPlainColumns) {
                                    try{
                                        logger.debug("XmlFileTransformer.insertFileDataToTempTable(): start  tmpDbTableOperator.substiuteDataInInsertQuery()");
                                        tmpDbTableOperator.substiuteDataInInsertQuery(rawData);
                                        dataFound=true;
                                        logger.debug("XmlFileTransformer.insertFileDataToTempTable(): finish  tmpDbTableOperator.substiuteDataInInsertQuery()");
                                    }
                                    catch(Exception e){
                                        logger.debug(e);
                                        notValidData++;
                                    }
                                }
                                else{
                                    notValidData++;
                                }
                                break;
                            }
                        }
                        if (event.isStartElement()) {
                            String name = event.asStartElement().getName().getLocalPart();
                            if (startNodeName.equals(name)) {
                                break;
                            }
                            String value = xmlEventReader.getElementText();
                            rawData[i]=value;
                            i++;
                        }
                    }
                }
            }
            logger.debug("XmlFileTransformer.insertFileDataToTempTable(): start   tmpDbTableOperator.executeBatch()");
            tmpDbTableOperator.executeBatch();
            logger.debug("XmlFileTransformer.insertFileDataToTempTable(): finish   tmpDbTableOperator.executeBatch()");
        } catch (XMLStreamException e) {
            logger.error("xmlFileTransformer.insertFileDataToTempTable() - Couldn't parse input file  " , e);
            throw new ApplicationException("Error parsing input xml source data: " +e);
        } catch (FileNotFoundException e) {
            logger.error("xmlFileTransformer.insertFileDataToTempTable() - Input file not found " , e);
            throw new ApplicationException(e);
        } catch (IOException e) {
            logger.error("xmlFileTransformer.insertFileDataToTempTable() - Couldn't read input file" ,e);
            throw new ApplicationException(e);
        } catch (SQLException e) {
            logger.error("xmlFileTransformer.insertFileDataToTempTable() - Couldn't insert data in table", e);
            throw new ApplicationException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        logger.debug("XmlFileTransformer.insertFileDataToTempTable finish");
    }


    @Override
    public void trasformFiles(File[] inputFiles, GenericInputStructure inputStructure, Logger logger) throws ApplicationException {
        //null implementation just to be an InputFilesTransformer.
    }
}
