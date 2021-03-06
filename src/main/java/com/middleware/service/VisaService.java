package com.middleware.service;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import com.middleware.entity.Visa;
import com.middleware.entity.VisaTransaction;

public interface VisaService {

    public void save(Visa visa) throws ServiceUnavailableException;

    public List<Visa> findByDateRange(String startDate, String endDate);

    public Visa findByTokenId(String tokenId);

}
