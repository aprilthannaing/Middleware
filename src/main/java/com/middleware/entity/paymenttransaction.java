package com.middleware.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "paymenttransaction")
public class paymenttransaction extends AbstractEntity implements Serializable {
	
	@Id
	@Column(name = "tranID", unique = true, nullable = false)
	private long tranID;
	
	@Column(name = "merchantID")
	private String merchantID;
	
	@Column(name = "respCode")
	private String respCode = "";
	
	@Column(name = "pan")
	private String pan = "";

	@Column(name = "amount")
	private String amount = "";
	
	@Column(name = "invoiceNo")
	private String invoiceNo = "";
	
	@Column(name = "tranRef")
	private String tranRef = "";
	
	@Column(name = "approvalCode")
	private String approvalCode = "";
	
	@Column(name = "dateTime")
	private String dateTime = "";
	
	@Column(name = "status")
	private String status = "";
	
	@Column(name = "failReason")
	private String failReason = "";

	@Column(name = "userDefined1")
	private String userDefined1 = "";
	
	@Column(name = "userDefined2")
	private String userDefined2 = "";
	
	@Column(name = "userDefined3")
	private String userDefined3 = "";
	
	@Column(name = "categoryCode")
	private String categoryCode = "";

	@Column(name = "t1")
	private String t1 = "";
	
	@Column(name = "t2")
	private String t2 = "";
	
	@Column(name = "t3")
	private String t3 = "";

	public long getTranID() {
		return tranID;
	}

	public void setTranID(long tranID) {
		this.tranID = tranID;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}


	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTranRef() {
		return tranRef;
	}

	public void setTranRef(String tranRef) {
		this.tranRef = tranRef;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
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

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
