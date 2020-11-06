package com.middleware.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class Transaction {
	
	@JsonView(Views.Thin.class)
	private String orderId;

    @JsonView(Views.Thin.class)
    private String bankIdentifier;

    @JsonView(Views.Thin.class)
    private String amount;

    @JsonView(Views.Thin.class)
    private String paymentReference;

    @JsonView(Views.Thin.class)
    private String tokenId;

    @JsonView(Views.Thin.class)
    private String transactionDate;

    @JsonView(Views.Thin.class)
    private String paymentConfirmationDate;

    @JsonView(Views.Thin.class)
    private String paymentStatus;

    @JsonView(Views.Thin.class)
    private String receiptNumber;

    public String getPaymentStatus() {
	return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
	this.paymentStatus = paymentStatus;
    }

    public String getReceiptNumber() {
	return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
	this.receiptNumber = receiptNumber;
    }

    public String getPaymentConfirmationDate() {
	return paymentConfirmationDate;
    }

    public void setPaymentConfirmationDate(String paymentConfirmationDate) {
	this.paymentConfirmationDate = paymentConfirmationDate;
    }

    public String getBankIdentifier() {
	return bankIdentifier;
    }

    public void setBankIdentifier(String bankIdentifier) {
	this.bankIdentifier = bankIdentifier;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    public String getTokenId() {
	return tokenId;
    }

    public void setTokenId(String tokenId) {
	this.tokenId = tokenId;
    }

    public String getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
	this.transactionDate = transactionDate;
    }

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
    
}
