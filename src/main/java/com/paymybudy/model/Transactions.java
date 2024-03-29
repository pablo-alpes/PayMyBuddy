package com.paymybudy.model;

import java.util.Date;

public class Transactions {

    private int transaction_id;
    private int client_id;
    private double amount;
    private String description;
    private Date date;
    private int status;

    public Transactions() {
    }

    public Transactions(int transaction_id, int client_id, double amount, String description) {
        this.transaction_id = transaction_id;
        this.client_id = client_id;
        this.amount = amount;
        this.description = description;
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