package com.middleware.service;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.User;

import antlr.collections.List;

public interface UserService {
	
	public void save(User user) throws ServiceUnavailableException;
	
	public User getUserbyemail(String emailaddress)throws ServiceUnavailableException;
	
	public User findByUserId(String boId)throws ServiceUnavailableException;
	

}
