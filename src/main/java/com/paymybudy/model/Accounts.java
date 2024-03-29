package com.paymybudy.model;

public class Accounts {
    private int account_ID;
    private int client_ID;
    float balance;

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
}
