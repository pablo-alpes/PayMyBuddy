package com.paymybudy.service;

import java.util.List;

public interface BillingService {
    //Service assumes the billing is done on a monthly basis
    List<Integer[]> calculateMonthlyCummulatedFees();
    float calculateTransactionFee(float amount);
    float calculateMonthlyFeesPerTransaction(float amount);


}
