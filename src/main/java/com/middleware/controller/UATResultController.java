package com.middleware.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Result;
import com.middleware.entity.Views;
import com.middleware.entity.paymenttransaction;
import com.middleware.service.PaymentTransactionService;

@RestController
@RequestMapping("payment")
public class UATResultController {
	@Autowired
	private PaymentTransactionService paymnentService;
	
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
	

	@RequestMapping(value = "/mpu/frontEndRedirect", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject getFrontEndURL(
			@RequestParam("merchantID") String merchantID, 
			@RequestParam("respCode") String respCode,
			@RequestParam("pan") String pan,
			@RequestParam("amount") String amount,
			@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("tranRef") String tranRef,
			@RequestParam("approvalCode") String approvalCode,
			@RequestParam("dateTime") String dateTime,
			@RequestParam("status") String status,
			@RequestParam("failReason") String failReason,
			@RequestParam("userDefined1") String userDefined1,
			@RequestParam("userDefined2") String userDefined2,
			@RequestParam("categoryCode") String categoryCode,
			@RequestParam("hashValue") String hashValue) throws Exception {
		JSONObject resultJson = new JSONObject();
		
		resultJson.put("merchantID",merchantID);
		resultJson.put("respCode",respCode);
		resultJson.put("pan",pan);
		resultJson.put("amount",amount);
		resultJson.put("invoiceNo",invoiceNo);
		resultJson.put("tranRef",tranRef);		
		resultJson.put("approvalCode",approvalCode);
		resultJson.put("dateTime",dateTime);
		resultJson.put("status",status);
		resultJson.put("failReason",failReason);
		resultJson.put("userDefined1",userDefined1);
		resultJson.put("userDefined2",userDefined2);
		resultJson.put("categoryCode",categoryCode);
		resultJson.put("userDefined2",userDefined2);
		return resultJson;
	}
	
	@RequestMapping(value = "/mpu/backEndRedirect", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject getBackEndURL(
			@RequestParam("merchantID") String merchantID, 
			@RequestParam("respCode") String respCode,
			@RequestParam("pan") String pan,
			@RequestParam("amount") String amount,
			@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("tranRef") String tranRef,
			@RequestParam("approvalCode") String approvalCode,
			@RequestParam("dateTime") String dateTime,
			@RequestParam("status") String status,
			@RequestParam("failReason") String failReason,
			@RequestParam("userDefined1") String userDefined1,
			@RequestParam("userDefined2") String userDefined2,
			@RequestParam("categoryCode") String categoryCode,
			@RequestParam("hashValue") String hashValue) throws Exception {
		JSONObject resultJson = new JSONObject();
		
		resultJson.put("merchantID",merchantID);
		resultJson.put("respCode",respCode);
		resultJson.put("pan",pan);
		resultJson.put("amount",amount);
		resultJson.put("invoiceNo",invoiceNo);
		resultJson.put("tranRef",tranRef);		
		resultJson.put("approvalCode",approvalCode);
		resultJson.put("dateTime",dateTime);
		resultJson.put("status",status);
		resultJson.put("failReason",failReason);
		resultJson.put("userDefined1",userDefined1);
		resultJson.put("userDefined2",userDefined2);
		resultJson.put("categoryCode",categoryCode);
		resultJson.put("userDefined2",userDefined2);
		return resultJson;
	}
	
	//save
	@RequestMapping(value = "saveTransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveTransaction(@RequestParam("merchantID") String merchantID, 
			@RequestParam("respCode") String respCode,
			@RequestParam("pan") String pan,
			@RequestParam("amount") String amount,
			@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("tranRef") String tranRef,
			@RequestParam("approvalCode") String approvalCode,
			@RequestParam("dateTime") String dateTime,
			@RequestParam("status") String status,
			@RequestParam("failReason") String failReason,
			@RequestParam("userDefined1") String userDefined1,
			@RequestParam("userDefined2") String userDefined2,
			@RequestParam("userDefined3") String userDefined3,
			@RequestParam("categoryCode") String categoryCode,
			@RequestParam("hashValue") String hashValue) throws Exception {
		Result result = new Result();
		paymenttransaction paymentdata = new paymenttransaction();
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
		result  = paymnentService.savepayment(paymentdata);
		return result;
	}

}
