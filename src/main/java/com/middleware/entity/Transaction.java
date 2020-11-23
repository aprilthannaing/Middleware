package com.middleware.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

public class Transaction {

    @JsonView(Views.Thin.class)
    private String transactionId;

    @JsonView(Views.Thin.class)
    private String bankIdentifier;

    @JsonView(Views.Thin.class)
    private String amount;

    @JsonView(Views.Thin.class)
    private String paymentReference; // ***

    @JsonView(Views.Thin.class)
    private String tokenId;

    @JsonView(Views.Thin.class)
    private String transactionDate;

    @JsonView(Views.Thin.class)
    private String paymentConfirmationDate;

    @JsonView(Views.Thin.class)
    private String paymentStatus;

    @JsonView(Views.Thin.class)
    private String currencyType;

    @JsonView(Views.Thin.class)
    private String receiptNumber;

    @JsonView(Views.Thin.class)
    private String paymentNote;

    @JsonView(Views.Thin.class)
    private Payer payer;

    public Payer getPayer() {
	return payer;
    }

    public void setPayer(Payer payer) {
	this.payer = payer;
    }

    @JsonView(Views.Thin.class)
    private List<AmountDetails> amountDetails;

    public List<AmountDetails> getAmountDetails() {
	if(amountDetails == null) {
	    amountDetails = new ArrayList<AmountDetails>();
	}
	return amountDetails;
    }

    public void setAmountDetails(List<AmountDetails> amountDetails) {
	this.amountDetails = amountDetails;
    }

    public String getPaymentNote() {
	return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
	this.paymentNote = paymentNote;
    }

    public String getTransactionId() {
	return transactionId;
    }

    public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
    }

    public String getCurrencyType() {
	return currencyType;
    }

    public void setCurrencyType(String currencyType) {
	this.currencyType = currencyType;
    }

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

}
