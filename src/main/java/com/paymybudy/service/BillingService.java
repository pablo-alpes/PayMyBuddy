package com.paymybudy.service;

public interface BillingService {
    //Service assumes the billing is done on a monthly basis
    float calculateTransactionFee(float amount);
    float calculateMonthlyFeesPerAccount(float amount);

}
