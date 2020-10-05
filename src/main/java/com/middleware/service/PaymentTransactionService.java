package com.middleware.service;

import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;
public interface PaymentTransactionService {
	public Result saveMPUPayment(MPUPaymentTransaction data);
}
