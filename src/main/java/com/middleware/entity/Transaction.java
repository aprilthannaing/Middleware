package com.middleware.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class Transaction {

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
}
