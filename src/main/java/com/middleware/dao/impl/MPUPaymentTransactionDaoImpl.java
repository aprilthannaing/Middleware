package com.middleware.dao.impl;

import org.springframework.stereotype.Repository;

import com.middleware.dao.MPUPaymentTransactionDao;
import com.middleware.entity.MPUPaymentTransaction;

@Repository
public class MPUPaymentTransactionDaoImpl extends AbstractDaoImpl<MPUPaymentTransaction, String> implements MPUPaymentTransactionDao{
	
	protected MPUPaymentTransactionDaoImpl() {
		super(MPUPaymentTransaction.class);
	}
}
