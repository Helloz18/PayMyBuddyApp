package com.PMB.PayMyBuddy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name ="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;	
	private Date birthDate;
	private double appAccount;
	
	//for Spring security
	private String role;
	private boolean enabled;
	
	@OneToMany(mappedBy="user")
	private List<Address> addresses;
	
	@OneToMany(mappedBy="user")
	private List<PhoneNumber> phoneNumbers;
		
	@OneToMany(mappedBy="user")
	private List<MoneyTransfer> moneyTransfers;
	
	@OneToMany(mappedBy="user")
	private List<BankAccount> bankAccounts;
	
	@ManyToMany(mappedBy = "user")
	private List<User> moneyFriends;
	
	public User() {
	}

	public User(String email, String password, String firstName, String lastName, Date birthDate, double appAccount) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.appAccount = appAccount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public double getAppAccount() {
		return appAccount;
	}

	public void setAppAccount(double appAccount) {
		this.appAccount = appAccount;
	}

	public List<MoneyTransfer> getMoneyTransfers() {
		return moneyTransfers;
	}

	public void setMoneyTransfers(List<MoneyTransfer> moneyTransfers) {
		this.moneyTransfers = moneyTransfers;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public List<User> getMoneyFriends() {
		return moneyFriends;
	}

	public void setMoneyFriends(List<User> moneyFriends) {
		this.moneyFriends = moneyFriends;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", birthDate=" + birthDate + ", appAccount=" + appAccount + "]";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}	
	
	
}
