package com.PMB.PayMyBuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "money_friends")
public class MoneyFriends {

	@Id
	@Column(name = "money_friends_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private User userSender;
	private User userReceiver;
	
		
	public MoneyFriends() {
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUserSender() {
		return userSender;
	}
	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}
	public User getUserReceiver() {
		return userReceiver;
	}
	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}
	
	
}
