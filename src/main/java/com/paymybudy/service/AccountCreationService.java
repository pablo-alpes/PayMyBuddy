package com.paymybudy.service;

import com.paymybudy.configuration.SecurityConfiguration;
import com.paymybudy.model.Accounts;
import com.paymybudy.model.Client;
import com.paymybudy.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountCreationService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private ClientIdentificationService clientIdentificationService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public void createClient(Client client) {
        //Rules : An unique mail for client and account. Only a single account can be held.
        //checks if no duplication of client record
        client.setRole("USER"); // by default all registered persons get USER access level
        client.setPassword(securityConfiguration.passwordEncoder().encode(client.getPassword()));//password is storaged encrypted and WITH THE SALT

        if (clientIdentificationService.duplicateClientCheck(client.getEmail()) == 0) {
            clientIdentificationService.saveClient(client); //records the new client
        }
    }

    public void createAccount(String email, String iban, String swift)  {
        Accounts account = new Accounts();
        int clientId = (clientIdentificationService.findClientByEmail(email)).getClientId(); //the person needs to have previously created a client account

        account.setClientId(clientId); //username = email and it's an unique value
        account.setBalance(0); //by default clients do not have money in the account
        account.setIban(iban);
        account.setSwift(swift);

        if (accountsRepository.duplicateAccountCheck(clientId) == 0){ //the person needs to have previously created a client account
            accountsRepository.saveAccount(account, clientId);
        }

    }

    public Optional<Accounts> findById(int clientId) {
        return accountsRepository.findById(clientId);
    }

    public float getBalanceById(int clientId) {
        return findById(clientId).get().getBalance();
    }

    public String getIbanByClientId(int clientId) {
        return findById(clientId).get().getIban();
    }

}
