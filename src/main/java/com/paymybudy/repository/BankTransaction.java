package com.paymybudy.repository;

import org.springframework.stereotype.Component;

@Component
public class BankTransaction {
    float withdraw;
    float deposit;

    public BankTransaction() {
    }

    public BankTransaction(float withdraw, float deposit) {
        this.withdraw = withdraw;
        this.deposit = deposit;
    }

    public float getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(float withdraw) {
        this.withdraw = withdraw;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }
}
