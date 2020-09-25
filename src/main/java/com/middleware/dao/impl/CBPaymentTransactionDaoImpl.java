package com.middleware.dao.impl;

import com.middleware.dao.CBPaymentTransactionDao;
import com.middleware.entity.CBPaytransaction;

public class CBPaymentTransactionDaoImpl extends AbstractDaoImpl<CBPaytransaction, String> implements CBPaymentTransactionDao{
	protected CBPaymentTransactionDaoImpl() {
		super(CBPaytransaction.class);
	}
}
