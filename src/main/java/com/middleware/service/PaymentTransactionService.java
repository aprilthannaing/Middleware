package com.middleware.service;

import com.middleware.entity.Result;
import com.middleware.entity.paymenttransaction;
public interface PaymentTransactionService {
	public Result savepayment(paymenttransaction data);
}
