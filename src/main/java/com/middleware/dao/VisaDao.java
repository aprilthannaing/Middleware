package com.middleware.dao;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Visa;

public interface VisaDao extends AbstractDao<Visa, String>  {
	
	public void save(Visa visa) throws ServiceUnavailableException;

}
