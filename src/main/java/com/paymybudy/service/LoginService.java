package com.paymybudy.service;

import com.paymybudy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private ClientRepository clientRepository;
    public void login(String email, String password) {
    }
    public int emailToId(String email) {
        return clientRepository.findByEmailIn(email);
    }
    public void logout() {
    }
    public void validateAccess(String email) {
    }
}
