package com.middleware.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mchange.rmi.ServiceUnavailableException;
import com.middleware.dao.MPUPaymentTransactionDao;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.service.MPUPaymentTransactionService;

@Service("PaymentTransactionServiceImpl")
public class MPUPaymentTransactionServiceImpl implements MPUPaymentTransactionService {

    @Autowired
    private MPUPaymentTransactionDao mpuPaymentDao;

    private static Logger logger = Logger.getLogger(MPUPaymentTransactionServiceImpl.class);

    public Result saveMPUPayment(MPUPaymentTransaction data) {
	Result res = new Result();
	try {
	    if (data.isBoIdRequired(data.getTranID()))
		data.setTranID(getMPUId());
	    boolean correct = mpuPaymentDao.checkSaveOrUpdate(data);
	    if (correct) {
		res.setCode("0000");
		res.setDescription("Successfully");
		res.setResult(data.getTranID() + "");
	    } else {
		res.setCode("0012");
		res.setDescription("Fail");
	    }

	} catch (ServiceUnavailableException e) {
	    logger.error("Error: " + e);
	}
	return res;
    }

    private Long getMPUId() {
	return countUser() + 1;
    }

    public long countUser() {
	String query = "select count(*) from MPUPaymentTransaction";
	return mpuPaymentDao.findLongByQueryString(query).get(0);
    }
    
    public List<MPUPaymentTransaction> findByDateRange(String startDate, String endDate) {
	String query = "from MPUPaymentTransaction mpu where mpu.creationDate between '" + startDate + "' and '" + endDate
			+ "'";
	return mpuPaymentDao.getEntitiesByQuery(query);
    }

}
