package com.middleware.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Session;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.VisaService;

@RestController
@RequestMapping("report")
public class ReportController extends AbstractController {

	@Autowired
	private VisaService visaService;

	@Autowired
	private CBPaymentTransactionService cbPayTransactionService;

	@Autowired
	private MPUPaymentTransactionService mpuPaymentService;

	@Value("${FILEPATH}")
	private String filePath;

	@Value("${SERVICECHARGES}")
	private String serviceChanges;

	private static Logger logger = Logger.getLogger(ReportController.class);

	public void writeValueinSpecificeCellWithBackGroundColor(Workbook workbook, String sheetName, String columnName, int rowNumber, String value, short fontSize, short color) {

		Sheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		int columnNumber = CellReference.convertColStringToIndex(columnName);
		row = sheet.getRow(rowNumber);

		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		font.setColor(color);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);

		Cell cell;
		if (row == null)
			row = sheet.createRow(rowNumber);
		cell = row.getCell(columnNumber);

		if (cell == null)
			cell = row.createCell(columnNumber);

		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	public void writeValueinSpecificeCell(Workbook workbook, String sheetName, String columnName, int rowNumber, String value, short fontSize) {
		Sheet sheet = workbook.getSheet(sheetName);
		Row row = null;
		int columnNumber = CellReference.convertColStringToIndex(columnName);
		row = sheet.getRow(rowNumber);

		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);

		Cell cell;
		if (row == null)
			row = sheet.createRow(rowNumber);
		cell = row.getCell(columnNumber);

		if (cell == null)
			cell = row.createCell(columnNumber);

		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	public String getStartDate(String param) {

		/*
		 * : param : Tue Dec 01 2020 00:00:00 GMT 0630 (Myanmar Time),Thu Dec 31 2020
		 * 00:00:00 GMT 0630 (Myanmar Time),CUSTOM : option : CUSTOM : startDate :
		 * 2020/12/01 : endDate : 2020/12/31
		 */

		String[] str = param.split(",");
		String option = str[str.length - 1];
		String startDate = "";

		switch (option) {
		case "CUSTOM":
			String[] start = str[0].split(" ");
			startDate = start[3] + "-" + parseMonthToInt(start[1]) + "-" + start[2];
			break;
		case "MONTHLY":
			startDate = str[0] + "-" + parseMonthToInt(str[1]) + "-" + "01";
			break;
		case "YEARLY":
			startDate = str[0] + "-01-01";
			break;
		}

		return startDate;
	}

	public String getEndDate(String param) {
		String[] str = param.split(",");
		String option = str[str.length - 1];
		String endDate = "";
		switch (option) {
		case "CUSTOM":
			String[] end = str[1].split(" ");
			endDate = end[3] + "-" + parseMonthToInt(end[1]) + "-" + end[2];
			break;
		case "MONTHLY":
			String monthYear = str[2] + "-" + parseMonthToInt(str[3]) + "-";
			endDate = monthYear + getEndDateOfMonth(monthYear + "01");
			break;
		case "YEARLY":
			endDate = getEndDateOfYear(str[0]);
			break;
		}
		return endDate;

	}

	@RequestMapping(value = "payment", method = RequestMethod.GET)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public void getReport(@RequestParam("input") String param, HttpServletResponse response) throws Exception {
		String startDate = getStartDate(param);
		String endDate = getEndDate(param);
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(new File(filePath + "TransactionReport.xlsx"));
		writeSheet(workbook, startDate, endDate);
//		String newFilePath = filePath + "Payment.xlsx";
//		FileOutputStream out = new FileOutputStream(new File(newFilePath));
//		workbook.write(out);
		workbook.write(response.getOutputStream());

	}

	public static String decode(String encString) {
		StringTokenizer token = new StringTokenizer(encString, ",");
		StringBuffer buffer = new StringBuffer();
		while (token.hasMoreTokens()) {
			buffer.append((char) Integer.parseInt(token.nextToken()));
		}
		return buffer.toString();
	}

}
