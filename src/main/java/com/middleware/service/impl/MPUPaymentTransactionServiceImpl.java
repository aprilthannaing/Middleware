package com.middleware.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mchange.rmi.ServiceUnavailableException;
import com.middleware.dao.PaymentTransactionDao;
import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.service.PaymentTransactionService;

@Service("PaymentTransactionServiceImpl")
public class MPUPaymentTransactionServiceImpl implements PaymentTransactionService {

    @Autowired
    private PaymentTransactionDao paymentDao;

    private static Logger logger = Logger.getLogger(MPUPaymentTransactionServiceImpl.class);

    public Result saveMPUPayment(MPUPaymentTransaction data) {
	Result res = new Result();
	try {
	    if (data.isBoIdRequired(data.getTranID()))
		data.setTranID(getMPUId());
	    boolean correct = paymentDao.checkSaveOrUpdate(data);
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
	return paymentDao.findLongByQueryString(query).get(0);
    }

}
