package com.paymybudy.model;

import jakarta.persistence.*;

@Entity
@Table(name= "accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ACCOUNT_ID")
    private int account_ID;
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

    public Accounts(int account_ID, int clientId, float balance, String iban, String swift) {
        this.account_ID = account_ID;
        this.clientId = clientId;
        this.balance = balance;
        this.iban = iban;
        this.swift = swift;
    }

    public int getAccount_ID() {
        return account_ID;
    }

    public void setAccount_ID(int account_ID) {
        this.account_ID = account_ID;
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
