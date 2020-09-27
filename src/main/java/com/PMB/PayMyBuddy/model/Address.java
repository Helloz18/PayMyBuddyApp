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
@Table(name ="Address")
public class Address {
	
	@Id
	@Column(name = "address_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String number;
	private String street;
	private int zip;
	private String city;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	
	public Address() {
	}
	
	
	
	public Address(String number, String street, int zip, String city) {
		super();
		this.number = number;
		this.street = street;
		this.zip = zip;
		this.city = city;
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "Address [number=" + number + ", street=" + street + ", zip=" + zip + ", city=" + city + "]";
	}
	
	

}
