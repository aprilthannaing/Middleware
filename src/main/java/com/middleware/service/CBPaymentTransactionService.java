package com.middleware.service;

import com.middleware.entity.CBPaytransaction;
import com.middleware.entity.Result;

public interface CBPaymentTransactionService {
	public Result savecbpayment(CBPaytransaction data);
	public CBPaytransaction checkTransRef(String transRef);
	public CBPaytransaction checkTranID(long tranid);
}
