package com.middleware.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity {

	public boolean isBoIdRequired(Long id) {
		return id == 0 || SystemConstant.ID_REQUIRED.equals(id);
	}
}
