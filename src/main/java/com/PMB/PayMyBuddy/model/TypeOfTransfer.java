package com.PMB.PayMyBuddy.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "type_of_transfer")
public class TypeOfTransfer {

	@Id
	@Column(name ="type_of_transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private double percentToCollect;
	
	@OneToMany(mappedBy="typeOfTransfer")
	@JsonIgnore
	private List<MoneyTransfer> moneyTransfers;
	
	public TypeOfTransfer() {		
	}
	
	
		
	public TypeOfTransfer(String name, double percentToCollect) {
		super();
		this.name = name;
		this.percentToCollect = percentToCollect;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPercentToCollect() {
		return percentToCollect;
	}
	public void setPercentToCollect(double percentToCollect) {
		this.percentToCollect = percentToCollect;
	}

	public List<MoneyTransfer> getMoneyTransfers() {
		return moneyTransfers;
	}

	public void setMoneyTransfers(List<MoneyTransfer> moneyTransfers) {
		this.moneyTransfers = moneyTransfers;
	}



	@Override
	public String toString() {
		return "TypeOfTransfer [name=" + name + ", percentToCollect=" + percentToCollect + "]";
	}
	
	
}
