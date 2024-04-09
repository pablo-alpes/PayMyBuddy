package com.paymybudy.controller;

import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.BeneficiariesRepository;
import com.paymybudy.service.BeneficiaryService;
import com.paymybudy.service.ClientIdentificationService;
import com.paymybudy.service.LoginService;
import com.paymybudy.service.TransactionService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
//import net.sf.jsqlparser.Model;

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
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping(value = "/transactions")
    public ModelAndView getTransactions() {
        //Doc: https://www.baeldung.com/get-user-in-spring-securit
        //Client login
        int clientID = loginService.emailToIdCurrentUser();
        Iterable<Transactions> transactions = transactionService.getTransactionHistoryByClientId(clientID);

        //Identities & Balance Inputs for the Client
        List<String> clientFirstName = clientIdentificationService.getNameById(1, clientID);
        List<String> beneficiaryFirstName = clientIdentificationService.getNameById(2, clientID);
        float balance = clientIdentificationService.getBalanceById(clientID);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("transaction");
        mv.getModel().put("transactions", transactions); ///mv.addAttribute("transactions", transactions), same but checks for null
        mv.getModel().put("clientFirstName", clientFirstName);
        mv.getModel().put("beneficiaries", beneficiaryFirstName);
        mv.getModel().put("balance", balance);

        return mv;
    }
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/newconnection")
    public String newBeneficiary(Model model) {
        model.addAttribute("beneficiary", new Beneficiaries());
        return "newconnection";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @PostMapping("/newconnection")
    //TODO -- Error 403, check why is not accessible
    public void newBeneficiaryInput(@ModelAttribute Beneficiaries beneficiary, Model model) {
        //storages the information obtained in the Form to the DB
        //if user input
        int clientID = loginService.emailToIdCurrentUser();
        model.addAttribute("beneficiary", beneficiary);
        System.out.println(beneficiary.toString());
        beneficiary.setClientId(clientID);

        beneficiaryService.addBeneficiary(
                beneficiary.getClientId(),
                beneficiary.getBeneficiaryFirstName(),
                beneficiary.getBeneficiaryLastName(),
                beneficiary.getIban(),
                beneficiary.getSwift());
        model.addAttribute("message","Registration of new connection has been succesful.");
    }

    //@PostMapping("/transfer")
    //Get values and doTransfer

}

