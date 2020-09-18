package com.middleware.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class Result {
	@JsonView(Views.Thin.class)
	String description;
	
	@JsonView(Views.Thin.class)
	String code;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
