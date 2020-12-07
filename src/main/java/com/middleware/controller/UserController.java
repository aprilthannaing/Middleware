package com.middleware.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
import com.middleware.entity.AES;
import com.middleware.entity.EntityStatus;
import com.middleware.entity.Gender;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.User;
import com.middleware.entity.Views;
import com.middleware.service.UserService;


@RestController
@RequestMapping("user")
public class UserController extends AbstractController {
	
	

    @Autowired
    private UserService userService;
    
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private static Logger logger = Logger.getLogger(UserController.class);

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Detailed.class)
    public JSONObject save(@RequestBody JSONObject json) throws Exception { // response - user session
	JSONObject resultJson = new JSONObject();
	
	byte[] base64DecryptedEmail = Base64.getDecoder().decode(json.get("email").toString());
    String decryptedEmail = AES.decrypt(base64DecryptedEmail, secretKey);
    
    byte[] base64DecryptedPassword = Base64.getDecoder().decode(json.get("password").toString());
    String decryptedPassword = AES.decrypt(base64DecryptedPassword, secretKey);
    
	String firstName = json.get("firstName").toString();
	String surName = json.get("surName").toString();
	String phone = json.get("phone").toString();
	String email = decryptedEmail;
	String gender = json.get("gender").toString();
	String password = decryptedPassword;
	final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}{8,64}$";
	if (!email.matches(regex)) {
	    resultJson.put("msg", "Your email is invalid!");
	    resultJson.put("status", "0");
	    return resultJson;
	}

	User user = new User();
	user.setId(SystemConstant.ID_REQUIRED);
	user.setBoId(SystemConstant.BOID_REQUIRED.toString());
	user.setName(firstName + " " + surName);
	user.setPhoneNo(phone);
	user.setEmail(email);
	

	byte[] encryptedMsg = AES.encrypt(password, secretKey);
    String base64Encrypted = Base64.getEncoder().encodeToString(encryptedMsg);
	user.setPassword(base64Encrypted);
	user.setGender(gender.equals("1") ? gender.equals("2") ? Gender.FEMALE : Gender.MALE : Gender.CUSTOM);//need to check gender
	user.setEntityStatus(EntityStatus.ACTIVE);
	userService.save(user);						

	resultJson.put("msg", "success");
	resultJson.put("userId", AES.encrypt(user.getBoId(),secretKey));
	resultJson.put("encryptedPassword",AES.encrypt(password,secretKey));
	resultJson.put("status", "1");
	return resultJson;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Detailed.class)
    public JSONObject login(@RequestBody JSONObject json) throws Exception {
    
    byte[] base64DecryptedEmail = Base64.getDecoder().decode(json.get("encryptedEmail").toString());
    String decryptedEmail = AES.decrypt(base64DecryptedEmail, secretKey);
    
    byte[] base64DecryptedPassword = Base64.getDecoder().decode(json.get("encryptedPassword").toString());
    String decryptedPassword = AES.decrypt(base64DecryptedPassword, secretKey);
    

	JSONObject resultJson = new JSONObject();
	String requestedEmail = decryptedEmail;
	String requestedPassword = decryptedPassword;

	if (requestedEmail.isEmpty() || requestedPassword.isEmpty()) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "Your email or password is empty.");
	    return resultJson;

	}

	User user = userService.getUserbyemail(requestedEmail);
	if(user == null) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "This email is not registerd!");
	    return resultJson;
	}
	byte[] base64Decrypted = Base64.getDecoder().decode(user.getPassword());
    String decryptedPasswordtoResend = AES.decrypt(base64Decrypted, secretKey);


	if (!requestedPassword.equals(decryptedPasswordtoResend)) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "Password is wrong!");
	    return resultJson;

	}

	resultJson.put("status", "1");
	resultJson.put("msg", "Login Successful!");
	resultJson.put("userId", AES.encrypt(user.getBoId(),secretKey));
	return resultJson;

    }
  
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Thin.class)
    public JSONObject changePassword(@RequestBody JSONObject json) throws Exception {

	JSONObject resultJson = new JSONObject();

	String userId = json.get("userId").toString();
	String newPassword = json.get("newPassword").toString();
	String confirmPassword = json.get("confirmPassword").toString();
	if(newPassword !=null && confirmPassword !=null && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
		if (!newPassword.equals(confirmPassword)) {
			resultJson.put("status", "0");
		    resultJson.put("message", "Your confirm password was wrong!");
		    return resultJson;
		}
		User user = userService.findByUserId(userId);
		if (user == null) {
			resultJson.put("status", "0");
		    resultJson.put("message", "Your Customer Id is not found");
		    return resultJson;
		}
		
//		byte[] passwordToDecrypt = Base64.getDecoder().decode(user.getPassword());
//		String decryptedPassword = AES.decrypt(passwordToDecrypt,secretKey);
	
		
	
			byte[] encryptedMsg = AES.encrypt(newPassword, secretKey);
		    String base64Encrypted = Base64.getEncoder().encodeToString(encryptedMsg);
		   
		    user.setPassword(base64Encrypted);
		    userService.save(user);
		    resultJson.put("status", "1");
		    resultJson.put("message", "Updated successfully!");
		    return resultJson;
	
		
	
		
	}
	else {
		resultJson.put("status", "0");
		resultJson.put("status", "Please enter a password!");
		return resultJson;
	}
	
	
    }
	
    @CrossOrigin(origins = "*")
	@RequestMapping(value = "checkPhone", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public JSONObject checkPhone(@RequestBody JSONObject json) throws ServiceUnavailableException {
		
		JSONObject resultJson = new JSONObject();
		
		String userId = json.get("userId").toString();
		String phoneNo = json.get("phone").toString();
		User user = userService.findByUserId(userId);
		if(user.getPhoneNo().equals(phoneNo)) {
			resultJson.put("status", "1");
			resultJson.put("msg", "Checking Phone Number is successful!!");
			return resultJson;
		}
		resultJson.put("status", "0");
		resultJson.put("msg", "Please enter a valid Phone Number!!");
		return resultJson;
		
	}
	
	

}
