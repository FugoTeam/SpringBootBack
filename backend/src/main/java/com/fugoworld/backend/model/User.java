package com.fugoworld.backend.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(name="lastname", nullable=false)
    private String lastname;
    @Column(name="firstname", nullable=false)
    private String firstname;
    @Column(name="city", nullable=false)
    private String city;
    @Column(name="email", nullable=false)
    private String email;
    @Column(name="verified", nullable=false)
    private boolean verified;
    @Column(name="isAdmin", nullable=false)
    private boolean isAdmin;
    @Column(name="password", nullable=false)
    private String password;
    @Column(name="code")
    private String code;
    @Column(name="codeExpiration")
    private Date codeExpiration;
    @Column(name="lastConnexion")
    private Date lastConnexion;
    @Column(name="unsuccessfulAttempt")
    private int unsuccessfulAttempt;
    @Column(name="currentToken")
    private String currentToken;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String ville) {
        this.city = ville;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCodeExpiration() {
        return this.codeExpiration;
    }

    public void setCodeExpiration(Date codeExpiration) {
        this.codeExpiration = codeExpiration;
    }

    public Date getLastConnexion() {
        return this.lastConnexion;
    }

    public void setLastConnexion(Date lastConnexion) {
        this.lastConnexion = lastConnexion;
    }

    public int getUnsuccessfulAttempt() {
        return this.unsuccessfulAttempt;
    }

    public void setUnsuccessfulAttempt(int unsuccessfulAttempt) {
        this.unsuccessfulAttempt = unsuccessfulAttempt;
    }

    public String getCurrentToken() {
        return this.currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public UserPartial toUserPartial() {
        return new UserPartial(this.id, this.lastname, this.firstname);
    }
}

