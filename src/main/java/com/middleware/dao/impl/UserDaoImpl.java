package com.middleware.dao.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.middleware.dao.UserDao;
import com.middleware.entity.User;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<User, String> implements UserDao {
	
	private Logger logger = Logger.getLogger(UserDaoImpl.class);

	protected UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	public void save(User user) throws ServiceUnavailableException {	
		try {
			saveOrUpdate(user);
		} catch (com.mchange.rmi.ServiceUnavailableException e) {
			logger.error("Error: " + e.getMessage());
		}
	}
}
