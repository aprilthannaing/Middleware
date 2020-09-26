package com.middleware.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mchange.rmi.ServiceUnavailableException;
import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.entity.CBPaytransaction;
import com.middleware.entity.Result;
import com.middleware.service.CBPaymentTransactionService;

@Service("CBPaymentTransactionServiceImpl")
public class CBPaymentTransactionServiceImpl implements CBPaymentTransactionService{
	@Autowired
	private CBPaymentTransactionDao cbpaymentDao;
	
	private static Logger logger = Logger.getLogger(PaymentTransactionServiceImpl.class);
	
		public Result savecbpayment(CBPaytransaction data) {
			Result res = new Result();
			try {
				if (data.isBoIdRequired(data.getTranID()))
					data.setTranID(getpayId());
				boolean correct = cbpaymentDao.checkSaveOrUpdate(data);
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
		
		private Long getpayId() {
			return countcbpay() + 1;
		}
		public long countcbpay() {
			String query = "select count(*) from CBPaytransaction";
			return cbpaymentDao.findLongByQueryString(query).get(0);
		}
		
		public CBPaytransaction checkTransRef(String transRef) {
			String query = "from CBPaytransaction where transRef=" + transRef;
			List<CBPaytransaction> mobileUserList = cbpaymentDao.getAll(query);
			if (CollectionUtils.isEmpty(mobileUserList))
				return null;
			return mobileUserList.get(0);
		}
}
