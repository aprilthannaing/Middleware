package com.middleware.entity;

public class Pay {

	private String redirectHTML;

	private Transaction transaction;

	public String getRedirectHTML() {
		return redirectHTML;
	}

	public void setRedirectHTML(String redirectHTML) {
		this.redirectHTML = redirectHTML;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
