package com.PMB.PayMyBuddy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	@Column(unique = true)
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private Date birthdate;
	private double appAccount;

	// for Spring security
	private String role;
	private boolean enabled;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Address> addresses;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<PhoneNumber> phoneNumbers;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bankAccount_id")
	private BankAccount bankAccount;

	@ManyToMany
	@JoinTable(name = "user_money_friends", joinColumns = {
			@JoinColumn(name = "money_sender", referencedColumnName = "user_id") }, inverseJoinColumns = {
					@JoinColumn(name = "money_friend", referencedColumnName = "user_id") })
	@JsonIgnoreProperties({ "id", "password", "appAccount", "moneyFriends", "role", "enabled", "addresses",
			"phoneNumbers", "bankAccount", "moneyTransfers", "birthdate" })
	private List<User> moneyFriends;

	@OneToMany(mappedBy = "moneySender")
	private List<MoneyTransfer> moneyTransfers;

	public User() {
	}

	public User(String email, String password, String firstname, String lastname, Date birthdate, double appAccount,
			String role, boolean enabled, List<Address> addresses, List<PhoneNumber> phoneNumbers,
			BankAccount bankAccount, List<MoneyTransfer> moneyTransfers, List<User> moneyFriends) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.appAccount = appAccount;
		this.role = role;
		this.enabled = enabled;
		this.addresses = addresses;
		this.phoneNumbers = phoneNumbers;
		this.bankAccount = bankAccount;
		this.moneyTransfers = moneyTransfers;
		this.moneyFriends = moneyFriends;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccounts) {
		this.bankAccount = bankAccounts;
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

	public List<User> getMoneyFriends() {
		return moneyFriends;
	}

	public void setMoneyFriends(List<User> moneyFriends) {
		this.moneyFriends = moneyFriends;
	}

	public List<MoneyTransfer> getMoneyTransfers() {
		return moneyTransfers;
	}

	public void setMoneyTransfers(List<MoneyTransfer> moneyTransfers) {
		this.moneyTransfers = moneyTransfers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addresses == null) ? 0 : addresses.hashCode());
		long temp;
		temp = Double.doubleToLongBits(appAccount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((bankAccount == null) ? 0 : bankAccount.hashCode());
		result = prime * result + ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((moneyFriends == null) ? 0 : moneyFriends.hashCode());
		result = prime * result + ((moneyTransfers == null) ? 0 : moneyTransfers.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (Double.doubleToLongBits(appAccount) != Double.doubleToLongBits(other.appAccount))
			return false;
		if (bankAccount == null) {
			if (other.bankAccount != null)
				return false;
		} else if (!bankAccount.equals(other.bankAccount))
			return false;
		if (birthdate == null) {
			if (other.birthdate != null)
				return false;
		} else if (!birthdate.equals(other.birthdate))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (id != other.id)
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (moneyFriends == null) {
			if (other.moneyFriends != null)
				return false;
		} else if (!moneyFriends.equals(other.moneyFriends))
			return false;
		if (moneyTransfers == null) {
			if (other.moneyTransfers != null)
				return false;
		} else if (!moneyTransfers.equals(other.moneyTransfers))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumbers == null) {
			if (other.phoneNumbers != null)
				return false;
		} else if (!phoneNumbers.equals(other.phoneNumbers))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", birthdate=" + birthdate + ", appAccount=" + appAccount + ", role="
				+ role + ", enabled=" + enabled + ", addresses=" + addresses + ", phoneNumbers=" + phoneNumbers
				+ ", bankAccount=" + bankAccount + ", moneyFriends=" + moneyFriends.size() + ", moneyTransfers="
				+ moneyTransfers + "]";
	}

}
