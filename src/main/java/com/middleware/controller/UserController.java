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
import com.middleware.entity.SystemConstant;
import com.middleware.entity.User;
import com.middleware.entity.UserStatus;
import com.middleware.entity.Views;
import com.middleware.service.UserService;


@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	private static Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Detailed.class)
	public String save(@RequestBody JSONObject json) throws ServiceUnavailableException{
		
		User user = new User();
		user.setId(SystemConstant.ID_REQUIRED);
		user.setBoId(SystemConstant.BOID_REQUIRED.toString());
		user.setName(json.get("name").toString());
		user.setPhoneNo(json.get("phoneNo").toString());
		user.setEmail(json.get("email").toString());
		user.setPassword(json.get("password").toString());
		user.setStatus(UserStatus.ACTIVE);
		userService.save(user);
		
		return "success";
		
	}
	
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Detailed.class)
	public String login(@RequestBody JSONObject json) throws ServiceUnavailableException{
		
		User user = new User();
		
		
		
		return "success";
		
	}
	

	
}
