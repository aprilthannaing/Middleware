package com.middleware.service;

import java.util.List;

import com.middleware.entity.MPUPaymentTransaction;
import com.middleware.entity.Result;

public interface MPUPaymentTransactionService {
    
    public Result saveMPUPayment(MPUPaymentTransaction data);

    public List<MPUPaymentTransaction> findByDateRange(String startDate, String endDate);

}
