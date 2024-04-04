package com.paymybudy.controller;

import com.paymybudy.model.Transactions;
import com.paymybudy.service.LoginService;
import com.paymybudy.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jsqlparser.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@Controller
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoginService loginService;
    String loggedUser;

    @RestController
    public class login {
        @GetMapping(value = "/username")
        public String currentUserNameSimple(HttpServletRequest request) {
            Principal principal = request.getUserPrincipal();
            loggedUser = principal.getName();
            return loggedUser;
        }
    }

    @GetMapping(value = "/transactions")
    public ModelAndView getTransactions() {
        //TODO -- SpringSecurity: Voir comment le parametre de loginid est passé et le traduire à accountID:
        //Doc: https://www.baeldung.com/get-user-in-spring-security
        //int clientID = loginService.emailToId(loggedUser);
        int clientID = 5;
        //TODO -- Checker la raison pour laquelle j'obtient un seul résultat (le problème commence dans la fonction .get)
        Iterable<Transactions> transactions = transactionService.getTransactionHistoryByClientId(clientID);
        //beneficiaries

        ModelAndView mv = new ModelAndView();
        mv.setViewName("transaction");
        System.out.println(transactions.spliterator());
        mv.getModel().put("transactions", transactions); //  //mv.addAttribute("transactions", transactions), same but checks for null
        //TODO -- Determiner pourquoi on obtient un seul valeur
        // -> cela demande d'avoir au préalable la jointure correcte
        // et: la mise en place du Impl associé au méthode GET et la config many to many, comme fait pour transactions
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

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

}
