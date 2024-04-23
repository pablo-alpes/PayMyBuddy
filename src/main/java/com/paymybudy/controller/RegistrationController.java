package com.paymybudy.controller;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Client;
import com.paymybudy.repository.BeneficiaryAddFormDTO;
import com.paymybudy.repository.RegistrationAddFormDTO;
import com.paymybudy.service.AccountCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private AccountCreationService accountCreationService;

    @GetMapping("/registration")
    public String clientRegistration(@ModelAttribute RegistrationAddFormDTO client, Model model) {
        model.addAttribute("client", client);
        return "registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute RegistrationAddFormDTO clientDTO, Model model) {
        model.addAttribute("client", clientDTO);
        accountCreationService.doUserRegistration(clientDTO);

        return "login";
    }


}
