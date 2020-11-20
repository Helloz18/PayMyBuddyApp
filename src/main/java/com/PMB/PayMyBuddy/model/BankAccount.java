package com.PMB.PayMyBuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bank_account")
public class BankAccount {

	@Id
	@Column(name ="bank_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String bankName;
	private String accountNumber;
	
	@JsonIgnore
	@OneToOne(mappedBy = "bankAccount")
	private User user;
	
	//boolean to simulate the response from the bank
	// --> if the user has money on his bank account or not
	private boolean responseFromBankApi;
	
	
	public BankAccount() {
	}
	
	public BankAccount(String bankName, String accountNumber) {
		this.bankName = bankName;
		this.accountNumber = accountNumber;
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}



	@Override
	public String toString() {
		return "BankAccount [bankName=" + bankName + ", accountNumber=" + accountNumber + "]";
	}

	public boolean isResponseFromBankApi() {
		return responseFromBankApi;
	}

	public void setResponseFromBankApi(boolean responseFromBankApi) {
		this.responseFromBankApi = responseFromBankApi;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	
}
