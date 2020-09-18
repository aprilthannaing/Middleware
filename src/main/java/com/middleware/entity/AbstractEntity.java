package com.middleware.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity {

	public boolean isBoIdRequired(Long boId) {
		return boId == 0 || SystemConstant.BOID_REQUIRED.equals(boId);
	}

}
