package com.paymybudy.service;

import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final AccountsRepository accountsRepository;

    @Autowired
    public BalanceService(final AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public float getBalance(int clientId) {
        //returns actual balance for the account given the clientId
        return accountsRepository.findById(getAccountIdFromClientID(clientId)).get().getBalance();
    }

    public boolean validOperation(float balance, float amount) {
        //returns 0 if not valid because not enough funds, 1 if OK
        float diff = balance - amount;
        return (diff > 0);
    }

    public void updateBalance(
            int clientId,
            float balance) {
        //updates the balance for the account and returns the new balance
        //it's done after a valid operation check
        accountsRepository.updateBalance(balance, getAccountIdFromClientID(clientId));
    }

    public int getAccountIdFromClientID(int clientId) {
        //search into DB given the ClientId from the Client returning a number > 0 if found
        return accountsRepository.findAccountsByClientID(clientId);
    }
}
