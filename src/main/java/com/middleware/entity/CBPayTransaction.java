package com.middleware.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "cbpayTransaction")
public class CBPayTransaction extends AbstractEntity implements Serializable {
    @Id
    @Column(name = "tranID", unique = true, nullable = false)
    private long tranID;

    @Column(name = "reqId")
    private String reqId = "";

    @Column(name = "merId")
    private String merId = "";

    @Column(name = "subMerId")
    private String subMerId = "";

    @Column(name = "terminalId")
    private String terminalId = "";

    @Column(name = "transAmount")
    private String transAmount = "";

    @Column(name = "transCurrency")
    private String transCurrency = "";

    @Column(name = "ref1")
    private String ref1 = "";

    @Column(name = "ref2")
    private String ref2 = "";

    @Column(name = "merDqrCode")
    private String merDqrCode = "";

    @Column(name = "transExpiredTime")
    private String transExpiredTime = "";

    @Column(name = "refNo")
    private String refNo = "";

    @Column(name = "transRef")
    private String transRef = "";

    @Column(name = "transStatus")
    private String transStatus = "";

    @Column(name = "code")
    private String code = "";

    @Column(name = "msg")
    private String msg = "";

    @Column(name = "t1")
    private String t1 = "";

    @Column(name = "t2")
    private String t2 = "";

    @Column(name = "t3")
    private String t3 = "";

    @Column(name = "checkedDateTime")
    private String checkedDateTime = "";

    @Column(name = "createdDateTime")
    private String createdDateTime = "";
    
    @Transient
    private String sessionId = "";

    @JsonView(Views.Thin.class)
    @JoinColumn(name = "sessionId")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Session session;

    public Session getSession() {
	return session;
    }

    public void setSession(Session session) {
	this.session = session;
    }

    public long getTranID() {
	return tranID;
    }

    public void setTranID(long tranID) {
	this.tranID = tranID;
    }

    public String getReqId() {
	return reqId;
    }

    public void setReqId(String reqId) {
	this.reqId = reqId;
    }

    public String getMerId() {
	return merId;
    }

    public void setMerId(String merId) {
	this.merId = merId;
    }

    public String getSubMerId() {
	return subMerId;
    }

    public void setSubMerId(String subMerId) {
	this.subMerId = subMerId;
    }

    public String getTerminalId() {
	return terminalId;
    }

    public void setTerminalId(String terminalId) {
	this.terminalId = terminalId;
    }

    public String getTransAmount() {
	return transAmount;
    }

    public void setTransAmount(String transAmount) {
	this.transAmount = transAmount;
    }

    public String getTransCurrency() {
	return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
	this.transCurrency = transCurrency;
    }

    public String getRef1() {
	return ref1;
    }

    public void setRef1(String ref1) {
	this.ref1 = ref1;
    }

    public String getRef2() {
	return ref2;
    }

    public void setRef2(String ref2) {
	this.ref2 = ref2;
    }

    public String getMerDqrCode() {
	return merDqrCode;
    }

    public void setMerDqrCode(String merDqrCode) {
	this.merDqrCode = merDqrCode;
    }

    public String getTransExpiredTime() {
	return transExpiredTime;
    }

    public void setTransExpiredTime(String transExpiredTime) {
	this.transExpiredTime = transExpiredTime;
    }

    public String getRefNo() {
	return refNo;
    }

    public void setRefNo(String refNo) {
	this.refNo = refNo;
    }

    public String getTransRef() {
	return transRef;
    }

    public void setTransRef(String transRef) {
	this.transRef = transRef;
    }

    public String getTransStatus() {
	return transStatus;
    }

    public void setTransStatus(String transStatus) {
	this.transStatus = transStatus;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
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

    public String getMsg() {
	return msg;
    }

    public void setMsg(String msg) {
	this.msg = msg;
    }

    public String getCheckedDateTime() {
	return checkedDateTime;
    }

    public void setCheckedDateTime(String checkedDateTime) {
	this.checkedDateTime = checkedDateTime;
    }

    public String getCreatedDateTime() {
	return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
	this.createdDateTime = createdDateTime;
    }

    public boolean isSuccess() {
	return getTransStatus().equals("S");
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
