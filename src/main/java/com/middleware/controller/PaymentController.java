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
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

	@ResponseBody
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "skipssl", method = RequestMethod.POST)
	@JsonView(Views.Thin.class)
	public String skipSSL() throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();
		return "success";
	}

	@ResponseBody
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "mpu", method = RequestMethod.POST)
	@JsonView(Views.Summary.class)
	public ResponseEntity<String> MPU(@RequestBody JSONObject json) throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();

		String url = "https://www.mpuecomuat.com:60145/UAT/Payment/Payment/Pay";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		factory.setHttpClient(httpClient);
		restTemplate.setRequestFactory(factory);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		logger.info("response !!!!!!!!!" + response);
		return response;
	}

	public JSONObject getOrder(JSONObject json) throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Basic bWVyY2hhbnQuQ0IwMDAwMDAwMzQyOmEzMTAyZTEzNmJkYzhlYjdkOTg2ODA0ZGZhNTMzZTAy");
		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		RestTemplate restTemplate = new RestTemplate();
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		factory.setHttpClient(httpClient);
		restTemplate.setRequestFactory(factory);
		String serviceUrl = "https://cbbank.gateway.mastercard.com/api/rest/version/57/merchant/CB0000000342/order/"
				+ json.get("orderId").toString();

		logger.info("serviceUrl!!" + serviceUrl);
		ResponseEntity<String> response = null;

		try {
			response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
		} catch (Exception e) {

			JSONObject errorObject = new JSONObject();
			JSONObject error = new JSONObject();
			error.put("cause", "INVALID_REQUEST");
			error.put("explanation", "Unable to find order D102020190022 for merchant CB0000000342");
			errorObject.put("error", error);
			errorObject.put("result", "ERROR");

			return errorObject;

		}

		logger.info("response: " + response.getBody());
		Gson g = new Gson();
		return g.fromJson(response.getBody(), JSONObject.class);
	}

	@ResponseBody
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "getorder", method = RequestMethod.POST)
	@JsonView(Views.Summary.class)
	public JSONObject getOrderForVisa(@RequestBody JSONObject json) throws IOException {
		return getOrder(json);
	}

	public JSONObject generateSession(JSONObject json) throws IOException {
		SSL ssl = new SSL();
		ssl.disableSslVerification();

		String url = "https://cbbank.gateway.mastercard.com/api/rest/version/57/merchant/CB0000000342/session";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization",
				"Basic bWVyY2hhbnQuQ0IwMDAwMDAwMzQyOmEzMTAyZTEzNmJkYzhlYjdkOTg2ODA0ZGZhNTMzZTAy");
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
	@RequestMapping(value = "generatesession", method = RequestMethod.POST)
	@JsonView(Views.Summary.class)
	public JSONObject getSession(@RequestBody JSONObject json) throws IOException {
		return generateSession(json);
	}

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

}