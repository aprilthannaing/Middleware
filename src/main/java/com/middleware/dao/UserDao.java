package com.middleware.dao;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.User;

public interface UserDao extends AbstractDao<User, String>  {
	
	public void save(User user) throws ServiceUnavailableException;

}
