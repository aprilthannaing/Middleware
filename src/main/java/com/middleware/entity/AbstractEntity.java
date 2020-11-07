package com.middleware.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonView;

@MappedSuperclass
public class AbstractEntity {

    @JsonView(Views.Thin.class)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    public EntityStatus getStatus() {
	return status;
    }

    public void setStatus(EntityStatus status) {
	this.status = status;
    }

    public boolean isBoIdRequired(Long id) {
	return id == 0 || SystemConstant.ID_REQUIRED.equals(id);
    }
}
