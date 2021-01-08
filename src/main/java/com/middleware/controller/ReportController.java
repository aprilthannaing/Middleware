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

	private static Logger logger = Logger.getLogger(ReportController.class);

	public void writeValueinSpecificeCellWithBackGroundColor(Workbook workbook, String sheetName, String columnName,
			int rowNumber, String value, short fontSize, short color) {

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

	public void writeValueinSpecificeCell(Workbook workbook, String sheetName, String columnName, int rowNumber,
			String value, short fontSize) {
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

	private String getStartDate(String param) {

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

	private String getEndDate(String param) {
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

	private void writeSheet(XSSFWorkbook workbook, String startDate, String endDate) {
		XSSFSheet sheet = workbook.getSheetAt(0);
		short font = 10;
		logger.info("StartDate!!!!!!!!!!!!!!!!" + startDate);
		logger.info("endDate!!!!!!!!!!!!!!!!" + endDate);

		List<Visa> visaList = visaService.findByDateRange(startDate, endDate);
		logger.info("visaList!!!!!!!!!!!!!!!!" + visaList.size());
		int count = 1;
		for (Visa visa : visaList) {
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "A", 2 + count, count + "", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "B", 2 + count,
					"မူပိုင်ခွင့် ဉီးစီးဌာန\r\n" + "(MD-0001) \r\n" + "ရုံးအမှတ် (၅၂)\r\n" + "နေပြည်တော်", (short) 10);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "C", 2 + count, "(MD-0001)", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "D", 2 + count,
					"IP Department Online Filing Application", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "E", 2 + count, "VISA", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "F", 2 + count, visa.getNameOnCard(), font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "G", 2 + count, visa.getNumber(), font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "H", 2 + count, "-", font);

			Session session = visa.getSession();
			if (session == null)
				continue;
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "I", 2 + count, session.getPayerName(), font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "J", 2 + count, session.getPayerPhone(), font);
			writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "K", 2 + count,
					session.getPayerEmail(), font, HSSFColor.HSSFColorPredefined.BLUE.getIndex());
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "L", 2 + count,
					session.getTotalAmount() + " " + session.getCurrencyType(), font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "M", 2 + count, "300 MMK", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "N", 2 + count,
					(Double.parseDouble(session.getTotalAmount()) + 300) + " " + session.getCurrencyType(), font);

			String date = session.getPaymentConfirmationDate();
			if (date != null && !date.isEmpty()) {
				String[] confirmationDate = date.split(" ");
				String[] processDate = confirmationDate[0].split("-");
				writeValueinSpecificeCell(workbook, sheet.getSheetName(), "O", 2 + count,
						processDate[2] + "-" + processDate[1] + "-" + processDate[0] + " " + confirmationDate[1], font);
			} else
				writeValueinSpecificeCell(workbook, sheet.getSheetName(), "O", 2 + count, "-", font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "P", 2 + count, session.getRequestorId(), font);
			writeValueinSpecificeCell(workbook, sheet.getSheetName(), "Q", 2 + count, session.getPaymentReference(),
					font);
			VisaTransaction visaTransaction = visa.getVisaTransaction();
			if (visaTransaction != null)
				writeValueinSpecificeCell(workbook, sheet.getSheetName(), "R", 2 + count,
						visaTransaction.getTransactionId(), font);

			count++;
		}

	}

	@RequestMapping(value = "payment", method = RequestMethod.GET)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public void getReport(@RequestParam("input") String param, HttpServletResponse response) throws Exception {
		String startDate = getStartDate(param);
		String endDate = getEndDate(param);
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory
				.create(new File("D:\\Project\\middle-workspace\\Report\\TransactionReport.xlsx"));

		writeSheet(workbook, startDate, endDate);

		String newFilePath = "D:\\Project\\middle-workspace\\Report\\Payment.xlsx";
		FileOutputStream out = new FileOutputStream(new File(newFilePath));
		workbook.write(out);
		// workbook.write(response.getOutputStream());

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
