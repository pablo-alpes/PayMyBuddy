package com.paymybudy.service;

import com.paymybudy.model.Transactions;
import com.paymybudy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    public void doTransfer(int beneficiaryId, double amount) {
        //check balance
    }
    public void receiveDeposit(double amount, int accountId, int beneficiaryId) {
    }

    public Iterable<Transactions> getTransactionHistoryByClientId(int clientId) {
        return transactionRepository.findByClient_idInOrderByDateDesc(clientId);
    }

    public void rollbackTransaction(int transactionId) {
    }

}
