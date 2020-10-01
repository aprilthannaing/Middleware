package com.middleware.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Result;
import com.middleware.entity.User;
import com.middleware.entity.Views;
import com.middleware.service.UserService;

@RestController
@RequestMapping("user")
public class UserDataController {
	@Autowired
	private UserService userDataService;
	
	@RequestMapping(value = "acceptUser", method = RequestMethod.POST)
	@ResponseBody
	@JsonView(Views.Summary.class)
	public Result acceptUser(@RequestBody JSONObject json)throws Exception {
		Result result = new Result();
		User userdata = new User();
		userdata.setName(json.get("name").toString());
		userdata.setEmail(json.get("email").toString());
		userdata.setPhoneNo(json.get("phoneNo").toString());
		userdata.setPaymentdescription(json.get("paymentdescription").toString());
		result  = userDataService.acceptUser(userdata);
		return result;
	}
}
