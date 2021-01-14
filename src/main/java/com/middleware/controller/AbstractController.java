package com.middleware.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.middleware.entity.Session;
import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.VisaService;

@Service
public class AbstractController {
//	public static final String secretKey = "67f878091a3d0b3d871bdc53f47b15aa74ad9e25";
	// wipouser,123//SHA1
	
	public static final String secretKey = "mykey@91mykey@91";
	
	@Autowired
	private VisaService visaService;

	@Autowired
	private CBPaymentTransactionService cbPayTransactionService;

	@Autowired
	private MPUPaymentTransactionService mpuPaymentService;
	
	private static Logger logger = Logger.getLogger(AbstractController.class);

	public String getMyanmarElement(String content, String element,
			String remover) {
		int begin = content.indexOf(element);
		content = content.substring(begin, content.length());
		int end = content.indexOf("</dynamic-content>");
		String remainString = content.substring(end, content.length());

		int mStart = remainString.indexOf(remover);
		if (mStart > 0) {
			int mEnd = remainString.indexOf("]]");
			if (!remainString.isEmpty() && mEnd > 0)
				return Jsoup.parse(remainString.substring(mStart, mEnd)).text();
		}
		return "";
	}

	public String getEngElement(String content, String element,
			String remover) {
		int begin = content.indexOf(element);
		String remainString = content.substring(begin, content.length());
		int mStart = remainString.indexOf(remover);
		if (mStart > 0) {
			int mEnd = remainString.indexOf("]]");
			if (remainString.isEmpty() || mEnd < 0 || mStart < 0
					|| mEnd < mStart)
				return "";

			return Jsoup.parse(remainString.substring(mStart, mEnd)).text();
		}
		return "";
	}

	public String ImageSourceChange(String htmlinput) {

		Document docimage = Jsoup.parse(htmlinput, "", Parser.htmlParser());
		Elements images = docimage.getElementsByTag("img");
		if (images.size() > 0) {
			for (Element img : images) {
				String imgsrc = img.attr("src");

				// System.out.println("source image...." + imgsrc);
				String imgreplace = "https://myanmar.gov.mm" + imgsrc;
				img.attr("src", imgreplace);
			}
		}
		return docimage.html();
	}

	public String ImageSourceChangeforanouncement(String htmlinput) {

		Document docimage = Jsoup.parse(htmlinput, "", Parser.htmlParser());
		Elements images = docimage.getElementsByTag("img");
		String imgreplace;
		if (images.size() > 0) {
			for (Element img : images) {
				String imgsrc = img.attr("src");
				if (img.attr("src").contains("data:image/jpeg;")) {
					imgreplace = imgsrc;
				} else if (img.attr("src").contains("https://")) {
					imgreplace = imgsrc;
				} else {
					imgreplace = "https://myanmar.gov.mm" + imgsrc;
				}
				// System.out.println("source image...." + imgsrc);
				img.attr("src", imgreplace);
			}
		}
		return docimage.html();
	}

	public void replaceTag(Elements els) {
		ListIterator<Element> iter = els.listIterator();
		while (iter.hasNext()) {
			Element el = iter.next();
			replaceTag(el.children());
			if (el.parentNode() != null)
				el.replaceWith(new TextNode("/" + el.text().trim() + "/"));
		}
	}

	public List<String> removeInvalidString(String[] titleArr) {
		List<String> titleList = new ArrayList<String>();
		for (String title : titleArr) {
			if (title != null && !title.isEmpty() && title.length() > 1)
				titleList.add(title);
		}
		return titleList;
	}

	public List<String> removeDelimeterFrom(String str) {
		Document doc = Jsoup.parse(str);
		replaceTag(doc.children());
		String[] infos = Jsoup.parse(doc.toString()).text().split("/");
		return removeInvalidString(infos);
	}

	public String getImage(String content) {
		int start = content.indexOf("/image/");
		if (start < 0)
			return "";
		String remainString = content.substring(start, content.length());
		int end = remainString.indexOf("<");
		return remainString.substring(0, end).startsWith("/")
				? "https://myanmar.gov.mm" + remainString.substring(0, end)
				: remainString.substring(0, end);
	}

	public String getDocumentImage(String content) {
		int start = content.indexOf("/document");
		if (start < 0)
			return "";
		String remainString = content.substring(start, content.length());
		int end = remainString.indexOf("]]");
		return remainString.substring(0, end).startsWith("/")
				? "https://myanmar.gov.mm" + remainString.substring(0, end)
				: remainString.substring(0, end);
	}

	public String getHttpImage(String content) {
		int start = content.indexOf("http");
		if (start < 0)
			return "";
		String remainString = content.substring(start, content.length());
		int end = remainString.indexOf("]]");
		return remainString.substring(0, end).startsWith("/")
				? "https://myanmar.gov.mm" + remainString.substring(0, end)
				: remainString.substring(0, end);
	}

	public String getHttpImage2(String content) {
		int start = content.indexOf("http");
		if (start < 0)
			return "";
		String remainString = content.substring(start, content.length());
		int end = remainString.indexOf("\"");
		return remainString.substring(0, end).startsWith("/")
				? "https://myanmar.gov.mm" + remainString.substring(0, end)
				: remainString.substring(0, end);
	}

	public String convertEntryListToString(List<String> entryList,
			String input) {
		int index = Integer.parseInt(input);
		int lastIndex = (entryList.size() - 1) - (index * 10 - 10);
		int substract = lastIndex < 9 ? lastIndex : 9;
		int startIndex = lastIndex - substract;

		String info = "";
		for (int i = startIndex; i <= lastIndex; i++)
			info += entryList.get(i) + ",";
		return info;
	}

	public String getMyanamrContent(String content) {
		int begin = content.indexOf("\"text_area\"");
		content = content.substring(begin, content.length());
		int end = content.indexOf("</dynamic-content>");
		String remainString = content.substring(end, content.length());

		int mStart = remainString
				.indexOf("<dynamic-content language-id=\"my_MM\">");
		if (mStart > 0) {
			int mEnd = remainString.lastIndexOf("</dynamic-content>");
			return Jsoup.parse(remainString.substring(mStart, mEnd)).text()
					.replaceAll("value 1", "");
		}
		return "";
	}

	public String getEngContent(String content) {
		int begin = content.indexOf("\"text_area\"");
		if (begin < 0)
			return "";
		content = content.substring(begin, content.length());

		int start = content.indexOf("<dynamic-content language-id=\"en_US\">");
		int end = content.indexOf("</dynamic-content>");

		if (start < 0 || end < 0)
			return "";
		return Jsoup.parse(content.substring(start, end)).text()
				.replaceAll("value 1", "");
	}

	public List<Object> bySize(List<Object> entryList, String input) {
		int index = Integer.parseInt(input);
		int lastIndex = (entryList.size() - 1) - (index * 10 - 10);
		int substract = lastIndex < 9 ? lastIndex : 9;
		int startIndex = lastIndex - substract;

		List<Object> objectList = new ArrayList<Object>();
		for (int i = startIndex; i <= lastIndex; i++)
			objectList.add(entryList.get(i));
		return objectList;
	}

	public JSONObject isValidComment(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();
		Object userName = json.get("userName");
		if (userName == null || userName.toString().isEmpty()) {
			resultJson.put("message", "User Name is Empty!");
			resultJson.put("status", "0");
		}

		Object userId = json.get("userId");
		if (userId == null || userId.toString().isEmpty()
				|| Long.parseLong(userId.toString()) == 0) {
			resultJson.put("message", "User Id is Empty!");
			resultJson.put("status", "0");
		}

		Object body = json.get("body");
		if (body == null || body.toString().isEmpty()) {
			resultJson.put("message", "Reply is Empty!");
			resultJson.put("status", "0");
		}

		Object classPK = json.get("classPK");
		if (classPK == null || classPK.toString().isEmpty()) {
			resultJson.put("message", "classPK is Empty!");
			resultJson.put("status", "0");
		}

		return resultJson;
	}

	public JSONObject isValidReply(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();
		Object userName = json.get("userName");
		if (userName == null || userName.toString().isEmpty()) {
			resultJson.put("message", "User Name is Empty!");
			resultJson.put("status", "0");
		}

		Object userId = json.get("userId");
		if (userId == null || userId.toString().isEmpty()
				|| Long.parseLong(userId.toString()) == 0) {
			resultJson.put("message", "User Id is Empty!");
			resultJson.put("status", "0");
		}

		Object body = json.get("body");
		if (body == null || body.toString().isEmpty()) {
			resultJson.put("message", "Reply is Empty!");
			resultJson.put("status", "0");
		}

		Object messageId = json.get("messageId");
		if (messageId == null || messageId.toString().isEmpty()) {
			resultJson.put("message", "Message Id is Empty!");
			resultJson.put("status", "0");
		}

		return resultJson;
	}

	public JSONObject isValidFeedback(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();

		Object name = json.get("name");
		if (name == null || name.toString().isEmpty()) {
			resultJson.put("message", "User Name is Empty!");
			resultJson.put("status", "0");
		}

		Object email = json.get("email");
		if (email == null || email.toString().isEmpty()) {
			resultJson.put("message", "Email is Empty!");
			resultJson.put("status", "0");
		}

		Object contactNo = json.get("contactNo");
		if (contactNo == null || contactNo.toString().isEmpty()) {
			resultJson.put("message", "Contact No is Empty!");
			resultJson.put("status", "0");
		}

		Object feedback = json.get("feedback");
		if (feedback == null || feedback.toString().isEmpty()) {
			resultJson.put("message", "Feedback is Empty!");
			resultJson.put("status", "0");
		}

		return resultJson;
	}

	public JSONObject isValidProfile(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();

		Object name = json.get("name");
		if (name == null || name.toString().isEmpty()) {
			resultJson.put("message", "User Name is Empty!");
			resultJson.put("status", "0");
		}

		Object email = json.get("email");
		if (email == null || email.toString().isEmpty()) {
			resultJson.put("message", "Email is Empty!");
			resultJson.put("status", "0");
		}

		Object contactNo = json.get("contactNo");
		if (contactNo == null || contactNo.toString().isEmpty()) {
			resultJson.put("message", "Contact No is Empty!");
			resultJson.put("status", "0");
		}

		Object feedback = json.get("feedback");
		if (feedback == null || feedback.toString().isEmpty()) {
			resultJson.put("message", "Feedback is Empty!");
			resultJson.put("status", "0");
		}

		return resultJson;
	}

	public static String getMessageDescription(String messageCode) {

		String retMsgDesc = "";
		ArrayList<String> arl = new ArrayList<>();
		Resource resource = new ClassPathResource("cbPay.txt");
		// InputStream inputStream = (InputStream) resource.getInputStream();

		// arl = readList(, false);

		try {
			for (int i = 0; i < arl.size(); i++) {
				if (!arl.get(i).equals("")) {

					if (!arl.get(i).equals("")) {

						if (arl.get(i).split(":")[0]
								.equalsIgnoreCase(messageCode)) {
							System.out.println(
									"msgdesc" + arl.get(i).split(":")[1]);
							retMsgDesc = arl.get(i).split(":")[1];
						}
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retMsgDesc;
	}

	public String getEndDateOfMonth(String date) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date convertedDate = dateFormat.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(convertedDate);
			c.set(Calendar.DAY_OF_MONTH,
					c.getActualMaximum(Calendar.DAY_OF_MONTH));
			return c.getTime().toString().split(" ")[2];
		} catch (ParseException e) {
			logger.error("Error: " + e.getMessage());
		}
		return null;
	}

	public String getEndDateOfYear(String year) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date convertedDate = dateFormat.parse(year + "/12/01");
			Calendar c = Calendar.getInstance();
			c.setTime(convertedDate);
			c.set(Calendar.DAY_OF_MONTH,
					c.getActualMaximum(Calendar.DAY_OF_MONTH));
			return year + "/12/" + c.getTime().toString().split(" ")[2];
		} catch (ParseException e) {
			logger.error("Error: " + e.getMessage());
		}
		return null;
	}

	public String parseMonthToInt(String month) {
		switch (month) {
			case "Jan" :
				return "01";
			case "Feb" :
				return "02";
			case "Mar" :
				return "03";
			case "Apr" :
				return "04";
			case "May" :
				return "05";
			case "June" :
				return "06";
			case "July" :
				return "07";
			case "Aug" :
				return "08";
			case "Sep" :
				return "09";
			case "Oct" :
				return "10";
			case "Nov" :
				return "11";
			case "Dec" :
				return "12";
		}
		return "-1";
	}

	public String dateTimeFormat() {
		DateTimeFormatter dateFormat = DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dateFormat.format(now);
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

	

	public void writeSheet(XSSFWorkbook workbook, String startDate, String endDate) {
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
					"á€™á€°á€•á€­á€¯á€„á€ºá€�á€½á€„á€·á€º á€‰á€®á€¸á€…á€®á€¸á€Œá€¬á€”\r\n" + "(MD-0001) \r\n" + "á€›á€¯á€¶á€¸á€¡á€™á€¾á€�á€º (á�…á�‚)\r\n" + "á€”á€±á€•á€¼á€Šá€ºá€�á€±á€¬á€º", (short) 10);
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

}
