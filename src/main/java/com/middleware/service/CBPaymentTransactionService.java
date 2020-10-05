package com.middleware.service;

import java.util.List;

import com.middleware.entity.CBPayTransaction;
import com.middleware.entity.Result;

public interface CBPaymentTransactionService {
	public Result savecbpayment(CBPayTransaction data);
	
	public CBPayTransaction checkTransRef(String transRef);
	
	public CBPayTransaction checkTranID(long tranid);
	
	public List<CBPayTransaction> findByDateRange(String startDate, String endDate);

}
