package com.middleware.service.impl;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.middleware.dao.SessionDao;
import com.middleware.entity.Result;
import com.middleware.entity.Session;
import com.middleware.service.SessionService;

@Service("sessionService")
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionDao sessionDao;

    private Logger logger = Logger.getLogger(SessionServiceImpl.class);

    @Transactional(readOnly = false)
    public void save(Session session) throws ServiceUnavailableException {

	if (session.isBoIdRequired(session.getId()))
	    session.setId(getId());
	sessionDao.save(session);
    }

    private Long getId() {
	return countSession() + 1000000;
    }

    public long countSession() {
	String query = "select count(*) from Session";
	return sessionDao.findLongByQueryString(query).get(0);
    }

    public String getUserId() {
       return "USR" + getId();
    }

    public Result acceptSession(Session session) {
	Result res = new Result();
	if (session.isBoIdRequired(session.getId()))
	    session.setId(getId());
		String sessionid = new CommonService().generateSession(session.getId()+"");
		session.setSessionId(sessionid);
		boolean correct = false;
	try {
	    correct = sessionDao.checkSaveOrUpdate(session);
	    if (correct) {
		res.setCode("0000");
		res.setDescription("Successfully");
		//res.setResult("localhost:4200/home/" + session.getSessionId() + "");
		res.setResult(session.getSessionId());
	    } else {
		res.setCode("0012");
		res.setDescription("Fail");
	    }
	} catch (com.mchange.rmi.ServiceUnavailableException e) {
	    e.printStackTrace();
	}

	return res;
    }

    public Session checkingSession(String id) {
	Session session = new Session();
	String query = "from Session where sessionId ='" + id + "'";
	List<Session> userList = sessionDao.getEntitiesByQuery(query);
	if (userList.size() > 0) {
	    session = userList.get(0);
	}else session = null;
	return session;
    }
    
    public Session findByUserId(String userId) {
	String query = "from Session where userId='" + userId + "'";
	List<Session> sessionList = sessionDao.getEntitiesByQuery(query);
	if(CollectionUtils.isEmpty(sessionList))
	    return null;
	return sessionList.get(0);	
    }
    
    public Session findBySessionId(String sessionId) {
	String query = "from Session where Id=" + sessionId;
	List<Session> sessionList = sessionDao.getEntitiesByQuery(query);
	if(CollectionUtils.isEmpty(sessionList))
	    return null;
	return sessionList.get(0);	
    }
}
