package com.paymybudy.service;

import com.paymybudy.model.Accounts;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    public float getBalance(Accounts account) {
        //returns actual balance for the account
        return 0;
    }

    public float operationBalance(
            Accounts account,
            float amount,
            String operation) {
        //updates the balance for the account and returns the new balance
        return amount;
    }

}
