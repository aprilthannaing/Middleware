package com.middleware.dao;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.VisaTransaction;

public interface VisaTransactionDao extends AbstractDao<VisaTransaction, String>  {
	
	public void save(VisaTransaction visaTransaction) throws ServiceUnavailableException;

}