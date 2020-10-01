package com.middleware.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.User;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;
import com.middleware.service.VisaService;

@RestController
@RequestMapping("report")
public class ReportController {
	
	@Autowired
	private VisaService visaService;
	

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
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
	
	public void writeValueinSpecificeCellWithColumn(Workbook workbook, String sheetName, String columnName,
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
	
	private void writeTitle(XSSFWorkbook workbook, XSSFSheet sheet) {

		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "H", 0, "PAYMENT TRANSACTION WITH VISA", (short) 25, IndexedColors.BLUE.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "J", 1, " Company Name  - WIPO ", (short) 15, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "A", 2, " Creation Time ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "B", 2, " User Name ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "C", 2, " Email ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "D", 2, " Customer Note ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "E", 2, " Description ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "F", 2, " Amount ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "G", 2, " Currency ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "H", 2, " Tax Amount ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "I", 2, " Acquirer Message ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithBackGroundColor(workbook, sheet.getSheetName(), "J", 2, " Result ", (short) 13, IndexedColors.BLACK.index);	
	}

	@RequestMapping(value = "visa", method = RequestMethod.GET)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public void getVisaReport(HttpServletResponse response) throws Exception { // @RequestBody JSONObject json,
		JSONObject json = new JSONObject();
		String newFilePath = "C:\\Users\\DELL\\middle-workspace\\Report\\Visa.xlsx";
		FileOutputStream out = new FileOutputStream(new File(newFilePath));
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Visa");
		writeTitle(workbook, sheet);
		List<Visa> visaList = visaService.findByDateRange("2020-09-01", "2020-10-01");
		
		int count = 3;
		for(Visa visa: visaList) {
			if(visa == null || visa.getVisaTransaction() == null)
				continue;
			
			VisaTransaction visaTransaction = visa.getVisaTransaction();
			User user = visa.getUser();
			
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "A", count, visa.getCreationTime() , (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "B", count, user.getName(), (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "C", count, user.getEmail(), (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "D", count, user.getPaymentdescription(), (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "E", count, visa.getDescription(), (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "F", count, visa.getAmount() + "" , (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "G", count, visa.getCurrency(), (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "H", count, visaTransaction.getTaxAmount() , (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "I", count, visaTransaction.getAcquirerMessage() , (short) 13, IndexedColors.BLACK.index);
			writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "J", count, visa.getResult() , (short) 13, IndexedColors.BLACK.index);
			count++;
		}
		
		workbook.write(response.getOutputStream());
		json.put("visaList", visaList);
		//return json;
	}
}
