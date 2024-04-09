package com.paymybudy.service;

import com.paymybudy.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private ClientRepository clientRepository;
    public void login(String email, String password) {
    }
    public int emailToIdCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        return clientRepository.findByEmailIn(loggedUser);
    }
    public void logout() {
    }
    public void validateAccess(String email) {
    }
}
