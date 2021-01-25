package com.middleware.controller;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.SessionService;
import com.middleware.service.VisaService;
import com.middleware.service.VisaTransactionService;

@RestController
@RequestMapping("operation")
public class OperationController extends AbstractController {

	@Autowired
	private MPUPaymentTransactionService paymnentService;

	@Autowired
	private CBPaymentTransactionService cbpaymentService;

	@Autowired
	private VisaService visaService;

	@Autowired
	private VisaTransactionService visaTransactionService;

	@Autowired
	private SessionService sessionService;

	private static Logger logger = Logger.getLogger(OperationController.class);

	@RequestMapping(value = "saveTransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveMPUPayment(@RequestBody JSONObject json) throws Exception {
		Result result = new Result();
		MPUPaymentTransaction paymentdata = new MPUPaymentTransaction();
		paymentdata.setMerchantID(json.get("merchantID").toString());
		paymentdata.setAmount(json.get("amount").toString());
		paymentdata.setInvoiceNo(json.get("invoiceNo").toString());
		paymentdata.setLink(json.get("link").toString());// mpu link
		result = paymnentService.saveMPUPayment(paymentdata);
		return result;
	}

	@RequestMapping(value = "saveCBPaytransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result saveCBPaytransaction(@RequestBody CBPayTransaction json) throws Exception {
		Result result = new Result();
		CBPayTransaction cbpaydata = cbpaymentService.checkTransRef(json.getTransRef());
		if (cbpaydata != null) {
			cbpaydata.setCheckedDateTime(dateTimeFormat());
			cbpaydata.setTransStatus(json.getTransStatus());
			result = cbpaymentService.savecbpayment(cbpaydata);
			// if(json.getTransStatus().equals("S"))
			// new WipoEndPonintsController().paymentStatus(json);
		} else {
			json.setCreatedDateTime(dateTimeFormat());
			Session session = sessionService.checkingSession(json.getSessionId());
			if (session != null)
				json.setSession(session);
			result = cbpaymentService.savecbpayment(json);
		}
		return result;
	}

	@RequestMapping(value = "saveVisa", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	@CrossOrigin(origins = "*")
	public JSONObject saveVisa(@RequestBody JSONObject json) throws ServiceUnavailableException {
		JSONObject jsonRes = new JSONObject();
		Object sessionId = json.get("sessionId");
		if (sessionId == null || sessionId.toString().isEmpty()) {
			jsonRes.put("", "Cannot Save");
			return jsonRes;
		}
		Session session = sessionService.findBySessionId(sessionId.toString());
		if (session != null)
			sessionService.save(session);

		VisaTransaction visaTransaction = new VisaTransaction();
		visaTransaction.setId(SystemConstant.BOID_REQUIRED);
		visaTransaction.setAmount(json.get("amount").toString());
		visaTransaction.setCurrency(json.get("currency").toString());
		visaTransaction.setTransactionId(json.get("transactionId").toString());
		visaTransaction.setReceipt(json.get("receipt").toString());
		visaTransaction.setTaxAmount(json.get("taxAmount").toString());
		visaTransaction.setType(json.get("type").toString());
		visaTransaction.setVersion(json.get("version").toString().isEmpty() ? 0 : Integer.parseInt(json.get("version").toString()));
		//visaTransaction.setAcquirerMessage(json.get("acquirerMessage").toString());
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
		visa.setResult(json.get("result").toString());
		visa.setExpiryMonth(json.get("expiryMonth").toString());
		visa.setExpiryYear(json.get("expiryYear").toString());
		visa.setFundingMethod(json.get("fundingMethod").toString());
		visa.setIssuer(json.get("issuer").toString());
		visa.setNameOnCard(json.get("nameOnCard").toString());
		visa.setType(json.get("type").toString());
		visa.setStatus(json.get("status").toString());
		visa.setTotalAuthorizedAmount(json.get("totalAuthorizedAmount").toString());
		visa.setTotalCapturedAmount(json.get("totalCapturedAmount").toString());
		visa.setTotalRefundedAmount(json.get("totalRefundedAmount").toString());
		visa.setVisaTransaction(visaTransaction);
		visa.setSession(session);
		visaService.save(visa);
		jsonRes.put("message", "Save SuccessFully");
		return jsonRes;
	}

}
