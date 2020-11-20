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
import com.middleware.dao.UserDao;
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

    private UserDao userDao;

    private static Logger logger = Logger.getLogger(UserController.class);

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Detailed.class)
    public JSONObject save(@RequestBody JSONObject json) throws ServiceUnavailableException { // response - user session
	JSONObject resultJson = new JSONObject();
	String firstName = json.get("firstName").toString();
	String surName = json.get("surName").toString();
	String phone = json.get("phone").toString();
	String email = json.get("email").toString();
	String gender = json.get("gender").toString();
	String password = json.get("password").toString();
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
	user.setPassword(AES.encrypt(password, secretKey));
	user.setGender(gender.equals("1") ? gender.equals("2") ? Gender.FEMALE : Gender.MALE : Gender.CUSTOM);
	user.setEntityStatus(EntityStatus.ACTIVE);
	userService.save(user);

	resultJson.put("msg", "success");
	resultJson.put("userId", AES.encrypt(user.getBoId(), secretKey));
	resultJson.put("status", "1");
	return resultJson;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Detailed.class)
    public JSONObject login(@RequestBody JSONObject json) throws ServiceUnavailableException {

	JSONObject resultJson = new JSONObject();
	String requestedEmail = json.get("email").toString();
	String requestedPassword = json.get("password").toString();

	if (requestedEmail.isEmpty() || requestedPassword.isEmpty()) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "Your email or password is empty.");
	    return resultJson;

	}

	User user = userService.getUserbyemail(requestedEmail);
	if (user == null) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "This email is not registerd!");
	    return resultJson;
	}

	String password = user.getPassword();
	String decryptedPassword = AES.decrypt(password, secretKey);
	
	logger.info("requestedPassword!!!!!!!!!" + requestedPassword);
	logger.info("decryptedPassword!!!!!!!!!" + decryptedPassword);



	if (!requestedPassword.equals(decryptedPassword)) {
	    resultJson.put("status", "0");
	    resultJson.put("msg", "Password is wrong!");
	    return resultJson;
	}

	resultJson.put("status", "1");
	resultJson.put("msg", "Login Successful!");
	return resultJson;

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "forgetpassword", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Thin.class)
    public JSONObject changePassword(@RequestBody JSONObject json) throws ServiceUnavailableException {

	JSONObject resultJson = new JSONObject();
	String userId = json.get("userId").toString();
	String newPassword = json.get("newPassword").toString();
	String confirmPassword = json.get("confirmPassword").toString();

	if (!newPassword.equals(confirmPassword)) {
	    resultJson.put("msg", "Your new password and confirm password must be equal!");
	    resultJson.put("status", "0");
	    return resultJson;
	}

	User user = userService.findByUserId(userId);
	if (user == null) {
	    resultJson.put("msg", "This User is not registered!");
	    resultJson.put("status", "0");
	    return resultJson;
	}

	String encryptedPassword = user.getPassword();
	user.setPassword(encryptedPassword);
	resultJson.put("msg", "Success!");
	resultJson.put("status", "1");
	return resultJson;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "checkphone", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public JSONObject checkPhone(@RequestBody JSONObject json) throws ServiceUnavailableException {
	JSONObject resultJson = new JSONObject();
	String userId = json.get("userId").toString();
	String phoneNo = json.get("phone").toString();
	User user = userService.findByUserId(userId);
	if (user == null) {
	    resultJson.put("msg", "This phone number isn't found!");
	    resultJson.put("status", "0");
	    return resultJson;

	}

	if (!user.getPhoneNo().equals(phoneNo)) {
	    resultJson.put("msg", "Your phone number is wrong!");
	    resultJson.put("status", "0");
	    return resultJson;

	}
	resultJson.put("msg", "Success");
	resultJson.put("status", "1");
	return resultJson;
    }
}
