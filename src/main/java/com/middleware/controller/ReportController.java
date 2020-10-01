package com.middleware.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.service.VisaService;

@RestController
@RequestMapping("report")
public class ReportController {
	
	@Autowired
	private VisaService visaService;
	

	private static Logger logger = Logger.getLogger(ReportController.class);

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
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "I", 0, "PAYMENT TRANSACTION WITH VISA", (short) 25, IndexedColors.BLUE.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "K", 1, " Company Name  - WIPO ", (short) 15, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "A", 2, " User Name ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "B", 2, " Email ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "C", 2, " Description ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "D", 2, " Customer Note ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "E", 2, " Amount ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "F", 2, " Currency ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "G", 2, " Tax Amount ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "H", 2, " Creation Time ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "I", 2, " Result ", (short) 13, IndexedColors.BLACK.index);
		writeValueinSpecificeCellWithColumn(workbook, sheet.getSheetName(), "J", 2, " Acquirer Message ", (short) 13, IndexedColors.BLACK.index);
	
	}

	@RequestMapping(value = "visa", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String getVisaReport(HttpServletResponse response) throws Exception { // @RequestBody JSONObject json,
		String newFilePath = "C:\\Users\\DELL\\middle-workspace\\Report\\Visa.xlsx";
		FileOutputStream out = new FileOutputStream(new File(newFilePath));
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Visa");
		writeTitle(workbook, sheet);
		List<Visa> visaList = visaService.findByDateRange(null, null);
		logger.info("visaList size!!!!!!!!!!!!!!!: " + visaList.size());
		for(Visa visa: visaList) {
			logger.info("visa transaction id!!!!!!!!!!!!!!: " +  visa.getVisaTransaction().getId());
			logger.info("user id!!!!!!!!!!!!!!: " +  visa.getUser().getName());
		}
	

		workbook.write(out);
		return "success";
	}
}
