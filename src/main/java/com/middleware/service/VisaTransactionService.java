package com.middleware.service;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;

public interface VisaTransactionService {
	
	public void save(VisaTransaction visaTransaction) throws ServiceUnavailableException;

}
