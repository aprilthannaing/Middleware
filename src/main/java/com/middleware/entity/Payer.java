package com.middleware.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class Payer {

    @JsonView(Views.Thin.class)
    private String email;

    @JsonView(Views.Thin.class)
    private String phone;

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

}
