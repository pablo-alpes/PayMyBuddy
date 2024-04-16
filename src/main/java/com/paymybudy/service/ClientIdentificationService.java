package com.paymybudy.service;

import com.paymybudy.model.Client;
import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.BeneficiariesRepository;
import com.paymybudy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Arrays.stream;

@Service
public class ClientIdentificationService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private ClientRepository clientRepository;

    /*
     * getNameById(int, int id)
     *  @param: int 1-2 to define type to search
     * @returns: 1: client Name,2: List of beneficiary names corresponding to the client's username currently logged
     */
    public List<String> getNameById(int param, int clientId) {
        return switch (param) {
            case 1 -> //from client id
                List.of(clientRepository.findById(clientId).get().getFirstName());  // singleton : https://www.geeksforgeeks.org/singleton-class-java/
            case 2 -> //from beneficiary id
                beneficiariesRepository.findByBeneficiary_idIn(clientId);
            default ->
                List.of();
        };
    }
    public float getBalanceById(int clientId) {
        return accountsRepository.findById(clientId).get().getBalance();
    }

    public String getIbanByClientId(int clientId) {
        return accountsRepository.findById(clientId).get().getIban();
    }

}
