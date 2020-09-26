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
import com.middleware.entity.CBPaytransaction;
import com.middleware.entity.Result;
import com.middleware.entity.Views;
import com.middleware.entity.paymenttransaction;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.PaymentTransactionService;

@RestController
@RequestMapping("operation")
public class OperationController {
	@Autowired
	private PaymentTransactionService paymnentService;
	
	@Autowired
	private CBPaymentTransactionService cbpaymentService; 
	
	@RequestMapping(value = "saveTransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveTransaction(@RequestBody JSONObject json)throws Exception {
		Result result = new Result();
		paymenttransaction paymentdata = new paymenttransaction();
		paymentdata.setMerchantID(json.get("merchantID").toString());
		paymentdata.setAmount(json.get("amount").toString());
		paymentdata.setInvoiceNo(json.get("invoiceNo").toString());
		paymentdata.setLink(json.get("link").toString());//mpu link
		result  = paymnentService.savepayment(paymentdata);
		return result;
	}
	
	@RequestMapping(value = "saveCBPaytransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveCBPaytransaction(@RequestBody CBPaytransaction json)throws Exception {
		Result result = new Result();
		CBPaytransaction cbpaydata = cbpaymentService.checkTransRef(json.getTransRef());
		if(cbpaydata != null) {
			cbpaydata.setTransStatus(json.getTransStatus());
			result  = cbpaymentService.savecbpayment(cbpaydata);
		}else result  = cbpaymentService.savecbpayment(json);
		return result;
	}
}
