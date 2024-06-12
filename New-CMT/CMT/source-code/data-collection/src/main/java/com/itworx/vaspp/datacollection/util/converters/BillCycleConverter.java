package com.itworx.vaspp.datacollection.util.converters;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class BillCycleConverter extends AbstractTextConverter {
	private static SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy 00:00:00");
	Logger logger = null;
	private static final String SEP = ",";

	@Override
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		Map<String, Long> dateVCount = new HashMap<String, Long>();
		logger.debug("Inside BillCycleConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());
		BufferedWriter outputStream = null;
		HSSFWorkbook workbook = null;
		String date = null;
		String key = null;
		long bcn = 0;
		double sla = 0.0;
		double actualBillCycleTotalDuration = 0.0;
		double PLAINEDBillCycleTotalDuration = 0.0;
		double actualBchTotalDuration = 0.0;
		double plainedBchTotalDuration = 0.0;
		Double[] totalNuberOfInvoices = new Double[2];
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("BillCycleConverter.convert() - converting file "
						+ inputFiles[i].getName());
				workbook = new HSSFWorkbook(new FileInputStream(inputFiles[i]));

				FormulaEvaluator evaluator = workbook.getCreationHelper()
						.createFormulaEvaluator();

				// Get first sheet from the workbook
				int x = 0;
				Boolean bchActualFound = false;
				HSSFSheet sheet = workbook.getSheetAt(0);

				if (sheet == null) {
					logger.error("- EX-1001:E   Sheet with name "
							+ inputFiles[i].getName() + " not found");
					throw new InputException("-" + inputFiles[i].getName()
							+ "not found");
				}
				// Iterate through each rows from first sheet
				Iterator rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					HSSFRow row = (HSSFRow) rowIterator.next();
					Iterator cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {
						CellValue cellValue = evaluator
								.evaluate((HSSFCell) cellIterator.next());
						if (cellValue != null) {
							if (cellValue.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								if (cellValue.getStringValue().contains("Date")
										&& cellValue.getStringValue().contains(
												":")) {
									date = getDate(cellValue.getStringValue()
											.split(":")[1].trim());

								} else if (cellValue.getStringValue().contains(
										"BC No:")) {
									bcn = Long
											.parseLong(cellValue
													.getStringValue()
													.split(":")[1] != null ? cellValue
													.getStringValue()
													.split(":")[1].trim() : "0");
								} else if (cellValue.getStringValue().contains(
										"SLA :")) {
									cellValue = evaluator
											.evaluate((HSSFCell) cellIterator
													.next());
									cellValue = evaluator
											.evaluate((HSSFCell) cellIterator
													.next());
									if (cellValue != null
											&& cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
										sla = Double.parseDouble((String
												.valueOf(cellValue
														.getNumberValue())
												.substring(0, 4)));

								} else if (cellValue.getStringValue().contains(
										"Total no. of Invoices:")) {

									cellValue = evaluator
											.evaluate((HSSFCell) cellIterator
													.next());
									if (cellValue != null
											&& cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
										totalNuberOfInvoices[x] = cellValue
												.getNumberValue();
										x++;
									}
								} else if (cellValue.getStringValue().contains(
										"BCH Actual")
										|| cellValue.getStringValue().contains(
												"Actual BCH")) {
									row = (HSSFRow) rowIterator.next();
									while (rowIterator.hasNext()) {
										row = (HSSFRow) rowIterator.next();

										if (row.getCell(1) == null
												|| row.getCell(1)
														.getStringCellValue()
														.equalsIgnoreCase("")) {
											break;
										}

									}

									Iterator actualCell = row.cellIterator();
									cellValue = evaluator
											.evaluate((HSSFCell) actualCell
													.next());
									cellValue = evaluator
											.evaluate((HSSFCell) actualCell
													.next());
									while (cellValue == null) {
										cellValue = evaluator
												.evaluate((HSSFCell) actualCell
														.next());

									}

									if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
										actualBchTotalDuration = Math
												.round(cellValue
														.getNumberValue() * 24);
									cellValue = evaluator
											.evaluate((HSSFCell) actualCell
													.next());
									while (cellValue == null) {
										cellValue = evaluator
												.evaluate((HSSFCell) actualCell
														.next());

									}
									if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
										plainedBchTotalDuration = Math
												.round(cellValue
														.getNumberValue() * 24);

								}

								else if (cellValue.getStringValue().contains(
										"Bill Cycle Total Duration")) {

									cellValue = evaluator
											.evaluate((HSSFCell) cellIterator
													.next());
									while (cellValue == null) {
										cellValue = evaluator
												.evaluate((HSSFCell) cellIterator
														.next());
									}
									if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
										actualBillCycleTotalDuration = Math
												.round(cellValue
														.getNumberValue() * 24);
									cellValue = evaluator
											.evaluate((HSSFCell) cellIterator
													.next());
									while (cellValue == null) {
										cellValue = evaluator
												.evaluate((HSSFCell) cellIterator
														.next());
									}
									if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
										PLAINEDBillCycleTotalDuration = Math
												.round(cellValue
														.getNumberValue() * 24);

								}

							}

						}
					}
				}// end of while
				key = date + SEP + bcn + SEP + sla + SEP
						+ totalNuberOfInvoices[0] + SEP
						+ totalNuberOfInvoices[1] + SEP
						+ actualBillCycleTotalDuration + SEP
						+ PLAINEDBillCycleTotalDuration + SEP
						+ actualBchTotalDuration + SEP
						+ plainedBchTotalDuration;
				outputStream.write(key);
				outputStream.newLine();	
				//System.out.println(key);
			}// end of files

			outputFiles[0] = outputFile;
			dateVCount.clear();
			logger.debug("BillCycleConverter.convert() - finished converting input files successfully ");
		} catch (FileNotFoundException e) {
			logger.error(
					"BillCycleConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error(
					"BillCycleConverter.convert() - Couldn't read input file",
					e);
			throw new ApplicationException(e);
		} catch (Exception e) {
			logger.error(
					"BillCycleConverter.convert() - Couldn't read input file",
					e);
			throw new ApplicationException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(
							"BillCycleConverter.convert() - Couldn't read input file",
							e);
					throw new ApplicationException(e);
				}
			}
		}
		logger.debug("BillCycleConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}

	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\VFE_VAS_SOURCE_CODE\\DataCollection");
			BillCycleConverter s = new BillCycleConverter();
			File[] input = new File[2];
			input[0] = new File(
					"D:\\VFE_VAS_SOURCE_CODE\\DataCollection\\Monitor Sheet 2013-02-BC11.xls");
			input[1] = new File(
					"D:\\VFE_VAS_SOURCE_CODE\\DataCollection\\Monitor Sheet-2013-02-BC02.xls");

			s.convert(input, "Maha_Test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDate(String str) throws ParseException {
		Date date = null;
		String dateString;
		date = inDateFormat.parse(str);
		dateString = outDateFormat.format(date);
		return dateString;
	}

} // inputStream = new BufferedReader(new FileReader(inputFiles[i]));

