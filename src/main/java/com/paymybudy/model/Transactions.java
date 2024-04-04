package com.paymybudy.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TRANSACTION_ID")
    private int transaction_id;
    @Column(name="CLIENT_ID")
    private int client_id;
    @Column(name="BENEFICIARY_ID")
    private int beneficiary_id;
    @Column(name="AMOUNT")
    private double amount;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="DATE")
    private Date date;
    @Column(name="STATUS")
    private int status;

    public Transactions() {
    }

    public Transactions(int transaction_id, int client_id, double amount, String description) {
        this.transaction_id = transaction_id;
        this.client_id = client_id;
        this.amount = amount;
        this.description = description;
    }

    public int getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(int beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
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
}
