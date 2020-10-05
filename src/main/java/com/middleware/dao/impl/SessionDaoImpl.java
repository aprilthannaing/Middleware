package com.middleware.dao.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.middleware.dao.SessionDao;
import com.middleware.entity.Session;

@Repository
public class SessionDaoImpl extends AbstractDaoImpl<Session, String> implements SessionDao {
	
	private Logger logger = Logger.getLogger(SessionDaoImpl.class);

	protected SessionDaoImpl() {
		super(Session.class);
	}
	
	@Override
	public void save(Session session) throws ServiceUnavailableException {	
		try {
			saveOrUpdate(session);
		} catch (com.mchange.rmi.ServiceUnavailableException e) {
			logger.error("Error: " + e.getMessage());
		}
	}
}
