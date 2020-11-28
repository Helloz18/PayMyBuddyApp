package com.PMB.PayMyBuddy.model;

import java.time.OffsetDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "money_transfer")
public class MoneyTransfer {

	@Id
	@Column(name = "money_transfer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private long id;

	private double amount;
	private String description;
	private OffsetDateTime date;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "type_of_transfer_id", nullable = false)
	@JsonIgnore
	private TypeOfTransfer typeOfTransfer;

	@ManyToOne
	@JoinColumn(name = "money_sender_id", nullable = false)
	@JsonIgnoreProperties({ "id", "password", "appAccount", "moneyFriends", "role", "enabled", "addresses",
			"phoneNumbers", "bankAccount", "moneyTransfers", "birthdate" })
	private User moneySender;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "money_friend_id")
	@JsonIgnoreProperties({ "id", "password", "appAccount", "moneyFriends", "role", "enabled", "addresses",
			"phoneNumbers", "bankAccount", "moneyTransfers", "birthdate" })
	private User moneyFriend;

	public MoneyTransfer() {
	}

	public MoneyTransfer(double amount, String description, OffsetDateTime date, TypeOfTransfer typeOfTransfer,
			User moneySender, User moneyFriend) {
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.typeOfTransfer = typeOfTransfer;
		this.moneySender = moneySender;
		this.moneyFriend = moneyFriend;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OffsetDateTime getDate() {
		return date;
	}

	public void setDate(OffsetDateTime date) {
		this.date = date;
	}

	public TypeOfTransfer getTypeOfTransfer() {
		return typeOfTransfer;
	}

	public void setTypeOfTransfer(TypeOfTransfer typeOfTransfer) {
		this.typeOfTransfer = typeOfTransfer;
	}

	public User getMoneySender() {
		return moneySender;
	}

	public void setMoneySender(User moneySender) {
		this.moneySender = moneySender;
	}

	public User getMoneyFriend() {
		return moneyFriend;
	}

	public void setMoneyFriend(User moneyFriend) {
		this.moneyFriend = moneyFriend;
	}

	@Override
	public String toString() {
		return "MoneyTransfer [id=" + id + ", amount=" + amount + ", description=" + description + ", date=" + date
				+ ", typeOfTransfer=" + typeOfTransfer.getId() + ", moneySender=" + moneySender.getEmail()
				+ ", moneyFriend=" + moneyFriend.getEmail() + "]";
	}

}
