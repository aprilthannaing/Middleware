package com.middleware.service;

import com.middleware.entity.MailEvent;

public interface MailService {

    public String sendMail(MailEvent mailEvent);

}
