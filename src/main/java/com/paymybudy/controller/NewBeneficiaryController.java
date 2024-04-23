package com.paymybudy.controller;

import com.paymybudy.model.Beneficiaries;
import com.paymybudy.repository.BeneficiaryAddFormDTO;
import com.paymybudy.service.BeneficiaryService;
import com.paymybudy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NewBeneficiaryController {
    private BeneficiaryAddFormDTO beneficiaryAddForm;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BeneficiaryService beneficiaryService;


    @GetMapping("/newconnection")
    public String newBeneficiary(Model model) {
        //add list of contacts the current user does not have
        int clientID = loginService.emailToIdCurrentUser();
        List<String> beneficiaryEmail = beneficiaryService.getBeneficiaryEmailByClientId(clientID);

        model.addAttribute("beneficiaryEmail", beneficiaryEmail);
        model.addAttribute("beneficiaryAddForm", new BeneficiaryAddFormDTO());
        return "newconnection";
    }

    @PostMapping("/newconnection")
    public void newBeneficiaryInput(@ModelAttribute BeneficiaryAddFormDTO beneficiaryAddForm, Model model) throws Exception {
        //storages the information obtained in the Form to the DB

        int clientID = loginService.emailToIdCurrentUser(); //if user input
        model.addAttribute("beneficiaryAddForm", beneficiaryAddForm);
        beneficiaryService.newConnection(beneficiaryAddForm, clientID);
        model.addAttribute("message", "Registration of new connection has been successful.");
    }

}
