package com.paymybudy.service;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Client;
import com.paymybudy.repository.AccountsRepository;
import com.paymybudy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;

@Service
public class AccountCreationService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public void createClient(Client client) {
        //Rules : An unique mail for client and account. Only a single account can be held.
        //checks if no duplication of client record
        client.setRole("USER"); // by default all registered persons get USER access level
        client.setPassword(encoder().encode(client.getPassword()));//password is storaged encrypted

        if (clientRepository.duplicateClientCheck(client.getEmail()) == 0) {
            clientRepository.saveClient(client); //records the new client
        }
    }

    public void createAccount(String username, String iban, String swift)  {
        Accounts account = new Accounts();
        int clientId = (clientRepository.findClientByEmail(username)).getClientId(); //the person needs to have previously created a client account

        account.setClientId(clientId); //username = email and it's an unique value
        account.setBalance(0); //by default clients do not have money in the account
        account.setIban(iban);
        account.setSwift(swift);

        if (accountsRepository.duplicateAccountCheck(clientId) == 0){ //the person needs to have previously created a client account
            accountsRepository.saveAccount(account, clientId);
        }

    }

}
