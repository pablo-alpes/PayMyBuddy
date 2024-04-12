package com.paymybudy.service;

import com.paymybudy.model.Transactions;
import com.paymybudy.repository.AccountsRepository;
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

    @Autowired
    private BalanceService balanceService;

    public int doTransfer(Transactions transaction) {
        //Check balance
        float balance = balanceService.getBalance(transaction.getClientId());
        if (balanceService.validOperation(balance, transaction.getAmount()) == true) {
            //Update Balance
            balanceService.updateBalance(transaction.getClientId(), balance - transaction.getAmount());
            transactionRepository.save(transaction);
            return 1;
        }
        else {
            return 0;
        }
    }

    public Iterable<Transactions> getTransactionHistoryByClientId(int clientId) {
        return transactionRepository.findByClient_idInOrderByDateDesc(clientId);
    }

    public void receiveDeposit(double amount, int accountId, int beneficiaryId) {
    }

    public void rollbackTransaction(int transactionId) {
    }

}
