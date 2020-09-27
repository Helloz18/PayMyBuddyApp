package com.PMB.PayMyBuddy.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "money_transfer")
public class MoneyTransfer {

	@Id
	@Column(name ="money_transfer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private MoneyFriends moneyFriends;
	private double amount;
	private String description;
	private OffsetDateTime date;
	
	@ManyToOne
	@JoinColumn(name="type_of_transfer_id", nullable=false)
	private TypeOfTransfer type;
	
	public MoneyTransfer() {
	}
	
	
	
	public MoneyTransfer(MoneyFriends moneyFriends, double amount, String description, OffsetDateTime date,
			TypeOfTransfer type) {
		this.moneyFriends = moneyFriends;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = type;
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public MoneyFriends getMoneyFriends() {
		return moneyFriends;
	}
	public void setMoneySender(MoneyFriends moneyFriends) {
		this.moneyFriends = moneyFriends;
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
	public TypeOfTransfer getType() {
		return type;
	}
	public void setType(TypeOfTransfer type) {
		this.type = type;
	}



	@Override
	public String toString() {
		return "MoneyTransfer [moneyFriends=" + moneyFriends + ", amount=" + amount
				+ ", description=" + description + ", date=" + date + ", type=" + type
				+ "]";
	}
	
	
}
