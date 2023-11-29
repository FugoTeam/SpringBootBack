package com.cashmanager.back.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "user")
public class User {
	@Id
    @GeneratedValue
	private long id;
	
	@Column(name = "USERNAME", nullable = false, unique=true)
	private String username;
	
	@Column(name = "EMAIL", nullable = false, unique=true)
	private String email;
	
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@ManyToMany
	List<CheckOutType> checkOutTypes= new ArrayList<CheckOutType>();
	
	public User() {
		super();
	}
	
	public User(long id) {
		this.id=id;
	}

	public User(String name, String email, String password) {
		super();
		this.username = name;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	@JsonProperty(value = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	@JsonProperty(value = "checkOutTypes")
	public List<CheckOutType> getCheckOutTypes() {
		return checkOutTypes;
	}

	public void setCheckOutTypes(List<CheckOutType> checkOutTypes) {
		this.checkOutTypes = checkOutTypes;
	}

	public void addCheckOutType(CheckOutType checkOutType) {
		this.checkOutTypes.add(checkOutType);
	}
	
	public void removeCheckOutType(CheckOutType checkOutType) {
		this.checkOutTypes.remove(checkOutType);
	}
}
