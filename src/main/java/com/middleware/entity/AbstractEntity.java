package com.middleware.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity {

	public boolean isIdRequired(Long id) {
		return id == 0 || SystemConstant.ID_REQUIRED.equals(id);
	}
}
