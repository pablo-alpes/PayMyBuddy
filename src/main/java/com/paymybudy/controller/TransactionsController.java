package com.paymybudy.controller;

import com.paymybudy.model.Beneficiaries;
import com.paymybudy.model.Transactions;
import com.paymybudy.repository.BeneficiariesRepository;
import com.paymybudy.repository.BeneficiaryAddFormDTO;
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
    private BeneficiariesRepository beneficiariesRepository;

    @Autowired
    private BeneficiaryService beneficiaryService;
    private BeneficiaryAddFormDTO beneficiaryAddForm;

    @GetMapping(value = "/transactions")
    public ModelAndView getTransactions(Model model) {
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

        //TODO -- Check if this object is retrieving the right information
        //ensure submission of transaction beneficiary key meets constraint
        //do search beneficiaryId by beneficiary name selected
        mv.getModel().put("payment", new Transactions()); //sends submit info to the PostMapping
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
        //add list of contacts the current user does not have
        int clientID = loginService.emailToIdCurrentUser();
        List<String> beneficiaryEmail = beneficiaryService.getBeneficiaryEmailByClientId(clientID);

        model.addAttribute("beneficiaryEmail", beneficiaryEmail);
        model.addAttribute("beneficiaryAddForm", new BeneficiaryAddFormDTO());
        return "newconnection";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @PostMapping("/newconnection")
    public void newBeneficiaryInput(@ModelAttribute BeneficiaryAddFormDTO beneficiaryAddForm, Model model) throws NullPointerException {
        //storages the information obtained in the Form to the DB
        //if user input
        // TODO
        //  -To distinguish between input in the dropbox versus manual input : "email" is collecting the both values
        int clientID = loginService.emailToIdCurrentUser();
        model.addAttribute("beneficiaryAddForm", beneficiaryAddForm);

        //if email is selected :
        try {
            if (beneficiaryAddForm.getSelectedEmail() != null) {
                String beneficiaryEmail = beneficiaryAddForm.getSelectedEmail();
                Beneficiaries beneficiaryFromEmail = beneficiaryService.getBeneficiaryFromEmailAndClientId(beneficiaryEmail, clientID);
                beneficiaryService.addExistingBeneficiaryToClientId(beneficiaryFromEmail, clientID);
            }
        }
        catch(NullPointerException e) {
            //if and user enters an input --
            // TODO--block the template to avoid blank page
                if (beneficiaryAddForm.getBeneficiaries().getEmail().length() > 1) { //checks if email is valid
                    beneficiaryService.addBeneficiary(
                            clientID,
                            beneficiaryAddForm.getBeneficiaries().getBeneficiaryFirstName(),
                            beneficiaryAddForm.getBeneficiaries().getBeneficiaryLastName(),
                            beneficiaryAddForm.getBeneficiaries().getIban(),
                            beneficiaryAddForm.getBeneficiaries().getSwift(),
                            beneficiaryAddForm.getBeneficiaries().getEmail());
            }
            model.addAttribute("message", "Registration of new connection has been successful.");
        }
   //     return "transaction";
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
        return getTransactions(model);
    }

}

