package com.middleware.service.impl;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
	if (visa.isBoIdRequired(visa.getId())) {
	    visa.setId(getId());
	}
	visaDao.save(visa);
    }

    private Long getId() {
	return countVisa() + 1000000;
    }

    public long countVisa() {
	String query = "select count(*) from Visa";
	return visaDao.findLongByQueryString(query).get(0);
    }

    public List<Visa> findByDateRange(String startDate, String endDate) {
	String query = "from Visa visa where visa.creationTime between '" + startDate + "' and '" + endDate + "'";
	List<Visa> visaList = visaDao.getEntitiesByQuery(query);
	return visaList;
    }

    public Visa findByTokenId(String tokenId) {
	String query = "from Visa visa where sessionId='" + tokenId + "'";
	List<Visa> visaList = visaDao.getEntitiesByQuery(query);
	if (CollectionUtils.isEmpty(visaList))
	    return null;
	return visaList.get(0);
    }
}
