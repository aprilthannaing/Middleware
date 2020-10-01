package com.middleware.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends AbstractEntity implements Serializable {

	@Id
	@Column(name = "Id", unique = true, nullable = false)
	private long Id;

	private String name;

	private String email;

	private String phoneNo;

	private String paymentdescription;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPaymentdescription() {
		return paymentdescription;
	}

	public void setPaymentdescription(String paymentdescription) {
		this.paymentdescription = paymentdescription;
	}
}
