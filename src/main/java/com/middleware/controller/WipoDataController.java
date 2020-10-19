package com.middleware.controller;

import java.security.Key;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.AES;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.entity.SessionStatus;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Views;
import com.middleware.service.SessionService;

@RestController
@RequestMapping("data")
public class WipoDataController extends AbstractController{
	private static final String ALGO = "AES/CBC/NoPadding";
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "accept", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public Result acceptUser(@RequestBody JSONObject json) throws Exception {
    AES aes = new AES();
	Result result = new Result();
	String myCode = valueHash(json);
	String yourCode = json.get("yourCode").toString();
	if(myCode.equals(yourCode)) {
		Session wipoData = convertRequest(json);
		result = sessionService.acceptSession(wipoData);
		if(result.getCode().equals("0000")) {
			//result.setResult("localhost:4200/home/" + aes.encrypt(result.getResult() + "", secretKey));
			String encryptValue = aes.encrypt(result.getResult() + "", secretKey);
			System.out.println("Encryption Value : " + encryptValue);
			System.out.println("Session Value    : " + result.getResult());
			System.out.println("Decryption Value : " + aes.decrypt(encryptValue, secretKey));
		}
	}else {
		result.setCode("0001");
		result.setDescription("please check your Input Value");
	}
	return result;
    }
    public Session convertRequest(JSONObject json) {
    	Session wipoData = new Session();
    	wipoData.setId(SystemConstant.BOID_REQUIRED);
    	wipoData.setUserId(sessionService.getUserId());
    	wipoData.setPaymentId(json.get("paymentId").toString());
    	wipoData.setName(json.get("name").toString());
    	wipoData.setEmail(json.get("email").toString());
    	wipoData.setPhoneNo(json.get("phoneNo").toString());
    	wipoData.setPaymentdescription(json.get("paymentdescription").toString());
    	wipoData.setAmount(json.get("amount").toString());
    	wipoData.setCurrency(json.get("currency").toString());
    	wipoData.setSessionStatus(SessionStatus.ACTIVE);
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	LocalDateTime now = LocalDateTime.now();
    	wipoData.setStartDate(dtf.format(now));
    	return wipoData;
    }
    @RequestMapping(value = "valueHash", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public String valueHash(@RequestBody JSONObject json) throws Exception {
    	String paymentId 			= json.get("paymentId").toString();
    	String name 				= json.get("name").toString();
    	String email				= json.get("email").toString();
    	String phoneNo				= json.get("phoneNo").toString();
    	String paymentdescription	= json.get("paymentdescription").toString();
    	String amount				= json.get("amount").toString();
    	String currency				= json.get("currency").toString();
    	String hashstr = paymentId + name + email + phoneNo + paymentdescription + amount + currency;
    	String hashValue = hmacSha1(hashstr, secretKey).toUpperCase();
    	return hashValue;
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

    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public Result checkingUser(@RequestBody JSONObject json) throws Exception {
		Result res = new Result();
		String id = json.get("id").toString();
		Session session = sessionService.checkingSession(id);
		if (session != null) {
		    res.setResult(session.getAmount());
		    res.setCode("0000");
		} else
		    res.setCode("0001");
	return res;
    }
    
}
