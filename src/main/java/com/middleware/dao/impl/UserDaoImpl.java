package com.middleware.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mchange.rmi.ServiceUnavailableException;
import com.middleware.dao.UserDao;
import com.middleware.entity.User;

@Repository
public class UserDaoImpl extends AbstractDaoImpl<User, String> implements UserDao {

	private Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	protected UserDaoImpl() {
		super(User.class);
	}

	@Override
	public void save(User user) throws javax.naming.ServiceUnavailableException {
		// TODO Auto-generated method stub
		
	}

	
	
}
