package com.middleware.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.persistence.CascadeType;

@Entity
@Table(name = "visa")
public class Visa extends AbstractEntity implements Serializable {

	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private long Id;

	private String interactionOperation;

	private String merchantId;

	private String merchantCategoryCode;

	private long orderId;

	private long amount;

	private String currency;

	private String description;

	private String creationTime;

	private String customerName;

	private String customerOrderDate;

	private String deviceType;

	private String ipAddress;

	private String result;

	private String brand;

	private String expiryMonth;

	private String expiryYear;

	private String fundingMethod;

	private String issuer;

	private String nameOnCard;

	private String number;

	private String scheme;

	private String storedOnFile;

	private String type;

	private String status;

	private String totalAuthorizedAmount;

	private String totalCapturedAmount;

	private String totalRefundedAmount;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "visaTransactionId")
	private VisaTransaction visaTransaction;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getInteractionOperation() {
		return interactionOperation;
	}

	public void setInteractionOperation(String interactionOperation) {
		this.interactionOperation = interactionOperation;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(String merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerOrderDate() {
		return customerOrderDate;
	}

	public void setCustomerOrderDate(String customerOrderDate) {
		this.customerOrderDate = customerOrderDate;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getFundingMethod() {
		return fundingMethod;
	}

	public void setFundingMethod(String fundingMethod) {
		this.fundingMethod = fundingMethod;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getStoredOnFile() {
		return storedOnFile;
	}

	public void setStoredOnFile(String storedOnFile) {
		this.storedOnFile = storedOnFile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalAuthorizedAmount() {
		return totalAuthorizedAmount;
	}

	public void setTotalAuthorizedAmount(String totalAuthorizedAmount) {
		this.totalAuthorizedAmount = totalAuthorizedAmount;
	}

	public String getTotalCapturedAmount() {
		return totalCapturedAmount;
	}

	public void setTotalCapturedAmount(String totalCapturedAmount) {
		this.totalCapturedAmount = totalCapturedAmount;
	}

	public String getTotalRefundedAmount() {
		return totalRefundedAmount;
	}

	public void setTotalRefundedAmount(String totalRefundedAmount) {
		this.totalRefundedAmount = totalRefundedAmount;
	}

	public VisaTransaction getVisaTransaction() {
		return visaTransaction;
	}

	public void setVisaTransaction(VisaTransaction visaTransaction) {
		this.visaTransaction = visaTransaction;
	}
}
