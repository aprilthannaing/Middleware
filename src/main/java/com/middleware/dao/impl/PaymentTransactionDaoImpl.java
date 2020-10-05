package com.middleware.dao.impl;

import org.springframework.stereotype.Repository;

import com.middleware.dao.PaymentTransactionDao;
import com.middleware.entity.MPUPaymentTransaction;

@Repository
public class PaymentTransactionDaoImpl extends AbstractDaoImpl<MPUPaymentTransaction, String> implements PaymentTransactionDao{
	
	protected PaymentTransactionDaoImpl() {
		super(MPUPaymentTransaction.class);
	}
}
