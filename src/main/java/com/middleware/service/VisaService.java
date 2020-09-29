package com.middleware.service;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Visa;

public interface VisaService {
	
	public void save(Visa visa) throws ServiceUnavailableException;

}
