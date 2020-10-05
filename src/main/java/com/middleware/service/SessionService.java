package com.middleware.service;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Result;
import com.middleware.entity.Session;

public interface SessionService {
    public void save(Session session) throws ServiceUnavailableException;

    public Result acceptSession(Session session);

    public Session checkingSession(String id);

    public String getUserId();

}
