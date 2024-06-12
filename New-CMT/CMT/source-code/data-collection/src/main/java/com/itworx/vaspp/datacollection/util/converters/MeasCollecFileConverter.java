/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mahmoud.awad 11/6/2017
 */
public class MeasCollecFileConverter extends AbstractTextConverter {

    private Logger logger;
    private org.jdom.Document document;
    private Document xmlDocument;

    private Map<Integer, String> pValues = new TreeMap<Integer, String>();

    private String beginDate;
    private String endDate;

    public MeasCollecFileConverter() {
    }

    public File[] convert(File[] inputFiles, String systemName)
            throws ApplicationException {
        logger = Logger.getLogger(systemName);
        logger
                .debug("Inside MeasCollecFileConverter convert - started converting input files");
        String path = PropertyReader.getConvertedFilesPath();
        //String path = "D:\\Work\\CMT New Envir\\converted";
        File[] outputFiles = new File[1];
        File outputFile = new File(path, inputFiles[0].getName());

        BufferedReader inputStream = null;
        BufferedWriter outputStream;

        try {
            outputStream = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < inputFiles.length; i++) {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                xmlDocument = dBuilder.parse(inputFiles[i]);

                xmlDocument.getDocumentElement().normalize();

                XPath xPath = XPathFactory.newInstance().newXPath();

                String expression1 = "measCollecFile/fileHeader/measCollec";

                String expression2 = "measCollecFile/measData/measInfo/granPeriod";

                String expression3 = "measCollecFile/measData/measInfo/measType";

                String expression4 = "measCollecFile/measData/measInfo/measValue/r";

                NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);

                Node fileHeaderNode = nodeList1.item(0);

                if (fileHeaderNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) fileHeaderNode;

                    beginDate = getDate(element.getAttribute("beginTime"));
                }

                NodeList nodeList2 = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);

                Node granPeriodNode = nodeList2.item(0);

                if (granPeriodNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) granPeriodNode;

                    endDate = getDate(element.getAttribute("endTime"));
                }

                NodeList nodeList3 = (NodeList) xPath.compile(expression3).evaluate(xmlDocument, XPathConstants.NODESET);

                for (int j = 0; j < nodeList3.getLength(); j++) {

                    Node node = nodeList3.item(j);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element element = (org.w3c.dom.Element) node;

                        pValues.put(Integer.parseInt(element.getAttribute("p")), element.getTextContent());
                    }

                }

                NodeList nodeList4 = (NodeList) xPath.compile(expression4).evaluate(xmlDocument, XPathConstants.NODESET);

                for (int j = 0; j < nodeList4.getLength(); j++) {

                    Node node = nodeList4.item(j);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element element = (org.w3c.dom.Element) node;

                        Integer key = Integer.parseInt(element.getAttribute("p"));
                        if (pValues.containsKey(key)) {
                            String pValue = pValues.get(key);
                            pValue += "," + element.getTextContent();
                            pValues.remove(key);
                            pValues.put(key, pValue);
                        }
                    }

                }

            }

            Iterator myVeryOwnIterator = pValues.keySet().iterator();

            while (myVeryOwnIterator.hasNext()) {

                Integer key = (Integer) myVeryOwnIterator.next();

                outputStream.write(beginDate + "," + endDate + "," + key + "," + pValues.get(key));
                outputStream.newLine();

            }

            outputStream.close();
            outputFiles[0] = outputFile;
            logger.debug("MeasCollecFileConverter.convert() - finished converting input files successfully ");

        } catch (Exception e) {
            logger.error("ConfigReader.ConfigReader() : couldn't open xml input file  " + e);
            throw new ApplicationException(e);
        }

        return outputFiles;
    }

    private String getDate(String value) throws ParseException {
        Date date = new Date();
        String dateString;
        
        StringBuilder sb = new StringBuilder(value);
        sb.deleteCharAt(sb.lastIndexOf(":"));
        
        SimpleDateFormat inDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat outDateFormat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");

        if (value != null) {
            date = inDateFormat.parse(sb.toString());
        }

        dateString = outDateFormat.format(date);

        return dateString;

    }

    /*
 * Validator class for XML File
     */
    private class Validator extends DefaultHandler {

        public boolean validationError = false;

        public SAXParseException saxParseException = null;

        public void error(SAXParseException exception) throws SAXException {
            validationError = true;
            saxParseException = exception;
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            validationError = true;
            saxParseException = exception;
        }

        public void warning(SAXParseException exception) throws SAXException {
        }
    }

    public void validateXML(File inputFile) throws ApplicationException {
        String schemaURL = "D:\\build\\VASPortal\\DataCollection\\resources\\xmlSchemas\\VPN_SCHEMA.xsd";
        String fileURL = "D:\\build\\VASPortal\\DataCollection\\resources//properties\\input_config.xml";
        try {
            SAXBuilder builder = new SAXBuilder(
                    "org.apache.xerces.parsers.SAXParser");
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                    "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            factory.setAttribute(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    schemaURL);
            DocumentBuilder builder2 = factory.newDocumentBuilder();
            Validator handler = new Validator();
            builder2.setErrorHandler(handler);
            builder2.parse(inputFile);
            if (handler.validationError == true) {
                logger
                        .error("ConfigReader.validateDocument() - Invalid XML Document: XML Document has Error:"
                                + handler.validationError
                                + " "
                                + handler.saxParseException.getMessage());
                throw new ApplicationException("Invalid XML Document:"
                        + handler.validationError + " "
                        + handler.saxParseException.getMessage(), 1001);
            } else {
                logger
                        .debug("ConfigReader.validateDocument() - XML Document is valid");
            }
        } catch (ParserConfigurationException e) {
            logger
                    .error("ConfigReader.validateDocument() - error with xml parser:"
                            + e);
            throw new ApplicationException("error with xml parser:" + e);
        } catch (IOException e) {
            logger
                    .error("ConfigReader.validateDocument() - couldn't access xml input file or schema:"
                            + e);
            throw new ApplicationException(
                    "couldn't access xml input file or schema:" + e);
        } catch (SAXException e) {
            logger
                    .error("ConfigReader.validateDocument() - XML Document has Error:"
                            + e);
            throw new ApplicationException("XML Document has Error:" + e);
        }
    }

    public static void main(String ag[]) {
        try {
            PropertyReader
                    .init("D:\\CMT");
            MeasCollecFileConverter s = new MeasCollecFileConverter();
            File[] input = new File[1];
            input[0] = new File("D:\\CMT\\A20161017.1100-1115-StatisticsPMJob_ESA.XML");
//            input[1] = new File("D:\\build\\VASPortal\\DataCollection\\sample2.xml");
            s.convert(input, "SDP");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
