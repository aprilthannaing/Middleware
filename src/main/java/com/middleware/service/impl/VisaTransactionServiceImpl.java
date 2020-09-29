package com.middleware.service.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.middleware.dao.VisaTransactionDao;
import com.middleware.entity.VisaTransaction;
import com.middleware.service.VisaTransactionService;

@Service("visaTransactionService")
public class VisaTransactionServiceImpl implements VisaTransactionService {
	
	@Autowired
	private VisaTransactionDao visaTransactionDao;
	
	private Logger logger = Logger.getLogger(VisaServiceImpl.class);
	
	@Transactional(readOnly = false)
	public void save(VisaTransaction visaTransaction) throws ServiceUnavailableException {
		logger.info("visaTransaction id !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + visaTransaction.getId());

		if (visaTransaction.isBoIdRequired(visaTransaction.getId())) {
			visaTransaction.setId(getId());
		}
		logger.info("visa id !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + visaTransaction.getId());
		visaTransactionDao.save(visaTransaction);
	}

	private Long getId() {
		return countVisaTransaction() + 1000000;
	}	
	
	public long countVisaTransaction() {
		String query = "select count(*) from VisaTransaction";
		return visaTransactionDao.findLongByQueryString(query).get(0);
	}	
}
