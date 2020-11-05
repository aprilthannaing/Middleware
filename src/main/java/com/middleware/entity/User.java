package com.middleware.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "user")
public class User extends AbstractEntity implements Serializable {
	
	@Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;
	
	@Id
    @Column(name = "boId", unique = true, nullable = false)
    private String boId;
	
	@JsonView(Views.Summary.class)
	private String name;
	
	@JsonView(Views.Summary.class)
	private String phoneNo;
	
	@JsonView(Views.Summary.class)
	private String email;
	
	@JsonView(Views.Summary.class)
	private String password;
	
    @JsonView(Views.Thin.class)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(EntityStatus status) {
		this.status = status;
	}

	public String getBoId() {
		return boId;
	}

	public void setBoId(String boId) {
		this.boId = boId;
	}

}
