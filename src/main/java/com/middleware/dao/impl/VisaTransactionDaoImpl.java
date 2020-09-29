package com.middleware.dao.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.middleware.dao.VisaTransactionDao;
import com.middleware.entity.VisaTransaction;

@Repository
public class VisaTransactionDaoImpl extends AbstractDaoImpl<VisaTransaction, String> implements VisaTransactionDao {
	
	private Logger logger = Logger.getLogger(VisaDaoImpl.class);

	protected VisaTransactionDaoImpl() {
		super(VisaTransaction.class);
	}
	
	@Override
	public void save(VisaTransaction visaTransaction) throws ServiceUnavailableException {	
		try {
			saveOrUpdate(visaTransaction);
		} catch (com.mchange.rmi.ServiceUnavailableException e) {
			logger.error("Error: " + e.getMessage());
		}
	}
}
