package com.paymybudy.controller;


import com.paymybudy.constants.Constants;
import com.paymybudy.model.Transactions;
import com.paymybudy.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


@Controller
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ClientIdentificationService clientIdentificationService;

    @Autowired
    private BeneficiaryService beneficiaryService;


    @Autowired
    private AccountCreationService accountCreationService;

    @GetMapping(value = "/transactions")
    @ResponseBody //response body is needed to get the parameters
    public ModelAndView getTransactions(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = Constants.ITEMS_PER_PAGE) int size,
            Model model) {
        //Doc: https://www.baeldung.com/get-user-in-spring-securit
        //Client login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getPrincipal());

        int clientID = loginService.emailToIdCurrentUser();
        Iterable<Transactions> transactions = transactionService.getTransactionHistoryByClientId(clientID);

        //Identities & Balance Inputs for the Client
        List<String> clientFirstName = clientIdentificationService.getNameById(clientID);
        List<String> beneficiaryFirstName = beneficiaryService.getNameById(clientID);

        float balance = accountCreationService.getBalanceById(clientID);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("transaction");
        mv.getModel().put("transactions", transactionService.getTransactionsList(pageNumber, size, clientID)); ///mv.addAttribute("transactions", transactions), same but checks for null
        mv.getModel().put("clientFirstName", clientFirstName);
        mv.getModel().put("beneficiaries", beneficiaryFirstName);
        mv.getModel().put("balance", balance);

        //ensure submission of transaction beneficiary key meets constraint
        //do search beneficiaryId by beneficiary name selected
        mv.getModel().put("payment", new Transactions()); //sends submit info to the PostMapping
        return mv;
    }

    @PostMapping("/transactions")
    public ModelAndView doTransfer(@ModelAttribute Transactions payment, Model model) {
        //Get values and doTransfer
        model.addAttribute("payment", payment);

        int clientID = loginService.emailToIdCurrentUser();

        payment.setBeneficiaryId(beneficiaryService.getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(clientID, payment.getBeneficiaryName()));
        payment.setDate(new Date());
        payment.setClientId(clientID);

        if (transactionService.doTransfer(payment) == 0) model.addAttribute("message","Not enough funds to perform the transaction.");
        return getTransactions(1, Integer.parseInt(Constants.ITEMS_PER_PAGE), model);
    }


}

