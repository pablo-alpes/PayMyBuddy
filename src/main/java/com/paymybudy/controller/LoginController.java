package com.paymybudy.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getPrincipal());

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //the user is already logged https://stackoverflow.com/questions/13131122/spring-security-redirect-if-already-logged-in
            return "forward:/transactions";
        }
        return "login";
    }
}
