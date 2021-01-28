
package com.middleware.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.middleware.entity.AmountDetails;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.EntityStatus;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Payer;
import com.middleware.entity.PaymentType;
import com.middleware.entity.Session;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Transaction;
import com.middleware.entity.Views;
import com.middleware.entity.Visa;
import com.middleware.service.CBPaymentTransactionService;
import com.middleware.service.MPUPaymentTransactionService;
import com.middleware.service.SessionService;
import com.middleware.service.VisaService;

@RestController
@RequestMapping("payments")
public class WipoEndPonintsController extends AbstractController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private MPUPaymentTransactionService mpuService;

	@Autowired
	private CBPaymentTransactionService cbPayService;

	@Autowired
	private VisaService visaService;

	@Value("${frondEndURL}")
	private String frondEndURL;

	@Value("${SERVICECHARGES}")
	private String SERVICECHARGES;

	private static Logger logger = Logger.getLogger(WipoEndPonintsController.class);

	/*
	 * response -------
	 * 
	 * { "transaction": { "bankIdentifier":" ACLEDA", "amount":"250",
	 * "paymentReference": "020170808001234B",
	 * "tokenIdâ€�: "2903e6c8-21b5-4e28-851b-4df2073e4095",
	 * "transactionDateâ€�: "2017-08-08T13:09" }, "errorsâ€�: { â€œcodeâ€�:â€�â€�,
	 * â€œbankCodeâ€�:â€�2052â€�, â€œbankDetailsâ€�:â€�â€� }, "redirectHTML":"" }
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

	/*
	 * { "requestorId":"WFT2020000447/Daw Zar Chi Htwe", "transaction":{
	 * "amount":150000.0, "bankIdentifier":"MM"," currencyType":"MMK",
	 * "paymentNote":"WIPOFile", "paymentReference":"D102020111900022",
	 * "transactionId":"45869391831927335" } }
	 */
	private Session getSession(JSONObject json, Transaction transaction) throws ServiceUnavailableException {
		Payer payer = transaction.getPayer();
		List<AmountDetails> amountDetails = transaction.getAmountDetails();

		Session session = new Session();
		session.setId(SystemConstant.BOID_REQUIRED);
		String[] requestorId = json.get("requestorId").toString().split("/");
		session.setRequestorId(requestorId[0]);
		session.setPayerName(requestorId[1]);
		session.setPaymentReference(transaction.getPaymentReference());
		session.setTransactionId(transaction.getTransactionId());
		session.setBankIdentifier(transaction.getBankIdentifier());
		session.setCurrencyType(transaction.getCurrencyType());
		session.setTotalAmount(transaction.getAmount());
		session.setPaymentNote(transaction.getPaymentNote());
		session.setPayerEmail(payer.getEmail());
		session.setPayerPhone(payer.getPhone());
		session.setPaymentStatus(0);
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String[] dateString = dateFormat.format(now).toString().split(" ");
		session.setStartDate(dateString[0] + "T" + dateString[1]);
		if (!CollectionUtils.isEmpty(amountDetails)) {
			session.setAmount1(amountDetails.get(0).getAmount() + "");
			session.setAmount2(amountDetails.get(1).getAmount() + "");
			session.setAmountDescription1(amountDetails.get(0).getDescription());
			session.setAmountDescription2(amountDetails.get(1).getDescription());
		}

		session.setEntityStatus(EntityStatus.ACTIVE);
		session.setEndDate(dateTimeFormat());
		session.setServiceCharges(SERVICECHARGES);
		// final Amount
		long finalAmt = 0;
		long serviceCharges = Long.parseLong(SERVICECHARGES);
		String totalAmount = transaction.getAmount().substring(0, transaction.getAmount().indexOf("."));
		long totalAmount1 = Long.parseLong(totalAmount);
		finalAmt = totalAmount1 + serviceCharges;
		session.setFinalAmount(finalAmt + "");
		sessionService.save(session);
		return session;
	}

	@RequestMapping(value = "", method = RequestMethod.POST) /*
																 * WIPO REST end point 2.3 (save session with
																 * transaction information, session id is token id.)
																 */
	@ResponseBody
	@JsonView(Views.Thin.class)
	public JSONObject payments(@RequestBody JSONObject json) throws Exception {
		JSONObject resultJson = new JSONObject();
		JSONObject errors = new JSONObject();

		Object requestorId = json.get("requestorId");
		Object transactionObject = json.get("transaction");
		ObjectMapper mapper = new ObjectMapper();
		Transaction transaction = mapper.convertValue(transactionObject, Transaction.class);

		if (transaction == null || transaction.getTransactionId() == null || transaction.getTransactionId().isEmpty()
				|| transaction.getPaymentReference() == null || transaction.getPaymentReference().isEmpty()
				|| transaction.getBankIdentifier() == null || transaction.getBankIdentifier().isEmpty()) {
			errors.put("code", "-3");
			errors.put("bankCode", "24503");
			errors.put("bankDetails", "unknown payment Transaction Identifier or Payment reference.");
			resultJson.put("errors", errors);
			return resultJson;
		}

		Payer payer = transaction.getPayer();
		List<AmountDetails> amountDetails = transaction.getAmountDetails(); // CollectionUtils.isEmpty(amountDetails) ||
		if (payer == null || requestorId == null || requestorId.toString().isEmpty()) {
			errors.put("code", "-3");
			errors.put("bankCode", "24502");
			errors.put("bankDetails", " unauthorized user credentials");
			resultJson.put("errors", errors);
			return resultJson;
		}

		Object paymentNote = transaction.getPaymentNote();
		Object amount = transaction.getAmount();
		Object currency = transaction.getCurrencyType();
		if (paymentNote == null || paymentNote.toString().isEmpty() || amount == null || amount.toString().isEmpty()
				|| currency == null || currency.toString().isEmpty()) {
			errors.put("code", "-3");
			errors.put("bankCode", "24501");
			errors.put("bankDetails", "unauthorized bank credentials");
			resultJson.put("errors", errors);
			return resultJson;
		}

		Session session = getSession(json, transaction);
		transaction.setTransactionDate(session.getStartDate());
		transaction.setTokenId(session.getSessionId());
		resultJson.put("transaction", transaction);
		String redirectHTML = "<link rel=\\\"stylesheet\\\" href=\\\"https://fonts.googleapis.com/icon?family=Material+Icons\\\"><link href=\\\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\\\" rel=\\\"stylesheet\\\" crossorigin=\\\"anonymous\\\"><!doctype html><html lang=\\\"en\\\"><head><meta charset=\\\"utf-8\\\"> <title>WIPO File</title> <base href=\\\"/\\\"> <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1\\\"> <link rel=\\\"icon\\\" type=\\\"image/x-icon\\\" href=\\\"favicon.ico\\\"></head><body> <script>window.location.replace"
				+ "('" + frondEndURL + "?id=" + session.getSessionId() + "')</script></body></html>";
		resultJson.put("redirectHTML", redirectHTML);
		return resultJson;
	}

	private Transaction getTransactionFromSession(Session session) {
		Transaction transaction = new Transaction();
		transaction.setBankIdentifier(session.getBankIdentifier());
		transaction.setAmount(session.getTotalAmount());
		transaction.setPaymentReference(session.getPaymentReference());
		transaction.setTokenId(session.getSessionId());
		transaction.setTransactionDate(session.getStartDate());
		transaction.setPaymentConfirmationDate(session.getPaymentConfirmationDate());

		String tokenId = session.getSessionId();
		PaymentType paymentType = session.getPaymentType();
		if (paymentType == null) {
			transaction.setPaymentStatus(0);
			return transaction;
		}

		switch (paymentType) {
		case MPU:
			MPUPaymentTransaction mpu = mpuService.findByTokenId(tokenId);
			if (mpu == null) {
				return null;
			}
			transaction.setPaymentStatus(mpu.isApproved() ? 1 : session.getPaymentStatus());
			transaction.setReceiptNumber("");
			break;

		case CBPAY:
			CBPayTransaction cbPay = cbPayService.findByTokenId(tokenId);
			if (cbPay == null) {
				return null;
			}
			transaction.setPaymentStatus(cbPay.isSuccess() ? 1 : session.getPaymentStatus());
			transaction.setReceiptNumber("");
			break;
		case VISA:
			Visa visa = visaService.findByTokenId(tokenId);
			if (visa == null) {
				return null;
			}
			transaction.setPaymentStatus(visa.isSuccess() ? 1 : session.getPaymentStatus());
			transaction.setReceiptNumber(visa.getVisaTransaction().getReceipt());
			break;

		}

		return transaction;
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

	@RequestMapping(value = "status", method = RequestMethod.POST) /* rest end point 2.4 */
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject paymentStatus(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();
		JSONObject errors = new JSONObject();
		Object paymentReferenceObject = json.get("paymentReference");
		Object tokenIdObject = json.get("tokenId");

		if (paymentReferenceObject == null || paymentReferenceObject.toString().isEmpty() || tokenIdObject == null
				|| tokenIdObject.toString().isEmpty()) {
			errors.put("code", "-3");
			errors.put("bankCode", "24503");
			errors.put("bankDetails", "unknown payment Transaction Identifier or Payment reference");
			resultJson.put("errors", errors);
			return resultJson;
		}

		String paymentReference = paymentReferenceObject.toString();
		String tokenId = tokenIdObject.toString();
		Session session = sessionService.findByPaymentReferenceAndTokenId(paymentReference, tokenId);
		if (session == null) {
			errors.put("code", "-3");
			errors.put("bankCode", "24503");
			errors.put("bankDetails", "unknown payment Transaction Identifier or Payment reference");
			resultJson.put("errors", errors);
			return resultJson;
		}

		Transaction transaction = getTransactionFromSession(session);
		if (transaction == null) {
			errors.put("code", "-5");
			errors.put("bankCode", "24000");
			errors.put("bankDetails", "bank service unavailable ");
			resultJson.put("errors", errors);
			return resultJson;
		}
		resultJson.put("transaction", transaction);
		return resultJson;
	}

	/*
	 * request like call back url response response --------
	 * 
	 * { "transaction": { "bankIdentifier":" ACLEDA", // compulsory
	 * "paymentReference": "020170808001234B", // compulsory
	 * "tokenId”: "2903e6c8-21b5-4e28-851b-4df2073e4095",// compulsory
	 * "transactionDate”: "2017-08-08T12:08", // compulsory
	 * "paymentConfirmationDate": "2017-08-08T13:08", // compulsory
	 * "paymentStatus":"1", // compulsory “receiptNumber”: ”20181203000017”
	 * //Optional },// error operation code and details which may provide more
	 * specific information "errors”: { “code”:””, “bankCode”:”2052”,
	 * “bankDetails”:”” }
	 * 
	 */
	@RequestMapping(value = "ack", method = RequestMethod.POST) /* rest end point 2.5 */
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject ack(@RequestBody JSONObject json) {
		JSONObject resultJson = new JSONObject();
		JSONObject errors = new JSONObject();

		Object paymentReferenceObject = json.get("paymentReference");
		if (paymentReferenceObject == null || paymentReferenceObject.toString().isEmpty()) {
			errors.put("code", "-3");
			errors.put("bankCode", "24503");
			errors.put("bankDetails", "unknown payment Transaction Identifier or Payment reference");
			resultJson.put("errors", errors);
			return resultJson;
		}

		String paymentReference = paymentReferenceObject.toString();
		Session session = sessionService.findByPaymentReference(paymentReference);
		if (session == null) {
			errors.put("code", "-5");
			errors.put("bankCode", "24503");
			errors.put("bankDetails", "unknown payment Transaction Identifier or Payment reference");
			resultJson.put("errors", errors);
			return resultJson;
		}

		Transaction transaction = getTransactionFromSession(session);
		if (transaction == null) {
			errors.put("code", "-5");
			errors.put("bankCode", "24000");
			errors.put("bankDetails", "bank service unavailable ");
			resultJson.put("errors", errors);
			return resultJson;
		}

		resultJson.put("transaction", transaction);
		return resultJson;
	}

	@RequestMapping(value = "check", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	@CrossOrigin(origins = "*")
	public JSONObject checkingUser(@RequestBody JSONObject json) throws Exception {
		JSONObject res = new JSONObject();
		Object requestIdObject = json.get("id");
		if (requestIdObject == null || requestIdObject.toString().isEmpty()) {
			res.put("code", "0001");
			res.put("Description", "SessionId is empty");
			return res;
		}

		String requestId = requestIdObject.toString();
		Session session = sessionService.checkingSession(requestId);
		if (session != null) {
			// update session
			session.setEndDate(dateTimeFormat());
			if (!json.get("type").toString().equals("")) {
				String paymentType = json.get("type").toString();
				if (paymentType.equals("MPU"))
					session.setPaymentType(PaymentType.MPU);
				else if (paymentType.equals("CBPAY"))
					session.setPaymentType(PaymentType.CBPAY);
				else if (paymentType.equals("VISA"))
					session.setPaymentType(PaymentType.VISA);
				session.setPaymentConfirmationDate(dateTimeFormat());
			}

			sessionService.save(session);

			res.put("code", "0000");
			res.put("Description", "Success");
			res.put("userObj", session);
		} else {
			res.put("code", "0001");
			res.put("Description", "User not Found!");
		}
		return res;
	}

//	@RequestMapping(value = "Message", method = RequestMethod.POST)
//	@ResponseBody
//	@JsonView(Views.Summary.class)
//	public String Response() {
//		String message = getMessageDescription("reqId");
//		return message;
//	}

	private static String serviceUrl = "http://<hostname>:<port-number>/efiling/paymentStatus";

	public void checkPaymentStatus(JSONObject json) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(json);

		logger.info("Request JSON =" + requestJson);
		HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		factory.setHttpClient(httpClient);
		restTemplate.setRequestFactory(factory);

		ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, String.class);

		logger.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		logger.info("response: " + response);
	}

	@RequestMapping(value = "sessionOut", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	@CrossOrigin(origins = "*")
	public JSONObject sessionOut(@RequestBody JSONObject json) throws Exception {
		JSONObject resultJson = new JSONObject();
		Session session = null;
		String id = json.get("id").toString();
		if (!id.equals("") || !id.equals(null)) {
			// id = AES.decrypt(id, secretKey);
			session = sessionService.checkingSession(id);
			logger.info(session);
		}

		if (session == null) {
			resultJson.put("code", "0001");
			resultJson.put("description", "Session Time Out");
		}
		session.setEndDate(dateTimeFormat());
		session.setEntityStatus(EntityStatus.INACTIVE);
		sessionService.save(session);
		resultJson.put("code", "0000");
		resultJson.put("description", "Session Completed");
		resultJson.put("response", session);
		return resultJson;
	}

	@RequestMapping(value = "paymentStatus", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Thin.class)
	@CrossOrigin(origins = "*")
	public JSONObject setPaymentStatus(@RequestBody JSONObject json) throws Exception {
		JSONObject resultJson = new JSONObject();

		Session session = sessionService.findBySessionId(json.get("sessionId").toString());
		session.setPaymentStatus(Integer.parseInt(json.get("paymentStatus").toString()));
		sessionService.save(session);
		
		resultJson.put("status", true);
		return resultJson;
	}

}
