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
import com.middleware.dao.UserDao;
import com.middleware.entity.AES;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.User;
import com.middleware.entity.EntityStatus;
import com.middleware.entity.Views;
import com.middleware.service.UserService;

@RestController
@RequestMapping("user")
public class UserController extends AbstractController {

	@Autowired
	private UserService userService;
	
	private UserDao userDao;
	

	private static Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Detailed.class)
	public String save(@RequestBody JSONObject json) throws ServiceUnavailableException {

		User user = new User();
		user.setId(SystemConstant.ID_REQUIRED);
		user.setBoId(SystemConstant.BOID_REQUIRED.toString());
		user.setName(json.get("name").toString());
		user.setPhoneNo(json.get("phoneNo").toString());
		String email = json.get("email").toString();
		
		final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}{8,64}$";
		
		if(!email.matches(regex)) {
			return "Your email is invalid!";
		}
	    
		user.setEmail(email);
		Object passwordObject = json.get("password");
		String password = passwordObject.toString();
		user.setPassword(AES.encrypt(password, secretKey));
		user.setStatus(EntityStatus.ACTIVE);
		if (json != null) {
			userService.save(user);

			return "Success";
		} else
			return "Register Failed!";

	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Detailed.class)
	public JSONObject login(@RequestBody JSONObject json) throws ServiceUnavailableException {

		JSONObject resultJson = new JSONObject();
		String requestedEmail = json.get("email").toString();
		String requestedPassword = json.get("password").toString();
		
		if (requestedEmail.isEmpty() || requestedPassword.isEmpty()) {
			resultJson.put("message", "Your email or password is empty.");
			return resultJson;
			
		}
		
		User user = userService.getUserbyemail(requestedEmail);
		String password = user.getPassword();
		String decryptedPassword = AES.decrypt(password, secretKey);
	
		
		if (requestedPassword.equals(decryptedPassword)) 
			resultJson.put("message", "Login Successful!");
		
		 else
			resultJson.put("message", "Login Failed!");
		
		return resultJson;

	}
	
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Thin.class)
	public JSONObject changePassword(@RequestBody JSONObject json) throws ServiceUnavailableException {
		
		JSONObject resultJson = new JSONObject();
		
		String userId = json.get("userId").toString();
		String oldPassword = json.get("oldPassword").toString();
		String newPassword = json.get("newPassword").toString();
		
		if(oldPassword.equals(newPassword)) {
			resultJson.put("message", "Your new password must be different with current password!");
			return resultJson;
		}
		User user = userService.findByUserId(userId);
		if(user == null) {
			resultJson.put("message", "Your Customer Id is not found");
			return resultJson;
		}
		String encryptedPassword = user.getPassword();
		
		String decryptedPassword = AES.decrypt(encryptedPassword, secretKey);
		
		if(oldPassword.equals(decryptedPassword)) {
			
			String encryptedNewPassword = AES.encrypt(newPassword, secretKey);
			user.setPassword(encryptedNewPassword);
//			resultJson.put("message", encryptedNewPassword);
//			return resultJson;
			userService.save(user);
			resultJson.put("message", "Updated successfully!");
			return resultJson;

		}
		resultJson.put("message", "Updating password is not successful!");
		return resultJson;
	}
}
