package com.paymybudy.service;

import com.paymybudy.model.Client;
import com.paymybudy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ClientIdentificationService {
    @Autowired
    private ClientRepository clientRepository;



    /*
     * getNameById(int, int id)
     *  @param: int 1-2 to define type to search
     * @returns: 1: client Name,2: List of beneficiary names corresponding to the client's username currently logged
     */
    public List<String> getNameById(int clientId) {
        return List.of(clientRepository.findById(clientId).get().getFirstName());  // singleton : https://www.geeksforgeeks.org/singleton-class-java/
    }

    public int duplicateClientCheck(String email) {
        return clientRepository.duplicateClientCheck(email);
    }

    public void saveClient(Client client) {
        clientRepository.saveClient(client);
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email);
    }

    public Optional<Client> findById(int clientId) {
        return clientRepository.findById(clientId);
    }
}
