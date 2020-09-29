package com.middleware.dao.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.middleware.dao.VisaDao;
import com.middleware.entity.Visa;

@Repository
public class VisaDaoImpl extends AbstractDaoImpl<Visa, String> implements VisaDao {
	
	private Logger logger = Logger.getLogger(VisaDaoImpl.class);

	protected VisaDaoImpl() {
		super(Visa.class);
	}
	
	@Override
	public void save(Visa visa) throws ServiceUnavailableException {	
		try {
			saveOrUpdate(visa);
		} catch (com.mchange.rmi.ServiceUnavailableException e) {
			logger.error("Error: " + e.getMessage());
		}
	}
}
