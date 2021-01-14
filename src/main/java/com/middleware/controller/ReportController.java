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
