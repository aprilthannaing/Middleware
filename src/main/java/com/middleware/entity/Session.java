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
    @JsonView(Views.Thin.class)
    private long Id;

    @JsonView(Views.Thin.class)
    private String requestorId;

    @JsonView(Views.Thin.class)
    private String paymentReference;

    @JsonView(Views.Thin.class)
    private String paymentNote;

    @JsonView(Views.Thin.class)
    private String payerName;

    @JsonView(Views.Thin.class)
    private String payerEmail;

    @JsonView(Views.Thin.class)
    private String payerPhone;

    @JsonView(Views.Thin.class)
    private String amount1;

    @JsonView(Views.Thin.class)
    private String amount2;

    @JsonView(Views.Thin.class)
    private String amountDescription1;

    @JsonView(Views.Thin.class)
    private String amountDescription2;

    @JsonView(Views.Thin.class)
    private String currencyType;

    @JsonView(Views.Thin.class)
    private String transactionId;

    @JsonView(Views.Thin.class)
    private String bankIdentifier;

    @JsonView(Views.Thin.class)
    private String startDate;

    @JsonView(Views.Thin.class)
    private String endDate;

    @JsonView(Views.Thin.class)
    private String totalAmount;

    @JsonView(Views.Thin.class)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @JsonView(Views.Thin.class)
    private String paymentConfirmationDate;

    @JsonView(Views.Thin.class)
    private String sessionId;

    public long getId() {
	return Id;
    }

    public void setId(long id) {
	Id = id;
    }

    public String getPayerName() {
	return payerName;
    }

    public void setPayerName(String payerName) {
	this.payerName = payerName;
    }

    public String getRequestorId() {
	return requestorId;
    }

    public void setRequestorId(String requestorId) {
	this.requestorId = requestorId;
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    public String getPaymentNote() {
	return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
	this.paymentNote = paymentNote;
    }

    public String getPayerEmail() {
	return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
	this.payerEmail = payerEmail;
    }

    public PaymentType getPaymentType() {
	return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
	this.paymentType = paymentType;
    }

    public String getPayerPhone() {
	return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
	this.payerPhone = payerPhone;
    }

    public String getAmount1() {
	return amount1;
    }

    public void setAmount1(String amount1) {
	this.amount1 = amount1;
    }

    public String getAmount2() {
	return amount2;
    }

    public void setAmount2(String amount2) {
	this.amount2 = amount2;
    }

    public String getAmountDescription1() {
	return amountDescription1;
    }

    public void setAmountDescription1(String amountDescription1) {
	this.amountDescription1 = amountDescription1;
    }

    public String getAmountDescription2() {
	return amountDescription2;
    }

    public String getPaymentConfirmationDate() {
	return paymentConfirmationDate;
    }

    public void setPaymentConfirmationDate(String paymentConfirmationDate) {
	this.paymentConfirmationDate = paymentConfirmationDate;
    }

    public void setAmountDescription2(String amountDescription2) {
	this.amountDescription2 = amountDescription2;
    }

    public String getCurrencyType() {
	return currencyType;
    }

    public void setCurrencyType(String currencyType) {
	this.currencyType = currencyType;
    }

    public String getTransactionId() {
	return transactionId;
    }

    public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
    }

    public String getBankIdentifier() {
	return bankIdentifier;
    }

    public void setBankIdentifier(String bankIdentifier) {
	this.bankIdentifier = bankIdentifier;
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

    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    public String getTotalAmount() {
	return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
	this.totalAmount = totalAmount;
    }

}
