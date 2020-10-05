package com.middleware.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.entity.SessionStatus;
import com.middleware.entity.SystemConstant;
import com.middleware.entity.Views;
import com.middleware.service.SessionService;

@RestController
@RequestMapping("data")
public class WipoDataController {
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "accept", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public Result acceptUser(@RequestBody JSONObject json) throws Exception {
	Result result = new Result();
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
	result = sessionService.acceptSession(wipoData);
	return result;
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
