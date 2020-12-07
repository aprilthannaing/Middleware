package com.middleware.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.MPUPaymentTransaction;
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
	public String getReport(@RequestParam("input") String param, HttpServletResponse response) throws Exception {
		
		String[] str = param.split(",");
		String option = str[str.length - 1];

		logger.info("param : " + param);
		logger.info("option : " + option);
		String startDate = "";
		String endDate = "";

		switch (option) {
		case "CUSTOM":
			String[] start = str[0].split(" ");
			String[] end = str[1].split(" ");

			startDate = start[3] + "/" + parseMonthToInt(start[1]) + "/" + start[2];
			endDate = end[3] + "/" + parseMonthToInt(end[1]) + "/" + end[2];
			break;
		case "MONTHLY":
			startDate = str[0] + "/" + parseMonthToInt(str[1]) + "/" + "01";
			String monthYear = str[2] + "/" + parseMonthToInt(str[3]) + "/";
			endDate = monthYear + getEndDateOfMonth(monthYear + "01");
			break;
		case "YEARLY":
			startDate = str[0] + "/01/01";
			endDate = getEndDateOfYear(str[0]);
			break;

		}

		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);

		XSSFWorkbook workbook = new XSSFWorkbook();
		
		writeVisaSheet(workbook, startDate, endDate);
		writeCBPaySheet(workbook, startDate, endDate);
		writeMPUSheet(workbook, startDate, endDate);
		//workbook.write(response.getOutputStream());
		
//		String newFilePath = "C:\\Users\\DELL\\middle-workspace\\Report\\Payment.xlsx";
		String newFilePath = "C:\\Users\\ASUS\\Downloads\\TestPayment.xlsx";
		FileOutputStream out = new FileOutputStream(new File(newFilePath));
		workbook.write(out);

		return "success";

		 
	}

}
