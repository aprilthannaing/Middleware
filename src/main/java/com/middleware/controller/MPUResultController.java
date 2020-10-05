package com.middleware.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Views;
import com.middleware.service.PaymentTransactionService;

@RestController
@RequestMapping("mpu")
public class MPUResultController {

    @Autowired
    private PaymentTransactionService paymnentService;

    private static Logger logger = Logger.getLogger(MPUResultController.class);

//{
//"merchantID":"206104000003467",
//"respCode":"00",
//"pan":"950319xxxxxx6549",
//"amount":"000000020000",
//"invoiceNo":"1598606019873622",
//"tranRef":"4067076",
//"approvalCode":"9XR32H",
//"dateTime":"20200828154547",
//"status":"AP",
//"failReason":"Approved",
//"userDefined1":"100","
//userDefined2":"2","userDefined3":null,
//"categoryCode":null,
//"hashValue":"CEF862727401D0A5BC0B1E8ED684A6C72E695F8B"
//}

    @RequestMapping(value = "/frontEndRedirect", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public void getFrontEndURL(@RequestParam("merchantID") String merchantID, @RequestParam("respCode") String respCode,
	    @RequestParam("pan") String pan, @RequestParam("amount") String amount,
	    @RequestParam("invoiceNo") String invoiceNo, @RequestParam("tranRef") String tranRef,
	    @RequestParam("approvalCode") String approvalCode, @RequestParam("dateTime") String dateTime,
	    @RequestParam("status") String status, @RequestParam("failReason") String failReason,
	    @RequestParam("userDefined1") String userDefined1, @RequestParam("userDefined2") String userDefined2,
	    @RequestParam("userDefined3") String userDefined3, @RequestParam("categoryCode") String categoryCode,
	    @RequestParam("hashValue") String hashValue) throws Exception {
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
    }

    @RequestMapping(value = "/backEndRedirect", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public String getBackEndURL(@RequestParam("merchantID") String merchantID,
	    @RequestParam("respCode") String respCode, @RequestParam("pan") String pan,
	    @RequestParam("amount") String amount, @RequestParam("invoiceNo") String invoiceNo,
	    @RequestParam("tranRef") String tranRef, @RequestParam("approvalCode") String approvalCode,
	    @RequestParam("dateTime") String dateTime, @RequestParam("status") String status,
	    @RequestParam("failReason") String failReason, @RequestParam("userDefined1") String userDefined1,
	    @RequestParam("userDefined2") String userDefined2, @RequestParam("userDefined3") String userDefined3,
	    @RequestParam("categoryCode") String categoryCode, @RequestParam("hashValue") String hashValue)
	    throws Exception {


	logger.info(" Calling Back End Redirect ...........");
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();

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
	paymentdata.setCreationDate(dtf.format(now));
	result = paymnentService.saveMPUPayment(paymentdata);
	logger.info("BackEndURL Save Data :" + result.getDescription());

	return "success";
    }
}
