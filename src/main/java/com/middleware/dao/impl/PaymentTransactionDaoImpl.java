package com.middleware.dao.impl;

import org.springframework.stereotype.Repository;

import com.middleware.dao.PaymentTransactionDao;
import com.middleware.entity.paymenttransaction;

@Repository
public class PaymentTransactionDaoImpl extends AbstractDaoImpl<paymenttransaction, String> implements PaymentTransactionDao{
	
	protected PaymentTransactionDaoImpl() {
		super(paymenttransaction.class);
	}
}
