package com.middleware.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.MailEvent;
import com.middleware.entity.Views;
import com.middleware.service.MailService;

@RestController
@RequestMapping("mail")
public class MailController extends AbstractController {

	@Autowired
	private MailService mailService;

	@Value("${FILEPATH}")
	private String filePath;

	private static Logger logger = Logger.getLogger(MailController.class);

	private String send(String email, String startDate, String endDate) throws Exception, IOException {
		MailEvent mailEvent = new MailEvent();
		mailEvent.setSubject("Transaction Report");
		mailEvent.setContent("Transaction Report of payment");
		mailEvent.setTo(email);

		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(new File(filePath + "TransactionReport.xlsx"));
		writeSheet(workbook, startDate, endDate);

		String newFilePath = filePath + "Payment.xlsx";

		FileOutputStream out = new FileOutputStream(new File(newFilePath));
		workbook.write(out);
		String result = mailService.sendMail(mailEvent);

		if (result.equals("success")) {

			out.close();
			Path path = FileSystems.getDefault().getPath(newFilePath);
			try {
				Files.delete(path);
			} catch (NoSuchFileException x) {
				System.err.format("%s: no such" + " file or directory%n", path);
			} catch (IOException x) {
				System.err.println(x);
			}

			return "success";

		} else
			return "fail";
	}

	private String sendEmail(JSONObject json) throws IOException, Exception {
		String emails = json.get("emails").toString();
		String startDate = getStartDate(json.get("parameter").toString());
		String endDate = getEndDate(json.get("parameter").toString());
		logger.info("StartDate!" + startDate);
		logger.info("endDate!" + endDate);
		if (emails.contains(",")) {
			for (String email : emails.split(",")) {
				logger.info("email!!!!!!!!!!!!!!: " + email);
				String msg = send(email, startDate, endDate);
				if (!msg.equals("success"))
					return msg;
			}
		} else
			return send(emails, startDate, endDate);
		return "success";
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "report", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String sendMail(@RequestBody JSONObject json) throws Exception {
		logger.info("return message!!!!!!!!!!!!!" + sendEmail(json));

		return sendEmail(json);
	}
}
