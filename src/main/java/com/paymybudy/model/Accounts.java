package com.paymybudy.model;

import jakarta.persistence.*;

@Entity
@Table(name= "accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ACCOUNT_ID")
    private int accountId;
    @Column(name="CLIENT_ID")
    private int clientId;
    @Column(name="BALANCE")
    float balance;
    @Column(name="IBAN")
    String iban;
    @Column(name="SWIFT")
    String swift;

    public Accounts() {
    }

    public Accounts(int accountId, int clientId, float balance, String iban, String swift) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.balance = balance;
        this.iban = iban;
        this.swift = swift;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
