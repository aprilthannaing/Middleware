package com.middleware.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.dao.PaymentTransactionDao;
import com.middleware.entity.Result;
import com.middleware.entity.CBPaytransaction;
import com.middleware.entity.paymenttransaction;
import com.middleware.service.PaymentTransactionService;
import com.mchange.rmi.ServiceUnavailableException;
import org.apache.log4j.Logger;

@Service("PaymentTransactionServiceImpl")
public class PaymentTransactionServiceImpl implements PaymentTransactionService{
	
	@Autowired
	private PaymentTransactionDao paymentDao;
	
	private static Logger logger = Logger.getLogger(PaymentTransactionServiceImpl.class);
	
	public Result savepayment(paymenttransaction data) {
		Result res = new Result();
		try {
			if (data.isBoIdRequired(data.getTranID()))
				data.setTranID(getMPUId());
			boolean correct = paymentDao.checkSaveOrUpdate(data);
			if (correct) {
				res.setCode("0000");
				res.setDescription("Successfully");
				res.setResult(data.getTranID()+"");
			}else {
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
		String query = "select count(*) from paymenttransaction";
		return paymentDao.findLongByQueryString(query).get(0);
	}
	
}
