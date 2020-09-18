package com.middleware.entity;

public class Transaction {

	private String bankIdentifier;

	private String amount;

	private String paymentReference;

	private long tokenId;

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

	public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
