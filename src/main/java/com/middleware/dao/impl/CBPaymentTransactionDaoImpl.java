package com.middleware.dao.impl;

import org.springframework.stereotype.Repository;

import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.entity.CBPaytransaction;

@Repository
public class CBPaymentTransactionDaoImpl extends AbstractDaoImpl<CBPaytransaction, String> implements CBPaymentTransactionDao{
	protected CBPaymentTransactionDaoImpl() {
		super(CBPaytransaction.class);
	}
}
