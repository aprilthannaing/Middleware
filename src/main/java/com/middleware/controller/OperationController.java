package com.middleware.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("operation")
public class OperationController {
	@Autowired
	private PaymentTransactionService paymnentService;
	
	@RequestMapping(value = "saveTransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveTransaction(@RequestParam("merchantID") String merchantID, 
			@RequestParam("amount") String amount,
			@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("link") String link)throws Exception {
		Result result = new Result();
		paymenttransaction paymentdata = new paymenttransaction();
		paymentdata.setMerchantID(merchantID);
		paymentdata.setAmount(amount);
		paymentdata.setInvoiceNo(invoiceNo);
		paymentdata.setLink(link);//mpu link
		result  = paymnentService.savepayment(paymentdata);
		return result;
	}
}
