package com.middleware.service.impl;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.middleware.dao.UserDao;
import com.middleware.entity.Result;
import com.middleware.entity.User;
import com.middleware.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	private Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Transactional(readOnly = false)
	public void save(User user) throws ServiceUnavailableException {

		if (user.isBoIdRequired(user.getId()))
			user.setId(getId());
		userDao.save(user);
	}

	private Long getId() {
		return countUser() + 1000000;
	}

	public long countUser() {
		String query = "select count(*) from User";
		return userDao.findLongByQueryString(query).get(0);
	}
	
	public Result acceptUser(User user) {
		Result res = new Result();
			if (user.isBoIdRequired(user.getId()))
				user.setId(getId());
			boolean correct = false;
			try {
				correct = userDao.checkSaveOrUpdate(user);
				if (correct) {
					res.setCode("0000");
					res.setDescription("Successfully");
					res.setResult( "localhost:4200/home/" + user.getId()+"");
				}else {
					res.setCode("0012");
					res.setDescription("Fail");
				}
			} catch (com.mchange.rmi.ServiceUnavailableException e) {
				e.printStackTrace();
			}

		return res;
	}
	
	public User checkingUser(String id) {
		User resUser = new User();
		String query = "from User where id=" + id;
		List<User> userList = userDao.getEntitiesByQuery(query);
		if(userList.size() > 0) {
			resUser = userList.get(0);
		}
		return resUser;
	}

}
