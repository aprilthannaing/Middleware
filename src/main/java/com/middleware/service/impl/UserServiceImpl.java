package com.middleware.service.impl;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.middleware.controller.AbstractController;
import com.middleware.dao.UserDao;
import com.middleware.entity.AES;
import com.middleware.entity.User;
import com.middleware.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public void save(User user) {
	try {
	    if (user.isBoIdRequired(user.getId())) {
		user.setId(getId());
	    }
	    if (user.isBoIdRequired(Long.parseLong(user.getBoId()))) {
		user.setBoId(getUserBoId());
	    }
	    userDao.saveOrUpdate(user);
	} catch (com.mchange.rmi.ServiceUnavailableException e) {
	    e.printStackTrace();
	}

    }

    private long getId() {
	return countUser() + 1;
    }

    private Long plus() {
	return countUser() + 10000;
    }

    public long countUser() {
	String query = "select count(*) from User";
	return userDao.findLongByQueryString(query).get(0);
    }

    public String getUserBoId() {
	return "USR" + plus();
    }


    @Override
    public User getUserbyemail(String emailaddress) {

	String query = "select User from User where email='" +emailaddress + "'";
	List<User> userList = userDao.getEntitiesByQuery(query);
	if (CollectionUtils.isEmpty(userList))
	    return null;
	return userList.get(0);
    }

    @Override
    public User findByUserId(String boId) throws ServiceUnavailableException {

	String query = "from User where boId='" + boId + "'";
	List<User> userList = userDao.getEntitiesByQuery(query);
	if (CollectionUtils.isEmpty(userList))
	    return null;
	return userList.get(0);
    }

	


}
