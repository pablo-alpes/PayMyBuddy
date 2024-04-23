package com.paymybudy.controller;

import com.paymybudy.repository.BankTransaction;
import com.paymybudy.service.AccountCreationService;
import com.paymybudy.service.LoginService;
import com.paymybudy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BankTransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private AccountCreationService accountCreationService;

    @GetMapping("/bank")
    public String bankOperations(Model model, @ModelAttribute BankTransaction bank) {
        int clientID = loginService.emailToIdCurrentUser();
        float balance = accountCreationService.getBalanceById(clientID);
        String iban = accountCreationService.getIbanByClientId(clientID);

        model.addAttribute("iban", iban);
        model.addAttribute("balance", balance);
        model.addAttribute("bank", bank);
        return "bank";
    }

    @PostMapping("/withdraw")
    public String doWithdraw(Model model, @ModelAttribute BankTransaction bank) {
        model.addAttribute("bank", bank);
        int clientID = loginService.emailToIdCurrentUser();
        float balance = accountCreationService.getBalanceById(clientID);
        //withdraw
        transactionService.withdraw(bank, clientID);
        model.addAttribute("balance", balance);
        return "forward:/bank";
    }

    @PostMapping("/balance")
    public String doBalanceUpdate(Model model, @ModelAttribute BankTransaction bank) {
        model.addAttribute("bank", bank);

        int clientId = loginService.emailToIdCurrentUser();
        float balance = accountCreationService.getBalanceById(clientId);
        //updates balance and runs all the internal operations required
        transactionService.receiveDeposit(bank, clientId);
        model.addAttribute("balance", balance);
        return "forward:/bank";
    }
}
