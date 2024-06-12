package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CCNCountersGenericConverter extends AbstractTextConverter {

    private Logger logger;
    private static String[] CCN_COUNTERS = null;

    public File[] convert(File[] inputFiles, String systemName)
            throws ApplicationException, InputException {
        //logger = Logger.getLogger(systemName);
        //logger.debug("CCNCountersGenericConverter.convert() - started converting input files ");
        //String ccnCounterString = PropertyReader.getCCNCounters();
        String ccnCounterString = "ApnConversion-Failed|GeneralCongestion-DataBase|GeneralCongestion-Dialogue-Overload|RequestRejected-Overload|Primary-Account-Finder-Error|Secondary-Account-Finder-Error|SDP-Lookup-Failed-Subscriber-Not-Found|AccessFailure-Database|\\\n"
                + "CAP-Received-Total-Invalid-Requests|\\\n"
                + "CAPv2-Received-Total-Requests|CAPv2-Received-Component-Rejects|CAP2-Received-Error-Indications|CAPv2-Sent-Total-Requests|CAPv2-Sent-Component-Rejects|CAPv2-Sent-Error-Indications|CAPv2-Received-Returned-Components|\\\n"
                + "CdrOutput-Encoded-Successful|CdrOutput-Encoded-Failed|CdrOutput-Data-Sent-To-Primary-Disc|CdrOutput-Data-Sent-To-Secondary-Disc|CdrOutput-Data-Discarded|CdrOutput-Data-Stored-On-Disc-0|CdrOutput-Data-Stored-On-Disc-1|CdrOutput-Files-Generated-On-Disc-0|CdrOutput-Files-Generated-On-Disc-1|CdrOutput-Files-Sent-To-FTP-Destination|CdrOutput-Files-Sent-To-FTPDestination-Failed|\\\n"
                + "INAP-Received-Component-Rejects|INAP-Received-Error-Indications|INAP-Received-Returned-Components|INAP-Received-Total-Invalid-Requests|INAP-Received-Total-Requests|INAP-Sent-Component-Rejects|INAP-Sent-Error-Indications|INAP-Sent-Total-Requests|\\\n"
                + "RelaySms-FirstInterrogation-Successful|RelaySms-FirstInterrogation-Congestion-Rejected|RelaySms-FirstInterrogation-Congestion-Allowed|RelaySms-FirstInterrogation-NoContactWithSDPRejected|RelaySms-FirstInterrogation-NoContactWithSDPAllowed|RelaySms-FirstInterrogation-InternalError-Rejected|RelaySms-FinalReport-Successful|RelaySms-FinalReport-NoContactWithSDP|RelaySms-FinalReport-InternalError|RelaySms-FinalReport-Congestion";
        CCN_COUNTERS = ccnCounterString.split("\\|");
        File[] outputFiles = new File[1];
        HashMap<String, double[]> hourMap = new HashMap<String, double[]>();
        try {
            //String path = PropertyReader.getConvertedFilesPath();	
            String path = "D:\\Work\\CMT New Envir\\converted";
            File output = new File(path, "CCNCountersGenericConverterFile");
            BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            for (int i = 0; i < inputFiles.length; i++) {
                String fileName = inputFiles[i].getName();
                String dateHourString = fileName.substring(fileName.indexOf("A") + 1, fileName.indexOf("A") + 12).trim();

                domFactory.setNamespaceAware(false);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                builder.setEntityResolver(new EntityResolver() {
                    public InputSource resolveEntity(String arg0, String arg1)
                            throws SAXException, IOException {
                        return new InputSource(new StringReader(""));
                    }
                });
                Document doc = builder.parse(inputFiles[i]);

                XPathExpression measObjInstIdsExpr = xpath.compile("/mdc/md/mi/mv/moid");
                Object measObjInstIdsResult = measObjInstIdsExpr.evaluate(doc, XPathConstants.NODESET);
                NodeList measObjInstIdsNodes = (NodeList) measObjInstIdsResult;
                for (int j = 0; j < measObjInstIdsNodes.getLength(); j++) {
                    Node currentNode = measObjInstIdsNodes.item(j);
                    String measObjInstIdTxt = currentNode.getTextContent();
                    if (measObjInstIdTxt != null && measObjInstIdTxt != "") {
                        measObjInstIdTxt = measObjInstIdTxt.replaceAll("\\s", "").replaceAll("\n", "").replaceAll("\r", "");
                        String[] measObjInstTxtArr = measObjInstIdTxt.split("=|,");
                        if (measObjInstTxtArr.length == 4 && "_SYSTEM".equals(measObjInstTxtArr[3])) {
                            NodeList nodeChildren = currentNode.getParentNode().getChildNodes();
                            //values arranged as CCN_COUNTERS field in app_config
                            for (int n = 0; n < nodeChildren.getLength(); n++) {
                                Node measureNode = nodeChildren.item(n);
                                if ("r".equals(measureNode.getNodeName())) {
                                    int neededIdx = Utils.indexOf(measObjInstTxtArr[1], CCN_COUNTERS);
                                    if (neededIdx > -1) {
                                        String measureValTxt = measureNode.getTextContent();
                                        if (!"".equals(measureValTxt)) {
                                            double measureVal = Double.parseDouble(measureValTxt);
                                            double measuresArr[] = null;
                                            if (hourMap.containsKey(dateHourString)) {
                                                measuresArr = (double[]) hourMap.get(dateHourString);
                                            } else {
                                                measuresArr = new double[CCN_COUNTERS.length];
                                                hourMap.put(dateHourString, measuresArr);
                                            }
                                            measuresArr[neededIdx] += measureVal;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println("date,"+Arrays.toString(CCN_COUNTERS).replaceAll("(\\[)|(\\])|(\\s)", ""));
            for (Iterator iter = hourMap.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                double[] measuresArr = (double[]) hourMap.get(key);
                String dateHour = Utils.convertToDateString(Utils.convertToDate(key, "yyyyMMdd.HH"), Utils.defaultFormat);
                String measuresStr = Arrays.toString(measuresArr).replaceAll("(\\[)|(\\])|(\\s)", "");
                outputStream.write(dateHour + "," + measuresStr);
                //System.out.println(dateHour+","+measuresStr);
                outputStream.newLine();
            }

            hourMap.clear();
            hourMap = null;
            outputStream.flush();
            outputStream.close();
            outputFiles[0] = output;
            logger.debug("CCNCountersGenericConverter.convert() - " + inputFiles[0].getName() + " converted");
        } catch (FileNotFoundException e) {
            logger.error("CCNCountersGenericConverter.convert() - Input file not found " + e);
            throw new ApplicationException(e);
        } catch (IOException e) {
            logger.error("CCNCountersGenericConverter.convert() - Couldn't read input file" + e);
            throw new ApplicationException(e);
        } catch (ParserConfigurationException e) {
            logger.error("CCNCountersGenericConverter.convert() - Couldn't parse input file " + e);
            throw new ApplicationException(e);
        } catch (SAXException e) {
            logger.error("CCNCountersGenericConverter.convert() - Couldn't parse input file " + e);
            throw new ApplicationException(e);
        } catch (XPathExpressionException e) {
            logger.error("CCNCountersGenericConverter.convert() - Invalid use of XPath expression " + e);
            throw new ApplicationException(e);
        }
        logger.debug("CCNCountersGenericConverter.convert() - finished converting input files successfully");
        return outputFiles;
    }

    public static void main(String ag[]) {
        try {

            //PropertyReader.init("D:\\VASPortalWF5\\Source Code\\DataCollection");
            CCNCountersGenericConverter s = new CCNCountersGenericConverter();
            File[] input = new File[1];
            /*
			input[0]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0000-0005_jambala_CcnCounters");
			input[1]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0005-0010_jambala_CcnCounters");
			input[2]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0010-0015_jambala_CcnCounters");
			input[3]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0015-0020_jambala_CcnCounters");
			input[4]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0020-0025_jambala_CcnCounters");
			input[5]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0025-0030_jambala_CcnCounters");
			input[6]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20070605.0030-0035_jambala_CcnCounters");
			input[7]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20090701.2020-2025_jambala_CcnCounters");
			input[8]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20090701.2020-2025_jambala_CcnCounters");
			input[9]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20090701.2020-2025_jambala_CcnCounters");
			input[10]=new File("D:\\VASPortalWF5\\Source Code\\DataCollection\\resources\\ftpfolder\\A20090701.2020-2025_jambala_CcnCounters");
             */
            input[0] = new File("D:\\CMT\\A20090701.2020-2025_jambala_CcnCounters.xml");

            s.convert(input, "Database");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
