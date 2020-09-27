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
@Table(name = "bank_account")
public class BankAccount {

	@Id
	@Column(name ="bank_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String bankName;
	private int accountNumber;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;	
	
	public BankAccount() {
	}
	
	
	
	public BankAccount(String bankName, int accountNumber, User user) {
		super();
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.user = user;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "BankAccount [bankName=" + bankName + ", accountNumber=" + accountNumber + ", user=" + user + "]";
	}
	
	
}
