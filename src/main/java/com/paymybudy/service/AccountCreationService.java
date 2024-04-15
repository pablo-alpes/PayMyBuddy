package com.paymybudy.service;

import org.springframework.stereotype.Service;

@Service
public class AccountCreationService {

    public void createAccount(String Email, String firstName, String LastName, String password)  {
    }

    public void addBeneficiary(int clientID, String firstName, String lastName, String iban, String swift, String email) {
        //Rules : An unique mail for client and account. Only a single account can be held.

        //checks if no duplication of client record
        //prepares POJO
        //records the new client
        //creates the new account to the client based on the provided bank details

        }
}
