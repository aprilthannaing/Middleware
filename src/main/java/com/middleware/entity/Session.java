package com.middleware.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "session")
public class Session extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "Id", unique = true, nullable = false)
    private long Id;

    @JsonView(Views.Thin.class)
    private String userId;

    @JsonView(Views.Thin.class)
    private String paymentId;

    @JsonView(Views.Thin.class)
    private String name;

    @JsonView(Views.Thin.class)
    private String email;

    @JsonView(Views.Thin.class)
    private String phoneNo;

    @JsonView(Views.Thin.class)
    private String paymentdescription;

    @JsonView(Views.Thin.class)
    private String amount;

    @JsonView(Views.Thin.class)
    private String currency;

    @JsonView(Views.Thin.class)
    private String startDate;

    @JsonView(Views.Thin.class)
    private String endDate;

    @JsonView(Views.Thin.class)
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;
    
    @JsonView(Views.Thin.class)
    private String sessionId;

    public String getPaymentId() {
	return paymentId;
    }

    public void setPaymentId(String paymentId) {
	this.paymentId = paymentId;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPhoneNo() {
	return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
    }

    public String getPaymentdescription() {
	return paymentdescription;
    }

    public void setPaymentdescription(String paymentdescription) {
	this.paymentdescription = paymentdescription;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public String getCurrency() {
	return currency;
    }

    public void setCurrency(String currency) {
	this.currency = currency;
    }

    public String getStartDate() {
	return startDate;
    }

    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }

    public String getEndDate() {
	return endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public SessionStatus getSessionStatus() {
	return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
	this.sessionStatus = sessionStatus;
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

}
