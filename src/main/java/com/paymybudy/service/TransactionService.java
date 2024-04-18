package com.paymybudy.service;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Client;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.BankTransaction;
import com.paymybudy.repository.ClientRepository;
import com.paymybudy.repository.TransactionRepository;
import com.paymybudy.view.Page;
import com.paymybudy.view.Paged;
import com.paymybudy.view.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountsRepository accountsRepository;

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

    public Paged<Transactions> getTransactionsList(int pageNumber, int size, int clientId) {
        //This function builds the page lists based on the requested parameters
        //Code snippet adapted to the objects used in the project from: https://github.com/martinwojtus/tutorials/blob/master/thymeleaf/thymeleaf-bootstrap-table/src/main/java/com/frontbackend/thymeleaf/
        Iterable<Transactions> transactions = transactionRepository.findByClient_idInOrderByDateDesc(clientId); //gets all elements

        int totalPages = (int) ((StreamSupport.stream(transactions.spliterator(), false).count() - 1) / size) + 1; //counts number of pages https://stackoverflow.com/questions/11598977/get-size-of-an-iterable-in-java
        int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;

        List<Transactions> paged = StreamSupport.stream(transactions.spliterator(), false) // iterable to stream: https://www.baeldung.com/java-iterable-to-stream
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());

        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }

    public void receiveDeposit(BankTransaction bank, int clientId) { // TODO-- add custom Exception
        //We build the transaction requested by the client
        Transactions transaction = new Transactions();

        //Client request and balance check
        float balanceToDeposit = bank.getDeposit();
        float balance = balanceService.getBalance(clientId);
        float totalOperation = balance + balanceToDeposit;

            //creates beneficiary if it doesn't exist, otherwise is updated
            beneficiaryService.addMirrorBeneficiary(clientId);
            String firstName = clientRepository.findById(clientId).get().getFirstName();
            int beneficiaryId = beneficiaryService.getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(clientId, firstName);

            //Destination Beneficiary - We proceed then to complete the transaction with destination beneficiary
            transaction.setClientId(clientId);
            transaction.setBeneficiaryId(beneficiaryId);
            transaction.setDate(new Date()); // date now
            transaction.setAmount(balanceToDeposit);
            transaction.setDescription("Credit deposit from User");
            transaction.setStatus(1);
            transaction.setBeneficiaryName(firstName);

            //transaction is recorded
            transactionRepository.save(transaction);
            //balance is updated
            balanceService.updateBalance(clientId, totalOperation);

    }

    public void withdraw(BankTransaction bank, int clientId) {
        //creates beneficiary if it doesn't exist, otherwise is updated
        beneficiaryService.addMirrorBeneficiary(clientId);
        String firstName = clientRepository.findById(clientId).get().getFirstName();

        int beneficiaryId = beneficiaryService.getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(clientId, firstName);

        //Initializes transaction - Withdraw details
        float withdraw = -1 * bank.getWithdraw(); //it's a credit operation on the account, so goes negative
        Transactions withdrawTransaction = new Transactions();
        withdrawTransaction.setClientId(clientId);
        withdrawTransaction.setBeneficiaryId(beneficiaryId);
        withdrawTransaction.setDate(new Date());
        withdrawTransaction.setAmount(-withdraw);
        withdrawTransaction.setDescription("Withdraw from User");
        withdrawTransaction.setStatus(1);
        withdrawTransaction.setBeneficiaryName(firstName);

        // Records transaction
        doTransfer(withdrawTransaction); //includes check for balance: does both update balance and save transaction
    }

}
