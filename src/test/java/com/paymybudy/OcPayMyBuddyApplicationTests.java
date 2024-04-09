package com.paymybudy;

import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.BeneficiariesRepository;
import com.paymybudy.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OcPayMyBuddyApplicationTests {

    @Autowired
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Test
    @DisplayName("Beneficiary Record stored")
    void WhenBeneficiaryIsPassed_GivenToTheRepositoryThenRecordIsSaved_test() {
        //UnitTest
        //ARRANGE get post request
        Beneficiaries beneficiary = new Beneficiaries();
        beneficiary.setBeneficiaryId(1);
        beneficiary.setClientId(1);
        beneficiary.setBeneficiaryLastName("Test");
        beneficiary.setBeneficiaryFirstName("Test");
        beneficiary.setIban("FR76");
        beneficiary.setSwift("CCC");

        //ACTION - save request in the DB
        Beneficiaries savedBeneficiary = beneficiariesRepository.save(beneficiary);

        //ASSERT
        assertNotNull(savedBeneficiary.getBeneficiaryId());
    }

    @Test
    @DisplayName("Beneficiary payment")
    void WhenClientDoesTransfer_GivenACertainBeneficiary_ThenTransactionIsSaved_test() {
        //ARRANGE - do a transfer
        Transactions transaction = new Transactions();
        //Transfer details
        transaction.setClientId(3);
        transaction.setBeneficiaryId(5);
        transaction.setAmount(500);
        transaction.setDescription("Epic purchase");
        transaction.setDate(new Date(124, Calendar.MAY,8,14,10,20));
        transaction.setStatus(1);
        transaction.setBeneficiaryName("Wopsy");

        //ACTION
        Transactions savedTransaction = transactionRepository.save(transaction);

        //ASSERT
        assertNotNull(savedTransaction.getTransactionId());
    }

}
