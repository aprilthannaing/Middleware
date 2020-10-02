package com.middleware.service;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Result;
import com.middleware.entity.User;

public interface UserService {
	public void save(User user) throws ServiceUnavailableException;
	public Result acceptUser(User user);
	public User checkingUser(String id); 
}
