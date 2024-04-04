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
    private int client_ID;
    @Column(name="BALANCE")
    float balance;
    @Column(name="IBAN")
    String iban;
    @Column(name="SWIFT")
    String swift;

    public Accounts() {
    }
    public Accounts(int account_ID, int client_ID, float balance) {
        this.account_ID = account_ID;
        this.client_ID = client_ID;
        this.balance = balance;
    }

    public int getAccount_ID() {
        return account_ID;
    }

    public void setAccount_ID(int account_ID) {
        this.account_ID = account_ID;
    }

    public int getClient_ID() {
        return client_ID;
    }

    public void setClient_ID(int client_ID) {
        this.client_ID = client_ID;
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
