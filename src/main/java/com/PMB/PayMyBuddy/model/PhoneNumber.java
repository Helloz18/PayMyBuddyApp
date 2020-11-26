package com.PMB.PayMyBuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="phone_number")
public class PhoneNumber {
	
	@Id
	@Column(name ="phone_number_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	public long id;
	public String name;
	public String number;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	@JsonIgnore
	private User user;
	
	public PhoneNumber() {
	}
	
	
	
	public PhoneNumber(String name, String number) {
		this.name = name;
		this.number = number;
	}


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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "PhoneNumber [name=" + name + ", number=" + number + "]";
	}
	
	

}
