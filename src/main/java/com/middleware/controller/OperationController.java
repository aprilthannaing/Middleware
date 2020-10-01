package com.middleware.controller;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.CBPaytransaction;
import com.middleware.entity.Result;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.User;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;
import com.middleware.entity.paymenttransaction;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.PaymentTransactionService;
import com.middleware.service.UserService;
import com.middleware.service.VisaService;
import com.middleware.service.VisaTransactionService;

@RestController
@RequestMapping("operation")
public class OperationController {

	@Autowired
	private PaymentTransactionService paymnentService;

	@Autowired
	private CBPaymentTransactionService cbpaymentService;

	@Autowired
	private VisaService visaService;

	@Autowired
	private VisaTransactionService visaTransactionService;

	@Autowired
	private UserService userService;

	private static Logger logger = Logger.getLogger(OperationController.class);

	@RequestMapping(value = "saveTransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveMPUPayment(@RequestBody JSONObject json)throws Exception {
		Result result = new Result();
		paymenttransaction paymentdata = new paymenttransaction();
		paymentdata.setMerchantID(json.get("merchantID").toString());
		paymentdata.setAmount(json.get("amount").toString());
		paymentdata.setInvoiceNo(json.get("invoiceNo").toString());
		paymentdata.setLink(json.get("link").toString());// mpu link
		paymentdata.setLink(json.get("link").toString());//mpu link
		result  = paymnentService.saveMPUPayment(paymentdata);
		return result;
	}

	@RequestMapping(value = "saveCBPaytransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveCBPaytransaction(@RequestBody CBPaytransaction json) throws Exception {
		Result result = new Result();
		CBPaytransaction cbpaydata = cbpaymentService.checkTransRef(json.getTransRef());
		if (cbpaydata != null) {
			cbpaydata.setTransStatus(json.getTransStatus());
			result = cbpaymentService.savecbpayment(cbpaydata);
		} else
			result = cbpaymentService.savecbpayment(json);
		return result;
	}

	private User createNewUser(JSONObject json) {

		User user = new User();
		user.setId(SystemConstant.BOID_REQUIRED);
		user.setName(json.get("userName").toString());
		user.setEmail(json.get("email").toString());
		user.setPhoneNo(json.get("phoneNo").toString());
		user.setPaymentdescription(json.get("Paymentdescription").toString());

		return user;
	}

	@RequestMapping(value = "saveVisa", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String saveVisa(@RequestBody JSONObject json) throws ServiceUnavailableException {

		User user = createNewUser(json);
		userService.save(user);

		VisaTransaction visaTransaction = new VisaTransaction();
		visaTransaction.setId(SystemConstant.BOID_REQUIRED);
		visaTransaction.setGatewayEntryPoint(json.get("gatewayEntryPoint").toString());
		visaTransaction.setAmount(
				json.get("amount").toString().isEmpty() ? 0 : Double.parseDouble(json.get("amount").toString()));
		visaTransaction.setCurrency(json.get("currency").toString());
		visaTransaction.setTransactionId(json.get("transactionId").toString());
		visaTransaction.setReceipt(json.get("receipt").toString());
		visaTransaction.setSource(json.get("source").toString());
		visaTransaction.setTaxAmount(json.get("taxAmount").toString());
		visaTransaction.setTerminal(json.get("terminal").toString());
		visaTransaction.setType(json.get("type").toString());
		visaTransaction.setVersion(
				json.get("version").toString().isEmpty() ? 0 : Integer.parseInt(json.get("version").toString()));
		visaTransactionService.save(visaTransaction);

		Visa visa = new Visa();
		visa.setId(SystemConstant.BOID_REQUIRED);
		visa.setMerchantId(json.get("merchantId").toString());
		visa.setMerchantCategoryCode(json.get("merchantCategoryCode").toString());
		visa.setOrderId(json.get("orderId").toString().isEmpty() ? 0 : Long.parseLong(json.get("orderId").toString()));
		visa.setCurrency(json.get("currency").toString());
		visa.setDescription(json.get("description").toString());
		visa.setCustomerName(json.get("customerName").toString());

		visa.setCreationTime(json.get("creationTime").toString());
		visa.setCustomerOrderDate(json.get("customerOrderDate").toString());
		visa.setDeviceType(json.get("deviceType").toString());
		visa.setIpAddress(json.get("ipAddress").toString());
		visa.setResult(json.get("result").toString());
		visa.setBrand(json.get("brand").toString());
		visa.setExpiryMonth(json.get("expiryMonth").toString());
		visa.setExpiryYear(json.get("expiryYear").toString());
		visa.setFundingMethod(json.get("fundingMethod").toString());
		visa.setIssuer(json.get("issuer").toString());
		visa.setNameOnCard(json.get("nameOnCard").toString());
		visa.setNumber(json.get("number").toString());
		visa.setScheme(json.get("scheme").toString());
		visa.setStoredOnFile(json.get("storedOnFile").toString());
		visa.setType(json.get("type").toString());
		visa.setStatus(json.get("status").toString());
		visa.setTotalAuthorizedAmount(json.get("totalAuthorizedAmount").toString());
		visa.setTotalCapturedAmount(json.get("totalCapturedAmount").toString());
		visa.setTotalRefundedAmount(json.get("totalRefundedAmount").toString());
		visa.setVisaTransaction(visaTransaction);
		visa.setUser(user);
		visaService.save(visa);
		return "Saved successfully";
	}
}
