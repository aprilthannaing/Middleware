package com.middleware.controller;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Payment;
import com.middleware.entity.Views;
import org.apache.commons.codec.binary.Base64;

@RestController
@RequestMapping("api")
public class PaymentController {

	private static Logger logger = Logger.getLogger(PaymentController.class);

	/*
	 * {"merchantId" : "204104001305002", "invoiceNo" : "1234567890333",
	 * "currencyCode": 104, "productDesc":"This is Testing", "userDefined1" : "123",
	 * "userDefined2" : "456", "userDefined3" : "789", "amount" : 0000001000000,
	 * "hashValue": "F9BdcXjDTvyYEamlxgFsODJgDjeNOcw+ZAdwVdhWBwU="}
	 */

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
	//	return paymentRestClient.thirdpartyAPI(payment);		
		return paymentRestClient.getForm(payment);
	}
}
