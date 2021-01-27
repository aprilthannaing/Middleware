package com.middleware.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;
import com.middleware.entity.Payment;
import com.middleware.entity.Views;
import com.middleware.service.SessionService;

@RestController
@RequestMapping("api")
public class PaymentController extends AbstractController {

	@Autowired
	private SessionService sessionService;

	private static Logger logger = Logger.getLogger(PaymentController.class);

	/*
	 * {"merchantId" : "204104001305002", "invoiceNo" : "1234567890333",
	 * "currencyCode": 104, "productDesc":"This is Testing", "userDefined1" : "123",
	 * "userDefined2" : "456", "userDefined3" : "789", "amount" : 0000001000000,
	 * "hashValue": "F9BdcXjDTvyYEamlxgFsODJgDjeNOcw+ZAdwVdhWBwU="} *
	 * 
	 * JSONObject requestJson = new JSONObject(); requestJson.put("reqId",
	 * "2d21a5715c034efb7e0aa383b885fc7b"); requestJson.put("merId",
	 * "581500000000017"); requestJson.put("subMerId", "0000000001700001");
	 * requestJson.put("terminalId", "03000001"); requestJson.put("transAmount",
	 * "100"); requestJson.put("transCurrency", "MMK"); requestJson.put("ref1",
	 * "95923535341"); requestJson.put("ref2", "10043553461");
	 */

	public JSONObject checkQR(JSONObject json) throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();

		String url = "https://103.150.78.103:4443/payment-api/v1/qr/check-transaction.service";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authen-Token",
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTY3NzU2NzIsIm1lcklkIjoiNTgxNTAwMDAwMDAwMDE3In0.hO4-eWFQHM5STCydXlwr2SjghmFe_4GgmccBq3vJvUY");
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json.toString());
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			System.out.println("inputLine : " + inputLine);

			response.append(inputLine);
		}
		in.close();

		Gson g = new Gson();
		return g.fromJson(response.toString(), JSONObject.class);
	}

	@ResponseBody
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "checkqr", method = RequestMethod.POST)
	@JsonView(Views.Summary.class)
	public JSONObject checkqr(@RequestBody JSONObject json) throws IOException {
		return checkQR(json);
	}

	public JSONObject generateQR(JSONObject json) throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();

		String url = "https://103.150.78.103:4443/payment-api/v1/qr/generate-transaction.service";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authen-Token",
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTY3NzU2NzIsIm1lcklkIjoiNTgxNTAwMDAwMDAwMDE3In0.hO4-eWFQHM5STCydXlwr2SjghmFe_4GgmccBq3vJvUY");
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json.toString());
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			System.out.println("inputLine : " + inputLine);

			response.append(inputLine);
		}
		in.close();

		Gson g = new Gson();
		return g.fromJson(response.toString(), JSONObject.class);
	}

	@ResponseBody
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "generateqr", method = RequestMethod.POST)
	@JsonView(Views.Summary.class)
	public JSONObject generateqr(@RequestBody JSONObject json) throws IOException {
		return generateQR(json);

	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		byte[] encodedBytes = Base64.encodeBase64("merchant.CB0000000342:a3102e136bdc8eb7d986804dfa533e02".getBytes());
		System.out.println("encodedBytes " + new String(encodedBytes));
		return new String(encodedBytes);
	}

	public static String hmacSha1(String value, String key) {
		try {
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(value.getBytes());

			byte[] hexBytes = new Hex().encode(rawHmac);

			return new String(hexBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "payment", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject getPayment(@RequestBody JSONObject json) throws Exception {
		JSONObject resultJson = new JSONObject();
		String secretKey = "7M8N3SLQ8QILSN6DOZIN1NBOVWMMGIVA";
		String merchantId = json.get("merchantId") != null ? json.get("merchantId").toString() : "";
		String invoiceNo = json.get("invoiceNo") != null ? json.get("invoiceNo").toString() : "";
		String currencyCode = json.get("currencyCode") != null ? json.get("currencyCode").toString() : "";
		String productDesc = json.get("productDesc") != null ? json.get("productDesc").toString() : "";
		String userDefined1 = json.get("userDefined1") != null ? json.get("userDefined1").toString() : "";
		String userDefined2 = json.get("userDefined2") != null ? json.get("userDefined2").toString() : "";
		String userDefined3 = json.get("userDefined3") != null ? json.get("userDefined3").toString() : "";
		String amount = json.get("amount") != null ? json.get("amount").toString() : "";

		String[] eg = new String[] { merchantId, invoiceNo, productDesc, amount, currencyCode, userDefined1,
				userDefined2, userDefined3 };
		Arrays.sort(eg);

		String str = "";
		for (String var : eg) {
			str += var + "";
		}

		String hashValue = hmacSha1(str, secretKey).toUpperCase();
		resultJson.put("hashvalue", hashValue);
		resultJson.put("sortedString", str);
		return resultJson;
	}

	@RequestMapping(value = "payments", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public ResponseEntity<String> getPayments(@RequestBody JSONObject json) throws Exception { // ResponseEntity<String>
		JSONObject resultJson = new JSONObject();
		String secretKey = "T3HJEW4ZIM1YT11ZOZN0CMNIHKEDQDFI";
		String merchantId = json.get("merchantId") != null ? json.get("merchantId").toString() : "";
		String invoiceNo = json.get("invoiceNo") != null ? json.get("invoiceNo").toString() : "";
		String currencyCode = json.get("currencyCode") != null ? json.get("currencyCode").toString() : "";
		String productDesc = json.get("productDesc") != null ? json.get("productDesc").toString() : "";
		String userDefined1 = json.get("userDefined1") != null ? json.get("userDefined1").toString() : "";
		String userDefined2 = json.get("userDefined2") != null ? json.get("userDefined2").toString() : "";
		String userDefined3 = json.get("userDefined3") != null ? json.get("userDefined3").toString() : "";
		String amount = json.get("amount") != null ? json.get("amount").toString() : "";

		String[] eg = new String[] { merchantId, invoiceNo, productDesc, amount, currencyCode, userDefined1,
				userDefined2, userDefined3 };
		Arrays.sort(eg);

		String str = "";
		for (String var : eg) {
			str += var + "";
		}

		String hashValue = hmacSha1(str, secretKey).toUpperCase();
		Payment payment = new Payment();
		payment.setCurrencyCode(Integer.parseInt(currencyCode));
		payment.setInvoiceNo(Long.parseLong(invoiceNo));
		payment.setMerchantId(Long.parseLong(merchantId));
		payment.setProductDesc(productDesc);
		payment.setUserDefined1(userDefined1);
		payment.setUserDefined2(userDefined2);
		payment.setUserDefined3(userDefined3);
		payment.setAmount(amount);
		payment.setHashValue(hashValue);

		resultJson.put("hashvalue", hashValue);

		MPUPaymentRestClient paymentRestClient = new MPUPaymentRestClient();
		// return paymentRestClient.thirdpartyAPI(payment);
		return paymentRestClient.getForm(payment);
	}

	@RequestMapping(value = "cbtransaction", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public String generationTransaction(JSONObject json) throws Exception {
//
//	String reqid = json.get("id").toString();
//	if (reqid.equals("") || reqid.equals(null)) {
//	    resultJson.put("code", "0001");
//	    resultJson.put("Description", "SessionId is empty");
//	    return resultJson;
//	}
//	String id = AES.decrypt(reqid, secretKey);
//	Session session = sessionService.checkingSession(id);
//	logger.info("getAmount from session !!!!!!!!!" + session.getAmount());
//	logger.info("getAmount from session !!!!!!!!!" + session.getCurrency());

//	HttpHeaders headers = new HttpHeaders();
//	headers.add("Content-Type", "application/json");
//	headers.add("Authen-Token",
//		"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTY3NzU2NzIsIm1lcklkIjoiNTgxNTAwMDAwMDAwMDE3In0.hO4-eWFQHM5STCydXlwr2SjghmFe_4GgmccBq3vJvUY");
//
//	JSONObject request = new JSONObject();
//	request.put("reqId", "2d21a5715c034efb7e0aa383b885fc7b"); //
//	request.put("merId", "581500000000017"); //
//	request.put("subMerId", "0000000001700001"); //
//	request.put("terminalId", "03000001"); //
//	request.put("transAmount", session.getAmount());
//	request.put("transCurrency", session.getCurrency());
//	request.put("ref1", "95923535341"); //
//	request.put("ref2", "10043553461"); //
//
//	HttpEntity<String> entityHeader = new HttpEntity<String>(request.toString(), headers);
//	logger.info("Request is: " + entityHeader);
//
//	String url = "https://103.150.78.103:4443/payment-api/v1/qr/generate-transaction.service";
//	logger.info("service url is: " + url);
//
//	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
//	logger.info("calling webservice..." + builder);
//	RestTemplate restTemplate = new RestTemplate();
//	HttpEntity<JSONObject> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST,
//		entityHeader, JSONObject.class);
//	logger.info("response ................" + response.getBody());
//	JSONObject resultJson = new JSONObject();
//	resultJson.put("code", "0000");
//	resultJson.put("merDqrCode", "00020101021250200006cbtest0106cbtest51450006cbtest0131581500000000017000000000170000152045815530310454031005802MM5911CB Test 0016006Hlaing61051105162620111959235353410311100435534610516201021000001946007080300000164210002my0111CB Test 001630424DE");
//	resultJson.put("transRef", "2010210000019460");
		return "success";
	}
}