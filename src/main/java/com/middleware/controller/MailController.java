package com.middleware.controller;

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
import com.middleware.entity.MailEvent;
import com.middleware.entity.Views;
import com.middleware.service.MailService;

@RestController
@RequestMapping("mail")
public class MailController extends AbstractController {

    @Autowired
    private MailService mailService;

    private static Logger logger = Logger.getLogger(MailController.class);

    private String send(String email) {
	MailEvent mailEvent = new MailEvent();
	mailEvent.setSubject("Transaction Report");
	mailEvent.setContent("Transaction Report of payment");
	mailEvent.setTo(email);
	return mailService.sendMail(mailEvent);
    }

    private String sendEmail(JSONObject json) {
	String emails = json.get("emails").toString();
	if (emails.contains(",")) {
	    for (String email : emails.split(",")) {
		logger.info("email!!!!!!!!!!!!!!: " + email);
		String msg = send(email);
		if (!msg.equals("success"))
		    return msg;
	    }
	} else
	    return send(emails);
	return "success";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "report", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Summary.class)
    public String sendMail(@RequestBody JSONObject json) throws Exception {
	logger.info("return message!!!!!!!!!!!!!" + sendEmail(json));

	return sendEmail(json);
    }
}
