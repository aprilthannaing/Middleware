package com.middleware.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class AmountDetails {

    @JsonView(Views.Thin.class)
    private double amount;

    @JsonView(Views.Thin.class)
    private String description;

    @JsonView(Views.Thin.class)
    private String feeCode;

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getFeeCode() {
	return feeCode;
    }

    public void setFeeCode(String feeCode) {
	this.feeCode = feeCode;
    }

}
