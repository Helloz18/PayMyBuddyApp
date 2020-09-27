package com.PMB.PayMyBuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="phone_number")
public class PhoneNumber {
	
	@Id
	@Column(name ="phone_number_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
	public String name;
	public int number;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	public PhoneNumber() {
	}
	
	
	
	public PhoneNumber(String name, int number) {
		super();
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
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
