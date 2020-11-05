package com.middleware.service.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.middleware.dao.UserDao;
import com.middleware.entity.User;
import com.middleware.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	private Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Override
	public void save(User user) throws ServiceUnavailableException {
		if (user.isBoIdRequired(user.getId())) {
		    user.setId(getId());
	    }
		if(user.isBoIdRequired(Long.parseLong(user.getBoId()))) {
			user.setBoId(getUserBoId());
		}
		userDao.save(user);

	}
	private long getId() {
		return countUser() + 1;
	}
	
	private Long plus() {
		return countUser() + 1;
	}
		
    public long countUser() {
    	String query = "select count(*) from User";
    	return userDao.findLongByQueryString(query).get(0);
    }

    public String getUserBoId() {
    	return "USR" + plus();
    }
    
}
