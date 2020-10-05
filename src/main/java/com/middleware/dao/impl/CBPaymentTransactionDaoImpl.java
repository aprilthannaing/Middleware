package com.middleware.dao.impl;

import org.springframework.stereotype.Repository;

import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.entity.CBPayTransaction;

@Repository
public class CBPaymentTransactionDaoImpl extends AbstractDaoImpl<CBPayTransaction, String> implements CBPaymentTransactionDao{
	protected CBPaymentTransactionDaoImpl() {
		super(CBPayTransaction.class);
	}
}
