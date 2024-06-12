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
import com.itworx.vaspp.datacollection.persistenceobjects.BuzzDownloadsData;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class BuzzDownloadsConverter extends AbstractTextConverter {

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
				long blackberryDownload = 0;
				long symbianDownload = 0;
				long androidDownload = 0;
				long jmeDownload = 0;
				long iphoneDownload = 0;
				long totalDownload = 0;
				for (Element e : rowElement) {
					List<Element> colElements = e.getChildren("col");
					// System.out.println("Size is " + colElements.size());
					String outDate = "";
					BuzzDownloadsData object = new BuzzDownloadsData();
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
							if (attributeName.equals("BlackBerry Download")) {
								blackberryDownload = Long.parseLong(element
										.getValue());
							} else if (attributeName.equals("Symbian Download")) {
								symbianDownload = Long.parseLong(element
										.getValue());
							} else if (attributeName.equals("J2ME Download")) {
								jmeDownload = Long
										.parseLong(element.getValue());
							} else if (attributeName.equals("Android Download")) {
								androidDownload = Long.parseLong(element
										.getValue());
							} else if (attributeName.equals("IPhone Download")) {
								iphoneDownload = Long.parseLong(element
										.getValue());
							} else if (attributeName.equals("Total Download")) {
								totalDownload = Long.parseLong(element
										.getValue());
							}
						} catch (NumberFormatException ex) {
							logger.error(ex);
							continue;
						}
					}

					// filling the countesr and thier values

					if (!outDate.equals("") && outDate != null) {
						outputStream.write(outDate + "," + blackberryDownload
								+ "," + symbianDownload + "," + jmeDownload
								+ "," + androidDownload + "," + iphoneDownload
								+ "," + totalDownload);
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
			BuzzDownloadsConverter object = new BuzzDownloadsConverter();
			File[] input = new File[1];
			input[0] = new File(
					"D:\\Projects\\VNNP_\\SourceCode\\DataCollection\\Vodafone Overall 7 Download.xml");
			object.convert(input, "BUZZ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
