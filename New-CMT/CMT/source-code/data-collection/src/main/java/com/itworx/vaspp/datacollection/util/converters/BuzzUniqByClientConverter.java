package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.BuzzUniqueByClientData;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class BuzzUniqByClientConverter extends AbstractTextConverter {
	private Logger logger;
	private org.jdom.Document document;

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {

		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside BuzzUniqByClientConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {

				SAXBuilder saxfac = new SAXBuilder(
						"org.apache.xerces.parsers.SAXParser");
				saxfac.setValidation(false);
				try {
					saxfac.setFeature("http://xml.org/sax/features/validation",
							false);
					saxfac
							.setFeature(
									"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
									false);
					saxfac
							.setFeature(
									"http://apache.org/xml/features/nonvalidating/load-external-dtd",
									false);
					saxfac
							.setFeature(
									"http://xml.org/sax/features/external-general-entities",
									false);
					saxfac
							.setFeature(
									"http://xml.org/sax/features/external-parameter-entities",
									false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				document = saxfac.build(inputFiles[i]);
				Element root = document.getRootElement();
				List<Element> rowElement = root.getChildren("row");
				long jmeUser = 0;
				long symbianUser = 0;
				long androidUser = 0;
				long iosUser = 0;
				long blackberryUser = 0;
				double jmePercentage = 0.0;
				double symbianPercentage = 0.0;
				double androidPercentage = 0.0;
				double iosPercentage = 0.0;
				double blackberryPercentage = 0.0;
				for (Element e : rowElement) {
					List<Element> colElements = e.getChildren("col");
					// System.out.println("Size is " + colElements.size());
					String outDate = "";
					for (Element element : colElements) {
						String attributeName = element
								.getAttributeValue("name");
						// System.out.println("Name is " + attributeName);
						try {
							if (attributeName.equals("Time")) {
								outDate = getDate(element.getValue());
							}
						} catch (ParseException exc) {
							logger.error(exc);
							break;
						}
						try {
							if (attributeName.equals("JME User")) {
								jmeUser = Long.parseLong(element.getValue());
							} else if (attributeName.equals("Symbian User")) {
								symbianUser = Long
										.parseLong(element.getValue());
							} else if (attributeName.equals("Android User")) {
								androidUser = Long
										.parseLong(element.getValue());
							} else if (attributeName.equals("iOS User")) {
								iosUser = Long.parseLong(element.getValue());
							} else if (attributeName.equals("BlackBerry User")) {
								blackberryUser = Long.parseLong(element
										.getValue());
							} else if (attributeName.equals("JME Percentage")) {
								String value = element.getValue();
								value = value.substring(0, value.lastIndexOf("%"));
								jmePercentage = Double.parseDouble(value);
							} else if (attributeName.equals("Symbian Percentage")) {
								String value = element.getValue();
								value = value.substring(0, value.lastIndexOf("%"));
								symbianPercentage = Double.parseDouble(value);
							} else if (attributeName.equals("Android Percentage")) {
								String value = element.getValue();
								value = value.substring(0, value.lastIndexOf("%"));
								androidPercentage = Double.parseDouble(value);
							} else if (attributeName.equals("iOS Percentage")) {
								String value = element.getValue();
								value = value.substring(0, value.lastIndexOf("%"));
								iosPercentage = Double.parseDouble(value);
							} else if (attributeName.equals("BlackBerry Percentage")) {
								String value = element.getValue();
								value = value.substring(0, value.lastIndexOf("%"));
								blackberryPercentage = Double.parseDouble(value);
							}
						} catch (NumberFormatException ex) {
							logger.error(ex);
							continue;
						}

					}

					// filling the countesr and thier values

					if (!outDate.equals("") && outDate != null) {
						outputStream.write(outDate + "," + jmeUser + ","
								+ symbianUser + "," + androidUser + ","
								+ iosUser + "," + blackberryUser + ","
								+ jmePercentage + "," + symbianPercentage + ","
								+ androidPercentage + "," + iosPercentage + ","
								+ blackberryPercentage);
						outputStream.newLine();
					}
				}
			}
			outputStream.close();
			outputFiles[0] = outputFile;
			logger
					.debug("VPNXMLCompaniesConverter.convert() - finished converting input files successfully ");

		} catch (Exception e) {
			logger
					.error("ConfigReader.ConfigReader() : couldn't open xml input file  "
							+ e);
			throw new ApplicationException(e);
		}

		return outputFiles;
	}

	private String getDate(String value) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy");

		if (!value.equals("") && value != null)

			date = inDateFormat.parse(value);

		dateString = outDateFormat.format(date);

		return dateString;

	}

	public static void main(String ag[]) {
		try {
			PropertyReader
					.init("D:\\Projects\\VNNP_\\SourceCode\\DataCollection");
			BuzzUniqByClientConverter object = new BuzzUniqByClientConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\Projects\\VNNP_\\SourceCode\\DataCollection\\Vodafone Overall 8 Unique User By Client Type.xml");
			object.convert(input, "BUZZ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
