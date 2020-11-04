package com.middleware.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.entity.AES;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.entity.SessionStatus;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Transaction;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.GeneralService;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.SessionService;
import com.middleware.service.VisaService;

@RestController
@RequestMapping("payments")
public class WipoEndPonintsController extends AbstractController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private MPUPaymentTransactionService mpuService;

    @Autowired
    private CBPaymentTransactionService cbPayService;

    @Autowired
    private VisaService visaService;

    private static Logger logger = Logger.getLogger(WipoEndPonintsController.class);

    /*
     * request ------- { "transaction": { “transactionId”:” 00906988547578B”, //
     * Generated by payment gateway "bankIdentifier":" ACLEDA",
     * "currencyType":"KHR", "amount":"250", "amountDetails": [ { "amount": 150.00,
     * "description": "1*150.00", "feeCode": "1001" }, { "amount": 100.00,
     * "description": "2*50.00", " feeCode ": "1002" } ],
     * 
     * "paymentReference": "020170808001234B", "paymentNote":
     * "IP Indonesia Associates", "payer":{ “email” : "john@abc.com",
     * "phone":”1234567” } } }
     */

    /*
     * response -------
     * 
     * { "transaction": { "bankIdentifier":" ACLEDA", "amount":"250",
     * "paymentReference": "020170808001234B",
     * "tokenId”: "2903e6c8-21b5-4e28-851b-4df2073e4095",
     * "transactionDate”: "2017-08-08T13:09" }, "errors”: { “code”:””,
     * “bankCode”:”2052”, “bankDetails”:”” }, "redirectHTML":"" }
     */

    private List<JSONObject> ObjectListToJSONObjectList(List<Object> amountDetails) {
	ObjectMapper mapper = new ObjectMapper();

	List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
	for (Object object : amountDetails) {
	    JSONObject payerEmailObject = mapper.convertValue(object, JSONObject.class);
	    jsonObjectList.add(payerEmailObject);
	}
	return jsonObjectList;

    }

    @RequestMapping(value = "", method = RequestMethod.POST) /* WIPO REST end point 2.3 */
    @ResponseBody
    @JsonView(Views.Summary.class)
    public JSONObject payments(@RequestBody JSONObject json) throws Exception {
	JSONObject resultJson = new JSONObject();
	Object paymentReference = json.get("paymentReference");
	if (paymentReference == null || paymentReference.toString().isEmpty()) {
	    resultJson.put("errorMsg", "Payment Reference must not empty!");
	    return resultJson;
	}

	Session session = new Session();
	session.setId(SystemConstant.BOID_REQUIRED);
	String sessionId = generalService.generateSession(session.getId());
	session.setSessionId(sessionId);
	session.setPaymentReference(paymentReference.toString());

	Object transactionId = json.get("transactionId");
	session.setTransactionId(
		transactionId != null && !transactionId.toString().isEmpty() ? transactionId.toString() : "");

	Object bankIdentifier = json.get("bankIdentifier");
	session.setBankIdentifier(
		bankIdentifier != null && !bankIdentifier.toString().isEmpty() ? bankIdentifier.toString() : "");

	Object currencyType = json.get("currencyType");
	session.setCurrencyType(
		currencyType != null && !currencyType.toString().isEmpty() ? currencyType.toString() : "");

	Object totalAmount = json.get("amount");
	session.setTotalAmount(totalAmount != null && !totalAmount.toString().isEmpty() ? totalAmount.toString() : "");

	Object paymentNote = json.get("paymentNote");
	session.setPaymentNote(paymentNote != null && !paymentNote.toString().isEmpty() ? paymentNote.toString() : "");

	Object payerEmail = json.get("payer");
	ObjectMapper mapper = new ObjectMapper();
	JSONObject payerEmailObject = mapper.convertValue(payerEmail, JSONObject.class);
	session.setPayerEmail(payerEmailObject.get("email").toString());
	session.setPayerPhone(payerEmailObject.get("phone").toString());

	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();
	session.setStartDate(dateFormat.format(now));

	List<Object> amountDetailObjectList = (List<Object>) json.get("amountDetails");
	session.setAmount1(ObjectListToJSONObjectList(amountDetailObjectList).get(0).get("amount").toString());
	session.setAmount2(ObjectListToJSONObjectList(amountDetailObjectList).get(1).get("amount").toString());
	session.setAmountDescription1(
		ObjectListToJSONObjectList(amountDetailObjectList).get(1).get("description").toString());
	session.setAmountDescription2(
		ObjectListToJSONObjectList(amountDetailObjectList).get(1).get("description").toString());
	session.setSessionStatus(SessionStatus.ACTIVE);
	sessionService.save(session);

	Transaction transaction = new Transaction();
	transaction.setBankIdentifier(session.getBankIdentifier());
	transaction.setAmount(session.getTotalAmount());
	transaction.setPaymentReference(session.getPaymentReference());
	transaction.setTokenId(sessionId);
	transaction.setTransactionDate(session.getStartDate());

	JSONObject errors = new JSONObject();
	errors.put("code", "");
	errors.put("bankCode", "2052");
	errors.put("bankDetails", "");

	resultJson.put("transaction", transaction);
	resultJson.put("errors", errors);

	String encryptValue = AES.encrypt(sessionId + "", secretKey);
	resultJson.put("redirectHTML", "localhost:4200/home/" + encryptValue);
	return resultJson;
    }

    /*
     * request -------{ "tokenId": "77MTv5xDWMlFMNe+utqDCERcqkI=",
     * "paymentReference": "123" }
     */

    /*
     * response -------- { "transaction": { "bankIdentifier":" ACLEDA", //
     * compulsory "amount":"250", // compulsory "paymentReference":
     * "020170808001234B", // compulsory "tokenId”: "2903e6c8-4df2073e4095", //
     * compulsory "transactionDate”: "2017-08-08T12:08", // compulsory
     * "paymentConfirmationDate": "2017-08-08T13:08", // compulsory
     * "paymentStatus":"1", // compulsory “receiptNumber”: ”20181203000017”
     * //Optional }, // error operation code and details which may provide more
     * specific information "errors”: { “code”:””, “bankCode”:”2052”,
     * “bankDetails”:”” } }
     */

    @RequestMapping(value = "status", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public JSONObject paymentStatus(@RequestBody JSONObject json) {
	JSONObject resultJson = new JSONObject();
	Object paymentReferenceObject = json.get("paymentReference");
	if (paymentReferenceObject == null || paymentReferenceObject.toString().isEmpty()) {
	    resultJson.put("errorMsg", "Payment Reference must not empty!");
	    return resultJson;
	}

	Object tokenIdObject = json.get("tokenId");
	if (tokenIdObject == null || tokenIdObject.toString().isEmpty()) {
	    resultJson.put("errorMsg", "Token Id must not empty!");
	    return resultJson;
	}

	String paymentReference = paymentReferenceObject.toString();
	String tokenId = tokenIdObject.toString();
	Session session = sessionService.findByPaymentReferenceAndTokenId(paymentReference, tokenId);
	if (session == null) {
	    resultJson.put("errorMsg", "Something wrong in your request!");
	    return resultJson;
	}

	Transaction transaction = new Transaction();
	transaction.setBankIdentifier(session.getBankIdentifier());
	transaction.setAmount(session.getTotalAmount());
	transaction.setPaymentReference(session.getPaymentReference());
	transaction.setTokenId(session.getSessionId());
	transaction.setTransactionDate(session.getStartDate());
	transaction.setPaymentConfirmationDate(session.getPaymentConfirmationDate());

	switch (session.getPaymentType()) {
	case MPU:
	    MPUPaymentTransaction mpu = mpuService.findByTokenId(tokenId);
	    transaction.setPaymentStatus(mpu.isApproved() ? "1" : "0");
	    transaction.setReceiptNumber("");
	    break;

	case CBPAY:
	    CBPayTransaction cbPay = cbPayService.findByTokenId(tokenId);
	    transaction.setPaymentStatus(cbPay.isSuccess() ? "1" : "0");
	    transaction.setReceiptNumber("");
	    break;
	case VISA:
	    Visa visa = visaService.findByTokenId(tokenId);
	    transaction.setPaymentStatus(visa.isSuccess() ? "1" : "0");
	    transaction.setReceiptNumber(visa.getVisaTransaction().getReceipt());
	    break;

	}
	
	JSONObject errors = new JSONObject();
	errors.put("code", "");
	errors.put("bankCode", "2052");
	errors.put("bankDetails", "");

	resultJson.put("transaction", transaction);
	resultJson.put("errors", errors);
	return resultJson;
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public JSONObject checkingUser(@RequestBody JSONObject json) throws Exception {
	JSONObject res = new JSONObject();
	Object requestIdObject = json.get("id");
	if (requestIdObject == null || requestIdObject.toString().isEmpty()) {
	    res.put("code", "0001");
	    res.put("Description", "SessionId is empty");
	    return res;
	}

	String requestId = requestIdObject.toString();
	String id = AES.decrypt(requestId, secretKey);
	Session session = sessionService.checkingSession(id);
	if (session != null) {
	    res.put("code", "0000");
	    res.put("Description", "Success");
	    res.put("userObj", session);
	} else {
	    res.put("code", "0001");
	    res.put("Description", "User not Found!");
	}
	return res;
    }

    @RequestMapping(value = "equalOrNot", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public Result TestData(@RequestBody String stringToTest) {
	Result result = new Result();
	result.setResult(new AES().decrypt(stringToTest, secretKey));
	return result;

    }

    @RequestMapping(value = "Message", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public String Response() {
	String message = getMessageDescription("reqId");
	return message;
    }

}
