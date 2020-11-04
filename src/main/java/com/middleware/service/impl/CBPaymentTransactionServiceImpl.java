package com.middleware.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mchange.rmi.ServiceUnavailableException;
import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.CBPaymentTransaction;
import com.middleware.entity.Result;
import com.middleware.service.CBPaymentTransactionService;

@Service("CBPaymentTransactionService")
public class CBPaymentTransactionServiceImpl implements CBPaymentTransactionService {
	@Autowired
	private CBPaymentTransactionDao cbpaymentDao;

	private static Logger logger = Logger.getLogger(MPUPaymentTransactionServiceImpl.class);

	public Result savecbpayment(CBPayTransaction data) {
		Result res = new Result();
		try {
			if (data.isBoIdRequired(data.getTranID()))
				data.setTranID(getpayId());
			boolean correct = cbpaymentDao.checkSaveOrUpdate(data);
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

	private Long getpayId() {
		return countcbpay() + 1;
	}

	public long countcbpay() {
		String query = "select count(*) from CBPaytransaction";
		return cbpaymentDao.findLongByQueryString(query).get(0);
	}

	public CBPayTransaction checkTransRef(String transRef) {
		String query = "select cbpay from CBPaytransaction cbpay where transRef=" + transRef;
		List<CBPayTransaction> mobileUserList = cbpaymentDao.byQuery(query);
		if (CollectionUtils.isEmpty(mobileUserList))
			return null;
		return mobileUserList.get(0);
	}

	public CBPayTransaction checkTranID(long tranid) {
		String query = "from CBPaytransaction where tranID=" + tranid;
		List<CBPayTransaction> mobileUserList = cbpaymentDao.getEntitiesByQuery(query);
		if (CollectionUtils.isEmpty(mobileUserList))
			return null;
		return mobileUserList.get(0);
	}

	public List<CBPayTransaction> findByDateRange(String startDate, String endDate) {
		String query = "from CBPayTransaction cb where cb.transExpiredTime between '" + startDate + "' and '" + endDate
				+ "'";
		List<CBPayTransaction> cbPayList = cbpaymentDao.getEntitiesByQuery(query);
		return cbPayList;
	}
	
	 public CBPayTransaction findByTokenId(String tokenId) {
		String query = "from CBPayTransaction cb where sessionId='" + tokenId + "'";
		List<CBPayTransaction> cbTransactionList = cbpaymentDao.getEntitiesByQuery(query);
		if (CollectionUtils.isEmpty(cbTransactionList))
		    return null;
		return cbTransactionList.get(0);
	    }	
	
}
