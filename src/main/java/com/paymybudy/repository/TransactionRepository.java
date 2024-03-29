package com.paymybudy.repository;

import java.util.Date;

public class TransactionRepository {
    public void executeTransaction(int client_ID, int beneficiary_ID, int amount, String description) {
    }
    public void getTransactionHistory(int client_ID, Date dateFrom, Date dateTo) {
    }
    public void rollbackTransaction(int transaction_ID) {
    }
}
