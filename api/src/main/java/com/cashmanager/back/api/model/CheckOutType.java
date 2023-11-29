package com.cashmanager.back.api.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "checkOutType")
public class CheckOutType {
	@Id
	private String id;
	
	@Column(name = "BALANCE", nullable = false)
	private double balance;
	
	public CheckOutType(String id, double balance) {
		super();
		this.id = id;
		this.balance = balance;
	}
	
	public CheckOutType(String id) {
		super();
		this.id = id;
	}
	
	public CheckOutType() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public boolean equals(Object a) {
		return this.id.equals(((CheckOutType) a).getId());
	}
	
	
}
