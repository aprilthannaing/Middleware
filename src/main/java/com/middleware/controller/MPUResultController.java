package com.middleware.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Views;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.SessionService;

@RestController
@RequestMapping("")
public class MPUResultController {

	@Autowired
	private MPUPaymentTransactionService paymnentService;

	@Autowired
	private SessionService sessionService;

	@Value("${frondEndURL}")
	private String frondEndURL;

	private static Logger logger = Logger.getLogger(MPUResultController.class);

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String getFrontEndURL(@RequestParam("merchantID") String merchantID, @RequestParam("respCode") String respCode, @RequestParam("pan") String pan, @RequestParam("amount") String amount, @RequestParam("invoiceNo") String invoiceNo, @RequestParam("tranRef") String tranRef, @RequestParam("approvalCode") String approvalCode, @RequestParam("dateTime") String dateTime, @RequestParam("status") String status, @RequestParam("failReason") String failReason, @RequestParam("userDefined1") String userDefined1, @RequestParam("userDefined2") String userDefined2, @RequestParam("userDefined3") String userDefined3, @RequestParam("categoryCode") String categoryCode, @RequestParam("hashValue") String hashValue) throws Exception {
		logger.info(" Calling Front End Redirect ...........");
		logger.info(" merchantID " + merchantID);
		logger.info(" respCode " + respCode);
		logger.info(" pan " + pan);
		logger.info(" invoiceNo " + invoiceNo);
		logger.info(" tranRef " + tranRef);
		logger.info(" dateTime " + dateTime);
		logger.info(" status " + status);
		logger.info(" failReason " + failReason);
		logger.info(" userDefined1 " + userDefined1);
		logger.info(" userDefined2 " + userDefined2);
		logger.info(" userDefined3 " + userDefined3);
		logger.info(" categoryCode " + categoryCode);
		logger.info(" hashValue " + hashValue);
		logger.info(" amount " + amount);
		String redirectUrl = "";
		Session session = sessionService.findBySessionId(userDefined1.trim());

		switch (status) {
		case "AP":
			redirectUrl = frondEndURL + "?id=" + userDefined1.trim() + "&success";
			session.setPaymentStatus(1);
			break;
		case "PR":
			redirectUrl = frondEndURL + "?id=" + userDefined1.trim();
			session.setPaymentStatus(-1);
			break;
		default:
			redirectUrl = frondEndURL + "?id=" + userDefined1.trim();
			session.setPaymentStatus(0);
			break;

		}

		sessionService.save(session);
		return "<link rel=\\\"stylesheet\\\" href=\\\"https://fonts.googleapis.com/icon?family=Material+Icons\\\"><link href=\\\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\\\" rel=\\\"stylesheet\\\" crossorigin=\\\"anonymous\\\"><!doctype html><html lang=\\\"en\\\"><head><meta charset=\\\"utf-8\\\"> <title>WIPO File</title> <base href=\\\"/\\\"> <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1\\\"> <link rel=\\\"icon\\\" type=\\\"image/x-icon\\\" href=\\\"favicon.ico\\\"></head><body> <script>window.location.replace" + "('" + redirectUrl + "')</script></body></html>";

	}

	@RequestMapping(value = "/backEndRedirect", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String getBackEndURL(@RequestParam("merchantID") String merchantID, @RequestParam("respCode") String respCode, @RequestParam("pan") String pan, @RequestParam("amount") String amount, @RequestParam("invoiceNo") String invoiceNo, @RequestParam("tranRef") String tranRef, @RequestParam("approvalCode") String approvalCode, @RequestParam("dateTime") String dateTime, @RequestParam("status") String status, @RequestParam("failReason") String failReason, @RequestParam("userDefined1") String userDefined1, @RequestParam("userDefined2") String userDefined2, @RequestParam("userDefined3") String userDefined3, @RequestParam("categoryCode") String categoryCode, @RequestParam("hashValue") String hashValue) throws Exception {

		logger.info(" Calling Back End Redirect ...........");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] dateString = dtf.format(now).toString().split(" ");

		Result result = new Result();
		MPUPaymentTransaction paymentdata = new MPUPaymentTransaction();
		paymentdata.setTranID(SystemConstant.BOID_REQUIRED);
		paymentdata.setMerchantID(merchantID);
		paymentdata.setRespCode(respCode);
		paymentdata.setPan(pan);
		paymentdata.setAmount(amount);
		paymentdata.setInvoiceNo(invoiceNo);
		paymentdata.setTranRef(tranRef);
		paymentdata.setApprovalCode(approvalCode);
		paymentdata.setDateTime(dateTime);
		paymentdata.setStatus(status);
		paymentdata.setFailReason(failReason);
		paymentdata.setUserDefined1(userDefined1);
		paymentdata.setUserDefined2(userDefined2);
		paymentdata.setUserDefined3(userDefined3);
		paymentdata.setCategoryCode(categoryCode);
		paymentdata.setCreationDate(dateString[0] + "T" + dateString[1]);
		Session session = sessionService.findByUserId(userDefined1);
		session.setPaymentConfirmationDate(dateString[0] + "T" + dateString[1]);
		session.setPaymentStatus(1);
		sessionService.save(session);

		if (session != null)
			paymentdata.setSession(session);
		result = paymnentService.saveMPUPayment(paymentdata);
		logger.info("BackEndURL Save Data :" + result.getDescription());
		return "success";
	}
}
