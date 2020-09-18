package com.middleware.entity;

public class Payment {

	private long invoiceNo;

	private long merchantId;

	private int currencyCode;

	private String productDesc;

	private String userDefined1;

	private String userDefined2;

	private String userDefined3;

	private String hashValue;

	private String amount;

	public long getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public int getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(int currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getUserDefined1() {
		return userDefined1;
	}

	public void setUserDefined1(String userDefined1) {
		this.userDefined1 = userDefined1;
	}

	public String getUserDefined2() {
		return userDefined2;
	}

	public void setUserDefined2(String userDefined2) {
		this.userDefined2 = userDefined2;
	}

	public String getUserDefined3() {
		return userDefined3;
	}

	public void setUserDefined3(String userDefined3) {
		this.userDefined3 = userDefined3;
	}

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
