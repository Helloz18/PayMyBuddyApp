package com.PMB.PayMyBuddy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name ="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String email;
	private String password;
	private String firstname;
	private String lastname;	
	private Date birthdate;
	private double appAccount;
	
	//for Spring security
	private String role;
	private boolean enabled;
	
	@OneToMany(mappedBy="user")
	private List<Address> addresses;
	
	@OneToMany(mappedBy="user")
	private List<PhoneNumber> phoneNumbers;
		
	
	@OneToMany(mappedBy="user")
	private List<BankAccount> bankAccounts;
	
	
	
	 @ManyToMany
     @JoinTable(name="user_money_friends",
         joinColumns={@JoinColumn(name="user_id", referencedColumnName="user_id")},
         inverseJoinColumns={@JoinColumn(name="money_friend_id", referencedColumnName="user_id")}
     )
	 private List<User> moneyFriends;
	
	
	public User() {
	}

	public User(String email, String password, String firstname, String lastname, Date birthdate, double appAccount) {
		super();
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public double getAppAccount() {
		return appAccount;
	}

	public void setAppAccount(double appAccount) {
		this.appAccount = appAccount;
	}


	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}


	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", birthdate=" + birthdate + ", appAccount=" + appAccount + "]";
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
