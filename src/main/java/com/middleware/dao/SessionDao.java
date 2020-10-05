package com.middleware.dao;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Session;

public interface SessionDao extends AbstractDao<Session, String>  {
	
	public void save(Session user) throws ServiceUnavailableException;

}
