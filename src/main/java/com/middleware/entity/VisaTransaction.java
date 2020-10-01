package com.middleware.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "visaTransaction")
public class VisaTransaction extends AbstractEntity implements Serializable {
	
	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private long Id;
	
	@JsonView(Views.Thin.class)
	private String secureId;
	
	@JsonView(Views.Thin.class)
	private String authenticationToken;
	
	@JsonView(Views.Thin.class)
	private String xid;
	
	@JsonView(Views.Thin.class)
	private String gatewayEntryPoint;
	
	@JsonView(Views.Thin.class)
	private String acquirerMessage;
	
	@JsonView(Views.Thin.class)
	private String gatewayCode;
	
	@JsonView(Views.Thin.class)
	private String batch;
	
	@JsonView(Views.Thin.class)
	private double amount;
	
	@JsonView(Views.Thin.class)
	private String authorizationCode;
	
	@JsonView(Views.Thin.class)
	private String currency;
	
	@JsonView(Views.Thin.class)
	private String transactionId;
	
	@JsonView(Views.Thin.class)
	private String frequency;
	
	@JsonView(Views.Thin.class)
	private String receipt;
	
	@JsonView(Views.Thin.class)
	private String source;
	
	@JsonView(Views.Thin.class)
	private String taxAmount;
	
	@JsonView(Views.Thin.class)
	private String terminal;
	
	@JsonView(Views.Thin.class)
	private String type;
	
	@JsonView(Views.Thin.class)
	private int version;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getSecureId() {
		return secureId;
	}

	public void setSecureId(String secureId) {
		this.secureId = secureId;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getGatewayEntryPoint() {
		return gatewayEntryPoint;
	}

	public void setGatewayEntryPoint(String gatewayEntryPoint) {
		this.gatewayEntryPoint = gatewayEntryPoint;
	}

	public String getAcquirerMessage() {
		return acquirerMessage;
	}

	public void setAcquirerMessage(String acquirerMessage) {
		this.acquirerMessage = acquirerMessage;
	}

	public String getGatewayCode() {
		return gatewayCode;
	}

	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
