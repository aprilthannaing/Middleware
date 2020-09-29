package com.middleware.service.impl;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.middleware.dao.VisaDao;
import com.middleware.entity.Visa;
import com.middleware.service.VisaService;

@Service("visaService")
public class VisaServiceImpl implements VisaService {
	
	@Autowired
	private VisaDao visaDao;
	
	private Logger logger = Logger.getLogger(VisaServiceImpl.class);
	
	@Transactional(readOnly = false)
	public void save(Visa visa) throws ServiceUnavailableException {
		logger.info("visa id !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + visa.getId());

		if (visa.isBoIdRequired(visa.getId())) {
			visa.setId(getId());
		}
		logger.info("visa id !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + visa.getId());
		visaDao.save(visa);
	}

	private Long getId() {
		return countVisa() + 1000000;
	}	
	
	public long countVisa() {
		String query = "select count(*) from Visa";
		return visaDao.findLongByQueryString(query).get(0);
	}
}
