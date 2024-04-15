package com.paymybudy.service;

import com.paymybudy.model.Transactions;
import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.TransactionRepository;
import com.paymybudy.view.Page;
import com.paymybudy.view.Paged;
import com.paymybudy.view.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Paged<Transactions> getTransactionsList(int pageNumber, int size, int clientId) {
        //This function builds the page lists based on the requested parameters
        //Code snippet adapted to the objects used in the project from: https://github.com/martinwojtus/tutorials/blob/master/thymeleaf/thymeleaf-bootstrap-table/src/main/java/com/frontbackend/thymeleaf/
        Iterable<Transactions> transactions = transactionRepository.findByClient_idInOrderByDateDesc(clientId); //gets all elements

        List<Transactions> paged = StreamSupport.stream(transactions.spliterator(), false) // iterable to stream: https://www.baeldung.com/java-iterable-to-stream
                .skip(pageNumber)
                .limit(size)
                .collect(Collectors.toList());

        int totalPages = (int) (StreamSupport.stream(transactions.spliterator(), false).count()) / size; //counts number of pages https://stackoverflow.com/questions/11598977/get-size-of-an-iterable-in-java

        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }

    public void receiveDeposit(double amount, int accountId, int beneficiaryId) {
    }

    public void rollbackTransaction(int transactionId) {
    }

}
